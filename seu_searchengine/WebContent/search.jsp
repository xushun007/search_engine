<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">


<script type="text/javascript">

	function searchNews() {
		var searchContent = document.getElementById("searchContent").value;
		
		window.location.href = "search.action?searchContent="+searchContent
	}
	
	function whenDown(event) {
		if(event.keyCode == 13) {
			searchNews();
		}
	}

</script>

<title>东大搜索</title>
</head>
<body>

<h1> 东南大学新闻搜索   </h1>

	<!-- 
	<div id="search" >
		<span class="black14b" >搜索新闻</span>
		<input id="searchContent" type="text" name="searchContent" value="${searchContent}" onkeypress="whenDown(event)">
		<input type = "button"  value = "搜索" onclick="searchNews()" >
	</div>
	 -->
	
	<span class="black14b" >搜索新闻</span>
	<form action="search.action" method = "post">
		<input id="searchContent" style="width:380px" type="text" name="searchContent" value="${searchContent}" >
		<input type = "submit"  value = "搜索"  >
	</form>
	

	<div class="result_kuang">		
		<c:forEach items = "${resultList}" var="record">
			<div>
				<div ><a href= "${record.url}"> ${record.title} </a> </div>
				<div > ${record.abstruct}</div>
				<div> <a href= "snapshot//${record.webCachePath}.html"> 网页快照 </a> </div>
			</div>
			<br/> <br/>
		</c:forEach>						
	</div>

</body>
</html>