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
        assertFuzzyEquals("", it.printRatings());
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


    /**
     * Tests deleting a score that does not exist.
     */
    public void testDeleteScoreNonExistent() {
        it.addReview(5, 5, 5);
        assertFalse(it.deleteScore(99, 99)); // Neither exists
        assertFalse(it.deleteScore(5, 99)); // Row exists, col doesn't
        assertFalse(it.deleteScore(99, 5)); // Col exists, row doesn't
    }


    /**
     * Tests deleting a single score that is the only item in its
     * row AND column. This specifically tests the header pointer updates.
     */
    public void testDeleteOnlyScore() {
        it.addReview(1, 1, 1);
        assertTrue(it.deleteScore(1, 1));
        assertNull(it.listReviewer(1)); // listReviewer returns null for no
                                        // ratings
        assertNull(it.listMovie(1));
        assertFuzzyEquals("", it.printRatings());
    }


    /**
     * Tests deleting a score that is in the MIDDLE of a row and column list.
     * This tests the core "prev.next = curr.next" pointer logic.
     */
    public void testDeleteScoreInMiddle() {
        // Setup: (5,5) will be in the middle of its row and col
        it.addReview(5, 2, 1);
        it.addReview(5, 5, 2);
        it.addReview(5, 8, 3);

        it.addReview(2, 5, 4);
        it.addReview(8, 5, 6);

        // Pre-check
        assertFuzzyEquals("5: 1 2 3", it.listReviewer(5));
        assertFuzzyEquals("5: 4 2 6", it.listMovie(5));

        // Action: Delete the middle node
        assertTrue(it.deleteScore(5, 5));

        // Verify: Both lists should be correctly relinked
        assertFuzzyEquals("5: 1 3", it.listReviewer(5)); // Row is relinked
        assertFuzzyEquals("5: 4 6", it.listMovie(5)); // Column is relinked
    }


    /**
     * Tests deleting a score that is the FIRST in its row, but not its col.
     */
    public void testDeleteScoreFirstInRow() {
        it.addReview(5, 2, 1); // Node to delete
        it.addReview(5, 5, 2);
        it.addReview(1, 2, 9); // Node before it in the column

        assertTrue(it.deleteScore(5, 2));
        assertFuzzyEquals("5: 2", it.listReviewer(5)); // Row header updated
        assertFuzzyEquals("2: 9", it.listMovie(2)); // Column relinked
    }


    /**
     * Tests deleting a score that is the FIRST in its column, but not its row.
     */
    public void testDeleteScoreFirstInCol() {
        it.addReview(5, 2, 1);
        it.addReview(5, 5, 2); // Node to delete
        it.addReview(1, 5, 9); // Node before it in the row

        assertTrue(it.deleteScore(5, 5));
        assertFuzzyEquals("5: 1", it.listReviewer(5)); // Row relinked
        assertFuzzyEquals("5: 9", it.listMovie(5)); // Col header updated (no
                                                    // ratings
        // left)
    }


    /**
     * Tests deleting a reviewer that has no ratings (or doesn't exist).
     */
    public void testDeleteNonExistentReviewer() {
        it.addReview(1, 1, 5); // Add some other data
        assertFalse(it.deleteReviewer(99)); // Doesn't exist

        // Reviewer 5 exists (header created) but has no nodes
        it.addReview(5, 1, 5);
        it.deleteScore(5, 1);
        assertFalse(it.deleteReviewer(5)); // Row exists but is empty
    }


    /**
     * Tests deleting a reviewer and verifies that all associated column lists
     * are correctly unlinked. This is the main test for removeRow.
     */
    public void testDeleteReviewerFull() {
        // Add ratings for the reviewer to be deleted (Reviewer 10)
        it.addReview(10, 5, 1); // This node is between 8 and 12 in its col
        it.addReview(10, 10, 2); // This node is first in its col
        it.addReview(10, 15, 3); // This node is last in its col

        // Add nodes *around* the ones we are about to delete
        it.addReview(8, 5, 9); // Before (10, 5)
        it.addReview(12, 5, 9); // After (10, 5)

        it.addReview(11, 10, 9); // After (10, 10)

        it.addReview(1, 15, 9); // Before (10, 15)

        // Verify pre-conditions
        assertFuzzyEquals("10: 1 2 3", it.listReviewer(10));
        assertFuzzyEquals("5: 9 1 9", it.listMovie(5));
        assertFuzzyEquals("10: 2 9", it.listMovie(10));
        assertFuzzyEquals("15: 9 3", it.listMovie(15));

        // Action: Delete the entire reviewer
        assertTrue(it.deleteReviewer(10));

        // Verify all traces are gone
        assertNull(it.listReviewer(10)); // The row is gone

        // Verify column integrity (this kills the mutants)
        assertFuzzyEquals("5: 9 9", it.listMovie(5)); // Middle node unlinked
        assertFuzzyEquals("10: 9", it.listMovie(10)); // Head node unlinked
        assertFuzzyEquals("15: 9", it.listMovie(15)); // Tail node unlinked
    }


    /**
     * Tests deleting a movie that has no ratings (or doesn't exist).
     */
    public void testDeleteNonExistentMovie() {
        it.addReview(1, 1, 5);
        assertFalse(it.deleteMovie(99)); // Doesn't exist

        // Movie 5 exists (header created) but has no nodes
        it.addReview(1, 5, 5);
        it.deleteScore(1, 5);
        assertFalse(it.deleteMovie(5)); // Col exists but is empty
    }


    /**
     * Tests deleting a movie and verifies that all associated row lists
     * are correctly unlinked. This is the main test for removeCol.
     */
    public void testDeleteMovieFull() {
        // Add ratings for the movie to be deleted (Movie 10)
        it.addReview(5, 10, 1); // This node is between 8 and 12 in its row
        it.addReview(10, 10, 2); // This node is first in its row
        it.addReview(15, 10, 3); // This node is last in its row

        // Add nodes *around* the ones we are about to delete
        it.addReview(5, 8, 9); // Before (5, 10)
        it.addReview(5, 12, 9); // After (5, 10)

        it.addReview(10, 11, 9); // After (10, 10)

        it.addReview(15, 1, 9); // Before (15, 10)

        // Verify pre-conditions
        assertFuzzyEquals("10: 1 2 3", it.listMovie(10));
        assertFuzzyEquals("5: 9 1 9", it.listReviewer(5));
        assertFuzzyEquals("10: 2 9", it.listReviewer(10));
        assertFuzzyEquals("15: 9 3", it.listReviewer(15));

        // Action: Delete the entire movie
        assertTrue(it.deleteMovie(10));

        // Verify all traces are gone
        assertNull(it.listMovie(10)); // The col is gone

        // Verify row integrity (this kills the mutants)
        assertFuzzyEquals("5: 9 9", it.listReviewer(5)); // Middle node unlinked
        assertFuzzyEquals("10: 9", it.listReviewer(10)); // Head node unlinked
        assertFuzzyEquals("15: 9", it.listReviewer(15)); // Tail node unlinked
    }

}
