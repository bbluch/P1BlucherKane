/**
 * The class containing the implementation of Sparse Matrix data structure.
 * 
 * @author benblucher, austink23
 * 
 * @version Sep 10, 2025
 */
public class SparseMatrix {

    private int numRows;
    private int numCols;
    private HeaderNode rowHeader;
    private HeaderNode colHeader;

    /**
     * node class for the values inside the orthogonal matrix
     * 
     * -row and col indexes start at 0
     */
    private static class Node {
        int row;
        int col;
        int value;
        Node left;
        Node right;
        Node up;
        Node down;

        Node(int row, int col, int val) {
            this.row = row;
            this.col = col;
            this.value = val;
        }
        // no getters and setters needed. use node.row or node.left instead
    }


    /**
     * node class for the headers that store the ring of starting nodes
     * 
     * -n is the next headerNode, so down for row and left for column
     * -nNode is the start of the doubly linked list of that row/col
     */
    private static class HeaderNode {
        int index;
        HeaderNode n;
        Node nNode;

        HeaderNode(int index) {
            this.index = index;
        }
    }

    /**
     * SparseMatric constructor. Sets up outside headers
     * 
     * @param numRows
     *            number of rows for header
     * @param numCols
     *            number of cols for header
     */
    public SparseMatrix(int numRows, int numCols) {
        this.numRows = numRows;
        this.numCols = numCols;

        rowHeader = new HeaderNode(0);
        // using a temp node to implement amount of rows, while the row header
        // stays at the start of the list
        HeaderNode tempStart = rowHeader;
        for (int i = 1; i < numRows; i++) {
            HeaderNode newNode = new HeaderNode(i);
            tempStart.n = newNode;
            tempStart = newNode;
        }

        colHeader = new HeaderNode(0);
        // using same temp node to implement amount of cols, while the col
        // header stays at the start of the list
        tempStart = colHeader;
        for (int i = 1; i < numCols; i++) {
            HeaderNode newNode = new HeaderNode(i);
            tempStart.n = newNode;
            tempStart = newNode;
        }

    }


    /**
     * Inserts a value at the specified row and column. If a node already
     * exists at this position, its value is updated.
     *
     * @param row
     *            - row value (reviewer)
     * @param col
     *            - column value (movie)
     * @param val
     *            - review value (score)
     */
    public void insert(int row, int col, int val) {
        Node newNode = new Node(row, col, val);

        // --- HORIZONTAL (ROW) INSERTION ---
        HeaderNode headRow = findRowHeader(row);
        Node currentRow = headRow.nNode;
        Node prevRow = null;

        // Traverse the row to find the correct position for the new node.
        // The original code only checked the first node. This loop finds the
        // correct position for insertion or finds an existing node to update.
        while (currentRow != null && currentRow.col < col) {
            prevRow = currentRow;
            currentRow = currentRow.right;
        }

        // If a node for this movie already exists, just update the score.
        if (currentRow != null && currentRow.col == col) {
            currentRow.value = val;
            return; // The node is updated, so we are done.
        }

        // Insert the new node into the row list
        newNode.left = prevRow;
        newNode.right = currentRow;
        if (prevRow != null) {
            prevRow.right = newNode;
        }
        else {
            headRow.nNode = newNode; // New first node for this row
        }
        if (currentRow != null) {
            currentRow.left = newNode;
        }

        // --- VERTICAL (COLUMN) INSERTION ---
        // The original method was missing this entire section. To conform to
        // the orthogonal list representation, each node must also be linked
        // vertically within its column.
        HeaderNode headCol = findColHeader(col);
        Node currentCol = headCol.nNode;
        Node prevCol = null;

        // Traverse the column to find the correct position for the new node
        while (currentCol != null && currentCol.row < row) {
            prevCol = currentCol;
            currentCol = currentCol.down;
        }

        // Insert the new node into the column list
        newNode.up = prevCol;
        newNode.down = currentCol;
        if (prevCol != null) {
            prevCol.down = newNode;
        }
        else {
            headCol.nNode = newNode; // New first node for this column
        }
        if (currentCol != null) {
            currentCol.up = newNode;
        }
    }


    /**
     * 
     * @param col
     *            column you want to list
     * @return String of values in column
     */
    public String colList(int col) {

        String fString = "";
        HeaderNode colNode = findColHeader(col);
        if (colNode == null) {
            return null;
        }

        fString = colNode.index + fString;
        fString = fString + ":";

        Node current = colNode.nNode;

        if (current == null) {
            return null;
        }

        while (current != null) {
            fString = fString + " " + current.value;
            current = current.down;
        }

        return fString;
    }


    /**
     * 
     * @param row
     *            you want to list
     * @return String of values in row
     */
    public String rowList(int row) {

        String fString = "";
        HeaderNode rowNode = findRowHeader(row);
        if (rowNode == null) {
            return null;
        }

        fString = rowNode.index + fString;
        fString = fString + ":";

        Node current = rowNode.nNode;

        if (current == null) {
            return null;
        }

        while (current != null) {
            fString = fString + " " + current.value;
            current = current.right;
        }

        return fString;
    }


