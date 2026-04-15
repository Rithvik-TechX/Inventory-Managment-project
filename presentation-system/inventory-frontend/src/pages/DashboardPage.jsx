import { useEffect } from 'react';
import { Link } from 'react-router-dom';
import Navbar from '../components/Navbar';
import Sidebar from '../components/Sidebar';
import SummaryCard from '../components/SummaryCard';
import ChartComponent from '../components/ChartComponent';
import LowStockBadge from '../components/LowStockBadge';
import { useProducts } from '../hooks/useProducts';
import { useReports } from '../hooks/useReports';
import { FormatUtils } from '../utilities/FormatUtils';
import '../styles/dashboard.css';

export default function DashboardPage() {
  const { products, loading, lowStockProducts } = useProducts();
  const { data: summary } = useReports('summary');

  const totalValue = products.reduce((acc, p) => acc + p.quantity * p.unitPrice, 0);

  // Category chart data
  const categoryMap = {};
  products.forEach(p => { categoryMap[p.category] = (categoryMap[p.category] || 0) + 1; });
  const chartData = Object.entries(categoryMap).map(([label, value]) => ({ label, value }));

  return (
    <div className="page-layout">
      <Sidebar />
      <div className="main-content">
        <Navbar title="Dashboard" />
        <div className="page-inner fade-up">
          <h1 className="page-title">Overview</h1>
          <p className="page-subtitle">Welcome back — here's your inventory at a glance.</p>

          {/* Summary cards */}
          <div className="summary-grid">
            <SummaryCard
              label="Total Products"
              value={loading ? '…' : FormatUtils.number(products.length)}
              icon="📦"
              iconClass="icon-blue"
              delay={0}
            />
            <SummaryCard
              label="Inventory Value"
              value={loading ? '…' : FormatUtils.currency(totalValue)}
              icon="₹"
              iconClass="icon-green"
              delay={80}
            />
            <SummaryCard
              label="Low Stock Items"
              value={loading ? '…' : lowStockProducts.length}
              icon="⚠"
              iconClass="icon-amber"
              footer={lowStockProducts.length > 0 ? <span className="badge-down">⬇ Needs attention</span> : <span className="badge-up">✓ All good</span>}
              delay={160}
            />
            <SummaryCard
              label="Categories"
              value={loading ? '…' : Object.keys(categoryMap).length}
              icon="🏷"
              iconClass="icon-purple"
              delay={240}
            />
          </div>

          {/* Bottom panels */}
          <div className="dashboard-bottom">
            {/* Category chart */}
            <div className="dashboard-panel">
              <div className="dashboard-panel-title">
                Products by Category
                <Link to="/products">View all →</Link>
              </div>
              <ChartComponent data={chartData} height={200} />
            </div>

            {/* Low stock list */}
            <div className="dashboard-panel">
              <div className="dashboard-panel-title">
                Low Stock Alerts
                <Link to="/reports">Reports →</Link>
              </div>
              {lowStockProducts.length === 0 ? (
                <div style={{ color: 'var(--accent-green)', fontSize: '0.88rem', paddingTop: 8 }}>
                  ✓ All products are sufficiently stocked.
                </div>
              ) : (
                <div style={{ display: 'flex', flexDirection: 'column', gap: 10 }}>
                  {lowStockProducts.slice(0, 6).map(p => (
                    <div key={p.id} style={{
                      display: 'flex', alignItems: 'center', justifyContent: 'space-between',
                      padding: '10px 14px',
                      background: 'var(--bg-secondary)',
                      borderRadius: 'var(--radius-sm)',
                      border: '1px solid var(--border)',
                    }}>
                      <div>
                        <div style={{ fontSize: '0.88rem', fontWeight: 500 }}>{p.name}</div>
                        <div style={{ fontSize: '0.75rem', color: 'var(--text-secondary)' }}>{p.category}</div>
                      </div>
                      <div style={{ display: 'flex', alignItems: 'center', gap: 10 }}>
                        <span style={{ fontFamily: 'DM Mono,monospace', fontSize: '0.85rem' }}>
                          {p.quantity}/{p.reorderLevel}
                        </span>
                        <LowStockBadge quantity={p.quantity} reorderLevel={p.reorderLevel} />
                      </div>
                    </div>
                  ))}
                </div>
              )}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}