package com.library;

import com.library.server.ApiServer;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            ApiServer.start();
            System.out.println("==================================");
            System.out.println("Library Management System Started");
            System.out.println("API available at: http://localhost:8080/api/books");
            System.out.println("==================================");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
