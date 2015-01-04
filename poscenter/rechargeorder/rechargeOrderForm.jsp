<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>维护充值单信息</title>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/skin/css/admin.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/skin/css/theme.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/jquery/msgbox/jquery.msgbox.css" />

<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.validate.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.form.js"></script>

<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/msgbox/jquery.dragndrop.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/msgbox/jquery.msgbox.js"></script>

<script type="text/javascript">
var rechargeOrder={};

//返回列表
rechargeOrder.backList = function(){
	window.location.href = $('#initPath').val()+'/pos/rechargeOrder!rechargeOrderList.do';
}

//提交
rechargeOrder.saveOrSubmit = function(useStatus){
	//表单验证
	if(!$('#rechargeOrderForm').valid()){
		return ;
	}

	if(useStatus==2 && !confirm('提交后将不能修改充值单信息，确定提交吗？')) {
		return ;
	}

	var data = {};
	data['rechargeOrder.useStatus'] = useStatus;
	if(useStatus == 2) {
		data['rechargeOrder.auditStatus'] = 1;
	}
	
	$('#rechargeOrderForm').ajaxSubmit({
		dataType: 'text',
		data: data,
		success: function(json){
			if(json == 'success'){
				rechargeOrder.backList();
			}else{
				alert(json);
			}
		},
		error: function(msg){
			alert(msg);
		}
	});
}

$(document).ready(function(){
	//验证表单
	$("#rechargeOrderForm").validate();

	//选择调价卡
	$('#priceCardId').click(function(){
		new $.msgbox({
			title: '选择调价卡',
			width: 700,
			height: 480,
			type: 'ajax',
			content: $('#initPath').val()+'/pos/priceCard!toSelectPriceCardListView.do'
		}).show();
	});
});
</script>
</head>
<body style="min-height:500px;">
<input type="hidden" id="initPath" value="<%=request.getContextPath()%>" />
<div class="box-positon">
	<div class="rpos">当前位置: 充值单管理 - <c:if test="${rechargeOrder.id != 0}">修改充值单信息</c:if><c:if test="${rechargeOrder.id == 0}">新增充值单</c:if></div>
	<div class="ropt"></div>
	<div class="clear"></div>
</div>

<div id="bodyBox" class="body-box">
<form id="rechargeOrderForm" action="<%=request.getContextPath()%>/pos/rechargeOrder!saveRechargeOrder.do" method="post">
<input type="hidden" name="rechargeOrder.id" value="${rechargeOrder.id }" />
<table cellspacing="1" cellpadding="2" border="0" width="100%" class="pn-ftable" style="font-size:13px;">
	<tr>
		<td width="12%" class="pn-flabel pn-flabel-h"><span class="pn-frequired">*</span>调价卡:</td>
		<td width="88%" class="pn-fcontent">
			<input type="text" id="priceCardId" name="rechargeOrder.priceCardId" class="required" value="${rechargeOrder.priceCardId }" readonly="readonly" />
		</td>
	</tr>
	<tr>
		<td class="pn-flabel pn-flabel-h"><span class="pn-frequired">*</span>充值点数:</td>
		<td class="pn-fcontent"><input type="text" class="required money" name="rechargeOrder.point" value="<c:if test="${rechargeOrder.id != 0}">${rechargeOrder.point }</c:if>" /></td>
	</tr>
	<tr>
		<td class="pn-fcontent" align="center" colspan="2">
			<input type="button" class="submit" value="保存" onclick="rechargeOrder.saveOrSubmit(1);" />&nbsp;&nbsp;
			<input type="button" class="submit" value="提交" onclick="rechargeOrder.saveOrSubmit(2);" />&nbsp;&nbsp;
			<input type="button" class="return-button" value="返回" onclick="rechargeOrder.backList();" />
		</td>
	</tr>
</table>
</form>
</div>
</body>
</html>