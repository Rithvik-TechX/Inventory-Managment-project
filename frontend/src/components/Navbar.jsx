import { useAuth } from '../hooks/useAuth';
import '../styles/navbar.css';

export default function Navbar({ title = 'Dashboard', onMenuToggle }) {
  const { user, logout } = useAuth();
  const initial = user?.username?.[0]?.toUpperCase() || 'U';

  return (
    <header className="navbar">
      {/* Left section */}
      <div className="navbar__left">
        <button className="navbar__menu-btn mobile-only" onClick={onMenuToggle} aria-label="Toggle menu">
          <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
            <line x1="3" y1="6" x2="21" y2="6" />
            <line x1="3" y1="12" x2="21" y2="12" />
            <line x1="3" y1="18" x2="21" y2="18" />
          </svg>
        </button>
        <h1 className="navbar__title">{title}</h1>
      </div>

      {/* Right section */}
      <div className="navbar__right">
        {/* User pill */}
        <div className="navbar__user-pill">
          <div className="navbar__avatar">{initial}</div>
          <div className="navbar__user-details">
            <span className="navbar__user-name">{user?.username || 'User'}</span>
            <span className="navbar__user-role">{user?.role || 'STAFF'}</span>
          </div>
        </div>

        <button className="navbar__action-btn" onClick={logout} title="Sign out">
          <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.8" strokeLinecap="round" strokeLinejoin="round">
            <path d="M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4" />
            <polyline points="16 17 21 12 16 7" />
            <line x1="21" y1="12" x2="9" y2="12" />
          </svg>
          <span className="navbar__action-label">Sign out</span>
        </button>
      </div>
    </header>
  );
}