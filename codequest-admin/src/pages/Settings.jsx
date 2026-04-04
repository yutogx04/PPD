import { useState, useEffect } from 'react';
import Topbar from '../components/Topbar';
import { FaBolt, FaGlobe, FaPalette, FaBell, FaDatabase } from 'react-icons/fa6';
import API from '../api/client';

export default function Settings() {
  const [settings, setSettings] = useState({
    appName: 'CodeQuest',
    backendUrl: 'http://localhost:8080',
    defaultLanguage: 'Français',
    dailyReminderTime: '20:00',
    streakAlertHours: 2,
    dailyChallengeTime: '08:00',
    sandboxTimeoutSec: 5,
    sandboxMemoryMb: 64,
    sandboxRateLimit: 1,
    primaryColor: '#6C63FF',
  });
  const [saving, setSaving] = useState(false);
  const [toast, setToast] = useState('');

  useEffect(() => {
    API.get('/admin/settings')
      .then(res => setSettings(res.data))
      .catch(() => console.warn('Could not load settings'));
  }, []);

  const handleChange = (key, value) => {
    setSettings(prev => ({ ...prev, [key]: value }));
  };

  const handleSave = async () => {
    setSaving(true);
    try {
      const res = await API.put('/admin/settings', settings);
      setSettings(res.data);
      setToast('Paramètres sauvegardés ✓');
      setTimeout(() => setToast(''), 3000);
    } catch (err) {
      setToast('Erreur lors de la sauvegarde');
      setTimeout(() => setToast(''), 3000);
    }
    setSaving(false);
  };

  return (
    <>
      <Topbar title="Paramètres" />
      <div className="page-content">
        {/* Toast notification */}
        {toast && (
          <div style={{
            position: 'fixed', top: '20px', right: '20px', zIndex: 9999,
            background: toast.includes('Erreur') ? '#ef4444' : '#22c55e',
            color: 'white', padding: '12px 24px', borderRadius: '8px',
            fontWeight: 600, fontSize: '14px', boxShadow: '0 4px 12px rgba(0,0,0,0.15)',
            animation: 'fadeIn 0.3s ease'
          }}>
            {toast}
          </div>
        )}

        {/* App info */}
        <div className="widget-card" style={{ marginBottom: '16px' }}>
          <h3><FaBolt style={{ color: 'var(--primary)', marginRight: '8px' }} /> CodeQuest — Administration</h3>
          <p style={{ color: 'var(--text-muted)', fontSize: '13px', marginTop: '8px' }}>
            Version 1.0.0 · Spring Boot 3 · PostgreSQL 15 · Redis 7
          </p>
        </div>

        <div className="widgets-row">
          {/* General */}
          <div className="widget-card">
            <h3><FaGlobe style={{ marginRight: '8px' }} /> Général</h3>
            <div className="form-group" style={{ marginTop: '12px' }}>
              <label>Nom de l'application</label>
              <input className="form-input" value={settings.appName}
                onChange={e => handleChange('appName', e.target.value)} />
            </div>
            <div className="form-group">
              <label>URL du backend</label>
              <input className="form-input" value={settings.backendUrl}
                onChange={e => handleChange('backendUrl', e.target.value)} />
            </div>
            <div className="form-group">
              <label>Langue par défaut</label>
              <select className="form-select" value={settings.defaultLanguage}
                onChange={e => handleChange('defaultLanguage', e.target.value)}>
                <option>Français</option>
                <option>English</option>
              </select>
            </div>
          </div>

          {/* Notifications */}
          <div className="widget-card">
            <h3><FaBell style={{ marginRight: '8px' }} /> Notifications</h3>
            <div className="form-group" style={{ marginTop: '12px' }}>
              <label>Heure de rappel quotidien</label>
              <input className="form-input" type="time" value={settings.dailyReminderTime}
                onChange={e => handleChange('dailyReminderTime', e.target.value)} />
            </div>
            <div className="form-group">
              <label>Alerte streak (heures avant minuit)</label>
              <input className="form-input" type="number" value={settings.streakAlertHours}
                onChange={e => handleChange('streakAlertHours', parseInt(e.target.value) || 0)} />
            </div>
            <div className="form-group">
              <label>Défi du Jour — heure d'envoi</label>
              <input className="form-input" type="time" value={settings.dailyChallengeTime}
                onChange={e => handleChange('dailyChallengeTime', e.target.value)} />
            </div>
          </div>
        </div>

        <div className="widgets-row" style={{ marginTop: '0' }}>
          {/* Sandbox */}
          <div className="widget-card">
            <h3><FaDatabase style={{ marginRight: '8px' }} /> Sandbox (Exécution code)</h3>
            <div className="form-group" style={{ marginTop: '12px' }}>
              <label>Timeout (secondes)</label>
              <input className="form-input" type="number" value={settings.sandboxTimeoutSec}
                onChange={e => handleChange('sandboxTimeoutSec', parseInt(e.target.value) || 0)} />
            </div>
            <div className="form-group">
              <label>Mémoire max (Mo)</label>
              <input className="form-input" type="number" value={settings.sandboxMemoryMb}
                onChange={e => handleChange('sandboxMemoryMb', parseInt(e.target.value) || 0)} />
            </div>
            <div className="form-group">
              <label>Rate limit (soumissions / 10 sec)</label>
              <input className="form-input" type="number" value={settings.sandboxRateLimit}
                onChange={e => handleChange('sandboxRateLimit', parseInt(e.target.value) || 0)} />
            </div>
          </div>

          {/* Theme */}
          <div className="widget-card">
            <h3><FaPalette style={{ marginRight: '8px' }} /> Apparence</h3>
            <div className="form-group" style={{ marginTop: '12px' }}>
              <label>Couleur primaire</label>
              <input className="form-input" type="color" value={settings.primaryColor}
                style={{ height: '40px' }}
                onChange={e => handleChange('primaryColor', e.target.value)} />
            </div>
            <div className="form-group">
              <label>Contenu minimum requis</label>
              <p style={{ fontSize: '12px', color: 'var(--text-muted)' }}>
                2 parcours · 5 modules · 20 leçons · 15 défis
              </p>
            </div>
          </div>
        </div>

        <div style={{ textAlign: 'right', marginTop: '16px' }}>
          <button className="btn btn-primary" onClick={handleSave} disabled={saving}>
            {saving ? 'Sauvegarde...' : 'Sauvegarder les paramètres'}
          </button>
        </div>
      </div>
    </>
  );
}
