package com.library.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.library.model.Book;
import com.library.service.BookService;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

public class ApiServer {
    private static final int PORT = 8080;
    private static BookService bookService = new BookService();
    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();
    
    public static void start() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
        
        // Enable CORS
        server.createContext("/api/books", new CorsHandler(new BookHandler()));
        server.createContext("/api/books/search", new CorsHandler(new SearchHandler()));
        server.createContext("/api/books/fetch-summary", new CorsHandler(new FetchSummaryHandler()));
        
        server.setExecutor(null);
        server.start();
        System.out.println("Server started on port " + PORT);
    }
    
    static class CorsHandler implements HttpHandler {
        private HttpHandler handler;
        
        CorsHandler(HttpHandler handler) {
            this.handler = handler;
        }
        
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
            
            if (exchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
                exchange.sendResponseHeaders(204, -1);
                return;
            }
            
            handler.handle(exchange);
        }
    }
    
    static class BookHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String method = exchange.getRequestMethod();
            
            try {
                if (method.equalsIgnoreCase("GET")) {
                    List<Book> books = bookService.getAllBooks();
                    String response = gson.toJson(books);
                    sendResponse(exchange, 200, response);
                } 
                else if (method.equalsIgnoreCase("POST")) {
                    String body = new BufferedReader(new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8))
                            .lines().collect(Collectors.joining("\n"));
                    Book book = gson.fromJson(body, Book.class);
                    boolean success = bookService.addBook(book, true);
                    sendResponse(exchange, success ? 201 : 400, success ? "Book added successfully" : "Failed to add book");
                }
                else if (method.equalsIgnoreCase("PUT")) {
                    String body = new BufferedReader(new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8))
                            .lines().collect(Collectors.joining("\n"));
                    Book book = gson.fromJson(body, Book.class);
                    boolean success = bookService.updateBook(book);
                    sendResponse(exchange, success ? 200 : 400, success ? "Book updated successfully" : "Failed to update book");
                }
                else if (method.equalsIgnoreCase("DELETE")) {
                    String path = exchange.getRequestURI().getPath();
                    String[] parts = path.split("/");
                    int id = Integer.parseInt(parts[parts.length - 1]);
                    boolean success = bookService.deleteBook(id);
                    sendResponse(exchange, success ? 200 : 400, success ? "Book deleted successfully" : "Failed to delete book");
                }
            } catch (Exception e) {
                e.printStackTrace();
                sendResponse(exchange, 500, "Internal Server Error");
            }
        }
    }
    
    static class SearchHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (exchange.getRequestMethod().equalsIgnoreCase("GET")) {
                String query = exchange.getRequestURI().getQuery();
                String searchTerm = query != null ? query.split("=")[1] : "";
                List<Book> books = bookService.searchBooks(searchTerm);
                String response = gson.toJson(books);
                sendResponse(exchange, 200, response);
            }
        }
    }
    
    static class FetchSummaryHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (exchange.getRequestMethod().equalsIgnoreCase("GET")) {
                String query = exchange.getRequestURI().getQuery();
                String[] params = query.split("&");
                String title = "", author = "";
                for (String param : params) {
                    String[] pair = param.split("=");
                    if (pair[0].equals("title")) title = pair[1];
                    if (pair[0].equals("author")) author = pair[1];
                }
                String summary = bookService.fetchBookSummary(title, author);
                sendResponse(exchange, 200, "{\"summary\": \"" + summary.replace("\"", "\\\"") + "\"}");
            }
        }
    }
    
    private static void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
