<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>supplier detail</title>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/skin/css/admin.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/skin/css/theme.css" />
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script>
<script type="text/javascript">
var supplier={};

//返回列表
supplier.backList = function(){
	window.location.href = $('#initPath').val()+'/pos/supplier!supplierList.do';
}
</script>
</head>
<body>
<input type="hidden" id="initPath" value="<%=request.getContextPath()%>" />
<input type="hidden" id="supplierId" value="${supplier.id }" />
<div class="box-positon">
	<div class="rpos">当前位置: 供应商管理 - 供应商详情</div>
	<div class="ropt"></div>
	<div class="clear"></div>
</div>

<table cellspacing="1" cellpadding="2" border="0" width="100%" class="pn-ftable" style="font-size:13px;">
<tbody>
	<tr>
		<td width="12%" class="pn-flabel pn-flabel-h">供应商名称：</td>
		<td width="88%" class="pn-fcontent"><span>${supplier.name }</span></td>
	</tr>
	<tr>
		<td class="pn-flabel pn-flabel-h">联系电话：</td>
		<td class="pn-fcontent"><span>${supplier.phone }</span></td>
	</tr>
	<tr>
		<td align="center" colspan="2" class="pn-fcontent">
			<input type="button" value="返回" onclick="supplier.backList();" class="return-button" />
		</td>
	</tr>
</tbody>
</table>
</body>
</html>