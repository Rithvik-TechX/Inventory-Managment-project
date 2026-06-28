import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { api } from '../utilities/ApiUtils';
import '../styles/auth.css';

const steps = ['Personal', 'Company', 'Security', 'Review'];
const initial = { firstName: '', lastName: '', email: '', phone: '', company: '', department: 'Warehouse', password: '', confirm: '' };

export default function RegisterPage() {
  const navigate = useNavigate();
  const [step, setStep] = useState(0);
  const [form, setForm] = useState(initial);
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const set = key => e => setForm(value => ({ ...value, [key]: e.target.value }));

  const next = () => {
    setError('');
    if (step === 0 && (!form.firstName || !form.lastName || !/^\S+@\S+\.\S+$/.test(form.email))) return setError('Add your name and a valid email address.');
    if (step === 1 && !form.company) return setError('Company name is required.');
    if (step === 2 && (form.password.length < 8 || form.password !== form.confirm)) return setError('Use at least 8 characters and make sure the passwords match.');
    setStep(value => Math.min(3, value + 1));
  };

  const submit = async () => {
    setLoading(true); setError('');
    try {
      await api.post('/auth/signup', { username: form.email, email: form.email, password: form.password, role: 'STAFF', name: `${form.firstName} ${form.lastName}` });
      navigate('/login?registered=true', { replace: true });
    } catch (e) { setError(e.message || 'Registration could not be completed.'); }
    finally { setLoading(false); }
  };

  return (
    <main className="auth-page auth-page--center">
      <section className="register-card">
        <Link to="/login" className="auth-logo"><span className="auth-logo__mark">I</span><span>InvenTrack</span></Link>
        <div className="register-heading"><p className="eyebrow">STAFF ONBOARDING</p><h1>Create your workspace account</h1><p>Four quick steps and you’ll be ready to manage inventory.</p></div>
        <div className="staff-notice"><strong>Staff registration only.</strong> Admin and Manager accounts are set up by your system administrator.</div>
        <div className="stepper" aria-label={`Step ${step + 1} of 4`}>
          {steps.map((label, index) => <div key={label} className={index < step ? 'done' : index === step ? 'current' : ''}><span>{index < step ? '✓' : index + 1}</span><small>{label}</small></div>)}
        </div>

        <div className="register-form">
          {step === 0 && <div className="form-grid"><Field label="First name" value={form.firstName} onChange={set('firstName')} /><Field label="Last name" value={form.lastName} onChange={set('lastName')} /><Field wide label="Work email" type="email" value={form.email} onChange={set('email')} /><Field wide label="Phone (optional)" value={form.phone} onChange={set('phone')} /></div>}
          {step === 1 && <div className="form-grid"><Field wide label="Company name" value={form.company} onChange={set('company')} /><Select wide label="Department" value={form.department} onChange={set('department')} options={['Warehouse','Sales','Admin','Finance']} /></div>}
          {step === 2 && <div className="form-grid"><Field wide label="Password" type="password" value={form.password} onChange={set('password')} hint="At least 8 characters" /><Field wide label="Confirm password" type="password" value={form.confirm} onChange={set('confirm')} /></div>}
          {step === 3 && <div className="review-card"><div className="review-avatar">{form.firstName[0]}{form.lastName[0]}</div><h2>{form.firstName} {form.lastName}</h2><p>{form.email}</p><dl><div><dt>Company</dt><dd>{form.company}</dd></div><div><dt>Department</dt><dd>{form.department}</dd></div><div><dt>Role</dt><dd>STAFF</dd></div></dl></div>}
          {error && <div className="auth-alert">{error}</div>}
          <div className="register-actions">{step > 0 && <button className="btn-secondary" onClick={() => setStep(step - 1)}>Back</button>}<button className="btn-primary" onClick={step === 3 ? submit : next} disabled={loading}>{loading ? 'Creating account…' : step === 3 ? 'Create account' : 'Continue'}</button></div>
        </div>
        <p className="register-signin">Already have an account? <Link to="/login">Sign in</Link></p>
      </section>
    </main>
  );
}

function Field({ label, hint, wide, ...props }) { return <label className={wide ? 'field-wide' : ''}><span>{label}</span><input {...props} />{hint && <small>{hint}</small>}</label>; }
function Select({ label, options, wide, ...props }) { return <label className={wide ? 'field-wide' : ''}><span>{label}</span><select {...props}>{options.map(option => <option key={option}>{option}</option>)}</select></label>; }
