import React from 'react';
import { useGlobalState } from '../context/GlobalStateContext'; 


const DarkModeButton = () => {

  const {isDarkMode, setIsDarkMode} = useGlobalState();
  return (
    <button 
      type="button" 
      onClick={() => {
        setIsDarkMode(prev => !prev);

      }
    }

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
      {isDarkMode ? 'Light Mode' : 'Dark Mode'}
    </button>
  );
};




export default DarkModeButton;
