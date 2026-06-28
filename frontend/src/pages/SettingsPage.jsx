import { useEffect, useState } from 'react';
import PageLayout from '../components/PageLayout';
import { useAuth } from '../hooks/useAuth';
import { useToast } from '../components/Toast';
import { api } from '../utilities/ApiUtils';
import '../styles/admin.css';

const generalDefaults = { companyName: 'InvenTrack Workspace', currency: 'INR', timezone: 'Asia/Kolkata', lowStockThreshold: 10, dateFormat: 'DD/MM/YYYY', language: 'English' };

export default function SettingsPage() {
  const { user } = useAuth();
  const isAdmin = user?.role === 'ADMIN';
  const tabs = isAdmin ? ['General', 'Users & Roles', 'Notifications'] : ['General', 'Notifications'];
  const [tab, setTab] = useState('General');
  return <PageLayout title="Settings"><div className="admin-heading"><div><h1>Settings</h1><p>Manage your company preferences and workspace access.</p></div></div><nav className="settings-tabs">{tabs.map(item => <button key={item} className={tab === item ? 'active' : ''} onClick={() => setTab(item)}>{item}</button>)}</nav>{tab === 'General' && <GeneralSettings />}{tab === 'Users & Roles' && isAdmin && <UsersSettings />}{tab === 'Notifications' && <NotificationSettings isAdmin={isAdmin} />}</PageLayout>;
}

function GeneralSettings() {
  const toast = useToast();
  const [form, setForm] = useState(() => JSON.parse(localStorage.getItem('inventtrack_settings') || 'null') || generalDefaults);
  const field = key => e => setForm({ ...form, [key]: e.target.value });
  const save = () => { localStorage.setItem('inventtrack_settings', JSON.stringify(form)); toast.success('Settings saved.'); };
  return <div className="settings-stack"><SettingsCard title="Company"><div className="admin-form-grid"><Field label="Company Name" value={form.companyName} onChange={field('companyName')} /><Select label="Default Currency" value={form.currency} onChange={field('currency')} options={[['INR','₹ INR'],['USD','$ USD'],['EUR','€ EUR']]} /><Select label="Timezone" value={form.timezone} onChange={field('timezone')} options={[['Asia/Kolkata','Asia/Kolkata'],['UTC','UTC'],['America/New_York','America/New York']]} /><Field label="Low Stock Threshold" type="number" value={form.lowStockThreshold} onChange={field('lowStockThreshold')} hint="Products with stock below this number will trigger alerts" /></div><button className="btn-primary" onClick={save}>Save Changes</button></SettingsCard><SettingsCard title="System"><div className="admin-form-grid"><Select label="Date Format" value={form.dateFormat} onChange={field('dateFormat')} options={['DD/MM/YYYY','MM/DD/YYYY','YYYY-MM-DD']} /><Select label="Language" value={form.language} onChange={field('language')} options={['English']} /></div><button className="btn-primary" onClick={save}>Save Changes</button></SettingsCard></div>;
}

