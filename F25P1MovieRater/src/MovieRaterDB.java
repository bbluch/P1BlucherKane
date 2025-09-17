// -------------------------------------------------------------------------
/**
 * Implementation for the MovieRater. This is a thin shell over
 * the sparse matrix class that does the work.
 *
 * @author CS3114/5040 Staff
 * @version Summer 2025
 *
 */
// test comment
public class MovieRaterDB implements MovieRater {
    private SparseMatrix matrix;

    // ----------------------------------------------------------
    /**
     * Create a new MovieRaterDB object.
     */
    MovieRaterDB() {
        matrix = new SparseMatrix(1000, 1000);
    }


    // ----------------------------------------------------------
    /**
     * (Re)initialize the database
     * 
     * @return true on clear
     */
    public boolean clear() {
        matrix = new SparseMatrix(1000, 1000);
        return true;
    }


    // ----------------------------------------------------------
    /**
     * Add a score to the database. If there already is a score for this
     * reviewer and movie pair, then update it.
     *
     * @param reviewer
     *            The reviewer giving the rating
     *            (must be a positive integer)
     * @param movie
     *            The movie being rated
     *            (must be a positive integer)
     * @param score
     *            The rating score (1-10)
     * @return True if the review was successfully added.
     *         False otherwise (for bad input values)
     */
    public boolean addReview(int reviewer, int movie, int score) {
        // Scores must be in the range 1 to 10.
        if (reviewer < 1 || movie < 1 || score < 1 || score > 10)
            return false;
        matrix.insert(reviewer, movie, score);
        return true;
    }


    // ----------------------------------------------------------
    /**
     * Delete the specified reviewer. This will delete all associated ratings.
     * 
     * @param reviewer
     *            The reviewer to delete
     *
     * @return True if the reviewer was successfully deleted.
     *         False if no such reviewer in the database.
     */
    public boolean deleteReviewer(int reviewer) {
        return matrix.removeRow(reviewer);
    }


    // ----------------------------------------------------------
    /**
     * Delete the specified movie. This will delete all associated ratings.
     * 
     * @param movie
     *            The movie to delete
     *
     * @return True if the movie was successfully deleted.
     *         False if no such movie in the database.
     */
    public boolean deleteMovie(int movie) {
        return matrix.removeCol(movie);
    }


    // ----------------------------------------------------------
    /**
     * Delete the specified score.
     * 
     * @param reviewer
     *            The reviewer of the score to delete
     * @param movie
     *            The movie of the score to delete
     *
     * @return True if the score was successfully deleted.
     *         False if no such score in the database.
     */
    public boolean deleteScore(int reviewer, int movie) {
        return matrix.remove(reviewer, movie);
    }


    // ----------------------------------------------------------
    /**
     * Dump out all the ratings. Each reviewer's rating should be in a
     * separate line (in ascending order by reviewer index), with
     * movie/score pairs listed in ascending order of movie index.
     * 
     * @return the string returned by the matrixList function
     */
    public String printRatings() {
        return matrix.matrixList();
    }


    // ----------------------------------------------------------
    /**
     * List all ratings for a given reviewer, with scores listed in
     * ascending order of movie index.
     * 
     * @param reviewer
     *            The reviewer to list ratings for
     * @return String representing the listing, null if no such reviewer
     */
    public String listReviewer(int reviewer) {
        return matrix.rowList(reviewer);
    }


    // ----------------------------------------------------------
    /**
     * List all ratings for a given movie, with scores listed in
     * ascending order of reviewer index.
     * 
     * @param movie
     *            The movie to list ratings for
     * @return String representing the listing, null if no such movie
     */
    public String listMovie(int movie) {
        return matrix.colList(movie);
    }


    // ----------------------------------------------------------
    /**
     * Return the index for the movie most similar to the specified one.
     * 
     * @param movie
     *            the movie to find match for.
     * @return The best matching index.
     *         Return -1 if this movie does not exist or if there is no
     *         suitable match
     */
    public int similarMovie(int movie) {
        // 1. Get the target movie's column data
        SparseMatrix.HeaderNode targetHeader = matrix.findColHeader(movie);

        // Return -1 if movie doesn't exist or has no ratings
        if (targetHeader == null || targetHeader.nNode == null) {
            return -1;
        }
        SparseMatrix.Node targetColNodes = targetHeader.nNode;

        int bestMovieId = -1;
        double lowestScore = Double.MAX_VALUE;

        // 2. Iterate through EVERY OTHER movie in the database
        SparseMatrix.HeaderNode otherHeader = matrix.getColHeaderList();
        while (otherHeader != null) {
            // Skip if it's the same movie or if the other movie has no ratings
            if (otherHeader.index != movie && otherHeader.nNode != null) {

                // 3. Calculate the similarity score
                double score = calculateMovieSimilarity(targetColNodes,
                    otherHeader.nNode);

                // 4. Track the best score (lowest positive score wins)
                if (score != -1.0) { // Score of -1 means no shared reviewers
                                     // [cite: 21]
                    if (score < lowestScore) {
                        lowestScore = score;
                        bestMovieId = otherHeader.index;
                    }
                    // Tie-breaker: If scores are equal, choose the movie with
                    // the lower index
                    else if (score == lowestScore) {
                        bestMovieId = Math.min(bestMovieId, otherHeader.index);
                    }
                }
            }
            otherHeader = otherHeader.n; // Move to the next movie
        }

        return bestMovieId; // Will be -1 if no suitable match was found
    }


