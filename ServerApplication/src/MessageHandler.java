import java.util.regex.Pattern;

import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;

import java.io.IOException;
import java.util.Arrays;
import java.util.regex.Matcher;


public class MessageHandler {
	String user = "(\\x01-\\x09|\\x0B-\\x0C|\\x0E-\\x1F|\\x21-x3F\\x41-\\xFF)";
	String server = "";
	String targetmask = "";
	String special = "\\[|\\]|\\\\|\\`|\\_|\\^|\\{|\\}|\\|";
	String shortname = "[a-z|0-9][a-z|0-9|-]*[a-z|0-9]";
	String hostname = shortname + "(\\." + shortname + ")*";
	String ip4addr = "[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}";
	String ip6addr = "([0-9A-F]{1,}(\\:[0-9A-F]{1,}){7,7})|0\\:0\\:0\\:0\\:0\\:(0|FFFF)\\:" + ip4addr;
	String hostaddr = ip4addr + "|" + ip6addr;
	String host = hostname + "|" + hostaddr;
	String servername = hostname;
	String nickname = "([a-zA-z]|" + special + ")(([a-zA-Z]|[0-9]|\\-|" + special + "){0,8})";
	String chanstring = "([\\x01-\\x07]|[\\x08-\\x09]|[\\x0B-\\x0C]|[\\x0E-\\x1F]|[\\x21-\\x2B]|[\\x2D-\\x39]|[\\x3B-\\xFF])";
	String channelid = "([\\x41-\\x5A]|[0-9]){5,5}";
	String channel = "(\\#|\\+|(\\!" + channelid + ")|\\&)" + "((" + chanstring + "){0,50})" + "((\\:" + "((" + chanstring + "){0,50})" + "){0,1})";
	String target = nickname + "|" + server;
	String msgto = channel + "|" + "(" + user + "[" + "\\%" + host + "]" + "\\@" + servername + ")" + "|" + "(" + user + "\\%" + host + ")|" + targetmask + "|" + nickname + "|(" + nickname + "\\!" + user + "\\@" + host + ")";
	
	String space = "\\x20";
	String letter = "([\\x41-\\x5A]|[\\x61-\\x7A])";
	String prefix = "(" + servername + "|" + "(" + nickname + "((((\\!" + user + "){0,1})\\@" + host + "){0,1})))";
	String command = "((" + letter + "){1,}|[0-9]{3,3})";
	String nospcrlfcl = "([\\x01-\\x09]|[\\x0B-\\x0C]|[\\x0E-\\x1F]|[\\x21-\\x39]|[\\x3B-\\xFF])";
	String middle = nospcrlfcl + "(\\:|"  + nospcrlfcl + "){0,}";
	String trailing = "(\\:| |" + nospcrlfcl + "){0,}";
	String SPACE = "\\x20";
	String crlf = "\\x0D\\x0A";
	String params = "(((" + SPACE + middle + "){0,14}(" + SPACE + "\\:" + trailing + "){0,1})|((" + SPACE + middle + "){14,14}(" + SPACE + "(\\:){0,1}" + trailing + "){0,1}))";
	String message = "(\\:" + prefix + space + "){0,1}" + command + "(" + params + "){0,1}" + crlf;
	String test = command + SPACE;
	
	ClientHandler clienthandler;
	public MessageHandler(ClientHandler clienthandler) {
		this.clienthandler = clienthandler;
	}
			
