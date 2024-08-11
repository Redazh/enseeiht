import React, { useRef, useState, useEffect } from 'react';
import FullCalendar from '@fullcalendar/react'; 
import dayGridPlugin from '@fullcalendar/daygrid'; 
import timeGridPlugin from '@fullcalendar/timegrid'; 
import interactionPlugin from '@fullcalendar/interaction'; 
import Modal from 'react-modal'; 

import './SessionPage.css';

Modal.setAppElement('#root'); 

const SessionPage = ({ onLogout, userType, navigateToHome, userEmail }) => {

  // Référence du calendrier
  const calendarRef = useRef(null);
  
  // État pour l'événement sélectionné
  const [selectedEvent, setSelectedEvent] = useState(null);
  
  // État pour indiquer si un événement est en cours de création
  const [isCreatingEvent, setIsCreatingEvent] = useState(false);
  
  // État pour stocker les événements
  const [events, setEvents] = useState([]);
  
  // Critères de filtre pour les événements
  const [filterCriteria, setFilterCriteria] = useState('');
  
  // État pour stocker les événements filtrés
  const [filteredEvents, setFilteredEvents] = useState([]);
  
  // État pour suivre la date actuelle pour la navigation
  const [currentDate, setCurrentDate] = useState(new Date());
  
  // État pour le sujet sélectionné
  const [selectedSubject, setSelectedSubject] = useState('');
  
  // État pour le nom complet sélectionné
  const [selectedfullname, setfullname] = useState('');
  
  // État pour le tuteur sélectionné
  const [selectedTutor, setSelectedTutor] = useState('');
  
  // État pour les sujets récupérés depuis le backend
  const [subjects, setSubjects] = useState([]);

  // Fonction pour changer la vue du calendrier
  const handleSwitchView = (viewType) => {
    const calendarApi = calendarRef.current.getApi();
    calendarApi.changeView(viewType);
  };

  // Fonction pour gérer le clic sur un événement
  const handleEventClick = (info) => {
    setSelectedEvent(info.event);
  };

  // Fonction pour fermer le modal
  const closeModal = () => {
    setSelectedEvent(null);
    setIsCreatingEvent(false);
  };

  // Fonction pour gérer le clic sur le bouton de fermeture du modal
  const handleCloseButtonClick = (event) => {
    event.stopPropagation(); 
    closeModal();
  };

  // Fonction pour gérer le clic sur le bouton de création d'événement
  const handleCreateEventButtonClick = () => {
    setIsCreatingEvent(true);
  };

  // Effet pour récupérer le nom complet du tuteur
  useEffect(() => {
    const fetchTutorFullName = async () => {
      try {
        const response = await fetch(`/API/get_user_full_name?email=${encodeURIComponent(userEmail)}`);
        if (response.ok) {
          const data = await response.json();
          const fullName = data.fullName;
          setfullname(fullName);
        } else {
          console.error('Failed to fetch tutor full name');
        }
      } catch (error) {
        console.error('Error fetching tutor full name:', error);
      }
    };  

    fetchTutorFullName();
  }, [userEmail]);

  // Effet pour récupérer les événements depuis le backend
  useEffect(() => {
    const fetchEvents = async () => {
      try {
        const response = await fetch('/API/get_events');
        if (response.ok) {
          const data = await response.json();
          const transformedEvents = data.map(event => ({
            title: event.title,
            start: event.start,
            end: event.end,
            subject: event.subject,
            tutor: event.tutor,
            lessonTitle: event.lessonTitle,
            sessionPoints: event.sessionPoints,
            additionalInfo: event.additionalInfo,
          }));
          setEvents(transformedEvents);
        } else {
          console.error('Failed to fetch events');
        }
      } catch (error) {
        console.error('Error fetching events:', error);
      }
    };

    fetchEvents();
  }, []);

  // Fonction pour créer une session
  const createSession = async (sessionData) => {
    try {
      sessionData.start = new Date(sessionData.start).toISOString();
      sessionData.end = new Date(sessionData.end).toISOString();

      const response = await fetch('/API/create_sessionServlet', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(sessionData),
      });

      if (response.ok) {
        alert('Session created successfully');
        // Recharger les événements après la création réussie
      } else {
        alert('Failed to create session');
      }
    } catch (error) {
      console.error('Error creating session:', error);
      alert('Error creating session');
    }
  };

  // Fonction pour soumettre le formulaire de création de session
  const handleSubmit = async (event) => {
    event.preventDefault();

    const formData = new FormData(event.target);

    const newEvent = {
      title: formData.get('title'),
      start: formData.get('startDate'),
      end: formData.get('endDate'),
      subject: selectedSubject,
      tutor: selectedfullname,
      lessonTitle: formData.get('lessonTitle'),
      sessionPoints: formData.get('sessionPoints').split(',').map(point => point.trim()),
      additionalInfo: formData.get('additionalInfo'),
      maxStudents: formData.get('maxStudents'),
      email: userEmail
    };

    await createSession(newEvent);
    closeModal();
  };

  // Fonction pour afficher le contenu d'un événement
