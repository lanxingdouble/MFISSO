
package cn.fdse.StackOverflow.topicFacet;

import cn.fdse.StackOverflow.searchModule.util.Global;
import cn.fdse.codeSearch.openInterface.module.Classification;
import cn.fdse.codeSearch.openInterface.module.ClassificationList;
import cn.fdse.codeSearch.openInterface.module.ModuleProvider;
import cn.fdse.codeSearch.openInterface.searchResult.CodeResult;
import cn.fdse.filter.RemoveStopWordAndStemming;
import cn.fdse.filter.StopWordAndStemm;
import org.carrot2.core.Cluster;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class topicFacet implements ModuleProvider {
    Map<String, Object> processingAttributes = new HashMap<String, Object>();
    List<Cluster> clusterByContentList = new ArrayList<Cluster>();


    List<CodeResult> postList;
    Map<String, Object> dataMap;
    focusFacet CONTENT;
    int size;
    int count;

    StopWordAndStemm wns = null;
    RemoveStopWordAndStemming rsws = null;
    String path;

    public topicFacet() {


    }

    @Override
    public ClassificationList analysis(List<CodeResult> postList,
                                       Map<String, Object> dataMap) {
        List<Classification> classifications = new ArrayList<Classification>();
        topicThread topicT[] = new topicThread[20];
        Thread t[] = new Thread[20];
        this.postList = postList;
        this.dataMap = dataMap;
        CONTENT = new focusFacet("Topic");
        path = dataMap.get("frame.workDir").toString();
        focusFacet CONTENT = new focusFacet("Topic");
        wns = new StopWordAndStemm(Global.root_path);
//		rsws = new RemoveStopWordAndStemming(path);

        // TODO Auto-generated method stub
        size = postList.size();
        count = size / 50;
        if (size % 50 != 0) {
            count++;
        }
        for (int i = 0; i < count; i++) {
            List<CodeResult> splitPostList = new ArrayList<CodeResult>();

            for (int k = i * 50; k < (i + 1) * 50; k++) {
                if (k == size)
                    break;
                splitPostList.add(postList.get(k));

            }
            topicT[i] = new topicThread(wns, splitPostList, classifications, false);
//			topicT[i] = new topicThread(rsws,splitPostList,classifications,false);

            t[i] = new Thread(topicT[i]);
            t[i].start();
        }
        for (int i = 0; i < count; i++) {
            try {
                t[i].join();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
//
//		int num = classifications.size();
//		for(int k = 0; k<num;k++)
//		{
//			FocusItem item = (FocusItem)classifications.get(k);
//
//			if(item.getDescription().equalsIgnoreCase("Other Topics"))
//			{			
//				classifications.remove(k);
//				wns = new RemoveStopWordAndStemming(path);
//				System.out.println(item.getCodeSegments().size());
//				topicThread otherTopic = new topicThread(wns,item.getCodeSegments(),classifications,true);
//				Thread otherT = new Thread(otherTopic);
//				otherT.start();
//				try {
//					otherT.join();
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				break;
//			}
//		}


        CONTENT.classifications = classifications;
        return CONTENT;
    }


    @Override
    public boolean needSpecialRefine() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public ClassificationList refine(List<CodeResult> arg0,
                                     Map<String, Object> arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    @SuppressWarnings("null")
    class focusFacet implements ClassificationList {

        List<Classification> classifications = new ArrayList<Classification>();
        private String title = null;
        private String id = null;

        public focusFacet(String title) {
            this.title = title;
        }

        @Override
        public List<Classification> getClassification() {
            // TODO Auto-generated method stub
            return classifications;
        }

        public void setClassification(List<FocusItem> items) {
            classifications = new ArrayList<Classification>();
            for (FocusItem item : items) {
                classifications.add(item);
            }
        }

        @Override
        public String getId() {
            // TODO Auto-generated method stub
            return id;
        }

        public void setId(int id) {
            this.id = id + "";
        }

        @Override
        public String getTitle() {
            // TODO Auto-generated method stub
            return title;
        }

        public void setTitle(String t) {
            title = t;
        }

    }

    public static void main(String args[]) {
//		String s="The school is very beautiful.";
//		PreprocessingContext pc = new PreprocessingContext();
//]		StopWordLabelFilter sf = new StopWordLabelFilter();
//		sf.acceptWord(s, 0);

    }


}
