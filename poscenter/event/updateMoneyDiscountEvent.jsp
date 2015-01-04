<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>修改金额折扣活动</title>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/skin/css/admin.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/skin/css/theme.css" />
<link rel="STYLESHEET" type="text/css" href="<%=request.getContextPath()%>/js/jquery/msgbox/jquery.msgbox.css" />

<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/My97DatePicker/WdatePicker.js"></script>

<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/msgbox/jquery.dragndrop.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/msgbox/jquery.msgbox.js"></script>

<script type="text/javascript">
var moneyDiscountEvent={};

//返回列表
moneyDiscountEvent.backList = function(){
	window.location.href = $('#initPath').val()+'/pos/event!eventList.do';
}

//删除选择的店面
moneyDiscountEvent.deleteShop = function(dom){
	$(dom).parent().parent().remove();
}

//删除金额折扣
moneyDiscountEvent.deleteMoneyDiscount = function(dom, moneyDiscountEventId){
	if($('#moneyListTable tr:gt(0)').length == 1){
		alert('至少保留一个金额折扣！');
	}else{
		//删除数据库中的记录
		if(moneyDiscountEventId != null){
			$.post($('#initPath').val()+'/pos/moneyDiscountEvent!deleteMoneyDiscountEvent.do', {'moneyDiscountEventId':moneyDiscountEventId}, function(data){
				if(data == 'success'){
					$(dom).parent().parent().remove();
				}else{
					alert('删除失败：'+data);
				}
			});
		}else{
			$(dom).parent().parent().remove();
		}
	}
}

//添加金额折扣
moneyDiscountEvent.addMoneyDiscount = function(){
	$("#moneyListTable").append($("#discountCloneTable tr").clone());
}

//改变金额
moneyDiscountEvent.changeMoney = function(dom){
	var value = $(dom).val();
	if(value=='' || isNaN(value) || Number(value)<=0){
		alert('金额无效！');
		$(dom).val('');
		return;
	}
	var index = $(dom).parent().parent().index();
	$('#moneyListTable tr:gt(0)').each(function(i,domE){
		if(i+1 != index){
			var money = $(domE).find('input[name=money]').val();
			if(Number(money) == Number(value)){
				alert('消费总金额等于 '+value+' 的记录已存在！');
				$(dom).val('');
			}
		}
	});
}

//改变优惠数值
moneyDiscountEvent.changeNumberValue = function(dom){
	var value = $(dom).val();
	var type = $(dom).parent().find('select[name=type]').val();
	if(type==1){
		if(value=='' || isNaN(value) || Number(value)<=0){
			alert('数据无效！');
			$(dom).val('');
		}
	}else{
		if(value=='' || isNaN(value) || Number(value)<=0 || Number(value)>=100){
			alert('数据无效！');
			$(dom).val('');
		}
	}
}

//切换优惠类型
moneyDiscountEvent.changeType = function(dom){
	var value = $(dom).val();
	if(value==1){
		$(dom).parent().find('span[name=dUnit]').hide();
		$(dom).parent().find('span[name=mUnit]').show();
	}else{
		$(dom).parent().find('span[name=mUnit]').hide();
		$(dom).parent().find('span[name=dUnit]').show();
		var numberValue = $(dom).parent().find('input[name=numberValue]').val();
		if(numberValue=='' || isNaN(numberValue) || Number(numberValue)<0 || Number(numberValue)>100){
			$(dom).parent().find('input[name=numberValue]').val('');
		}
	}
}

//表单验证
moneyDiscountEvent.validateForm = function(){
	var isValid = true;
	if($('#startTime').val()=='' || $('#endTime').val()==''){
		alert('活动时间不能为空！');
		return false;
	}
	if($('#startTime').val() >= $('#endTime').val()){
	    alert('活动开始时间大于活动结束时间,不合法！');
	    return false;
	} 
	if($('#eventShopListTable tr').length == 0){
		alert('适用门店不能为空！');
		return false;
	}
	
	//金额折扣
	$('#moneyListTable tr:gt(0)').each(function(i,dom){
		var money = $(dom).find('input[name=money]').val();
		var numberValue = $(dom).find('input[name=numberValue]').val();
		if(money == ''){
			alert('消费总金额为空！');
			isValid = false;
			return false;
		}
		if(numberValue == ''){
			alert('优惠数值为空！');
			isValid = false;
			return false;
		}
	});
	return isValid;
}

