<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>维护商品信息</title>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/skin/css/admin.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/skin/css/theme.css" />
<link rel="STYLESHEET" type="text/css" href="<%=request.getContextPath()%>/js/jquery/msgbox/jquery.msgbox.css" />

<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.validate.js"></script>

<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/msgbox/jquery.dragndrop.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/msgbox/jquery.msgbox.js"></script>

<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/dhtmlxTree/dhtmlxtree.css" />
<script src="<%=request.getContextPath()%>/js/dhtmlxTree/dhtmlxcommon.js"></script>
<script src="<%=request.getContextPath()%>/js/dhtmlxTree/dhtmlxtree.js"></script>

<script type="text/javascript">
var product={};

//表单验证
product.validateForm = function(){
	//验证国际条码
	if($('#EANBarcode').hasClass('required')){
		if($('#barcode').val() == ''){
			alert('国际条码无效！');
			return false;
		}
	}
	return $('#productForm').valid();
}

//返回列表
product.backList = function(){
	window.location.href = $('#initPath').val()+'/pos/Product!productList.do';
}

$(document).ready(function(){
	//验证表单
	$("#productForm").validate();

	//修改条形码按钮
	$("#updateBarcodeBut").toggle(
		function (){
			$(this).text('使用系统条码');
			$('#systemBarcode').css('text-decoration', 'line-through');
			$('#EANBarcode').addClass('required').parent().parent().show();
			$('#barcode').val('');
		},
		function (){
			$(this).text('使用国际条码');
			$('#systemBarcode').css('text-decoration', '');
			$('#EANBarcode').removeClass('required').val('').parent().parent().hide();
			$('#barcode').val($('#systemBarcode').text());
		}
	);
	//修改国际条码
	$('#EANBarcode').change(function(){
		var EANBarcode = $('#EANBarcode').val();
		if($('#EANBarcode').valid()){
			$.post($('#initPath').val()+'/pos/Product!validateBarcode.do', {'product.barCode': EANBarcode}, function(json){
				if(json != 'true'){
					alert(json);
				}else{
					$('#barcode').val(EANBarcode);
				}
			});
		}
		
	});

	//选择商品分类
	$('#goodsClassName').click(function(){
		new $.msgbox({
			title: '选择商品分类',
			width: 300,
			height: 400,
			type: 'ajax',
			content: $('#initPath').val()+'/poscenter/goodsclass/goodsClassTree.jsp?IDS=goodsClassId&NAMES=goodsClassName&childNodeOnly=true',
			onAjaxed: function(){
			}
		}).show();
	});
	//选择供应商
	$('#supplierName').click(function(){
		new $.msgbox({
			title: '选择供应商',
			width: 700,
			height: 480,
			type: 'ajax',
			content: $('#initPath').val()+'/pos/supplier!supplierChooseList.do',
			onAjaxed: function(){
			}
		}).show();
	});

});
</script>
</head>
<body style="min-height:500px;">
<input type="hidden" id="initPath" value="<%=request.getContextPath()%>" />
<div class="box-positon">
	<div class="rpos">当前位置: 商品管理 - <c:if test="${product.id != 0}">修改商品信息</c:if><c:if test="${product.id == 0}">新增商品</c:if></div>
	<div class="ropt"></div>
	<div class="clear"></div>
</div>

