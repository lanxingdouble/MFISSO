package cn.fdse.MultiFacetWebService.framework;

import java.util.HashMap;
import java.util.Map;

import cn.fdse.codeSearch.openInterface.searchInput.UserInput;

public class FrameUserInput implements UserInput{

	private String input = null;
	private Map<String, String> map = 
			new HashMap<String, String>();
	
	public FrameUserInput(String input){
		this.input = input;
	}
	
	@Override
	public String getKeyWords() {
		return input;
	}

	@Override
	public String getProperties(String p) {
		return map.get(p);
	}

	public void addProperty(String name, String property){
		map.put(name, property);
	}
}
