package com.ag.nanshi.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Redis公用类
 * */
public class RedisPoolCommons {
	
	private static JedisPool jedisPool=null;
	
	public RedisPoolCommons(){}
	
	/**
	 * 获取Jedis实例
	 * */
	public static synchronized JedisPool getJedisPool(){
		if(jedisPool==null){
			JedisPoolConfig jedisPoolConfig=new JedisPoolConfig();
			//设置最大空闲连接数
			jedisPoolConfig.setMaxIdle(10);
			//最大连接数
			jedisPoolConfig.setMaxTotal(100);
			//设置等待连接时间 小于0不确定时间
			jedisPoolConfig.setMaxWaitMillis(5000);
			jedisPool=new JedisPool(jedisPoolConfig,"101.201.104.101",6379,5000);
		}
		return jedisPool;
	}
	
	public static void main(String[] args) {
		jedisPool=RedisPoolCommons.getJedisPool();
		Jedis jedis=jedisPool.getResource();
		jedis.set("name", "liujie");
		System.out.println(jedis.get("name"));
	}

}
