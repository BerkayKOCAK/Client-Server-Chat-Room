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
				} else {
					System.out.println(str);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

}
