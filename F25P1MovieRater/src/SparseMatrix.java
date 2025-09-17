/**
 * The class containing the implementation of Sparse Matrix data structure.
 * 
 * @author benblucher, austink23
 * 
 * @version Sep 10, 2025
 */
public class SparseMatrix {

    private int numRows; // number of rows in the matrix
    private int numCols; // number of columns in the matrix
    private HeaderNode rowHeader; // start of the header list of the row headers
    private HeaderNode colHeader; // start of the header list of the column
                                  // headers

    /**
     * node class for the values inside the orthogonal matrix
     * 
     * -row and col indexes start at 0
     */
    public static class Node {
        // index of the row of the Node (reviewer)
        private int row;
        // index of the col of the Node (movie)
        private int col;
        // value in node (rating)
        private int value;
        // Node to the left
        private Node left;
        // Node to the right
        private Node right;
        // Node above
        private Node up;
        // Node below
        private Node down;

        // ----------------------------------------------------------
        /**
         * Create a new Node object.
         * 
         * @param row
         * @param col
         * @param val
         */
        Node(int row, int col, int val) {
            this.row = row;
            this.col = col;
            this.value = val;
        }


        // ----------------------------------------------------------
        /**
         * Get the current value of row.
         * 
         * @return The value of row for this object.
         */
        public int getRow() {
            return row;
        }


        // ----------------------------------------------------------
        /**
         * Get the current value of col.
         * 
         * @return The value of col for this object.
         */
        public int getCol() {
            return col;
        }


        // ----------------------------------------------------------
        /**
         * Get the current value of value.
         * 
         * @return The value of value variable for this object.
         */
        public int getValue() {
            return value;
        }


        // ----------------------------------------------------------
        /**
         * Get the current value of right.
         * 
         * @return The value of right for this object.
         */
        public Node getRight() {
            return right;
        }


        // ----------------------------------------------------------
        /**
         * Get the current value of down.
         * 
         * @return The value of down for this object.
         */
        public Node getDown() {
            return down;
        }
    }


    /**
     * node class for the headers that store the ring of starting nodes
     * 
     * -n is the next headerNode, so down for row and left for column
     * -nNode is the start of the doubly linked list of that row/col
     */
    public static class HeaderNode {
        // index of header on the header list chain
        private int index;
        // next HeaderNode, below for row, to the right for col
        private HeaderNode n;
        // start of the data values attached to header
        private Node nNode;

        // ----------------------------------------------------------
        /**
         * Create a new HeaderNode object.
         * 
         * @param index
         */
        HeaderNode(int index) {
            this.setIndex(index);
        }


        // ----------------------------------------------------------
        /**
         * Get the current value of nNode.
         * 
         * @return The value of nNode for this object.
         */
        public Node getnNode() {
            return nNode;
        }


        // ----------------------------------------------------------
        /**
         * Set the value of nNode
         * 
         * @param nNode
         *            next node
         */
        public void setnNode(SparseMatrix.Node nNode) {
            this.nNode = nNode;

        }


        // ----------------------------------------------------------
        /**
         * Get the current value of index.
         * 
         * @return The value of index for this object.
         */
        public int getIndex() {
            return index;
        }


        // ----------------------------------------------------------
        /**
         * Set the value of index for this object.
         * 
         * @param index
         *            The new value for index.
         */
        public void setIndex(int index) {
            this.index = index;
        }


        // ----------------------------------------------------------
        /**
         * Get the current value of n.
         * 
         * @return The value of n for this object.
         */
        public HeaderNode getN() {
            return n;
        }


        // ----------------------------------------------------------
        /**
         * Set the value of n for this object.
         * 
         * @param n
         *            The new value for n.
         */
        public void setN(HeaderNode n) {
            this.n = n;
        }
    }

