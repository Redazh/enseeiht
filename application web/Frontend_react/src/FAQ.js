import React from 'react';
import './SupportFAQ.css';


// Définition du composant fonctionnel FAQ
const FAQ = ({ onBack }) => {
    // Déclaration d'un tableau d'objets contenant les questions et réponses de la FAQ
  const faqs = [
    {
      question: 'Comment puis-je créer un compte?',
      answer: 'Pour créer un compte, cliquez sur le bouton "S\'inscrire" en haut à droite de la page d\'accueil et remplissez le formulaire d\'inscription.'
    },
    {
      question: 'Comment puis-je réinitialiser mon mot de passe?',
      answer: 'Pour réinitialiser votre mot de passe, cliquez sur "Mot de passe oublié?" sur la page de connexion et suivez les instructions.'
    },
    {
      question: 'Comment réserver une session de tutorat?',
      answer: 'Pour réserver une session de tutorat, connectez-vous à votre compte, allez dans la section "Sessions de Tutorat" et sélectionnez une session disponible.'
    },
    
  ];

  return (
    <div className="faq-container">
      <h2>FAQ</h2>
      {faqs.map((faq, index) => (
        <div key={index} className="faq-item">
          <h3>{faq.question}</h3>
          <p>{faq.answer}</p>
        </div>
      ))}
      <button onClick={onBack} className="back-button">Back</button>
    </div>
  );
};

export default FAQ;
