import { useEffect, useState } from 'react';
import PageLayout from '../components/PageLayout';
import Modal from '../components/Modal';
import ConfirmDialog from '../components/ConfirmDialog';
import { useAuth } from '../hooks/useAuth';
import { useToast } from '../components/Toast';
import { api } from '../utilities/ApiUtils';
import '../styles/admin.css';
import '../styles/v4.css';

const defaults = { companyName: 'InvenTrack Workspace', currency: 'INR', timezone: 'Asia/Kolkata', lowStockThreshold: 10, dateFormat: 'DD/MM/YYYY', language: 'English' };
const emptyAccount = { firstName: '', lastName: '', email: '', role: 'MANAGER', password: '' };

export default function SettingsPageV4() {
  const { user } = useAuth();
  const admin = user?.role === 'ADMIN';
  const tabs = admin ? ['General', 'Users & Roles', 'Appearance', 'Notifications'] : ['General', 'Appearance', 'Notifications'];
  const [tab, setTab] = useState('General');
  return (
    <PageLayout title="Settings">
      <div className="admin-heading"><div><h1>Settings</h1><p>Manage company preferences and workspace access.</p></div></div>
      <nav className="settings-tabs">{tabs.map(item => <button className={tab === item ? 'active' : ''} onClick={() => setTab(item)} key={item}>{item}</button>)}</nav>
      {tab === 'General' && <General />}
      {tab === 'Users & Roles' && admin && <Users />}
      {tab === 'Appearance' && <Appearance />}
      {tab === 'Notifications' && <Notifications admin={admin} />}
    </PageLayout>
  );
}

function General() {
  const toast = useToast();
  const [form, setForm] = useState(() => JSON.parse(localStorage.getItem('inventtrack_settings') || 'null') || defaults);
  const save = () => { localStorage.setItem('inventtrack_settings', JSON.stringify(form)); toast.success('Settings saved.'); };
  return <Card title="Company & System"><div className="admin-form-grid"><Field label="Company Name" value={form.companyName} onChange={change(form, setForm, 'companyName')} /><Select label="Default Currency" value={form.currency} onChange={change(form, setForm, 'currency')} options={['INR', 'USD', 'EUR']} /><Select label="Timezone" value={form.timezone} onChange={change(form, setForm, 'timezone')} options={['Asia/Kolkata', 'UTC', 'America/New_York']} /><Field label="Low Stock Threshold" type="number" value={form.lowStockThreshold} onChange={change(form, setForm, 'lowStockThreshold')} /><Select label="Date Format" value={form.dateFormat} onChange={change(form, setForm, 'dateFormat')} options={['DD/MM/YYYY', 'MM/DD/YYYY', 'YYYY-MM-DD']} /><Select label="Language" value={form.language} onChange={change(form, setForm, 'language')} options={['English']} /></div><button className="btn-primary" onClick={save}>Save Changes</button></Card>;
}

