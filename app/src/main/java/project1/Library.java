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

    //Constructor
    public Library(String name){
        this.name = name;
        this.readers = new ArrayList<>();
        this.shelves = new HashMap<>();
        this.books = new HashMap<>();
    }

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

    public int listShelves(){
        return listShelves(false);
    }

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

    public int listBooks(){
        int bookCount = 0;
        for(Book book : books.keySet()){
            System.out.println(books.get(book) + " copies of " +books.toString());
            bookCount += books.get(book);
        }
        //Return number of unique books in library
        return bookCount;
    }

    public int listReaders(){
        // This method calls the overloaded listReaders method with 'false'
        // to print readers without their checked-out books.
        return listReaders(false);
    }

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
                    return Code.READER_RECORD_COUNT_ERROR;
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
                checkoutBook(newReader, bookToAdd);
            }
        }
        return Code.SUCCESS;
    }

    public Code addBook(Book newBook){
        if(books.containsKey(newBook)){
            books.put(newBook, books.get(newBook) + 1);
            System.out.println(books.get(newBook) + " copies of ["+newBook.getTitle() +"] in the stacks");
        }
        else{
            books.put(newBook,1);
            System.out.println("["+newBook.getTitle() + "] added to the stacks.");
        }

        if(shelves.containsKey(newBook.getSubject())){ 
            shelves.get(newBook.getSubject()).addBook(newBook);
        }
        else{
            System.out.println("No shelf for subject [" + newBook.getSubject() + "] books");
            return Code.SHELF_EXISTS_ERROR;
        }
        return Code.SUCCESS;
    }



    public Code addShelf(String shelfSubject) {
        Shelf newShelf = new Shelf(shelves.size() + 1, shelfSubject);
        return addShelf(newShelf);
    }

    public Code addShelf(Shelf shelf) {
        if (shelves.containsKey(shelf.getSubject())) {
            System.out.println("ERROR: Shelf already exists [" + shelf + "]");
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

    public Code addReader(Reader reader){
        if(readers.contains(reader)) { //if readers already has the reader
            System.out.println("["+reader.getName()+"] already has an account!");
            return Code.READER_ALREADY_EXISTS_ERROR;
        }
        else{ //Checks if the card number matches the new reader
            for(Reader r : readers){
                if(r.getCardNumber() == reader.getCardNumber()){ 
                    System.out.println("[" + r.getCardNumber() + "] and [" + reader.getName() + "] have the same card number!");
                    return Code.READER_CARD_NUMBER_ERROR;
                }
            }
        }
        //Adds the reader to the list
        readers.add(reader);
        if(readers.contains(reader)){
            System.out.println("["+reader.getName()+"] added to the library!");
        }
        //If the reader object's library card value is larger than the current field libraryCard in the Library object, set the libraryCard field to that value.
        if(reader.getCardNumber() >= libraryCard){
            libraryCard = reader.getCardNumber() + 1;
        }
        return Code.SUCCESS;
    }

    public Code removeReader(Reader reader){
        if(!readers.contains(reader)){
            System.out.println("["+reader.getName()+"]\n is not a part of this library");
            return Code.READER_NOT_IN_LIBRARY_ERROR;
        }
        if(readers.contains(reader) && reader.getBookCount()>0){
            System.out.println("["+reader.getName()+"] must return all books!");
            return Code.READER_STILL_HAS_BOOKS_ERROR;
        }
        

    private Code errorCode(int codeNumber) {
        for (Code code : Code.values()) {
        if (code.getCode() == codeNumber) {
            return code;
        }
        }
        return Code.UNKNOWN_ERROR;
  }


}
