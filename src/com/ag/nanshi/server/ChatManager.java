package com.ag.nanshi.server;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.ag.nanshi.utils.DateUtil;
import com.ag.nanshi.other.rottomak.RotToMak;
import com.ag.nanshi.other.rottomak.RotToMakSocketRedis;
import com.ag.nanshi.other.rottomak.RotToMakUser;
import com.ag.nanshi.other.rottomak.vo.RotToMakResult;
import com.ag.nanshi.other.rottomak.vo.RotToMakRoomUser;
import com.ag.nanshi.other.rottomak.vo.RotToMakTimer;
import com.ag.nanshi.constant.type.EventType;
import com.ag.nanshi.utils.JsonUtil;
import com.ag.nanshi.vo.Commons;
import com.ag.nanshi.vo.User;

public class ChatManager {

	/**
	 * 解析与发送消�?
	 */
	public static void publish(Socket socket, String msg) {
		RotToMakResult rotToMakResult = null;
		// 判断该Socket是否绑定计时器
		if (RotToMakTimer.rotToMakTimer.get(socket) == null) {
			RotToMakTimer.rotToMakTimer.put(socket, new Timer());
		}
		try {
			// 获取Socket对应计时器
			final Timer timer = (Timer) RotToMakTimer.rotToMakTimer.get(socket);
			// 输出客户端发送的信息
			System.out.println("客户端发送的消息" + msg);
			// 截取参数
			String roomId = msg.substring(0, msg.indexOf("-"));
			String userResult = msg.substring(msg.indexOf("-") + 1, msg.lastIndexOf("-"));
			String userId = userResult.substring(0, userResult.indexOf("-"));
			String username = userResult.substring(userResult.indexOf("-") + 1, userResult.lastIndexOf("-"));
			String openMicrophoneUserId = userResult.substring(userResult.lastIndexOf("-") + 1);
			String event = msg.substring(msg.lastIndexOf("-") + 1);
			// 添加用户对应的Socket 已添加的会重新覆�?
			RotToMakSocketRedis.addConnectionSocket(userId, socket);
			// 添加房间连接用户
			List<String> roomUserList = RotToMakRoomUser.addRotToMakRoomUser(roomId, userId);
			if (roomId != null && userId != null && event != null) {
				if (event.equals(EventType.INROTTOMAK.getCode())) {
					// 判断该用户是否已经抢麦
					if (!RotToMak.judgeUserIsExistRotToMakUserList(roomId, userId)) {
						User user = new User();
						user.setUserId(userId);
						user.setUsername(username);
						user.setPic("");
						// 添加抢麦用户
						List<RotToMakUser> rotToMakUserList = RotToMak.addRobToMakUserSorting(roomId, user,
								openMicrophoneUserId);
						System.out.println("添加抢麦用户");
						// 添加抢麦Socket
						RotToMakSocketRedis.addRotToMakSocket(userId, socket);
						System.out.println("添加抢麦Socket");
						// 判断当前用户是否为第一位
						if (rotToMakUserList.size() == 1) {
							// 设置房间起始时间
							RotToMakTimer.roomRemaining.put(roomId, Commons.getNowTime());
							// 设置返回客户端Socket信息
							rotToMakResult = new RotToMakResult("resultData", rotToMakUserList, 180);
							// 启动计时器
							System.out.println("启动计时器");
							godie(timer, socket, roomId);
						} else {
							// 获取所属房间抢麦时间
							String roomRemainingTime = RotToMakTimer.roomRemaining.get(roomId).toString();
							// 计算创建时间与当前时间时间差
							long time = getTwoTimeOut(DateUtil.toDate(roomRemainingTime),
									DateUtil.toDate(Commons.getNowTime()));
							// 设置凡湖客户端Socket信息
							rotToMakResult = new RotToMakResult("resultData", rotToMakUserList, 180 - time);
						}
						// 查询连接Socket
						List<Socket> sendSocketList = getConnectionSocketList(roomUserList);
						for (Socket socketSend : sendSocketList) {
							try {
								sendMsg(JsonUtil.toJson(rotToMakResult), socketSend);
							} catch (Exception e) {
								// 获取发消息失败的Socket的userId
								String socketUserId = RotToMakSocketRedis.getUserIdBySocket(socketSend);
								// 删除对应房间的用户
								RotToMakRoomUser.removeRotToMakRoomUser(roomId, socketUserId);
								// 删除用户对应的Socket
								RotToMakSocketRedis.removeRotToMakSocket(socketUserId);
								// 删除连接的Socket
								RotToMakSocketRedis.removeConnectionSocket(socketUserId);
								// 关闭连接
								socketSend.close();
							}
						}
					} else {
						// 查询该房间抢麦用户
						List<RotToMakUser> rotToMakUserList = RotToMak.getRotToMakUserList(roomId);
						// 获取所属房间抢麦时间
						String roomRemainingTime = RotToMakTimer.roomRemaining.get(roomId).toString();
						// 计算创建时间与当前时间时间差
						long time = getTwoTimeOut(DateUtil.toDate(roomRemainingTime),
								DateUtil.toDate(Commons.getNowTime()));
						// 设置返回客户端Socket信息
						rotToMakResult = new RotToMakResult("resultData", rotToMakUserList, 180 - time);
						List<Socket> sendSocketList = getConnectionSocketList(roomUserList);
						for (Socket socketSend : sendSocketList) {
							try {
								sendMsg(JsonUtil.toJson(rotToMakResult), socketSend);
							} catch (Exception e) {
								// 获取发消息失败的Socket的userId
								String socketUserId = RotToMakSocketRedis.getUserIdBySocket(socketSend);
								// 删除对应房间的用户
								RotToMakRoomUser.removeRotToMakRoomUser(roomId, socketUserId);
								// 删除用户对应的Socket
								RotToMakSocketRedis.removeRotToMakSocket(socketUserId);
								// 删除连接的Socket
								RotToMakSocketRedis.removeConnectionSocket(socketUserId);
								// 判断该用户是否存在于当前抢麦列表
								if (RotToMak.judgeUserIsExistRotToMakUserList(roomId, socketUserId)) {
									User user = new User();
									user.setUserId(socketUserId);
									// 查询连接的Socket
									sendSocketList = getConnectionSocketList(roomUserList);
									// 判断当前用户是否为上麦用户
									boolean flag = RotToMak.judgeUserIsFirstRotToMakUser(roomId, user);
									// 删除麦序列表的对应用户
									rotToMakUserList = RotToMak.removeRotToMakUserListByUserId(roomId, socketUserId);
									System.out.println("删除抢麦用户");
									if (flag) {
										timer.cancel();
										System.out.println("停止了计时器");
										RotToMakTimer.rotToMakTimer.remove(socket);
										System.out.println("删除了计时器");
										if (rotToMakUserList.size() > 0) {
											// 设置房间起始时间
											RotToMakTimer.roomRemaining.put(roomId, Commons.getNowTime());
											rotToMakResult = new RotToMakResult("resultData", rotToMakUserList, 180);
											// 获取第二个人计时器
											Timer twoTimer = (Timer) RotToMakTimer.rotToMakTimer.get(RotToMakSocketRedis
													.getRotToMakSocketList(rotToMakUserList.get(0).getUserId()));
											// 启动第二个人计时器
											godie(twoTimer, RotToMakSocketRedis.getRotToMakSocketList(
													rotToMakUserList.get(0).getUserId()), roomId);
											System.out.println("启动第二个人计时器");
											// 更改房间直播开始时间
											rotToMakResult = new RotToMakResult("resultData", rotToMakUserList, 180);
										} else {
											rotToMakResult = new RotToMakResult("resultData", rotToMakUserList, -1);
										}
										// 更改房间直播开始时间
										RotToMakTimer.roomRemaining.put(roomId, Commons.getNowTime());
									} else {
										// 查询该房间抢麦用户
										rotToMakUserList = RotToMak.getRotToMakUserList(roomId);
										// 获取�?属房间抢麦时间
										roomRemainingTime = RotToMakTimer.roomRemaining.get(roomId).toString();
										// 计算创建时间与当前时间时间差(�?)
										time = getTwoTimeOut(DateUtil.toDate(roomRemainingTime),
												DateUtil.toDate(Commons.getNowTime()));
										// 设置返回客户端Socket信息
										rotToMakResult = new RotToMakResult("resultData", rotToMakUserList, 180 - time);
									}
								}
								// 关闭连接
								socketSend.close();
							}
						}
					}
				} else if (event.equals(EventType.OUTROTTOMAK.getCode())) {
					if (RotToMak.judgeUserIsExistRotToMakUserList(roomId, userId)) {
						long time = 180;
						User user = new User();
						user.setUserId(userId);
						// 查询连接的Socket
						List<Socket> sendSocketList = getConnectionSocketList(roomUserList);
						// 判断当前用户是否为上麦�??
						boolean flag = RotToMak.judgeUserIsFirstRotToMakUser(roomId, user);
						// 删除麦序列表的对应用�?
						List<RotToMakUser> rotToMakUserList = RotToMak.removeRotToMakUserListByUserId(roomId, userId);
						System.out.println("删除抢麦用户");
						// 删除抢麦Socket
						RotToMakSocketRedis.removeRotToMakSocket(userId);
						System.out.println("删除抢麦Socket");
						if (flag) {
							timer.cancel();
							System.out.println("停止了计时器");
							RotToMakTimer.rotToMakTimer.remove(socket);
							System.out.println("删除了计时器");
							if (rotToMakUserList.size() > 0) {
								// 设置房间起始时间
								RotToMakTimer.roomRemaining.put(roomId, Commons.getNowTime());
								rotToMakResult = new RotToMakResult("resultData", rotToMakUserList, 180);
								// 获取第二个人计时�?
								Timer twoTimer = (Timer) RotToMakTimer.rotToMakTimer.get(
										RotToMakSocketRedis.getRotToMakSocketList(rotToMakUserList.get(0).getUserId()));
								// 启动第二个人计时�?
								godie(twoTimer,
										RotToMakSocketRedis.getRotToMakSocketList(rotToMakUserList.get(0).getUserId()),
										roomId);
								System.out.println("启动第二个人计时器");
								// 更改房间直播�?始时�?
								rotToMakResult = new RotToMakResult("resultData", rotToMakUserList, 180);
							} else {
								rotToMakResult = new RotToMakResult("resultData", rotToMakUserList, -1);
							}
							// 更改房间直播�?始时�?
							RotToMakTimer.roomRemaining.put(roomId, Commons.getNowTime());
						} else {
							// 查询该房间抢麦用�?
							rotToMakUserList = RotToMak.getRotToMakUserList(roomId);
							// 获取�?属房间抢麦时�?
							String roomRemainingTime = RotToMakTimer.roomRemaining.get(roomId).toString();
							// 计算创建时间与当前时间时间差(�?)
							time = getTwoTimeOut(DateUtil.toDate(roomRemainingTime),
									DateUtil.toDate(Commons.getNowTime()));
							// 设置返回客户端Socket信息
							rotToMakResult = new RotToMakResult("resultData", rotToMakUserList, 180 - time);
						}
						for (Socket socketSend : sendSocketList) {
							try {
								sendMsg(JsonUtil.toJson(rotToMakResult), socketSend);
							} catch (Exception e) {
								// 获取发�?�失败的Socket的userId
								String socketUserId = RotToMakSocketRedis.getUserIdBySocket(socketSend);
								// 删除对应房间的用�?
								RotToMakRoomUser.removeRotToMakRoomUser(roomId, socketUserId);
								// 删除用户对应的Socket
								RotToMakSocketRedis.removeRotToMakSocket(socketUserId);
								// 删除连接的Socket
								RotToMakSocketRedis.removeConnectionSocket(socketUserId);
								// 判断该用户是否存在于当前抢麦列表
								if (RotToMak.judgeUserIsExistRotToMakUserList(roomId, socketUserId)) {
									user = new User();
									user.setUserId(socketUserId);
									// 查询连接的Socket
									sendSocketList = getConnectionSocketList(roomUserList);
									// 判断当前用户是否为上麦�??
									flag = RotToMak.judgeUserIsFirstRotToMakUser(roomId, user);
									// 删除麦序列表的对应用�?
									rotToMakUserList = RotToMak.removeRotToMakUserListByUserId(roomId, socketUserId);
									System.out.println("删除抢麦用户");
									if (flag) {
										timer.cancel();
										System.out.println("停止了计时器");
										RotToMakTimer.rotToMakTimer.remove(socket);
										System.out.println("删除了计时器");
										if (rotToMakUserList.size() > 0) {
											// 设置房间起始时间
											RotToMakTimer.roomRemaining.put(roomId, Commons.getNowTime());
											rotToMakResult = new RotToMakResult("resultData", rotToMakUserList, 180);
											// 获取第二个人计时�?
											Timer twoTimer = (Timer) RotToMakTimer.rotToMakTimer.get(RotToMakSocketRedis
													.getRotToMakSocketList(rotToMakUserList.get(0).getUserId()));
											// 启动第二个人计时�?
											godie(twoTimer, RotToMakSocketRedis.getRotToMakSocketList(
													rotToMakUserList.get(0).getUserId()), roomId);
											System.out.println("启动第二个人计时器");
											// 更改房间直播�?始时�?
											rotToMakResult = new RotToMakResult("resultData", rotToMakUserList, 180);
										} else {
											rotToMakResult = new RotToMakResult("resultData", rotToMakUserList, -1);
										}
										// 更改房间直播�?始时�?
										RotToMakTimer.roomRemaining.put(roomId, Commons.getNowTime());
									} else {
										// 查询该房间抢麦用�?
										rotToMakUserList = RotToMak.getRotToMakUserList(roomId);
										// 获取�?属房间抢麦时�?
										String roomRemainingTime = RotToMakTimer.roomRemaining.get(roomId).toString();
										// 计算创建时间与当前时间时间差(�?)
										time = getTwoTimeOut(DateUtil.toDate(roomRemainingTime),
												DateUtil.toDate(Commons.getNowTime()));
										// 设置返回客户端Socket信息
										rotToMakResult = new RotToMakResult("resultData", rotToMakUserList, 180 - time);
									}
								}
								// 关闭连接
								socketSend.close();
							}
						}
					} else {
						// 查询该房间抢麦用�?
						List<RotToMakUser> rotToMakUserList = RotToMak.getRotToMakUserList(roomId);
						// 获取�?属房间抢麦时�?
						String roomRemainingTime = RotToMakTimer.roomRemaining.get(roomId).toString();
						// 计算创建时间与当前时间时间差(�?)
						long time = getTwoTimeOut(DateUtil.toDate(roomRemainingTime),
								DateUtil.toDate(Commons.getNowTime()));
						// 设置凡湖客户端Socket信息
						rotToMakResult = new RotToMakResult("resultData", rotToMakUserList, 180 - time);
						List<Socket> sendSocketList = getConnectionSocketList(roomUserList);
						for (Socket socketSend : sendSocketList) {
							try {
								sendMsg(JsonUtil.toJson(rotToMakResult), socketSend);
							} catch (Exception e) {
								// 获取发�?�失败的Socket的userId
								String socketUserId = RotToMakSocketRedis.getUserIdBySocket(socketSend);
								// 删除对应房间的用�?
								RotToMakRoomUser.removeRotToMakRoomUser(roomId, socketUserId);
								// 删除用户对应的Socket
								RotToMakSocketRedis.removeRotToMakSocket(socketUserId);
								// 删除连接的Socket
								RotToMakSocketRedis.removeConnectionSocket(socketUserId);
								// 判断该用户是否存在于当前抢麦列表
								if (RotToMak.judgeUserIsExistRotToMakUserList(roomId, socketUserId)) {
									User user = new User();
									user.setUserId(socketUserId);
									// 查询连接的Socket
									sendSocketList = getConnectionSocketList(roomUserList);
									// 判断当前用户是否为上麦�??
									boolean flag = RotToMak.judgeUserIsFirstRotToMakUser(roomId, user);
									// 删除麦序列表的对应用�?
									rotToMakUserList = RotToMak.removeRotToMakUserListByUserId(roomId, socketUserId);
									System.out.println("删除抢麦用户");
									if (flag) {
										timer.cancel();
										System.out.println("停止了计时器");
										RotToMakTimer.rotToMakTimer.remove(socket);
										System.out.println("删除了计时器");
										if (rotToMakUserList.size() > 0) {
											// 设置房间起始时间
											RotToMakTimer.roomRemaining.put(roomId, Commons.getNowTime());
											rotToMakResult = new RotToMakResult("resultData", rotToMakUserList, 180);
											// 获取第二个人计时�?
											Timer twoTimer = (Timer) RotToMakTimer.rotToMakTimer.get(RotToMakSocketRedis
													.getRotToMakSocketList(rotToMakUserList.get(0).getUserId()));
											// 启动第二个人计时�?
											godie(twoTimer, RotToMakSocketRedis.getRotToMakSocketList(
													rotToMakUserList.get(0).getUserId()), roomId);
											System.out.println("启动第二个人计时器");
											// 更改房间直播�?始时�?
											rotToMakResult = new RotToMakResult("resultData", rotToMakUserList, 180);
										} else {
											rotToMakResult = new RotToMakResult("resultData", rotToMakUserList, -1);
										}
										// 更改房间直播�?始时�?
										RotToMakTimer.roomRemaining.put(roomId, Commons.getNowTime());
									} else {
										// 查询该房间抢麦用�?
										rotToMakUserList = RotToMak.getRotToMakUserList(roomId);
										// 获取�?属房间抢麦时�?
										roomRemainingTime = RotToMakTimer.roomRemaining.get(roomId).toString();
										// 计算创建时间与当前时间时间差(�?)
										time = getTwoTimeOut(DateUtil.toDate(roomRemainingTime),
												DateUtil.toDate(Commons.getNowTime()));
										// 设置返回客户端Socket信息
										rotToMakResult = new RotToMakResult("resultData", rotToMakUserList, 180 - time);
									}
								}
								// 关闭连接
								socketSend.close();
							}

						}
					}
				} else if (event.equals(EventType.OUTROOM.getCode())) {

				} else {
					// 获取抢麦剩余时间
					if (RotToMak.getRotToMakUserList(roomId) != null) {
						if (RotToMak.getRotToMakUserList(roomId).size() > 0) {
							if(RotToMakTimer.roomRemaining.get(roomId)!=null){
								String roomRemainingTime = RotToMakTimer.roomRemaining.get(roomId).toString();
								long time = getTwoTimeOut(DateUtil.toDate(roomRemainingTime),
										DateUtil.toDate(Commons.getNowTime()));
								List<RotToMakUser> rotToMakUserList = RotToMak.getRotToMakUserList(roomId);
								rotToMakResult = new RotToMakResult("resultData", rotToMakUserList, 180 - time);
								sendMsg(JsonUtil.toJson(rotToMakResult), socket);
							}else{
								List<RotToMakUser> rotToMakUserList = RotToMak.getRotToMakUserList(roomId);
								rotToMakResult = new RotToMakResult("resultData", rotToMakUserList, -1);
								sendMsg(JsonUtil.toJson(rotToMakResult), socket);
							}
						} else {
							List<RotToMakUser> rotToMakUserList = RotToMak.getRotToMakUserList(roomId);
							rotToMakResult = new RotToMakResult("resultData", rotToMakUserList, -1);
							sendMsg(JsonUtil.toJson(rotToMakResult), socket);
						}
					} else {
						List<RotToMakUser> rotToMakUserList = RotToMak.getRotToMakUserList(roomId);
						rotToMakResult = new RotToMakResult("resultData", rotToMakUserList, -1);
						sendMsg(JsonUtil.toJson(rotToMakResult), socket);
					}
				}
			}
		} catch (

		Exception e)

		{
			e.printStackTrace();
		}

	}

