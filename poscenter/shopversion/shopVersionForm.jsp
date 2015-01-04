<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>ShopVersion form</title>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/skin/css/admin.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/skin/css/theme.css" />

<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.validate.js"></script>

<script type="text/javascript">
var shopVersion={};

//表单验证
shopVersion.validateForm = function(){
	if(!$('#shopVersionForm').valid()){return false;}
	return true;
}

//返回列表
shopVersion.backList = function(){
	window.location.href = $('#initPath').val()+'/pos/shopVersion!shopVersionList.do';
}

$(document).ready(function(){
	//验证表单
	$("#shopVersionForm").validate();

	//修改密码按钮
	$("#updateDeployFileBut").toggle(
		function (){
			$(this).text('取消修改').attr('update', '1');
			$('#deployFile').addClass('required').parent().parent().show();
		},
		function (){
			$(this).text('修改').attr('update', '0');
			$('#deployFile').removeClass('required').val('')//.parent().parent().hide();
		}
	);
});
</script>
</head>
<body style="min-height:500px;">
<input type="hidden" id="initPath" value="<%=request.getContextPath()%>" />
<div class="box-positon">
	<div class="rpos">当前位置: 店面系统版本管理 - <c:if test="${ShopVersion.id != 0}">修改店面系统版本信息</c:if><c:if test="${ShopVersion.id == 0}">新增店面系统版本</c:if></div>
	<div class="ropt"></div>
	<div class="clear"></div>
</div>

<div id="bodyBox" class="body-box">
	<form id="shopVersionForm" action="<%=request.getContextPath()%>/pos/shopVersion!saveShopVersion.do" method="post" onsubmit="return shopVersion.validateForm();" enctype="multipart/form-data">
	<input type="hidden" name="shopVersion.id" value="${ShopVersion.id }" />
	<input type="hidden" name="shopVersion.versionNumber" value="${shopVersion.versionNumber }" />
	<input type="hidden" name="shopVersion.filePath" value="${shopVersion.filePath }" />
	<input type="hidden" name="shopVersion.useStatus" value="${shopVersion.useStatus }" />
	<table cellspacing="1" cellpadding="2" border="0" width="100%" class="pn-ftable" style="font-size:13px;">
		<c:if test="${ShopVersion.id != 0}">
		<tr>
			<td width="12%" class="pn-flabel pn-flabel-h">版本编号:</td>
			<td width="88%" class="pn-fcontent">${shopVersion.versionNumber }</td>
		</tr>
		</c:if>
		<tr>
			<td width="12%" class="pn-flabel pn-flabel-h"><span class="pn-frequired">*</span>版本名称:</td>
			<td width="88%" class="pn-fcontent"><input type="text" class="required" name="shopVersion.versionName" value="${shopVersion.versionName }" /></td>
		</tr>
		<c:if test="${shopVersion.id == 0}">
		<tr>
			<td class="pn-flabel pn-flabel-h"><span class="pn-frequired">*</span>部署包:</td>
			<td class="pn-fcontent"><input type="file" class="required" name="deploy" value="" /></td>
		</tr>
		</c:if>
		<c:if test="${shopVersion.id != 0}">
			<tr>
				<td class="pn-flabel pn-flabel-h">部署包:</td>
				<td class="pn-fcontent">
					<a href="<%=request.getContextPath()%>/${shopVersion.filePath }">${shopVersion.filePath }</a>
					<a href="javascript:;" id="updateDeployFileBut" update="0">修改</a>
				</td>
			</tr>
			<tr style="display: none;">
				<td class="pn-flabel pn-flabel-h"><span class="pn-frequired">*</span>新部署包:</td>
				<td class="pn-fcontent"><input id="deployFile" type="file" name="deploy" /></td>
			</tr>
		</c:if>
		<tr>
			<td class="pn-flabel pn-flabel-h">版本描述:</td>
			<td class="pn-fcontent"><textarea name="shopVersion.versionDesc">${shopVersion.versionDesc }</textarea></td>
		</tr>
		<tr>
			<td class="pn-fcontent" align="center" colspan="2">
				<input type="submit" class="submit" value="提交" />
				<input type="button" class="return-button" value="返回" onclick="shopVersion.backList();" />
			</td>
		</tr>
	</table>
	</form>
</div>
</body>
</html>