/**
 * Empty state component for pages/sections with no data.
 */
export default function EmptyState({ icon, title, description, actionLabel, onAction }) {
  return (
    <div className="empty-state">
      {icon && <div className="empty-state-icon">{icon}</div>}
      {title && <h3 className="empty-state-title">{title}</h3>}
      {description && <p className="empty-state-desc">{description}</p>}
      {actionLabel && onAction && (
        <button className="btn btn-primary" onClick={onAction} style={{ marginTop: 16 }}>
          {actionLabel}
        </button>
      )}
    </div>
  );
}
