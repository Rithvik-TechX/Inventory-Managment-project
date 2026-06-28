import { useState } from 'react';
import { Link, useNavigate, useSearchParams } from 'react-router-dom';
import { useAuth } from '../hooks/useAuth';
import '../styles/login.css';

export default function LoginPage() {
  const navigate = useNavigate();
  const [params] = useSearchParams();
  const { login, loading } = useAuth();
  const [form, setForm] = useState({ email: '', password: '' });
  const [error, setError] = useState('');
  const [showPassword, setShowPassword] = useState(false);
  const [remember, setRemember] = useState(true);
  const [shaking, setShaking] = useState(false);

  const submit = async event => {
    event.preventDefault();
    setError('');
    if (!form.email || !form.password) return setError('Invalid email or password.');
    const result = await login(form.email, form.password);
    if (result.success) return navigate('/app/dashboard', { replace: true });
    setError('Invalid email or password.');
    setForm(value => ({ ...value, password: '' }));
    setShaking(true);
    window.setTimeout(() => setShaking(false), 320);
  };

  return <main className="classic-login">
    <section className={`classic-login__card ${shaking ? 'is-shaking' : ''}`}>
      <div className="classic-login__brand"><span>I</span><strong>InvenTrack</strong></div>
      <p className="classic-login__subtitle">Sign in to your account</p>
      {params.get('registered') === 'true' && <div className="auth-success">Account created! You can now sign in.</div>}
      <form onSubmit={submit} noValidate>
        <label className="classic-field"><span>Email address</span><input type="email" autoComplete="username" placeholder="you@company.com" value={form.email} onChange={e => setForm({ ...form, email: e.target.value })} /></label>
        <label className="classic-field"><span>Password</span><div className="classic-password"><input type={showPassword ? 'text' : 'password'} autoComplete="current-password" placeholder="Enter your password" value={form.password} onChange={e => setForm({ ...form, password: e.target.value })} /><button type="button" onClick={() => setShowPassword(value => !value)}>{showPassword ? 'Hide' : 'Show'}</button></div></label>
        <div className="classic-login__options"><label><input type="checkbox" checked={remember} onChange={e => setRemember(e.target.checked)} /> Remember me</label><Link to="/forgot-password">Forgot password?</Link></div>
        <button className="classic-login__submit" disabled={loading}>{loading && <span className="login-spinner" />}{loading ? 'Signing in...' : 'Sign in'}</button>
        {error && <p className="classic-login__error" role="alert">{error}</p>}
      </form>
      <div className="classic-login__divider"><span>or</span></div>
      <p className="classic-login__signup">Don&apos;t have an account? <Link to="/register">Sign up as Staff</Link></p>
      <p className="classic-login__note">Admin and Manager accounts are created by your administrator.</p>
    </section>
  </main>;
}
