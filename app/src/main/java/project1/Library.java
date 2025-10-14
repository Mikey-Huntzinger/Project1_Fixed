package project1;

import java.io.File;
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
        for(HashMap<Book,Integer> book : books.entrySet()){
            System.out.println(book.get + " copies of " +book.toString());
            bookCount += book.getValue();
        }
        //Return number of unique books in library
        return books.size();
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