	/**
	 * 发�?�消�?
	 * 
	 * @throws Exception
	 */
	public static void sendMsg(String message, Socket socket) throws Exception {
		try {
			message = message + "\r\n";
			System.out.println("发出数据" + message);
			// System.out.println("二进�?:" + message.getBytes("UTF-8"));
			System.out.println("发送的Socket的Ip地址" + socket.getInetAddress().getHostAddress());
			DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
			dataOutputStream.write(message.getBytes("UTF-8"));
			System.out.println("发送成功");
		} catch (Exception e) {
			System.out.println("发送失败");
			throw e;
		}
	}

	/**
	 * 定时删除第一位用户并向所有该房间socket返回房间用户列表
	 */
	public static void godie(final Timer timer, final Socket socket, final String roomId) {
		timer.schedule(new TimerTask() {
			public void run() {
				try {
					RotToMakResult rotToMakResult = new RotToMakResult("exitRotToMak", new ArrayList<RotToMakUser>());
					sendMsg(JsonUtil.toJson(rotToMakResult), socket);
				} catch (Exception e) {
					System.out.println("定时器发送消息异常");
					RotToMakResult rotToMakResult = null;
					// 获取发送失败的Socket的userId
					String socketUserId = RotToMakSocketRedis.getUserIdBySocket(socket);
					// 删除对应房间的用户
					RotToMakRoomUser.removeRotToMakRoomUser(roomId, socketUserId);
					// 删除用户对应的Socket
					RotToMakSocketRedis.removeRotToMakSocket(socketUserId);
					// 删除连接的Socket
					RotToMakSocketRedis.removeConnectionSocket(socketUserId);
					// 添加房间连接用户
					List<String> roomUserList = RotToMakRoomUser.getRotToMakRoomUser(roomId);
					// 判断该用户是否存在于当前抢麦列表
					if (RotToMak.judgeUserIsExistRotToMakUserList(roomId, socketUserId)) {
						User user = new User();
						user.setUserId(socketUserId);
						// 查询连接的Socket
						List<Socket> sendSocketList = getConnectionSocketList(roomUserList);
						// 判断当前用户是否为上麦者
						boolean flag = RotToMak.judgeUserIsFirstRotToMakUser(roomId, user);
						// 删除麦序列表的对应用户
						List<RotToMakUser> rotToMakUserList = RotToMak.removeRotToMakUserListByUserId(roomId,
								socketUserId);
						System.out.println("删除抢麦用户");
						if (flag) {
							timer.cancel();
							System.out.println("停止了计时器");
							RotToMakTimer.rotToMakTimer.remove(socket);
							System.out.println("删除了计时器");
							if (rotToMakUserList.size() > 0) {
								// 设置房间起始时间
								RotToMakTimer.roomRemaining.put(roomId, Commons.getNowTime());
								rotToMakResult = new RotToMakResult("resultData", rotToMakUserList, 180);
								// 获取第二个人计时�?
								Timer twoTimer = (Timer) RotToMakTimer.rotToMakTimer.get(
										RotToMakSocketRedis.getRotToMakSocketList(rotToMakUserList.get(0).getUserId()));
								// 启动第二个人计时�?
								godie(twoTimer,
										RotToMakSocketRedis.getRotToMakSocketList(rotToMakUserList.get(0).getUserId()),
										roomId);
								System.out.println("启动第二个人计时器");
								// 更改房间直播�?始时�?
								rotToMakResult = new RotToMakResult("resultData", rotToMakUserList, 180);
							} else {
								rotToMakResult = new RotToMakResult("resultData", rotToMakUserList, -1);
							}
							// 更改房间直播�?始时�?
							RotToMakTimer.roomRemaining.put(roomId, Commons.getNowTime());
							sendSocketList = getConnectionSocketList(roomUserList);
							for (Socket socketSend : sendSocketList) {
								try {
									sendMsg(JsonUtil.toJson(rotToMakResult), socketSend);
								} catch (Exception e44) {
									// 获取发�?�失败的Socket的userId
									socketUserId = RotToMakSocketRedis.getUserIdBySocket(socketSend);
									// 删除对应房间的用�?
									RotToMakRoomUser.removeRotToMakRoomUser(roomId, socketUserId);
									// 删除用户对应的Socket
									RotToMakSocketRedis.removeRotToMakSocket(socketUserId);
									// 删除连接的Socket
									RotToMakSocketRedis.removeConnectionSocket(socketUserId);
									// 判断该用户是否存在于当前抢麦列表
									if (RotToMak.judgeUserIsExistRotToMakUserList(roomId, socketUserId)) {
										user = new User();
										user.setUserId(socketUserId);
										// 查询连接的Socket
										sendSocketList = getConnectionSocketList(roomUserList);
										// 判断当前用户是否为上麦�??
										flag = RotToMak.judgeUserIsFirstRotToMakUser(roomId, user);
										// 删除麦序列表的对应用�?
										rotToMakUserList = RotToMak.removeRotToMakUserListByUserId(roomId,
												socketUserId);
										System.out.println("删除抢麦用户");
										if (flag) {
											timer.cancel();
											System.out.println("停止了计时器");
											RotToMakTimer.rotToMakTimer.remove(socket);
											System.out.println("删除了计时器");
											if (rotToMakUserList.size() > 0) {
												// 设置房间起始时间
												RotToMakTimer.roomRemaining.put(roomId, Commons.getNowTime());
												rotToMakResult = new RotToMakResult("resultData", rotToMakUserList,
														180);
												// 获取第二个人计时�?
												Timer twoTimer = (Timer) RotToMakTimer.rotToMakTimer
														.get(RotToMakSocketRedis.getRotToMakSocketList(
																rotToMakUserList.get(0).getUserId()));
												// 启动第二个人计时�?
												godie(twoTimer, RotToMakSocketRedis.getRotToMakSocketList(
														rotToMakUserList.get(0).getUserId()), roomId);
												System.out.println("启动第二个人计时器");
												// 更改房间直播�?始时�?
												rotToMakResult = new RotToMakResult("resultData", rotToMakUserList,
														180);
											} else {
												rotToMakResult = new RotToMakResult("resultData", rotToMakUserList, -1);
											}
											// 更改房间直播�?始时�?
											RotToMakTimer.roomRemaining.put(roomId, Commons.getNowTime());
										} else {
											// 查询该房间抢麦用�?
											rotToMakUserList = RotToMak.getRotToMakUserList(roomId);
											// 获取�?属房间抢麦时�?
											String roomRemainingTime = RotToMakTimer.roomRemaining.get(roomId)
													.toString();
											// 计算创建时间与当前时间时间差(�?)
											long time = getTwoTimeOut(DateUtil.toDate(roomRemainingTime),
													DateUtil.toDate(Commons.getNowTime()));
											// 设置返回客户端Socket信息
											rotToMakResult = new RotToMakResult("resultData", rotToMakUserList,
													180 - time);
										}
									}
									// 关闭连接
									try {
										socketSend.close();
									} catch (IOException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
								}

							}
						} else {
							// 查询该房间抢麦用�?
							rotToMakUserList = RotToMak.getRotToMakUserList(roomId);
							// 获取�?属房间抢麦时�?
							String roomRemainingTime = RotToMakTimer.roomRemaining.get(roomId).toString();
							// 计算创建时间与当前时间时间差(�?)
							long time = getTwoTimeOut(DateUtil.toDate(roomRemainingTime),
									DateUtil.toDate(Commons.getNowTime()));
							// 设置返回客户端Socket信息
							rotToMakResult = new RotToMakResult("resultData", rotToMakUserList, 180 - time);
							sendSocketList = getConnectionSocketList(roomUserList);
							for (Socket socketSend : sendSocketList) {
								try {
									sendMsg(JsonUtil.toJson(rotToMakResult), socketSend);
								} catch (Exception e45) {
									// 获取发�?�失败的Socket的userId
									socketUserId = RotToMakSocketRedis.getUserIdBySocket(socketSend);
									// 删除对应房间的用�?
									RotToMakRoomUser.removeRotToMakRoomUser(roomId, socketUserId);
									// 删除用户对应的Socket
									RotToMakSocketRedis.removeRotToMakSocket(socketUserId);
									// 删除连接的Socket
									RotToMakSocketRedis.removeConnectionSocket(socketUserId);
									// 判断该用户是否存在于当前抢麦列表
									if (RotToMak.judgeUserIsExistRotToMakUserList(roomId, socketUserId)) {
										user = new User();
										user.setUserId(socketUserId);
										// 查询连接的Socket
										sendSocketList = getConnectionSocketList(roomUserList);
										// 判断当前用户是否为上麦�??
										flag = RotToMak.judgeUserIsFirstRotToMakUser(roomId, user);
										// 删除麦序列表的对应用�?
										rotToMakUserList = RotToMak.removeRotToMakUserListByUserId(roomId,
												socketUserId);
										System.out.println("删除抢麦用户");
										if (flag) {
											timer.cancel();
											System.out.println("停止了计时器");
											RotToMakTimer.rotToMakTimer.remove(socket);
											System.out.println("删除了计时器");
											if (rotToMakUserList.size() > 0) {
												// 设置房间起始时间
												RotToMakTimer.roomRemaining.put(roomId, Commons.getNowTime());
												rotToMakResult = new RotToMakResult("resultData", rotToMakUserList,
														180);
												// 获取第二个人计时�?
												Timer twoTimer = (Timer) RotToMakTimer.rotToMakTimer
														.get(RotToMakSocketRedis.getRotToMakSocketList(
																rotToMakUserList.get(0).getUserId()));
												// 启动第二个人计时�?
												godie(twoTimer, RotToMakSocketRedis.getRotToMakSocketList(
														rotToMakUserList.get(0).getUserId()), roomId);
												System.out.println("启动第二个人计时器");
												// 更改房间直播�?始时�?
												rotToMakResult = new RotToMakResult("resultData", rotToMakUserList,
														180);
											} else {
												rotToMakResult = new RotToMakResult("resultData", rotToMakUserList, -1);
											}
											// 更改房间直播�?始时�?
											RotToMakTimer.roomRemaining.put(roomId, Commons.getNowTime());
										} else {
											// 查询该房间抢麦用�?
											rotToMakUserList = RotToMak.getRotToMakUserList(roomId);
											// 获取�?属房间抢麦时�?
											roomRemainingTime = RotToMakTimer.roomRemaining.get(roomId).toString();
											// 计算创建时间与当前时间时间差(�?)
											time = getTwoTimeOut(DateUtil.toDate(roomRemainingTime),
													DateUtil.toDate(Commons.getNowTime()));
											// 设置返回客户端Socket信息
											rotToMakResult = new RotToMakResult("resultData", rotToMakUserList,
													180 - time);
										}
									}
									// 关闭连接
									try {
										socketSend.close();
									} catch (IOException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
								}

							}
						}
					}
					try {
						socket.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					timer.cancel();
				}
			}
		}, 20 * 1000);
	}

	/**
	 * 获取两个时间相隔的秒�?
	 * 
	 * @author liujie
	 * @时间 2016�?6�?17�?15:29:10
	 */
	public static long getTwoTimeOut(Date startTime, Date endTime) {
		Calendar dateOne = Calendar.getInstance();
		Calendar dateTwo = Calendar.getInstance();
		dateOne.setTime(startTime); // 设置时间
		dateTwo.setTime(endTime); // 设置时间
		long timeOne = dateOne.getTimeInMillis();
		long timeTwo = dateTwo.getTimeInMillis();
		long minute = (timeTwo - timeOne) / 1000;// 转化minute
		return minute <= 0 ? 0 : minute;
	}

	/**
	 * 根据抢麦用户获取抢麦socket列表
	 */
	public static List<Socket> getConnectionSocketList(List<String> userList) {
		List<Socket> socketList = new ArrayList<>();
		for (String userId : userList) {
			Socket socket = RotToMakSocketRedis.getConnectionSocket(userId);
			if (socket != null) {
				socketList.add(socket);
			}
		}
		return socketList;
	}

}
