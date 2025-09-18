// -------------------------------------------------------------------------
/**
 * Implementation for the MovieRater. This is a thin shell over
 * the sparse matrix class that does the work.
 *
 * @author CS3114/5040 Staff
 * @version Summer 2025
 *
 */
public class MovieRaterDB implements MovieRater {
    private SparseMatrix matrix;

    // ----------------------------------------------------------
    /**
     * Create a new MovieRaterDB object.
     */
    MovieRaterDB() {
        matrix = new SparseMatrix();
    }


    // ----------------------------------------------------------
    /**
     * (Re)initialize the database.
     * 
     * @return true on clear
     */
    public boolean clear() {
        matrix = new SparseMatrix();
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

        // stores the first matrix node
        SparseMatrix.Node targetColNodes = targetHeader.getnNode();

        // initializes the variable to store most similar movie, -1 if invalid
        int bestMovieId = -1;
        double lowestScore = Double.MAX_VALUE;

        // 2. Iterate through EVERY OTHER movie in the database
        SparseMatrix.HeaderNode otherHeader = matrix.getColHeaderList();
        while (otherHeader != null) {
            // Skip if it's the same movie or if the other movie has no ratings
            if (otherHeader.getIndex() != movie) {

                // 3. Calculate the similarity score
                double score = calculateMovieSimilarity(targetColNodes,
                    otherHeader.getnNode());

                // 4. Track the best score (lowest positive score wins)
                if (score != -1.0) { // Score of -1 means no shared reviewers
                    if (score < lowestScore) {
                        lowestScore = score;
                        bestMovieId = otherHeader.getIndex();
                    }
                    // Tie-breaker: If scores are equal, choose the movie with
                    // the lower index
                    else if (score == lowestScore) {
                        bestMovieId = Math.min(bestMovieId, otherHeader
                            .getIndex());
                    }
                }
            }
            otherHeader = otherHeader.getN(); // Move to the next movie
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

        // stores the first matrix node
        SparseMatrix.Node targetRowNodes = targetHeader.getnNode();

        // variable to store similar movie index, -1 if no suitable match
        int bestReviewerId = -1;
        double lowestScore = Double.MAX_VALUE;

        // 2. Iterate through EVERY OTHER reviewer in the database
        SparseMatrix.HeaderNode otherHeader = matrix.getRowHeaderList();
        while (otherHeader != null) {
            // Skip if it's the same reviewer or has no ratings
            if (otherHeader.getIndex() != reviewer) {

                // 3. Calculate similarity score
                double score = calculateReviewerSimilarity(targetRowNodes,
                    otherHeader.getnNode());

                // 4. Track the best score (lowest positive score wins)
                if (score != -1.0) { // Score of -1 means no shared movies
                    if (score < lowestScore) {
                        lowestScore = score;
                        bestReviewerId = otherHeader.getIndex();
                    }
                    // Tie-breaker: If scores are equal, choose the reviewer
                    // with the lower index
                    else if (score == lowestScore) {
                        bestReviewerId = Math.min(bestReviewerId, otherHeader
                            .getIndex());
                    }
                }
            }
            otherHeader = otherHeader.getN(); // Move to the next reviewer
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
     * @return The similarity score, or -1.0 if no movies
     *         are shared.
     */
    private double calculateReviewerSimilarity(
        SparseMatrix.Node rowX,
        SparseMatrix.Node rowY) {

        // initialize variable for total difference between scores between
        // reviewers
        double totalDiff = 0;

        // initialize shared count of movies that reviewers have
        int sharedCount = 0;

        // first node for row x stored
        SparseMatrix.Node currX = rowX;

        // first node for row y stored
        SparseMatrix.Node currY = rowY;

        // enter loop while each node is not null
        while (currX != null && currY != null) {
            if (currX.getCol() < currY.getCol()) {
                currX = currX.getRight(); // entry rated by X, not Y
            }
            else if (currY.getCol() < currX.getCol()) {
                currY = currY.getRight(); // entry rated by Y, not X
            }
            else {
                // Shared movie found!
                totalDiff += Math.abs(currX.getValue() - currY.getValue());
                sharedCount++;
                currX = currX.getRight();
                currY = currY.getRight();
            }
        }

        if (sharedCount == 0) {
            return -1.0; // Per spec, score is -1 if no entries are shared
        }
        // return the result, which is the difference of scores over shared
        // number of entries
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

        // initialize variable for total difference between scores between
        // movies
        double totalDiff = 0;
        
        // initialize shared count of movies that reviewers have
        int sharedCount = 0;
        
        //first nodes for col A, col B stored
        SparseMatrix.Node currA = colA;
        SparseMatrix.Node currB = colB;

        //enter loop while neither node is null
        while (currA != null && currB != null) {
            if (currA.getRow() < currB.getRow()) {
                currA = currA.getDown(); // Reviewer rated A, not B
            }
            else if (currB.getRow() < currA.getRow()) {
                currB = currB.getDown(); // Reviewer rated B, not A
            }
            else {
                // Shared reviewer found!
                totalDiff += Math.abs(currA.getValue() - currB.getValue());
                sharedCount++;
                currA = currA.getDown();
                currB = currB.getDown();
            }
        }

        if (sharedCount == 0) {
            return -1.0; // Per spec, score is -1 if no reviewers are shared
        }

        return totalDiff / sharedCount;
    }
}
