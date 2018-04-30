package StockFinalProject;

//import java.util.Vector;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
//import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

public class SocketUtilities
{
	Socket clientSocket=null;
    DataOutputStream outToServer=null;
    BufferedReader inFromServer=null;
    //StringBuilder client_username = new StringBuilder("");

	public boolean socketConnect()
	{
		String ipAddress, portString;
		int portNumber;
		boolean rc=false;
		
		try 
		{
			File file = new File("config.txt");
	        if (file.exists())
	        {
	           BufferedReader br = new BufferedReader(new FileReader("config.txt"));
	           
               ipAddress  = br.readLine();
               portString = br.readLine();
           
               portNumber = Integer.parseInt(portString);
               br.close();
	        }
	        else
	        {
	           ipAddress  = "localhost";
	           portNumber = 3333;
	        }
  
           clientSocket  = new Socket(ipAddress, portNumber);
           
           outToServer   = new DataOutputStream(clientSocket.getOutputStream());
           inFromServer  = new BufferedReader(
   	                       new InputStreamReader(clientSocket.getInputStream()));
           
           rc= true;
		}
		catch (ConnectException ex)
		{
			ex.printStackTrace();
		}					
		catch (UnknownHostException ex)
	    {
			ex.printStackTrace();
	    }
		catch (IOException ex) 
	    {
			ex.printStackTrace();
	    }
		
		return rc;
	}
	
//	public String getKey() {
//		
//		String user_key = "\0";
//		
//		try {
//			ObjectOutputStream out_os = new ObjectOutputStream(clientSocket.getOutputStream());
//			ObjectInputStream in_os = new ObjectInputStream(clientSocket.getInputStream());
//			user_key = (String) in_os.readObject();
//		}
//		catch (Exception e)
//		{
//			// TODO Auto-generated catch block
//			System.out.println("somethings wrong with getkey");
//			e.printStackTrace();
//		}
//		
//		return user_key;
//	}
	
	public boolean sendUserKO(SGUserKO userKO)
	{
		boolean rc=false;
		
		try 
		{
			ObjectOutputStream out_os = new ObjectOutputStream(clientSocket.getOutputStream());
//			ObjectInputStream in_os = new ObjectInputStream(clientSocket.getInputStream());
			out_os.writeObject(userKO);
			
//			StringBuilder clientdata_strb = new StringBuilder();
//			clientdata_strb.append(client_username.toString());
//			clientdata_strb.append(" ==> ");
//			
//			for(int i=0; i<msg.size(); i++) {
//				clientdata_strb.append(msg.get(i));
//				clientdata_strb.append(" | ");
//			}
//			
//			String clientdata_fullstr = clientdata_strb.toString();
			
			// StringBuilder works
			// Can also access array element as long as index is specified
			//outToServer.writeBytes(clientdata_fullstr + "\r\n");
			rc = true;
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			System.out.println("Something went wrong with sendmessage");
			e.printStackTrace();
		}
		
		return rc;
	}
	
	public boolean sendKey(String key) {
		boolean rc = false;
		
		try 
		{
//			ObjectOutputStream out_os = new ObjectOutputStream(clientSocket.getOutputStream());
//			out_os.writeObject(userKO);
			
			outToServer.writeBytes(key);
			rc = true;
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			System.out.println("somethings wrong with sendmessage");
			e.printStackTrace();
		}
		
		return rc;
	}
	
//	public boolean incrementLS(String userKey)
//	{
//		boolean rc=false;
//		
//		try 
//		{
//			ObjectOutputStream out_os = new ObjectOutputStream(clientSocket.getOutputStream());
//			//ObjectInputStream in_os = new ObjectInputStream(clientSocket.getInputStream());
//			out_os.writeObject(userKey);
//			
//			rc = true;
//		}
//		catch (IOException e)
//		{
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		return rc;
//	}
	
	public String recvMessage()
	{
		String msg=null;
		
		try
		{
			msg = inFromServer.readLine();
		} 
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return msg;
	}
	
	public boolean closeSocket()
	{
		boolean rc=false;
		
		try
		{
			clientSocket.close();
                        rc=true;
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return rc;
	}
}