import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../hooks/useAuth';
import { api } from '../utilities/ApiUtils';
import { ValidationUtils } from '../utilities/ValidationUtils';
import '../styles/login.css';

export default function LoginPage() {
  const navigate = useNavigate();
  const { login, loading } = useAuth();

  const [mode, setMode] = useState('login'); // 'login' or 'signup'

  // Login state
  const [loginForm, setLoginForm] = useState({ username: '', password: '' });
  const [loginErrors, setLoginErrors] = useState({});
  const [loginApiErr, setLoginApiErr] = useState('');

  // Signup state
  const [signupForm, setSignupForm] = useState({ name: '', email: '', password: '' });
  const [signupErrors, setSignupErrors] = useState({});
  const [signupApiErr, setSignupApiErr] = useState('');
  const [signupSuccess, setSignupSuccess] = useState('');

  const setLogin = (field) => (e) => setLoginForm(f => ({ ...f, [field]: e.target.value }));
  const setSignup = (field) => (e) => setSignupForm(f => ({ ...f, [field]: e.target.value }));

  const handleLogin = async (e) => {
    e.preventDefault();
    const errs = ValidationUtils.validateLogin(loginForm);
    if (Object.keys(errs).length) { setLoginErrors(errs); return; }
    setLoginErrors({}); setLoginApiErr('');

    const result = await login(loginForm.username, loginForm.password);
    if (result.success) {
      navigate('/dashboard');
    } else {
      setLoginApiErr('Invalid username/email or password. Please try again.');
    }
  };

  const handleSignup = async (e) => {
    e.preventDefault();
    const errs = {};
    if (!signupForm.name.trim()) errs.name = 'Name is required.';
    if (!signupForm.email.trim()) errs.email = 'Email is required.';
    else if (!/\S+@\S+\.\S+/.test(signupForm.email)) errs.email = 'Enter a valid email.';
    if (!signupForm.password || signupForm.password.length < 6) errs.password = 'Password must be at least 6 characters.';
    if (Object.keys(errs).length) { setSignupErrors(errs); return; }
    setSignupErrors({}); setSignupApiErr(''); setSignupSuccess('');

    try {
      await api.post('/auth/signup', {
        username: signupForm.email,
        name: signupForm.name,
        email: signupForm.email,
        password: signupForm.password,
        role: 'STAFF',
      });
      setSignupSuccess('Account created! You can now sign in.');
      setTimeout(() => {
        setMode('login');
        setLoginForm({ username: signupForm.email, password: '' });
        setSignupSuccess('');
      }, 1500);
    } catch (err) {
      setSignupApiErr(err.message || 'Failed to create account.');
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

        {/* ─── SIGN IN ─── */}
        {mode === 'login' && (
          <>
            <h2>Welcome back</h2>
            <p className="sub">Sign in to your account to continue.</p>

            {loginApiErr && <div className="login-error">{loginApiErr}</div>}

            <form onSubmit={handleLogin} noValidate>
              <div className="form-group">
                <label>Username / Email</label>
                <input
                  type="text"
                  value={loginForm.username}
                  onChange={setLogin('username')}
                  placeholder="Enter your username or email"
                  autoFocus
                />
                {loginErrors.username && <div className="error-msg">{loginErrors.username}</div>}
              </div>

              <div className="form-group">
                <label>Password</label>
                <input
                  type="password"
                  value={loginForm.password}
                  onChange={setLogin('password')}
                  placeholder="••••••••"
                />
                {loginErrors.password && <div className="error-msg">{loginErrors.password}</div>}
              </div>

              <button type="submit" className="login-btn" disabled={loading}>
                {loading ? 'Signing in…' : 'Sign In'}
              </button>
            </form>

            <p className="auth-switch">
              Don't have an account?{' '}
              <button className="link-btn" onClick={() => setMode('signup')}>Create one</button>
            </p>
          </>
        )}

        {/* ─── SIGN UP (Staff only) ─── */}
        {mode === 'signup' && (
          <>
            <h2>Create Account</h2>
            <p className="sub">Register as a Staff member to get started.</p>

            {signupApiErr && <div className="login-error">{signupApiErr}</div>}
            {signupSuccess && <div className="login-success">{signupSuccess}</div>}

            <form onSubmit={handleSignup} noValidate>
              <div className="form-group">
                <label>Full Name</label>
                <input
                  type="text"
                  value={signupForm.name}
                  onChange={setSignup('name')}
                  placeholder="e.g. Rithvik Gandhamalla"
                  autoFocus
                />
                {signupErrors.name && <div className="error-msg">{signupErrors.name}</div>}
              </div>

              <div className="form-group">
                <label>Email</label>
                <input
                  type="email"
                  value={signupForm.email}
                  onChange={setSignup('email')}
                  placeholder="you@example.com"
                />
                {signupErrors.email && <div className="error-msg">{signupErrors.email}</div>}
              </div>

              <div className="form-group">
                <label>Password</label>
                <input
                  type="password"
                  value={signupForm.password}
                  onChange={setSignup('password')}
                  placeholder="Min. 6 characters"
                />
                {signupErrors.password && <div className="error-msg">{signupErrors.password}</div>}
              </div>

              <div className="role-info-box">
                <span className="role-info-badge">STAFF</span>
                <span className="role-info-text">View-only access. Admin & Manager roles are assigned by an administrator.</span>
              </div>

              <button type="submit" className="login-btn">
                Create Account
              </button>
            </form>

            <p className="auth-switch">
              Already have an account?{' '}
              <button className="link-btn" onClick={() => setMode('login')}>Sign in</button>
            </p>
          </>
        )}
      </div>
    </div>
  );
}