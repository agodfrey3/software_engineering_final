//Kevin Ho, Andrew Godfrey, Saul Soto
//CISC 3598 - Software Engineering
//Professor Kounavelis

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.Color;
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
		setBounds(100, 100, 1400, 800);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		// Account Balance Title Label
		account_title_label = new JLabel("Account Balance", SwingConstants.CENTER);
		account_title_label.setBounds(100, 50, 200, 50);
		contentPane.add(account_title_label);
		
		// Account Balance Label
		account_label = new JLabel("$0", SwingConstants.CENTER);
		account_label.setBounds(100, 100, 200, 50);
		contentPane.add(account_label);
				
		// Ticker Label
		ticker_label = new JLabel("Ticker", SwingConstants.CENTER);
		ticker_label.setBounds(600, 100, 200, 50);
		contentPane.add(ticker_label);
		
		// Price Label
		price_label = new JLabel("0.00", SwingConstants.CENTER);
		price_label.setBounds(600, 150, 200, 50);
		contentPane.add(price_label);
				
		// Turn Count Title Label
		turn_title_label = new JLabel("Turn Count", SwingConstants.CENTER);
		turn_title_label.setBounds(1100, 50, 200, 50);
		contentPane.add(turn_title_label);
		
		// Turn Count Label
		turn_label = new JLabel("0", SwingConstants.CENTER);
		turn_label.setBounds(1100, 100, 200, 50);
		contentPane.add(turn_label);
		
		// Long Button
		long_button = new JButton("Long");
		long_button.setBounds(500, 550, 150, 50);
		long_button.setBackground(Color.GREEN);
		long_button.setOpaque(true);
		contentPane.add(long_button);
		
		// Short Button
		short_button = new JButton("Short");
		short_button.setBounds(750, 550, 150, 50);
		short_button.setBackground(Color.RED);
		contentPane.add(short_button);
		
		// Leaderboard Button
		leaderboard_button = new JButton("Leaderboard");
		leaderboard_button.setBounds(100, 650, 200, 50);
		contentPane.add(leaderboard_button);

		// Transaction History Button
		transaction_button = new JButton("Transaction History");
		transaction_button.setBounds(1100, 650, 200, 50);
		contentPane.add(transaction_button);
		
		this.setLocationRelativeTo(null);
	}
}