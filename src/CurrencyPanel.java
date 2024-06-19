//Importing  Package into class 

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.*;

/**
 * @author Nischal Aryal
 */
@SuppressWarnings("serial") // Disabling compilation warning

//Inherit JPanel class in MainPanel class
public class CurrencyPanel extends JPanel {

	// Declaring Variables

	// String array containing different types of conversion
	private JTextField input; // Textfield for user input
	private JLabel resultLabel, counter; // Label for answer to the conversion and counter
	private JComboBox conversionList;
	private JCheckBox reverse;
	private JButton convertButton, clearButton;
	private boolean isReverse;
	static int count = 0;
	private String[] currency_symbol = new String[40];
	private File file = null;
	private ArrayList<UnitData> ud1 = new ArrayList<UnitData>();
	private Scanner scan;
	private float[] factor = new float[40];
	private String symbol;

	// Creating the menu
	JMenuBar setupMenu() {

		// Adding MenuBar inside our app
		JMenuBar menuBar = new JMenuBar();

		// Adding menu element
		JMenu filemenu = new JMenu("File");
		filemenu.setToolTipText("Click here to open File");

		JMenu help = new JMenu("Help");
		help.setToolTipText("Click here for help");

		menuBar.add(filemenu);
		menuBar.add(help);
		// Adding file and help option inside menubar

		JMenuItem load = new JMenuItem("Load");
		load.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
		load.setIcon(new ImageIcon("load.png"));
		load.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int response, i;
				File file1;
				JFileChooser fileChooser = new JFileChooser();
				response = fileChooser.showOpenDialog(null); // Select File to open
				if (response == JFileChooser.APPROVE_OPTION) {
					file1 = fileChooser.getSelectedFile();
//					System.out.println(file);
//					new 
					ud1.clear();
					conversionList.removeAllItems();
					loadFile(file1);
					String temp = null;
					for (i = 0; i < ud1.size(); i++) {
						try {
							temp = ud1.get(i).getCurrency1();
							currency_symbol[i] = ud1.get(i).getCurrency_symbol();
							factor[i] = Float.parseFloat(ud1.get(i).getFactor());
							conversionList.addItem(temp);
						} catch (NumberFormatException e2) {
							JOptionPane.showMessageDialog(null, "Invalid Factor!!");
						}
					}

				} else {
					JOptionPane.showMessageDialog(null, "No File Selected");
				}
			}
		});
		filemenu.add(load);

		// Adding MenuItem inside Menu element
		JMenuItem exit = new JMenuItem("Exit");
		exit.setToolTipText("Click here to Exit the app");
		// Adding image icon for exit
		exit.setIcon(new ImageIcon("exit.png"));
		// Adding key-press that selects exit option
		exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_MASK));
		// Adding Actionlistener for exit menu item
		exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0); // When Exit MenuItem is pressed it closes the app
			}
		});
		filemenu.add(exit); // Associating MenuItem with Menu elements

		JMenuItem about = new JMenuItem("About");
		about.setToolTipText("Click here for more info.");
		// Adding Actionlistener for exit menu item
		about.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null,
						"This is GUI based app that performs currency conversion from pound to selected currency and vice versa\n@Nischal 2021\nTBC, Kathmandu");
			}
		});// When About MenuItem is pressed it display a brief description about the
			// functionality of the app

		// Adding image icon for exit
		about.setIcon(new ImageIcon("about.png"));

		// Adding key-press that selects about option
		about.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_MASK));
		help.add(about); //// Associating MenuItem with Menu elements

		return menuBar;
	}

	// Creating a default constructor
	CurrencyPanel() {
		// Opening a default file when the app is opened
		File file = new File("currency.txt");
		// Calling a method to get the content inside the file
		loadFile(file);
		int i;
		ActionListener listener = new ConvertListener();

		reverse = new JCheckBox("Reverse Conversion");// initialize a checkbox called ReverseConversion
		reverse.setBackground(Color.black);
		reverse.setForeground(Color.white);
		reverse.setFont(new Font("MV Boli", Font.PLAIN, 18)); // setting font style and font size
		reverse.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == 1)
					isReverse = true;// if checkBox is checked isReverse is true else isReverse is false
				else
					isReverse = false;

			}

		});
		reverse.setToolTipText("Click here to perform reverse conversion");

		conversionList = new JComboBox();
		conversionList.setFont(new Font("MV Boli", Font.PLAIN, 15));
		String temp = null;
		// Using a loop to store the content of each line of the file
		for (i = 0; i < ud1.size(); i++) {
			temp = ud1.get(i).getCurrency1();
			currency_symbol[i] = ud1.get(i).getCurrency_symbol();
			try {
				factor[i] = Float.parseFloat(ud1.get(i).getFactor());
				conversionList.addItem(temp);
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(null, "Invalid Factor!!");
			}
		}
		conversionList.setToolTipText("Choose the prefered conversion");

		JLabel inputLabel = new JLabel("Enter value:"); // Initializing inputLabel
		inputLabel.setForeground(Color.white);
		inputLabel.setFont(new Font("MV Boli", Font.PLAIN, 18));
		// Creating a button to perform conversion
		convertButton = new JButton("Convert");
		convertButton.addActionListener(listener); // convert values when pressed
		convertButton.setToolTipText("Click here to perform conversion");

		// Creating a button to perform clear operation
		clearButton = new JButton("Clear");
		clearButton.addActionListener(new ActionListener()// adding action listener to crate button
		{
			@Override
			public void actionPerformed(ActionEvent e) {
				reverse.setSelected(false);
				count = 0;
				counter.setText("Conversion Count: 0");
				resultLabel.setText("----");
				input.setText("");
				conversionList.setSelectedIndex(0);
			}
		});
		// When Clear button is pressed it resets all the data
		clearButton.setToolTipText("Click here to clear all data");

		resultLabel = new JLabel("----"); // Initializing a label named ans
		resultLabel.setToolTipText("Result");
		resultLabel.setFont(new Font("MV Boli", Font.PLAIN, 18));
		resultLabel.setForeground(Color.white);

		counter = new JLabel("Conversion Count: 0");
		counter.setToolTipText("Conversion Counter");
		counter.setForeground(Color.white);
		counter.setFont(new Font("MV Boli", Font.PLAIN, 18));
		input = new JTextField(5);
		input.setToolTipText("Enter your value");
		input.setFont(new Font("MV Boli", Font.PLAIN, 18));
		// Adding a keyListener to the textField
		input.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == 10)
					convertButton.doClick();
			}
		});// After user inputs the value and press the enter key it acts as if convert
			// button is pressed and performs the conversion

		add(conversionList);
		add(inputLabel);
		add(input);
		add(resultLabel);
		add(convertButton);// Adding all the components to the Panel
		add(clearButton);
		add(counter);
		add(reverse);

		setPreferredSize(new Dimension(520, 120)); // Setting the prefered diamension of the GUI frame
		setBackground(Color.black); // Setting Background color of the frame
	}

	public void loadFile(File file1) {
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file1), "UTF8"));
			// Creating a scanner object to take input from the file
			scan = new Scanner(in);
			String line;
			String currency1, currency_symbol, factor;
			while (scan.hasNext()) {
				// Process 'line' (split up).

				line = scan.nextLine();
				float fact;
				String[] parts = line.split(",");
				if (parts.length == 3) {
					currency1 = parts[0].trim();
					factor = parts[1].trim();
					try {
						fact = Float.parseFloat(factor);
						currency_symbol = parts[2].trim();
						if (currency1.isEmpty() || currency_symbol.isEmpty()) {
							continue;
						} else {
							ud1.add(new UnitData(currency1, currency_symbol, factor));
						}
					} catch (NumberFormatException e) {
						JOptionPane.showMessageDialog(null, "Invalid Factor!!");
					}

				} else {
					JOptionPane.showMessageDialog(null, "Insufficient value");
				}
				line = in.readLine(); // read next line (if available)
			}

			in.close();

		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "File cannot be opened");
		}
	}

	// ConvertListener class is using an interface named ActionListener
	private class ConvertListener implements ActionListener {
		// Method Overriding
		@Override
		public void actionPerformed(ActionEvent event) {

			String text = input.getText().trim();

			try {
				if (text.isEmpty() == false) // If convert button is pressed and textfield has some data then if
											// statement becomes true
				{
					// Type casting the string data type input into double
					double value = Double.parseDouble(text);

					++count; // Increase the value of count only when conversion is being performed

					double result = 0;

					// Setting up the correct factor and offset values depending on required
					// conversion
					int index = conversionList.getSelectedIndex();
					symbol = currency_symbol[index];
					if (isReverse)// When the value of isReverse is true then it perform reverse operation
					{
						result = (value) / factor[index]; // Reverse conversion formulae
						symbol = "£";
					} else
						result = (value) * factor[index]; // Normal conversion formulae
					String new_result = new DecimalFormat("0.00").format(result);
					String currency_value = symbol.concat(new_result);
					resultLabel.setText(currency_value);
					counter.setText("Conversion Count: " + count);
				} else {
					JOptionPane.showMessageDialog(null, "Field empty!! Please enter a value");
					// IF the textfield is empty it displays a pop up with the given message
				}
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(null, "Please enter a numeric value");
				// When an attempt is made to convert a string with improper format into a
				// numeric value is shows a pop up with appropriate message

			}
		}
	}
}
