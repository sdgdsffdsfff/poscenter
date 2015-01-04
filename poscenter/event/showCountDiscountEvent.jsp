<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>查看数量折扣活动</title>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/skin/css/admin.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/skin/css/theme.css" />
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script>
<script type="text/javascript">
var countDiscountEvent={};

//返回列表
countDiscountEvent.backList = function(){
	window.location.href = $('#initPath').val()+'/pos/event!eventList.do';
}
</script>
</head>
<body style="min-height:500px;">
<input type="hidden" id="initPath" value="<%=request.getContextPath()%>" />
<div class="box-positon">
	<div class="rpos">当前位置: 促销活动管理 - 查看数量折扣活动</div>
	<div class="ropt"></div>
	<div class="clear"></div>
</div>

<div id="bodyBox" class="body-box">
<table cellspacing="1" cellpadding="2" border="0" width="100%" class="pn-ftable" style="font-size:13px;">
	<tr>
		<td width="12%" class="pn-flabel pn-flabel-h">活动类型:</td>
		<td width="88%" class="pn-fcontent">数量折扣</td>
	</tr>
	<tr>
		<td width="12%" class="pn-flabel pn-flabel-h">开始时间:</td>
		<td width="88%" class="pn-fcontent"><fmt:formatDate value="${event.startTime}" pattern="yyyy-MM-dd HH:mm" /></td>
	</tr>
	<tr>
		<td width="12%" class="pn-flabel pn-flabel-h">结束时间:</td>
		<td width="88%" class="pn-fcontent"><fmt:formatDate value="${event.endTime}" pattern="yyyy-MM-dd HH:mm" /></td>
	</tr>
	<tr>
		<td class="pn-flabel pn-flabel-h">适用门店:</td>
		<td class="pn-fcontent">
			<c:forEach var="shop" items="${event.shopList}">${shop.name}；</c:forEach>
		</td>
	</tr>
	<tr>
		<td class="pn-flabel pn-flabel-h">规则描述:</td>
		<td class="pn-fcontent">${event.ruleDesc }</td>
	</tr>
	<tr>
		<td class="pn-flabel pn-flabel-h">具体规则:</td>
		<td class="pn-fcontent">
			<%-- 固定折扣商品 --%>
			<div>
				<span style="font-weight:bold;">固定折扣商品</span>（只要商品数量>=2时，全部享受一个折扣优惠）
			</div>
			<table id="fixedProductListTable" width="100%" class="pn-ftable" cellspacing="1">
				<tr align="center">
					<td class="pn-fcontent" width="40%">商品</td>
					<td class="pn-fcontent" width="40%">折扣</td>
				</tr>
				<c:forEach var="countDiscountEvent" items="${fixedCountDiscountEventList}">
				<tr align="center">
					<td class="pn-fcontent">${countDiscountEvent.product.name }</td>
					<td class="pn-fcontent"><fmt:formatNumber value="${countDiscountEvent.discount * 100}"/></td>
				</tr>
				</c:forEach>
			</table>
			
			<%-- 阶梯折扣商品 --%>
			<div style="margin-top:30px;">
				<span style="font-weight:bold;">阶梯折扣商品</span>（第一件默认是原价，从第二件起产生阶梯折扣）
			</div>
			<table id="ladderProductListTable" width="100%" class="pn-ftable" cellspacing="1">
				<tr align="center">
					<td class="pn-fcontent" width="40%">商品</td>
					<td class="pn-fcontent" width="40%">折扣</td>
				</tr>
				<c:forEach var="ladderCountDiscountEventList" items="${ladderCountDiscountEventListList}">
				<c:forEach var="ladderCountDiscountEvent" items="${ladderCountDiscountEventList}" end="0">
				<tr align="center">
					<td class="pn-fcontent">${ladderCountDiscountEvent.product.name }</td>
					<td class="pn-fcontent">
						<table width="100%">
							<tr>
								<td align="center">第1件</td>
								<td align="center"><fmt:formatNumber value="${ladderCountDiscountEvent.discount * 100}" pattern="###.##" /></td>
							</tr>
							<c:forEach var="ladderCountDiscountEvent2" items="${ladderCountDiscountEventList}" begin="1" varStatus="status2">
							<tr>
								<td align="center">第${status2.index+1}件</td>
								<td align="center"><fmt:formatNumber value="${ladderCountDiscountEvent2.discount * 100}" pattern="###.##" /></td>
							</tr>
							</c:forEach>
						</table>
					</td>
				</tr>
				</c:forEach>
				</c:forEach>
			</table>
		</td>
	</tr>
</table>

<div style="text-align:center; margin-top:10px; margin-bottom: 20px;">
	<input type="button" class="return-button" value="返回" onclick="countDiscountEvent.backList();" />
</div>
</div>
</body>
</html>