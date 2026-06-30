import { useEffect, useRef, useState } from 'react';
import { Link } from 'react-router-dom';
import { useAuth } from '../hooks/useAuth';
import { api } from '../utilities/ApiUtils';
import GlobalSearch from './GlobalSearch';
import '../styles/navbar.css';

const Bell = () => <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.8" strokeLinecap="round" strokeLinejoin="round"><path d="M18 8a6 6 0 0 0-12 0c0 7-3 7-3 9h18c0-2-3-2-3-9" /><path d="M13.7 21a2 2 0 0 1-3.4 0" /></svg>;

export default function Navbar({ title = 'Dashboard', onMenuToggle }) {
  const { user, logout, hasRole } = useAuth();
  const [panel, setPanel] = useState(null);
  const [alerts, setAlerts] = useState([]);
  const root = useRef(null);

  useEffect(() => {
    const close = event => { if (!root.current?.contains(event.target)) setPanel(null); };
    document.addEventListener('mousedown', close);
    api.get('/products/low-stock?threshold=10').then(setAlerts).catch(() => {});
    return () => document.removeEventListener('mousedown', close);
  }, []);

  const name = user?.name || user?.username || 'User';
  const initial = name[0]?.toUpperCase() || 'U';

  return (
    <header className="navbar" ref={root}>
      <div className="navbar__left"><button className="navbar__menu-btn mobile-only" onClick={onMenuToggle} aria-label="Open navigation">☰</button><div><span className="navbar__crumb">Workspace /</span><h1 className="navbar__title">{title}</h1></div></div>
      <GlobalSearch />
      <div className="navbar__right">
        <div className="navbar__popover-wrap">
          <button className="navbar__icon-btn" onClick={() => setPanel(panel === 'notifications' ? null : 'notifications')} aria-label="Notifications"><Bell />{alerts.length > 0 && <span className="notification-count">{alerts.length}</span>}</button>
          {panel === 'notifications' && <div className="navbar__popover notification-panel"><div className="popover-head"><strong>Notifications</strong><button onClick={() => setAlerts([])}>Mark all read</button></div>{alerts.length ? alerts.slice(0, 5).map(item => <div className="notification-item" key={item.id}><span className="notification-item__dot" /><div><strong>Low stock alert</strong><p>{item.name} has {item.quantity} units left</p></div><time>Now</time></div>) : <div className="notification-empty">You&apos;re all caught up</div>}<Link to="/app/notifications">View all notifications →</Link></div>}
        </div>
        <div className="navbar__popover-wrap">
          <button className="navbar__user-pill" onClick={() => setPanel(panel === 'user' ? null : 'user')}><span className="navbar__avatar">{initial}</span><span className="navbar__user-details"><strong>{name}</strong><small>{user?.role || 'STAFF'}</small></span><span>⌄</span></button>
          {panel === 'user' && <div className="navbar__popover user-panel"><Link to="/app/profile">Profile</Link>{hasRole('MANAGER') && <Link to="/app/settings">Settings</Link>}<button onClick={logout}>Sign out</button></div>}
        </div>
      </div>
    </header>
  );
}
