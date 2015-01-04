<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false"%>
<%@ include file="/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>盘点条目列表</title>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/skin/css/admin.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/skin/css/theme.css" />
<link rel="STYLESHEET" type="text/css" href="<%=request.getContextPath()%>/js/jquery/msgbox/jquery.msgbox.css" />

<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.validate.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/My97DatePicker/WdatePicker.js"></script>

<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/msgbox/jquery.dragndrop.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/msgbox/jquery.msgbox.js"></script>

<script type="text/javascript">
var checkStockItem={};

//跳转到指定页面
checkStockItem.goPage = function(pageNum,pageCount){
	var date = $('#date').val();
	var url = $('#initPath').val()+'/pos/checkStockItem!checkStockItemList.do?checkStockId='+$('#checkStockId').val();
	if(pageNum != null && pageCount != null){
		url += '&page.pageNum='+pageNum;
	}
	if(pageCount != null){
		url += '&page.pageCount='+pageCount;
	}
	window.location.href = url;
}

//新增盘点
checkStockItem.addCheckStock = function(){
	var date = $('#date').val();
	var url = $('#initPath').val()+'/pos/checkStock!createEmptyCheckStock.do?date='+date;
	window.location.href = url;
}

//保存初盘
checkStockItem.saveFirstCheck = function(dom,id){
	var count = $(dom).parent().parent().find('input[name=count]').val();
	var charger = $(dom).parent().parent().find('input[name=charger]').val();
	var remark = $(dom).parent().parent().find('textarea[name=remark]').val();
	if(count=='' || isNaN(count) || Number(count)<0) {
		alert('盘点数量无效！');
		return ;
	}
	if(charger=='') {
		alert('盘点负责人不能为空！');
		return ;
	}

	var data={'checkStockItem.id':id, 'checkStockItem.count':count, 'checkStockItem.charger':charger, 'checkStockItem.remark':remark};
	$.post($('#initPath').val()+'/pos/checkStockItem!saveFirstCheck.do', data, function(json){
		if(json){
			checkStockItem.goPage();
		}else{
			alert('操作失败！');
		}
	});
}

//保存复盘
checkStockItem.saveAgainCheck = function(dom,productId){
	var count = $(dom).parent().parent().next().find('input[name=count]').val();
	var charger = $(dom).parent().parent().next().find('input[name=charger]').val();
	var remark = $(dom).parent().parent().next().find('textarea[name=remark]').val();
	var stock = $(dom).parent().find('input[name=stock]').val();
	if(count=='' || isNaN(count) || Number(count)<0) {
		alert('盘点数量无效！');
		return ;
	}
	if(charger=='') {
		alert('盘点负责人不能为空！');
		return ;
	}

	var data={'checkStockItem.checkStockId':$('#checkStockId').val(), 'checkStockItem.productId':productId, 'checkStockItem.stock':stock, 'checkStockItem.count':count, 'checkStockItem.charger':charger, 'checkStockItem.remark':remark};
	$.post($('#initPath').val()+'/pos/checkStockItem!saveAgainCheck.do', data, function(json){
		if(json){
			checkStockItem.goPage();
		}else{
			alert('操作失败！');
		}
	});
}

//点击复盘按钮
checkStockItem.clickAgainCheckBut = function(dom,productId){
	var stock = $(dom).parent().find('input[name=stock]').val();
	//复盘
	new $.msgbox({
		title: '商品复盘',
		width: 600,
		height: 380,
		type: 'ajax',
		content: $('#initPath').val()+'/poscenter/checkstock/againCheckForm.jsp?productId='+productId+'&stock='+stock,
		onAjaxed: function(){
		}
	}).show();
}

//更新商品库存
checkStockItem.updateProductStock = function(dom,productId){
	if(confirm('确定更新该商品的库存信息吗？')){
		$.getJSON($('#initPath').val()+'/pos/checkStockItem!updateProductStock.do', {productId: productId, date: $('#date').val()}, function(json){
			if(json){
				$(dom).hide();
				alert('更新成功！');
			}else{
				alert('更新失败！');
			}
		});
	}
}

