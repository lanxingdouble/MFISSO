package cn.fdse.MultiFacetWebService.framework;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.fdse.codeSearch.openInterface.module.ClassificationList;
import cn.fdse.codeSearch.openInterface.module.ModuleProvider;
import cn.fdse.codeSearch.openInterface.searchResult.CodeResult;

public class FrameSession {

	private List<ModuleProvider> modules = new ArrayList<ModuleProvider>();
	FrameResult fr = null;
	Map<String, Object> dataMap = 
			new HashMap<String, Object>();
	
	void addModuleProvider(ModuleProvider mp){
		modules.add(mp);
	}
	
	public void refine(List<CodeResult> refinedList){
		for(CodeResult cr:refinedList){
			if(cr==null)
				System.out.println("null in preprocess");
		}
		List<ClassificationList> ret = new ArrayList<ClassificationList>();
		for(ModuleProvider p:modules){
			if(p.needSpecialRefine()){
				ret.add(p.refine(refinedList, dataMap));
			}else{
				
				ret.add(p.analysis(refinedList, dataMap));
			}
		}
		fr = new FrameResult();
		fr.facets = ret;
		fr.results = refinedList;
	}
	
	public void refine(String listData){
//		System.out.println(listData);
		if(resultMap == null) buildResultMap();
		List<String> list = new ArrayList<String>();
		List<CodeResult> refinedSet = new ArrayList<CodeResult>();
//		Set<CodeResult> refinedSet = new HashSet<CodeResult>();
		String[] ids = listData.split(";");
		for(String id:ids){
			if(id.length()==0||list.contains(id))
				continue;
		    list.add(id);
			CodeResult cr = resultMap.get(id);
			if(cr==null){
				System.out.println("null::"+id);
			}else
			refinedSet.add(cr);
		}
		List<CodeResult> refinedList = new ArrayList<CodeResult>();
		refinedList.addAll(refinedSet);
		refine(refinedList);
	}
	
	private void buildResultMap() {
		resultMap = new HashMap<String, CodeResult>();
		for(CodeResult cr:allResult){
			resultMap.put(cr.getId(), cr);
		}
	}

	public FrameResult getFrameResult(){
		return fr;
	}

	private Map<String, CodeResult> resultMap = null;
	private List<CodeResult> allResult = null;
	
	void setAllResult(List<CodeResult> resultList) {
		allResult = resultList;
	}

}
