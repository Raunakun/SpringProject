import React, { useState, useEffect } from 'react';
import { addBook, updateBook, fetchBookSummary } from '../services/api';

function BookForm({ onClose, onSave, editingBook }) {
  const [formData, setFormData] = useState({
    title: '',
    author: '',
    isbn: '',
    publicationYear: '',
    summary: '',
  });
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (editingBook) {
      setFormData({
        title: editingBook.title || '',
        author: editingBook.author || '',
        isbn: editingBook.isbn || '',
        publicationYear: editingBook.publicationYear
