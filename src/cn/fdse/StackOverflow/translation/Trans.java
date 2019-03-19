package cn.fdse.StackOverflow.translation;
import java.io.Serializable;

import com.alibaba.fastjson.JSON;


public class Trans implements Serializable {
	private String src;
	private String dst;
	
	public Trans(){
		
	}

	public String getSrc() {
		return src;
	}

	public void setSrc(String src) {
		this.src = src;
	}

	public String getDst() {
		return dst;
	}

	public void setDst(String dst) {
		this.dst = dst;
	}
	
	public String toString() {
	    return JSON.toJSONString(this);
	}
}
