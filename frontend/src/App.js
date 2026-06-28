import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { useEffect } from 'react';
import { AuthProvider } from './context/AuthContext';
import { useAuth } from './hooks/useAuth';
import { ToastProvider } from './components/Toast';
import { useToast } from './components/Toast';
import { ThemeProvider } from './context/ThemeContext';

import LoginPage     from './pages/LoginPage.jsx';
import DashboardPage from './pages/DashboardPage.jsx';
import ProductsPage  from './pages/ProductsPage.jsx';
import AddProductPage from './pages/AddProductPage.jsx';
import ReportsPage   from './pages/ReportsPage.jsx';
import AddUserPage   from './pages/AddUserPage.jsx';
import RegisterPage from './pages/RegisterPage.jsx';
import ForgotPasswordPage from './pages/ForgotPasswordPage.jsx';
import LandingPage from './pages/LandingPage.jsx';
import WorkspacePage from './pages/WorkspacePage.jsx';
import SettingsPage from './pages/SettingsPageV4.jsx';
import { CategoriesPage, SuppliersPage } from './pages/ResourcePages.jsx';
import OrdersPage from './pages/OrdersPage.jsx';

import './styles/global.css';
import './styles/components.css';
import './styles/auth.css';
import './styles/product-shell.css';
import './styles/admin.css';
import './styles/corrections.css';
import './styles/v3.css';
import './styles/v4.css';
import './styles/v4-extra.css';
import './styles/v4-notification.css';

function Protected({ children, requiredRole }) {
  const { isAuthenticated, hasRole } = useAuth();
  const toast = useToast();
  const denied = Boolean(requiredRole && isAuthenticated && !hasRole(requiredRole));
  useEffect(() => { if (denied) toast.warning("You don't have permission to access this page."); }, [denied]);
  if (!isAuthenticated) return <Navigate to="/login" replace />;
  if (denied) return <Navigate to="/app/dashboard" replace />;
  return children;
}

function AppRoutes() {
  const { isAuthenticated } = useAuth();

  return (
    <Routes>
      <Route path="/" element={<LandingPage />} />
      <Route path="/login" element={
        isAuthenticated ? <Navigate to="/app/dashboard" replace /> : <LoginPage />
      } />
      <Route path="/register" element={isAuthenticated ? <Navigate to="/app/dashboard" replace /> : <RegisterPage />} />
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
        <Protected requiredRole="MANAGER"><ReportsPage /></Protected>
      } />
      <Route path="/add-user" element={
        <Protected requiredRole="ADMIN"><AddUserPage /></Protected>
      } />
      <Route path="/app/dashboard" element={<Protected><DashboardPage /></Protected>} />
      <Route path="/app/products/*" element={<Protected><ProductsPage /></Protected>} />
      <Route path="/app/reports/*" element={<Protected requiredRole="MANAGER"><ReportsPage /></Protected>} />
      <Route path="/app/categories" element={<Protected><CategoriesPage /></Protected>} />
      <Route path="/app/suppliers" element={<Protected><SuppliersPage /></Protected>} />
      <Route path="/app/orders" element={<Protected><OrdersPage /></Protected>} />
      <Route path="/app/notifications" element={<Protected><WorkspacePage type="Notifications" /></Protected>} />
      <Route path="/app/settings/*" element={<Protected requiredRole="MANAGER"><SettingsPage /></Protected>} />
      <Route path="/app/profile" element={<Protected><WorkspacePage type="Profile" /></Protected>} />
      <Route path="*" element={
        <Navigate to={isAuthenticated ? '/app/dashboard' : '/login'} replace />
      } />
    </Routes>
  );
}

export default function App() {
  return (
    <BrowserRouter future={{ v7_startTransition: true, v7_relativeSplatPath: true }}>
      <ThemeProvider>
        <AuthProvider>
          <ToastProvider><AppRoutes /></ToastProvider>
        </AuthProvider>
      </ThemeProvider>
    </BrowserRouter>
  );
}
