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

        String f_string = "";
        HeaderNode colNode = findColHeader(col);
        if (colNode == null) {
            return null;
        }

        f_string = colNode.index + f_string;
        f_string = f_string + ":";

        Node current = colNode.nNode;

        if (current == null) {
            return "";
        }

        while (current != null) {
            f_string = f_string + " " + current.value;
            current = current.down;
        }

        return f_string;
    }


    /**
     * 
     * @param row
     *            you want to list
     * @return String of values in row
     */
    public String rowList(int row) {

        String f_string = "";
        HeaderNode rowNode = findRowHeader(row);
        if (rowNode == null) {
            return null;
        }

        f_string = rowNode.index + f_string;
        f_string = f_string + ":";

        Node current = rowNode.nNode;

        if (current == null) {
            return "";
        }

        while (current != null) {
            f_string = f_string + " " + current.value;
            current = current.right;
        }

        return f_string;
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
        Node currentRow = rowHeader.nNode;

        while (tempRowHeader != null) {
            fString = fString + tempRowHeader.index + ":";
            while (currentRow != null) {
                fString = fString + " (" + currentRow.col + ", "
                    + currentRow.value + ")";
                currentRow = currentRow.right;
            }
            fString = fString + "/n";
            tempRowHeader = tempRowHeader.n;
            currentRow = tempRowHeader.nNode;
        }

        return fString;
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
