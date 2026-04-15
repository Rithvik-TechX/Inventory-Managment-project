import { useAuth } from '../hooks/useAuth';

export default function Navbar({ title = 'Dashboard' }) {
  const { user, logout } = useAuth();

  return (
    <nav style={{
      position: 'fixed', top: 0, left: 'var(--sidebar-width)', right: 0,
      height: 'var(--navbar-height)', zIndex: 100,
      background: 'rgba(10,13,20,0.85)', backdropFilter: 'blur(12px)',
      borderBottom: '1px solid var(--border)',
      display: 'flex', alignItems: 'center',
      padding: '0 36px', justifyContent: 'space-between',
    }}>
      <span style={{ fontFamily: 'Syne,sans-serif', fontWeight: 700, fontSize: '1rem' }}>
        {title}
      </span>

      <div style={{ display: 'flex', alignItems: 'center', gap: 16 }}>
        <div style={{
          display: 'flex', alignItems: 'center', gap: 10,
          background: 'var(--bg-card)', border: '1px solid var(--border)',
          borderRadius: 'var(--radius-sm)', padding: '6px 14px',
        }}>
          <div style={{
            width: 28, height: 28, borderRadius: '50%',
            background: 'var(--accent-primary)',
            display: 'flex', alignItems: 'center', justifyContent: 'center',
            fontSize: '0.75rem', fontWeight: 700, color: '#fff',
          }}>
            {user?.username?.[0]?.toUpperCase() || 'U'}
          </div>
          <div>
            <div style={{ fontSize: '0.82rem', fontWeight: 600, lineHeight: 1.2 }}>
              {user?.username}
            </div>
            <div style={{ fontSize: '0.7rem', color: 'var(--text-secondary)', lineHeight: 1 }}>
              {user?.role}
            </div>
          </div>
        </div>

        <button
          onClick={logout}
          style={{
            background: 'none', border: '1px solid var(--border)',
            color: 'var(--text-secondary)', borderRadius: 'var(--radius-sm)',
            padding: '7px 14px', fontSize: '0.82rem', cursor: 'pointer',
          }}
        >
          Sign out
        </button>
      </div>
    </nav>
  );
}