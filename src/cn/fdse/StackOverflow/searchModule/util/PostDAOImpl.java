package cn.fdse.StackOverflow.searchModule.util;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.NumericRangeFilter;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Formatter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleFragmenter;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

//import com.sun.org.apache.xalan.internal.xsltc.runtime.Hashtable;

import cn.fdse.StackOverflow.searchModule.Answer;
import cn.fdse.StackOverflow.searchModule.Post;
import cn.fdse.codeSearch.openInterface.searchResult.CodeResult;

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
    TermQuery titleQuery = null, bodyQuery = null, tagQuery = null;
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
            System.out.println("---------------------data dir:" + dirPost);
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

    public void setPath(String systemPath) {

        file = new File(systemPath + "postAndFacet");
    }


    public List<CodeResult> findPostFromLuceneAndDatabase(String keywords) {
        postList.clear();

        String[] keyword = keywords.split(",");
        int num1 = 0, num2 = 0;
        boolean iS = false;

        for (String kw : keyword) {
            if (!kw.equalsIgnoreCase("")) {
                kw = kw.toLowerCase().replace(":", "");
                titleQuery = new TermQuery(new Term("Title", kw));
                titleQuery.setBoost(40f);
                bodyQuery = new TermQuery(new Term("Body", kw));
                bodyQuery.setBoost(1f);
                tagQuery = new TermQuery(new Term("Tags", kw));
                tagQuery.setBoost(1f);
                booleanQuery.add(titleQuery, BooleanClause.Occur.SHOULD);
                booleanQuery.add(bodyQuery, BooleanClause.Occur.SHOULD);
                booleanQuery.add(tagQuery, BooleanClause.Occur.SHOULD);
//				 
            }

        }
//		 

//		 titleQuery = new TermQuery(new Term("Title","java"));
//		 booleanQuery.add(titleQuery,BooleanClause.Occur.SHOULD);

        try {
            docsPost = searcherPost.search(booleanQuery, 200);
            System.out.println("---------booleanQuery----------");
            System.out.println(booleanQuery);
            System.out.println("---------docsPost--------------");
            System.out.println(docsPost);
            queryScorer = new QueryScorer(booleanQuery);

            highlighter = new Highlighter(formatter, queryScorer);
            highlighter.setTextFragmenter(new SimpleFragmenter(200));
            int num = 0;
            int numIndex = 0;
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
