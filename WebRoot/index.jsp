<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ page isELIgnored="false" %>
<%

%>
<html>
<head>
    <title>Multi-Facet Web Service</title>
    <link rel="shortcut icon" href="favicon.ico" />
    <link href="css/style.css" rel="stylesheet" type="text/css" />
</head>
<body >
<div class="index">
    <div class="index_lg">
        <a title="Multi-Facet Feature Location"><img class="indexlogo" src="images/logo.png" />
        </a>
    </div>
    <div class="index_form">
        <form id="form1" method="post" action="SearchServlet" onsubmit = "return checkUser();">
            <div class="header_input">
                <input name="userid" type="text" class="header_input_txt" id="useridfield"
                       size="30" maxlength="1000" placeholder="please input your user id"/>
                <button id="hidden_button" class="hidden_btn" disabled="true"/>
            </div>
            <div class="header_input">
                <input name="formTag" type="hidden" value="search"></input>
                <input name="q" type="text" class="header_input_txt" id="textfield"
                       size="30" maxlength="1000" />
                <button id="search" class="search_btn"
                        onmouseout="this.className='search_btn'"
                        onmouseover="this.className='search_btn search_btn_hover'" />
                <span class="search_png">Search</span>
            </div>
        </form>
    </div>
</div>
<script type="text/javascript">
    function checkUser(){
        var result = document.getElementById("useridfield").value;
        if(result == ""){
            alert("The user id cannot be null.");
            return false;
        }else{
            return true;
        }
    }
</script>
</body>
</html>