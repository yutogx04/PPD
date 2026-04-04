import { useState } from 'react';
import { FaBolt, FaEnvelope, FaLock } from 'react-icons/fa6';
import API from '../api/client';

export default function Login({ onLogin }) {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);
    try {
      const res = await API.post('/auth/login', { email, password });
      const { accessToken, user } = res.data;
      if (user.role !== 'ADMIN') {
        setError('Accès refusé : droits administrateur requis');
        setLoading(false);
        return;
      }
      localStorage.setItem('admin_token', accessToken);
      localStorage.setItem('admin_user', JSON.stringify(user));
      onLogin(user);
    } catch (err) {
      setError(err.response?.data?.error || 'Identifiants incorrects');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={{
      minHeight: '100vh',
      background: 'linear-gradient(135deg, #0D0D1A 0%, #1E1B4B 100%)',
      display: 'flex',
      alignItems: 'center',
      justifyContent: 'center',
      fontFamily: "'Poppins', sans-serif"
    }}>
      <div style={{
        background: 'white',
        borderRadius: '20px',
        padding: '40px',
        width: '400px',
        boxShadow: '0 20px 60px rgba(0,0,0,0.3)',
      }}>
        <div style={{ textAlign: 'center', marginBottom: '32px' }}>
          <div style={{
            background: 'linear-gradient(135deg, #6C63FF, #7C3AED)',
            width: '56px', height: '56px', borderRadius: '14px',
            display: 'inline-flex', alignItems: 'center', justifyContent: 'center',
            color: 'white', fontSize: '24px', marginBottom: '16px'
          }}>
            <FaBolt />
          </div>
          <h1 style={{ fontSize: '22px', fontWeight: 700, color: '#1E1B4B' }}>CodeQuest Admin</h1>
          <p style={{ fontSize: '13px', color: '#6B7280', marginTop: '4px' }}>Connectez-vous avec un compte administrateur</p>
        </div>

        <form onSubmit={handleSubmit}>
          <div style={{ marginBottom: '16px' }}>
            <label style={{ display: 'block', fontSize: '12px', fontWeight: 600, color: '#6B7280', marginBottom: '6px' }}>Email</label>
            <div style={{
              display: 'flex', alignItems: 'center', gap: '10px',
              border: '1px solid #E5E7EB', borderRadius: '10px', padding: '10px 14px',
            }}>
              <FaEnvelope style={{ color: '#9CA3AF', fontSize: '14px' }} />
              <input type="email" value={email} onChange={e => setEmail(e.target.value)}
                placeholder="admin@codequest.com"
                style={{ border: 'none', outline: 'none', flex: 1, fontFamily: 'Poppins', fontSize: '13px' }}
                required />
            </div>
          </div>

          <div style={{ marginBottom: '20px' }}>
            <label style={{ display: 'block', fontSize: '12px', fontWeight: 600, color: '#6B7280', marginBottom: '6px' }}>Mot de passe</label>
            <div style={{
              display: 'flex', alignItems: 'center', gap: '10px',
              border: '1px solid #E5E7EB', borderRadius: '10px', padding: '10px 14px',
            }}>
              <FaLock style={{ color: '#9CA3AF', fontSize: '14px' }} />
              <input type="password" value={password} onChange={e => setPassword(e.target.value)}
                placeholder="••••••••"
                style={{ border: 'none', outline: 'none', flex: 1, fontFamily: 'Poppins', fontSize: '13px' }}
                required />
            </div>
          </div>

          {error && (
            <div style={{
              background: '#FEE2E2', color: '#EF4444', padding: '10px 14px',
              borderRadius: '8px', fontSize: '12px', fontWeight: 500, marginBottom: '16px'
            }}>
              {error}
            </div>
          )}

          <button type="submit" disabled={loading} style={{
            width: '100%', padding: '12px',
            background: 'linear-gradient(135deg, #6C63FF, #7C3AED)',
            color: 'white', border: 'none', borderRadius: '10px',
            fontFamily: 'Poppins', fontSize: '14px', fontWeight: 600,
            cursor: loading ? 'wait' : 'pointer',
            opacity: loading ? 0.7 : 1,
          }}>
            {loading ? 'Connexion...' : 'Se connecter'}
          </button>
        </form>
      </div>
    </div>
  );
}
