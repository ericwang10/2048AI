import java.awt.*;

import javax.swing.JFrame;

public class Frame {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		JFrame frame = new JFrame();
		frame.setSize(1000,1000);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Board board = new Board();
		frame.add(board);
		frame.setVisible(true);

	}

}
