import { NavLink, useLocation } from 'react-router-dom';
import { useAuth } from '../hooks/useAuth';
import { useState, useEffect } from 'react';
import '../styles/sidebar.css';
import { Logo } from './ui/Logo';

const NAV_ITEMS = [
  {
    to: '/app/dashboard',
    label: 'Dashboard',
    icon: (
      <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.8" strokeLinecap="round" strokeLinejoin="round">
        <rect x="3" y="3" width="7" height="7" rx="1" />
        <rect x="14" y="3" width="7" height="7" rx="1" />
        <rect x="3" y="14" width="7" height="7" rx="1" />
        <rect x="14" y="14" width="7" height="7" rx="1" />
      </svg>
    ),
  },
  {
    to: '/app/products',
    label: 'Products',
    icon: (
      <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.8" strokeLinecap="round" strokeLinejoin="round">
        <path d="M21 16V8a2 2 0 0 0-1-1.73l-7-4a2 2 0 0 0-2 0l-7 4A2 2 0 0 0 3 8v8a2 2 0 0 0 1 1.73l7 4a2 2 0 0 0 2 0l7-4A2 2 0 0 0 21 16z" />
        <polyline points="3.27 6.96 12 12.01 20.73 6.96" />
        <line x1="12" y1="22.08" x2="12" y2="12" />
      </svg>
    ),
  },
  {
    to: '/app/reports',
    label: 'Reports',
    role: 'MANAGER',
    icon: (
      <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.8" strokeLinecap="round" strokeLinejoin="round">
        <line x1="18" y1="20" x2="18" y2="10" />
        <line x1="12" y1="20" x2="12" y2="4" />
        <line x1="6" y1="20" x2="6" y2="14" />
      </svg>
    ),
  },
  {
    to: '/app/categories',
    label: 'Categories',
    icon: (
      <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.8" strokeLinecap="round" strokeLinejoin="round">
        <line x1="12" y1="5" x2="12" y2="19" />
        <line x1="5" y1="12" x2="19" y2="12" />
      </svg>
    ),
  },
  {
    to: '/app/suppliers',
    label: 'Suppliers',
    icon: (
      <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.8" strokeLinecap="round" strokeLinejoin="round">
        <path d="M16 21v-2a4 4 0 0 0-4-4H6a4 4 0 0 0-4 4v2" />
        <circle cx="9" cy="7" r="4" />
        <line x1="19" y1="8" x2="19" y2="14" />
        <line x1="22" y1="11" x2="16" y2="11" />
      </svg>
    ),
  },
  { to: '/app/orders', label: 'Orders', icon: <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.8"><path d="M6 2h12l3 6-9 4-9-4 3-6Z"/><path d="M3 8v10l9 4 9-4V8"/></svg> },
];

export default function Sidebar({ isOpen, onToggle }) {
  const { user, hasRole, logout } = useAuth();
  const location = useLocation();
  const [collapsed, setCollapsed] = useState(false);

  // Close mobile sidebar on navigation
  useEffect(() => {
    if (window.innerWidth <= 768 && isOpen) {
      onToggle?.();
    }
  }, [location.pathname]); // close mobile sidebar on nav

  const initial = user?.username?.[0]?.toUpperCase() || 'U';
  const roleBadgeClass =
    user?.role === 'ADMIN' ? 'role-admin' :
    user?.role === 'MANAGER' ? 'role-manager' : 'role-staff';

  return (
    <>
      {/* Mobile overlay */}
      {isOpen && <div className="sidebar-overlay" onClick={onToggle} />}

      <aside className={`sidebar ${isOpen ? 'sidebar--open' : ''} ${collapsed ? 'sidebar--collapsed' : ''}`}>
        {/* Logo */}
        <div className="sidebar__logo">
          <Logo size={34} showText={!collapsed} textSize={17} />
          <button
            className="sidebar__collapse-btn desktop-only"
            onClick={() => setCollapsed(!collapsed)}
            title={collapsed ? 'Expand sidebar' : 'Collapse sidebar'}
          >
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"
              style={{ transform: collapsed ? 'rotate(180deg)' : 'none', transition: 'transform 0.2s' }}>
              <polyline points="11 17 6 12 11 7" />
              <polyline points="18 17 13 12 18 7" />
            </svg>
          </button>
        </div>

        {/* Section label */}
        {!collapsed && <div className="sidebar__section-label">Navigation</div>}

        {/* Nav links */}
        <nav className="sidebar__nav">
          {NAV_ITEMS.map(({ to, icon, label, role }) => {
            if (role && !hasRole(role)) return null;
            return (
              <NavLink
                key={to}
                to={to}
                className={({ isActive }) => `sidebar__link ${isActive ? 'sidebar__link--active' : ''}`}
                title={collapsed ? label : undefined}
              >
                <span className="sidebar__link-icon">{icon}</span>
                {!collapsed && <span className="sidebar__link-label">{label}</span>}
              </NavLink>
            );
          })}
        </nav>

        <div className="sidebar__nav sidebar__nav--secondary">
          {!collapsed && <div className="sidebar__section-label">Workspace</div>}
          {hasRole('MANAGER') && <NavLink to="/app/settings" className={({isActive}) => `sidebar__link ${isActive ? 'sidebar__link--active' : ''}`}><span className="sidebar__link-icon">⚙</span>{!collapsed && <span className="sidebar__link-label">Settings</span>}</NavLink>}
        </div>

        {/* Bottom user strip */}
        <div className="sidebar__footer">
          <div className="sidebar__user">
            <div className="sidebar__avatar">{initial}</div>
            {!collapsed && (
              <div className="sidebar__user-info">
                <div className="sidebar__user-name">{user?.username || 'User'}</div>
                <div className={`sidebar__user-role ${roleBadgeClass}`}>{user?.role || 'STAFF'}</div>
              </div>
            )}
          </div>
          <button
            className="sidebar__logout-btn"
            onClick={logout}
            title="Sign out"
          >
            <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.8" strokeLinecap="round" strokeLinejoin="round">
              <path d="M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4" />
              <polyline points="16 17 21 12 16 7" />
              <line x1="21" y1="12" x2="9" y2="12" />
            </svg>
          </button>
        </div>
      </aside>
    </>
  );
}
