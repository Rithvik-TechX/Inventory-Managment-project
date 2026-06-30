import Modal from './Modal';

export default function ConfirmDialog({
  isOpen,
  title = 'Confirm action',
  message,
  confirmLabel = 'Delete',
  onConfirm,
  onClose,
  busy = false,
}) {
  return (
    <Modal isOpen={isOpen} onClose={onClose} title={title} size="sm">
      <p className="confirm-dialog__message">{message}</p>
      <div className="modal-actions">
        <button type="button" className="btn-secondary" onClick={onClose} disabled={busy}>Cancel</button>
        <button type="button" className="btn btn-danger" onClick={onConfirm} disabled={busy}>
          {busy ? 'Deleting…' : confirmLabel}
        </button>
      </div>
    </Modal>
  );
}
