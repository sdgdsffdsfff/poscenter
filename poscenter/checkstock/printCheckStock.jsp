<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false"%>
<%@ include file="/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>打印盘点表</title>
<link href="<%=request.getContextPath()%>/css/global.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script>
<script type="text/javascript">
$(function(){
	window.print();
});
</script>
</head>
<body>
<input type="hidden" id="initPath" value="<%=request.getContextPath()%>" />
<table width="900" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<th colspan="3"><span style="font-size: 18px;">盘 点 表</span></th>
	</tr>
	<tr>
		<td></td>
		<td width="150" align="right">盘点日期：</td>
		<td width="120" align="right"><fmt:formatDate value="${checkStock.checkTime}" pattern="yyyy年MM月dd日" /></td>
	</tr>
</table>
<table width="900" border="1" cellpadding="0" cellspacing="0">
	<tr>
		<th width="130">商品条形码</th>
		<th>商品名称</th>
		<auth:authorize ifGranted="showComputerStock">
		<th>电脑库存</th>
		</auth:authorize>
		<th>初盘数量</th>
		<th>复盘数量</th>
		<th>备注</th>
	</tr>
	<c:forEach var="data" items="${checkStockItemList}">
		<tr>
			<td align="center">${data[0] }</td>
			<td>${data[1] }</td>
			<auth:authorize ifGranted="showComputerStock">
			<td>${data[2] }</td>
			</auth:authorize>
			<td>
				<c:if test="${data[3] != -1}">${data[3]}</c:if>
			</td>
			<td>${data[4] }</td>
			<td>${data[6] }</td>
		</tr>
	</c:forEach>
</table>
<table width="900" border="0" cellpadding="0" cellspacing="0" style="margin-top: 5px;">
	<tr>
		<td>负责人：${checkStock.charger}</td>
	</tr>
</table>
</body>
</html>