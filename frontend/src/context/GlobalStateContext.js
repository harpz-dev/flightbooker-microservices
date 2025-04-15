import React, { createContext, useState, useContext, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import "./global.css";

const GlobalStateContext = createContext();

export const useGlobalState = () => useContext(GlobalStateContext);

export const GlobalStateProvider = ({ children }) => {
  const [isDarkMode, setIsDarkMode] = useState(() => {
    const storedDarkMode = localStorage.getItem('isDarkMode');
    return storedDarkMode === 'true';  // Default to true if it's not set in localStorage
  });

  const [isLoggedIn, setIsLoggedIn] = useState(() => {
    const storedLoginState = localStorage.getItem('isLoggedIn');
    return storedLoginState === 'true';
  });

  const navigate = useNavigate();

  useEffect(() => {
    localStorage.setItem('isLoggedIn', isLoggedIn ? 'true' : 'false');
    if (isLoggedIn) {
      navigate('/dashboard');
    } else {
      navigate('/login');
      console.error("loginStateChanged");
    }
  }, [isLoggedIn, navigate]);

  useEffect(() => {
    if (isDarkMode) {
      document.body.classList.add("dark-mode");
    } else {
      document.body.classList.remove("dark-mode");
    }
    localStorage.setItem('isDarkMode', isDarkMode ? 'true' : 'false'); // Persist dark mode state
    console.log("Current body classes:", document.body.classList);
  }, [isDarkMode]);

  useEffect(() => {
    const handleStorageChange = () => {
      setIsLoggedIn(localStorage.getItem('isLoggedIn') === 'true');
    };

    window.addEventListener('storage', handleStorageChange);
    return () => window.removeEventListener('storage', handleStorageChange);
  }, []);

  return (
    <GlobalStateContext.Provider value={{ isDarkMode, setIsDarkMode, isLoggedIn, setIsLoggedIn }}>
      {children}
    </GlobalStateContext.Provider>
  );
};
