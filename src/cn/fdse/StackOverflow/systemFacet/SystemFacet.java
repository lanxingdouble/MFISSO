package cn.fdse.StackOverflow.systemFacet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import cn.fdse.codeSearch.openInterface.module.Classification;
import cn.fdse.codeSearch.openInterface.module.ClassificationList;
import cn.fdse.codeSearch.openInterface.module.ModuleProvider;
import cn.fdse.codeSearch.openInterface.searchResult.CodeResult;

public class SystemFacet implements ModuleProvider {

	@Override
	public ClassificationList analysis(List<CodeResult> postList,
			Map<String, Object> dataMap) {
		// TODO Auto-generated method stub	
		HashMap<String,List<CodeResult>> facetMap = new HashMap<String,List<CodeResult>>();
		
		for(CodeResult post:postList)
		{
			String systemSet = post.getSystem();
			if(systemSet == null)
				continue;
			String[] system = systemSet.split(",");
			for(String sys:system)
			{
				int index = sys.lastIndexOf("-");
				if(index!=-1)
					sys = sys.substring(index+1,sys.length());
				if(facetMap.containsKey(sys))
				{
					facetMap.get(sys).add(post);				
				}
				else
				{
					List<CodeResult> list = new ArrayList<CodeResult>();
					list.add(post);
					facetMap.put(sys, list);
				}
			}
		}
		
		focusFacet SYSTEM = new focusFacet("System");
		Iterator iter = facetMap.entrySet().iterator();
		while (iter.hasNext()) {
		   Map.Entry entry = (Map.Entry) iter.next();
		   FocusItem item = new FocusItem((String) entry.getKey(),(List<CodeResult>)entry.getValue());
		   SYSTEM.classifications.add(item);
		}
		
		return SYSTEM;
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
