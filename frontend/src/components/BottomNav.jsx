import { NavLink } from 'react-router-dom';
import { useAuth } from '../hooks/useAuth';

export default function BottomNav() {
  const { hasRole } = useAuth();
  const items = [
    ['/app/dashboard', '⌂', 'Home'], ['/app/products', '□', 'Products'],
    hasRole('MANAGER') ? ['/app/reports', '⌁', 'Reports'] : ['/app/categories', '▦', 'Categories'],
    ['/app/notifications', '♢', 'Alerts'],
    hasRole('MANAGER') ? ['/app/settings', '•••', 'More'] : ['/app/suppliers', '⌂', 'Suppliers'],
  ];
  return <nav className="bottom-nav" aria-label="Mobile navigation">{items.map(([to,icon,label])=><NavLink key={to} to={to} className={({isActive})=>isActive?'active':''}><span className="bottom-nav__icon">{icon}</span><span>{label}</span></NavLink>)}</nav>;
}
