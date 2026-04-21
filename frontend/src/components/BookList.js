import React from 'react';
import { deleteBook } from '../services/api';

function BookList({ books, onEdit, onRefresh }) {
  const handleDelete = async (id) => {
    if (window.confirm('Are you sure you want to delete this book?')) {
      await deleteBook(id);
      onRefresh();
    }
  };

  if (books.length === 0) {
    return <div className="no-books">No books found. Add your first book!</div>;
  }

  return (
    <div className="book-list">
      {books.map((book) => (
        <div key={book.id} className="book-card">
          <h3>{book.title}</h3>
          <div className="author">✍️ {book.author}</div>
          <div className="isbn">📖 ISBN: {book.isbn || 'N/A'}</div>
          <div className="year">📅 Year: {book.publicationYear || 'N/A'}</div>
          <div className="summary">
            <strong>Summary:</strong> {book.summary || 'No summary available'}
          </div>
          <div className="card-actions">
            <button className="edit-btn" onClick={() => onEdit(book)}>
              Edit
            </button>
            <button className="delete-btn" onClick={() => handleDelete(book.id)}>
              Delete
            </button>
          </div>
        </div>
      ))}
    </div>
  );
}

export default BookList;
