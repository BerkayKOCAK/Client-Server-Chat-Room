import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.*;

public class TCPMutliServer {
	
	//private static HashMap<String, Socket> hashMap = new HashMap<String, Socket>();//can be diff.
	private static List<HashMap<String, Socket>> listOfMaps = new ArrayList<HashMap<String, Socket>>();
	
	
	public static void main(String[] args) 
	{
		
		int i = 0;
		
		try {
			System.out.println("Server is working...");
			BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
					
			System.out.println("Please a number to create chat rooms: ");
			int roomNumber = Integer.parseInt(inFromUser.readLine());
			System.out.println("Creating = "+roomNumber+" chat rooms...");
			System.out.println("Ready to accept connections. ");
			
			for (int count = 0; count < roomNumber ;count++ )
				{
				HashMap<String, Socket> hashMap = new HashMap<String, Socket>();
				listOfMaps.add(hashMap);
				}
			ServerSocket serverSocket = new ServerSocket(6789);
			while (true) {
				Socket incoming = serverSocket.accept();
				i++;
				//getHashMap().put("client 1",incoming);
				new ServerHelper(incoming).start();
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}
	
	public static synchronized List<HashMap<String,Socket>> getHashMap() {
		return listOfMaps;
	}

	public static synchronized void setHashMap( List<HashMap<String,Socket>>  hashMap) {
		TCPMutliServer.listOfMaps = hashMap;
	}
	
	

}