	public int parseMessage(String clientmessage) {
		int numericreply = 0;
		if (clientmessage.matches(message)){
			System.out.print("Client message: " + clientmessage);
			String[] nospcmsg = clientmessage.split(" ", 0);
			System.out.println(nospcmsg[1]);
			switch(nospcmsg[1]) {
				case "PASS" :
					if (clienthandler.getServerPass().equals(nospcmsg[1]) == false){
						//ERR_PASSWDMISMATCH
						numericreply = 464;
					}
					else if (clienthandler.getServerPass().equals(nospcmsg[1])) {
						
					}
				case "NICK" :
					if (nospcmsg.length < 2) {
						//ERR_NONICKNAMEGIVEN
						numericreply = 431;
					}
					else if (!(nospcmsg[1].matches(nickname))){
						//ERR_ERRONEUSNICKNAME
						numericreply = 432;
					}
					else {
						
					}
				case "USER" :
				case "OPER" :
				case "QUIT" :
				case "JOIN" :
					if (nospcmsg.length < 2) {
						//ERR_NEEDMOREPARAMS
						numericreply = 461;
					}
					else {
						String[] channels = nospcmsg[1].split(",",0);
						String[] keylist;
						Boolean chanerr = false;
						if (nospcmsg.length == 3) {
							keylist = nospcmsg[2].split(",",0);
						}
						for (String chan: channels) {
							if (!(chan.matches(channel))){
								//ERR_BADCHANNELKEY
								numericreply = 475;
								chanerr = true;
								break;
							}
						}
						if (chanerr == false) {
							for (String chan: channels) {
								Channel currchan = clienthandler.getChannelManager().getChannel(chan);
								if (currchan == null) {
									//ERR_NOSUCHCHANNEL
									numericreply = 403;
									break;
								}
								else {
									clienthandler.getChannelManager().getChannel(chan).addMember(clienthandler);
								}
							}
						}
						
					}
				case "KICK" :
					if (nospcmsg.length < 2) {
						//ERR_NEEDMOREPARAMS
						numericreply = 461;
					} 
					else {
						String[] channels = nospcmsg[1].split(",",0);
						String[] users = nospcmsg[2].split(",",0);
						Boolean chanerr = false;
						
						for (String chan: channels) {
							if (!(chan.matches(channel))){
								//ERR_BADCHANNELKEY
								numericreply = 475;
								chanerr = true;
								break;
							}
						}
						
						if (chanerr == false) {
							for (String chan: channels) {
								Channel currchan = clienthandler.getChannelManager().getChannel(chan);
								if (currchan == null) {
									//ERR_NOSUCHCHANNEL
									numericreply = 403;
									break;
								}
								else {
									for (String user : users) {
										ClientHandler chan_user = currchan.getMember(user);
										if (chan_user == null) {
											//ERR_USERNOTINCHANNEL
											numericreply = 441;
											break;
										} 
										else {
											currchan.removeMember(chan_user);
										}
										
									}
								}
							}
						}
					}
				case "PRIVMSG" :
					if (nospcmsg.length < 4) {
						//ERR_NORECIPIENT
						numericreply = 411;
					} 
					else if (nospcmsg.length > 4) {
						//ERR_TOOMANYTARGETS
						numericreply = 407;
					}
					else {
						System.out.println("Calling sendMessage method");
						System.out.print("Target: " + nospcmsg[2]);
						String message = String.join(" ", Arrays.copyOfRange(nospcmsg, 3, nospcmsg.length-1));
						this.sendMessage(this.clienthandler, message, nospcmsg[2]);
					}
			}
			
			
		}
		return numericreply;
	}
	private void sendMessage(ClientHandler sender, String message, String target) {
		System.out.println("Inside sendMessage");
		System.out.println("Target inside sendMessage: " + target);
		if (target.matches(channel)){
			System.out.println("Target matches");
			Channel currchan = clienthandler.getChannelManager().getChannel(target);
			for (ClientHandler client : currchan.getMemberList()) {
				System.out.println("Sending message to recepient " + client.getNickName());
				System.out.println("Message is: " + message);
				try 
				{
					client.getOutPutStreamWriter().write(sender.getNickName() + ":" + message);
					client.getOutPutStreamWriter().flush();
				}
				 catch (IOException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return;
	}
	
	public boolean testMessage(String clientmessage) {
		return clientmessage.matches(message);
	}
	
	public boolean testNick(String clientmessage) {
		return clientmessage.matches(nickname);
	}
	
	public boolean testPrefix(String clientmessage) {
		return clientmessage.matches(prefix);
	}
	
	public boolean testChannel(String clientmessage) {
		return clientmessage.matches(channel);
	}
	
	public boolean testChannelString(String clientmessage) {
		return clientmessage.matches(chanstring);
	}
}	
