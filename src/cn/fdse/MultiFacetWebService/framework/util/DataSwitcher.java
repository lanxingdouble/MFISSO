package cn.fdse.MultiFacetWebService.framework.util;


import cn.fdse.codeSearch.openInterface.module.Classification;
import cn.fdse.codeSearch.openInterface.module.ClassificationList;
import cn.fdse.codeSearch.openInterface.searchResult.CodeResult;

public class DataSwitcher {

	public static String switchData(ClassificationList cl){
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		for(Classification c:cl.getClassification())
		{
			
			StringBuffer subSb = new StringBuffer();
			StringBuffer idSb = new StringBuffer();
			for(CodeResult cr:c.getCodeSegments())
			{
				idSb.append(cr.getId()+";");
				subSb.append("{text:'");
				subSb.append(
						processSpecialChar(cr.getTitle())+
						"<span style=\"display:none\" >"+
						cr.getId()+
						"</span>");
				subSb.append("'},");
			}
			idSb.deleteCharAt(idSb.length()-1);
			subSb.deleteCharAt(subSb.length()-1);
			
			sb.append("{");
			sb.append("text:'");
			
			sb.append(processSpecialChar(c.getDescription())+"("+c.getCodeSegments().size()+")"+
					"<span id=\"x\" style=\"display:none\" >"+
					idSb.toString()+
					"</span>");
			
			sb.append("',children:[");
			
			//add leaf nodes
			sb.append(subSb.toString());
			
			sb.append("]");
			sb.append("},");
		}
		if(sb.length()!=1)
			sb.deleteCharAt(sb.length()-1);
		sb.append("]");
//		System.out.println(sb.toString());
		return sb.toString();
	}

	private static String getShownTitle(CodeResult cr) {
		return cr.getTitle();
	}
//	public static String processSpecialChar(String string)
//	{
//		return string.replace("{", "")
//				.replace("}", "")
//				.replace("[", "")
//				.replace("]", "")
//				.replace("'", "")
//				.replace("&","")
//				.replace(",","")
//				.replace("<", "")
//				.replace(">", "");
//	}
	public static String processSpecialChar(String string)
	{
		return string.replace("<font color='red'>","")
				.replace("</font>", "")
				.replace("{", "")
				.replace("}", "")
				.replace("[", "")
				.replace("]", "")
				.replace("\'", "&#39;")
				.replace("\"", "&quot;")
				.replace("&","&amp;")
				.replace(",","")
				.replace("<", "&gt;")
				.replace(">", "&lt")
				.replace("\n", "\\n")
				.replace("\r", "\\r")
				.replace("/", "\\/")
				.replace("\\", "\\\\");
	
	}

}