<div id="bodyBox" class="body-box">
<form id="productForm" action="<%=request.getContextPath()%>/pos/Product!saveProduct.do" method="post" onsubmit="return product.validateForm();">
	<input type="hidden" id="productId" name="product.id" value="${product.id }" />
	<input type="hidden" id="supplierId" name="product.supplierId" value="${product.supplierId}" />
	<input type="hidden" id="barcode" name="product.barCode" value="${product.barCode }" />
	<table cellspacing="1" cellpadding="2" border="0" width="100%" class="pn-ftable" style="font-size:13px;">
		<tr>
			<td class="pn-flabel pn-flabel-h">条形码:</td>
			<td class="pn-fcontent">
				<span id="systemBarcode">${product.barCode }</span>&nbsp;
				<c:if test="${product.id == 0}">
				<a href="javascript:;" id="updateBarcodeBut">使用国际条码</a>
				</c:if>
			</td>
		</tr>
		<tr style="display: none;">
			<td class="pn-flabel pn-flabel-h"><span class="pn-frequired">*</span>国际条码:</td>
			<td class="pn-fcontent">
				<input type="text" id="EANBarcode" class="digits" maxlength="13" />
			</td>
		</tr>
		<tr>
			<td width="12%" class="pn-flabel pn-flabel-h"><span class="pn-frequired">*</span>商品名称:</td>
			<td width="88%" class="pn-fcontent"><input type="text" class="required" id="name" name="product.name" value="${product.name }" /></td>
		</tr>
		<tr>
			<td class="pn-flabel pn-flabel-h"><span class="pn-frequired">*</span>商品分类:</td>
			<td class="pn-fcontent">
				<input type="hidden" id="goodsClassId" name="product.goodsClassId" value="${product.goodsClassId }" />
				<input type="text" id="goodsClassName" name="className" class="required" value="${product.goodsClass.name }" readonly="readonly" />
			</td>
		</tr>
		<tr>
			<td class="pn-flabel pn-flabel-h">供货商:</td>
			<td class="pn-fcontent">
				<input type="text" id="supplierName" name="product.supplierName" readonly="readonly" value="<c:if test="${product.id != 0}">${product.supplierName }</c:if>" />
			</td>
		</tr>
		<tr>
			<td class="pn-flabel pn-flabel-h"><span class="pn-frequired">*</span>日租赁价格(元):</td>
			<td class="pn-fcontent"><input type="text" class="required money" id="leasePrice" name="product.leasePrice" value="<c:if test="${product.id != 0}">${product.leasePrice }</c:if>" /></td>
		</tr>
		<tr>
			<td class="pn-flabel pn-flabel-h"><span class="pn-frequired">*</span>包月租赁价格(元):</td>
			<td class="pn-fcontent"><input type="text" class="required money" id="monthLeasePrice" name="product.monthLeasePrice" value="<c:if test="${product.id != 0}">${product.monthLeasePrice }</c:if>" /></td>
		</tr>
		<tr>
			<td class="pn-flabel pn-flabel-h"><span class="pn-frequired">*</span>押金(元):</td>
			<td class="pn-fcontent"><input type="text" class="required money" id="deposit" name="product.deposit" value="<c:if test="${product.id != 0}">${product.deposit }</c:if>" /></td>
		</tr>
		<tr>
			<td class="pn-flabel pn-flabel-h"><span class="pn-frequired">*</span>标牌价(元):</td>
			<td class="pn-fcontent"><input type="text" class="required money" name="product.salePrice" value="<c:if test="${product.id != 0}">${product.salePrice }</c:if>" /></td>
		</tr>
		<tr>
			<td class="pn-flabel pn-flabel-h">限价(元):</td>
			<td class="pn-fcontent"><input type="text" class="money" name="product.limitPrice" value="<c:if test="${product.id != 0}">${product.limitPrice }</c:if>" /></td>
		</tr>
		<tr>
			<td class="pn-flabel pn-flabel-h">锁价(元):</td>
			<td class="pn-fcontent"><input type="text" class="money" name="product.lockPrice" value="<c:if test="${product.id != 0}">${product.lockPrice }</c:if>" /></td>
		</tr>
		<tr>
			<td class="pn-flabel pn-flabel-h">红卡额度:</td>
			<td class="pn-fcontent"><input type="text" class="floats" name="product.redLines" value="<c:if test="${product.id != 0}">${product.redLines }</c:if>" /></td>
		</tr>
		<tr>
			<td class="pn-flabel pn-flabel-h">蓝卡额度:</td>
			<td class="pn-fcontent"><input type="text" class="floats" name="product.blueLines" value="<c:if test="${product.id != 0}">${product.blueLines }</c:if>" /></td>
		</tr>
		<tr>
			<td class="pn-fcontent" align="center" colspan="2">
				<input type="submit" class="submit" value="提交" />
				<input type="button" class="return-button" value="返回" onclick="product.backList();" />
			</td>
		</tr>
	</table>
</form>
</div>
</body>
</html>