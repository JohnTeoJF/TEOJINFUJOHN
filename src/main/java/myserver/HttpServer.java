package myserver;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

//HttpServer class starting serverSocket and listens client's request
//HttpServer class implements Runnable interface and override it's public void run() method.

public class HttpServer implements Runnable {

	private ServerSocket serverSocket; //Reference to serverSocket where server will be started
	private int serverPort; //port where server has to start
	private String docRoot; //docRoot to check for custom directory
	
	//Default host and port values for the serverSocket
	private final int DEFAULT_PORT = 3000;
	private final String DEFAULT_HOST = "localhost";
	private final String DEFAULT_DOCROOT = "/";
	
	//Default constructor if no port is passed
	public HttpServer ()
	{
		this.docRoot = DEFAULT_DOCROOT; //default docRoot set as /
		this.serverPort = DEFAULT_PORT; //default port 3000
	}
	
	
	//Parameterized constructor if a port and docRoot are passed
	public HttpServer (String dRoot, int port)
	{
		this.docRoot = dRoot; //hostname of the server
		this.serverPort = port; //default port 3000
	}
	
	
	//Parameterized constructor if a port is passed
	public HttpServer (int port)
	{
		this.docRoot = DEFAULT_DOCROOT; //default docRoot set as /
		this.serverPort = port; //port passed by the ServerInitializer
	}

	
	@Override
	public void run() {
		
		try {

			//get inet address of the host
			InetAddress serverInet = InetAddress.getByName(DEFAULT_HOST);
			
			
			//now using serverInet address and serverPort, initialize serverSocket
			//using a default backlog value which depends on the implementation
			serverSocket = new ServerSocket(serverPort, 0, serverInet);

			System.out.println("[SERVER]> SERVER started at host: " + serverSocket.getInetAddress() + " port: " + serverSocket.getLocalPort() + "\n");
			
			//provide each client an ID, starting with zero
			int clientID=0;
			
			//multithreaded server
			while(true){
				
				//wait for a client to get connected
				Socket clientSocket = serverSocket.accept();
				
				//a new client has connected to this server
				System.out.println("[SERVER - CLIENT "+clientID+"]> Connection established with the client at " + clientSocket.getInetAddress() + ":" + clientSocket.getPort());
				
				//pass clientSocket and clientID to HttpClientConnection object
				HttpClientConnection rh = new HttpClientConnection(clientSocket, clientID,docRoot);
				
				//handover processing for the newly connected client to HttpClientConnection in a separate thread
				new Thread(rh).start();
				
				//increment clientID for the next client;
				clientID++;
			}
			
		} catch (UnknownHostException e) {
			System.err.println("[SERVER]> UnknownHostException for the hostname: " + DEFAULT_HOST);
		} catch (IllegalArgumentException iae) {
			System.err.println("[SERVER]> EXCEPTION in starting the SERVER: " + iae.getMessage());
		}
		catch (IOException e) {
			System.err.println("[SERVER]> EXCEPTION in starting the SERVER: " + e.getMessage());
		}
		finally {
				try {
					if(serverSocket != null){
						serverSocket.close();
					}
				} catch (IOException e) {
					System.err.println("[SERVER]> EXCEPTION in closing the server socket." + e);
				}
		}
	}
}
