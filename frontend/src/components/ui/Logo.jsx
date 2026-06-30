import './Logo.css';

export function LogoMark({ size = 32, className = '' }) {
  return (
    <svg
      className={`inventtrack-logo__mark ${className}`}
      width={size}
      height={size}
      viewBox="0 0 64 64"
      role="img"
      aria-label="InvenTrack"
    >
      <rect width="64" height="64" rx="16" fill="#4F46E5" />
      <g fill="none" stroke="white" strokeWidth="3" strokeLinecap="round" strokeLinejoin="round">
        <path d="m18 24 14-8 14 8v17L32 49 18 41z" />
        <path d="m18 24 14 8 14-8M32 32v17M25 20l14 8" />
      </g>
    </svg>
  );
}

export function Logo({ size = 32, showText = true, textSize = 18, textColor = '#111827', className = '' }) {
  return (
    <span className={`inventtrack-logo ${className}`}>
      <LogoMark size={size} />
      {showText && <span className="inventtrack-logo__text" style={{ fontSize: textSize, color: textColor }}>InvenTrack</span>}
    </span>
  );
}
