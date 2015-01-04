<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false"%>
<%@ include file="/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>审核充值单列表</title>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/skin/css/admin.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/skin/css/theme.css" />

<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script>
<script type="text/javascript">
var rechargeOrder={};

//审核
rechargeOrder.auditRechargeOrder = function(rechargeOrderId,auditStatus){
	var msg = (auditStatus==2 ? '您确定要审批通过此充值申请吗？' : '您确定要拒绝通过此充值申请吗？');
	if(confirm(msg)){
		window.location.href = $('#initPath').val()+'/pos/rechargeOrder!auditRechargeOrder.do?rechargeOrder.id='+rechargeOrderId+'&rechargeOrder.auditStatus='+auditStatus;
	}
}

//跳转到指定页面
rechargeOrder.goPage = function(pageNum,pageCount){
	var pageNum = '<input type="hidden" name="page.pageNum" value="'+pageNum+'" />';
	var pageCount = '<input type="hidden" name="page.pageCount" value="'+pageCount+'" />';
	$('#rechargeOrderSearchForm').append(pageNum);
	$('#rechargeOrderSearchForm').append(pageCount);
	$('#rechargeOrderSearchForm').submit();
}

$(document).ready(function(){
});
</script>
</head>
<body style="min-height: 500px;">
<input type="hidden" id="initPath" value="<%=request.getContextPath()%>" />
<div class="box-positon">
	<div class="rpos">当前位置: 审核充值单</div>
	<div class="ropt">
	</div>
	<div class="clear"></div>
</div>

<div id="bodyBox" class="body-box">
<form id="rechargeOrderSearchForm" action="<%=request.getContextPath()%>/pos/rechargeOrder!auditRechargeOrderList.do" style="padding-top:5px;" method="post">
<div>
	卡号:<input type="text" name="priceCardId" value="${priceCardId}"/>&nbsp;&nbsp;
	类型:
	<select name="priceCardType">
		<option value="0">全部</option>
		<option value="1" <c:if test="${priceCardType==1}">selected="selected"</c:if>>红卡</option>
		<option value="2" <c:if test="${priceCardType==2}">selected="selected"</c:if>>蓝卡</option>
	</select>&nbsp;&nbsp;
	<input type="submit" value="查询" class="query" />
</div>
</form>

<table width="100%" cellspacing="1" cellpadding="0" border="0" class="pn-ltable">
<thead class="pn-lthead">
<tr>
	<th>类型</th>
	<th>卡号</th>
	<th>持卡人</th>
	<th>所在店面</th>
	<th>卡内余额</th>
	<th>充值点数</th>
	<th>创建时间</th>
	<th>操作</th>
</tr>
</thead>
<tbody class="pn-ltbody">
<c:forEach var="rechargeOrder" items="${page.list}">
<tr>
	<td align="center">
		<c:if test="${rechargeOrder.priceCard.type==1}">红卡</c:if>
		<c:if test="${rechargeOrder.priceCard.type==2}">蓝卡</c:if>
	</td>
	<td align="center">${rechargeOrder.priceCardId}</td>
	<td>${rechargeOrder.priceCard.clerkName}</td>
	<td>${rechargeOrder.priceCard.shopName}</td>
	<td  align="right">${rechargeOrder.priceCard.point}</td>
	<td  align="right">${rechargeOrder.point}</td>
	<td align="center"><fmt:formatDate value="${rechargeOrder.createTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
	<td align="center">
		<a href="javascript:void(0);" onclick="rechargeOrder.auditRechargeOrder(${rechargeOrder.id},2);">通过审核</a>
		<a href="javascript:void(0);" onclick="rechargeOrder.auditRechargeOrder(${rechargeOrder.id},3);">拒绝</a>
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
		<a href="javascript:;" onclick="rechargeOrder.goPage(1,${page.pageCount});">首页</a>&nbsp;&nbsp;&nbsp;
		<a href="javascript:;" onclick="rechargeOrder.goPage(${page.pageNum-1},${page.pageCount});">上一页</a>
		</c:if>
		&nbsp;&nbsp;&nbsp;
		<c:if var="isLastPage" test="${page.pageNum>=page.totalPages}">
		下一页&nbsp;&nbsp;&nbsp;尾页&nbsp;&nbsp;&nbsp;
		</c:if>
		<c:if test="${!isLastPage}">
		<a href="javascript:;" onclick="rechargeOrder.goPage(${page.pageNum+1},${page.pageCount});">下一页</a>&nbsp;&nbsp;&nbsp;
		<a href="javascript:;" onclick="rechargeOrder.goPage(${page.totalPages},${page.pageCount});">尾页</a>
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