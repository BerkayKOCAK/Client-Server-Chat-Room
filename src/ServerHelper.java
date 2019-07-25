import java.io.*;
import java.net.*;
import java.util.Map.Entry;

public class ServerHelper extends Thread {

	private Socket incoming;

	public ServerHelper(Socket incoming) {
		this.incoming = incoming;
	}

	public void run() {
		try {
			
			PrintWriter out;
			
			out = new PrintWriter(new OutputStreamWriter(incoming.getOutputStream()));
			if(TCPMutliServer.getHashMap().get(0).isEmpty())
			{out.println("Chat rooms empty");}
			for(int iterator1 = 0;iterator1<TCPMutliServer.getHashMap().size();iterator1++)
			{out.println("Chat room "+(iterator1+1)+" has : ");
			
				for(Entry<String, Socket> socketReader2: TCPMutliServer.getHashMap().get(iterator1).entrySet())
				{
					
					//System.out.println(socketReader2.getKey());
					out.println(socketReader2.getKey());
					out.flush();
					
				}
			}
			
			
			BufferedReader in = new BufferedReader(
					new InputStreamReader(
					incoming.getInputStream()));
			String nickname = in.readLine();
			String roomNumHolder = in.readLine();
			
			int roomToEnter = Integer.parseInt(roomNumHolder);
			int roomOriginalSize= roomToEnter;
			roomToEnter = roomToEnter-1;
			if(roomOriginalSize<TCPMutliServer.getHashMap().size())
			{TCPMutliServer.getHashMap().get(roomToEnter).put(nickname,incoming);}
			else
				{
				System.out.println("Server doesnt have a chat room with number : "+roomOriginalSize);
				out.println("Server doesnt have a chat room with number : "+roomOriginalSize);
				incoming.close();
				
				}
			int currentNumber = roomToEnter;
			
			
			
			while (true) 
			{
				String str = in.readLine();
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
								for(Entry<String, Socket> s: TCPMutliServer.getHashMap().get(roomToEnter).entrySet())
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
							else if (command.startsWith("switch_channel"))
							{
								//System.out.println("inside switch_channel");
								String recivedCommand = command;
								
								out = new PrintWriter(new OutputStreamWriter(incoming.getOutputStream()));
								recivedCommand = recivedCommand.substring(recivedCommand.indexOf(" ") + 1);//now it only takes last char
								
								out.println(nickname +" switching to chat room "+ recivedCommand);
								out.flush();
								
								int roomNumber = Integer.parseInt(recivedCommand);
								roomNumber = roomNumber-1;
								
								if (roomNumber>TCPMutliServer.getHashMap().size())
								{
									out.println("there is no room with number "+roomNumber+"!");
									out.flush();
								}
								else
								{
									
									out.println(nickname+" was in room " + (currentNumber+1) );
									out.println("Switching to channel number " + (roomNumber+1) +" containing :");
									out.flush();
									//
									System.out.println("\n-before add/rmv-\n");
									for(int j = 0;j<TCPMutliServer.getHashMap().size();j++)
									{System.out.println("values in list ="+TCPMutliServer.getHashMap().get(j));}
									//
									for(int j = 0;j<TCPMutliServer.getHashMap().size();j++)
									{TCPMutliServer.getHashMap().get(j).remove(nickname, incoming);System.out.println("----remove ("+ nickname +") from listofmaps["+j+"]");}
									TCPMutliServer.getHashMap().get(roomNumber).put(nickname,incoming);System.out.println("----put ("+ nickname +") to listofmaps ["+roomNumber+"]");
									//TCPMutliServer.getHashMap().get(currentNumber).remove(nickname, incoming);System.out.println("----remove ("+ nickname +") from listofmaps["+currentNumber+"]");
									System.out.println("\n-after add/rmv-\n");
									//
									for(int j = 0;j<TCPMutliServer.getHashMap().size();j++)
									{System.out.println("values in list ="+TCPMutliServer.getHashMap().get(j));}
									//
									currentNumber = roomNumber;
	
									for(Entry<String, Socket> socketReader2: TCPMutliServer.getHashMap().get(roomNumber).entrySet())
									{
										if(!(socketReader2.getKey().equals(nickname)))
										{
										out.println(socketReader2.getKey());
										out.flush();
										}
										
									}
								}
							}
							else 
							{	System.out.println("roomtoenter = "+roomToEnter);
								System.out.println("roomtoenter = "+currentNumber);
								for(Entry<String, Socket> s: TCPMutliServer.getHashMap().get(currentNumber).entrySet())
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
		} catch (Exception exception) {
			
			exception.printStackTrace();
		}
	}
}