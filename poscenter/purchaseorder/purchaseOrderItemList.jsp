<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false"%>
<%@ include file="/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>采购单条目列表</title>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/skin/css/admin.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/skin/css/theme.css" />

<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script>
<script type="text/javascript">
var purchaseOrderItem={};

//新增采购单
purchaseOrderItem.addPurchaseOrderItem = function(){
	window.location.href = $('#initPath').val()+'/pos/purchaseOrderItem!toPurchaseOrderItemFormView.do?purchaseOrderId='+$('#purchaseOrderId').val();
}

//删除采购单
purchaseOrderItem.deletePurchaseOrderItem = function(purchaseOrderItemId){
	if(confirm('确定删除该采购单条目吗？')){
		window.location.href = $('#initPath').val()+'/pos/purchaseOrderItem!deletePurchaseOrderItem.do?purchaseOrderItem.id='+purchaseOrderItemId;
	}
}

//跳转到指定页面
purchaseOrderItem.goPage = function(pageNum,pageCount){
	var url = $('#initPath').val()+'/pos/purchaseOrderItem!purchaseOrderItemList.do?purchaseOrderId='+$('#purchaseOrderId').val();
	if(pageNum != null && pageCount != null){
		url += '&page.pageNum='+pageNum;
	}
	if(pageCount != null){
		url += '&page.pageCount='+pageCount;
	}
	window.location.href = url;
}

//返回列表
purchaseOrderItem.backList = function(){
	window.location.href = $('#initPath').val()+'/pos/purchaseOrder!purchaseOrderList.do';
}

//提交采购单
purchaseOrderItem.submitPurchaseOrder = function(purchaseOrderId){
	if(confirm('提交后将不能修改采购单信息，确定提交该采购单吗？')){
		window.location.href = $('#initPath').val()+'/pos/purchaseOrder!submitPurchaseOrder.do?purchaseOrder.id='+purchaseOrderId;
	}
}

//导出Excel
purchaseOrderItem.exportExcel = function(){
	window.location.href = $('#initPath').val()+'/pos/purchaseOrder!exportExcel.do?purchaseOrder.id='+$('#purchaseOrderId').val();
}
</script>
</head>
<body>
<input type="hidden" id="initPath" value="<%=request.getContextPath()%>" />
<div class="box-positon">
	<div class="rpos">当前位置: 采购单管理 - 采购单条目列表</div>
	<div class="ropt">
		<c:if test="${purchaseOrder.useStatus == 0}">
			<auth:authorize ifGranted="addPurchaseOrderItem">
			<input type="button" onclick="purchaseOrderItem.addPurchaseOrderItem();" class="submit" value="新增" /> &nbsp; 
			</auth:authorize>
			<c:if test="${page.totalRecords > 0}">
			<auth:authorize ifGranted="submitPurchaseOrder">
			<input type="button" onclick="purchaseOrderItem.submitPurchaseOrder(${purchaseOrder.id});" class="" value="提交采购单" /> &nbsp;
			</auth:authorize> 
			</c:if>
		</c:if>
		<c:if test="${page.totalRecords > 0}">
			<input type="button" onclick="purchaseOrderItem.exportExcel();" class="sendbox" value="导出Excel" /> &nbsp; 
		</c:if>
		<input type="button" class="return-button" value="返回" onclick="purchaseOrderItem.backList();" /> &nbsp; 
	</div>
	<div class="clear"></div>
</div>

<div id="bodyBox" class="body-box">
<form style="padding-top:5px;" action="<%=request.getContextPath()%>/pos/purchaseOrderItem!purchaseOrderItemList.do" method="post">
<input type="hidden" id="purchaseOrderId" name="purchaseOrderId" value="${purchaseOrderId }" />
<div>
	商品名称:<input type="text" name="productName" value="${productName}" />&nbsp;&nbsp;
	供应商:<input type="text" name="supplierName" value="${supplierName}" />&nbsp;&nbsp;
	<input type="submit" value="查询" class="query" />
</div>
</form>

<table id="goodsList" width="100%" cellspacing="1" cellpadding="0" border="0" style="" class="pn-ltable">
<thead class="pn-lthead">
<tr>
	<th>序号</th>
	<th>采购单号</th>
	<th>商品</th>
	<th>供应商</th>
	<th>商品数量</th>
	<auth:authorize ifGranted="showPurchaseOrderPrice">
	<th>采购价</th>
	</auth:authorize>
	<th>操作</th>
</tr>
</thead>
<tbody class="pn-ltbody">
<c:forEach var="purchaseOrderItem" items="${page.list}" varStatus="status">
<tr>
	<td align="center">${status.index+1}</td>
	<td align="center">${purchaseOrder.orderNumber }</td>
	<td>${purchaseOrderItem.product.name }</td>
	<td>${purchaseOrderItem.supplier.name }</td>
	<td align="center">${purchaseOrderItem.count}</td>
	<auth:authorize ifGranted="showPurchaseOrderPrice">
	<td align="center">${purchaseOrderItem.purchasePrice }</td>
	</auth:authorize>
	<td align="center">
		<c:if test="${purchaseOrder.useStatus == 0}">
			<auth:authorize ifGranted="updatePurchaseOrderItem">
			<a href="<%=request.getContextPath()%>/pos/purchaseOrderItem!toPurchaseOrderItemFormView.do?purchaseOrderId=${purchaseOrderId}&purchaseOrderItem.id=${purchaseOrderItem.id }">修改</a>
			</auth:authorize>
			<auth:authorize ifGranted="deletePurchaseOrderItem">
			<a href="javascript:void(0);" onclick="purchaseOrderItem.deletePurchaseOrderItem(${purchaseOrderItem.id });">删除</a>
			</auth:authorize>
		</c:if>
	</td>
</tr>
</c:forEach>
</tbody>
</table>

<c:if test="${page.totalPages > 1}">
<table width="100%" style="font-size:13px;">
	<tbody>
	<tr>
		<td align="center" class="pn-sp">
		<c:if var="isFirstPage" test="${page.pageNum<=1}">
		首页 &nbsp;&nbsp;&nbsp;上一页&nbsp;&nbsp;&nbsp;
		</c:if>
		<c:if test="${!isFirstPage}">
		<a href="javascript:;" onclick="purchaseOrderItem.goPage(1,${page.pageCount});">首页</a>&nbsp;&nbsp;&nbsp;
		<a href="javascript:;" onclick="purchaseOrderItem.goPage(${page.pageNum-1},${page.pageCount});">上一页</a>
		</c:if>
		&nbsp;&nbsp;&nbsp;
		<c:if var="isLastPage" test="${page.pageNum>=page.totalPages}">
		下一页&nbsp;&nbsp;&nbsp;尾页&nbsp;&nbsp;&nbsp;
		</c:if>
		<c:if test="${!isLastPage}">
		<a href="javascript:;" onclick="purchaseOrderItem.goPage(${page.pageNum+1},${page.pageCount});">下一页</a>&nbsp;&nbsp;&nbsp;
		<a href="javascript:;" onclick="purchaseOrderItem.goPage(${page.totalPages},${page.pageCount});">尾页</a>
		</c:if>
		&nbsp;&nbsp;&nbsp;&nbsp;
		第${page.pageNum}/${page.totalPages}页,共${page.totalRecords}条记录
	</td>
	</tr>
	</tbody>
</table>
</c:if>
</div>
</body>
</html>