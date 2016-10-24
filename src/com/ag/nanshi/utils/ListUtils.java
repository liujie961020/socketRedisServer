package com.ag.nanshi.utils;

import java.util.List;

public class ListUtils  extends org.apache.commons.collections.ListUtils {

	/**
	 * @description 【判断集合是否为空,如果每个元素都为空，就认为集合为空】
	 * @param list
	 * @return
	 * @author lx
	 */
	public static boolean isBlank(List<?> list){
		boolean isEmpty = true;
		if (!isEmpty(list)){
			isEmpty = false;
		}
		else{
			for(Object temp : list){
				if(temp != null){
					isEmpty = false;
					break;
				}
			}
		}
		return isEmpty;
	}
	/**
	 * @description 【判断集合是否为空】
	 * @return
	 * @author lx
	 */
	public static boolean isEmpty(List<?> list){
		boolean isEmpty = false;
		if (null == list || list.isEmpty()) {
			isEmpty = true;
		}
		return isEmpty;
	}
	
	/**
	 * @Description:集合不为空返回为真。
	 * @Author 王卫星
	 * @param list
	 * @return
	 * 时间：2015年12月21日下午2:48:41
	 */
	public static boolean isNotEmpty(List<?> list){
		return !isEmpty(list);
	}
}
