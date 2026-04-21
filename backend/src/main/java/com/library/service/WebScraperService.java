package com.library.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.IOException;

public class WebScraperService {
    
    public String fetchBookSummaryFromOpenLibrary(String bookTitle, String author) {
        try {
            String searchQuery = (bookTitle + " " + author).replace(" ", "+");
            String url = "https://openlibrary.org/search?q=" + searchQuery;
            
            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                    .timeout(10000)
                    .get();
            
            // Extract description from the first result
            String description = doc.select(".book-description").first() != null ? 
                    doc.select(".book-description").first().text() : "";
            
            if (!description.isEmpty()) {
                return description;
            }
            
            // Alternative: Get from the book page
            String bookUrl = doc.select(".booktitle a").first() != null ?
                    "https://openlibrary.org" + doc.select(".booktitle a").first().attr("href") : null;
            
            if (bookUrl != null) {
                Document bookDoc = Jsoup.connect(bookUrl).userAgent("Mozilla/5.0").timeout(10000).get();
                description = bookDoc.select(".description").text();
                if (!description.isEmpty()) return description;
            }
            
            return "No summary available for this book. Please add manually.";
            
        } catch (IOException e) {
            e.printStackTrace();
            return "Unable to fetch summary. Please add manually.";
        }
    }
    
    public String fetchBookDetailsFromGoogleBooks(String isbn) {
        try {
            String url = "https://www.googleapis.com/books/v1/volumes?q=isbn:" + isbn;
            // This is a placeholder - in production, you would parse JSON response
            return "Summary fetched from Google Books API for ISBN: " + isbn;
        } catch (Exception e) {
            e.printStackTrace();
            return "Unable to fetch from Google Books.";
        }
    }
}
