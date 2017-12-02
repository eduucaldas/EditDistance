import java.util.*;


public class EditDistance implements EditDistanceInterface {
     
    int c_i, c_d, c_r;
    static int MAX = Integer.MAX_VALUE;
    static int UNDEF = -1;

    public EditDistance (int c_i, int c_d, int c_r) {
        this.c_i = c_i;
        this.c_d = c_d;
        this.c_r = c_r;
    }
        
    private int new_d(int[][] d, int i, int j, String s1, String s2) {
    	
    	int term1 = d[i-1][j-1],term2 = d[i-1][j] + c_d, term3 = d[i][j-1] + c_i;
    	if(s1.charAt(i-1) != s2.charAt(j-1)) {
    		term1 += c_r;
    	}
    	return Math.min(Math.min(term1, term2), term3);
    }//shouldn`t receive anything no i, j == 0
    
    public int[][] getEditDistanceDP(String s1, String s2) {
        /* To be completed in Part 1. Remove line below. */
        int[][] dist = newDist(s1.length(), s2.length());
        for(int i = 1; i <= s1.length(); i++) {
        	for(int j = 1; j <= s2.length(); j++) {
        		dist[i][j] = new_d(dist, i, j, s1, s2);
        	}
        }
        
        
        return dist;
    }

    public List<String> getMinimalEditSequence(String s1, String s2) {
        /* To be completed in Part 2. Remove sample code block below. */
        LinkedList<String> ls = new LinkedList<> ();
        if (c_r == 6) {
            ls.add("delete(1)");
            ls.add("delete(1)");
            ls.add("insert(2,c)");
            ls.add("insert(3,b)");
        }
        else {
            ls.add("replace(1,d)");
            ls.add("replace(3,b)");
        }
        return ls;
        /* Code block to be removed ends. */
    }
    
    private int[][] newDist(int m, int n){
    	int[][] dist = new int[m+1][n+1];
        for(int j = 1; j < n+1; j++) {
        	for(int i = 1; i < m+1; i++) {
            	dist[i][j] = UNDEF;
            }
        }
        for(int i = 0; i < m+1; i++) {
        	dist[i][0] = i*c_i;
        }
        for(int i = 1; i < n+1; i++) {
        	dist[0][i] = i*c_d;
        }
        return dist;
    }
    
    public static void display(int[][] matrix) {
    	for (int i = 0; i < matrix.length; i++) {
    	    for (int j = 0; j < matrix[i].length; j++) {
    	        System.out.print(matrix[i][j] + " ");
    	    }
    	    System.out.println();
    	}
    }
    
    private static void testNewDist(int m, int n, int c_i, int c_d, int c_r) {
    	System.out.println("testNewDist(" + m + ", " +  n + ", " +  c_i + ", " +  c_d + ", " +  c_r + ")");
    	EditDistance eD = new EditDistance(c_i, c_d, c_r);
    	int[][] dist = eD.newDist(m,n);
    	display(dist);
    }
    
    private static void testGetEditDistanceDP(int c_i, int c_d, int c_r, String s1, String s2) {
    	System.out.println("testGetEditDistanceDP(" + c_i + ", " +  c_d + ", " +  c_r + ", " + s1 + ", " +  s2 + ")");

    	EditDistance eD = new EditDistance(c_i, c_d, c_r);
    	int[][] dist = eD.getEditDistanceDP(s1, s2);
    	display(dist);
    }
    /*
    public static void main(String[] args) {
    	int m = 5;
    	int n = 4;
    	int c_i = 3;
    	int c_d = 2;
    	int c_r = 6;
    	String s1 = "abcd", s2 = "adcb";
    	//testNewDist(m, n, c_i, c_d, c_r);
    	testGetEditDistanceDP(c_i, c_d, c_r, s1, s2);
    	
	}
	*/
};
