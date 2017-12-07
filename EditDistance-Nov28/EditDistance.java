import java.util.*;


public class EditDistance implements EditDistanceInterface {
     
    int c_i, c_d, c_r;
    static int MAX = Integer.MAX_VALUE;
    static final int UNDEF = -1;
    static final int RIGHT = 0;
    static final int DOWN = 1;
    static final int DIAG = 2;
    static final int INSERT = 1;
    static final int DELETE = 2;
    static final int REPLACE = 3;
    static final int JUMP = 4;
    
    int[][] direcFrom;//from which direction did it came in dist, this is unique
    //int[][] direcTo;//this is not unique, itll be obtained from direct from
    //Pair[][] prec;
    //Pair[][] son;
    //final static Pair undefPos = new Pair(UNDEF, UNDEF);
    /*
    static class Pair{
    	public int i;
    	public int j;
    	
    	public static void display(Pair[][] matrix) {
	    	for (int i = 0; i < matrix.length; i++) {
	    	    for (int j = 0; j < matrix[i].length; j++) {
	    	        System.out.print(matrix[i][j] + " ");
	    	    }
	    	    System.out.println();
	    	}
    	}
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
    }*/
    
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
    			//prec[i][j].set(i-1, j-1);
    			//son[i-1][j-1].set(i, j);
    			//direcTo[i-1][j-1] = DIAG;
    			direcFrom[i][j] = DIAG;
    			return termR;
    		}
    		else{
    			//prec[i][j].set(i, j-1);
    			//son[i][j-1].set(i, j);
    			//direcTo[i][j-1] = RIGHT;
    			direcFrom[i][j] = RIGHT;
    			return termI;
    		}
    	}
    	else {
    		if(termD < termI) {
    			//prec[i][j].set(i-1, j);
    			//son[i-1][j].set(i, j);
    			//direcTo[i-1][j] = DOWN;
    			direcFrom[i][j] = DOWN;
    			return termD;
    		}
    		else{
    			//prec[i][j].set(i, j-1);
    			//son[i][j-1].set(i, j);
    			//direcTo[i][j-1] = RIGHT;
    			direcFrom[i][j] = RIGHT;
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
    
    public List<String> getMinimalEditSequence(String s1, String s2) {
        LinkedList<String> ls = new LinkedList<> ();
        int dist[][] = getEditDistanceDP(s1, s2);
        int i = s1.length(), j = s2.length();
        Stack<Integer> direcTo = new Stack<>();
        //System.out.println("direcFrom:");display(direcFrom);
        //System.out.println("dist:");display(dist);
        while(i != 0 || j != 0) {
        	//display(s1, s2, i, j);
        	
        	//display(direcFrom);
            
        	if(direcFrom[i][j] == DIAG) {
        		if(dist[i][j] == dist[i-1][j-1] + c_r)
        			direcTo.push(REPLACE);	
        		else if(dist[i][j] == dist[i-1][j-1])
        			direcTo.push(JUMP);
        		else	
        			System.out.println("Error");
        		i--;
        		j--;
        	}
        	else if(direcFrom[i][j] == RIGHT) {
        		direcTo.push(INSERT);
        		j--;
        	}
        	else if(direcFrom[i][j] == DOWN) {
        		direcTo.push(DELETE);
        		i--;
        	}
        	else
        		System.out.println("error in Stack filling");
        	//i = son[i][j].i;
        	//j = son[i][j].j;
        }
        while(!direcTo.isEmpty()) {
        	int command = direcTo.pop();
        	
        	if(command == REPLACE) {
        		ls.addFirst("replace(" + i + "," + s2.charAt(j) + ")");
        		i++;
        		j++;
        	}
        	else if(command == JUMP) {
        		i++;
        		j++;
        	}
        	else if(command == INSERT) {
        		ls.addLast("insert(" + j + "," + s2.charAt(j) + ")");
        		j++;
        	}
        	else if(command == DELETE) {
        		ls.addFirst("delete(" + i + ")");
        		i++;
        	}
        	else
        		System.out.println("error processing");
        }
        return ls;
    }
    
    public LinkedList<String> getMinimalEditSequence1(String s1, String s2){
    	int dist[][] = getEditDistanceDP(s1, s2);
    	return getMinimalEditSequenceRec(s1, s2, dist, s1.length(), s2.length());
    }
    
    public LinkedList<String> getMinimalEditSequenceRec(String s1, String s2, int[][] dist, int i, int j){
    	LinkedList<String> l;
    	if(i > 0 && j > 0) {
        	display(s1, s2, i, j);
    		if(direcFrom[i][j] == DIAG && dist[i][j] == dist[i-1][j-1] + c_r) {
        		l = getMinimalEditSequenceRec(s1, s2, dist, i-1, j-1);
        		l.addFirst("replace(" + (i-1) + "," + s2.charAt(j-1) + ")");
        	}
        	else if(direcFrom[i][j] == RIGHT) {
        		l = getMinimalEditSequenceRec(s1, s2, dist, i, j-1);
        		l.addLast("insert(" + (j-1) + "," + s2.charAt(j-1) + ")");
        	}
        	else if(direcFrom[i][j] == DOWN) {
        		l = getMinimalEditSequenceRec(s1, s2, dist, i-1, j);
        		l.addFirst("delete(" + (i-1) + ")");
        	}
        	else if(direcFrom[i][j] == DIAG && dist[i][j] == dist[i-1][j-1]) {
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
    	//prec = new Pair[m+1][n+1];
    	//son = new Pair[m+1][n+1];
    	direcFrom = new int[m+1][n+1];
    	//direcTo = new int[m+1][n+1];
    	int[][] dist = new int[m+1][n+1];
        for(int j = 1; j <= n; j++) {
        	for(int i = 1; i <= m; i++) {
            	dist[i][j] = UNDEF;
            	//prec[i][j] = new Pair(UNDEF, UNDEF);
            	//son[m-i][n-j] = new Pair(UNDEF, UNDEF);
            	//direcTo[m-i][n-j] = UNDEF;
            	direcFrom[i][j] = UNDEF;
            }
        }
        for(int i = 1; i < n+1; i++) {
        	dist[0][i] = i*c_i;
        	//prec[0][i] = new Pair(0, i-1);
        	//son[m][i-1] = new Pair(m,i);
        	//direcTo[m][i-1] = RIGHT;
        	direcFrom[0][i] = RIGHT;
        }
        for(int i = 1; i < m+1; i++) {
        	dist[i][0] = i*c_d;
        	//prec[i][0] = new Pair(i-1,0);
        	//son[i-1][n] = new Pair(i,n);
        	//direcTo[i-1][n] = DOWN;
        	direcFrom[i][0] = DOWN;
        }
        dist[0][0] = 0;
        //prec[0][0] = new Pair(UNDEF, UNDEF);
        //son[m][n] = new Pair(UNDEF, UNDEF);
        direcFrom[0][0] = UNDEF;
        //direcTo[m][n] = UNDEF;
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
    	int n_op = 0;
    	for(String op: ls) {
    		System.out.println(op);
    		n_op++;
    	}
    	System.out.println("Number of operations: " + n_op);
    }
    
    public static void display(String s1, String s2, int i, int j) {
    	System.out.println(i + " " + j);
    	System.out.println(s1.substring(0, i));
    	System.out.println(s2.substring(0, j));
    }

    public static boolean isOptimalSequence(List<String> es, int cost, int c_i, int c_d, int c_r) throws Exception{
        int i = 0, d = 0, r = 0;
    	for (String op : es) {
            String [] args = op.split("[\\(\\)\\,]", 20);
            if(args[0].startsWith("replace"))
                r ++;
            else if(args[0].startsWith("delete"))
                d++;
            else if(args[0].startsWith("insert"))
                i++;
            else throw new Exception ("Invalid command");
        }
        if(i*c_i + d*c_d + r*c_r == cost)
        	return true;
        else
        	return false;
    }

    private static void testInit(int m, int n, int c_i, int c_d, int c_r) {
    	System.out.println("testInit(" + m + ", " +  n + ", " +  c_i + ", " +  c_d + ", " +  c_r + ")");
    	EditDistance eD = new EditDistance(c_i, c_d, c_r);
    	int[][] dist = eD.init(m,n);
    	display(dist);
    	System.out.println("direcTo: ");
    	//display(eD.direcTo);
    	//display(eD.prec);
    	//display(eD.son);
    }
    
    private static void testGetEditDistanceDP(int c_i, int c_d, int c_r, String s1, String s2) {
    	System.out.println("testGetEditDistanceDP(" + c_i + ", " +  c_d + ", " +  c_r + ", " + s1 + ", " +  s2 + ")");

    	EditDistance eD = new EditDistance(c_i, c_d, c_r);
    	int[][] dist = eD.getEditDistanceDP(s1, s2);
    	display(dist);
    	//display(eD.direcTo);
    	//display(eD.prec);
    }
    
    private static void testGetMinimalEditSequence(int c_i, int c_d, int c_r, String s1, String s2) {
    	System.out.println("testGetMinimalEditSequence(" + c_i + ", " +  c_d + ", " +  c_r + ", " + s1 + ", " +  s2 + ")");
    	
    	EditDistance eD = new EditDistance(c_i, c_d, c_r);
    	List<String> ls = eD.getMinimalEditSequence(s1, s2);
    	boolean validSequence = Main.preValidateSequence(ls, s1, s2);
    	System.out.println("Is it a valid Sequence ? " + validSequence);
    	//display(ls);
    }
    
    private static void testEditDistance(int c_i, int c_d, int c_r, String s1, String s2) throws Exception {
    	System.out.println("testEditDistance(" + c_i + ", " +  c_d + ", " +  c_r + ", " + s1 + ", " +  s2 + ")");
    	long tictoc = System.nanoTime();
    	EditDistance eD = new EditDistance(c_i, c_d, c_r);
    	int dist[][] = eD.getEditDistanceDP(s1, s2);
    	List<String> ls = eD.getMinimalEditSequence(s1, s2);
    	tictoc = (System.nanoTime() - tictoc)/1000000;
    	System.out.println("Transforming "+s1+" to "+s2+" with (c_i,c_d,c_r) = (" + c_i + "," + c_d + "," + c_r + ")");
    	display(dist);
    	System.out.println("Cost = "+dist[s1.length()][s2.length()]);
    	System.out.println("Minimal edits from "+s1+" to "+s2+" with (c_i,c_d,c_r) = (" + c_i + "," + c_d + "," + c_r + "):");
    	display(ls);
    	//System.out.println("direcTo:");
    	//display(eD.direcTo);
    	boolean validSequence = Main.preValidateSequence(ls, s1, s2);
    	boolean optimalSequence = isOptimalSequence(ls, dist[s1.length()][s2.length()], eD.c_i, eD.c_d, eD.c_r);
    	System.out.println("Is it a valid Sequence ? " + validSequence);
    	System.out.println("Is it the optimal Sequence ? " + optimalSequence);
    	System.out.println("This test had m = " + s1.length() + ", n = " + s2.length() + "\n It took " + tictoc + " milliseconds");	
    }
    
    private static void testBIGEditDistance(int c_i, int c_d, int c_r, String s1, String s2) throws Exception {
    	//System.out.println("testBIGEditDistance(" + c_i + ", " +  c_d + ", " +  c_r + ", " + s1 + ", " +  s2 + ")");
    	long start = System.nanoTime();
    	
    	EditDistance eD = new EditDistance(c_i, c_d, c_r);
    	int dist[][] = eD.getEditDistanceDP(s1, s2);
    	long tictoc = (System.nanoTime() - start)/1000000;
    	System.out.println("This test had m = " + s1.length() + ", n = " + s2.length() + "\n First part took " + tictoc + " milliseconds");
    	List<String> ls = eD.getMinimalEditSequence(s1, s2);
    	tictoc = (System.nanoTime()-start)/1000000 - tictoc;
    	System.out.println("This test had m = " + s1.length() + ", n = " + s2.length() + "\n Second part took " + tictoc + " milliseconds");
    	
    	//System.out.println("Transforming "+s1+" to "+s2+" with (c_i,c_d,c_r) = (" + c_i + "," + c_d + "," + c_r + ")");
    	//display(dist);
    	//System.out.println("Cost = "+dist[s1.length()][s2.length()]);
    	//System.out.println("Minimal edits from "+s1+" to "+s2+" with (c_i,c_d,c_r) = (" + c_i + "," + c_d + "," + c_r + "):");
    	//display(ls);
    	
    	boolean isValidSequence = Main.preValidateSequence(ls, s1, s2);
    	boolean isOptimalSequence = isOptimalSequence(ls, dist[s1.length()][s2.length()], eD.c_i, eD.c_d, eD.c_r);
    	System.out.println("Is it a valid Sequence ? " + isValidSequence);
    	System.out.println("Is it an optimal Sequence ? " + isOptimalSequence);
    	System.out.println("This test had m = " + s1.length() + ", n = " + s2.length() + "\n It took " + ((System.nanoTime() - start)/1000000) + " milliseconds");
    }

   
    
    public static void main(String[] args) throws Exception {
    	int c_i = 3;
    	int c_d = 2;
    	int c_r = 6;
    	
    	//use lorem ipsum generator for big tests
    	String s1 = "Mr. Sherlock Holmes+ who was usually very late in the mornings+ save upon those not infrequent occasions when he was up all night+ was seated at the breakfast table. I stood upon the hearth-rug and picked up the stick which our visitor had left behind him the night before. It was a fine+ thick piece of wood+ bulbous-headed+ of the sort which is known as a \"Penang lawyer.\" Just under the head was a broad silver band nearly an inch across. \"To James Mortimer+ M.R.C.S.+ from his friends of the C.C.H.+\" was engraved upon it+ with the date \"1884.\" It was just such a stick as the old-fashioned family practitioner used to carry -- dignified+ solid+ and reassuring.";
        String s2 = "Chapter I. Mr. Sherlock Holmes+ who was usually very late in the mornings+ save upon those not infrequent occasions when he was up all night+ was seated at the breakfast table. I stood upon the hearth-rug and picked up the walking stick which our visitor had left behind him the night before. It was a fine+ thick piece of wood+ of the sort which is known as a \"Penang lawyer.\" Just under the head was a broad silver band nearly an inch across. \"To James Mortimer+ M.R.C.S.+ from his friends of the C.C.H.+\" was engraved upon it+ with the date \"1984.\" It was just such a stick as the old-fashioned family practitioner used to carry -- dignified+ solid+ and reassuring. \"Well+ Watson+ what do you make of it?\"";
        
        s1 = "Duis varius odio dui, eu vulputate dui vehicula mollis. Morbi metus enim, finibus id lacus in, pretium mollis justo. Pellentesque gravida, est ut ultricies condimentum, nisl lacus egestas tellus, in ullamcorper nunc ex et nulla. Morbi iaculis vehicula pharetra. Pellentesque quis elit diam. Aenean eget feugiat felis. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Duis erat justo, vulputate et malesuada ut, blandit sed dui. Quisque sed convallis dolor. Praesent eu eleifend mauris. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Morbi tempor sapien id dignissim vehicula. Morbi eu metus ex.\n" + 
        		"\n" + 
        		"Nunc porta massa eget leo interdum posuere. Aenean vehicula, risus in blandit placerat, turpis metus lobortis felis, sed interdum mi lacus volutpat erat. Integer tincidunt, quam eu tempor lobortis, tortor purus pharetra lorem, sit amet lobortis tellus sem quis erat. Nulla finibus pulvinar pulvinar. Phasellus cursus sit amet nulla vitae vestibulum. Phasellus vitae turpis et lectus iaculis accumsan. Maecenas laoreet quam massa, vitae suscipit leo consequat a. Phasellus auctor mi vel ornare ullamcorper. Nullam eget iaculis eros, nec maximus nisl.\n" + 
        		"\n" + 
        		"Nunc condimentum sapien at magna interdum consectetur ac et est. In lacus nisl, fermentum id luctus vel, ultrices vel velit. Nullam leo sapien, dignissim nec mattis nec, tristique et ante. Aenean nec mollis nunc. Quisque tincidunt orci ac massa vulputate egestas. Maecenas efficitur, sem at porta dapibus, sem mi iaculis nisl, nec volutpat tellus erat id libero. Suspendisse venenatis turpis quis feugiat vulputate. Quisque sit amet nisi nec dui cursus scelerisque.\n" + 
        		"\n" + 
        		"Nam dignissim id est at tincidunt. Pellentesque quis magna sagittis, commodo sapien eget, tincidunt nunc. Aenean molestie sollicitudin neque. Curabitur et purus dolor. Sed condimentum maximus consequat. Curabitur eu semper ex. Morbi venenatis magna in tristique pharetra. Curabitur rhoncus bibendum nisi vel euismod. Vestibulum quis est rutrum, semper magna sed, aliquet nisl. Etiam rhoncus ligula eu ullamcorper elementum. Donec libero quam, vestibulum vel arcu nec, congue auctor lectus. Donec dignissim, massa eget sagittis feugiat, lacus tellus fringilla tortor, ac molestie enim ante sed arcu. Pellentesque quis porttitor sapien. Aliquam at ultrices lectus, sit amet imperdiet sem. Sed feugiat accumsan enim, in porta nisl blandit a. Quisque condimentum massa euismod enim consectetur, eu porta nunc gravida.\n" + 
        		"\n" + 
        		"Sed ut orci eleifend, suscipit lorem in, hendrerit lectus. Maecenas tincidunt eros justo, vel tempor elit lobortis vitae. Pellentesque mollis nunc id purus varius interdum. Nam sagittis ligula ac tellus pellentesque, vel egestas ex maximus. Sed ut elit orci. Pellentesque velit mi, posuere sit amet suscipit eu, malesuada vel purus. Sed ultrices scelerisque dignissim. Proin efficitur magna sapien. Nulla in interdum nunc.\n" + 
        		"\n" + 
        		"Nam ullamcorper ipsum non magna dapibus, nec iaculis libero semper. Duis tincidunt ex eget ligula placerat, quis porttitor mi hendrerit. Quisque a urna ut ipsum tristique feugiat. Nulla eget varius sem. Donec imperdiet a sem quis dictum. Integer erat libero, laoreet vehicula facilisis ac, pulvinar ut augue. Curabitur quis turpis finibus lorem vehicula pharetra. Nunc lacinia vulputate nisi, vitae facilisis leo pulvinar vitae. Donec vitae dui odio. Curabitur quis quam sed lacus euismod volutpat ac nec odio.\n" + 
        		"\n" + 
        		"Etiam feugiat tellus sed dapibus fringilla. Donec aliquam, odio ut dignissim semper, quam nisi consequat sem, mollis convallis lorem ligula quis odio. Aenean tristique, nunc sed commodo tincidunt, tortor nulla porta metus, non mattis mauris neque eu velit. Nullam ligula quam, suscipit in dictum ac, consectetur sed mi. Quisque euismod ex ac tortor feugiat, ac aliquet odio tristique. Etiam tellus arcu, accumsan quis luctus ac, dictum in lectus. Quisque id ipsum bibendum, feugiat diam ut, placerat ligula. Pellentesque ultricies lacinia nisl in maximus. Cras accumsan maximus lorem, non pretium enim vestibulum eu. Cras quis pulvinar tellus. Nullam mattis id massa sit amet consequat. Morbi a ante enim. Suspendisse potenti. Vestibulum cursus commodo felis, in pellentesque velit suscipit ac.\n" + 
        		"\n" + 
        		"Vivamus eget feugiat mi, id lacinia sem. In rutrum sit amet libero eget eleifend. Donec sit amet nulla posuere, vehicula tortor gravida, consectetur neque. Nunc luctus, est vel iaculis congue, velit elit fringilla orci, sed sollicitudin sem odio ut risus. Proin mollis mauris sit amet odio dapibus, eget tincidunt nulla egestas. Ut ultricies erat eu quam ultricies, a sollicitudin massa lobortis. Quisque sit amet mi quam. Nam convallis odio ut scelerisque laoreet. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Donec eu purus purus.\n" + 
        		"\n" + 
        		"Etiam ultrices, nulla eu venenatis elementum, leo erat venenatis massa, sed eleifend orci sem eget lorem. Pellentesque a eros tincidunt, scelerisque erat id, condimentum dui. Proin eu ipsum lacus. Vestibulum non nunc pulvinar, hendrerit arcu eget, tristique diam. Aenean felis massa, dictum eget justo vel, aliquet ultrices sapien. Morbi id fermentum nisl. Morbi quis tempus lacus.\n" + 
        		"\n" + 
        		"Duis ut ligula eget erat molestie laoreet ac eu metus. Aliquam eu imperdiet odio. Vivamus in mauris aliquam, consectetur nulla vitae, tempus nunc. Maecenas ullamcorper eget purus non vehicula. Proin eu dui magna. Etiam congue tristique ligula, et feugiat magna. Curabitur congue dui lacus, et aliquam neque scelerisque ac.\n" + 
        		"\n" + 
        		"Nam sed augue quam. Vivamus nunc ipsum, vestibulum vitae ex vitae, molestie tincidunt lectus. Maecenas in auctor quam. Sed dignissim, purus non mattis fringilla, purus tortor lacinia tellus, eget ornare tortor magna id augue. Maecenas ac tempus urna, nec eleifend lectus. Nam malesuada velit ut leo vulputate maximus. Cras consectetur metus vel viverra imperdiet. Nam mattis ullamcorper nisl at accumsan. Praesent venenatis laoreet gravida. Sed pharetra ipsum et mi suscipit, nec imperdiet ex elementum. Sed dolor mauris, vulputate sit amet augue eu, placerat gravida sapien. Cras ac ornare justo, id maximus tortor. Integer ornare leo quam, eget rhoncus diam ultrices egestas. Vestibulum nec rhoncus sapien, sed hendrerit justo. Proin luctus imperdiet nulla sit amet tempus. Cras vitae scelerisque risus, eu lacinia lacus.\n" + 
        		"\n" + 
        		"Cras eget bibendum nunc, ac molestie sapien. Aliquam tellus eros, efficitur ut turpis porta, cursus luctus magna. Vivamus placerat massa eros, quis pellentesque justo aliquet sed. Sed venenatis dui ac eros dictum hendrerit. Quisque malesuada tortor eget vehicula facilisis. Cras eleifend luctus lectus, eget facilisis leo pretium vel. Integer ac ex non ex rhoncus venenatis non quis est. Nam at nunc luctus, aliquet arcu vel, mattis sapien. Etiam eu mauris turpis.\n" + 
        		"\n" + 
        		"In at tortor sed orci gravida rutrum. Sed in suscipit elit. Vivamus eu varius enim. Maecenas dictum felis quis rutrum feugiat. Sed semper erat id massa gravida, ac convallis arcu auctor. Ut varius consequat ipsum, eget fringilla nisl. Sed non lacinia nisl.\n" + 
        		"\n" + 
        		"Etiam lobortis risus in gravida porta. Etiam tristique semper risus, vitae pellentesque urna aliquet eu. Nulla diam enim, dignissim vitae tincidunt ut, pretium vel diam. Praesent porta egestas felis, et luctus nibh sagittis non. Sed vel massa eget risus scelerisque venenatis. Quisque vestibulum erat sed ipsum rutrum eleifend ac id orci. Proin faucibus nunc eu turpis placerat tempor. Interdum et malesuada fames ac ante ipsum primis in faucibus. Mauris sagittis turpis a velit bibendum, at facilisis sapien sagittis.\n" + 
        		"\n" + 
        		"Nunc faucibus vel nulla non fringilla. Aliquam maximus elementum sollicitudin. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Nullam quam urna, condimentum eget egestas vitae, maximus vel magna. Donec eu ligula pulvinar tortor aliquam efficitur vel et magna. Suspendisse luctus eleifend interdum. Duis a velit dignissim, ultricies velit ac, convallis justo. Pellentesque malesuada, urna vel semper eleifend, mi sem pellentesque turpis, eget condimentum risus arcu ac metus. Proin tincidunt posuere sapien, eget congue metus posuere eget. Proin imperdiet sollicitudin elit, eu viverra ex semper at. Aenean ac volutpat orci. Ut volutpat molestie eros, ac porttitor purus congue at. Donec neque risus, cursus eu commodo faucibus, ullamcorper ac erat. Donec id nibh non turpis porta semper. Etiam efficitur vulputate orci et vestibulum. Fusce vel cursus ex.\n" + 
        		"\n" + 
        		"Aliquam a elit ligula. Cras at ipsum eu metus tempor pretium eget at sem. Aenean eget sapien ac lectus gravida pulvinar quis in tellus. Ut efficitur lacus a sapien dignissim accumsan. Nunc dictum faucibus metus. Praesent vel hendrerit sem. Cras a lectus congue, convallis tellus id, ornare libero. Nullam vitae lacus at turpis eleifend tristique. Etiam posuere tincidunt risus in viverra. Fusce risus odio, vehicula in rutrum id, blandit non dolor. Nulla facilisi. Morbi varius odio nec tempus porta. Praesent ut ligula blandit, malesuada nibh vitae, egestas ex. In molestie sodales mi, dictum sollicitudin nibh eleifend ut. Sed pellentesque massa in tellus ullamcorper consequat. Pellentesque dictum metus enim, ut efficitur odio aliquet sit amet.\n" + 
        		"\n" + 
        		"Maecenas at ante dignissim, faucibus nulla a, commodo velit. Proin sit amet ultricies sem. Etiam eu erat in ex interdum porttitor. Interdum et malesuada fames ac ante ipsum primis in faucibus. Nam quis lectus orci. Proin sed arcu ac ipsum finibus laoreet aliquam fermentum dui. In luctus felis vitae mi molestie, sed lacinia est fermentum. In non pretium nulla. Vivamus scelerisque sapien odio, nec ultrices erat tincidunt ac. In hac habitasse platea dictumst. Sed a ex eu ipsum venenatis viverra. Maecenas mollis tempor orci id elementum.\n" + 
        		"\n" + 
        		"Donec mi metus, pulvinar sed semper non, eleifend non lectus. Ut finibus, felis et bibendum accumsan, dui sapien varius sapien, quis euismod elit erat eget nunc. Praesent mollis elit non euismod pretium. Nulla ac erat sem. Lorem volutpat.";
        s2 = "Fusce ut justo mi. Pellentesque at posuere orci. Curabitur ac tempus odio, id mattis nibh. Nam a tortor vitae erat rhoncus porta. Ut dictum sodales purus sit amet volutpat. Maecenas id augue sit amet nisi rutrum pellentesque. Phasellus facilisis lobortis dolor, nec luctus leo congue eu. Sed gravida orci in metus luctus, id aliquam neque dictum. Quisque ut viverra elit. Etiam in nulla erat. Donec ante risus, efficitur sit amet arcu ac, ultrices semper orci. Nunc euismod, tortor et lobortis scelerisque, arcu est pretium massa, mattis tincidunt neque risus vel nibh. Sed fringilla ipsum mauris, nec euismod orci lobortis a. Vestibulum mi eros, aliquet ut urna ac, tincidunt suscipit orci. Cras a justo augue.\n" + 
        		"\n" + 
        		"Phasellus tincidunt facilisis malesuada. Suspendisse tincidunt, massa non venenatis condimentum, neque libero porta orci, vel facilisis ante nisl non dui. Proin et justo non arcu tincidunt sodales. Vestibulum sit amet ante tellus. Vestibulum consequat sit amet nisi id finibus. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Nullam ac ipsum condimentum, iaculis neque ac, scelerisque risus. Pellentesque elementum tellus diam, non vulputate magna fringilla quis. In euismod, nunc in auctor rutrum, neque augue fringilla massa, ut blandit turpis diam vitae odio. Nulla rutrum eros quis augue mollis, quis vehicula mi volutpat. Fusce tincidunt scelerisque venenatis. Donec facilisis consequat lorem ultrices tristique. Praesent quis elit aliquam urna mollis tincidunt.\n" + 
        		"\n" + 
        		"Aenean ornare eleifend magna, ut aliquet elit tempus eget. Sed quis odio pulvinar, commodo enim et, pellentesque ex. Sed tempus turpis felis, a molestie mi aliquet id. Nam non mollis risus. Mauris vitae erat mi. Vestibulum id felis quam. Pellentesque ornare lacus massa, sed auctor elit sagittis vel.\n" + 
        		"\n" + 
        		"Nulla dui ipsum, iaculis euismod blandit in, aliquet nec quam. Etiam faucibus dapibus justo, id luctus leo efficitur in. Cras vehicula egestas magna, non ultricies risus faucibus ut. Donec ut viverra enim, in commodo odio. Quisque laoreet nec felis ut ornare. Morbi a sapien ullamcorper, volutpat tortor vitae, gravida felis. Duis sapien odio, sollicitudin et diam id, feugiat congue quam. Aenean et nunc nisi.\n" + 
        		"\n" + 
        		"Maecenas sed nulla ac enim ullamcorper auctor eu in est. Etiam malesuada eleifend venenatis. Cras vulputate mi sed felis mattis pretium. Proin feugiat ex ligula, quis bibendum sem dignissim sit amet. Nullam vulputate, urna quis ullamcorper tincidunt, diam nunc sollicitudin nibh, id iaculis eros nulla nec dolor. Aliquam at pharetra eros. Praesent eu maximus libero. Maecenas lobortis lectus non dolor laoreet blandit. Vestibulum placerat finibus purus, et bibendum diam porttitor sed. Donec semper luctus facilisis. Quisque mi ex, egestas sit amet urna porta, vulputate aliquet orci. Pellentesque ut facilisis libero, et porta ipsum.\n" + 
        		"\n" + 
        		"Nam malesuada arcu justo, id efficitur sapien molestie nec. Aliquam eleifend quam et iaculis faucibus. Phasellus enim dui, tempor quis pretium et, tristique vel orci. Donec quis augue eget quam aliquet viverra. Fusce vehicula est elit, sit amet consectetur lectus suscipit a. Nam at risus non dolor finibus fermentum non non dolor. Donec a lorem ut dui commodo congue. Nulla dui tellus, mollis ac luctus at, volutpat suscipit tellus. Fusce a ornare risus, at mollis purus.\n" + 
        		"\n" + 
        		"Integer mi nunc, gravida non lorem congue, consectetur vulputate enim. Phasellus imperdiet lacinia sapien, eu porta urna venenatis eu. Quisque vel lorem cursus leo blandit malesuada. Cras eget urna rhoncus, fringilla ante in, sagittis metus. Sed sed iaculis nulla. Maecenas in nisl non orci maximus consectetur vel quis nunc. Morbi imperdiet nibh quis nunc lobortis, finibus dictum lacus tempus. Maecenas fermentum, est nec interdum rutrum, elit elit placerat urna, eget laoreet turpis metus vitae nunc. Proin rutrum mollis blandit.\n" + 
        		"\n" + 
        		"Nunc facilisis sem ipsum, quis bibendum velit dapibus id. Sed lacinia, dui ut faucibus imperdiet, lectus metus consequat mauris, id fringilla est tellus sit amet enim. Cras vehicula aliquet pretium. Cras tristique magna in lectus consectetur, eu posuere diam imperdiet. Aenean at ante ut turpis fermentum mollis. Sed finibus pellentesque tortor, at gravida felis placerat eu. Integer ultrices elementum iaculis. Mauris malesuada diam leo, id malesuada arcu bibendum quis. Vivamus aliquam vel massa sit amet sodales.\n" + 
        		"\n" + 
        		"Pellentesque accumsan ipsum id fermentum tempus. Donec tempus ipsum tortor, ut malesuada purus vulputate ac. Donec suscipit fringilla velit sed faucibus. Cras suscipit ipsum nibh, in imperdiet eros laoreet at. Aliquam lobortis elementum fermentum. Integer non lacus laoreet, accumsan velit sit amet, ultricies nisl. Integer et bibendum leo, sit amet malesuada diam. Phasellus non erat a est sollicitudin porttitor. Phasellus ultrices ligula vitae leo mattis lobortis. Fusce in mi tincidunt, porta purus vel, accumsan dolor.\n" + 
        		"\n" + 
        		"Nullam porttitor purus sit amet tortor elementum malesuada. Etiam ac magna sollicitudin, finibus augue in, mollis ex. Praesent semper dolor ac nibh tempor efficitur. Integer id lectus lobortis erat cursus posuere in sed libero. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec volutpat nunc sit amet venenatis aliquet. Ut nibh tortor, auctor nec tincidunt sit amet, fringilla vel risus. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Suspendisse pretium rutrum orci.\n" + 
        		"\n" + 
        		"Cras et porttitor ligula. In tempor non magna id finibus. Aenean orci diam, tempus sed lorem ut, imperdiet accumsan risus. Aliquam sit amet vulputate erat, in volutpat metus. Vestibulum a faucibus magna. Fusce vulputate vehicula odio vel sodales. Maecenas convallis ex fermentum mi euismod scelerisque. Donec eu tempor libero, vitae bibendum turpis. Morbi id tristique mi. Integer bibendum leo ut fringilla bibendum. Nulla facilisi. Donec gravida risus in pharetra gravida. Aliquam ligula ligula, feugiat et euismod vel, malesuada in purus. Integer quis dui eget lectus ullamcorper fringilla et et augue. Nullam sem purus, sodales et feugiat et, pellentesque dignissim sapien. Nunc cursus suscipit urna vitae eleifend.\n" + 
        		"\n" + 
        		"Etiam pellentesque sed ante interdum interdum. Nulla dictum, orci vitae malesuada iaculis, metus felis rhoncus quam, id facilisis metus ipsum ut eros. Sed a eleifend purus. Nunc eget ultricies libero, nec euismod eros. Praesent libero neque, pulvinar in leo id, porttitor imperdiet dolor. Suspendisse urna tortor, fermentum sit amet eleifend vitae, viverra non justo. Praesent mauris leo, aliquam quis lacinia at, sagittis eget velit. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed est erat, molestie eget libero vitae, vestibulum placerat tellus. Aliquam pretium venenatis pharetra. Morbi leo metus, imperdiet a semper luctus, ultrices ut risus.\n" + 
        		"\n" + 
        		"Maecenas malesuada, purus id finibus auctor, risus tortor mollis orci, eget viverra leo est sit amet est. Nullam eu lacinia diam, a dignissim massa. Vivamus nec ipsum quis nulla molestie pellentesque sit amet non leo. Duis gravida non dui efficitur volutpat. Sed id ornare ex. Sed laoreet pharetra lorem, id euismod nisi. Donec eu odio ullamcorper, tempus libero vel, aliquet justo. Proin luctus, urna in faucibus scelerisque, diam nunc facilisis enim, sed sollicitudin nisl eros at nulla. Sed lobortis tortor vitae tortor tristique varius. Pellentesque tempus eros id dui varius, eu aliquet quam ultricies.\n" + 
        		"\n" + 
        		"Morbi lacinia mauris nec lectus lacinia, placerat tempus diam efficitur. Ut vitae augue quis massa porta egestas. Etiam a ullamcorper lacus. Curabitur blandit sodales porta. Aenean commodo ex convallis neque porttitor posuere. Pellentesque dictum suscipit elit a accumsan. Nam tincidunt mauris fringilla tellus pulvinar, et mollis ex imperdiet. Aliquam iaculis tincidunt risus, volutpat efficitur massa facilisis nec.\n" + 
        		"\n" + 
        		"Phasellus eleifend, sapien id finibus fringilla, orci leo fringilla dolor, id scelerisque leo est eget eros. Nulla a lorem leo. Curabitur interdum rhoncus interdum. Suspendisse tempus sed nisi vitae vehicula. Nullam fringilla nulla et enim auctor efficitur. Suspendisse ac consequat sem. Integer id sagittis sapien. Sed suscipit ut ipsum non tincidunt. Integer non placerat neque.\n" + 
        		"\n" + 
        		"Nulla sodales orci id diam scelerisque, nec efficitur arcu tempus. Aliquam tempor blandit felis, ut ullamcorper mauris semper vel. Suspendisse potenti. In mattis rutrum vestibulum. Aenean at justo sit amet quam hendrerit efficitur. Morbi tincidunt nunc sit amet mi ullamcorper pharetra. Donec vel lectus malesuada, ultricies nibh non, dignissim velit. Nulla pharetra ante quis commodo porta. Proin sit amet condimentum diam. Nullam sed finibus eros, non rutrum enim. Nam convallis id lorem non porta. Etiam laoreet aliquet felis. Nulla sed vehicula turpis, eget vulputate ex. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Praesent eget ipsum eget enim imperdiet malesuada eu vel sem. Nullam consequat condimentum libero, et pellentesque felis maximus et.\n" + 
        		"\n" + 
        		"Morbi tempor est vitae libero faucibus, vel sodales sem scelerisque. Vivamus vel congue tellus. Aliquam ac tempor nunc, sed feugiat urna. Aliquam sagittis rutrum dolor eget fermentum. Proin porta tincidunt lorem vel convallis. Cras venenatis, mi sit amet aliquet vulputate, dolor mauris suscipit tellus, a maximus risus massa sit amet justo. Vestibulum dictum diam augue, a ultrices nibh posuere quis. Praesent consequat est non enim dignissim rutrum. Maecenas ultrices tempor nisl. Interdum et malesuada fames ac ante ipsum primis in faucibus. Duis auctor vel augue ut commodo. Nulla facilisi. Vestibulum tincidunt magna id ex cursus placerat. Cras ante mauris, condimentum at fermentum ut, luctus vitae arcu.\n" + 
        		"\n" + 
        		"Duis et consequat erat, at accumsan nisi. Nulla eu dictum augue, non rhoncus risus. Cras velit odio, maximus ac tempus non, pretium id lacus. Duis quis eros sed nibh semper ornare. Praesent elit erat, facilisis ac scelerisque sed, elementum tristique leo. Sed augue amet.";
        
        s1 = s1.replace(",", "");
        s2 = s2.replace(",", "");
    	
        //testInit(s1.length(), s2.length(), c_i, c_d, c_r);
    	//testGetEditDistanceDP(c_i, c_d, c_r, s1, s2);
    	//testGetMinimalEditSequence(c_i, c_d, c_r, s1, s2);
    	testBIGEditDistance(c_i, c_d, c_r, s1, s2);
	}
	
};
