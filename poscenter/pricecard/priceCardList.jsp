<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false"%>
<%@ include file="/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>红蓝卡列表</title>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/skin/css/admin.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/skin/css/theme.css" />
<link rel="STYLESHEET" type="text/css" href="<%=request.getContextPath()%>/js/jquery/msgbox/jquery.msgbox.css" />

<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.validate.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/msgbox/jquery.dragndrop.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/msgbox/jquery.msgbox.js"></script>
<script type="text/javascript">
var priceCard={};

priceCard.addPriceCard = function(){
    var url = $('#initPath').val()+'/pos/priceCard!priceCardForm.do';
    window.location.href = url;
}

priceCard.search = function(){
    var priceCardId = $('#priceCardId').val();
    var clerkName = $('#clerkName').val();
    var state = $('#state').val();
    var type = $('#type').val();
    var url = $('#initPath').val()+'/pos/priceCard!priceCardList.do?priceCardId='+priceCardId+'&clerkName='+clerkName+'&state='+state+'&type='+type;
    window.location.href = url;
}

//跳转到指定页面
priceCard.goPage = function(pageNum,pageCount){
    var priceCardId = $('#priceCardId').val();
    var clerkName = $('#clerkName').val();
    var state = $('#state').val();
    var type = $('#type').val();
    var url = $('#initPath').val()+'/pos/priceCard!priceCardList.do?priceCardId='+priceCardId+'&clerkName='+clerkName+'&state='+state+'&type='+type;
	if(pageNum != null && pageCount != null){
		url += '&page.pageNum='+pageNum;
	}
	if(pageCount != null){
		url += '&page.pageCount='+pageCount;
	}
	window.location.href = url;
}

$(document).ready(function(){
	//验证表单
	$("#priceCardForm").validate();
});

//查看
priceCard.showPriceCardDetail = function(priceCardId){
	new $.msgbox({
		title: '查看红蓝卡详情',
		width: 700,
		height: 480,
		type: 'ajax',
		content: $('#initPath').val()+'/pos/priceCard!toPriceCardDtail.do?priceCard.id='+priceCardId
	}).show();
}
</script>
</head>
<body style="min-height:500px;">
<input type="hidden" id="initPath" value="<%=request.getContextPath()%>" />
<div class="box-positon">
	<div class="rpos">当前位置: 红蓝卡管理 - 列表</div>
	<div class="ropt">
		<auth:authorize ifGranted="addPriceCard">
		<input type="button" onclick="priceCard.addPriceCard();" class="submit" value="新增" /> &nbsp; 
		</auth:authorize>
	</div>
	<div class="clear"></div>
</div>

<div id="bodyBox" class="body-box">
<form id="priceCardForm" name="priceCardForm" action="<%=request.getContextPath()%>/pos/priceCard!priceCardList.do" method="post" style="padding-top:5px;" >
<div>
    类型:&nbsp;
    <select id="type" name="type">
		<option value="0">-全部-</option>
		<option value="1" <c:if test="${type == 1}">selected="selected"</c:if>>红卡</option>
		<option value="2" <c:if test="${type == 2}">selected="selected"</c:if>>蓝卡</option>
	</select>&nbsp;&nbsp;
	卡号:&nbsp;<input type="text" id="priceCardId" name="priceCardId" value="${priceCardId}">&nbsp;&nbsp;&nbsp;
	店员姓名:&nbsp;<input type="text" id="clerkName" name="clerkName" value="${clerkName}">&nbsp;&nbsp;&nbsp;
	状态:&nbsp;
	  <select id="state" name="state">
		<option value="0">-全部-</option>
		<option value="1" <c:if test="${state == 1}">selected="selected"</c:if>>白卡</option>
		<option value="2" <c:if test="${state == 2}">selected="selected"</c:if>>可使用</option>
		<option value="3" <c:if test="${state == 3}">selected="selected"</c:if>>停用</option>
	</select>&nbsp;&nbsp;
	<input id="queryBut" type="submit" class="query" value="查询"  />
</div>
</form>
<table width="100%" cellspacing="1" cellpadding="0" border="0" class="pn-ltable">
<thead class="pn-lthead">
      <tr>
		<th>卡号</th>
		<th>类型</th>
		<th>店铺名称</th>
		<th>店员姓名</th>
		<th>开卡时间</th>
		<th>供应商名称</th>
		<th>余额</th>
		<th>状态</th>
		<th>操作</th>
	</tr>
</thead>
<tbody class="pn-ltbody">
	<c:forEach var="priceCard" items="${list}">
		<tr>
			<td align="center">${priceCard.id }</td>
			<c:if test="${priceCard.type == 1 }">
		       <td align="center">红卡</td>
		    </c:if>
			<c:if test="${priceCard.type == 2 }">
		       <td align="center">蓝卡</td>
		    </c:if>
			<td align="center">${priceCard.shopName }</td>
			<td align="center">${priceCard.clerkName }</td>
			<td align="center"><fmt:formatDate value="${priceCard.openTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
			<td align="center">${priceCard.supplierName }</td>
			<td align="center">${priceCard.point }</td>
			<c:if test="${priceCard.state == 1 }">
		       <td align="center">白卡</td>
		    </c:if>
			<c:if test="${priceCard.state == 2 }">
		       <td align="center">可使用</td>
		    </c:if>
		    <c:if test="${priceCard.state == 3 }">
		       <td align="center">停用</td>
		    </c:if>
			<td align="center">
			    <c:if test="${priceCard.state == 1 }">
				<a href="<%=request.getContextPath()%>/pos/priceCard!openCardForm.do?priceCard.id=${priceCard.id }">开卡</a>
				</c:if>
				<a href="<%=request.getContextPath()%>/pos/priceCard!updateCardForm.do?priceCard.id=${priceCard.id }">修改</a>
				<a href="javascript:;" onclick="priceCard.showPriceCardDetail('${priceCard.id }');">查看</a>
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
		<a href="javascript:;" onclick="priceCard.goPage(1,${page.pageCount});">首页</a>&nbsp;&nbsp;&nbsp;
		<a href="javascript:;" onclick="priceCard.goPage(${page.pageNum-1},${page.pageCount});">上一页</a>
		</c:if>
		&nbsp;&nbsp;&nbsp;
		<c:if var="isLastPage" test="${page.pageNum>=page.totalPages}">
		下一页&nbsp;&nbsp;&nbsp;尾页&nbsp;&nbsp;&nbsp;
		</c:if>
		<c:if test="${!isLastPage}">
		<a href="javascript:;" onclick="priceCard.goPage(${page.pageNum+1},${page.pageCount});">下一页</a>&nbsp;&nbsp;&nbsp;
		<a href="javascript:;" onclick="priceCard.goPage(${page.totalPages},${page.pageCount});">尾页</a>
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