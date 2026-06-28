import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../hooks/useAuth';
import { ValidationUtils } from '../utilities/ValidationUtils';
import '../styles/login.css';

export default function LoginPage() {
  const navigate = useNavigate();
  const { login, loading } = useAuth();
  const [form, setForm]     = useState({ username: '', password: '' });
  const [errors, setErrors] = useState({});

  const handleChange = (field) => (e) =>
    setForm(prev => ({ ...prev, [field]: e.target.value }));

  const handleSubmit = async (e) => {
    e.preventDefault();
    const errs = ValidationUtils.validateLogin(form);
    if (Object.keys(errs).length) { setErrors(errs); return; }
    setErrors({});
    const result = await login(form.username, form.password);
    if (result.success) {
      navigate('/dashboard');
    } else {
      setErrors({ api: result.error || 'Invalid credentials. Please try again.' });
    }
  };

  return (
    <div className="login-page">
      {/* Left: Branding panel */}
      <div className="login-brand">
        <div className="login-brand-content">
          <div className="login-brand-logo">
            <svg width="32" height="32" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
              <path d="M21 16V8a2 2 0 0 0-1-1.73l-7-4a2 2 0 0 0-2 0l-7 4A2 2 0 0 0 3 8v8a2 2 0 0 0 1 1.73l7 4a2 2 0 0 0 2 0l7-4A2 2 0 0 0 21 16z" />
              <polyline points="3.27 6.96 12 12.01 20.73 6.96" />
              <line x1="12" y1="22.08" x2="12" y2="12" />
            </svg>
            <span>InvenTrack</span>
          </div>
          <h1 className="login-brand-heading">
            Smart inventory<br />management
          </h1>
          <p className="login-brand-desc">
            Real-time stock monitoring, analytics, and low-stock alerts — everything you need to run your warehouse efficiently.
          </p>

          {/* Feature pills */}
          <div className="login-features">
            <div className="login-feature">
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                <path d="M22 11.08V12a10 10 0 1 1-5.93-9.14" /><polyline points="22 4 12 14.01 9 11.01" />
              </svg>
              Real-time stock tracking
            </div>
            <div className="login-feature">
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                <line x1="18" y1="20" x2="18" y2="10" /><line x1="12" y1="20" x2="12" y2="4" /><line x1="6" y1="20" x2="6" y2="14" />
              </svg>
              Analytics & reports
            </div>
            <div className="login-feature">
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                <path d="M10.29 3.86L1.82 18a2 2 0 0 0 1.71 3h16.94a2 2 0 0 0 1.71-3L13.71 3.86a2 2 0 0 0-3.42 0z" /><line x1="12" y1="9" x2="12" y2="13" /><line x1="12" y1="17" x2="12.01" y2="17" />
              </svg>
              Low-stock alerts
            </div>
            <div className="login-feature">
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                <rect x="3" y="11" width="18" height="11" rx="2" ry="2" /><path d="M7 11V7a5 5 0 0 1 10 0v4" />
              </svg>
              Role-based access
            </div>
          </div>
        </div>

        {/* Decorative background elements */}
        <div className="login-brand-bg">
          <div className="login-brand-circle login-brand-circle--1" />
          <div className="login-brand-circle login-brand-circle--2" />
          <div className="login-brand-circle login-brand-circle--3" />
        </div>
      </div>

      {/* Right: Login form */}
      <div className="login-form-panel">
        <div className="login-form-wrapper">
          <div className="login-form-header">
            <h2>Welcome back</h2>
            <p>Sign in to your account to continue.</p>
          </div>

          {errors.api && (
            <div className="login-error">
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                <circle cx="12" cy="12" r="10" /><line x1="15" y1="9" x2="9" y2="15" /><line x1="9" y1="9" x2="15" y2="15" />
              </svg>
              {errors.api}
            </div>
          )}

          <form onSubmit={handleSubmit} noValidate>
            <div className="login-form-group">
              <label htmlFor="username">Username</label>
              <input
                id="username"
                type="text"
                value={form.username}
                onChange={handleChange('username')}
                placeholder="Enter your username"
                autoComplete="username"
                autoFocus
              />
              {errors.username && <span className="login-field-error">{errors.username}</span>}
            </div>

            <div className="login-form-group">
              <label htmlFor="password">Password</label>
              <input
                id="password"
                type="password"
                value={form.password}
                onChange={handleChange('password')}
                placeholder="Enter your password"
                autoComplete="current-password"
              />
              {errors.password && <span className="login-field-error">{errors.password}</span>}
            </div>

            <button type="submit" className="login-submit" disabled={loading}>
              {loading ? (
                <span className="login-spinner" />
              ) : (
                'Sign in'
              )}
            </button>
          </form>

          <div className="login-footer">
            <p>
              Inventory Management System &middot; <span style={{ color: 'var(--text-muted)' }}>v2.0</span>
            </p>
          </div>
        </div>
      </div>
    </div>
  );
}