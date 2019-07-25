import java.io.*;
import java.net.*;

public class TCPClient {

	public static void main(String[] args) {
		try {
			System.out.println("Client is working");
			
			String host = "localhost";
			Socket socket = new Socket(host, 6789);
			BufferedReader inFromUser = new BufferedReader(
					new InputStreamReader(System.in));
			PrintWriter out = new PrintWriter(new OutputStreamWriter(
					socket.getOutputStream()));
			BufferedReader in = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			new ClientPrinter(in).start();
			
			String str;
			String nickname;
			int roomToEnter;
			
			System.out.println("Enter your nickname ");
			nickname = inFromUser.readLine();
			System.out.println("Please a number to join a chat room: ");
			roomToEnter = Integer.parseInt(inFromUser.readLine());
			System.out.println("you are entering chat room: "+roomToEnter+" with nickname: "+nickname);
			
			//out.println("Welcome "+nickname+" you can start chatting !");
			
			out.println(nickname);
			out.flush();
			out.println(roomToEnter);
			out.flush();
			
			while (true) 
			{
				str = inFromUser.readLine();
				out.println(str);
				out.flush();
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

}