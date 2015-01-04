<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<script type="text/javascript">
var department={};

//选择
department.selectedDepartment = function(obj){
	var departmentId = $(obj).parent().find('input[name=departmentId]').val();
	var departmentName = $(obj).parent().parent().find('td:eq(1)').text();
	$('#departmentId').val(departmentId);
	$('#departmentName').val(departmentName);
	$('a.jMsgbox-closeWrap').click(); //关闭弹出框
}

//跳转到指定页面
department.goPage = function(pageNum,pageCount){
	var url = $('#initPath').val()+'/pos/department!departmentChooseList.do?d=1';
	if(pageNum != null && pageCount != null){
		url += '&page.pageNum='+pageNum;
	}
	if(pageCount != null){
		url += '&page.pageCount='+pageCount;
	}
	$('#departmentListDiv').load(url, {'name':$('#name').val(),'companyName':$('#companyName').val()});
}

$(document).ready(function(){
	//搜索
	$('#searchBut').click(function(){
		var url = $('#initPath').val()+'/pos/department!departmentChooseList.do';
		$('#departmentListDiv').load(url, {'name':$('#name').val(),'companyName':$('#companyName').val()});
	});
});
</script>
<div id="departmentListDiv">
<div id="bodyBox" class="body-box">
<form id="departmentSearchForm" action="<%=request.getContextPath()%>/pos/department!departmentChooseList.do" style="padding-top:5px;" method="post">
<div>
	部门名称:<input type="text" id="name" name="name" value="${name}"/> &nbsp;&nbsp;
	机构名称:<input type="text" id="companyName" name="companyName" value="${companyName}"/> &nbsp;&nbsp;
	<input type="button" id="searchBut" value="查询" class="query" />
</div>
</form>

<table width="100%" cellspacing="1" cellpadding="0" border="0" style="" class="pn-ltable">
<thead class="pn-lthead">
<tr>
	<th>编号</th>
	<th>部门名称</th>
	<th>所属机构</th>
	<th>备注</th>
	<th>创建时间</th>
	<th>操作</th>
</tr>
</thead>
<tbody class="pn-ltbody">
<c:forEach var="department" items="${page.list}">
<tr style="height:21px">
	<td align="center">${department.id }</td>
	<td>${department.name }</td>
	<td>${department.company.name }</td>
	<td>${department.remark }</td>
	<td align="center"><fmt:formatDate value="${department.createTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
	<td align="center">
	    <input type="hidden" name="departmentId" value="${department.id }" />
		<a href="javascript:void(0);" onclick="department.selectedDepartment(this);">选择</a>
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
		<a href="javascript:;" onclick="department.goPage(1,${page.pageCount});">首页</a>&nbsp;&nbsp;&nbsp;
		<a href="javascript:;" onclick="department.goPage(${page.pageNum-1},${page.pageCount});">上一页</a>
		</c:if>
		&nbsp;&nbsp;&nbsp;
		<c:if var="isLastPage" test="${page.pageNum>=page.totalPages}">
		下一页&nbsp;&nbsp;&nbsp;尾页&nbsp;&nbsp;&nbsp;
		</c:if>
		<c:if test="${!isLastPage}">
		<a href="javascript:;" onclick="department.goPage(${page.pageNum+1},${page.pageCount});">下一页</a>&nbsp;&nbsp;&nbsp;
		<a href="javascript:;" onclick="department.goPage(${page.totalPages},${page.pageCount});">尾页</a>
		</c:if>
		&nbsp;&nbsp;&nbsp;&nbsp;
		第${page.pageNum}/${page.totalPages}页,共${page.totalRecords}条记录
	</td>
	</tr>
	</tbody>
</table>
</div>
</div>