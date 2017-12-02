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
        LinkedList<String> ls = new LinkedList<> ();
        int dist[][] = getEditDistanceDP(s1, s2);
        int i = s1.length(), j = s2.length();
        while(i>0 && j>0) {
        	if(dist[i][j] == dist[i-1][j-1]) {
        		i--;
        		j--;
        	}
        	else if(dist[i][j] == dist[i-1][j-1] + c_r) {
        		ls.addFirst("replace(" + (i-1) + "," + s2.charAt(j-1) + ")");
        		i--;
        		j--;
        	}
        	else if(dist[i][j] == dist[i][j-1] + c_i) {
        		ls.addFirst("insert(" + (i-1) + "," + s2.charAt(j-1) + ")");
        		j--;
        	}
        	else if(dist[i][j] == dist[i-1][j] + c_d) {
        		ls.addFirst("delete(" + (i-1) + ")");
        		i--;
        	}
        	else
        		System.out.println("Error");
        }
        while(j > 0) {
        	ls.addFirst("insert(" + (i-1) + "," + s2.charAt(j-1) + ")");
    		j--;
        }
        while(i>0) {
        	ls.addFirst("delete(" + (i-1) + ")");
    		i--;
        }
        return ls;
    }
    
    //public List<String> getMinimalEditSequenceRec(String s1, String s2)
    
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
    
    public static void display(List<String> ls) {
    	for(String op: ls) {
    		System.out.println(op);
    	}
    }
    /*
    public void stepByStepSolution(final String s1, final String s2) {
    	final char[] start = s1.toCharArray();
    	final char[] end = s2.toCharArray();
    	System.out.println(start);
        final int dist[][] = getEditDistanceDP(s1, s2);
        int i = start.length, j = end.length;
        while(i>0 && j>0) {
        	if(dist[i][j] == dist[i-1][j-1]) {
        		i--;
        		j--;
        	}
        	else if(dist[i][j] == dist[i-1][j-1] + c_r) {
        		
        		ls.addFirst("replace(" + (i-1) + "," + s2.charAt(j-1) + ")");
        		i--;
        		j--;
        	}
        	else if(dist[i][j] == dist[i][j-1] + c_i) {
        		ls.addFirst("insert(" + (i-1) + "," + s2.charAt(j-1) + ")");
        		j--;
        	}
        	else if(dist[i][j] == dist[i-1][j] + c_d) {
        		ls.addFirst("delete(" + (i-1) + ")");
        		i--;
        	}
        	else
        		System.out.println("Error");
        }
        while(i == 0) {}
        	
    	
    	
    	
    	System.out.println();
    	System.out.println(end);
    }*/
    
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
    
    private static void testGetMinimalEditSequence(int c_i, int c_d, int c_r, String s1, String s2) {
    	System.out.println("testGetMinimalEditSequence(" + c_i + ", " +  c_d + ", " +  c_r + ", " + s1 + ", " +  s2 + ")");

    	EditDistance eD = new EditDistance(c_i, c_d, c_r);
    	List<String> ls = eD.getMinimalEditSequence(s1, s2);
    	
    	display(ls);
    }
    
    
    
    public static void main(String[] args) {
    	int m = 5;
    	int n = 4;
    	int c_i = 3;
    	int c_d = 2;
    	int c_r = 6;
    	String s1 = "ab", s2 = "";
    	//testNewDist(m, n, c_i, c_d, c_r);
    	testGetEditDistanceDP(c_i, c_d, c_r, s1, s2);
    	testGetMinimalEditSequence(c_i, c_d, c_r, s1, s2);
	}
	
};
