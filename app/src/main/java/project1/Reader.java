package project1;
import Utilities.Code;
import java.util.List;
// import project1.Code; // Removed because Code class does not exist or cannot be resolved

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
        this.books = new java.util.ArrayList<>();
    }

    public Code addBook(Book book){
        if(this.books.contains(book)) {
            return Code.BOOK_ALREADY_CHECKED_OUT_ERROR;
        }
        books.add(book);
        return Code.SUCCESS;
    }

    public Code removeBook(Book book){
        if (!this.books.contains(book)) {
            return Code.READER_DOESNT_HAVE_BOOK_ERROR;
        }
        boolean removed = books.remove(book);
        if (removed) {
            return Code.SUCCESS;
        } else {
            return Code.READER_COULD_NOT_REMOVE_BOOK_ERROR;
        }
    }

    public boolean hasBook(Book Book){
        return books.contains(Book);
    }

    public int getBookCount(){
        return books.size();
    }

    public List<Book> getBooks(){
        return books;
    }

    public void setBooks(List<Book> books){
        this.books = books;
    }

    public int getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(int cardNumber){
        this.cardNumber = cardNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone){
        this.phone = phone;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Reader reader = (Reader) obj;
        return cardNumber == reader.cardNumber &&
               java.util.Objects.equals(name, reader.name) &&
               java.util.Objects.equals(phone, reader.phone);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(cardNumber, name, phone);
    }

    @Override
    public String toString(){
        return getName() + " (#" + getCardNumber() + ") has checked out " + getBooks();
    }
}
