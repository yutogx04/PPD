import { FaShieldHalved } from 'react-icons/fa6';

export default function Topbar({ title }) {
  return (
    <div className="topbar">
      <h1>{title}</h1>
      <div className="topbar-user">
        <span>Administrateur</span>
        <div className="topbar-avatar">
          <FaShieldHalved />
        </div>
      </div>
    </div>
  );
}
