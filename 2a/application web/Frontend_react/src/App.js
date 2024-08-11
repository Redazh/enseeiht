import React, { useState } from 'react';
import LoginForm from './LoginForm';
import RegistrationForm from './RegistrationForm';
import HomeApp from './HomeApp';

const App = () => {
  // Définit la vue initiale sur 'login'
  const [view, setView] = useState('login'); 
  // State pour propager le type d'utilisateur
  const [userType, setUserType] = useState(''); 
  // State pour stocker l'email de l'utilisateur
  const [userEmail, setEmail] = useState(''); 

  // Fonction pour gérer la connexion
  const handleLogin = async (selectedUserType, username, password) => {
    const data = {
      "user": selectedUserType,
      "username": username,
      "password": password
    };

    try {
      // Envoie une requête POST à l'API de login
      const response = await fetch('/API/login', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(data),
      });

      if (response.ok) {
        // Si l'authentification réussit, met à jour le type d'utilisateur et l'email
        setUserType(selectedUserType);
        setEmail(username);
        // Redirige vers la page d'accueil
        setView('home'); 
      } else {
        alert('Email ou mot de passe incorrect');
      }
    } catch (error) {
      alert('Erreur lors de la tentative de connexion');
    }
  };

  // Fonction pour gérer la redirection vers la page d'inscription
  const handleRegister = () => {
    setView('register');
  };

  // Fonction pour gérer la déconnexion
  const handleLogout = () => {
    // Redirige vers la page de connexion après la déconnexion
    setView('login'); 
  };

  // Fonction pour gérer l'inscription
  const handleRegistrationSubmit = async (selectedUserType, formData) => {
    const data = {
      "user": selectedUserType,
      "nom": formData.nom,
      "prenom": formData.prenom,
      "email": formData.email,
      "password": formData.password,
      "telephone": formData.contactInfo.telephone,
      "adresse": formData.contactInfo.adresse
    };

    try {
      // Envoie une requête POST à l'API d'inscription
      const response = await fetch('/API/register', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(data)
      });

      if (response.ok) {
        alert("Inscription réussie ! Bienvenue sur notre site.");
        // Redirige vers la page de connexion après une inscription réussie
        setView('login');
      } else {
        alert('Erreur lors de la tentative d\'inscription');
      }
    } catch (error) {
      alert('Erreur lors de la tentative d\'inscription');
      console.error('Erreur lors de la tentative d\'inscription:', error);
    }
  };

  return (
    <div>
      {view === 'login' && <LoginForm onRegister={handleRegister} onLogin={handleLogin} />}
      {view === 'register' && <RegistrationForm onRegistrationSubmit={handleRegistrationSubmit} onRegister={handleRegister} onLogin={handleLogin} />}
      {view === 'home' && <HomeApp onLogout={handleLogout} userType={userType} userEmail={userEmail} tutorId={userEmail} onLogin={handleLogin} onRegister={handleRegister} />}
    </div>
  );
};

export default App;