//打印盘点表
checkStockItem.printCheckStockItem = function(){
	var url = $('#initPath').val()+'/pos/checkStockItem!toPrintCheckStockItemView.do?checkStockId='+$('#checkStockId').val();
	window.open(url);
}

//提交盘点任务
checkStockItem.submitCheckStock = function(){
	if(!$('#checkStockForm').valid()){return;}
	return confirm('提交后将不能进行复盘操作，并会根据最后一次复盘数量自动更新商品库存，确定提交吗？');
}

//返回列表
checkStockItem.backList = function(){
	window.location.href = $('#initPath').val()+'/pos/checkStock!checkStockList.do';
}

//全选、取消
checkStockItem.selectAll = function(dom){
	//全选
	if($(dom).attr('checked')){
		$('#checkStockItemTable').find('input:not(:disabled)[type=checkbox][name=selectAll]').attr('checked', true);
	}else{
		$('#checkStockItemTable').find('input:not(:disabled)[type=checkbox][name=selectAll]').attr('checked', false);
	}
}

//批量提交
checkStockItem.batchSubmit = function(dom){
	if($('#checkStockItemTable').find('input:checked[type=checkbox][name=selectAll]').length == 0){
		alert('请选择需要提交的记录！');
		return ;
	}

	//组装数据
	var data = {};
	var isValid = true;
	$('#checkStockItemTable').find('input:checked[type=checkbox][name=selectAll]').each(function(i,dom){
		//判断是否为初盘
		var count, charger, remark;
		var isFirst = $(dom).attr('checktimes') == 0;
		if(isFirst){
			var itemId = $(dom).attr('id');
			count = $(dom).parent().parent().find('input[name=count]').val();
			charger = $(dom).parent().parent().find('input[name=charger]').val();
			remark = $(dom).parent().parent().find('textarea[name=remark]').val();
			data['checkStockItemList['+i+'].id'] = itemId;
		}else{
			count = $(dom).parent().parent().next().find('input[name=count]').val();
			charger = $(dom).parent().parent().next().find('input[name=charger]').val();
			remark = $(dom).parent().parent().next().find('textarea[name=remark]').val();
		}

		if(count=='' || isNaN(count) || Number(count)<0) {
			isValid = false;
			alert('盘点数量无效！');
			return false;
		}
		if(charger=='') {
			isValid = false;
			alert('盘点负责人不能为空！');
			return false;
		}
		data['checkStockItemList['+i+'].checkStockId'] = $('#checkStockId').val();
		data['checkStockItemList['+i+'].productId'] = $(dom).attr('productid');
		data['checkStockItemList['+i+'].stock'] = $(dom).parent().parent().find('input[name=stock]').val();
		data['checkStockItemList['+i+'].count'] = count;
		data['checkStockItemList['+i+'].charger'] = charger;
		data['checkStockItemList['+i+'].remark'] = remark;
	});

	//表单验证
	if(!isValid){
		return false;
	}

	//保存
	$.post($('#initPath').val()+'/pos/checkStockItem!saveBatchCheck.do', data, function(json){
		if(json){
			checkStockItem.goPage();
		}else{
			alert('操作失败！');
		}
	});
}

$(document).ready(function(){
	//验证表单
	$("#checkStockForm").validate();
});

</script>
</head>
<body style="min-height: 500px;">
<input type="hidden" id="initPath" value="<%=request.getContextPath()%>" />
<input type="hidden" id="checkStockId" value="${checkStockId }" />
<form id="checkStockForm" action="<%=request.getContextPath()%>/pos/checkStockItem!submitCheckStock.do" method="post" onsubmit="return checkStockItem.submitCheckStock();">
<div class="box-positon">
	<div class="rpos">当前位置: 盘点管理 - 盘点条目列表</div>
	<div class="ropt">
		<c:if test="${canSubmit}">
			<auth:authorize ifGranted="submitCheckStockTask">
			<input type="submit" value="提交盘点任务" />
			</auth:authorize>
		</c:if>
		<c:if test="${checkStock.useStatus == 1}">
			<input type="button" onclick="checkStockItem.printCheckStockItem();" value="打印盘点表" /> &nbsp; 
		</c:if>
		<input type="button" class="return-button" value="返回" onclick="checkStockItem.backList();" />&nbsp;&nbsp;
	</div>
	<div class="clear"></div>
