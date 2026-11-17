package com.example.booknest;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Adapter for displaying currently borrowed books in BorrowedBooksActivity.
 */
public class BorrowedBookAdapter extends RecyclerView.Adapter<BorrowedBookAdapter.BorrowedBookViewHolder> {

    public interface OnBorrowedBookClickListener {
        void onReturnClick(Book book);
        void onBookClick(Book book);
    }

    private final ArrayList<Book> borrowedList;
    private final OnBorrowedBookClickListener listener;

    public BorrowedBookAdapter(List<Book> books, OnBorrowedBookClickListener listener) {
        this.borrowedList = new ArrayList<>(books != null ? books : new ArrayList<>());
        this.listener = listener;
    }

    public void updateBooks(List<Book> newBooks) {
        this.borrowedList.clear();
        if (newBooks != null) {
            this.borrowedList.addAll(newBooks);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BorrowedBookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_borrowed_book, parent, false);
        return new BorrowedBookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BorrowedBookViewHolder holder, int position) {
        Book book = borrowedList.get(position);
        holder.bind(book, listener);
    }

    @Override
    public int getItemCount() {
        return borrowedList.size();
    }

    public static class BorrowedBookViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imgBorrowedCover;
        private final TextView tvBorrowedCategory;
        private final TextView tvBorrowedTitle;
        private final TextView tvBorrowedAuthor;
        private final TextView tvBorrowedDueDate;
        private final MaterialButton btnReturnBook;
        private final MaterialButton btnViewDetails;

        public BorrowedBookViewHolder(@NonNull View itemView) {
            super(itemView);
            imgBorrowedCover = itemView.findViewById(R.id.imgBorrowedCover);
            tvBorrowedCategory = itemView.findViewById(R.id.tvBorrowedCategory);
            tvBorrowedTitle = itemView.findViewById(R.id.tvBorrowedTitle);
            tvBorrowedAuthor = itemView.findViewById(R.id.tvBorrowedAuthor);
            tvBorrowedDueDate = itemView.findViewById(R.id.tvBorrowedDueDate);
            btnReturnBook = itemView.findViewById(R.id.btnReturnBook);
            btnViewDetails = itemView.findViewById(R.id.btnViewDetails);
        }

        public void bind(final Book book, final OnBorrowedBookClickListener listener) {
            if (book.getCoverResId() != 0) {
                imgBorrowedCover.setImageResource(book.getCoverResId());
            } else {
                imgBorrowedCover.setImageResource(R.drawable.ic_booknest_logo);
            }

            tvBorrowedCategory.setText(book.getCategory().toUpperCase(Locale.getDefault()));
            tvBorrowedTitle.setText(book.getTitle());
            tvBorrowedAuthor.setText(book.getAuthor());
            tvBorrowedDueDate.setText("Due: 14 days remaining");

            btnReturnBook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onReturnClick(book);
                    }
                }
            });

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
