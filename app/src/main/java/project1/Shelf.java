package Utilities.Code;

import java.util.HashMap;
import java.util.Map;

/**
 * @author aweso
 * @version project1
 * @name Project1
 * @since 10/7/2025
 **/
public class Shelf {
    public static final int SHELF_NUMBER_ = 0;
    public static int SUBJECT_ = 1;
    private HashMap<Book,Integer> books;
    private int shelfNumber;
    private String subject;

    public Shelf(){

    }

    public Shelf(int shelfNumber, String subject){
        this.shelfNumber = shelfNumber;
        this.subject = subject;
        this.books = new HashMap<>();
    }

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

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Shelf shelf = (Shelf) o;
        return shelfNumber == shelf.shelfNumber && subject.equals(shelf.subject);
    }

    public int hashCode(){
        int result = Integer.hashCode(shelfNumber);
        result = 31 * result + (subject != null ? subject.hashCode() : 0);
        return result;
    }

    public int getBookCount(Book book){
        if(!books.containsKey(book)){ return -1;}
//        Returns count of number of books on shelf
        return books.get(book);
    }

    public HashMap<Book, Integer> getBooks(){
        return books;
    }

    public int getShelfNumber(){
        return shelfNumber;
    }

    public String getSubject(){
        return subject;
    }

    public String listBooks(){
        StringBuilder sb = new StringBuilder();

        sb.append(books.size()).append(" books on shelf: ").append(this.toString()).append("\n");

        for (Book book : books.keySet()) {
            // For each book, get its corresponding count from the map.
            Integer count = books.get(book);

            // Append the information just like before.
            sb.append(book.toString()).append(" ").append(count).append("\n");
        }
        return sb.toString();
    }

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

    public void setBooks(HashMap<Book,Integer> books){
        this.books = books;
    }

    public void setShelfNumber(int shelfNumber){
        this.shelfNumber = shelfNumber;
    }

    public void setSubject(String subject){
        this.subject = subject;
    }

    public String toString(){
        return shelfNumber + " : " + subject;
    }



}
