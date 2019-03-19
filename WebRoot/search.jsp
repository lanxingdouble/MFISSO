<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%@ page language="java" import="cn.fdse.codeSearch.openInterface.module.ClassificationList" %>
<%@ page language="java" import="cn.fdse.codeSearch.openInterface.searchResult.CodeResult" %>

<%
    List<?> facetIds = (List<?>) (request.getSession().getAttribute("facetIds"));
    List<?> titles = (List<?>) (request.getSession().getAttribute("titles"));
    Map<?, ?> map = (Map<?, ?>) (request.getSession().getAttribute("map"));
    List<?> results = (List<?>) (request.getSession().getAttribute("results"));
    String sysPath = (String) (request.getSession().getAttribute("path"));
    String keyword = (String) (request.getSession().getAttribute("Keyword"));


%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html;charset=gb2312"/>
    <title>Multi-Facet Web Service</title>
    <link rel="shortcut icon" href="favicon.ico"/>
    <link href="css/style.css" rel="stylesheet" type="text/css"/>
    <link href="css/treeStyle.css" rel="stylesheet" type="text/css"/>
    <link href="css/codesStyle.css" rel="stylesheet" type="text/css"/>
    <link href="css/popwindow.css" rel="stylesheet" type="text/css"/>

    <style>
        div.l-body span {
            float: left;
        }
    </style>
    <script src="LigerUiLib/jquery/jquery-1.11.1.min.js" type="text/javascript"></script>
    <link href="LigerUiLib/ligerUI/skins/Aqua/css/ligerui-tree.css" rel="stylesheet" type="text/css"/>
    <script src="LigerUiLib/ligerUI/js/core/base.js" type="text/javascript"></script>
    <script src="LigerUiLib/ligerUI/js/plugins/ligerTree.js" type="text/javascript"></script>
    <script type="text/javascript">
        javascript:window.history.forward(1);

        var updateButton = "";
        var facetId;
        var facetItem;
        window.onbeforeunload = function (event) {
            var content = document.getElementById("idtextarea").value;
            if (content != "")
                process("Suggestion:" + content);
            process("Window Close" + updateButton);
            upateButton = "";
        };

        $.ligerui.controls.Tree.prototype.dealSelect = function (resultSetIndex, note) {//sai xuan huo qu shu ju yuan su
            $("div.hit_block").css('display', 'none');
            var data = this.getChecked();

            getFacetAndItem(resultSetIndex, note.data.text);
            var str = "";
            for (var i = 0; i < data.length; i++) {
                var resultId = getId(data[i]['data']['text']);
                if (resultId) {
                    str += resultId + ";";
//				$("div#lId_"+resultId).show();
                }
            }
            resultSet[resultSetIndex] = str.substring(0, str.length - 1);


            showInterSection();
        };

        function getFacetAndItem(facetId, element) {

            var id = element.indexOf("<span");
            process("Facet:" + facetId + " FacetItem:" + element.substring(1, id));
        }

        function showInterSection() {//chu li jiao ji
            var intersection = new Array();
            var first = true;
            var resultIndex = null;
            for (resultIndex in resultSet) {
                if (resultSet[resultIndex] == "") {

                } else {
                    var resultIds = (resultSet[resultIndex]).split(";");
                    if (first) {
                        for (var i = 0; resultIds.length > i; i++) {
                            intersection.push(resultIds[i]);
                        }
                        first = false;
                    } else {
                        var tmp = new Array();
                        for (var i = 0; resultIds.length > i; i++) {
                            var j = null;
                            for (j in intersection) {
                                if (intersection[j] == resultIds[i]) {
                                    tmp.push(intersection[j]);
                                }
                            }
                        }
                        intersection = tmp;
                    }
                }

            }
            var selectedNum = 0;
            if (intersection.length == 0) {
                $("div.hit_block").css('display', 'block');
            } else {
                var ind = null;
                for (ind in intersection) {
                    //if($("div#lId_"+intersection[ind]).is(":hidden")){
                    selectedNum++;
                    $("div#lId_" + intersection[ind]).css('display', 'block');
                    //}
                }
            }

            document.getElementById("selecteNum").innerHTML = "" + selectedNum;
        }

        var resultSet = new Array();
        $(function () {
            <%for(int i=0; facetIds.size() > i;i++){
                String id = (String)facetIds.get(i);
            %>
            $("#facetTr_<%=id%>").ligerTree({
                data:<%=map.get(id).toString()%>,
                delay: true,
                onCheck: function (note, checked) {
                    this.dealSelect(<%=i%>, note);
                },

                onAfterAppend: function () {
                    this.collapseAll();
                },

            });
            resultSet[<%=i%>] = "";
            <%}%>
            $("#result").find("div.hit_block").each(function () {
                var hitBlockId = $(this).attr("id");
                $(this).children("div.content").children("div").find("code").each(function (i) {
                    var preId = "code" + i + hitBlockId;
                    var buttonId = "codeButton" + i + hitBlockId;

                    $(this).attr("id", preId);
                    $(this).before("<button id=\"" + buttonId + "\" class=\"codeButton\" onclick=\"showCode('" + preId + "','" + buttonId + "')\">code...</button>");
                    $(this).click(function () {
                        $("#" + preId).hide();
                        $("#" + buttonId).show();
                    });
                    hideCode(preId, buttonId);
                });
                $(this).children("div.content").children("div").find("img").each(function (i) {
                    var imgId = "img" + i + hitBlockId;
                    var buttonId = "imgButton" + i + hitBlockId;

                    $(this).attr("id", imgId);
                    $(this).before("<button id=\"" + buttonId + "\" class=\"codeButton\" onclick=\"showCode('" + imgId + "','" + buttonId + "')\">image</button>");
                    $(this).click(function () {
                        $("#" + imgId).hide();
                        $("#" + buttonId).show();
                    });
                    hideCode(imgId, buttonId);
                });
            });

        });

        function hideCode(preId, buttonId) {
            $("#" + preId).hide();
            $("#" + buttonId).show();
        }

        function showCode(preId, buttonId) {
            $("#" + preId).show();
            $("#" + buttonId).hide();
        }

        function getId(text) {
            var ret = null;

            ret = getIdsInText(text);
            return ret;
        }

        function getIdsInText(nodeText) {
            var startIndex = nodeText.indexOf(">");
            var endIndex = nodeText.lastIndexOf("<");
            return nodeText.substring(startIndex + 1, endIndex);
        }

        function showAllResult() {
            $("div.hit_block").css('display', 'none');
            var resultIndex = null;
            for (resultIndex in resultSet) {
                var resultIds = new Array();
                resultIds = (resultSet[resultIndex]).split(";");
                for (var i = 0; i < resultIds.length; i++) {
                    $("div#lId_" + resultIds[i]).css('display', 'block');
                }
            }
        }

        function doUpdate() {
            var dataToUpdate = "";
            var intersection = new Array();
            var first = true;
            var resultIndex = null;
            for (resultIndex in resultSet) {
                if (resultSet[resultIndex] == "") {

                } else {
                    var resultIds = (resultSet[resultIndex]).split(";");
                    if (first) {
                        for (var i = 0; i < resultIds.length; i++) {
                            intersection.push(resultIds[i]);
                        }
                        first = false;
                    } else {
                        var tmp = new Array();
                        for (var i = 0; i < resultIds.length; i++) {
                            var j = null;
                            for (j in intersection) {
                                if (intersection[j] == resultIds[i]) {
                                    tmp.push(intersection[j]);
                                }
                            }
                        }
                        intersection = tmp;
                    }
                }

            }
            for (ind in intersection) {
                dataToUpdate += intersection[ind] + ";";
            }
            process("Button:update");
            updateButton = "updateButton";
            document.getElementById("updateData").value = dataToUpdate;
            document.getElementById("updateForm").submit();

        }

        function hideOrShow(id) {
            if ($("div#" + id).is(":hidden")) {
                $("div#" + id).css('display', 'block');
            } else {
                $("div#" + id).css('display', 'none');
            }
        }

        function showAllSearchedResult() {
            $("div.hit_block").css('display', 'block');
        }

        function showPageResult(pageNumber) {
            $("div.hit_block").css('display', 'none');
            showPage = pageNumber;
            $("div.hit_block").css('display', 'block');

        }

        function showStackoverflowPage(titleId, title) {
            window.open("http://stackoverflow.com/questions/" + titleId);
            process("Click:" + title + "(http://stackoverflow.com/questions/" + titleId + ")");
        }

        function Copy(titleId, title) {
            process("Copy:" + title + "(http://stackoverflow.com/questions/" + titleId + ")");
        }

        function openTextArea() {
            document.getElementById("divtextarea").style.display = "block";
        }

        function closeTextArea() {
            var content = document.getElementById("idtextarea").value;
            if (content != "")
                process("Suggestion:" + content);
            document.getElementById("idtextarea").value = "";
            document.getElementById("divtextarea").style.display = "none";
        }

        function process(processData) {
            $.ajax({
                    type: "post",
                    url: "ProcessServlet",
                    dataType: "json",
                    data: {
                        text: processData

                    }
                }
            )
        }

    </script>

