package project1;
/**
 * @author aweso
 * @version project1
 * @name Project1
 * @since 10/7/2025
 **/
public class Shelf {
    public static int SHELF_NUMBER_;
    public static int SUBJECT_;
    private HashMap<String,Integer> books;
    private int shelfNumber;
    private String subject;

    public Shelf(){

    }

    public Shelf(int shelfNumber, String subject){
        this.shelfNumber = shelfNumber;
        this.subject = subject;
    }

    public void addBook(Book book){
        this.books.add(book);
    }

    public boolean equals(Object object){
        return false;
    }

    public int getBookCount(){
        return books.length();
    }

    public Books getBooks(){
        return books;
    }


}
