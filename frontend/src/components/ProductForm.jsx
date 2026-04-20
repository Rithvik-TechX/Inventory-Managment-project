import { useState, useEffect } from 'react';
import { ValidationUtils } from '../utilities/ValidationUtils';
import { api } from '../utilities/ApiUtils';

const EMPTY = { name: '', sku: '', categoryId: '', quantity: '', unitPrice: '', reorderLevel: 10, supplierId: '', description: '' };

export default function ProductForm({ initial = null, onSubmit, onCancel, loading = false }) {
  const [form,       setForm]       = useState(EMPTY);
  const [errors,     setErrors]     = useState({});
  const [categories, setCategories] = useState([]);
  const [suppliers,  setSuppliers]  = useState([]);

  // Load categories and suppliers from backend
  useEffect(() => {
    api.get('/categories').then(setCategories).catch(() => {});
    api.get('/suppliers').then(setSuppliers).catch(() => {});
  }, []);

  // Populate form when editing
  useEffect(() => {
    if (initial) {
      setForm({
        name:         initial.name || '',
        sku:          initial.sku || '',
        categoryId:   initial.categoryId || '',
        quantity:     initial.quantity ?? '',
        unitPrice:    initial.unitPrice ?? '',
        reorderLevel: initial.reorderLevel ?? 10,
        supplierId:   initial.supplierId || '',
        description:  initial.description || '',
      });
    } else {
      setForm(EMPTY);
    }
  }, [initial]);

  const set = (field) => (e) => setForm(f => ({ ...f, [field]: e.target.value }));

  const handleSubmit = async (e) => {
    e.preventDefault();
    const errs = {};
    if (!form.name?.trim())    errs.name     = 'Product name is required.';
    if (!form.sku?.trim())     errs.sku      = 'SKU is required.';
    if (!form.categoryId)      errs.categoryId = 'Category is required.';
    if (!form.supplierId)      errs.supplierId = 'Supplier is required.';
    if (form.quantity === '' || isNaN(form.quantity) || Number(form.quantity) < 0)
                               errs.quantity  = 'Quantity must be 0 or more.';
    if (!form.unitPrice || isNaN(form.unitPrice) || Number(form.unitPrice) <= 0)
                               errs.unitPrice = 'Price must be greater than 0.';
    if (Object.keys(errs).length) { setErrors(errs); return; }
    setErrors({});

    await onSubmit({
      name:         form.name,
      sku:          form.sku,
      categoryId:   Number(form.categoryId),
      supplierId:   Number(form.supplierId),
      quantity:     Number(form.quantity),
      unitPrice:    Number(form.unitPrice),
      reorderLevel: Number(form.reorderLevel),
      description:  form.description,
    });
  };

  return (
    <form onSubmit={handleSubmit} className="form-card">
      <div className="form-grid-2">

        <div className="form-group">
          <label>Product Name *</label>
          <input type="text" value={form.name} onChange={set('name')} placeholder="e.g. Wireless Mouse" />
          {errors.name && <div className="error-msg">{errors.name}</div>}
        </div>

        <div className="form-group">
          <label>SKU *</label>
          <input type="text" value={form.sku} onChange={set('sku')} placeholder="e.g. ELEC-005" />
          {errors.sku && <div className="error-msg">{errors.sku}</div>}
        </div>

        <div className="form-group">
          <label>Category *</label>
          <select value={form.categoryId} onChange={set('categoryId')}>
            <option value="">Select category</option>
            {categories.map(c => <option key={c.id} value={c.id}>{c.name}</option>)}
          </select>
          {errors.categoryId && <div className="error-msg">{errors.categoryId}</div>}
        </div>

        <div className="form-group">
          <label>Supplier *</label>
          <select value={form.supplierId} onChange={set('supplierId')}>
            <option value="">Select supplier</option>
            {suppliers.map(s => <option key={s.id} value={s.id}>{s.name}</option>)}
          </select>
          {errors.supplierId && <div className="error-msg">{errors.supplierId}</div>}
        </div>

        <div className="form-group">
          <label>Quantity *</label>
          <input type="number" min="0" value={form.quantity} onChange={set('quantity')} placeholder="0" />
          {errors.quantity && <div className="error-msg">{errors.quantity}</div>}
        </div>

        <div className="form-group">
          <label>Unit Price (₹) *</label>
          <input type="number" min="0" step="0.01" value={form.unitPrice} onChange={set('unitPrice')} placeholder="0.00" />
          {errors.unitPrice && <div className="error-msg">{errors.unitPrice}</div>}
        </div>

        <div className="form-group">
          <label>Reorder Level</label>
          <input type="number" min="0" value={form.reorderLevel} onChange={set('reorderLevel')} placeholder="10" />
        </div>

        <div className="form-group">
          <label>Description</label>
          <input type="text" value={form.description} onChange={set('description')} placeholder="Product description" />
        </div>

      </div>

      <div className="form-actions">
        <button type="submit" className="btn btn-primary" disabled={loading}>
          {loading ? 'Saving…' : (initial ? 'Update Product' : 'Add Product')}
        </button>
        {onCancel && (
          <button type="button" className="btn btn-ghost" onClick={onCancel}>
            Cancel
          </button>
        )}
      </div>
    </form>
  );
}