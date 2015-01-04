<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>销售订单列表</title>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/skin/css/admin.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/skin/css/theme.css" />
<link rel="STYLESHEET" type="text/css" href="<%=request.getContextPath()%>/js/jquery/msgbox/jquery.msgbox.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/jquery/qtip/jquery.qtip.min.css" />

<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/qtip/jquery.qtip.min.js"></script>

<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/msgbox/jquery.dragndrop.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/msgbox/jquery.msgbox.js"></script>
<script type="text/javascript">
var orderList={};

//跳转到指定页面
orderList.goPage = function(pageNum,pageCount){
	var pageNum = '<input type="hidden" name="saledOrderPage.pageNum" value="'+pageNum+'" />';
	var pageCount = '<input type="hidden" name="saledOrderPage.pageCount" value="'+pageCount+'" />';
	$('#saledOrderForm').append(pageNum);
	$('#saledOrderForm').append(pageCount);
	$('#saledOrderForm').submit();
}

//查看订单活动详情
orderList.goEventDetailView = function(saledOrderId,shopId){
    new $.msgbox({
		title: '订单活动详情',
		width: 700,
		height: 480,
		anim: 1,
		type: 'ajax',
		content: $('#initPath').val()+'/pos/shopSaledOrderEvent!toShopOrderDetailEventView.do?shopSaledOrder.saledOrderId='+saledOrderId+'&shopSaledOrder.shopId='+shopId
	}).show();
}




$(document).ready(function(){
	$('#saledOrderList').find('a[name=moneyDetail]').each(function(){
		$(this).qtip({
			show: {
				event: 'click'
			}
			,position: {
				my: 'left center'
			}
			,hide: 'unfocus'
		});
	}).click(function(event) { event.preventDefault(); });
});
</script>
</head>
<body>
<input type="hidden" id="initPath" value="<%=request.getContextPath()%>" />
<div class="box-positon">
	<div class="rpos">当前位置: 销售订单 - 列表</div>
	<div class="clear"></div>
</div>

<form id="saledOrderForm" action="<%=request.getContextPath()%>/pos/shopOrder!shopSaledOrderList.do" method="post" style="padding-top:5px; padding-left: 10px;">
<div>
	订单类型:<input type="radio" id="orderType0" name="orderType" value="0" onclick="orderList.goPage(1,${saledOrderPage.pageCount});" <c:if test="${orderType==0}">checked="checked"</c:if>><label for="orderType0">购买流水单</label>
	<input type="radio" id="orderType1" name="orderType" value="1" onclick="orderList.goPage(1,${saledOrderPage.pageCount});" <c:if test="${orderType==1}">checked="checked"</c:if>><label for="orderType1">退货流水单</label>&nbsp;&nbsp;&nbsp;
	店面名称:<input type="text" size="12" name="shopName" value="${shopName}">&nbsp;&nbsp;&nbsp;
	订单流水号:<input type="text" size="12" name="serialNumber" value="${serialNumber}">&nbsp;&nbsp;&nbsp;
	会员名称:<input type="text" size="12" name="memberName" value="${memberName}">&nbsp;&nbsp;&nbsp;
	<c:if test="${orderType==0}">
	支付方式:<input id="bankPay" type="checkbox" name="payMethod" value="3" <c:if test="${payMethod==3}">checked="checked"</c:if> /><label for="bankPay">银行卡</label>&nbsp;&nbsp;&nbsp;
	刷卡流水号:<input type="text" size="12" name="swipCardNumber" value="${swipCardNumber}">&nbsp;&nbsp;&nbsp;
	</c:if>
	<input type="submit" class="query" value="查询" />
</div>
</form>

<div id="bodyBox" class="body-box">
<table id="saledOrderList" width="100%" cellspacing="1" cellpadding="0" border="0" class="pn-ltable">
<thead class="pn-lthead">
	<tr>
		<th>店面名称</th>
		<th>订单流水号</th>
		<th>会员名称</th>
		<th>订单总金额</th>
		<th width="180">销售时间</th>
		<th>参与活动</th>
		<th>商品名称</th>
		<th>数量</th>
		<th>单价</th>
	</tr>
