import java.util.concurrent.CopyOnWriteArrayList;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.io.*;
import java.util.*;

public class ChannelManager {
	private CopyOnWriteArrayList<String> nickNamesList = new CopyOnWriteArrayList<String>();
	private CopyOnWriteArrayList<String> userList = new CopyOnWriteArrayList<String>();
	private CopyOnWriteArrayList<Channel> channels = new CopyOnWriteArrayList<Channel>();
	
	public synchronized CopyOnWriteArrayList<String> getNickNameList() {
		return nickNamesList;
	}
	
	public synchronized CopyOnWriteArrayList<String> getUserList() {
		return userList;
	}
	
	public synchronized Channel getChannel(String channelName) {
		Channel currChannel = null;
		for (Channel cm : channels) {
			if (cm.getName().equals(channelName)) {
				currChannel = cm;
				break;
			}
		}
		return currChannel;
	}
	
	
	public synchronized void createChannel(String name) {
		Channel channel = new Channel(name);
		channels.add(channel);
		
		return;
	}
	
}
