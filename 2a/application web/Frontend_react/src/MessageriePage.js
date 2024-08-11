import React, { useState, useEffect } from 'react';
import './MessageriePage.css';

const MessageriePage = ({ userEmail, userType, onLogout, navigateToHome }) => {
   
    const [receiverEmail, setReceiverEmail] = useState(''); // État pour stocker l'email du destinataire
    const [messageContent, setMessageContent] = useState(''); // État pour stocker le contenu du message
    const [messages, setMessages] = useState([]); // État pour stocker les messages
    const [receivers, setReceivers] = useState([]); // État pour stocker les destinataires
    const [notification, setNotification] = useState(''); // État pour stocker les notifications
    const [showMessages, setShowMessages] = useState(false); // État pour indiquer si les messages doivent être affichés ou non

    // Effet pour récupérer les destinataires au chargement du composant
    useEffect(() => {
        fetchReceivers();
    }, []);

    // Gestionnaire pour récupérer les destinataires
    const fetchReceivers = async () => {
        try {
            const queryParams = new URLSearchParams({
                user_type: userType
            }).toString();

            const response = await fetch(`/API/getReceivers?${queryParams}`);
            if (response.ok) {
                const data = await response.json();
                setReceivers(data);
            } else {
                console.error('Erreur lors de la récupération des destinataires:', response.statusText);
            }
        } catch (error) {
            console.error('Erreur lors de la récupération des destinataires:', error);
        }
    };
    // Gestionnaire des messages envoyés.
    const fetchMessages = async () => {
        try {
            const queryParams = new URLSearchParams({
                user_email: userEmail,
                target_email: receiverEmail,
                user_type: userType,
            }).toString();

            const response = await fetch(`/API/messages?${queryParams}`, {
                method: 'GET',
                headers: { 'Content-Type': 'application/json' },
            });

            if (response.ok) {
                const data = await response.json();
                console.log("Messages récupérés:", data);
                setMessages(data);
                setShowMessages(true);
            } else {
                console.error('Erreur lors de la récupération des messages:', response.statusText);
            }
        } catch (error) {
            console.error('Erreur lors de la récupération des messages:', error);
        }
    };
    // Gestionnaire des messages reçus.
    const fetchMessages2 = async () => {
        try {
            const queryParams = new URLSearchParams({
                user_email: userEmail,
                user_type: userType,
            }).toString();

            const response = await fetch(`/API/messages2?${queryParams}`, {
                method: 'GET',
                headers: { 'Content-Type': 'application/json' },
            });

            if (response.ok) {
                const data = await response.json();
                console.log("Messages récupérés:", data);
                setMessages(data);
                setShowMessages(true);
            } else {
                console.error('Erreur lors de la récupération des messages:', response.statusText);
            }
        } catch (error) {
            console.error('Erreur lors de la récupération des messages:', error);
        }
    };

    // Gestionnaire pour envoyer un message
    const sendMessage = async (e) => {
        e.preventDefault();
        const messageData = {
            sender_email: userEmail,
            receiver_email: receiverEmail,
            usertype: userType,
            message: messageContent,
        };

        console.log("Envoi des données:", messageData);

        try {
            const response = await fetch('/API/messages', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(messageData),
            });

            if (response.ok) {
                setNotification('Message envoyé avec succès');
                setMessageContent('');
                setShowMessages(false); // Masquer les messages pour éviter l'affichage de données obsolètes
            } else {
                setNotification('Erreur lors de l\'envoi du message');
            }
        } catch (error) {
            console.error('Erreur lors de l\'envoi du message:', error);
            setNotification('Erreur lors de l\'envoi du message');
        }
    };

    // Gestionnaire pour gérer le clic sur le bouton de récupération des messages envoyés
    const handleRetrieveMessages = () => {
        fetchMessages();
    };

    // Gestionnaire pour gérer le clic sur le bouton de récupération des messages reçus
    const handleRetrieveMessages2 = () => {
        fetchMessages2();
    };

    return (
        <div className="container">
            <h1>Système de Messagerie</h1>

            <div>
                <button className="back-button" onClick={navigateToHome}>Retour</button> {/* Bouton de retour */}
                <h2>Envoyer un Message</h2>
                <select value={receiverEmail} onChange={e => setReceiverEmail(e.target.value)}>
                    <option value="">Sélectionner le Destinataire</option>
                    {receivers.map(receiver => (
                        <option key={receiver} value={receiver}>
                            {receiver}
                        </option>
                    ))}
                </select>
                <textarea
                    placeholder="Contenu du Message"
                    value={messageContent}
                    onChange={e => setMessageContent(e.target.value)}
                ></textarea>
                <button onClick={sendMessage}>Envoyer le Message</button>
                <button onClick={handleRetrieveMessages}>Récupérer les Messages Envoyés</button>
                <button onClick={handleRetrieveMessages2}>Récupérer les Messages Reçus</button>

                {notification && <p className="notification">{notification}</p>}
            </div>

            <div>
                {showMessages && (
                    <div>
                        <h2>Messages Reçus ou envoyés</h2>
                        <table>
                            <thead>
                                <tr>
                                    <th>Expéditeur</th>
                                    <th>Destinataire</th>
                                    <th>Message</th>
                                </tr>
                            </thead>
                            <tbody>
                                {messages.map((message, index) => (
                                    <tr key={index}>
                                        <td className="sender">{message.sender}</td>
                                        <td className="receiver">{message.receiver}</td>
                                        <td className="message-content">{message.content}</td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                    </div>
                )}
            </div>
        </div>
    );
};

export default MessageriePage;
