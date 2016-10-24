package com.ag.nanshi.server;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class CrazyitMap<K, V> extends HashMap<K, V> {
	private static final long serialVersionUID = 1L;

	//根据value 来删除指定项
	public void removeByValue(Object value){
		for(Object key: keySet()){
			if(get(key) == value){
				remove(key);
				break;
			}
		}
	}
	
	//获取�?有value组成的set集合
	public Set<V> valueSet(){
		Set<V> result = new HashSet<V>();
		//遍历�?有key组成的集�?
		for (K key: keySet()){
			//将每个key对应value 添加到result集合�?
			result.add(get(key));
		}
		return result;
	}
	
	//根据value查找key
	public K getKeyByValue(V val){
		//遍历�?有key组成的集�?
		for (K key: keySet()){
			if(get(key).equals(val) && get(key) == val){
				return key;
			}
		}
		return null;
	}

	//重写HashMap 的put方法，该方法不允许value 重复
	public  V put(K key, V value){
		//遍历�?有value组成的集�?
		for (V val: valueSet()){
			if(val.equals(value) && val.hashCode() == value.hashCode()){
				throw new RuntimeException("MyMap 实例中不允许有重�? value�?");
			}
		}
		return super.put(key, value);
	}
	
}
