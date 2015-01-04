<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="s" uri="/struts-tags"%> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>收货单条目</title>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/skin/css/admin.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/skin/css/theme.css" />

<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script>
<script type="text/javascript">
var receiveOrderItem = {};

//返回列表
receiveOrderItem.backList = function(){
	window.location.href = $('#initPath').val()+'/pos/receiveOrder!getReceiveOrderList.do';
}

//跳转到指定页面
receiveOrderItem.goPage = function(pageNum,pageCount){
	var url = $('#initPath').val()+'/pos/receiveOrderItem!getReceiveOrderItemList.do?roi.orderId='+$('#receiveOrderId').val();
	if(pageNum != null && pageCount != null){
		url += '&page.pageNum='+pageNum;
	}
	if(pageCount != null){
		url += '&page.pageCount='+pageCount;
	}
	window.location.href = url;
}
</script>
</head>
<body>
<input type="hidden" id="initPath" value="<%=request.getContextPath()%>" />
<div class="box-positon">
	<div class="rpos">当前位置: 收货单管理 - 收货单条目</div>
	<div class="ropt">
	  <input type="button" class="return-button" value="返回" onclick="receiveOrderItem.backList();" />
	</div>
	<div class="clear"></div>
</div>

<div id="bodyBox" class="body-box">
<form style="padding-top:5px;" action="<%=request.getContextPath()%>/pos/receiveOrderItem!getReceiveOrderItemList.do" method="post">
<input type="hidden" id="receiveOrderId" name="roi.orderId" value="${roi.orderId }" />
<div>
	商品编码:<input type="text" name="barCode" value="${barCode}" />&nbsp;&nbsp;
	商品名称:<input type="text" name="productName" value="${productName}" />&nbsp;&nbsp;
	供应商:<input type="text" name="supplierName" value="${supplierName}" />&nbsp;&nbsp;
	<input type="submit" value="查询" class="query" />
</div>
</form>

<table width="100%" cellspacing="1" cellpadding="0" border="0" style="" class="pn-ltable">
<thead class="pn-lthead">
<tr>
	<th>商品编码</th>
	<th>商品名称</th>
	<th>供应商</th>
	<th>采购数量</th>
	<th>实收数量</th>
</tr>
</thead>
<tbody class="pn-ltbody">
<c:forEach var="item" items="${page.list}" varStatus="status">
<tr>
	<td align="center">${item.productCode }</td>
	<td align="center">${item.productName }</td>
	<td align="center">${item.supplierName }</td>
	<td align="center">${item.sendCount }</td>
	<td align="center">${item.receiveCount }</td>
</tr>
</c:forEach>
</tbody>
</table>

<c:if test="${page.totalPages > 1}">
<table width="100%" style="font-size:13px;">
	<tbody>
	<tr>
		<td align="center" class="pn-sp">
		<c:if var="isFirstPage" test="${page.pageNum<=1}">
		首页 &nbsp;&nbsp;&nbsp;上一页&nbsp;&nbsp;&nbsp;
		</c:if>
		<c:if test="${!isFirstPage}">
		<a href="javascript:;" onclick="receiveOrderItem.goPage(1,${page.pageCount});">首页</a>&nbsp;&nbsp;&nbsp;
		<a href="javascript:;" onclick="receiveOrderItem.goPage(${page.pageNum-1},${page.pageCount});">上一页</a>
		</c:if>
		&nbsp;&nbsp;&nbsp;
		<c:if var="isLastPage" test="${page.pageNum>=page.totalPages}">
		下一页&nbsp;&nbsp;&nbsp;尾页&nbsp;&nbsp;&nbsp;
		</c:if>
		<c:if test="${!isLastPage}">
		<a href="javascript:;" onclick="receiveOrderItem.goPage(${page.pageNum+1},${page.pageCount});">下一页</a>&nbsp;&nbsp;&nbsp;
		<a href="javascript:;" onclick="receiveOrderItem.goPage(${page.totalPages},${page.pageCount});">尾页</a>
		</c:if>
		&nbsp;&nbsp;&nbsp;&nbsp;
		第${page.pageNum}/${page.totalPages}页,共${page.totalRecords}条记录
	</td>
	</tr>
	</tbody>
</table>
</c:if>
</div>
</body>
</html>