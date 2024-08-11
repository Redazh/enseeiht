import React, { useState } from 'react';
import './p1.css'; 

const UserTypeSelection = ({ onSelect }) => {
  const [selectedUserType, setSelectedUserType] = useState('');

  const handleUserTypeClick = (userType) => {
    setSelectedUserType(userType);
    onSelect(userType);
  };

  return (
    <div className="user-type-container">
      <button type="button"
        className={`user-type-button ${selectedUserType === 'tutor' ? 'active' : ''}`}
        onClick={() => handleUserTypeClick('tutor')}
      >
        Tutor
      </button>
      <button type="button"
        className={`user-type-button ${selectedUserType === 'student' ? 'active' : ''}`}
        onClick={() => handleUserTypeClick('student')}
      >
        Student
      </button>
    </div>
  );
};

export default UserTypeSelection;
