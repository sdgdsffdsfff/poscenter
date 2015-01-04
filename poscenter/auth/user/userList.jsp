<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>用户列表</title>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/skin/css/admin.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/skin/css/theme.css" />
<link rel="STYLESHEET" type="text/css" href="<%=request.getContextPath()%>/js/jquery/msgbox/jquery.msgbox.css" />

<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/msgbox/jquery.dragndrop.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/msgbox/jquery.msgbox.js"></script>
<script type="text/javascript">
var user={};

//新增
user.addUser = function(){
	window.location.href = $('#initPath').val()+'/pos/user!toUserFormView.do';
}

//删除
user.deleteUser = function(userId){
	if(confirm('确定删除该用户吗？')){
		window.location.href = $('#initPath').val()+'/pos/user!deleteUser.do?user.id='+userId;
	}
}

//跳转到指定页面
user.goPage = function(pageNum,pageCount){
	var pageNum = '<input type="hidden" name="page.pageNum" value="'+pageNum+'" />';
	var pageCount = '<input type="hidden" name="page.pageCount" value="'+pageCount+'" />';
	$('#userSearchForm').append(pageNum);
	$('#userSearchForm').append(pageCount);
	$('#userSearchForm').submit();
}

//分配角色
user.allotRole = function(userId){
	new $.msgbox({
		title: '分配角色',
		width: 700,
		height: 480,
		type: 'ajax',
		content: $('#initPath').val()+'/pos/role!toAllotRoleView.do?userId='+userId
	}).show();
}

//查看
user.showUserDetail = function(userId, departmentName){
	new $.msgbox({
		title: '查看用户详情',
		width: 700,
		height: 480,
		type: 'ajax',
		content: $('#initPath').val()+'/pos/user!toUserDetailView.do?user.id='+userId+'&user.departmentName='+departmentName
	}).show();
}
</script>
</head>
<body style="min-height:500px;">
<input type="hidden" id="initPath" value="<%=request.getContextPath()%>" />
<div class="box-positon">
	<div class="rpos">当前位置: 用户管理 - 列表</div>
	<div class="ropt">
		<input type="button" onclick="user.addUser();" class="submit" value="新增" /> &nbsp; 
	</div>
	<div class="clear"></div>
</div>

<div id="bodyBox" class="body-box">
<form id="userSearchForm" action="<%=request.getContextPath()%>/pos/user!userList.do" style="padding-top:5px;" method="post">
<div>
	用户名称:<input type="text" name="userName" value="${userName}"/> &nbsp;&nbsp;
	部门名称:<input type="text" name="departmentName" value="${departmentName}"/> &nbsp;&nbsp;
	<input type="submit" value="查询" class="query" />
</div>
</form>

<table width="100%" cellspacing="1" cellpadding="0" border="0" style="" class="pn-ltable">
<thead class="pn-lthead">
<tr>
	<th>用户名称</th>
	<th>部门名称</th>
	<th>真实姓名</th>
	<th>使用状态</th>
	<th>创建时间</th>
	<th>操作</th>
</tr>
</thead>
<tbody class="pn-ltbody">
<c:forEach var="user" items="${page.list}">
<tr>
	<td>${user.userName }</td>
	<td>${user.departmentName }</td>
	<td>${user.userRealName }</td>
	<td align="center"> 
		<c:if test="${user.useStatus == 1 }">有效</c:if>
		<c:if test="${user.useStatus == 2 }">禁用</c:if>
	</td>
	<td align="center"><fmt:formatDate value="${user.createTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
	<td align="center">
	    <a href="javascript:;" onclick="user.allotRole(${user.id });">分配角色</a>
	    <a href="javascript:;" onclick="user.showUserDetail(${user.id },'${user.departmentName }');">查看</a>
	    <c:if test="${user.userName != 'admin'}">
		<a href="<%=request.getContextPath()%>/pos/user!toUserFormView.do?user.id=${user.id }&user.departmentName=${user.departmentName }">修改</a>
		<a href="javascript:void(0);" onclick="user.deleteUser(${user.id });">删除</a>
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