<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="s" uri="/struts-tags"%> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>确认收货</title>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/skin/css/admin.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/skin/css/theme.css" />

<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.validate.js"></script>
<script type="text/javascript">
var receiveOrder={};

//返回列表
receiveOrder.backList = function(){
	window.location.href = $('#initPath').val()+'/pos/receiveOrder!getReceiveOrderList.do';
}

//确认收货
receiveOrder.confirmReceive = function(){
	if(!$('#receiveOrderForm').valid()){
		return false;
	}
	
	if(confirm('确认后将不能修改收货单信息，确定提交该收货单吗？')){
		$("#receiveOrderForm").submit();
	}
}

$(document).ready(function(){
	//验证表单
	$("#receiveOrderForm").validate();
});
</script>
</head>
<body style="min-height:500px;">
<input type="hidden" id="initPath" value="<%=request.getContextPath()%>" />
<div class="box-positon">
	<div class="rpos">当前位置: 收货单管理 - 确认收货</div>
	<div class="ropt"></div>
	<div class="clear"></div>
</div>

<div id="bodyBox" class="body-box">
<form id="receiveOrderForm" action="<%=request.getContextPath()%>/pos/receiveOrder!saveReceiveOrder.do" method="post">
	<input type="hidden" id="initPath" value="<%=request.getContextPath()%>" />
	<input type="hidden" id="orderId" name="receiveOrder.id" value="${receiveOrder.id}" />
	<input type="hidden" name="receiveOrder.orderNumber" value="${receiveOrder.orderNumber}" />
	<table cellspacing="1" cellpadding="2" border="0" width="100%" class="pn-ftable" style="font-size:13px;">
		<tr>
			<td width="12%" class="pn-flabel pn-flabel-h">收货单编号:</td>
			<td width="88%" class="pn-fcontent">${receiveOrder.orderNumber}</td>
		</tr>
		<tr>
			<td class="pn-flabel pn-flabel-h"><span class="pn-frequired">*</span>收货负责人:</td>
			<td class="pn-fcontent">
				<input type="text" id="charger" name="receiveOrder.charger" class="required" value="${receiveOrder.charger}" />
			</td>
		</tr>
	</table>
	
	<table width="100%" cellspacing="1" cellpadding="0" border="0" class="pn-ltable">
	<thead class="pn-lthead">
	<tr>
		<th>序号</th>
		<th>商品编码</th>
		<th>商品名称</th>
		<th>采购数量</th>
		<th>实收数量</th>
	</tr>
	</thead>
	<tbody class="pn-ltbody">
	<s:iterator value="itemlist" var="li" status="L">
	<tr>
		<td align="center">${L.index+1}<input type="hidden" name="recOrItem[${L.index}].productId" value="<s:property value="#li.productId"/>" /></td>
		<td align="center"><s:property value="#li.productCode"/></td>
		<td><s:property value="#li.productName"/></td>
		<td align="center"><s:property value="#li.sendCount"/></td>
		<td align="center">
			<input type="text" size="6" class="required digits" name="recOrItem[${L.index}].receiveCount" value="<s:property value="#li.sendCount"/>" />
		</td>
	</tr>
	</s:iterator>
	<tr>
		<td colspan="5" align="center">
			<input type="button" class="submit" onclick="receiveOrder.confirmReceive();" value="提交" />
			<input type="button" class="return-button" value="返回" onclick="receiveOrder.backList();" />
		</td>
	</tr>
	</tbody>
	</table>
</form>
</div>
</body>
</html>