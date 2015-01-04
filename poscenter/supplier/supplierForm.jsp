<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>维护供应商信息</title>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/skin/css/admin.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/skin/css/theme.css" />

<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/checkForm.js"></script>
<script type="text/javascript">
var supplier={};

//表单验证
supplier.validateForm = function(){
	//供应商名称
	if(isNull('name')){
		alert('供应商名称不能为空！');
		return false;
	}
	return true;
}

//返回列表
supplier.backList = function(){
	window.location.href = $('#initPath').val()+'/pos/supplier!supplierList.do';
}
</script>
</head>
<body>
<input type="hidden" id="initPath" value="<%=request.getContextPath()%>" />
<div class="box-positon">
	<div class="rpos">当前位置: 供应商管理 - <c:if test="${supplier.id != 0}">修改供应商信息</c:if><c:if test="${supplier.id == 0}">新增供应商</c:if></div>
	<div class="ropt"></div>
	<div class="clear"></div>
</div>

<div id="bodyBox" class="body-box">
	<form action="<%=request.getContextPath()%>/pos/supplier!saveSupplier.do" method="post" onsubmit="return supplier.validateForm();">
	<input type="hidden" id="supplierId" name="supplier.id" value="${supplier.id }" />
	<table cellspacing="1" cellpadding="2" border="0" width="100%" class="pn-ftable" style="font-size:13px;">
		<tr>
			<td width="12%" class="pn-flabel pn-flabel-h"><span class="pn-frequired">*</span>供应商名称:</td>
			<td width="88%" class="pn-fcontent"><input type="text" id="name" name="supplier.name" value="${supplier.name }" /></td>
		</tr>
		<tr>
			<td class="pn-flabel pn-flabel-h">联系电话:</td>
			<td class="pn-fcontent"><input type="text" id="phone" name="supplier.phone" value="${supplier.phone }" /></td>
		</tr>
		<tr>
			<td class="pn-fcontent" align="center" colspan="2">
				<input type="submit" class="submit" value="提交" />
				<input type="button" class="return-button" value="返回" onclick="supplier.backList();" />
			</td>
		</tr>
	</table>
	</form>
</div>
</body>
</html>