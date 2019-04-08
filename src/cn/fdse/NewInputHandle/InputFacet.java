package cn.fdse.NewInputHandle;

import cn.fdse.se.NLP.Sentence;
import cn.fdse.se.NLP.TestCoreNLP;
import cn.fdse.se.Post;
import cn.fdse.se.PostDAOImpl;
import cn.fdse.se.facet.Facet;
import cn.fdse.se.facet.Grade;
import cn.fdse.se.facet.InitFacetItem;
import cn.fdse.se.facet.PostFacetType;

import java.util.*;


public class InputFacet {
    private TestCoreNLP tcNLP;
    private InitFacetItem ift;
    private List<Sentence> sentenceList;
    List<Post> postList;
    List<Facet> itemList;

    public PostFacetType handleinput(String text) {
//		FilterPost.setFilterTag("database");
        tcNLP = new TestCoreNLP();
        PostDAOImpl pdi = new PostDAOImpl();
        postList = new ArrayList<Post>();
        String title =text;
        String body = "";
        Post p = new Post(0, title, body, "", 0, 1, 0, 0);
        postList.add(p);
        //System.out.println("Size:"+postList.size());
        ift = new InitFacetItem();
        itemList = ift.getItem();
        //System.out.println(itemList);
        matchFacetItem();
        HashMap<Integer, PostFacetType> postfacettype = getPostFacetType();
        PostFacetType f = postfacettype.get(p.postId);
        System.out.println(f.concern);
        System.out.println(f.system);
        System.out.println(f.language);
        System.out.println(f.technology);
        System.out.println(f.database);
        System.out.println(f.framework);
        System.out.println(f.development);
        return postfacettype.get(p.postId);
    }

    public void matchFacetItem() {
        int postNum = 0;
        for (Post post : postList) {
            postNum++;
            for (Facet item : itemList) {
                item.setIsMatch(false);
                item.MatchPattern(post);//match tag && code
            }
            String text = post.getText((post.post_body_text));
            text = post.post_title + ". " + text;
            text = replaceSpecialWord(text);

//	       System.out.println("time1:"+new Date(System.currentTimeMillis()));
            tcNLP.CoreNLP(text);
//	        System.out.println("time:"+new Date(System.currentTimeMillis()));
            sentenceList = tcNLP.getSentence();
            for (Facet item : itemList) {
//			   if(item.getEnvir().contains("Explain"))
//			   {
//				   System.out.println("ok");
//				   item.init(sentenceList, post, queryClause);
//			   }
                if (item.isMatch())
                    continue;
                item.init(sentenceList, post, null);
            }
        }

    }

