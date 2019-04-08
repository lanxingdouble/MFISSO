package cn.fdse.filter;

import cn.fdse.StackOverflow.searchModule.util.Global;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

public class FilterStopWord
{
	int stopWordLength = 100;//Ϊ����queryʱ������һЩ�����ͣ�ô�
	Set stopWordSet;
	String lowCase;
	public FilterStopWord(String path)
	{
		stopWordSet = new HashSet<String>();
		try {
			BufferedReader stopWordFileBr = new BufferedReader(new InputStreamReader(new FileInputStream(new File(path+"stopWord.txt"))));
			String stopWord = null;
			for(;(stopWord = stopWordFileBr.readLine()) != null;)
			{
				stopWordSet.add(stopWord);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void setStopWordLength(int length)
	{
		this.stopWordLength = length;
	}
	
	public String getStringWithoutStopWord(String s)
	{
		String result = "";
		String[] resultArray = s.split(" ");
		for(String ra:resultArray)
		{
			ra= ra.replace("[","").replace("]","").replace("?","").replace("(","").replace(")","").replace("{","").replace("}","");
			if(!stopWordSet.contains(ra.toLowerCase())) {
				result += ra + ",";
			}
		}
		System.out.println(result);
		return result;
	
	}
	

	
	public String removeStopWord(String s)
	{

		lowCase = s.toLowerCase();
	   if(!stopWordSet.contains(lowCase))
		   return s;
	   else
		   return "";

	}

	public static void main(String args[])
	{
		FilterStopWord fsw = new FilterStopWord(Global.root_path);
		System.out.println(fsw.getStringWithoutStopWord("I like to eat apple Can"));
	}
}
