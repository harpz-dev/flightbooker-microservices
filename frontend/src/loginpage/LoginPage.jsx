import React, { useState, useEffect } from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import './LoginPage.css';
import LoginForm from '../components/LoginForm';
import { useGlobalState } from '../context/GlobalStateContext';
import LogoPanel from '../components/LogoPanel';

const LoginPage = () => {

  const [token, setToken] = useState(localStorage.getItem('accessToken') || '');
  const [feedbackMessage, setFeedbackMessage] = useState(''); //feedback message on login form


  return (
    <div className="d-flex flex-column flex-md-row align-items-center justify-content-center vh-100">
      <div>
          <LogoPanel />
      </div>

      <div className="card shadow card-container" style={{ minWidth: "300px" }}>
          <LoginForm token={token} setToken={setToken} setFeedbackMessage={setFeedbackMessage} />
          {feedbackMessage && <div className="feedback-message">{feedbackMessage}</div>}
      </div>
  </div>
  );
};

export default LoginPage;
