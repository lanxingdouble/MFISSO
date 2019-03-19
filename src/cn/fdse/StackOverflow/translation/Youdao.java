package cn.fdse.StackOverflow.translation;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import com.alibaba.fastjson.JSON;

 public class Youdao {

 
 	public static String translate(String text) throws Exception {
        StringBuffer sb = new StringBuffer();
 		 try { 
 			 text =  java.net.URLEncoder.encode(text,"utf-8");   
 			 String string =  "http://fanyi.youdao.com/openapi.do?keyfrom=huawei&key=1206476313&type=data&doctype=json&version=1.1&q="+text;
             URL url = new URL(string);

             URLConnection connection = url.openConnection();
             connection.connect();
             InputStream is = connection.getInputStream(); 

             InputStreamReader isr = new InputStreamReader(is, "UTF-8"); 

             BufferedReader br = new BufferedReader(isr);
             String line = null; 

             while ((line = br.readLine()) != null) {
                 sb.append(line); 
             } 
             br.close();

             isr.close(); 

             is.close(); 

         } catch (IOException e) { 

             e.printStackTrace(); 

         }
 		YoudaoResult result = JSON.parseObject(sb.toString(), YoudaoResult.class);
 		if(result.getErrorCode()==0)
 			return result.getTranslation()[0]; 
 		else
 			return "����ʧ��";
 	}
 	
 	public static void main(String args[])
 	{
 		String word = "�Ұ���";
 		try {
			System.out.println(Youdao.translate(word));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 	}
 }
