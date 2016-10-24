package com.ag.nanshi.other.rottomak.vo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RotToMakRoomUser {
	
	private static Map<String, Object> rotToMakRoomUserList = new ConcurrentHashMap<>();
	
	/**
	 * 添加抢麦房间用户
	 * */
	public static List<String> addRotToMakRoomUser(String roomId,String userId){
		List<String> userList = null;
		userList = (List<String>)rotToMakRoomUserList.get(roomId);
		if(userList == null){
			userList = new ArrayList<>();
		}else{
			//如果有重复的则不添加
			for(int i=0; i<userList.size(); i++){
				if(userList.get(i).equals(userId)){
					return userList;
				}
			}
		}
		userList.add(userId);
		rotToMakRoomUserList.put(roomId, userList);
		return userList;
	}
	
	/**
	 * 删除抢麦房间用户
	 * */
	public static List<String> removeRotToMakRoomUser(String roomId,String userId){
		List<String> userList = null;
		userList = (List<String>)rotToMakRoomUserList.get(roomId);
		if(userList == null){
			//清除房间
			rotToMakRoomUserList.remove(roomId);
			userList = new ArrayList<>();
		}else{
			for(int i=0; i<userList.size(); i++){
				if(userList.get(i).equals(userId)){
					userList.remove(i);
				}
			}
			rotToMakRoomUserList.put(roomId, userList);
		}
		return userList;
	}
	
	/**
	 * 获取抢麦房间用户
	 * */
	public static List<String> getRotToMakRoomUser(String roomId){
		List<String> userList = null;
		userList = (List<String>)rotToMakRoomUserList.get(roomId);
		if(userList == null){
			userList = new ArrayList<>();
		}
		return userList;
	}

}
