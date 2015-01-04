<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false"%>
<%@ include file="/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>发货单列表</title>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/skin/css/admin.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/skin/css/theme.css" />

<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript">
var sendOrder={};

//新增发货单
sendOrder.addSendOrder = function(){
	window.location.href = $('#initPath').val()+'/pos/sendOrder!toSendOrderFormView.do';
}

//删除发货单
sendOrder.deleteSendOrder = function(sendOrderId){
	if(confirm('确定删除该发货单及其所有发货单条目吗？')){
		window.location.href = $('#initPath').val()+'/pos/sendOrder!deleteSendOrder.do?sendOrder.id='+sendOrderId;
	}
}

//提交发货单
sendOrder.submitSendOrder = function(sendOrderId){
	if(confirm('提交后将不能修改发货单信息，确定提交该发货单吗？')){
		window.location.href = $('#initPath').val()+'/pos/sendOrder!submitSendOrder.do?sendOrder.id='+sendOrderId;
	}
}

//跳转到指定页面
sendOrder.goPage = function(pageNum,pageCount){
	var url = $('#initPath').val()+'/pos/sendOrder!sendOrderList.do?d=1';
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
	<div class="rpos">当前位置: 发货单管理 - 发货单列表</div>
	<div class="ropt">
		<auth:authorize ifGranted="addSendOrder">
		<input type="button" onclick="sendOrder.addSendOrder();" class="submit" value="新增" /> &nbsp; 
		</auth:authorize>
	</div>
	<div class="clear"></div>
</div>

<div id="bodyBox" class="body-box">
<form style="padding-top:5px;" action="<%=request.getContextPath()%>/pos/sendOrder!sendOrderList.do" method="post">
<div>
	发货单号:<input type="text" size="12" name="sendOrder.orderNumber" value="${sendOrder.orderNumber }" />&nbsp;&nbsp;
	店面:<input type="text" size="12" name="shopName" value="${shopName}" />&nbsp;&nbsp;
	负责人:<input type="text" size="12" name="sendOrder.charger" value="${sendOrder.charger }" />&nbsp;&nbsp;
	创建时间:<input type="text" size="15" name="startTime" class="Wdate" onclick="WdatePicker()" value="${startTime }" readonly="readonly" />
	- <input type="text" size="15" name="endTime" class="Wdate" onclick="WdatePicker()" value="${endTime }" readonly="readonly"/>&nbsp;&nbsp;
	订单状态:
	<select name="sendOrder.useStatus">
		<option value="-1">-全部-</option>
		<option value="0" <c:if test="${sendOrder.useStatus == 0}">selected="selected"</c:if>>未提交</option>
		<option value="1" <c:if test="${sendOrder.useStatus == 1}">selected="selected"</c:if>>已提交</option>
		<option value="2" <c:if test="${sendOrder.useStatus == 2}">selected="selected"</c:if>>已归档</option>
	</select>&nbsp;&nbsp;
	<input type="submit" value="查询" class="query" />
</div>
</form>

<table id="goodsList" width="100%" cellspacing="1" cellpadding="0" border="0" style="" class="pn-ltable">
<thead class="pn-lthead">
<tr>
	<th>发货单号</th>
	<th>店面</th>
	<th>负责人</th>
	<th>收货单位</th>
	<th>创建时间</th>
	<th>状态</th>
	<th>操作</th>
</tr>
</thead>
<tbody class="pn-ltbody">
<c:forEach var="sendOrder" items="${page.list}">
<tr>
	<td align="center">${sendOrder.orderNumber }</td>
	<td>${sendOrder.shop.name }</td>
	<td>${sendOrder.charger }</td>
	<td>${sendOrder.department }</td>
	<td align="center"><fmt:formatDate value="${sendOrder.createTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
	<td align="center">
		<c:choose>
			<c:when test="${sendOrder.useStatus == 0}">未提交</c:when>
			<c:when test="${sendOrder.useStatus == 1}">已提交</c:when>
			<c:when test="${sendOrder.useStatus == 2}">已归档</c:when>
		</c:choose>
	</td>
	<td align="center">
		<c:if test="${sendOrder.useStatus == 0}">
		<auth:authorize ifGranted="updateSendOrder">
			<a href="<%=request.getContextPath()%>/pos/sendOrder!toSendOrderFormView.do?sendOrder.id=${sendOrder.id }">修改</a>
		</auth:authorize>
		<auth:authorize ifGranted="deleteSendOrder">
			<a href="javascript:void(0);" onclick="sendOrder.deleteSendOrder(${sendOrder.id });">删除</a>
		</auth:authorize>
		</c:if>
		<a href="<%=request.getContextPath()%>/pos/sendOrderItem!sendOrderItemList.do?sendOrderId=${sendOrder.id }">发货单条目</a>
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
		<a href="javascript:;" onclick="sendOrder.goPage(1,${page.pageCount});">首页</a>&nbsp;&nbsp;&nbsp;
		<a href="javascript:;" onclick="sendOrder.goPage(${page.pageNum-1},${page.pageCount});">上一页</a>
		</c:if>
		&nbsp;&nbsp;&nbsp;
		<c:if var="isLastPage" test="${page.pageNum>=page.totalPages}">
		下一页&nbsp;&nbsp;&nbsp;尾页&nbsp;&nbsp;&nbsp;
		</c:if>
		<c:if test="${!isLastPage}">
		<a href="javascript:;" onclick="sendOrder.goPage(${page.pageNum+1},${page.pageCount});">下一页</a>&nbsp;&nbsp;&nbsp;
		<a href="javascript:;" onclick="sendOrder.goPage(${page.totalPages},${page.pageCount});">尾页</a>
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