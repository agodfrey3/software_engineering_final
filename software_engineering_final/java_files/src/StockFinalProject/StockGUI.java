//Kevin Ho, Andrew Godfrey, Saul Soto
//CISC 3598 - Software Engineering
//Professor Kounavelis

package StockFinalProject;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
//import javax.swing.text.BadLocationException;
//import javax.swing.text.DefaultCaret;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
//import java.util.Vector;
import java.io.File;
//import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
//import java.awt.GridBagConstraints;
//import java.awt.GridBagLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
//import java.io.ObjectOutputStream;
import java.util.concurrent.ThreadLocalRandom;
//import java.util.concurrent.TimeUnit;
import java.awt.Graphics2D;

public class StockGUI extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	private static JFrame app_frame;
	private JPanel contentPane;
	
	private static SGUsers newuser_obj = new SGUsers();
	private static String user_key;
	private JButton long_button;
	private JButton short_button;
	private JButton leaderboard_button;
	private JLabel account_title_label;
	private JLabel account_label;
	private JLabel lastgained_label;
	private JLabel pointsgained_label;
	private JLabel turn_title_label;
	private JLabel playertext_label;
	private JLabel playercount_label;
	private JLabel turncounter_label;
	private JLabel ticker_label;
	private JLabel price_label;
	private JTextArea clock_textarea;

	private ArrayList<String> graph_file_paths = new ArrayList<String>();

	public static int player_count = 1;
	public static double account_balance = 100000.00;
	
	public static int randomNum;
	public static String graph_file_to_be_parsed;
	public static double current_price;
	public static double future_price;
	public static double net;
	public static String ticker;
	public static double last_gained = 0.0;
	
	public static final double leverage = 5000.0; // How many shares of each stock you transact with
	
	/**
	 * Launch the application.
	 */
	
	private void startRealTimeClock()
	  {	
		   Thread refreshClock = new Thread()
		   {
			  public void run()
			  {   
				 while (true)
				 {	 			      
					   Date date = new Date();
					   String str = String.format("     %tc", date);
					 
				   clock_textarea.setText("");
				   clock_textarea.append(str);
				   clock_textarea.repaint();
				   
			    	try
				    {
					   sleep(1000L);
				    }
				    catch (InterruptedException e)
				   {
					   // TODO Auto-generated catch block
					  e.printStackTrace();
				   }
	         } // end while true 
		     }
		  };
	
	    refreshClock.start();
	  }
	
	public static void main(String[] args)
	{
		createStartingFrame();
	}

	public static String createUserKey(SGUsers user_obj) {
		   //String user_key = String.format("|%020d|", user_obj.getUserName());
		   Random rand = new Random();
		   int  m = rand.nextInt(100) + 1;
		   int n = rand.nextInt(100) + 1;
		   
		   String user_key = Integer.toString(m) + user_obj.getUserName() + Integer.toString(n);
		   
		   return user_key;
	}
	
	// Function creates frame/window that asks for user's desired username
	// before transitioning to the stock market game
	public static void createStartingFrame() {
		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		}catch(Exception e){
			e.printStackTrace();
		}

		app_frame = new JFrame();
		app_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		app_frame.setSize(500,90);
		app_frame.setLocationRelativeTo(null);
		app_frame.setTitle("Welcome!");
		
		JPanel encompassing_panel = new JPanel();
		encompassing_panel.setBackground(new Color(24, 110, 155));
		app_frame.setContentPane(encompassing_panel);
		encompassing_panel.setLayout(new BoxLayout(encompassing_panel, BoxLayout.Y_AXIS));
		
		JPanel field_panel = new JPanel();
		field_panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		field_panel.setBackground(new Color(24, 110, 155));
		
		JLabel username_label = new JLabel("Choose your username:", SwingConstants.CENTER);
		username_label.setFont(new Font("Lucida Sans Unicode", Font.BOLD, 15));
		username_label.setForeground(Color.white);
		
		JTextField username_textfield = new JTextField();
		username_textfield.setColumns(12);
		username_textfield.setPreferredSize(new Dimension(0,25));
		
		JButton accept_button = new JButton("Accept");
		accept_button.setPreferredSize(new Dimension(150, 25));
		accept_button.setBackground(new Color(196, 236, 237));
		accept_button.setOpaque(true);
		accept_button.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		field_panel.add(username_label);
		field_panel.add(username_textfield);
		encompassing_panel.add(field_panel);
		encompassing_panel.add(Box.createRigidArea(new Dimension(0,5)));
		encompassing_panel.add(accept_button);
		encompassing_panel.add(Box.createRigidArea(new Dimension(0,5)));
		app_frame.setContentPane(encompassing_panel);
		app_frame.setVisible(true);
				
		accept_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{	
				StockGUI game_frame = new StockGUI();
				app_frame.getContentPane().removeAll();
				app_frame.setContentPane(game_frame);
				app_frame.revalidate();
				app_frame.pack();
				app_frame.setLocationRelativeTo(null);
				app_frame.setSize(new Dimension(1225, 715));
				newuser_obj.setUserName(username_textfield.getText());
				Thread t = new Thread(new Runnable()
				{
					public void run()
					{
						SocketUtilities su = new SocketUtilities();
						if (su.socketConnect() == true)
						{
							user_key = createUserKey(newuser_obj);
							SGUserKO userKO = new SGUserKO(user_key,newuser_obj);
							su.sendUserKO(userKO);
							
					        //System.out.println("returned key is = " + user_key);
							
							su.closeSocket();
						}
						else
						{
							JOptionPane.showMessageDialog(null,
									"ERROR: Connection to Socket Server is down!",
									"Socket Server Error",
									JOptionPane.WARNING_MESSAGE);
						}
					}
				});
				t.start();
				app_frame.setTitle("Stock Market Game: Welcome " + newuser_obj.getUserName() + "!");
				app_frame.setVisible(true);
			}	
		});
	}
	
	public void fill_file_array() {
		String main_dir = "./data/";
		File folder = new File(main_dir);
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				String file_name = main_dir + listOfFiles[i].getName();
//				System.out.println("File " + file_name);
				graph_file_paths.add(file_name);
			}
		}
	}
	
	// Calculates the gain/loss from longing or shorting a stock and updates the frames of the GUI
	public void GameLogic(String long_or_short) {
		graph_file_to_be_parsed = graph_file_paths.get(randomNum);
		String cleaned_file_name = graph_file_to_be_parsed.substring(7, graph_file_to_be_parsed.length() - 4);
		
		// parse file name
		current_price = Double.parseDouble(cleaned_file_name.split("_")[0]);
		future_price = Double.parseDouble(cleaned_file_name.split("_")[1]);
		ticker = cleaned_file_name.split("_")[2];
		
		// calculate the loss/gain
		
		if (long_or_short == "long" || long_or_short == "short") {
			net = leverage * (future_price - current_price);

			if (long_or_short == "short") // if it's a short then negate the net
				net *= -1;

			if (net < 0) // losses are doubled
				net *= 2;

			account_balance += net;
			last_gained = net;
		}
	}
	
	public void ResetGame() {
		account_balance = 100000.00;
		net = 0.0;
		newuser_obj.setTurnCounter(0);
		account_label.setText("$" + String.format("%.2f", account_balance));
		pointsgained_label.setText("$" + String.format("%.2f", net));
		turncounter_label.setText(Integer.toString(newuser_obj.getTurnCounter()));
	}
	
	/**
	 * Create the frame.
	 */
	public StockGUI()
	{	
		fill_file_array();
		//stockslonged_vector.add("Longed ");
		//stocksshorted_vector.add("Shorted ");
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5)); // Top, Left, Bottom, Right
		contentPane.setBackground(new Color(24, 110, 155));

		// Second-degree panel to contentPane
		// Panel within contentPane
		JPanel encompassing_panel = new JPanel();
		// Organizes components from left to right, starting with the first added component
		FlowLayout encompFlow = new FlowLayout(FlowLayout.CENTER, 5, 5);
		encompassing_panel.setLayout(encompFlow);
		encompassing_panel.setBackground(new Color(24, 110, 155));

		// Panel that houses users remaining balance label, balance amount, points gained panel/label,
		// and the leaderboard button
		JPanel balanceleaderboard_panel = new JPanel();
		// Organizes components from top to bottom, starting with the first added component
		balanceleaderboard_panel.setLayout(new BoxLayout(balanceleaderboard_panel, BoxLayout.Y_AXIS));
		balanceleaderboard_panel.setPreferredSize(new Dimension(275,675));
		balanceleaderboard_panel.add(Box.createRigidArea(new Dimension(0,40)));
		balanceleaderboard_panel.setBackground(new Color(196, 236, 237));
		balanceleaderboard_panel.setBorder(BorderFactory.createLineBorder(new Color(127, 194, 198),5));

		JPanel pointsgained_panel = new JPanel();
		pointsgained_panel.setBackground(new Color(196, 236, 237));
		pointsgained_panel.setBorder(BorderFactory.createMatteBorder(5, 0, 5, 0, new Color(127, 194, 198)));
		
		// Panel houses dynamic text area
		JPanel transhistorydisplay_panel = new JPanel();
		transhistorydisplay_panel.setLayout(new BoxLayout(transhistorydisplay_panel, BoxLayout.Y_AXIS));
		transhistorydisplay_panel.setBackground(Color.white);
		
		/*GridBagConstraints gbc = new GridBagConstraints();
        //gbc.gridx = 0;
        //gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.weighty = 1D;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = GridBagConstraints.REMAINDER;

		//DefaultCaret caret = (DefaultCaret)transhistory_display.getCaret();
		//caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);*/
		
		// Panel that allows text area to be scrollable if first dimensions are filled 
		JScrollPane transhistory_scrollpane = new JScrollPane(transhistorydisplay_panel);
		transhistory_scrollpane.setPreferredSize(new Dimension(300,450));
		
		// Panel that houses users the ticker label, price label, stock graph panel, and the
		// playbuttons_panel
		JPanel tickerplaybuttons_panel = new JPanel();
		tickerplaybuttons_panel.setLayout(new BoxLayout(tickerplaybuttons_panel, BoxLayout.Y_AXIS));
		tickerplaybuttons_panel.setPreferredSize(new Dimension(650,675));
		tickerplaybuttons_panel.add(Box.createRigidArea(new Dimension(0,15))); //Pushes the ticker label to a lower position
		tickerplaybuttons_panel.setBackground(new Color(196, 236, 237));
		tickerplaybuttons_panel.setBorder(BorderFactory.createLineBorder(new Color(127, 194, 198),5));

		// Panel that houses the stock graph
		JPanel stockgraph_panel = new JPanel();
		// Width dimension doesn't matter when using a flowlayout
		stockgraph_panel.setBackground(new Color(127, 194, 198));
		stockgraph_panel.setBorder(BorderFactory.createMatteBorder(3, 0, 0, 0, new Color(127, 194, 198)));
		
		try {
			// Random number to choose a random file from the list of graphs.
			randomNum = ThreadLocalRandom.current().nextInt(0, graph_file_paths.size());

			// Loads a graph image from the data directory.
			BufferedImage stock_graph = ImageIO.read(new File(graph_file_paths.get(randomNum)));
			
			int type = stock_graph.getType() == 0? BufferedImage.TYPE_INT_ARGB
                    : stock_graph.getType();
			
			BufferedImage resizedImage = new BufferedImage(590, 490, type);
			Graphics2D g = resizedImage.createGraphics();
			g.drawImage(stock_graph, 0, 0, 590, 490, null);
			g.dispose();
			
			// Adds the image to a JLabel and then adds it to the JPanel.
			JLabel stock_graph_label = new JLabel(new ImageIcon(resizedImage));
			stockgraph_panel.add(stock_graph_label);
		}
		catch (IOException e){
			// In case of file not found error. Could potentially handle this more gracefully.
			e.printStackTrace();
		}

		// Panel that houses the long button and short buttons
		JPanel playbuttons_panel = new JPanel();
		playbuttons_panel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 0));
		playbuttons_panel.setBackground(new Color(196, 236, 237));
		playbuttons_panel.setAlignmentX(Component.CENTER_ALIGNMENT);

		//Panel for number of player counter
		JPanel playercount_panel = new JPanel();
		playercount_panel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 0));
		playercount_panel.setBackground(new Color(196, 236, 237));
		playercount_panel.setBorder(BorderFactory.createMatteBorder(5, 0, 5, 0, new Color(127, 194, 198)));
		
		//Panel that houses right side of GUI
		JPanel right_panel = new JPanel();
		right_panel.setLayout(new BoxLayout(right_panel, BoxLayout.Y_AXIS));
		right_panel.setPreferredSize(new Dimension(275, 675));
		right_panel.setBackground(new Color(196, 236, 237));
		right_panel.setBorder(BorderFactory.createLineBorder(new Color(127, 194, 198),5));
		
		// Panel that houses the turn counter (title label and count label) and
		// the transaction history button
		JPanel turntranshistory_panel = new JPanel();
		turntranshistory_panel.setLayout(new BoxLayout(turntranshistory_panel, BoxLayout.Y_AXIS));
		turntranshistory_panel.setPreferredSize(new Dimension(300,545)); //570
		turntranshistory_panel.add(Box.createRigidArea(new Dimension(0,22)));
		turntranshistory_panel.setBackground(new Color(196, 236, 237));

		// Account Balance Title Label
		account_title_label = new JLabel("Account Balance", SwingConstants.CENTER);
		account_title_label.setFont(new Font(account_title_label.getFont().getName(), account_title_label.getFont().getStyle(), 15));
		account_title_label.setAlignmentX(Component.CENTER_ALIGNMENT);
		balanceleaderboard_panel.add(account_title_label);
		
		// Account Balance Label
