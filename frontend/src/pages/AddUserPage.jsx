import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import PageLayout from '../components/PageLayout';
import { UserService } from '../services/UserService';
import { ValidationUtils } from '../utilities/ValidationUtils';
import { useToast } from '../components/Toast';
import { ROLES } from '../utilities/Constants';
import '../styles/inventory.css';

export default function AddUserPage() {
  const navigate = useNavigate();
  const toast    = useToast();
  const [form,    setForm]    = useState({ username: '', password: '', email: '', role: 'STAFF' });
  const [errors,  setErrors]  = useState({});
  const [loading, setLoading] = useState(false);

  const set = (f) => (e) => setForm(prev => ({ ...prev, [f]: e.target.value }));

  const validate = () => {
    const errs = {};
    if (!form.username.trim()) errs.username = 'Username is required.';
    if (!form.password || form.password.length < 6) errs.password = 'Password must be at least 6 characters.';
    const emailErr = ValidationUtils.email(form.email);
    if (emailErr) errs.email = emailErr;
    if (!form.role) errs.role = 'Role is required.';
    return errs;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const errs = validate();
    if (Object.keys(errs).length) { setErrors(errs); return; }
    setErrors({}); setLoading(true);
    try {
      await UserService.create(form);
      toast.success('User created successfully!');
      setTimeout(() => navigate('/dashboard'), 800);
    } catch (err) {
      toast.error(err.message || 'Failed to create user.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <PageLayout title="Add User">
      <button className="btn btn-ghost btn-sm" style={{ marginBottom: 20 }} onClick={() => navigate('/dashboard')}>
        ← Back to Dashboard
      </button>

      <h1 className="page-title">Add New User</h1>
      <p className="page-subtitle">Create a new account and assign a role.</p>

      <form onSubmit={handleSubmit} className="form-card" noValidate>
        <div className="form-grid-2">
          <div className="form-group">
            <label>Username *</label>
            <input value={form.username} onChange={set('username')} placeholder="e.g. jsmith" />
            {errors.username && <div className="error-msg">{errors.username}</div>}
          </div>

          <div className="form-group">
            <label>Email *</label>
            <input type="email" value={form.email} onChange={set('email')} placeholder="user@company.com" />
            {errors.email && <div className="error-msg">{errors.email}</div>}
          </div>

          <div className="form-group">
            <label>Password *</label>
            <input type="password" value={form.password} onChange={set('password')} placeholder="Min. 6 characters" />
            {errors.password && <div className="error-msg">{errors.password}</div>}
          </div>

          <div className="form-group">
            <label>Role *</label>
            <select value={form.role} onChange={set('role')}>
              {Object.values(ROLES).map(r => <option key={r} value={r}>{r}</option>)}
            </select>
            {errors.role && <div className="error-msg">{errors.role}</div>}
          </div>
        </div>

        {/* Role info hint */}
        <div className="role-info-panel">
          <strong>Role permissions:</strong><br />
          <span className="role-admin">ADMIN</span> — Full access including user management &nbsp;|&nbsp;
          <span className="role-manager">MANAGER</span> — Add / edit / restock products &nbsp;|&nbsp;
          <span className="role-staff">STAFF</span> — View-only access
        </div>

        <div className="form-actions">
          <button type="submit" className="btn btn-primary" disabled={loading}>
            {loading ? 'Creating…' : 'Create User'}
          </button>
          <button type="button" className="btn btn-ghost" onClick={() => navigate('/dashboard')}>
            Cancel
          </button>
        </div>
      </form>
    </PageLayout>
  );
}