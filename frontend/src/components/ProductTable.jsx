import LowStockBadge from './LowStockBadge';
import { FormatUtils } from '../utilities/FormatUtils';
import { useAuth } from '../hooks/useAuth';
import { SkeletonTable } from './Skeleton';

export default function ProductTable({ products, onEdit, onDelete, loading, highlightId }) {
  const { hasRole } = useAuth();

  if (loading) {
    return <SkeletonTable rows={6} cols={7} />;
  }

  if (!products || products.length === 0) {
    return (
      <div className="table-wrapper">
        <div className="empty-state">
          <div className="empty-state-icon">
            <svg width="40" height="40" viewBox="0 0 24 24" fill="none" stroke="var(--text-muted)" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round">
              <path d="M21 16V8a2 2 0 0 0-1-1.73l-7-4a2 2 0 0 0-2 0l-7 4A2 2 0 0 0 3 8v8a2 2 0 0 0 1 1.73l7 4a2 2 0 0 0 2 0l7-4A2 2 0 0 0 21 16z" />
              <polyline points="3.27 6.96 12 12.01 20.73 6.96" />
              <line x1="12" y1="22.08" x2="12" y2="12" />
            </svg>
          </div>
          <h3 className="empty-state-title">No products found</h3>
          <p className="empty-state-desc">Add your first product or adjust your search filters.</p>
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
            <tr key={p.id} className={String(p.id) === String(highlightId) ? 'search-highlight' : ''}>
              <td style={{ color: 'var(--text-muted)', fontFamily: 'var(--font-mono)', fontSize: 'var(--text-xs)' }}>
                {String(i + 1).padStart(2, '0')}
              </td>
              <td>
                <div className="product-name-cell">
                  {p.name}
                  {p.supplier && <span>{p.supplier}</span>}
                </div>
              </td>
              <td><span className="category-pill">{p.category}</span></td>
              <td style={{ fontFamily: 'var(--font-mono)', fontWeight: 500 }}>
                {FormatUtils.number(p.quantity)}
              </td>
              <td style={{ fontFamily: 'var(--font-mono)' }}>
                {FormatUtils.currency(p.unitPrice)}
              </td>
              <td style={{ fontFamily: 'var(--font-mono)', color: 'var(--accent-primary)' }}>
                {FormatUtils.currency(p.quantity * p.unitPrice)}
              </td>
              <td>
                <LowStockBadge quantity={p.quantity} maxStock={p.maxStock} lowStock={p.lowStock} />
              </td>
              {hasRole('MANAGER') && (
                <td>
                  <div className="actions-cell">
                    <button className="btn btn-ghost btn-sm" onClick={() => onEdit(p)}>
                      <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                        <path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7" />
                        <path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z" />
                      </svg>
                      Edit
                    </button>
                    {hasRole('ADMIN') && (
                      <button className="btn btn-danger btn-sm" onClick={() => onDelete(p)}>
                        <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                          <polyline points="3 6 5 6 21 6" />
                          <path d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2" />
                        </svg>
                        Delete
                      </button>
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
