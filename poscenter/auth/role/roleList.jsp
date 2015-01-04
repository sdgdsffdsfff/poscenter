<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>角色列表</title>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/skin/css/admin.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/skin/css/theme.css" />

<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script>
<script type="text/javascript">
var role={};

//新增
role.addRole = function(){
	window.location.href = $('#initPath').val()+'/pos/role!toRoleFormView.do';
}

//删除
role.deleteRole = function(roleId){
	if(confirm('删除角色后，该角色下的权限信息和用户角色信息也将被删除，确定删除该角色吗？')){
		window.location.href = $('#initPath').val()+'/pos/role!deleteRole.do?role.id='+roleId;
	}
}

//跳转到指定页面
role.goPage = function(pageNum,pageCount){
	var pageNum = '<input type="hidden" name="page.pageNum" value="'+pageNum+'" />';
	var pageCount = '<input type="hidden" name="page.pageCount" value="'+pageCount+'" />';
	$('#roleSearchForm').append(pageNum);
	$('#roleSearchForm').append(pageCount);
	$('#roleSearchForm').submit();
}
</script>
</head>
<body>
<input type="hidden" id="initPath" value="<%=request.getContextPath()%>" />
<div class="box-positon">
	<div class="rpos">当前位置: 角色管理 - 列表</div>
	<div class="ropt">
		<input type="button" onclick="role.addRole();" class="submit" value="新增" /> &nbsp; 
	</div>
	<div class="clear"></div>
</div>

<div id="bodyBox" class="body-box">
<form id="roleSearchForm" action="<%=request.getContextPath()%>/pos/role!roleList.do" style="padding-top:5px;" method="post">
<div>
	角色名称:<input type="text" name="roleName" value="${roleName}"/> &nbsp;&nbsp;
	角色中文名称:<input type="text" name="roleChName" value="${roleChName}"/> &nbsp;&nbsp;
	<input type="submit" value="查询" class="query" />
</div>
</form>

<table width="100%" cellspacing="1" cellpadding="0" border="0" style="" class="pn-ltable">
<thead class="pn-lthead">
<tr>
	<th>角色名称</th>
	<th>角色中文名称</th>
	<th>描述</th>
	<th>创建时间</th>
	<th>操作</th>
</tr>
</thead>
<tbody class="pn-ltbody">
<c:forEach var="role" items="${page.list}">
<tr>
	<td>${role.roleName }</td>
	<td>${role.roleChName }</td>
	<td>${role.roleDesc }</td>
	<td align="center"><fmt:formatDate value="${role.createTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
	<td align="center">
	    <a href="<%=request.getContextPath()%>/pos/role!roleResourceListView.do?roleId=${role.id }">分配权限</a>
	    <c:if test="${role.roleName != 'admin'}">
		<a href="<%=request.getContextPath()%>/pos/role!toRoleFormView.do?role.id=${role.id }">修改</a>
		<a href="javascript:void(0);" onclick="role.deleteRole(${role.id });">删除</a>
	    </c:if>
	</td>
</tr>
</c:forEach>
</tbody>
</table>

<table width="100%" style="font-size:13px;">
	<tbody>
	<tr>
		<td align="center" class="pn-sp">
		<c:if var="isFirstPage" test="${page.pageNum<=1}">
		首页 &nbsp;&nbsp;&nbsp;上一页&nbsp;&nbsp;&nbsp;
		</c:if>
		<c:if test="${!isFirstPage}">
		<a href="javascript:;" onclick="user.goPage(1,${page.pageCount});">首页</a>&nbsp;&nbsp;&nbsp;
		<a href="javascript:;" onclick="user.goPage(${page.pageNum-1},${page.pageCount});">上一页</a>
		</c:if>
		&nbsp;&nbsp;&nbsp;
		<c:if var="isLastPage" test="${page.pageNum>=page.totalPages}">
		下一页&nbsp;&nbsp;&nbsp;尾页&nbsp;&nbsp;&nbsp;
		</c:if>
		<c:if test="${!isLastPage}">
		<a href="javascript:;" onclick="user.goPage(${page.pageNum+1},${page.pageCount});">下一页</a>&nbsp;&nbsp;&nbsp;
		<a href="javascript:;" onclick="user.goPage(${page.totalPages},${page.pageCount});">尾页</a>
		</c:if>
		&nbsp;&nbsp;&nbsp;&nbsp;
		第${page.pageNum}/${page.totalPages}页,共${page.totalRecords}条记录
	</td>
	</tr>
	</tbody>
</table>
</div>
</body>
</html>