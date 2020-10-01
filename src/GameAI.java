import java.util.ArrayList;
import java.util.Collections;

public class GameAI{ //extend it for now idk if I'm going to use it though...
	String[][] trackerboard;
	int[][] multiplier = new int[4][4]; 
	String[][] resetBoard;
	String[][] gamestateBoard;
	ArrayList<Integer> score = new ArrayList<Integer>();
	String possiblespots; //spots for a random 2 to pop up, maybe turn into a string instead
	int mergebooster =0; //makes ai more inclined to merge rather than not merge
	public GameAI(String[][] storedboard){
		trackerboard = clone(storedboard); //main board that will be doing all the transformations 
		resetBoard = clone(trackerboard); //board that will be resetting trackerboard when transformations are done 

		for(int row =0; row<4; row++){
			for(int col=0; col<4; col++){
				multiplier[row][col] = 1;
			}
		}
		multiplier[0][0] = 9; //multiplier rewards top left of the screen with the highest number 
		multiplier[1][0] = 5;
		multiplier[0][1] = 5;
		multiplier[1][1] = 2; //make each a bit more than 2 so ai prefers combining rather than having 2 near each other
		multiplier[0][2] = 2; //although this currently has best performance 
		multiplier[2][0] = 2;
	}
	public String boardToString(String[][] board){
		String converted = "";
		for(int row =0; row<4; row++){
			for (int col =0; col<4; col++){
				converted += board[row][col];
			}
		}
		return converted;
	}
	public int getHighest(){ //wow getting highest actually works really well, reflects the highest number depending on 
		//the sliding direction
		//eg [8][8][2][2] will reflect 16 highest for left and right slide, but only 8 for up and down slide, so it works
		int max =0;
		for(int row =0; row<4; row++){
			for(int col =0; col<4; col++){
				if(trackerboard[row][col]!=null && Integer.parseInt(trackerboard[row][col])>max){
					max = Integer.parseInt(trackerboard[row][col]);
				}
			}
		}

		return max;
	}
	public int getPoints(){ //gets points of current board 
		int score =0;
		int nearscore =0; //score for having tiles near 
		int emptyscore =0; //score for having empty tiles 
		for(int row =0; row<4; row++){
			for(int col =0; col<4; col++){
				if(trackerboard[row][col]!=null){
					score += Integer.parseInt(trackerboard[row][col])*multiplier[row][col];
					System.out.println("jhi" + col);
					if(col<3 && trackerboard[row][col].equals(trackerboard[row][col+1])){ // this conditional block of code is 
						//to let the ai be more inclined to shift blocks that are equal next to each other 
						score+= Integer.parseInt(trackerboard[row][col])*multiplier[row][col] /2;
						nearscore +=Integer.parseInt(trackerboard[row][col])*multiplier[row][col] /2;
						System.out.println("RIGHT NEAR SCORE");
						System.out.println("" + row+ (col+1) +" " + row+col);
					}
					else if(row >0 && trackerboard[row][col].equals(trackerboard[row-1][col])){ //have to use .equals for strings
						//for some reason since == doesn't work
						score+= Integer.parseInt(trackerboard[row][col])*multiplier[row][col] /2;
						nearscore +=Integer.parseInt(trackerboard[row][col])*multiplier[row][col] /2;
					}
					else if(row <3 && trackerboard[row][col].equals(trackerboard[row+1][col])){
						score+= Integer.parseInt(trackerboard[row][col])*multiplier[row][col] /2;
						nearscore +=Integer.parseInt(trackerboard[row][col])*multiplier[row][col] /2;
					}
				}
				else{
					score += getHighest()*multiplier[0][0]/4; // im just assuming top left will be highest
					emptyscore += getHighest()*multiplier[0][0]/4;
				}
			}
		}
		/*System.out.println(boardToString(resetBoard));
		System.out.println(boardToString(trackerboard));*/
		//score += mergebooster; //add the mergebooster to the score
		mergebooster=0; //reset it for next time
		System.out.println("Highest= " + getHighest());
		System.out.println("nearscore= " + nearscore);
		System.out.println("emptyscore= " + emptyscore);
		if(boardToString(resetBoard).equals(boardToString(trackerboard))){
			score = 0;
			System.out.println("SAME");
		}
		return score;
	}
	public void updateBoard(String[][] board){ //updates the board 
		resetBoard = clone(board); // make sure to keep a copy of new board for resets
		trackerboard = clone(board);
	}
	public void viewScore(){
		for(int row =0; row<4; row++){
			for(int col =0; col<4; col++){
				if(trackerboard[row][col]!=null){
					System.out.print("["+Integer.parseInt(trackerboard[row][col])*multiplier[row][col]+"]");
				}else{
					System.out.print("[null]");
				}
			}
			System.out.println();
		}
	}
	public void viewBoard(String[][] board){
		for(int row=0;row<4;row++){
			for(int col=0; col<4; col++){
				System.out.print("["+board[row][col]+"]");
			}
			System.out.println();
		}
	}
	public void setNew(int x, int y){ //adds new "2" square in given coords
		for(int row=0;row<4;row++){
			for(int col =0; col<4; col++){
				if(x==row && y == col){
					trackerboard[row][col]="2";
				}
			}
		}
	}
	public String[][] stringToBoard(String boardstring){ //takes in board parameter that has been converted to string 
		String[] board = boardstring.split(","); //splits this board string into an array for easier processing 
		String[][] returnboard = new String[4][4]; //make new board to return 
		int idx =0;
		for(int row =0; row<4;row++){
			for (int col =0; col<4; col++){
				System.out.println(board.length);
				returnboard[row][col] = board[idx];
				idx++;
			}
		}
		return returnboard;
	}
	public void print(String[][] array){
		for(int row=0; row<4; row++){
			for (int col =0; col<4; col++){
				System.out.print("["+array[row][col]+"]");
			}
			System.out.println();
		}
	}
	public String[] findAllMoves(String[][] board){
		String[] moves = new String[4]; //this will store the 4 boards 
		String[][] permuteboard = clone(board);
		String[][] resetboard = clone(board);
		slide(permuteboard, "right"); //sliding right
		moves[0] = boardToString(permuteboard);
		permuteboard = clone(resetboard);
		
		slide(permuteboard, "left"); //left etc 
		moves[1] = boardToString(permuteboard);
		permuteboard = clone(resetboard);
		
		slide(permuteboard, "up");
		moves[2] = boardToString(permuteboard);
		permuteboard = clone(resetboard);
		
		slide(permuteboard, "down");
		moves[3] = boardToString(permuteboard);
		permuteboard = clone(resetboard);
		
		return moves; //moves is a 4 length array that stores all the board combos after one slide
	}
	public void find2ndMove(){ //WIP
		String[] spots = possiblespots.split(",");
		for(int x=0; x<spots.length;x++){
			String spot = spots[x];
			int row = Character.getNumericValue(spot.charAt(0));
			int col = Character.getNumericValue(spot.charAt(1));
			resetBoard = clone(trackerboard); //need to store value before changing 
			setNew(row,col); //changes trackerboard
			findMove();
			
		}
	}
	public String findMove(){ //this will be the bulk of the ai, looking for best moves 
		//this method will actually be just proof of concept, going to work on another method instead
		int max =0;
		int directionscore=0; //score for each direction, use this to stop repeating the score method, which is just good
		//for not wasting resources and also because of printing issues
		String dir ="";
		//slide right 
		slide(trackerboard, "right");
		System.out.println("SLIDE RIGHT");
		viewBoard(trackerboard);
		directionscore = getPoints();
		if(directionscore>max){
			max = directionscore;
			dir = "right";
			gamestateBoard = clone(trackerboard);
		}
		trackerboard = clone(resetBoard);
		
		//slide left 
		System.out.println("SLIDE LEFT");

		slide(trackerboard, "left");
		viewBoard(trackerboard);
		directionscore = getPoints();
		if(directionscore>max){ //if no move is the best move, move left then
			//don't know why i was viewing left board but whatever, ohhh for making it left justified
			max = directionscore;
			dir = "left";
			gamestateBoard = clone(trackerboard);
		}
		trackerboard = clone(resetBoard);
		
		//slide up
		System.out.println("SLIDE UP");

		slide(trackerboard, "up");
		viewBoard(trackerboard);

		directionscore = getPoints();
		if(directionscore>max){
			max = directionscore;
			dir = "up";
			gamestateBoard = clone(trackerboard);
		}
		trackerboard = clone(resetBoard);
		
		//slide down
		System.out.println("SLIDE DOWN");

		slide(trackerboard, "down");
		viewBoard(trackerboard);
		directionscore = getPoints();
		if(directionscore>max){
			max = directionscore;
			dir = "down";
			gamestateBoard = clone(trackerboard);
		}
		trackerboard = clone(resetBoard);
		System.out.println(dir);
		//System.out.println((boardToString(trackerboard)));
		//print(stringToBoard(boardToString(trackerboard)));
		return dir;

	}
	public int getMax(){
		int max =0;
		int idx =0;
		for(int x =0; x<score.size();x++){
			if(score.get(x)>max){
				max = score.get(x);
				idx = x;
			}
		}
		return idx;
	}
	public void getPossibleSpots(){ //for finding the randomly generated 2's that will occcur
		possiblespots = "";
		for(int row=0;row<4;row++){
			for (int col=0; col<4; col++){
				if(trackerboard[row][col]==null){
					possiblespots+= new String(""+row+col+",");
				}
			}
		}
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
	public void slide(String[][] board,String direction){
		if(direction =="right"){
			getSlideRight(board, false);
			getSlideRight(board, false);
			getSlideRight(board, true);
			getSlideRight(board, false);

		}else if(direction =="left"){
			getSlideLeft(board, false);
			getSlideLeft(board, false);
			getSlideLeft(board, true);
			getSlideLeft(board, false);
		}else if(direction =="up"){
			getSlideUp(board, false);
			getSlideUp(board, false);
			getSlideUp(board, true);
			getSlideUp(board, false);
		}else if(direction =="down"){
			getSlideDown(board, false);
			getSlideDown(board, false);
			getSlideDown(board, true);
			getSlideDown(board, false);

		}
	}
	public void getSlideRight(String[][] trackerboard, boolean last){	// need to change getslide method a bit due to using ONLY trackerboard
		//no skipone needed because it doesn't work properly, before it would skip the one in the middle that should not be
		//merged (eg 2,2,2,2 won't merge middle 2. However, since I'm applying transformationn within the code already, this
		//would never happen since the block would be null (eg 2,2,null,4) meaning that I think it would skip the next merge...
		//not sure... yeah it happens because now theres a null position which then the block shifts because of the if statement?
		//not too sure
		//but yeah removing skipone makes it work normally
		//also I added a conditional to shifting into a blank square. This can only be done if it is not the merging turn
		//otherwise if it is the merging turn, it will ever only merge once because of the null space that is created
		//eg 2,2,null,4 becomes null,2,2,4 and not null,null,4,4 since theres always a null position
		for(int num = 0; num <4;num++){
			for(int col = 2; col>=0;col--){
				if(trackerboard[num][col]!=null && trackerboard[num][col+1]==null && !last){	
					trackerboard[num][col+1]=trackerboard[num][col];
					trackerboard[num][col]=null;

				}
				else if(trackerboard[num][col]!=null && trackerboard[num][col].equals(trackerboard[num][col+1]) && last){ 
					trackerboard[num][col] = null;
					trackerboard[num][col+1] = Integer.parseInt(trackerboard[num][col+1])*2 +"";
					mergebooster+=Integer.parseInt(trackerboard[num][col+1])*2 * (multiplier[num][col+1]-1); //the bigger the number being merged, the 
					//more inclined the ai wants to merge the pieces
					//this basically works like the score, where the booster is equal to the tile you make 
				}
			}
		}
	}
	public void getSlideDown(String[][] trackerboard, boolean last ){	
		for(int num = 0; num <4; num++){
			for(int row = 2; row>=0;row--){
				if(trackerboard[row][num]!=null && trackerboard[row+1][num]==null && !last){
					trackerboard[row+1][num]=trackerboard[row][num];
					trackerboard[row][num]=null;
				}
				else if(trackerboard[row][num]!=null && trackerboard[row][num].equals(trackerboard[row+1][num]) && last){ 
					trackerboard[row][num] = null;
					trackerboard[row+1][num] = Integer.parseInt(trackerboard[row+1][num])*2 +"";
					mergebooster+=Integer.parseInt(trackerboard[row+1][num])*2 * (multiplier[row+1][num]-1);
				}
			}
		}
	}
	public void getSlideLeft(String[][] trackerboard, boolean last){		
		for(int num = 0; num<4;num++){
			for(int col = 1; col<=3;col++){
				if(trackerboard[num][col]!=null && trackerboard[num][col-1]==null && !last){
					trackerboard[num][col-1]=trackerboard[num][col];
					trackerboard[num][col]=null;
				}
				else if(trackerboard[num][col]!=null && trackerboard[num][col].equals(trackerboard[num][col-1]) && last){ 
					trackerboard[num][col] = null;
					trackerboard[num][col-1] = Integer.parseInt(trackerboard[num][col-1])*2 +"";
					mergebooster+=Integer.parseInt(trackerboard[num][col-1])*2 * (multiplier[num][col-1]-1);
				}
			}
		}
	}
	public void getSlideUp(String[][] trackerboard, boolean last){	
		for(int num = 0; num<4;num++){
			for(int row = 1; row<=3;row++){
				if(trackerboard[row][num]!=null && trackerboard[row-1][num]==null && !last){	
					trackerboard[row-1][num]=trackerboard[row][num];
					trackerboard[row][num]=null;
				}
				else if(trackerboard[row][num]!=null && trackerboard[row][num].equals(trackerboard[row-1][num]) && last){ 
					trackerboard[row][num] = null;
					trackerboard[row-1][num] = Integer.parseInt(trackerboard[row-1][num])*2 +"";
					mergebooster+=Integer.parseInt(trackerboard[row-1][num])*2 * (multiplier[row-1][num]-1);
				}
			}
		}
	}

}
