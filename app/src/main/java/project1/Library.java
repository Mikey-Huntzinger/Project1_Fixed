package project1;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import Utilities.Code;

public class Library {
    
    public static final int LENDING_LIMIT = 5;
    private String name;
    private static int libraryCard;
    private List<Reader> readers;
    private HashMap<String,Shelf> shelves;
    private HashMap<Book,Integer> books;

    /**
     * Converts a String to an int, handling NumberFormatException and printing error messages as specified.
     * @param recordCountString The string to convert
     * @param code The Code object for error context
     * @return The integer value, or the error code if conversion fails
     */
    public static int convertInt(String recordCountString, Code code) {
        try {
            return Integer.parseInt(recordCountString);
        } catch (NumberFormatException e) {
            System.out.println("Value which caused the error: " + recordCountString);
            System.out.println("Error message: " + code.getMessage());
            switch (code) {
                case BOOK_COUNT_ERROR:
                    System.out.println("Error: Could not read number of books");
                    break;
                case PAGE_COUNT_ERROR:
                    System.out.println("Error: could not parse page count");
                    break;
                case DATE_CONVERSION_ERROR:
                    System.out.println("Error: Could not parse date component");
                    break;
                default:
                    System.out.println("Error: Unknown conversion error");
            }
            return code.getCode();
        }
    }

    /**
     * Converts a date String to a LocalDate object. Returns 1970-01-01 for errors or "0000".
     * @param date The date string in yyyy-MM-dd format
     * @param errorCode The Code object for error context
     * @return The LocalDate object, or 1970-01-01 if conversion fails
     */
    public static LocalDate convertDate(String date, Code errorCode) {
        if (date.equals("0000")) {
            return LocalDate.of(1970, 1, 1);
        }
        String[] parts = date.split("-");
        if (parts.length != 3) {
            System.out.println("ERROR: date conversion error, could not parse " + date);
            System.out.println("Using default date (01-jan-1970)");
            return LocalDate.of(1970, 1, 1);
        }
        int year, month, day;
        try {
            year = Integer.parseInt(parts[0]);
            month = Integer.parseInt(parts[1]);
            day = Integer.parseInt(parts[2]);
        } catch (NumberFormatException e) {
            System.out.println("ERROR: date conversion error, could not parse " + date);
            System.out.println("Using default date (01-jan-1970)");
            return LocalDate.of(1970, 1, 1);
        }
        if (year < 0) {
            System.out.println("Error converting date: Year " + year);
            System.out.println("Using default date (01-jan-1970)");
            return LocalDate.of(1970, 1, 1);
        }
        if (month < 0) {
            System.out.println("Error converting date: Month " + month);
            System.out.println("Using default date (01-jan-1970)");
            return LocalDate.of(1970, 1, 1);
        }
        if (day < 0) {
            System.out.println("Error converting date: Day " + day);
            System.out.println("Using default date (01-jan-1970)");
            return LocalDate.of(1970, 1, 1);
        }
        return LocalDate.of(year, month, day);
    }

    /**
     * Returns a new library card number one higher than the stored value.
     * @return the next library card integer
     */

     public static int getLibraryCardNumber(){
        return libraryCard+1;
     }

    /**
     * Constructs a Library with the given name.
     * @param name The name of the library
     */
    public Library(String name){
        this.name = name;
        this.readers = new ArrayList<>();
        this.shelves = new HashMap<>();
        this.books = new HashMap<>();
    }

