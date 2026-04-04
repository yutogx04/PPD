import { useState, useEffect } from 'react';
import Topbar from '../components/Topbar';
import { FaPlus, FaPenToSquare, FaTrash, FaMagnifyingGlass } from 'react-icons/fa6';
import { FaFire, FaCode, FaBullseye, FaHeart, FaShieldAlt, FaCompass, FaMoon, FaPython, FaTrophy, FaWalking, FaMedal } from 'react-icons/fa';
import API from '../api/client';

const conditionLabels = {
  FIRST_LESSON:         'Compléter sa 1ère leçon',
  FIRST_CHALLENGE:      'Résoudre son 1er défi',
  FIRST_ATTEMPT_SUCCESS:'Réussir un défi du 1er essai',
  STREAK_7:             'Maintenir un streak de 7 jours',
  STREAK_30:            'Maintenir un streak de 30 jours',
  CHALLENGES_10:        'Résoudre 10 défis',
  TRACKS_2:             'Commencer 2 parcours différents',
  NIGHT_SUBMIT:         'Soumettre après minuit',
  TRACK_PYTHON_COMPLETE:'Terminer le parcours Python',
  LEVEL_6:              'Atteindre le niveau 6',
};

const iconMap = {
  user_plus:  { component: FaWalking,  color: '#7C3AED' },
  code:       { component: FaCode,     color: '#6366F1' },
  fire:       { component: FaFire,     color: '#F59E0B' },
  bullseye:   { component: FaBullseye, color: '#EF4444' },
  heart_pulse:{ component: FaHeart,    color: '#EC4899' },
  sword:      { component: FaShieldAlt,color: '#8B5CF6' },
  compass:    { component: FaCompass,  color: '#14B8A6' },
  moon:       { component: FaMoon,     color: '#6366F1' },
  snake:      { component: FaPython,   color: '#10B981' },
  trophy:     { component: FaTrophy,   color: '#F59E0B' },
};

const iconOptions = [
  { value: 'user_plus',    label: 'Premier Pas (marche)' },
  { value: 'code',         label: 'Code (crochets)' },
  { value: 'fire',         label: 'Flamme (streak)' },
  { value: 'bullseye',     label: 'Cible (précision)' },
  { value: 'heart_pulse',  label: 'Cœur (endurance)' },
  { value: 'sword',        label: 'Bouclier (défi)' },
  { value: 'compass',      label: 'Boussole (exploration)' },
  { value: 'moon',         label: 'Lune (nuit)' },
  { value: 'snake',        label: 'Python (langage)' },
  { value: 'trophy',       label: 'Trophée (élite)' },
];

function BadgeIcon({ iconName, size = 22 }) {
  const mapping = iconMap[iconName];
  if (mapping) {
    const IconComponent = mapping.component;
    return <IconComponent size={size} color={mapping.color} />;
  }
  return <FaMedal size={size} color="#7C3AED" />;
}

const emptyForm = { name: '', description: '', icon: '', conditionType: 'FIRST_LESSON', conditionValue: 0 };

