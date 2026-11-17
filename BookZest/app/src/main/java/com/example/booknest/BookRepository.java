package com.example.booknest;

import android.util.Log;

import java.util.ArrayList;

/**
 * Repository class managing in-memory book data for BookNest.
 * Shared across the entire application process.
 */
public class BookRepository {
    private static final String TAG = "BookRepository";
    public static final int MAX_BORROW_LIMIT = 3;

    private static volatile BookRepository instance;
    private final ArrayList<Book> bookList;

    private BookRepository() {
        bookList = new ArrayList<>();
        initializeSampleBooks();
    }

    public static BookRepository getInstance() {
        if (instance == null) {
            synchronized (BookRepository.class) {
                if (instance == null) {
                    instance = new BookRepository();
                }
            }
        }
        return instance;
    }

    /**
     * Initializes the 10 required sample books.
     * All books are initially marked as Available.
     */
    private void initializeSampleBooks() {
        bookList.clear();

        bookList.add(new Book(
                "B101",
                "Learn Java",
                "James Gosling",
                "Programming",
                "Learn Java is a comprehensive introduction to Java programming. Explore object-oriented programming, classes, objects, inheritance, polymorphism and practical coding concepts tailored for modern real-world architecture.",
                true,
                R.drawable.cover_b101,
                "EPUB",
                4.8,
                120
        ));

        bookList.add(new Book(
                "B102",
                "Python Basics",
                "Eric Matthes",
                "Programming",
                "A practical, beginner-friendly guide to Python programming. Learn core concepts including data structures, functions, file handling, and hands-on project creation.",
                true,
                R.drawable.cover_b102,
                "PDF",
                4.9,
                340
        ));

        bookList.add(new Book(
                "B103",
                "The Alchemist",
                "Paulo Coelho",
                "Fiction",
                "A magical fable about following your dream. Follow the journey of Santiago, an Andalusian shepherd boy who yearns to travel in search of a worldly treasure.",
                true,
                R.drawable.cover_b103,
                "HARDCOVER",
                4.7,
                890
        ));

        bookList.add(new Book(
                "B104",
                "A Brief History of Time",
                "Stephen Hawking",
                "Science",
                "A landmark volume in science writing by world-renowned theoretical physicist Stephen Hawking, exploring cosmology, black holes, big bang theory, and the nature of time.",
                true,
                R.drawable.cover_b104,
                "EBOOK",
                4.9,
                415
        ));

        bookList.add(new Book(
                "B105",
                "Think and Grow Rich",
                "Napoleon Hill",
                "Business",
                "A classic personal development and self-improvement guide outlining thirteen principles of success, wealth creation, mindset, and personal achievement.",
                true,
                R.drawable.cover_b105,
                "AUDIOBOOK",
                4.6,
                520
        ));

        bookList.add(new Book(
                "B106",
                "C++ Fundamentals",
                "Bjarne Stroustrup",
                "Programming",
                "An authoritative guide to fundamental C++ language concepts, object-oriented design, memory management, templates, and modern system programming techniques.",
                true,
                R.drawable.cover_b106,
                "PDF",
                4.7,
                198
        ));

        bookList.add(new Book(
                "B107",
                "The Hobbit",
                "J.R.R. Tolkien",
                "Fiction",
                "An unforgettable fantasy adventure following Bilbo Baggins as he embarks on an epic quest to reclaim the lost Dwarf Kingdom of Erebor from the dragon Smaug.",
                true,
                R.drawable.cover_b107,
                "HARDCOVER",
                4.9,
                1400
        ));

        bookList.add(new Book(
                "B108",
                "Cosmos",
                "Carl Sagan",
                "Science",
                "An extraordinary journey through space and time exploring science, civilization, astronomy, and the universe. Based on Carl Sagan's legendary television series.",
                true,
                R.drawable.cover_b108,
                "EPUB",
                5.0,
                780
        ));

        bookList.add(new Book(
                "B109",
                "Atomic Habits",
                "James Clear",
                "Business",
                "A powerful, practical framework for improving every day. Discover how small changes lead to remarkable results using behavioral science and habit loops.",
                true,
                R.drawable.cover_b109,
                "HARDCOVER",
                4.9,
                2100
        ));

        bookList.add(new Book(
                "B110",
                "Clean Code",
                "Robert C. Martin",
                "Programming",
                "A handbook of agile software craftsmanship. Learn best practices for writing readable, maintainable, efficient code, refactoring, and software architecture.",
                true,
                R.drawable.cover_b110,
                "PDF",
                4.8,
                910
        ));

        Log.d(TAG, "Sample books initialized successfully. Count: " + bookList.size());
    }

