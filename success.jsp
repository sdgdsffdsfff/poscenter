<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ page isELIgnored="false"%>
<%@page import="java.util.*"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
String path = request.getContextPath();
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title>成功页面</title>
</head>
<body>
	<%
		List<String> list = (List<String>)request.getAttribute("list");
		if(list!=null && list.size()>0){
			for(int i=0;i<list.size();i++){
				out.println("<p><font color='red'>" + list.get(i)+"</font></p>");
			}
		}else{
	%>
	<c:choose>
		<c:when test="${message!=null}">
			<h3><font color="green">${message}</font></h3>
		</c:when>
		<c:otherwise>
			<h3><font color="green">操作成功!</font></h3>
		</c:otherwise>
	</c:choose>
	<%
		}
	%>
</body>
</html>
