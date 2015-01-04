<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<script type="text/javascript">
var shop={};

//选择
shop.selectedShop = function(obj){
	//多选
	if($('#_checkBox').val() == 'true'){
		$(obj).parent().parent().find('input[type=checkbox][name=selectAll]').attr('checked', true);
	}
	//单选
	else{
		var shopId = $(obj).parent().find('input[name=shopId]').val();
		var shopName = $(obj).parent().parent().find('td:eq(1)').text();
		$('#shopId').val(shopId);
		$('#shopName').val(shopName);
		$('a.jMsgbox-closeWrap').click(); //关闭弹出框
	}
}

//跳转到指定页面
shop.goPage = function(pageNum,pageCount){
	var url = $('#initPath').val()+'/pos/shop!toSelectShopListView.do?d=1';
	if(pageNum != null && pageCount != null){
		url += '&page.pageNum='+pageNum;
	}
	if(pageCount != null){
		url += '&page.pageCount='+pageCount;
	}
	$('#shopListDiv').load(url, {'shop.name':$('#pageSearch').val()});
}

//全选、取消
shop.selectAll = function(dom){
	//全选
	if($(dom).attr('checked')){
		$('#shopListTable').find('input:not(:disabled)[type=checkbox][name=selectAll]').attr('checked', true);
	}else{
		$('#shopListTable').find('input:not(:disabled)[type=checkbox][name=selectAll]').attr('checked', false);
	}
}

//批量选择
shop.batchSelect = function(){
	if($('#shopListTable').find('input:checked:not(:disabled)[type=checkbox][name=selectAll]').length == 0){
		alert('请选择您需要记录！');
		return ;
	}

	//组装数据
	var shopIds = '';
	var shopNames = '';
	$('#shopListTable').find('input:checked:not(:disabled)[type=checkbox][name=selectAll]').each(function(i,dom){
		var shopId = $(dom).parent().parent().find('input[name=shopId]').val();
		var shopName = $(dom).parent().parent().find('td:eq(2)').text();
		if(i > 0){
			shopIds += ';';
			shopNames += ';';
		}
		shopIds += shopId;
		shopNames += shopName;
	});

	//回填数据
	$('#shopIds').val(shopIds);
	$('#shopNames').val(shopNames);
	$('a.jMsgbox-closeWrap').click(); //关闭弹出框
}

$(document).ready(function(){
	//搜索
	$('#searchBut').click(function(){
		var url = $('#initPath').val()+'/pos/shop!toSelectShopListView.do';
		$('#shopListDiv').load(url, {'shop.name':$('#pageSearch').val()});
	});

	//标记已选择的店面
	if($('#_checkBox').val() == 'true'){
		var selectedShopIds = $('#_selectedShopIds').val().split(';');
		for(var i=0; i<selectedShopIds.length; i++){
			$('#shopListTable').find('tbody tr').each(function(j,dom){
				var shopId = $(dom).find('input[name=shopId]').val();
				if(shopId == selectedShopIds[i]){
					$(dom).find('input[type=checkbox][name=selectAll]').attr('checked', true).attr('disabled', true);
					$(dom).find('a').remove();
				}
			});
		}
	}
	
});
</script>
<div id="shopListDiv">
<input type="hidden" id="_checkBox" value="<c:out value="${param.checkBox}"/>"/><!-- 显示复选框，并表示为多选 -->
<input type="hidden" id="_selectedShopIds" value="<c:out value="${param.selectedShopIds}"/>"/><!-- 已选择的店面 -->

<c:if test="${param.checkBox=='true'}">
<input type="button" value="确定" onclick="shop.batchSelect();" />
</c:if>
<table id="shopListTable" width="100%" cellspacing="1" cellpadding="0" border="0" class="pn-ltable">
<thead class="pn-lthead">
<tr>
	<c:if test="${param.checkBox=='true'}">
	<th><input type="checkbox" onclick="shop.selectAll(this);" /></th>
	</c:if>
	<th>店面编号</th>
	<th>店面名称</th>
	<th>负责人</th>
	<th>地址</th>
	<th>操作</th>
</tr>
</thead>
<tbody class="pn-ltbody">
	<c:forEach var="shop" items="${page.list}">
	<tr style="height: 21px;" ondblclick="<c:if test="${param.checkBox!='true'}">$(this).find('a').click();</c:if>">
		<c:if test="${param.checkBox=='true'}">
		<td align="center"><input type="checkbox" name="selectAll"/></td>
		</c:if>
		<td align="center">${shop.code }</td>
		<td>${shop.name }</td>
		<td>${shop.address }</td>
		<td>${shop.charger }</td>
		<td align="center">
			<input type="hidden" name="shopId" value="${shop.id }" />
			<a href="javascript:void(0);" onclick="shop.selectedShop(this);">选择</a>
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