function UsersSettings() {
  const toast = useToast();
  const [users, setUsers] = useState([]); const [loading, setLoading] = useState(true);
  const [form, setForm] = useState({ firstName:'', lastName:'', email:'', role:'MANAGER', password:'' });
  const load = async () => { try { setUsers(await api.get('/users')); } catch(e) { toast.error(e.message); } finally { setLoading(false); } };
  useEffect(() => { load(); }, []);
  const update = async (id, values) => { try { const next = await api.put(`/users/${id}`, values); setUsers(list => list.map(item => item.id === id ? next : item)); toast.success('User updated.'); } catch(e) { toast.error(e.message); } };
  const remove = async user => { if (!window.confirm(`Delete ${user.name}?`)) return; try { await api.delete(`/users/${user.id}`); setUsers(list => list.filter(item => item.id !== user.id)); toast.success('User deleted.'); } catch(e) { toast.error(e.message); } };
  const create = async e => { e.preventDefault(); try { const name = `${form.firstName} ${form.lastName}`.trim(); const next = await api.post('/users', { name, email: form.email, role: form.role, password: form.password }); setUsers(list => [...list, next]); setForm({ firstName:'',lastName:'',email:'',role:'MANAGER',password:'' }); toast.success(`Account created for ${name}. Share login credentials securely.`); } catch(e) { toast.error(e.message.toLowerCase().includes('exist') ? 'An account with this email already exists.' : e.message); } };
  return <div className="settings-stack"><SettingsCard title="All Users"><div className="table-scroll"><table className="admin-table"><thead><tr><th>Name</th><th>Email</th><th>Role</th><th>Status</th><th>Created</th><th>Actions</th></tr></thead><tbody>{loading ? <tr><td colSpan="6">Loading users…</td></tr> : users.length ? users.map(item => <tr key={item.id}><td>{item.name}</td><td>{item.email}</td><td><span className={`role-chip ${item.role.toLowerCase()}`}>{item.role}</span></td><td><span className={`status-chip ${item.active === false ? 'inactive' : ''}`}>{item.active === false ? 'Inactive' : 'Active'}</span></td><td>{item.createdAt ? new Date(item.createdAt).toLocaleDateString() : '—'}</td><td><div className="table-actions"><select aria-label={`Edit role for ${item.name}`} value={item.role} onChange={e => update(item.id,{ role:e.target.value })}><option>ADMIN</option><option>MANAGER</option><option>STAFF</option></select><button onClick={() => update(item.id,{ active:String(item.active === false) })}>{item.active === false ? 'Activate' : 'Deactivate'}</button><button className="danger-link" onClick={() => remove(item)}>Delete</button></div></td></tr>) : <tr><td colSpan="6"><EmptyRow text="No users found." /></td></tr>}</tbody></table></div></SettingsCard><SettingsCard title="Create Account" subtitle="Staff members self-register. Use this form to create Admin or Manager accounts."><form className="admin-form-grid" onSubmit={create}><Field label="First Name" required value={form.firstName} onChange={e => setForm({...form,firstName:e.target.value})}/><Field label="Last Name" required value={form.lastName} onChange={e => setForm({...form,lastName:e.target.value})}/><Field label="Email" type="email" required value={form.email} onChange={e => setForm({...form,email:e.target.value})}/><Select label="Role" value={form.role} onChange={e => setForm({...form,role:e.target.value})} options={[["MANAGER","Manager"],["ADMIN","Admin"]]}/><Field wide label="Temporary Password" type="password" required value={form.password} onChange={e => setForm({...form,password:e.target.value})} hint="The user will be asked to change this on first login."/><div className="field-wide"><button className="btn-primary">Create Account</button></div></form></SettingsCard></div>;
}

function NotificationSettings({isAdmin}){ const toast=useToast(); const [values,setValues]=useState({low:true,staff:true,supplier:false}); const rows=[['low','Low Stock Alerts','Get notified when a product falls below its reorder point'],...(isAdmin?[['staff','New Staff Registration','Get notified when a new staff member registers']]:[]),['supplier','Supplier Updates','Get notified on supplier changes']]; return <SettingsCard title="Notifications"><div className="toggle-list">{rows.map(([key,label,desc])=><div className="toggle-row" key={key}><div><strong>{label}</strong><p>{desc}</p></div><button className={`switch ${values[key]?'on':''}`} aria-pressed={values[key]} onClick={()=>setValues({...values,[key]:!values[key]})}><span/></button></div>)}</div><button className="btn-primary" onClick={()=>toast.success('Notification preferences saved.')}>Save Preferences</button></SettingsCard> }
function SettingsCard({title,subtitle,children}){return <section className="settings-card"><header><h2>{title}</h2>{subtitle&&<p>{subtitle}</p>}</header>{children}</section>}
function Field({label,hint,wide,...props}){return <label className={wide?'field-wide':''}><span>{label}</span><input {...props}/>{hint&&<small>{hint}</small>}</label>}
function Select({label,options,...props}){return <label><span>{label}</span><select {...props}>{options.map(item=>{const [value,text]=Array.isArray(item)?item:[item,item];return <option key={value} value={value}>{text}</option>})}</select></label>}
function EmptyRow({text}){return <div className="table-empty">{text}</div>}
