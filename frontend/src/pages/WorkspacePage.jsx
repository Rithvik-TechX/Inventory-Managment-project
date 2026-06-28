import PageLayout from '../components/PageLayout';

const content = {
  Categories: ['Organize the catalog', 'Group products into clear, searchable collections.'],
  Suppliers: ['Supplier network', 'Keep vendor contacts, lead times, and order history in one place.'],
  Orders: ['Purchase orders', 'Track replenishment from request to receiving.'],
  Notifications: ['Notifications', 'Stay ahead of stock issues and team activity.'],
  Settings: ['Workspace settings', 'Manage preferences, access, security, and integrations.'],
  Profile: ['Your profile', 'Update personal details and account security.'],
};

export default function WorkspacePage({ type }) {
  const [title, subtitle] = content[type];
  return <PageLayout title={type}><div className="section-heading"><div><span className="eyebrow">WORKSPACE</span><h1>{title}</h1><p>{subtitle}</p></div><button className="btn-primary">{type === 'Notifications' ? 'Mark all read' : `Add ${type.replace(/s$/, '')}`}</button></div><div className="feature-empty"><div className="feature-empty__icon">{type[0]}</div><h2>{type} workspace is ready</h2><p>This module is connected to the product shell and ready for your backend data.</p><button className="btn-secondary">View documentation</button></div></PageLayout>;
}
