import React from 'react';
import './HomePage.css';

const HomePage = ({ onLogout, handleSessionsClick, handleMatiereClick, handleMessagingClick, userType, navigateToHome, handleSettings, handleFAQClick, handleSupportClick, handleFeedbackClick, handleStatisticsClick , handleHistorySessionsClick}) => {
  return (
    <div className="homepage-container">
      <div className="header">
        <h1>Dashboard Tutorat</h1>
        <button onClick={onLogout} className="logout-button">Déconnecter</button>
      </div>
      <div className="content">
        <div className="column">
          <div className="section">
            <button className="title-button session-button" onClick={handleSessionsClick}>
              <h2>Sessions de Tutorat</h2>
            </button>
            <p>Liste des sessions à venir, avec des détails comme la date, le sujet, le tuteur, etc.</p>
          </div>
          <div className="section">
            <button className="title-button matiere-button" onClick={handleMatiereClick}>
              <h2>Matière</h2>
            </button>
            <p>Affichage des matières, affichage des tuteurs experts, puis affichage des statistiques des tuteurs.</p>
          </div>
          <div className="section">
            <button className="title-button feedback-button" onClick={handleFeedbackClick}>
              <h2>Feedback et Évaluations</h2>
            </button>
            <p>Évaluation des tuteurs par les étudiants et leurs commentaires associés.</p>
          </div>
          <div className="section">
            <button className="title-button messaging-button" onClick={handleMessagingClick}>
              <h2>Système de Messagerie</h2>
            </button>
            <p>Messages entre utilisateurs.</p>
          </div>
        </div>
        <div className="column">
          <div className="section">
            <button className="title-button stats-button" onClick={handleStatisticsClick}>
              <h2>Statistiques et Suivi</h2>
            </button>
            <p>Évaluations attribuées par les étudiants aux tuteurs</p>
          </div>
          <div className="section">
            <button className="title-button faq-button" onClick={handleFAQClick}>
              <h2>FAQ</h2>
            </button>
            <p>Questions fréquentes et réponses détaillées.</p>
          </div>
          <div className="section">
            <button className="title-button support-button" onClick={handleSupportClick}>
              <h2>Support</h2>
            </button>
            <p>Assistance et aide pour les utilisateurs.</p>
          </div>
          <div className="section">
            <button className="title-button settings-button" onClick={handleSettings}>
              <h2>Paramètres du Compte Utilisateur</h2>
            </button>
            <p>Options de notification et paramètres de confidentialité.</p>
          </div>
          <div className="section">
            <button className="title-button history-button" onClick={handleHistorySessionsClick}>
              <h2>Historique des Sessions</h2>
            </button>
            <p>Liste et détails des sessions passées.</p>
          </div>
        </div>
      </div>
    </div>
  );
};

export default HomePage;