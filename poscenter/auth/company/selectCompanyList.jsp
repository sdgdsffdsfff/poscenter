<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<script type="text/javascript">
var selectCompany={};

//选择
selectCompany.selectedCompany = function(obj){
	var companyId = $(obj).parent().find('input[name=companyId]').val();
	var companyName = $(obj).parent().parent().find('td:eq(1)').text();
	$('#companyId').val(companyId);
	$('#companyName').val(companyName);
	$('a.jMsgbox-closeWrap').click(); //关闭弹出框
}

//跳转到指定页面
selectCompany.goPage = function(pageNum,pageCount){
	var url = $('#initPath').val()+'/pos/company!toSelectCompanyListView.do?d=1';
	if(pageNum != null && pageCount != null){
		url += '&page.pageNum='+pageNum;
	}
	if(pageCount != null){
		url += '&page.pageCount='+pageCount;
	}
	$('#companyListDiv').load(url, {'company.name':$('#pageSearch').val()});
}

$(document).ready(function(){
	//搜索
	$('#searchBut').click(function(){
		var url = $('#initPath').val()+'/pos/company!toSelectCompanyListView.do';
		$('#companyListDiv').load(url, {'name':$('#pageSearch').val()});
	});
});
</script>
<div id="companyListDiv">
<table width="100%" cellpadding="3" cellspacing="1">
	<tr>
		<td>商品名称：
			<input type="text" id="pageSearch" size="20" value="${name}" > &nbsp;&nbsp;
			<input id="searchBut" type="button" value="搜索" class="query" />
		</td>
	</tr>
</table>
<table width="100%" cellspacing="1" cellpadding="0" border="0" class="pn-ltable">
<thead class="pn-lthead">
	<tr style="height: 22px;">
		<th>编号</th>
		<th>机构名称</th>
		<th>联系电话</th>
		<th>创建时间</th>
		<th>操作</th>
	</tr>
</thead>
<tbody class="pn-ltbody">
	<c:forEach var="company" items="${page.list}">
	<tr style="height: 21px;" ondblclick="$(this).find('a').click();">
		<td>${company.id }</td>
		<td>${company.name }</td>
		<td>${company.telephone }</td>
		<td align="center"><fmt:formatDate value="${company.createTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
		<td align="center">
			<input type="hidden" name="companyId" value="${company.id }" />
			<a href="javascript:void(0);" onclick="selectCompany.selectedCompany(this);">选择</a>
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
		<a href="javascript:;" onclick="selectCompany.goPage(1,${page.pageCount});">首页</a>&nbsp;&nbsp;&nbsp;
		<a href="javascript:;" onclick="selectCompany.goPage(${page.pageNum-1},${page.pageCount});">上一页</a>
		</c:if>
		&nbsp;&nbsp;&nbsp;
		<c:if var="isLastPage" test="${page.pageNum>=page.totalPages}">
		下一页&nbsp;&nbsp;&nbsp;尾页&nbsp;&nbsp;&nbsp;
		</c:if>
		<c:if test="${!isLastPage}">
		<a href="javascript:;" onclick="selectCompany.goPage(${page.pageNum+1},${page.pageCount});">下一页</a>&nbsp;&nbsp;&nbsp;
		<a href="javascript:;" onclick="selectCompany.goPage(${page.totalPages},${page.pageCount});">尾页</a>
		</c:if>
		&nbsp;&nbsp;&nbsp;&nbsp;
		第${page.pageNum}/${page.totalPages}页,共${page.totalRecords}条记录
	</td>
	</tr>
	</tbody>
</table>
</div>