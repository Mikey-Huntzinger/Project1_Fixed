package project1;

import java.util.HashMap;
import java.util.Map;

/**
 * @author aweso
 * @version project1
 * @name Project1
 * @since 10/7/2025
 **/
public class Shelf {
    public static int SHELF_NUMBER_;
    public static int SUBJECT_;
    private HashMap<Book,Integer> books;
    private int shelfNumber;
    private String subject;

    public Shelf(){

    }

    public Shelf(int shelfNumber, String subject){
        this.shelfNumber = shelfNumber;
        this.subject = subject;
    }

    public Code addBook(Book book){
        if(books.containsKey(book)){ // Checks if the book exists in the hashmap then increments the count. return SUCCESS
            books.put(book, books.get(book) + 1);
            return Code.SUCCESS;
        }
        else if(!books.containsKey(book) && book.getSubject().equals(this.subject)){ //if not in books and subject
            books.put(book,1);
            return Code.SUCCESS;
        }
        else{ //Not on shelf and subject mismatch
            return Code.SHELF_SUBJECT_MISMATCH_ERROR;
        }
    }

    @Override
    public boolean equals(Object object){
        return false;
    }

    public int hashCode(){
        hashCode();
    }

    public int getBookCount(Book book){
        if(!books.containsKey(book)){ return -1};
//        Returns count of number of books on shelf.
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

        sb.append(shelfNumber);

        for(Map.Entry<Book,Integer> entry: Map.entrySet()){
            Book book = entry.getKey();
            Integer count = entry.getValue();

            sb.append(book.toString()).append(" ").append(count).append("\n");
        }
    }

    public Code removeBook(Book book){
        if(!books.containsKey(book)){
            System.out.println(book.getTitle() " is not on shelf "+subject);
            return Code.BOOK_NOT_IN_INVENTORY_ERROR;
        }
        else if(books.containsKey(book) && books.get(book) ==0){
            System.out.println("No copies of "+book.getTitle() +"remain on shelf "+subject);
            return Code.BOOK_NOT_IN_INVENTORY_ERROR;
        }
        else if(books.containsKey(book) && books.get(book) > 0){
            System.out.println(book.getTitle() + " successfully removed from shelf "+subject);
        }
        else {
            return Code.SHELF_SUBJECT_MISMATCH_ERROR;
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
