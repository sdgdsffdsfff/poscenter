<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="java.util.*,mmb.auth.domain.*,mmb.framework.*" %>
<%
User user = (User) session.getAttribute(IConstants.USER_VIEW_KEY);
pageContext.setAttribute("userName", user.getUserName());
pageContext.setAttribute("menuList", user.getMenuList());
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title>一级菜单</title>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/skin/css/admin.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/skin/css/systop.css" />
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script>
<script>
//选中菜单
function selectedCurrentMenu(obj){
	$(obj).siblings('li:not(.sep)').removeClass('current').addClass('normal');
	$(obj).removeClass('normal').addClass('current');
	window.open($(obj).children().attr('href'), 'leftFrame');
}

//修改密码
function updatePassword(){
	window.open($('#initPath').val()+'/poscenter/auth/user/updatePassword.jsp', 'rightFrame');
}

$(document).ready(function(){
	//默认选中第一个菜单
	if($('#nav_menu_list li').length > 0){
		$('#nav_menu_list li:eq(0)').click();
		window.open($('#nav_menu_list li:eq(0)').find('a').attr('href'), 'leftFrame');
	}
});
</script> 
</head>
<body>
<input type="hidden" id="initPath" value="<%=request.getContextPath()%>" />
<div id="top">
<div class="top">
<table width="100%" cellspacing="0" cellpadding="0" border="0">
<tbody>
<tr>
	<td width="215"><div class="logo"><img width="215" height="69" src="<%=request.getContextPath()%>/css/skin/img/admin/logo3.png" /></div></td>
	<td valign="top">
		<div class="topbg">
			<div class="login-welcome">
				<table width="100%" cellspacing="0" cellpadding="0" border="0">
				<tbody>
				<tr>
					<td width="320" height="38">
						<a href="javascript:;" onclick="updatePassword();"><img src="<%=request.getContextPath()%>/css/skin/img/admin/welconlogin-icon.png" /> ${userName}</a>&nbsp;&nbsp;&nbsp;&nbsp;
						<a href="<%=request.getContextPath()%>/poscenter/desktop/logout.jsp" target="_top"><img src="<%=request.getContextPath()%>/css/skin/img/admin/loginout-icon.png" /> 退出</a>
					</td>
					<td align="right">
						<span style="font-size:20px;">好东东手机娱乐4S店</span>
					</td>
					<td width="150"></td>
				</tr>
				</tbody>
				</table>
			</div>  
			<div class="nav">
				<table width="100%" cellspacing="0" cellpadding="0" border="0">
				<tbody>
				<tr>
					<td width="14" height="31" style="background-image:url('<%=request.getContextPath()%>/css/skin/img/admin/nav-left.png')"></td>
					<td>
						<ul id="nav_menu_list" class="nav-menu">
							<c:forEach var="menu" items="${menuList}">
							<li onclick="selectedCurrentMenu(this);" class="normal">
								<a target="leftFrame" href="<%=request.getContextPath()%>/pos/menu!toSecondLevelMenuListView.do?id=${menu.id}">${menu.menuName}</a>
							</li>
							<li class="sep"></li>
							</c:forEach>
						</ul>
					</td>
				</tr>
				</tbody>
				</table>
			</div>  
		</div>
	</td>
</tr>
</tbody>
</table>
</div>
</div>
<div class="top-bottom"></div>
</body>
</html>