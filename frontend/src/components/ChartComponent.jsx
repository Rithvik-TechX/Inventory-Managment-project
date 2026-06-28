import { BarChart, Bar, Cell, CartesianGrid, ResponsiveContainer, Tooltip, XAxis, YAxis } from 'recharts';

const COLORS=['#4F46E5','#06B6D4','#10B981','#F59E0B','#EF4444','#8B5CF6'];
function TooltipCard({active,payload}){if(!active||!payload?.length)return null;const item=payload[0].payload;return <div className="category-tooltip"><strong>{item.label}</strong><span>{item.value} products</span></div>}
export default function ChartComponent({data=[],title,height=240}){
 if(!data.length)return <div className="chart-empty">No category data available</div>;
 return <div className="category-chart">{title&&<div className="category-chart__title">{title}</div>}<ResponsiveContainer width="100%" height={Math.max(240,height)}><BarChart data={data} margin={{top:18,right:16,left:-12,bottom:60}}><CartesianGrid vertical={false} stroke="var(--border)" strokeDasharray="3 3"/><XAxis dataKey="label" interval={0} angle={-30} textAnchor="end" height={66} tick={{fill:'var(--text-secondary)',fontSize:11}} axisLine={false} tickLine={false}/><YAxis allowDecimals={false} tick={{fill:'var(--text-muted)',fontSize:10}} axisLine={false} tickLine={false}/><Tooltip content={<TooltipCard/>}/><Bar dataKey="value" radius={[5,5,0,0]} maxBarSize={46}>{data.map((_,index)=><Cell key={index} fill={COLORS[index%COLORS.length]}/>)}</Bar></BarChart></ResponsiveContainer></div>
}
