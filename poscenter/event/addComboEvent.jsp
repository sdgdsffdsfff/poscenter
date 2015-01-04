<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>新增套餐活动</title>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/skin/css/admin.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/skin/css/theme.css" />
<link rel="STYLESHEET" type="text/css" href="<%=request.getContextPath()%>/js/jquery/msgbox/jquery.msgbox.css" />

<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/My97DatePicker/WdatePicker.js"></script>

<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/msgbox/jquery.dragndrop.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/msgbox/jquery.msgbox.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/poscenter/event/commonEvent.js"></script>

<script type="text/javascript">
var comboEvent={};

//删除选择的店面
comboEvent.deleteShop = function(dom){
	$(dom).parent().parent().remove();
}

//删除商品
comboEvent.deleteProduct = function(dom){
	$(dom).parent().parent().remove();
}

//删除套餐
comboEvent.deleteCombo = function(dom){
	if($('#productListTable tr[name=product]').length == 1){
		alert('至少保留一个套餐商品！');
	}else{
		$(dom).parent().parent().remove();
	}
}

//选择商品
comboEvent.selectProduct = function(dom){
	new $.msgbox({
		title: '选择商品',
		width: 700,
		height: 480,
		anim: 1,
		type: 'ajax',
		content: $('#initPath').val()+'/pos/Product!toSelectProductListView.do',
		onClose: function(){
			if($('#productId').val() != ''){
				//检查是否已经存在
				var isExist = false;
				var productId = $('#productId').val();
				$(dom).parent().find('table[name=combo] tr').each(function(i,trobj){
					var pid = $(trobj).attr('productId');
					if(productId == pid){
						isExist = true;
						return ;
					}
				});

				//不存在
				if(!isExist){
					var productName = $('#productName').val();
					var tr = '<tr productId="'+productId+'">'+
						'<td class="pn-fcontent" width="200">'+productName+'</td>'+
						'<td class="pn-fcontent" width="50" align="center"><input type="text" size="5" name="productCount" value="1" onchange="comboEvent.countChange(this);" /></td>'+
						'<td class="pn-fcontent" width="50" align="center"><a href="javascript:;" onclick="comboEvent.deleteProduct(this);">删除</a></td>'+
						'</tr>';
					$(dom).parent().find('table').append(tr);
					$('#productId').val('');
					$('#productName').val('');
				}
			}
		}
	}).show();
}

//添加套餐
comboEvent.addCombo = function(){
	$("#productListTable").append($("#producttListCloneTable tr").clone());
}

//输入框的数量改变
comboEvent.countChange = function(dom){
	var value = $(dom).val();
	if(value=='' || isNaN(value) || Number(value)<0){
		alert('数据无效！');
		$(dom).val(1);
	}
}

//表单验证
comboEvent.validateForm = function(){
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
	//套餐商品
	$('#productListTable tr[name=product]').each(function(i,dom){
		var comboSize = $(dom).find('table[name=combo] tr').length;
		if(comboSize == 0){
			alert('套餐商品为空！');
			isValid = false;
			return false;
		}else if(comboSize == 1){
			alert('套餐商品至少包含两种商品！');
			isValid = false;
			return false;
		}

		//套餐价格
		var comboPrice = $(dom).find('input[name=comboPrice]').val();
		if(comboPrice==''){
			alert('套餐价格为空！');
			isValid = false;
			return false;
		}
	});
	return isValid;
}

