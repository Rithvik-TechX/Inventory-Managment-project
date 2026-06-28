import { createContext, useContext } from 'react';

const ThemeContext = createContext({ theme: 'light' });

export function ThemeProvider({ children }) {
  return <ThemeContext.Provider value={{ theme: 'light' }}>{children}</ThemeContext.Provider>;
}

export const useTheme = () => useContext(ThemeContext);