//提交
moneyDiscountEvent.saveOrSubmit = function(useStatus){
	//表单验证
	if(!moneyDiscountEvent.validateForm()){
		return ;
	}

	var data={};
	data['event.id'] = $('#eventId').val();
	data['event.useStatus'] = useStatus;
	data['event.type'] = 3;
	data['event.startTime'] = $('#startTime').val()+':00';
	data['event.endTime'] = $('#endTime').val()+':00';
	data['event.ruleDesc'] = $('#ruleDesc').val();
	//适用店面
	$('#eventShopListTable tr').each(function(i,dom){
		var shopId = $(dom).attr('shopid');
		data['event.shopList['+i+'].id'] = shopId;
	});

	//金额折扣
	$('#moneyListTable tr:gt(0)').each(function(i,dom){
		var money = $(dom).find('input[name=money]').val();
		var numberValue = $(dom).find('input[name=numberValue]').val();
		var type = $(dom).find('select[name=type]').val();
		if(type==2){
			numberValue = numberValue/100;
		}
		data['event.moneyDiscountEventList['+i+'].money'] = money;
		data['event.moneyDiscountEventList['+i+'].numberValue'] = numberValue;
		data['event.moneyDiscountEventList['+i+'].type'] = type;
		var id = $(dom).attr('moneyDiscountEventId');
		if(id != null){
			data['event.moneyDiscountEventList['+i+'].id'] = id;
		}
	});

	//保存
	$.post($('#initPath').val()+'/pos/moneyDiscountEvent!saveEvent.do', data, function(result){
		if(result == 'success'){
			alert('保存成功！');
			moneyDiscountEvent.backList();
		}else{
			alert('保存失败：'+result);
		}
	});
}

$(document).ready(function(){
	//选择店面
	$('#selectShopBut').click(function(){
		//已选择店面
		var selectedShopIds = [];
		$('#eventShopListTable tr').each(function(i,dom){
			selectedShopIds.push($(dom).attr('shopid'));
		});
		new $.msgbox({
			title: '选择店面',
			width: 700,
			height: 480,
			type: 'ajax',
			content: $('#initPath').val()+'/pos/shop!toSelectShopListView.do?checkBox=true&selectedShopIds='+selectedShopIds.join(';'),
			onClose: function(){
				if($('#shopIds').val() != ''){
					var shopIds = $('#shopIds').val().split(';');
					var shopNames = $('#shopNames').val().split(';');
					for(var i=0; i<shopIds.length; i++){
						var tr = '<tr shopid="'+shopIds[i]+'"><td class="pn-fcontent" width="200">'+shopNames[i]+'</td>'+
							'<td class="pn-fcontent" width="50" align="center"><a href="javascript:;" onclick="moneyDiscountEvent.deleteShop(this);">删除</a></td></tr>';
						$('#eventShopListTable').append(tr);
					}
					$('#shopIds').val('');
					$('#shopNames').val('');
				}
			}
		}).show();
	});
});
</script>
</head>
<body style="min-height:500px;">
<input type="hidden" id="initPath" value="<%=request.getContextPath()%>" />
<div class="box-positon">
	<div class="rpos">当前位置: 促销活动管理 - 修改金额折扣活动</div>
	<div class="ropt"></div>
	<div class="clear"></div>
</div>

