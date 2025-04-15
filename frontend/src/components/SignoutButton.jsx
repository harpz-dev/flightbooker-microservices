import React, { useState } from 'react';
import { LOGOUT_URL } from '../config/apiUrls';
import { useGlobalState } from '../context/GlobalStateContext';

const SignOutButton = () => {
  const { setIsLoggedIn } = useGlobalState();  
  const [loading, setLoading] = useState(false);

  const handleLogout = async () => {
    setLoading(true);
    try {
      const response = await fetch(LOGOUT_URL, {
        method: 'POST',
        credentials: 'include',
      });

      const data = await response.json();
      if (response.ok) {
        // Successfully logged out through API
      } else {
        // Already logged out
        console.error('Already logged in error');
      }
    } catch (error) {
      // Already logged out on client-side (refresh token already cleared/invalid)
      console.error('Logout error:', error);
    } finally {
      localStorage.removeItem('accessToken'); // Clear token
      setIsLoggedIn(false);
      setLoading(false);
    }
  };

  return (
    <button 
      type="button" 
      onClick={handleLogout} 
      disabled={loading}
      style={{
        width: '100%',        
        borderRadius: '0',     
        padding: '10px',         
        backgroundColor: 'rgba(0, 123, 255, 0.1)', // Blue with 80% opacity for transparency
        color: 'black',         
        border: 'none',           
        textAlign: 'center',     
        cursor: 'pointer',      
      }}
    >
      {loading ? 'Signing out...' : 'Sign out'}
    </button>
  );
};

export default SignOutButton;
