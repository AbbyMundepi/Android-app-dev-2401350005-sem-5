# BookNest – Data Architecture & State Management Explanation

**Course**: Android Application Development (University Practical Assignment)  
**Project**: BookNest – Digital Library & Book Borrowing System  
**Architecture**: Process-Wide In-Memory Application Repository  

---

## 1. Overview of Architecture

BookNest implements an **Application-Level Shared In-Memory Repository Architecture**. In accordance with university assignment constraints prohibiting SQLite, Room, or external backend APIs, all application state resides in memory during the execution process.

To prevent data duplication and inconsistent state across screens, the application uses the **Singleton Pattern** anchored to the custom `BookNestApplication` subclass.

---

## 2. Core Components

### 2.1 The Data Model (`Book.java`)
The `Book` class encapsulates book information:
- `bookId` (String): Unique identifier (e.g., `"B101"`, `"B102"`).
- `title` (String), `author` (String), `category` (String).
- `description` (String): Comprehensive overview text.
- `isAvailable` (boolean): Flag indicating whether the book is available (`true`) or borrowed (`false`).
- `coverResId` (int): Local drawable resource ID for cover art.
- Additional metadata (`format`, `rating`, `reviewCount`).

### 2.2 Shared Repository (`BookRepository.java`)
`BookRepository` manages a single `ArrayList<Book>` instance containing the 10 cataloged sample books (`B101`–`B110`).
- **Thread-Safe Singleton Pattern**: Instantiated via `BookRepository.getInstance()` using double-checked locking.
- **Single Source of Truth**: All Activities read from and mutate this identical `ArrayList<Book>`.
- **Initialization**: Sample books are initialized exactly once upon process startup inside `BookNestApplication.onCreate()`.

### 2.3 Application Subclass (`BookNestApplication.java`)
Extends `android.app.Application` and is registered in `AndroidManifest.xml` (`android:name=".BookNestApplication"`).
`BookNestApplication` initializes `BookRepository` when the application process launches. Because the Application class lives as long as the application process, the repository instance remains active across Activity navigations and configuration changes.

---

## 3. Data Passing & Cross-Activity Synchronization

### 3.1 Explicit Intent Navigation
When a user selects a book from `MainActivity` or `BorrowedBooksActivity`:
1. The app creates an explicit `Intent` targetting `BookDetailsActivity`.
2. The unique `bookId` String is attached as an Intent Extra (`intent.putExtra(BookDetailsActivity.EXTRA_BOOK_ID, book.getBookId())`).
3. **RecyclerView positions are never passed or relied upon**; navigation relies exclusively on the immutable `bookId`.

### 3.2 Dynamic Detail Resolution
Upon launching `BookDetailsActivity`:
1. The Activity extracts `bookId` from the Intent extras.
2. It queries `bookRepository.getBookById(bookId)`.
3. The returned `Book` object's properties populate the view fields dynamically.

### 3.3 Borrowing & Returning State Synchronization
- **Borrowing**: `bookRepository.borrowBook(bookId)` validates availability and the 3-book limit, then mutates the `Book` object's `isAvailable` field to `false`.
- **Returning**: `bookRepository.returnBook(bookId)` sets `isAvailable = true`.
- **UI Refresh (`onResume()`)**: `MainActivity`, `BookDetailsActivity`, and `BorrowedBooksActivity` query `BookRepository` in their `onResume()` callbacks:
  - `MainActivity`: Updates statistics cards (*Currently Borrowed* count) and badge colors.
  - `BookDetailsActivity`: Toggles between the *Borrow Book* and *Return Book* buttons.
  - `BorrowedBooksActivity`: Updates the active loans list, progress bar, and empty state.

---

## 4. Screen Rotation & Lifecycle Preservation

When a device rotates:
1. Android recreates the active `Activity`.
2. The process-wide `BookRepository` remains intact in JVM heap memory.
3. Temporary UI state (such as the typed search string and active category chip in `MainActivity`) is preserved via `onSaveInstanceState()` and restored in `onCreate()`.
4. In `onResume()`, the Activity queries `BookRepository`, ensuring the latest borrowing states and filtered results render accurately without data loss or duplication.
