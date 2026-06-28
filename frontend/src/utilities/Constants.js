export const API_BASE_URL =
  (process.env.REACT_APP_API_URL || 'http://localhost:8083') + '/api';
 
export const ROLES = {
  ADMIN:   'ADMIN',
  MANAGER: 'MANAGER',
  STAFF:   'STAFF',
};
 
export const TOKEN_KEY    = 'inventtrack_token';
export const USER_KEY     = 'inventtrack_user';
 
export const LOW_STOCK_THRESHOLD = 10;
 
export const CATEGORIES = [
  'Electronics',
  'Stationery',
  'Furniture',
  'Clothing',
  'Food & Beverages',
  'Tools & Hardware',
  'Medical',
  'Other',
];
 
export const REPORT_TYPES = {
  SUMMARY:    'summary',
  LOW_STOCK:  'low-stock',
  VALUE:      'value',
  CATEGORIES: 'categories',
};
