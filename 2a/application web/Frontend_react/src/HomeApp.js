import React, { useState } from 'react';
import './HomePage.css';
import './HomeApp.css';
import SessionPage from './SessionPage';
import HomePage from './HomePage';
import MessageriePage from './MessageriePage';
import SubjectSelection from './SubjectSelection';
import LoginForm from './LoginForm';
import ModificationForm from './ModificationForm';
import FeedbackForm from './FeedbackForm';
import FAQ from './FAQ';
import Support from './Support';
import FeedbackStatistics from './FeedbackStatistics'; 
import HistorySessions from './HistorySessions';

const HomeApp = ({ onLogout, userType, userEmail, onLogin, onRegister, userId }) => {
  
  const [view, setView] = useState('home'); // Définit la vue initiale sur 'home'
  const [selectedTutorEmail, setSelectedTutorEmail] = useState('');

  // Fonction pour changer la vue
  const handleViewChange = (newView) => {
    setView(newView);
  };
  
  // Fonction pour gérer la sélection des statistiques d'un tuteur
  const handleTutorStatistics = (tutorEmail) => {
    setSelectedTutorEmail(tutorEmail);
    handleViewChange('statisticsProf');
  };
  return (
    <div>
      {view === 'home' && (
        <HomePage 
          onLogout={onLogout} 
          handleSessionsClick={() => handleViewChange('sessions')} 
          handleMatiereClick={() => handleViewChange('subjects')} 
          handleMessagingClick={() => handleViewChange('messagerie')} 
          userType={userType} 
          navigateToHome={() => handleViewChange('home')} 
          handleSettings={() => handleViewChange('Settings')}
          handleFAQClick={() => handleViewChange('faq')}
          handleSupportClick={() => handleViewChange('support')}
          handleFeedbackClick={() => handleViewChange('feedback')} 
          handleStatisticsClick={() => handleViewChange('statistics')} 
          handleHistorySessionsClick={() => handleViewChange('historySessions')} 
        />
      )}
      {view === 'login' && (
        <LoginForm 
          onRegister={onRegister} 
          onLogin={onLogin} 
           
        />
      )}
      {view === 'sessions' && (
        <SessionPage 
          onLogout={onLogout} 
          userType={userType} 
          navigateToHome={() => handleViewChange('home')} 
          userEmail={userEmail}
        />
      )}
      
      {view === 'messagerie' && <MessageriePage userEmail = {userEmail} onLogout={onLogout} userType={userType} navigateToHome={() => handleViewChange('home')}/>}
      
      {view === 'Settings' && (
        <ModificationForm 
          userType={userType} 
          userEmail={userEmail}
          navigateToHome={() => handleViewChange('home')} 
          
        />
      )}

      {view === 'faq' && (
        <FAQ 
          onBack={() => handleViewChange('home')} 
        />
      )}
      {view === 'support' && (
        <Support 
          onBack={() => handleViewChange('home')} 
        />
      )}
      {view === 'feedback' && (
        <FeedbackForm 
          session={{ title: "Example Session", id: 1 }} 
          userType={userType} 
          userEmail={userEmail} 
          onBack={() => handleViewChange('home')} 
        />
      )}
      {view === 'subjects' && (
        <SubjectSelection 
          onLogout={onLogout} 
          userType={userType} 
          navigateToHome={() => handleViewChange('home')} 
          onTutorSelect={handleTutorStatistics}
        />
      )}
       {view === 'statistics' && (
                <FeedbackStatistics 
                userType={userType} 
                tutorId={userEmail} 
                onBack={() => handleViewChange('home')}  
                choixProf={true}
                

              />
            )}

      {view === 'statisticsProf' && (
        <FeedbackStatistics 
          userType="student"
          tutorId={selectedTutorEmail} 
          onBack={() => handleViewChange('home')} 
          choixProf={false}
        />
      )}

      {view === 'historySessions' && ( // added condition
        <HistorySessions 
          userEmail={userEmail}
          navigateToHome={() => handleViewChange('home')}
        />
      )}
    </div>
  );
};

export default HomeApp;