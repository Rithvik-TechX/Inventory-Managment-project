export default function SummaryCard({ label, value, icon, iconClass = 'icon-blue', footer, delay = 0 }) {
  return (
    <div className="summary-card fade-up" style={{ animationDelay: `${delay}ms` }}>
      <div className="summary-card-header">
        <span className="summary-card-label">{label}</span>
        <div className={`summary-card-icon ${iconClass}`}>{icon}</div>
      </div>
      <div className="summary-card-value">{value}</div>
      {footer && <div className="summary-card-footer">{footer}</div>}
    </div>
  );
}