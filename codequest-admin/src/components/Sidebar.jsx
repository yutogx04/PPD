import { NavLink } from 'react-router-dom';
import { FaBolt, FaChartLine, FaRoute, FaFileLines, FaTerminal, FaUsers, FaMedal, FaGear, FaRightFromBracket } from 'react-icons/fa6';

const navItems = [
  { to: '/', icon: FaChartLine, label: "Vue d'ensemble" },
  { to: '/tracks', icon: FaRoute, label: 'Parcours' },
  { to: '/lessons', icon: FaFileLines, label: 'Leçons' },
  { to: '/challenges', icon: FaTerminal, label: 'Défis' },
  { to: '/users', icon: FaUsers, label: 'Utilisateurs' },
  { to: '/badges', icon: FaMedal, label: 'Badges' },
];

export default function Sidebar({ onLogout }) {
  return (
    <aside className="sidebar">
      <div className="sidebar-logo">
        <FaBolt className="logo-icon" />
        <span>CodeQuest</span>
      </div>
      <nav className="sidebar-nav">
        <div className="sidebar-nav-main">
          {navItems.map(item => (
            <NavLink
              key={item.to}
              to={item.to}
              end={item.to === '/'}
              className={({ isActive }) => `nav-item ${isActive ? 'active' : ''}`}
            >
              <item.icon className="nav-icon" />
              <span>{item.label}</span>
            </NavLink>
          ))}
        </div>
        <div className="sidebar-bottom">
          <NavLink to="/settings" className={({ isActive }) => `nav-item ${isActive ? 'active' : ''}`}>
            <FaGear className="nav-icon" />
            <span>Paramètres</span>
          </NavLink>
          {onLogout && (
            <button className="nav-item" onClick={onLogout} style={{ border: 'none', background: 'none', cursor: 'pointer', width: '100%', textAlign: 'left', color: 'inherit', font: 'inherit', padding: '10px 16px', display: 'flex', alignItems: 'center', gap: '12px' }}>
              <FaRightFromBracket className="nav-icon" />
              <span>Déconnexion</span>
            </button>
          )}
        </div>
      </nav>
    </aside>
  );
}
