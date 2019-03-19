package cn.fdse.StackOverflow.translation;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;

import com.alibaba.fastjson.JSON;


public class BaiDuTranslator {
	public static void main(String args[]){
		BaiDuTranslator trans = new BaiDuTranslator();
		System.out.println(trans.getResult("我的mysql"));
		
	}
	/**
	 * @param urlAll
	 *            :����ӿ�
	 * @param httpArg
	 *            :����
	 * @return ���ؽ��
	 */
	
	public String getResult(String input)
	{
		if(ChineseAndEnglish.isChinese(input))
		{
			String httpUrl = "http://openapi.baidu.com/public/2.0/bmt/translate";
			String httpArg = "client_id=c7SSKybUK0mNWX8i0aleRrDG&q="+input+"&from=auto&to=en";
			String jsonResult = request(httpUrl, httpArg);
			Result result = JSON.parseObject(jsonResult, Result.class);
			return result.getTrans_result().get(0).getDst();
		}else
			return input;
		
	}
	public String request(String httpUrl, String httpArg) {
	    BufferedReader reader = null;
	    String result = null;
	    StringBuffer sbf = new StringBuffer();
	    httpUrl = httpUrl + "?" + httpArg;

	    try {
	        URL url = new URL(httpUrl);
	        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	        connection.setRequestMethod("GET");
	        connection.connect();
	        InputStream is = connection.getInputStream();
	        reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
	        String strRead = null;
	        while ((strRead = reader.readLine()) != null) {
	            sbf.append(strRead);
	            //sbf.append("\r\n");
	        }
	        reader.close();
	        result = sbf.toString();
	    } catch (Exception e) {
	    	return "";
//	        e.printStackTrace();
	    }
	    return result;
	}
}
