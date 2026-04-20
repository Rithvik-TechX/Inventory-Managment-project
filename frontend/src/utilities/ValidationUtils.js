export const ValidationUtils = {
  required: (val) => (!val || String(val).trim() === '') ? 'This field is required.' : '',
 
  minLength: (val, min) =>
    String(val).length < min ? `Must be at least ${min} characters.` : '',
 
  positiveNumber: (val) =>
    isNaN(val) || Number(val) < 0 ? 'Must be a positive number.' : '',
 
  email: (val) =>
    !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(val) ? 'Invalid email address.' : '',
 
  validateProduct: ({ name, sku, category, quantity, unitPrice, reorderLevel }) => {
    const errors = {};
    if (!name?.trim())          errors.name         = 'Product name is required.';
    if (!sku?.trim())           errors.sku           = 'SKU is required.';
    if (!category)              errors.category     = 'Category is required.';
    if (quantity === '' || isNaN(quantity) || Number(quantity) < 0)
                                errors.quantity     = 'Quantity must be 0 or more.';
    if (!unitPrice || isNaN(unitPrice) || Number(unitPrice) <= 0)
                                errors.unitPrice    = 'Price must be greater than 0.';
    if (reorderLevel === '' || isNaN(reorderLevel) || Number(reorderLevel) < 0)
                                errors.reorderLevel = 'Reorder level must be 0 or more.';
    return errors;
  },
 
  validateLogin: ({ username, password }) => {
    const errors = {};
    if (!username?.trim()) errors.username = 'Username is required.';
    if (!password)         errors.password = 'Password is required.';
    return errors;
  },
};