//package client;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class EchoServer {
	  public static void main(String[] args) throws Exception {
		  //creates a server socket to send information over the network
	    ServerSocket m_ServerSocket = new ServerSocket(2004,10);
	    int id = 0;
	    
	    while (true) 
	    {
	      Socket clientSocket = m_ServerSocket.accept();
	      ClientServiceThread cliThread = new ClientServiceThread(clientSocket, id++);
	      cliThread.start();
	    }
	  }
	}
	
	class ClientServiceThread extends Thread {
	  Socket clientSocket;
	  String [][] userMessage = new String [100][100];//used to store client file with username and passwords
	  String message;
	  String UserName;
	  String UserPassword;
	  boolean passed = false;
	  int clientID = -1;
	  boolean running = true;
	  InputStream in;
	 	OutputStream out;
	 	DataInputStream dataInS;
	 	DataOutputStream dataOutS;
	 	
	 	public void menu(){
			
		}
	
	  ClientServiceThread(Socket s, int i) {
	    clientSocket = s;
	    clientID = i;
	  }
	
	  void sendMessage(String msg)//this method sends the message to client
		{
			try{
				dataOutS.writeUTF(msg);
				dataOutS.flush();
				System.out.println("client> " + msg);
			}
			catch(IOException ioException){
				ioException.printStackTrace();
			}
		}
	  public void run() {
		  int i = 0;
		    String line = null;
		    String [] stringMessage ;
		    
		    try {
		        // FileReader reads text files in the default encoding.
		        FileReader fileReader = 
		            new FileReader("CLIENT1.txt");
	
		        // Wrap FileReader in BufferedReader.
		        BufferedReader bufferedReader = new BufferedReader(fileReader);
	
		        while((line = bufferedReader.readLine()) != null) 
		        {
		        	//System.out.println(line);
	    
		        	stringMessage = line.split(" ");
		        	for(int j = 0; j < stringMessage.length; j++){
		        	//System.out.println(stringMessage[j]);
	    
		        		userMessage[i][j] = stringMessage[j];
		        	}
		        	i++;
		        }   
	
		        // Always close files.
		        bufferedReader.close();         
		    }
		    catch(IOException ioException){
				ioException.printStackTrace();
		    }
				
	    System.out.println("Accepted Client : ID - " + clientID + " : Address - "
	        + clientSocket.getInetAddress().getHostName());
	    
	    try 
	    {
	    	out = clientSocket.getOutputStream();
			out.flush();
			System.out.println("Hello");
			in = clientSocket.getInputStream();
			dataInS=new DataInputStream(in);
			dataOutS=new DataOutputStream(out);
			//
			System.out.println("Accepted Client : ID - " + clientID + " : Address - "
			        + clientSocket.getInetAddress().getHostName());
			
			sendMessage("Connection successful");
			sendMessage("Press any key to continue");
			sendMessage("Enter Username"); 
			UserName = dataInS.readUTF();
			sendMessage("Enter Password"); 
			UserPassword= dataInS.readUTF();
			
			//this for loop reads over 
			for(int j = 0; j < userMessage.length; j++){
				if (UserName.equals(userMessage[j][0])){
					passed = true;
				}
				passed = false;
					if (UserName.equals(userMessage[j][1])){
						passed = true;
					}
						
			}//for
			
			if (passed = false){
				sendMessage("Not valid username or password"); 
				return;
			}//end if a
			
			do{
					System.out.println("client>"+clientID+"  "+ message);
					//if (message.equals("bye"))
					sendMessage("server got the following: "+message);
					 message = dataInS.readUTF();
				
	    	}while(!message.equals("bye"));
			
			System.out.println("Ending Client : ID - " + clientID + " : Address - "
			        + clientSocket.getInetAddress().getHostName());
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	  }
	}

