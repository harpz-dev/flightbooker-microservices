import React, { useState, useEffect } from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';

import { useGlobalState } from '../context/GlobalStateContext'; 
import { LOGIN_URL, REGISTER_URL, REFRESH_URL } from '../config/apiUrls';

const LoginForm = ({token, setToken, feedbackMessage, setFeedbackMessage}) =>{

      const [username, setUsername] = useState('');
      const [password, setPassword] = useState('');
      const [email, setEmail] = useState('');
      const [isRegistering, setIsRegistering] = useState(false);
      const [countdown, setCountdown] = useState(5);
      const [isRedirecting, setIsRedirecting] = useState(false);
    
    
      //const [token, setToken] = useState(localStorage.getItem('accessToken') || '');
      const {setIsLoggedIn} = useGlobalState();
      const {isDarkMode} = useGlobalState();
    
    
      // Error states for validation feedback
      const [usernameError, setUsernameError] = useState('');
      const [emailError, setEmailError] = useState('');
      const [passwordError, setPasswordError] = useState('');

       // 🔹 Auto-clear password field on any state update (form switch, submission feedback, etc.)
  useEffect(() => {
    setPassword('');
  }, [isRegistering, feedbackMessage]);

  useEffect(() => {
    setFeedbackMessage('');
    setUsernameError('');
  }, [isRegistering]);


  // Handle token refresh
  const handleRefresh = async (e) => {
    e.preventDefault();
    setFeedbackMessage('Attempting to refresh token...');
    try {
      const response = await fetch(REFRESH_URL, {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        credentials: 'include',
      });

      const data = await response.json();
      if (response.ok) {
        setFeedbackMessage('Token refreshed successfully!');
        localStorage.setItem('accessToken', data.accessToken);
        setToken(data.accessToken);
      } else {
        setFeedbackMessage(data.message);
      }
    } catch (error) {
      setFeedbackMessage('Error occurred during token refresh.');
      console.error('Token refresh error:', error);
    }
  };


  // Handle login
  const handleLogin = async (e) => {
    e.preventDefault();
    setFeedbackMessage('Attempting to log in...');
    try {
      const formData = new URLSearchParams();
      formData.append('username', username);
      formData.append('password', password);

      const response = await fetch(LOGIN_URL, {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        credentials: 'include',
        body: formData.toString(),
      });

      const data = await response.json();
      if (response.ok) {
        setFeedbackMessage('Login successful!');
        setIsLoggedIn(true);
        localStorage.setItem('accessToken', data.accessToken);
        setToken(data.accessToken);
      } else {
        setFeedbackMessage(data.message);
      }
    } catch (error) {
      setFeedbackMessage('Error occurred during login.');
      console.error('Login error:', error);
    }
  };

  // Handle registration
  const handleRegister = async (e) => {
    e.preventDefault();
    setFeedbackMessage('');
    setUsernameError('');
    setEmailError('');
    setPasswordError('');

    try {
      const formData = new URLSearchParams();
      formData.append('username', username);
      formData.append('email', email);
      formData.append('password', password);

      const response = await fetch(REGISTER_URL, {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: formData.toString(),
      });

      const textResponse = await response.text();

      if (response.ok) {
        setFeedbackMessage('Registration successful! Redirecting to login...');
        setIsRedirecting(true);
        const interval = setInterval(() => {
          setCountdown((prev) => {
            if (prev === 1) {
              clearInterval(interval);
              setIsRegistering(false);
            }
            return prev - 1;
          });
        }, 1000);
      } else {
        if (textResponse.includes("Username already exists")) {
          setUsernameError("Username is already taken.");
        } else if (textResponse.includes("Email already exists")) {
          setEmailError("Email is already in use.");
        } else {
          setFeedbackMessage("Registration failed. Please try again.");
        }
      }
    } catch (error) {
      setFeedbackMessage('Error occurred during registration.');
      console.error('Registration error:', error);
    }
  };


    return(

        
            <div className="card-body">
              <h2 className="text-center mb-4">{isRegistering ? 'Register' : 'Login'}</h2>
              <form onSubmit={isRegistering ? handleRegister : handleLogin}>
                <div className="mb-3">
                  <label htmlFor="username" className="form-label">Username</label>
                  <input
                    type="text"
                    className={`form-control ${usernameError ? 'is-invalid' : ''}`}
                    id="username"
                    placeholder="Enter your username"
                    value={username}
                    onChange={(e) => setUsername(e.target.value)}
                    required
                  />
                  {usernameError && <div className="invalid-feedback">{usernameError}</div>}
                </div>
    
                {isRegistering && (
                  <div className="mb-3">
                    <label htmlFor="email" className="form-label">Email</label>
                    <input
                      type="email"
                      className={`form-control ${emailError ? 'is-invalid' : ''}`}
                      id="email"
                      placeholder="Enter your email"
                      value={email}
                      onChange={(e) => setEmail(e.target.value)}
                      required
                    />
                    {emailError && <div className="invalid-feedback">{emailError}</div>}
                  </div>
                )}
    
                <div className="mb-3">
                  <label htmlFor="password" className="form-label">Password</label>
                  <input
                    type="password"
                    className={`form-control ${passwordError ? 'is-invalid' : ''}`}
                    id="password"
                    placeholder="Enter your password"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    required
                  />
                  {passwordError && <div className="invalid-feedback">{passwordError}</div>}
                </div>
    
                <button type="submit" className="btn btn-primary w-100">
                  {isRegistering ? 'Register' : 'Login'}
                </button>
              </form>
    
              {isRedirecting && countdown > 0 && (
                <div className="countdown-message">
                  Redirecting to login in {countdown} seconds...
                </div>
              )}
    
              <div className="d-flex justify-content-between mt-3">
                <button className="btn btn-link" onClick={() => setIsRegistering(!isRegistering)}>
                  {isRegistering ? 'Already have an account? Login' : 'Don\'t have an account? Register'}
                </button>
              </div>
            </div>
         
    );

};



export default LoginForm;

