package cn.fdse.StackOverflow.componentFacet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import cn.fdse.codeSearch.openInterface.module.Classification;
import cn.fdse.codeSearch.openInterface.module.ClassificationList;
import cn.fdse.codeSearch.openInterface.module.ModuleProvider;
import cn.fdse.codeSearch.openInterface.searchResult.CodeResult;

public class ComponentFacet implements ModuleProvider {

	@Override
	public ClassificationList analysis(List<CodeResult> postList,
			Map<String, Object> dataMap) {
		// TODO Auto-generated method stub	
		HashMap<String,List<CodeResult>> facetMap = new HashMap<String,List<CodeResult>>();
		
		for(CodeResult post:postList)
		{
			String componentSet = post.getComponent();
			if(componentSet == null)
				continue;
			String[] component = componentSet.split(",");
			for(String com:component)
			{
				int index = com.lastIndexOf("-");
				if(index!=-1)
					com = com.substring(index+1,com.length());
				if(facetMap.containsKey(com))
				{
					facetMap.get(com).add(post);				
				}
				else
				{
					List<CodeResult> list = new ArrayList<CodeResult>();
					list.add(post);
					facetMap.put(com, list);
				}
			}
		}
		
		focusFacet COMPONENT = new focusFacet("Component");
		Iterator iter = facetMap.entrySet().iterator();
		while (iter.hasNext()) {
		   Map.Entry entry = (Map.Entry) iter.next();
		   FocusItem item = new FocusItem((String) entry.getKey(),(List<CodeResult>)entry.getValue());
		   COMPONENT.classifications.add(item);
		}
		
		return COMPONENT;
	}

	@Override
	public boolean needSpecialRefine() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ClassificationList refine(List<CodeResult> arg0,
			Map<String, Object> arg1) {
		// TODO Auto-generated method stub
		return null;
	}
	
	class focusFacet implements ClassificationList {

		List<Classification> classifications = new ArrayList<Classification>();
		private String title = null;
		private String id = null;
		
		public focusFacet(String title){
			this.title = title;
		}
		
		@Override
		public List<Classification> getClassification() {
			// TODO Auto-generated method stub
			return classifications;
		}
		
		public void setClassification(List<FocusItem> items){
			classifications = new ArrayList<Classification>();
			for(FocusItem item:items){
				classifications.add(item);
			}
		}

		@Override
		public String getId() {
			// TODO Auto-generated method stub
			return id;
		}

		public void setId(int id){
			this.id = id+"";
		}

		@Override
		public String getTitle() {
			// TODO Auto-generated method stub
			return title;
		}

		public void setTitle(String t){
			title = t;
		}

	}
	class FocusItem implements Classification {

		public List<CodeResult> focusItem =new ArrayList<CodeResult>();
		private String description = null;
		
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
			return description;
		}
		
	}
}
