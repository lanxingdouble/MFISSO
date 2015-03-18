package cn.fdse.StackOverflow.searchModule;

import java.util.List;
import java.util.Map;


import cn.fdse.StackOverflow.searchModule.util.FilterStopWord;
import cn.fdse.StackOverflow.searchModule.util.PostDAOImpl;
import cn.fdse.codeSearch.openInterface.searchInput.UserInput;
import cn.fdse.codeSearch.openInterface.searchResult.CodeResult;
import cn.fdse.codeSearch.openInterface.searchResult.SearchProvider;

public class SerarchResult implements SearchProvider{
	private static Map<String, Object> configMap = null;
	protected String keywords = null;
	protected List<CodeResult> codeResultList = null;
	@Override
	public  List<CodeResult> getResultOf(UserInput ui, Map<String, Object> dataMap){
		configMap = dataMap; 	
		keywords = ui.getKeyWords();
		
		PostDAOImpl pdi = new PostDAOImpl();
		FilterStopWord fsw = new FilterStopWord();
		List<CodeResult> postList = pdi.findPosts(fsw.getStringWithoutStopWord(keywords));	    	
		return postList;
	}

}
