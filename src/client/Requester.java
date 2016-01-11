package client;

import java.io.*;
import java.net.*;
import java.util.Scanner;
public class Requester{
	Socket requestSocket;
	//i
 	InputStream in;
 	OutputStream out;
 	DataInputStream dataInS;
 	DataOutputStream dataOutS;
 	String message="";
 	String ipaddress;
 	Scanner stdin;
	Requester(){}
	void run()
	{
		stdin = new Scanner(System.in);
		try{
			//1. creating a socket to connect to the server
			System.out.println("Please Enter your IP Address\n");
			ipaddress = stdin.next();
			
			requestSocket = new Socket(ipaddress, 2004);
			System.out.println("Connected to "+ipaddress+" in port 2004");
			//2. get Input and Output streams
			System.out.println("Hello");
			out = requestSocket.getOutputStream();
			out.flush();

			in = requestSocket.getInputStream();
			dataInS=new DataInputStream(in);
			dataOutS=new DataOutputStream(out);
			
			
			message = dataInS.readUTF();
			System.out.println(message);
			
			message = stdin.next();
			dataOutS.writeUTF(message);
            dataOutS.flush();
			
			message = dataInS.readUTF();
			System.out.println(message);
			
			message = stdin.next();
			dataOutS.writeUTF(message);
            dataOutS.flush();
			
			message = dataInS.readUTF();
			System.out.println(message);
			
			
			Scanner in = new Scanner(System.in);
			
			do{
				System.out.println("1: Send files to server\n"
						+ "2: Send files from the server to client\n"
						+ "3: List all files in directory\n"
						+ "4: Create a new directory\n"
						+ "5: Move directory \n"); 
				message = in.nextLine();
			
			switch(message)
		      {
		         case "1" :
		        	 
		        	 dataOutS.writeUTF(message);
		             dataOutS.flush();
		        	 String line;
		        	 String fileMessage = " ";
		        	 String file;
		        	 
		        	 
		        	 Scanner input = new Scanner(System.in);
		        	 
		             System.out.print("Enter the file name with extention : ");
		             file=input.next();
		             dataOutS.writeUTF(file);
		             dataOutS.flush();
		        	 try {
		 		        // FileReader reads text files in the default encoding.
			 		        FileReader fileReader = 
			 		            new FileReader(file);
			 	
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
		         case "2" :
		        	 System.out.print("Enter the file name with extention : ");
		        	 message = in.nextLine();
		        	 dataOutS.writeUTF(message);
		             dataOutS.flush();
		        	 
		        	 File file1 = new File(message );
		        	 fileMessage= dataInS.readUTF();
		        	 
		        	 if (!file1.exists()){
		        		 file1.createNewFile();
		        	 }
		        	 FileWriter fw = new FileWriter(file1);
		        	 BufferedWriter bw = new BufferedWriter(fw);
		        	 bw.write(fileMessage);
		        	 bw.close();
		            break;
		         case "3" :
		        	 dataOutS.writeUTF(message);
		        	 dataOutS.flush();
		             message = dataInS.readUTF();//this reads message from user
					 System.out.println(message);
		             break;
		         case "4" :
		        	 dataOutS.writeUTF(message);
		        	 dataOutS.flush();
		        	 System.out.print("Please enter the new directory name : \n");
		        	 message = in.nextLine();
		        	 dataOutS.writeUTF(message);
		             dataOutS.flush();
		             message = dataInS.readUTF();
		             System.out.println(message);
		             break;
		         case "5" :
		        	 dataOutS.writeUTF(message);//sends the 5 to the server
		        	 dataOutS.flush();
		        	 
		        	 message = dataInS.readUTF();
		             System.out.println(message);
		             
		             message = dataInS.readUTF();
		             System.out.println(message);
		             
		        	 message = in.nextLine();
		        	 dataOutS.writeUTF(message);
		             dataOutS.flush();
		             
		            
		             message = dataInS.readUTF();
		             System.out.println(message);
		             
		             break;
		         default :
		            System.out.println("Invalid choice");
		      }
			}while(message != "bye");
			//3: Communicating with the server
			do{
				message = dataInS.readUTF();//this reads message from user
				System.out.println(message);
				message = stdin.next();
				dataOutS.writeUTF(message);
			}while(!message.equals("bye"));
		}
		catch(UnknownHostException unknownHost){
			System.err.println("You are trying to connect to an unknown host!");
		}
		catch(IOException ioException){
			ioException.printStackTrace();
		}
		finally{
			//4: Closing connection
			try{
				in.close();
				out.close();
				requestSocket.close();
			}
			catch(IOException ioException){
				ioException.printStackTrace();
			}
		}
	}
	
	public static void main(String args[])
	{
		Requester client = new Requester();
		client.run();
	}
}