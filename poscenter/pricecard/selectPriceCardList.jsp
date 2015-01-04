<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false"%>
<%@ include file="/taglibs.jsp" %>
<div id="priceCardListDiv">
<script type="text/javascript">
var selectPriceCard={};

//选择
selectPriceCard.selectedPriceCard = function(obj){
	var priceCardId = $(obj).parent().find('input[name=priceCardId]').val();
	$('#priceCardId').val(priceCardId);
	$('a.jMsgbox-closeWrap').click(); //关闭弹出框
}

//跳转到指定页面
selectPriceCard.goPage = function(pageNum,pageCount){
	var data = {};
	data['type'] = $('select[name=type]').val();
	data['priceCardId'] = $('input[name=priceCardId]').val();
	data['clerkName'] = $('input[name=clerkName]').val();
	if(pageNum != null && pageCount != null){
		data['page.pageNum'] = pageNum;
		data['page.pageCount'] = pageCount;
	}
	var url = $('#initPath').val()+'/pos/priceCard!toSelectPriceCardListView.do';
	$('#priceCardListDiv').load(url, data);
}

$(document).ready(function(){
});
</script>
<div>
	类型:
	<select id="type" name="type">
		<option value="0">-全部-</option>
		<option value="1" <c:if test="${type == 1}">selected="selected"</c:if>>红卡</option>
		<option value="2" <c:if test="${type == 2}">selected="selected"</c:if>>蓝卡</option>
	</select>&nbsp;&nbsp;
	卡号:<input type="text" id="priceCardId" name="priceCardId" value="${priceCardId}">&nbsp;&nbsp;&nbsp;
	店员姓名:<input type="text" id="clerkName" name="clerkName" value="${clerkName}">&nbsp;&nbsp;&nbsp;
	<input type="button" class="query" value="查询" onclick="selectPriceCard.goPage();" />
</div>
<table width="100%" cellspacing="1" cellpadding="0" border="0" class="pn-ltable">
<thead class="pn-lthead">
<tr>
	<th>卡号</th>
	<th>类型</th>
	<th>店铺名称</th>
	<th>店员姓名</th>
	<th>开卡时间</th>
	<th>供应商名称</th>
	<th>状态</th>
	<th>操作</th>
</tr>
</thead>
<tbody class="pn-ltbody">
	<c:forEach var="priceCard" items="${page.list}">
	<tr style="height: 21px;" ondblclick="$(this).find('a').click();">
		<td align="center">${priceCard.id }</td>
		<td align="center">
			<c:if test="${priceCard.type == 1 }">红卡</c:if>
			<c:if test="${priceCard.type == 2 }">蓝卡</c:if>
		</td>
		<td align="center">${priceCard.shopName }</td>
		<td align="center">${priceCard.clerkName }</td>
		<td align="center"><fmt:formatDate value="${priceCard.openTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
		<td align="center">${priceCard.supplierName }</td>
		<td align="center">
			<c:if test="${priceCard.state == 1}">白卡</c:if>
			<c:if test="${priceCard.state == 2}">可使用</c:if>
			<c:if test="${priceCard.state == 3}">停用</c:if>
		</td>
		<td align="center">
			<input type="hidden" name="priceCardId" value="${priceCard.id }" />
			<a href="javascript:void(0);" onclick="selectPriceCard.selectedPriceCard(this);">选择</a>
		</td>
	</tr>
	</c:forEach>
</tbody>
</table>

<table width="100%" style="font-size:13px;">
	<tbody>
	<tr>
		<td align="center" class="pn-sp">
		<c:if var="isFirstPage" test="${page.pageNum<=1}">
		首页 &nbsp;&nbsp;&nbsp;上一页&nbsp;&nbsp;&nbsp;
		</c:if>
		<c:if test="${!isFirstPage}">
		<a href="javascript:;" onclick="selectPriceCard.goPage(1,${page.pageCount});">首页</a>&nbsp;&nbsp;&nbsp;
		<a href="javascript:;" onclick="selectPriceCard.goPage(${page.pageNum-1},${page.pageCount});">上一页</a>
		</c:if>
		&nbsp;&nbsp;&nbsp;
		<c:if var="isLastPage" test="${page.pageNum>=page.totalPages}">
		下一页&nbsp;&nbsp;&nbsp;尾页&nbsp;&nbsp;&nbsp;
		</c:if>
		<c:if test="${!isLastPage}">
		<a href="javascript:;" onclick="selectPriceCard.goPage(${page.pageNum+1},${page.pageCount});">下一页</a>&nbsp;&nbsp;&nbsp;
		<a href="javascript:;" onclick="selectPriceCard.goPage(${page.totalPages},${page.pageCount});">尾页</a>
		</c:if>
		&nbsp;&nbsp;&nbsp;&nbsp;
		第${page.pageNum}/${page.totalPages}页,共${page.totalRecords}条记录
	</td>
	</tr>
	</tbody>
</table>
</div>