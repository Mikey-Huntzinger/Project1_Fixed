package project1;

import java.time.LocalDate;
import java.util.Objects;

public class Book {
    private String author;
    private LocalDate dueDate;
    private String isbn;
    private int pageCount;
    private String subject;
    private String title;

    public Book(String author, LocalDate dueDate, String isbn, int pageCount, String subject, String title) {
        this.author = author;
        this.dueDate = dueDate;
        this.isbn = isbn;
        this.pageCount = pageCount;
        this.subject = subject;
        this.title = title;
    }

    @Override
    public String toString() {
        return title + " by " + author + ", ISBN: " + isbn;
    }

    // Equals method excluding the date
    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Book book = (Book) object;
        return pageCount == book.pageCount &&
               Objects.equals(author, book.author) &&
               Objects.equals(isbn, book.isbn) &&
               Objects.equals(subject, book.subject) &&
               Objects.equals(title, book.title);
    }
    //HashCode method excluding the date
    @Override
    public int hashCode() {
        return Objects.hash(author, isbn, pageCount, subject, title);
    }

    // Getters and Setters
    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

