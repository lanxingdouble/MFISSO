package cn.fdse.codeSearch.openInterface.module;

import java.util.List;
import cn.fdse.codeSearch.openInterface.searchResult.CodeResult;

public interface Classification {
	
	public List<CodeResult> getCodeSegments();
	public String getDescription();

}
