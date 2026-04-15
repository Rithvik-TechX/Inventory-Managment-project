import { createContext, useContext, useState, useCallback } from 'react';
import { api } from '../utilities/ApiUtils';

const InventoryContext = createContext(null);

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

export function InventoryProvider({ children }) {
  const [products, setProducts] = useState([]);
  const [loading,  setLoading]  = useState(false);
  const [error,    setError]    = useState(null);

  const fetchProducts = useCallback(async () => {
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

  const addProduct = async (product) => {
    const data = await api.post('/products', product);
    const mapped = mapProduct(data);
    setProducts(prev => [mapped, ...prev]);
    return mapped;
  };

  const updateProduct = async (id, product) => {
    const data = await api.put(`/products/${id}`, product);
    const mapped = mapProduct(data);
    setProducts(prev => prev.map(p => p.id === id ? mapped : p));
    return mapped;
  };

  const deleteProduct = async (id) => {
    await api.delete(`/products/${id}`);
    setProducts(prev => prev.filter(p => p.id !== id));
  };

  const getLowStock = () => products.filter(p => p.quantity <= p.reorderLevel);

  return (
    <InventoryContext.Provider value={{
      products, loading, error,
      fetchProducts, addProduct, updateProduct, deleteProduct, getLowStock,
    }}>
      {children}
    </InventoryContext.Provider>
  );
}

export const useInventoryContext = () => useContext(InventoryContext);
