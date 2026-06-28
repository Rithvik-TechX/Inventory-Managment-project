import { useEffect, useRef, useState } from 'react';
import { Link } from 'react-router-dom';
import { useAuth } from '../hooks/useAuth';
import { useTheme } from '../context/ThemeContext';
import '../styles/navbar.css';

export default function Navbar({ title = 'Dashboard', onMenuToggle }) {
  const { user, logout } = useAuth();
  const { theme, toggleTheme } = useTheme();
  const [panel, setPanel] = useState(null);
  const root = useRef(null);
  useEffect(() => { const close = e => !root.current?.contains(e.target) && setPanel(null); document.addEventListener('mousedown', close); return () => document.removeEventListener('mousedown', close); }, []);
  const initial = user?.username?.[0]?.toUpperCase() || 'R';
  return <header className="navbar" ref={root}>
    <div className="navbar__left"><button className="navbar__menu-btn mobile-only" onClick={onMenuToggle} aria-label="Open navigation">☰</button><div><span className="navbar__crumb">Workspace /</span><h1 className="navbar__title">{title}</h1></div></div>
    <div className="navbar__search"><span>⌕</span><input aria-label="Search inventory" placeholder="Search inventory…" /><kbd>⌘ K</kbd></div>
    <div className="navbar__right">
      <button className="navbar__icon-btn" onClick={toggleTheme} aria-label={`Use ${theme === 'dark' ? 'light' : 'dark'} mode`}>{theme === 'dark' ? '☼' : '◐'}</button>
      <div className="navbar__popover-wrap"><button className="navbar__icon-btn" onClick={() => setPanel(panel === 'notifications' ? null : 'notifications')} aria-label="Notifications">♢<span className="notification-dot">3</span></button>{panel === 'notifications' && <div className="navbar__popover notification-panel"><div className="popover-head"><strong>Notifications</strong><button>Mark all read</button></div>{[['Low stock alert','Wireless Mouse has 3 units left','8m'],['Stock received','50 units of Samsung TV added','1h'],['Report ready','May inventory report is ready','3h']].map(item => <div className="notification-item" key={item[0]}><span className="notification-item__dot"/><div><strong>{item[0]}</strong><p>{item[1]}</p></div><time>{item[2]}</time></div>)}<Link to="/app/notifications">View all notifications →</Link></div>}</div>
      <div className="navbar__popover-wrap"><button className="navbar__user-pill" onClick={() => setPanel(panel === 'user' ? null : 'user')}><span className="navbar__avatar">{initial}</span><span className="navbar__user-details"><strong>{user?.username || 'Rithvik'}</strong><small>{user?.role || 'ADMIN'}</small></span><span>⌄</span></button>{panel === 'user' && <div className="navbar__popover user-panel"><Link to="/app/profile">Profile</Link><Link to="/app/settings">Settings</Link><button onClick={logout}>Sign out</button></div>}</div>
    </div>
  </header>;
}
