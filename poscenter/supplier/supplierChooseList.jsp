<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<script type="text/javascript">
var supplier={};

//选择
supplier.selectedSupplier = function(obj){
	var supplierId = $(obj).parent().find('input[name=supplierId]').val();
	var supplierName = $(obj).parent().parent().find('td:eq(1)').text();
	$('#supplierId').val(supplierId);
	$('#supplierName').val(supplierName);
	$('a.jMsgbox-closeWrap').click(); //关闭弹出框
}

//跳转到指定页面
supplier.goPage = function(pageNum,pageCount){
	var url = $('#initPath').val()+'/pos/supplier!supplierChooseList.do?';
	if(pageNum != null && pageCount != null){
		url += '&page.pageNum='+pageNum;
	}
	if(pageCount != null){
		url += '&page.pageCount='+pageCount;
	}
	$('#supplierListDiv').load(url, {'page.search':$('#pageSearch').val()});
}

$(document).ready(function(){
	//搜索
	$('#searchBut').click(function(){
		var url = $('#initPath').val()+'/pos/supplier!supplierChooseList.do';
		$('#supplierListDiv').load(url, {'page.search':$('#pageSearch').val()});
	});
});
</script>
<div id="supplierListDiv">
<table width="100%" cellpadding="3" cellspacing="1">
	<tr>
		<td>供应商名称：
			<input type="text" id="pageSearch" name="page.search" size="20" value="${page.search}" > &nbsp;&nbsp;
			<input id="searchBut" type="button" value="搜索" class="query" />
		</td>
	</tr>
</table>
<table width="100%" width="100%" cellspacing="1" cellpadding="0" border="0" class="pn-ltable">
<thead class="pn-lthead">
	<tr style="height: 22px;">
		<th>序号</th>
		<th>供应商名称</th>
		<th>联系电话</th>
		<th>操作</th>
	</tr>
</thead>
<tbody class="pn-ltbody">
	<s:iterator value="list" var="li">
		<tr style="height: 21px;" ondblclick="$(this).find('a').click();">
			<td align="center"><s:property value="#li.id" /></td>
			<td><s:property value="#li.name" /></td>
			<td><s:property value="#li.phone" /></td>
			<td align="center">
				<input type="hidden" name="supplierId" value="<s:property value="#li.id" />" />
				<a href="javascript:void(0);" onclick="supplier.selectedSupplier(this);">选择</a>
			</td>
		</tr>
	</s:iterator>
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
		<a href="javascript:;" onclick="supplier.goPage(1,${page.pageCount});">首页</a>&nbsp;&nbsp;&nbsp;
		<a href="javascript:;" onclick="supplier.goPage(${page.pageNum-1},${page.pageCount});">上一页</a>
		</c:if>
		&nbsp;&nbsp;&nbsp;
		<c:if var="isLastPage" test="${page.pageNum>=page.totalPages}">
		下一页&nbsp;&nbsp;&nbsp;尾页&nbsp;&nbsp;&nbsp;
		</c:if>
		<c:if test="${!isLastPage}">
		<a href="javascript:;" onclick="supplier.goPage(${page.pageNum+1},${page.pageCount});">下一页</a>&nbsp;&nbsp;&nbsp;
		<a href="javascript:;" onclick="supplier.goPage(${page.totalPages},${page.pageCount});">尾页</a>
		</c:if>
		&nbsp;&nbsp;&nbsp;&nbsp;
		第${page.pageNum}/${page.totalPages}页,共${page.totalRecords}条记录
	</td>
	</tr>
	</tbody>
</table>
</div>