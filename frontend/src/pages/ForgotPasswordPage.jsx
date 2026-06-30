import { useState } from 'react';
import { Link } from 'react-router-dom';
import { AuthService } from '../services/AuthService';
import '../styles/auth.css';
import '../styles/password-reset.css';
import { Logo } from '../components/ui/Logo';

export default function ForgotPasswordPage() {
  const [email, setEmail] = useState('');
  const [sent, setSent] = useState(false);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const submit = async event => {
    event.preventDefault();
    setLoading(true);
    setError('');
    try {
      await AuthService.forgotPassword(email);
      setSent(true);
    } catch (requestError) {
      setError(requestError.message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <main className="auth-page auth-page--center"><section className="reset-card">
      <div className="auth-logo auth-logo--center"><Logo size={40} textSize={18} /></div>
      {sent ? <><div className="success-icon">✓</div><h1>Check your inbox</h1><p>If an account exists for <strong>{email}</strong>, reset instructions are on the way.</p><Link className="btn-primary auth-button" to="/login">Back to sign in</Link></> : <>
        <p className="eyebrow">ACCOUNT RECOVERY</p><h1>Forgot your password?</h1><p>Enter your work email and we’ll send you a secure reset link.</p>
        <form onSubmit={submit}><label><span>Email address</span><input type="email" required value={email} onChange={event => setEmail(event.target.value)} placeholder="you@company.com" /></label>{error && <p className="auth-error" role="alert">{error}</p>}<button className="btn-primary auth-button" disabled={loading || !/^\S+@\S+\.\S+$/.test(email)}>{loading ? 'Sending…' : 'Send reset link'}</button></form>
        <Link className="back-link" to="/login">← Back to sign in</Link>
      </>}
    </section></main>
  );
}
