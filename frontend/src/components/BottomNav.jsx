import { NavLink } from 'react-router-dom';

const items = [
  ['/app/dashboard', '⌂', 'Home'],
  ['/app/products', '□', 'Products'],
  ['/app/reports', '⌁', 'Reports'],
  ['/app/notifications', '♢', 'Alerts'],
  ['/app/settings', '•••', 'More'],
];

export default function BottomNav() {
  return (
    <nav className="bottom-nav" aria-label="Mobile navigation">
      {items.map(([to, icon, label]) => (
        <NavLink key={to} to={to} className={({ isActive }) => isActive ? 'active' : ''}>
          <span className="bottom-nav__icon">{icon}</span>
          <span>{label}</span>
        </NavLink>
      ))}
    </nav>
  );
}
