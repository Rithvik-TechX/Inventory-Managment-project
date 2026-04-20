import { useState, useRef } from 'react';
import {
  BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer,
  PieChart, Pie, Cell, Legend,
  AreaChart, Area,
  RadarChart, Radar, PolarGrid, PolarAngleAxis, PolarRadiusAxis,
} from 'recharts';
import Navbar from '../components/Navbar';
import Sidebar from '../components/Sidebar';
import LowStockBadge from '../components/LowStockBadge';
import { useProducts } from '../hooks/useProducts';
import { useReports } from '../hooks/useReports';
import { FormatUtils } from '../utilities/FormatUtils';
import '../styles/inventory.css';
import '../styles/dashboard.css';
import '../styles/reports.css';

const TABS = ['Summary', 'Low Stock', 'Value Report', 'Category Breakdown'];

const CHART_COLORS = ['#4f8eff', '#a78bfa', '#22c55e', '#f59e0b', '#ef4444', '#06b6d4', '#ec4899', '#8b5cf6'];

const CustomTooltip = ({ active, payload, label, formatter }) => {
  if (!active || !payload?.length) return null;
  return (
    <div className="chart-tooltip">
      <p className="chart-tooltip-label">{label}</p>
      {payload.map((p, i) => (
        <p key={i} className="chart-tooltip-value" style={{ color: p.color }}>
          {p.name}: {formatter ? formatter(p.value) : p.value}
        </p>
      ))}
    </div>
  );
};

