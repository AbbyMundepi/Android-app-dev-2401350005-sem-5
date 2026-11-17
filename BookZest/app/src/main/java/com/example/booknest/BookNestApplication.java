package com.example.booknest;

import android.app.Application;
import android.util.Log;

/**
 * Custom Application class for BookNest.
 * Initializes the process-wide BookRepository upon application startup.
 */
public class BookNestApplication extends Application {
    private static final String TAG = "BookNestApplication";
    private static BookNestApplication instance;
    private BookRepository bookRepository;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        Log.d(TAG, "BookNestApplication onCreate() called. Initializing BookRepository...");
        bookRepository = BookRepository.getInstance();
    }

    public static BookNestApplication getInstance() {
        return instance;
    }

    public BookRepository getBookRepository() {
        return bookRepository;
    }
}