function Users() {
  const { user: current } = useAuth();
  const toast = useToast();
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [edit, setEdit] = useState(null);
  const [createOpen, setCreateOpen] = useState(false);
  const [create, setCreate] = useState(emptyAccount);
  const [deleteTarget, setDeleteTarget] = useState(null);
  const [deleting, setDeleting] = useState(false);

  useEffect(() => {
    api.get('/users').then(setUsers).catch(error => toast.error(error.message)).finally(() => setLoading(false));
  }, [toast]);

  const self = user => String(user.id) === String(current?.id) || user.email === current?.email || user.email === current?.username;
  const patch = async (user, body, message) => {
    try {
      const next = await api.patch(`/users/${user.id}`, body);
      setUsers(list => list.map(item => item.id === user.id ? next : item));
      toast.success(message);
    } catch (error) { toast.error(error.message); }
  };

  const remove = async () => {
    setDeleting(true);
    try {
      await api.delete(`/users/${deleteTarget.id}`);
      setUsers(list => list.filter(item => item.id !== deleteTarget.id));
      setDeleteTarget(null);
      toast.success('User deleted');
    } catch (error) { toast.error(error.message); }
    finally { setDeleting(false); }
  };

  const submitEdit = async event => {
    event.preventDefault();
    await patch(edit, { name: edit.name.trim(), role: edit.role, active: String(edit.active) }, 'User updated');
    setEdit(null);
  };

  const submitCreate = async event => {
    event.preventDefault();
    try {
      const next = await api.post('/auth/create-user', create);
      setUsers(list => [...list, next]);
      setCreate(emptyAccount);
      setCreateOpen(false);
      toast.success(`Account created for ${create.firstName} ${create.lastName}.`);
    } catch (error) { toast.error(error.message); }
  };

  return (
    <div className="settings-stack">
      <Card title="All Users" action={<button className="btn-primary" onClick={() => setCreateOpen(true)}>+ Create Account</button>}>
        <div className="table-scroll"><table className="admin-table users-table"><thead><tr><th>Name</th><th>Email</th><th>Role</th><th>Status</th><th>Created</th><th>Actions</th></tr></thead><tbody>
          {loading ? [1, 2, 3].map(item => <tr className="skeleton-row" key={item}><td colSpan="6"><span /></td></tr>) : users.map(user => <tr key={user.id}><td>{user.name}</td><td>{user.email}</td><td><span className={`role-chip ${user.role.toLowerCase()}`}>{user.role}</span></td><td><span className={`status-chip ${user.active === false ? 'inactive' : ''}`}>{user.active === false ? 'Inactive' : 'Active'}</span></td><td>{formatDate(user.createdAt)}</td><td><div className="user-actions"><button className="row-btn edit" onClick={() => setEdit({ ...user })}>✎ Edit</button><button className={`row-btn ${user.active === false ? 'activate' : 'deactivate'}`} onClick={() => patch(user, { active: String(user.active === false) }, user.active === false ? 'User activated' : 'User deactivated')}>{user.active === false ? '✓ Activate' : 'Deactivate'}</button><button className="row-btn delete" disabled={self(user)} title={self(user) ? 'You cannot delete your own account' : ''} onClick={() => setDeleteTarget(user)}>Delete</button></div></td></tr>)}
        </tbody></table></div>
      </Card>

      <Modal isOpen={Boolean(edit)} onClose={() => setEdit(null)} title="Edit User">
        <form className="modal-form" onSubmit={submitEdit}>{edit && <><Field label="Full Name" required value={edit.name} onChange={event => setEdit({ ...edit, name: event.target.value })} /><Field label="Email" readOnly value={edit.email} /><Select label="Role" disabled={self(edit)} value={edit.role} onChange={event => setEdit({ ...edit, role: event.target.value })} options={['ADMIN', 'MANAGER', 'STAFF']} /><label className="inline-toggle"><input type="checkbox" checked={edit.active !== false} onChange={event => setEdit({ ...edit, active: event.target.checked })} /> Active user</label></>}<div className="modal-actions"><button type="button" className="btn-secondary" onClick={() => setEdit(null)}>Cancel</button><button className="btn-primary">Save Changes</button></div></form>
      </Modal>

      <Modal isOpen={createOpen} onClose={() => setCreateOpen(false)} title="Create Account">
        <form className="modal-form" onSubmit={submitCreate}><div className="admin-form-grid"><Field label="First Name" required value={create.firstName} onChange={change(create, setCreate, 'firstName')} /><Field label="Last Name" required value={create.lastName} onChange={change(create, setCreate, 'lastName')} /></div><Field label="Email" type="email" required value={create.email} onChange={change(create, setCreate, 'email')} /><Select label="Role" value={create.role} onChange={change(create, setCreate, 'role')} options={['MANAGER', 'ADMIN']} /><Field label="Temporary Password" type="password" required minLength="8" value={create.password} onChange={change(create, setCreate, 'password')} /><div className="modal-actions"><button type="button" className="btn-secondary" onClick={() => setCreateOpen(false)}>Cancel</button><button className="btn-primary">Create Account</button></div></form>
      </Modal>

      <ConfirmDialog isOpen={Boolean(deleteTarget)} title="Delete user" message={deleteTarget ? `Delete ${deleteTarget.name}? Accounts with referenced inventory history are protected.` : ''} onClose={() => setDeleteTarget(null)} onConfirm={remove} busy={deleting} />
    </div>
  );
}

function Appearance() { return <Card title="Appearance"><div className="light-only"><span>☀</span><div><strong>Light Mode</strong><p>InvenTrack uses a clear, consistent light interface.</p></div></div></Card>; }
function Notifications({ admin }) { const toast = useToast(); return <Card title="Notifications"><div className="toggle-list"><Toggle title="Low Stock Alerts" text="Get notified when a product reaches its reorder point" />{admin && <Toggle title="New Staff Registration" text="Get notified when a staff member registers" />}<Toggle title="Supplier Updates" text="Get notified about supplier changes" off /></div><button className="btn-primary" onClick={() => toast.success('Notification preferences saved.')}>Save Preferences</button></Card>; }
function Toggle({ title, text, off }) { const [on, setOn] = useState(!off); return <div className="toggle-row"><div><strong>{title}</strong><p>{text}</p></div><button className={`switch ${on ? 'on' : ''}`} onClick={() => setOn(!on)}><span /></button></div>; }
function Card({ title, subtitle, action, children }) { return <section className="settings-card"><header className="settings-card__header"><div><h2>{title}</h2>{subtitle && <p>{subtitle}</p>}</div>{action}</header>{children}</section>; }
function Field({ label, wide, ...props }) { return <label className={wide ? 'field-wide' : ''}><span>{label}</span><input {...props} /></label>; }
function Select({ label, options, ...props }) { return <label><span>{label}</span><select {...props}>{options.map(item => <option key={item}>{item}</option>)}</select></label>; }
const change = (form, setForm, key) => event => setForm({ ...form, [key]: event.target.value });
const formatDate = value => value ? new Date(value).toLocaleDateString('en-IN', { day: 'numeric', month: 'short', year: 'numeric' }) : '—';
