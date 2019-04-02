package cn.fdse.StackOverflow.searchModule.util;

import cn.fdse.StackOverflow.searchModule.Answer;
import cn.fdse.StackOverflow.searchModule.Post;
import cn.fdse.codeSearch.openInterface.searchResult.CodeResult;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.apache.lucene.search.highlight.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

//import com.sun.org.apache.xalan.internal.xsltc.runtime.Hashtable;

public class PostDAOImpl implements PostDAO {
    String answerCount;
    int size;
    //	Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_46);
    Analyzer analyzer = new SimpleAnalyzer(Version.LUCENE_46);
    List<Answer> answerList = new ArrayList<Answer>();
    List<Answer> goodAnswerList = new ArrayList<Answer>();

    List<CodeResult> postList = new ArrayList<CodeResult>();
    File file = null, fileAnswer = null;
    String parentId, postTypeId, postId;
    Directory dirPost, dirAnswer;
    TermQuery titleQuery = null, bodyQuery = null, tagQuery = null,synonymsQuery=null;
    Query numberRange = null, postType = null;
    int id = 0;
    String title, body, tag;
    Post post = null;
    IndexSearcher searcherPost = null, searchAnswer = null;
    TopDocs docsPost = null;
    Document document = null;
    String tempBody = "";
    String splitBody;
    String startPre = "<code>";
    String endPre = "</code>";
    boolean omit = false;
    List<Post> tempPostList;

    int score;
    String highterBody, titleBody, tagBody;
    int count;
    //	 BooleanQuery booleanQuery=new BooleanQuery();
    BooleanQuery booleanQuery;
    IndexReader reader, readerAnswer;
    QueryScorer queryScorer;
    Formatter formatter;
    Highlighter highlighter;
    searchAnswerThread sat[] = new searchAnswerThread[11];
    Thread t[] = new Thread[11];

    public PostDAOImpl() {

    }

