<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false"%>
<%@ include file="/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>发货单条目列表</title>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/skin/css/admin.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/skin/css/theme.css" />

<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script>
<script type="text/javascript">
var sendOrderItem={};

//新增发货单
sendOrderItem.addSendOrderItem = function(){
	window.location.href = $('#initPath').val()+'/pos/sendOrderItem!toSendOrderItemFormView.do?sendOrderId='+$('#sendOrderId').val();
}

//删除发货单
sendOrderItem.deleteSendOrderItem = function(sendOrderItemId){
	if(confirm('确定删除该发货单条目吗？')){
		window.location.href = $('#initPath').val()+'/pos/sendOrderItem!deleteSendOrderItem.do?sendOrderItem.id='+sendOrderItemId+'&sendOrderId='+$('#sendOrderId').val();
	}
}

//跳转到指定页面
sendOrderItem.goPage = function(pageNum,pageCount){
	var url = $('#initPath').val()+'/pos/sendOrderItem!sendOrderItemList.do?sendOrderId='+$('#sendOrderId').val();
	if(pageNum != null && pageCount != null){
		url += '&page.pageNum='+pageNum;
	}
	if(pageCount != null){
		url += '&page.pageCount='+pageCount;
	}
	window.location.href = url;
}

//返回列表
sendOrderItem.backList = function(){
	window.location.href = $('#initPath').val()+'/pos/sendOrder!sendOrderList.do';
}

//提交发货单
sendOrderItem.submitSendOrder = function(sendOrderId){
	if(confirm('提交后将不能修改发货单信息，确定提交该发货单吗？')){
		window.location.href = $('#initPath').val()+'/pos/sendOrder!submitSendOrder.do?sendOrder.id='+sendOrderId;
	}
}
</script>
</head>
<body>
<input type="hidden" id="initPath" value="<%=request.getContextPath()%>" />
<div class="box-positon">
	<div class="rpos">当前位置: 发货单管理 - 发货单条目列表</div>
	<div class="ropt">
		<c:if test="${sendOrder.useStatus == 0}">
			<c:if test="${page.totalRecords > 0}">
			<auth:authorize ifGranted="submitSendOrder">
			<input type="button" onclick="sendOrderItem.submitSendOrder(${sendOrder.id});" class="" value="提交发货单" /> &nbsp; 
			</auth:authorize>
			</c:if>
			<auth:authorize ifGranted="addSendOrderItem">
			<input type="button" onclick="sendOrderItem.addSendOrderItem();" class="submit" value="新增" /> &nbsp;
			</auth:authorize> 
		</c:if>
		<input type="button" class="return-button" value="返回" onclick="sendOrderItem.backList();" />
	</div>
	<div class="clear"></div>
</div>

<div id="bodyBox" class="body-box">
<form style="padding-top:5px;" action="<%=request.getContextPath()%>/pos/sendOrderItem!sendOrderItemList.do" method="post">
<input type="hidden" id="sendOrderId" name="sendOrderId" value="${sendOrderId }" />
<div>
	商品名称:<input type="text" name="productName" value="${productName}" />&nbsp;&nbsp;
	<input type="submit" value="查询" class="query" />
</div>
</form>

<table id="goodsList" width="100%" cellspacing="1" cellpadding="0" border="0" style="" class="pn-ltable">
<thead class="pn-lthead">
<tr>
	<th>序号</th>
	<th>发货单号</th>
	<th>商品</th>
	<th>商品数量</th>
	<th>操作</th>
</tr>
</thead>
<tbody class="pn-ltbody">
<c:forEach var="sendOrderItem" items="${page.list}" varStatus="status">
<tr>
	<td align="center">${status.index+1}</td>
	<td align="center">${sendOrder.orderNumber }</td>
	<td>${sendOrderItem.product.name }</td>
	<td align="center">${sendOrderItem.count}</td>
	<td align="center">
		<c:if test="${sendOrder.useStatus == 0}">
			<auth:authorize ifGranted="updateSendOrderItem">
			<a href="<%=request.getContextPath()%>/pos/sendOrderItem!toSendOrderItemFormView.do?sendOrderId=${sendOrderId}&sendOrderItem.id=${sendOrderItem.id }">修改</a>
			</auth:authorize>
			<auth:authorize ifGranted="deleteSendOrderItem">
			<a href="javascript:void(0);" onclick="sendOrderItem.deleteSendOrderItem(${sendOrderItem.id });">删除</a>
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
		<a href="javascript:;" onclick="sendOrderItem.goPage(1,${page.pageCount});">首页</a>&nbsp;&nbsp;&nbsp;
		<a href="javascript:;" onclick="sendOrderItem.goPage(${page.pageNum-1},${page.pageCount});">上一页</a>
		</c:if>
		&nbsp;&nbsp;&nbsp;
		<c:if var="isLastPage" test="${page.pageNum>=page.totalPages}">
		下一页&nbsp;&nbsp;&nbsp;尾页&nbsp;&nbsp;&nbsp;
		</c:if>
		<c:if test="${!isLastPage}">
		<a href="javascript:;" onclick="sendOrderItem.goPage(${page.pageNum+1},${page.pageCount});">下一页</a>&nbsp;&nbsp;&nbsp;
		<a href="javascript:;" onclick="sendOrderItem.goPage(${page.totalPages},${page.pageCount});">尾页</a>
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