package cn.fdse.StackOverflow.searchModule.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Highlighter;

import cn.fdse.StackOverflow.searchModule.Answer;
import cn.fdse.StackOverflow.searchModule.Post;
import cn.fdse.codeSearch.openInterface.searchResult.CodeResult;

public class searchAnswerThread implements Runnable{
	
	 List<Post> postList;
	 IndexSearcher searcherPost = null;
	 List<Answer> goodAnswerList = new ArrayList<Answer>();
	 int size,count;
	 String body,tempBody,splitBody,highterBody;
	 Highlighter highlighter;
	 Analyzer analyzer;

	
	public searchAnswerThread(List<Post> tempPostList, IndexSearcher searcherPost,Highlighter highlighter,Analyzer analyzer)
	{
		postList = tempPostList;
		this.searcherPost = searcherPost;
		this.analyzer = analyzer;
		this.highlighter = highlighter;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		for(Post p:postList)
		{
		   p.setAnaser(sortAnswer(findPostRelatedAnswers(p.getPostId(),searcherPost)));
		}
		
	}
	
	public List<Post> getPost()
	{
		return postList;
	}
	
	public List<Answer> findPostRelatedAnswers(int postId,IndexSearcher searcherPost) 
	{
		    List<Answer> answerList = new ArrayList<Answer>();
		    int score;
		    String body;
	    	TopDocs postFacetDoc;
	 
			try {
				postFacetDoc = searcherPost.search(NumericRangeQuery.newIntRange("ParentId", postId, postId, true, true),20);

				for(ScoreDoc d:postFacetDoc.scoreDocs)
			    {

		    		Document document =searcherPost.doc(d.doc);
		    		score = Integer.parseInt(document.get("Score"));
		    		if(score<0)
		    			continue;
		    		body = document.get("Body");
		    		Answer answer = new Answer(score,body);
		    		answerList.add(answer);
			    }
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			return answerList;
	}
	public List<Answer> sortAnswer(List<Answer> answerList)
	{
		goodAnswerList.clear();
		Collections.sort(answerList, new Comparator<Answer>() {  
	         public int compare(Answer o1, Answer o2) {  
		              int result = o2.getScore() - o1.getScore();   
		              return result;  
		          }  
	       }); 
	   size = answerList.size();
    	if(size >=3)
		size = 3;
	 for(int j = 0; j < size; j++)
	 {
		Answer answer = answerList.get(j);
		body = answer.getBody();
		tempBody="";
		try{
			count = body.length()/100;			    	
	    	for(int i = 0;i < count; i++)
	    	{
	    		splitBody= body.substring(i*100,(i+1)*100);

	    		highterBody = highlighter.getBestFragment(analyzer.tokenStream("token", splitBody),splitBody);
	    		if(highterBody==null)
	    			tempBody += splitBody;
	    		else
	    			tempBody+=highterBody;
	    	}
	    	splitBody = body.substring(count*100,body.length());
	    	highterBody = highlighter.getBestFragment(analyzer.tokenStream("token", splitBody),splitBody);
	    	if(highterBody==null)
    			tempBody += splitBody;
    		else
    			tempBody+=highterBody;
	    	body = tempBody;
	    	answer.setBody(body);
		}catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace(); 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		goodAnswerList.add(answer);
	 }
		return goodAnswerList;
	}
	

}