//		account_label = new JLabel(Double.toString(newuser_obj.getBalance()), SwingConstants.CENTER);
		account_label = new JLabel("$" + String.format("%.2f", account_balance), SwingConstants.CENTER);

		account_label.setFont(new Font(account_label.getFont().getName(), account_label.getFont().getStyle(), 15));
		account_label.setAlignmentX(Component.CENTER_ALIGNMENT);
		balanceleaderboard_panel.add(account_label);
		balanceleaderboard_panel.add(Box.createRigidArea(new Dimension(0,20)));
		
		//lasttrans_label = new JLabel("Last Transaction", SwingConstants.CENTER);
		
		lastgained_label = new JLabel("Last Gained:", SwingConstants.CENTER);
		lastgained_label.setFont(new Font(lastgained_label.getFont().getName(), lastgained_label.getFont().getStyle(), 15));
		lastgained_label.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		pointsgained_label = new JLabel("$0", SwingConstants.CENTER);
		pointsgained_label.setAlignmentX(Component.CENTER_ALIGNMENT);
		pointsgained_label.setFont(new Font(pointsgained_label.getFont().getName(), pointsgained_label.getFont().getStyle(), 20));
		pointsgained_label.setPreferredSize(new Dimension(125, 100));
		
		pointsgained_panel.add(lastgained_label);
		pointsgained_panel.add(pointsgained_label);
		balanceleaderboard_panel.add(pointsgained_panel);
		balanceleaderboard_panel.add(transhistory_scrollpane);//transhistorydisplay_panel);
		
		// Ticker Label
		ticker_label = new JLabel("Ticker", SwingConstants.CENTER);
		ticker_label.setFont(new Font(ticker_label.getFont().getName(), ticker_label.getFont().getStyle(), 25));
		ticker_label.setAlignmentX(Component.CENTER_ALIGNMENT);
		tickerplaybuttons_panel.add(ticker_label);

		GameLogic("long");
		account_balance -= net;
		ticker_label.setText(ticker.toUpperCase());
		
		// Price Label
		price_label = new JLabel("0.00", SwingConstants.CENTER);
		price_label.setFont(new Font(price_label.getFont().getName(), price_label.getFont().getStyle(), 25));
		price_label.setAlignmentX(Component.CENTER_ALIGNMENT);
		tickerplaybuttons_panel.add(price_label);

		price_label.setText(String.format("%.2f", current_price));
		
		// Turn Count Title Label
		turn_title_label = new JLabel("Turn Count", SwingConstants.CENTER);
		turn_title_label.setFont(new Font(turn_title_label.getFont().getName(), turn_title_label.getFont().getStyle(), 15));
		turn_title_label.setAlignmentX(Component.CENTER_ALIGNMENT);
		turntranshistory_panel.add(turn_title_label);
		
		// Real time clock text area
		clock_textarea = new JTextArea();
		clock_textarea.setEditable(false);
		clock_textarea.setFont(new Font("Lucia Sans Unicode", Font.PLAIN, 15));
		clock_textarea.setRows(1);
		clock_textarea.setColumns(36);
		
		// Current players Label	
		playertext_label = new JLabel("Currently Playing: ", SwingConstants.CENTER);
		playertext_label.setFont(new Font(playertext_label.getFont().getName(), playertext_label.getFont().getStyle(), 15));
		playertext_label.setAlignmentX(Component.LEFT_ALIGNMENT);
		playercount_panel.add(playertext_label);

		// Number of players currently playing Label
		playercount_label = new JLabel(Integer.toString(player_count), SwingConstants.CENTER);
		playercount_label.setAlignmentX(Component.CENTER_ALIGNMENT);
		playercount_panel.add(playercount_label);
		right_panel.add(clock_textarea);
		right_panel.add(playercount_panel);
		right_panel.add(Box.createRigidArea(new Dimension(0,10)));
		
		// Turn Count Label
		turncounter_label = new JLabel(Integer.toString(newuser_obj.getTurnCounter()), SwingConstants.CENTER);
		turncounter_label.setFont(new Font(turncounter_label.getFont().getName(), turncounter_label.getFont().getStyle(), 15));
		turncounter_label.setAlignmentX(Component.CENTER_ALIGNMENT);
		turntranshistory_panel.add(turncounter_label);
		
		// Long Button
		long_button = new JButton("Long");
		long_button.setPreferredSize(new Dimension(150, 50));
		long_button.setBackground(new Color(7, 206, 67));
		long_button.setOpaque(true);
		playbuttons_panel.add(long_button);

		// Short Button
		short_button = new JButton("Short");
		short_button.setPreferredSize(new Dimension(150,50));
		short_button.setBackground(Color.RED);
		playbuttons_panel.add(short_button);

		// Leaderboard Button
		leaderboard_button = new JButton("Leaderboard");
		leaderboard_button.setForeground(Color.white);
		leaderboard_button.setPreferredSize(new Dimension(150,50));
		leaderboard_button.setBackground(new Color(6, 135, 137));
		leaderboard_button.setOpaque(true);
		leaderboard_button.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		long_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{	
				//newuser_obj.addToTurnCounter();
				
				turncounter_label.setText(Integer.toString(newuser_obj.getTurnCounter()));
				// Game Logic
				GameLogic("long");
				account_label.setText("$" + String.format("%.2f", account_balance));
				pointsgained_label.setText("$" + String.format("%.2f", net));
						
				JLabel editable_label = new JLabel();
				
				GameLogic("update");
				
				editable_label.setText("Long " + ticker.toUpperCase());
				editable_label.setAlignmentX(CENTER_ALIGNMENT);
				editable_label.setFont(new Font("Monospaced", Font.BOLD, 15));
				editable_label.setForeground(new Color(35, 142, 57));
				
				transhistorydisplay_panel.add(editable_label, 0);
				transhistorydisplay_panel.add(Box.createRigidArea(new Dimension(0,10)),0);
				transhistorydisplay_panel.validate();
				transhistorydisplay_panel.repaint();
				
				try {
					stockgraph_panel.removeAll();
					
					// Random number to choose a random file from the list of graphs.
					randomNum = ThreadLocalRandom.current().nextInt(0, graph_file_paths.size());
	
					// Loads a graph image from the data directory.
					BufferedImage stock_graph = ImageIO.read(new File(graph_file_paths.get(randomNum)));
							
					int type = stock_graph.getType() == 0? BufferedImage.TYPE_INT_ARGB : stock_graph.getType();
							
					BufferedImage resizedImage = new BufferedImage(590, 490, type);
					Graphics2D g = resizedImage.createGraphics();
					g.drawImage(stock_graph, 0, 0, 590, 490, null);
					g.dispose();
							
					// Adds the image to a JLabel and then adds it to the JPanel.
					JLabel stock_graph_label = new JLabel(new ImageIcon(resizedImage));
					stockgraph_panel.add(stock_graph_label);
				}
				catch (IOException i){
					// In case of file not found error. Could potentially handle this more gracefully.
					i.printStackTrace();
				}
				
				graph_file_to_be_parsed = graph_file_paths.get(randomNum);
				String cleaned_file_name = graph_file_to_be_parsed.substring(7, graph_file_to_be_parsed.length() - 4);
				current_price = Double.parseDouble(cleaned_file_name.split("_")[0]);
				ticker = cleaned_file_name.split("_")[2];
				
				price_label.setText(String.format("%.2f", current_price));
				ticker_label.setText(ticker.toUpperCase());
				
				if (account_balance <= 0) {
					if (JOptionPane.showConfirmDialog(null, "Game Over, you lasted " + Integer.toString(newuser_obj.getTurnCounter()) + " turn(s).\n\nPlay Again?", "Game Over!",
					        JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
						ResetGame();
						transhistorydisplay_panel.removeAll();
						transhistorydisplay_panel.revalidate();
						transhistorydisplay_panel.repaint();
						
						try {
							stockgraph_panel.removeAll();
							
							// Random number to choose a random file from the list of graphs.
							randomNum = ThreadLocalRandom.current().nextInt(0, graph_file_paths.size());
			
							// Loads a graph image from the data directory.
							BufferedImage stock_graph = ImageIO.read(new File(graph_file_paths.get(randomNum)));
									
							int type = stock_graph.getType() == 0? BufferedImage.TYPE_INT_ARGB : stock_graph.getType();
									
							BufferedImage resizedImage = new BufferedImage(590, 490, type);
							Graphics2D g = resizedImage.createGraphics();
							g.drawImage(stock_graph, 0, 0, 590, 490, null);
							g.dispose();
									
							// Adds the image to a JLabel and then adds it to the JPanel.
							JLabel stock_graph_label = new JLabel(new ImageIcon(resizedImage));
							stockgraph_panel.add(stock_graph_label);
						}
						catch (IOException i) {
							// In case of file not found error. Could potentially handle this more gracefully.
							i.printStackTrace();
						}
						
						GameLogic("update");
						price_label.setText(String.format("%.2f", current_price));
						ticker_label.setText(ticker.toUpperCase());
						
					} else {
					    System.exit(0);
					}
				}
				
				else {
					newuser_obj.addToStocksLonged("New Stock");

					Thread t = new Thread(new Runnable()
					{
						public void run()
						{
							SocketUtilities su = new SocketUtilities();
							if (su.socketConnect() == true)
							{	
								
								su.sendKey(user_key);
								//su.incrementLS(user_key);

								//							ObjectOutputStream outputStream;
								//							try {
								//								outputStream = new ObjectOutputStream(su.clientSocket.getOutputStream());
								//								String[] names = new String[1]; // Empty at the moment
								//								names[0] = "Blah";
								//								String temp = "blah";
								//								outputStream.writeObject(temp);
								//							} catch (IOException e) {
								//								// TODO Auto-generated catch block
								//								e.printStackTrace();
								//							}

								//su.closeSocket();
							}
							else
							{
								JOptionPane.showMessageDialog(null,
										"ERROR: Connection to Socket Server is Down!",
										"Client",
										JOptionPane.WARNING_MESSAGE);
							}
						}
					});
					t.start();
				}
			}
		});
				
		short_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				newuser_obj.addToTurnCounter();
				turncounter_label.setText(Integer.toString(newuser_obj.getTurnCounter()));
				// Game Logic
				GameLogic("short");
				account_label.setText("$" + String.format("%.2f", account_balance));
				pointsgained_label.setText("$" + String.format("%.2f", net));
				
				GameLogic("update");
				
				JLabel editable_label = new JLabel();
				editable_label.setText("Short " + ticker.toUpperCase());
				editable_label.setFont(new Font("Monospaced", Font.BOLD, 15));
				editable_label.setForeground(Color.red);
				editable_label.setAlignmentX(CENTER_ALIGNMENT);

				transhistorydisplay_panel.add(editable_label,0);
				transhistorydisplay_panel.add(Box.createRigidArea(new Dimension(0,10)),0);
				transhistorydisplay_panel.validate();
				transhistorydisplay_panel.repaint();
				
				try {
					stockgraph_panel.removeAll();
					
					// Random number to choose a random file from the list of graphs.
					randomNum = ThreadLocalRandom.current().nextInt(0, graph_file_paths.size());
	
					// Loads a graph image from the data directory.
					BufferedImage stock_graph = ImageIO.read(new File(graph_file_paths.get(randomNum)));
							
					int type = stock_graph.getType() == 0? BufferedImage.TYPE_INT_ARGB : stock_graph.getType();
							
					BufferedImage resizedImage = new BufferedImage(590, 490, type);
					Graphics2D g = resizedImage.createGraphics();
					g.drawImage(stock_graph, 0, 0, 590, 490, null);
					g.dispose();
							
					// Adds the image to a JLabel and then adds it to the JPanel.
					JLabel stock_graph_label = new JLabel(new ImageIcon(resizedImage));
					stockgraph_panel.add(stock_graph_label);
				}
				catch (IOException i){
					// In case of file not found error. Could potentially handle this more gracefully.
					i.printStackTrace();
				}
				
				graph_file_to_be_parsed = graph_file_paths.get(randomNum);
				String cleaned_file_name = graph_file_to_be_parsed.substring(7, graph_file_to_be_parsed.length() - 4);
				current_price = Double.parseDouble(cleaned_file_name.split("_")[0]);
				ticker = cleaned_file_name.split("_")[2];
				
				price_label.setText(String.format("%.2f", current_price));
				ticker_label.setText(ticker.toUpperCase());
				
				if (account_balance <= 0) {
					if (JOptionPane.showConfirmDialog(null, "Game Over, you lasted " + Integer.toString(newuser_obj.getTurnCounter()) + " turn(s).\n\nPlay Again?", "Game Over!",
					        JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
						ResetGame();
						transhistorydisplay_panel.removeAll();
						transhistorydisplay_panel.revalidate();
						transhistorydisplay_panel.repaint();
						
						try {
							stockgraph_panel.removeAll();
							
							// Random number to choose a random file from the list of graphs.
							randomNum = ThreadLocalRandom.current().nextInt(0, graph_file_paths.size());
			
							// Loads a graph image from the data directory.
							BufferedImage stock_graph = ImageIO.read(new File(graph_file_paths.get(randomNum)));
									
							int type = stock_graph.getType() == 0? BufferedImage.TYPE_INT_ARGB : stock_graph.getType();
									
							BufferedImage resizedImage = new BufferedImage(590, 490, type);
							Graphics2D g = resizedImage.createGraphics();
							g.drawImage(stock_graph, 0, 0, 590, 490, null);
							g.dispose();
									
							// Adds the image to a JLabel and then adds it to the JPanel.
							JLabel stock_graph_label = new JLabel(new ImageIcon(resizedImage));
							stockgraph_panel.add(stock_graph_label);
						}
						catch (IOException i){
							// In case of file not found error. Could potentially handle this more gracefully.
							i.printStackTrace();
						}
						
						GameLogic("update");
						price_label.setText(String.format("%.2f", current_price));
						ticker_label.setText(ticker.toUpperCase());
						
					} else {
					    System.exit(0);
					}
				}
				
				else {

					newuser_obj.addToStocksShorted("New Stock");

					Thread t = new Thread(new Runnable()
					{
						public void run()
						{
							SocketUtilities su = new SocketUtilities();
							if (su.socketConnect() == true)
							{	
								//su.sendMessage(stocksshorted_vector);

								//							String recvMsgStr = su.recvMessage();
								//							su.sendMessage("QUIT>");
								//
								//							su.closeSocket();
								//
								//							JOptionPane.showMessageDialog(null,
								//									"Message : " + recvMsgStr,
								//									"Client",
								//									JOptionPane.WARNING_MESSAGE);
							}
							else
							{
								JOptionPane.showMessageDialog(null,
										"ERROR: Connection to Socket Server is Down!",
										"Client",
										JOptionPane.WARNING_MESSAGE);
							}
						}
					});
					t.start();
				}
			}
		});
		
		leaderboard_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
