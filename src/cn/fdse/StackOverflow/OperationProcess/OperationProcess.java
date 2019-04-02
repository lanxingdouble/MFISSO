package cn.fdse.StackOverflow.OperationProcess;

import cn.fdse.StackOverflow.searchModule.util.Global;
import cn.fdse.StackOverflow.writeText.WriteText;

import java.util.HashMap;
import java.util.List;

public class OperationProcess {
	private String processData = ""; 
	private String title = "";
	private List<String> facetList;
	private String keyword="";
	private String suggestion = "";
	
	public OperationProcess(List<String> list)
	{
		facetList = list;
	}
	public String getProcessData(String facet)
	{
		if(!facet.contains("Facet:"))
			return facet;
		else{
			if(facetList.contains(facet))
			{
				int size = facetList.size();
				String temp;
				for(int i=0;i<size;i++)
				{
					temp = facetList.get(i);
					if(temp.equals(facet))
					{
						facetList.remove(i);
						return "Delete"+temp;
					}
				}
			}else
			{
				facetList.add(facet);
				return "Select"+facet;
			}
		}
		return "Facet:error";		

	}
	public void setProcessData(String data)
	{
		if(data.contains("Keywords:"))
		{
			keyword += data +"|";
		
		}
		if(data.contains("Click:")||data.contains("Copy:"))
		{
			title += data + " ";
		}
		if(data.contains("Suggestion:"))
		{
			suggestion += data +"|";
					
		}
		processData += getProcessData(data)+" ";
	
	}
	
	public void outPut()
	{ 
		
		HashMap<String,String> idFacetName = Global.idFacetName;
		int size = idFacetName.size();
		for(int i = 0;i<size;i++)
		{
			if(processData.contains("Facet:"+i))
			{
				processData = processData.replace("Facet:"+i, "Facet:"+idFacetName.get("Facet:"+i));
			}
		}
		System.out.println("---------keyword:"+keyword);
		System.out.println("---------processData:"+processData);
		System.out.println("---------title:"+title);
		System.out.println("---------suggestion:"+suggestion);
		WriteText wt = new WriteText();
		String s = "UserId:"+Global.userId + " " + processData +"\r\n";
		System.out.println(s);
//		String s  = keyword+" & "+ processData +" & "+title +" & "+suggestion +" & " +"\r\n";
		wt.writeText(s);

	}

}