    public PostDAOImpl(String systemPath) {
        try {
            booleanQuery = new BooleanQuery();
            BooleanQuery.setMaxClauseCount(50000);

            file = new File(systemPath + "postAndFacet");
//			 fileAnswer = new File(systemPath+"answers");

            dirPost = FSDirectory.open(file);
//			 dirAnswer = FSDirectory.open(fileAnswer);
            reader = DirectoryReader.open(dirPost);// 读取目录
//             readerAnswer = DirectoryReader.open(dirAnswer);
            searcherPost = new IndexSearcher(reader);
//			 searchAnswer = new IndexSearcher(readerAnswer);
            formatter = new SimpleHTMLFormatter("<font color='red'>", "</font>");

        } catch (IOException e2) {
            // TODO Auto-generated catch block
            e2.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }

    public static JsonObject readJson(String path) {
        File file = new File(path + "synonyms.json");
        String cont = null;
        JsonParser jsonParser = new JsonParser();
        try {
            if (file.exists()) {
                cont = new BufferedReader(new FileReader(file)).lines().collect(Collectors.joining("\n"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (cont == null || cont.equals("")) {
            cont = "{}";
        }
        return jsonParser.parse(cont).getAsJsonObject();
    }

    public void setPath(String systemPath) {

        file = new File(systemPath + "postAndFacet");
    }


    public List<CodeResult> findPostFromLuceneAndDatabase(String keywords) {
        JsonObject synonyms = readJson("E:\\MFISSO\\StackOverflow Search Tool code\\MFISSO WEB\\");
        postList.clear();
        String[] keyword = keywords.split(",");
        boolean iS = false;

        for (String kw : keyword) {
            if (!kw.equalsIgnoreCase("")) {
                kw = kw.toLowerCase().replace(":", "");
                //未分词的查询
                titleQuery = new TermQuery(new Term("Title", kw));
                //设置该field查询的权重
                //boost：激励因子，可以通过setBoost方法设置，需要说明的通过field和doc都可以设置，所设置的值会同时起作用
                titleQuery.setBoost(40f);
                bodyQuery = new TermQuery(new Term("Body", kw));
                bodyQuery.setBoost(10f);
                tagQuery = new TermQuery(new Term("Tags", kw));
                tagQuery.setBoost(1f);
                /*booleanQueryb表示多个查询组合，SHOULD与SHOULD：表示“或”关系，最终检索结果为所有检索子句的并集。
                 * SHOULD与MUST连用时，结果为MUST子句的检索结果,但是SHOULD可影响排序。 */
                booleanQuery.add(titleQuery, BooleanClause.Occur.SHOULD);
                booleanQuery.add(bodyQuery, BooleanClause.Occur.SHOULD);
                booleanQuery.add(tagQuery, BooleanClause.Occur.SHOULD);
            }
            if (synonyms.has(kw)) {
                //System.out.println(synonyms.get(kw).toString());
                String synonymstag_str = synonyms.get(kw).toString();
                synonymstag_str = synonymstag_str.replace("[", "").replace("]", "").replace("\"", "")+",";
                String[] synonyms_tag = synonymstag_str.split(",");
                float boost= (float) (1.0/(synonyms_tag.length*1.0));
                System.out.println("boost: "+boost);
                for (String tag:synonyms_tag) {
                    synonymsQuery = new TermQuery(new Term("Tags", tag));
                    synonymsQuery.setBoost(boost);
                    booleanQuery.add(synonymsQuery, BooleanClause.Occur.SHOULD);
                }
            }
        }
        try {
            docsPost = searcherPost.search(booleanQuery, 200);
            System.out.println("---------booleanQuery----------");
            System.out.println(booleanQuery);
//            System.out.println("---------docsPost--------------");
//            System.out.println(docsPost);
            queryScorer = new QueryScorer(booleanQuery);
            //高亮显示Lucene检索结果的关键词
            highlighter = new Highlighter(formatter, queryScorer);
            highlighter.setTextFragmenter(new SimpleFragmenter(200));
            int num = 0;
            int numIndex = 0;
            //在Lucene中score简单说是由 tf * idf * boost * lengthNorm计算得出的
            for (ScoreDoc doc : docsPost.scoreDocs) {
                tempBody = "";
                splitBody = "";
                highterBody = "";
                titleBody = "";
                tagBody = "";
                document = searcherPost.doc(doc.doc);
                postTypeId = document.get("postTypeId");
                parentId = document.get("ParentId");
                postId = document.get("postId");
                score = Integer.parseInt(document.get("Score"));
                answerCount = document.get("AnswerCount");

                if (score >= 0 && Integer.parseInt(answerCount) >= 1 && postTypeId.equals("1")) {
                    title = document.get("Title");
                    title = processJSPSpecialChar(title);
                    body = document.get("Body");
                    tag = document.get("Tags").replace("<", " ").replace(">", " ");
                    id = Integer.parseInt(postId);

                    count = body.length() / 100;
                    for (int i = 0; i < count; i++) {
                        //从beginIndex开始取，到endIndex结束，从0开始数，其中不包括endIndex位置的字符
                        splitBody = body.substring(i * 100, (i + 1) * 100);
                        //在splitBody里高亮显示匹配最好的片段，如果返回null，则显示全部
                        highterBody = highlighter.getBestFragment(analyzer.tokenStream("token", splitBody), splitBody);

                        if (highterBody == null)
                            tempBody += splitBody;
                        else
                            tempBody += highterBody;
                    }
                    //string最后一个子串
                    splitBody = body.substring(count * 100, body.length());
                    highterBody = highlighter.getBestFragment(analyzer.tokenStream("token", splitBody), splitBody);
                    if (highterBody == null)
                        tempBody += splitBody;
                    else
                        tempBody += highterBody;


                    body = tempBody;

                    titleBody = highlighter.getBestFragment(analyzer.tokenStream("token", title), title);
                    if (titleBody != null)
                        title = titleBody;

                    tagBody = highlighter.getBestFragment(analyzer.tokenStream("token", tag), tag);
                    if (tagBody != null)
                        tag = tagBody;

                    post = new Post(id, title, body, tag);
                    post.setFacetName(document.get("Concern"), document.get("System"), document.get("Language"), document.get("Development"),
                            document.get("Framework"), document.get("Database"), document.get("Technology"));
                    if (num == 0) {
                        tempPostList = new ArrayList<Post>();
                    }
                    tempPostList.add(post);
                    num++;

                    //set post answer
                    if (num == 30) {
                        sat[numIndex] = new searchAnswerThread(tempPostList, searcherPost, highlighter, analyzer);
                        t[numIndex] = new Thread(sat[numIndex]);
                        t[numIndex].start();
                        num = 0;
                        numIndex++;
                        iS = false;
                    }
//				    post.setAnaser(sortAnswer(findPostRelatedAnswers(id,searcherPost)));
//			    	postList.add(post);
                }
//				    else if(postTypeId.equals("2")&&(score>=0))
//			    {
//			    	TopDocs parentDoc = searcherPost.search(new TermQuery(new Term("postId",parentId)),1); 
//			    	for(ScoreDoc d:parentDoc.scoreDocs)
//				    {
//					    document =searcherPost.doc(d.doc);
//
//					    if(filter(document)&&!postList.contains(parentId))
//					    {
//					        id = Integer.parseInt(parentId);
//					        
//					        title = document.get("Title");
//					     	title = processJSPSpecialChar(title);
//					    	body = document.get("Body");
//
//					    	tag = document.get("Tags").replace("<", " ").replace(">", " ");
//					    	
//		     
//					    	count = body.length()/100;
//					    	for(int i = 0;i < count; i++)
//					    	{
//					    		splitBody= body.substring(i*100,(i+1)*100);
//					    		highterBody = highlighter.getBestFragment(analyzer.tokenStream("token", splitBody),splitBody);
//					    		if(highterBody==null)
//					    			tempBody += splitBody;
//					    		else
//					    			tempBody+=highterBody;
//					    	}
//					    	splitBody = body.substring(count*100,body.length());
//					    	highterBody = highlighter.getBestFragment(analyzer.tokenStream("token", splitBody),splitBody);
//					    	if(highterBody==null)
//				    			tempBody += splitBody;
//				    		else
//				    			tempBody+=highterBody;
//					    	
//					    	 titleBody = highlighter.getBestFragment(analyzer.tokenStream("token", title), title);
//
//			                 if(titleBody!=null)
//			                    title = titleBody; 
//	
//			                 tagBody = highlighter.getBestFragment(analyzer.tokenStream("token", tag), tag);
//			                    
//			                 if(tagBody!=null)
//			                    tag = tagBody;
//
//						    post = new Post(id,title,body,tag);
//							post.setFacetName(document.get("Concern"), document.get("System"), document.get("Language"), document.get("Development"),
//								document.get("Framework"),document.get("Database"),document.get("Technology"));	
//						    post.setAnaser(sortAnswer(findPostRelatedAnswers(id,searcherPost)));
//
//						    postList.add(post);
//					    }
//								 
//				    }
//			     }		    
            }

            if (tempPostList != null && tempPostList.size() != 0) {
                sat[numIndex] = new searchAnswerThread(tempPostList, searcherPost, highlighter, analyzer);
                t[numIndex] = new Thread(sat[numIndex]);
                t[numIndex].start();
                num = 0;
                numIndex++;
            }
//			readerAnswer.close();
//			dirAnswer.close();
            for (int i = 0; i < numIndex; i++) {
                t[i].join();
                postList.addAll(sat[i].getPost());
            }

        } catch (IOException e2) {
            // TODO Auto-generated catch block
            e2.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {

                reader.close();
                dirPost.close();

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return postList;
    }

    public boolean iS(List<CodeResult> postList, String postId) {
        for (CodeResult post : postList) {
            if (postId.equals(post.getId()))
                return false;
        }
        return true;
    }

    public boolean filter(Document document) {
        answerCount = document.get("AnswerCount");
        score = Integer.parseInt(document.get("Score"));
        if (answerCount.equals("0") || (score < 0))
            return false;
        return true;
    }


    @Override
    public List<Post> findPosts() throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    public static String processJSPSpecialChar(String string) {
        return string.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("\'", "\\'");
    }


    public List<Answer> findPostRelatedAnswers(int postId, IndexSearcher searcherPost) {
        answerList.clear();
        TopDocs postFacetDoc;
        try {
            postFacetDoc = searcherPost.search(NumericRangeQuery.newIntRange("ParentId", postId, postId, true, true), 20);

            for (ScoreDoc d : postFacetDoc.scoreDocs) {
                Document document = searcherPost.doc(d.doc);
                score = Integer.parseInt(document.get("Score"));
                if (score < 0)
                    continue;
                body = document.get("Body");
                Answer answer = new Answer(score, body);
                answerList.add(answer);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return answerList;


    }

    public List<Answer> sortAnswer(List<Answer> answerList) {
        goodAnswerList.clear();
        Collections.sort(answerList, new Comparator<Answer>() {
            public int compare(Answer o1, Answer o2) {
                int result = o2.getScore() - o1.getScore();
                return result;
            }
        });
        size = answerList.size();
        if (size >= 3)
            size = 3;
        for (int j = 0; j < size; j++) {
            Answer answer = answerList.get(j);
            body = answer.getBody();
            tempBody = "";
            try {
                count = body.length() / 100;
                for (int i = 0; i < count; i++) {
                    splitBody = body.substring(i * 100, (i + 1) * 100);

                    highterBody = highlighter.getBestFragment(analyzer.tokenStream("token", splitBody), splitBody);
                    if (highterBody == null)
                        tempBody += splitBody;
                    else
                        tempBody += highterBody;
                }
                splitBody = body.substring(count * 100, body.length());
                highterBody = highlighter.getBestFragment(analyzer.tokenStream("token", splitBody), splitBody);
                if (highterBody == null)
                    tempBody += splitBody;
                else
                    tempBody += highterBody;
                body = tempBody;
                answer.setBody(body);
            } catch (IOException e2) {
                // TODO Auto-generated catch block
                e2.printStackTrace();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            goodAnswerList.add(answer);
        }
        return goodAnswerList;
    }


    @Override
    public List<CodeResult> findPosts(String keyword) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    public static void main(String args[]) throws Exception {
        List<CodeResult> postList = null;
//	    System.out.println("time1:"+new Date(System.currentTimeMillis())); 
//		PostDAOImpl pdi = new PostDAOImpl();
//		How,connect,mysql,
//		how,connect,mysql
//		postList = pdi.findPostFromLuceneAndDatabase("java,array,");
//		System.out.println(postList.size());
//	    System.out.println("time2:"+new Date(System.currentTimeMillis())); 

    }

}
