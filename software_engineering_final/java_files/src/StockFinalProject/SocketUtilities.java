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
	
	public boolean sendUserKO(SGUserKO userKO)
	{
		boolean rc=false;
		
		try 
		{
			ObjectOutputStream out_os = new ObjectOutputStream(clientSocket.getOutputStream());
			out_os.writeObject(userKO);
			
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
	
	public boolean closeSocket(SGUserKO userKO)
	{
		boolean rc=false;
		
		try
		{
			ObjectOutputStream out_os = new ObjectOutputStream(clientSocket.getOutputStream());
			out_os.writeObject(userKO);
			
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