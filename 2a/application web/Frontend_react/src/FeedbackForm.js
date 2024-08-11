import React, { useState, useEffect } from 'react';
import './FeedbackForm.css';

const FeedbackForm = ({ onBack, session, userType, userEmail}) => {
  // Déclaration des états pour gérer les valeurs des champs de formulaire et autres données
  const [name, setName] = useState(''); // État pour le nom
  const [email, setEmail] = useState(''); // État pour l'email
  const [message, setMessage] = useState(''); // État pour le message
  const [rating, setRating] = useState(0); // État pour la note
  const [responseMessage, setResponseMessage] = useState(''); // État pour le message de réponse après soumission du formulaire
  const [tutors, setTutors] = useState([]); // État pour la liste des tuteurs
  const [selectedTutor, setSelectedTutor] = useState(''); // État pour le tuteur sélectionné
  const [feedbacks, setFeedbacks] = useState([]); // État pour la liste des feedbacks reçus

  // Utilisation de useEffect pour effectuer des opérations de récupération de données après le montage du composant
  useEffect(() => {
    if (userType === 'student') {
      const fetchTutors = async () => {
        try {
          const response = await fetch('/API/get_tuteurs');
          if (response.ok) {
            const data = await response.json();
            setTutors(data);
          } else {
            console.error('Échec de la récupération des tuteurs');
          }
        } catch (error) {
          console.error('Échec de la récupération des tuteurs:', error);
        }
      };
      fetchTutors();
    } else if (userType === 'tutor') {
      const fetchFeedbacks = async () => {
        try {
          if (true) {
            const response = await fetch(`/API/get_feedbacks?tutorId=${userEmail}`);
            if (response.ok) {
              const data = await response.json();
              setFeedbacks(data);
            } else {
              console.error('Échec de la récupération des feedbacks');
            }
          } 
        } catch (error) {
          console.error('Échec de la récupération des feedbacks:', error);
        }
      };
      fetchFeedbacks();
    }
  }, [userType, userEmail]);

  // Gestionnaire de soumission du formulaire
  const handleSubmit = async (e) => {
    e.preventDefault(); // Empêche le comportement par défaut du formulaire (rechargement de la page)    
    const feedback = { 
      name, 
      email, 
      message, 
      rating, 
      sessionId: session.id, 
      tutorId: selectedTutor
    };

    try {
      const response = await fetch('/API/submit-feedback', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(feedback),
      });

      const data = await response.json();
      setResponseMessage(data.message);
      onBack();
    } catch (error) {
      setResponseMessage('Une erreur est survenue. Veuillez réessayer plus tard.');
    }
  };

  return (
    <div className="feedback-form-container">
      <h1 className="animated-title">Feedback</h1>
      {userType === 'student' ? (
        <form onSubmit={handleSubmit} className="feedback-form">
          <label>
            Nom:
            <input type="text" value={name} onChange={(e) => setName(e.target.value)} required />
          </label>
          <label>
            Email (optional):
            <input type="email" value={email} onChange={(e) => setEmail(e.target.value)} />
          </label>
          <label>
            Message:
            <textarea value={message} onChange={(e) => setMessage(e.target.value)} required />
          </label>
          <label>
          Rating:
          <div className="star-rating">
            {[...Array(5)].map((star, index) => {
              index += 1;
              return (
                <button
                  type="button"
                  key={index}
                  className={index <= rating ? "on" : "off"}
                  onClick={() => setRating(index)}
                >
                  <span className="star">&#9733;</span>
                </button>
              );
            })}
          </div>
        </label>
          <label>
            Tutor:
            <select value={selectedTutor} onChange={(e) => setSelectedTutor(e.target.value)} required>
              <option value="">Select a tutor</option>
              {tutors.map(tutor => (
                <option key={tutor.id} value={tutor.id}>
                  {tutor.nom} {tutor.prenom}
                </option>
              ))}
            </select>
          </label>
          <button type="submit" className="submit-button">Submit Feedback</button>
          <button type="button" onClick={onBack} className="back-button">Back</button>
        </form>
      ) : (
        <div className="feedback-list">
          <h2>Feedback reçu</h2>
          {feedbacks.length === 0 ? (
            <p>Aucun feedback reçu.</p>
          ) : (
            feedbacks.map(feedback => (
              <div key={feedback.id} className="feedback-item">
                <p><strong>Name:</strong> {feedback.name}</p>
                <p><strong>Email:</strong> {feedback.email}</p>
                <p><strong>Message:</strong> {feedback.message}</p>
                <p><strong>Rating:</strong> {feedback.rating}</p>
              </div>
            ))
          )}
          <button type="button" onClick={onBack} className="back-button">Back</button>
        </div>
      )}
      {responseMessage && <p>{responseMessage}</p>}
    </div>
  );
};

export default FeedbackForm;
