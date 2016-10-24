package com.ag.nanshi.utils;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;


/**
 * 
 * Description : JSON常用�?   
 * <pre>
 * +--------------------------------------------------------------------
 * 更改历史
 * 更改时间		 更改�?		目标版本		更改内容
 * +--------------------------------------------------------------------
 * 2012-12-12       Snail Join 		1.00	 	创建
 * 
 */
public class JsonUtil {
	
	private static final Gson GSON = new Gson();
	
	/**
	 * 
	 * @Title: toObject
	 * @Description: 将json串转换为�?单对�?
	 * @param @param <T>
	 * @param @param json
	 * @param @param clazz
	 * @return T
	 * @throws
	 */
	public static <T> T toObject(String json, Class<T> clazz) {
		return GSON.fromJson(json, clazz);
	}

	/**
	 * 
	 * @Title: toJson
	 * @Description: 将对象转换为json�?
	 * @param @param obj
	 * @param @return
	 * @return String
	 * @throws
	 */
	public static String toJson(Object obj) {
		return GSON.toJson(obj);
	}
	
	
	public static void main(String[] args) {
		TestJson test = new TestJson();
		test.setName("dsf");
		List<String> list = new ArrayList<String>();
		list.add("dff");
		list.add("fdf");
		list.add("asd");
		list.add("yyu");
		test.setMachineIds(list);
		System.out.println(toJson(test));
		TestJson t = toObject(toJson(test), TestJson.class);
		System.out.println(t.getName());
	}
	
}


