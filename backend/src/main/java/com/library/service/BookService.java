package com.library.service;

import com.library.dao.BookDAO;
import com.library.model.Book;
import java.util.List;

public class BookService {
    private BookDAO bookDAO;
    private WebScraperService scraperService;
    
    public BookService() {
        this.bookDAO = new BookDAO();
        this.scraperService = new WebScraperService();
    }
    
    public boolean addBook(Book book, boolean fetchSummary) {
        if (fetchSummary && (book.getSummary() == null || book.getSummary().isEmpty())) {
            String summary = scraperService.fetchBookSummaryFromOpenLibrary(
                book.getTitle(), book.getAuthor());
            book.setSummary(summary);
        }
        return bookDAO.addBook(book);
    }
    
    public List<Book> getAllBooks() {
        return bookDAO.getAllBooks();
    }
    
    public Book getBookById(int id) {
        return bookDAO.getBookById(id);
    }
    
    public boolean updateBook(Book book) {
        return bookDAO.updateBook(book);
    }
    
    public boolean deleteBook(int id) {
        return bookDAO.deleteBook(id);
    }
    
    public List<Book> searchBooks(String query) {
        return bookDAO.searchBooks(query);
    }
    
    public String fetchBookSummary(String title, String author) {
        return scraperService.fetchBookSummaryFromOpenLibrary(title, author);
    }
}
