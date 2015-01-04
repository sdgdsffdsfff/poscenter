<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="s" uri="/struts-tags"%> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>商品列表</title>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/skin/css/admin.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/skin/css/theme.css" />

<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script>
<script type="text/javascript">
var product={};

//跳转到指定页面
product.goPage = function(pageNum,pageCount){
	var url = $('#initPath').val()+'/pos/shopStock!productList.do?d=1';
	if(pageNum != null && pageCount != null){
		url += '&productPage.pageNum='+pageNum;
	}
	if(pageCount != null){
		url += '&productPage.pageCount='+pageCount;
	}
	window.location.href = url;
}
</script>
</head>
<body>
<input type="hidden" id="initPath" value="<%=request.getContextPath()%>" />
<div class="box-positon">
	<div class="rpos">当前位置: 店面库存 - 商品列表</div>
	<div class="ropt">
	</div>
	<div class="clear"></div>
</div>

<div id="bodyBox" class="body-box">
<form style="padding-top:5px;" action="<%=request.getContextPath()%>/pos/shopStock!productList.do" method="post">
<div>
	条形码:<input type="text" name="barCode" value="${barCode}" />&nbsp;&nbsp;
	商品名称:<input type="text" name="productName" value="${productName}" />&nbsp;&nbsp;
	商品分类:<input type="text" name="goodsClassName" value="${goodsClassName}" />&nbsp;&nbsp;
	<input type="submit" value="查询" class="query" />
</div>
</form>

<table width="100%" cellspacing="1" cellpadding="0" border="0" style="" class="pn-ltable">
<thead class="pn-lthead">
<tr>
	<th>条形码</th>
	<th>商品名称</th>
	<th>商品分类</th>
	<th>操作</th>
</tr>
</thead>
<tbody class="pn-ltbody">
<s:iterator value="productPage.list" var="li">
<tr>
	<td align="center"><s:property value="#li.barCode"/></td>
	<td><s:property value="#li.name"/></td>
	<td><s:property value="#li.goodsClass.name"/></td>
	<td align="center">
		<a href="<%=request.getContextPath()%>/pos/shopStock!shopStockList.do?productId=<s:property value="#li.id"/>">查看</a>
	</td>
</tr>
</s:iterator>
</tbody>
</table>

<table width="100%" style="font-size:13px;">
	<tbody>
	<tr>
		<td align="center" class="pn-sp">
		<c:if var="isFirstPage" test="${productPage.pageNum<=1}">
		首页 &nbsp;&nbsp;&nbsp;上一页&nbsp;&nbsp;&nbsp;
		</c:if>
		<c:if test="${!isFirstPage}">
		<a href="javascript:;" onclick="product.goPage(1,${productPage.pageCount});">首页</a>&nbsp;&nbsp;&nbsp;
		<a href="javascript:;" onclick="product.goPage(${productPage.pageNum-1},${productPage.pageCount});">上一页</a>
		</c:if>
		&nbsp;&nbsp;&nbsp;
		<c:if var="isLastPage" test="${productPage.pageNum>=productPage.totalPages}">
		下一页&nbsp;&nbsp;&nbsp;尾页&nbsp;&nbsp;&nbsp;
		</c:if>
		<c:if test="${!isLastPage}">
		<a href="javascript:;" onclick="product.goPage(${productPage.pageNum+1},${productPage.pageCount});">下一页</a>&nbsp;&nbsp;&nbsp;
		<a href="javascript:;" onclick="product.goPage(${productPage.totalPages},${productPage.pageCount});">尾页</a>
		</c:if>
		&nbsp;&nbsp;&nbsp;&nbsp;
		第${productPage.pageNum}/${productPage.totalPages}页,共${productPage.totalRecords}条记录
	</td>
	</tr>
	</tbody>
</table>
</div>
</body>
</html>