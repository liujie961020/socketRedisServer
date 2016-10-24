package com.ag.nanshi.utils;


/**
 * 
 * Description : æ—¶é—´æ ¼å¼   
 * <pre>
 * +--------------------------------------------------------------------
 * æ›´æ”¹å†å²
 * æ›´æ”¹æ—¶é—´		 æ›´æ”¹äº?		ç›®æ ‡ç‰ˆæœ¬		æ›´æ”¹å†…å®¹
 * +--------------------------------------------------------------------
 * 2012-11-3       Snail Join 		1.00	 	åˆ›å»º
 *           		 	 	                               
 * </pre>
 * @author çŸ«è¿©(Snail Join) <a href="mailto:13439185754@163.com">
 *         E-mail:13697654@qq.com </a><a href="tencent://message/?uin=13697654">
 *         QQ:13697654</a>
 */
public enum DateFormatEnum {
	

	// å¹?-æœ?-æ—? æ—?:åˆ?:ç§?
	// å¹?-æœ?-æ—? æ—?:åˆ?
	// å¹?-æœ?-æ—?
	// æ—?:åˆ?:ç§?
	// æ—?:åˆ?
	
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
			return "yyyyå¹´MMæœˆddæ—?";
		}
	},
}
