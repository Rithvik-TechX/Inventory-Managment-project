import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import Navbar from '../components/Navbar';
import Sidebar from '../components/Sidebar';
import ProductTable from '../components/ProductTable';
import ProductForm from '../components/ProductForm';
import { useProducts } from '../hooks/useProducts';
import { useAuth } from '../hooks/useAuth';
import '../styles/inventory.css';

export default function ProductsPage() {
  const { products, loading, editProduct, removeProduct } = useProducts();
  const { hasRole } = useAuth();
  const navigate    = useNavigate();

  const [search,     setSearch]     = useState('');
  const [editTarget, setEditTarget] = useState(null);
  const [saving,     setSaving]     = useState(false);
  const [deleteId,   setDeleteId]   = useState(null);

  const filtered = products.filter(p =>
    p.name.toLowerCase().includes(search.toLowerCase()) ||
    p.category.toLowerCase().includes(search.toLowerCase()) ||
    (p.supplier || '').toLowerCase().includes(search.toLowerCase())
  );

  const handleEdit = async (data) => {
    setSaving(true);
    try { await editProduct(editTarget.id, data); setEditTarget(null); }
    catch (e) { alert('Update failed: ' + e.message); }
    finally { setSaving(false); }
  };

  const handleDelete = async (id) => {
    if (!window.confirm('Delete this product? This cannot be undone.')) return;
    try { await removeProduct(id); }
    catch (e) { alert('Delete failed: ' + e.message); }
  };

  return (
    <div className="page-layout">
      <Sidebar />
      <div className="main-content">
        <Navbar title="Products" />
        <div className="page-inner fade-up">

          {/* Edit drawer */}
          {editTarget && (
            <div style={{ marginBottom: 28 }}>
              <h2 style={{ marginBottom: 4, fontSize: '1.1rem' }}>Edit Product</h2>
              <p style={{ color: 'var(--text-secondary)', fontSize: '0.85rem', marginBottom: 18 }}>
                Updating: <strong>{editTarget.name}</strong>
              </p>
              <ProductForm
                initial={editTarget}
                onSubmit={handleEdit}
                onCancel={() => setEditTarget(null)}
                loading={saving}
              />
              <hr style={{ margin: '32px 0', borderColor: 'var(--border)' }} />
            </div>
          )}

          <h1 className="page-title">Products</h1>
          <p className="page-subtitle">Manage your full inventory catalogue.</p>

          <div className="inventory-toolbar">
            <div className="search-box">
              <span className="search-icon">🔍</span>
              <input
                type="text"
                placeholder="Search by name, category, supplier…"
                value={search}
                onChange={e => setSearch(e.target.value)}
              />
            </div>
            {hasRole('MANAGER') && (
              <button className="btn btn-primary" onClick={() => navigate('/add-product')}>
                + Add Product
              </button>
            )}
          </div>

          <ProductTable
            products={filtered}
            loading={loading}
            onEdit={setEditTarget}
            onDelete={handleDelete}
          />
        </div>
      </div>
    </div>
  );
}