import { act } from 'react';
import { createRoot } from 'react-dom/client';
import App from './App';

test('renders the InvenTrack landing page', () => {
  const container = document.createElement('div');
  document.body.appendChild(container);
  const root = createRoot(container);
  act(() => root.render(<App />));
  expect(container.textContent).toContain('InvenTrack');
  expect(container.textContent).toContain('Effortlessly.');
  act(() => root.unmount());
  container.remove();
});
