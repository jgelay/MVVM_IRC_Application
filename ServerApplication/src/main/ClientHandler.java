package main;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class ClientHandler implements Runnable {
	private Socket socket;
	private String serverPass = "TESTING";
	
	private String ERR_PASSWDMISMATCH = "464";
	private String ERR_NONICKNAMEGIVEN = "431";
	private String ERR_NICKNAMEINUSE = "433";
	private String ERR_USERNAMEINUSE = "434";
	private String ERR_ERRONEUSNICKNAME = "432";
	private String RPL_WELCOME = "001";
	
	
	ChannelManager cm;
	Channel currChannel;
	
	private InputStream is;
	private BufferedReader br;
	private OutputStreamWriter wr;
	private MessageHandler messageHandler;
	
	private String nickName;
	private String userName;
	private volatile boolean connected;
	private volatile boolean registered;
	private ClientHandler currMember;
	
	public ClientHandler(Socket clientSocket, ChannelManager cm) {
		this.socket = clientSocket;
		this.cm = cm;
		this.messageHandler = new MessageHandler(this);
	}
	
	public void run() {
		try 
		{
			connected = true;
			registered = false;
			String resp;
			is = socket.getInputStream();
			br  = new BufferedReader(new InputStreamReader(is));
			String r = "Hello from Server";
			// make sure to send something back ...
			
	        wr = new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.US_ASCII);
	        System.out.println(r);
	        do {
	        	resp = client_Registration(br.readLine());
	        	if (resp == RPL_WELCOME) {
	        		registered = true;
	        	} 
	        	
	        	System.out.println(resp);
	        	wr.write(resp + "\n");
        		wr.flush();
	        } while (registered == false);
	       
	        
	        currChannel = cm.getChannel("#default");
	        currChannel.addMember(this);
	        System.out.println("User joined default channel");
	        System.out.println("Current users in default channel:");
	        for (ClientHandler cl : currChannel.getMemberList()) {
	        	System.out.println(cl.getNickName());
	        }
	        readingMessages();
	        
			
		
		}
		catch (IOException e)
		{
		
		}
		
		try {
			System.out.print("I am " + this.getNickName() + " and I am going to close now, goodbye.");
			socket.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void readingMessages() {
		String r;
		try {
			while (connected) {
				if (br.ready()) {
					r = br.readLine();
					System.out.println("Message received: " + r);
					for(ClientHandler member : currChannel.getMemberList()) {
						currMember = member;
						System.out.println("Sending to Member: " + currMember.getNickName());
						currMember.getOutPutStreamWriter().write(this.getNickName() + ":" + r);
						currMember.getOutPutStreamWriter().flush();
					}			
				}
			}
		}
		catch (IOException e) {
			System.out.println("Client may have unexpectedly close");
			if (currMember.getRegisteredState() == true) {
				currChannel.removeMember(currMember);
				cm.getNickNameList().remove(currMember.getNickName());
				cm.getUserList().remove(currMember.getUserName());
				try {
					currMember.getOutPutStreamWriter().flush();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			currMember.setConnected(false);
			readingMessages();
		}
		
		return;
	}
	public TimerTask Test() {
		System.out.println("Testing Timer");
		return null;
	}
	public OutputStreamWriter getOutPutStreamWriter() {
		return wr;
	}
	
	public String getServerPass() {
		return serverPass;
	}
	
	public  void setConnected(boolean state) {
		connected = state;
		return;
	}
	public boolean getRegisteredState() {
		return registered;
	}
	
	public String client_Registration(String credentials) {
		String[] messageArray = null;
		boolean passwordMatched = false;
		System.out.println("Credentials: " + credentials);
		messageArray = credentials.split(" ");
		try {
			System.out.println("Asking for Credentials");
			//System.out.println(messageArray[0]);
        	if (messageArray[0].equals(serverPass)) {
        		passwordMatched = true;
        		System.out.println("Password Matched");	
        	}
             else {
            	throw new IOException();
            }
        	
        	if (passwordMatched == false) {
        		return ERR_PASSWDMISMATCH;
        	}
        	else {
            	if (!cm.getNickNameList().contains(messageArray[1]) || cm.getNickNameList().isEmpty()) {
            		if (!messageHandler.testNick(messageArray[1])) {
            			return ERR_ERRONEUSNICKNAME;
            		} else {
                		cm.getNickNameList().add(messageArray[1]);
                		nickName = messageArray[1];
                		for (String name : cm.getNickNameList()) {
                			System.out.println(name);
                		}
            		}
            		
				} else if (cm.getNickNameList().contains(messageArray[1])){
            		return ERR_NICKNAMEINUSE;
                } else {
                	throw new IOException();
                }
			
            	if (!cm.getUserList().contains(messageArray[2]) || cm.getUserList().isEmpty()) {
            		cm.getUserList().add(messageArray[2]);
            		userName = messageArray[2];
            		
            	} else if (cm.getUserList().contains(messageArray[2])){
            		return ERR_USERNAMEINUSE;
            	} else {
            		throw new IOException();
            	}
            		
        	}
			//this.join_Channel("#default");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("Credentials Matched");
		return RPL_WELCOME;
	}
	
	public String getNickName() {
		return nickName;
	}
	
	public void setNickName(String newNickName) {
		nickName = newNickName;
		return;
	}
	
	public String getUserName() {
		return userName;
	}
	
	public void setUserName(String newUserName) {
		userName = newUserName;
	}
	
	
	public ChannelManager getChannelManager() {
		return cm;
	}
	
}
