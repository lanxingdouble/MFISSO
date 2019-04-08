package cn.fdse.MultiFacetWebService.servlet;

import cn.fdse.MultiFacetWebService.framework.FrameManager;
import cn.fdse.MultiFacetWebService.framework.FrameResult;
import cn.fdse.MultiFacetWebService.framework.FrameSession;
import cn.fdse.MultiFacetWebService.framework.util.DataSwitcher;
import cn.fdse.StackOverflow.OperationProcess.OperationProcess;
import cn.fdse.StackOverflow.searchModule.util.Global;
import cn.fdse.codeSearch.openInterface.module.ClassificationList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Servlet implementation class SearchServlet
 */
public class SearchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SearchServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    request.setCharacterEncoding("UTF-8");
		FrameManager fm = null;
		if((fm = FrameManager.getSingleton())==null){
			String sysPath = this.getServletConfig().getServletContext().getRealPath("WEB-INF")+File.separator;
	        String initFile = "config.ini";
	        fm = FrameManager.getSingleton(sysPath, initFile);
	        Global.syspath = sysPath;
	        String userId = request.getParameter("userid");
			//System.out.println("+++++++++++++++ userid:"+userId);
			Global.userId = userId;
	        
 		}
		
		//get user id
		String formTag = request.getParameter("formTag");
		//System.out.println("+++++++++++++++ formTag:"+formTag);

		if("search".equals(formTag)){
//			OperationProcess.getInstance().outPut();
//			OperationProcess.getInstance().initProcessData();
			OperationProcess op;
			List<String> facetList = new ArrayList<String>();
			op = (OperationProcess)request.getSession().getAttribute("OperationProcess");
			if(op!=null)
			{
				op.outPut();
				
			}
			op = new OperationProcess(facetList);
			request.getSession().setAttribute("OperationProcess", op);
			String keywords = request.getParameter("q");
//			System.out.println("+++++++++++++++ query keywords:"+keywords);
			FrameSession fs = fm.run(keywords);;
			op.setProcessData("Keywords:"+keywords);
			setPageData(fs.getFrameResult(), request);
			request.getSession().setAttribute("FrameSession", fs);
			request.getSession().setAttribute("Keyword", keywords);
			System.out.println("search done");
			
		}else if("update".equals(formTag)){
			String updateString = request.getParameter("updateData");
			FrameSession fs = (FrameSession) request.getSession().getAttribute("FrameSession");
			fs.refine(updateString);
			setPageData(fs.getFrameResult(), request);
		}

//		{
//			request.getSession().setAttribute("results", new ArrayList<CodeResult>());
//			request.getSession().setAttribute("map", new HashMap<String,String>());
//			request.getSession().setAttribute("facetIds", new ArrayList<String>());
//			request.getSession().setAttribute("titles", new ArrayList<String>());
//		}
		//服务器内部跳转，地址栏不变
		request.getRequestDispatcher("/search.jsp").forward(request, response);
	
	}

	private void setPageData(FrameResult fr, HttpServletRequest request){
		List<ClassificationList> ret = fr.facets;
		Map<String, String> map = new HashMap<String, String>();
		List<String> facetIds = new ArrayList<String>();
		List<String> titles = new ArrayList<String>();
		
		for(ClassificationList cl : ret){
			String id = ret.indexOf(cl)+"";
			facetIds.add(id);
			titles.add(cl.getTitle());
			map.put(id, DataSwitcher.switchData(cl));
		}
		
		request.getSession().setAttribute("results", fr.results);
		request.getSession().setAttribute("map", map);
		request.getSession().setAttribute("facetIds", facetIds);
		request.getSession().setAttribute("titles", titles);
		
	}

}