</div>

<c:if test="${canSubmit}">
<table cellspacing="1" cellpadding="2" border="0" width="99%" class="pn-ftable" style="font-size:13px; margin-left:5px;">
	<tr>
		<td width="12%" class="pn-flabel pn-flabel-h"><span class="pn-frequired">*</span>负责人:</td>
		<td width="88%" class="pn-fcontent">
			<input type="text" name="checkStock.charger" class="required" value="${checkStock.charger }" />
			<input type="hidden" name="checkStock.id" value="${checkStockId}" />
			<input type="hidden" name="checkStockId" value="${checkStockId}" />
		</td>
	</tr>
	<tr>
		<td width="12%" class="pn-flabel pn-flabel-h">备注:</td>
		<td width="88%" class="pn-fcontent">
			<textarea rows="3" cols="30" name="checkStock.remark"></textarea>
		</td>
	</tr>
</table>
</c:if>
<c:if test="${checkStock.useStatus == 1}">
<table cellspacing="1" cellpadding="2" border="0" width="99%" class="pn-ftable" style="font-size:13px; margin-left:5px;">
	<tr>
		<td width="12%" class="pn-flabel pn-flabel-h">负责人：</td>
		<td width="88%" class="pn-fcontent">${checkStock.charger }</td>
	</tr>
	<tr>
		<td class="pn-flabel pn-flabel-h">备注：</td>
		<td class="pn-fcontent">${checkStock.remark }</td>
	</tr>
</table>
</c:if>
</form>

<div id="bodyBox" class="body-box">
<c:if test="${checkStock.useStatus == 0 && !canSubmit}">
<input type="button" onclick="checkStockItem.batchSubmit();" value="批量提交" class="generate-index-page" style="margin-top: 5px;" />
</c:if>
<table id="checkStockItemTable" width="100%" cellspacing="1" cellpadding="0" border="0" style="" class="pn-ltable">
<thead class="pn-lthead">
	<tr>
		<c:if test="${checkStock.useStatus == 0 && !canSubmit}">
		<th><input title="全选" type="checkbox" onclick="checkStockItem.selectAll(this);" checked="checked" /></th>
		</c:if>
		<th>商品名称</th>
		<th>盘点时间</th>
		<th>盘点数量</th>
		<th>盘点负责人</th>
		<th>备注</th>
		<th>操作</th>
	</tr>
