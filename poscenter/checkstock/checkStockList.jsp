<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false"%>
<%@ include file="/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>盘点列表</title>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/skin/css/admin.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/skin/css/theme.css" />

<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script>
<script type="text/javascript">
var checkStock={};

//跳转到指定页面
checkStock.goPage = function(pageNum,pageCount){
	var url = $('#initPath').val()+'/pos/checkStock!checkStockList.do?d=1';
	if(pageNum != null && pageCount != null){
		url += '&page.pageNum='+pageNum;
	}
	if(pageCount != null){
		url += '&page.pageCount='+pageCount;
	}
	window.location.href = url;
}

//新增盘点
checkStock.addCheckStock = function(){
	var url = $('#initPath').val()+'/pos/checkStock!createEmptyCheckStock.do';
	window.location.href = url;
}
</script>
</head>
<body>
<input type="hidden" id="initPath" value="<%=request.getContextPath()%>" />
<div class="box-positon">
	<div class="rpos">当前位置: 盘点管理 - 列表</div>
	<div class="ropt">
		<auth:authorize ifGranted="addCheckStockTask">
		<input type="button" onclick="checkStock.addCheckStock();" class="" value="新建盘点任务" /> &nbsp;
		</auth:authorize> 
	</div>
	<div class="clear"></div>
</div>

<div id="bodyBox" class="body-box">

<table id="goodsList" width="100%" cellspacing="1" cellpadding="0" border="0" style="" class="pn-ltable">
<thead class="pn-lthead">
<tr>
	<th>盘点时间</th>
	<th>负责人</th>
	<th>状态</th>
	<th>操作</th>
</tr>
</thead>
<tbody class="pn-ltbody">
<c:forEach var="checkStock" items="${page.list}">
<tr>
	<td align="center"><fmt:formatDate value="${checkStock.checkTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
	<td align="center">${checkStock.charger }</td>
	<td align="center">
		<c:choose>
			<c:when test="${checkStock.useStatus == 0}">未提交</c:when>
			<c:when test="${checkStock.useStatus == 1}">已提交</c:when>
		</c:choose>
	</td>
	<td align="center">
		<a href="<%=request.getContextPath()%>/pos/checkStockItem!checkStockItemList.do?checkStockId=${checkStock.id }">盘点条目</a>
	</td>
</tr>
</c:forEach>
</tbody>
</table>

<table id="dtPageTable" width="100%" style="font-size:13px;">
	<tbody>
	<tr>
		<td align="center" class="pn-sp">
		<c:if var="isFirstPage" test="${page.pageNum<=1}">
		首页 &nbsp;&nbsp;&nbsp;上一页&nbsp;&nbsp;&nbsp;
		</c:if>
		<c:if test="${!isFirstPage}">
		<a href="javascript:;" onclick="checkStock.goPage(1,${page.pageCount});">首页</a>&nbsp;&nbsp;&nbsp;
		<a href="javascript:;" onclick="checkStock.goPage(${page.pageNum-1},${page.pageCount});">上一页</a>
		</c:if>
		&nbsp;&nbsp;&nbsp;
		<c:if var="isLastPage" test="${page.pageNum>=page.totalPages}">
		下一页&nbsp;&nbsp;&nbsp;尾页&nbsp;&nbsp;&nbsp;
		</c:if>
		<c:if test="${!isLastPage}">
		<a href="javascript:;" onclick="checkStock.goPage(${page.pageNum+1},${page.pageCount});">下一页</a>&nbsp;&nbsp;&nbsp;
		<a href="javascript:;" onclick="checkStock.goPage(${page.totalPages},${page.pageCount});">尾页</a>
		</c:if>
		&nbsp;&nbsp;&nbsp;&nbsp;
		第${page.pageNum}/${page.totalPages}页,共${page.totalRecords}条记录
	</td>
	</tr>
	</tbody>
</table>
</div>
</body>
</html>