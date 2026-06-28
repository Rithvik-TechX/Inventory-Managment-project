export default function SummaryCard({ label, value, icon, iconClass = 'icon-blue', footer, trend, tone, currency = false, delay = 0 }) {
  return (
    <div className={`summary-card kpi-card ${currency ? 'kpi-card--currency' : ''} fade-up`} style={{ animationDelay: `${delay}ms` }}>
      <div className="summary-card-header kpi-card__top">
        <span className="summary-card-label kpi-card__label">{label}</span>
        <div className={`summary-card-icon kpi-card__icon ${iconClass}`}>{icon}</div>
      </div>
      <div className="summary-card-bottom kpi-card__bottom"><div className="summary-card-value kpi-card__value">{value}</div>{(trend || footer) && <div className={`summary-card-trend kpi-card__sub ${tone === 'amber' ? 'kpi-card__sub--warning' : tone || ''}`}>{trend || footer}</div>}</div>
    </div>
  );
}
