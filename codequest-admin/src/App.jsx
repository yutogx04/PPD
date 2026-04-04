import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { useState, useEffect } from 'react';
import Sidebar from './components/Sidebar';
import Dashboard from './pages/Dashboard';
import Tracks from './pages/Tracks';
import Lessons from './pages/Lessons';
import Challenges from './pages/Challenges';
import Users from './pages/Users';
import Badges from './pages/Badges';
import Settings from './pages/Settings';
import Login from './pages/Login';

function App() {
  const [user, setUser] = useState(null);

  useEffect(() => {
    const token = localStorage.getItem('admin_token');
    const savedUser = localStorage.getItem('admin_user');
    if (token && savedUser) {
      try {
        setUser(JSON.parse(savedUser));
      } catch { /* invalid stored data */ }
    }
  }, []);

  const handleLogin = (u) => setUser(u);

  const handleLogout = () => {
    localStorage.removeItem('admin_token');
    localStorage.removeItem('admin_user');
    setUser(null);
  };

  if (!user) {
    return <Login onLogin={handleLogin} />;
  }

  return (
    <BrowserRouter>
      <div className="app-layout">
        <Sidebar onLogout={handleLogout} />
        <main className="main-content">
          <Routes>
            <Route path="/" element={<Dashboard />} />
            <Route path="/tracks" element={<Tracks />} />
            <Route path="/lessons" element={<Lessons />} />
            <Route path="/challenges" element={<Challenges />} />
            <Route path="/users" element={<Users />} />
            <Route path="/badges" element={<Badges />} />
            <Route path="/settings" element={<Settings />} />
            <Route path="*" element={<Navigate to="/" />} />
          </Routes>
        </main>
      </div>
    </BrowserRouter>
  );
}

export default App;
