package com.example.booknest;

import java.io.Serializable;
import java.util.Objects;

/**
 * Model class representing a Book in the BookNest Library System.
 */
public class Book implements Serializable {
    private String bookId;
    private String title;
    private String author;
    private String category;
    private String description;
    private boolean isAvailable;
    private int coverResId;
    private String format;
    private double rating;
    private int reviewCount;

    public Book() {
    }

    public Book(String bookId, String title, String author, String category, String description, boolean isAvailable) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.category = category;
        this.description = description;
        this.isAvailable = isAvailable;
        this.coverResId = R.drawable.ic_booknest_logo;
        this.format = "EPUB";
        this.rating = 4.8;
        this.reviewCount = 100;
    }

    public Book(String bookId, String title, String author, String category, String description, boolean isAvailable, int coverResId, String format, double rating, int reviewCount) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.category = category;
        this.description = description;
        this.isAvailable = isAvailable;
        this.coverResId = coverResId;
        this.format = format;
        this.rating = rating;
        this.reviewCount = reviewCount;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public int getCoverResId() {
        return coverResId;
    }

    public void setCoverResId(int coverResId) {
        this.coverResId = coverResId;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(bookId, book.bookId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookId);
    }
}
