
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ page language="java" import="cn.fdse.codeSearch.openInterface.module.ClassificationList" %>
<%@ page language="java" import="cn.fdse.codeSearch.openInterface.searchResult.CodeResult" %>
<%
	List<?> facetIds = (List<?>)(request.getSession().getAttribute("facetIds"));
	List<?> titles = (List<?>)(request.getSession().getAttribute("titles"));
	Map<?, ?> map = (Map<?, ?>)(request.getSession().getAttribute("map"));
	List<?> results = (List<?>)(request.getSession().getAttribute("results"));
	String sysPath = (String)(request.getSession().getAttribute("path"));
	
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html;charset=gb2312" />
<title>Multi-Facet Web Service</title>
<link rel="shortcut icon" href="favicon.ico" />
<link href="css/style.css" rel="stylesheet" type="text/css" />
<link href="css/treeStyle.css" rel="stylesheet" type="text/css" />
<link href="css/codesStyle.css" rel="stylesheet" type="text/css" />
<link href="css/popwindow.css" rel="stylesheet" type="text/css" />
<style>
div.l-body span{
	float:left;
}
</style>

	<script src="LigerUiLib/jquery/jquery-1.11.1.min.js" type="text/javascript"></script>
	<link href="LigerUiLib/ligerUI/skins/Aqua/css/ligerui-tree.css" rel="stylesheet" type="text/css" />		
	<script src="LigerUiLib/ligerUI/js/core/base.js" type="text/javascript"></script>
	<script src="LigerUiLib/ligerUI/js/plugins/ligerTree.js" type="text/javascript"></script>
	<script type="text/javascript">
	javascript:window.history.forward(1);
	$.ligerui.controls.Tree.prototype.dealSelect = function (resultSetIndex){
		$("div.hit_block").hide();
		var data = this.getChecked();
		var str = "";
		for(var i=0;i<data.length;i++){
			var resultId = getId(data[i]['data']['text']);
			if(resultId){
				str+=resultId+";";
//				$("div#lId_"+resultId).show();
			}
		}
		resultSet[resultSetIndex] = str.substring(0, str.length-1);
		showInterSection();
	};
	
	function showInterSection(){
		var intersection = new Array();
		var first = true;
		var resultIndex = null;
		for(resultIndex in resultSet){
			if(resultSet[resultIndex]==""){
				
			}else{
				var resultIds = (resultSet[resultIndex]).split(";");
				if(first){
					for(var i=0;i<resultIds.length;i++ ){
						intersection.push(resultIds[i]);
					}
					first=false;
				}else{
					var tmp = new Array();
					for(var i=0;i<resultIds.length;i++ ){
						var j = null;
						for(j in intersection){
							if(intersection[j]==resultIds[i]){
								tmp.push(intersection[j]);
							}
						}
					}
					intersection = tmp;
				}
			}
			
		}
		var selectedNum = 0;
		if(intersection.length==0){
			$("div.hit_block").show();
		}else{
			var ind = null;
			for(ind in intersection){
				if($("div#lId_"+intersection[ind]).is(":hidden")){
					selectedNum++;
					$("div#lId_"+intersection[ind]).show();
				}
			}
		}
		document.getElementById("selecteNum").innerHTML=""+selectedNum;
	}
	
	var resultSet = new Array();
	
	$(function(){
		<%for(int i=0;i<facetIds.size();i++){
			String id = (String)facetIds.get(i);
		%>
		$("#facetTr_<%=id%>").ligerTree({
			data:<%=map.get(id).toString()%>,
			onCheck: function (note, checked){
				this.dealSelect(<%=i%>);
			},
			onAfterAppend: function () {this.collapseAll();}
			
		});
		resultSet[<%=i%>]="";
		<%}%>
	});
	
	function getId(text){
		var ret = null;
		if(text.indexOf("id=")>-1){
		}else{
			ret = getIdsInText(text);
		}
		return ret;
	}
	
	function getIdsInText(nodeText){
		var startIndex = nodeText.indexOf(">");
		var endIndex = nodeText.lastIndexOf("<");
		return nodeText.substring(startIndex+1, endIndex);
	}
	
	function showAllResult(){
		$("div.hit_block").hide();
		var resultIndex = null;
		for(resultIndex in resultSet){
			var resultIds = new Array();
			resultIds = (resultSet[resultIndex]).split(";");
			for(var i=0;i<resultIds.length;i++ ){
				$("div#lId_"+resultIds[i]).show();
			}
		}
	}
	
	function doUpdate(){
		var dataToUpdate = "";
		var resultIndex = null;
		for(resultIndex in resultSet){
			dataToUpdate += (resultSet[resultIndex])+";";
		}
		dataToUpdate = dataToUpdate.substring(0, dataToUpdate.length-1);
		document.getElementById("updateData").value=dataToUpdate;
		document.getElementById("updateForm").submit();
	}
	
	function hideOrShow(id){
		if($("div#"+id).is(":hidden")){
			$("div#"+id).show("slow");
		}else{
			$("div#"+id).hide("slow");
		}
	}
	
	function showAllSearchedResult(){
		$("div.hit_block").show();
	}
	
	</script>


