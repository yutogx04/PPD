import { useState, useEffect } from 'react';
import Topbar from '../components/Topbar';
import { FaMagnifyingGlass, FaBan, FaShieldHalved } from 'react-icons/fa6';
import API from '../api/client';

export default function Users() {
  const [users, setUsers] = useState([]);
  const [search, setSearch] = useState('');
  const [loading, setLoading] = useState(true);

  const fetchUsers = async () => {
    try {
      const res = await API.get('/admin/users');
      setUsers(res.data);
    } catch (err) { console.error(err); }
    finally { setLoading(false); }
  };

  useEffect(() => { fetchUsers(); }, []);

  const filtered = users.filter(u =>
    u.pseudo.toLowerCase().includes(search.toLowerCase()) ||
    u.email.toLowerCase().includes(search.toLowerCase())
  );

  const handleRoleToggle = async (id, currentRole) => {
    const newRole = currentRole === 'ADMIN' ? 'USER' : 'ADMIN';
    if (!confirm(`Changer le rôle en ${newRole} ?`)) return;
    try {
      await API.put(`/admin/users/${id}/role`, { role: newRole });
      fetchUsers();
    } catch (err) { alert('Erreur: ' + err.message); }
  };

  const handleToggleActive = async (id) => {
    try {
      await API.put(`/admin/users/${id}/toggle-active`);
      fetchUsers();
    } catch (err) { alert('Erreur: ' + err.message); }
  };

  return (
    <>
      <Topbar title="Utilisateurs" />
      <div className="page-content">
        <div className="page-header">
          <div className="search-input">
            <FaMagnifyingGlass className="search-icon" />
            <input placeholder="Rechercher un utilisateur..." value={search} onChange={e => setSearch(e.target.value)} />
          </div>
          <span style={{ fontSize: '13px', color: 'var(--text-muted)' }}>{filtered.length} utilisateur{filtered.length > 1 ? 's' : ''}</span>
        </div>

        <div className="table-card">
          {loading ? (
            <div className="empty-state"><p>Chargement...</p></div>
          ) : (
            <table className="data-table">
              <thead>
                <tr>
                  <th>Utilisateur</th>
                  <th>Email</th>
                  <th>Niveau</th>
                  <th>XP</th>
                  <th>Streak</th>
                  <th>Leçons</th>
                  <th>Défis</th>
                  <th>Rôle</th>
                  <th>Statut</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {filtered.length === 0 ? (
                  <tr><td colSpan={10} style={{ textAlign: 'center', color: 'var(--text-muted)' }}>Aucun utilisateur</td></tr>
                ) : (
                  filtered.map(u => (
                    <tr key={u.id}>
                      <td><strong>{u.pseudo}</strong></td>
                      <td style={{ color: 'var(--text-muted)', fontSize: '12px' }}>{u.email}</td>
                      <td>{u.level}</td>
                      <td style={{ fontWeight: 600, color: 'var(--primary)' }}>{(u.xp || 0).toLocaleString()}</td>
                      <td>🔥 {u.streak}</td>
                      <td>{u.totalLessonsCompleted}</td>
                      <td>{u.totalChallengesSolved}</td>
                      <td><span className={`status-badge ${u.role === 'ADMIN' ? 'accepted' : 'tle'}`}>{u.role}</span></td>
                      <td><span className={`status-badge ${u.enabled ? 'accepted' : 'wrong'}`}>{u.enabled ? 'Actif' : 'Désactivé'}</span></td>
                      <td>
                        <div className="action-btns">
                          <button className="btn btn-outline btn-sm" title={u.role === 'ADMIN' ? 'Rétrograder' : 'Promouvoir admin'} onClick={() => handleRoleToggle(u.id, u.role)}><FaShieldHalved /></button>
                          <button className="btn btn-danger btn-sm" title={u.enabled ? 'Désactiver' : 'Réactiver'} onClick={() => handleToggleActive(u.id)}><FaBan /></button>
                        </div>
                      </td>
                    </tr>
                  ))
                )}
              </tbody>
            </table>
          )}
        </div>
      </div>
    </>
  );
}
