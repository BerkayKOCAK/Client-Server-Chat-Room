

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.Key;
import java.security.PublicKey;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileSystemView;

/**
* 
* Client interface 
* Implements Thread
* 
**/

@SuppressWarnings("unused")
public class clientGUI extends JFrame implements Runnable
{
	static JTextArea chatBox;
	static JTextArea userBox;
	static JFrame frame;
	static JPanel panel;
	static JButton sendButton;
	static JTextField message;
	//static JTextField chatBox;
	static String username = "";
	static RSA clientKey = new RSA();
	static TCPClient clientConnect;
	static JMenuItem disconnectServer;

	public clientGUI() throws IOException, ClassNotFoundException 
	   {  
		
		
		    }
		

	@Override
	public void run() {
		
				
				
				//GUI Items
				JMenuBar menuBar = new JMenuBar();
				JMenu fileMenu, helpMenu;
				JMenuItem generateKey , connectServer , exitApp , about;
				
				about = new JMenuItem("About");
				generateKey = new JMenuItem("Generate Key");
				connectServer = new JMenuItem("Connect To Network");
				disconnectServer = new JMenuItem("Disconnect");
				exitApp = new JMenuItem("Exit");
		
				frame = new JFrame("Chat Application");
				panel = new JPanel();
				GridBagConstraints constrait = new GridBagConstraints();
				JLabel keyText = new JLabel();
				JTextArea aboutBox = new JTextArea();
				chatBox = new JTextArea();
				userBox  = new JTextArea("USER LIST : \n");
				JLabel infoText = new JLabel();
				JLabel currentFilesText =  new JLabel("");
				message = new JTextField("Enter a message");
				sendButton = new JButton("Send");
				Border border = BorderFactory.createLineBorder(Color.BLACK);
				
				JOptionPane unamePopUp = new JOptionPane();
				
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
				frame.getContentPane().add(panel,BorderLayout.PAGE_START);
				
				
				//MENU ITEMS
				
				fileMenu = new JMenu("File");
				menuBar.add(fileMenu);
				
				helpMenu = new JMenu("Help");
				menuBar.add(helpMenu);
				
				//ABOUT
				helpMenu.add(about);
				about.addActionListener(new ActionListener(){

					@Override
					public void actionPerformed(ActionEvent arg0) {
						//panel.removeAll();
						if(aboutBox.getParent() == panel)
						{
							panel.remove(aboutBox);
							panel.updateUI();
						}
						else
						{
							aboutBox.setText("  --- ABOUT DEVELOPER --- \n Name : Berkay KOÇAK \n Degree : Bachelor \n Completion Date : 06/01/2019");
							panel.add(aboutBox);
							panel.updateUI();
						}
					}
					
				});
				
				
				//Sub-MENU ITEMS
				//GENERATE
				generateKey.getAccessibleContext().setAccessibleDescription(
				       "generates key");
				fileMenu.add(generateKey);
				generateKey.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {
						panel.removeAll();
						Dimension newDimension = new Dimension(100,100);
						try {

							clientKey.generateKey();
							
						} catch (Exception e) {
							System.out.println("Error Occured During Key Gen.");
							e.printStackTrace();
						}
						constrait.fill = GridBagConstraints.HORIZONTAL;	
						constrait.gridx = 0;
						constrait.gridy = 0;
						keyText.setMinimumSize(newDimension);
						keyText.setPreferredSize(newDimension);
						keyText.setMaximumSize(newDimension);
						keyText.setText("Public Key = "+clientKey.getPublicKey_String());

						panel.add(keyText,constrait);
						panel.updateUI();
					
					}
					
				});
				
				
				//CONNECT
				connectServer.getAccessibleContext().setAccessibleDescription(
				       "Asks username then connects to server");
				fileMenu.add(connectServer);
				connectServer.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {
						
						if(clientKey.getPublicKey_String() == null)
						{
							JOptionPane.showMessageDialog(frame, "CREATE A KEY FIRST !");
						}
						else
						{	
							panel.removeAll();
							fileMenu.remove(connectServer);
							Dimension newDimension = new Dimension(200,50);
							if (username.equals(""))
							{username = JOptionPane.showInputDialog(frame,"Enter your nickname : ");}
							
							infoText.setText("Enter A Message : ");
							keyText.setText("Welcome "+username + " you can start chatting.");
							
							
							constrait.fill = GridBagConstraints.HORIZONTAL;	
							constrait.gridx = 0;
							constrait.gridy = 0;
							constrait.anchor = GridBagConstraints.FIRST_LINE_START;
							panel.add(keyText,constrait);
							
							/*
							constrait.gridx = 0;
							constrait.gridy = 1;
							constrait.anchor = GridBagConstraints.LINE_START;
							panel.add(infoText,constrait);
							*/
							
							constrait.gridx = 0;
							constrait.gridy = 1;
							constrait.anchor = GridBagConstraints.LINE_END;	
							message.setMinimumSize(newDimension);
							message.setPreferredSize(new Dimension( 200, 24 ));
							panel.add(message,constrait);
							
							
							constrait.gridx = 1;
							constrait.gridy = 1;
							constrait.fill = GridBagConstraints.HORIZONTAL;	
							sendButton.setMinimumSize(new Dimension( 50, 24 ));
							//sendButton.setPreferredSize(new Dimension( 75, 24 ));
							panel.add(sendButton,constrait);
							
							
							chatBox.setBorder(BorderFactory.createCompoundBorder(border, 
						            BorderFactory.createEmptyBorder(10, 10, 10, 10)));
							chatBox.setEditable(false);
							chatBox.setLineWrap(true);
							chatBox.setWrapStyleWord(true);
							constrait.gridx = 0;
							constrait.gridy = 2;
							chatBox.setMinimumSize(new Dimension( 300, 200 ));
							chatBox.setPreferredSize(new Dimension( 300, 200 ));
							chatBox.setText("");
							panel.add(chatBox,constrait);
							
							
							userBox.setBorder(BorderFactory.createCompoundBorder(border, 
							            BorderFactory.createEmptyBorder(10, 10, 10, 10)));
							userBox.setEditable(false);
							userBox.setLineWrap(true);
							userBox.setWrapStyleWord(true);
							userBox.setText("USER LIST : \n");
							constrait.gridx = 3;
							constrait.gridy = 2;
							constrait.fill = GridBagConstraints.VERTICAL;	
							userBox.setMinimumSize(new Dimension( 175, 200 ));
							userBox.setPreferredSize(new Dimension( 175, 200 ));
							panel.add(userBox,constrait);
							
							fileMenu.add(disconnectServer);
							fileMenu.remove(exitApp);
							fileMenu.add(exitApp);
							panel.updateUI();
							
							try {clientConnect = new TCPClient(username , clientKey.getPublicKey_String() , clientKey.getPrivateKey());}
							catch (ClassNotFoundException | IOException e) {e.printStackTrace();}
						}
					}
					
				});
				
				
				//DISCONNECT
				disconnectServer.getAccessibleContext().setAccessibleDescription(
				       "Disconnects from server");
				disconnectServer.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {
						panel.removeAll();
						fileMenu.add(connectServer);
						//TCPClient.closingSequence();
						//stop thread
						panel.updateUI();

					}
					
				});
				
				//EXIT			
				exitApp.getAccessibleContext().setAccessibleDescription(
				       "Full exit");
				fileMenu.add(exitApp);
				exitApp.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {
						//TCPClient.closingSequence();
						//BROADCAST QUIT MESSAGE TO ALL USERS
						panel.removeAll();
						System.exit(0);
					}
					
				});
				

				panel.setSize(500,500);
				panel.setLayout(new GridBagLayout());
				frame.setJMenuBar(menuBar);
				frame.pack();
				frame.setSize(800,800);
				frame.setVisible(true);
	}	
}
