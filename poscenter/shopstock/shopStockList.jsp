<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>店面库存列表</title>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/skin/css/admin.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/skin/css/theme.css" />

<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script>
<script type="text/javascript">
var shopStock={};

//跳转到指定页面
shopStock.goPage = function(pageNum,pageCount){
	var pageNum = '<input type="hidden" name="page.pageNum" value="'+pageNum+'" />';
	var pageCount = '<input type="hidden" name="page.pageCount" value="'+pageCount+'" />';
	$('#shopStockQueryForm').append(pageNum);
	$('#shopStockQueryForm').append(pageCount);
	$('#shopStockQueryForm').submit();
}

//返回列表
shopStock.backList = function(){
	window.location.href = $('#initPath').val()+'/pos/shopStock!productList.do';
}
</script>
</head>
<body>
<input type="hidden" id="initPath" value="<%=request.getContextPath()%>" />
<div class="box-positon">
	<div class="rpos">当前位置: 店面库存 - <span style="font-weight:bold;">${product.name }</span></div>
	<div class="ropt">
		<input type="button" class="return-button" value="返回" onclick="shopStock.backList();" />&nbsp;&nbsp;
	</div>
	<div class="clear"></div>
</div>

<div id="bodyBox" class="body-box">
<form id="shopStockQueryForm" style="padding-top:5px;" action="<%=request.getContextPath()%>/pos/shopStock!shopStockList.do" method="post">
<input type="hidden" name="productId" value="${productId}" />
<div>
	店面编号:<input type="text" name="shopCode" value="${shopCode}" />&nbsp;&nbsp;
	店面名称:<input type="text" name="shopName" value="${shopName}" />&nbsp;&nbsp;
	<input type="submit" value="查询" class="query" />
</div>
</form>

<table width="100%" cellspacing="1" cellpadding="0" border="0" style="" class="pn-ltable">
<thead class="pn-lthead">
<tr>
	<th>店面编号</th>
	<th>店面名称</th>
	<th>库存</th>
	<th>同步时间</th>
</tr>
</thead>
<tbody class="pn-ltbody">
<c:forEach var="shopStock" items="${page.list}">
<tr>
	<td align="center">${shopStock.shop.code }</td>
	<td>${shopStock.shop.name }</td>
	<td align="right">${shopStock.stock }</td>
	<td align="center"><fmt:formatDate value="${shopStock.createTime }" pattern="yyyy-MM-dd HH:mm:ss" /></td>
</tr>
</c:forEach>
</tbody>
</table>

<table width="100%" style="font-size:13px;">
	<tbody>
	<tr>
		<td align="center" class="pn-sp">
		<c:if var="isFirstPage" test="${page.pageNum<=1}">
		首页 &nbsp;&nbsp;&nbsp;上一页&nbsp;&nbsp;&nbsp;
		</c:if>
		<c:if test="${!isFirstPage}">
		<a href="javascript:;" onclick="shopStock.goPage(1,${page.pageCount});">首页</a>&nbsp;&nbsp;&nbsp;
		<a href="javascript:;" onclick="shopStock.goPage(${page.pageNum-1},${page.pageCount});">上一页</a>
		</c:if>
		&nbsp;&nbsp;&nbsp;
		<c:if var="isLastPage" test="${page.pageNum>=page.totalPages}">
		下一页&nbsp;&nbsp;&nbsp;尾页&nbsp;&nbsp;&nbsp;
		</c:if>
		<c:if test="${!isLastPage}">
		<a href="javascript:;" onclick="shopStock.goPage(${page.pageNum+1},${page.pageCount});">下一页</a>&nbsp;&nbsp;&nbsp;
		<a href="javascript:;" onclick="shopStock.goPage(${page.totalPages},${page.pageCount});">尾页</a>
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