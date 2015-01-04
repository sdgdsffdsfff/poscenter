<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>租赁订单列表</title>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/skin/css/admin.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/skin/css/theme.css" />

<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script>
<script type="text/javascript">
var orderList={};

//跳转到指定页面
orderList.goPage = function(pageNum,pageCount){
	var pageNum = '<input type="hidden" name="leaseOrderPage.pageNum" value="'+pageNum+'" />';
	var pageCount = '<input type="hidden" name="leaseOrderPage.pageCount" value="'+pageCount+'" />';
	$('#leaseOrderForm').append(pageNum);
	$('#leaseOrderForm').append(pageCount);
	$('#leaseOrderForm').submit();
}
</script>
</head>
<body>
<input type="hidden" id="initPath" value="<%=request.getContextPath()%>" />
<div class="box-positon">
	<div class="rpos">当前位置: 租赁订单 - 列表</div>
	<div class="clear"></div>
</div>

<form  id="leaseOrderForm" action="<%=request.getContextPath()%>/pos/shopOrder!shopLeaseOrderList.do" method="post" style="padding-top:5px; padding-left: 10px;">
<div>
	订单类型:<input type="radio" id="orderType0" name="orderType" value="0" onclick="$('#leaseOrderForm').submit();" <c:if test="${orderType==0}">checked="checked"</c:if>><label for="orderType0">租赁流水单</label>
			<input type="radio" id="orderType1" name="orderType" value="1" onclick="$('#leaseOrderForm').submit();" <c:if test="${orderType==1}">checked="checked"</c:if>><label for="orderType1">还租流水单</label>&nbsp;&nbsp;&nbsp;
	店面名称:<input type="text" name="shopName" value="${shopName}">&nbsp;&nbsp;&nbsp;
	会员名称:<input type="text" name="memberName" value="${memberName}">&nbsp;&nbsp;&nbsp;
	<input id="queryBut" type="submit" class="query" value="查询" />
</div>
</form>


<div id="bodyBox" class="body-box">
<table id="goodsList" width="100%" cellspacing="1" cellpadding="0" border="0" style="" class="pn-ltable">
<thead class="pn-lthead">
	<tr>
		<th>店面名称</th>
		<th>订单流水号</th>
		<th>会员名称</th>
		<th>订单总金额</th>
		<th>订单总押金</th>
		<th>商品名称</th>
		<th>数量</th>
		<th>单价</th>
		<th>单押金</th>
		<th width="100">租赁开始时间</th>
		<th width="100">租赁结束时间</th>
		<th>租赁时长</th>
		<th>租赁方式</th>
	</tr>
</thead>
<tbody class="pn-ltbody">
	<c:forEach var="order" items="${leaseOrderPage.list}">
	<tr>
		<td rowspan="${fn:length(order.shopLeaseOrderProduct)}">${order.shopName }</td>
		<td rowspan="${fn:length(order.shopLeaseOrderProduct)}" align="center">${order.serialNumber }</td>
		<td rowspan="${fn:length(order.shopLeaseOrderProduct)}" align="center">${order.member.name }</td>
		<td rowspan="${fn:length(order.shopLeaseOrderProduct)}" align="right">${order.price }</td>
		<td rowspan="${fn:length(order.shopLeaseOrderProduct)}" align="right">${order.deposit }</td>
		<c:forEach var="op" items="${order.shopLeaseOrderProduct}" end="0">
			<td align="center">${op.product.name }</td>
			<td align="right">${op.count }</td>
			<td align="right"><fmt:formatNumber value="${op.prePrice }" pattern="#,##0.0#" /></td>
			<td align="right"><fmt:formatNumber value="${op.perDeposit }" pattern="#,##0.0#" /></td>
			<td align="center"><fmt:formatDate value="${op.startTime}" pattern="yyyy-MM-dd" /></td>
			<td align="center"><fmt:formatDate value="${op.endTime}" pattern="yyyy-MM-dd" /></td>
			<td align="right">${op.timeLength }</td>
			<td align="center"><c:if test="${op.leaseStyle==0}">日租</c:if><c:if test="${op.leaseStyle==1}">月租</c:if></td>
		</c:forEach>
	</tr>
	<c:if test="${fn:length(order.shopLeaseOrderProduct) > 1}">
		<c:forEach var="op" items="${order.shopLeaseOrderProduct}" begin="1">
		<tr>
			<td align="center">${op.product.name }</td>
			<td align="right">${op.count }</td>
			<td align="right"><fmt:formatNumber value="${op.prePrice }" pattern="#,##0.0#" /></td>
			<td align="right"><fmt:formatNumber value="${op.perDeposit }" pattern="#,##0.0#" /></td>
			<td align="center"><fmt:formatDate value="${op.startTime}" pattern="yyyy-MM-dd" /></td>
			<td align="center"><fmt:formatDate value="${op.endTime}" pattern="yyyy-MM-dd" /></td>
			<td align="right">${op.timeLength }</td>
			<td align="center"><c:if test="${op.leaseStyle==0}">日租</c:if><c:if test="${op.leaseStyle==1}">月租</c:if></td>
		</tr>
		</c:forEach>
	</c:if>
	</c:forEach>
</tbody>
</table>

<table id="dtPageTable" width="100%" style="font-size:13px;">
	<tbody>
	<tr>
		<td align="center" class="pn-sp">
		<c:if var="isFirstPage" test="${leaseOrderPage.pageNum<=1}">
		首页 &nbsp;&nbsp;&nbsp;上一页&nbsp;&nbsp;&nbsp;
		</c:if>
		<c:if test="${!isFirstPage}">
		<a href="javascript:;" onclick="orderList.goPage(1,${leaseOrderPage.pageCount});">首页</a>&nbsp;&nbsp;&nbsp;
		<a href="javascript:;" onclick="orderList.goPage(${leaseOrderPage.pageNum-1},${leaseOrderPage.pageCount});">上一页</a>
		</c:if>
		&nbsp;&nbsp;&nbsp;
		<c:if var="isLastPage" test="${leaseOrderPage.pageNum>=leaseOrderPage.totalPages}">
		下一页&nbsp;&nbsp;&nbsp;尾页&nbsp;&nbsp;&nbsp;
		</c:if>
		<c:if test="${!isLastPage}">
		<a href="javascript:;" onclick="orderList.goPage(${leaseOrderPage.pageNum+1},${leaseOrderPage.pageCount});">下一页</a>&nbsp;&nbsp;&nbsp;
		<a href="javascript:;" onclick="orderList.goPage(${leaseOrderPage.totalPages},${leaseOrderPage.pageCount});">尾页</a>
		</c:if>
		&nbsp;&nbsp;&nbsp;&nbsp;
		第${leaseOrderPage.pageNum}/${leaseOrderPage.totalPages}页,共${leaseOrderPage.totalRecords}条记录
	</td>
	</tr>
	</tbody>
</table>
</div>
</body>
</html>