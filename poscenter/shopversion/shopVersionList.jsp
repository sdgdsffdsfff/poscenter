<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false"%>
<%@ include file="/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>店面系统版本列表</title>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/skin/css/admin.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/skin/css/theme.css" />

<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script>
<script type="text/javascript">
var shopVersion={};

//新增店面系统版本
shopVersion.addShopVersion = function(){
	window.location.href = $('#initPath').val()+'/pos/shopVersion!toShopVersionFormView.do';
}

//删除店面系统版本
shopVersion.deleteShopVersion = function(shopVersionId){
	if(confirm('确定删除该店面系统版本吗？')){
		window.location.href = $('#initPath').val()+'/pos/shopVersion!deleteShopVersion.do?shopVersion.id='+shopVersionId;
	}
}

//发布
shopVersion.releaseVersion = function(id){
	if(confirm('确定发布该版本吗？')){
		window.location.href = $('#initPath').val()+'/pos/shopVersion!releaseVersion.do?shopVersion.id='+id;
	}
}

//跳转到指定页面
shopVersion.goPage = function(pageNum,pageCount){
	var url = $('#initPath').val()+'/pos/shopVersion!shopVersionList.do?d=1';
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
	<div class="rpos">当前位置: 店面系统版本管理 - 列表</div>
	<div class="ropt">
		<auth:authorize ifGranted="addShopVersion">
		<input type="button" onclick="shopVersion.addShopVersion();" class="submit" value="新增" /> &nbsp; 
		</auth:authorize>
	</div>
	<div class="clear"></div>
</div>

<div id="bodyBox" class="body-box">
<table width="100%" cellspacing="1" cellpadding="0" border="0" style="" class="pn-ltable">
<thead class="pn-lthead">
<tr>
	<th>版本编号</th>
	<th>版本名称</th>
	<th>版本描述</th>
	<th>创建时间</th>
	<th>发布状态</th>
	<th>操作</th>
</tr>
</thead>
<tbody class="pn-ltbody">
<c:forEach var="shopVersion" items="${page.list}">
<tr>
	<td align="center">${shopVersion.versionNumber }</td>
	<td>${shopVersion.versionName }</td>
	<td>${shopVersion.versionDesc }</td>
	<td align="center"><fmt:formatDate value="${shopVersion.createTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
	<td align="center">
		<c:if test="${shopVersion.useStatus == 1}">未发布</c:if>
		<c:if test="${shopVersion.useStatus == 2}">已发布</c:if>
	</td>
	<td align="center">
		<c:if test="${shopVersion.useStatus == 1}">
			<auth:authorize ifGranted="releaseShopVersion">
			<a href="javascript:void(0);" onclick="shopVersion.releaseVersion(${shopVersion.id });">发布</a>
			</auth:authorize>
			<auth:authorize ifGranted="updateShopVersion">
			<a href="<%=request.getContextPath()%>/pos/shopVersion!toShopVersionFormView.do?shopVersion.id=${shopVersion.id }">修改</a>
			</auth:authorize>
			<auth:authorize ifGranted="deleteShopVersion">
			<a href="javascript:void(0);" onclick="shopVersion.deleteShopVersion(${shopVersion.id });">删除</a>
			</auth:authorize>
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
		<a href="javascript:;" onclick="ShopVersion.goPage(1,${page.pageCount});">首页</a>&nbsp;&nbsp;&nbsp;
		<a href="javascript:;" onclick="ShopVersion.goPage(${page.pageNum-1},${page.pageCount});">上一页</a>
		</c:if>
		&nbsp;&nbsp;&nbsp;
		<c:if var="isLastPage" test="${page.pageNum>=page.totalPages}">
		下一页&nbsp;&nbsp;&nbsp;尾页&nbsp;&nbsp;&nbsp;
		</c:if>
		<c:if test="${!isLastPage}">
		<a href="javascript:;" onclick="ShopVersion.goPage(${page.pageNum+1},${page.pageCount});">下一页</a>&nbsp;&nbsp;&nbsp;
		<a href="javascript:;" onclick="ShopVersion.goPage(${page.totalPages},${page.pageCount});">尾页</a>
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