//提交
comboEvent.saveOrSubmit = function(useStatus){
	//表单验证
	if(!comboEvent.validateForm()){
		return ;
	}

	var data={};
	data['event.useStatus'] = useStatus;
	data['event.type'] = 5;
	data['event.startTime'] = $('#startTime').val()+':00';
	data['event.endTime'] = $('#endTime').val()+':00';
	data['event.ruleDesc'] = $('#ruleDesc').val();
	//适用店面
	$('#eventShopListTable tr').each(function(i,dom){
		var shopId = $(dom).attr('shopid');
		data['event.shopList['+i+'].id'] = shopId;
	});

	//套餐
	$('#productListTable tr[name=product]').each(function(i,dom){
		var comboPrice = $(dom).find('input[name=comboPrice]').val();
		data['event.comboEventList['+i+'].comboPrice'] = comboPrice;

		//套餐商品
		$(dom).find('table[name=combo] tr').each(function(j,trdom){
			var productId = $(trdom).attr('productId');
			var productCount = $(trdom).find('input[name=productCount]').val();
			data['event.comboEventList['+i+'].comboProductList['+j+'].productId'] = productId;
			data['event.comboEventList['+i+'].comboProductList['+j+'].productCount'] = productCount;
		});
	});

	//保存
	$.post($('#initPath').val()+'/pos/comboEvent!saveEvent.do', data, function(result){
		if(result == 'success'){
			alert('保存成功！');
			commonEvent.backEventList();
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
							'<td class="pn-fcontent" width="50" align="center"><a href="javascript:;" onclick="comboEvent.deleteShop(this);">删除</a></td></tr>';
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
	<div class="rpos">当前位置: 促销活动管理 - 新增套餐活动</div>
	<div class="ropt"></div>
	<div class="clear"></div>
</div>

<div id="bodyBox" class="body-box">
<form>
<input type="hidden" id="shopIds" />
<input type="hidden" id="shopNames" />
<input type="hidden" id="productId" />
<input type="hidden" id="productName" />
<table cellspacing="1" cellpadding="2" border="0" width="100%" class="pn-ftable" style="font-size:13px;">
	<tr>
		<td width="12%" class="pn-flabel pn-flabel-h">活动类型:</td>
		<td width="88%" class="pn-fcontent">
			<input type="radio" id="eventType1" onclick="commonEvent.toAddEventView(1);"><label for="eventType1">买赠</label>
			<input type="radio" id="eventType2" onclick="commonEvent.toAddEventView(2);"><label for="eventType2">换购</label>
			<input type="radio" id="eventType3" onclick="commonEvent.toAddEventView(3);"><label for="eventType3">金额折扣</label>
			<input type="radio" id="eventType4" onclick="commonEvent.toAddEventView(4);"><label for="eventType4">数量折扣</label>
			<input type="radio" id="eventType5" onclick="commonEvent.toAddEventView(5);" checked="checked"><label for="eventType5">套餐</label>
		</td>
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
			<table id="eventShopListTable" class="pn-ftable" cellspacing="1"></table>
		</td>
	</tr>
	<tr>
		<td class="pn-flabel pn-flabel-h">规则描述:</td>
		<td class="pn-fcontent"><textarea rows="2" cols="30" id="ruleDesc"></textarea></td>
	</tr>
	<tr>
		<td class="pn-flabel pn-flabel-h"><span class="pn-frequired">*</span>具体规则:</td>
		<td class="pn-fcontent">
			<table id="productListTable" width="100%" class="pn-ftable" cellspacing="1">
				<tr align="center">
					<td class="pn-fcontent" width="50%">套餐商品</td>
					<td class="pn-fcontent">套餐价</td>
					<td class="pn-fcontent">操作</td>
				</tr>
				<tr align="center" name="product">
					<td class="pn-fcontent">
						<a href="javascript:;" style="color:blue;" onclick="comboEvent.selectProduct(this);">添加商品</a>
						<table name="combo" width="100%"></table>
					</td>
					<td class="pn-fcontent"><input type="text" size="10" name="comboPrice" onchange="comboEvent.countChange(this);" /></td>
					<td class="pn-fcontent"><a href="javascript:;" onclick="comboEvent.deleteCombo(this);">删除</a></td>
				</tr>
			</table>
			<div style="text-align:center; margin-top:3px;">
				<a title="添加套餐" href="javascript:;" onclick="comboEvent.addCombo();"><img width="25" height="20" alt="添加" src="<%=request.getContextPath()%>/css/skin/img/theme/add_icon.png"/></a>
			</div>
		</td>
	</tr>
</table>

<div style="text-align:center; margin-top:10px; margin-bottom: 20px;">
	<input type="button" class="submit" value="保存" onclick="comboEvent.saveOrSubmit(1);" />&nbsp;&nbsp;
	<input type="button" class="submit" value="提交" onclick="comboEvent.saveOrSubmit(2);" />&nbsp;&nbsp;
	<input type="button" class="return-button" value="返回" onclick="commonEvent.backEventList();" />
</div>
</form>

<!-- 用于克隆 -->
<table id="producttListCloneTable" style="display: none;">
	<tr align="center" name="product">
		<td class="pn-fcontent">
			<a href="javascript:;" style="color:blue;" onclick="comboEvent.selectProduct(this);">添加商品</a>
			<table name="combo" width="100%"></table>
		</td>
		<td class="pn-fcontent"><input type="text" size="10" name="comboPrice" onchange="comboEvent.countChange(this);" /></td>
		<td class="pn-fcontent"><a href="javascript:;" onclick="comboEvent.deleteCombo(this);">删除</a></td>
	</tr>
</table>
</div>
</body>
</html>