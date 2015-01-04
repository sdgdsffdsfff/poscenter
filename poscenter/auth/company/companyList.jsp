<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>机构列表</title>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/skin/css/admin.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/skin/css/theme.css" />

<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script>
<script type="text/javascript">
var company={};

//新增
company.addCompany = function(){
	window.location.href = $('#initPath').val()+'/pos/company!toCompanyFormView.do';
}

//删除
company.deletecompany = function(companyId){
	if(confirm('确定删除该机构吗？')){
		$.post($('#initPath').val()+'/pos/company!deleteCompany.do', {'company.id': companyId}, function(json){
			if(json == 'success'){
				alert('删除成功！');
				window.location.href = $('#initPath').val()+'/pos/company!companyList.do';
			}else{
				alert(json);
			}
		});
	}
}

//跳转到指定页面
company.goPage = function(pageNum,pageCount){
	var pageNum = '<input type="hidden" name="page.pageNum" value="'+pageNum+'" />';
	var pageCount = '<input type="hidden" name="page.pageCount" value="'+pageCount+'" />';
	$('#companySearchForm').append(pageNum);
	$('#companySearchForm').append(pageCount);
	$('#companySearchForm').submit();
}
</script>
</head>
<body>
<input type="hidden" id="initPath" value="<%=request.getContextPath()%>" />
<div class="box-positon">
	<div class="rpos">当前位置: 机构管理 - 列表</div>
	<div class="ropt">
		<input type="button" onclick="company.addCompany();" class="submit" value="新增" /> &nbsp; 
	</div>
	<div class="clear"></div>
</div>

<div id="bodyBox" class="body-box">
<form id="companySearchForm" action="<%=request.getContextPath()%>/pos/company!companyList.do" style="padding-top:5px;" method="post">
<div>
	机构名称:<input type="text" name="name" value="${name}"/> &nbsp;&nbsp;
	<input type="submit" value="查询" class="query" />
</div>
</form>

<table width="100%" cellspacing="1" cellpadding="0" border="0" style="" class="pn-ltable">
<thead class="pn-lthead">
<tr>
	<th>机构名称</th>
	<th>联系电话</th>
	<th>创建时间</th>
	<th>操作</th>
</tr>
</thead>
<tbody class="pn-ltbody">
<c:forEach var="company" items="${page.list}">
<tr>
	<td>${company.name }</td>
	<td>${company.telephone }</td>
	<td align="center"><fmt:formatDate value="${company.createTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
	<td align="center">
		<a href="<%=request.getContextPath()%>/pos/company!toCompanyFormView.do?company.id=${company.id }">修改</a>
		<a href="javascript:void(0);" onclick="company.deletecompany(${company.id });">删除</a>
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
		<a href="javascript:;" onclick="company.goPage(1,${page.pageCount});">首页</a>&nbsp;&nbsp;&nbsp;
		<a href="javascript:;" onclick="company.goPage(${page.pageNum-1},${page.pageCount});">上一页</a>
		</c:if>
		&nbsp;&nbsp;&nbsp;
		<c:if var="isLastPage" test="${page.pageNum>=page.totalPages}">
		下一页&nbsp;&nbsp;&nbsp;尾页&nbsp;&nbsp;&nbsp;
		</c:if>
		<c:if test="${!isLastPage}">
		<a href="javascript:;" onclick="company.goPage(${page.pageNum+1},${page.pageCount});">下一页</a>&nbsp;&nbsp;&nbsp;
		<a href="javascript:;" onclick="company.goPage(${page.totalPages},${page.pageCount});">尾页</a>
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