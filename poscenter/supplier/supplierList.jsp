<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false"%>
<%@ include file="/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>供应商列表</title>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/skin/css/admin.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/skin/css/theme.css" />
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script>
<script type="text/javascript">
var supplier={};

//新增供应商
supplier.addSupplier = function(){
	window.location.href = '<%=request.getContextPath()%>/pos/supplier!toSupplierFormView.do';
}

//删除
supplier.deleteSupplier = function(supplierId){
	if(confirm('确定删除该供应商吗？')){
		$.get($('#initPath').val()+'/pos/supplier!deleteSupplier.do', {'supplier.id': supplierId}, function(json){
			if(json == 'success'){
				window.location.href = $('#initPath').val()+'/pos/supplier!supplierList.do';
			}else{
				alert(json);
			}
		});
	}
}

//跳转到指定页面
supplier.goPage = function(pageNum,pageCount){
	var url = $('#initPath').val()+'/pos/supplier!supplierList.do?page.search='+$('#pageSearch').val();
	if(pageNum != null && pageCount != null){
		url += '&page.pageNum='+pageNum;
	}
	if(pageCount != null){
		url += '&page.pageCount='+pageCount;
	}
	window.location.href = url;
}
</script>
</head>
<body>
<input type="hidden" id="initPath" value="<%=request.getContextPath()%>" />
<div class="box-positon">
	<div class="rpos">当前位置: 供应商管理 - 列表</div>
	<div class="ropt">
		<auth:authorize ifGranted="addSupplier">
		<input type="button" onclick="supplier.addSupplier();" class="submit" value="新增" /> &nbsp; 
		</auth:authorize>
	</div>
	<div class="clear"></div>
</div>

<div id="bodyBox" class="body-box">
<form action="<%=request.getContextPath()%>/pos/supplier!supplierList.do" method="post" style="padding-top:5px;">
<div>
	供应商名称: <input type="text" id="pageSearch" name="page.search" value="${page.search}" />
	<input type="submit" value="查询" class="query" />
</div>
</form>

<table id="goodsList" width="100%" cellspacing="1" cellpadding="0" border="0" style="" class="pn-ltable">
<thead class="pn-lthead">
<tr>
	<th>供应商名称</th>
	<th>联系电话</th>
	<th>操作</th>
</tr>
</thead>
<tbody class="pn-ltbody">
<s:iterator value="list" var="li">
<tr>
	<td><s:property value="#li.name"/></td>
	<td><s:property value="#li.phone"/></td>
	<td align="center">
		<a href="<%=request.getContextPath()%>/pos/supplier!toSupplierDetailView.do?supplier.id=<s:property value="#li.id"/>">查看</a>
		<auth:authorize ifGranted="updateSupplier">
		<a href="<%=request.getContextPath()%>/pos/supplier!toSupplierFormView.do?supplier.id=<s:property value="#li.id"/>">修改</a>
		</auth:authorize>
		<auth:authorize ifGranted="deleteSupplier">
		<a href="javascript:void(0);" onclick="supplier.deleteSupplier(<s:property value="#li.id"/>);">删除</a>
		</auth:authorize>
	</td>
</tr>
</s:iterator>
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
</body>
</html>
