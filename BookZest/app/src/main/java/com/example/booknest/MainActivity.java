package com.example.booknest;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

/**
 * MainActivity displaying the Library Home screen of BookNest
 * with live search, category filtering, real-time statistics,
 * lifecycle management, and state restoration on screen rotation.
 */
public class MainActivity extends AppCompatActivity implements BookAdapter.OnBookClickListener {
    private static final String LIFECYCLE_TAG = "BookNestLifecycle";
    private static final String KEY_SEARCH_QUERY = "KEY_SEARCH_QUERY";
    private static final String KEY_SELECTED_CATEGORY = "KEY_SELECTED_CATEGORY";

    private BookRepository bookRepository;
    private BookAdapter bookAdapter;

    // UI Widgets
    private EditText etSearchInput;
    private ImageView btnClearSearch;

    private TextView chipAll;
    private TextView chipProgramming;
    private TextView chipFiction;
    private TextView chipScience;
    private TextView chipBusiness;

    private TextView tvTotalBooksCount;
    private TextView tvBorrowedBooksCount;
    private TextView tvTitlesCountBadge;
    private RecyclerView rvBooksList;

    private LinearLayout layoutEmptyState;
    private MaterialButton btnClearFilters;

    // Filtering State
    private String selectedCategory = "All";
    private String searchQuery = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LIFECYCLE_TAG, "MainActivity: onCreate");
        setContentView(R.layout.activity_main);

        // Access shared BookRepository instance
        bookRepository = BookRepository.getInstance();

        // Initialize UI Views
        etSearchInput = findViewById(R.id.etSearchInput);
        btnClearSearch = findViewById(R.id.btnClearSearch);

        chipAll = findViewById(R.id.chipAll);
        chipProgramming = findViewById(R.id.chipProgramming);
        chipFiction = findViewById(R.id.chipFiction);
        chipScience = findViewById(R.id.chipScience);
        chipBusiness = findViewById(R.id.chipBusiness);

        tvTotalBooksCount = findViewById(R.id.tvTotalBooksCount);
        tvBorrowedBooksCount = findViewById(R.id.tvBorrowedBooksCount);
        tvTitlesCountBadge = findViewById(R.id.tvTitlesCountBadge);
        rvBooksList = findViewById(R.id.rvBooksList);

        layoutEmptyState = findViewById(R.id.layoutEmptyState);
        btnClearFilters = findViewById(R.id.btnClearFilters);

        // Setup Edge-to-Edge System Bar Insets & Notch Protection
        View rootLayout = findViewById(R.id.rootLayout);
        View mainContentContainer = findViewById(R.id.mainContentContainer);
        View bottomNavContainer = findViewById(R.id.bottomNavigationContainer);
        EdgeToEdgeUtils.applyEdgeToEdge(this, rootLayout, mainContentContainer, bottomNavContainer);

        // Configure RecyclerView
        rvBooksList.setLayoutManager(new LinearLayoutManager(this));
        bookAdapter = new BookAdapter(new ArrayList<Book>(), this);
        rvBooksList.setAdapter(bookAdapter);

        // Setup Live Search Listener
        setupSearchListeners();

        // Setup Category Filter Chips
        setupCategoryChipListeners();

        // Restore saved instance state on configuration change / rotation
        if (savedInstanceState != null) {
            searchQuery = savedInstanceState.getString(KEY_SEARCH_QUERY, "");
            selectedCategory = savedInstanceState.getString(KEY_SELECTED_CATEGORY, "All");
            if (etSearchInput != null && !searchQuery.isEmpty()) {
                etSearchInput.setText(searchQuery);
            }
            updateCategoryChipStyles();
        }

        // Setup Clear Filters Button
        if (btnClearFilters != null) {
            btnClearFilters.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clearAllFilters();
                }
            });
        }

        // Setup Bottom Navigation
        setupBottomNavigation();
    }

    private void setupSearchListeners() {
        if (etSearchInput != null) {
            etSearchInput.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    searchQuery = (s != null) ? s.toString() : "";

                    // Toggle Clear Search Icon
                    if (btnClearSearch != null) {
                        if (searchQuery.trim().isEmpty()) {
                            btnClearSearch.setVisibility(View.GONE);
                        } else {
                            btnClearSearch.setVisibility(View.VISIBLE);
                        }
                    }

                    // Apply combined filtering
                    applyCombinedFilter();
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
        }

        if (btnClearSearch != null) {
            btnClearSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (etSearchInput != null) {
                        etSearchInput.setText("");
                    }
                }
            });
        }
    }

    private void setupCategoryChipListeners() {
        View.OnClickListener categoryClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = v.getId();
                if (id == R.id.chipAll) {
                    selectedCategory = "All";
                } else if (id == R.id.chipProgramming) {
                    selectedCategory = "Programming";
                } else if (id == R.id.chipFiction) {
                    selectedCategory = "Fiction";
                } else if (id == R.id.chipScience) {
                    selectedCategory = "Science";
                } else if (id == R.id.chipBusiness) {
                    selectedCategory = "Business";
                }

                updateCategoryChipStyles();
                applyCombinedFilter();
            }
        };

        if (chipAll != null) chipAll.setOnClickListener(categoryClickListener);
        if (chipProgramming != null) chipProgramming.setOnClickListener(categoryClickListener);
        if (chipFiction != null) chipFiction.setOnClickListener(categoryClickListener);
        if (chipScience != null) chipScience.setOnClickListener(categoryClickListener);
        if (chipBusiness != null) chipBusiness.setOnClickListener(categoryClickListener);
    }

    private void updateCategoryChipStyles() {
        setChipStyle(chipAll, "All".equalsIgnoreCase(selectedCategory));
        setChipStyle(chipProgramming, "Programming".equalsIgnoreCase(selectedCategory));
        setChipStyle(chipFiction, "Fiction".equalsIgnoreCase(selectedCategory));
        setChipStyle(chipScience, "Science".equalsIgnoreCase(selectedCategory));
        setChipStyle(chipBusiness, "Business".equalsIgnoreCase(selectedCategory));
    }

    private void setChipStyle(TextView chip, boolean isSelected) {
        if (chip == null) return;
        if (isSelected) {
            chip.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_chip_selected));
            chip.setTextColor(ContextCompat.getColor(this, R.color.white));
        } else {
            chip.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_chip_unselected));
            chip.setTextColor(ContextCompat.getColor(this, R.color.on_surface_variant));
        }
    }

    /**
     * Applies search query and category filter simultaneously.
     */
    private void applyCombinedFilter() {
        ArrayList<Book> filteredList = bookRepository.filterBooks(searchQuery, selectedCategory);
        bookAdapter.updateBooks(filteredList);

        // Update count badge
        int count = filteredList.size();
        if (tvTitlesCountBadge != null) {
            tvTitlesCountBadge.setText(count + (count == 1 ? " title" : " titles"));
        }

        // Toggle Empty State vs RecyclerView
        if (filteredList.isEmpty()) {
            if (rvBooksList != null) rvBooksList.setVisibility(View.GONE);
            if (layoutEmptyState != null) layoutEmptyState.setVisibility(View.VISIBLE);
        } else {
            if (rvBooksList != null) rvBooksList.setVisibility(View.VISIBLE);
            if (layoutEmptyState != null) layoutEmptyState.setVisibility(View.GONE);
        }
    }

    private void clearAllFilters() {
        selectedCategory = "All";
        searchQuery = "";
        if (etSearchInput != null) {
            etSearchInput.setText("");
        }
        updateCategoryChipStyles();
        applyCombinedFilter();
    }

    private void setupBottomNavigation() {
        View navMyBooks = findViewById(R.id.navMyBooks);
        if (navMyBooks != null) {
            navMyBooks.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, BorrowedBooksActivity.class);
                    startActivity(intent);
                }
            });
        }

        View navAbout = findViewById(R.id.navAbout);
        if (navAbout != null) {
            navAbout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_my_books) {
            Intent intent = new Intent(MainActivity.this, BorrowedBooksActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_about) {
            Intent intent = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_exit) {
            finishAffinity();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(LIFECYCLE_TAG, "MainActivity: onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(LIFECYCLE_TAG, "MainActivity: onResume");

        // Refresh statistics cards
        if (tvTotalBooksCount != null) {
            tvTotalBooksCount.setText(String.valueOf(bookRepository.getTotalBooksCount()));
        }
        if (tvBorrowedBooksCount != null) {
            tvBorrowedBooksCount.setText(String.valueOf(bookRepository.getBorrowedCount()));
        }

        // Re-apply combined filter to render latest repository data
        applyCombinedFilter();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(LIFECYCLE_TAG, "MainActivity: onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(LIFECYCLE_TAG, "MainActivity: onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(LIFECYCLE_TAG, "MainActivity: onDestroy");
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(LIFECYCLE_TAG, "MainActivity: onSaveInstanceState");
        outState.putString(KEY_SEARCH_QUERY, searchQuery);
        outState.putString(KEY_SELECTED_CATEGORY, selectedCategory);
    }

    @Override
    public void onBookClick(Book book) {
        if (book == null) return;
        Log.d(LIFECYCLE_TAG, "MainActivity: onBookClick - " + book.getTitle());

        Intent intent = new Intent(MainActivity.this, BookDetailsActivity.class);
        intent.putExtra(BookDetailsActivity.EXTRA_BOOK_ID, book.getBookId());
        startActivity(intent);
    }
}
