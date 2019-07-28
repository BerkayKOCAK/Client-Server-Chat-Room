import java.io.IOException;
import java.util.ArrayList;

public class Client {
	
	public static void main(String[] args) {
		
		clientGUI guiThread;
		
		try {
			guiThread = new clientGUI();
			guiThread.run();
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

	}

}
