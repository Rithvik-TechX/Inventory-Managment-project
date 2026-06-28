import PageLayout from '../components/PageLayout';
import { useAuth } from '../hooks/useAuth';

export default function WorkspacePage({type}){
 const {user}=useAuth();
 if(type==='Profile')return <PageLayout title="Profile"><div className="admin-heading"><div><h1>Your profile</h1><p>Review your account and workspace access.</p></div></div><section className="settings-card profile-summary"><div className="review-avatar">{(user?.name||user?.username||'U')[0]}</div><div><h2>{user?.name||user?.username||'User'}</h2><p>{user?.email||user?.username}</p><span className={`role-chip ${(user?.role||'STAFF').toLowerCase()}`}>{user?.role||'STAFF'}</span></div></section></PageLayout>;
 return <PageLayout title="Notifications"><div className="admin-heading"><div><h1>Notifications</h1><p>Stock alerts and workspace activity.</p></div><button className="btn-secondary">Mark all read</button></div><section className="settings-card"><div className="proper-empty"><div>♢</div><strong>You&apos;re all caught up</strong><p>We&apos;ll let you know when something needs your attention.</p></div></section></PageLayout>;
}