export default function ReportsPage() {
  const { products, loading, lowStockProducts } = useProducts();
  const { data: summary } = useReports('summary');
  const [activeTab, setActiveTab] = useState(0);
  const reportRef = useRef(null);

  const totalValue = products.reduce((acc, p) => acc + p.quantity * p.unitPrice, 0);
  const totalStock = products.reduce((acc, p) => acc + p.quantity, 0);
  const totalMaxStock = products.reduce((acc, p) => acc + (p.maxStock || 100), 0);
  const overallHealth = totalMaxStock > 0 ? Math.round((totalStock / totalMaxStock) * 100) : 0;

  // Category breakdown
  const categoryMap = {};
  products.forEach(p => {
    if (!categoryMap[p.category]) categoryMap[p.category] = { count: 0, value: 0, stock: 0, maxStock: 0 };
    categoryMap[p.category].count += 1;
    categoryMap[p.category].value += p.quantity * p.unitPrice;
    categoryMap[p.category].stock += p.quantity;
    categoryMap[p.category].maxStock += (p.maxStock || 100);
  });

  // Chart data
  const categoryBarData = Object.entries(categoryMap).map(([name, d]) => ({
    name, count: d.count, value: d.value, stock: d.stock,
  }));

  const categoryPieData = Object.entries(categoryMap).map(([name, d]) => ({
    name, value: d.value,
  }));

  const valueRanked = [...products]
    .sort((a, b) => (b.quantity * b.unitPrice) - (a.quantity * a.unitPrice))
    .slice(0, 10)
    .map(p => ({ name: p.name.length > 15 ? p.name.slice(0, 15) + '…' : p.name, value: p.quantity * p.unitPrice }));

  const stockAreaData = [...products]
    .sort((a, b) => a.name.localeCompare(b.name))
    .map(p => ({
      name: p.name.length > 12 ? p.name.slice(0, 12) + '…' : p.name,
      current: p.quantity,
      max: p.maxStock || 100,
    }));

  const radarData = Object.entries(categoryMap).map(([cat, d]) => ({
    category: cat,
    products: d.count,
    stockLevel: d.maxStock > 0 ? Math.round((d.stock / d.maxStock) * 100) : 0,
    value: Math.round(d.value / 1000),
  }));

  const handlePrint = () => window.print();

  const now = new Date();
  const reportDate = now.toLocaleDateString('en-IN', { day: 'numeric', month: 'long', year: 'numeric' });
  const reportTime = now.toLocaleTimeString('en-IN', { hour: '2-digit', minute: '2-digit' });

  return (
    <div className="page-layout">
      <Sidebar />
      <div className="main-content">
        <Navbar title="Reports" />
        <div className="page-inner fade-up" ref={reportRef}>
          <div className="report-header">
            <div>
              <h1 className="page-title">Inventory Reports</h1>
              <p className="page-subtitle">
                Real-time analytics • Generated on {reportDate} at {reportTime}
              </p>
            </div>
            <button className="btn btn-primary btn-sm" onClick={handlePrint}>
              🖨️ Print Report
            </button>
          </div>

          {/* Tab bar */}
          <div className="report-tabs">
            {TABS.map((t, i) => (
              <button
                key={t}
                className={`report-tab ${activeTab === i ? 'active' : ''}`}
                onClick={() => setActiveTab(i)}
              >
                {t}
              </button>
            ))}
          </div>

          {/* ── Summary ── */}
          {activeTab === 0 && (
            <div className="report-section">
              {/* KPI Cards */}
              <div className="summary-grid">
                {[
                  { label: 'Total SKUs', value: products.length, icon: '📦', cls: 'icon-blue' },
                  { label: 'Inventory Value', value: FormatUtils.currency(totalValue), icon: '₹', cls: 'icon-green' },
                  { label: 'Low Stock Items', value: lowStockProducts.length, icon: '⚠', cls: 'icon-amber' },
                  { label: 'Categories', value: Object.keys(categoryMap).length, icon: '🏷', cls: 'icon-purple' },
                ].map((c, i) => (
                  <div key={i} className="summary-card">
                    <div className="summary-card-header">
                      <span className="summary-card-label">{c.label}</span>
                      <div className={`summary-card-icon ${c.cls}`}>{c.icon}</div>
                    </div>
                    <div className="summary-card-value">{loading ? '…' : c.value}</div>
                  </div>
                ))}
              </div>

              {/* Inventory Health */}
              <div className="report-panel">
                <div className="report-panel-title">Inventory Health</div>
                <div className="health-meter">
                  <div className="health-bar-track">
                    <div
                      className={`health-bar-fill ${overallHealth < 40 ? 'critical' : overallHealth < 60 ? 'warning' : 'good'}`}
                      style={{ width: `${overallHealth}%` }}
                    />
                  </div>
                  <div className="health-stats">
                    <span className="health-pct">{overallHealth}%</span>
                    <span className="health-label">
                      Overall Stock Level ({FormatUtils.number(totalStock)} / {FormatUtils.number(totalMaxStock)} units)
                    </span>
                  </div>
                </div>
              </div>

              {/* Charts Row */}
              <div className="charts-row">
                {/* Bar Chart — Products per Category */}
                <div className="report-panel chart-panel">
                  <div className="report-panel-title">Products by Category</div>
                  <div className="chart-container">
                    <ResponsiveContainer width="100%" height={300}>
                      <BarChart data={categoryBarData} margin={{ top: 10, right: 20, left: 0, bottom: 5 }}>
                        <CartesianGrid strokeDasharray="3 3" stroke="#1e2a3d" />
                        <XAxis dataKey="name" tick={{ fill: '#8892a4', fontSize: 12 }} axisLine={{ stroke: '#1e2a3d' }} />
                        <YAxis tick={{ fill: '#8892a4', fontSize: 12 }} axisLine={{ stroke: '#1e2a3d' }} allowDecimals={false} />
                        <Tooltip content={<CustomTooltip />} />
                        <Bar dataKey="count" name="Products" radius={[6, 6, 0, 0]} maxBarSize={50}>
                          {categoryBarData.map((_, i) => (
                            <Cell key={i} fill={CHART_COLORS[i % CHART_COLORS.length]} />
                          ))}
                        </Bar>
                      </BarChart>
                    </ResponsiveContainer>
                  </div>
                </div>

                {/* Pie Chart — Value Distribution */}
                <div className="report-panel chart-panel">
                  <div className="report-panel-title">Value Distribution</div>
                  <div className="chart-container">
                    <ResponsiveContainer width="100%" height={300}>
                      <PieChart>
                        <Pie
                          data={categoryPieData}
                          cx="50%" cy="50%"
                          innerRadius={60} outerRadius={100}
                          paddingAngle={4}
                          dataKey="value"
                          stroke="none"
                        >
                          {categoryPieData.map((_, i) => (
                            <Cell key={i} fill={CHART_COLORS[i % CHART_COLORS.length]} />
                          ))}
                        </Pie>
                        <Tooltip formatter={(v) => FormatUtils.currency(v)} />
                        <Legend
                          verticalAlign="bottom"
                          iconType="circle"
                          iconSize={8}
                          formatter={(v) => <span style={{ color: '#8892a4', fontSize: '0.78rem' }}>{v}</span>}
                        />
                      </PieChart>
                    </ResponsiveContainer>
                  </div>
                </div>
              </div>

              {/* Transaction Stats */}
              {summary && (
                <div className="report-panel">
                  <div className="report-panel-title">Transaction Overview</div>
                  <div className="stats-grid">
                    <div className="stat-item">
                      <span className="stat-number">{summary.totalTransactions || 0}</span>
                      <span className="stat-desc">Total Transactions</span>
                    </div>
                    <div className="stat-item">
                      <span className="stat-number text-green">{summary.salesTransactions || 0}</span>
                      <span className="stat-desc">Sales</span>
                    </div>
                    <div className="stat-item">
                      <span className="stat-number text-blue">{summary.purchaseTransactions || 0}</span>
                      <span className="stat-desc">Purchases</span>
                    </div>
                    <div className="stat-item">
                      <span className="stat-number text-amber">{summary.pendingTransactions || 0}</span>
                      <span className="stat-desc">Pending</span>
                    </div>
                  </div>
                </div>
              )}
            </div>
          )}

          {/* ── Low Stock ── */}
          {activeTab === 1 && (
            <div className="report-section">
              {/* Low Stock Area Chart */}
              <div className="report-panel">
                <div className="report-panel-title">
                  Stock Levels Overview
                  <span className="report-count">{lowStockProducts.length} items below 50% capacity</span>
                </div>
                <div className="chart-container">
                  <ResponsiveContainer width="100%" height={300}>
                    <AreaChart data={stockAreaData} margin={{ top: 10, right: 20, left: 0, bottom: 5 }}>
                      <defs>
                        <linearGradient id="gradMax" x1="0" y1="0" x2="0" y2="1">
                          <stop offset="5%" stopColor="#4f8eff" stopOpacity={0.15} />
                          <stop offset="95%" stopColor="#4f8eff" stopOpacity={0} />
                        </linearGradient>
                        <linearGradient id="gradCurrent" x1="0" y1="0" x2="0" y2="1">
                          <stop offset="5%" stopColor="#22c55e" stopOpacity={0.3} />
                          <stop offset="95%" stopColor="#22c55e" stopOpacity={0} />
                        </linearGradient>
                      </defs>
                      <CartesianGrid strokeDasharray="3 3" stroke="#1e2a3d" />
                      <XAxis dataKey="name" tick={{ fill: '#8892a4', fontSize: 11 }} axisLine={{ stroke: '#1e2a3d' }} interval={0} angle={-25} textAnchor="end" height={60} />
                      <YAxis tick={{ fill: '#8892a4', fontSize: 12 }} axisLine={{ stroke: '#1e2a3d' }} />
                      <Tooltip content={<CustomTooltip />} />
                      <Area type="monotone" dataKey="max" name="Max Capacity" stroke="#4f8eff" fill="url(#gradMax)" strokeWidth={2} />
                      <Area type="monotone" dataKey="current" name="Current Stock" stroke="#22c55e" fill="url(#gradCurrent)" strokeWidth={2} />
                    </AreaChart>
                  </ResponsiveContainer>
                </div>
              </div>

              {/* Low Stock Table */}
              <div className="report-panel">
                <div className="report-panel-title">
                  Low Stock Alert Report
                </div>
                {lowStockProducts.length === 0 ? (
                  <div className="empty-state">
                    <div className="empty-icon">✅</div>
                    <h3>All products are sufficiently stocked</h3>
                    <p>No items are currently below 50% of their maximum stock level.</p>
                  </div>
                ) : (
                  <div className="table-wrapper">
                    <table className="data-table">
                      <thead>
                        <tr>
                          <th>Product</th><th>SKU</th><th>Category</th>
                          <th>Current Stock</th><th>Max Stock</th>
                          <th>Stock Level</th><th>Supplier</th><th>Status</th>
                        </tr>
                      </thead>
                      <tbody>
                        {lowStockProducts.map(p => {
                          const pct = p.maxStock > 0 ? Math.round((p.quantity / p.maxStock) * 100) : 0;
                          return (
                            <tr key={p.id}>
                              <td style={{ fontWeight: 500 }}>{p.name}</td>
                              <td><code className="sku-code">{p.sku}</code></td>
                              <td><span className="category-pill">{p.category}</span></td>
                              <td className="mono-cell text-red">{p.quantity}</td>
                              <td className="mono-cell">{p.maxStock}</td>
                              <td>
                                <div className="mini-bar-track">
                                  <div className={`mini-bar-fill ${pct < 25 ? 'critical' : 'warning'}`} style={{ width: `${pct}%` }} />
                                </div>
                                <span className="mini-bar-pct">{pct}%</span>
                              </td>
                              <td style={{ color: 'var(--text-secondary)' }}>{p.supplier || '—'}</td>
                              <td><LowStockBadge quantity={p.quantity} maxStock={p.maxStock} lowStock={p.lowStock} /></td>
                            </tr>
                          );
                        })}
                      </tbody>
                    </table>
                  </div>
                )}
              </div>
            </div>
          )}

          {/* ── Value Report ── */}
          {activeTab === 2 && (
            <div className="report-section">
              {/* Top 10 Bar Chart */}
              <div className="report-panel">
                <div className="report-panel-title">Top 10 Products by Value</div>
                <div className="chart-container">
                  <ResponsiveContainer width="100%" height={350}>
                    <BarChart data={valueRanked} layout="vertical" margin={{ top: 5, right: 40, left: 10, bottom: 5 }}>
                      <CartesianGrid strokeDasharray="3 3" stroke="#1e2a3d" horizontal={false} />
                      <XAxis type="number" tick={{ fill: '#8892a4', fontSize: 12 }} axisLine={{ stroke: '#1e2a3d' }} tickFormatter={(v) => `₹${(v / 1000).toFixed(0)}K`} />
                      <YAxis type="category" dataKey="name" width={130} tick={{ fill: '#8892a4', fontSize: 11 }} axisLine={{ stroke: '#1e2a3d' }} />
                      <Tooltip formatter={(v) => FormatUtils.currency(v)} />
                      <Bar dataKey="value" name="Total Value" radius={[0, 6, 6, 0]} maxBarSize={28}>
                        {valueRanked.map((_, i) => (
                          <Cell key={i} fill={`hsl(${250 + i * 12}, 70%, ${55 + i * 2}%)`} />
                        ))}
                      </Bar>
                    </BarChart>
                  </ResponsiveContainer>
                </div>
              </div>

              {/* Full table */}
              <div className="report-panel">
                <div className="report-panel-title">Complete Inventory Valuation</div>
                <div className="table-wrapper">
                  <table className="data-table">
                    <thead>
                      <tr><th>#</th><th>Product</th><th>SKU</th><th>Category</th><th>Qty</th><th>Unit Price</th><th>Total Value</th><th>% of Total</th></tr>
                    </thead>
                    <tbody>
                      {[...products].sort((a, b) => (b.quantity * b.unitPrice) - (a.quantity * a.unitPrice)).map((p, i) => {
                        const val = p.quantity * p.unitPrice;
                        const pctOfTotal = totalValue > 0 ? ((val / totalValue) * 100).toFixed(1) : 0;
                        return (
                          <tr key={p.id}>
                            <td className="mono-cell">{i + 1}</td>
                            <td style={{ fontWeight: 500 }}>{p.name}</td>
                            <td><code className="sku-code">{p.sku}</code></td>
                            <td><span className="category-pill">{p.category}</span></td>
                            <td className="mono-cell">{p.quantity}</td>
                            <td className="mono-cell">{FormatUtils.currency(p.unitPrice)}</td>
                            <td className="mono-cell text-blue" style={{ fontWeight: 600 }}>{FormatUtils.currency(val)}</td>
                            <td className="mono-cell">{pctOfTotal}%</td>
                          </tr>
                        );
                      })}
                    </tbody>
                    <tfoot>
                      <tr>
                        <td colSpan={4} style={{ fontWeight: 600 }}>Total Inventory Value</td>
                        <td className="mono-cell" style={{ fontWeight: 600 }}>{totalStock}</td>
                        <td></td>
                        <td className="mono-cell text-green" style={{ fontWeight: 700, fontSize: '1rem' }}>{FormatUtils.currency(totalValue)}</td>
                        <td className="mono-cell" style={{ fontWeight: 600 }}>100%</td>
                      </tr>
                    </tfoot>
                  </table>
                </div>
              </div>
            </div>
          )}

          {/* ── Category Breakdown ── */}
          {activeTab === 3 && (
            <div className="report-section">
              {/* Charts Row */}
              <div className="charts-row">
                {/* Category Value Bar Chart */}
                <div className="report-panel chart-panel">
                  <div className="report-panel-title">Category Value Comparison</div>
                  <div className="chart-container">
                    <ResponsiveContainer width="100%" height={300}>
                      <BarChart data={categoryBarData} margin={{ top: 10, right: 20, left: 0, bottom: 5 }}>
                        <CartesianGrid strokeDasharray="3 3" stroke="#1e2a3d" />
                        <XAxis dataKey="name" tick={{ fill: '#8892a4', fontSize: 12 }} axisLine={{ stroke: '#1e2a3d' }} />
                        <YAxis tick={{ fill: '#8892a4', fontSize: 12 }} axisLine={{ stroke: '#1e2a3d' }} tickFormatter={(v) => `₹${(v / 1000).toFixed(0)}K`} />
                        <Tooltip formatter={(v) => FormatUtils.currency(v)} />
                        <Bar dataKey="value" name="Value" radius={[6, 6, 0, 0]} maxBarSize={50}>
                          {categoryBarData.map((_, i) => (
                            <Cell key={i} fill={CHART_COLORS[i % CHART_COLORS.length]} />
                          ))}
                        </Bar>
                      </BarChart>
                    </ResponsiveContainer>
                  </div>
                </div>

                {/* Radar Chart */}
                <div className="report-panel chart-panel">
                  <div className="report-panel-title">Category Radar Analysis</div>
                  <div className="chart-container">
                    <ResponsiveContainer width="100%" height={300}>
                      <RadarChart data={radarData} cx="50%" cy="50%" outerRadius="70%">
                        <PolarGrid stroke="#1e2a3d" />
                        <PolarAngleAxis dataKey="category" tick={{ fill: '#8892a4', fontSize: 11 }} />
                        <PolarRadiusAxis tick={{ fill: '#4a5568', fontSize: 10 }} />
                        <Radar name="Products" dataKey="products" stroke="#4f8eff" fill="#4f8eff" fillOpacity={0.25} strokeWidth={2} />
                        <Radar name="Stock %" dataKey="stockLevel" stroke="#22c55e" fill="#22c55e" fillOpacity={0.15} strokeWidth={2} />
                        <Legend iconType="circle" iconSize={8} formatter={(v) => <span style={{ color: '#8892a4', fontSize: '0.78rem' }}>{v}</span>} />
                        <Tooltip />
                      </RadarChart>
                    </ResponsiveContainer>
                  </div>
                </div>
              </div>

              {/* Donut Chart for stock composition */}
              <div className="report-panel">
                <div className="report-panel-title">Stock Composition by Category</div>
                <div className="chart-container" style={{ maxWidth: 500, margin: '0 auto' }}>
                  <ResponsiveContainer width="100%" height={320}>
                    <PieChart>
                      <Pie
                        data={categoryBarData.map(d => ({ name: d.name, value: d.stock }))}
                        cx="50%" cy="50%"
                        innerRadius={70} outerRadius={110}
                        paddingAngle={3}
                        dataKey="value"
                        stroke="none"
                        label={({ name, percent }) => `${name} ${(percent * 100).toFixed(0)}%`}
                      >
                        {categoryBarData.map((_, i) => (
                          <Cell key={i} fill={CHART_COLORS[i % CHART_COLORS.length]} />
                        ))}
                      </Pie>
                      <Tooltip formatter={(v) => FormatUtils.number(v) + ' units'} />
                    </PieChart>
                  </ResponsiveContainer>
                </div>
              </div>

              {/* Category cards */}
              <div className="category-grid">
                {Object.entries(categoryMap).map(([cat, data], idx) => {
                  const healthPct = data.maxStock > 0 ? Math.round((data.stock / data.maxStock) * 100) : 0;
                  const lowInCat = products.filter(p => p.category === cat && (p.lowStock || p.quantity < p.maxStock * 0.5)).length;
                  return (
                    <div key={cat} className="category-card" style={{ borderTop: `3px solid ${CHART_COLORS[idx % CHART_COLORS.length]}` }}>
                      <div className="category-card-header">
                        <h3>{cat}</h3>
                        <span className="category-card-count">{data.count} items</span>
                      </div>
                      <div className="category-card-stats">
                        <div className="category-stat">
                          <span className="category-stat-label">Value</span>
                          <span className="category-stat-value">{FormatUtils.currency(data.value)}</span>
                        </div>
                        <div className="category-stat">
                          <span className="category-stat-label">Stock Level</span>
                          <span className="category-stat-value">{healthPct}%</span>
                        </div>
                        <div className="category-stat">
                          <span className="category-stat-label">Low Stock</span>
                          <span className={`category-stat-value ${lowInCat > 0 ? 'text-red' : 'text-green'}`}>
                            {lowInCat > 0 ? `${lowInCat} items` : 'None'}
                          </span>
                        </div>
                      </div>
                      <div className="mini-bar-track" style={{ marginTop: 10 }}>
                        <div className={`mini-bar-fill ${healthPct < 40 ? 'critical' : healthPct < 60 ? 'warning' : 'good'}`} style={{ width: `${healthPct}%` }} />
                      </div>
                    </div>
                  );
                })}
              </div>

              {/* Full category table */}
              <div className="report-panel">
                <div className="report-panel-title">Category Summary Table</div>
                <div className="table-wrapper">
                  <table className="data-table">
                    <thead>
                      <tr><th>Category</th><th>Products</th><th>Total Stock</th><th>Max Capacity</th><th>Stock Level</th><th>Total Value</th></tr>
                    </thead>
                    <tbody>
                      {Object.entries(categoryMap)
                        .sort(([, a], [, b]) => b.value - a.value)
                        .map(([cat, data]) => {
                          const pct = data.maxStock > 0 ? Math.round((data.stock / data.maxStock) * 100) : 0;
                          return (
                            <tr key={cat}>
                              <td style={{ fontWeight: 500 }}>{cat}</td>
                              <td className="mono-cell">{data.count}</td>
                              <td className="mono-cell">{FormatUtils.number(data.stock)}</td>
                              <td className="mono-cell">{FormatUtils.number(data.maxStock)}</td>
                              <td>
                                <div className="mini-bar-track" style={{ width: 80, display: 'inline-block', verticalAlign: 'middle' }}>
                                  <div className={`mini-bar-fill ${pct < 40 ? 'critical' : pct < 60 ? 'warning' : 'good'}`} style={{ width: `${pct}%` }} />
                                </div>
                                <span className="mini-bar-pct" style={{ marginLeft: 8 }}>{pct}%</span>
                              </td>
                              <td className="mono-cell text-blue" style={{ fontWeight: 600 }}>{FormatUtils.currency(data.value)}</td>
                            </tr>
                          );
                        })}
                    </tbody>
                  </table>
                </div>
              </div>
            </div>
          )}

        </div>
      </div>
    </div>
  );
}