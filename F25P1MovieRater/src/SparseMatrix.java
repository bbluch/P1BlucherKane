/**
 * The class containing the implementation of Sparse Matrix data structure.
 * 
 * @author benblucher, austink23
 * 
 * @version Sep 10, 2025
 */
public class SparseMatrix {

    private HeaderNode rowHeader; // start of the header list of the row headers
    private HeaderNode colHeader; // start of the header list of the column
                                  // headers

    /**
     * node class for the values inside the orthogonal matrix
     * 
     * -row and col indexes start at 0
     */
    public static class Node {
        private int row; // index of the row of the Node (reviewer)
        private int col; // index of the col of the Node (movie)
        private int value; // value in node (rating)
        private Node left; // Node to the left
        private Node right; // Node to the right
        private Node up; // Node above
        private Node down; // Node below

        /**
         * Create a new Node object.
         * 
         * @param row
         *            - row value for node
         * @param col
         *            - col value for node
         * @param val
         *            - val for node
         */
        Node(int row, int col, int val) {
            this.row = row;
            this.col = col;
            this.value = val;
        }


        /**
         * Get the current value of row.
         * 
         * @return The value of row for this object.
         */
        public int getRow() {
            return row;
        }


        /**
         * Get the current value of col.
         * 
         * @return The value of col for this object.
         */
        public int getCol() {
            return col;
        }


        /**
         * Get the current value of value.
         * 
         * @return The value of value variable for this object.
         */
        public int getValue() {
            return value;
        }


        /**
         * Get the current value of right.
         * 
         * @return The value of right for this object.
         */
        public Node getRight() {
            return right;
        }


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
        private int index; // index of header on the header list chain
        private HeaderNode n; // next HeaderNode, below for row, to the right
                              // for col
        private Node nNode; // start of the data values attached to header

        /**
         * Create a new HeaderNode object.
         * 
         * @param index
         *            - index of the HeaderNode
         */
        HeaderNode(int index) {
            this.setIndex(index);
        }


        /**
         * Get the current value of nNode.
         * 
         * @return The value of nNode for this object.
         */
        public Node getnNode() {
            return nNode;
        }


        /**
         * Set the value of nNode
         * 
         * @param nNode
         *            next node
         */
        public void setnNode(SparseMatrix.Node nNode) {
            this.nNode = nNode;

        }


        /**
         * Get the current value of index.
         * 
         * @return The value of index for this object.
         */
        public int getIndex() {
            return index;
        }


        /**
         * Set the value of index for this object.
         * 
         * @param index
         *            The new value for index.
         */
        public void setIndex(int index) {
            this.index = index;
        }


        /**
         * Get the current value of n.
         * 
         * @return The value of n for this object.
         */
        public HeaderNode getN() {
            return n;
        }


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
     */
    public SparseMatrix() {
        // Create the starting (dummy) headers. Lists will grow on demand
        rowHeader = new HeaderNode(0);
        colHeader = new HeaderNode(0);

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

        HeaderNode headRow = findRowHeader(row); // find header for row you want
                                                 // to insert to
        Node currentRow = headRow.getnNode(); // start Node of the row you want
                                              // to insert to

        Node prevRow = null; // placeholder to move through list

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

        // find header for col you want to insert to
        HeaderNode headCol = findColHeader(col);
        // start Node of the col you want to insert to
        Node currentCol = headCol.getnNode();
        Node prevCol = null; // placeholder to move through the list

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
            currentCol.up = newNode; // iterate to next node
        }
    }


    /**
     * print col from top to bottom
     * 
     * @param col
     *            column you want to list
     * @return String of values in column
     */
    public String colList(int col) {
        String fString = ""; // string to concatenate and return
        HeaderNode colNode = findColHeader(col); // find header for col you want
                                                 // to print

        fString = colNode.getIndex() + fString;
        fString = fString + ":";
        Node current = colNode.getnNode(); // placeholder to traverse the list

        if (current == null) {
            return null;
        }

        while (current != null) {
            fString = fString + " " + current.value;
            current = current.down;
        }

        return fString; // return list string
    }


    /**
     * print row from left to right
     * 
     * @param row
     *            you want to list
     * @return String of values in row
     */
    public String rowList(int row) {

        String fString = ""; // string to concatenate and return
        // find header for col you want to print
        HeaderNode rowNode = findRowHeader(row);

        fString = rowNode.getIndex() + fString;
        fString = fString + ":";
        Node current = rowNode.getnNode(); // placeholder to traverse the list

        if (current == null) {
            return null;
        }

        while (current != null) {
            fString = fString + " " + current.value;
            current = current.right;
        }
        return fString; // return string list
    }


    /**
     * list whole matrix using 'X: (X,X) (X,X)...' format
     * 
     * @return String of all values in matrix, "" if null
     */
    public String matrixList() {

        String fString = ""; // string to concatenate and return

        HeaderNode tempRowHeader = rowHeader; // placeholder to traverse the
                                              // header list

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
            tempRowHeader = tempRowHeader.getN(); // iterate to next node
        }
        return fString.trim(); // return string minus new line char at end
    }


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

        Node curr = headRow.getnNode();
        // Traverse the row list (which is faster than traversing a column)
        while (curr != null && curr.col < col) {
            curr = curr.right;
        }

        // If we found the node at the exact column
        if (curr != null && curr.col == col) {
            return curr;
        }

        return null; // Node doesn't exist
    }


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

        if (headRow.getnNode() == null) {
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

        if (headCol.getnNode() == null) {
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
     * Helper to locate row header that you want
     * 
     * @param row
     *            index of the row you are searching for
     * @return null or the header of the row you are searching for
     */
    public HeaderNode findRowHeader(int row) {
        // Start 'prev' at the dummy header (index 0)
        HeaderNode prev = rowHeader;
        // Start 'curr' at the first *real* header (index >= 1)
        HeaderNode curr = rowHeader.n;

        // Traverse the header list to find the correct sorted position
        while (curr != null && curr.index < row) {
            prev = curr;
            curr = curr.n;
        }

        // Case 1: The header for this row already exists. Return it.
        if (curr != null && curr.index == row) {
            return curr;
        }

        // Case 2: Header not found. Create it and insert it between pre and cur
        HeaderNode newNode = new HeaderNode(row);
        newNode.n = curr; // Links new node to the next one (or null if at end)
        prev.n = newNode; // Links the previous node to our new one
        return newNode;
    }


    /**
     * Helper to locate col header that you want
     * 
     * @param col
     *            index of the row you are searching for
     * @return HeaderNode - null or the header node of the row you are searching
     *         for
     */
    public HeaderNode findColHeader(int col) {
        HeaderNode prev = colHeader;
        HeaderNode curr = colHeader.n;

        // Traverse the header list to find the correct sorted position
        while (curr != null && curr.index < col) {
            prev = curr;
            curr = curr.n;
        }

        // Case 1: The header for this col already exists. Return it.
        if (curr != null && curr.index == col) {
            return curr;
        }

        // Case 2: Header not found. Create and insert.
        HeaderNode newNode = new HeaderNode(col);
        newNode.n = curr;
        prev.n = newNode;
        return newNode;

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
