import { useNavigate } from 'react-router-dom';
import Navbar from '../components/Navbar';
import Sidebar from '../components/Sidebar';
import ProductForm from '../components/ProductForm';
import { useProducts } from '../hooks/useProducts';
import { useState } from 'react';
import '../styles/inventory.css';

export default function AddProductPage() {
  const navigate             = useNavigate();
  const { addProduct }       = useProducts();
  const [loading, setLoading] = useState(false);
  const [success, setSuccess] = useState(false);

  const handleSubmit = async (data) => {
    setLoading(true);
    try {
      await addProduct(data);
      setSuccess(true);
      setTimeout(() => navigate('/products'), 1200);
    } catch (e) {
      alert('Failed to add product: ' + e.message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="page-layout">
      <Sidebar />
      <div className="main-content">
        <Navbar title="Add Product" />
        <div className="page-inner fade-up">
          <button
            className="btn btn-ghost btn-sm"
            style={{ marginBottom: 20 }}
            onClick={() => navigate('/products')}
          >
            ← Back to Products
          </button>

          <h1 className="page-title">Add New Product</h1>
          <p className="page-subtitle">Fill in the details below to add a product to your inventory.</p>

          {success && (
            <div style={{
              background: 'rgba(34,197,94,0.1)', border: '1px solid rgba(34,197,94,0.3)',
              color: 'var(--accent-green)', borderRadius: 'var(--radius-sm)',
              padding: '12px 16px', marginBottom: 20, fontSize: '0.88rem',
            }}>
              ✓ Product added successfully! Redirecting…
            </div>
          )}

          <ProductForm
            onSubmit={handleSubmit}
            onCancel={() => navigate('/products')}
            loading={loading}
          />
        </div>
      </div>
    </div>
  );
}
