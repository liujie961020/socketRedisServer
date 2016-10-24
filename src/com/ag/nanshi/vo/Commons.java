package com.ag.nanshi.vo;

import com.ag.nanshi.utils.DateFormatEnum;
import com.ag.nanshi.utils.DateUtil;

/**
 * 通用类
 * */
public class Commons {
	
	/**
	 * 默认状态
	 * */
	public static Integer defaultStatus=1;
	
	/**
	 * 默认版本号
	 * */
	public static Integer defaultVesionNo=1;
	
	/**
	 * 默认账户余额
	 * */
	public static Float defaultMoney=0.00f;
	
	/**
	 * 默认头像地址
	 * */
	public static String imgUrl = "http://101.201.104.101:8088/nanshi/image/";
	
	/**
	 * 默认每页显示数据
	 * */
	public static Integer defaultPageSize = 10000;
	
	/**
	 * 默认直播时间
	 * */
	public static double defaultPlayTime = 0;
	
	/**
	 * 获取当前时间
	 * */
	public static String getNowTime(){
		return DateUtil.getDate(DateFormatEnum.yyyy_MM_dd_HH_mm_ss);
	}

}
