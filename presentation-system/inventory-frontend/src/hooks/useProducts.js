import { useState, useEffect, useCallback } from 'react';
import { api } from '../utilities/ApiUtils';

// Maps backend ProductDTO fields to what the frontend components expect
function mapProduct(p) {
  return {
    id: p.id,
    name: p.name,
    sku: p.sku,
    description: p.description,
    category: p.categoryName || p.category || '',
    categoryId: p.categoryId,
    supplier: p.supplierName || p.supplier || '',
    supplierId: p.supplierId,
    quantity: p.quantity,
    unitPrice: p.price != null ? p.price : p.unitPrice,
    reorderLevel: p.reorderLevel || 10,
    active: p.active,
    createdAt: p.createdAt,
    updatedAt: p.updatedAt,
  };
}

export function useProducts() {
  const [products, setProducts] = useState([]);
  const [loading,  setLoading]  = useState(false);
  const [error,    setError]    = useState(null);

  const load = useCallback(async () => {
    setLoading(true); setError(null);
    try {
      const data = await api.get('/products');
      setProducts(data.map(mapProduct));
    } catch (e) {
      setError(e.message);
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => { load(); }, [load]);

  const addProduct = async (product) => {
    const created = await api.post('/products', product);
    setProducts(prev => [mapProduct(created), ...prev]);
    return mapProduct(created);
  };

  const editProduct = async (id, data) => {
    const updated = await api.put(`/products/${id}`, data);
    setProducts(prev => prev.map(p => p.id === id ? mapProduct(updated) : p));
    return mapProduct(updated);
  };

  const removeProduct = async (id) => {
    await api.delete(`/products/${id}`);
    setProducts(prev => prev.filter(p => p.id !== id));
  };

  const lowStockProducts = products.filter(p => p.quantity <= p.reorderLevel);

  return {
    products, loading, error,
    refresh: load,
    lowStockProducts,
    addProduct, editProduct, removeProduct,
  };
}
