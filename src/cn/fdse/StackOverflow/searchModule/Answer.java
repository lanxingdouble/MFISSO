package cn.fdse.StackOverflow.searchModule;

import java.io.Serializable;

public class Answer implements Serializable{
	private static final long serialVersionUID = 1L;

	private int score;
	private String body;
	public Answer(int score,String body)
	{
		this.score = score;
		this.body = body;
	}
	
	public int getScore()
	{
		return this.score;
	}
	
	public String getBody()
	{
		return this.body;
	}
	
	public void setScore(int score)
	{
		this.score = score;
	}
	
	public void setBody(String body)
	{
		this.body = body;
	}

}
