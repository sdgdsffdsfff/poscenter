<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title>失败页面</title>
</head>
<body>
	<c:choose>
		<c:when test="${message!=null}">
			<h3><font color="red">${message}</font></h3>
		</c:when>
		<c:otherwise>
		<h3><font color="red">操作失败!</font></h3>
		</c:otherwise>
	</c:choose>
</body>
</html>