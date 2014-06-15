package projcbir;

import javax.swing.*;

public class Display extends JFrame {
	public void showImage(String imgstr) {

		// Creates the actual frame with title 'image_name' and dimensions
		JFrame frame = new JFrame(imgstr.substring(17));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(400, 400);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);

		ImageIcon image = new ImageIcon(imgstr);
		JLabel label1 = new JLabel(" ", image, JLabel.CENTER);
		frame.getContentPane().add(label1);

		frame.validate();
		frame.setVisible(true);

	}

}
