package cn.fdse.StackOverflow.translation;

import java.io.Serializable;
import com.alibaba.fastjson.JSON;


public class YoudaoResult implements Serializable {
	private String translation[];
	private String query;
	private int errorCode;
	
	public YoudaoResult(){
		
	}
	
	public String[] getTranslation() {
		return translation;
	}

	public void setTranslation(String translation[]) {
		this.translation = translation;
	}


	public String getQuery() {
		return query;
	}


	public void setQuery(String query) {
		this.query = query;
	}


	public int getErrorCode() {
		return errorCode;
	}


	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public String toString() {
	    return JSON.toJSONString(this);
	}
}