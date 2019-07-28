import java.io.*;

public class ClientPrinter extends Thread {

	private BufferedReader in;

	public ClientPrinter(BufferedReader in) {
		this.in = in;
	}

	public void run() {
		while (true) {
			try {
				String str = in.readLine();
				
				if (str == null) {
					break;
				} 
				else if (str.startsWith("__userList"))
				{	
					String recivedNick = str;
					recivedNick = recivedNick.substring(recivedNick.indexOf(" ") + 1,recivedNick.length());
					String Userlist =clientGUI.userBox.getText();
					Userlist += recivedNick+"\n";
					/*for(String item : TCPMultiServer.getUserList()){
						Userlist += item;
					}*/
					clientGUI.userBox.setText(Userlist);
					
				}
				
				else {
					
					//String str  = clientGUI.message.getText();
					clientGUI.message.setText("");
					String tempSTR = clientGUI.chatBox.getText();
					tempSTR += "\n" + str;
					clientGUI.chatBox.setText(tempSTR);
					System.out.println(str);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

}