<div id="bodyBox" class="body-box">
<form>
<input type="hidden" id="eventId" value="${event.id }" />
<input type="hidden" id="shopIds" />
<input type="hidden" id="shopNames" />
<input type="hidden" id="productId" />
<input type="hidden" id="productName" />
<table cellspacing="1" cellpadding="2" border="0" width="100%" class="pn-ftable" style="font-size:13px;">
	<tr>
		<td width="12%" class="pn-flabel pn-flabel-h">活动类型:</td>
		<td width="88%" class="pn-fcontent">金额折扣</td>
	</tr>
	<tr>
		<td width="12%" class="pn-flabel pn-flabel-h"><span class="pn-frequired">*</span>活动时间:</td>
		<td width="88%" class="pn-fcontent">
			<input type="text" id="startTime" class="Wdate" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})" value="${startTime }" readonly="readonly" /> - 
			<input type="text" id="endTime" class="Wdate" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})" value="${endTime }" readonly="readonly"/>&nbsp;&nbsp;
		</td>
	</tr>
	<tr>
		<td class="pn-flabel pn-flabel-h"><span class="pn-frequired">*</span>适用门店:</td>
		<td class="pn-fcontent">
			<a href="javascript:;" id="selectShopBut" style="color: blue;">选择店面</a>
			<table id="eventShopListTable" class="pn-ftable" cellspacing="1">
				<c:forEach var="shop" items="${event.shopList}">
				<tr shopid="${shop.id}">
					<td class="pn-fcontent" width="200">${shop.name}</td>
					<td class="pn-fcontent" width="50" align="center">
						<c:if test="${event.useStatus == 1}">
						<a href="javascript:;" onclick="moneyDiscountEvent.deleteShop(this);">删除</a>
						</c:if>
					</td>
				</tr>
				</c:forEach>
			</table>
		</td>
	</tr>
	<tr>
		<td class="pn-flabel pn-flabel-h">规则描述:</td>
		<td class="pn-fcontent"><textarea rows="2" cols="30" id="ruleDesc">${event.ruleDesc }</textarea></td>
	</tr>
	<tr>
		<td class="pn-flabel pn-flabel-h"><span class="pn-frequired">*</span>具体规则:</td>
		<td class="pn-fcontent">
			<table id="moneyListTable" width="100%" class="pn-ftable" cellspacing="1">
				<tr align="center">
					<td class="pn-fcontent" width="40%">消费总计金额</td>
					<td class="pn-fcontent" width="40%">享受优惠</td>
					<td class="pn-fcontent">操作</td>
				</tr>
				<c:forEach var="moneyDiscountEvent" items="${event.moneyDiscountEventList}">
				<tr align="center" moneyDiscountEventId="${moneyDiscountEvent.id }">
					<td class="pn-fcontent">
						<input type="text" name="money" onchange="moneyDiscountEvent.changeMoney(this);" value="${moneyDiscountEvent.money }" />
					</td>
					<td class="pn-fcontent">
						<select name="type" onchange="moneyDiscountEvent.changeType(this);">
							<option value="1" <c:if test="${moneyDiscountEvent.type==1}">selected="selected"</c:if>>减钱</option>
							<option value="2" <c:if test="${moneyDiscountEvent.type==2}">selected="selected"</c:if>>折扣</option>
						</select>
						<c:set var="numberValue" value="${moneyDiscountEvent.numberValue }" />
						<c:if test="${moneyDiscountEvent.type==2}">
							<c:set var="numberValue" value="${moneyDiscountEvent.numberValue * 100}" />
						</c:if>
						<input type="text" size="10" name="numberValue" onchange="moneyDiscountEvent.changeNumberValue(this);" value="<fmt:formatNumber value="${numberValue}" pattern="###.##" />" />
						<span name="mUnit" <c:if test="${moneyDiscountEvent.type==2}">style="display:none;"</c:if>>元</span>
						<span name="dUnit" <c:if test="${moneyDiscountEvent.type==1}">style="display:none;"</c:if>>折</span>
					</td>
					<td class="pn-fcontent"><a href="javascript:;" onclick="moneyDiscountEvent.deleteMoneyDiscount(this, ${moneyDiscountEvent.id });">删除</a></td>
				</tr>
				</c:forEach>
			</table>
			<div style="text-align:center; margin-top:3px;">
				<a title="添加金额折扣" href="javascript:;" onclick="moneyDiscountEvent.addMoneyDiscount();"><img width="25" height="20" alt="添加" src="<%=request.getContextPath()%>/css/skin/img/theme/add_icon.png"/></a>
			</div>
			
		</td>
	</tr>
</table>

<div style="text-align:center; margin-top:10px; margin-bottom: 20px;">
	<input type="button" class="submit" value="保存" onclick="moneyDiscountEvent.saveOrSubmit(${event.useStatus});" />&nbsp;&nbsp;
	<c:if test="${event.useStatus == 1}">
		<input type="button" class="submit" value="提交" onclick="moneyDiscountEvent.saveOrSubmit(2);" />&nbsp;&nbsp;
	</c:if>
	<input type="button" class="return-button" value="返回" onclick="moneyDiscountEvent.backList();" />
</div>
</form>

<!-- 用于克隆 -->
<table id="discountCloneTable" style="display: none;">
	<tr align="center">
		<td class="pn-fcontent">
			<input type="text" name="money" onchange="moneyDiscountEvent.changeMoney(this);" />
		</td>
		<td class="pn-fcontent">
			<select name="type" onchange="moneyDiscountEvent.changeType(this);">
				<option value="1">减钱</option>
				<option value="2">折扣</option>
			</select>
			<input type="text" size="10" name="numberValue" onchange="moneyDiscountEvent.changeNumberValue(this);" />
			<span name="mUnit">元</span>
			<span name="dUnit" style="display:none;">折</span>
		</td>
		<td class="pn-fcontent"><a href="javascript:;" onclick="moneyDiscountEvent.deleteMoneyDiscount(this);">删除</a></td>
	</tr>
</table>
</div>
</body>
</html>