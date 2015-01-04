<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false"%>
<%@ include file="/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>采购单列表</title>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/skin/css/admin.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/skin/css/theme.css" />

<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript">
var purchaseOrder={};

//新增采购单
purchaseOrder.addPurchaseOrder = function(){
	window.location.href = $('#initPath').val()+'/pos/purchaseOrder!toPurchaseOrderFormView.do';
}

//删除采购单
purchaseOrder.deletePurchaseOrder = function(purchaseOrderId){
	if(confirm('确定删除该采购单及其所有采购单条目吗？')){
		window.location.href = $('#initPath').val()+'/pos/purchaseOrder!deletePurchaseOrder.do?purchaseOrder.id='+purchaseOrderId;
	}
}

//提交采购单
purchaseOrder.submitPurchaseOrder = function(purchaseOrderId){
	if(confirm('提交后将不能修改采购单信息，确定提交该采购单吗？')){
		window.location.href = $('#initPath').val()+'/pos/purchaseOrder!submitPurchaseOrder.do?purchaseOrder.id='+purchaseOrderId;
	}
}

//跳转到指定页面
purchaseOrder.goPage = function(pageNum,pageCount){
	var url = $('#initPath').val()+'/pos/purchaseOrder!purchaseOrderList.do?d=1';
	if(pageNum != null && pageCount != null){
		url += '&page.pageNum='+pageNum;
	}
	if(pageCount != null){
		url += '&page.pageCount='+pageCount;
	}
	window.location.href = url;
}

//跳转到导入采购单页面
purchaseOrder.toImportPurchaseOrderView = function(purchaseOrderId){
	window.location.href = $('#initPath').val()+'//poscenter/purchaseorder/importPurchaseOrder.jsp';
}
</script>
</head>
<body>
<input type="hidden" id="initPath" value="<%=request.getContextPath()%>" />
<div class="box-positon">
	<div class="rpos">当前位置: 采购单管理 - 采购单列表</div>
	<div class="ropt">
		<auth:authorize ifGranted="addPurchaseOrder">
		<input type="button" onclick="purchaseOrder.addPurchaseOrder();" class="submit" value="新增" /> &nbsp;
		</auth:authorize>
		<auth:authorize ifGranted="importPurchaseOrder">
		<input type="button" onclick="purchaseOrder.toImportPurchaseOrderView();" class="generate-index-page" value="导入采购单" /> &nbsp;
		</auth:authorize>
	</div>
	<div class="clear"></div>
</div>

<div id="bodyBox" class="body-box">
<form style="padding-top:5px;" action="<%=request.getContextPath()%>/pos/purchaseOrder!purchaseOrderList.do" method="post">
<div>
	采购单号:<input type="text" size="12" name="purchaseOrder.orderNumber" value="${purchaseOrder.orderNumber }" />&nbsp;&nbsp;
	负责人:<input type="text" size="12" name="purchaseOrder.charger" value="${purchaseOrder.charger }" />&nbsp;&nbsp;
	采购时间:<input type="text" size="15" name="startTime" class="Wdate" onclick="WdatePicker()" value="${startTime }" readonly="readonly" />
	- <input type="text" size="15" name="endTime" class="Wdate" onclick="WdatePicker()" value="${endTime }" readonly="readonly"/>&nbsp;&nbsp;
	订单状态:
	<select name="purchaseOrder.useStatus">
		<option value="-1">-全部-</option>
		<option value="0" <c:if test="${purchaseOrder.useStatus == 0}">selected="selected"</c:if>>未提交</option>
		<option value="1" <c:if test="${purchaseOrder.useStatus == 1}">selected="selected"</c:if>>已提交</option>
	</select>&nbsp;&nbsp;
	<input type="submit" value="查询" class="query" />
</div>
</form>

<table id="goodsList" width="100%" cellspacing="1" cellpadding="0" border="0" style="" class="pn-ltable">
<thead class="pn-lthead">
<tr>
	<th>采购单号</th>
	<th>负责人</th>
	<th>采购单位</th>
	<th>创建时间</th>
	<th>状态</th>
	<th>操作</th>
</tr>
</thead>
<tbody class="pn-ltbody">
<c:forEach var="purchaseOrder" items="${page.list}">
<tr>
	<td align="center">${purchaseOrder.orderNumber }</td>
	<td>${purchaseOrder.charger }</td>
	<td>${purchaseOrder.department }</td>
	<td align="center"><fmt:formatDate value="${purchaseOrder.createTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
	<td align="center">
		<c:choose>
			<c:when test="${purchaseOrder.useStatus == 0}">未提交</c:when>
			<c:when test="${purchaseOrder.useStatus == 1}">已提交</c:when>
			<c:when test="${purchaseOrder.useStatus == 2}">已归档</c:when>
		</c:choose>
	</td>
	<td align="center">
		<c:if test="${purchaseOrder.useStatus == 0}">
		<auth:authorize ifGranted="updatePurchaseOrder">
			<a href="<%=request.getContextPath()%>/pos/purchaseOrder!toPurchaseOrderFormView.do?purchaseOrder.id=${purchaseOrder.id }">修改</a>
		</auth:authorize>
		<auth:authorize ifGranted="deletePurchaseOrder">
			<a href="javascript:void(0);" onclick="purchaseOrder.deletePurchaseOrder(${purchaseOrder.id });">删除</a>
		</auth:authorize>
		</c:if>
		<a href="<%=request.getContextPath()%>/pos/purchaseOrderItem!purchaseOrderItemList.do?purchaseOrderId=${purchaseOrder.id }">采购单条目</a>
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
		<a href="javascript:;" onclick="purchaseOrder.goPage(1,${page.pageCount});">首页</a>&nbsp;&nbsp;&nbsp;
		<a href="javascript:;" onclick="purchaseOrder.goPage(${page.pageNum-1},${page.pageCount});">上一页</a>
		</c:if>
		&nbsp;&nbsp;&nbsp;
		<c:if var="isLastPage" test="${page.pageNum>=page.totalPages}">
		下一页&nbsp;&nbsp;&nbsp;尾页&nbsp;&nbsp;&nbsp;
		</c:if>
		<c:if test="${!isLastPage}">
		<a href="javascript:;" onclick="purchaseOrder.goPage(${page.pageNum+1},${page.pageCount});">下一页</a>&nbsp;&nbsp;&nbsp;
		<a href="javascript:;" onclick="purchaseOrder.goPage(${page.totalPages},${page.pageCount});">尾页</a>
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