# BookNest – Digital Library & Book Borrowing System

**BookNest** is a native Android application designed as an individual university practical assessment (100 Marks). It offers students a digital library management system to discover, search, filter, borrow, and track academic and leisure reading materials.

---

## 📱 Features

- **Library Home Catalog**: Browse the 10 cataloged books with cover art, author details, categories, rating scores, and availability badges.
- **Live Search & Category Filtering**: Perform case-insensitive search by title or author simultaneously with category filters (*All*, *Programming*, *Fiction*, *Science*, *Business*).
- **Book Details View**: View detailed metadata including edition, format, review counts, and complete book descriptions.
- **Borrowing Management**: One-tap borrowing with a strict 3-book limit validation and Material 3 confirmation dialogs.
- **Book Return System**: Return borrowed books through *My Borrowed Books* or *Book Details* with instant availability restoration.
- **Process-Wide In-Memory Repository**: Application-level singleton pattern ensuring data consistency across all Activities and screen orientation changes.
- **About & Info Screen**: Overview of application capabilities, tech stack, and academic accreditation.
- **Options Menu**: Quick navigation to *My Borrowed Books*, *About*, and graceful exit.

---

## 🛠️ Technology Stack

- **Language**: Java
- **UI Framework**: Android XML Layouts & Material Design 3
- **IDE**: Android Studio
- **Architecture**: Shared Process-Wide In-Memory Repository (`BookRepository`)
- **UI Components**: `RecyclerView`, `MaterialCardView`, `MaterialButton`, `NestedScrollView`, `MaterialAlertDialogBuilder`
- **Navigation**: Explicit `Intent`s with extra data passing (`EXTRA_BOOK_ID`)

*Strictly No Kotlin, Jetpack Compose, Firebase, SQLite, Room, or External APIs.*

---

## 🏗️ Project Architecture & Structure

```
com.example.booknest/
│
├── BookNestApplication.java   # Custom Application subclass initializing BookRepository
├── BookRepository.java        # Shared in-memory data store with thread-safe singleton
├── Book.java                  # Model class representing book records
├── BookAdapter.java           # RecyclerView adapter for Library Home catalog
├── BorrowedBookAdapter.java   # RecyclerView adapter for My Borrowed Books screen
│
├── MainActivity.java          # Library Home activity (search, filtering, catalog)
├── BookDetailsActivity.java   # Detailed book view & borrow/return actions
├── BorrowedBooksActivity.java # User's active loans & borrowing quota screen
└── AboutActivity.java         # App information & capabilities
```

---

## 🚀 Setup & Execution Instructions

1. **Prerequisites**:
   - Android Studio (2022.3 or newer)
   - Android SDK (API 24 or higher)
   - Java Development Kit (JDK 11 or higher)

2. **Opening in Android Studio**:
   - Launch Android Studio $\rightarrow$ Select **Open an Existing Project**.
   - Navigate to the project directory and click **OK**.
   - Wait for Gradle sync to complete automatically.

3. **Running the Application**:
   - Connect an Android physical device via USB debugging or start an Android Virtual Device (AVD Emulator).
   - Select the `app` run configuration in the top bar.
   - Click the green **Run** button ($\text{Shift} + \text{F10}$).

4. **Running Unit Tests**:
   - Execute `./gradlew app:testDebugUnitTest` or run `BookRepositoryTest.java` inside Android Studio.

---

## 🎓 Academic Accreditation
Developed as a native Android university assignment showcasing Java application architecture, Material Design 3, explicit Intent navigation, and lifecycle management.
