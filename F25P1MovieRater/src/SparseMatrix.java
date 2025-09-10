
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
     * 
     */
    public SparseMatrix(int numRows, int numCols) {
        this.numRows = numRows;
        this.numCols = numCols;
        
        rowHeader = new HeaderNode(0);
        // using a temp node to implement amount of rows, while the row header stays at the start of the list
        HeaderNode tempStart = rowHeader;
        for(int i = 2; i < numRows; i++) {
            HeaderNode newNode = new HeaderNode(i);
            tempStart.n = newNode;
            tempStart = newNode;
            
            
        }
    }
    
    
    
    
    
    
    
    
    
    
    
    
}
