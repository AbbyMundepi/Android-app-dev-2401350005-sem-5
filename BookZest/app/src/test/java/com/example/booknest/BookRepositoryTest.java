package com.example.booknest;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests validating BookRepository functionality, search, filtering,
 * borrowing, returning, and 3-book limit enforcement.
 */
public class BookRepositoryTest {

    private BookRepository repository;

    @Before
    public void setUp() {
        repository = BookRepository.getInstance();
        // Reset book availability for clean test execution
        for (Book book : repository.getAllBooks()) {
            book.setAvailable(true);
        }
    }

    @Test
    public void testTC01_AllTenBooksLoaded() {
        ArrayList<Book> books = repository.getAllBooks();
        assertEquals(10, books.size());
    }

    @Test
    public void testTC02_SearchCaseInsensitive() {
        ArrayList<Book> resultLower = repository.filterBooks("java", "All");
        ArrayList<Book> resultUpper = repository.filterBooks("JAVA", "All");

        assertEquals(1, resultLower.size());
        assertEquals("Learn Java", resultLower.get(0).getTitle());

        assertEquals(1, resultUpper.size());
        assertEquals("Learn Java", resultUpper.get(0).getTitle());
    }

    @Test
    public void testTC03_CategoryFilteringProgramming() {
        ArrayList<Book> programmingBooks = repository.filterBooks("", "Programming");
        assertEquals(4, programmingBooks.size());

        ArrayList<Book> fictionBooks = repository.filterBooks("", "Fiction");
        assertEquals(2, fictionBooks.size());
    }

    @Test
    public void testTC04_CombinedSearchAndCategoryFiltering() {
        // "java" + Programming = Learn Java
        ArrayList<Book> match = repository.filterBooks("java", "Programming");
        assertEquals(1, match.size());
        assertEquals("B101", match.get(0).getBookId());

        // "java" + Fiction = 0 books
        ArrayList<Book> noMatch = repository.filterBooks("java", "Fiction");
        assertEquals(0, noMatch.size());
    }

    @Test
    public void testTC05_SearchNoResults() {
        ArrayList<Book> results = repository.filterBooks("xyz123", "All");
        assertEquals(0, results.size());
    }

    @Test
    public void testTC06_GetBookById() {
        Book b101 = repository.getBookById("B101");
        assertNotNull(b101);
        assertEquals("Learn Java", b101.getTitle());
        assertEquals("James Gosling", b101.getAuthor());
        assertEquals("Programming", b101.getCategory());

        Book invalid = repository.getBookById("INVALID_ID");
        assertNull(invalid);
    }

    @Test
    public void testTC07_BorrowBookSuccess() {
        assertTrue(repository.borrowBook("B101"));
        Book b101 = repository.getBookById("B101");
        assertFalse(b101.isAvailable());
        assertEquals(1, repository.getBorrowedCount());
    }

    @Test
    public void testTC09_DuplicateBorrowingPrevented() {
        assertTrue(repository.borrowBook("B101"));
        // Attempting to borrow B101 again must return false
        assertFalse(repository.borrowBook("B101"));
        assertEquals(1, repository.getBorrowedCount());
    }

    @Test
    public void testTC10_GetBorrowedBooks() {
        repository.borrowBook("B101");
        ArrayList<Book> borrowed = repository.getBorrowedBooks();
        assertEquals(1, borrowed.size());
        assertEquals("B101", borrowed.get(0).getBookId());
    }

    @Test
    public void testTC11_ReturnBookSuccess() {
        repository.borrowBook("B101");
        assertTrue(repository.returnBook("B101"));

        Book b101 = repository.getBookById("B101");
        assertTrue(b101.isAvailable());
        assertEquals(0, repository.getBorrowedCount());
    }

    @Test
    public void testTC13_DuplicateReturnPrevented() {
        // Book B101 is initially available; returning an available book must return false
        assertFalse(repository.returnBook("B101"));
    }

    @Test
    public void testThreeBookLimitEnforced() {
        assertTrue(repository.borrowBook("B101"));
        assertTrue(repository.borrowBook("B102"));
        assertTrue(repository.borrowBook("B103"));
        assertEquals(3, repository.getBorrowedCount());

        // Attempting 4th borrow must fail
        assertFalse(repository.borrowBook("B104"));
        assertEquals(3, repository.getBorrowedCount());
    }
}
