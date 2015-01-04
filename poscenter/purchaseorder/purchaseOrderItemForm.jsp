<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>purchaseOrderItem form</title>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/skin/css/admin.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/skin/css/theme.css" />
<link rel="STYLESHEET" type="text/css" href="<%=request.getContextPath()%>/js/jquery/msgbox/jquery.msgbox.css" />

<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.validate.js"></script>

<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/msgbox/jquery.dragndrop.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/msgbox/jquery.msgbox.js"></script>

<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/dhtmlxTree/dhtmlxtree.css" />
<script src="<%=request.getContextPath()%>/js/dhtmlxTree/dhtmlxcommon.js"></script>
<script src="<%=request.getContextPath()%>/js/dhtmlxTree/dhtmlxtree.js"></script>

<script type="text/javascript">
var purchaseOrderItem={};

//表单验证
purchaseOrderItem.validateForm = function(){
	if($('#purchaseOrderItemForm').valid()){
		if($('input[name="purchaseOrderItem.count"]').val() == 0){
			alert('商品数量必须大于0！');
			return false;
		}
		return true;
	}else{
		return false;
	}
}

//返回列表
purchaseOrderItem.backList = function(){
	window.location.href = $('#initPath').val()+'/pos/purchaseOrderItem!purchaseOrderItemList.do?purchaseOrderId='+$('#purchaseOrderId').val();
}

$(document).ready(function(){
	//验证表单
	$("#purchaseOrderItemForm").validate();

	//选择商品
	$('#productName').click(function(){
		new $.msgbox({
			title: '选择商品',
			width: 700,
			height: 480,
			type: 'ajax',
			content: $('#initPath').val()+'/pos/Product!toSelectProductListView.do',
			onClose: function(){
				$.getJSON($('#initPath').val()+'/pos/Product!getPurchaseReferPrice.do', {'product.id': $('#productId').val()}, function(data){
					if(Number(data)!=0 && $('#purchasePrice').val()==''){
						$('#purchasePrice').val(data);
					}
				});
			}
		}).show();
	});
	
	//选择供应商
	$('#supplierName').click(function(){
		new $.msgbox({
			title: '选择供应商',
			width: 700,
			height: 480,
			type: 'ajax',
			content: $('#initPath').val()+'/pos/supplier!supplierChooseList.do',
			onAjaxed: function(){}
		}).show();
	});
});
</script>
</head>
<body style="min-height:500px;">
<input type="hidden" id="initPath" value="<%=request.getContextPath()%>" />
<div class="box-positon">
	<div class="rpos">当前位置: 采购单管理 - <c:if test="${purchaseOrderItem.id != 0}">修改采购单条目</c:if><c:if test="${purchaseOrderItem.id == 0}">新增采购单条目</c:if></div>
	<div class="ropt"></div>
	<div class="clear"></div>
</div>

<div id="bodyBox" class="body-box">
	<form id="purchaseOrderItemForm" action="<%=request.getContextPath()%>/pos/purchaseOrderItem!savePurchaseOrderItem.do" method="post" onsubmit="return purchaseOrderItem.validateForm();">
	<input type="hidden" name="purchaseOrderItem.id" value="${purchaseOrderItem.id }" />
	<input type="hidden" name="purchaseOrderItem.purchaseOrderId" value="${purchaseOrderId}" />
	<input type="hidden" id="purchaseOrderId" name="purchaseOrderId" value="${purchaseOrderId}" />
	<table cellspacing="1" cellpadding="2" border="0" width="100%" class="pn-ftable" style="font-size:13px;">
		<tr>
			<td width="12%" class="pn-flabel pn-flabel-h">采购单号:</td>
			<td width="88%" class="pn-fcontent">${purchaseOrder.orderNumber }</td>
		</tr>
		<tr>
			<td width="12%" class="pn-flabel pn-flabel-h">负责人:</td>
			<td width="88%" class="pn-fcontent">${purchaseOrder.charger }</td>
		</tr>
		<tr>
			<td width="12%" class="pn-flabel pn-flabel-h"><span class="pn-frequired">*</span>商品:</td>
			<td width="88%" class="pn-fcontent">
				<input type="hidden" id="productId" name="purchaseOrderItem.productId" value="${purchaseOrderItem.productId }" />
				<input type="text" id="productName" class="required" value="${purchaseOrderItem.product.name }" readonly="readonly" />
			</td>
		</tr>
		<tr>
			<td class="pn-flabel pn-flabel-h">供应商:</td>
			<td class="pn-fcontent">
				<input type="hidden" id="supplierId" name="purchaseOrderItem.supplierId" value="${purchaseOrderItem.supplierId }" />
				<input type="text" id="supplierName" value="${purchaseOrderItem.supplier.name }" readonly="readonly" />
			</td>
		</tr>
		<tr>
			<td class="pn-flabel pn-flabel-h"><span class="pn-frequired">*</span>商品数量:</td>
			<td class="pn-fcontent">
				<input type="text" name="purchaseOrderItem.count" class="required digits" value="<c:if test="${purchaseOrderItem.id != 0}">${purchaseOrderItem.count }</c:if>" />
			</td>
		</tr>
		<tr>
			<td class="pn-flabel pn-flabel-h"><span class="pn-frequired">*</span>采购价:</td>
			<td class="pn-fcontent"><input type="text" class="required money" id="purchasePrice" name="purchaseOrderItem.purchasePrice" value="<c:if test="${purchaseOrderItem.id != 0}">${purchaseOrderItem.purchasePrice }</c:if>" /></td>
		</tr>
		<tr>
			<td class="pn-fcontent" align="center" colspan="2">
				<input type="submit" class="submit" value="提交" />
				<input type="button" class="return-button" value="返回" onclick="purchaseOrderItem.backList();" />
			</td>
		</tr>
	</table>
	</form>
</div>
</body>
</html>