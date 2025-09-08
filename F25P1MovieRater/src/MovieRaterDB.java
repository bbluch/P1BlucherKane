//-------------------------------------------------------------------------
/**
 * Implementation for the MovieRater. This is a thin shell over
 * the sparse matrix class that does the work.
 *
 * @author CS3114/5040 Staff
 * @version Summer 2025
 *
 */
public class MovieRaterDB implements MovieRater {

    // ----------------------------------------------------------
    /**
     * Create a new MovieRaterDB object.
     */
    MovieRaterDB() {
    }

    // ----------------------------------------------------------
    /**
     * (Re)initialize the database
     * @return true on clear
     */
    public boolean clear() {
        return true;
    }


    // ----------------------------------------------------------
    /**
     * Add a score to the database. If there already is a score for this
     * reviewer and movie pair, then update it.
     *
     * @param reviewer The reviewer giving the rating
     *   (must be a positive integer)
     * @param movie The movie being rated
     *   (must be a positive integer)
     * @param score The rating score (1-10)
     * @return True if the review was successfully added.
     *         False otherwise (for bad input values)
     */
    public boolean addReview(int reviewer, int movie, int score) {
        // Scores must be in the range 1 to 10.
        return true;
    }


    // ----------------------------------------------------------
    /**
     * Delete the specified reviewer. This will delete all associated ratings.
     * @param reviewer The reviewer to delete
     *
     * @return True if the reviewer was successfully deleted.
     *         False if no such reviewer in the database.
     */
    public boolean deleteReviewer(int reviewer) {
        return true;
    }


    // ----------------------------------------------------------
    /**
     * Delete the specified movie. This will delete all associated ratings.
     * @param movie The movie to delete
     *
     * @return True if the movie was successfully deleted.
     *         False if no such movie in the database.
     */
    public boolean deleteMovie(int movie) {
        return true;
    }


    // ----------------------------------------------------------
    /**
     * Delete the specified score.
     * @param reviewer The reviewer of the score to delete
     * @param movie The movie of the score to delete
     *
     * @return True if the score was successfully deleted.
     *         False if no such score in the database.
     */
    public boolean deleteScore(int reviewer, int movie) {
        return true;
    }


    // ----------------------------------------------------------
    /**
     * Dump out all the ratings. Each reviewer's rating should be in a
     * separate line (in ascending order by reviewer index), with
     * movie/score pairs listed in ascending order of movie index.
     * @return String representing the listing, empty string if there are none
     */
    public String printRatings() {
        return "";
    }


    // ----------------------------------------------------------
    /**
     * List all ratings for a given reviewer, with scores listed in
     * ascending order of movie index.
     * @param reviewer The reviewer to list ratings for
     * @return String representing the listing, null if no such reviewer
     */
    public String listReviewer(int reviewer) {
        return "";
    }


    // ----------------------------------------------------------
    /**
     * List all ratings for a given movie, with scores listed in
     * ascending order of reviewer index.
     * @param movie The movie to list ratings for
     * @return String representing the listing, null if no such movie
     */
    public String listMovie(int movie) {
        return "";
    }


    // ----------------------------------------------------------
    /**
     * Return the index for the movie most similar to the specified one.
     * @param movie the movie to find match for.
     * @return The best matching index.
     *         Return -1 if this movie does not exist or if there is no
     *           suitable match
     */
    public int similarMovie(int movie) {
        return -1;
    }


    // ----------------------------------------------------------
    /**
     * Return the index for the reviewer most similar to the specified one.
     * @param reviewer the reviewer to find match for.
     * @return The best matching index.
     *         Return -1 if this reviewer does not exist or if there is no
     *           suitable match
     */
    public int similarReviewer(int reviewer) {
        return -1;
    }
}