</thead>
<tbody class="pn-ltbody">
	<c:forEach var="order" items="${saledOrderPage.list}">
	
		<%--金钱来源--%>
		<c:set var="msList" value="" />
		<c:forEach var="moneySource" items="${order.shopMoneySource}">
			<c:choose>
				<c:when test="${moneySource.type == 1}">
					<c:set var="msList" value="${msList}&#12288;现金消费：${moneySource.money}<br/>" />
				</c:when>
				<c:when test="${moneySource.type == 2}">
					<c:set var="msList" value="${msList}会员卡消费：${moneySource.money}<br/>" />
				</c:when>
				<c:when test="${moneySource.type == 3}">
					<c:set var="msList" value="${msList}银行卡消费：${moneySource.money}<br/>刷卡流水号：${moneySource.swipCardNumber}<br/>" />
				</c:when>
				<c:when test="${moneySource.type == 4}">
					<c:set var="msList" value="${msList}购物券消费：${moneySource.money}<br/>" />
				</c:when>
			</c:choose>
		</c:forEach>
		
		<%--金钱去向--%>
		<c:set var="mdList" value="" />
		<c:forEach var="moneyDestination" items="${order.shopMoneyDestinationList}">
			<c:choose>
				<c:when test="${moneyDestination.type == 1}">
					<c:set var="mdList" value="${mdList}&#12288;&#12288;返还现金：${moneyDestination.money}<br/>" />
				</c:when>
				<c:when test="${moneyDestination.type == 2}">
					<c:set var="mdList" value="${mdList}返还到会员卡：${moneyDestination.money}<br/>" />
				</c:when>
				<c:when test="${moneyDestination.type == 3}">
					<c:set var="mdList" value="${mdList}返还到银行卡：${moneyDestination.money}<br/>" />
				</c:when>
			</c:choose>
		</c:forEach>
	
	<tr>
		<td rowspan="${fn:length(order.shopSaledOrderProduct)}">${order.shopName }</td>
		<td rowspan="${fn:length(order.shopSaledOrderProduct)}" align="center">${order.serialNumber }</td>
		<td rowspan="${fn:length(order.shopSaledOrderProduct)}" align="center">${order.member.name }</td>
		<td rowspan="${fn:length(order.shopSaledOrderProduct)}" align="right">
			<fmt:formatNumber value="${order.price }" pattern="#,##0.00" />&nbsp;
			<a href="javascript:;" name="moneyDetail" title="${msList}${mdList}"><img style="vertical-align:middle;" src="<%=request.getContextPath()%>/css/skin/img/admin/model-icon.png"/></a>
		</td>
		<td rowspan="${fn:length(order.shopSaledOrderProduct)}" align="center"><fmt:formatDate value="${order.saledTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
		
		
		<td rowspan="${fn:length(order.shopSaledOrderProduct)}" align="center">
	      <c:if test="${fn:length(order.shopSaledOrderEventList) > 0}">
	          <a href="javascript:;" onclick="orderList.goEventDetailView(${order.saledOrderId },${order.shopId});" >活动详情</a>
	       </c:if>
	   </td>
		
		<c:forEach var="op" items="${order.shopSaledOrderProduct}" end="0">
			<td><c:if test="${op.eventRemark!=null && op.eventRemark!=''}">（${op.eventRemark}）</c:if>${op.product.name }</td>
			<td align="right">${op.count }</td>
			<td align="right"><fmt:formatNumber value="${op.prePrice }" pattern="#,##0.00" /></td>
		</c:forEach>
	</tr>
	<c:if test="${fn:length(order.shopSaledOrderProduct) > 1}">
		<c:forEach var="op" items="${order.shopSaledOrderProduct}" begin="1">
		<tr>
			<td><c:if test="${op.eventRemark!=null && op.eventRemark!=''}">（${op.eventRemark}）</c:if>${op.product.name }</td>
			<td align="right">${op.count }</td>
			<td align="right"><fmt:formatNumber value="${op.prePrice }" pattern="#,##0.00" /></td>
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
		<c:if var="isFirstPage" test="${saledOrderPage.pageNum<=1}">
		首页 &nbsp;&nbsp;&nbsp;上一页&nbsp;&nbsp;&nbsp;
		</c:if>
		<c:if test="${!isFirstPage}">
		<a href="javascript:;" onclick="orderList.goPage(1,${saledOrderPage.pageCount});">首页</a>&nbsp;&nbsp;&nbsp;
		<a href="javascript:;" onclick="orderList.goPage(${saledOrderPage.pageNum-1},${saledOrderPage.pageCount});">上一页</a>
		</c:if>
		&nbsp;&nbsp;&nbsp;
		<c:if var="isLastPage" test="${saledOrderPage.pageNum>=saledOrderPage.totalPages}">
		下一页&nbsp;&nbsp;&nbsp;尾页&nbsp;&nbsp;&nbsp;
		</c:if>
		<c:if test="${!isLastPage}">
		<a href="javascript:;" onclick="orderList.goPage(${saledOrderPage.pageNum+1},${saledOrderPage.pageCount});">下一页</a>&nbsp;&nbsp;&nbsp;
		<a href="javascript:;" onclick="orderList.goPage(${saledOrderPage.totalPages},${saledOrderPage.pageCount});">尾页</a>
		</c:if>
		&nbsp;&nbsp;&nbsp;&nbsp;
		第${saledOrderPage.pageNum}/${saledOrderPage.totalPages}页,共${saledOrderPage.totalRecords}条记录
	</td>
	</tr>
	</tbody>
</table>
</div>
</body>
</html>