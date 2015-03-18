

package cn.fdse.StackOverflow.searchModule.util;

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
	
	public void setStopWordLength(int length)
	{
		this.stopWordLength = length;
	}
	
	public String getStringWithoutStopWord(String s)
	{
		String result = "";
		String[] resultArray = s.split(" ");
		Set stopWordSet = new HashSet<String>();
		try {
			BufferedReader stopWordFileBr = new BufferedReader(new InputStreamReader(new FileInputStream(new File("C:\\Users\\jqt\\Desktop\\Others\\stackoverflow\\ExtendedMFIE\\ExtendedMFIE\\stopWord.txt"))));
			String stopWord = null;
			for(;(stopWord = stopWordFileBr.readLine()) != null;)
			{
				stopWordSet.add(stopWord);
			}
			
			for(int i = 0;i < resultArray.length; i++)
			{
				if(!stopWordSet.contains(resultArray[i]))
					result += resultArray[i] + ",";
				else if(resultArray[i].length() >= stopWordLength)
					result += resultArray[i] + ",";
			}
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(result.equals(""))
		{
			String[] keword = s.split(" ");
			for(String k:keword)
				result += k +",";
			return result.substring(0,result.length()-1);
		}
		return result.substring(0,result.length()-1);
	
	}
	
	
	public static void main(String args[])
	{
		FilterStopWord fsw = new FilterStopWord();
		System.out.println(fsw.getStringWithoutStopWord("How do you get it"));
	}
}
