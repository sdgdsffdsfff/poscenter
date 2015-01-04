<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>shop form</title>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/skin/css/admin.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/skin/css/theme.css" />

<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.validate.js"></script>

<script type="text/javascript">
var shop={};

//表单验证
shop.validateForm = function(){
	if(!$('#shopForm').valid()){return false;}
	return true;
}

//返回列表
shop.backList = function(){
	window.location.href = $('#initPath').val()+'/pos/shop!shopList.do';
}

$(document).ready(function(){
	//验证表单
	$("#shopForm").validate();
});
</script>
</head>
<body style="min-height:500px;">
<input type="hidden" id="initPath" value="<%=request.getContextPath()%>" />
<div class="box-positon">
	<div class="rpos">当前位置: 店面管理 - <c:if test="${shop.id != 0}">修改店面信息</c:if><c:if test="${shop.id == 0}">新增店面</c:if></div>
	<div class="ropt"></div>
	<div class="clear"></div>
</div>

<div id="bodyBox" class="body-box">
	<form id="shopForm" action="<%=request.getContextPath()%>/pos/shop!saveShop.do" method="post" onsubmit="return shop.validateForm();">
	<input type="hidden" name="shop.id" value="${shop.id }" />
	<table cellspacing="1" cellpadding="2" border="0" width="100%" class="pn-ftable" style="font-size:13px;">
		<tr>
			<td width="12%" class="pn-flabel pn-flabel-h"><span class="pn-frequired">*</span>店面编号:</td>
			<td width="88%" class="pn-fcontent"><input type="text" class="required" name="shop.code" value="${shop.code }" /></td>
		</tr>
		<tr>
			<td class="pn-flabel pn-flabel-h"><span class="pn-frequired">*</span>店面名称:</td>
			<td class="pn-fcontent"><input type="text" class="required" name="shop.name" value="${shop.name }" /></td>
		</tr>
		<tr>
			<td class="pn-flabel pn-flabel-h">店面负责人:</td>
			<td class="pn-fcontent"><input type="text" name="shop.charger" value="${shop.charger }" /></td>
		</tr>
		<tr>
			<td class="pn-flabel pn-flabel-h">店面地址:</td>
			<td class="pn-fcontent"><input type="text" name="shop.address" value="${shop.address }" /></td>
		</tr>
		<tr>
			<td class="pn-fcontent" align="center" colspan="2">
				<input type="submit" class="submit" value="提交" />
				<input type="button" class="return-button" value="返回" onclick="shop.backList();" />
			</td>
		</tr>
	</table>
	</form>
</div>
</body>
</html>