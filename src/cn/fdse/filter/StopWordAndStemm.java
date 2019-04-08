package cn.fdse.filter;

import cn.fdse.StackOverflow.searchModule.util.Global;

public class StopWordAndStemm {


	FilterStopWord fsw;
	public StopWordAndStemm(String systemPath)
	{

	    fsw = new FilterStopWord(systemPath);
	}
	
	public String getClearSentence(String text)
	{
	  text = text.replace("<", "").replace(">", "");
	   String[] words = text.split(" ");
	   String sen = "";
	   for(String w:words)
	   {
		   String word = fsw.removeStopWord(w);
			sen += word +" ";
		}
		return sen;
	}

  public static void main(String args[])
  {
	  String text = "mainly finding good documentation ";
	 
	  StopWordAndStemm stm = new StopWordAndStemm(Global.root_path);
	  System.out.println(stm.getClearSentence(text));
  }
}
