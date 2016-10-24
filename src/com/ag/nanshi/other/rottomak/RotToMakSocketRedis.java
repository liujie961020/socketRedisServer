package com.ag.nanshi.other.rottomak;

import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RotToMakSocketRedis {
	
	private static Map<String, Object> rotToMakSocket = new ConcurrentHashMap<>();
	
	private static Map<String, Object> connectionSocket = new ConcurrentHashMap<>();
	
	/**   
	 * 添加连入的Socket
	 * @return 返回该用户对应的Socket列表
 	 */
	public static void addRotToMakSocket(String userId,Socket socket){
		try {
			rotToMakSocket.put(userId, socket);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 删除指定用户的Socket
	 * @return Socket列表
	 * */
	public static void removeRotToMakSocket(String userId){
		try {
			if(rotToMakSocket.get(userId)!=null){
				rotToMakSocket.remove(userId);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取抢麦用户对应的Socket
	 * @return Socket列表
	 * */
	public static Socket getRotToMakSocketList(String userId){
		try {
			return rotToMakSocket.get(userId)!=null?(Socket)rotToMakSocket.get(userId):null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 添加链接的Socket
	 * */
	public static void addConnectionSocket(String userId,Socket socket){
		try {
			connectionSocket.put(userId, socket);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 删除链接Socket
	 * */
	public static void removeConnectionSocket(String userId){
		try {
			if(connectionSocket.get(userId)!=null){
				connectionSocket.remove(userId);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取链接Socket
	 * */
	public static Socket getConnectionSocket(String userId){
		try {
			return connectionSocket.get(userId)!=null?(Socket)connectionSocket.get(userId):null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 根据Socket获取对应的UserId
	 * */
	public static String getUserIdBySocket(Socket socket){
		//遍历所有key组成的集合
		for (String userId : rotToMakSocket.keySet()){
			if(socket == rotToMakSocket.get(userId)){
				return userId;
			}
		}
		return null;
	}

}