    public HashMap<Integer, PostFacetType> getPostFacetType() {
//        if(isShow)
//        {
//            for(Facet item:itemList)
//            {
//                System.out.println(item.getEnvir());
//                item.postList(null);
////                System.out.println("getpost: "+item.getPost());
//                item.showPost();
//                System.out.println("----------------");
//            }
//        }
//		List<PostFacetType> pftList = new ArrayList<PostFacetType>();
        HashMap<Integer, PostFacetType> hashpft = new HashMap<Integer, PostFacetType>();

        for (Facet item : itemList) {
            HashMap<Post, Grade> postM = item.getPost();
            if (postM == null)
                continue;
            Iterator iter = postM.keySet().iterator();

            while (iter.hasNext()) {
                Post post = (Post) iter.next();
                if (item.getEnvir().contains("Concern")) {
                    if (!hashpft.containsKey(post.postId)) {
                        PostFacetType pft = new PostFacetType();
                        pft.concern += item.getEnvir() + ",";
                        pft.postId = post.postId;
                        pft.postTypeId = 1;
                        hashpft.put(post.postId, pft);
                    } else {
                        PostFacetType pft = hashpft.get(post.postId);

                        pft.concern += item.getEnvir() + ",";
                    }


                }
                if (item.getEnvir().contains("Language")) {
                    if (!hashpft.containsKey(post.postId)) {
                        PostFacetType pft = new PostFacetType();
                        pft.language += item.getEnvir() + ",";
                        pft.postId = post.postId;
                        pft.postTypeId = 1;
                        hashpft.put(post.postId, pft);
                    } else {
                        PostFacetType pft = hashpft.get(post.postId);
                        pft.language += item.getEnvir() + ",";
                    }
                }
                if (item.getEnvir().contains("Operation System")) {
                    if (!hashpft.containsKey(post.postId)) {
                        PostFacetType pft = new PostFacetType();
                        pft.system += item.getEnvir() + ",";
                        pft.postId = post.postId;
                        pft.postTypeId = 1;
                        hashpft.put(post.postId, pft);
                    } else {
                        PostFacetType pft = hashpft.get(post.postId);
                        pft.system += item.getEnvir() + ",";
                    }
                }
                if (item.getEnvir().contains("Library and Software Technology")) {
                    if (!hashpft.containsKey(post.postId)) {
                        PostFacetType pft = new PostFacetType();
                        pft.technology += item.getEnvir() + ",";
                        pft.postId = post.postId;
                        pft.postTypeId = 1;
                        hashpft.put(post.postId, pft);
                    } else {
                        PostFacetType pft = hashpft.get(post.postId);
                        pft.technology += item.getEnvir() + ",";
                    }
                }
                if (item.getEnvir().contains("Database")) {
                    if (!hashpft.containsKey(post.postId)) {
                        PostFacetType pft = new PostFacetType();
                        pft.database += item.getEnvir() + ",";
                        pft.postId = post.postId;
                        pft.postTypeId = 1;
                        hashpft.put(post.postId, pft);
                    } else {
                        PostFacetType pft = hashpft.get(post.postId);
                        pft.database += item.getEnvir() + ",";
                    }
                }
                if (item.getEnvir().contains("Middleware and Framework")) {
                    if (!hashpft.containsKey(post.postId)) {
                        PostFacetType pft = new PostFacetType();
                        pft.framework += item.getEnvir() + ",";
                        pft.postId = post.postId;
                        pft.postTypeId = 1;
                        hashpft.put(post.postId, pft);
                    } else {
                        PostFacetType pft = hashpft.get(post.postId);
                        pft.framework += item.getEnvir() + ",";
                    }
                }
                if (item.getEnvir().contains("Development Tool")) {
                    if (!hashpft.containsKey(post.postId)) {
                        PostFacetType pft = new PostFacetType();
                        pft.development += item.getEnvir() + ",";
                        pft.postId = post.postId;
                        pft.postTypeId = 1;
                        hashpft.put(post.postId, pft);
                    } else {
                        PostFacetType pft = hashpft.get(post.postId);
                        pft.development += item.getEnvir() + ",";
                    }
                }
            }
        }

        for (Post post : postList) {
            if (!hashpft.containsKey(post.postId)) {
                PostFacetType p = new PostFacetType();
                p.postTypeId = 1;
                p.creationDate = post.creationTime;
                p.lastEditDate = post.lastEditTime;
                hashpft.put(post.postId, p);
            } else {
                PostFacetType pft = hashpft.get(post.postId);
                pft.creationDate = post.creationTime;
                pft.lastEditDate = post.lastEditTime;

            }

        }
        Iterator iter = hashpft.keySet().iterator();

        while (iter.hasNext()) {
            PostFacetType pft = (PostFacetType) hashpft.get(iter.next());
            if (pft.concern.equals(""))
                pft.concern = "Others";
            if (pft.system.equals(""))
                pft.system = "Others";
            if (pft.language.equals(""))
                pft.language = "Others";
            if (pft.technology.equals(""))
                pft.technology = "Others";
            if (pft.database.equals(""))
                pft.database = "Others";
            if (pft.framework.equals(""))
                pft.framework = "Others";
            if (pft.development.equals(""))
                pft.development = "Others";
        }
//        Iterator iterr = hashpft.keySet().iterator();
//        while (iterr.hasNext()) {
//            PostFacetType pft = (PostFacetType) hashpft.get(iterr.next());
//            System.out.println(pft.concern);
//            System.out.println(pft.system);
//            System.out.println(pft.language);
//            System.out.println(pft.technology);
//            System.out.println(pft.database);
//            System.out.println(pft.framework);
//            System.out.println(pft.development);
//        }
        return hashpft;
    }


    public String replaceSpecialWord(String text) {
        text = text.toLowerCase();
        return text.replace(".net", "dotNET")
                .replace("apache tomcat", "Tomcat")
                .replace(".jar", "jar")
                .replace("c++", "CPlusLanguage")
                .replace("c#", "CWellLanguage")
                .replace("sql server", "SQLServer")
                .replace("node.js", "NodeDotJs")
                .replace("visual studio", "VisualStudio")
                .replace("java ee", "javaEE")
                .replace("?", "?");
    }

    public static void main(String args[]) {
        InputFacet main = new InputFacet();
        String text="How to show a modal dialog in CSS JavaScript using bootstrap? Im using react-16 in a project created using create-react-app and the vanilla bootstrap 4 library that provides styling (no components). Can someone point me towards a working example where pressing a button I can open a modal dialog styled with bootstrap?";
        main.handleinput(text);
    }
}
