import React, { useState, useEffect } from 'react';

const TokenViewer = ({ token }) => {
    const [decodedToken, setDecodedToken] = useState(null);

  
  useEffect(() => {
    if (token) {
      try {

        const payload = JSON.parse(atob(token.split('.')[1])); // Decode Base64 payload
        const iat= payload.iat;
        const exp= payload.exp;

        const iatDate = new Date(iat * 1000); // Multiply by 1000 to convert from seconds to milliseconds
        const expDate = new Date(exp * 1000);

        //edit it to show actual date time instead of linux thingy
        payload.iat= iatDate.toLocaleString();
        payload.exp= expDate.toLocaleString();

        setDecodedToken(payload);
      } catch (error) {
        console.error('Invalid token format', error);
        setDecodedToken({ error: 'Invalid token format' });
      }
    } else {
      setDecodedToken({ error: 'No token found in localStorage' });
    }
  }, [token]);

  return (
    <div style={{ padding: '20px', fontFamily: 'Arial', background: '#f4f4f4' }}>
      <h2>Decoded JWT Token</h2>
      {decodedToken ? (
        <pre style={{ background: '#fff', padding: '10px', borderRadius: '5px' }}>
          {JSON.stringify(decodedToken, null, 2)}
        </pre>
      ) : (
        <p>Loading...</p>
      )}
    </div>
  );
};

export default TokenViewer;
