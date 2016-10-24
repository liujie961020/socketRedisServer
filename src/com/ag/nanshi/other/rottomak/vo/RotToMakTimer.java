package com.ag.nanshi.other.rottomak.vo;

import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RotToMakTimer {

	/**
	 * 抢麦Socket绑定的计时器
	 * */
	public static Map<Socket, Object> rotToMakTimer = new ConcurrentHashMap<>();
	
	/**
	 * 房间剩余时间
	 * */
	public static Map<String, Object> roomRemaining = new ConcurrentHashMap<>();
	
}
