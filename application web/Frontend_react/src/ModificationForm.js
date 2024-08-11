import React, { useState, useEffect } from 'react';
import './ModificationForm.css';  

const ModificationForm = ({ userType, userEmail, navigateToHome }) => {
  const [formData, setFormData] = useState({
    nom: '',
    prenom: '',
    password: '',
    contactInfo: {
      telephone: '',
      adresse: ''
    },
    allSubjectNames: [], 
    selectedSubjects: [],
  });

  // Effet pour récupérer tous les noms de matières au chargement du composant

  useEffect(() => {
    fetch('/API/GetAllSubjectNamesServlet')
      .then(response => response.json())
      .then(data => {
        console.log('Données récupérées:', data);
        setFormData(prev => ({ ...prev, allSubjectNames: data }));
      })
      .catch(error => console.error('Erreur lors de la récupération des matières:', error));
  }, []);

  // Gestionnaire pour gérer les changements dans les champs de saisie
  const handleInputChange = (e) => {
    const { name, value } = e.target;
    if (name === 'subjectNames') {
      const selectedOptions = Array.from(e.target.selectedOptions, option => option.value);
      setFormData(prev => ({ ...prev, selectedSubjects: selectedOptions }));
    } else if (name in formData.contactInfo) {
      setFormData(prev => ({
        ...prev,
        contactInfo: { ...prev.contactInfo, [name]: value }
      }));
    } else {
      setFormData(prev => ({ ...prev, [name]: value }));
    }
  };

  // Gestionnaire  pour soumettre le formulaire de mise à jour
  const handleSubmit = async (e) => {
    e.preventDefault();
    
    const data = {
      userType: userType,
      email: userEmail, 
      nom: formData.nom,
      prenom: formData.prenom,
      motDePasse: formData.password,
      telephone: formData.contactInfo.telephone,
      adresse: formData.contactInfo.adresse,
      subjectNames: formData.selectedSubjects.join(',') 
    };

    try {
      const response = await fetch('/API/UpdateInfoServlet', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(data),
      });

      if (response.ok) {
        alert('Mise à jour réussie');
      } else {
        alert('Erreur lors de la mise à jour');
      }
    } catch (error) {
      alert('Erreur lors de la tentative de mise à jour');
    }
  };

  return (
    <div className="form-container">
      <button className="back-button" onClick={navigateToHome}>Back</button> 
      <h2>Modification des Informations Utilisateur</h2>
      <form onSubmit={handleSubmit}>
        <input type="text" name="nom" placeholder="Nom" onChange={handleInputChange} value={formData.nom} required />
        <input type="text" name="prenom" placeholder="Prénom" onChange={handleInputChange} value={formData.prenom} required />
        <input type="password" name="password" placeholder="Mot de passe" onChange={handleInputChange} value={formData.password} required />
        <input type="text" name="telephone" placeholder="Téléphone" onChange={handleInputChange} value={formData.contactInfo.telephone} required />
        <input type="text" name="adresse" placeholder="Adresse" onChange={handleInputChange} value={formData.contactInfo.adresse} required />
        {userType === 'tutor' && (
          <div className="select-container">
            <label>Matieres disponibles:</label>
            <select name="subjectNames" multiple={true} onChange={handleInputChange} value={formData.selectedSubjects}>
              {formData.allSubjectNames.map(subjectName => (
                <option key={subjectName} value={subjectName}>{subjectName}</option>
              ))}
            </select>
          </div>
        )}
        
        <button type="submit">Mettre à jour</button>
      </form>
    </div>
  );
};

export default ModificationForm;