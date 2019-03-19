package cn.fdse.codeSearch.openInterface.searchResult;
import java.util.List;

import cn.fdse.StackOverflow.searchModule.Answer;

public interface CodeResult {
	public String getId();
	public String getTitle();
	public String getBody();
	public String getBody_text();
	public String getBody_code();
	public String getTag();
	public String getFocus();
	public String getSystem();
	public String getLanguage();
	
	public String getDevelopment();
	public String getComponent();
	public String getDatabase();
	public String getTechnology();
	
	public List<Answer> getAnswerList();
}