</thead>
<tbody class="pn-ltbody">
	<c:forEach var="checkStockItemList" items="${checkStockItemListList}" varStatus="status">
		<!-- 记录盘点次数 -->
		<c:set var="checkTimes" value="${fn:length(checkStockItemList)}" />
		<c:set var="nowProductId" value="0" />
		<c:forEach var="checkStockItem" items="${checkStockItemList}" end="0">
		
		<!-- 计算盘点次数 -->
		<c:if test="${checkStockItem.count==-1}">
			<c:set var="checkTimes" value="0" />
		</c:if>
		<c:set var="nowProductId" value="${checkStockItem.productId }" />
		
		<!-- 计算表格跨行数 -->
		<c:set var="rowspan" value="${fn:length(checkStockItemList)}" />
		<c:if test="${checkStockItem.count!=-1 && fn:length(checkStockItemList)==1}">
			<c:set var="rowspan" value="2" />
		</c:if>
		
		<tr <c:if test="${status.index%2 == 1}">style="background-color:#eeeeee;"</c:if>>
			<c:if test="${checkStock.useStatus == 0 && !canSubmit}">
			<td align="center" rowspan="${rowspan}"><input id="${checkStockItem.id}" productid="${checkStockItem.productId}" type="checkbox" name="selectAll" checktimes="${checkTimes}" <c:if test="${checkTimes <= 1}">checked="checked"</c:if> <c:if test="${checkTimes > 1}">disabled="disabled"</c:if> /></td>
			</c:if>
			<td rowspan="${rowspan}">${checkStockItem.product.name }</td>
			<td width="200" align="center"><fmt:formatDate value="${checkStockItem.checkTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
			<td align="center">
				<c:if test="${checkStock.useStatus==0 && checkStockItem.count==-1}">
					<input type="text" name="count" size="5" value="" />
				</c:if>
				<c:if test="${checkStockItem.count!=-1}">${checkStockItem.count}</c:if>
			</td>
			<td align="center">
				<c:if test="${checkStock.useStatus==0 && checkStockItem.count==-1}">
					<input type="text" name="charger" size="5" value="${checkStockItem.charger}" />
				</c:if>
				<c:if test="${checkStockItem.count!=-1}">${checkStockItem.charger}</c:if>
			</td>
			<td align="center">
				<c:if test="${checkStock.useStatus==0 && checkStockItem.count==-1}">
					<textarea name="remark" rows="2" cols="20"></textarea>
				</c:if>
				<c:if test="${checkStockItem.count!=-1}">${checkStockItem.remark}</c:if>
			</td>
			<td rowspan="${rowspan}" align="center">
				<c:if test="${checkStock.useStatus==0 && checkStockItem.count==-1}">
					<input type="button" value="提交" onclick="checkStockItem.saveFirstCheck(this,${checkStockItem.id});" />
				</c:if>
				<c:if test="${checkStock.useStatus==0 && checkStockItem.count!=-1}">
					<input type="hidden" name="stock" value="${checkStockItem.stock}" />
					<c:if test="${checkTimes == 1}">
					<input type="button" value="提交" onclick="checkStockItem.saveAgainCheck(this,${checkStockItem.productId});" />
					</c:if>
					<c:if test="${fn:length(checkStockItemList) > 1}">
						<input type="button" value="复盘" onclick="checkStockItem.clickAgainCheckBut(this,${checkStockItem.productId });" />
						<!--<input type="button" value="更新库存" onclick="checkStockItem.updateProductStock(this,${checkStockItem.productId });" />-->
					</c:if>
				</c:if>
			</td>
		</tr>
		</c:forEach>
		
		<c:if test="${fn:length(checkStockItemList) > 1}">
		<c:forEach var="checkStockItem" items="${checkStockItemList}" begin="1">
		<tr <c:if test="${status.index%2 == 1}">style="background-color:#eeeeee;"</c:if>>
			<td align="center"><fmt:formatDate value="${checkStockItem.checkTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
			<td align="center"><fmt:formatNumber value="${checkStockItem.count}" pattern="#,###" /></td>
			<td align="center">${checkStockItem.charger }</td>
			<td align="center">${checkStockItem.remark }</td>
		</tr>
		</c:forEach>
		</c:if>
		
		<!-- 已经盘点过一次 -->
		<c:if test="${checkTimes == 1}">
		<tr id="moreCheckDiv_${nowProductId }" <c:if test="${status.index%2 == 1}">style="background-color:#eeeeee;"</c:if>>
			<td align="center"></td>
			<td align="center"><input type="text" name="count" size="5" /></td>
			<td align="center"><input type="text" name="charger" size="5" /></td>
			<td align="center"><textarea name="remark" rows="2" cols="20"></textarea></td>
		</tr>
		</c:if>
		
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
		<a href="javascript:;" onclick="checkStockItem.goPage(1,${page.pageCount});">首页</a>&nbsp;&nbsp;&nbsp;
		<a href="javascript:;" onclick="checkStockItem.goPage(${page.pageNum-1},${page.pageCount});">上一页</a>
		</c:if>
		&nbsp;&nbsp;&nbsp;
		<c:if var="isLastPage" test="${page.pageNum>=page.totalPages}">
		下一页&nbsp;&nbsp;&nbsp;尾页&nbsp;&nbsp;&nbsp;
		</c:if>
		<c:if test="${!isLastPage}">
		<a href="javascript:;" onclick="checkStockItem.goPage(${page.pageNum+1},${page.pageCount});">下一页</a>&nbsp;&nbsp;&nbsp;
		<a href="javascript:;" onclick="checkStockItem.goPage(${page.totalPages},${page.pageCount});">尾页</a>
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