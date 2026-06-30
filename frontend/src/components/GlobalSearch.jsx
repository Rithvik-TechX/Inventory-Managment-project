import { useEffect, useRef, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { api } from '../utilities/ApiUtils';
import '../styles/global-search.css';

const emptyResults = { products: [], categories: [], suppliers: [] };

export default function GlobalSearch() {
  const [query, setQuery] = useState('');
  const [isOpen, setIsOpen] = useState(false);
  const [loading, setLoading] = useState(false);
  const [results, setResults] = useState(emptyResults);
  const inputRef = useRef(null);
  const rootRef = useRef(null);
  const navigate = useNavigate();

  useEffect(() => {
    const handleKeyDown = event => {
      if ((event.metaKey || event.ctrlKey) && event.key.toLowerCase() === 'k') { event.preventDefault(); inputRef.current?.focus(); setIsOpen(true); }
      if (event.key === 'Escape') { setIsOpen(false); inputRef.current?.blur(); }
    };
    const handleOutside = event => { if (!rootRef.current?.contains(event.target)) setIsOpen(false); };
    window.addEventListener('keydown', handleKeyDown);
    document.addEventListener('mousedown', handleOutside);
    return () => { window.removeEventListener('keydown', handleKeyDown); document.removeEventListener('mousedown', handleOutside); };
  }, []);

  useEffect(() => {
    const trimmed = query.trim();
    if (trimmed.length < 2) { setResults(emptyResults); setLoading(false); return undefined; }
    setLoading(true);
    const timeout = window.setTimeout(async () => {
      try { setResults(await api.get(`/search?q=${encodeURIComponent(trimmed)}`)); setIsOpen(true); }
      catch { setResults(emptyResults); }
      finally { setLoading(false); }
    }, 300);
    return () => window.clearTimeout(timeout);
  }, [query]);

  const select = (type, id) => { setIsOpen(false); setQuery(''); navigate(`/app/${type}?highlight=${id}`); };
  const hasResults = results.products.length || results.categories.length || results.suppliers.length;

  return <div className="global-search" ref={rootRef}><div className="global-search__input"><SearchIcon /><input ref={inputRef} aria-label="Search inventory" value={query} onChange={event => setQuery(event.target.value)} onFocus={() => query.trim().length >= 2 && setIsOpen(true)} placeholder="Search inventory..." /><kbd>Ctrl K</kbd></div>{isOpen && query.trim().length >= 2 && <div className="global-search__results" role="listbox" aria-label="Search results">{loading ? <div className="global-search__status">Searching…</div> : hasResults ? <><ResultGroup title="Products" items={results.products} type="products" onSelect={select} secondary={item => item.sku} /><ResultGroup title="Categories" items={results.categories} type="categories" onSelect={select} /><ResultGroup title="Suppliers" items={results.suppliers} type="suppliers" onSelect={select} /></> : <div className="global-search__status">No inventory matches “{query.trim()}”</div>}</div>}</div>;
}

function ResultGroup({ title, items, type, onSelect, secondary }) { if (!items.length) return null; return <section className="global-search__group"><h2>{title}</h2>{items.map(item => <button type="button" role="option" key={item.id} onClick={() => onSelect(type, item.id)}><span>{item.name}</span>{secondary?.(item) && <small>{secondary(item)}</small>}</button>)}</section>; }
function SearchIcon() { return <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><circle cx="11" cy="11" r="8" /><path d="m21 21-4.35-4.35" /></svg>; }
