package com.ag.nanshi.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ChatSocket extends Thread {

	private Socket socket;

	public ChatSocket(Socket socket) {
		super();
		this.socket = socket;
	}
	
	@Override
	public void run() {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(),"UTF-8"));
			String line;
			while ((line=br.readLine())!=null) {
				ChatManager.publish(socket, line);
			}
			br.close();
		} catch (IOException e) {
			//System.out.println(getUnlineSocket(socket)+"�?出连接！");
		}
	}
	
//	public String getUnlineSocket(Socket socket){
//		String machineId = null;
//		for (ClientConnection conn : ChatManager.clients.values()) {
//			if(conn.getSocket() == socket){
//				machineId = conn.getRoomId();
//				
//			}
//		}
//		return machineId;
//	}
	
	
//	@SuppressWarnings("static-access")
//	public void send(String msg){
//		try {
//			msg = msg+"\n";
//			socket.getOutputStream().write(msg.getBytes("UTF-8"));
//		} catch (Exception e) {
//			ChatManager.getChatManager().clients.remove(socket);
//			System.out.println(this.getName()+"与服务器断开连接…�??");
//		}
//	}
}
