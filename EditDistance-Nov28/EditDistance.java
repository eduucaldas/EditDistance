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
        	//display(s1, s2, i, j);
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
        	dist[i][0] = i*c_d;
        	prec[i][0] = new Pair(i-1,0);
        	son[i-1][n] = new Pair(i,n);
        }
        for(int i = 1; i < n+1; i++) {
        	dist[0][i] = i*c_i;
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
    	boolean validSequence = Main.preValidateSequence(ls, s1, s2);
    	System.out.println("Is it a valid Sequence ? " + validSequence);
    	display(ls);
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
    	
    	boolean validSequence = Main.preValidateSequence(ls, s1, s2);
    	boolean optimalSequence = optimalSequence(ls, dist[s1.length()][s2.length()], eD.c_i, eD.c_d, eD.c_r);
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
    	tictoc = (System.nanoTime()/1000000 - tictoc);
    	System.out.println("This test had m = " + s1.length() + ", n = " + s2.length() + "\n Second part took " + tictoc + " milliseconds");
    	
    	//System.out.println("Transforming "+s1+" to "+s2+" with (c_i,c_d,c_r) = (" + c_i + "," + c_d + "," + c_r + ")");
    	//display(dist);
    	//System.out.println("Cost = "+dist[s1.length()][s2.length()]);
    	//System.out.println("Minimal edits from "+s1+" to "+s2+" with (c_i,c_d,c_r) = (" + c_i + "," + c_d + "," + c_r + "):");
    	//display(ls);
    	
    	boolean validSequence = Main.preValidateSequence(ls, s1, s2);
    	boolean optimalSequence = optimalSequence(ls, dist[s1.length()][s2.length()], eD.c_i, eD.c_d, eD.c_r);
    	System.out.println("Is it a valid Sequence ? " + validSequence);
    	System.out.println("Is it the optimal Sequence ? " + optimalSequence);
    	System.out.println("This test had m = " + s1.length() + ", n = " + s2.length() + "\n It took " + (System.nanoTime() - start)/1000000 + " milliseconds");
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

    public static boolean optimalSequence(List<String> es, int cost, int c_i, int c_d, int c_r) throws Exception{
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
    
    public static void main(String[] args) throws Exception {
    	int c_i = 3;
    	int c_d = 2;
    	int c_r = 1;
    	String s1 = "Phasellus aliquam metus quam, sed congue tortor malesuada a. Cras non lectus egestas, aliquam massa a, mattis mauris. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Etiam at velit hendrerit, tristique nunc eu, vestibulum ex. Phasellus et faucibus felis. In laoreet lorem a ligula volutpat finibus. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec vulputate justo eu dui condimentum dignissim. Fusce sit amet bibendum odio.\n" + 
    			"\n" + 
    			"Quisque egestas fermentum pulvinar. Sed eleifend at nisi ut consectetur. Pellentesque scelerisque quam sed urna scelerisque mattis ut aliquam odio. Maecenas aliquam quam nec erat tristique feugiat. Aenean in faucibus nisi, vel tincidunt enim. Fusce vel sapien ac sem finibus placerat. Nulla vehicula ipsum eu nibh sollicitudin maximus. Nunc dignissim erat sed felis iaculis, at condimentum diam fermentum. Sed sodales efficitur ligula, in porttitor sem ultricies id. Praesent a urna a erat ullamcorper dapibus. Nunc fringilla metus ut volutpat egestas. Aliquam molestie faucibus rhoncus. Vivamus id arcu a odio scelerisque hendrerit eu id orci. Ut efficitur mauris neque, sit amet suscipit metus sodales lacinia. Phasellus neque nisl, condimentum a elit quis, egestas ultrices augue.\n" + 
    			"\n" + 
    			"Proin at quam eget dolor vehicula sollicitudin eu quis nisi. Sed tempus pretium ex, sed mollis dui sagittis eu. Suspendisse vulputate imperdiet risus, sed porttitor tortor congue quis. Quisque est ipsum, placerat sagittis ipsum at, facilisis condimentum ante. Nunc tincidunt tincidunt dolor, in suscipit metus. Donec sit amet justo non quam rutrum aliquet. Curabitur gravida dapibus metus, ac euismod turpis lacinia in. Etiam auctor ipsum porttitor mi sollicitudin condimentum. Ut id placerat metus, a tempor ante. Sed facilisis egestas est nec tincidunt. Quisque in malesuada urna. Nunc aliquet tortor quis ante accumsan, at porta eros auctor.\n" + 
    			"\n" + 
    			"Nullam vulputate elementum metus, eget lacinia quam consectetur ut. Etiam id ex id mi blandit lacinia. Quisque imperdiet risus velit, vitae blandit arcu lacinia et. Phasellus aliquam eleifend felis a mollis. Praesent a condimentum augue, eget congue ligula. Suspendisse mollis quam eu velit interdum sagittis. Sed mattis condimentum vulputate.\n" + 
    			"\n" + 
    			"Vestibulum maximus quam massa, ac semper diam dictum ac. Aenean augue velit, ultricies sed tortor nec, condimentum aliquam nunc. Vivamus faucibus, arcu vitae aliquet mollis, diam tellus commodo quam, ac blandit lorem dolor eu metus. Suspendisse et dignissim massa, vitae laoreet purus. Fusce libero ex, volutpat vel tortor ut, semper commodo felis. Sed in magna porta, rutrum lorem eu, rutrum sapien. Suspendisse vehicula enim quis congue lobortis. Sed porttitor tempus ligula. Vestibulum id lacus vel ipsum interdum pellentesque. Suspendisse dictum suscipit varius. Nulla eget lectus malesuada, finibus neque malesuada, iaculis odio. Fusce bibendum porta risus, vel pellentesque nunc egestas vitae. Nam eu porta magna. Mauris ullamcorper, massa sed iaculis facilisis, magna ipsum vestibulum urna, a interdum arcu sapien vitae ante. Suspendisse luctus dapibus metus, consectetur vulputate nibh accumsan nec.\n" + 
    			"\n" + 
    			"Proin purus augue, tristique et elementum non, lacinia nec arcu. Sed tempus diam sit amet sagittis commodo. Integer orci lacus, viverra hendrerit ipsum ut, facilisis vulputate massa. Etiam quis nulla non turpis lobortis elementum. Maecenas vel purus non dolor rhoncus mattis nec at orci. Vivamus quis consequat nunc. Fusce orci magna, vehicula eu ultricies ac, lacinia in arcu. Nullam dignissim eros in eros lacinia condimentum.\n" + 
    			"\n" + 
    			"Nulla iaculis massa lorem. Ut auctor, metus vel placerat scelerisque, ligula quam sollicitudin orci, vel pulvinar libero nibh non tellus. Aliquam tempus odio id purus lacinia, a tempus quam tempus. Suspendisse sodales, est a accumsan pulvinar, lectus diam tristique metus, quis aliquam lorem arcu eu mauris. Suspendisse blandit tellus elit, ut feugiat neque malesuada sit amet. Cras sit amet eleifend nulla. Nullam molestie felis quis nibh faucibus, eu maximus risus consequat. Integer nec ante libero. Vestibulum ac lectus ut ligula rhoncus pulvinar id in ante. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Aenean mattis arcu purus, at dictum libero varius non. Aliquam erat volutpat. Donec sit amet dapibus lectus. Sed non suscipit leo. Praesent in enim fermentum, convallis velit at, ullamcorper sapien.\n" + 
    			"\n" + 
    			"Nulla facilisi. Fusce placerat auctor hendrerit. Morbi at molestie nisl. Donec rhoncus sed dui vulputate porttitor. Integer et pellentesque odio, eu dapibus augue. Morbi eu tortor condimentum, blandit magna et, pretium leo. Vivamus semper vel justo volutpat porta. Maecenas ut tellus id ligula lobortis eleifend at nec ligula. Nulla in tempor metus. Pellentesque rhoncus molestie malesuada. Fusce placerat ornare libero sed lacinia. Phasellus non dictum erat, in pharetra tellus. Donec euismod ultricies mi, nec dapibus lorem fringilla eget. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Aliquam erat volutpat.\n" + 
    			"\n" + 
    			"Sed ultrices euismod sem. Mauris volutpat ligula eget egestas aliquam. Aliquam mattis eu nisi a dapibus. Ut neque est, pulvinar sit amet tristique non, finibus tincidunt est. Phasellus aliquam sed ante nec mattis. Integer luctus elit in commodo varius. In pharetra est vitae dolor malesuada euismod. Nulla a scelerisque purus, et condimentum tortor. Donec eu suscipit lorem. Aliquam in molestie turpis, aliquet ultrices est. Ut molestie bibendum diam, ut interdum metus volutpat id. Praesent quis suscipit erat, eu orci aliquam.";
        String s2 = "Sed accumsan, tortor sed lobortis lobortis, justo ligula aliquam diam, at elementum velit ligula sit amet libero. Ut pellentesque, enim id efficitur commodo, ante ligula facilisis purus, id aliquam mi tortor vel metus. Donec sed laoreet mauris. Pellentesque a lobortis lacus, sed porta ex. Pellentesque volutpat lacinia velit et mollis. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Aliquam faucibus, dui sit amet dignissim auctor, mi nisi cursus ante, vitae dapibus massa erat fermentum ante. Ut ipsum augue, fermentum vitae dignissim a, sagittis ut nibh.\n" + 
        		"\n" + 
        		"Sed vehicula quam ut est finibus porttitor. Interdum et malesuada fames ac ante ipsum primis in faucibus. Fusce non arcu quis sem mollis pharetra et ac est. Praesent tincidunt dui in eros venenatis suscipit. Duis egestas odio sed vehicula finibus. Etiam euismod, erat ut facilisis facilisis, nulla turpis porta ligula, nec vulputate arcu ligula vel ligula. Aenean varius quam dui, sit amet consectetur tellus consectetur quis.\n" + 
        		"\n" + 
        		"Vestibulum at erat at lectus dapibus interdum. Nam porttitor risus a fringilla feugiat. Sed id mattis tellus. Phasellus tincidunt, nunc at fringilla accumsan, nulla quam egestas elit, eget placerat nisi ligula non nibh. Donec in tortor sem. Curabitur in faucibus erat, nec molestie dolor. Suspendisse potenti. Nam consequat lectus ac vulputate efficitur. Nulla eget libero ex. Fusce suscipit neque in diam aliquet, in tristique odio vehicula. Nullam non facilisis leo.\n" + 
        		"\n" + 
        		"Morbi sed urna ornare, malesuada est vitae, finibus lorem. Vivamus eu malesuada ex. Mauris ut aliquet sapien, sit amet aliquet lectus. Donec suscipit vulputate purus ultricies suscipit. Curabitur nec facilisis ipsum, et varius ligula. Ut ultricies orci nisi, id volutpat nulla pretium quis. Sed non consectetur nibh, viverra facilisis neque. Quisque rhoncus est a turpis laoreet scelerisque. Integer in justo et lectus pulvinar tincidunt. Sed sit amet elit sed sapien ultrices gravida.\n" + 
        		"\n" + 
        		"Proin luctus id nisl a pharetra. Suspendisse quis augue sit amet dolor varius ullamcorper a quis mauris. Nullam mi turpis, facilisis sed vestibulum nec, condimentum nec est. Nulla a gravida nibh, euismod dictum sem. Aliquam a sapien in orci eleifend ornare. Donec at auctor est, in sollicitudin arcu. Vivamus vitae libero in eros elementum gravida vitae non mi. Phasellus lacinia mi sed tortor cursus pulvinar. Donec ut lacus at dui lobortis aliquam quis eu nulla. Nunc a dolor nec nulla porta vulputate a eu tortor. Aliquam tristique neque lacus, ac tempor mi sollicitudin eget. Sed tempus posuere iaculis. Nullam ligula orci, fermentum vel ornare non, finibus tincidunt libero.\n" + 
        		"\n" + 
        		"Cras commodo nisi non venenatis cursus. Ut vestibulum semper mattis. Vivamus sit amet lacus vehicula, molestie lacus eget, eleifend nunc. Aliquam porttitor quis turpis ut volutpat. Donec feugiat vestibulum lacus, in consequat ante tincidunt in. Aliquam in nunc non augue aliquam vehicula vitae id felis. Etiam neque mauris, feugiat et tristique at, fermentum nec nibh. Nullam congue ipsum at risus malesuada eleifend. Duis at ipsum iaculis, luctus lorem porta, auctor ex. Nullam finibus, dolor sit amet hendrerit sodales, urna sem placerat turpis, at tincidunt mauris sem eu purus. Aliquam erat volutpat.\n" + 
        		"\n" + 
        		"Pellentesque pulvinar a lacus quis cursus. Nullam sem orci, sodales sit amet iaculis at, lacinia vitae mauris. Aenean iaculis pulvinar justo, tempor euismod sapien euismod quis. Phasellus condimentum velit vel nisi varius ullamcorper. Nunc euismod urna tellus, et finibus eros dictum id. Nam nec quam dolor. Ut at nisl eu dui ultricies convallis. Proin eu tempus neque, ut dapibus arcu. Sed elementum cursus nulla a tempus. Nullam nulla elit, mattis ac mauris dictum, varius volutpat tellus. Phasellus rutrum consectetur luctus. Praesent placerat, enim a eleifend lobortis, nisl magna placerat enim, eu mollis magna urna nec risus.\n" + 
        		"\n" + 
        		"Vivamus a lacus dolor. Praesent id libero vitae dolor pulvinar suscipit. Donec porta ex ex, vitae fringilla nibh tincidunt id. Aliquam porta aliquet diam. Proin quis commodo quam, quis efficitur nunc. Aenean sit amet neque pretium, facilisis ex vel, iaculis tortor. Vivamus condimentum gravida elit eu fringilla. Nulla interdum sed purus ut sollicitudin. Duis quam nulla, laoreet vel luctus id, faucibus sed diam. Quisque ornare dui quis tortor scelerisque aliquam.\n" + 
        		"\n" + 
        		"Ut ullamcorper eros vitae purus mollis, eget faucibus mi auctor. Aliquam erat volutpat. Suspendisse accumsan dui odio, non aliquet nisl pulvinar in. Sed molestie ex et est dictum molestie. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Integer vitae est et orci placerat molestie. Donec vitae rutrum est. Fusce sagittis, orci eget ullamcorper placerat, erat nulla dignissim nisl, sed rhoncus ex amet.";
        s1 = s1.replace(",", "");
        s2 = s2.replace(",", "");
    	//testInit(s1.length(), s2.length(), c_i, c_d, c_r);
    	//testGetEditDistanceDP(c_i, c_d, c_r, s1, s2);
    	//testGetMinimalEditSequence(c_i, c_d, c_r, s1, s2);
    	testBIGEditDistance(c_i, c_d, c_r, s1, s2);
	}
	
};
