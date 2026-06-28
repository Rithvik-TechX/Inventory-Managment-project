import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import { useAuth } from './hooks/useAuth';
import { ToastProvider } from './components/Toast';
import { ThemeProvider } from './context/ThemeContext';

import LoginPage     from './pages/LoginPage.jsx';
import DashboardPage from './pages/DashboardPage.jsx';
import ProductsPage  from './pages/ProductsPage.jsx';
import AddProductPage from './pages/AddProductPage.jsx';
import ReportsPage   from './pages/ReportsPage.jsx';
import AddUserPage   from './pages/AddUserPage.jsx';
import RegisterPage from './pages/RegisterPage.jsx';
import ForgotPasswordPage from './pages/ForgotPasswordPage.jsx';
import WorkspacePage from './pages/WorkspacePage.jsx';

import './styles/global.css';
import './styles/components.css';
import './styles/auth.css';
import './styles/product-shell.css';

function Protected({ children, requiredRole }) {
  const { isAuthenticated, hasRole } = useAuth();
  if (!isAuthenticated) return <Navigate to="/login" replace />;
  if (requiredRole && !hasRole(requiredRole)) return <Navigate to="/dashboard" replace />;
  return children;
}

function AppRoutes() {
  const { isAuthenticated } = useAuth();

  return (
    <Routes>
      <Route path="/login" element={
        isAuthenticated ? <Navigate to="/dashboard" replace /> : <LoginPage />
      } />
      <Route path="/register" element={<RegisterPage />} />
      <Route path="/forgot-password" element={<ForgotPasswordPage />} />
      <Route path="/dashboard" element={
        <Protected><DashboardPage /></Protected>
      } />
      <Route path="/products" element={
        <Protected><ProductsPage /></Protected>
      } />
      <Route path="/add-product" element={
        <Protected requiredRole="MANAGER"><AddProductPage /></Protected>
      } />
      <Route path="/reports" element={
        <Protected><ReportsPage /></Protected>
      } />
      <Route path="/add-user" element={
        <Protected requiredRole="ADMIN"><AddUserPage /></Protected>
      } />
      <Route path="/app/dashboard" element={<Protected><DashboardPage /></Protected>} />
      <Route path="/app/products/*" element={<Protected><ProductsPage /></Protected>} />
      <Route path="/app/reports/*" element={<Protected><ReportsPage /></Protected>} />
      <Route path="/app/categories" element={<Protected><WorkspacePage type="Categories" /></Protected>} />
      <Route path="/app/suppliers" element={<Protected><WorkspacePage type="Suppliers" /></Protected>} />
      <Route path="/app/orders" element={<Protected><WorkspacePage type="Orders" /></Protected>} />
      <Route path="/app/notifications" element={<Protected><WorkspacePage type="Notifications" /></Protected>} />
      <Route path="/app/settings/*" element={<Protected><WorkspacePage type="Settings" /></Protected>} />
      <Route path="/app/profile" element={<Protected><WorkspacePage type="Profile" /></Protected>} />
      <Route path="*" element={
        <Navigate to={isAuthenticated ? '/dashboard' : '/login'} replace />
      } />
    </Routes>
  );
}

export default function App() {
  return (
    <BrowserRouter>
      <ThemeProvider>
        <AuthProvider>
          <ToastProvider><AppRoutes /></ToastProvider>
        </AuthProvider>
      </ThemeProvider>
    </BrowserRouter>
  );
}
