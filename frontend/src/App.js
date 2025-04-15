// src/App.js

import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import { GlobalStateProvider } from "./context/GlobalStateContext"; // Global state context

import PrivateRoute from './components/PrivateRoute';

import LoginPage from "./loginpage/LoginPage"; 
import Dashboard from './dashboardpage/DashboardPage'; // 

function App() {
  return (
    <Router>
      <GlobalStateProvider>
        <Routes>
          <Route path="/login" element={<LoginPage />} />
          <Route
            path="/dashboard"
            element={<PrivateRoute element={<Dashboard />} />} // Protected route
          />
        </Routes>
      </GlobalStateProvider>
    </Router>
  );
}

export default App;
