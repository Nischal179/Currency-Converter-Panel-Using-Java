import java.io.UnsupportedEncodingException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

/**
 * The main driver program for the GUI based conversion program.
 * 
 * @author Nischal Aryal
 */
public class Converter {

	public static void main(String[] args) throws UnsupportedEncodingException {

		// Making a new instance of JFrame
		JFrame frame = new JFrame("Currency Converter");

		// Making our app close when close button of the frame is pressed
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Making a new instance of MainPanel class named panel
		CurrencyPanel panel = new CurrencyPanel();

		// Calling setupMenu method of MainPanel class and setting it up with a frame
		frame.setJMenuBar(panel.setupMenu());
		frame.getContentPane().add(panel);

		frame.pack();

		ImageIcon icon = new ImageIcon("currency.png");
		frame.setIconImage(icon.getImage());

		// Making frame visible to naked eye
		frame.setVisible(true);

		// Making frame's relative location at the center of the display screen
		frame.setLocationRelativeTo(null);
	}
}