    /**
     * Initializes the library from a CSV file.
     * @param filename The name of the CSV file
     * @return Code indicating success or error type
     */
    public Code init(String filename){
        File file = new File(filename);
        Scanner scanner;
        try {
            scanner = new Scanner(file);
        } catch (Exception FileNotFoundException) {
            return Code.FILE_NOT_FOUND_ERROR;
        }

        //Now that file is initialized and found, we can read from it
        //We can do books now
        String bookCountString =scanner.nextLine();
        int bookCount = convertInt(bookCountString, Code.BOOK_COUNT_ERROR);
        if(bookCount < 0) {
            return errorCode(bookCount);
        }
        Code bookResults = initBooks(bookCount, scanner);
        if(bookResults != Code.SUCCESS){
            scanner.close();
            return bookResults;
        }
        listBooks();

        //Now onto shelves
        int shelfCount = convertInt(scanner.nextLine(), Code.SHELF_COUNT_ERROR);
        if(shelfCount < 0){
            return errorCode(shelfCount);
        }
        Code shelfResults = initShelves(shelfCount, scanner);
        if(shelfResults != Code.SUCCESS){
            return shelfResults;
        }
        listShelves();

        //Now into readers
        int readerCount = convertInt(scanner.nextLine(), Code.READER_COUNT_ERROR);
        if(readerCount < 0){
            return errorCode(readerCount);
        }
        Code readerResults = initReader(readerCount,scanner);
        if(readerResults != Code.SUCCESS){
            return readerResults;
        }
        listReaders();

        scanner.close();
        return Code.SUCCESS;
    }

    /**
     * Returns the number of shelves in the library.
     * @return Number of shelves
     */
    public int listShelves(){
        return listShelves(false);
    }

    /**
     * Lists shelves and optionally their books.
     * @param showBooks If true, lists books on each shelf
     * @return Number of shelves
     */
    public int listShelves(boolean showBooks){
        if(showBooks){
            for(Shelf shelf : shelves.values()){
                // Lists the books of every shelf
                shelf.listBooks();
            }
        }
        else{
            for(Shelf shelf : shelves.values()){
                // Displays the toString of every Shelf
                System.out.println(shelf.toString());
            }
        }
        //Return number of shelves in library
        return shelves.size();
    }

    /**
     * Lists all books in the library and returns the total count.
     * @return Total number of books
     */
    public int listBooks(){
        int bookCount = 0;
        for(Book book : books.keySet()){
            System.out.println(books.get(book) + " copies of " +book.getTitle()+" by " + book.getAuthor() + " ISBN:"+book.getISBN());
            bookCount += books.get(book);
        }
        //Return number of unique books in library
        return bookCount;
    }

    /**
     * Lists all readers in the library.
     * @return Number of readers
     */
    public int listReaders(){
        // This method calls the overloaded listReaders method with 'false'
        // to print readers without their checked-out books.
        return listReaders(false);
    }

    /**
     * Lists readers and optionally their checked-out books.
     * @param showBooks If true, lists books for each reader
     * @return Number of readers
     */
    public int listReaders(boolean showBooks){
        // Loop through each reader in the list of readers.
        for (Reader reader : readers){
            // Check if we should display the books for the reader.
            if(showBooks){
                // If true, print the reader's name and card number, followed by a header for the book list on a new line.
                System.out.println(reader.getName() + "(#" + reader.getCardNumber() + ") has the following books:");
                // Print the list of books returned by the reader's getBooks() method.
                System.out.println(reader.getBooks());
            } else {
                // If false, print only the reader's name and card number on a single line.
                System.out.println(reader.getName() + " (#" + reader.getCardNumber() + ")");
            }
        }
        // Return the total number of readers in the library.
        return readers.size();
    }

    // Init Books,Shelves,Reader

    //This method takes an integer, the number of books to parse, and a Scanner.
    //The scanner represents the current position of the CSV file that is being parsed.
    /**
     * Initializes books from the CSV file.
     * @param bookCount Number of books to parse
     * @param scan Scanner for reading file
     * @return Code indicating success or error type
     */
    public Code initBooks(int bookCount, Scanner scan){
        if (bookCount<1) return Code.LIBRARY_ERROR;
        
        //Loop through the number of books specified
        for(int i=0;i < bookCount; i++){
            if(!scan.hasNextLine()){
                return Code.BOOK_RECORD_COUNT_ERROR;
            }
            //Read and split the next line of the scanner.
            String line = scan.nextLine();
            String[] bookData = line.split(",");

            if(bookData.length != Book.DUE_DATE_){
                return Code.BOOK_RECORD_COUNT_ERROR;
            }
            //Initialize Values from bookData
            String isbn = bookData[Book.ISBN_];
            String title = bookData[Book.TITLE_];
            String subject = bookData[Book.SUBJECT_];
            int pageCount = convertInt(bookData[Book.PAGE_COUNT_], Code.PAGE_COUNT_ERROR);
            String author = bookData[Book.AUTHOR_];
            String dueDateStr = bookData[Book.DUE_DATE_];

            if(pageCount <= 0){
                return Code.PAGE_COUNT_ERROR;
            }

            //Convert dueDateStr to LocalDate
            LocalDate dueDate = convertDate(dueDateStr, Code.DATE_CONVERSION_ERROR);
            if (dueDate==null){
                return Code.DATE_CONVERSION_ERROR;
            }

            //Finally, create the new Book.
            Book newBook = new Book(isbn, title, subject, pageCount, author, dueDate);
            addBook(newBook);
        }
        return Code.SUCCESS;

    }

