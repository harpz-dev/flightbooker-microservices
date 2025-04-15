import React from "react";
import "./LogoPanel.css"; // CSS file for styling
import logoLight from "../assets/logo.png"; // Light mode logo

const LogoPanel = () => {
  return (
    <div className="logo-panel">
      <img src={logoLight} alt="Logo" className="logo" />
      <h2 className="logo-text">Welcome to FlightBooker</h2>
      <p className="tagline">Your journey begins here!</p>
    </div>
  );
};

export default LogoPanel;
