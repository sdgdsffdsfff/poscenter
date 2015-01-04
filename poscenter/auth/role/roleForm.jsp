<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>维护角色信息</title>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/skin/css/admin.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/skin/css/theme.css" />
<link rel="STYLESHEET" type="text/css" href="<%=request.getContextPath()%>/js/jquery/msgbox/jquery.msgbox.css" />

<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.validate.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.form.js"></script>

<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/msgbox/jquery.dragndrop.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/msgbox/jquery.msgbox.js"></script>

<script type="text/javascript">
var role={};

//返回列表
role.backList = function(){
	window.location.href = $('#initPath').val()+'/pos/role!roleList.do';
}

$(document).ready(function(){
	//验证表单
	$("#roleForm").validate();
		
	
	//提交表单
	$('#roleForm').submit(function(){
		if(!$('#roleForm').valid()){return;}
		$(this).ajaxSubmit({
			dataType: 'text',
			success: function(json){
				if(json == 'success'){
					alert('保存成功！');
					role.backList();
				}else{
					alert(json);
				}
			},
			error: function(msg){
				alert(msg);
			}
		});

		//(重要)always return false to prevent standard browser submit and page navigation 
		return false;
	});
});

</script>
</head>
<body style="min-height:500px;">
<input type="hidden" id="initPath" value="<%=request.getContextPath()%>" />
<div class="box-positon">
	<div class="rpos">当前位置:权限管理 - <c:if test="${role.id != 0}">修改角色信息</c:if><c:if test="${role.id == 0}">新增角色</c:if></div>
	<div class="ropt"></div>
	<div class="clear"></div>
</div>

<div id="bodyBox" class="body-box">
	<form id="roleForm" action="<%=request.getContextPath()%>/pos/role!saveRole.do" method="post">
	<input type="hidden" id="roleId" name="role.id" value="${role.id }" />
	<table cellspacing="1" cellpadding="2" border="0" width="100%" class="pn-ftable" style="font-size:13px;">
		<tr>
			<td width="12%" class="pn-flabel pn-flabel-h"><span class="pn-frequired">*</span>角色名称:</td>
			<td width="88%" class="pn-fcontent"><input type="text" class="required" id="roleName" name="role.roleName" value="${role.roleName }" /></td>
		</tr>
		<tr>
			<td class="pn-flabel pn-flabel-h">角色中文名称:</td>
			<td class="pn-fcontent"><input type="text"  name="role.roleChName" value="${role.roleChName }" /></td>
		</tr>
		<tr>
			<td class="pn-flabel pn-flabel-h">描述:</td>
			<td class="pn-fcontent">
				<textarea name="role.roleDesc">${role.roleDesc }</textarea>
			</td>
		</tr>
		<tr>
			<td class="pn-fcontent" align="center" colspan="2">
				<input type="submit" class="submit" value="提交" />
				<input type="button" class="return-button" value="返回" onclick="role.backList();" />
			</td>
		</tr>
	</table>
	</form>
</div>
</body>
</html>