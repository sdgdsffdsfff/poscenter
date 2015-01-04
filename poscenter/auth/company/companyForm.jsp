<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>维护机构信息</title>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/skin/css/admin.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/skin/css/theme.css" />

<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.validate.js"></script>

<script type="text/javascript">
var company={};

//表单验证
company.validateForm = function(){
	if(!$('#companyForm').valid()){return false;}
	return true;
}

//返回列表
company.backList = function(){
	window.location.href = $('#initPath').val()+'/pos/company!companyList.do';
}

$(document).ready(function(){
	//验证表单
	$("#companyForm").validate({
		rules: {
			'company.name': {
				required: true,
				remote: {
					url: $('#initPath').val()+"/pos/company!isUniqueCompanyName.do",
					type: "post",
					dataType: "json",
					data: {
						'company.name': function(){ return $('#companyName').val(); },
						'company.id': $('#companyId').val()
					}
				}
			}
		},
		messages: {
			'company.name': {
				remote: "机构名称已经存在!"
			}
		}
	});
});
</script>
</head>
<body style="min-height:500px;">
<input type="hidden" id="initPath" value="<%=request.getContextPath()%>" />
<div class="box-positon">
	<div class="rpos">当前位置: 机构管理 - <c:if test="${company.id != 0}">修改机构信息</c:if><c:if test="${company.id == 0}">新增机构</c:if></div>
	<div class="ropt"></div>
	<div class="clear"></div>
</div>

<div id="bodyBox" class="body-box">
	<form id="companyForm" action="<%=request.getContextPath()%>/pos/company!saveCompany.do" method="post" onsubmit="return company.validateForm();">
	<input type="hidden" id="companyId" name="company.id" value="${company.id }" />
	<table cellspacing="1" cellpadding="2" border="0" width="100%" class="pn-ftable" style="font-size:13px;">
		<tr>
			<td width="12%" class="pn-flabel pn-flabel-h"><span class="pn-frequired">*</span>机构名称:</td>
			<td width="88%" class="pn-fcontent"><input type="text" class="required" id="companyName" name="company.name" value="${company.name }" /></td>
		</tr>
		<tr>
			<td class="pn-flabel pn-flabel-h">联系电话:</td>
			<td class="pn-fcontent"><input type="text" name="company.telephone" value="${company.telephone }" /></td>
		</tr>
		<tr>
			<td class="pn-fcontent" align="center" colspan="2">
				<input type="submit" class="submit" value="提交" />
				<input type="button" class="return-button" value="返回" onclick="company.backList();" />
			</td>
		</tr>
	</table>
	</form>
</div>
</body>
</html>