    /**
     * 
     * @return String of all values in matrix, "" if null
     */
    public String matrixList() {
        String fString = "";

        if (numRows == 0 || numCols == 0) {
            return "";
        }

        if (rowHeader == null || colHeader == null) {
            return "";
        }

        HeaderNode tempRowHeader = rowHeader;
        // Node currentRow = rowHeader.nNode;

        while (tempRowHeader != null) {
            Node currentRow = tempRowHeader.nNode;
            if (currentRow != null) {
                fString = fString + tempRowHeader.index + ":";
                while (currentRow != null) {
                    fString = fString + " (" + currentRow.col + ", "
                        + currentRow.value + ")";
                    currentRow = currentRow.right;
                }
                fString = fString + "\n";
            }
            tempRowHeader = tempRowHeader.n;
            // currentRow = tempRowHeader.nNode;
        }

        return fString.trim();
    }


    // --- NEW METHOD START: getNode (Helper) ---
    /**
     * Helper method to find and return a specific node at (row, col).
     * 
     * @param row
     *            The row index
     * @param col
     *            The col index
     * @return The Node at (row, col), or null if not found.
     */
    private Node getNode(int row, int col) {
        HeaderNode headRow = findRowHeader(row);
        if (headRow == null) {
            return null;
        }

        Node curr = headRow.nNode;
        // Traverse the row list (which is faster than traversing a column)
        while (curr != null && curr.col < col) {
            curr = curr.right;
        }

        // If we found the node at the exact column
        if (curr != null && curr.col == col) {
            return curr;
        }

        // Node doesn't exist
        return null;
    }
    // --- NEW METHOD END ---


    /**
     * Removes a single node (score) from the matrix at (row, col).
     * This method unlinks the node from both its row and column lists.
     *
     * @param row
     *            The row (reviewer) index
     * @param col
     *            The column (movie) index
     * @return True if a node was found and removed, false otherwise.
     */
    public boolean remove(int row, int col) {
        Node nodeToRemove = getNode(row, col);

        // Node does not exist, so nothing to remove
        if (nodeToRemove == null) {
            return false;
        }

        // Unlink from horizontal (row) list
        if (nodeToRemove.left != null) {
            // It's not the first node in the row
            nodeToRemove.left.right = nodeToRemove.right;
        }
        else {
            // It IS the first node, so update the row header
            findRowHeader(row).nNode = nodeToRemove.right;
        }
        if (nodeToRemove.right != null) {
            nodeToRemove.right.left = nodeToRemove.left;
        }

        // Unlink from vertical (column) list
        if (nodeToRemove.up != null) {
            // It's not the first node in the column
            nodeToRemove.up.down = nodeToRemove.down;
        }
        else {
            // It IS the first node, so update the column header
            findColHeader(col).nNode = nodeToRemove.down;
        }
        if (nodeToRemove.down != null) {
            nodeToRemove.down.up = nodeToRemove.up;
        }

        return true;
    }


    /**
     * Removes an entire row (all ratings for a reviewer).
     * This iterates the row and unlinks each node from its respective column.
     *
     * @param row
     *            The row (reviewer) to remove.
     * @return True if the row had ratings and was removed, false if the
     *         reviewer did not exist or had no ratings.
     */
    public boolean removeRow(int row) {
        HeaderNode headRow = findRowHeader(row);

        // If row doesn't exist or already has no ratings, return false.
        if (headRow == null || headRow.nNode == null) {
            return false;
        }

        Node curr = headRow.nNode;
        while (curr != null) {
            // For each node in the row, we must unlink it from its column list
            if (curr.up != null) {
                curr.up.down = curr.down;
            }
            else {
                // This node was the first in its column, update column header
                findColHeader(curr.col).nNode = curr.down;
            }

            if (curr.down != null) {
                curr.down.up = curr.up;
            }
            curr = curr.right; // Move to the next node in the row
        }

        // Finally, clear the row header's pointer, deleting the entire row list
        headRow.nNode = null;
        return true;
    }


    /**
     * Removes an entire column (all ratings for a movie).
     * This iterates the column and unlinks each node from its respective row.
     *
     * @param col
     *            The column (movie) to remove.
     * @return True if the column had ratings and was removed, false if the
     *         movie did not exist or had no ratings.
     */
    public boolean removeCol(int col) {
        HeaderNode headCol = findColHeader(col);

        // If col doesn't exist or already has no ratings, return false
        if (headCol == null || headCol.nNode == null) {
            return false;
        }

        Node curr = headCol.nNode;
        while (curr != null) {
            // For each node in the column, unlink it from its row list
            if (curr.left != null) {
                curr.left.right = curr.right;
            }
            else {
                // This node was the first in its row, update row header
                findRowHeader(curr.row).nNode = curr.right;
            }

            if (curr.right != null) {
                curr.right.left = curr.left;
            }
            curr = curr.down; // Move to the next node in the column
        }

        // Clear the column header's pointer, deleting the entire column list
        headCol.nNode = null;
        return true;
    }


    /**
     * 
     * @param row
     *            index of the row you are searching for
     * @return null or the header of the row you are searching for
     */
    private HeaderNode findRowHeader(int row) {
        HeaderNode checker = rowHeader.n;

        while (checker != null && checker.index != row) {
            checker = checker.n;
        }
        return checker;

    }


    /**
     * Helper file
     * 
     * @param col
     *            index of the row you are searching for
     * @return HeaderNode - null or the header node of the row you are searching
     *         for
     */
    private HeaderNode findColHeader(int col) {
        HeaderNode checker = colHeader.n;

        while (checker != null && checker.index != col) {
            checker = checker.n;
        }
        return checker;

    }

}
