package project1;

import Utilities.Code;
import java.util.HashMap;

/**
 * The Shelf class represents a bookshelf in a library management system.
 * Each shelf has a unique number, a specific subject category, and can hold multiple books.
 * Books are stored with their quantities in a HashMap, allowing for inventory management.
 * The shelf enforces subject matching - only books with the same subject as the shelf can be added.
 * 
 * @author aweso
 * @version project1
 * @since 10/7/2025
 */
public class Shelf {
    public static final int SHELF_NUMBER_ = 0;
    public static int SUBJECT_ = 1;
    private HashMap<Book,Integer> books;
    private int shelfNumber;
    private String subject;

    /**
     * Default constructor that creates an empty shelf with no number or subject set.
     * Initializes the books HashMap to prevent null pointer exceptions.
     */
    public Shelf(){
        this.books = new HashMap<>();
    }

    /**
     * Constructor that creates a shelf with specified number and subject.
     * @param shelfNumber The unique identifier number for this shelf
     * @param subject The subject category that this shelf will hold (e.g., "Adventure", "Science Fiction")
     */
    public Shelf(int shelfNumber, String subject){
        this.shelfNumber = shelfNumber;
        this.subject = subject;
        this.books = new HashMap<>();
    }

    /**
     * Adds a book to the shelf following these conditions:
     * 1. If the book already exists on the shelf, increment its count
     * 2. If the book does not exist and its subject matches the shelf subject, add it with count 1
     * 3. If the book does not exist and its subject doesn't match, return subject mismatch error
     * 
     * @param book The book to add to the shelf
     * @return Code.SUCCESS if book was added successfully, 
     *         Code.SHELF_SUBJECT_MISMATCH_ERROR if book subject doesn't match shelf subject
     */
    public Code addBook(Book book){
        if(books.containsKey(book)){ // Checks if the book exists in the hashmap then increments the count. return SUCCESS
            books.put(book, books.get(book) + 1);
            System.out.println(book.toString() + " added to shelf " + this.toString());
            return Code.SUCCESS;
        }
        else if(!books.containsKey(book) && book.getSubject().equals(this.subject)){ //if not in books and subject
            books.put(book,1);
            System.out.println(book.toString() + " added to shelf " + this.toString());
            return Code.SUCCESS;
        }
        else{ //Not on shelf and subject mismatch
            return Code.SHELF_SUBJECT_MISMATCH_ERROR;
        }
    }

    /**
     * Compares this shelf to another object for equality.
     * Two shelves are considered equal if they have the same shelf number and subject.
     * @param o The object to compare with this shelf
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Shelf shelf = (Shelf) o;
        return shelfNumber == shelf.shelfNumber && subject.equals(shelf.subject);
    }

    /**
     * Generates a hash code for this shelf based on shelf number and subject.
     * @return The hash code value for this shelf
     */
    public int hashCode(){
        int result = Integer.hashCode(shelfNumber);
        result = 31 * result + (subject != null ? subject.hashCode() : 0);
        return result;
    }

    /**
     * Returns the count of a specific book on this shelf.
     * @param book The book to count
     * @return The number of copies of the book on the shelf, or -1 if the book is not on the shelf
     */
    public int getBookCount(Book book){
        if(!books.containsKey(book)){ return -1;}
//        Returns count of number of books on shelf
        return books.get(book);
    }

    /**
     * Gets the HashMap containing all books and their quantities on this shelf.
     * @return HashMap with Book as key and Integer count as value
     */
    public HashMap<Book, Integer> getBooks(){
        return books;
    }

    /**
     * Gets the shelf number.
     * @return The unique identifier number of this shelf
     */
    public int getShelfNumber(){
        return shelfNumber;
    }

    /**
     * Gets the subject category of this shelf.
     * @return The subject string (e.g., "Adventure", "Science Fiction")
     */
    public String getSubject(){
        return subject;
    }

    /**
     * Creates a formatted string listing all books on the shelf with their quantities.
     * Uses proper singular/plural grammar: "1 book" vs "2 books".
     * @return A formatted string showing the shelf contents
     */
    public String listBooks(){
        StringBuilder sb = new StringBuilder();

        // Use singular "book" for 1, plural "books" for 0 or >1
        String bookLabel = (books.size() == 1) ? " book" : " books";
        sb.append(books.size()).append(bookLabel).append(" on shelf: ").append(this.toString()).append("\n");

        for (Book book : books.keySet()) {
            // For each book, get its corresponding count from the map.
            Integer count = books.get(book);

            // Append the information just like before.
            sb.append(book.toString()).append(" ").append(count).append("\n");
        }
        return sb.toString();
    }

    /**
     * Removes a book from the shelf following these conditions:
     * 1. If the book is not present in the books HashMap, return BOOK_NOT_IN_INVENTORY_ERROR
     * 2. If the book is present but has a count of 0, return BOOK_NOT_IN_INVENTORY_ERROR  
     * 3. If the book is present and count is greater than 0, decrement count and return SUCCESS
     * 
     * @param book The book to remove from the shelf
     * @return Code.SUCCESS if book was removed successfully,
     *         Code.BOOK_NOT_IN_INVENTORY_ERROR if book is not on shelf or count is 0
     */
    public Code removeBook(Book book){
        if(!books.containsKey(book)){
            System.out.println(book.getTitle() +" is not on shelf "+subject);
            return Code.BOOK_NOT_IN_INVENTORY_ERROR;
        }
        else if(books.containsKey(book) && books.get(book) ==0){
            System.out.println("No copies of "+book.getTitle() +"remain on shelf "+subject);
            return Code.BOOK_NOT_IN_INVENTORY_ERROR;
        }
        else {
            System.out.println(book.getTitle() + " successfully removed from shelf "+subject);
            books.put(book, books.get(book) - 1);
            return Code.SUCCESS;
        }


    }

    /**
     * Sets the books HashMap for this shelf.
     * @param books HashMap containing books and their quantities to set for this shelf
     */
    public void setBooks(HashMap<Book,Integer> books){
        this.books = books;
    }

    /**
     * Sets the shelf number.
     * @param shelfNumber The unique identifier number to assign to this shelf
     */
    public void setShelfNumber(int shelfNumber){
        this.shelfNumber = shelfNumber;
    }

    /**
     * Sets the subject category for this shelf.
     * @param subject The subject category string (e.g., "Adventure", "Science Fiction")
     */
    public void setSubject(String subject){
        this.subject = subject;
    }

    /**
     * Returns a string representation of the shelf.
     * @return String in format "shelfNumber : subject"
     */
    public String toString(){
        return shelfNumber + " : " + subject;
    }



}
