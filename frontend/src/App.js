import React, { useState, useEffect } from 'react';
import BookList from './components/BookList';
import BookForm from './components/BookForm';
import SearchBar from './components/SearchBar';
import Navbar from './components/Navbar';
import { getAllBooks, searchBooks } from './services/api';
import './App.css';

function App() {
  const [books, setBooks] = useState([]);
  const [loading, setLoading] = useState(false);
  const [showForm, setShowForm] = useState(false);
  const [editingBook, setEditingBook] = useState(null);

  useEffect(() => {
    fetchBooks();
  }, []);

  const fetchBooks = async () => {
    setLoading(true);
    const data = await getAllBooks();
    setBooks(data);
    setLoading(false);
  };

  const handleSearch = async (query) => {
    if (query.trim() === '') {
      fetchBooks();
    } else {
      setLoading(true);
      const results = await searchBooks(query);
      setBooks(results);
      setLoading(false);
    }
  };

  const handleBookSaved = () => {
    setShowForm(false);
    setEditingBook(null);
    fetchBooks();
  };

  const handleEdit = (book) => {
    setEditingBook(book);
    setShowForm(true);
  };

  return (
    <div className="app">
      <Navbar onAddClick={() => {
        setEditingBook(null);
        setShowForm(true);
      }} />
      
      <div className="container">
        <SearchBar onSearch={handleSearch} />
        
        {showForm && (
          <BookForm 
            onClose={() => {
              setShowForm(false);
              setEditingBook(null);
            }}
            onSave={handleBookSaved}
            editingBook={editingBook}
          />
        )}
        
        {loading ? (
          <div className="loading">Loading books...</div>
        ) : (
          <BookList books={books} onEdit={handleEdit} onRefresh={fetchBooks} />
        )}
      </div>
    </div>
  );
}

export default App;
