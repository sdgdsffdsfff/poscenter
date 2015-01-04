<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>维护部门信息</title>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/skin/css/admin.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/skin/css/theme.css" />
<link rel="STYLESHEET" type="text/css" href="<%=request.getContextPath()%>/js/jquery/msgbox/jquery.msgbox.css" />

<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.validate.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.form.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/msgbox/jquery.dragndrop.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/msgbox/jquery.msgbox.js"></script>

<script type="text/javascript">
var department={};

//返回列表
department.backList = function(){
	window.location.href = $('#initPath').val()+'/pos/department!departmentList.do';
}

$(document).ready(function(){
	//验证表单
	$("#departmentForm").validate();

	//选择机构
	$('#companyName').click(function(){
		new $.msgbox({
			title: '选择机构',
			width: 700,
			height: 480,
			type: 'ajax',
			content: $('#initPath').val()+'/pos/company!toSelectCompanyListView.do'
		}).show();
	});

	//提交表单
	$('#departmentForm').submit(function(){
		if(!$('#departmentForm').valid()){return;}
		$(this).ajaxSubmit({
			dataType: 'text',
			success: function(json){
				if(json == 'success'){
					department.backList();
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
	<div class="rpos">当前位置: 部门管理 - <c:if test="${department.id != 0}">修改部门信息</c:if><c:if test="${department.id == 0}">新增部门</c:if></div>
	<div class="ropt"></div>
	<div class="clear"></div>
</div>

<div id="bodyBox" class="body-box">
	<form id="departmentForm" action="<%=request.getContextPath()%>/pos/department!saveDepartment.do" method="post">
	<input type="hidden" name="department.id" value="${department.id }" />
	<table cellspacing="1" cellpadding="2" border="0" width="100%" class="pn-ftable" style="font-size:13px;">
		<tr>
			<td width="12%" class="pn-flabel pn-flabel-h"><span class="pn-frequired">*</span>部门名称:</td>
			<td width="88%" class="pn-fcontent"><input type="text" class="required" name="department.name" value="${department.name }" /></td>
		</tr>
		<tr>
			<td class="pn-flabel pn-flabel-h"><span class="pn-frequired">*</span>所属机构:</td>
			<td class="pn-fcontent">
				<input type="hidden" id="companyId" name="department.companyId" value="${department.companyId }" />
				<input type="text" class="required" id="companyName" value="${department.company.name }" readonly="readonly" />
			</td>
		</tr>
		<tr>
			<td class="pn-flabel pn-flabel-h">备注:</td>
			<td class="pn-fcontent"><textarea name="department.remark">${department.remark }</textarea></td>
		</tr>
		<tr>
			<td class="pn-fcontent" align="center" colspan="2">
				<input type="submit" class="submit" value="提交" />
				<input type="button" class="return-button" value="返回" onclick="department.backList();" />
			</td>
		</tr>
	</table>
	</form>
</div>
</body>
</html>