    /**
     * Initializes shelves from the CSV file.
     * @param shelfCount Number of shelves to parse
     * @param scan Scanner for reading file
     * @return Code indicating success or error type
     */
    public Code initShelves(int shelfCount, Scanner scan){
        if(shelfCount<1) return Code.SHELF_COUNT_ERROR;

        for(int i =0;i<shelfCount;i++){
            String line = scan.nextLine();
            String[] shelfData = line.split(",");
            String shelfNumberStr = shelfData[Shelf.SHELF_NUMBER_];
            String subject = shelfData[Shelf.SUBJECT_];

            //Convert str to int
            int shelfNumber = convertInt(shelfNumberStr, Code.SHELF_NUMBER_PARSE_ERROR);

            Shelf newShelf = new Shelf(shelfNumber,subject);

            addShelf(newShelf);
        }
        if(shelves.size() != shelfCount){
            System.out.println("Number of shelves doesn't match expected");
            return Code.SHELF_NUMBER_PARSE_ERROR;
        }
        return Code.SUCCESS;
    }

    /**
     * Initializes readers from the CSV file.
     * @param readerCount Number of readers to parse
     * @param scan Scanner for reading file
     * @return Code indicating success or error type
     */
    public Code initReader(int readerCount,Scanner scan){
        if(readerCount<1) return Code.READER_COUNT_ERROR;
        for(int i =0;i<readerCount;i++){
            if(!scan.hasNextLine()){
                return Code.READER_COUNT_ERROR;
            }
            String line = scan.nextLine();
            String[] readerData = line.split(",");

            String cardNumberStr = readerData[Reader.CARD_NUMBER_];
            String cardName = readerData[Reader.NAME_];
            String phoneStr = readerData[Reader.PHONE_];

            String numBooksStr = readerData[Reader.BOOK_COUNT_];
            
            //Convert to ints
            int cardNumber = convertInt(cardNumberStr, Code.READER_CARD_NUMBER_ERROR);
            int numBooks = convertInt(numBooksStr, Code.READER_COUNT_ERROR);

            //Make Reader
            Reader newReader = new Reader(cardNumber, cardName, phoneStr);
            addReader(newReader);

            //Gets the list of books from the reader, starting at Book.START_DATE_ and iterating through numBooks
            for(int j=0;j<numBooks;j++){

                // Failsafe: Check if we have enough data elements remaining
                if (Reader.BOOK_START_ + j * 2 + 1 >= readerData.length) {
                    return Code.READER_COUNT_ERROR;
                }
                // Get ISBN and start date
                String isbn = readerData[Reader.BOOK_START_ + j * 2];
                String startDate = readerData[Reader.BOOK_START_ + j * 2 + 1];

                //Convert
                LocalDate startLocalDate = convertDate(startDate, Code.DATE_CONVERSION_ERROR);
                Book bookToAdd = getBookByISBN(isbn);

                //In case the book is not found in the library
                if(bookToAdd == null){
                    System.out.println("ERROR");
                    continue;
                }

                //Add book to reader
                checkOutBook(newReader, bookToAdd);
            }
        }
        return Code.SUCCESS;
    }

