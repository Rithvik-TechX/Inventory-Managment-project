import { useState } from 'react';
import { useSearchParams } from 'react-router-dom';
import PageLayout from '../components/PageLayout';
import ProductTable from '../components/ProductTable';
import ProductForm from '../components/ProductForm';
import Modal from '../components/Modal';
import ConfirmDialog from '../components/ConfirmDialog';
import { useProducts } from '../hooks/useProducts';
import { useAuth } from '../hooks/useAuth';
import { useToast } from '../components/Toast';
import '../styles/inventory.css';

export default function ProductsPage() {
  const { products, loading, addProduct, editProduct, removeProduct } = useProducts();
  const { hasRole } = useAuth();
  const [searchParams] = useSearchParams();
  const toast = useToast();

  const [search, setSearch]         = useState('');
  const [editTarget, setEditTarget] = useState(null);
  const [creating, setCreating] = useState(false);
  const [deleteTarget, setDeleteTarget] = useState(null);
  const [saving, setSaving]         = useState(false);
  const [categoryFilter, setCategoryFilter] = useState('');
  const [sortBy, setSortBy]         = useState('name');
  const [statusFilter, setStatusFilter] = useState('');
  const [page, setPage] = useState(1);
  const [rowsPerPage, setRowsPerPage] = useState(10);

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
  if (statusFilter === 'low') filtered = filtered.filter(p => p.quantity > 0 && p.quantity <= p.reorderLevel);
  if (statusFilter === 'in') filtered = filtered.filter(p => p.quantity > p.reorderLevel);
  if (statusFilter === 'out') filtered = filtered.filter(p => p.quantity === 0);

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
  const pageCount = Math.max(1, Math.ceil(filtered.length / rowsPerPage));
  const currentPage = Math.min(page, pageCount);
  const visibleProducts = filtered.slice((currentPage - 1) * rowsPerPage, currentPage * rowsPerPage);

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

  const handleAdd = async (data) => {
    setSaving(true);
    try {
      await addProduct(data);
      setCreating(false);
      toast.success('Product added successfully');
    } catch (e) {
      toast.error('Create failed: ' + e.message);
    } finally {
      setSaving(false);
    }
  };

  const handleDelete = async () => {
    try {
      await removeProduct(deleteTarget.id);
      setDeleteTarget(null);
      toast.success('Product deleted');
    } catch (e) {
      toast.error('Delete failed: ' + e.message);
    }
  };

  return (
    <PageLayout title="Products">
      <Modal isOpen={Boolean(editTarget)} onClose={() => setEditTarget(null)} title="Edit Product" size="lg">
        {editTarget && (
          <ProductForm
            initial={editTarget}
            onSubmit={handleEdit}
            onCancel={() => setEditTarget(null)}
            loading={saving}
          />
        )}
      </Modal>

      <Modal isOpen={creating} onClose={() => setCreating(false)} title="Add Product" size="lg">
        <ProductForm onSubmit={handleAdd} onCancel={() => setCreating(false)} loading={saving} />
      </Modal>

      <ConfirmDialog
        isOpen={Boolean(deleteTarget)}
        title="Delete product"
        message={deleteTarget ? `Delete “${deleteTarget.name}”? This cannot be undone.` : ''}
        onClose={() => setDeleteTarget(null)}
        onConfirm={handleDelete}
      />

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
          <select className="sort-select" value={statusFilter} onChange={e => { setStatusFilter(e.target.value); setPage(1); }}>
            <option value="">All statuses</option><option value="in">In Stock</option><option value="low">Low Stock</option><option value="out">Out of Stock</option>
          </select>

          {hasRole('MANAGER') && (
            <button className="btn btn-primary" onClick={() => setCreating(true)}>
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
        products={visibleProducts}
        loading={loading}
        onEdit={setEditTarget}
        onDelete={product => setDeleteTarget(product)}
        highlightId={searchParams.get('highlight')}
      />
      {!loading && filtered.length > 0 && <div className="pagination-bar"><button className="btn btn-secondary btn-sm" disabled={currentPage<=1} onClick={()=>setPage(p=>p-1)}>Previous</button><span>Page {currentPage} of {pageCount}</span><button className="btn btn-secondary btn-sm" disabled={currentPage>=pageCount} onClick={()=>setPage(p=>p+1)}>Next</button><label>Rows per page <select value={rowsPerPage} onChange={e=>{setRowsPerPage(Number(e.target.value));setPage(1)}}><option>10</option><option>25</option><option>50</option></select></label></div>}
    </PageLayout>
  );
}
