package cn.fdse.MultiFacetWebService.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.fdse.StackOverflow.OperationProcess.OperationProcess;

/**
 * Servlet implementation class ProcessServlet
 */
public class ProcessServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ProcessServlet() {
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
		// TODO Auto-generated method stub
		String processData = request.getParameter("text");
		if(processData.equals("Window Close"))
		{
			OperationProcess op = (OperationProcess)request.getSession().getAttribute("OperationProcess");
			if(op!=null)
				op.outPut();
			request.getSession().setAttribute("OperationProcess", null);
			
		}else
		{
			if(!processData.equals("Window CloseupdateButton"))
			{
				OperationProcess op = (OperationProcess)request.getSession().getAttribute("OperationProcess");
				if(op!=null)
					op.setProcessData(processData);
			}
		}
	}

}
