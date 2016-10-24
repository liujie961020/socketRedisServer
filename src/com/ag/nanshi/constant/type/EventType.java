package com.ag.nanshi.constant.type;


public enum EventType {
	/**
	 * 事件
	 * inRotToMak:抢麦
	 * outRotToMak:�?出抢�?
	 */
	
	/**
	 * 
	 * */
	QUERYLIST("queryList","查询列表"),
	
	/**
	 * 抢麦
	 * */
	INROTTOMAK("inRotToMak","抢麦"),
	
	/**
	 * �?出抢�?
	 * */
	OUTROTTOMAK("outRotToMak","�?出抢�?"),
	
	/**
	 * �?出房�?
	 * */
	OUTROOM("outRoom","�?出房�?");
	
	private String code;
	private String name;
	private EventType(String code, String name){
		this.code = code;
		this.name = name;
	}

	/**
	 *  根据code获取对应的名�?
	 * @param Code
	 * @return
	 */
	public static String getName(Integer statusCode){
		for(EventType value : values()){
			if(value.getCode().equals( statusCode)){
				return value.getName();
			}
		}
		return null;
	}
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
