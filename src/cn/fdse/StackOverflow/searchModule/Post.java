package cn.fdse.StackOverflow.searchModule;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.jsoup.Jsoup;

import cn.fdse.codeSearch.openInterface.searchResult.CodeResult;

public class Post implements CodeResult,Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//common attributes
	private String concern;
	private String system;
	private String language;
	private String developT;
	private String framework;
	private String database;
	private String technology;
	
	public int postId;

	public String post_body;
	public String post_title;
	public String post_tag;
	public String post_body_text;
	public String post_body_code;
	
	private List<Answer> answer = new ArrayList<Answer>();

	
	public void setFacetName(String concern, String system, String language, String developT, String framework,String database,String technology)
	{
		this.concern = concern;
		this.system = system;
		this.language = language;
		this.database = database;
		this.developT = developT;
		this.framework = framework;
		this.technology = technology;
	}
	
	public Post(int postId,String post_title,String post_body,String post_tag)
	{
		this.postId = postId;
		this.post_title=post_title;
		this.post_body=post_body;
		this.post_tag=post_tag;
		splitBody(post_body);
	}
	public void splitBody(String post_body)
	{
		String tempBody = post_body;
		this.post_body_code="";
		this.post_body_text="";
		String codePrefix="<pre";
		String codePostfix="</code></pre>";
		while(tempBody.contains(codePrefix)&&tempBody.contains(codePostfix))
		{
			int prefixIndex = tempBody.indexOf(codePrefix);
			int postfixIndex= tempBody.indexOf(codePostfix)+codePostfix.length();
			try{
			   post_body_code += tempBody.substring(prefixIndex, postfixIndex);
			   tempBody = tempBody.substring(0,prefixIndex)+tempBody.substring(postfixIndex,tempBody.length());
			}catch(StringIndexOutOfBoundsException e)
			{
				break;
			}
		}
		codePrefix="<code";
		codePostfix="</code>";
		while(tempBody.contains(codePrefix)&&tempBody.contains(codePostfix))
		{
			int prefixIndex = tempBody.indexOf(codePrefix);
			int postfixIndex= tempBody.indexOf(codePostfix)+codePostfix.length();
			try{
			   post_body_code += tempBody.substring(prefixIndex, postfixIndex);
			   tempBody = tempBody.substring(0,prefixIndex)+tempBody.substring(postfixIndex,tempBody.length());
			}catch(StringIndexOutOfBoundsException e)
			{
				break;
			}
		}		
		post_body_text = tempBody;
	

	}
	public String getText(String text)
	{
		return Jsoup.parse(text).body().text();
	}
	
	public String getPost_body() {
		return this.post_body;
	}

	public String getPost_title() {
		return post_title;
	}

	public String getPost_tag() {
	
		return post_tag.replace("<", " ").replace(">", " ");
	}
	
	public void setPost_tag(String post_tag) {
		this.post_tag = post_tag;
	}

	public int getPostId() {
		return postId;
	}

	public void setPostId(int postId) {
		this.postId = postId;
	}

	public String getDatabase() {
		return database;
	}

	
	public String getSystem()
	{
		return this.system;
	}

	
	public String getLanguage()
	{
		return this.language;
	}
	
	public void setAnaser(List<Answer> answerList)
	{
		int size = answerList.size();
		for(int i = 0; i < size; i++)
		{
			
			this.answer.add(answerList.get(i));
		}
	}

	@Override
	public String getBody() {
		// TODO Auto-generated method stub
		return post_body;
	}



	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return this.postId+"";
	}

	@Override
	public String getTag() {
		// TODO Auto-generated method stub
		return this.post_tag;
	}

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return this.post_title;
	}

	@Override
	public String getBody_code() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getBody_text() {
		// TODO Auto-generated method stub
		return getText(post_body_text);
	}

	@Override
	public String getComponent() {
		// TODO Auto-generated method stub
		return this.framework;
	}

	@Override
	public String getDevelopment() {
		// TODO Auto-generated method stub
		return this.developT;
	}

	@Override
	public String getFocus() {
		// TODO Auto-generated method stub
		return this.concern;
	}

	@Override
	public String getTechnology() {
		// TODO Auto-generated method stub
		return this.technology;
	}

	@Override
	public List<Answer> getAnswerList() {
		// TODO Auto-generated method stub
		return answer;
	}

}