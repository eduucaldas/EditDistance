import java.util.*;


public class EditDistance implements EditDistanceInterface {
     
    int c_i, c_d, c_r;
    static int MAX = Integer.MAX_VALUE;
    static int UNDEF = -1;
    Pair[][] prec;
    Pair[][] son;
    final static Pair undefPos = new Pair(UNDEF, UNDEF);
    
    static class Pair{
    	public int i;
    	public int j;
    	
    	
    	public Pair(int i, int j) {
    		this.i = i;
    		this.j = j;
    	}
    	
    	public void set(int i, int j) {
    		this.i = i;
    		this.j = j;
    	}
    	public String toString() {
    		return "(" + i + "," + j + ")";
    	}
    	
    	public boolean equals(Object o) {
    		Pair p = (Pair) o;
    		return (p.i == this.i) && (p.j == this.j);
    	}
    }
    
    public EditDistance (int c_i, int c_d, int c_r) {
        this.c_i = c_i;
        this.c_d = c_d;
        this.c_r = c_r;
    }
        
    private int new_d(int[][] d, int i, int j, String s1, String s2) {
    	int termR = d[i-1][j-1],termD = d[i-1][j] + c_d, termI = d[i][j-1] + c_i;
    	if(s1.charAt(i-1) != s2.charAt(j-1)) {
    		termR += c_r;
    	}
    	if(termR < termD) {
    		if(termR < termI) {
    			prec[i][j].set(i-1, j-1);
    			son[i-1][j-1].set(i, j);
    			return termR;
    		}
    		else{
    			prec[i][j].set(i, j-1);
    			son[i][j-1].set(i, j);
    			return termI;
    		}
    	}
    	else {
    		if(termD < termI) {
    			prec[i][j].set(i-1, j);
    			son[i-1][j].set(i, j);
    			return termD;
    		}
    		else{
    			prec[i][j].set(i, j-1);
    			son[i][j-1].set(i, j);
    			return termI;
    		}
    	}
    }//shouldn`t receive anything no i, j == 0
    
    public int[][] getEditDistanceDP(String s1, String s2) {
        /* To be completed in Part 1. Remove line below. */
        int[][] dist = init(s1.length(), s2.length());
        for(int i = 1; i <= s1.length(); i++) {
        	for(int j = 1; j <= s2.length(); j++) {
        		dist[i][j] = new_d(dist, i, j, s1, s2);
        	}
        }
        return dist;
    }
    
    public List<String> getMinimalEditSequence1(String s1, String s2) {
        LinkedList<String> ls = new LinkedList<> ();
        int dist[][] = getEditDistanceDP(s1, s2);
        int i = 0, j = 0;
        while(i != UNDEF || j != UNDEF) {
        	display(s1, s2, i, j);
        	if(son[i][j].i - i == 1 && son[i][j].j - j == 1 && dist[i][j] == dist[i-1][j-1] + c_r) {
        		ls.addFirst("replace(" + i + "," + s2.charAt(j) + ")");
        	}
        	else if(son[i][j].i - i == 0 && son[i][j].j - j == 1 ) {
        		ls.addLast("insert(" + i + "," + s2.charAt(j) + ")");
        	}
        	else if(son[i][j].i - i == 1 && son[i][j].j - j == 0  ) {
        		ls.addFirst("delete(" + i + ")");
        	}
        	else if(!(son[i][j].i - i == 1 && son[i][j].j - j == 1 && dist[i][j] == dist[i-1][j-1]))
        		System.out.println("Error");
        	i = son[i][j].i;
        	j = son[i][j].j;
        }
        while(j > 0) {
        	ls.addFirst("insert(" + i + "," + s2.charAt(j-1) + ")");
    		j--;
        }
        while(i>0) {
        	ls.addLast("delete(" + (i-1) + ")");
    		i--;
        }
        return ls;
    }
    
    public LinkedList<String> getMinimalEditSequence(String s1, String s2){
    	int dist[][] = getEditDistanceDP(s1, s2);
    	return getMinimalEditSequenceRec(s1, s2, dist, s1.length(), s2.length());
    }
    
