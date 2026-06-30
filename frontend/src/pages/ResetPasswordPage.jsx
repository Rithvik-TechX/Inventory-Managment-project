import { useMemo, useState } from 'react';
import { Link, useNavigate, useSearchParams } from 'react-router-dom';
import { AuthService } from '../services/AuthService';
import '../styles/auth.css';
import '../styles/password-reset.css';
import { Logo } from '../components/ui/Logo';

const passwordScore = password => [password.length >= 8, /[A-Z]/.test(password) && /[a-z]/.test(password), /\d/.test(password), /[^A-Za-z0-9]/.test(password)].filter(Boolean).length;

export default function ResetPasswordPage() {
  const [searchParams] = useSearchParams();
  const navigate = useNavigate();
  const token = searchParams.get('token') || '';
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [loading, setLoading] = useState(false);
  const [success, setSuccess] = useState(false);
  const [error, setError] = useState('');
  const score = useMemo(() => passwordScore(password), [password]);

  const submit = async event => {
    event.preventDefault();
    if (!token) return setError('This reset link is invalid. Request a new one.');
    if (password !== confirmPassword) return setError('Passwords do not match.');
    if (score < 3) return setError('Use at least 8 characters with uppercase, lowercase, and a number.');
    setLoading(true); setError('');
    try {
      await AuthService.resetPassword(token, password);
      setSuccess(true);
      window.setTimeout(() => navigate('/login', { replace: true }), 2000);
    } catch (requestError) { setError(requestError.message); }
    finally { setLoading(false); }
  };

  return (
    <main className="auth-page auth-page--center"><section className="reset-card">
      <div className="auth-logo auth-logo--center"><Logo size={40} textSize={18} /></div>
      {success ? <><div className="success-icon">✓</div><h1>Password updated!</h1><p>Redirecting you to sign in…</p></> : <>
        <p className="eyebrow">ACCOUNT RECOVERY</p><h1>Set a new password</h1><p>Choose a strong password you haven’t used for this account.</p>
        <form onSubmit={submit}><label><span>New password</span><input type="password" autoComplete="new-password" required value={password} onChange={event => setPassword(event.target.value)} /></label><div className="password-strength" aria-label={`Password strength ${score} of 4`}><div>{[0, 1, 2, 3].map(level => <span key={level} className={level < score ? `active score-${score}` : ''} />)}</div><small>{['Enter a password', 'Weak', 'Fair', 'Good', 'Strong'][score]}</small></div><label><span>Confirm password</span><input type="password" autoComplete="new-password" required value={confirmPassword} onChange={event => setConfirmPassword(event.target.value)} /></label>{error && <p className="auth-error" role="alert">{error}</p>}<button className="btn-primary auth-button" disabled={loading}>{loading ? 'Updating…' : 'Reset Password'}</button></form>
        <Link className="back-link" to="/forgot-password">Request a new reset link</Link>
      </>}
    </section></main>
  );
}
