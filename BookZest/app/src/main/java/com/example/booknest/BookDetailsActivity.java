package com.example.booknest;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.Locale;

/**
 * Activity for displaying complete details and managing borrowing/returning for a selected book.
 * Fully supports lifecycle management and state restoration on configuration changes.
 */
public class BookDetailsActivity extends AppCompatActivity {
    public static final String EXTRA_BOOK_ID = "book_id";
    private static final String LIFECYCLE_TAG = "BookNestLifecycle";
    private static final String KEY_BOOK_ID = "KEY_BOOK_ID";

    private BookRepository bookRepository;
    private String currentBookId;

    // UI Widgets
    private ImageView btnBack;
    private ImageView imgBookCoverDetails;
    private TextView tvDetailsTitle;
    private TextView tvDetailsAuthor;
    private TextView tvDetailsCategoryBadge;
    private TextView tvDetailsAvailabilityBadge;
    private TextView tvDetailsRatingBadge;

    private TextView tvDetailsBookId;
    private TextView tvDetailsFormat;
    private TextView tvDetailsLanguage;
    private TextView tvDetailsReviewsCount;

    private TextView tvDetailsDescription;

    private TextView tvShelfStatusHeader;
    private TextView tvShelfStatusSubtext;
    private ImageView imgShelfStatusIcon;

    private MaterialButton btnBorrowBook;
    private MaterialButton btnReturnBook;