    public LinkedList<String> getMinimalEditSequenceRec(String s1, String s2, int[][] dist, int i, int j){
    	LinkedList<String> l;
    	if(i > 0 && j > 0) {
        	display(s1, s2, i, j);
    		if(i - prec[i][j].i == 1 && j - prec[i][j].j == 1 && dist[i][j] == dist[i-1][j-1] + c_r) {
        		l = getMinimalEditSequenceRec(s1, s2, dist, i-1, j-1);
        		l.addFirst("replace(" + (i-1) + "," + s2.charAt(j-1) + ")");
        	}
        	else if(i - prec[i][j].i == 0 && j - prec[i][j].j == 1 ) {
        		l = getMinimalEditSequenceRec(s1, s2, dist, i, j-1);
        		l.addLast("insert(" + (j-1) + "," + s2.charAt(j-1) + ")");
        	}
        	else if(i - prec[i][j].i == 1 && j - prec[i][j].j == 0) {
        		l = getMinimalEditSequenceRec(s1, s2, dist, i-1, j);
        		l.addFirst("delete(" + (i-1) + ")");
        	}
        	else if(dist[i][j] == dist[i-1][j-1]) {
        		l = getMinimalEditSequenceRec(s1, s2, dist, i-1, j-1);
        	}
        	else {
        		System.out.println("Error");
        		l = null;
        	}
        	return l;
    	}
    	else {
    		l = new LinkedList<>();
    		while(j>0) {
    			l.addFirst("insert(" + (j-1) + "," + s2.charAt(j-1) + ")");
    			j--;
    		}
    		while(i>0) {
    			l.addLast("delete(" + (i-1) + ")");
    			i--;
    		}
    	}
    	return l;
    	
    }
    
    private int[][] init(int m, int n){
    	prec = new Pair[m+1][n+1];
    	son = new Pair[m+1][n+1];
    	int[][] dist = new int[m+1][n+1];
        for(int j = 1; j <= n; j++) {
        	for(int i = 1; i <= m; i++) {
            	dist[i][j] = UNDEF;
            	prec[i][j] = new Pair(UNDEF, UNDEF);
            	son[m-i][n-j] = new Pair(UNDEF, UNDEF);
            }
        }
        for(int i = 1; i < m+1; i++) {
        	dist[i][0] = i*c_i;
        	prec[i][0] = new Pair(i-1,0);
        	son[i-1][n] = new Pair(i,n);
        }
        for(int i = 1; i < n+1; i++) {
        	dist[0][i] = i*c_d;
        	prec[0][i] = new Pair(0, i-1);
        	son[m][i-1] = new Pair(m,i);
        }
        dist[0][0] = 0;
        prec[0][0] = new Pair(UNDEF, UNDEF);
        son[m][n] = new Pair(UNDEF, UNDEF);
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
    
    public static void display(Pair[][] matrix) {
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
    
    public static void display(String s1, String s2, int i, int j) {
    	System.out.println(i + " " + j);
    	System.out.println(s1.substring(0, i));
    	System.out.println(s2.substring(0, j));
    }
    
    private static void testInit(int m, int n, int c_i, int c_d, int c_r) {
    	System.out.println("testInit(" + m + ", " +  n + ", " +  c_i + ", " +  c_d + ", " +  c_r + ")");
    	EditDistance eD = new EditDistance(c_i, c_d, c_r);
    	int[][] dist = eD.init(m,n);
    	display(dist);
    	display(eD.prec);
    	display(eD.son);
    }
    
    private static void testGetEditDistanceDP(int c_i, int c_d, int c_r, String s1, String s2) {
    	System.out.println("testGetEditDistanceDP(" + c_i + ", " +  c_d + ", " +  c_r + ", " + s1 + ", " +  s2 + ")");

    	EditDistance eD = new EditDistance(c_i, c_d, c_r);
    	int[][] dist = eD.getEditDistanceDP(s1, s2);
    	display(dist);
    	display(eD.prec);
    }
    
    private static void testGetMinimalEditSequence(int c_i, int c_d, int c_r, String s1, String s2) {
    	System.out.println("testGetMinimalEditSequence(" + c_i + ", " +  c_d + ", " +  c_r + ", " + s1 + ", " +  s2 + ")");

    	EditDistance eD = new EditDistance(c_i, c_d, c_r);
    	List<String> ls = eD.getMinimalEditSequence(s1, s2);
    	boolean ok = Main.preValidateSequence(ls, s1, s2);
    	System.out.println(ok);
    	display(ls);
    }
    
    private static void testPair() {
    	Pair p = new Pair(1,2);
    	int MAX_VECTOR = 10;
    	Pair[] v = new Pair[MAX_VECTOR];
    	for(int i = 0; i < MAX_VECTOR; i++)
    		v[i] = new Pair(i, MAX_VECTOR - i);
    	v[0].set(0, 0);
    	System.out.println(p);
    	for(int i = 0; i < MAX_VECTOR; i++)
    		System.out.print(v[i] + " ");
    	System.out.println();
    }

    public static void main(String[] args) {
    	int c_i = 3;
    	int c_d = 2;
    	int c_r = 6;
    	String s1 = "abcd", s2 = "adcb";
    	testInit(s1.length(), s2.length(), c_i, c_d, c_r);
    	testGetEditDistanceDP(c_i, c_d, c_r, s1, s2);
    	testGetMinimalEditSequence(c_i, c_d, c_r, s1, s2);
	}
	
};
