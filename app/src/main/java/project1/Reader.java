package project1;
import java.util.List;

public class Reader {
    public static int CARD_NUMBER_;
    public static int NAME_;
    public static int PHONE_;
    public static int BOOK_COUNT_;
    public static int BOOK_START_;

    private int cardNumber;
    private String name;
    private String phone;
    private List<Book> books;

    public Reader(int cardNumber, String name, String phone) {
        this.cardNumber = cardNumber;
        this.name = name;
        this.phone = phone;
    }

    
}
