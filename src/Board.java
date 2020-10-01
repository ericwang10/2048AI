import java.awt.Color;
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
import java.util.Arrays;
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
	boolean gameover = false; //for when game is over
	int availablespace; //tracker of available space to generate the next random number 
	Timer tm = new Timer(3,this);

	boolean slideright = false; //booleans for when you press a button 
	boolean slideleft = false;
	boolean slideup = false;
	boolean slidedown = false;
	
	int counter =0; //counter to only let the slides run 3 times to save processing power and move next turn 
	
	boolean turnfinished = false; //the final block of code that resets and generates new numbers
	boolean moving = false; //boolean for moving(animation) block of code 
	int movingcount = 0; //count for the animation 
	
	boolean merging = false; //boolean for merging (animation) code 
	int mergingcount = 0; //count for animation
	
	boolean finalshift = false; //boolean for the shift after the merging (if necessary) 
	int finalshiftcount = 0; // count fo animation 
	
	ArrayList<Square> movesquare = new ArrayList<Square>();
	ArrayList<Square> mergesquare = new ArrayList<Square>(); //square that is moving into merge
	ArrayList<Square> mergeintosquare = new ArrayList<Square>(); //square that will be merged into, changing into a new one
	String direction; // can't declare direction in actionperformed or else it won't run 3 times 
	//this specifies direction user pressed
	boolean finished = true; //boolean to signal that everything is finished and next input can be read 
	int score = 0;
	GameAI AI;
	String[][] temp; //for testing moves that are made 
	boolean startAI = false; //for letting ai run by itself 
	
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
		numbers[0][0] = makeNewSquare(0,0);
		
		trackerboard[1][0] = "2";
		numbers[1][0] = makeNewSquare(1,0);
		
		trackerboard[2][0] = "2";
		numbers[2][0] = makeNewSquare(2,0);

		/*trackerboard[3][0] = "2";
		numbers[3][0] = makeNewSquare(3,0);*/
		trackerboard[0][1] = "2";
		numbers[0][1] = makeNewSquare(0,1);
		
		numbers[0][2] = makeNewSquare(0,2);
		numbers[0][3] = makeNewSquare(0,3);
		
		trackerboard[0][2] = "2";
		trackerboard[0][3] = "2";
		
		trackerboard[1][3] = "2";
		numbers[1][3] = makeNewSquare(1,3);
		
		trackerboard[2][2] = "2";
		numbers[2][2] = makeNewSquare(2,2);
		
		trackerboard[3][0] = "2";
		numbers[3][0] = makeNewSquare(3,0);
		
		availablespace = 16;
		for(int row = 0; row<4; row++){ //find how much available space there is 
			for (int col = 0; col<4; col++)
			{
				if(numbers[row][col]!=null ){
					availablespace--;
				}
			}
		}
		AI = new GameAI(trackerboard); //initialize AI, which passes current board into the constructor

		//trackerboard[1][1] = "4";
		String str1 = "hi";
		String str2 = "hi";
		System.out.println(str1==str2); //RETURNS FALSE WTF
		
		temp = clone(trackerboard);
		System.out.println(Arrays.deepEquals(temp,trackerboard)); //need to use deep equals for multidimensional array
		/*ArrayList<String> test1 = new ArrayList<String>();
		test1.add("test1");
		ArrayList<String> test2 = new ArrayList<String>(test1); // need to use this to actually make a clone, otherwise 
		//using equals just points one array list to the other, which is not intended 
		test2.set(0, "test2");
		System.out.println(test1.get(0));*/
		System.out.println(Integer.parseInt("2")*6 +"");
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
					g2d.setColor(numbers[row][col].getColor());//DYNAMIC coloring
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
		g2d.setFont(new Font("arial", Font.BOLD,30));

		g2d.drawString(score+"", 800, 50);
		if(gameover){
			g2d.setFont(new Font("arial", Font.BOLD,80));
			g2d.setColor(Color.RED);
			g2d.drawString("Game Over", 300, 500);

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
		//System.out.println("ACTION PERFOMRED");
		
		if(slideright){ //so initially, the key pressed will input a boolean which corresponds to the direction pressed
			//numbers[0][0].slideright(300);
			getSlideRight(); //run method that returns an arraylist of all possible moves 
			direction = "right"; //direction is the right side 
			moving = true; //make moving true, which initiates the animation
			slideright = false; //turn slider right back to false again ... might not be needed 
			movingcount =0;
			finished = false; 
		}
		if(slideleft){ //finished will wait until the current turn is over before you can input again 
			getSlideLeft();

			direction = "left";
			moving = true;
			slideleft = false;
			movingcount =0;
			finished = false;
		}
		if(slidedown){
			getSlideDown();

			direction = "down";
			moving = true;
			slidedown = false;
			movingcount =0;
			finished = false; 
		}
		if(slideup){
			getSlideUp();

			direction = "up";
			moving = true;
			slideup = false;
			movingcount =0;
			finished = false; 
		}
		if(moving && movingcount<10){ //so if animation is running, and it has been running less than 10 counts 
			for(int x=0;x<movesquare.size();x++){ //for each available move
				movesquare.get(x).move(direction, 20); //move it by 20 pixels, corresponds to the direction 
			}
			movingcount++; //add one to count 
		}
		if(movingcount==10){ //once count reaches 10 
			if(direction == "right"){ //set the slider boolean back to true 
				slideright = true;
			}else if(direction == "left"){
				slideleft = true;
			}else if (direction == "up"){
				slideup = true;
			}
			else if (direction == "down"){
				slidedown = true;
			}
			//ok this runs 3 times which is good 
			counter++;
		}
		if(counter==3){ //UNLESS counter is 3, which means it has already ran 3 times, which is the max that a tile can shift
			//need this block of code FIRST to make turn finished = true, it won't work if both are just one block of ode
			slidedown = false; //set everything false 
			slideup = false;
			slideright=false;
			slideleft =false;
			moving = false;
			movingcount =0; //i think this is needed to deactivate top, this deactcivates everything from running again 
//			System.out.println("TURN OGRE");
			merging = true;//initiate merging logic
			//System.out.println(counter);
			counter = 0; // reset counter so it doesn't run a billion times 
		}
		//merging logic 
		if (merging && mergingcount<10){ //merging logic is same as moving logic 
			for(int x=0;x<mergesquare.size();x++){
				//System.out.println(mergesquare.get(x).getRow()+ " " + mergesquare.get(x).getCol());
				mergesquare.get(x).move(direction, 20); //uses merging arraylist instead 
			}

			mergingcount++; 
		}
		if(mergingcount == 10){ //once merging is 10, we should shift one more time 
			//System.out.println("merginc count"); //ok just need to write program that actually sets everything in place 

			for(int x =0;x<mergesquare.size();x++){
				mergeNumbers(mergesquare.get(x),direction, true);
			}
			getSlide(direction); //run arraylist again to see what values are possible
			finalshift = true;
			
			mergingcount=0; //reset merging so this only runs once
			merging = false; //reset this because merging is over 

		}
		if(finalshift && finalshiftcount<10){ //make sure to include both conditionals or it WILL run forever
			//can't use a forloop over this or else animations won't work
			for(int x=0;x<movesquare.size();x++){ //for each available move
				
				movesquare.get(x).move(direction, 20); //move it by 20 pixels, corresponds to the direction 
			}
			finalshiftcount++;
		}
		if(finalshiftcount==10){
			finalshift = false;
			turnfinished = true;// then turn is finished 
		}
		
		if(turnfinished){ //THEN this block can run
			//don't put a thread.sleep here or else everything becomes very slow
			generateNextNumber();
			turnfinished =false; //reset this so it can run again
			counter = 0; //reset everything //whoa if I turn this off it works, but things don't merge and shift correctly
			finalshiftcount = 0; //ok this is the last piece of the puzzle, set this = 0 so the finalshift doesn't always
			//turn false and it doesn't always trigger turn finished, causing counter to reset
			finished = true;
			if(availablespace ==0 && testEnd()){
				gameover= true;
			}
		}
		
		if(startAI){
			//if(turnfinished){ //i want to just make sure it doesn't do anything before turn finishes
				runAI();
			//}
		}
		
		repaint();
	}
	public String[][] clone(String[][] arr){ //write a method to clone the trackerboard since shallow cloning doesn't work
		//for multidimensional arrays
		String[][] clone = new String[arr.length][arr[0].length];
		for(int row =0; row<arr.length;row++){
			for(int col =0; col<arr[0].length;col++){
				clone[row][col] = arr[row][col];
			}
		}
		return clone;
	}
	public Square[][] clone(Square[][] arr){ //write a method to clone the trackerboard since shallow cloning doesn't work
		//for multidimensional arrays
		Square[][] clone = new Square[arr.length][arr[0].length];
		for(int row =0; row<arr.length;row++){
			for(int col =0; col<arr[0].length;col++){
				clone[row][col] = arr[row][col];
			}
		}
		return clone;
	}
	public boolean testEnd(){ //i'll put the availablespace ==0 test in actionperformed...
		String[][] tempboard = clone(trackerboard);
		//String[][] originalboard = clone(trackerboard); //don't think another copy of board is needed since temp isn't changed
		Square[][] temp = clone(numbers);
		getSlideRight(); //slides and merges and checks to see if it is equal 
		for(int x =0;x<mergesquare.size();x++){
			mergeNumbers(mergesquare.get(x),"right", false);
		}
		if(!Arrays.deepEquals(tempboard,trackerboard)){
			trackerboard = clone(tempboard); //always make sure to reset the board, even if it is false 
			numbers = clone(temp);
			return false;
		}
		trackerboard = clone(tempboard); //reset board for next slide test 
		numbers = clone(temp);
		
		getSlideLeft();
		for(int x =0;x<mergesquare.size();x++){
			mergeNumbers(mergesquare.get(x),"left", false);
		}
		if(!Arrays.deepEquals(tempboard,trackerboard)){
			trackerboard = clone(tempboard);
			numbers = clone(temp);
			return false;
		}
		trackerboard = clone(tempboard);
		numbers = clone(temp);

		getSlideUp();
		for(int x =0;x<mergesquare.size();x++){
			mergeNumbers(mergesquare.get(x),"up", false);
		}
		if(!Arrays.deepEquals(tempboard,trackerboard)){
			trackerboard = clone(tempboard);
			numbers = clone(temp);
			return false;
		}
		trackerboard = clone(tempboard);
		numbers = clone(temp);

		getSlideDown();
		for(int x =0;x<mergesquare.size();x++){
			mergeNumbers(mergesquare.get(x),"down", false);
		}
		if(!Arrays.deepEquals(tempboard,trackerboard)){
			trackerboard = clone(tempboard);
			numbers = clone(temp);
			return false;
		}
		trackerboard = clone(tempboard);
		numbers = clone(temp);

		return true;

	}
	public void reset(Square[][] org, String[][] org2){ //not working since objects in org are just pointers to the original
		//they aren't copies 
		numbers = clone(org); //think I need to make a new one, not point it at the same one again or else
		//slides will be permanent 
		trackerboard = clone(org2);
		print(trackerboard);
		for(int row =0; row<4; row++){
			for(int col=0; col<4;col++){
				if(numbers[row][col]!=null){
					numbers[row][col].updateValue(org[row][col].getValue());

				}
				else{
					System.out.print("null");
				}
			}
			System.out.println();
		}
	}
	public void getSlide(String direction){
		if(direction == "right"){
			getSlideRight();
		}else if (direction == "left"){
			getSlideLeft();
		}else if (direction == "up"){
			getSlideUp();
		}else if (direction == "down"){
			getSlideDown();
		}
	}

	public void print(String[][] array){
		for(int row=0; row<4; row++){
			for (int col =0; col<4; col++){
				System.out.print(array[row][col]);
			}
			System.out.println();
		}
	}
	public void getSlideRight(){	
		movesquare.clear(); //make sure we get a clean arraylist before running everything to prevent duplicates
		boolean skipone = false;
		mergesquare.clear(); //clear here so we get the value of the last loop, not working until i implement board changes
		for(int num = 0; num <4;num++){ //num is length of rows or number of times this sliding needs to be run 
			//this for loop allows it to run for the entire board instead of just the first row 
			skipone = false; //need to make sure skipone is reset every row or else it will skip the merging in the next row

			for(int col = 2; col>=0;col--){
				if(trackerboard[num][col]!=null && trackerboard[num][col+1]==null){
					movesquare.add(numbers[num][col]); //add available tile for shifting to arraylist
	
					trackerboard[num][col+1]=trackerboard[num][col];
					trackerboard[num][col]=null;
					numbers[num][col+1]=numbers[num][col];
					numbers[num][col]=null;
					numbers[num][col+1].setRowCol(num, col+1); //NEED THIS LINE OF CODE TO UPDATE THE ROW AND COL
					//OR ELSE THE MERGING WON'T WORK BECAUSE it takes the last known row and col, which is now null since
					//the values have swapped places
					
				}
				if(skipone){
					skipone=false;
					continue;
				}
				else if(trackerboard[num][col]!=null && trackerboard[num][col].equals(trackerboard[num][col+1])){ 
					mergesquare.add(numbers[num][col]); //make sure to update row and col again just to make sure there aren't
					//any mistakes in the row and col values that will cause array errors 
					numbers[num][col].setRowCol(num, col);
					skipone=true; 
					
				}
			}
		}
	}
	public void getSlideDown(){	
		movesquare.clear();
		mergesquare.clear();
		boolean skipone= false;
		for(int num = 0; num <4; num++){
			skipone = false;
			for(int row = 2; row>=0;row--){
				if(trackerboard[row][num]!=null && trackerboard[row+1][num]==null){

					movesquare.add(numbers[row][num]);
	
					trackerboard[row+1][num]=trackerboard[row][num];
					trackerboard[row][num]=null;
					numbers[row+1][num]=numbers[row][num];
					numbers[row][num]=null;
					numbers[row+1][num].setRowCol(row+1, num); //need this for storing reasons and merging

				}
				if(skipone){
					skipone=false;
					continue;
				}
				else if(trackerboard[row][num]!=null && trackerboard[row][num].equals(trackerboard[row+1][num])){ 
					mergesquare.add(numbers[row][num]);
					numbers[row][num].setRowCol(row, num);
					skipone=true; 
				}
			}
		}
	}
	public void getSlideLeft(){		
		movesquare.clear();
		mergesquare.clear();
		boolean skipone = false;
		for(int num = 0; num<4;num++){
			skipone = false;
			for(int col = 1; col<=3;col++){
				if(trackerboard[num][col]!=null && trackerboard[num][col-1]==null){
					movesquare.add(numbers[num][col]); //add available tile for shifting to arraylist
	
					trackerboard[num][col-1]=trackerboard[num][col];
					trackerboard[num][col]=null;
					numbers[num][col-1]=numbers[num][col];
					numbers[num][col]=null;
					numbers[num][col-1].setRowCol(num, col-1);
				}
				if(skipone){
					skipone=false;
					continue;
				}
				else if(trackerboard[num][col]!=null && trackerboard[num][col].equals(trackerboard[num][col-1])){ 
					mergesquare.add(numbers[num][col]);
					numbers[num][col].setRowCol(num, col);
					skipone=true; 
				}
			}
		}
	}
	public void getSlideUp(){	
		movesquare.clear();
		boolean skipone = false;
		mergesquare.clear();
		for(int num = 0; num<4;num++){
			skipone = false; 
			for(int row = 1; row<=3;row++){
				if(trackerboard[row][num]!=null && trackerboard[row-1][num]==null){
					movesquare.add(numbers[row][num]); //add available tile for shifting to arraylist
	
					trackerboard[row-1][num]=trackerboard[row][num];
					trackerboard[row][num]=null;
					numbers[row-1][num]=numbers[row][num];
					numbers[row][num]=null;
					numbers[row-1][num].setRowCol(row-1, num);
				}
				if(skipone){
					skipone=false;
					continue;
				}
				else if(trackerboard[row][num]!=null && trackerboard[row][num].equals(trackerboard[row-1][num])){ 
					numbers[row][num].setRowCol(row, num); 
					mergesquare.add(numbers[row][num]);
					skipone=true; 
				}
			}
		}
	}
	public void mergeNumbers(Square sq1, String direction, boolean real){ //method to merge numbers 
		Square sq2 = null; //create a variable sq2 
		if(direction.equals("up")){ //assign sq2 to be the tile that another slide will slide into, thus it will always be in front
			//of the tile with respect to the sliding direction. eg if slide right [sq1][sq2] since moving --> this way
			sq2 = numbers[sq1.getRow()-1][sq1.getCol()];
			sq2.setRowCol(sq1.getRow()-1, sq1.getCol());
		}else if (direction.equals("down")){
			sq2 = numbers[sq1.getRow()+1][sq1.getCol()];
			sq2.setRowCol(sq1.getRow()+1, sq1.getCol());
		}
		else if (direction.equals("right")){
			sq2 = numbers[sq1.getRow()][sq1.getCol()+1];
			sq2.setRowCol(sq1.getRow(), sq1.getCol()+1);
		}
		else if (direction.equals("left")){
			sq2 = numbers[sq1.getRow()][sq1.getCol()-1];
			sq2.setRowCol(sq1.getRow(), sq1.getCol()-1);
		}
		
		numbers[sq1.getRow()][sq1.getCol()] = null; //this stuff replaces the array in trackerboard and numbers so it can 
		//properly display 
		trackerboard[sq1.getRow()][sq1.getCol()] = null;
		
		int previousvalue = Integer.parseInt(trackerboard[sq2.getRow()][sq2.getCol()]);
		trackerboard[sq2.getRow()][sq2.getCol()] = new String(previousvalue*2+"");
		if(real){ //this is to distinguish real merging from test merging 
			score+=previousvalue*2;
			numbers[sq2.getRow()][sq2.getCol()].updateValue(previousvalue*2); //update value only if it is real, which changes color
			//I believe this is still fine since i doesn't affect anything else since we are comparing trackerboard and numbers...
			availablespace++; //this is for generating new numbers, if i dont update this the while loop will get stuck and run forever
		}
	}
	public void generateNextNumber(){ //this makes random tiles appear after 
		while(true){
			int randorow = ran.nextInt(4);
			int randocol = ran.nextInt(4);// can't use available spaces because random numbers will just keep generating
			//random nubmers while the number of tries keeps going down
			if(trackerboard[randorow][randocol]==null){
				trackerboard[randorow][randocol] = "2";
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
	public void clearBoard(){
		for (int row =0; row<4;row++){
			for (int col =0; col<4; col++){
				trackerboard[row][col] = null;
				numbers[row][col] = null;
			}
		}
	}
	public void runAI(){
		AI.updateBoard(trackerboard);
		String dir = AI.findMove();
		if (dir == "right" && finished){
			slideright=true;
			slideleft=false;
			slidedown = false;
			slideup = false;
		}else if(dir=="left" && finished){
			slideright=false;
			slideleft=true;
			slidedown = false;
			slideup = false;
		}else if(dir=="up" && finished){
			slideright=false;
			slideleft=false;
			slidedown = false;
			slideup = true;
		}else if(dir=="down" && finished){
			slideright=false;
			slideleft=false;
			slidedown = true;
			slideup = false;
		}
	}
	public void viewBoard(){
		/*for(int row=0;row<4;row++){
			for(int col=0; col<4; col++){
				System.out.print("["+trackerboard[row][col]+"]");
			}
			System.out.println();
		}*/
		for(int row=0;row<4;row++){
			for(int col=0; col<4; col++){
				if(numbers[row][col]!=null){
					System.out.print("["+numbers[row][col].getValue()+"]");
				}
				else{
					System.out.print("null");
				}
			}
			System.out.println();
		}
	}
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		int key = e.getKeyCode();
		if (key==KeyEvent.VK_T){
			AI.updateBoard(trackerboard); //make sure to update the board always before doing anything with it
			AI.getPossibleSpots();
			System.out.println(AI.possiblespots);
		}
		if (key==KeyEvent.VK_E){
			startAI = true;
		}
			
		if (key==KeyEvent.VK_SPACE){
			/*viewBoard();
			for(int x=0;x <mergesquare.size();x++){
				System.out.println(mergesquare.get(x));
			}
			for(int x=0;x<4;x++){
				for(int col = 0;col<4;col++){
					System.out.print(temp[x][col]);
				}
				System.out.println();
			}*/
			viewBoard();
			System.out.println(Arrays.deepEquals(temp,trackerboard));
			System.out.println("testing end " + testEnd());
		}
		if(key == KeyEvent.VK_ENTER){
			clearBoard();
			trackerboard[0][0] = "2";
			numbers[0][0] = makeNewSquare(0,0);
			
			trackerboard[1][0] = "2";
			numbers[1][0] = makeNewSquare(1,0);
			
			trackerboard[2][0] = "2";
			numbers[2][0] = makeNewSquare(2,0);

			/*trackerboard[3][0] = "2";
			numbers[3][0] = makeNewSquare(3,0);*/
			trackerboard[0][1] = "2";
			numbers[0][1] = makeNewSquare(0,1);
			
			numbers[0][2] = makeNewSquare(0,2);
			numbers[0][3] = makeNewSquare(0,3);
			
			trackerboard[0][2] = "4";
			trackerboard[0][3] = "8";
			
			trackerboard[1][3] = "2";
			numbers[1][3] = makeNewSquare(1,3);
			
			trackerboard[2][2] = "2";
			numbers[2][2] = makeNewSquare(2,2);
			
			trackerboard[3][0] = "2";
			numbers[3][0] = makeNewSquare(3,0);
		}
		if (key==KeyEvent.VK_RIGHT && finished){
			slideright=true;
			slideleft=false;
			slidedown = false;
			slideup = false;
		}
		if (key ==KeyEvent.VK_LEFT && finished){
			slideleft=true;
			slideright = false;
			slidedown = false;
			slideup = false;
		}
		if (key ==KeyEvent.VK_DOWN && finished){
			slidedown=true;
			slideup = false;
			slideright=false;
			slideleft = false;
		}
		if (key ==KeyEvent.VK_UP && finished){
			slideup=true;
			slidedown = false;
			slideright = false;
			slideleft = false; 
		}
		if (key==KeyEvent.VK_UP || key==KeyEvent.VK_DOWN || key==KeyEvent.VK_LEFT || key==KeyEvent.VK_RIGHT){

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
		for(int row =0; row<4;row++){
			for(int col =0; col<4; col++){
				if(numbers[row][col]!=null){
					if(e.getX()>numbers[row][col].getX() && e.getX()<numbers[row][col].getX()+180 && 
							e.getY()>numbers[row][col].getY() && e.getY()<numbers[row][col].getY()+180){
						System.out.println(numbers[row][col].value);
					}
				}
			}
		}
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
			System.out.println("that");
			slideright = true;

			Thread.sleep(1500);
			System.out.println("this");
		}catch(InterruptedException e){
			
		}
		repaint();
	}
}
