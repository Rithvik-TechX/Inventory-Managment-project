import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../hooks/useAuth';
import { ValidationUtils } from '../utilities/ValidationUtils';
import '../styles/login.css';

export default function LoginPage() {
  const navigate        = useNavigate();
  const { login, loading } = useAuth();

  const [form,   setForm]   = useState({ username: '', password: '' });
  const [errors, setErrors] = useState({});
  const [apiErr, setApiErr] = useState('');

  const set = (field) => (e) => setForm(f => ({ ...f, [field]: e.target.value }));

  const handleSubmit = async (e) => {
    e.preventDefault();
    const errs = ValidationUtils.validateLogin(form);
    if (Object.keys(errs).length) { setErrors(errs); return; }
    setErrors({}); setApiErr('');

    const result = await login(form.username, form.password);
    if (result.success) {
      navigate('/dashboard');
    } else {
      setApiErr('Invalid username or password. Please try again.');
    }
  };

  return (
    <div className="login-root">
      {/* Left branding panel */}
      <div className="login-brand">
        <div className="login-brand-logo">
          <span className="dot" />
          InvenTrack
        </div>
        <h1>
          Smart <em>Inventory</em><br />
          Management
        </h1>
        <p>
          Monitor stock levels, generate insightful reports, and keep your
          supply chain running smoothly — all in one place.
        </p>
        <div className="login-brand-stats">
          <div className="login-stat">
            <span className="login-stat-value">Real‑time</span>
            <span className="login-stat-label">Stock Monitoring</span>
          </div>
          <div className="login-stat">
            <span className="login-stat-value">Auto</span>
            <span className="login-stat-label">Low Stock Alerts</span>
          </div>
          <div className="login-stat">
            <span className="login-stat-value">Full</span>
            <span className="login-stat-label">Audit Reports</span>
          </div>
        </div>
      </div>

      {/* Right form panel */}
      <div className="login-form-panel">
        <h2>Welcome back</h2>
        <p className="sub">Sign in to your account to continue.</p>

        {apiErr && <div className="login-error">{apiErr}</div>}

        <form onSubmit={handleSubmit} noValidate>
          <div className="form-group">
            <label>Username</label>
            <input
              type="text"
              value={form.username}
              onChange={set('username')}
              placeholder="Enter your username"
              autoFocus
            />
            {errors.username && <div className="error-msg">{errors.username}</div>}
          </div>

          <div className="form-group">
            <label>Password</label>
            <input
              type="password"
              value={form.password}
              onChange={set('password')}
              placeholder="••••••••"
            />
            {errors.password && <div className="error-msg">{errors.password}</div>}
          </div>

          <button type="submit" className="login-btn" disabled={loading}>
            {loading ? 'Signing in…' : 'Sign In'}
          </button>
        </form>
      </div>
    </div>
  );
}