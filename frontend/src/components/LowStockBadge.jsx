export default function LowStockBadge({ quantity, maxStock, lowStock }) {
  // Low stock = quantity < 50% of maxStock (computed by backend)
  const isLow = lowStock != null ? lowStock : (maxStock ? quantity < maxStock * 0.5 : false);

  if (isLow) {
    return (
      <span className="low-stock-badge">
        LOW STOCK
      </span>
    );
  }
  return (
    <span className="ok-badge">
      ✓ OK
    </span>
  );
}