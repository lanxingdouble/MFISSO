package cn.fdse.StackOverflow.searchModule;

import cn.fdse.NewInputHandle.InputFacet;
import cn.fdse.NewInputHandle.InputIDF;
import cn.fdse.StackOverflow.searchModule.util.Global;
import cn.fdse.StackOverflow.searchModule.util.PostDAOImpl;
import cn.fdse.StackOverflow.translation.BaiDuTranslator;
import cn.fdse.codeSearch.openInterface.searchInput.UserInput;
import cn.fdse.codeSearch.openInterface.searchResult.CodeResult;
import cn.fdse.codeSearch.openInterface.searchResult.SearchProvider;
import cn.fdse.filter.FilterStopWord;
import cn.fdse.se.facet.PostFacetType;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class SerarchResult implements SearchProvider {
    private static Map<String, Object> configMap = null;
    protected String keywords = null;
    protected List<CodeResult> codeResultList = null;

    PostDAOImpl pdi = new PostDAOImpl(Global.facet_index_path);
//	PostDAOImpl pdi = new PostDAOImpl(Global.syspath);

    FilterStopWord fsw = new FilterStopWord(Global.root_path);

    //	RemoveStopWordAndStemming rsws = new RemoveStopWordAndStemming(Global.syspath);
    BaiDuTranslator trans = new BaiDuTranslator();

    @Override
    public List<CodeResult> getResultOf(UserInput ui, Map<String, Object> dataMap) {

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
        System.out.println("input query: " + keywords);

        keywords = keywords.replace("O(", "").replace(".", " ").replace("(", "").replace(")", "");
//		System.out.println("jqt:"+keywords);
//		keywords = trans.getResult(keywords);
//		System.out.println(keywords);
        //对query去除停用词

        InputFacet input_facet = new InputFacet();
        PostFacetType pt = input_facet.handleinput(keywords);

        keywords = fsw.getStringWithoutStopWord(keywords);
        System.out.println("---------start print key word without stop word---------");
        System.out.println(keywords);
//		InputTextRank input_text=new InputTextRank();
//		keywords =input_text.handleInput(keywords);
////		keywords = rsws.removeStopWordAndStemm(keywords);
//		System.out.println("---------start print key word with textrank---------");
//		System.out.println(keywords);
        InputIDF input_idf = new InputIDF();
        try {
            keywords = input_idf.handleInput(keywords, 20);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("---------start print key word with tf-idf---------");
        System.out.println(keywords);
        List<CodeResult> postList = pdi.findPostFromLuceneAndDatabase(keywords, pt);
//		 List<CodeResult> postList = new ArrayList<CodeResult>();

        return postList;
    }

}
