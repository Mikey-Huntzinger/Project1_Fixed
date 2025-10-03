package project1;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * Test class for the Book object
 */
public class BookTest {

    // A Book instance that will be created before each test runs.
    private Book book;

    // Variables holding the expected initial values for our book.
    private final String expectedAuthor = "George Orwell";
    private final LocalDate expectedDueDate = LocalDate.of(2024, 10, 31);
    private final String expectedIsbn = "978-0451524935";
    private final int expectedPageCount = 328;
    private final String expectedSubject = "Dystopian Fiction";
    private final String expectedTitle = "1984";

    /**
     * This method runs BEFORE EACH @Test method in this class.
     * It sets up a fresh Book instance to ensure tests are isolated.
     */
    @BeforeEach
    void setUp() {
        book = new Book(
                expectedAuthor,
                expectedDueDate,
                expectedIsbn,
                expectedPageCount,
                expectedSubject,
                expectedTitle
        );
    }

    // Constructor and Getter Tests

    @Test
    void testGetAuthor() {
        assertEquals(expectedAuthor, book.getAuthor(), "getAuthor should return the value set by the constructor.");
    }

    @Test
    void testGetDueDate() {
        assertEquals(expectedDueDate, book.getDueDate(), "getDueDate should return the value set by the constructor.");
    }

    @Test
    void testGetIsbn() {
        assertEquals(expectedIsbn, book.getIsbn(), "getIsbn should return the value set by the constructor.");
    }

    @Test
    void testGetPageCount() {
        assertEquals(expectedPageCount, book.getPageCount(), "getPageCount should return the value set by the constructor.");
    }

    @Test
    void testGetSubject() {
        assertEquals(expectedSubject, book.getSubject(), "getSubject should return the value set by the constructor.");
    }

    @Test
    void testGetTitle() {
        assertEquals(expectedTitle, book.getTitle(), "getTitle should return the value set by the constructor.");
    }

    // Setter Tests

    @Test
    void testSetAuthor() {
        String newAuthor = "J.R.R. Tolkien";
        book.setAuthor(newAuthor);
        assertEquals(newAuthor, book.getAuthor(), "setAuthor should update the author.");
    }

    @Test
    void testSetDueDate() {
        LocalDate newDueDate = LocalDate.of(2025, 1, 1);
        book.setDueDate(newDueDate);
        assertEquals(newDueDate, book.getDueDate(), "setDueDate should update the due date.");
    }

    @Test
    void testSetIsbn() {
        String newIsbn = "978-0618640157";
        book.setIsbn(newIsbn);
        assertEquals(newIsbn, book.getIsbn(), "setIsbn should update the ISBN.");
    }

    @Test
    void testSetPageCount() {
        int newPageCount = 1178;
        book.setPageCount(newPageCount);
        assertEquals(newPageCount, book.getPageCount(), "setPageCount should update the page count.");
    }

    @Test
    void testSetSubject() {
        String newSubject = "High Fantasy";
        book.setSubject(newSubject);
        assertEquals(newSubject, book.getSubject(), "setSubject should update the subject.");
    }

    @Test
    void testSetTitle() {
        String newTitle = "The Lord of the Rings";
        book.setTitle(newTitle);
        assertEquals(newTitle, book.getTitle(), "setTitle should update the title.");
    }

    // Equals and HashCode tests
    @Test
    void testBookEquality() {
        // Step 1: Create an instance of a Book with given values.
        Book book1 = new Book(
                "George Orwell",
                LocalDate.of(2024, 10, 31),
                "978-0451524935",
                328,
                "Dystopian Fiction",
                "1984"
        );

        // Step 2: Create an instance of a Book with DIFFERENT values.
        Book bookWithDifferentValues = new Book(
                "J.R.R. Tolkien",
                LocalDate.of(2025, 12, 25),
                "978-0618640157",
                432,
                "Fantasy",
                "The Hobbit"
        );

        // Step 3: Ensure that they are NOT equal.
        assertNotEquals(book1, bookWithDifferentValues, "Books with different data should not be equal.");

        // Step 4: Create a new instance of Book with the same values as one of the previous.
        // This is NOT an alias (book3 = book1), but a distinct object in memory.
        Book bookWithSameValues = new Book(
                "George Orwell",
                LocalDate.of(2024, 10, 31), // Note: The date is different but excluded from equals()
                "978-0451524935",
                328,
                "Dystopian Fiction",
                "1984"
        );

        // Step 5: Assert that they are equal.
        assertEquals(book1, bookWithSameValues, "Two distinct book objects with the same data should be equal.");
    }

}