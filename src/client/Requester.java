package client;

import java.io.*;
import java.net.*;
import java.util.Scanner;
public class Requester{
	Socket requestSocket;
	//ObjectOutputStream out;
 	//ObjectInputStream in;
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

			message = dataInS.readUTF();
			System.out.println(message);
			message = stdin.next();
			sendMessage(message);
			
			message = dataInS.readUTF();
			System.out.println(message);
			message = stdin.next();
			sendMessage(message);
			
			message = dataInS.readUTF();
			System.out.println(message);
			//3: Communicating with the server
			do{
				message = dataInS.readUTF();//this reads message from user
				System.out.println(message);
				message = stdin.next();
				sendMessage(message);
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
	void sendMessage(String msg)
	{
		try{
			dataOutS.writeUTF(msg);
			dataOutS.flush();
			System.out.println("client>" + msg);
		}
		catch(IOException ioException){
			ioException.printStackTrace();
		}
	}
	public static void main(String args[])
	{
		Requester client = new Requester();
		client.run();
	}
}