package cn.fdse.filter;

import java.util.List;
import java.util.Properties;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

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
	 
	  StopWordAndStemm stm = new StopWordAndStemm("E:\\MFISSO\\StackOverflow Search Tool code\\MFISSO WEB\\WebRoot\\");
	  System.out.println(stm.getClearSentence(text));
  }
}
