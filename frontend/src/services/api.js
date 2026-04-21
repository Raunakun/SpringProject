import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

export const getAllBooks = async () => {
  try {
    const response = await api.get('/books');
    return response.data;
  } catch (error) {
    console.error('Error fetching books:', error);
    return [];
  }
};

export const addBook = async (book) => {
  try {
    const response = await api.post('/books', book);
    return response.data;
  } catch (error) {
    console.error('Error adding book:', error);
    throw error;
  }
};

export const updateBook = async (book) => {
  try {
    const response = await api.put('/books', book);
    return response.data;
  } catch (error) {
    console.error('Error updating book:', error);
    throw error;
  }
};

export const deleteBook = async (id) => {
  try {
    const response = await api.delete(`/books/${id}`);
    return response.data;
  } catch (error) {
    console.error('Error deleting book:', error);
    throw error;
  }
};

export const searchBooks = async (query) => {
  try {
    const response = await api.get(`/books/search?q=${query}`);
    return response.data;
  } catch (error) {
    console.error('Error searching books:', error);
    return [];
  }
};

export const fetchBookSummary = async (title, author) => {
  try {
    const response = await api.get(`/books/fetch-summary?title=${encodeURIComponent(title)}&author=${encodeURIComponent(author)}`);
    return response.data.summary;
  } catch (error) {
    console.error('Error fetching summary:', error);
    return 'Unable to fetch summary';
  }
};
