  import React from 'react';
  import { Navigate } from 'react-router-dom';
  import { useGlobalState } from '../context/GlobalStateContext';

  const PrivateRoute = ({ element }) => {
    const { isLoggedIn } = useGlobalState();

    if (!isLoggedIn) {
      return <Navigate to="/login" />;
    }

    return element;
  };

  export default PrivateRoute;