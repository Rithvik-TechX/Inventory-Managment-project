import { api } from '../utilities/ApiUtils';
 
export const ProductService = {
  getAll:        ()          => api.get('/products'),
  getById:       (id)        => api.get(`/products/${id}`),
  search:        (q)         => api.get(`/products/search?q=${encodeURIComponent(q)}`),
  getByCategory: (cat)       => api.get(`/products/category/${encodeURIComponent(cat)}`),
  getLowStock:   ()          => api.get('/products/low-stock'),
  create:        (product)   => api.post('/products', product),
  update:        (id, data)  => api.put(`/products/${id}`, data),
  delete:        (id)        => api.delete(`/products/${id}`),
  restock:       (id, qty)   => api.put(`/products/${id}/restock`, { quantity: qty }),
};