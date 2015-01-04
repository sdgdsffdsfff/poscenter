<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false"%>
<%@ include file="/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>店面列表</title>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/skin/css/admin.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/skin/css/theme.css" />

<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script>
<script type="text/javascript">
var shop={};

//新增店面
shop.addShop = function(){
	window.location.href = $('#initPath').val()+'/pos/shop!toShopFormView.do';
}

//删除店面
shop.deleteShop = function(shopId){
	if(confirm('确定删除该店面吗？')){
		window.location.href = $('#initPath').val()+'/pos/shop!deleteShop.do?shop.id='+shopId;
	}
}

//跳转到指定页面
shop.goPage = function(pageNum,pageCount){
	var url = $('#initPath').val()+'/pos/shop!shopList.do?d=1';
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
	<div class="rpos">当前位置: 店面管理 - 列表</div>
	<div class="ropt">
		<auth:authorize ifGranted="addShop">
		<input type="button" onclick="shop.addShop();" class="submit" value="新增" /> &nbsp; 
		</auth:authorize>
	</div>
	<div class="clear"></div>
</div>

<div id="bodyBox" class="body-box">
<table width="100%" cellspacing="1" cellpadding="0" border="0" style="" class="pn-ltable">
<thead class="pn-lthead">
<tr>
	<th>店面编号</th>
	<th>店面名称</th>
	<th>负责人</th>
	<th>地址</th>
	<th>IP地址</th>
	<th>创建时间</th>
	<th>操作</th>
</tr>
</thead>
<tbody class="pn-ltbody">
<c:forEach var="shop" items="${page.list}">
<tr>
	<td align="center">${shop.code }</td>
	<td>${shop.name }</td>
	<td>${shop.charger }</td>
	<td>${shop.address }</td>
	<td>${shop.ipAddress }</td>
	<td align="center"><fmt:formatDate value="${shop.createTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
	<td align="center">
		<a href="<%=request.getContextPath()%>/pos/shop!toShopDetailView.do?shop.id=${shop.id }">查看</a>
		<auth:authorize ifGranted="updateShop">
		<a href="<%=request.getContextPath()%>/pos/shop!toShopFormView.do?shop.id=${shop.id }">修改</a>
		</auth:authorize>
		<auth:authorize ifGranted="deleteShop">
		<a href="javascript:void(0);" onclick="shop.deleteShop(${shop.id });">删除</a>
		</auth:authorize>
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
		<a href="javascript:;" onclick="shop.goPage(1,${page.pageCount});">首页</a>&nbsp;&nbsp;&nbsp;
		<a href="javascript:;" onclick="shop.goPage(${page.pageNum-1},${page.pageCount});">上一页</a>
		</c:if>
		&nbsp;&nbsp;&nbsp;
		<c:if var="isLastPage" test="${page.pageNum>=page.totalPages}">
		下一页&nbsp;&nbsp;&nbsp;尾页&nbsp;&nbsp;&nbsp;
		</c:if>
		<c:if test="${!isLastPage}">
		<a href="javascript:;" onclick="shop.goPage(${page.pageNum+1},${page.pageCount});">下一页</a>&nbsp;&nbsp;&nbsp;
		<a href="javascript:;" onclick="shop.goPage(${page.totalPages},${page.pageCount});">尾页</a>
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