import { Link } from 'react-router-dom';
import PageLayout from '../components/PageLayout';
import SummaryCard from '../components/SummaryCard';
import ChartComponent from '../components/ChartComponent';
import LowStockBadge from '../components/LowStockBadge';
import { useProducts } from '../hooks/useProducts';
import { useReports } from '../hooks/useReports';
import { useAuth } from '../hooks/useAuth';
import { FormatUtils } from '../utilities/FormatUtils';
import '../styles/dashboard.css';

export default function DashboardPage() {
  const { products, loading, lowStockProducts } = useProducts();
  const { data: summary } = useReports('summary');
  const { user } = useAuth();

  const totalValue = products.reduce((acc, p) => acc + p.quantity * p.unitPrice, 0);
  const totalStock = products.reduce((acc, p) => acc + p.quantity, 0);

  // Category chart data
  const categoryMap = {};
  products.forEach(p => { categoryMap[p.category] = (categoryMap[p.category] || 0) + 1; });
  const chartData = Object.entries(categoryMap).map(([label, value]) => ({ label, value }));

  // Greeting based on time
  const hour = new Date().getHours();
  const greeting = hour < 12 ? 'Good morning' : hour < 17 ? 'Good afternoon' : 'Good evening';

  return (
    <PageLayout title="Dashboard">
      {/* Page header */}
      <div className="dashboard-header">
        <div>
          <h1 className="page-title">{greeting}, {user?.username || 'there'}</h1>
          <p className="page-subtitle">Here's your inventory at a glance.</p>
        </div>
        {summary && (
          <div className="dashboard-header-stat">
            <span className="dashboard-header-stat-label">Total Transactions</span>
            <span className="dashboard-header-stat-value">{summary.totalTransactions || 0}</span>
          </div>
        )}
      </div>

      {/* Summary cards */}
      <div className="summary-grid">
        <SummaryCard
          label="Total Products"
          value={loading ? '—' : FormatUtils.number(products.length)}
          icon={
            <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.8" strokeLinecap="round" strokeLinejoin="round">
              <path d="M21 16V8a2 2 0 0 0-1-1.73l-7-4a2 2 0 0 0-2 0l-7 4A2 2 0 0 0 3 8v8a2 2 0 0 0 1 1.73l7 4a2 2 0 0 0 2 0l7-4A2 2 0 0 0 21 16z" />
              <polyline points="3.27 6.96 12 12.01 20.73 6.96" />
              <line x1="12" y1="22.08" x2="12" y2="12" />
            </svg>
          }
          iconClass="icon-blue"
          trend={products.length > 0 ? `${totalStock} units` : null}
          delay={0}
        />
        <SummaryCard
          label="Inventory Value"
          value={loading ? '—' : FormatUtils.currency(totalValue)}
          icon={
            <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.8" strokeLinecap="round" strokeLinejoin="round">
              <line x1="12" y1="1" x2="12" y2="23" />
              <path d="M17 5H9.5a3.5 3.5 0 0 0 0 7h5a3.5 3.5 0 0 1 0 7H6" />
            </svg>
          }
          iconClass="icon-green"
          delay={80}
        />
        <SummaryCard
          label="Low Stock Items"
          value={loading ? '—' : lowStockProducts.length}
          icon={
            <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.8" strokeLinecap="round" strokeLinejoin="round">
              <path d="M10.29 3.86L1.82 18a2 2 0 0 0 1.71 3h16.94a2 2 0 0 0 1.71-3L13.71 3.86a2 2 0 0 0-3.42 0z" />
              <line x1="12" y1="9" x2="12" y2="13" />
              <line x1="12" y1="17" x2="12.01" y2="17" />
            </svg>
          }
          iconClass="icon-amber"
          footer={lowStockProducts.length > 0
            ? <span className="badge-warning">⬇ Needs attention</span>
            : <span className="badge-success">✓ All good</span>
          }
          delay={160}
        />
        <SummaryCard
          label="Categories"
          value={loading ? '—' : Object.keys(categoryMap).length}
          icon={
            <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.8" strokeLinecap="round" strokeLinejoin="round">
              <path d="M20.59 13.41l-7.17 7.17a2 2 0 0 1-2.83 0L2 12V2h10l8.59 8.59a2 2 0 0 1 0 2.82z" />
              <line x1="7" y1="7" x2="7.01" y2="7" />
            </svg>
          }
          iconClass="icon-purple"
          delay={240}
        />
      </div>

      {/* Bottom panels */}
      <div className="dashboard-bottom">
        {/* Category chart */}
        <div className="dashboard-panel">
          <div className="dashboard-panel-title">
            <span>Products by Category</span>
            <Link to="/app/categories" className="panel-link">View all →</Link>
          </div>
          <ChartComponent data={chartData} height={260} />
        </div>

        {/* Low stock list */}
        <div className="dashboard-panel">
          <div className="dashboard-panel-title">
            <span>Low Stock Alerts</span>
            <Link to="/reports" className="panel-link">Reports →</Link>
          </div>
          {lowStockProducts.length === 0 ? (
            <div className="dashboard-empty-state">
              <svg width="32" height="32" viewBox="0 0 24 24" fill="none" stroke="var(--accent-green)" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round">
                <path d="M22 11.08V12a10 10 0 1 1-5.93-9.14" />
                <polyline points="22 4 12 14.01 9 11.01" />
              </svg>
              <p>All products are sufficiently stocked.</p>
            </div>
          ) : (
            <div className="low-stock-list">
              {lowStockProducts.slice(0, 6).map(p => (
                <div key={p.id} className="low-stock-item">
                  <div className="low-stock-item-info">
                    <div className="low-stock-item-name">{p.name}</div>
                    <div className="low-stock-item-category">{p.category}</div>
                  </div>
                  <div className="low-stock-item-stats">
                    <span className="low-stock-item-qty">
                      {p.quantity}/{p.maxStock}
                    </span>
                    <LowStockBadge quantity={p.quantity} maxStock={p.maxStock} lowStock={p.lowStock} />
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>
      </div>

      {/* Transaction stats (if available) */}
      {summary && (
        <div className="dashboard-stats-bar">
          <div className="dashboard-stat-item">
            <span className="dashboard-stat-value text-green">{summary.salesTransactions || 0}</span>
            <span className="dashboard-stat-label">Sales</span>
          </div>
          <div className="dashboard-stat-divider" />
          <div className="dashboard-stat-item">
            <span className="dashboard-stat-value text-blue">{summary.purchaseTransactions || 0}</span>
            <span className="dashboard-stat-label">Purchases</span>
          </div>
          <div className="dashboard-stat-divider" />
          <div className="dashboard-stat-item">
            <span className="dashboard-stat-value text-amber">{summary.pendingTransactions || 0}</span>
            <span className="dashboard-stat-label">Pending</span>
          </div>
          <div className="dashboard-stat-divider" />
          <div className="dashboard-stat-item">
            <span className="dashboard-stat-value">{summary.totalUsers || 0}</span>
            <span className="dashboard-stat-label">Users</span>
          </div>
        </div>
      )}
    </PageLayout>
  );
}
