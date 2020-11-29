package main;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.io.*;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class Channel {
	
	String name;
	private CopyOnWriteArrayList<ClientHandler> members;
	
	public Channel(String name) {
		this.name = name;
		members = new CopyOnWriteArrayList<ClientHandler>();
	}
	
	public String getName() {
		return name;
	}
	
	public synchronized void addMember(ClientHandler client) {
		members.add(client);
		return;
	}
	
	public synchronized void removeMember(ClientHandler client) {
		members.remove(client);
		return;
	}
	
	public synchronized CopyOnWriteArrayList<ClientHandler> getMemberList(){
		return members;
	}
	
	public synchronized ClientHandler getMember(String nickName) {
		for (ClientHandler member: members) {
			if (member.getNickName().equals(nickName)) {
				return member;
			}
		}
		return null;
	}
	
	
}
