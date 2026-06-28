import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import PageLayout from '../components/PageLayout';
import ProductTable from '../components/ProductTable';
import ProductForm from '../components/ProductForm';
import { useProducts } from '../hooks/useProducts';
import { useAuth } from '../hooks/useAuth';
import { useToast } from '../components/Toast';
import '../styles/inventory.css';

export default function ProductsPage() {
  const { products, loading, editProduct, removeProduct } = useProducts();
  const { hasRole } = useAuth();
  const navigate = useNavigate();
  const toast = useToast();

  const [search, setSearch]         = useState('');
  const [editTarget, setEditTarget] = useState(null);
  const [saving, setSaving]         = useState(false);
  const [categoryFilter, setCategoryFilter] = useState('');
  const [sortBy, setSortBy]         = useState('name');

  // Unique categories for filter chips
  const categories = [...new Set(products.map(p => p.category).filter(Boolean))];

  // Filter
  let filtered = products.filter(p =>
    p.name.toLowerCase().includes(search.toLowerCase()) ||
    p.category.toLowerCase().includes(search.toLowerCase()) ||
    (p.supplier || '').toLowerCase().includes(search.toLowerCase())
  );
  if (categoryFilter) {
    filtered = filtered.filter(p => p.category === categoryFilter);
  }

  // Sort
  filtered = [...filtered].sort((a, b) => {
    switch (sortBy) {
      case 'name':      return a.name.localeCompare(b.name);
      case 'quantity':   return b.quantity - a.quantity;
      case 'price':      return b.unitPrice - a.unitPrice;
      case 'value':      return (b.quantity * b.unitPrice) - (a.quantity * a.unitPrice);
      default:           return 0;
    }
  });

  const handleEdit = async (data) => {
    setSaving(true);
    try {
      await editProduct(editTarget.id, data);
      setEditTarget(null);
      toast.success('Product updated successfully');
    } catch (e) {
      toast.error('Update failed: ' + e.message);
    } finally {
      setSaving(false);
    }
  };

  const handleDelete = async (id) => {
    if (!window.confirm('Delete this product? This cannot be undone.')) return;
    try {
      await removeProduct(id);
      toast.success('Product deleted');
    } catch (e) {
      toast.error('Delete failed: ' + e.message);
    }
  };

  return (
    <PageLayout title="Products">
      {/* Edit drawer */}
      {editTarget && (
        <div className="edit-drawer">
          <h2 className="edit-drawer-title">Edit Product</h2>
          <p className="edit-drawer-subtitle">
            Updating: <strong>{editTarget.name}</strong>
          </p>
          <ProductForm
            initial={editTarget}
            onSubmit={handleEdit}
            onCancel={() => setEditTarget(null)}
            loading={saving}
          />
          <hr className="edit-drawer-divider" />
        </div>
      )}

      <h1 className="page-title">Products</h1>
      <p className="page-subtitle">
        Manage your full inventory catalogue.
        {!loading && <span className="page-count">{filtered.length} of {products.length} items</span>}
      </p>

      <div className="inventory-toolbar">
        <div className="search-box">
          <svg className="search-icon" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
            <circle cx="11" cy="11" r="8" />
            <line x1="21" y1="21" x2="16.65" y2="16.65" />
          </svg>
          <input
            type="text"
            placeholder="Search by name, category, supplier…"
            value={search}
            onChange={e => setSearch(e.target.value)}
          />
          {search && (
            <button className="search-clear" onClick={() => setSearch('')}>✕</button>
          )}
        </div>

        <div className="toolbar-actions">
          <select
            className="sort-select"
            value={sortBy}
            onChange={e => setSortBy(e.target.value)}
          >
            <option value="name">Sort: Name</option>
            <option value="quantity">Sort: Quantity</option>
            <option value="price">Sort: Price</option>
            <option value="value">Sort: Value</option>
          </select>

          {hasRole('MANAGER') && (
            <button className="btn btn-primary" onClick={() => navigate('/add-product')}>
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.5" strokeLinecap="round" strokeLinejoin="round">
                <line x1="12" y1="5" x2="12" y2="19" /><line x1="5" y1="12" x2="19" y2="12" />
              </svg>
              Add Product
            </button>
          )}
        </div>
      </div>

      {/* Category filter chips */}
      {categories.length > 0 && (
        <div className="filter-chips">
          <button
            className={`filter-chip ${!categoryFilter ? 'filter-chip--active' : ''}`}
            onClick={() => setCategoryFilter('')}
          >
            All
          </button>
          {categories.map(cat => (
            <button
              key={cat}
              className={`filter-chip ${categoryFilter === cat ? 'filter-chip--active' : ''}`}
              onClick={() => setCategoryFilter(categoryFilter === cat ? '' : cat)}
            >
              {cat}
            </button>
          ))}
        </div>
      )}

      <ProductTable
        products={filtered}
        loading={loading}
        onEdit={setEditTarget}
        onDelete={handleDelete}
      />
    </PageLayout>
  );
}