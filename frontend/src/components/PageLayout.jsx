import { useState } from 'react';
import Sidebar from './Sidebar';
import Navbar from './Navbar';

/**
 * Standard page layout used by all authenticated pages.
 * Provides sidebar, navbar, and mobile-responsive navigation.
 */
export default function PageLayout({ title, children }) {
  const [sidebarOpen, setSidebarOpen] = useState(false);

  return (
    <div className="page-layout">
      <Sidebar
        isOpen={sidebarOpen}
        onToggle={() => setSidebarOpen(prev => !prev)}
      />
      <div className="main-content">
        <Navbar
          title={title}
          onMenuToggle={() => setSidebarOpen(prev => !prev)}
        />
        <div className="page-inner fade-up">
          {children}
        </div>
      </div>
    </div>
  );
}
