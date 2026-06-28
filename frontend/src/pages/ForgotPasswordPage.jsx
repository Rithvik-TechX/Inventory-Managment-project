import { useState } from 'react';
import { Link } from 'react-router-dom';
import '../styles/auth.css';

export default function ForgotPasswordPage() {
  const [email, setEmail] = useState('');
  const [sent, setSent] = useState(false);
  return <main className="auth-page auth-page--center"><section className="reset-card"><div className="auth-logo auth-logo--center"><span className="auth-logo__mark">N</span></div>{sent ? <><div className="success-icon">✓</div><h1>Check your inbox</h1><p>We sent password reset instructions to <strong>{email}</strong>.</p><Link className="btn-primary auth-button" to="/login">Back to sign in</Link></> : <><p className="eyebrow">ACCOUNT RECOVERY</p><h1>Forgot your password?</h1><p>No worries. Enter your work email and we’ll send you a secure reset link.</p><label><span>Email address</span><input type="email" value={email} onChange={e => setEmail(e.target.value)} placeholder="you@company.com" /></label><button className="btn-primary auth-button" disabled={!/^\S+@\S+\.\S+$/.test(email)} onClick={() => setSent(true)}>Send reset link</button><Link className="back-link" to="/login">← Back to sign in</Link></>}</section></main>;
}
