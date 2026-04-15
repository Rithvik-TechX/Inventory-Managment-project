import { NavLink } from 'react-router-dom';
import { useAuth } from '../hooks/useAuth';

const NAV = [
  { to: '/dashboard',   icon: '⊞',  label: 'Dashboard' },
  { to: '/products',    icon: '📦', label: 'Products'  },
  { to: '/reports',     icon: '📊', label: 'Reports'   },
  { to: '/add-product', icon: '+',  label: 'Add Product', role: 'MANAGER' },
  { to: '/add-user',    icon: '👤', label: 'Add User',    role: 'ADMIN'   },
];

export default function Sidebar() {
  const { user, hasRole, logout } = useAuth();

  return (
    <aside style={{
      position: 'fixed', top: 0, left: 0,
      width: 'var(--sidebar-width)', height: '100vh',
      background: 'var(--bg-secondary)',
      borderRight: '1px solid var(--border)',
      display: 'flex', flexDirection: 'column',
      zIndex: 200,
    }}>
      {/* Logo */}
      <div style={{
        padding: '22px 24px',
        borderBottom: '1px solid var(--border)',
        display: 'flex', alignItems: 'center', gap: 10,
      }}>
        <div style={{
          width: 32, height: 32, borderRadius: 8,
          background: 'var(--accent-primary)',
          display: 'flex', alignItems: 'center', justifyContent: 'center',
          fontSize: '1rem',
        }}>📦</div>
        <span style={{
          fontFamily: 'Syne,sans-serif', fontWeight: 800,
          fontSize: '1rem', letterSpacing: '-0.02em',
        }}>InvenTrack</span>
      </div>

      {/* Nav links */}
      <nav style={{ flex: 1, padding: '16px 12px', display: 'flex', flexDirection: 'column', gap: 4 }}>
        {NAV.map(({ to, icon, label, role }) => {
          if (role && !hasRole(role)) return null;
          return (
            <NavLink key={to} to={to} style={({ isActive }) => ({
              display: 'flex', alignItems: 'center', gap: 12,
              padding: '10px 14px', borderRadius: 'var(--radius-sm)',
              fontSize: '0.88rem', fontWeight: 500,
              textDecoration: 'none',
              color: isActive ? 'var(--text-primary)' : 'var(--text-secondary)',
              background: isActive ? 'var(--bg-hover)' : 'transparent',
              borderLeft: isActive ? '3px solid var(--accent-primary)' : '3px solid transparent',
              transition: 'all 0.15s',
            })}>
              <span style={{ fontSize: '1rem', width: 20, textAlign: 'center' }}>{icon}</span>
              {label}
            </NavLink>
          );
        })}
      </nav>

      {/* Bottom user strip */}
      <div style={{
        padding: '16px', borderTop: '1px solid var(--border)',
        display: 'flex', alignItems: 'center', gap: 10,
      }}>
        <div style={{
          width: 34, height: 34, borderRadius: '50%',
          background: 'var(--accent-primary)',
          display: 'flex', alignItems: 'center', justifyContent: 'center',
          fontWeight: 700, fontSize: '0.85rem', color: '#fff', flexShrink: 0,
        }}>
          {user?.username?.[0]?.toUpperCase()}
        </div>
        <div style={{ flex: 1, minWidth: 0 }}>
          <div style={{ fontSize: '0.82rem', fontWeight: 600, whiteSpace: 'nowrap', overflow: 'hidden', textOverflow: 'ellipsis' }}>
            {user?.username}
          </div>
          <div style={{ fontSize: '0.7rem', color: 'var(--text-secondary)' }}>{user?.role}</div>
        </div>
        <button onClick={logout} title="Sign out" style={{
          background: 'none', border: 'none', color: 'var(--text-muted)',
          fontSize: '1rem', cursor: 'pointer', padding: 4,
          transition: 'color 0.15s',
        }}>⏻</button>
      </div>
    </aside>
  );
}