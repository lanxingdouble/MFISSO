package cn.fdse.StackOverflow.searchModule;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;











import cn.fdse.StackOverflow.searchModule.util.Global;
import cn.fdse.StackOverflow.searchModule.util.PostDAOImpl;
import cn.fdse.StackOverflow.translation.BaiDuTranslator;
import cn.fdse.codeSearch.openInterface.searchInput.UserInput;
import cn.fdse.codeSearch.openInterface.searchResult.CodeResult;
import cn.fdse.codeSearch.openInterface.searchResult.SearchProvider;
import cn.fdse.filter.FilterStopWord;
import cn.fdse.filter.RemoveStopWordAndStemming;

public class SerarchResult implements SearchProvider{
	private static Map<String, Object> configMap = null;
	protected String keywords = null;
	protected List<CodeResult> codeResultList = null;

	PostDAOImpl pdi = new PostDAOImpl("H:\\MySqlDatabase\\MySQLIndex\\");
//	PostDAOImpl pdi = new PostDAOImpl("H:\\");

//	PostDAOImpl pdi = new PostDAOImpl(Global.syspath);
	
	FilterStopWord fsw = new FilterStopWord(Global.syspath);
	
//	RemoveStopWordAndStemming rsws = new RemoveStopWordAndStemming(Global.syspath);
	BaiDuTranslator trans = new BaiDuTranslator();

	@Override
	public  List<CodeResult> getResultOf(UserInput ui, Map<String, Object> dataMap){
		
//		Global.titleL.add("Indexing and Searching Lucene Index");
//		Global.titleL.add("Merge index in Lucene");
//		Global.titleL.add("Indexing and Searching Lucene Index");//
//		Global.titleL.add("Lucene wild card search");//0.5
//		Global.titleL.add("Lucene - Creating an Index using FSDirectory");
//		Global.titleL.add("search in lucene index");
//		Global.titleL.add("Search for a specific term in a Lucene index");//0.5
//		Global.titleL.add("Correct use of Lucene BooleanQuery?");//0.5
//		Global.titleL.add("How to index date field in lucene");
//		Global.titleL.add("Lucene QueryParser ");
//		Global.titleL.add("Combine queries in Lucene with BooleanQuery");
//		Global.titleL.add("Lucene Index - single term and phrase querying");
		configMap = dataMap; 
		String path = configMap.get("frame.workDir").toString();
//		pdi.setPath(path);
		//在部署的时候将postAndFacet放到
		//WEB-INF下因为path是WEB-INF
		
	
		keywords = ui.getKeyWords();
//		System.out.println("ww:"+keywords);
		
		keywords = keywords.replace("O(", "").replace(".", " ").replace("(", "").replace(")", "");
//		System.out.println("jqt:"+keywords);
//		keywords = trans.getResult(keywords);
//		System.out.println(keywords);
		
		keywords = fsw.getStringWithoutStopWord(keywords);	
		
//		keywords = rsws.removeStopWordAndStemm(keywords);
		
		System.out.println(keywords);
//		System.out.println("keword:"+keywords);
		
		List<CodeResult> postList = pdi.findPostFromLuceneAndDatabase(keywords);
//		 List<CodeResult> postList = new ArrayList<CodeResult>();

		return postList;
	}

}
