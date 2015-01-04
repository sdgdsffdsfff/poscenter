<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>SendOrder form</title>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/skin/css/admin.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/skin/css/theme.css" />
<link rel="STYLESHEET" type="text/css" href="<%=request.getContextPath()%>/js/jquery/msgbox/jquery.msgbox.css" />

<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.validate.js"></script>

<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/msgbox/jquery.dragndrop.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/msgbox/jquery.msgbox.js"></script>

<script type="text/javascript">
var sendOrder={};

//表单验证
sendOrder.validateForm = function(){
	if($('#sendOrderForm').valid()){
		return true;
	}else{
		return false;
	}
}

//返回列表
sendOrder.backList = function(){
	window.location.href = $('#initPath').val()+'/pos/sendOrder!sendOrderList.do';
}

$(document).ready(function(){
	//验证表单
	$("#sendOrderForm").validate();

	//选择店面
	$('#shopName').click(function(){
		new $.msgbox({
			title: '选择店面',
			width: 700,
			height: 480,
			type: 'ajax',
			content: $('#initPath').val()+'/pos/shop!toSelectShopListView.do',
			onAjaxed: function(){
			}
		}).show();
	});
});
</script>
</head>
<body style="min-height:500px;">
<input type="hidden" id="initPath" value="<%=request.getContextPath()%>" />
<div class="box-positon">
	<div class="rpos">当前位置: 发货单管理 - <c:if test="${sendOrder.id != 0}">修改发货单</c:if><c:if test="${sendOrder.id == 0}">新增发货单</c:if></div>
	<div class="ropt"></div>
	<div class="clear"></div>
</div>

<div id="bodyBox" class="body-box">
	<form id="sendOrderForm" action="<%=request.getContextPath()%>/pos/sendOrder!saveSendOrder.do" method="post" onsubmit="return sendOrder.validateForm();">
	<input type="hidden" id="sendOrderId" name="sendOrder.id" value="${sendOrder.id }" />
	<input type="hidden" name="sendOrder.useStatus" value="0" />
	<table cellspacing="1" cellpadding="2" border="0" width="100%" class="pn-ftable" style="font-size:13px;">
		<tr>
			<td width="12%" class="pn-flabel pn-flabel-h"><span class="pn-frequired">*</span>发货单号：</td>
			<td width="88%" class="pn-fcontent"><input type="text" class="required" name="sendOrder.orderNumber" value="${sendOrder.orderNumber }" readonly="readonly" /></td>
		</tr>
		<tr>
			<td class="pn-flabel pn-flabel-h"><span class="pn-frequired">*</span>选择店面:</td>
			<td class="pn-fcontent">
				<input type="hidden" id="shopId" name="sendOrder.shopId" value="${sendOrder.shopId }" />
				<input type="text" id="shopName" class="required" value="${sendOrder.shop.name }" readonly="readonly" />
			</td>
		</tr>
		<tr>
			<td class="pn-flabel pn-flabel-h"><span class="pn-frequired">*</span>负责人:</td>
			<td class="pn-fcontent">
				<input type="text" name="sendOrder.charger" class="required" value="${sendOrder.charger }" />
			</td>
		</tr>
		<tr>
			<td class="pn-flabel pn-flabel-h">收货单位:</td>
			<td class="pn-fcontent">
				<input type="text" name="sendOrder.department" value="${sendOrder.department }" />
			</td>
		</tr>
		<tr>
			<td class="pn-fcontent" align="center" colspan="2">
				<input type="submit" class="submit" value="提交" />
				<input type="button" class="return-button" value="返回" onclick="sendOrder.backList();" />
			</td>
		</tr>
	</table>
	</form>
</div>
</body>
</html>