    /**
     * Adds a book to the library inventory and shelf if possible.
     * @param newBook The book to add
     * @return Code indicating success or error type
     */
    public Code addBook(Book newBook){
        if(books.containsKey(newBook)){
            books.put(newBook, books.get(newBook) + 1);
            System.out.println(books.get(newBook) + " copies of "+newBook.getTitle() +" in the stacks");
        }
        else{
            books.put(newBook,1);
            System.out.println(newBook.getTitle() + " added to the stacks.");
        }

        if(shelves.containsKey(newBook.getSubject())){ 
            shelves.get(newBook.getSubject()).addBook(newBook);
        }
        else{
            System.out.println("No shelf for " + newBook.getSubject() + " books");
            return Code.SHELF_EXISTS_ERROR;
        }
        return Code.SUCCESS;
    }



    /**
     * Adds a new shelf by subject.
     * @param shelfSubject The subject of the shelf
     * @return Code indicating success or error type
     */
    public Code addShelf(String shelfSubject) {
        Shelf newShelf = new Shelf(shelves.size() + 1, shelfSubject);
        return addShelf(newShelf);
    }

    /**
     * Adds a shelf to the library if it does not already exist.
     * @param shelf The shelf to add
     * @return Code indicating success or error type
     */
    public Code addShelf(Shelf shelf) {
        if (shelves.containsKey(shelf.getSubject())) {
            System.out.println("ERROR: Shelf already exists " + shelf);
            return Code.SHELF_EXISTS_ERROR;
        }

        shelves.put(shelf.getSubject(), shelf);

        for (Book book : books.keySet()) {
            // Only gets the books that match the shelf subject
            if (book.getSubject().equals(shelf.getSubject())) {
                // We need to add the correct number of copies to the shelf
                int count = books.get(book);
                for (int i = 0; i < count; i++) {
                    //Adds the book
                    shelf.addBook(book);
                }
            }
        }
        return Code.SUCCESS;
    }

    public Shelf getShelf(Integer shelfNumber){
        for(Shelf shelf : shelves.values()){
            if(shelf.getShelfNumber() == shelfNumber){
                
                return shelf;
            }
        }
        System.out.println("No shelf number "+shelfNumber+" found");
        return null;
    }

    public Shelf getShelf(String subject){
        for(String subjects : shelves.keySet()){
            if(subject.equals(subjects)){
                return shelves.get(subjects);
            }
        }
        System.out.println("No shelf for "+subject+" books");
        return null;
    }

    /**
     * Adds a reader to the library.
     * @param reader The reader to add
     * @return Code indicating success or error type
     */
    public Code addReader(Reader reader){
        if(readers.contains(reader)) { //if readers already has the reader
            System.out.println(reader.getName()+" already has an account!");
            return Code.READER_ALREADY_EXISTS_ERROR;
        }
        else{ //Checks if the card number matches the new reader
            for(Reader r : readers){
                if(r.getCardNumber() == reader.getCardNumber()){ 
                    System.out.println(r.getName() + " and " + reader.getName() + " have the same card number!");
                    return Code.READER_CARD_NUMBER_ERROR;
                }
            }
        }
        //Adds the reader to the list
        readers.add(reader);
        if(readers.contains(reader)){
            System.out.println(reader.getName()+" added to the library!");
        }
        //If the reader object's library card value is larger than the current field libraryCard in the Library object, set the libraryCard field to that value.
        if(reader.getCardNumber() >= libraryCard){
            libraryCard = reader.getCardNumber() + 1;
        }
        return Code.SUCCESS;
    }

    /**
     * Removes a reader from the library if they have no books checked out.
     * @param reader The reader to remove
     * @return Code indicating success or error type
     */
    public Code removeReader(Reader reader){
        if(!readers.contains(reader)){
            System.out.println(reader.getName()+"\n is not a part of this library");
            return Code.READER_NOT_IN_LIBRARY_ERROR;
        }
        if(readers.contains(reader) && reader.getBookCount()>0){
            System.out.println(reader.getName()+" must return all books!");
            return Code.READER_STILL_HAS_BOOKS_ERROR;
        }

        readers.remove(reader);
        return Code.SUCCESS;
    }

