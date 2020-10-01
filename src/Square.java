import java.awt.Color;
import java.awt.Rectangle;

public class Square{
	int row, col;
	Rectangle rect; 
	int rectx,recty;
	int rectwidth, rectheight;
	int counter=0; //using counter to shift everything
	boolean shiftright = false;
	boolean shiftleft = false;
	boolean shiftup = false;
	boolean shiftdown = false;
	boolean shifting = true; 
	int value;
	public Square(int x, int y, int width, int height){
		rect = new Rectangle(x,y,width,height);
		rectx = x;
		recty = y;
		rectwidth = width;
		rectheight = height;
		value = 2; 
	}
	public void updateValue(int x){
		value = x; 
	}
	public int getValue(){
		return value;
	}
	public Color getColor(){
		Color c = null; 
		if(value ==2){
			c = Color.decode("#eee4da"); //decode method decodes hexadecimal, these values taken off of a blog
		}else if (value == 4){
			c = Color.decode("#ede0c8");
		}else if (value == 8){
			c = Color.decode("#f2b179");
		}else if (value == 16){
			c = Color.decode("#f59563");
		}else if (value == 32){
			c = Color.decode("#f67c5f");
		}else if (value == 64){
			c = Color.decode("#f65e3b");
		}else if (value == 128){
			c = Color.decode("#edcf72");
		}else if (value == 256){
			c = Color.decode("#edcc61");
		}else if (value == 512){
			c = Color.decode("#edc850");
		}else if (value == 1024){
			c = Color.decode("#edc53f");
		}else if (value == 2048){
			c = Color.decode("#edc22e");
		}
		return c;
	}
	public Square(double x, double y, double width, double height){ //apparently I use this method a lot more, forgot 
		//to add the updates to the local variables rectx,recty, etc so thats why it wasn't updating in the board
		//make sure to update both constructors
		rect = new Rectangle((int)x,(int)y,(int)width,(int)height);
		rectx = (int) x;
		recty = (int)y;
		rectwidth = (int)width;
		rectheight = (int)height;
		value = 2; 

	}
	public Rectangle getRect(){
		rect = new Rectangle(rectx,recty,rectwidth,rectheight);
		return rect;
	}
	public void setRowCol(int x, int y){
		row = x;
		col = y;
	}
	public void addX(int x){
		if(rectx<700){
			rectx+=x;
		}
	}
	public void move(String dir, int x){
		if(dir == "right"){
			if(rectx<700){
				rectx+=x;
			}
		}else if(dir == "left"){
			if(rectx>100){
				rectx-=x;
			}
		}else if(dir == "down"){
			if(recty<700){
				recty+=x;
			}
		}else if (dir =="up"){
			if(recty>100){
				recty-=x;
			}
		}
	}
	public void addY(int y){
		recty +=y;
	}
	public int getX(){
		return rectx;
	}
	public int getY(){
		return recty;
	}
	public int getWidth(){
		return rectwidth;
	}
	public int getHeight(){
		return rectheight;
	}
	public void slideright(){
		if(counter<10 && rectx<700){ // I believe 700 is 4, 500 is 3, 300 is 2, 100 is 1...
			/*rectx+=20;
			System.out.println("SLIDING");
			counter++;*/
			shiftright = true;
			shiftleft=false;
			shifting = true; 
		}
	}
	public void slidedown(){
		if(counter<10 && recty<700){ // I believe 700 is 4, 500 is 3, 300 is 2, 100 is 1...
			/*rectx+=20;
			System.out.println("SLIDING");
			counter++;*/
			shiftdown = true;
			shiftup=false;
			shifting = true; 
		}
	}
	public void slideup(){
		if(counter<10 && recty>100){ // I believe 700 is 4, 500 is 3, 300 is 2, 100 is 1...
			/*rectx+=20;
			System.out.println("SLIDING");
			counter++;*/
			shiftdown = false;
			shiftup=true;
			shifting = true; 
		}
	}
	public void resetCounter(){
		counter=0;
	}
	public void slideleft(){
		if(counter<10 && rectx>100){ // I believe 700 is 4, 500 is 3, 300 is 2, 100 is 1...
			/*rectx+=20;
			System.out.println("SLIDING");
			counter++;*/
			shiftright=false;
			shiftleft = true;
			shifting = true; 
		}
	}
	public int getRow(){
		return row;
	}
	public int getCol(){
		return col;
	}

	
}
