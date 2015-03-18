package cn.fdse.StackOverflow.tagFacet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.carrot2.clustering.lingo.LingoClusteringAlgorithm;
import org.carrot2.core.Cluster;
import org.carrot2.core.Controller;
import org.carrot2.core.ControllerFactory;
import org.carrot2.core.Document;
import org.carrot2.core.ProcessingResult;
import cn.fdse.codeSearch.openInterface.module.Classification;
import cn.fdse.codeSearch.openInterface.module.ClassificationList;
import cn.fdse.codeSearch.openInterface.module.ModuleProvider;
import cn.fdse.codeSearch.openInterface.searchResult.CodeResult;

public class Tag implements ModuleProvider {

	@Override
	public ClassificationList analysis(List<CodeResult> postList,
			Map<String, Object> dataMap) {
		// TODO Auto-generated method stub	
		HashMap<String,List<CodeResult>> facetMap = new HashMap<String,List<CodeResult>>();	
		List<Cluster> clusterByTagList = new ArrayList<Cluster>();
		try {
				clusterByTagList = clusteringByTag(postList);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		focusFacet TAG = new focusFacet("Tag");
		for(Cluster tagCluster:clusterByTagList){
			List<Document> documentList = tagCluster.getDocuments();
			List<CodeResult> codeResultList = new ArrayList<CodeResult>();
			for (Document document:documentList)
			{
				CodeResult post = document.getField("post");
				codeResultList.add(post);
			}
		   	FocusItem item = new FocusItem(tagCluster.getLabel(),codeResultList);
		   	TAG.classifications.add(item);
		}
		
		return TAG;
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
	
	public  List<Cluster> clusteringByTag(List<CodeResult> postList) throws Exception {
		/* Prepare Carrot2 documents */
		final ArrayList<Document> documents = new ArrayList<Document>();
		documents.clear();
		for (CodeResult post : postList) {
			String tag = post.getTag();
				Document doc = new Document(tag, "Summary",
						tag);
				doc.setField("post", post);
				documents.add(doc);
			}

		/* A controller to manage the processing pipeline. */
		final Controller controller = ControllerFactory.createSimple();
		final ProcessingResult byTagClusters = controller.process(documents,
				null, LingoClusteringAlgorithm.class);
		final List<Cluster> clustersByTag = byTagClusters.getClusters();
//		ConsoleFormatter.displayClusters(clustersByTag);
		return clustersByTag;
	}
	
	class focusFacet implements ClassificationList {

		List<Classification> classifications = new ArrayList<Classification>();
		private String title = null;
		private String id = null;
		
		public focusFacet(String title){
			this.title = title;
		}
		
		@Override
		public List<Classification> getClassification() {
			// TODO Auto-generated method stub
			return classifications;
		}
		
		public void setClassification(List<FocusItem> items){
			classifications = new ArrayList<Classification>();
			for(FocusItem item:items){
				classifications.add(item);
			}
		}

		@Override
		public String getId() {
			// TODO Auto-generated method stub
			return id;
		}

		public void setId(int id){
			this.id = id+"";
		}

		@Override
		public String getTitle() {
			// TODO Auto-generated method stub
			return title;
		}

		public void setTitle(String t){
			title = t;
		}

	}
	class FocusItem implements Classification {

		public List<CodeResult> focusItem =new ArrayList<CodeResult>();
		private String description = null;
		
		public FocusItem(String description,List<CodeResult> focusItem){
			this.description = description;
			this.focusItem = focusItem;
		}
		@Override
		public List<CodeResult> getCodeSegments() {
			// TODO Auto-generated method stub
			return focusItem;
		}
		 
		@Override
		public String getDescription() {
			// TODO Auto-generated method stub
			return description;
		}
		
	}
}
