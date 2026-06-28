import { useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { useAuth } from '../hooks/useAuth';
import { ValidationUtils } from '../utilities/ValidationUtils';
import '../styles/login.css';

export default function LoginPage() {
  const navigate = useNavigate();
  const location = useLocation();
  const { login, loading } = useAuth();
  const [form, setForm] = useState({ username: '', password: '' });
  const [errors, setErrors] = useState({});
  const [role, setRole] = useState('Staff');
  const [showPassword, setShowPassword] = useState(false);
  const [remember, setRemember] = useState(true);
  const handleChange = field => e => setForm(value => ({ ...value, [field]: e.target.value }));
  const handleSubmit = async e => {
    e.preventDefault();
    const nextErrors = ValidationUtils.validateLogin(form);
    if (Object.keys(nextErrors).length) return setErrors(nextErrors);
    const result = await login(form.username, form.password);
    if (result.success) navigate('/app/dashboard');
    else setErrors({ api: result.error || 'Those credentials do not match our records.' });
  };

  return <main className="login-page">
    <section className="login-brand">
      <div className="login-brand-content">
        <div className="login-brand-logo"><span className="brand-mark">N</span><span>NexaStock</span></div>
        <p className="brand-eyebrow">INVENTORY INTELLIGENCE</p>
        <h1 className="login-brand-heading">Inventory,<br/>under control.</h1>
        <p className="login-brand-desc">Real-time stock visibility for teams that move fast. One clear workspace, from receiving to reorder.</p>
        <div className="login-features">{['Live stock alerts and low inventory warnings','Role-based access for every team','One-click reports and supplier analytics'].map(text => <div className="login-feature" key={text}><span>✓</span>{text}</div>)}</div>
        <blockquote className="login-quote"><div>★★★★★</div><p>“We cut stockout incidents by 60% in the first month.”</p><footer>Priya S. · Operations Manager</footer></blockquote>
      </div>
      <div className="login-brand-bg"><i/><i/><i/></div>
    </section>
    <section className="login-form-panel"><div className="login-form-wrapper">
      <div className="login-mobile-logo"><span className="brand-mark">N</span>NexaStock</div>
      <div className="login-form-header"><p className="eyebrow">WELCOME BACK</p><h2>Sign in to your account</h2><p>New here? <Link to="/register">Create an account</Link></p></div>
      {location.state?.registered && <div className="auth-success">Account created. You can sign in now.</div>}
      {errors.api && <div className="login-error">ⓘ {errors.api}</div>}
      <form onSubmit={handleSubmit} noValidate>
        <div className="role-selector">{['Staff','Manager','Admin'].map(item => <button type="button" className={role === item ? 'active' : ''} onClick={() => setRole(item)} key={item}>{item}</button>)}</div>
        <div className="login-form-group"><label htmlFor="username">Email address</label><input id="username" value={form.username} onChange={handleChange('username')} placeholder="you@company.com" autoComplete="username" autoFocus/>{errors.username && <span className="login-field-error">{errors.username}</span>}</div>
        <div className="login-form-group"><label htmlFor="password">Password</label><div className="password-wrap"><input id="password" type={showPassword ? 'text' : 'password'} value={form.password} onChange={handleChange('password')} placeholder="Enter your password" autoComplete="current-password"/><button type="button" onClick={() => setShowPassword(!showPassword)}>{showPassword ? 'Hide' : 'Show'}</button></div>{errors.password && <span className="login-field-error">{errors.password}</span>}</div>
        <div className="login-options"><label><input type="checkbox" checked={remember} onChange={e => setRemember(e.target.checked)}/> Remember me</label><Link to="/forgot-password">Forgot password?</Link></div>
        <button type="submit" className="login-submit" disabled={loading}>{loading ? <><span className="login-spinner"/> Signing in…</> : 'Sign in to NexaStock'}</button>
      </form>
      <div className="login-divider"><span>or continue with</span></div><button className="google-button"><strong>G</strong> Continue with Google</button>
      <div className="login-footer"><p>By continuing, you agree to our Terms and Privacy Policy.</p></div>
    </div></section>
  </main>;
}
