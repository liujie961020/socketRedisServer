package com.ag.nanshi.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;

public class HttpRequest {
    /**
     * 向指定URL发�?�GET方法的请�?
     * 
     * @param url
     *            发�?�请求的URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式�??
     * @return URL �?代表远程资源的响应结�?
     */
    public static String sendGet(String url, String param) {
        StringBuffer result = new StringBuffer();
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连�?
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属�?
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连�?
            connection.connect();
            // 定义 BufferedReader输入流来读取URL的响�?
            in = new BufferedReader(new InputStreamReader( connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
        	result = null;
        }
        // 使用finally块来关闭输入�?
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
            }
        }
        return result.toString();
    }

    /**
     * 向指�? URL 发�?�POST方法的请�?
     * 
     * @param url
     *            发�?�请求的 URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式�??
     * @return �?代表远程资源的响应结�?
     */
    public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连�?
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属�?
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发�?�POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.connect();
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发�?�请求参�?
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响�?
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发�?? POST 请求出现异常�?"+e);
        }
        //使用finally块来关闭输出流�?�输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }    
    
    public static void order(String uri, String str) throws IOException{
    	URL url = new URL(uri);
		URLConnection connection =   url.openConnection();
		connection.connect();
		new InputStreamReader(connection.getInputStream()); 
	}
    
    public static void main(String[] args) {
    	String s = sendGet("http://www.jinxiangchuan.com/weixin/userMachineV2Action_update.action","userMachine.machineId=gdjjb6259&userMachine.validateNum=2254&userMachine.wxId=gh_990216d097f3");
    	System.out.println(s);
	}
}