package cn.fdse.StackOverflow.topicFacet;

import java.util.ArrayList;
import java.util.List;

import cn.fdse.codeSearch.openInterface.module.Classification;
import cn.fdse.codeSearch.openInterface.searchResult.CodeResult;

public class FocusItem implements Classification {

	public List<CodeResult> focusItem =new ArrayList<CodeResult>();
	public String description = null;
	
	public FocusItem(String description,List<CodeResult> focusItem){
		this.description = description;
		this.focusItem = focusItem;
	}
	@Override
	public List<CodeResult> getCodeSegments() {
		// TODO Auto-generated method stub
		return focusItem;
	}
	 
	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		//System.out.println("---------:"+description);
		return description;
	}
	
}

