package cn.fdse.codeSearch.openInterface.module;

import java.util.List;
import java.util.Map;

import cn.fdse.codeSearch.openInterface.searchResult.CodeResult;

public interface ModuleProvider {
	public ClassificationList analysis(List<CodeResult> codes, Map<String, Object> dataMap);
	public ClassificationList refine(List<CodeResult> codes, Map<String, Object> dataMap);
	public boolean needSpecialRefine();
}