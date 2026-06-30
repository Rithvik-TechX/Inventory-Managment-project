import { useEffect, useState } from 'react';
import { useSearchParams } from 'react-router-dom';
import PageLayout from '../components/PageLayout';
import Modal from '../components/Modal';
import ConfirmDialog from '../components/ConfirmDialog';
import { useToast } from '../components/Toast';
import { api } from '../utilities/ApiUtils';
import '../styles/admin.css';

export function CategoriesPage() { return <ResourcePage type="category" />; }
export function SuppliersPage() { return <ResourcePage type="supplier" />; }

const configs = {
  category: {
    title: 'Categories', path: '/categories', empty: { name: '', description: '' },
    columns: [['name', 'Name'], ['description', 'Description'], ['productCount', 'Product Count'], ['createdAt', 'Created Date']],
  },
  supplier: {
    title: 'Suppliers', path: '/suppliers',
    empty: { name: '', contactPerson: '', email: '', phone: '', address: '', active: true },
    columns: [['name', 'Name'], ['contactPerson', 'Contact Person'], ['email', 'Email'], ['phone', 'Phone'], ['productCount', 'Products'], ['active', 'Status']],
  },
};

function ResourcePage({ type }) {
  const cfg = configs[type];
  const toast = useToast();
  const [searchParams] = useSearchParams();
  const [rows, setRows] = useState([]);
  const [loading, setLoading] = useState(true);
  const [modal, setModal] = useState(false);
  const [editing, setEditing] = useState(null);
  const [deleteTarget, setDeleteTarget] = useState(null);
  const [deleting, setDeleting] = useState(false);
  const [form, setForm] = useState(cfg.empty);

  useEffect(() => {
    const load = async () => {
      try {
        const data = await api.get(cfg.path);
        let next = Array.isArray(data) ? data : data.content || [];
        if (type === 'category') {
          const productsData = await api.get('/products');
          const products = Array.isArray(productsData) ? productsData : productsData.content || [];
          next = next.map(category => ({
            ...category,
            productCount: products.filter(product =>
              product.category === category.name || product.category?.name === category.name).length,
          }));
        }
        setRows(next);
      } catch (error) {
        toast.error(error.message);
      } finally {
        setLoading(false);
      }
    };
    load();
  }, [cfg.path, toast, type]);

  const open = row => {
    setEditing(row || null);
    setForm(row ? { ...cfg.empty, ...row } : cfg.empty);
    setModal(true);
  };

  const save = async event => {
    event.preventDefault();
    try {
      const payload = type === 'supplier' ? { ...form, phone: form.phoneNumber || form.phone } : form;
      const result = editing
        ? await api.put(`${cfg.path}/${editing.id}`, payload)
        : await api.post(cfg.path, payload);
      setRows(list => editing ? list.map(item => item.id === editing.id ? result : item) : [...list, result]);
      setModal(false);
      toast.success(`${type === 'category' ? 'Category' : 'Supplier'} saved.`);
    } catch (error) {
      toast.error(error.message);
    }
  };

  const remove = async () => {
    setDeleting(true);
    try {
      await api.delete(`${cfg.path}/${deleteTarget.id}`);
      setRows(list => list.filter(item => item.id !== deleteTarget.id));
      setDeleteTarget(null);
      toast.success(`${type === 'category' ? 'Category' : 'Supplier'} deleted.`);
    } catch (error) {
      toast.error(error.message);
    } finally {
      setDeleting(false);
    }
  };

  const display = (row, key) => {
    if (key === 'active') return <span className={`status-chip ${row.active === false ? 'inactive' : ''}`}>{row.active === false ? 'Inactive' : 'Active'}</span>;
    if (key === 'createdAt') return row[key] ? new Date(row[key]).toLocaleDateString('en-IN', { day: 'numeric', month: 'short', year: 'numeric' }) : 'Not recorded';
    return row[key] ?? (key === 'productCount' ? 0 : '—');
  };

  const resourceName = type === 'category' ? 'Category' : 'Supplier';
  const highlightId = searchParams.get('highlight');

  return (
    <PageLayout title={cfg.title}>
      <div className="admin-heading">
        <div><h1>{cfg.title}</h1><p>{type === 'category' ? 'Organize products into clear, searchable groups.' : 'Manage vendor contacts and availability.'}</p></div>
        <button className="btn-primary" onClick={() => open()}>+ Add {resourceName}</button>
      </div>
      <section className="settings-card resource-card">
        <div className="table-scroll">
          <table className={`admin-table ${type === 'category' ? 'categories-table' : ''}`}>
            <thead><tr>{cfg.columns.map(([, label]) => <th key={label}>{label}</th>)}<th>Actions</th></tr></thead>
            <tbody>
              {loading ? <tr><td colSpan={cfg.columns.length + 1}>Loading {cfg.title.toLowerCase()}…</td></tr> : rows.length ? rows.map(row => (
                <tr key={row.id} className={String(row.id) === String(highlightId) ? 'search-highlight' : ''}>
                  {cfg.columns.map(([key]) => <td key={key}>{display(row, key)}</td>)}
                  <td><div className="table-actions"><button className="edit-action" onClick={() => open(row)}>✎ Edit</button><button className="delete-action" onClick={() => setDeleteTarget(row)}>Delete</button></div></td>
                </tr>
              )) : <tr><td colSpan={cfg.columns.length + 1}><div className="proper-empty"><strong>No {cfg.title.toLowerCase()} yet</strong><p>Add your first {type} to get started.</p><button className="btn-primary" onClick={() => open()}>+ Add {resourceName}</button></div></td></tr>}
            </tbody>
          </table>
        </div>
      </section>

      <Modal isOpen={modal} onClose={() => setModal(false)} title={`${editing ? 'Edit' : 'Add'} ${resourceName}`}>
        <form onSubmit={save} className="modal-form">
          {type === 'category' ? <>
            <Field label="Category Name" required value={form.name} onChange={set('name', form, setForm)} />
            <Field label="Description" textarea value={form.description} onChange={set('description', form, setForm)} />
          </> : <>
            <Field label="Supplier Name" required value={form.name} onChange={set('name', form, setForm)} />
            <Field label="Contact Person Name" required value={form.contactPerson} onChange={set('contactPerson', form, setForm)} />
            <Field label="Email" type="email" required value={form.email} onChange={set('email', form, setForm)} />
            <Field label="Phone Number" value={form.phoneNumber || form.phone} onChange={set('phoneNumber', form, setForm)} />
            <Field label="Address" textarea value={form.address} onChange={set('address', form, setForm)} />
            <label className="inline-toggle"><input type="checkbox" checked={form.active !== false} onChange={event => setForm({ ...form, active: event.target.checked })} /> Active supplier</label>
          </>}
          <div className="modal-actions"><button type="button" className="btn-secondary" onClick={() => setModal(false)}>Cancel</button><button className="btn-primary">Save {resourceName}</button></div>
        </form>
      </Modal>

      <ConfirmDialog
        isOpen={Boolean(deleteTarget)}
        title={`Delete ${resourceName.toLowerCase()}`}
        message={deleteTarget ? `Delete “${deleteTarget.name}”? Referenced inventory records will be protected.` : ''}
        onClose={() => setDeleteTarget(null)}
        onConfirm={remove}
        busy={deleting}
      />
    </PageLayout>
  );
}

const set = (key, form, setForm) => event => setForm({ ...form, [key]: event.target.value });
function Field({ label, textarea, ...props }) {
  return <label><span>{label}</span>{textarea ? <textarea rows="3" {...props} /> : <input {...props} />}</label>;
}
