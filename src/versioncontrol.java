/*import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.*;
public class Board extends JPanel implements KeyListener, ActionListener, MouseListener, Runnable{
	Rectangle[][] background = new Rectangle[4][4];
	Rectangle[][] squares = new Rectangle[4][4];
	Square[][] numbers = new Square[4][4];
	String[][] trackerboard = new String[4][4];
	Color backgroundcolor = new Color(150,138,137); //color for background board
	Color squarecolor = new Color(191,172,170); //color for background squares 
	Random ran = new Random(); //random number generator
	boolean gameover; //for when game is over
	int availablespace; //tracker of available space to generate the next random number 
	Timer tm = new Timer(5,this);

	boolean slideright = false; //booleans for when you press a button 
	boolean slideleft = false;
	boolean slideup = false;
	boolean slidedown = false;
	int counter =0; //counter to only let the slides run 3 times to save processing power and move next turn 
	
	boolean once = true;
	boolean turnfinished = false; 
	boolean alreadymerged = false;
	boolean merged = false; 
	int[] mergecounter = new int[4]; // this is to prevent numbers from merging too much
	Square shiphtur;
	ArrayList<Square> movesquares = new ArrayList<Square>(); 
	public Board(){
		addKeyListener(this);
		addMouseListener(this);
		setFocusable(true);
		requestFocus();
		
		for(int row =0; row<4; row++){ //initialize background squares
			for (int col = 0; col<4; col++){
				squares[row][col] = new Rectangle(100+col*200, 100+row*200, 180, 180);
				//this is actually switched, column is first, then row, similar to the chess board
			}
		}
		
		for(int row =0;row<4;row++){ //initialize tracking board 
			for(int col=0; col<4; col++){
				trackerboard[row][col] = null;
			}
		}
		trackerboard[0][0] = "2";
		//trackerboard[0][1] = "2";

		numbers[0][0] = makeNewSquare(0,0);
		//numbers[0][1] = makeNewSquare(0,1);
		
		//numbers[0][2] = makeNewSquare(0,2);
		//numbers[0][3] = makeNewSquare(0,3);
		
		//trackerboard[0][2] = "2";
		//trackerboard[0][3] = "2";

		availablespace = 16 - 2; // need to somehow make this dynamic based on the number of filled squares
		trackerboard[1][0] = "4";
		numbers[1][0] = makeNewSquare(1,0);
		String str1 = "hi";
		String str2 = "hi";
		System.out.println(str1==str2); //RETURNS FALSE WTF
		
	}
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		tm.start(); //start timer 
		Graphics2D g2d = (Graphics2D) g;
		//making background
		g2d.setColor(backgroundcolor);
		g2d.fillRect(75,75,830,830);
		for(int row =0; row<4; row++){
			for (int col = 0; col<4; col++){
				g2d.setColor(squarecolor);
				g2d.fill(squares[row][col]);
			}
		}
		for(int row = 0; row<4;row++){
			for (int col=0; col <4; col++){
				if(trackerboard[row][col]!=null){ //if trackerboard is not empty, fill a square class (numbers rectangle)
					//on game board 
					g2d.setColor(new Color(217,198,178));
					g2d.fill(numbers[row][col].getRect());
					g2d.setFont(new Font("arial", Font.BOLD,80));
					Rectangle tempsquare = numbers[row][col].getRect();
					g2d.setColor(Color.BLACK);
					g2d.drawString(trackerboard[row][col],(int) (tempsquare.getX()+tempsquare.getWidth()/2-20),
							(int) (tempsquare.getY()+tempsquare.getWidth()/2+25));
					//draw the equivalent value of the trackerboard stored value 
				}
			}
		}

		
	}
	public void resetMergeCounter(){
		for (int x =0; x<4; x++){
			mergecounter[x] =0;
		}
	}
	public Square makeNewSquare(int row, int col){
		Rectangle tempsquare = squares[row][col];
		Square newSq = new Square(tempsquare.getX(),tempsquare.getY(),tempsquare.getWidth(),tempsquare.getHeight());
		newSq.setRowCol(row, col); //store their row and column for easier access
		return newSq;
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		String direction ="";
		if(slideright){
			//numbers[0][0].slideright(300);
			//resetMergeCounter();
			
			direction = "right";
			counter++;
		}
		if(slideleft){
			resetMergeCounter();
			getSlideLeft();
			counter++;
		}
		if(slidedown){
			resetMergeCounter();
			getSlideDown();
			counter++;
		}
		if(slideup){
			resetMergeCounter();
			getSlideUp();
			counter++;
		}
		if(counter==3){ //need this block of code FIRST to make turn finished = true, it won't work if both are just one block of
			//code
			slidedown = false;
			slideup = false;
			slideright=false;
			slideleft =false;
			//mergeEnd(direction);
			turnfinished = true;
			//System.out.println(counter);
		}
		if(turnfinished){ //THEN this block can run
			//generateNextNumber();
			turnfinished =false;
			counter = 0;
			resetMerge();

		}
		repaint();
	}
	public void resetMerge(){
		merged = false;
		alreadymerged = false;
	}
	public void mergeEnd(String direction){
		if (direction.equals("right")){
			for(int row =0; row<4;row++){
				if(trackerboard[row][2]!=null && trackerboard[row][2]==trackerboard[row][3]){
					mergeNumbers(numbers[row][2],numbers[row][3],direction);
				}
				if(trackerboard[row][1]!=null && trackerboard[row][1]==trackerboard[row][2]){
					mergeNumbers(numbers[row][1],numbers[row][2],direction);
				}
				if(trackerboard[row][0]!=null && trackerboard[row][0]==trackerboard[row][1]){
					mergeNumbers(numbers[row][0],numbers[row][1],direction);
				}
			}
		}
		else if(direction.equals("left")){
			for(int row =0; row<4;row++){
				if(trackerboard[row][1]!=null && trackerboard[row][1]==trackerboard[row][0]){
					//merge
				}
				if(trackerboard[row][2]!=null && trackerboard[row][2]==trackerboard[row][1]){
					//merge
				}
				if(trackerboard[row][3]!=null && trackerboard[row][3]==trackerboard[row][2]){
					//merge
				}
			}
		}
	}
	public void getSlideRight(){		

		for(int num = 0; num <4;num++){ //num is length of rows or number of times this sliding needs to be run 
			//this for loop allows it to run for the entire board instead of just the first row 
			for(int col = 2; col>=0;col--){
				if(trackerboard[num][col]!=null && trackerboard[num][col+1]==null){ //for moving 
					if(once){
						numbers[0][col].slideright(100);
						once= false;
					}
					System.out.println("first");
					System.out.println(numbers[num][col]);
					movesquares.add(numbers[num][col]);
					try{
						Thread.sleep((long) 0.2); //this thread makes animation go slow
					}catch(InterruptedException e){
						
					} //maybe don't even use this because it makes animation super smooth, no still need it to work
					//wait if I set it to 0.2, it won't work for the next one, the next tile in the next row will be slower
					//eg 2 in 1st row, 4 in second row, 4 will be slightly behind....
					
					
					trackerboard[num][col+1]=trackerboard[num][col];
					trackerboard[num][col]=null;
					numbers[num][col+1]=numbers[num][col];
					numbers[num][col]=null;
					numbers[num][col+1].setRowCol(num, col+1); 
					
					
					//NEED THIS LINE OF CODE TO UPDATE THE ROW AND COL
					//OR ELSE THE MERGING WON'T WORK BECAUSE it takes the last known row and col, which is now null since
					//the values have swapped places
					for(int row=0;row<4;row++){
						for(int c=0; c<4; c++){
							System.out.print("["+trackerboard[row][c]+"]");
						}
						System.out.println();
					}
				}
				else if(trackerboard[num][col]!=null && trackerboard[num][col].equals(trackerboard[num][col+1]) 
						&& !alreadymerged){ 
					//if they are null it will work lol
					//thus add conditional that they are equal but not null 
					System.out.println("COL NUMBER: " + col + " row number " + num);
					
					for(int row=0;row<4;row++){
						for(int c=0; c<4; c++){
							System.out.print("["+trackerboard[row][c]+"]");
						}
						System.out.println();
					}
					mergeNumbers(numbers[num][col],numbers[num][col+1],"right");
					System.out.println("MERGING");
					mergecounter[num]++;
					if(mergecounter[num]>=2){
						merged = true;
					}
				}

			}
		}
		//this thread is for the animations 
		Thread t = new Thread(this); // should start thread outside of for loop, this way it can iterate through entire thing
		//before we start moving stuff around
		t.start(); 
		if(merged){
			alreadymerged = true;
		}
	}
	public void mergeNumbers(Square sq1, Square sq2, String direction){
		System.out.println("Merge" + sq2.getRow()+" "+sq2.getCol());
		System.out.println("square 1 = " +trackerboard[sq1.getRow()][sq1.getCol()]+" square 2 = " 
				+ trackerboard[sq2.getRow()][sq2.getCol()]);
		Thread t = new Thread(sq1);
		if(direction.equals("up")){
			sq1.slideup();

		}else if (direction.equals("down")){
			sq1.slidedown();
		}
		else if (direction.equals("right")){
			sq1.slideright();
		}
		else if (direction.equals("left")){
			sq1.slideleft();
		}
		
		t.start();
		try{
			Thread.sleep(16);
		}catch(InterruptedException e){
			
		}
		sq1.resetCounter();
		numbers[sq1.getRow()][sq1.getCol()] = null;
		trackerboard[sq1.getRow()][sq1.getCol()] = null;
		
		System.out.println(trackerboard[sq2.getRow()][sq2.getCol()]);
		int previousvalue = Integer.parseInt(trackerboard[sq2.getRow()][sq2.getCol()]);
		trackerboard[sq2.getRow()][sq2.getCol()] = new String(previousvalue*2+"");


	}
	public void getSlideDown(){	
		for(int num = 0; num <4; num++){
			for(int row = 2; row>=0;row--){
				if(trackerboard[row][num]!=null && trackerboard[row+1][num]==null){
					if(once){
						numbers[0][col].slideright(100);
						once= false;
					}
					Thread t = new Thread(numbers[row][num]);
					numbers[row][num].slidedown();
					t.start();
					try{
						Thread.sleep(16);
					}catch(InterruptedException e){
						
					}
					numbers[row][num].resetCounter();	
					trackerboard[row+1][num]=trackerboard[row][num];
					trackerboard[row][num]=null;
					numbers[row+1][num]=numbers[row][num];
					numbers[row][num]=null;
					numbers[row+1][num].setRowCol(row+1, num); //need this for storing reasons and merging
					for(int r=0;r<4;r++){
						for(int c=0; c<4; c++){
							System.out.print("["+trackerboard[r][c]+"]");
						}
						System.out.println();
					}
				}
				else if(trackerboard[row][num]!=null && trackerboard[row][num].equals(trackerboard[row+1][num])){ 
					//if they are null it will work lol
					//thus add conditional that they are equal but not null 					
					//mergeNumbers(numbers[row][num],numbers[row+1][num]);
				}
			}
		}
	}
	public void getSlideLeft(){		
		for(int num = 0; num<4;num++){
			for(int col = 1; col<=3;col++){
				if(trackerboard[num][col]!=null && trackerboard[num][col-1]==null){
					if(once){
						numbers[0][col].slideright(100);
						once= false;
					}
					Thread t = new Thread(numbers[num][col]);
					numbers[num][col].slideleft();
					t.start();
					try{
						Thread.sleep(16);
					}catch(InterruptedException e){
						
					}
					numbers[num][col].resetCounter();
	
					trackerboard[num][col-1]=trackerboard[num][col];
					trackerboard[num][col]=null;
					numbers[num][col-1]=numbers[num][col];
					numbers[num][col]=null;
					numbers[num][col-1].setRowCol(num, col-1);
					for(int row=0;row<4;row++){
						for(int c=0; c<4; c++){
							System.out.print("["+trackerboard[row][c]+"]");
						}
						System.out.println();
					}
				}
				else if(trackerboard[num][col]!=null && trackerboard[num][col].equals(trackerboard[num][col-1])){ 
					//if they are null it will work lol
					//thus add conditional that they are equal but not null 					
					//mergeNumbers(numbers[num][col],numbers[num][col-1]);
				}
			}
		}
	}
	public void getSlideUp(){		
		for(int num = 0; num<4;num++){
			for(int row = 1; row<=3;row++){
				if(trackerboard[row][num]!=null && trackerboard[row-1][num]==null){
					if(once){
						numbers[0][col].slideright(100);
						once= false;
					}
					Thread t = new Thread(numbers[row][num]);
					numbers[row][num].slideup();
					t.start();
					try{
						Thread.sleep(16);
					}catch(InterruptedException e){
						
					}
					numbers[row][num].resetCounter();
	
					trackerboard[row-1][num]=trackerboard[row][num];
					trackerboard[row][num]=null;
					numbers[row-1][num]=numbers[row][num];
					numbers[row][num]=null;
					numbers[row-1][num].setRowCol(row-1, num);
					
					for(int r=0;r<4;r++){
						for(int c=0; c<4; c++){
							System.out.print("["+trackerboard[row][c]+"]");
						}
						System.out.println();
					}
				}else if(trackerboard[row][num]!=null && trackerboard[row][num].equals(trackerboard[row-1][num])){ 
					//if they are null it will work lol
					//thus add conditional that they are equal but not null 					
					//mergeNumbers(numbers[row][num],numbers[row-1][num]);
				}
			}
		}
	}
	public void generateNextNumber(){
		while(true){
			int randorow = ran.nextInt(4);
			int randocol = ran.nextInt(4);// can't use available spaces because random numbers will just keep generating
			//random nubmers while the number of tries keeps going down
			if(trackerboard[randorow][randocol]==null){
				trackerboard[randorow][randocol] = "2";
				System.out.println("FILLING SQUARE ROW= "+randorow + " COL= "+randocol);
				Rectangle tempsq = squares[randorow][randocol];
				numbers[randorow][randocol] = new Square(tempsq.getX(),tempsq.getY(),tempsq.getWidth(),tempsq.getHeight());
				availablespace--;
				numbers[randorow][randocol].setRowCol(randorow, randocol);
				break;
			}else{
				if(availablespace==0){
					System.out.println("NOT NULL");
					break;
				}
			}
		}
	}
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		int key = e.getKeyCode();
		if (key==KeyEvent.VK_SPACE){
			for(int row=0;row<4;row++){
				for(int col=0; col<4; col++){
					System.out.print("["+trackerboard[row][col]+"]");
				}
				System.out.println();
			}
			for(int row=0;row<4;row++){
				for(int col=0; col<4; col++){
					System.out.print("["+numbers[row][col]+"]");
				}
				System.out.println();
			}

		}
		if (key==KeyEvent.VK_RIGHT){
			slideright=true;
			slideleft=false;
			slidedown = false;
			slideup = false;
			getSlideRight();

		}
		if (key ==KeyEvent.VK_LEFT){
			slideleft=true;
			slideright = false;
			slidedown = false;
			slideup = false;
		}
		if (key ==KeyEvent.VK_DOWN){
			slidedown=true;
			slideup = false;
			slideright=false;
			slideleft = false;
		}
		if (key ==KeyEvent.VK_UP){
			slideup=true;
			slidedown = false;
			slideright = false;
			slideleft = false; 
		}
		if (key==KeyEvent.VK_UP || key==KeyEvent.VK_DOWN || key==KeyEvent.VK_LEFT || key==KeyEvent.VK_RIGHT){
			try{
				Thread.sleep(5);
			}catch(InterruptedException ex){
				
			}
		}
		repaint();
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		System.out.println("X = " +e.getX() + " Y = "+ e.getY());
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		try{
			while(true){
				for(int x=0; x<movesquares.size(); x++){
					movesquares.get(x).slideright();
				}
				numbers[0][0].slideright();
				numbers[1][0].slideright();
				System.out.println("Counter " + numbers[0][0].counter);
				System.out.println("second");
				Thread.sleep(10);
			}
		}catch(InterruptedException e){
			
		}
		repaint();
	}
}


for(int row =0; row<4; row++){
	for (int col =0; col<4; col++){
		if(trackerboard[row][col]!=null){
			Rectangle tempsquare = squares[row][col];
			g2d.setColor(new Color(217,198,178));
			numbers[row][col] = new Square(tempsquare.getX(),tempsquare.getY(),tempsquare.getWidth(),tempsquare.getHeight());
			g2d.fill(numbers[row][col].getRect());
			g2d.setFont(new Font("arial", Font.BOLD,80));
			g2d.setColor(Color.BLACK);
			g2d.drawString(trackerboard[row][col],(int) (tempsquare.getX()+tempsquare.getWidth()/2-20),
					(int) (tempsquare.getY()+tempsquare.getWidth()/2+25));
		}
	}
}

//I can use runnable and stuff
public void run() {
	// TODO Auto-generated method stub
	try{
		while(true){
			numbers[0][0].slideright();
			Thread.sleep(50);
			//System.out.println("THREAD RUNNING");
		}
	}catch(Exception ex){
		System.out.println(ex);
	}
}
Thread t = new Thread(this); 
t.start(); //in constructor


for(int row =0; row<4; row++){
for(int num = 0; num<3; num++){
	for(int col = 2; col>=0;col--){
		if(trackerboard[row][col]!=null && trackerboard[row][col+1]==null){
			//allnums += ""+row+col+trackerboard[row][col]; //data will be stored as row,col,then value
			numbers[row][col].slideright(100);
			String temp = trackerboard[row][col];
			trackerboard[row][col]=null;
			trackerboard[row][col+1]=temp;
			numbers[row][col].resetCounter();
			Square tempnum = numbers[row][col];
			numbers[row][col]=null;
			numbers[row][col+1]=tempnum;
		}
	}
}
}


	for(int row =0; row<4; row++){
for (int col =3; col>=0; col--){
	if(trackerboard[row][col]!=null && trackerboard[row][col+1]==null){
		//allnums += ""+row+col+trackerboard[row][col]; //data will be stored as row,col,then value
		numbers[row][col].slideright(100);
		String temp = trackerboard[row][col];
		trackerboard[row][col]=null;
		trackerboard[row][col+1]=temp;
		numbers[row][col].resetCounter();
	}
}
}



		for(int row =0 ;row<4;row++){
if(trackerboard[row][2]!=null && trackerboard[row][3] ==null){
	numbers[row][2].slideright(500);
}
if (trackerboard[row][1] != null && trackerboard[row][2] ==null){
	numbers[row][1].slideright(700);
}
else if(trackerboard[row][1] != null && trackerboard[row][2] !=null){
	
}
}*/





























/*	public void mergeNumbers(Square sq1, Square sq2, String direction){
		System.out.println("Merge" + sq2.getRow()+" "+sq2.getCol());
		System.out.println("square 1 = " +trackerboard[sq1.getRow()][sq1.getCol()]+" square 2 = " 
				+ trackerboard[sq2.getRow()][sq2.getCol()]);
		Thread t = new Thread(sq1);
		if(direction.equals("up")){
			sq1.slideup();

		}else if (direction.equals("down")){
			sq1.slidedown();
		}
		else if (direction.equals("right")){
			sq1.slideright();
		}
		else if (direction.equals("left")){
			sq1.slideleft();
		}
		
		t.start();
		try{
			Thread.sleep(16);
		}catch(InterruptedException e){
			
		}
		sq1.resetCounter();
		numbers[sq1.getRow()][sq1.getCol()] = null;
		trackerboard[sq1.getRow()][sq1.getCol()] = null;
		
		System.out.println(trackerboard[sq2.getRow()][sq2.getCol()]);
		int previousvalue = Integer.parseInt(trackerboard[sq2.getRow()][sq2.getCol()]);
		trackerboard[sq2.getRow()][sq2.getCol()] = new String(previousvalue*2+"");


	}*/