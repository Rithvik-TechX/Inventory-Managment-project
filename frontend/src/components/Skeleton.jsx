/**
 * Skeleton loading components for premium loading states.
 */

export function SkeletonLine({ width = '100%', height = 14 }) {
  return (
    <div
      className="skeleton-shimmer"
      style={{
        width,
        height,
        borderRadius: 6,
        background: 'var(--bg-hover, #1c2235)',
      }}
    />
  );
}

export function SkeletonCard() {
  return (
    <div style={{
      background: 'var(--bg-card)',
      border: '1px solid var(--border)',
      borderRadius: 12,
      padding: 24,
      display: 'flex',
      flexDirection: 'column',
      gap: 12,
    }}>
      <div style={{ display: 'flex', justifyContent: 'space-between' }}>
        <SkeletonLine width="40%" height={12} />
        <SkeletonLine width={32} height={32} />
      </div>
      <SkeletonLine width="60%" height={28} />
      <SkeletonLine width="30%" height={12} />
    </div>
  );
}

export function SkeletonTable({ rows = 5, cols = 6 }) {
  return (
    <div style={{
      background: 'var(--bg-card)',
      border: '1px solid var(--border)',
      borderRadius: 12,
      overflow: 'hidden',
    }}>
      {/* Header */}
      <div style={{
        display: 'grid',
        gridTemplateColumns: `repeat(${cols}, 1fr)`,
        gap: 16,
        padding: '14px 20px',
        borderBottom: '1px solid var(--border)',
      }}>
        {Array.from({ length: cols }).map((_, i) => (
          <SkeletonLine key={i} width={`${60 + Math.random() * 30}%`} height={12} />
        ))}
      </div>
      {/* Rows */}
      {Array.from({ length: rows }).map((_, r) => (
        <div key={r} style={{
          display: 'grid',
          gridTemplateColumns: `repeat(${cols}, 1fr)`,
          gap: 16,
          padding: '14px 20px',
          borderBottom: '1px solid var(--border)',
        }}>
          {Array.from({ length: cols }).map((_, c) => (
            <SkeletonLine key={c} width={`${40 + Math.random() * 50}%`} height={14} />
          ))}
        </div>
      ))}
    </div>
  );
}