const renderEventContent = ({ event }) => {
  // Déterminer la couleur de l'événement en fonction du sujet
  const eventColor = determineEventColor(event.extendedProps.subject);
  
  return (
    <div className="event-content" style={{ backgroundColor: eventColor }}>
      <h3>{event.title}</h3>
      <p><strong>👨‍🏫:</strong> {event.extendedProps.subject}</p>
      <p><strong>📚 :</strong> {event.extendedProps.tutor}</p>
    </div>
  );
};


// Fonction pour déterminer la couleur de l'événement en fonction du sujet
const determineEventColor = (subject) => {
  const subjectColors = {
    maths: '#FF5733', // Couleur orange pour les mathématiques
    science: '#3366FF', // Couleur bleue pour les sciences
    literature: '#FF33FF', // Couleur rose pour la littérature
    // Ajouter plus de sujets et leurs couleurs correspondantes ici
    biology: '#FFD700', // Couleur or pour la biologie
    chemistry: '#008000', // Couleur verte pour la chimie
    physics: '#FF69B4' // Couleur rose pour la physique
  };

  // Convertir le sujet en minuscules pour une correspondance insensible à la casse
  const lowercaseSubject = subject.toLowerCase();

  // Vérifier si le sujet existe dans l'objet subjectColors
  if (lowercaseSubject in subjectColors) {
    return subjectColors[lowercaseSubject];
  } else {
    // Si le sujet n'est pas trouvé, retourner une couleur par défaut
    return '#808080'; // Couleur grise pour les autres sujets
  }
};

///////////Partie du code responsable sur la recherche par filtres //////////////////////////////////////////////


// Fonction pour effacer la sélection des filtres
const clearSelection = () => {
  setSelectedSubject('');
  setSelectedTutor('');
};


// Fonction pour gérer le changement de filtre
const handleFilterChange = (event) => {
  setFilterCriteria(event.target.value);
};


// Fonction pour soumettre les filtres
const handleFilterSubmit = () => {
  const filtered = events.filter(event =>
    event.start.includes(filterCriteria) || 
    event.subject.toLowerCase().includes(filterCriteria.toLowerCase()) ||
    event.tutor.toLowerCase().includes(filterCriteria.toLowerCase())
  );
  setFilteredEvents(filtered);
  
  // Mettre à jour les événements filtrés à afficher dans le calendrier
  calendarRef.current.getApi().removeAllEvents(); // Supprimer tous les événements existants
  calendarRef.current.getApi().addEventSource(filtered); // Ajouter les événements filtrés au calendrier

  // Effacer les événements filtrés après les avoir affichés
  setTimeout(() => {
    setFilteredEvents([]); // Réinitialiser filteredEvents après le rendu
    clearSelection(); // Réinitialiser selectedSubject et selectedTutor
  }, 0);
};


// Fonction pour naviguer vers le prochain événement filtré
const handleNextButtonClick = () => {
  const nextFilteredEvent = getNextFilteredEvent(currentDate);
  if (nextFilteredEvent) {
    const nextEventStartDate = new Date(nextFilteredEvent.start);
    const nextEventWeekStartDate = getWeekStartDate(nextEventStartDate);

    // Mettre à jour la date actuelle et naviguer vers la semaine du prochain événement
    setCurrentDate(nextEventWeekStartDate);
    navigateToWeek(nextEventWeekStartDate);
  }
};

// Fonction pour obtenir le prochain événement filtré
const getNextFilteredEvent = (currentDate) => {
  const futureFilteredEvents = events.filter(event =>
    (new Date(event.start) > currentDate) && // Considérer uniquement les événements futurs
    (
      event.subject.toLowerCase().includes(filterCriteria.toLowerCase()) ||
      event.tutor.toLowerCase().includes(filterCriteria.toLowerCase())
    )
  );

  // Trier futureFilteredEvents par date de début et retourner le premier événement
  return futureFilteredEvents.sort((a, b) => new Date(a.start) - new Date(b.start))[0];
};

// Fonction pour obtenir la date de début de la semaine
const getWeekStartDate = (date) => {
  const currentDate = new Date(date);
  const dayOfWeek = currentDate.getDay();
  const weekStartDate = new Date(currentDate.setDate(currentDate.getDate() - dayOfWeek));
  weekStartDate.setHours(0, 0, 0, 0); // Réinitialiser l'heure à minuit
  return weekStartDate;
};


// Fonction pour naviguer vers une semaine spécifique dans le calendrier
const navigateToWeek = (startDate) => {
  const calendarApi = calendarRef.current.getApi();
  calendarApi.gotoDate(startDate);
};
///////////////////////////////////////////////////////////////


