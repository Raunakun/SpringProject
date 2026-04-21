import React from 'react';

function Navbar({ onAddClick }) {
  return (
    <nav className="navbar">
      <h1>📚 Library Management System</h1>
      <button className="add-btn" onClick={onAddClick}>
        + Add New Book
      </button>
    </nav>
  );
}

export default Navbar;
