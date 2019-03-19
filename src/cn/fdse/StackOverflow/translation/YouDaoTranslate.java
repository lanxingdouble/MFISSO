package cn.fdse.StackOverflow.translation;





import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.*;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONObject;

 public class YouDaoTranslate {

 
 	public static String translate(String text) throws Exception {
        StringBuffer sb = new StringBuffer();//用于接收返回的数据 

 		 try { 

 			 String string =  "http://fanyi.youdao.com/openapi.do?keyfrom=huawei&key=1206476313&type=data&doctype=json&version=1.1&q="+text;
             URL url = new URL(string);//此处访问有道的webService服务，参数都在url中 

             URLConnection connection = url.openConnection();//得到一个连接对象 

             InputStream is = connection.getInputStream(); 

             InputStreamReader isr = new InputStreamReader(is, "UTF-8"); 

             BufferedReader br = new BufferedReader(isr);//用于读取返回的数据流 
             String line = null; 

             while ((line = br.readLine()) != null) { 
                 sb.append(line); 

             } 


             br.close();//关闭各种连接 

             isr.close(); 

             is.close(); 

         } catch (IOException e) { 

             e.printStackTrace(); 

         } 
         return sb.toString(); 

 	}
 	
 	public static void main(String args[])
 	{
 		String word = "我爱你";
 		try {
			System.out.println(YouDaoTranslate.translate(word));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 	}
 }