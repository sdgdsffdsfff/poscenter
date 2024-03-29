<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>红蓝卡管理</title>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/skin/css/admin.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/skin/css/theme.css" />
<link rel="STYLESHEET" type="text/css" href="<%=request.getContextPath()%>/js/jquery/msgbox/jquery.msgbox.css" />

<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.validate.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.form.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/msgbox/jquery.dragndrop.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/msgbox/jquery.msgbox.js"></script>

<script type="text/javascript">
var priceCard={};

//表单验证
priceCard.validateForm = function(){
	if(!$('#priceCardForm').valid()){return false;}
	
	if($('#type').val() == 2){
	    if($('#supplierName').val() == ''){
	            alert('必须输入供应商名称！');
	            return false;
	       }
	}
	
	if($('#password').val() == ''){
	   alert('必须输入密码');
	   return false;
	}
	if($('#password').val() != $('#password1').val()){
	  alert('两次输入密码不一致');
	  return false;
	}
	return true;
}

priceCard.changeCardType = function(type){
    if(type == 1){
       $('#supplierId').parent().parent().hide();
       $('#supplierId').val('');
       $('#supplierName').val('');
    }
    if(type == 2){
       $('#supplierId').parent().parent().show();
    }

}

//返回列表
priceCard.backList = function(){
	window.location.href = $('#initPath').val()+'/pos/priceCard!priceCardList.do';
}

$(document).ready(function(){
	//验证表单
	$("#priceCardForm").validate();
	
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
	
	//选择店面
	$('#shopName').click(function(){
		new $.msgbox({
			title: '选择店面店',
			width: 700,
			height: 480,
			type: 'ajax',
			content: $('#initPath').val()+'/pos/shop!toSelectShopListView.do',
			onAjaxed: function(){
			}
		}).show();
	});
	
	//提交表单
	$('#priceCardForm').submit(function(){
		if(!priceCard.validateForm()){return false;}
		$(this).ajaxSubmit({
			dataType: 'text',
			success: function(json){
				if(json == 'success'){
					priceCard.backList();
				}else{
					alert(json);
				}
			},
			error: function(msg){
				alert(msg);
			}
		});

		//(重要)always return false to prevent standard browser submit and page navigation 
		return false;
	});
});
</script>
</head>
<body style="min-height:500px;">
<input type="hidden" id="initPath" value="<%=request.getContextPath()%>" />
<div class="box-positon">
	<div class="rpos">当前位置: 红蓝卡管理 - 开卡</div>
	<div class="ropt"></div>
	<div class="clear"></div>
</div>

<div id="bodyBox" class="body-box">
	<form id="priceCardForm" action="<%=request.getContextPath()%>/pos/priceCard!openPriceCard.do" method="post" >
	
	<table cellspacing="1" cellpadding="2" border="0" width="100%" class="pn-ftable" style="font-size:13px;">
		<tr>
			<td width="12%" class="pn-flabel pn-flabel-h"><span class="pn-frequired">*</span>卡号:</td>
			<td width="88%" class="pn-fcontent"><input type="hidden" class="required" name="priceCard.id" value="${priceCard.id }" />${priceCard.id }</td>
		</tr>
		<tr>
			<td class="pn-flabel pn-flabel-h"><span class="pn-frequired">*</span>店铺名称:</td>
			<input type="hidden" id="shopId" name="priceCard.shopId" value="${priceCard.shopId}" />
			<td class="pn-fcontent"><input type="text" class="required" id="shopName" name="priceCard.shopName" value="${priceCard.shopName }" /></td>
		</tr>
		<tr>
			<td class="pn-flabel pn-flabel-h"><span class="pn-frequired">*</span>店员姓名:</td>
			<td class="pn-fcontent"><input type="text" class="required" name="priceCard.clerkName" value="${priceCard.clerkName }" /></td>
		</tr>
		<tr>
			<td class="pn-flabel pn-flabel-h">卡密码:</td>
			<td class="pn-fcontent"><input type="password" id="password" name="priceCard.password" value="" /></td>
		</tr>
		<tr>
			<td class="pn-flabel pn-flabel-h">重复卡密码:</td>
			<td class="pn-fcontent"><input type="password" id="password1"  /></td>
		</tr>
		<tr>
			<td class="pn-flabel pn-flabel-h">类型:</td>
			<td class="pn-fcontent"><input type="hidden" id="type" name="priceCard.type" value="${priceCard.type }"><c:if test="${priceCard.type==1 }">红卡</c:if><c:if test="${priceCard.type==2 }">蓝卡</c:if></td>
		</tr>
		<tr <c:if test="${priceCard.type==1 }">style="display: none;"</c:if>>
				<td class="pn-flabel pn-flabel-h"><span class="pn-frequired">*</span>供货商:<input type="hidden"  id="supplierId" name="priceCard.supplierId" value="${priceCard.supplierId}" /></td>
				<td class="pn-fcontent"><input type="text"  id="supplierName" name="priceCard.supplierName" value="${priceCard.supplierName}"/></td>
		</tr>
		<tr>
			<td class="pn-flabel pn-flabel-h">状态:</td>
			<td class="pn-fcontent">白卡
			</td>
		</tr>
		<tr>
			<td class="pn-fcontent" align="center" colspan="2">
				<input type="submit" class="submit" value="开卡" />
				<input type="button" class="return-button" value="返回" onclick="priceCard.backList();" />
			</td>
		</tr>
	</table>
	</form>
</div>
</body>
</html>