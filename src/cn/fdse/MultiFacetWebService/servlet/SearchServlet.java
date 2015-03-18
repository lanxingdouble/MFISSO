package cn.fdse.MultiFacetWebService.servlet;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.fdse.MultiFacetWebService.framework.FrameManager;
import cn.fdse.MultiFacetWebService.framework.FrameResult;
import cn.fdse.MultiFacetWebService.framework.FrameSession;
import cn.fdse.MultiFacetWebService.framework.util.DataSwitcher;
import cn.fdse.codeSearch.openInterface.module.ClassificationList;

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
		FrameManager fm = null;
		if((fm = FrameManager.getSingleton())==null){
			String sysPath = this.getServletConfig().getServletContext().getRealPath("WEB-INF")+File.separator;
	        String initFile = "config.ini";
	        fm = FrameManager.getSingleton(sysPath, initFile);
		}
		
		String formTag = request.getParameter("formTag");
		if("search".equals(formTag)){
			String keywords = request.getParameter("q");
			
			FrameSession fs = fm.run(keywords);
			
			setPageData(fs.getFrameResult(), request);
			
			request.getSession().setAttribute("FrameSession", fs);
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
