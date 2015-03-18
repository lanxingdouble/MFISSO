package cn.fdse.StackOverflow.searchModule;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;

import cn.fdse.codeSearch.openInterface.searchResult.CodeResult;

public class Post implements CodeResult{
	//common attributes
	private String focus;
	private String system;
	private String language;
	private String developE;
	private String component;
	private String technology;
	private String database;
	
	public int postId;

	public String post_body;
	public String post_body_text;
	public String post_body_code;
	public String post_title;
	public String post_tag;
	
	public float grade = 0;
	
	/*public int post_comment_count;
	public List<Comment> commentList;
	
	//answer related
	public int parentId;
	public Question parent_question;
	//question related 
	public int post_answer_count;
	public List<Answer> AnswerList;	
	public int accepted_answerId;
	public Answer accpted_answer;*/
	
	public void setFacetName(String focus, String system, String language, String developE, String component, String technology, String database)
	{
		this.focus = focus;
		this.system = system;
		this.language = language;
		this.database = database;
		this.developE = developE;
		this.technology = technology;
		this.component = component;
	}
	
	public Post(int postId,String post_title,String post_body,String post_tag,int post_comment_count,
			int parentId,int post_answer_count,int accepted_answerId)
	{
		this.postId = postId;
		this.post_title=post_title;
		this.post_body=post_body;
		this.post_tag=post_tag;
	}
	
	public void setGrade(float gade)
	{
		this.grade = grade;
	}
	
	public float getGrade()
	{
		return grade;
	}
	
	public String getText(String text)
	{
		return Jsoup.parse(text).body().text();
	}
	
	public String getPost_body() {
		return this.post_body;
	}

	public String getPost_body_text() {
		return Jsoup.parse(post_body_text).body().text();
	}

	public String getPost_body_code() {
		return post_body_code;
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

	
	public void setFocus(String focus)
	{
		this.focus = focus;
	}
	
	public String getFocus()
	{
		return this.focus;
	}
	public String getDevelopment() {
		return developE;
	}

	public void getDevelopment(String develop) {
		this.developE = develop;
	}

	public String getComponent() {
		return component;
	}

	public void setComponent(String component) {
		this.component = component;
	}

	public String getDatabase() {
		return database;
	}

	public void setDatabase(String database) {
		this.database = database;
	}

	public String getTechnology() {
		return technology;
	}

	public void setTechnology(String technology) {
		this.technology = technology;
	}

	public void setSystem(String system)
	{
		this.system = system;
	}
	
	public String getSystem()
	{
		return this.system;
	}
	public void setLanguage(String language)
	{
		this.language = language;
	}
	
	public String getLanguage()
	{
		return this.language;
	}

	@Override
	public String getBody() {
		// TODO Auto-generated method stub
		return this.post_body;
	}

	@Override
	public String getBody_code() {
		// TODO Auto-generated method stub
		return this.post_body_code;
	}

	@Override
	public String getBody_text() {
		// TODO Auto-generated method stub
		return this.post_body_text;
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

}