</head>
<body>


	<div class="header">
		<div class="header_content">
			<div class="search">
				<form id="form1" method="post" action="SearchServlet">
					<div class="header_input">
						<input name="formTag" type="hidden" value="search"></input>
						<input name="q" type="text" class="header_input_txt" id="textfield"
							size="30" maxlength="50" />
						<button id="search" class="search_btn"
							onmouseout="this.className='search_btn'"
							onmouseover="this.className='search_btn search_btn_hover'" />
						<span class="search_png">Search</span>
					</div>
				</form>
			</div>
		</div>
	</div>
	<div class="maincontent">
		<div class="maincolmask maincol">
			<div class="col1">
					<div id="tabTopic" >
					<%for(int i=0;i<facetIds.size();i++){ 
						String title = (String)titles.get(i);
						String id = (String)facetIds.get(i);
					%>
						<div class="facetBar">
							<div onclick="hideOrShow('facetDiv_<%=id%>')" class="facetTitle">
							<%=title %>
							</div>
							<div class="facetTree">
								<div id="facetDiv_<%=id%>" class="facetDiv" >
									<ul id="facetTr_<%=id%>"></ul>
								</div>
							</div>
						</div>
					<%} %>
					</div>

					<div class="con">
					<!-- 
						<button id="showAll"
							onclick="showAllResult()" >show all results</button>
					-->
						<!-- id="btnType" -->
						<button id="updateButton" 
							onclick="doUpdate()">Update</button>
						
						<form id="updateForm" method="post" action="SearchServlet">
							<div class="functional_forms">
								<input name="formTag" type="hidden" value="update"></input>
								<input name="updateData" id="updateData" type="hidden"></input>
							</div>
						</form>
						
					</div>

			</div>

			<div class="col2">
				 
				<div onclick="showAllSearchedResult()" id="resultCount" ><span id="selecteNum" >0</span> results selected of <%=results.size() %> results</div>
				
				<div id="result" style="height: 700px;  overflow-y:auto;">
					<%for(int i=0;i<results.size();i++){
						CodeResult cr = (CodeResult)results.get(i);
					%>
						<div class="hit_block" id="lId_<%=cr.getId()%>" >
						<td class="postcell" >
								<div class="content">
										<h3 class="r">
											<input type="checkbox" /> <a href="javascript:void(0)"
												class="name" onclick="showStackoverflowPage('<%=cr.getId()%>');"><%=cr.getTitle()%></a>
										</h3>
									<div class ="post-text itemprop="text">
										<%=cr.getBody() %>
									</div>
									<div class="s">	
										<a class="post-tag"><%=cr.getTag().replace("<", " ").replace(">", " ")%></a>
									</div>
								</div>
						</td>							
						</div>
						
<%-- 							<div class="hit_title">
								<a><%=cr.getTitle() %></a>
							</div>
							<div class="hit_meta">
								<div class="meta_tag">Project:</div>
 								<div class="meta_val"><%=cr.getProjectName() %></div>
								<div class="meta_tag">Tag:</div>
 									<div class="meta_val"><%=cr.getTag() %></div>
 							</div>
							<div class="hit_codes">
								<table width="inherit" cellspacing="0" cellpadding="0">
									<tbody>
									<%	List<String> lines = cr.getCodes();
										int n = lines.size();
										int start = cr.getStartLineNum();
										for(int j=0;j<n;j++){%>
										<tr>
											<td class="line_number"><%=start+j %></td>
											<td class="code_line">
												<pre><%=cr.getBody()%></pre>
											</td>
										</tr>
									<%	}%>
									</tbody>
								</table>
							</div> --%>
					<%} %>
				</div>

			</div>
			<!-- End of col2 -->

		</div>
	</div>
	<div class="footer">
		<div class="copyright">
			Copyright © 2015 <a href="http://www.se.fudan.edu.cn">SE Lab of
				Fudan University</a>. All rights reserved.
			<p>Last Update: Apr 20th, 2012</p>
		</div>
	</div>
	
	<script type="text/javascript">
	
	</script>
</body>
</html>
