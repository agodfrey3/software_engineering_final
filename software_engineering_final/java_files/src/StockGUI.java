//Kevin Ho, Andrew Godfrey, Saul Soto
//CISC 3598 - Software Engineering
//Professor Kounavelis

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

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
	private JLabel turn_title_label;
	private JLabel turn_label;
	private JLabel ticker_label;
	private JLabel price_label;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		StockGUI frame = new StockGUI();
		frame.setVisible(true);
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
		
		setTitle("The Stock Market Game");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1200, 800);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		// Second-degree panel to contentPane
		// Panel within contentPane
		JPanel encompassing_panel = new JPanel();
		// Organizes components from left to right, starting with the first added component
		FlowLayout encompFlow = new FlowLayout(FlowLayout.LEFT, 0, 0);
		encompassing_panel.setLayout(encompFlow);
		
		// Panel that houses users remaining balance label and amount
		// as well as the leaderboard button
		JPanel balanceleaderboard_panel = new JPanel();
		// Organizes components from top to bottom, starting with the first added component
		balanceleaderboard_panel.setLayout(new BoxLayout(balanceleaderboard_panel, BoxLayout.Y_AXIS));
		balanceleaderboard_panel.setPreferredSize(new Dimension(325,800));
		balanceleaderboard_panel.add(Box.createRigidArea(new Dimension(0,50)));
		
		// Panel that houses users the ticker label, price label, and the
		// playbuttons_panel
		JPanel tickerplaybuttons_panel = new JPanel();
		tickerplaybuttons_panel.setLayout(new BoxLayout(tickerplaybuttons_panel, BoxLayout.Y_AXIS));
		tickerplaybuttons_panel.setPreferredSize(new Dimension(550,800));
		tickerplaybuttons_panel.add(Box.createRigidArea(new Dimension(0,100)));
		
		// Panel that houses the long button and short buttons
		JPanel playbuttons_panel = new JPanel();
		playbuttons_panel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 0));
		playbuttons_panel.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		// Panel that houses the turn counter (title label and count label) and
		// the transaction history button
		JPanel turntranshistory_panel = new JPanel();
		turntranshistory_panel.setLayout(new BoxLayout(turntranshistory_panel, BoxLayout.Y_AXIS));
		turntranshistory_panel.setPreferredSize(new Dimension(325,800));
		turntranshistory_panel.add(Box.createRigidArea(new Dimension(0,50)));
		
		// Account Balance Title Label
		account_title_label = new JLabel("Account Balance", SwingConstants.CENTER);
		account_title_label.setAlignmentX(Component.CENTER_ALIGNMENT);
		balanceleaderboard_panel.add(account_title_label);
		
		// Account Balance Label
		account_label = new JLabel("$0", SwingConstants.CENTER);
		account_label.setAlignmentX(Component.CENTER_ALIGNMENT);
		balanceleaderboard_panel.add(account_label);
				
		// Ticker Label
		ticker_label = new JLabel("Ticker", SwingConstants.CENTER);
		ticker_label.setAlignmentX(Component.CENTER_ALIGNMENT);
		tickerplaybuttons_panel.add(ticker_label);
		
		// Price Label
		price_label = new JLabel("0.00", SwingConstants.CENTER);
		price_label.setAlignmentX(Component.CENTER_ALIGNMENT);
		tickerplaybuttons_panel.add(price_label);
				
		// Turn Count Title Label
		turn_title_label = new JLabel("Turn Count", SwingConstants.CENTER);
		turn_title_label.setAlignmentX(Component.CENTER_ALIGNMENT);
		turntranshistory_panel.add(turn_title_label);
		
		// Turn Count Label
		turn_label = new JLabel("0", SwingConstants.CENTER);
		turn_label.setAlignmentX(Component.CENTER_ALIGNMENT);
		turntranshistory_panel.add(turn_label);
		
		// Long Button
		long_button = new JButton("Long");
		long_button.setPreferredSize(new Dimension(150, 50));
		long_button.setBackground(Color.GREEN);
		long_button.setOpaque(true);
		playbuttons_panel.add(long_button);
		
		// Short Button
		short_button = new JButton("Short");
		short_button.setPreferredSize(new Dimension(150,50));
		short_button.setBackground(Color.RED);
		playbuttons_panel.add(short_button);
		
		// Leaderboard Button
		leaderboard_button = new JButton("Leaderboard");
		leaderboard_button.setAlignmentX(Component.CENTER_ALIGNMENT);
		balanceleaderboard_panel.add(Box.createRigidArea(new Dimension(0,500)));
		balanceleaderboard_panel.add(leaderboard_button);
		
		// Transaction History Button
		transaction_button = new JButton("Transaction History");
		transaction_button.setAlignmentX(Component.CENTER_ALIGNMENT);
		turntranshistory_panel.add(Box.createRigidArea(new Dimension(0,500)));
		turntranshistory_panel.add(transaction_button);
		
		encompassing_panel.add(balanceleaderboard_panel);
		tickerplaybuttons_panel.add(Box.createRigidArea(new Dimension(0,400)));
		tickerplaybuttons_panel.add(playbuttons_panel);
		encompassing_panel.add(tickerplaybuttons_panel);
		encompassing_panel.add(turntranshistory_panel);
		
		contentPane.add(encompassing_panel);
		
		this.setLocationRelativeTo(null);
	}
}