    /**
     * Returns the Reader object associated with a card number.
     * @param cardNumber
     * @return The corresponding reader object, or null if not found
     */
    public Reader getReaderByCard(int cardNumber){
        for(Reader reader : readers){
            if(reader.getCardNumber() == cardNumber){
                return reader;
            }
        }
        return null;
    }

    /**
     * Returns the Book object associated with an ISBN.
     * @param isbn
     * @return The corresponding book, or null if not found
     */
    public Book getBookByISBN(String isbn){
        for(Book book : books.keySet()){
            if(book.getIsbn().equals(isbn)){
                return book;
            }
        }
        System.out.println("ERROR: could not find book with isbn: "+isbn);
        return null;
    }

    /**
     * Checks out a book to a reader following specific conditions.
     * @param reader
     * @param book
     * @return Code indicating success or error type
     */
    public Code checkOutBook(Reader reader, Book book){
        if(!readers.contains(reader)){ //If no reader
            System.out.println(reader.getName()+" doesn't have an account here");
            return Code.READER_NOT_IN_LIBRARY_ERROR;
        }
        if(reader.getBookCount() >= LENDING_LIMIT){ //If reader has too many books
            System.out.println(reader.getName()+" has reached the lending limit, ("+LENDING_LIMIT+")");
            return Code.BOOK_LIMIT_REACHED_ERROR;
        }
        if(!books.containsKey(book)){ //If books doesn't contain the book
            System.out.println("ERROR: could not find "+book);
            return Code.BOOK_NOT_IN_INVENTORY_ERROR;
        } 
        if(!shelves.containsKey(book.getSubject())){ //If no shelf for the book's subject
            System.out.println("no shelf for "+book.getSubject()+" books!");
            return Code.SHELF_EXISTS_ERROR;
        }
        if(shelves.get(book.getSubject()).getBookCount(book) < 1){ //If shelf has less than 1 copy
            System.out.println("ERROR: no copies of "+book+" remain");
            return Code.BOOK_NOT_IN_INVENTORY_ERROR;
        }

        Code addBookResult = reader.addBook(book);
        if(addBookResult != Code.SUCCESS){
            System.out.println("Couldn't checkout "+book);
            return addBookResult;
        }
        
        // If we get here, the book was successfully added to the reader
        shelves.get(book.getSubject()).removeBook(book);
        System.out.println(book+" checked out successfully");
        return Code.SUCCESS;
    }

    /**
     * Returns a book from a reader to the library.
     * @param reader The reader returning the book
     * @param book The book being returned
     * @return Code indicating success or error type
     */
    public Code returnBook(Reader reader, Book book){
        if(!reader.hasBook(book)){ //If reader doesnt have book
            System.out.println(reader.getName()+" doesn't have "+book.getTitle()+" checked out");
            return Code.READER_DOESNT_HAVE_BOOK_ERROR;
        }
        if(!books.containsKey(book)){ //If book is not in books
            return Code.BOOK_NOT_IN_INVENTORY_ERROR;
        }
        
        //If reader has the book
        System.out.println(reader.getName()+" is returning "+book);

        Code removeBookCode = reader.removeBook(book);
        if(removeBookCode == Code.SUCCESS){
            return returnBook(book);
        }
        else{
            System.out.println("Could not return "+book);
            return removeBookCode;
        }
    }

    /**
     * Returns a book to the appropriate shelf in the library.
     * @param book The book to return
     * @return Code indicating success or error type
     */
    public Code returnBook(Book book){
        if (!shelves.containsKey(book.getSubject())) {
            System.out.println("No shelf for " + book);
            return Code.SHELF_EXISTS_ERROR;
        }
        shelves.get(book.getSubject()).addBook(book);
        return Code.SUCCESS;
    }

    /**
     * Returns the Code object associated with a code number.
     * @param codeNumber The code number
     * @return The corresponding Code object, or Code.UNKNOWN_ERROR if not found
     */
    private Code errorCode(int codeNumber) {
        for (Code code : Code.values()) {
        if (code.getCode() == codeNumber) {
            return code;
        }
        }
        return Code.UNKNOWN_ERROR;
  }


}
