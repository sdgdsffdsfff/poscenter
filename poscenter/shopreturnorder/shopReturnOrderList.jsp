<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false"%>
<%@ include file="/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>店面退货单列表</title>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/skin/css/admin.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/skin/css/theme.css" />

<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript">
var shopReturnOrder={};

//跳转到指定页面
shopReturnOrder.goPage = function(pageNum,pageCount){
	var url = $('#initPath').val()+'/pos/shopReturnOrder!shopReturnOrderList.do?d=1';
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
	<div class="rpos">当前位置: 店面退货单管理 - 退货单列表</div>
	<div class="ropt">
	</div>
	<div class="clear"></div>
</div>

<div id="bodyBox" class="body-box">
<form style="padding-top:5px;" action="<%=request.getContextPath()%>/pos/shopReturnOrder!shopReturnOrderList.do" method="post">
<div>
	店面名称:<input type="text" size="12" name="shopName" value="${shopName}" />&nbsp;&nbsp;
	退货单号:<input type="text" size="12" name="shopReturnOrder.orderNumber" value="${shopReturnOrder.orderNumber }" />&nbsp;&nbsp;
	负责人:<input type="text" size="12" name="shopReturnOrder.charger" value="${shopReturnOrder.charger }" />&nbsp;&nbsp;
	创建时间:<input type="text" size="15" name="startTime" class="Wdate" onclick="WdatePicker()" value="${startTime }" readonly="readonly" />
	- <input type="text" size="15" name="endTime" class="Wdate" onclick="WdatePicker()" value="${endTime }" readonly="readonly"/>&nbsp;&nbsp;
	订单状态:
	<select name="shopReturnOrder.useStatus">
		<option value="-1">-全部-</option>
		<option value="0" <c:if test="${shopReturnOrder.useStatus == 0}">selected="selected"</c:if>>未确认</option>
		<option value="1" <c:if test="${shopReturnOrder.useStatus == 1}">selected="selected"</c:if>>已确认</option>
	</select>&nbsp;&nbsp;
	<input type="submit" value="查询" class="query" />
</div>
</form>

<table id="goodsList" width="100%" cellspacing="1" cellpadding="0" border="0" style="" class="pn-ltable">
<thead class="pn-lthead">
<tr>
	<th>店面名称</th>
	<th>退货单号</th>
	<th>负责人</th>
	<th>创建时间</th>
	<th>状态</th>
	<th>操作</th>
</tr>
</thead>
<tbody class="pn-ltbody">
<c:forEach var="shopReturnOrder" items="${page.list}">
<tr>
	<td>${shopReturnOrder.shop.name }</td>
	<td align="center">${shopReturnOrder.orderNumber }</td>
	<td>${shopReturnOrder.charger }</td>
	<td align="center"><fmt:formatDate value="${shopReturnOrder.createTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
	<td align="center">
		<c:choose>
			<c:when test="${shopReturnOrder.useStatus == 0}">未确认</c:when>
			<c:when test="${shopReturnOrder.useStatus == 1}">已确认</c:when>
		</c:choose>
	</td>
	<td align="center">
		<c:if test="${shopReturnOrder.useStatus == 0}">
			<auth:authorize ifGranted="confirmShopReturnOrder">
			<a href="<%=request.getContextPath()%>/pos/shopReturnOrderItem!shopReturnOrderItemList.do?shopReturnOrderId=${shopReturnOrder.id }">确认退货</a>
			</auth:authorize>
		</c:if>
		<c:if test="${shopReturnOrder.useStatus == 1}">
			<a href="<%=request.getContextPath()%>/pos/shopReturnOrderItem!shopReturnOrderItemList.do?shopReturnOrderId=${shopReturnOrder.id }">退货单条目</a>
		</c:if>
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
		<a href="javascript:;" onclick="shopReturnOrder.goPage(1,${page.pageCount});">首页</a>&nbsp;&nbsp;&nbsp;
		<a href="javascript:;" onclick="shopReturnOrder.goPage(${page.pageNum-1},${page.pageCount});">上一页</a>
		</c:if>
		&nbsp;&nbsp;&nbsp;
		<c:if var="isLastPage" test="${page.pageNum>=page.totalPages}">
		下一页&nbsp;&nbsp;&nbsp;尾页&nbsp;&nbsp;&nbsp;
		</c:if>
		<c:if test="${!isLastPage}">
		<a href="javascript:;" onclick="shopReturnOrder.goPage(${page.pageNum+1},${page.pageCount});">下一页</a>&nbsp;&nbsp;&nbsp;
		<a href="javascript:;" onclick="shopReturnOrder.goPage(${page.totalPages},${page.pageCount});">尾页</a>
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