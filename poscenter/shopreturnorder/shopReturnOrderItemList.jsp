<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>店面退货单条目列表</title>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/skin/css/admin.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/skin/css/theme.css" />

<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.validate.js"></script>
<script type="text/javascript">
var shopReturnOrderItem={};

//表单验证
shopReturnOrderItem.validateForm = function(){
	if($('#shopReturnOrderForm').valid()){
		return true;
	}else{
		return false;
	}
}

//返回列表
shopReturnOrderItem.backList = function(){
	window.location.href = $('#initPath').val()+'/pos/shopReturnOrder!shopReturnOrderList.do';
}

//确认收货
shopReturnOrderItem.confirmReturn = function(){
	if(!$('#shopReturnOrderForm').valid()){
		return false;
	}
	
	if(confirm('确认后将不能修改退货单信息，确定提交该退货单吗？')){
		$("#shopReturnOrderForm").submit();
	}
}

//导出Excel
shopReturnOrderItem.exportExcel = function(shopReturnOrderId){
	window.location.href = $('#initPath').val()+'/pos/shopReturnOrder!exportExcel.do?shopReturnOrder.id='+shopReturnOrderId;
}

$(document).ready(function(){
	//验证表单
	$("#shopReturnOrderForm").validate();

});
</script>
</head>
<body>
<input type="hidden" id="initPath" value="<%=request.getContextPath()%>" />
<div class="box-positon">
	<div class="rpos">当前位置: 店面退货单管理 - 退货单条目列表</div>
	<div class="ropt">
		<c:if test="${shopReturnOrder.useStatus == 0}">
			<input type="button" onclick="shopReturnOrderItem.confirmReturn();" class="" value="确认退货" /> &nbsp; 
		</c:if>
		<c:if test="${shopReturnOrder.useStatus == 1}">
			<input type="button" onclick="shopReturnOrderItem.exportExcel(${shopReturnOrder.id});" class="sendbox" value="导出Excel" /> &nbsp; 
		</c:if>
		<input type="button" class="return-button" value="返回" onclick="shopReturnOrderItem.backList();" />
	</div>
	<div class="clear"></div>
</div>

<div id="bodyBox" class="body-box">

<form id="shopReturnOrderForm" action="<%=request.getContextPath()%>/pos/shopReturnOrder!confirmReturn.do" method="post">
<input type="hidden" id="shopReturnOrderId" name="shopReturnOrder.id" value="${shopReturnOrder.id }" />
<c:if var="noConfirmReturn" test="${shopReturnOrder.useStatus == 0}">
<table cellspacing="1" cellpadding="2" border="0" width="100%" class="pn-ftable" style="font-size:13px;">
	<tr>
		<td width="12%" class="pn-flabel pn-flabel-h">店面名称:</td>
		<td width="88%" class="pn-fcontent">${shopReturnOrder.shop.name }</td>
	</tr>
	<tr>
		<td class="pn-flabel pn-flabel-h">退货单号:</td>
		<td class="pn-fcontent">
			${shopReturnOrder.orderNumber }
			<input type="hidden" name="shopReturnOrder.orderNumber" value="${shopReturnOrder.orderNumber }" />
		</td>
	</tr>
	<tr>
		<td class="pn-flabel pn-flabel-h"><span class="pn-frequired">*</span>负责人:</td>
		<td class="pn-fcontent">
			<input type="text" name="shopReturnOrder.charger" class="required" value="${shopReturnOrder.charger }" />
		</td>
	</tr>
</table>
</c:if>
<c:if test="${!noConfirmReturn}">
<table cellspacing="1" cellpadding="2" border="0" width="100%" class="pn-ftable" style="font-size:13px;">
	<tr>
		<td width="12%" class="pn-flabel pn-flabel-h">店面名称:</td>
		<td width="88%" class="pn-fcontent">${shopReturnOrder.shop.name }</td>
	</tr>
	<tr>
		<td class="pn-flabel pn-flabel-h">退货单号:</td>
		<td class="pn-fcontent">${shopReturnOrder.orderNumber }</td>
	</tr>
	<tr>
		<td class="pn-flabel pn-flabel-h">负责人:</td>
		<td class="pn-fcontent">${shopReturnOrder.charger }</td>
	</tr>
</table>
</c:if>

<table id="goodsList" width="100%" cellspacing="1" cellpadding="0" border="0" style="" class="pn-ltable">
<thead class="pn-lthead">
<tr>
	<th>序号</th>
	<th>商品名称</th>
	<th>退货数量</th>
	<th>实收数量</th>
</tr>
</thead>
<tbody class="pn-ltbody">
<c:forEach var="shopReturnOrderItem" items="${allItemList}" varStatus="status">
<tr>
	<td align="center">${status.index+1}</td>
	<td>${shopReturnOrderItem.product.name }</td>
	<td align="center">${shopReturnOrderItem.returnCount}</td>
	<td align="center">
		<c:if test="${noConfirmReturn}">
			<input type="hidden" name="itemList[${status.index}].id" value="${shopReturnOrderItem.id}" />
			<input type="text" size="5" class="required digits" name="itemList[${status.index}].receiveCount" value="${shopReturnOrderItem.returnCount}" />
			<input type="hidden" name="itemList[${status.index}].productId" value="${shopReturnOrderItem.productId}" />
		</c:if>
		<c:if test="${!noConfirmReturn}">
			${shopReturnOrderItem.receiveCount}
		</c:if>
	</td>
</tr>
</c:forEach>
</tbody>
</table>
</form>
<br/>
</div>
</body>
</html>