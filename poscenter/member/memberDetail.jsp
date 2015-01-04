<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>会员详细信息</title>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/skin/css/admin.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/skin/css/theme.css" />

<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script>
<script type="text/javascript">
var member={};

//返回列表
member.backList = function(){
	window.location.href = $('#initPath').val()+'/pos/member!memberList.do';
}
</script>
</head>
<body>
<input type="hidden" id="initPath" value="<%=request.getContextPath()%>" />
<div class="box-positon">
	<div class="rpos">当前位置: 会员管理 - 详情</div>
	<div class="ropt"></div>
	<div class="clear"></div>
</div>

<table cellspacing="1" cellpadding="2" border="0" width="100%" class="pn-ftable" style="font-size:13px;">
<tbody>
        <tr>
			<td width="12%" class="pn-flabel pn-flabel-h">会员编号：</td>
			<td class="pn-fcontent">${member.id }</td>
		</tr>
		<tr>
			<td width="12%" class="pn-flabel pn-flabel-h">会员名称：</td>
			<td class="pn-fcontent">${member.name }</td>
		</tr>
		<tr>
			<td class="pn-flabel pn-flabel-h">手机号：</td>
			<td class="pn-fcontent">${member.mobile }</td>
		</tr>
		<tr>
			<td class="pn-flabel pn-flabel-h">身份证号：</td>
			<td class="pn-fcontent">${member.idCard }</td>
		</tr>
		<tr>
			<td class="pn-flabel pn-flabel-h">注册时间：</td>
			<td class="pn-fcontent"><fmt:formatDate value="${member.registerTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
		</tr>
		<tr>
			<td class="pn-flabel pn-flabel-h">更新时间：</td>
			<td class="pn-fcontent"><fmt:formatDate value="${member.changeTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
		</tr>
		<tr>
			<td class="pn-flabel pn-flabel-h">状态：</td>
			<c:if test="${member.useState == 0 }">
		       <td class="pn-fcontent">使用中</td>
		    </c:if>
			<c:if test="${member.useState == 1 }">
		       <td class="pn-fcontent">已注销</td>
		    </c:if>
		    <c:if test="${member.useState == 2 }">
		       <td class="pn-fcontent">新卡</td>
		    </c:if>
		    <c:if test="${member.useState == 3 }">
		       <td class="pn-fcontent">挂失</td>
		    </c:if>
		</tr>
		<tr>
			<td align="center" colspan="2" class="pn-fcontent">
				<input type="button" value="返回" onclick="member.backList();" class="return-button" />
			</td>
		</tr>
</tbody>
</table>
</body>
</html>