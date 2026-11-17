package com.example.booknest;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;

/**
 * Activity for displaying and managing currently borrowed books in BookNest.
 * Includes lifecycle logging and state management across screen rotations.
 */
public class BorrowedBooksActivity extends AppCompatActivity implements BorrowedBookAdapter.OnBorrowedBookClickListener {
    private static final String LIFECYCLE_TAG = "BookNestLifecycle";

    private BookRepository bookRepository;
    private BorrowedBookAdapter borrowedBookAdapter;

    // UI Widgets
    private ImageView btnBack;
    private TextView tvQuotaFraction;
    private ProgressBar progressBarQuota;
    private TextView tvQuotaPercentage;
    private TextView tvQuotaAvailableSlots;
    private TextView tvActiveLoansBadge;

    private RecyclerView rvBorrowedBooksList;
    private LinearLayout layoutEmptyState;
    private MaterialButton btnExploreLibrary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LIFECYCLE_TAG, "BorrowedBooksActivity: onCreate");
        setContentView(R.layout.activity_borrowed_books);

        // Access shared BookRepository
        bookRepository = BookRepository.getInstance();

        // Initialize Views
        btnBack = findViewById(R.id.btnBack);
        tvQuotaFraction = findViewById(R.id.tvQuotaFraction);
        progressBarQuota = findViewById(R.id.progressBarQuota);
        tvQuotaPercentage = findViewById(R.id.tvQuotaPercentage);
        tvQuotaAvailableSlots = findViewById(R.id.tvQuotaAvailableSlots);
        tvActiveLoansBadge = findViewById(R.id.tvActiveLoansBadge);

        rvBorrowedBooksList = findViewById(R.id.rvBorrowedBooksList);
        layoutEmptyState = findViewById(R.id.layoutEmptyState);
        btnExploreLibrary = findViewById(R.id.btnExploreLibrary);

        // Setup Edge-to-Edge System Bar Insets & Notch Protection
        View rootLayout = findViewById(R.id.rootLayout);
        View headerBar = findViewById(R.id.headerBar);
        View bottomNav = findViewById(R.id.bottomNavigationContainer);
        EdgeToEdgeUtils.applyEdgeToEdge(this, rootLayout, headerBar, bottomNav);

        // Setup RecyclerView
        rvBorrowedBooksList.setLayoutManager(new LinearLayoutManager(this));
        borrowedBookAdapter = new BorrowedBookAdapter(new ArrayList<Book>(), this);
        rvBorrowedBooksList.setAdapter(borrowedBookAdapter);

        // Back Navigation
        if (btnBack != null) {
            btnBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }

        // Explore Library Button in Empty State
        if (btnExploreLibrary != null) {
            btnExploreLibrary.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(BorrowedBooksActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                    finish();
                }
            });
        }

        // Setup Bottom Navigation
        setupBottomNavigation();
    }

    private void refreshBorrowedBooksUI() {
        ArrayList<Book> borrowedBooks = bookRepository.getBorrowedBooks();
        borrowedBookAdapter.updateBooks(borrowedBooks);

        int count = bookRepository.getBorrowedCount();
        int maxLimit = BookRepository.MAX_BORROW_LIMIT;

        // Update Quota Card UI
        if (tvQuotaFraction != null) {
            tvQuotaFraction.setText(count + " / " + maxLimit);
        }
        if (progressBarQuota != null) {
            progressBarQuota.setProgress(count);
        }
        if (tvQuotaPercentage != null) {
            int percentage = (int) (((double) count / maxLimit) * 100);
            tvQuotaPercentage.setText(percentage + "% allowance used");
        }
        if (tvQuotaAvailableSlots != null) {
            int availableSlots = maxLimit - count;
            tvQuotaAvailableSlots.setText(availableSlots + (availableSlots == 1 ? " slot available" : " slots available"));
        }
        if (tvActiveLoansBadge != null) {
            tvActiveLoansBadge.setText(count + " Active");
        }

        // Toggle Empty State vs RecyclerView
        if (count == 0) {
            if (rvBorrowedBooksList != null) rvBorrowedBooksList.setVisibility(View.GONE);
            if (layoutEmptyState != null) layoutEmptyState.setVisibility(View.VISIBLE);
        } else {
            if (rvBorrowedBooksList != null) rvBorrowedBooksList.setVisibility(View.VISIBLE);
            if (layoutEmptyState != null) layoutEmptyState.setVisibility(View.GONE);
        }
    }

    private void setupBottomNavigation() {
        View navHome = findViewById(R.id.navHome);
        if (navHome != null) {
            navHome.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(BorrowedBooksActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                    finish();
                }
            });
        }

        View navAbout = findViewById(R.id.navAbout);
        if (navAbout != null) {
            navAbout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(BorrowedBooksActivity.this, AboutActivity.class);
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    public void onReturnClick(final Book book) {
        if (book == null) return;
        Log.d(LIFECYCLE_TAG, "BorrowedBooksActivity: Return requested for " + book.getTitle());

        // Confirmation AlertDialog
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
                        boolean success = bookRepository.returnBook(book.getBookId());
                        if (success) {
                            refreshBorrowedBooksUI();
                            Toast.makeText(BorrowedBooksActivity.this,
                                    book.getTitle() + " has been returned successfully!",
                                    Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(BorrowedBooksActivity.this,
                                    "Failed to return " + book.getTitle(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .show();
    }

    @Override
    public void onBookClick(Book book) {
        if (book == null) return;
        Log.d(LIFECYCLE_TAG, "BorrowedBooksActivity: Opening details for " + book.getTitle());

        Intent intent = new Intent(BorrowedBooksActivity.this, BookDetailsActivity.class);
        intent.putExtra(BookDetailsActivity.EXTRA_BOOK_ID, book.getBookId());
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(LIFECYCLE_TAG, "BorrowedBooksActivity: onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(LIFECYCLE_TAG, "BorrowedBooksActivity: onResume");
        refreshBorrowedBooksUI();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(LIFECYCLE_TAG, "BorrowedBooksActivity: onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(LIFECYCLE_TAG, "BorrowedBooksActivity: onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(LIFECYCLE_TAG, "BorrowedBooksActivity: onDestroy");
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(LIFECYCLE_TAG, "BorrowedBooksActivity: onSaveInstanceState");
    }
}
