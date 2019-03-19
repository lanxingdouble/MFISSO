package cn.fdse.codeSearch.openInterface.searchResult;

import java.util.List;
import java.util.Map;

import cn.fdse.codeSearch.openInterface.searchInput.UserInput;

public interface SearchProvider {
	public List<CodeResult> getResultOf(UserInput ui, Map<String, Object> dataMap);
}
