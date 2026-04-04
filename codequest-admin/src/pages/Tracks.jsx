import { useState, useEffect } from 'react';
import Topbar from '../components/Topbar';
import { FaPlus, FaPenToSquare, FaTrash, FaMagnifyingGlass } from 'react-icons/fa6';
import API from '../api/client';

const diffLabels = { BEGINNER: 'Débutant', INTERMEDIATE: 'Intermédiaire', ADVANCED: 'Avancé' };
const diffClass  = { BEGINNER: 'easy',     INTERMEDIATE: 'medium',        ADVANCED: 'hard'   };

const emptyForm = { title: '', description: '', language: 'PYTHON', difficulty: 'BEGINNER' };

export default function Tracks() {
  const [tracks,       setTracks]       = useState([]);
  const [search,       setSearch]       = useState('');
  const [showCreate,   setShowCreate]   = useState(false);
  const [showEdit,     setShowEdit]     = useState(false);
  const [form,         setForm]         = useState(emptyForm);
  const [editTarget,   setEditTarget]   = useState(null);
  const [loading,      setLoading]      = useState(true);
  const [saving,       setSaving]       = useState(false);
  const [toast,        setToast]        = useState('');

  const showToast = (msg, ok = true) => {
    setToast({ msg, ok });
    setTimeout(() => setToast(''), 3000);
  };

  const fetchTracks = async () => {
    try { const res = await API.get('/admin/tracks'); setTracks(res.data); }
    catch (err) { console.error(err); }
    finally { setLoading(false); }
  };

  useEffect(() => { fetchTracks(); }, []);

  const filtered = tracks.filter(t => t.title.toLowerCase().includes(search.toLowerCase()));

  const handleCreate = async () => {
    setSaving(true);
    try {
      await API.post('/admin/tracks', form);
      setShowCreate(false);
      setForm(emptyForm);
      fetchTracks();
      showToast('Parcours créé ✓');
    } catch (err) { showToast('Erreur: ' + (err.response?.data?.message || err.message), false); }
    finally { setSaving(false); }
  };

  const openEdit = (track) => {
    setEditTarget(track);
    setForm({ title: track.title, description: track.description || '', language: track.language, difficulty: track.difficulty });
    setShowEdit(true);
  };

  const handleEdit = async () => {
    if (!editTarget) return;
    setSaving(true);
    try {
      await API.put(`/admin/tracks/${editTarget.id}`, form);
      setShowEdit(false);
      setEditTarget(null);
      setForm(emptyForm);
      fetchTracks();
      showToast('Parcours mis à jour ✓');
    } catch (err) { showToast('Erreur: ' + (err.response?.data?.message || err.message), false); }
    finally { setSaving(false); }
  };

  const handleDelete = async (id) => {
    if (!confirm('Supprimer ce parcours et tout son contenu ?')) return;
    try { await API.delete(`/admin/tracks/${id}`); fetchTracks(); showToast('Supprimé'); }
    catch (err) { showToast('Erreur: ' + err.message, false); }
  };

  const Modal = ({ title, onSave, onClose }) => (
    <div className="modal-overlay" onClick={onClose}>
      <div className="modal" onClick={e => e.stopPropagation()}>
        <div className="modal-header">
          <h3>{title}</h3>
          <button className="modal-close" onClick={onClose}>✕</button>
        </div>
        <div className="modal-body">
          <div className="form-group">
            <label>Nom du parcours</label>
            <input className="form-input" value={form.title} onChange={e => setForm({ ...form, title: e.target.value })} placeholder="ex: Python" />
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
              <option value="BEGINNER">Débutant</option>
              <option value="INTERMEDIATE">Intermédiaire</option>
              <option value="ADVANCED">Avancé</option>
            </select>
          </div>
          <div className="form-group">
            <label>Description</label>
            <textarea className="form-textarea" value={form.description} onChange={e => setForm({ ...form, description: e.target.value })} placeholder="Description du parcours..." />
          </div>
        </div>
        <div className="modal-footer">
          <button className="btn btn-outline" onClick={onClose}>Annuler</button>
          <button className="btn btn-primary" onClick={onSave} disabled={saving}>{saving ? 'Enregistrement...' : 'Enregistrer'}</button>
        </div>
      </div>
    </div>
  );

  return (
    <>
      <Topbar title="Parcours" />
      <div className="page-content">
        {toast && (
          <div style={{ position:'fixed', top:'20px', right:'20px', zIndex:9999, background: toast.ok ? '#22c55e' : '#ef4444', color:'white', padding:'12px 24px', borderRadius:'8px', fontWeight:600, fontSize:'14px', boxShadow:'0 4px 12px rgba(0,0,0,0.15)' }}>
            {toast.msg}
          </div>
        )}
        <div className="page-header">
          <div className="search-input">
            <FaMagnifyingGlass className="search-icon" />
            <input placeholder="Rechercher un parcours..." value={search} onChange={e => setSearch(e.target.value)} />
          </div>
          <button className="btn btn-primary" onClick={() => { setForm(emptyForm); setShowCreate(true); }}>
            <FaPlus /> Ajouter un parcours
          </button>
        </div>

        <div className="table-card">
          {loading ? <div className="empty-state"><p>Chargement...</p></div> : (
            <table className="data-table">
              <thead>
                <tr>
                  <th>Nom</th><th>Langage</th><th>Difficulté</th><th>Modules</th><th>Leçons</th><th>Défis</th><th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {filtered.length === 0 ? (
                  <tr><td colSpan={7} style={{ textAlign:'center', color:'var(--text-muted)' }}>Aucun parcours</td></tr>
                ) : filtered.map(t => (
                  <tr key={t.id}>
                    <td><strong>{t.title}</strong></td>
                    <td><span className={`lang-badge ${t.language.toLowerCase()}`}>{t.language}</span></td>
                    <td><span className={`diff-badge ${diffClass[t.difficulty]}`}>{diffLabels[t.difficulty]}</span></td>
                    <td>{t.modules}</td><td>{t.lessons}</td><td>{t.challenges}</td>
                    <td>
                      <div className="action-btns">
                        <button className="btn btn-outline btn-sm" title="Modifier" onClick={() => openEdit(t)}><FaPenToSquare /></button>
                        <button className="btn btn-danger btn-sm" title="Supprimer" onClick={() => handleDelete(t.id)}><FaTrash /></button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          )}
        </div>

        {showCreate && <Modal title="Nouveau parcours" onSave={handleCreate} onClose={() => setShowCreate(false)} />}
        {showEdit   && <Modal title={`Modifier — ${editTarget?.title}`} onSave={handleEdit} onClose={() => setShowEdit(false)} />}
      </div>
    </>
  );
}
