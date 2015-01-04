<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false"%>
<%@ include file="/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>会员列表</title>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/skin/css/admin.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/skin/css/theme.css" />

<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.validate.js"></script>
<script type="text/javascript">
var member={};

//表单验证
member.validateForm = function(){
	if(!$('#memberForm').valid()){return false;}
	return true;
}

member.search = function(){
    var memberId = $('#memberId').val();
    var memberName = $('#memberName').val();
    var url = $('#initPath').val()+'/pos/member!memberList.do?memberId='+memberId+'&memberName='+memberName;
    window.location.href = url;
}

//跳转到指定页面
member.goPage = function(pageNum,pageCount){
var memberId = $('#memberId').val();
    var memberName = $('#memberName').val();
	var url = $('#initPath').val()+'/pos/member!memberList.do?d=1&memberId='+memberId+'&memberName='+memberName;
	if(pageNum != null && pageCount != null){
		url += '&page.pageNum='+pageNum;
	}
	if(pageCount != null){
		url += '&page.pageCount='+pageCount;
	}
	window.location.href = url;
}

$(document).ready(function(){
	//验证表单
	$("#memberForm").validate();
});
</script>
</head>
<body>
<input type="hidden" id="initPath" value="<%=request.getContextPath()%>" />
<div class="box-positon">
	<div class="rpos">当前位置: 会员管理 - 列表</div>
	<div class="ropt">
	</div>
	<div class="clear"></div>
</div>

<div id="bodyBox" class="body-box">
<div>
<table><tr><td>
<form id="memberForm" name="memberForm" action="<%=request.getContextPath()%>/pos/member!batImportMember.do" method="post"  enctype="multipart/form-data" style="padding-top:5px;" onsubmit="return member.validateForm();">
	<auth:authorize ifGranted="importMember">
	批量导入会员信息: <input type="file" class="required"  name="importFile" id="importFile"  />
	<input type="submit" value="导入" />(只能导入txt文件)&nbsp;&nbsp;&nbsp;
	</auth:authorize>
</form>
</td><td>
<form name="searchForm" action="<%=request.getContextPath()%>/pos/member!memberList.do" method="post" >
	会员卡号:&nbsp;<input type="text" id="memberId" name="memberId" value="${memberId}">&nbsp;&nbsp;&nbsp;
	会员名称:&nbsp;<input type="text" id="memberName" name="memberName" value="${memberName}">&nbsp;&nbsp;&nbsp;
	<input id="queryBut" type="submit" class="query" value="查询" />
</form>
</td>
</tr>
</table>
</div>
<table width="100%" cellspacing="1" cellpadding="0" border="0" class="pn-ltable">
<thead class="pn-lthead">
      <tr>
		<th>会员编号</th>
		<th>会员名称</th>
		<th>手机号</th>
		<th>身份证号</th>
		<th>注册时间</th>
		<th>更改时间</th>
		<th>状态</th>
		<th>操作</th>
	</tr>
</thead>
<tbody class="pn-ltbody">
	<c:forEach var="member" items="${list}">
		<tr>
			<td align="center">${member.id }</td>
			<td align="center">${member.name }</td>
			<td align="center">${member.mobile }</td>
			<td align="center">${member.idCard }</td>
			<td align="center"><fmt:formatDate value="${member.registerTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
			<td align="center"><fmt:formatDate value="${member.changeTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
			<c:if test="${member.useState == 0 }">
		       <td align="center">使用中</td>
		    </c:if>
			<c:if test="${member.useState == 1 }">
		       <td align="center">已注销</td>
		    </c:if>
		    <c:if test="${member.useState == 2 }">
		       <td align="center">新卡</td>
		    </c:if>
		    <c:if test="${member.useState == 3 }">
		       <td align="center">挂失</td>
		    </c:if>
			<td align="center">
				<a href="<%=request.getContextPath()%>/pos/member!toMemberDetailView.do?member.id=${member.id }">查看</a>
			</td>
		</tr>
	</c:forEach>
</tbody>
</table>

<table id="dtPageTable" width="100%" style="font-size:13px;">
	<tbody>
	<tr>
		<td align="center" class="pn-sp">
		<c:if var="isFirstPage" test="${page.pageNum<=1}">
		首页 &nbsp;&nbsp;&nbsp;上一页&nbsp;&nbsp;&nbsp;
		</c:if>
		<c:if test="${!isFirstPage}">
		<a href="javascript:;" onclick="member.goPage(1,${page.pageCount});">首页</a>&nbsp;&nbsp;&nbsp;
		<a href="javascript:;" onclick="member.goPage(${page.pageNum-1},${page.pageCount});">上一页</a>
		</c:if>
		&nbsp;&nbsp;&nbsp;
		<c:if var="isLastPage" test="${page.pageNum>=page.totalPages}">
		下一页&nbsp;&nbsp;&nbsp;尾页&nbsp;&nbsp;&nbsp;
		</c:if>
		<c:if test="${!isLastPage}">
		<a href="javascript:;" onclick="member.goPage(${page.pageNum+1},${page.pageCount});">下一页</a>&nbsp;&nbsp;&nbsp;
		<a href="javascript:;" onclick="member.goPage(${page.totalPages},${page.pageCount});">尾页</a>
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