import { useState, useEffect } from 'react';
import Topbar from '../components/Topbar';
import { FaUsers, FaFileLines, FaTerminal, FaCircleCheck } from 'react-icons/fa6';
import API from '../api/client';

const kpiConfig = [
  { key: 'totalUsers', icon: FaUsers, label: 'Apprenants', variant: 'users' },
  { key: 'totalLessons', icon: FaFileLines, label: 'Leçons actives', variant: 'lessons' },
  { key: 'totalSubmissions', icon: FaTerminal, label: 'Soumissions', variant: 'challenges' },
  { key: 'successRate', icon: FaCircleCheck, label: 'Taux de succès', variant: 'success', suffix: '%' },
];

function formatNum(n) {
  if (typeof n === 'number') return n.toLocaleString('fr-FR');
  return n;
}

function getStatusClass(status) {
  if (status === 'ACCEPTED') return 'accepted';
  if (status === 'WRONG_ANSWER' || status === 'RUNTIME_ERROR' || status === 'COMPILE_ERROR') return 'wrong';
  if (status === 'TIMEOUT' || status === 'MEMORY_LIMIT') return 'tle';
  return '';
}

function getStatusLabel(status) {
  switch (status) {
    case 'ACCEPTED': return 'ACCEPTED';
    case 'WRONG_ANSWER': return 'WRONG';
    case 'RUNTIME_ERROR': return 'ERROR';
    case 'COMPILE_ERROR': return 'COMPILE';
    case 'TIMEOUT': return 'TLE';
    case 'MEMORY_LIMIT': return 'MLE';
    default: return status;
  }
}

export default function Dashboard() {
  const [stats, setStats] = useState({});
  const [topChallenges, setTopChallenges] = useState([]);
  const [recentSubs, setRecentSubs] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    async function fetchData() {
      try {
        const [statsRes, topRes, subsRes] = await Promise.all([
          API.get('/admin/stats'),
          API.get('/admin/stats/top-challenges'),
          API.get('/admin/stats/recent-submissions'),
        ]);
        setStats(statsRes.data);
        setTopChallenges(topRes.data);
        setRecentSubs(subsRes.data);
      } catch (err) {
        console.error('Failed to load dashboard stats:', err);
      } finally {
        setLoading(false);
      }
    }
    fetchData();
  }, []);

  const maxSubmissions = topChallenges.length > 0 ? Math.max(...topChallenges.map(c => c.submissions)) : 1;

  return (
    <>
      <Topbar title="Vue d'ensemble" />
      <div className="page-content">
        {loading ? (
          <div className="empty-state"><p>Chargement...</p></div>
        ) : (
          <>
            {/* KPI Cards */}
            <div className="kpi-grid">
              {kpiConfig.map((kpi, i) => (
                <div className="kpi-card" key={i}>
                  <div className={`kpi-icon ${kpi.variant}`}>
                    <kpi.icon />
                  </div>
                  <div className="kpi-info">
                    <span className="kpi-num">
                      {formatNum(stats[kpi.key] || 0)}{kpi.suffix || ''}
                    </span>
                    <span className="kpi-label">{kpi.label}</span>
                  </div>
                </div>
              ))}
            </div>

            {/* Widgets Row */}
            <div className="widgets-row">
              <div className="widget-card">
                <h3>Défis les plus tentés</h3>
                {topChallenges.length === 0 ? (
                  <p style={{ color: 'var(--text-muted)', fontSize: '13px' }}>Aucune soumission encore</p>
                ) : (
                  topChallenges.map((c, i) => (
                    <div className="bar-item" key={i}>
                      <span className="bar-label">{c.title}</span>
                      <div className="bar-track">
                        <div className="bar-fill" style={{ width: `${(c.submissions / maxSubmissions) * 100}%` }} />
                      </div>
                      <span className="bar-count">{c.submissions}</span>
                    </div>
                  ))
                )}
              </div>

              <div className="widget-card">
                <h3>Soumissions récentes</h3>
                {recentSubs.length === 0 ? (
                  <p style={{ color: 'var(--text-muted)', fontSize: '13px' }}>Aucune soumission encore</p>
                ) : (
                  recentSubs.slice(0, 5).map((s, i) => (
                    <div className="abandon-item" key={i}>
                      <div className={`abn-badge ${getStatusClass(s.status) === 'accepted' ? 'ok' : 'warn'}`}>
                        {getStatusLabel(s.status).substring(0, 3)}
                      </div>
                      <div className="abn-info">
                        <strong>{s.user} — {s.challenge}</strong>
                        <p>{s.language} · {s.executionTimeMs > 0 ? s.executionTimeMs + 'ms' : '—'}</p>
                      </div>
                    </div>
                  ))
                )}
              </div>
            </div>

            {/* Full Submissions Table */}
            <div className="table-card">
              <h3>Soumissions récentes</h3>
              <table className="data-table">
                <thead>
                  <tr>
                    <th>Apprenant</th>
                    <th>Défi</th>
                    <th>Statut</th>
                    <th>Langage</th>
                    <th>Durée</th>
                  </tr>
                </thead>
                <tbody>
                  {recentSubs.length === 0 ? (
                    <tr><td colSpan={5} style={{ textAlign: 'center', color: 'var(--text-muted)' }}>Pas de soumissions</td></tr>
                  ) : (
                    recentSubs.map((s, i) => (
                      <tr key={i}>
                        <td>{s.user}</td>
                        <td>{s.challenge}</td>
                        <td><span className={`status-badge ${getStatusClass(s.status)}`}>{getStatusLabel(s.status)}</span></td>
                        <td><span className={`lang-badge ${s.language.toLowerCase()}`}>{s.language}</span></td>
                        <td>{s.executionTimeMs > 0 ? s.executionTimeMs + 'ms' : '—'}</td>
                      </tr>
                    ))
                  )}
                </tbody>
              </table>
            </div>
          </>
        )}
      </div>
    </>
  );
}
