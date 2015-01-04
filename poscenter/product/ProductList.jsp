<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false"%>
<%@ include file="/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>商品列表</title>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/skin/css/admin.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/skin/css/theme.css" />
<link rel="STYLESHEET" type="text/css" href="<%=request.getContextPath()%>/js/jquery/msgbox/jquery.msgbox.css" />

<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/msgbox/jquery.dragndrop.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/msgbox/jquery.msgbox.js"></script>

<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/dhtmlxTree/dhtmlxtree.css" />
<script src="<%=request.getContextPath()%>/js/dhtmlxTree/dhtmlxcommon.js"></script>
<script src="<%=request.getContextPath()%>/js/dhtmlxTree/dhtmlxtree.js"></script>
<script type="text/javascript">
var product={};

//新增商品
product.addProduct = function(){
	window.location.href = '<%=request.getContextPath()%>/pos/Product!toProductFormView.do';
}

//删除商品
product.deleteProduct = function(productId){
	if(confirm('确定删除该商品吗？')){
		window.location.href = '<%=request.getContextPath()%>/pos/Product!deleteProduct.do?product.id='+productId;
	}
}

//查看
product.showProduct = function(id){
	new $.msgbox({
		title: '查看商品详情',
		width: 700,
		height: 480,
		type: 'ajax',
		content: $('#initPath').val()+'/pos/Product!toProductDetailView.do?product.id='+id
	}).show();
}

//跳转到指定页面
product.goPage = function(pageNum,pageCount){
	var pageNum = '<input type="hidden" name="page.pageNum" value="'+pageNum+'" />';
	var pageCount = '<input type="hidden" name="page.pageCount" value="'+pageCount+'" />';
	$('#productSearchForm').append(pageNum);
	$('#productSearchForm').append(pageCount);
	$('#productSearchForm').submit();
}

//跳转到导入商品页面
product.toImportProductView = function(){
	window.location.href = $('#initPath').val()+'/poscenter/product/importProduct.jsp';
}

//导出Excel
product.exportExcel = function(){
	window.location.href = $('#initPath').val()+'/pos/Product!exportExcel.do';
}

$(document).ready(function(){
	//选择商品分类
	$('#selectGoodsClassBut').click(function(){
		new $.msgbox({
			title: '选择商品分类',
			width: 300,
			height: 400,
			type: 'ajax',
			content: $('#initPath').val()+'/poscenter/goodsclass/goodsClassTree.jsp?IDS=goodsClassId&NAMES=goodsClassName',
			onAjaxed: function(){
			}
		}).show();
	});
	//手动输入
	$('#goodsClassName').change(function(){
		$('#goodsClassId').val('');
	});
	
	//选择供应商
	$('#selectSupplierBut').click(function(){
		new $.msgbox({
			title: '选择供应商',
			width: 700,
			height: 480,
			type: 'ajax',
			content: $('#initPath').val()+'/pos/supplier!supplierChooseList.do',
			onAjaxed: function(){
			}
		}).show();
	});
	//手动输入
	$('#supplierName').change(function(){
		$('#supplierId').val('0');
	});
});
</script>
</head>
<body style="min-height: 500px;">
<input type="hidden" id="initPath" value="<%=request.getContextPath()%>" />
<div class="box-positon">
	<div class="rpos">当前位置: 商品管理 - 列表</div>
	<div class="ropt">
		<auth:authorize ifGranted="addProduct">
		<input type="button" onclick="product.addProduct();" class="submit" value="新增" /> &nbsp;
		</auth:authorize>
		<auth:authorize ifGranted="importProduct"> 
		<input type="button" onclick="product.toImportProductView();" class="generate-index-page" value="导入商品" /> &nbsp;
		</auth:authorize>
		<input type="button" onclick="product.exportExcel();" class="sendbox" value="导出Excel" /> &nbsp; 
	</div>
	<div class="clear"></div>
</div>

<div id="bodyBox" class="body-box">
<form id="productSearchForm" action="<%=request.getContextPath()%>/pos/Product!productList.do" style="padding-top:5px;" method="post">
<div>
	商品名称:<input type="text" id="productName" name="productName" value="${productName}"/> &nbsp;&nbsp;
	商品分类:<input type="text" id="goodsClassName" name="goodsClassName" value="${goodsClassName}"/><a style="margin-left:2px;" href="javascript:;" id="selectGoodsClassBut"><img alt="分类" src="<%=request.getContextPath()%>/css/skin/img/admin/model-icon.png"/></a>
	<input type="hidden" id="goodsClassId" name="goodsClassId" value="${goodsClassId }" />&nbsp;&nbsp;
	供货商:<input type="text" id="supplierName" name="supplierName" value="${supplierName}" /><a style="margin-left:2px;" href="javascript:;" id="selectSupplierBut"><img alt="供货商" src="<%=request.getContextPath()%>/css/skin/img/admin/model-icon.png"/></a>
	<input type="hidden" id="supplierId" name="supplierId" value="${supplierId}" /> &nbsp;&nbsp;
	<input type="submit" value="查询" class="query" />
</div>
</form>

<table id="goodsList" width="100%" cellspacing="1" cellpadding="0" border="0" class="pn-ltable">
<thead class="pn-lthead">
<tr>
	<th>条形码</th>
	<th>商品名称</th>
	<th>商品分类</th>
	<th>供货商</th>
	<th>标牌价</th>
	<th>限价</th>
	<th>锁价</th>
	<th>红卡额度</th>
	<th>蓝卡额度</th>
	<th>日租赁价格</th>
	<th>包月租赁价格</th>
	<th>押金</th>
	<th>库存量</th>
	<th>操作</th>
</tr>
</thead>
<tbody class="pn-ltbody">
<s:iterator value="list" var="li">
<tr>
	<td align="center"><s:property value="#li.barCode"/></td>
	<td><s:property value="#li.name"/></td>
	<td><s:property value="#li.goodsClass.name"/></td>
	<td><s:property value="#li.supplierName"/></td>
	<td align="right"><s:property value="#li.salePrice"/></td>
	<td align="right"><s:property value="#li.limitPrice"/></td>
	<td align="right"><s:property value="#li.lockPrice"/></td>
	<td align="right"><s:property value="#li.redLines"/></td>
	<td align="right"><s:property value="#li.blueLines"/></td>
	<td align="right"><s:property value="#li.leasePrice"/></td>
	<td align="right"><s:property value="#li.monthLeasePrice"/></td>
	<td align="right"><s:property value="#li.deposit"/></td>
	<td align="right"><s:property value="#li.stock"/></td>
	<td align="center">
		<a href="javascript:;" onclick="product.showProduct(<s:property value="#li.id"/>);">查看</a>
		<auth:authorize ifGranted="updateProduct">
		<a href="<%=request.getContextPath()%>/pos/Product!toProductFormView.do?product.id=<s:property value="#li.id"/>">修改</a>
		</auth:authorize>
		<auth:authorize ifGranted="deleteProduct">
		<a href="javascript:void(0);" onclick="product.deleteProduct(<s:property value="#li.id"/>);">删除</a>
		</auth:authorize>
		<a href="<%=request.getContextPath()%>/pos/Product!toShowBarcodeView.do?product.id=<s:property value="#li.id"/>&product.barCode=<s:property value="#li.barCode"/>&product.name=<s:property value="#li.name"/>">条形码</a>
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
</body>
</html>