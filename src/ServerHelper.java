import java.io.*;
import java.net.*;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map.Entry;

public class ServerHelper extends Thread {

	private Socket incoming;
	private PublicKey userPubKey;
	public ServerHelper(Socket incoming) throws ClassNotFoundException, IOException {
		
		this.incoming = incoming;
	

		new Thread(new Runnable() {
			public void run() {
		try {
			
			PrintWriter out;
			
			out = new PrintWriter(new OutputStreamWriter(incoming.getOutputStream()));
			if(TCPMultiServer.getHashMap().get(0).isEmpty())
			{out.println("Chat rooms empty");}
			for(int iterator1 = 0;iterator1<TCPMultiServer.getHashMap().size();iterator1++)
			{out.println("Chat room "+(iterator1+1)+" has : ");
			
				for(Entry<String, Socket> socketReader2: TCPMultiServer.getHashMap().get(iterator1).entrySet())
				{					
					
					out.println(socketReader2.getKey());
					out.flush();
					out.println("__userList "+socketReader2.getKey());
					out.flush();
				}
			}
			
			BufferedReader in = new BufferedReader(
					new InputStreamReader(
					incoming.getInputStream()));
			String nickname = in.readLine();
			String pubKeySTR = in.readLine();
			userPubKey = RSA.convertStringToPubKey(pubKeySTR);
			
			ArrayList<String> tempArr = UserList.getUserList();
			tempArr.add(nickname);
			UserList.setUserList(tempArr);
			
			int roomToEnter = 0;//Integer.parseInt(roomNumHolder);
			int roomOriginalSize= 0;
			if(roomOriginalSize<TCPMultiServer.getHashMap().size())
			{TCPMultiServer.getHashMap().get(roomToEnter).put(nickname,incoming);}
			else
				{
				System.out.println("Server doesnt have a chat room with number : "+roomOriginalSize);
				out.println("Server doesnt have a chat room with number : "+roomOriginalSize);
				incoming.close();
				
				}
			int currentNumber = roomToEnter;
		
			while (true) 
			{
				String str = resolveMessage();
				System.out.println("From "+nickname+" Server received : " + str);
				if (str == null) 
				{
					System.out.println("breaking bad");
					break;
				} 
				else 
				{						
					String command = str;
					if (command.startsWith("PM"))
					{	
						//System.out.println("inside pm");
						String recivedNick = command;
						System.out.println("hey "+recivedNick);
						recivedNick = recivedNick.substring(recivedNick.indexOf(" ") + 1);
						recivedNick = recivedNick.substring(0, recivedNick.indexOf(" "));
						for(Entry<String, Socket> s: TCPMultiServer.getHashMap().get(roomToEnter).entrySet())
						{
							if(s.getKey().equals(recivedNick))
							{	
								System.out.println("inside sending");
								out = new PrintWriter(new OutputStreamWriter(s.getValue().getOutputStream()));
								out.println(nickname + " : " + command);
								out.flush();
							}
						}
					}
					else if (command.startsWith("__close"))
					{
						
						in.close();
						out.close();
						
						for(Entry<String, Socket> s: TCPMultiServer.getHashMap().get(currentNumber).entrySet())
						{
							
							if(!(s.getValue().getInetAddress().getHostAddress().
									equals(incoming.getInetAddress().getHostAddress()) 
									&& s.getValue().getPort() == incoming.getPort())) 
							{
								out = new PrintWriter(new OutputStreamWriter(s.getValue().getOutputStream()));
								out.println(nickname + " is leaving! ");
								out.flush();
							}
							
							else
							{}
							
						}
						TCPMultiServer.getHashMap().get(roomToEnter).remove(nickname,incoming);
						break;
					}
					else if (command.startsWith("__userList"))
					{
						

						for(Entry<String, Socket> s: TCPMultiServer.getHashMap().get(currentNumber).entrySet())
						{
								System.out.println(" s val = "+s.getValue().toString());
								out = new PrintWriter(new OutputStreamWriter(s.getValue().getOutputStream()));
								out.println("__userList "+nickname);
								out.flush();
							
						}			

					}

					else 
						{	
						for(Entry<String, Socket> s: TCPMultiServer.getHashMap().get(currentNumber).entrySet())
							{
								if(!(s.getValue().getInetAddress().getHostAddress().
										equals(incoming.getInetAddress().getHostAddress()) 
										&& s.getValue().getPort() == incoming.getPort())) 
								{

									out = new PrintWriter(new OutputStreamWriter(s.getValue().getOutputStream()));
									out.println(nickname + " : " + str);
									out.flush();
								}		
							}				
						
						}		
				
				}
			}
			incoming.close();
			} catch (Exception exception) 
				{exception.printStackTrace();}
		}
	}).start();
	}
	
	
	private String resolveMessage () throws Exception
	{
		InputStream inpStream = incoming.getInputStream();
	    DataInputStream dis = new DataInputStream(inpStream);

	    int len = dis.readInt();
	    byte[] data = new byte[len];
	    if (len > 0) {
	        dis.readFully(data);
	    }
	    
	    byte[] message = RSA.decrypt(userPubKey, data);
	    String messageSTR = new String(message);
	    //System.out.println("messages = "+messageSTR);
	    //dis.close();
	    //inpStream.close();
		return messageSTR;
		
	}
	
}