<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>PurchaseOrder form</title>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/skin/css/admin.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/skin/css/theme.css" />
<link rel="STYLESHEET" type="text/css" href="<%=request.getContextPath()%>/js/jquery/msgbox/jquery.msgbox.css" />

<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.validate.js"></script>

<script type="text/javascript">
var purchaseOrder={};

//表单验证
purchaseOrder.validateForm = function(){
	if($('#purchaseOrderForm').valid()){
		return true;
	}else{
		return false;
	}
}

//返回列表
purchaseOrder.backList = function(){
	window.location.href = $('#initPath').val()+'/pos/purchaseOrder!purchaseOrderList.do';
}

$(document).ready(function(){
	//验证表单
	$("#purchaseOrderForm").validate();

});
</script>
</head>
<body style="min-height:500px;">
<input type="hidden" id="initPath" value="<%=request.getContextPath()%>" />
<div class="box-positon">
	<div class="rpos">当前位置: 采购单管理 - <c:if test="${purchaseOrder.id != 0}">修改采购单</c:if><c:if test="${purchaseOrder.id == 0}">新增采购单</c:if></div>
	<div class="ropt"></div>
	<div class="clear"></div>
</div>

<div id="bodyBox" class="body-box">
	<form id="purchaseOrderForm" action="<%=request.getContextPath()%>/pos/purchaseOrder!savePurchaseOrder.do" method="post" onsubmit="return purchaseOrder.validateForm();">
	<input type="hidden" id="purchaseOrderId" name="purchaseOrder.id" value="${purchaseOrder.id }" />
	<input type="hidden" name="purchaseOrder.orderNumber" value="${purchaseOrder.orderNumber }" />
	<input type="hidden" name="purchaseOrder.useStatus" value="0" />
	<table cellspacing="1" cellpadding="2" border="0" width="100%" class="pn-ftable" style="font-size:13px;">
		<tr>
			<td width="12%" class="pn-flabel pn-flabel-h">采购单号:</td>
			<td width="88%" class="pn-fcontent">${purchaseOrder.orderNumber }</td>
		</tr>
		<tr>
			<td class="pn-flabel pn-flabel-h"><span class="pn-frequired">*</span>负责人:</td>
			<td class="pn-fcontent">
				<input type="text" name="purchaseOrder.charger" class="required" value="${purchaseOrder.charger }" />
			</td>
		</tr>
		<tr>
			<td class="pn-flabel pn-flabel-h"><span class="pn-frequired">*</span>采购单位:</td>
			<td class="pn-fcontent">
				<input type="text" name="purchaseOrder.department" class="required" value="${purchaseOrder.department }" />
			</td>
		</tr>
		<tr>
			<td class="pn-fcontent" align="center" colspan="2">
				<input type="submit" class="submit" value="提交" />
				<input type="button" class="return-button" value="返回" onclick="purchaseOrder.backList();" />
			</td>
		</tr>
	</table>
	</form>
</div>
</body>
</html>