<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<script type="text/javascript">
var product={};

//选择
product.selectedProduct = function(obj){
	var productId = $(obj).parent().find('input[name=productId]').val();
	var productName = $(obj).parent().parent().find('td:eq(1)').text();
	$('#productId').val(productId);
	$('#productName').val(productName);
	$('a.jMsgbox-closeWrap').click(); //关闭弹出框
}

//跳转到指定页面
product.goPage = function(pageNum,pageCount){
	var url = $('#initPath').val()+'/pos/Product!toSelectProductListView.do?d=1';
	if(pageNum != null && pageCount != null){
		url += '&page.pageNum='+pageNum;
	}
	if(pageCount != null){
		url += '&page.pageCount='+pageCount;
	}
	$('#productListDiv').load(url, {'product.name':$('#pageSearch').val()});
}

$(document).ready(function(){
	//搜索
	$('#searchBut').click(function(){
		var url = $('#initPath').val()+'/pos/Product!toSelectProductListView.do';
		$('#productListDiv').load(url, {'product.name':$('#pageSearch').val()});
	});
});
</script>
<div id="productListDiv">
<table width="100%" cellpadding="3" cellspacing="1">
	<tr>
		<td>商品名称：
			<input type="text" id="pageSearch" size="20" value="${product.name}" > &nbsp;&nbsp;
			<input id="searchBut" type="button" value="搜索" class="query" />
		</td>
	</tr>
</table>
<table width="100%" cellspacing="1" cellpadding="0" border="0" class="pn-ltable">
<thead class="pn-lthead">
	<tr style="height: 22px;">
		<th>条形码</th>
		<th>商品名称</th>
		<th>商品分类</th>
		<th>销售价格</th>
		<th>操作</th>
	</tr>
</thead>
<tbody class="pn-ltbody">
	<c:forEach var="product" items="${page.list}">
	<tr style="height: 21px;" ondblclick="$(this).find('a').click();">
		<td align="center">${product.barCode }</td>
		<td>${product.name }</td>
		<td>${product.goodsClass.name }</td>
		<td align="right"><fmt:formatNumber value="${product.salePrice }" pattern="#,##0.00" /></td>
		<td align="center">
			<input type="hidden" name="productId" value="${product.id }" />
			<a href="javascript:void(0);" onclick="product.selectedProduct(this);">选择</a>
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
		<a href="javascript:;" onclick="product.goPage(1,${page.pageCount});">首页</a>&nbsp;&nbsp;&nbsp;
		<a href="javascript:;" onclick="product.goPage(${page.pageNum-1},${page.pageCount});">上一页</a>
		</c:if>
		&nbsp;&nbsp;&nbsp;
		<c:if var="isLastPage" test="${page.pageNum>=page.totalPages}">
		下一页&nbsp;&nbsp;&nbsp;尾页&nbsp;&nbsp;&nbsp;
		</c:if>
		<c:if test="${!isLastPage}">
		<a href="javascript:;" onclick="product.goPage(${page.pageNum+1},${page.pageCount});">下一页</a>&nbsp;&nbsp;&nbsp;
		<a href="javascript:;" onclick="product.goPage(${page.totalPages},${page.pageCount});">尾页</a>
		</c:if>
		&nbsp;&nbsp;&nbsp;&nbsp;
		第${page.pageNum}/${page.totalPages}页,共${page.totalRecords}条记录
	</td>
	</tr>
	</tbody>
</table>
</div>