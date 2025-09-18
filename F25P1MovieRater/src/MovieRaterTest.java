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


    /**
     * Test similarity for a reviewer that does not exist or has no ratings.
     * Both should return -1.
     */
    public void testSimilarReviewerNonExistent() {
        // Test on an empty database
        assertEquals(-1, it.similarReviewer(1));

        // Add data, but test a reviewer ID that hasn't rated anything
        it.addReview(1, 1, 5);
        assertEquals(-1, it.similarReviewer(2)); // Reviewer 2 has no ratings
    }


    /**
     * Test similarity when other reviewers exist, but none share any movies.
     * Per the spec, the score for each is -1, so no suitable match exists.
     */
    public void testSimilarReviewerNoSharedMovies() {
        it.addReview(1, 1, 5); // Reviewer 1
        it.addReview(1, 2, 5);
        it.addReview(2, 3, 5); // Reviewer 2 (shares no movies with 1)
        it.addReview(3, 4, 5); // Reviewer 3 (shares no movies with 1)

        // No one has a valid similarity score, so it must return -1.
        assertEquals(-1, it.similarReviewer(1));
    }


    /**
     * Test a scenario with one perfect match (score 0.0) and one bad match.
     * This tests the core "find lowest score" logic.
     */
    public void testSimilarReviewerPerfectMatch() {
        // Reviewer 10 (Target)
        it.addReview(10, 1, 5);
        it.addReview(10, 2, 8);

        // Reviewer 20 (Bad Match)
        // Score = (abs(5-1) + abs(8-10)) / 2 = (4 + 2) / 2 = 3.0
        it.addReview(20, 1, 1);
        it.addReview(20, 2, 10);

        // Reviewer 3 (Perfect Match)
        // Score = (abs(5-5) + abs(8-8)) / 2 = (0 + 0) / 2 = 0.0
        it.addReview(3, 1, 5);
        it.addReview(3, 2, 8);

        // Reviewer 3 must be the best match.
        assertEquals(3, it.similarReviewer(10));
    }


    /**
     * Tests that the helper logic correctly skips over non-shared movies.
     */
    public void testSimilarReviewerComplexTraversal() {
        // Reviewer 1 (Target)
        it.addReview(1, 1, 5); // Skips
        it.addReview(1, 5, 10); // Match
        it.addReview(1, 10, 1); // Match
        it.addReview(1, 20, 8); // Skips

        // Reviewer 2 (Competitor)
        it.addReview(2, 2, 5); // Skips
        it.addReview(2, 5, 8); // Match: diff 2
        it.addReview(2, 9, 1); // Skips
        it.addReview(2, 10, 3); // Match: diff 2
        it.addReview(2, 15, 8); // Skips

        // Score = (abs(10-8) + abs(1-3)) / 2 = (2 + 2) / 2 = 2.0
        // Since R2 is the only other reviewer, they must be the match.
        assertEquals(2, it.similarReviewer(1));
    }


    /**
     * Tests the tie-breaker rule: when two reviewers have the same lowest
     * score,
     * the one with the SMALLER index must be returned.
     */
    public void testSimilarReviewerTieBreaker() {
        // Reviewer 100 (Target)
        it.addReview(100, 1, 5);

        // Reviewer 50
        // Score = abs(5-1) / 1 = 4.0
        it.addReview(50, 1, 1);

        // Reviewer 25
        // Score = abs(5-9) / 1 = 4.0 (TIE)
        it.addReview(25, 1, 9);

        // Both 50 and 25 have the score 4.0. The method MUST return 25.
        assertEquals(25, it.similarReviewer(100));

        // Add a new, better reviewer to make sure logic still works
        it.addReview(30, 1, 6); // Score = 1.0
        assertEquals(30, it.similarReviewer(100));
    }


    /**
     * Test similarity for a movie that does not exist or has no ratings.
     */
    public void testSimilarMovieNonExistent() {
        // Test on an empty database
        assertEquals(-1, it.similarMovie(1));

        // Add data, but test a movie ID that hasn't been rated
        it.addReview(1, 1, 5);
        assertEquals(-1, it.similarMovie(2)); // Movie 2 has no ratings
    }


    /**
     * Test similarity when other movies exist, but none share any reviewers.
     */
    public void testSimilarMovieNoSharedReviewers() {
        it.addReview(1, 1, 5); // Movie 1
        it.addReview(2, 1, 5);
        it.addReview(3, 2, 5); // Movie 2 (shares no reviewers with 1)
        it.addReview(4, 3, 5); // Movie 3 (shares no reviewers with 1)

        // No one has a valid similarity score, so it must return -1.
        assertEquals(-1, it.similarMovie(1));
    }


    /**
     * Test a scenario with one perfect match (score 0.0) for a movie.
     */
    public void testSimilarMoviePerfectMatch() {
        // Movie 10 (Target)
        it.addReview(1, 10, 5);
        it.addReview(2, 10, 8);

        // Movie 20 (Bad Match)
        // Score = (abs(5-1) + abs(8-10)) / 2 = 3.0
        it.addReview(1, 20, 1);
        it.addReview(2, 20, 10);

        // Movie 3 (Perfect Match)
        // Score = (abs(5-5) + abs(8-8)) / 2 = 0.0
        it.addReview(1, 3, 5);
        it.addReview(2, 3, 8);

        // Movie 3 must be the best match.
        assertEquals(3, it.similarMovie(10));
    }


    /**
     * Tests the tie-breaker rule for movies. When two movies have the same
     * lowest score, the one with the SMALLER index must be returned.
     */
    public void testSimilarMovieTieBreaker() {
        // Movie 100 (Target)
        it.addReview(1, 100, 5);

        // Movie 50
        // Score = abs(5-1) / 1 = 4.0
        it.addReview(1, 50, 1);

        // Movie 25
        // Score = abs(5-9) / 1 = 4.0 (TIE)
        it.addReview(1, 25, 9);

        // Both 50 and 25 have the score 4.0. The method MUST return 25.
        assertEquals(25, it.similarMovie(100));
    }


    // ----------------------------------------------------------
    /**
     * Tests to make sure -1 is returned when a movie has no ratings.
     */
    public void testSimilarMovieNoRatings() {
        it.addReview(1, 10, 5);
        it.deleteScore(1, 10);
        assertEquals(-1, it.similarMovie(10));
    }


    // ----------------------------------------------------------
    /**
     * Tests for logic branch where a Reviewer rated movie B but not A.
     */
    public void testSimilarMovieLogicBranch() {
        it.addReview(5, 10, 8);
        it.addReview(10, 10, 10);
        it.addReview(3, 20, 5);
        it.addReview(10, 20, 5);
        it.addReview(10, 30, 1);
        assertEquals(20, it.similarMovie(10));
    }


    /**
     * This test comprehensively covers all "fail-fast" and "skip" branches
     * in the similarMovie method to kill multiple mutants at once.
     * It tests all 4 conditions required to hit lines 164 and 176.
     */
    public void testSimilarMovieAllFailAndSkipConditions() {
        // --- Test Branch 1 (L164): targetHeader == null ---
        // First, test on a totally empty database.
        // This makes findColHeader(99) return null.
        assertEquals(-1, it.similarMovie(99));

        // Now add data, but ask for a movie that still doesn't exist.
        it.addReview(1, 1, 5); // DB is no longer empty
        it.addReview(2, 1, 8);
        // This still executes targetHeader == null
        assertEquals(-1, it.similarMovie(99));

        // --- Test Branch 2 (L164): targetHeader.nNode == null ---
        // Create a header for Movie 5, then delete all its ratings
        // so its header exists, but its node list is null.
        it.addReview(1, 5, 10);
        it.deleteScore(1, 5); // Header for movie 5 now exists, but nNode is
                              // null

        // This now executes targetHeader != null, but targetHeader.nNode ==
        // null
        assertEquals(-1, it.similarMovie(5));

        // --- Test Branches 3 & 4 (L176): self-skip and empty-competitor-skip
        // ---
        // We will create a database where the *only* other movies are:
        // 1. The target movie itself (which must be skipped)
        // 2. An "empty" movie (which also must be skipped)

        it.clear();
        it.addReview(1, 10, 8); // Our Target (Movie 10)
        it.addReview(2, 10, 4);

        it.addReview(1, 20, 1); // An Empty Competitor (Movie 20)
        it.deleteScore(1, 20); // Movie 20 header now exists, but nNode is null

        // When similarMovie(10) runs:
        // 1. Loop finds Movie 10. (other.index == movie) -> SKIPS (Covers
        // Branch 3)
        // 2. Loop finds Movie 20. (other.nNode == null) -> SKIPS (Covers Branch
        // 4)
        // 3. The loop finishes having found no *valid* competitors.

        // The method MUST return -1, as no suitable match exists.
        assertEquals(-1, it.similarMovie(10));
    }


    /**
     * This test covers both branches of the fail-fast check on line 132.
     * It tests a reviewer that doesn't exist (targetHeader == null) and
     * a reviewer who exists but has no ratings (targetHeader.nNode == null).
     */
    public void testSimilarReviewerFailFastConditions() {
        // --- Test Branch 1 (targetHeader == null) ---
        // Test on an empty DB, then on a populated one with an unknown ID
        assertEquals(-1, it.similarReviewer(99));
        it.addReview(1, 1, 5); // Add some data
        assertEquals(-1, it.similarReviewer(99)); // Still returns -1

        // --- Test Branch 2 (targetHeader.nNode == null) ---
        // Create a header for Reviewer 5, then delete their only rating.
        it.addReview(5, 1, 10);
        it.deleteScore(5, 1); // Header for 5 exists, but nNode is now null

        // This executes the second part of the check on line 132
        assertEquals(-1, it.similarReviewer(5));
    }


    /**
     * This test covers both branches of the "skip" logic on line 144.
     * It ensures the method skips self-comparison AND skips competitors
     * who have no ratings, correctly identifying the only valid match.
     */
    public void testSimilarReviewerSkipsSelfAndEmptyCompetitors() {
        // Target Reviewer (ID 10)
        it.addReview(10, 1, 8);

        // Empty Competitor (ID 5)
        it.addReview(5, 5, 5);
        it.deleteScore(5, 5); // Header 5 exists, but nNode == null

        // Valid Competitor (ID 20)
        it.addReview(20, 1, 5); // Score = abs(8-5)/1 = 3.0

        // When similarReviewer(10) runs, the loop must:
        // 1. Find R5. Hit (otherHeader.getNode() == null). SKIPS (Covers Branch
        // 2 of L144)
        // 2. Find R10. Hit (otherHeader.getIndex() == reviewer). SKIPS (Covers
        // Branch 1 of L144)
        // 3. Find R20. Calculate score. R20 becomes the best match.

        assertEquals(20, it.similarReviewer(10));
    }
}
