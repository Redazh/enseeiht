import React, { useState, useEffect } from 'react';
import './FeedbackStatistics.css';

const FeedbackStatistics = ({ userType, tutorId, onBack, choixProf }) => {
  // Déclaration des états pour gérer les données et les états de chargement et d'erreur
  const [feedbacks, setFeedbacks] = useState([]); // État pour la liste des feedbacks
  const [tutors, setTutors] = useState([]); // État pour la liste des tuteurs
  const [selectedTutorEmail, setSelectedTutorEmail] = useState(choixProf ? '' : tutorId); // État pour l'email du tuteur sélectionné
  const [loading, setLoading] = useState(false); // État pour gérer le chargement
  const [error, setError] = useState(null); // État pour gérer les erreurs

  // Utilisation de useEffect pour récupérer les tuteurs si l'utilisateur peut choisir un tuteur et est un étudiant
  useEffect(() => {
    if (choixProf && userType === 'student') {
      const fetchTutors = async () => {
        try {
          const response = await fetch('/API/get_tuteurs');
          if (response.ok) {
            const data = await response.json();
            setTutors(data);
          } else {
            console.error('Échec de la récupération des tuteurs');
            setError('Échec de la récupération des tuteurs');
          }
        } catch (error) {
          console.error('Échec de la récupération des tuteurs:', error);
          setError(error);
        }
      };
      fetchTutors();
    }
  }, [choixProf, userType]);

    // Utilisation de useEffect pour récupérer les feedbacks en fonction de l'email du tuteur sélectionné
  useEffect(() => {
    const fetchFeedbacks = async () => {
      if (!selectedTutorEmail) {
        return;
      }
      setLoading(true);
      
      try {
        const response = await fetch(`/API/get_feedbacks?tutorId=${selectedTutorEmail}`);
        if (response.ok) {
          const data = await response.json();
          setFeedbacks(data);
        } else {
          console.error('Échec de la récupération des feedbacks');
          setError('Échec de la récupération des feedbacks');
        }
      } catch (error) {
        console.error('Échec de la récupération des feedbacks:', error);
        setError(error);
      } finally {
        setLoading(false);
      }
    };

    if (!choixProf || selectedTutorEmail) {
      fetchFeedbacks();
    }
  }, [selectedTutorEmail, choixProf]);

  // Gestionnaire de sélection d'un tuteur dans la liste déroulante
  const handleSelectTutor = (e) => {
    setSelectedTutorEmail(e.target.value);
  };

  if (loading) {
    return <div>Loading...</div>;
  }

  if (error) {
    return <div>Error: {error.message || error}</div>;
  }

  // Calcul de la distribution des notes
  const ratingCounts = feedbacks.reduce((counts, feedback) => {
    counts[feedback.rating - 1]++;
    return counts;
  }, Array(5).fill(0)).reverse();

  // Calcul du nombre total de feedbacks et de la note moyenne
  const totalFeedbacks = feedbacks.length;
  const averageRating = totalFeedbacks > 0 
    ? feedbacks.reduce((sum, feedback) => sum + feedback.rating, 0) / totalFeedbacks 
    : 0;
    
  // Calcul du nombre d'étoiles pleines et de la partie fractionnelle de l'étoile
  const fullStars = Math.floor(averageRating);
  const fractionalStar = averageRating - fullStars;

  return (
    <div className="feedback-statistics-container">
      <button type="button" onClick={onBack} className="back-button">Back</button>
      <h1 className="animated-title">Feedback Statistics</h1>
      {choixProf && userType === 'student' && (
        <div className="tutor-select">
          <label htmlFor="tutor">Select a Tutor:</label>
          <select
            id="tutor"
            value={selectedTutorEmail}
            onChange={handleSelectTutor}
          >
            <option value="">--Select--</option>
            {tutors.map(tutor => (
              <option key={tutor.id} value={tutor.Email}>
                {tutor.nom} {tutor.prenom}
              </option>
            ))}
          </select>
        </div>
      )}
      {selectedTutorEmail && feedbacks.length > 0 && (
        <div>
          <div className="average-rating">
            <span className="rating-value">{averageRating.toFixed(1)}</span>
            <div className="stars">
              {Array(fullStars).fill().map((_, i) => (
                <span key={i} className="star filled">&#9733;</span>
              ))}
              {fractionalStar > 0 && (
                <span className="star fractional" style={{ '--fraction': `${fractionalStar * 100}%` }}>&#9733;</span>
              )}
              {Array(5 - fullStars - (fractionalStar > 0 ? 1 : 0)).fill().map((_, i) => (
                <span key={i} className="star">&#9733;</span>
              ))}
            </div>
          </div>
          <div className="ratings-distribution">
            {ratingCounts.map((count, index) => (
              <div key={index} className="rating-row">
                <span className="star-count">{5 - index} <span className="star">&#9733;</span></span>
                <div className="progress-bar">
                  <div className="progress" style={{ width: `${(count / totalFeedbacks) * 100}%` }}></div>
                </div>
                <span className="count">{count}</span>
              </div>
            ))}
          </div>
        </div>
      )}
    </div>
  );
};

export default FeedbackStatistics;
