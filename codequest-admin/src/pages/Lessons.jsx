import { useState, useEffect } from 'react';
import Topbar from '../components/Topbar';
import { FaPlus, FaPenToSquare, FaTrash, FaMagnifyingGlass } from 'react-icons/fa6';
import API from '../api/client';

const emptyForm = { title: '', moduleId: '', type: 'THEORY', xpReward: 20, durationMinutes: 5 };

export default function Lessons() {
  const [lessons,    setLessons]    = useState([]);
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

  const fetchLessons = async () => {
    try { const res = await API.get('/admin/lessons'); setLessons(res.data); }
    catch (err) { console.error(err); }
    finally { setLoading(false); }
  };

  const fetchModules = async () => {
    try { const res = await API.get('/admin/modules'); setModules(res.data); }
    catch (err) { console.error(err); }
  };

  useEffect(() => { fetchLessons(); fetchModules(); }, []);

  const filtered = lessons.filter(l =>
    l.title.toLowerCase().includes(search.toLowerCase()) ||
    (l.trackName || '').toLowerCase().includes(search.toLowerCase())
  );

  const handleCreate = async () => {
    setSaving(true);
    try {
      await API.post('/admin/lessons', { ...form, moduleId: parseInt(form.moduleId) });
      setShowCreate(false); setForm(emptyForm); fetchLessons();
      showToast('Leçon créée ✓');
    } catch (err) { showToast('Erreur: ' + (err.response?.data?.message || err.message), false); }
    finally { setSaving(false); }
  };

  const openEdit = (lesson) => {
    setEditTarget(lesson);
    setForm({ title: lesson.title, moduleId: lesson.moduleId, type: lesson.type, xpReward: lesson.xpReward, durationMinutes: lesson.durationMinutes || 5 });
    setShowEdit(true);
  };

  const handleEdit = async () => {
    if (!editTarget) return;
    setSaving(true);
    try {
      await API.put(`/admin/lessons/${editTarget.id}`, { ...form, moduleId: parseInt(form.moduleId) });
      setShowEdit(false); setEditTarget(null); setForm(emptyForm); fetchLessons();
      showToast('Leçon mise à jour ✓');
    } catch (err) { showToast('Erreur: ' + (err.response?.data?.message || err.message), false); }
    finally { setSaving(false); }
  };

  const handleDelete = async (id) => {
    if (!confirm('Supprimer cette leçon ?')) return;
    try { await API.delete(`/admin/lessons/${id}`); fetchLessons(); showToast('Supprimée'); }
    catch (err) { showToast('Erreur: ' + err.message, false); }
  };

  const ModuleSelect = () => (
    <select className="form-select" value={form.moduleId} onChange={e => setForm({ ...form, moduleId: e.target.value })}>
      <option value="">— Choisir un module —</option>
      {modules.map(m => (
        <option key={m.id} value={m.id}>{m.trackName} → {m.title}</option>
      ))}
    </select>
  );

  const Modal = ({ title, onSave, onClose }) => (
    <div className="modal-overlay" onClick={onClose}>
      <div className="modal" onClick={e => e.stopPropagation()}>
        <div className="modal-header">
          <h3>{title}</h3>
          <button className="modal-close" onClick={onClose}>✕</button>
        </div>
        <div className="modal-body">
          <div className="form-group">
            <label>Titre de la leçon</label>
            <input className="form-input" value={form.title} onChange={e => setForm({ ...form, title: e.target.value })} placeholder="ex: Variables et Types" />
          </div>
          <div className="form-group">
            <label>Module</label>
            <ModuleSelect />
          </div>
          <div className="form-group">
            <label>Type</label>
            <select className="form-select" value={form.type} onChange={e => setForm({ ...form, type: e.target.value })}>
              <option value="THEORY">Théorie</option>
              <option value="PRACTICE">Pratique</option>
            </select>
          </div>
          <div className="form-group">
            <label>XP</label>
            <input className="form-input" type="number" value={form.xpReward} onChange={e => setForm({ ...form, xpReward: parseInt(e.target.value) || 0 })} />
          </div>
          <div className="form-group">
            <label>Durée (minutes)</label>
            <input className="form-input" type="number" value={form.durationMinutes} onChange={e => setForm({ ...form, durationMinutes: parseInt(e.target.value) || 0 })} />
          </div>
        </div>
        <div className="modal-footer">
          <button className="btn btn-outline" onClick={onClose}>Annuler</button>
          <button className="btn btn-primary" onClick={onSave} disabled={saving || !form.moduleId}>{saving ? 'Enregistrement...' : 'Enregistrer'}</button>
        </div>
      </div>
    </div>
  );

  return (
    <>
      <Topbar title="Leçons" />
      <div className="page-content">
        {toast && (
          <div style={{ position:'fixed', top:'20px', right:'20px', zIndex:9999, background: toast.ok ? '#22c55e' : '#ef4444', color:'white', padding:'12px 24px', borderRadius:'8px', fontWeight:600, fontSize:'14px', boxShadow:'0 4px 12px rgba(0,0,0,0.15)' }}>
            {toast.msg}
          </div>
        )}
        <div className="page-header">
          <div className="search-input">
            <FaMagnifyingGlass className="search-icon" />
            <input placeholder="Rechercher une leçon..." value={search} onChange={e => setSearch(e.target.value)} />
          </div>
          <button className="btn btn-primary" onClick={() => { setForm(emptyForm); setShowCreate(true); }}>
            <FaPlus /> Ajouter une leçon
          </button>
        </div>

        <div className="table-card">
          {loading ? <div className="empty-state"><p>Chargement...</p></div> : (
            <table className="data-table">
              <thead>
                <tr><th>Titre</th><th>Parcours</th><th>Module</th><th>Type</th><th>Slides</th><th>XP</th><th>Actions</th></tr>
              </thead>
              <tbody>
                {filtered.length === 0 ? (
                  <tr><td colSpan={7} style={{ textAlign:'center', color:'var(--text-muted)' }}>Aucune leçon</td></tr>
                ) : filtered.map(l => (
                  <tr key={l.id}>
                    <td><strong>{l.title}</strong></td>
                    <td><span className={`lang-badge ${(l.trackName||'').toLowerCase()}`}>{l.trackName}</span></td>
                    <td>{l.moduleName}</td>
                    <td><span className={`diff-badge ${l.type === 'THEORY' ? 'easy' : 'medium'}`}>{l.type === 'THEORY' ? 'Théorie' : 'Pratique'}</span></td>
                    <td>{l.slides}</td>
                    <td>+{l.xpReward} XP</td>
                    <td>
                      <div className="action-btns">
                        <button className="btn btn-outline btn-sm" title="Modifier" onClick={() => openEdit(l)}><FaPenToSquare /></button>
                        <button className="btn btn-danger btn-sm" title="Supprimer" onClick={() => handleDelete(l.id)}><FaTrash /></button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          )}
        </div>

        {showCreate && <Modal title="Nouvelle leçon" onSave={handleCreate} onClose={() => setShowCreate(false)} />}
        {showEdit   && <Modal title={`Modifier — ${editTarget?.title}`} onSave={handleEdit} onClose={() => setShowEdit(false)} />}
      </div>
    </>
  );
}
