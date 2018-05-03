package StockFinalProject;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.Date;
import java.awt.event.ActionEvent;
import javax.swing.border.LineBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.JTextArea;
import java.awt.Font;

public class SocketServerGUI extends JFrame 
{
	public static JTextArea textArea;
	public static JTextArea textArea_1;
	public static JTextArea textArea_2;
	public static JTextArea textArea_3;
	
	
    public static JLabel long_label = new JLabel();
    public static JLabel short_label = new JLabel();
    public static JLabel num_correct_long_label = new JLabel();
    public static JLabel num_correct_short_label = new JLabel();
    public static JLabel long_percentage = new JLabel();
    public static JLabel short_percentage = new JLabel();
    public static JLabel correct_label = new JLabel();
    public static JLabel correct_percentage = new JLabel();
	
	
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SocketServerGUI frame = new SocketServerGUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

  private void startSocketServer()
  {	
	   Thread refreshThread = new Thread()
	   {
		  public void run()
		  { 	
			  SocketServer.runSocketServer();
	     }
	  };
	  
    refreshThread.start();
  }
  
  /*
   * Thread to get the real time   
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
				   String str = String.format("    %tc", date);
					 
				   textArea_1.setText("");
				   textArea_1.append(str);
				   textArea_1.repaint();
				   
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
	/**
	 * Create the frame.
	 */
	public SocketServerGUI()
	{
		setTitle("The Stock Market Game");
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 929, 618);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel.setBounds(10, 11, 531, 360);
		contentPane.add(panel);
		
		textArea = new JTextArea();
		textArea.setFont(new Font("Monospaced", Font.BOLD, 10));
		textArea.setEditable(false);
		textArea.setRows(26); //15
		textArea.setColumns(85); //46
		panel.add(textArea);
		
		JScrollPane txt_more_info_pane = new JScrollPane(textArea);  
	    txt_more_info_pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
	    txt_more_info_pane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	    panel.add(txt_more_info_pane);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_1.setBounds(551, 110, 330, 261);
		contentPane.add(panel_1);
		
		textArea_3 = new JTextArea();
		textArea_3.setEditable(false);
		textArea_3.setRows(12);
		textArea_3.setColumns(25);
		panel_1.add(textArea_3);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBounds(123, 466, 103, 109);
		contentPane.add(panel_2);
		
		JButton btnNewButton = new JButton("Analytics");
		panel_2.add(btnNewButton);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				JFrame analysis_frame = new JFrame("Analytics");    
				analysis_frame.setSize(400, 400);
				analysis_frame.setLayout(null);
	            analysis_frame.setLocationRelativeTo(null);
	            analysis_frame.setVisible(true);
	            
				GridLayout analysisLayout = new GridLayout(0, 2);

		        analysis_frame.setLayout(analysisLayout);

				long_label.setText("Total Longs");
				long_label.setAlignmentX(CENTER_ALIGNMENT);
				long_label.setFont(new Font("Monospaced", Font.BOLD, 15));
				long_label.setForeground(new Color(35, 142, 57));
				long_label.setHorizontalAlignment(JLabel.CENTER);
			    long_label.setVerticalAlignment(JLabel.CENTER);
				
				short_label.setText("Total Shorts");
				short_label.setAlignmentX(CENTER_ALIGNMENT);
				short_label.setFont(new Font("Monospaced", Font.BOLD, 15));
				short_label.setForeground(Color.red);
				short_label.setHorizontalAlignment(JLabel.CENTER);
			    short_label.setVerticalAlignment(JLabel.CENTER);

				num_correct_long_label.setHorizontalAlignment(JLabel.CENTER);
				num_correct_long_label.setVerticalAlignment(JLabel.CENTER);

				num_correct_short_label.setHorizontalAlignment(JLabel.CENTER);
				num_correct_short_label.setVerticalAlignment(JLabel.CENTER);

				long_percentage.setHorizontalAlignment(JLabel.CENTER);
				long_percentage.setVerticalAlignment(JLabel.CENTER);

				short_percentage.setHorizontalAlignment(JLabel.CENTER);
				short_percentage.setVerticalAlignment(JLabel.CENTER);
				
	            correct_label.setText("Prediction Accuracy:");
	            correct_label.setFont(new Font("Monospaced", Font.BOLD, 15));
	            correct_label.setForeground(Color.blue);
	            correct_label.setHorizontalAlignment(JLabel.CENTER);
	            correct_label.setVerticalAlignment(JLabel.CENTER);
	            
	            correct_percentage.setHorizontalAlignment(JLabel.CENTER);
	            correct_percentage.setVerticalAlignment(JLabel.CENTER);
				
				analysis_frame.add(long_label, BorderLayout.CENTER);
				analysis_frame.add(short_label);
				analysis_frame.add(num_correct_long_label);
				analysis_frame.add(num_correct_short_label);
				analysis_frame.add(long_percentage);
				analysis_frame.add(short_percentage);
				analysis_frame.add(correct_label);
				analysis_frame.add(correct_percentage);
			}	
		});
		
		JPanel panel_3 = new JPanel();
		panel_3.setBounds(10, 466, 103, 109);
		contentPane.add(panel_3);
		
		JButton btnNewButton_1 = new JButton("EXIT");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				System.exit(0);
			}
		});
		panel_3.add(btnNewButton_1);
		
		JPanel panel_7 = new JPanel();
		panel_7.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_7.setBounds(551, 11, 330, 88);
		contentPane.add(panel_7);
		
		textArea_1 = new JTextArea();
		textArea_1.setEditable(false);
		textArea_1.setFont(new Font("Monospaced", Font.PLAIN, 15));
		textArea_1.setRows(2);
		textArea_1.setColumns(36);
		panel_7.add(textArea_1);
		
		JPanel panel_8 = new JPanel();
		panel_8.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_8.setBounds(10, 382, 871, 73);
		contentPane.add(panel_8);
		
		textArea_2 = new JTextArea();
		textArea_2.setEditable(false);
		textArea_2.setRows(3);
		textArea_2.setColumns(70);
		panel_8.add(textArea_2);
		
		startSocketServer();
		
		startRealTimeClock();

		this.setLocationRelativeTo(null);
		
	}
	
}
