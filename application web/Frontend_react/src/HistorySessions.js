import React, { useEffect, useState } from 'react';
import { Card, Button, Modal } from 'react-bootstrap';
import './HistorySessionsPage.css';

const HistorySessionsPage = ({ userEmail, navigateToHome }) => {
    const [sessions, setSessions] = useState([]);
    const [selectedSession, setSelectedSession] = useState(null);
    const [showModal, setShowModal] = useState(false);

    useEffect(() => {
        fetch(`/API/get_student_sessions?email=${userEmail}`)
            .then(response => response.json())
            .then(data => setSessions(data))
            .catch(error => console.error('Error fetching sessions:', error));
    }, [userEmail]);

    const handleCardClick = (session) => {
        setSelectedSession(session);
        setShowModal(true);
    };

    const handleClose = () => setShowModal(false);

    return (
        <div className="history-sessions-container">
            <h1 className="history-sessions-header">History Sessions</h1>
            <Button onClick={navigateToHome} className="history-sessions-back-button">Back to Home</Button>
            {sessions.map((session, index) => (
                <Card key={index} className="history-sessions-card">
                    <Card.Body>
                        <Card.Title className="history-sessions-card-title">{session.tutor}</Card.Title>
                        <Card.Text className="history-sessions-card-text">
                            {session.title} - {session.lessonTitle}
                        </Card.Text>
                        <Button variant="primary" onClick={() => handleCardClick(session)} className="history-sessions-button">
                            View Details
                        </Button>
                    </Card.Body>
                </Card>
            ))}
            {selectedSession && (
                <Modal show={showModal} onHide={handleClose} className="history-sessions-modal" backdropClassName="history-sessions-modal-backdrop" centered>
                    <div className="modal-centered">
                        <Modal.Header closeButton className="history-sessions-modal-header">
                            <Modal.Title className="history-sessions-modal-title">{selectedSession.title}</Modal.Title>
                        </Modal.Header>
                        <Modal.Body className="history-sessions-modal-body">
                            <p><strong>Start:</strong> {new Date(selectedSession.start).toLocaleString()}</p>
                            <p><strong>End:</strong> {new Date(selectedSession.end).toLocaleString()}</p>
                            <p><strong>Subject:</strong> {selectedSession.subject}</p>
                            <p><strong>Tutor:</strong> {selectedSession.tutor}</p>
                            <p><strong>Lesson Title:</strong> {selectedSession.lessonTitle}</p>
                            <p><strong>Additional Info:</strong> {selectedSession.additionalInfo}</p>
                            <p><strong>Session Points:</strong></p>
                            <ul>
                                {selectedSession.sessionPoints.map((point, index) => (
                                    <li key={index}>{point}</li>
                                ))}
                            </ul>
                        </Modal.Body>
                        <Modal.Footer className="history-sessions-modal-footer">
                            <Button variant="secondary" onClick={handleClose} className="history-sessions-button">
                                Close
                            </Button>
                        </Modal.Footer>
                    </div>
                </Modal>
            )}
        </div>
    );
};

export default HistorySessionsPage;
