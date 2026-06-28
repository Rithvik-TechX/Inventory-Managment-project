import { createContext, useContext, useState, useCallback, useEffect } from 'react';

const ToastContext = createContext(null);

let toastId = 0;

export function ToastProvider({ children }) {
  const [toasts, setToasts] = useState([]);

  const addToast = useCallback((message, type = 'info', duration = 4000) => {
    const id = ++toastId;
    setToasts(prev => [...prev, { id, message, type, exiting: false }]);
    setTimeout(() => {
      setToasts(prev => prev.map(t => t.id === id ? { ...t, exiting: true } : t));
      setTimeout(() => {
        setToasts(prev => prev.filter(t => t.id !== id));
      }, 300);
    }, duration);
    return id;
  }, []);

  const removeToast = useCallback((id) => {
    setToasts(prev => prev.map(t => t.id === id ? { ...t, exiting: true } : t));
    setTimeout(() => {
      setToasts(prev => prev.filter(t => t.id !== id));
    }, 300);
  }, []);

  const api = {
    success: (msg) => addToast(msg, 'success'),
    error:   (msg) => addToast(msg, 'error', 6000),
    warning: (msg) => addToast(msg, 'warning', 5000),
    info:    (msg) => addToast(msg, 'info'),
  };

  return (
    <ToastContext.Provider value={api}>
      {children}
      <ToastContainer toasts={toasts} onDismiss={removeToast} />
    </ToastContext.Provider>
  );
}

export function useToast() {
  const ctx = useContext(ToastContext);
  if (!ctx) throw new Error('useToast must be used within ToastProvider');
  return ctx;
}

/* ── Icons per type ── */
const icons = {
  success: (
    <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.5" strokeLinecap="round" strokeLinejoin="round">
      <path d="M22 11.08V12a10 10 0 1 1-5.93-9.14" /><polyline points="22 4 12 14.01 9 11.01" />
    </svg>
  ),
  error: (
    <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.5" strokeLinecap="round" strokeLinejoin="round">
      <circle cx="12" cy="12" r="10" /><line x1="15" y1="9" x2="9" y2="15" /><line x1="9" y1="9" x2="15" y2="15" />
    </svg>
  ),
  warning: (
    <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.5" strokeLinecap="round" strokeLinejoin="round">
      <path d="M10.29 3.86L1.82 18a2 2 0 0 0 1.71 3h16.94a2 2 0 0 0 1.71-3L13.71 3.86a2 2 0 0 0-3.42 0z" /><line x1="12" y1="9" x2="12" y2="13" /><line x1="12" y1="17" x2="12.01" y2="17" />
    </svg>
  ),
  info: (
    <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.5" strokeLinecap="round" strokeLinejoin="round">
      <circle cx="12" cy="12" r="10" /><line x1="12" y1="16" x2="12" y2="12" /><line x1="12" y1="8" x2="12.01" y2="8" />
    </svg>
  ),
};

const typeColors = {
  success: { bg: 'var(--accent-green-subtle, rgba(34,197,94,0.12))', border: 'var(--accent-green)', color: 'var(--accent-green)' },
  error:   { bg: 'var(--accent-red-subtle, rgba(239,68,68,0.12))',   border: 'var(--accent-red)',   color: 'var(--accent-red)' },
  warning: { bg: 'var(--accent-amber-subtle, rgba(245,158,11,0.12))', border: 'var(--accent-amber)', color: 'var(--accent-amber)' },
  info:    { bg: 'var(--accent-primary-subtle, rgba(79,142,255,0.12))', border: 'var(--accent-primary)', color: 'var(--accent-primary)' },
};

function ToastContainer({ toasts, onDismiss }) {
  if (!toasts.length) return null;

  return (
    <div style={{
      position: 'fixed', top: 20, right: 20, zIndex: 10000,
      display: 'flex', flexDirection: 'column', gap: 10,
      pointerEvents: 'none', maxWidth: 400, width: '100%',
    }}>
      {toasts.map(toast => (
        <ToastItem key={toast.id} toast={toast} onDismiss={onDismiss} />
      ))}
    </div>
  );
}

function ToastItem({ toast, onDismiss }) {
  const { bg, border, color } = typeColors[toast.type] || typeColors.info;

  return (
    <div style={{
      display: 'flex', alignItems: 'center', gap: 12,
      padding: '14px 16px',
      background: 'var(--bg-card, #161b28)',
      border: `1px solid ${border}`,
      borderLeft: `4px solid ${border}`,
      borderRadius: 10,
      boxShadow: '0 8px 32px rgba(0,0,0,0.3)',
      backdropFilter: 'blur(12px)',
      pointerEvents: 'auto',
      animation: toast.exiting ? 'toastExit 0.3s ease forwards' : 'toastEnter 0.3s ease forwards',
      fontSize: '0.85rem',
      color: 'var(--text-primary, #f0f4ff)',
      fontFamily: 'var(--font-sans, Inter, sans-serif)',
    }}>
      <span style={{ color, flexShrink: 0 }}>{icons[toast.type]}</span>
      <span style={{ flex: 1 }}>{toast.message}</span>
      <button
        onClick={() => onDismiss(toast.id)}
        style={{
          background: 'none', border: 'none', color: 'var(--text-muted)',
          cursor: 'pointer', padding: 4, flexShrink: 0, lineHeight: 1,
          fontSize: '1rem',
        }}
      >
        ✕
      </button>
      <style>{`
        @keyframes toastEnter {
          from { opacity: 0; transform: translateX(80px) scale(0.95); }
          to   { opacity: 1; transform: translateX(0) scale(1); }
        }
        @keyframes toastExit {
          from { opacity: 1; transform: translateX(0) scale(1); }
          to   { opacity: 0; transform: translateX(80px) scale(0.95); }
        }
      `}</style>
    </div>
  );
}