    /**
     * Retrieves all books in the repository.
     */
    public synchronized ArrayList<Book> getAllBooks() {
        return new ArrayList<>(bookList);
    }

    /**
     * Finds a book by its unique book ID.
     */
    public synchronized Book getBookById(String bookId) {
        if (bookId == null) return null;
        for (Book book : bookList) {
            if (book.getBookId().equalsIgnoreCase(bookId)) {
                return book;
            }
        }
        return null;
    }

    /**
     * Retrieves all currently borrowed books.
     */
    public synchronized ArrayList<Book> getBorrowedBooks() {
        ArrayList<Book> borrowedList = new ArrayList<>();
        for (Book book : bookList) {
            if (!book.isAvailable()) {
                borrowedList.add(book);
            }
        }
        return borrowedList;
    }

    /**
     * Returns the count of currently borrowed books.
     */
    public synchronized int getBorrowedCount() {
        int count = 0;
        for (Book book : bookList) {
            if (!book.isAvailable()) {
                count++;
            }
        }
        return count;
    }

    /**
     * Returns total books count.
     */
    public synchronized int getTotalBooksCount() {
        return bookList.size();
    }

    /**
     * Checks if maximum borrow limit reached.
     */
    public synchronized boolean isBorrowLimitReached() {
        return getBorrowedCount() >= MAX_BORROW_LIMIT;
    }

    /**
     * Attempts to borrow a book by its book ID.
     * Enforces availability check and 3-book borrowing limit.
     *
     * @return true if successfully borrowed, false otherwise.
     */
    public synchronized boolean borrowBook(String bookId) {
        Book book = getBookById(bookId);
        if (book == null) {
            Log.e(TAG, "Cannot borrow: Book with ID " + bookId + " not found.");
            return false;
        }
        if (!book.isAvailable()) {
            Log.w(TAG, "Cannot borrow: Book " + book.getTitle() + " is already borrowed.");
            return false;
        }
        if (getBorrowedCount() >= MAX_BORROW_LIMIT) {
            Log.w(TAG, "Cannot borrow: Maximum borrow limit of " + MAX_BORROW_LIMIT + " reached.");
            return false;
        }
        book.setAvailable(false);
        Log.d(TAG, "Book borrowed successfully: " + book.getTitle() + " (" + bookId + "). Total borrowed: " + getBorrowedCount());
        return true;
    }

    /**
     * Attempts to return a book by its book ID.
     *
     * @return true if successfully returned, false otherwise.
     */
    public synchronized boolean returnBook(String bookId) {
        Book book = getBookById(bookId);
        if (book == null) {
            Log.e(TAG, "Cannot return: Book with ID " + bookId + " not found.");
            return false;
        }
        if (book.isAvailable()) {
            Log.w(TAG, "Cannot return: Book " + book.getTitle() + " is already available.");
            return false;
        }
        book.setAvailable(true);
        Log.d(TAG, "Book returned successfully: " + book.getTitle() + " (" + bookId + "). Total borrowed: " + getBorrowedCount());
        return true;
    }

    /**
     * Filters books by search query and category filter.
     */
    public synchronized ArrayList<Book> filterBooks(String query, String category) {
        ArrayList<Book> filtered = new ArrayList<>();
        String normalizedQuery = (query != null) ? query.trim().toLowerCase() : "";
        String normalizedCat = (category != null) ? category.trim() : "All";

        for (Book book : bookList) {
            boolean matchesCategory = normalizedCat.equalsIgnoreCase("All")
                    || normalizedCat.equalsIgnoreCase("All Categories")
                    || book.getCategory().equalsIgnoreCase(normalizedCat);

            boolean matchesQuery = normalizedQuery.isEmpty()
                    || book.getTitle().toLowerCase().contains(normalizedQuery)
                    || book.getAuthor().toLowerCase().contains(normalizedQuery);

            if (matchesCategory && matchesQuery) {
                filtered.add(book);
            }
        }
        return filtered;
    }
}
