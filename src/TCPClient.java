import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import java.security.Key;
import java.security.PrivateKey;

public class TCPClient extends Thread{
	
	String nickname;
	static Socket socket;
	static BufferedReader inFromUser;
	static BufferedReader in;
	static PrintWriter out;
	//constructor for TCP thread
	TCPClient(String username, String publicKey, PrivateKey privateKey) throws ClassNotFoundException, IOException
	{
		super();
		nickname = username;
		
	
	
	 new Thread(new Runnable() {
	public void run() {
		try {
			System.out.println("Client is working");
			socket = new Socket("localhost", 6789);
			inFromUser = new BufferedReader(
					new InputStreamReader(System.in));
			out = new PrintWriter(new OutputStreamWriter(
					socket.getOutputStream()));
			in = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			new ClientPrinter(in).start();
			
			System.out.println("Welcome "+nickname+" you can start chatting !");
			
			//SYSTEM EXCHANGE 
			out.println(nickname);
			out.flush();
			out.println(publicKey);
			out.flush();
			byte[] signedUserList;
			signedUserList = RSA.encrypt(privateKey, "__userList "+nickname);
	        sendBytes(signedUserList);

			//when press send encryt with private key
			clientGUI.sendButton.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {
						

						if(socket.isConnected())
						{	
							String userInput  = clientGUI.message.getText();
							byte[] signedMSG;
							try {
							signedMSG = RSA.encrypt(privateKey, userInput);
					        sendBytes(signedMSG);
							
					        clientGUI.message.setText("");
							String tempSTR = clientGUI.chatBox.getText();
							tempSTR += "\n" + nickname + " : " + userInput;
							clientGUI.chatBox.setText(tempSTR);
							
							/*out.println(userInput);
							out.flush(); */
							}catch (Exception e) {
								e.printStackTrace();
							}     
						}
						else
						{
							clientGUI.chatBox.setText("YOU ARE NOT CONNECTED TO SERVER");
						}
					}});
			
			
			//BAD PRACTISE !!!
			clientGUI.disconnectServer.getAccessibleContext().setAccessibleDescription(
				       "Disconnects from server");
			clientGUI.disconnectServer.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {
									
						
						
						String tempSTR = clientGUI.chatBox.getText();
						tempSTR += "\n DISCONNETTING ...";
						clientGUI.chatBox.setText(tempSTR);
						byte[] signedUserList;
						try {
							signedUserList = RSA.encrypt(privateKey, "__close "+nickname);
							sendBytes(signedUserList);

							in.close();
							out.close();
							inFromUser.close();
							socket.close();
						} catch (Exception e) {
							e.printStackTrace();
						}
						
					}
					
				});
			
			
			/*while (true) 
			{
				str = inFromUser.readLine();
				out.println(str);
				out.flush();
			}*/
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		
	}
	 }).start();
	}
	
	
	static public void closingSequence()
	{
		String tempSTR = clientGUI.chatBox.getText();
		tempSTR += "\n DISCONNETTING ...";
		clientGUI.chatBox.setText(tempSTR);
		out.println("__close");
		out.flush();
		try {
			in.close();
			out.close();
			inFromUser.close();
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	public void sendBytes(byte[] myByteArray) throws IOException {
	    sendBytes(myByteArray, 0, myByteArray.length);
	}

	private void sendBytes(byte[] myByteArray, int start, int len) throws IOException {
	    if (len < 0)
	        throw new IllegalArgumentException("Negative length not allowed");
	    if (start < 0 || start >= myByteArray.length)
	        throw new IndexOutOfBoundsException("Out of bounds: " + start);
	    // Other checks if needed.

	    // May be better to save the streams in the support class;
	    // just like the socket variable.
	    OutputStream out = socket.getOutputStream(); 
	    DataOutputStream dos = new DataOutputStream(out);

	    dos.writeInt(len);
	    if (len > 0) {
	        dos.write(myByteArray, start, len);
	    }
	}
		
	
}