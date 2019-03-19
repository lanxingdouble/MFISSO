package cn.fdse.StackOverflow.topicFacet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.carrot2.clustering.lingo.LingoClusteringAlgorithm;
import org.carrot2.clustering.stc.STCClusteringAlgorithm;
import org.carrot2.core.Cluster;
import org.carrot2.core.Controller;
import org.carrot2.core.ControllerFactory;
import org.carrot2.core.Document;
import org.carrot2.core.ProcessingResult;
import org.carrot2.core.attribute.AttributeNames;

import cn.fdse.StackOverflow.searchModule.Post;
import cn.fdse.StackOverflow.searchModule.util.Global;
import cn.fdse.codeSearch.openInterface.module.Classification;
import cn.fdse.codeSearch.openInterface.searchResult.CodeResult;
import cn.fdse.filter.RemoveStopWordAndStemming;
import cn.fdse.filter.StopWordAndStemm;

public class topicThread implements Runnable {

    Map<String, Object> processingAttributes = new HashMap<String, Object>();
    StopWordAndStemm wns = null;
    RemoveStopWordAndStemming rsws = null;

    List<CodeResult> postList;

    List<Cluster> clusterByContentList = new ArrayList<Cluster>();
    List<Classification> classifications;
    ;
    String label;
    boolean output = false;

    public topicThread(StopWordAndStemm wns, List<CodeResult> postList, List<Classification> classifications, boolean output) {
        this.wns = wns;
        this.postList = postList;
        this.classifications = classifications;
        this.output = output;

    }

    public topicThread(RemoveStopWordAndStemming rsws, List<CodeResult> postList, List<Classification> classifications, boolean output) {
        this.rsws = rsws;
        this.postList = postList;
        this.classifications = classifications;
        this.output = output;

    }

    @Override
    public void run() {
        // TODO Auto-generated method stub

        clusterByContentList = clusteringByContent(postList);
//        System.out.println("postlist:"+postList);
//        for(CodeResult i:postList){
//            System.out.println("post body**"+i.getBody_text());
//        }
//		List<String> titleList = Global.titleL;
        for (Cluster contentCluster : clusterByContentList) {
            List<Document> documentList = contentCluster.getDocuments();
            List<CodeResult> codeResultList = new ArrayList<CodeResult>();
            label = contentCluster.getLabel();
            System.out.println("contentCluster label***:" + label);
            for (Document document : documentList) {
                CodeResult post = document.getField("post");
//				for(String title:titleList)
//				{
//					String t = post.getTitle().replace("<font color='red'>", "").replace("</font>", "");
//
//					if(t.toLowerCase().contains(title.toLowerCase()))
//						System.out.println("Label:"+label);
//				}
                codeResultList.add(post);
            }
//			FocusItem item = new FocusItem(label,codeResultList);
//			    classifications.add(item);
            mergeLabel(label, codeResultList, classifications);
        }


//		}
    }

    public List<Cluster> clusteringByContent(List<CodeResult> postList) {
        /* Prepare Carrot2 documents */
        ArrayList<Document> documents = new ArrayList<Document>();
        String title;
        String body;
        String tag;

        for (CodeResult post : postList) {
//				Document doc = new Document(wns.removeStopWordAndStemm(post.getTitle()),
//						wns.removeStopWordAndStemm(post.getBody_text()), "Description");
            title = post.getTitle().replace("<font color='red'>", "").replace("</font>", "");
            body = post.getBody_text().replace("<font color='red'>", "").replace("</font>", "");
            tag = post.getTag().replace("<font color='red'>", "").replace("</font>", "");
            Document doc = new Document(wns.getClearSentence(title + " " + body + " " + tag).toLowerCase()
                    , "", "Description");
//			Document doc = new Document(rsws.removeStopWordAndStemm(title+" "+body+" "+tag).toLowerCase()
//					,"", "Description");
//			Document doc = new Document(title+" "+body+" "+tag
//					,"", "Description");
//			Document doc = new Document(post.getTitle(),
//			post.getBody_text(), "Description");

            doc.setField("post", post);
            documents.add(doc);
        }
        STCClusteringAlgorithm stc = new STCClusteringAlgorithm();
        stc.documents = documents;
        stc.process();
        return stc.clusters;
    }

    public void mergeLabel(String label, List<CodeResult> list, List<Classification> classifi) {

        int size = classifi.size();
        if (size == 0) {
            FocusItem facetItem = new FocusItem(label, list);
            classifi.add(facetItem);
            return;
        }

        boolean isAdd = false;
        for (int i = 0; i < size; i++) {
            FocusItem item = (FocusItem) classifi.get(i);
            String tempLabel = item.getDescription();
            String s1[] = tempLabel.split(" ");
            String s2[] = label.split(" ");
            if (s1.length != s2.length) {
                continue;
            } else {

                if (judge(s1, s2)) {
                    list.addAll(item.getCodeSegments());
                    item.focusItem = list;
                    item.description = label;
                    isAdd = true;
                }
            }


        }
        if (!isAdd) {
            FocusItem facetItem = new FocusItem(label, list);
            List<String> titleList = Global.titleL;


            classifi.add(facetItem);

        }
    }

    //	public void mergeLabel(String label,List<CodeResult> list,List<Classification> classifi)
//	{
//		
//		String max[],min[];
//		String finalLabel = "";
//
//		int size = classifi.size();
//		if(size==0)   
//		{
//			FocusItem facetItem = new FocusItem(label,list);
//		   	classifi.add(facetItem);
//		   	return;
//		}
//
//		boolean isAdd = false;
//		for(int i = 0;i<size;i++)
//		{
//			FocusItem item = (FocusItem)classifi.get(i);
//			String tempLabel = item.getDescription();
//			String s1[] = tempLabel.split(" ");
//			String s2[] = label.split(" ");
//			if(s1.length>s2.length)
//			{
//				max = s1;
//				min = s2;
//				finalLabel = tempLabel;
//			}else
//			{
//				max = s2;
//				min = s1;
//				finalLabel = label;
//			}
//		
//			if(judge(min,max))
//			{
//				list.addAll(item.getCodeSegments());
//				item.focusItem = list;
//				item.description = finalLabel;
//				isAdd = true;
//			}
//		}
//		if(!isAdd)
//		{
//			FocusItem facetItem = new FocusItem(label,list);
//		   	classifi.add(facetItem);
//
//		}
//	}
//	
//
    public boolean judge(String[] min, String[] max) {
        boolean judge = true;
        for (String minLabel : min) {
            judge = false;
            for (String maxLabel : max) {
                if (minLabel.equalsIgnoreCase(maxLabel)) {
                    judge = true;
                    break;
                }
            }
            if (!judge) {
                return false;
            }
        }
        return true;
    }

}