// Fonction pour inscrire un étudiant à une session
const handleEnrollClick = async () => {
  if (selectedEvent) {
    const enrollData = {
      userEmail: userEmail, // Supposant que userEmail est disponible dans les props ou l'état du composant
      sessionDetails: {
        title: selectedEvent.title,
        start: selectedEvent.start,
        end: selectedEvent.end,
        subject: selectedEvent.extendedProps.subject,
        tutor: selectedEvent.extendedProps.tutor,
        lessonTitle: selectedEvent.extendedProps.lessonTitle,
        sessionPoints: selectedEvent.extendedProps.sessionPoints,
        additionalInfo: selectedEvent.extendedProps.additionalInfo,
      }
    };

    try {
      const response = await fetch('/API/enroll', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(enrollData),
      });

      if (response.ok) {
        alert('Inscription réussie');
        // Mettre à jour la liste des événements ou les détails de la session si nécessaire
      } else {
        alert('Échec de l\'inscription');
      }
    } catch (error) {
      console.error('Erreur lors de l\'inscription à la session:', error);
      alert('Erreur lors de l\'inscription à la session');
    }
  }
};

// Effet pour récupérer les sujets depuis le backend lors du montage du composant
useEffect(() => {
  const fetchSubjects = async () => {
    try {
      const response = await fetch('/API/GetAllSubjectNamesServlet'); // Endpoint pour récupérer les sujets
      if (response.ok) {
        const data = await response.json();
        console.log('Sujets récupérés:', data); // Log les sujets récupérés
        setSubjects(data); // Mettre à jour l'état des sujets avec les données récupérées
      } else {
        console.error('Échec de la récupération des sujets');
      }
    } catch (error) {
      console.error('Erreur lors de la récupération des sujets:', error);
    }
  };

  fetchSubjects();
}, []);

//////////////////////////////

  return (
    <div>
      <h1>Calendar</h1>
      <button className="back-button" onClick={navigateToHome}>Back</button> {/* Back button */}
      <div className="search-bar">
        <input 
          className="search-input"
          type="text" 
          placeholder="Search by day, subject, or tutor name" 
          value={filterCriteria} 
          onChange={handleFilterChange} 
          style={{ fontSize: '14px' }} 
        />
        <button className="search-button" onClick={handleFilterSubmit}>Search</button>
      </div>
      <div className="button-container">
        <button onClick={() => handleSwitchView('dayGridMonth')}>Month</button>
        <button onClick={() => handleSwitchView('timeGridWeek')}>Week</button>
        <button onClick={() => handleSwitchView('timeGridDay')}>Day</button>
        {userType === 'tutor' && (
          <button onClick={handleCreateEventButtonClick}>Create an Event</button>
        )}
      </div>
      <FullCalendar 
        ref={calendarRef}
        plugins={[dayGridPlugin, timeGridPlugin, interactionPlugin]} 
        initialView="timeGridWeek"
        selectable={true} 
        editable={true} 
        eventClick={handleEventClick} 
        eventContent={renderEventContent} 
        slotMinTime="08:00:00" 
        slotMaxTime="18:00:00" 
        events={events} 
      />

      <Modal
        isOpen={selectedEvent !== null || isCreatingEvent}
        onRequestClose={closeModal}
        contentLabel={isCreatingEvent ? "Create an Event" : "Event Details"}
        className="react-modal-content"
        overlayClassName="react-modal-overlay"
      >
        {isCreatingEvent ? (
          <div>
            <form onSubmit={handleSubmit}>
              <label>Title:</label>
              <input type="text" name="title" />
              <label>Start Date:</label>
              <input type="datetime-local" name="startDate" />
              <label>End Date:</label>
              <input type="datetime-local" name="endDate" />
              <label>Subject:</label>
                  <select name="subject" value={selectedSubject} onChange={(e) => setSelectedSubject(e.target.value)}>
                  <option value="">Select Subject</option>
                  {subjects && subjects.map((subject, index) => ( 
                    <option key={index} value={subject}>{subject}</option>
                  ))}
                 </select>
              <label>Lesson Title:</label>
              <input type="text" name="lessonTitle" />
              <label>Max Students:</label>
              <input type="number" name="maxStudents" />
              <label>Session Points:</label>
              <input type="text" name="sessionPoints" />
              <label>Additional Info:</label>
              <input type="text" name="additionalInfo" />
              <button type="submit">Create</button>
              <button type="button" onClick={closeModal}>Cancel</button>
            </form>
          </div>
        ) : (
          selectedEvent && selectedEvent.extendedProps && (
            <div>
              <h2>{selectedEvent.title}</h2>
              <p>Start: {selectedEvent.startStr}</p>
              <p>End: {selectedEvent.endStr}</p>
              <p>Subject: {selectedEvent.extendedProps.subject}</p>
              <p>Tutor: {selectedEvent.extendedProps.tutor}</p>
              <p>Lesson Title: {selectedEvent.extendedProps.lessonTitle}</p>
              <p>Session Points: {selectedEvent.extendedProps.sessionPoints.join(', ')}</p>
              <p>Additional Info: {selectedEvent.extendedProps.additionalInfo}</p>
              {userType === 'student' && (
                  <button onClick={handleEnrollClick}>Enroll</button>
                )}              
              <button className="react-modal-close" onClick={handleCloseButtonClick}>Close</button>

            </div>
          )
        )}
      </Modal>
      <button onClick={handleNextButtonClick}>Next</button> 
    </div>
  );
};

export default SessionPage;