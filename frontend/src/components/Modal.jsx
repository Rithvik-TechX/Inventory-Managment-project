import { useEffect, useCallback } from 'react';
import { createPortal } from 'react-dom';

/**
 * Accessible modal dialog with overlay, escape-to-close, and backdrop click.
 */
export default function Modal({ isOpen, onClose, title, children, size = 'md' }) {
  const handleEscape = useCallback((e) => {
    if (e.key === 'Escape') onClose();
  }, [onClose]);

  useEffect(() => {
    const previousOverflow = document.body.style.overflow;
    if (isOpen) {
      document.addEventListener('keydown', handleEscape);
      document.body.style.overflow = 'hidden';
    }
    return () => {
      document.removeEventListener('keydown', handleEscape);
      document.body.style.overflow = previousOverflow;
    };
  }, [isOpen, handleEscape]);

  if (!isOpen) return null;

  const sizeMap = { sm: 420, md: 560, lg: 720 };
  const maxWidth = sizeMap[size] || sizeMap.md;

  return createPortal(
    <div className="modal-overlay modal-backdrop" onClick={onClose}>
      <div
        className="modal-content modal-card"
        onClick={e => e.stopPropagation()}
        style={{ maxWidth }}
        role="dialog"
        aria-modal="true"
        aria-label={title}
      >
        <div className="modal-header">
          <h2 className="modal-title">{title}</h2>
          <button className="modal-close" onClick={onClose} aria-label="Close">
            <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
              <line x1="18" y1="6" x2="6" y2="18" /><line x1="6" y1="6" x2="18" y2="18" />
            </svg>
          </button>
        </div>
        <div className="modal-body">
          {children}
        </div>
      </div>
    </div>,
    document.body
  );
}
