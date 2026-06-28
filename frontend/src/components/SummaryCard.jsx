export default function SummaryCard({ label, value, icon, iconClass = 'icon-blue', footer, trend, tone, delay = 0 }) {
  return (
    <div className="summary-card fade-up" style={{ animationDelay: `${delay}ms` }}>
      <div className="summary-card-header">
        <span className="summary-card-label">{label}</span>
        <div className={`summary-card-icon ${iconClass}`}>{icon}</div>
      </div>
      <div className="summary-card-bottom"><div className="summary-card-value">{value}</div>{(trend || footer) && <div className={`summary-card-trend ${tone || ''}`}>{trend || footer}</div>}</div>
    </div>
  );
}
