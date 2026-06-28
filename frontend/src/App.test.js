import { act } from 'react';
import { createRoot } from 'react-dom/client';
import App from './App';

test('renders the InvenTrack sign-in screen', () => {
  const container = document.createElement('div');
  document.body.appendChild(container);
  const root = createRoot(container);
  act(() => root.render(<App />));
  expect(container.textContent).toContain('InvenTrack');
  expect(container.textContent).toContain('Sign in to your account');
  act(() => root.unmount());
  container.remove();
});
