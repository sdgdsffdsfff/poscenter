<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>user form</title>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/skin/css/admin.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/skin/css/theme.css" />
<link rel="STYLESHEET" type="text/css" href="<%=request.getContextPath()%>/js/jquery/msgbox/jquery.msgbox.css" />

<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.validate.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/checkForm.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.form.js"></script>


<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/msgbox/jquery.dragndrop.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/msgbox/jquery.msgbox.js"></script>

<script type="text/javascript">
var user={};

//表单验证
user.validateForm = function(){
	if(!$('#userForm').valid()){
		return false;
	}
	
	if($('#userId').val()=='0' || $('#updatePasswordBut').attr('update')=='1'){
		//两次输入密码不相同
		if($('#password1').val() != $('#password2').val()) {
			alert('两次输入密码不相同！');
			return false;
		}
	}
	return true;
}

//返回列表
user.backList = function(){
	window.location.href = $('#initPath').val()+'/pos/user!userList.do';
}

$(document).ready(function(){
	//验证表单
	$("#userForm").validate();
	
	//修改密码按钮
	$("#updatePasswordBut").toggle(
		function (){
			$(this).text('取消修改').attr('update', '1');
			$('#password1').parent().parent().show();
			$('#password2').parent().parent().show();
		},
		function (){
			$(this).text('修改密码').attr('update', '0');
			$('#password1').val('').parent().parent().hide();
			$('#password2').val('').parent().parent().hide();
		}
	);
	
	//选择所属部门
	$('#departmentName').click(function(){
		new $.msgbox({
			title: '选择部门',
			width: 700,
			height: 480,
			type: 'ajax',
			content: $('#initPath').val()+'/pos/department!departmentChooseList.do',
			onAjaxed: function(){
			}
		}).show();
	});
	
	
	//提交表单
	$('#userForm').submit(function(){
		if(!user.validateForm()){return false;}
		$(this).ajaxSubmit({
			dataType: 'text',
			success: function(json){
				if(json == 'success'){
					alert('保存成功！');
					user.backList();
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
	<div class="rpos">当前位置:权限管理 - <c:if test="${user.id != 0}">修改用户信息</c:if><c:if test="${user.id == 0}">新增用户</c:if></div>
	<div class="ropt"></div>
	<div class="clear"></div>
</div>

<div id="bodyBox" class="body-box">
	<form id="userForm" action="<%=request.getContextPath()%>/pos/user!saveUser.do" method="post">
	<input type="hidden" id="userId" name="user.id" value="${user.id }" />
	<input type="hidden" id="departmentId" name="user.departmentId" value="${user.departmentId}" />
	<table cellspacing="1" cellpadding="2" border="0" width="100%" class="pn-ftable" style="font-size:13px;">
		<tr>
			<td width="12%" class="pn-flabel pn-flabel-h"><span class="pn-frequired">*</span>用户名:</td>
			<td width="88%" class="pn-fcontent"><input type="text" class="required" id="userName" name="user.userName" value="${user.userName }" /></td>
		</tr>
		<c:if test="${user.id == 0}">
			<tr>
				<td class="pn-flabel pn-flabel-h"><span class="pn-frequired">*</span>密码:</td>
				<td class="pn-fcontent"><input type="password" class="required" id="password1" name="user.userPassword" /></td>
			</tr>
			<tr>
				<td class="pn-flabel pn-flabel-h"><span class="pn-frequired">*</span>确认密码:</td>
				<td class="pn-fcontent"><input type="password" class="required" id="password2" /></td>
			</tr>
		</c:if>
		<c:if test="${user.id != 0}">
			<tr>
				<td class="pn-flabel pn-flabel-h">密码:</td>
				<td class="pn-fcontent"><a href="javascript:;" id="updatePasswordBut" update="0">修改密码</a></td>
			</tr>
			<tr style="display: none;">
				<td class="pn-flabel pn-flabel-h">新密码:</td>
				<td class="pn-fcontent"><input type="password"  id="password1" name="user.userPassword" /></td>
			</tr>
			<tr style="display: none;">
				<td class="pn-flabel pn-flabel-h">确认密码:</td>
				<td class="pn-fcontent"><input type="password" id="password2" /></td>
			</tr>
		</c:if>
		<tr>
			<td class="pn-flabel pn-flabel-h">真实姓名:</td>
			<td class="pn-fcontent"><input type="text" name="user.userRealName" value="${user.userRealName }" /></td>
		</tr>
		<tr>
			<td class="pn-flabel pn-flabel-h">所属部门:</td>
			<td class="pn-fcontent">
				<input type="text" id="departmentName" name="user.departmentName" readonly="readonly" value="<c:if test="${user.id != 0}">${user.departmentName }</c:if>" />
			</td>
		</tr>
		<c:if test="${user.id != 0}">
		<tr>
			<td class="pn-flabel pn-flabel-h">使用状态:</td>
			<td class="pn-fcontent">
				<input type="radio" name="user.useStatus" value="1" <c:if test="${user.useStatus == 1}">checked="checked"</c:if> />有效
				<input type="radio" name="user.useStatus" value="2" <c:if test="${user.useStatus == 2}">checked="checked"</c:if> />禁用
			</td>
		</tr>
		</c:if>
		<tr>
			<td class="pn-fcontent" align="center" colspan="2">
				<input type="submit" class="submit" value="提交" />
				<input type="button" class="return-button" value="返回" onclick="user.backList();" />
			</td>
		</tr>
	</table>
	</form>
</div>
</body>
</html>