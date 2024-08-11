import React, { useState } from 'react';
import UserTypeSelection from './UserTypeSelection';

const LoginForm = ({ onLogin, onRegister }) => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [selectedUserType, setSelectedUserType] = useState('');

  const handleLogin = () => {
    onLogin(selectedUserType, username, password);
  };

  return (
    <div>
      <h2>Login</h2>
      <form>
        <label>Username:
          <input 
            type="text"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
          />
        </label>
        <label>Password:
          <input 
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
          />
        </label>
        <UserTypeSelection onSelect={setSelectedUserType} />
        <button type="button" onClick={handleLogin}>Connect</button>
        <button type="button" onClick={onRegister}>Register</button>
      </form>
    </div>
  );
};

export default LoginForm;
