<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>导入商品</title>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/skin/css/admin.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/skin/css/theme.css" />

<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.validate.js"></script>

<script type="text/javascript">
var importProduct={};

//表单验证
importProduct.validateForm = function(){
	if($('#importProductForm').valid()){
		return true;
	}else{
		return false;
	}
}

//返回列表
importProduct.backList = function(){
	window.location.href = $('#initPath').val()+'/pos/Product!productList.do';
}

$(document).ready(function(){
	//验证表单
	$("#importProductForm").validate();
});
</script>
</head>
<body style="min-height:500px;">
<input type="hidden" id="initPath" value="<%=request.getContextPath()%>" />
<div class="box-positon">
	<div class="rpos">当前位置: 商品管理 - 导入商品</div>
	<div class="ropt"></div>
	<div class="clear"></div>
</div>

<div class="body-box">
	<form id="importProductForm" action="<%=request.getContextPath()%>/pos/Product!importProduct.do" method="post" onsubmit="return importProduct.validateForm();" enctype="multipart/form-data">
	<table cellspacing="1" cellpadding="2" border="0" width="100%" class="pn-ftable" style="font-size:13px;">
		<tr>
			<td width="20%" class="pn-flabel pn-flabel-h">商品导入文件:</td>
			<td width="80%" class="pn-fcontent">
				<input type="file" name="excel" class="required" />
			</td>
		</tr>
		<tr>
			<td class="pn-flabel pn-flabel-h">提示:</td>
			<td class="pn-fcontent">
				1、点击下载<a href="<%=request.getContextPath()%>/poscenter/product/productTemplate.xls" style="color: blue;">商品Excel导入模板文件</a><br/>
				2、确保商品导入文件与模板文件格式一致<br/>
				3、导入前确保系统中已经存在要导入的商品分类和供应商信息
			</td>
		</tr>
		<tr>
			<td class="pn-fcontent" align="center" colspan="2">
				<input type="submit" class="submit" value="导入" />
				<input type="button" class="return-button" value="返回" onclick="importProduct.backList();" />
			</td>
		</tr>
	</table>
	</form>
</div>
</body>
</html>