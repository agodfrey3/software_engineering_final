package StockFinalProject;

import java.io.IOException;
import java.io.BufferedReader;
import java.net.BindException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.SocketException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Set;
import java.util.Vector;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;

public class SocketServer implements Runnable
{
	   static Socket csocket;
	   String ipString;
	   char threadType;

	   static Vector<String> vec = new Vector<String>(5);
	   static Hashtable<String, SGUsers> usersplaying_hash = new Hashtable<String, SGUsers>();
	   
	   static final String newline = "\n";
	   static int first_time = 1;
	   
	   static int port_num = 3333;
	   
	   static int numOfConnections = 0;
	   static int numOfMessages = 0;
	   static int max_connections = 5;

	   static int number_of_transactions = 0;
	   static int number_of_longs = 0;
	   static int number_of_shorts = 0;
	   static int number_of_correct = 0;
	   static int number_of_neutral = 0;

	   SocketServer(Socket csocket, String ip)
	   {
		  this.csocket  = csocket;
  this.ipString = ip;
	   } 

	   public static void runSocketServer()   // throws Exception
	   {
	     boolean sessionDone = false;
	  
	     ServerSocket ssock = null;
	   
	     try
	     {
		   ssock = new ServerSocket(port_num);
	     }
	     catch (BindException e)
	     {
		    e.printStackTrace();
	     }
	     catch (IOException e)
	     {
		    e.printStackTrace();
	     }
	 
	     // update the status text area to show progress of program
	     try 
	     {
		     InetAddress ipAddress = InetAddress.getLocalHost();
		     SocketServerGUI.textArea.append("IP Address: " + ipAddress.getHostAddress() + newline);
	     }
	     catch (UnknownHostException e1)
	     {
		    // TODO Auto-generated catch block
		    e1.printStackTrace();
	     }
	 
	     SocketServerGUI.textArea.append("Listening on Port: " + port_num + newline);
	     SocketServerGUI.textArea.setCaretPosition(SocketServerGUI.textArea.getDocument().getLength());
	     SocketServerGUI.textArea.repaint();
	        
	     sessionDone = false;
	     while (sessionDone == false)
	     {
	        Socket sock = null;
		    try
		    {
		    // blocking system call
			   sock = ssock.accept();
		    }
		    catch (IOException e)
		    {
			   e.printStackTrace();
		    }

		    // update the status text area to show progress of program
//		    String tempString = sock.getInetAddress().toString().replace("/", "");
//	        SocketServerGUI.textArea.append("Client Connected: " + tempString + newline);
//	        SocketServerGUI.textArea.setCaretPosition(SocketServerGUI.textArea.getDocument().getLength());
//	        SocketServerGUI.textArea.repaint();
	        
	        new Thread(new SocketServer(sock, sock.getInetAddress().toString())).start();
		    
	     }
	 
	     try 
	     {
		    ssock.close();
	     }
	     catch (IOException e) 
	     {
		    e.printStackTrace();
	     }
	}	  
	   