//				Thread t = new Thread(new Runnable()
//				{
//					public void run()
//					{
//						SocketUtilities su = new SocketUtilities();
//
//						if (su.socketConnect() == true)
//						{
//							su.sendMessage("Hello there.");
//							String recvMsgStr = su.recvMessage();
//							su.sendMessage("QUIT>");
//
//							su.closeSocket();
//
//							JOptionPane.showMessageDialog(null,
//									"Message : " + recvMsgStr,
//									"Client",
//									JOptionPane.WARNING_MESSAGE);
//						}
//						else
//						{
//							JOptionPane.showMessageDialog(null,
//									"ERROR: Connection to Socket Server is Down!",
//									"Client",
//									JOptionPane.WARNING_MESSAGE);
//						}
//					}
//				});
//				t.start();
			}
		});
		
		/*transaction_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				Thread t = new Thread(new Runnable()
				{
					public void run()
					{
						SocketUtilities su = new SocketUtilities();

						if (su.socketConnect() == true)
						{
							su.sendMessage("Transaction History");
							String recvMsgStr = su.recvMessage();
							su.sendMessage("QUIT>");

							su.closeSocket();

							JOptionPane.showMessageDialog(null,
									"Message : " + recvMsgStr,
									"Client",
									JOptionPane.WARNING_MESSAGE);
						}
						else
						{
							JOptionPane.showMessageDialog(null,
									"ERROR: Connection to Socket Server is Down!",
									"Client",
									JOptionPane.WARNING_MESSAGE);
						}
					}
				});
				t.start();
			}
		});*/
		
		encompassing_panel.add(balanceleaderboard_panel);
		tickerplaybuttons_panel.add(Box.createRigidArea(new Dimension(0,20)));
		tickerplaybuttons_panel.add(stockgraph_panel);
		tickerplaybuttons_panel.add(Box.createRigidArea(new Dimension(0,10)));
		tickerplaybuttons_panel.add(playbuttons_panel);
		encompassing_panel.add(tickerplaybuttons_panel);
		right_panel.add(turntranshistory_panel);
		right_panel.add(Box.createRigidArea(new Dimension(0,10)));
		right_panel.add(leaderboard_button);
		right_panel.add(Box.createRigidArea(new Dimension(0,25)));
		encompassing_panel.add(right_panel);
		add(encompassing_panel);

		startRealTimeClock();
	}
}