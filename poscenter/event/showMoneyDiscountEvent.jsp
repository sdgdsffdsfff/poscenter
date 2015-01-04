<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>查看金额折扣活动</title>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/skin/css/admin.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/skin/css/theme.css" />
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script>
<script type="text/javascript">
var moneyDiscountEvent={};

//返回列表
moneyDiscountEvent.backList = function(){
	window.location.href = $('#initPath').val()+'/pos/event!eventList.do';
}
</script>
</head>
<body style="min-height:500px;">
<input type="hidden" id="initPath" value="<%=request.getContextPath()%>" />
<div class="box-positon">
	<div class="rpos">当前位置: 促销活动管理 - 查看金额折扣活动</div>
	<div class="ropt"></div>
	<div class="clear"></div>
</div>

<div id="bodyBox" class="body-box">
<table cellspacing="1" cellpadding="2" border="0" width="100%" class="pn-ftable" style="font-size:13px;">
	<tr>
		<td width="12%" class="pn-flabel pn-flabel-h">活动类型:</td>
		<td width="88%" class="pn-fcontent">金额折扣</td>
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
			<table id="moneyListTable" width="100%" class="pn-ftable" cellspacing="1">
				<tr align="center">
					<td class="pn-fcontent">消费总计金额</td>
					<td class="pn-fcontent">满减优惠</td>
					<td class="pn-fcontent">打折优惠</td>
				</tr>
				<c:forEach var="moneyDiscountEvent" items="${event.moneyDiscountEventList}">
				<tr align="center">
					<td class="pn-fcontent"><fmt:formatNumber value="${moneyDiscountEvent.money }" pattern="#,##0.00" /></td>
					<td class="pn-fcontent">
						<c:if test="${moneyDiscountEvent.type==1}">
						<fmt:formatNumber value="${moneyDiscountEvent.numberValue}" pattern="#,##0.00" />
						</c:if>
					</td>
					<td class="pn-fcontent">
						<c:if test="${moneyDiscountEvent.type==2}">
						<fmt:formatNumber value="${moneyDiscountEvent.numberValue*100}" pattern="###.##" />
						</c:if>
					</td>
				</tr>
				</c:forEach>
			</table>
		</td>
	</tr>
</table>

<div style="text-align:center; margin-top:10px; margin-bottom: 20px;">
	<input type="button" class="return-button" value="返回" onclick="moneyDiscountEvent.backList();" />
</div>
</div>
</body>
</html>