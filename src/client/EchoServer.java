//package client;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
			
			dataOutS.writeUTF("Enter Username");
	    	dataOutS.flush();
			UserName = dataInS.readUTF();
			
			dataOutS.writeUTF("Enter Password");
	    	dataOutS.flush(); 
			UserPassword= dataInS.readUTF();
			
			//this for loop reads over 
			for(int j = 0; j < userMessage.length; j++){
				if (UserName.equals(userMessage[j][0]))
					if (UserPassword.equals(userMessage[j][1]))			
						passed = true;
			}//for
			
			if (passed == false){
				dataOutS.writeUTF("Not valid username or password");
		    	dataOutS.flush();
				return;
			}else dataOutS.writeUTF("User Authenticated");
	    	dataOutS.flush(); 
			
			
			String fileName = " ";
			String fileMessage = " ";
			
			File currentDir = new File("C:"+File.separator + "Users" + File.separator + "username" + File.separator + "Desktop" + 
			File.separator + "root" +File.separator + UserName);
					File home = currentDir;
					
					//message = dataInS.readUTF();
					System.out.println("message recieved test "+ message);
			do{
					System.out.println("client>"+clientID+"  "+ message);
					//if (message.equals("bye"))
					//dataOutS.writeUTF("server got the following: "+message);
			    	//dataOutS.flush();
					
					 message = dataInS.readUTF();
					 switch(message)
				      {
				         case "1":
				        	 fileMessage="";
				        	 fileName = dataInS.readUTF();
				        	 File file = new File(currentDir + File.separator + fileName);
				        	 fileMessage= dataInS.readUTF();
				        	 
				        	 if (!file.exists()){
				        		 file.createNewFile();
				        	 }
				        	 FileWriter fw = new FileWriter(file);
				        	 BufferedWriter bw = new BufferedWriter(fw);
				        	 bw.write(fileMessage);
				        	 bw.close();
				        	 
				            break;
				         case "2" :
				        	 String file2 = " ";
				        	 fileMessage="";
				        	 Scanner input = new Scanner(System.in);
				        	
				        	 file2 = dataInS.readUTF();
				             
				             
				        	 try {
				 		        // FileReader reads text files in the default encoding.
					 		        FileReader fileReader = 
					 		            new FileReader(file2);
					 	
					 		        // Wrap FileReader in BufferedReader.
					 		        BufferedReader bufferedReader = new BufferedReader(fileReader);
					 	
					 		        while((line = bufferedReader.readLine()) != null) 
					 		        {
					 		        	fileMessage += line;
					 		        }
					 		       dataOutS.writeUTF(fileMessage);
					 		       dataOutS.flush();
					 		       bufferedReader.close();         
				 		    }
				 		    catch(IOException ioException){
				 				ioException.printStackTrace();
				 		    }
				            break;
				         case "3":
				        	// System.out.println("test "+ message);
				    	   File f = new File(currentDir.toString());
					    	 
					    	 File[] children = f.listFiles();
					    	 //System.out.println(children);
					    	 String childrenNames = " ";
					    	 for(File file1 : children){
					    		 childrenNames += file1.getName() + "\n";
					    		 System.out.println(childrenNames);
					    	 }
					    	 System.out.println(childrenNames);
					    	 dataOutS.writeUTF(childrenNames);
					    	 dataOutS.flush();
					    	break;
				         case "4":
				        	 message = dataInS.readUTF();
				        	 File theDir = new File("C:"+File.separator + "Users" + File.separator + "username" + File.separator + "Desktop" + 
				        				File.separator + "root" +File.separator + message);

				        	// if the directory does not exist, create it
				        	if (!theDir.exists()) {
	
				        	    boolean result = false;

				        	    try{
				        	        theDir.mkdir();
				        	        result = true;
				        	    } 
				        	    catch(SecurityException se){
				        	        //handle it
				        	    }        
				        	    if(result) {    
				        	        System.out.println("DIR " + message + " created");
				        	        dataOutS.writeUTF("DIR " + message + " created");
							    	dataOutS.flush();
				        	    }
				        	}
					    	   
						    	break;
				         case "5":
				        	 //message = dataInS.readUTF();
				        	 //String curDir = " ";
				        	 System.out.println("Current Working Directory: " + currentDir.getAbsolutePath());
				        	 dataOutS.writeUTF("Current Working Directory: " + currentDir.getAbsolutePath());
						     dataOutS.flush();
						     
						     System.out.print("Please enter the name of the directory to move to: \n");
				        	 dataOutS.writeUTF("Please enter the name of the directory to move to: \n");
				             dataOutS.flush();
				             
				             message = dataInS.readUTF();
				             System.setProperty("user.dir",  "message");
				             
				             System.out.println("New Current Working Directory: " + currentDir.getAbsolutePath());
				             dataOutS.writeUTF(" NewCurrent Working Directory: " + currentDir.getAbsolutePath());
						     dataOutS.flush();
						    	break;
				         	default :
				            System.out.println("Invalid choice");
				      }
	    	}while(!message.equals("bye"));
			
			System.out.println("Ending Client : ID - " + clientID + " : Address - "
			        + clientSocket.getInetAddress().getHostName());
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	  }
	}

	
	