    /**
     * SparseMatrix constructor. Sets up outside headers
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
            tempStart.setN(newNode);
            tempStart = newNode;
        }

        colHeader = new HeaderNode(0);
        // using same temp node to implement amount of cols, while the col
        // header stays at the start of the list
        tempStart = colHeader;
        for (int i = 1; i < numCols; i++) {
            HeaderNode newNode = new HeaderNode(i);
            tempStart.setN(newNode);
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
        // find header for row you want to insert to
        HeaderNode headRow = findRowHeader(row);
        // start Node of the row you want to insert to
        Node currentRow = headRow.getnNode();
        // placeholder to move through list
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
            headRow.setnNode(newNode); // New first node for this row
        }
        if (currentRow != null) {
            currentRow.left = newNode;
        }

        // --- VERTICAL (COLUMN) INSERTION ---
        // The original method was missing this entire section. To conform to
        // the orthogonal list representation, each node must also be linked
        // vertically within its column.
        // find header for col you want to insert to
        HeaderNode headCol = findColHeader(col);
        // start Node of the col you want to insert to
        Node currentCol = headCol.getnNode();
        // placeholder to move through the list
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
            headCol.setnNode(newNode); // New first node for this column
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
        // string to concatenate and return
        String fString = "";
        // find header for col you want to print
        HeaderNode colNode = findColHeader(col);

        // if (colNode == null) {
        // return null;
        // }

        fString = colNode.getIndex() + fString;
        fString = fString + ":";
        // placeholder to traverse the list
        Node current = colNode.getnNode();

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

        // string to concatenate and return
        String fString = "";
        // find header for col you want to print
        HeaderNode rowNode = findRowHeader(row);

        // if (rowNode == null) {
        // return null;
        // }

        fString = rowNode.getIndex() + fString;
        fString = fString + ":";
        // placeholder to traverse the list
        Node current = rowNode.getnNode();

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

        // string to concatenate and return
        String fString = "";

        if (numRows == 0 || numCols == 0) {
            return "";
        }

        if (rowHeader == null || colHeader == null) {
            return "";
        }

        // placeholder to traverse the header list
        HeaderNode tempRowHeader = rowHeader;
        // Node currentRow = rowHeader.nNode;

        while (tempRowHeader != null) {
            // placeholder to traverse the list
            Node currentRow = tempRowHeader.getnNode();
            if (currentRow != null) {
                fString = fString + tempRowHeader.getIndex() + ":";
                while (currentRow != null) {
                    fString = fString + " (" + currentRow.col + ", "
                        + currentRow.value + ")";
                    currentRow = currentRow.right;
                }
                fString = fString + "\n";
            }
            tempRowHeader = tempRowHeader.getN();
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

        Node curr = headRow.getnNode();
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
            findRowHeader(row).setnNode(nodeToRemove.right);
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
            findColHeader(col).setnNode(nodeToRemove.down);
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
        if (headRow == null || headRow.getnNode() == null) {
            return false;
        }

        Node curr = headRow.getnNode();
        while (curr != null) {
            // For each node in the row, we must unlink it from its column list
            if (curr.up != null) {
                curr.up.down = curr.down;
            }
            else {
                // This node was the first in its column, update column header
                findColHeader(curr.col).setnNode(curr.down);
            }

            if (curr.down != null) {
                curr.down.up = curr.up;
            }
            curr = curr.right; // Move to the next node in the row
        }

        // Finally, clear the row header's pointer, deleting the entire row list
        headRow.setnNode(null);
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
        if (headCol == null || headCol.getnNode() == null) {
            return false;
        }

        Node curr = headCol.getnNode();
        while (curr != null) {
            // For each node in the column, unlink it from its row list
            if (curr.left != null) {
                curr.left.right = curr.right;
            }
            else {
                // This node was the first in its row, update row header
                findRowHeader(curr.row).setnNode(curr.right);
            }

            if (curr.right != null) {
                curr.right.left = curr.left;
            }
            curr = curr.down; // Move to the next node in the column
        }

        // Clear the column header's pointer, deleting the entire column list
        headCol.setnNode(null);
        return true;
    }


    /**
     * 
     * @param row
     *            index of the row you are searching for
     * @return null or the header of the row you are searching for
     */
    public HeaderNode findRowHeader(int row) {
        HeaderNode checker = rowHeader.getN();

        while (checker != null && checker.getIndex() != row) {
            checker = checker.getN();
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
    public HeaderNode findColHeader(int col) {
        HeaderNode checker = colHeader.getN();

        while (checker != null && checker.getIndex() != col) {
            checker = checker.getN();
        }
        return checker;

    }


    /**
     * Gets the main row header node (the start of the header list, index 0).
     * 
     * @return the rowHeader (header for index 0)
     */
    public HeaderNode getRowHeaderList() {
        return rowHeader;
    }


    /**
     * Gets the main col header node (the start of the header list, index 0).
     * 
     * @return the colHeader (header for index 0)
     */
    public HeaderNode getColHeaderList() {
        return colHeader;
    }

}
