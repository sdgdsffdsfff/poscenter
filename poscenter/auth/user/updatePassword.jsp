<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false" %>
<%@ page import="java.util.*,mmb.auth.domain.*,mmb.framework.*" %>
<%@ include file="/taglibs.jsp" %>
<%
User user = (User) session.getAttribute(IConstants.USER_VIEW_KEY);
pageContext.setAttribute("userId", user.getId());
pageContext.setAttribute("userName", user.getUserName());
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>user form</title>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/skin/css/admin.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/skin/css/theme.css" />

<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.validate.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.form.js"></script>
<script type="text/javascript">
$(document).ready(function(){
	//验证表单
	$("#userForm").validate();

	//提交表单
	$('#userForm').submit(function(){
		if(!$('#userForm').valid()){return;}
		$(this).ajaxSubmit({
			dataType: 'text',
			success: function(json){
				if(json == 'success'){
					alert('修改成功！');
					window.history.go(-1);
				}else{
					alert(json);
				}
			},
			error: function(msg){
				alert(msg);
			}
		});
		return false;
	});
});
</script>
</head>
<body style="min-height:500px;">
<input type="hidden" id="initPath" value="<%=request.getContextPath()%>" />
<div class="box-positon">
	<div class="rpos">当前位置: 修改密码</div>
	<div class="ropt"></div>
	<div class="clear"></div>
</div>

<div class="body-box">
<form id="userForm" action="<%=request.getContextPath()%>/pos/user!updatePassword.do" method="post">
	<input type="hidden" name="user.id" value="${userId}" />
	<table cellspacing="1" cellpadding="2" border="0" width="100%" class="pn-ftable" style="font-size:13px;">
		<tr>
			<td width="12%" class="pn-flabel pn-flabel-h">用户名:</td>
			<td width="88%" class="pn-fcontent">${userName}</td>
		</tr>
		<tr>
			<td class="pn-flabel pn-flabel-h"><span class="pn-frequired">*</span>旧密码:</td>
			<td class="pn-fcontent"><input type="password" required="true" name="oldPassword" /></td>
		</tr>
		<tr>
			<td class="pn-flabel pn-flabel-h"><span class="pn-frequired">*</span>新密码:</td>
			<td class="pn-fcontent"><input type="password" required="true" id="newPassword1" name="newPassword" /></td>
		</tr>
		<tr>
			<td class="pn-flabel pn-flabel-h"><span class="pn-frequired">*</span>确认密码:</td>
			<td class="pn-fcontent"><input type="password" required="true" equalTo="#newPassword1" name="user.userPassword" /></td>
		</tr>
		<tr>
			<td class="pn-fcontent" align="center" colspan="2">
				<input type="submit" class="submit" value="提交" />
				<input type="button" class="return-button" value="返回" onclick="window.history.go(-1);" />
			</td>
		</tr>
	</table>
	</form>
</div>
</body>
</html>