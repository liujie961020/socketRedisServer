package com.ag.nanshi.utils;


/**
 * 
 * Description : 时间格式   
 * <pre>
 * +--------------------------------------------------------------------
 * 更改历史
 * 更改时间		 更改�?		目标版本		更改内容
 * +--------------------------------------------------------------------
 * 2012-11-3       Snail Join 		1.00	 	创建
 *           		 	 	                               
 * </pre>
 * @author 矫迩(Snail Join) <a href="mailto:13439185754@163.com">
 *         E-mail:13697654@qq.com </a><a href="tencent://message/?uin=13697654">
 *         QQ:13697654</a>
 */
public enum DateFormatEnum {
	

	// �?-�?-�? �?:�?:�?
	// �?-�?-�? �?:�?
	// �?-�?-�?
	// �?:�?:�?
	// �?:�?
	
	ID {
		public String toString(){
			return "yyyy-MM-dd-HH-mm-ss";
		}
	},
	
	yyyy_MM_dd_HH_mm_ss {
		public String toString(){
			return "yyyy-MM-dd HH:mm:ss";
		}
	},
	
	yyyy_MM_dd_HH_mm {
		public String toString(){
			return "yyyy-MM-dd HH:mm";
		}
	},

	yyyy_MM_dd {
		public String toString(){
			return "yyyy-MM-dd";
		}
	},
	
	HH_mm_ss {
		public String toString(){
			return "HH:mm:ss";
		}
	},
	
	HH_mm {
		public String toString(){
			return "HH:mm";
		}
	},
	yyyyMMdd {
		public String toString(){
			return "yyyy/MM/dd";
		}
	},
	yyyyZHMMZHddZH {
		public String toString(){
			return "yyyy年MM月dd�?";
		}
	},
}
