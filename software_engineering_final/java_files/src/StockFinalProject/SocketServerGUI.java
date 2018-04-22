package StockFinalProject;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.Date;
import java.awt.event.ActionEvent;
import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.JTextArea;
import java.awt.Font;

public class SocketServerGUI extends JFrame 
{
	public static JTextArea textArea;
	public static JTextArea textArea_1;
	public static JTextArea textArea_2;
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

	
	/*
     * Thread to update weather info for NYC and Boston    
     */     
  private void startSocketServer()
  {	
	   Thread refreshWeatherThread = new Thread()
	   {
		  public void run()
		  { 	
			  SocketServer.runSocketServer();
	     }
	  };

    refreshWeatherThread.start();
  }
	
  
  /*
   * Thread to update weather info for NYC and Boston    
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
					   sleep(5000L);
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
		
		JTextArea textArea_3 = new JTextArea();
		textArea_3.setEditable(false);
		textArea_3.setRows(12);
		textArea_3.setColumns(25);
		panel_1.add(textArea_3);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBounds(123, 466, 103, 109);
		contentPane.add(panel_2);
		
		JButton btnNewButton = new JButton("1");
		panel_2.add(btnNewButton);
		
		JPanel panel_3 = new JPanel();
		panel_3.setBounds(10, 466, 103, 109);
		contentPane.add(panel_3);
		
		JButton btnNewButton_1 = new JButton("EXIT");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				System.exit(0);;
			}
		});
		panel_3.add(btnNewButton_1);
		
		JPanel panel_4 = new JPanel();
		panel_4.setBounds(236, 466, 103, 109);
		contentPane.add(panel_4);
		
		JButton btnR = new JButton("2");
		panel_4.add(btnR);
		
		JPanel panel_5 = new JPanel();
		panel_5.setBounds(349, 466, 103, 109);
		contentPane.add(panel_5);
		
		JButton btnNewButton_2 = new JButton("3");
		panel_5.add(btnNewButton_2);
		
		JPanel panel_6 = new JPanel();
		panel_6.setBounds(462, 466, 103, 109);
		contentPane.add(panel_6);
		
		JButton btnNewButton_3 = new JButton("4");
		panel_6.add(btnNewButton_3);
		
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
		
		JPanel panel_9 = new JPanel();
		panel_9.setBounds(575, 466, 103, 109);
		contentPane.add(panel_9);
		
		JButton btnNewButton_4 = new JButton("5");
		panel_9.add(btnNewButton_4);
		
		JPanel panel_10 = new JPanel();
		panel_10.setBounds(688, 466, 103, 109);
		contentPane.add(panel_10);
		
		JButton btnNewButton_5 = new JButton("6");
		panel_10.add(btnNewButton_5);
		
		JPanel panel_11 = new JPanel();
		panel_11.setBounds(801, 466, 103, 109);
		contentPane.add(panel_11);
		
		JButton btnNewButton_6 = new JButton("7");
		panel_11.add(btnNewButton_6);
		
		startSocketServer();
		
		startRealTimeClock();
		
		this.setLocationRelativeTo(null);
	}
}
