import React, { useState, useEffect } from 'react';
import './SubjectSelection.css';

const SubjectSelection = ({ userType, onLogout, navigateToHome, onTutorSelect }) => {
  
  const [subjects, setSubjects] = useState([]); // Initialisation de l'état pour les matières
  const [selectedSubject, setSelectedSubject] = useState(''); // Initialisation de l'état pour la matière sélectionnée
  const [selectedTutor, setSelectedTutor] = useState(''); // Initialisation de l'état pour le tuteur sélectionné
  const [tutors, setTutors] = useState([]); // Initialisation de l'état pour la liste des tuteurs

  // Effet pour récupérer toutes les matières disponibles
  useEffect(() => {
    fetch('/API/GetAllSubjectNamesServlet')
      .then(response => response.json())
      .then(data => {
        setSubjects(data);
      })
      .catch(error => console.error('Erreur lors de la récupération de matières:', error));
  }, []);

  // Gestionnaire pour gérer la sélection d'une matière
  const handleSelectSubject = (subject) => {
    setSelectedSubject(subject);
    setSelectedTutor('');

    fetch(`/API/GetTutorBySubjectName?matiere=${subject}`)
      .then(response => response.json())
      .then(data => {
        setTutors(data);
      })
      .catch(error => console.error('Erreur lors de la récupération de tuteurs:', error));
  };

  // Gestionnaire pour gérer la sélection d'un tuteur
  const handleSelectTutor = (tutor) => {
    setSelectedTutor(tutor);
    onTutorSelect(tutor.email); 
  };
  

  return (
    <div className="subject-selection-container">
      <button className="back-button" onClick={navigateToHome}>Back</button>
      <h1>Choisissez une matière</h1>
      <div className="subject-buttons">
        {subjects.map((subject, index) => (
          <button
            key={index}
            className={selectedSubject === subject ? 'selected subject-button' : 'subject-button'}
            onClick={() => handleSelectSubject(subject)}
          >
            {subject}
          </button>
        ))}
      </div>
      {selectedSubject && (
        <div className="tutors-list">
          <h2>Tuteurs disponibles pour {selectedSubject} :</h2>
          <div className="tutor-buttons">
            {tutors.map((tutor, index) => (
              <button
                key={index}
                className={selectedTutor === tutor ? 'selected tutor-button' : 'tutor-button'}
                onClick={() => handleSelectTutor(tutor)}
              >
                {tutor.nom} {tutor.prenom}
              </button>
            ))}
          </div>
        </div>
      )}
    </div>
  );
};

export default SubjectSelection;
