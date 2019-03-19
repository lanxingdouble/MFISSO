package cn.fdse.StackOverflow.translation;
import java.io.Serializable;
import java.util.List;

import com.alibaba.fastjson.JSON;


public class Result implements Serializable {
	private String to;
	private String from;
	private List<Trans> trans_result;
	
	public Result(){
		
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}
	
	public List<Trans> getTrans_result() {
		return trans_result;
	}

	public void setTrans_result(List<Trans> trans_result) {
		this.trans_result = trans_result;
	}

	public String toString() {
	    return JSON.toJSONString(this);
	}
}
