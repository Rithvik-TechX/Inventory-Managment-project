import { Link, Navigate } from 'react-router-dom';
import { useAuth } from '../hooks/useAuth';
import '../styles/landing.css';

const Brand=()=> <span className="landing-brand"><span>I</span><strong>InvenTrack</strong></span>;
const features=[
  ['box','Real-Time Stock Tracking',"Know exactly what's in stock, what's running low, and what needs reordering — updated live as your team makes changes."],
  ['users','Role-Based Access Control','Admin, Manager, and Staff roles keep the right people in control. Admins manage users; staff focus on inventory.'],
  ['chart','Reports & Analytics','Generate inventory reports, track stock value by category, and export data in one click.'],
];
const Icon=({type})=> type==='users'?<svg viewBox="0 0 24 24"><path d="M16 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"/><circle cx="8.5" cy="7" r="4"/><path d="M20 8v6M23 11h-6"/></svg>:type==='chart'?<svg viewBox="0 0 24 24"><path d="M4 19V9M10 19V5M16 19v-7M22 19V2"/></svg>:<svg viewBox="0 0 24 24"><path d="m21 8-9 5-9-5 9-5 9 5Z"/><path d="m3 8 9 5 9-5v8l-9 5-9-5V8Z"/></svg>;

export default function LandingPage(){
 const {isAuthenticated}=useAuth();
 if(isAuthenticated)return <Navigate to="/app/dashboard" replace/>;
 return <div className="landing-page">
  <header className="landing-nav"><Link to="/"><Brand/></Link><nav><a href="#features">Features</a><a href="#how">How it works</a><a href="#pricing">Pricing</a></nav><div><Link className="landing-btn outline small" to="/login">Sign In</Link><Link className="landing-btn primary small" to="/register">Get Started Free</Link></div></header>
  <main>
   <section className="landing-hero"><p className="landing-eyebrow">Inventory Management, Simplified</p><h1>Take control of your<br/>inventory. <em>Effortlessly.</em></h1><p className="landing-hero__copy">InvenTrack helps your team track stock levels, manage suppliers, and generate reports — all from one clean workspace.</p><div className="landing-actions"><Link className="landing-btn primary" to="/register">Start for Free</Link><Link className="landing-btn outline" to="/login">Sign In</Link></div><p className="trust-line"><span>✓ No credit card required</span><b>·</b><span>✓ Free to use</span><b>·</b><span>✓ Setup in minutes</span></p></section>
   <section className="preview-section"><p className="preview-kicker">Everything your team needs, in one place</p><DashboardPreview/></section>
   <section className="landing-features" id="features"><div className="landing-section-head"><h2>Built for teams that manage real inventory</h2><p>From warehouse staff to business owners — InvenTrack works for every role.</p></div><div className="feature-grid">{features.map(([type,title,body])=><article className={`landing-feature ${type}`} key={title}><div className="landing-feature__icon"><Icon type={type}/></div><h3>{title}</h3><p>{body}</p></article>)}</div></section>
   <section className="how-section" id="how"><h2>Get started in minutes</h2><div className="steps">{[['Create your account','Sign up as staff in seconds. Admins can set up the full workspace.'],['Add your inventory','Add products, categories, and suppliers — import or enter manually.'],['Track and report','Monitor stock levels, get low-stock alerts, and generate reports.']].map(([title,body],i)=><article className="step" key={title}><div className="step-top"><span>{i+1}</span>{i<2&&<i/>}</div><h3>{title}</h3><p>{body}</p></article>)}</div></section>
   <section className="stats-section" id="pricing"><div>{[['10,000+','Products tracked daily'],['99.9%','Uptime reliability'],['3 Roles','Admin, Manager, Staff'],['Free','No subscription needed']].map(([number,label],i)=><article key={label}><strong>{number}</strong><span>{label}</span>{i<3&&<i/>}</article>)}</div></section>
   <section className="final-cta"><h2>Ready to take control of your inventory?</h2><p>Join your team on InvenTrack today.</p><div className="landing-actions"><Link className="landing-btn primary" to="/register">Create Staff Account</Link><Link className="landing-btn outline" to="/login">Sign In</Link></div></section>
  </main>
  <footer className="landing-footer"><div><Brand/><p>© 2026 InvenTrack. All rights reserved.</p></div><nav><a href="#privacy">Privacy Policy</a><a href="#terms">Terms of Service</a><a href="mailto:support@inventtrack.local">Contact</a></nav></footer>
 </div>
}

function DashboardPreview(){return <div className="dashboard-preview"><header><div><span>I</span><strong>Dashboard</strong></div><i/></header><div className="preview-content"><div className="preview-kpis">{[['Total Products','11'],['Stock Value','₹19,19,897'],['Low Stock','5'],['Categories','4']].map(([label,value],i)=><div key={label}><span>{label}</span><strong>{value}</strong><i className={`preview-dot dot-${i}`}/></div>)}</div><div className="preview-panels"><section><header>Products by Category</header><div className="preview-chart">{[44,72,55,86,62,38].map((height,i)=><i key={i} style={{height:`${height}%`}}/>)}</div></section><section><header>Low Stock Alerts</header>{[['Wireless Mouse','3 remaining'],['Printer Paper','8 remaining'],['Office Chair','4 remaining']].map(([name,count])=><div className="preview-alert" key={name}><span><b>{name}</b><small>{count}</small></span><em>LOW STOCK</em></div>)}</section></div></div></div>}
