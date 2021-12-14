package myserver;

import java.util.HashMap;

//Initializes HttpServer by passing port from the command line 

public class Main {

	//main method
	public static void main(String[] args) {
		
		//initialize port to default 3000
		int port = 3000;
		String docRoot = "";

		//check command line arguments for port
		if(args.length >0){
			HashMap<String, String> input = new HashMap<String, String>();
			
			for(int i=0;i<args.length;i++){
				System.out.println("ARGS IS " +args[i]);

				if(args[i].equals("--port") && args.length>i+1){
					input.put(args[i], args[i+1]);

				//port is provided	
				System.out.println("PORT GIVEN IS " +input.get(args[i]));

			try {
				//check if port is an integer
				port = Integer.parseInt(input.get(args[i])); 
				System.out.println("TRIED PORT " +input.get(args[i]));
			}
			catch (NumberFormatException nfe)
			{
				System.err.println("[SERVER]> Integer Port number not provided. Server will start at default port.");
			}
					i++;
				}else if(args[i].equals("--docRoot") && args.length>i+1){
					//check command line arguments for docRoot
					input.put(args[i], args[i+1]);
					//docRoot is provided	
					System.out.println("docRoot GIVEN IS " +input.get(args[i]));
					try {
						//check if docRoot is valid
						docRoot = input.get(args[i]);
						System.out.println("docRoot has been set " + docRoot);
					}
					catch (Exception e)
					{
						System.err.println(e);
					}
					i++;
		}
	}
}
		System.out.println("[SERVER]> Using Server Port : " + port);
		
		//constructing WebServer object
		HttpServer ws = new HttpServer(docRoot,port);
		
		//start WebServer in a new thread
		new Thread(ws).start();
}

}