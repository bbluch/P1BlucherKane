
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
        
        Node(int row, int col, int val){
            this.row = row;
            this.col = col;
            this.value = value;
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
        
        HeaderNode(int index){
            this.index = index;
        }
    }
    
    /**
     * SparseMatric constructor. Sets up outside headers
     * @param numRows number of rows for header
     * @param numCols number of cols for header
     */
    public SparseMatrix(int numRows, int numCols) {
        this.numRows = numRows;
        this.numCols = numCols;
        
        rowHeader = new HeaderNode(0);
        // using a temp node to implement amount of rows, while the row header stays at the start of the list
        HeaderNode tempStart = rowHeader;
        for(int i = 1; i < numRows; i++) {
            HeaderNode newNode = new HeaderNode(i);
            tempStart.n = newNode;
            tempStart = newNode;   
        }
        
        
        colHeader = new HeaderNode(0);
        // using same temp node to implement amount of cols, while the col header stays at the start of the list
        tempStart = colHeader;
        for(int i = 1; i < numCols; i++) {
            HeaderNode newNode = new HeaderNode(i);
            tempStart.n = newNode;
            tempStart = newNode;   
        }
        
    }
    
    /**
     * 
     * @param row index of the row you are searching for
     * @return null or the header of the row you are searching for 
     */
    private HeaderNode findRowHeader(int row) {
        HeaderNode checker = rowHeader.n;
        
        while(checker != null && checker.index != row) {
            checker = checker.n;            
        }
        return checker;
        
    }
    
    /**
     * 
     * @param col index of the row you are searching for
     * @return HeaderNode - null or the header node of the row you are searching for 
     */
    private HeaderNode findColHeader(int col) {
        HeaderNode checker = colHeader.n;
        
        while(checker != null && checker.index != col) {
            checker = checker.n;            
        }
        return checker;
        
    }

    
    
    
    
    
    
    
    
    
    
    
    
}
