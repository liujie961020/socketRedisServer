package com.ag.nanshi.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerListener extends Thread {

	public static int port = 12345;
	
	@Override
	public void run() {
		try {
			@SuppressWarnings("resource")
			ServerSocket server = new ServerSocket(port);
			System.out.println("Server is starting ...");
			while (true) {
				Socket client = server.accept();
				//建立连接  将socket 传�?�给新的进程
				ChatSocket cs = new ChatSocket(client);
				cs.start();			
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		new ServerListener().start();
	}
}
