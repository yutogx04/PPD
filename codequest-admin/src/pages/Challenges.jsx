import { useState, useEffect } from 'react';
import Topbar from '../components/Topbar';
import { FaPlus, FaPenToSquare, FaTrash, FaMagnifyingGlass } from 'react-icons/fa6';
import API from '../api/client';

const diffLabels = { BEGINNER: 'Facile', INTERMEDIATE: 'Moyen', ADVANCED: 'Difficile' };
const diffClass  = { BEGINNER: 'easy',   INTERMEDIATE: 'medium', ADVANCED: 'hard'     };

const emptyForm = { title: '', description: '', moduleId: '', difficulty: 'BEGINNER', language: 'PYTHON', starterCode: '', xpReward: 30 };

export default function Challenges() {
  const [challenges, setChallenges] = useState([]);
  const [modules,    setModules]    = useState([]);
  const [search,     setSearch]     = useState('');
  const [showCreate, setShowCreate] = useState(false);
  const [showEdit,   setShowEdit]   = useState(false);
  const [form,       setForm]       = useState(emptyForm);
  const [editTarget, setEditTarget] = useState(null);
  const [loading,    setLoading]    = useState(true);
  const [saving,     setSaving]     = useState(false);
  const [toast,      setToast]      = useState('');

  const showToast = (msg, ok = true) => { setToast({ msg, ok }); setTimeout(() => setToast(''), 3000); };

  const fetchChallenges = async () => {
    try { const res = await API.get('/admin/challenges'); setChallenges(res.data); }
    catch (err) { console.error(err); }
    finally { setLoading(false); }
  };

  const fetchModules = async () => {
    try { const res = await API.get('/admin/modules'); setModules(res.data); }
    catch (err) { console.error(err); }
  };

  useEffect(() => { fetchChallenges(); fetchModules(); }, []);

  const filtered = challenges.filter(c => c.title.toLowerCase().includes(search.toLowerCase()));

  const handleCreate = async () => {
    setSaving(true);
    try {
      await API.post('/admin/challenges', { ...form, moduleId: parseInt(form.moduleId) });
      setShowCreate(false); setForm(emptyForm); fetchChallenges();
      showToast('Défi créé ✓');
    } catch (err) { showToast('Erreur: ' + (err.response?.data?.message || err.message), false); }
    finally { setSaving(false); }
  };

  const openEdit = (challenge) => {
    setEditTarget(challenge);
    setForm({
      title: challenge.title,
      description: challenge.description || '',
      moduleId: '', // no moduleId in list response, user can change if needed
      difficulty: challenge.difficulty,
      language: challenge.language,
      starterCode: challenge.starterCode || '',
      xpReward: challenge.xpReward,
    });
    setShowEdit(true);
  };

  const handleEdit = async () => {
    if (!editTarget) return;
    setSaving(true);
    try {
      const payload = { ...form };
      if (!form.moduleId) delete payload.moduleId; // don't update module if not selected
      else payload.moduleId = parseInt(form.moduleId);
      await API.put(`/admin/challenges/${editTarget.id}`, payload);
      setShowEdit(false); setEditTarget(null); setForm(emptyForm); fetchChallenges();
      showToast('Défi mis à jour ✓');
    } catch (err) { showToast('Erreur: ' + (err.response?.data?.message || err.message), false); }
    finally { setSaving(false); }
  };

  const handleDelete = async (id) => {
    if (!confirm('Supprimer ce défi ?')) return;
    try { await API.delete(`/admin/challenges/${id}`); fetchChallenges(); showToast('Supprimé'); }
    catch (err) { showToast('Erreur: ' + err.message, false); }
  };

  const ModuleSelect = ({ required }) => (
    <select className="form-select" value={form.moduleId} onChange={e => setForm({ ...form, moduleId: e.target.value })}>
      <option value="">{required ? '— Choisir un module —' : '— Ne pas changer —'}</option>
      {modules.map(m => (
        <option key={m.id} value={m.id}>{m.trackName} → {m.title}</option>
      ))}
    </select>
  );

  const Modal = ({ title, onSave, onClose, isEdit }) => (
    <div className="modal-overlay" onClick={onClose}>
      <div className="modal" style={{ maxWidth: '600px' }} onClick={e => e.stopPropagation()}>
        <div className="modal-header">
          <h3>{title}</h3>
          <button className="modal-close" onClick={onClose}>✕</button>
        </div>
        <div className="modal-body">
          <div className="form-group">
            <label>Titre du défi</label>
            <input className="form-input" value={form.title} onChange={e => setForm({ ...form, title: e.target.value })} placeholder="ex: Inverser une chaîne" />
          </div>
          <div className="form-group">
            <label>Module {isEdit && <span style={{ color:'var(--text-muted)', fontSize:'12px' }}>(laisser vide pour ne pas changer)</span>}</label>
            <ModuleSelect required={!isEdit} />
          </div>
          <div className="form-group">
            <label>Langage</label>
            <select className="form-select" value={form.language} onChange={e => setForm({ ...form, language: e.target.value })}>
              <option value="PYTHON">Python</option>
              <option value="JAVASCRIPT">JavaScript</option>
              <option value="JAVA">Java</option>
            </select>
          </div>
          <div className="form-group">
            <label>Difficulté</label>
            <select className="form-select" value={form.difficulty} onChange={e => setForm({ ...form, difficulty: e.target.value })}>
              <option value="BEGINNER">Facile</option>
              <option value="INTERMEDIATE">Moyen</option>
              <option value="ADVANCED">Difficile</option>
            </select>
          </div>
          <div className="form-group">
            <label>Énoncé</label>
            <textarea className="form-textarea" value={form.description} onChange={e => setForm({ ...form, description: e.target.value })} placeholder="Décris le problème..." />
          </div>
          <div className="form-group">
            <label>Code initial</label>
            <textarea className="form-textarea" style={{ fontFamily:"'JetBrains Mono', monospace", fontSize:'12px' }}
              value={form.starterCode} onChange={e => setForm({ ...form, starterCode: e.target.value })}
              placeholder={"def solve():\n    pass"} />
          </div>
          <div className="form-group">
            <label>XP</label>
            <input className="form-input" type="number" value={form.xpReward} onChange={e => setForm({ ...form, xpReward: parseInt(e.target.value) || 0 })} />
          </div>
        </div>
        <div className="modal-footer">
          <button className="btn btn-outline" onClick={onClose}>Annuler</button>
          <button className="btn btn-primary" onClick={onSave} disabled={saving || (!isEdit && !form.moduleId)}>{saving ? 'Enregistrement...' : 'Enregistrer'}</button>
        </div>
      </div>
    </div>
  );

  return (
    <>
      <Topbar title="Défis" />
      <div className="page-content">
        {toast && (
          <div style={{ position:'fixed', top:'20px', right:'20px', zIndex:9999, background: toast.ok ? '#22c55e' : '#ef4444', color:'white', padding:'12px 24px', borderRadius:'8px', fontWeight:600, fontSize:'14px', boxShadow:'0 4px 12px rgba(0,0,0,0.15)' }}>
            {toast.msg}
          </div>
        )}
        <div className="page-header">
          <div className="search-input">
            <FaMagnifyingGlass className="search-icon" />
            <input placeholder="Rechercher un défi..." value={search} onChange={e => setSearch(e.target.value)} />
          </div>
          <button className="btn btn-primary" onClick={() => { setForm(emptyForm); setShowCreate(true); }}>
            <FaPlus /> Ajouter un défi
          </button>
        </div>

        <div className="table-card">
          {loading ? <div className="empty-state"><p>Chargement...</p></div> : (
            <table className="data-table">
              <thead>
                <tr><th>Titre</th><th>Parcours</th><th>Difficulté</th><th>XP</th><th>Tests</th><th>Soumissions</th><th>Succès</th><th>Actions</th></tr>
              </thead>
              <tbody>
                {filtered.length === 0 ? (
                  <tr><td colSpan={8} style={{ textAlign:'center', color:'var(--text-muted)' }}>Aucun défi</td></tr>
                ) : filtered.map(c => (
                  <tr key={c.id}>
                    <td><strong>{c.title}</strong></td>
                    <td><span className={`lang-badge ${c.language.toLowerCase()}`}>{c.trackName}</span></td>
                    <td><span className={`diff-badge ${diffClass[c.difficulty]}`}>{diffLabels[c.difficulty]}</span></td>
                    <td>+{c.xpReward} XP</td>
                    <td>{c.testCases}</td>
                    <td>{c.submissions}</td>
                    <td>{c.successRate}</td>
                    <td>
                      <div className="action-btns">
                        <button className="btn btn-outline btn-sm" title="Modifier" onClick={() => openEdit(c)}><FaPenToSquare /></button>
                        <button className="btn btn-danger btn-sm" title="Supprimer" onClick={() => handleDelete(c.id)}><FaTrash /></button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          )}
        </div>

        {showCreate && <Modal title="Nouveau défi" onSave={handleCreate} onClose={() => setShowCreate(false)} isEdit={false} />}
        {showEdit   && <Modal title={`Modifier — ${editTarget?.title}`} onSave={handleEdit} onClose={() => setShowEdit(false)} isEdit={true} />}
      </div>
    </>
  );
}
