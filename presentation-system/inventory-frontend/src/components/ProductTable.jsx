import LowStockBadge from './LowStockBadge';
import { FormatUtils } from '../utilities/FormatUtils';
import { useAuth } from '../hooks/useAuth';

export default function ProductTable({ products, onEdit, onDelete, loading }) {
  const { hasRole } = useAuth();

  if (loading) {
    return (
      <div className="table-wrapper" style={{ padding: 48, textAlign: 'center', color: 'var(--text-secondary)' }}>
        <div style={{ fontSize: '1.5rem', marginBottom: 12 }}>⟳</div>
        Loading products…
      </div>
    );
  }

  if (!products || products.length === 0) {
    return (
      <div className="table-wrapper">
        <div className="empty-state">
          <div className="empty-icon">📦</div>
          <h3>No products found</h3>
          <p>Add your first product or adjust your search filters.</p>
        </div>
      </div>
    );
  }

  return (
    <div className="table-wrapper">
      <table className="data-table">
        <thead>
          <tr>
            <th>#</th>
            <th>Product</th>
            <th>Category</th>
            <th>Quantity</th>
            <th>Unit Price</th>
            <th>Total Value</th>
            <th>Status</th>
            {hasRole('MANAGER') && <th>Actions</th>}
          </tr>
        </thead>
        <tbody>
          {products.map((p, i) => (
            <tr key={p.id}>
              <td style={{ color: 'var(--text-muted)', fontFamily: 'DM Mono,monospace', fontSize: '0.78rem' }}>
                {String(i + 1).padStart(2, '0')}
              </td>
              <td>
                <div className="product-name-cell">
                  {p.name}
                  {p.supplier && <span>{p.supplier}</span>}
                </div>
              </td>
              <td><span className="category-pill">{p.category}</span></td>
              <td style={{ fontFamily: 'DM Mono,monospace', fontWeight: 500 }}>
                {FormatUtils.number(p.quantity)}
              </td>
              <td style={{ fontFamily: 'DM Mono,monospace' }}>
                {FormatUtils.currency(p.unitPrice)}
              </td>
              <td style={{ fontFamily: 'DM Mono,monospace', color: 'var(--accent-primary)' }}>
                {FormatUtils.currency(p.quantity * p.unitPrice)}
              </td>
              <td>
                <LowStockBadge quantity={p.quantity} reorderLevel={p.reorderLevel} />
              </td>
              {hasRole('MANAGER') && (
                <td>
                  <div className="actions-cell">
                    <button className="btn btn-ghost btn-sm" onClick={() => onEdit(p)}>Edit</button>
                    {hasRole('ADMIN') && (
                      <button className="btn btn-danger btn-sm" onClick={() => onDelete(p.id)}>Delete</button>
                    )}
                  </div>
                </td>
              )}
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}