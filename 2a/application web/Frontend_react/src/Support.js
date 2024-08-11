import React, { useState } from 'react';
import './SupportFAQ.css';

const Support = ({ onBack }) => {
  const [name, setName] = useState('');
  const [email, setEmail] = useState('');
  const [message, setMessage] = useState('');
  const [responseMessage, setResponseMessage] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    const supportRequest = { name, email, message };

    try {
      const response = await fetch('/API/support', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(supportRequest),
      });

      const data = await response.json();
      setResponseMessage(data.message);
    } catch (error) {
      setResponseMessage('erreur.');
    }
  };

  return (
    <div className="support-container">
      <h2>Support</h2>
      <p>Si vous avez besoin d'aide supplémentaire, n'hésitez pas à nous contacter.</p>
      <h3>Horaires de disponibilité</h3>
      <p>Lundi - Vendredi : 9h00 - 18h00</p>
      <h3>Contactez-nous</h3>
      <form className="support-form" onSubmit={handleSubmit}>
        <label>
          Nom:
          <input type="text" value={name} onChange={(e) => setName(e.target.value)} required />
        </label>
        <label>
          Email:
          <input type="email" value={email} onChange={(e) => setEmail(e.target.value)} required />
        </label>
        <label>
          Message:
          <textarea value={message} onChange={(e) => setMessage(e.target.value)} required></textarea>
        </label>
        <button type="submit">Envoyer</button>
      </form>
      {responseMessage && <p>{responseMessage}</p>}
      <button onClick={onBack} className="back-button">Back</button>
    </div>
  );
};

export default Support;
