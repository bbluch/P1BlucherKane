import java.io.IOException;
import student.TestCase;

/**
 * @author CS3114/5040 Staff
 * @version Summer 2025
 */
// test
public class MovieRaterTest extends TestCase {

    private MovieRaterDB it;

    /**
     * Sets up the tests that follow. In general, used for initialization
     */
    public void setUp() {
        it = new MovieRaterDB();
    }


    /**
     * Test clearing on initial
     * 
     * @throws IOException
     */
    public void testClearInit() throws IOException {
        assertTrue(it.clear());
    }


    /**
     * Test empty print movie or reviewer
     * 
     * @throws IOException
     */
    public void testRefMissing() throws IOException {
        assertNull(it.listMovie(2));
        assertNull(it.listReviewer(3));
        assertFalse(it.deleteScore(5, 1));
        assertFalse(it.deleteReviewer(2));
        assertFalse(it.deleteMovie(2));
    }


    /**
     * Test insert two items and print
     * 
     * @throws IOException
     */
    public void testRefinsertTwo() throws IOException {
        assertTrue(it.addReview(2, 3, 7));
        assertTrue(it.addReview(2, 5, 5));
        assertFuzzyEquals(it.printRatings(), "2: (3, 7) (5, 5)");
        assertFuzzyEquals(it.listMovie(3), "3: 7");
        assertFuzzyEquals(it.listReviewer(2), "2: 7 5");
    }


    /**
     * Test bad review values
     * 
     * @throws IOException
     */
    public void testRefBadRatings() throws IOException {
        assertFalse(it.addReview(2, 3, -1));
        assertFalse(it.addReview(2, 4, 0));
        assertFalse(it.addReview(2, 5, 20));
        assertFuzzyEquals(it.printRatings(), "");
    }


    /**
     * Test bad reviewer value/movie values
     * 
     * @throws IOException
     */
    public void testBadReviewerMovieRatings() throws IOException {
        assertFalse(it.addReview(0, 3, 3));
        assertFalse(it.addReview(-1, 3, 4));
        assertFalse(it.addReview(1, 0, 3));
        assertFalse(it.addReview(2, -1, 7));

    }


    /**
     * Test insert 5 items and print
     * 
     * @throws IOException
     */
    public void testRefinsertFive() throws IOException {
        assertTrue(it.addReview(7, 3, 10));
        assertTrue(it.addReview(2, 3, 7));
        assertTrue(it.addReview(3, 5, 8));
        assertTrue(it.addReview(5, 7, 9));
        assertTrue(it.addReview(7, 7, 1));
        assertFuzzyEquals(multiline("2: (3, 7)", "3: (5, 8)", "5: (7, 9)",
            "7: (3, 10) (7, 1)"), it.printRatings());
    }


    /**
     * Tests clear with values inserted
     * 
     * @throws IOException
     */
    public void testClear() throws IOException {
        assertTrue(it.addReview(7, 3, 10));
        assertTrue(it.addReview(2, 3, 7));
        assertTrue(it.addReview(3, 5, 8));
        assertTrue(it.addReview(5, 7, 9));
        assertTrue(it.addReview(7, 7, 1));
        assertTrue(it.clear());
        assertFuzzyEquals(it.printRatings(), "");

    }


    /**
     * Tests the implementation of inserting at end of list
     * 
     * @throws IOException
     */
    public void testInsertAtEndOfList() throws IOException {
        assertTrue(it.addReview(1, 1, 5));
        assertTrue(it.addReview(1, 10, 8));
        assertFuzzyEquals(it.listReviewer(1), "1: 5 8");
    }


    /**
     * Tests the implementation of inserting at middle of list
     * 
     * @throws IOException
     */
    public void testInsertAtMiddleOfList() throws IOException {
        assertTrue(it.addReview(2, 1, 5));
        assertTrue(it.addReview(2, 10, 8));
        assertTrue(it.addReview(2, 5, 7));
        assertFuzzyEquals(it.listReviewer(2), "2: 5 7 8");
    }


    /**
     * Tests the implementation of inserting at beginning of list
     * 
     * @throws IOException
     */
    public void testInsertAtBeginningOfList() throws IOException {
        assertTrue(it.addReview(3, 10, 3));
        assertTrue(it.addReview(3, 1, 7));
        assertFuzzyEquals(it.listReviewer(3), "3: 7 3");
    }


    /**
     * Tests the implementation of inserting then updating
     * 
     * @throws IOException
     */
    public void testInsertUpdate() throws IOException {
        assertTrue(it.addReview(2, 1, 5));
        assertFuzzyEquals(it.listReviewer(2), "2: 5");
        assertTrue(it.addReview(2, 1, 8));
        assertFuzzyEquals(it.listReviewer(2), "2: 8");
    }


    /**
     * Tests correct behavior for when a list does not exist
     * 
     * @throws IOException
     */
    public void testNonExistentList() throws IOException {
        assertNull(it.listReviewer(7));
        assertNull(it.listMovie(6));
    }


    /**
     * Tests printing an empty matrix
     * 
     * @throws IOException
     */
    public void testPrintEmpty() throws IOException {
        assertEquals("", it.printRatings());
    }


    /**
     * Tests inserting
     * 
     * @throws IOException
     */
    public void testInsertOneItem() throws IOException {
        assertTrue(it.addReview(2, 1, 5));
        assertFuzzyEquals(it.listReviewer(2), "2: 5");
    }
}
