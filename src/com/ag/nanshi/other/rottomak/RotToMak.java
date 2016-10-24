package com.ag.nanshi.other.rottomak;

import java.util.ArrayList;
import java.util.List;
import com.ag.nanshi.redis.RedisPoolCommons;
import com.ag.nanshi.redis.transcoder.rottomakuser.ListTranscoder;
import com.ag.nanshi.utils.ListUtils;
import com.ag.nanshi.vo.User;

import redis.clients.jedis.Jedis;

public class RotToMak {
	
	private static String key="rotToMak";

	/**
	 * 定时任务 为直播列表排序添加抢麦列表 返回抢麦人员列表
	 */
	public static List<RotToMakUser> addRobToMakUserSorting(String roomId, User user, String openMicrophoneUserId) {
		Jedis robToMakJedis = RedisPoolCommons.getJedisPool().getResource();
		List<RotToMakUser> userList = null;
		try {
			RotToMakUser rotToMakUser = new RotToMakUser();
			rotToMakUser.setPic(user.getPic());
			rotToMakUser.setOpenMicrophoneUserId(openMicrophoneUserId);
			rotToMakUser.setUserId(user.getUserId());
			rotToMakUser.setUsername(user.getUsername());
			byte[] in = robToMakJedis.get((key+roomId).getBytes());
			userList = (List<RotToMakUser>) ListTranscoder.deserialize(in);
			if(!ListUtils.isNotEmpty(userList)){
				userList=new ArrayList<>();
			}
			userList.add(rotToMakUser);
			robToMakJedis.set((key+roomId).getBytes(), ListTranscoder.serialize(userList));
			return userList;
		} catch (Exception e) {
			userList=new ArrayList<>();
			//Log4jUtil.CommonLog.error("为直播列表排序添加抢麦列表-addRobToMakUserSorting"+e.getMessage());
			e.printStackTrace();
			return userList;
		}finally {
			robToMakJedis.close();
		}
	}

	/**
	 * 删除指定房间指定排队用户
	 */
	public static List<RotToMakUser> removeRotToMakUserListByUserId(String roomId, String userId) {
		Jedis robToMakJedis = RedisPoolCommons.getJedisPool().getResource();
		List<RotToMakUser> userList = null;
		try {
			if (robToMakJedis.get((key+roomId).getBytes()) != null) {
				byte[] in = robToMakJedis.get((key+roomId).getBytes());
				userList = (List<RotToMakUser>) ListTranscoder.deserialize(in);
				for (int i = 0; i < userList.size(); i++) {
					RotToMakUser rotToMakUser = userList.get(i);
					if (rotToMakUser.getUserId().equals(userId)) {
						userList.remove(i);
					}
				}
			} else {
				userList = new ArrayList<>();
			}
			if(userList.size()==0){
				robToMakJedis.del((key+roomId).getBytes());
			}else{
				robToMakJedis.set((key+roomId).getBytes(), ListTranscoder.serialize(userList));
			}
			robToMakJedis.set((key+roomId).getBytes(), ListTranscoder.serialize(userList));
			return userList;
		} catch (Exception e) {
			userList=new ArrayList<>();
			//Log4jUtil.CommonLog.error("删除指定房间指定排队用户-removeRotToMakUserListByUserId"+e.getMessage());
			e.printStackTrace();
			return userList;
		} finally {
			robToMakJedis.close();
		}
	}

	/**
	 * 获取抢麦用户列表
	 */
	public static List<RotToMakUser> getRotToMakUserList(String roomId) {
		Jedis robToMakJedis = RedisPoolCommons.getJedisPool().getResource();
		List<RotToMakUser> userList = null;
		try {
			if (robToMakJedis.get((key+roomId).getBytes()) == null) {
				userList = new ArrayList<>();
			} else {
				byte[] in = robToMakJedis.get((key+roomId).getBytes());
				userList = (List<RotToMakUser>) ListTranscoder.deserialize(in);
			}
			return userList;
		} catch (Exception e) {
			userList = new ArrayList<>();
			//Log4jUtil.CommonLog.error("获取抢麦用户列表-getRotToMakUserList"+e.getMessage());
			e.printStackTrace();
			return userList;
		} finally {
			robToMakJedis.close();
		}
	}
	
	/**
	 * 根据房间号删除该房间第一抢麦用户
	 * */
	public static List<RotToMakUser> removeFirstRotToMakUser(String roomId){
		Jedis robToMakJedis = RedisPoolCommons.getJedisPool().getResource();
		List<RotToMakUser> userList = null;
		try {
			if (robToMakJedis.get((key+roomId).getBytes()) != null) {
				byte[] in = robToMakJedis.get((key+roomId).getBytes());
				userList = (List<RotToMakUser>) ListTranscoder.deserialize(in);
				userList.remove(0);
			} else {
				userList = new ArrayList<>();
			}
			if(userList.size()==0){
				robToMakJedis.del((key+roomId).getBytes());
			}else{
				robToMakJedis.set((key+roomId).getBytes(), ListTranscoder.serialize(userList));
			}
			return userList;
		} catch (Exception e) {
			userList = new ArrayList<>();
			//Log4jUtil.CommonLog.error("根据房间号删除该房间第一抢麦用户-removeFirstRotToMakUser"+e.getMessage());
			e.printStackTrace();
			return userList;
		} finally {
			robToMakJedis.close();
		}
	}
	
	/**
	 * 判断用户是否当前上麦者
	 * */
	public static boolean judgeUserIsFirstRotToMakUser(String roomId,User user){
		Jedis robToMakJedis = RedisPoolCommons.getJedisPool().getResource();
		List<RotToMakUser> userList = null;
		try {
			if (robToMakJedis.get((key+roomId).getBytes()) != null) {
				byte[] in = robToMakJedis.get((key+roomId).getBytes());
				userList = (List<RotToMakUser>) ListTranscoder.deserialize(in);
				if(ListUtils.isNotEmpty(userList)){
					RotToMakUser rotToMakUser = userList.get(0);
					if(rotToMakUser.getUserId().equals(user.getUserId())){
						return true;
					}else{
						return false;
					}
				}else{
					return false;
				}
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			robToMakJedis.close();
		}
	}
	
	/**
	 * 判断用户是否存在
	 * */
	public static boolean judgeUserIsExistRotToMakUserList(String roomId,String userId){
		Jedis robToMakJedis = RedisPoolCommons.getJedisPool().getResource();
		List<RotToMakUser> userList = null;
		try {
			if (robToMakJedis.get((key+roomId).getBytes()) != null) {
				byte[] in = robToMakJedis.get((key+roomId).getBytes());
				userList = (List<RotToMakUser>) ListTranscoder.deserialize(in);
				if(ListUtils.isNotEmpty(userList)){
					for (RotToMakUser rotToMakUser : userList) {
						if(rotToMakUser.getUserId().equals(userId)){
							return true;
						}
					}
					return false;
				}else{
					return false;
				}
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			robToMakJedis.close();
		}
	}

}