export default function Badges() {
  const [badges,     setBadges]     = useState([]);
  const [search,     setSearch]     = useState('');
  const [showCreate, setShowCreate] = useState(false);
  const [showEdit,   setShowEdit]   = useState(false);
  const [form,       setForm]       = useState(emptyForm);
  const [editTarget, setEditTarget] = useState(null);
  const [loading,    setLoading]    = useState(true);
  const [saving,     setSaving]     = useState(false);
  const [toast,      setToast]      = useState('');

  const showToast = (msg, ok = true) => { setToast({ msg, ok }); setTimeout(() => setToast(''), 3000); };

  const fetchBadges = async () => {
    try { const res = await API.get('/admin/badges'); setBadges(res.data); }
    catch (err) { console.error(err); }
    finally { setLoading(false); }
  };

  useEffect(() => { fetchBadges(); }, []);

  const filtered = badges.filter(b => b.name.toLowerCase().includes(search.toLowerCase()));

  const handleCreate = async () => {
    setSaving(true);
    try {
      await API.post('/admin/badges', form);
      setShowCreate(false); setForm(emptyForm); fetchBadges();
      showToast('Badge créé ✓');
    } catch (err) { showToast('Erreur: ' + (err.response?.data?.message || err.message), false); }
    finally { setSaving(false); }
  };

  const openEdit = (badge) => {
    setEditTarget(badge);
    setForm({ name: badge.name, description: badge.description || '', icon: badge.icon || '', conditionType: badge.conditionType, conditionValue: badge.conditionValue || 0 });
    setShowEdit(true);
  };

  const handleEdit = async () => {
    if (!editTarget) return;
    setSaving(true);
    try {
      await API.put(`/admin/badges/${editTarget.id}`, form);
      setShowEdit(false); setEditTarget(null); setForm(emptyForm); fetchBadges();
      showToast('Badge mis à jour ✓');
    } catch (err) { showToast('Erreur: ' + (err.response?.data?.message || err.message), false); }
    finally { setSaving(false); }
  };

  const handleDelete = async (id) => {
    if (!confirm('Supprimer ce badge ?')) return;
    try { await API.delete(`/admin/badges/${id}`); fetchBadges(); showToast('Supprimé'); }
    catch (err) { showToast('Erreur: ' + err.message, false); }
  };

  const FormFields = () => (
    <>
      <div className="form-group">
        <label>Nom du badge</label>
        <input className="form-input" value={form.name} onChange={e => setForm({ ...form, name: e.target.value })} placeholder="ex: Assidu" />
      </div>
      <div className="form-group">
        <label>Icône</label>
        <div style={{ display:'flex', alignItems:'center', gap:'12px' }}>
          <select className="form-select" value={form.icon} onChange={e => setForm({ ...form, icon: e.target.value })} style={{ flex:1 }}>
            <option value="">— Choisir une icône —</option>
            {iconOptions.map(o => <option key={o.value} value={o.value}>{o.label}</option>)}
          </select>
          {form.icon && (
            <div style={{ width:'36px', height:'36px', display:'flex', alignItems:'center', justifyContent:'center', background:'var(--bg-card)', borderRadius:'8px', border:'1px solid var(--border)' }}>
              <BadgeIcon iconName={form.icon} size={20} />
            </div>
          )}
        </div>
      </div>
      <div className="form-group">
        <label>Condition</label>
        <select className="form-select" value={form.conditionType} onChange={e => setForm({ ...form, conditionType: e.target.value })}>
          {Object.entries(conditionLabels).map(([key, label]) => (
            <option key={key} value={key}>{label}</option>
          ))}
        </select>
      </div>
      <div className="form-group">
        <label>Description</label>
        <textarea className="form-textarea" value={form.description} onChange={e => setForm({ ...form, description: e.target.value })} placeholder="Description du badge..." />
      </div>
    </>
  );

  const Modal = ({ title, onSave, onClose }) => (
    <div className="modal-overlay" onClick={onClose}>
      <div className="modal" onClick={e => e.stopPropagation()}>
        <div className="modal-header">
          <h3>{title}</h3>
          <button className="modal-close" onClick={onClose}>✕</button>
        </div>
        <div className="modal-body"><FormFields /></div>
        <div className="modal-footer">
          <button className="btn btn-outline" onClick={onClose}>Annuler</button>
          <button className="btn btn-primary" onClick={onSave} disabled={saving || !form.name}>{saving ? 'Enregistrement...' : 'Enregistrer'}</button>
        </div>
      </div>
    </div>
  );

  return (
    <>
      <Topbar title="Badges" />
      <div className="page-content">
        {toast && (
          <div style={{ position:'fixed', top:'20px', right:'20px', zIndex:9999, background: toast.ok ? '#22c55e' : '#ef4444', color:'white', padding:'12px 24px', borderRadius:'8px', fontWeight:600, fontSize:'14px', boxShadow:'0 4px 12px rgba(0,0,0,0.15)' }}>
            {toast.msg}
          </div>
        )}
        <div className="page-header">
          <div className="search-input">
            <FaMagnifyingGlass className="search-icon" />
            <input placeholder="Rechercher un badge..." value={search} onChange={e => setSearch(e.target.value)} />
          </div>
          <button className="btn btn-primary" onClick={() => { setForm(emptyForm); setShowCreate(true); }}>
            <FaPlus /> Ajouter un badge
          </button>
        </div>

        <div className="table-card">
          {loading ? <div className="empty-state"><p>Chargement...</p></div> : (
            <table className="data-table">
              <thead>
                <tr><th>Icône</th><th>Nom</th><th>Condition</th><th>Détenteurs</th><th>Actions</th></tr>
              </thead>
              <tbody>
                {filtered.length === 0 ? (
                  <tr><td colSpan={5} style={{ textAlign:'center', color:'var(--text-muted)' }}>Aucun badge</td></tr>
                ) : filtered.map(b => (
                  <tr key={b.id}>
                    <td><BadgeIcon iconName={b.icon} size={24} /></td>
                    <td><strong>{b.name}</strong></td>
                    <td style={{ color:'var(--text-secondary)' }}>{conditionLabels[b.conditionType] || b.description}</td>
                    <td style={{ fontWeight:600 }}>{b.holders}</td>
                    <td>
                      <div className="action-btns">
                        <button className="btn btn-outline btn-sm" title="Modifier" onClick={() => openEdit(b)}><FaPenToSquare /></button>
                        <button className="btn btn-danger btn-sm" title="Supprimer" onClick={() => handleDelete(b.id)}><FaTrash /></button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          )}
        </div>

        {showCreate && <Modal title="Nouveau badge" onSave={handleCreate} onClose={() => setShowCreate(false)} />}
        {showEdit   && <Modal title={`Modifier — ${editTarget?.name}`} onSave={handleEdit} onClose={() => setShowEdit(false)} />}
      </div>
    </>
  );
}
