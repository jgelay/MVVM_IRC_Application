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
	
	public ClientHandler(Socket clientSocket, ChannelManager cm) {
		this.socket = clientSocket;
		this.cm = cm;
		this.messageHandler = new MessageHandler(this);
	}
	
	public void run() {
		try 
		{
			
			boolean registered = false;
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
	        	
	        	wr.write(resp);
        		wr.flush();
	        } while (registered == false);
	       
	        currChannel = cm.getChannel("#default");
	        currChannel.addMember(this);
	        System.out.println("User joined default channel");
	        System.out.println("Current users in default channel:");
	        for (ClientHandler cl : currChannel.getMemberList()) {
	        	System.out.println(cl.getNickName());
	        }
	        
			while (!r.equals("end")) {
				if (br.ready()) {
					r = br.readLine();
					System.out.println("Message received: " + r);
	        					
				}
			}
		
		}
		catch (Exception e)
		{
			
		}
		
		try {
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public OutputStreamWriter getOutPutStreamWriter() {
		return wr;
	}
	
	public String getServerPass() {
		return serverPass;
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
                		for (String name : cm.getNickNameList()) {
                			System.out.println(name);
                			nickName = name;
                		}
            		}
            		
				} else if (cm.getNickNameList().contains(messageArray[1])){
            		return ERR_NICKNAMEINUSE;
                } else {
                	throw new IOException();
                }
			
            	if (!cm.getUserList().contains(messageArray[2]) || cm.getUserList().isEmpty()) {
            		cm.getUserList().add(messageArray[2]);
            		
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