    // ----------------------------------------------------------
    /**
     * Return the index for the reviewer most similar to the specified one.
     * 
     * @param reviewer
     *            the reviewer to find match for.
     * @return The best matching index.
     *         Return -1 if this reviewer does not exist or if there is no
     *         suitable match
     */
    public int similarReviewer(int reviewer) {
        // 1. Get the target reviewer's row data
        SparseMatrix.HeaderNode targetHeader = matrix.findRowHeader(reviewer);

        // Return -1 if reviewer doesn't exist or has no ratings
        if (targetHeader == null || targetHeader.nNode == null) {
            return -1;
        }
        SparseMatrix.Node targetRowNodes = targetHeader.nNode;

        int bestReviewerId = -1;
        double lowestScore = Double.MAX_VALUE;

        // 2. Iterate through EVERY OTHER reviewer in the database [cite: 17]
        SparseMatrix.HeaderNode otherHeader = matrix.getRowHeaderList();
        while (otherHeader != null) {
            // Skip if it's the same reviewer or has no ratings
            if (otherHeader.index != reviewer && otherHeader.nNode != null) {

                // 3. Calculate similarity score
                double score = calculateReviewerSimilarity(targetRowNodes,
                    otherHeader.nNode);

                // 4. Track the best score (lowest positive score wins) [cite:
                // 20]
                if (score != -1.0) { // Score of -1 means no shared movies
                                     // [cite: 21]
                    if (score < lowestScore) {
                        lowestScore = score;
                        bestReviewerId = otherHeader.index;
                    }
                    // Tie-breaker: If scores are equal, choose the reviewer
                    // with the lower index
                    else if (score == lowestScore) {
                        bestReviewerId = Math.min(bestReviewerId,
                            otherHeader.index);
                    }
                }
            }
            otherHeader = otherHeader.n; // Move to the next reviewer
        }

        return bestReviewerId; // Will be -1 if no suitable match was found
    }


    /**
     * Private helper to calculate similarity score between two reviewers.
     * Iterates both row lists simultaneously to find matching movies.
     * 
     * @param rowX
     *            Node list for Reviewer X
     * @param rowY
     *            Node list for Reviewer Y
     *            [cite_start]@return The similarity score, or -1.0 if no movies
     *            are shared. [cite: 21]
     */
    private double calculateReviewerSimilarity(
        SparseMatrix.Node rowX,
        SparseMatrix.Node rowY) {
        double totalDiff = 0;
        int sharedCount = 0;
        SparseMatrix.Node currX = rowX;
        SparseMatrix.Node currY = rowY;

        while (currX != null && currY != null) {
            if (currX.col < currY.col) {
                currX = currX.right; // Movie rated by X, not Y
            }
            else if (currY.col < currX.col) {
                currY = currY.right; // Movie rated by Y, not X
            }
            else {
                // Shared movie found! [cite: 18]
                totalDiff += Math.abs(currX.value - currY.value); // [cite: 18]
                sharedCount++; // [cite: 19]
                currX = currX.right;
                currY = currY.right;
            }
        }

        if (sharedCount == 0) {
            return -1.0; // Per spec, score is -1 if no movies are shared
        }

        return totalDiff / sharedCount;
    }


    /**
     * Private helper to calculate similarity score between two movies.
     * Iterates both column lists simultaneously to find matching reviewers.
     * 
     * @param colA
     *            Node list for Movie A
     * @param colB
     *            Node list for Movie B
     * @return The similarity score, or -1.0 if no reviewers are shared.
     */
    private double calculateMovieSimilarity(
        SparseMatrix.Node colA,
        SparseMatrix.Node colB) {
        double totalDiff = 0;
        int sharedCount = 0;
        SparseMatrix.Node currA = colA;
        SparseMatrix.Node currB = colB;

        while (currA != null && currB != null) {
            if (currA.row < currB.row) {
                currA = currA.down; // Reviewer rated A, not B
            }
            else if (currB.row < currA.row) {
                currB = currB.down; // Reviewer rated B, not A
            }
            else {
                // Shared reviewer found!
                totalDiff += Math.abs(currA.value - currB.value);
                sharedCount++;
                currA = currA.down;
                currB = currB.down;
            }
        }

        if (sharedCount == 0) {
            return -1.0; // Per spec, score is -1 if no reviewers are shared
        }

        return totalDiff / sharedCount;
    }
}
