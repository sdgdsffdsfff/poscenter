<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>sendOrderItem form</title>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/skin/css/admin.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/skin/css/theme.css" />
<link rel="STYLESHEET" type="text/css" href="<%=request.getContextPath()%>/js/jquery/msgbox/jquery.msgbox.css" />

<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.validate.js"></script>

<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/msgbox/jquery.dragndrop.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/msgbox/jquery.msgbox.js"></script>

<script type="text/javascript">
var sendOrderItem={};

//表单验证
sendOrderItem.validateForm = function(){
	if($('#sendOrderItemForm').valid()){
		if($('input[name="sendOrderItem.count"]').val() == 0){
			alert('商品数量必须大于0！');
			return false;
		}
		return true;
	}else{
		return false;
	}
}

//返回列表
sendOrderItem.backList = function(){
	window.location.href = $('#initPath').val()+'/pos/sendOrderItem!sendOrderItemList.do?sendOrderId='+$('#sendOrderId').val();
}

$(document).ready(function(){
	//验证表单
	$("#sendOrderItemForm").validate();

	//选择商品
	$('#productName').click(function(){
		new $.msgbox({
			title: '选择商品',
			width: 700,
			height: 480,
			type: 'ajax',
			content: $('#initPath').val()+'/pos/Product!toSelectProductListView.do',
			onAjaxed: function(){}
		}).show();
	});
	
});
</script>
</head>
<body style="min-height:500px;">
<input type="hidden" id="initPath" value="<%=request.getContextPath()%>" />
<div class="box-positon">
	<div class="rpos">当前位置: 发货单管理 - <c:if test="${sendOrderItem.id != 0}">修改发货单条目</c:if><c:if test="${sendOrderItem.id == 0}">新增发货单条目</c:if></div>
	<div class="ropt"></div>
	<div class="clear"></div>
</div>

<div id="bodyBox" class="body-box">
	<form id="sendOrderItemForm" action="<%=request.getContextPath()%>/pos/sendOrderItem!saveSendOrderItem.do" method="post" onsubmit="return sendOrderItem.validateForm();">
	<input type="hidden" name="sendOrderItem.id" value="${sendOrderItem.id }" />
	<input type="hidden" name="sendOrderItem.sendOrderId" value="${sendOrderId}" />
	<input type="hidden" id="sendOrderId" name="sendOrderId" value="${sendOrderId}" />
	<table cellspacing="1" cellpadding="2" border="0" width="100%" class="pn-ftable" style="font-size:13px;">
		<tr>
			<td width="12%" class="pn-flabel pn-flabel-h">发货单号:</td>
			<td width="88%" class="pn-fcontent">${sendOrder.orderNumber }</td>
		</tr>
		<tr>
			<td width="12%" class="pn-flabel pn-flabel-h">负责人:</td>
			<td width="88%" class="pn-fcontent">${sendOrder.charger }</td>
		</tr>
		<tr>
			<td width="12%" class="pn-flabel pn-flabel-h"><span class="pn-frequired">*</span>商品:</td>
			<td width="88%" class="pn-fcontent">
				<input type="hidden" id="productId" name="sendOrderItem.productId" value="${sendOrderItem.productId }" />
				<input type="text" id="productName" class="required" value="${sendOrderItem.product.name }" readonly="readonly" />
			</td>
		</tr>
		<tr>
			<td class="pn-flabel pn-flabel-h"><span class="pn-frequired">*</span>商品数量:</td>
			<td class="pn-fcontent">
				<input type="text" name="sendOrderItem.count" class="required digits" value="<c:if test="${sendOrderItem.id != 0}">${sendOrderItem.count }</c:if>" />
			</td>
		</tr>
		<tr>
			<td class="pn-fcontent" align="center" colspan="2">
				<input type="submit" class="submit" value="提交" />
				<input type="button" class="return-button" value="返回" onclick="sendOrderItem.backList();" />
			</td>
		</tr>
	</table>
	</form>
</div>
</body>
</html>