	// This is the thread code that ALL clients will run()
	public void run()
	{
	   try
	   {
		  boolean session_done = false; 
	      long threadId;
	      String clientString;
	      String clientKey;
	      
	      String keyString = "";
	    
	      threadId = Thread.currentThread().getId();
	      
	      BufferedReader rstream = new BufferedReader(new InputStreamReader(csocket.getInputStream()));
	      ObjectInputStream in_os = new ObjectInputStream(csocket.getInputStream());
	      
	      if(first_time == 1) {
	      numOfConnections++;
	      
	      SocketServerGUI.textArea.append("Number of Connections = " + numOfConnections + newline);
	      SocketServerGUI.textArea.setCaretPosition(SocketServerGUI.textArea.getDocument().getLength());
	      SocketServerGUI.textArea.repaint();
	      first_time++;
	      }
	      
	      String tempIPString = ipString.toString().replace("/", "");
	      keyString = tempIPString + ":" + threadId;
	      
	      if (vec.contains(keyString) == false)
	        {
	    	    int counter = 0;
	        	vec.addElement(keyString);
	        	
	        	SocketServerGUI.textArea_2.setText("");
	        	Enumeration<String> en = vec.elements();
	        	while (en.hasMoreElements())
	        	{
	        		SocketServerGUI.textArea_2.append(en.nextElement() + " || ");
	        		
	        		if (++counter >= 6)
	        		{
	        			SocketServerGUI.textArea_2.append("\r\n");
	        			counter = 0;
	        		}
	        	}

  	            SocketServerGUI.textArea_2.repaint();
	        }
	      
	      //}
	      //ObjectInputStream out_os = new ObjectInputStream(csocket.getInputStream());
	      //ObjectInputStream in_os = new ObjectInputStream(csocket.getInputStream());
	      
	      while (session_done == false)
	      {
	       	if (rstream.ready())   // check for any data messages
	       	{
	       		  SGUserKO userKO = (SGUserKO) in_os.readObject();
	       		  clientKey = userKO.getUserKey();
	       		  
	       		  if(userKO.getUserObj().getTurnCounter() == -12345) {
	       			System.out.println("User logged off");
	       			   clientString = userKO.getUserObj().getUserName();
	       			   SocketServerGUI.textArea.append("User: " + clientString + " logged off" + newline);
		     	       SocketServerGUI.textArea.setCaretPosition(SocketServerGUI.textArea.getDocument().getLength());
		     	       SocketServerGUI.textArea.repaint();

		     	       SocketServerGUI.textArea.setCaretPosition(SocketServerGUI.textArea.getDocument().getLength());
		     	       SocketServerGUI.textArea.repaint();
		     	       
	       			   usersplaying_hash.remove(userKO.getUserKey());
	       			  
	       			   session_done = true;
	       			  
	       		  } else {
	       			  
	       			  if(usersplaying_hash.containsKey(clientKey) == false) {
	       				System.out.println("User logged on");
			       		  usersplaying_hash.put(clientKey, userKO.getUserObj());		  	 
			              clientString = usersplaying_hash.get(clientKey).getUserName();
			              // update the status text area to show progress of program
			   	           SocketServerGUI.textArea.append("User: " + clientString + " logged on" + newline);
			     	       SocketServerGUI.textArea.setCaretPosition(SocketServerGUI.textArea.getDocument().getLength());
			     	       SocketServerGUI.textArea.repaint();
			     	       // update the status text area to show progress of program
			     	       SocketServerGUI.textArea.setCaretPosition(SocketServerGUI.textArea.getDocument().getLength());
			     	       SocketServerGUI.textArea.repaint();
	       			 } else {
	       				 
	       				 // Function to update the Analytics
	       				runAnalytics();
	       				 
	       				 System.out.println("Analytics was updated");
						  usersplaying_hash.replace(clientKey, userKO.getUserObj());		  	 
						  clientString = usersplaying_hash.get(clientKey).getUserName();
						  Integer turnCounter = usersplaying_hash.get(clientKey).getTurnCounter();
						  // update the status text area to show progress of program
						   SocketServerGUI.textArea_3.append(clientString + " is on turn " + turnCounter + newline);
						   SocketServerGUI.textArea_3.setCaretPosition(SocketServerGUI.textArea_3.getDocument().getLength());
						   SocketServerGUI.textArea_3.repaint();
						   // update the status text area to show progress of program
						   SocketServerGUI.textArea_3.setCaretPosition(SocketServerGUI.textArea_3.getDocument().getLength());
						   SocketServerGUI.textArea_3.repaint();
	       			 }
	       		  }	           
	       	   }
	         			    		        	
	           Thread.sleep(500);
	           
	        }    // end while loop

	        csocket.close();
		     
	     } // end try  
	 
	     catch (SocketException e)
	     {
		  // update the status text area to show progress of program
	      SocketServerGUI.textArea.append("ERROR: Socket Exception!" + newline);
	      SocketServerGUI.textArea.setCaretPosition(SocketServerGUI.textArea.getDocument().getLength());
	      SocketServerGUI.textArea.repaint();
	     }
	     catch (InterruptedException e)
	     {
		  // update the status text area to show progress of program
	      SocketServerGUI.textArea.append("ERROR: Interrupted Exception!" + newline);
	      SocketServerGUI.textArea.setCaretPosition(SocketServerGUI.textArea.getDocument().getLength());
	      SocketServerGUI.textArea.repaint();
	     }
	     catch (UnknownHostException e)
	     {
		  // update the status text area to show progress of program
	      SocketServerGUI.textArea.append("ERROR: Unknown Host Exception" + newline);
	      SocketServerGUI.textArea.setCaretPosition(SocketServerGUI.textArea.getDocument().getLength());
	      SocketServerGUI.textArea.repaint();
	     }
	     catch (IOException e) 
	     {
	     // update the status text area to show progress of program
	     // SocketServerGUI.textArea.append("ERROR: IO Exception!" + newline);
	      SocketServerGUI.textArea.setCaretPosition(SocketServerGUI.textArea.getDocument().getLength());
	      SocketServerGUI.textArea.repaint();       
	     }     
	     catch (Exception e)
	     { 
		  numOfConnections--;
		  
		  // update the status text area to show progress of program
	      SocketServerGUI.textArea.append("ERROR: Generic Exception!" + newline);
	      SocketServerGUI.textArea.setCaretPosition(SocketServerGUI.textArea.getDocument().getLength());
	      SocketServerGUI.textArea.repaint(); 
	     }
	   
	  }  // end run thread method
	
		public static void runAnalytics() {
			int number_of_longs = 0;
			int number_of_shorts = 0;
			int number_of_correct = 0;
			int number_of_neutral = 0;
			int number_of_transactions = 0;
			
			double long_percent;
			double short_percent;
			double correct_percent;
			
		   Set<String> keys = usersplaying_hash.keySet();
	       for (String key : keys) {
	    	   number_of_longs += usersplaying_hash.get(key).getNumLongs();
	    	   number_of_shorts += usersplaying_hash.get(key).getNumShorts();
	    	   number_of_correct += usersplaying_hash.get(key).getNumCorrect();
	    	   number_of_neutral += usersplaying_hash.get(key).getNumNeutral();
	    	   number_of_transactions += usersplaying_hash.get(key).getTurnCounter();
	    	   
	    	   long_percent = (double) number_of_longs / (double) number_of_transactions * 100.0;
	    	   short_percent = (double) number_of_shorts / (double) number_of_transactions * 100.0;
	    	   correct_percent = (double) (number_of_correct - number_of_neutral) / (double) (number_of_transactions - number_of_neutral) * 100.0;
	    	   
	    	   SocketServerGUI.num_correct_long_label.setText(number_of_longs + " Longs");
	    	   SocketServerGUI.num_correct_short_label.setText(number_of_shorts + " Shorts");
	    	   SocketServerGUI.long_percentage.setText(String.format("%.2f", long_percent) + "% of Trades");
	    	   SocketServerGUI.short_percentage.setText(String.format("%.2f", short_percent) + "% of Trades");
	    	   SocketServerGUI.correct_percentage.setText(String.format("%.2f", correct_percent) + "%");
	       }
		}
}