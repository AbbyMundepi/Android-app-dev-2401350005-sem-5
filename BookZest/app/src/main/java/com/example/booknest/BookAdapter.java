package com.example.booknest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * RecyclerView Adapter for displaying books in the BookNest library.
 */
public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {

    public interface OnBookClickListener {
        void onBookClick(Book book);
    }

    private final ArrayList<Book> bookList;
    private final OnBookClickListener listener;

    public BookAdapter(List<Book> books, OnBookClickListener listener) {
        this.bookList = new ArrayList<>(books != null ? books : new ArrayList<>());
        this.listener = listener;
    }

    public void updateBooks(List<Book> newBooks) {
        this.bookList.clear();
        if (newBooks != null) {
            this.bookList.addAll(newBooks);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_book, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = bookList.get(position);
        holder.bind(book, listener);
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public static class BookViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imgBookCover;
        private final TextView tvBookCategory;
        private final TextView tvAvailabilityBadge;
        private final TextView tvBookTitle;
        private final TextView tvBookAuthor;
        private final TextView tvBookRating;
        private final MaterialButton btnViewDetails;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            imgBookCover = itemView.findViewById(R.id.imgBookCover);
            tvBookCategory = itemView.findViewById(R.id.tvBookCategory);
            tvAvailabilityBadge = itemView.findViewById(R.id.tvAvailabilityBadge);
            tvBookTitle = itemView.findViewById(R.id.tvBookTitle);
            tvBookAuthor = itemView.findViewById(R.id.tvBookAuthor);
            tvBookRating = itemView.findViewById(R.id.tvBookRating);
            btnViewDetails = itemView.findViewById(R.id.btnViewDetails);
        }

        public void bind(final Book book, final OnBookClickListener listener) {
            Context context = itemView.getContext();

            // Bind cover image with fallback handling
            if (book.getCoverResId() != 0) {
                imgBookCover.setImageResource(book.getCoverResId());
            } else {
                imgBookCover.setImageResource(R.drawable.ic_booknest_logo);
            }

            // Category & Title & Author
            tvBookCategory.setText(book.getCategory().toUpperCase(Locale.getDefault()));
            tvBookTitle.setText(book.getTitle());
            tvBookAuthor.setText(book.getAuthor());

            // Rating
            String ratingText = String.format(Locale.getDefault(), "%.1f (%d)", book.getRating(), book.getReviewCount());
            tvBookRating.setText(ratingText);

            // Availability Badge
            if (book.isAvailable()) {
                tvAvailabilityBadge.setText("Available");
                tvAvailabilityBadge.setTextColor(ContextCompat.getColor(context, R.color.status_available_text));
                tvAvailabilityBadge.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_badge_available));
            } else {
                tvAvailabilityBadge.setText("Borrowed");
                tvAvailabilityBadge.setTextColor(ContextCompat.getColor(context, R.color.status_borrowed_text));
                tvAvailabilityBadge.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_badge_borrowed));
            }

            // Click Handlers
            View.OnClickListener clickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onBookClick(book);
                    }
                }
            };

            itemView.setOnClickListener(clickListener);
            btnViewDetails.setOnClickListener(clickListener);
        }
    }
}
