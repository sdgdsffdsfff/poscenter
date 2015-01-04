<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>shop detail</title>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/skin/css/admin.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/skin/css/theme.css" />

<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script>
<script type="text/javascript">
var shop={};

//返回列表
shop.backList = function(){
	window.location.href = $('#initPath').val()+'/pos/shop!shopList.do';
}
</script>
</head>
<body>
<input type="hidden" id="initPath" value="<%=request.getContextPath()%>" />
<div class="box-positon">
	<div class="rpos">当前位置: 店面管理 - 店面详情</div>
	<div class="ropt"></div>
	<div class="clear"></div>
</div>

<table cellspacing="1" cellpadding="2" border="0" width="100%" class="pn-ftable" style="font-size:13px;">
<tbody>
	<tr>
		<td width="12%" class="pn-flabel pn-flabel-h">店面编号：</td>
		<td width="88%" class="pn-fcontent"><span>${shop.code}</span></td>
	</tr>
	<tr>
		<td width="12%" class="pn-flabel pn-flabel-h">店面名称：</td>
		<td width="88%" class="pn-fcontent"><span>${shop.name }</span></td>
	</tr>
	<tr>
		<td class="pn-flabel pn-flabel-h">店面负责人：</td>
		<td class="pn-fcontent"><span>${shop.charger}</span></td>
	</tr>
	<tr>
		<td class="pn-flabel pn-flabel-h">店面地址：</td>
		<td class="pn-fcontent"><span>${shop.address }</span></td>
	</tr>
	<tr>
		<td align="center" colspan="2" class="pn-fcontent">
			<input type="button" value="返回" onclick="shop.backList();" class="return-button" />
		</td>
	</tr>
</tbody>
</table>
</body>
</html>