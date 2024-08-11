import React, { useState } from 'react';
import UserTypeSelection from './UserTypeSelection';

const RegistrationForm = ({ onRegistrationSubmit }) => {
  const [formData, setFormData] = useState({
    nom: '',
    prenom: '',
    email: '',
    password: '',
    contactInfo: {
      telephone: '',
      adresse: ''
    }
  });
  const [selectedUserType, setSelectedUserTypeRegistration] = useState('');

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    if (name in formData.contactInfo) {
      setFormData(prev => ({
        ...prev,
        contactInfo: { ...prev.contactInfo, [name]: value }
      }));
    } else {
      setFormData(prev => ({ ...prev, [name]: value }));
    }
  };

  // Gestionnaire pour soumettre le formulaire d'inscription
  const handleSubmit = (e) => {
    e.preventDefault();
    onRegistrationSubmit(selectedUserType,formData);
  };

  return (
    <div>
      <h2>Inscription</h2>
      <form onSubmit={handleSubmit}>
        <input type="text" name="nom" placeholder="Nom" onChange={handleInputChange} value={formData.nom} required />
        <input type="text" name="prenom" placeholder="Prénom" onChange={handleInputChange} value={formData.prenom} required />
        <input type="email" name="email" placeholder="Adresse email" onChange={handleInputChange} value={formData.email} required />
        <input type="password" name="password" placeholder="Mot de passe" onChange={handleInputChange} value={formData.password} required />
        <input type="text" name="telephone" placeholder="Téléphone" onChange={handleInputChange} value={formData.contactInfo.telephone} required/>
        <input type="text" name="adresse" placeholder="Adresse" onChange={handleInputChange} value={formData.contactInfo.adresse} required/>
        <UserTypeSelection onSelect={setSelectedUserTypeRegistration} />
        <button type="submit">Submit</button>
      </form>
    </div>
  );
};

export default RegistrationForm;
