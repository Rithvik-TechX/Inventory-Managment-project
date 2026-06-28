import { useNavigate } from 'react-router-dom';
import PageLayout from '../components/PageLayout';
import ProductForm from '../components/ProductForm';
import { useProducts } from '../hooks/useProducts';
import { useToast } from '../components/Toast';
import { useState } from 'react';
import '../styles/inventory.css';

export default function AddProductPage() {
  const navigate       = useNavigate();
  const { addProduct } = useProducts();
  const toast          = useToast();
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (data) => {
    setLoading(true);
    try {
      await addProduct(data);
      toast.success('Product added successfully!');
      setTimeout(() => navigate('/products'), 800);
    } catch (e) {
      toast.error('Failed to add product: ' + e.message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <PageLayout title="Add Product">
      <button
        className="btn btn-ghost btn-sm"
        style={{ marginBottom: 20 }}
        onClick={() => navigate('/products')}
      >
        ← Back to Products
      </button>

      <h1 className="page-title">Add New Product</h1>
      <p className="page-subtitle">Fill in the details below to add a product to your inventory.</p>

      <ProductForm
        onSubmit={handleSubmit}
        onCancel={() => navigate('/products')}
        loading={loading}
      />
    </PageLayout>
  );
}
