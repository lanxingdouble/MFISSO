package cn.fdse.filter;

import cn.fdse.StackOverflow.searchModule.util.Global;
import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.POS;
import edu.mit.jwi.morph.IStemmer;
import edu.mit.jwi.morph.WordnetStemmer;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

public class RemoveStopWordAndStemming {
	
	IDictionary dict ;
	IStemmer stemmer = null;
	WordnetStemmer stem = null;
	HashMap<String,String> wordStemmMap;
	FilterStopWord fsw;
	public RemoveStopWordAndStemming(String systemPath)
	{
		String wnhome = systemPath+ "WordNet/2.1/";
		String path = wnhome + File.separator + "dict";
		URL url = null;
		try {
			url = new URL("file", null, path);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // ����һ��URL����ָ��WordNet��ditcĿ¼

		// construct the dictionary object and open it
		dict = new Dictionary(url);
		dict.open(); // �򿪴ʵ�	
		stem =  new WordnetStemmer( dict );
		wordStemmMap = new HashMap<String,String>();
		fsw = new FilterStopWord(Global.root_path);

	}
	
	public String removeStopWordAndStemm(String sentense) {
		String result = "";
		List<String> stemmed_words;
		String temmWord = "";
	    String[] words = sentense.split(" ");
	    for(String word:words)
	    {
	    	word = fsw.removeStopWord(word);
	    	if(!word.equals(""))
	    	{
	    	 	if(wordStemmMap.containsKey(word))
		    	{
		    		result += wordStemmMap.get(word)+" ";
		    	}
		    	else
		    	{
		    	    stemmed_words = stem.findStems(word,POS.VERB);
		   	        if(!stemmed_words.isEmpty())
		   	        {
		   	        	temmWord = fsw.removeStopWord(stemmed_words.get(0));
		   	        	result += temmWord + " ";
		   	        	wordStemmMap.put(word, temmWord);
		   	        	continue;
		   	        }
//		   	        stemmed_words = stem.findStems(word,POS.NOUN);
//		   	        if(!stemmed_words.isEmpty())
//		   	        {
//		   	        	temmWord = stemmed_words.get(0);
//		   	        	result += temmWord + " ";
//		   	        	wordStemmMap.put(word, temmWord);
//		   	        	continue;
//		   	        }
//		   	       stemmed_words = stem.findStems(word,POS.ADVERB);
//		   	        if(!stemmed_words.isEmpty())
//		   	        {
//		   	        	temmWord = stemmed_words.get(0);
//		   	        	result += temmWord + " ";
//		   	        	wordStemmMap.put(word, temmWord);
//		   	        	continue;
//		   	        }
//		      	     stemmed_words = stem.findStems(word,POS.ADJECTIVE);
//			   	     if(!stemmed_words.isEmpty())
//			   	     {
//			   	        temmWord = stemmed_words.get(0);
//			   	        result += temmWord + " ";
//			   	        wordStemmMap.put(word, temmWord);
//			   	        continue;
//			   	      }
		   	          result += word +",";
		   	          wordStemmMap.put(word, word);
		    	}
	    	}
	   
	    }
	    return result;

	}
	
	public static void main(String args[]) {
//		RemoveStopWordAndStemming t = new RemoveStopWordAndStemming();
//		t.removeStopWordAndStemm("I am looking for the papers");


	}

}
