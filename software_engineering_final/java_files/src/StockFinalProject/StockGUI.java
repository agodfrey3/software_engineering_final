//Kevin Ho, Andrew Godfrey, Saul Soto
//CISC 3598 - Software Engineering
//Professor Kounavelis

package StockFinalProject;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;

import java.util.ArrayList;
import java.util.Date;
import java.io.File;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class StockGUI extends JFrame
{
	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	private JButton long_button;
	private JButton short_button;
	private JButton leaderboard_button;
	private JButton transaction_button;
	private JLabel account_title_label;
	private JLabel account_label;
	private JLabel lastgained_label;
	private JLabel pointsgained_label;
	private JLabel turn_title_label;
	private JLabel playertext_label;
	private JLabel playercount_label;
	private JLabel turn_label;
	private JLabel ticker_label;
	private JLabel price_label;
	private JTextArea clock_textarea;

	private ArrayList<String> graph_file_paths = new ArrayList<String>();

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
					   String str = String.format("  %tc", date);
					 
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
		StockGUI frame = new StockGUI();
		frame.setVisible(true);
	}

	public void fill_file_array() {
		String main_dir = "./data/";
		File folder = new File(main_dir);
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				String file_name = main_dir + listOfFiles[i].getName();
				System.out.println("File " + file_name);
				graph_file_paths.add(file_name);
			}
		}
	}

	/**
	 * Create the frame.
	 */
	public StockGUI()
	{
		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		}catch(Exception e){
			e.printStackTrace();
		}

		fill_file_array();

		setTitle("The Stock Market Game");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1225, 800);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5)); // Top, Left, Bottom, Right
		setContentPane(contentPane);
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
		balanceleaderboard_panel.setPreferredSize(new Dimension(300,675));
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
		tickerplaybuttons_panel.setPreferredSize(new Dimension(600,675));
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
			int randomNum = ThreadLocalRandom.current().nextInt(0, graph_file_paths.size());

			// Loads a graph image from the data directory.
			BufferedImage stock_graph = ImageIO.read(new File(graph_file_paths.get(randomNum)));
			// Adds the image to a JLabel and then adds it to the JPanel.
			JLabel stock_graph_label = new JLabel(new ImageIcon(stock_graph));
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
		
		//Panel that houses right side of gui
		JPanel right_panel = new JPanel();
		right_panel.setLayout(new BoxLayout(right_panel, BoxLayout.Y_AXIS));
		right_panel.setPreferredSize(new Dimension(300, 675));
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
		account_label = new JLabel("$0", SwingConstants.CENTER);
		account_label.setFont(new Font(account_label.getFont().getName(), account_label.getFont().getStyle(), 15));
		account_label.setAlignmentX(Component.CENTER_ALIGNMENT);
		balanceleaderboard_panel.add(account_label);
		balanceleaderboard_panel.add(Box.createRigidArea(new Dimension(0,20)));
		
		//lasttrans_label = new JLabel("Last Transaction", SwingConstants.CENTER);
		
		lastgained_label = new JLabel("Last Gained:", SwingConstants.CENTER);
		lastgained_label.setFont(new Font(lastgained_label.getFont().getName(), lastgained_label.getFont().getStyle(), 15));
		lastgained_label.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		pointsgained_label = new JLabel("+35", SwingConstants.CENTER);
		pointsgained_label.setAlignmentX(Component.CENTER_ALIGNMENT);
		pointsgained_label.setFont(new Font(pointsgained_label.getFont().getName(), pointsgained_label.getFont().getStyle(), 50));
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

		// Price Label
		price_label = new JLabel("0.00", SwingConstants.CENTER);
		price_label.setFont(new Font(price_label.getFont().getName(), price_label.getFont().getStyle(), 25));
		price_label.setAlignmentX(Component.CENTER_ALIGNMENT);
		tickerplaybuttons_panel.add(price_label);

		// Turn Count Title Label
		turn_title_label = new JLabel("Turn Count", SwingConstants.CENTER);
		turn_title_label.setFont(new Font(turn_title_label.getFont().getName(), turn_title_label.getFont().getStyle(), 15));
		turn_title_label.setAlignmentX(Component.CENTER_ALIGNMENT);
		turntranshistory_panel.add(turn_title_label);
		
		// Real time clock text area
		clock_textarea = new JTextArea();
		clock_textarea.setEditable(false);
		clock_textarea.setFont(new Font("Monospaced", Font.PLAIN, 15));
		clock_textarea.setRows(1);
		clock_textarea.setColumns(36);
		
		// Current players Label	
		playertext_label = new JLabel("Currently Playing: ", SwingConstants.CENTER);
		playertext_label.setFont(new Font(playertext_label.getFont().getName(), playertext_label.getFont().getStyle(), 15));
		playertext_label.setAlignmentX(Component.LEFT_ALIGNMENT);
		playercount_panel.add(playertext_label);

		// Number of players currently playing Label
		playercount_label = new JLabel("0", SwingConstants.CENTER);
		playercount_label.setAlignmentX(Component.CENTER_ALIGNMENT);
		playercount_panel.add(playercount_label);
		right_panel.add(clock_textarea);
		right_panel.add(playercount_panel);
		right_panel.add(Box.createRigidArea(new Dimension(0,10)));
		
		// Turn Count Label
		turn_label = new JLabel("0", SwingConstants.CENTER);
		turn_label.setFont(new Font(turn_label.getFont().getName(), turn_label.getFont().getStyle(), 15));
		turn_label.setAlignmentX(Component.CENTER_ALIGNMENT);
		turntranshistory_panel.add(turn_label);
		
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
		
		// Transaction History Button
		/*transaction_button = new JButton("Transaction History");
		transaction_button.setAlignmentX(Component.CENTER_ALIGNMENT);
		turntranshistory_panel.add(Box.createRigidArea(new Dimension(0,450)));
		turntranshistory_panel.add(transaction_button);*/
		
		long_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				JLabel editable_label = new JLabel();
				editable_label.setText("Long this stock");
				editable_label.setAlignmentX(CENTER_ALIGNMENT);
				editable_label.setFont(new Font("Monospaced", Font.BOLD, 15));
				editable_label.setForeground(new Color(35, 142, 57));

				transhistorydisplay_panel.add(editable_label, 0);//, gbc);
				transhistorydisplay_panel.add(Box.createRigidArea(new Dimension(0,10)),0);
				transhistorydisplay_panel.validate();
				transhistorydisplay_panel.repaint();
				
				Thread t = new Thread(new Runnable()
				{
					public void run()
					{
						SocketUtilities su = new SocketUtilities();

						if (su.socketConnect() == true)
						{
							/*su.sendMessage("Long");
							String recvMsgStr = su.recvMessage();
							su.sendMessage("QUIT>");
							*/
							
							ObjectOutputStream outputStream;
							try {
								outputStream = new ObjectOutputStream(su.clientSocket.getOutputStream());
								String[] names = new String[1]; // Empty at the moment
								names[0] = "Fart";
								outputStream.writeObject(names[0]);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							su.closeSocket();

							/*JOptionPane.showMessageDialog(null,
									"Message : " + recvMsgStr,
									"Client",
									JOptionPane.WARNING_MESSAGE);*/
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
		});
		
		short_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				JLabel editable_label = new JLabel();
				editable_label.setText("Short this stock");
				editable_label.setFont(new Font("Monospaced", Font.BOLD, 15));
				editable_label.setForeground(Color.red);
				editable_label.setAlignmentX(CENTER_ALIGNMENT);

				transhistorydisplay_panel.add(editable_label,0);//, gbc);
				transhistorydisplay_panel.add(Box.createRigidArea(new Dimension(0,10)),0);
				transhistorydisplay_panel.validate();
				transhistorydisplay_panel.repaint();
				
				Thread t = new Thread(new Runnable()
				{
					public void run()
					{
						SocketUtilities su = new SocketUtilities();

						if (su.socketConnect() == true)
						{
							su.sendMessage("Short");
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
		});
		
		leaderboard_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				Thread t = new Thread(new Runnable()
				{
					public void run()
					{
						SocketUtilities su = new SocketUtilities();

						if (su.socketConnect() == true)
						{
							su.sendMessage("Hello there.");
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
		contentPane.add(encompassing_panel);

		startRealTimeClock();
		this.setLocationRelativeTo(null);
	}
}