</head>
<body>

<div class="header">
    <div class="header_content">
        <div class="search">
            <form id="form1" method="post" action="SearchServlet" style="width:460px; display:inline-block;">
                <div class="header_input">
                    <input name="formTag" type="hidden" value="search"></input>
                    <input name="q" type="text" class="header_input_txt" id="textfield"
                           size="30" maxlength="1000" value="<%=keyword%>"/>
                    <button id="search" class="search_btn"
                            onmouseout="this.className='search_btn'"
                            onmouseover="this.className='search_btn search_btn_hover'"/>
                    <span class="search_png">Search</span>
                </div>
            </form>
            <div class="sugg">
                <button class="suggestion_btn" id="opentextarea"
                        onclick="openTextArea()">Suggest
                </button>
            </div>
        </div>

    </div>
</div>

<div class="suggestion" style="display:none" id="divtextarea">
    <div class="textareadiv">
        <textarea rows="4" cols="80" id="idtextarea"></textarea>
    </div>
    <div class="finishdiv">
        <button class="finish_btn" id="closetextarea"
                onclick="closeTextArea()">Finish
        </button>
    </div>

</div>

<div class="maincontent">
    <div class="maincolmask maincol">
        <div class="col1">
            <div id="tabTopic">
                <%
                    for (int i = 0; i < facetIds.size(); i++) {
                        String title = (String) titles.get(i);
                        String id = (String) facetIds.get(i);
                %>
                <div class="facetBar">
                    <div onclick="hideOrShow('facetDiv_<%=id%>')" class="facetTitle">
                        <%=title %>
                    </div>
                    <div class="facetTree">
                        <div id="facetDiv_<%=id%>" class="facetDiv">
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
                        onclick="doUpdate()">Update
                </button>

                <form id="updateForm" method="post" action="SearchServlet">
                    <div class="functional_forms">
                        <input name="formTag" type="hidden" value="update"></input>
                        <input name="updateData" id="updateData" type="hidden"></input>
                    </div>
                </form>

            </div>

        </div>

        <div class="col2">

            <div onclick="showAllSearchedResult()" id="resultCount"><span id="selecteNum">0</span> results selected
                of <%=results.size() %> results
            </div>

            <div id="result" style="height: 1800px;  overflow-y:auto;">
                <%
                    for (int i = 0; i < results.size(); i++) {
                        CodeResult cr = (CodeResult) results.get(i);

                %>
                <div class="hit_block" id="lId_<%=cr.getId()%>"
                     onBeforeCopy="Copy('<%=cr.getId()%>','<%=cr.getTitle().replace("<font color='red'>","").replace("</font>","").replace("'","")%>');">

                    <div class="content">
                        <h3 class="r">
                            <input type="checkbox"/> <a href="javascript:void(0)"
                                                        class="name"
                                                        onclick="showStackoverflowPage('<%=cr.getId()%>','<%=cr.getTitle().replace("<font color='red'>","").replace("</font>","").replace("'","")%>');"><%=cr.getTitle()%>
                        </a>
                        </h3>
                        <div class="post-text itemprop=" text">
                        <%=cr.getBody() %>
                    </div>
                    <div class="s">
                        <a class="post-tag"><%=cr.getTag()%>
                        </a>
                    </div>
                    <div class="answer_area">
                        <h2 class="answer_title">Answers</h2>
                        <% if (cr.getAnswerList().size() == 0) { %>
                        <div class="answer_textarea">
                            No one have ever answered.
                        </div>
                        <%} %>
                        <% for (int j = 0; j < cr.getAnswerList().size(); j++) { %>
                        <div class="answer_textarea">
                            <%=cr.getAnswerList().get(j).getBody() %>
                        </div>
                        <%} %>
                    </div>
                </div>

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
            <%}%>
        </div>
        <div>
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