    private LinearLayout layoutDetailsError;
    private MaterialButton btnErrorBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LIFECYCLE_TAG, "BookDetailsActivity: onCreate");
        setContentView(R.layout.activity_book_details);

        // Access shared BookRepository
        bookRepository = BookRepository.getInstance();

        // Restore or Extract Intent Extras
        if (savedInstanceState != null) {
            currentBookId = savedInstanceState.getString(KEY_BOOK_ID);
        }

        if (currentBookId == null) {
            Intent intent = getIntent();
            if (intent != null) {
                currentBookId = intent.getStringExtra(EXTRA_BOOK_ID);
                if (currentBookId == null) {
                    currentBookId = intent.getStringExtra("BOOK_ID");
                }
            }
        }

        Log.d(LIFECYCLE_TAG, "BookDetailsActivity: Opened for Book ID: " + currentBookId);

        // Initialize UI Views
        btnBack = findViewById(R.id.btnBack);
        imgBookCoverDetails = findViewById(R.id.imgBookCoverDetails);
        tvDetailsTitle = findViewById(R.id.tvDetailsTitle);
        tvDetailsAuthor = findViewById(R.id.tvDetailsAuthor);
        tvDetailsCategoryBadge = findViewById(R.id.tvDetailsCategoryBadge);
        tvDetailsAvailabilityBadge = findViewById(R.id.tvDetailsAvailabilityBadge);
        tvDetailsRatingBadge = findViewById(R.id.tvDetailsRatingBadge);

        tvDetailsBookId = findViewById(R.id.tvDetailsBookId);
        tvDetailsFormat = findViewById(R.id.tvDetailsFormat);
        tvDetailsLanguage = findViewById(R.id.tvDetailsLanguage);
        tvDetailsReviewsCount = findViewById(R.id.tvDetailsReviewsCount);

        tvDetailsDescription = findViewById(R.id.tvDetailsDescription);

        tvShelfStatusHeader = findViewById(R.id.tvShelfStatusHeader);
        tvShelfStatusSubtext = findViewById(R.id.tvShelfStatusSubtext);
        imgShelfStatusIcon = findViewById(R.id.imgShelfStatusIcon);

        btnBorrowBook = findViewById(R.id.btnBorrowBook);
        btnReturnBook = findViewById(R.id.btnReturnBook);

        layoutDetailsError = findViewById(R.id.layoutDetailsError);
        btnErrorBack = findViewById(R.id.btnErrorBack);

        // Setup Edge-to-Edge System Bar Insets & Notch Protection
        View rootLayout = findViewById(R.id.rootLayout);
        View headerBar = findViewById(R.id.headerBar);
        EdgeToEdgeUtils.applyEdgeToEdge(this, rootLayout, headerBar, null);

        // Back Navigation Listeners
        if (btnBack != null) {
            btnBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }

        if (btnErrorBack != null) {
            btnErrorBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }

        // Attach Button Listeners
        setupActionListeners();
    }

    private void setupActionListeners() {
        if (btnBorrowBook != null) {
            btnBorrowBook.setEnabled(true);
            btnBorrowBook.setClickable(true);
            btnBorrowBook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(LIFECYCLE_TAG, "BookDetailsActivity: btnBorrowBook clicked!");
                    handleBorrowClick();
                }
            });
        }

        if (btnReturnBook != null) {
            btnReturnBook.setEnabled(true);
            btnReturnBook.setClickable(true);
            btnReturnBook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(LIFECYCLE_TAG, "BookDetailsActivity: btnReturnBook clicked!");
                    handleReturnClick();
                }
            });
        }
    }

    private void populateBookDetails() {
        if (currentBookId == null || currentBookId.trim().isEmpty()) {
            showErrorState("Invalid book ID received.");
            return;
        }

        Book book = bookRepository.getBookById(currentBookId);
        if (book == null) {
            showErrorState("Book with ID '" + currentBookId + "' was not found.");
            return;
        }

        // Hide error state
        if (layoutDetailsError != null) {
            layoutDetailsError.setVisibility(View.GONE);
        }

        // Bind Cover Image
        if (imgBookCoverDetails != null) {
            if (book.getCoverResId() != 0) {
                imgBookCoverDetails.setImageResource(book.getCoverResId());
            } else {
                imgBookCoverDetails.setImageResource(R.drawable.ic_booknest_logo);
            }
        }

        // Bind Text Fields
        if (tvDetailsTitle != null) tvDetailsTitle.setText(book.getTitle());
        if (tvDetailsAuthor != null) tvDetailsAuthor.setText(book.getAuthor());
        if (tvDetailsCategoryBadge != null) tvDetailsCategoryBadge.setText(book.getCategory());
        if (tvDetailsBookId != null) tvDetailsBookId.setText(book.getBookId());
        if (tvDetailsFormat != null) tvDetailsFormat.setText(book.getFormat());
        if (tvDetailsLanguage != null) tvDetailsLanguage.setText("English");
        if (tvDetailsReviewsCount != null) tvDetailsReviewsCount.setText(String.valueOf(book.getReviewCount()));

        if (tvDetailsRatingBadge != null) {
            tvDetailsRatingBadge.setText(String.format(Locale.getDefault(), "★ %.1f (%d reviews)", book.getRating(), book.getReviewCount()));
        }

        if (tvDetailsDescription != null) {
            tvDetailsDescription.setText(book.getDescription());
        }

        // Bind Availability & Action Buttons
        if (book.isAvailable()) {
            if (tvDetailsAvailabilityBadge != null) {
                tvDetailsAvailabilityBadge.setText("Available");
                tvDetailsAvailabilityBadge.setTextColor(ContextCompat.getColor(this, R.color.status_available_text));
                tvDetailsAvailabilityBadge.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_badge_available));
            }
            if (tvShelfStatusHeader != null) tvShelfStatusHeader.setText("In Stock & Ready");
            if (tvShelfStatusSubtext != null) tvShelfStatusSubtext.setText("Available for immediate 14-day campus loan");
            if (imgShelfStatusIcon != null) {
                imgShelfStatusIcon.setColorFilter(ContextCompat.getColor(this, R.color.status_available_text));
            }

            if (btnBorrowBook != null) btnBorrowBook.setVisibility(View.VISIBLE);
            if (btnReturnBook != null) btnReturnBook.setVisibility(View.GONE);
        } else {
            if (tvDetailsAvailabilityBadge != null) {
                tvDetailsAvailabilityBadge.setText("Borrowed");
                tvDetailsAvailabilityBadge.setTextColor(ContextCompat.getColor(this, R.color.status_borrowed_text));
                tvDetailsAvailabilityBadge.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_badge_borrowed));
            }
            if (tvShelfStatusHeader != null) tvShelfStatusHeader.setText("Currently Borrowed");
            if (tvShelfStatusSubtext != null) tvShelfStatusSubtext.setText("Active loan schedule registered in patron record");
            if (imgShelfStatusIcon != null) {
                imgShelfStatusIcon.setColorFilter(ContextCompat.getColor(this, R.color.status_borrowed_text));
            }

            if (btnBorrowBook != null) btnBorrowBook.setVisibility(View.GONE);
            if (btnReturnBook != null) btnReturnBook.setVisibility(View.VISIBLE);
        }

        // Ensure action listeners remain active
        setupActionListeners();
    }

    private void handleBorrowClick() {
        if (currentBookId == null || currentBookId.trim().isEmpty()) {
            Toast.makeText(this, "Error: Invalid Book ID", Toast.LENGTH_SHORT).show();
            return;
        }

        Book book = bookRepository.getBookById(currentBookId);

        if (book == null) {
            showAlertDialog("Book Not Found", "The requested book could not be found.");
            return;
        }

        if (!book.isAvailable()) {
            showAlertDialog("Book Unavailable", "This book is currently borrowed.");
            return;
        }

        // Check 3-book borrowing limit
        if (bookRepository.getBorrowedCount() >= BookRepository.MAX_BORROW_LIMIT) {
            new MaterialAlertDialogBuilder(this)
                    .setTitle("Borrowing Limit Reached")
                    .setMessage("You can borrow a maximum of 3 books at a time. Please return a book before borrowing another.")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();
            return;
        }

        // Show Borrow Confirmation Dialog
        new MaterialAlertDialogBuilder(this)
                .setTitle("Confirm Borrowing")
                .setMessage("Do you want to borrow " + book.getTitle() + "?")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("Confirm Borrow", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        performBorrowBook(book);
                    }
                })
                .show();
    }

    private void performBorrowBook(Book book) {
        if (book == null) return;
        boolean success = bookRepository.borrowBook(book.getBookId());
        if (success) {
            populateBookDetails();
            Toast.makeText(this, book.getTitle() + " has been borrowed successfully!", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Failed to borrow " + book.getTitle(), Toast.LENGTH_SHORT).show();
        }
    }

    private void handleReturnClick() {
        if (currentBookId == null || currentBookId.trim().isEmpty()) return;
        Book book = bookRepository.getBookById(currentBookId);

        if (book == null) {
            showAlertDialog("Book Not Found", "The requested book could not be found.");
            return;
        }

        if (book.isAvailable()) {
            showAlertDialog("Book Available", "This book is not currently borrowed.");
            return;
        }

        // Show Return Confirmation Dialog
        new MaterialAlertDialogBuilder(this)
                .setTitle("Return Book")
                .setMessage("Are you sure you want to return " + book.getTitle() + "?")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("Confirm Return", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        performReturnBook(book);
                    }
                })
                .show();
    }

    private void performReturnBook(Book book) {
        if (book == null) return;
        boolean success = bookRepository.returnBook(book.getBookId());
        if (success) {
            populateBookDetails();
            Toast.makeText(this, book.getTitle() + " has been returned successfully!", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Failed to return " + book.getTitle(), Toast.LENGTH_SHORT).show();
        }
    }

    private void showAlertDialog(String title, String message) {
        new MaterialAlertDialogBuilder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void showErrorState(String message) {
        Log.e(LIFECYCLE_TAG, "BookDetailsActivity: " + message);
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        if (layoutDetailsError != null) {
            layoutDetailsError.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(LIFECYCLE_TAG, "BookDetailsActivity: onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(LIFECYCLE_TAG, "BookDetailsActivity: onResume");
        populateBookDetails();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(LIFECYCLE_TAG, "BookDetailsActivity: onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(LIFECYCLE_TAG, "BookDetailsActivity: onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(LIFECYCLE_TAG, "BookDetailsActivity: onDestroy");
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(LIFECYCLE_TAG, "BookDetailsActivity: onSaveInstanceState");
        outState.putString(KEY_BOOK_ID, currentBookId);
    }
}
