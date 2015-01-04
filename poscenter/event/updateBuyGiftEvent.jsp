<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>修改买赠活动</title>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/skin/css/admin.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/skin/css/theme.css" />
<link rel="STYLESHEET" type="text/css" href="<%=request.getContextPath()%>/js/jquery/msgbox/jquery.msgbox.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/jquery/qtip/jquery.qtip.min.css" />

<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/qtip/jquery.qtip.min.js"></script>

<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/msgbox/jquery.dragndrop.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/msgbox/jquery.msgbox.js"></script>

<script type="text/javascript">
var buyGiftEvent={};

//返回列表
buyGiftEvent.backList = function(){
	window.location.href = $('#initPath').val()+'/pos/event!eventList.do';
}

//删除选择的店面
buyGiftEvent.deleteShop = function(dom){
	$(dom).parent().parent().remove();
}

//删除赠品
buyGiftEvent.deleteGift = function(dom, giftId){
	//删除数据库中的记录
	if(giftId != null){
		$.post($('#initPath').val()+'/pos/buyGiftEvent!deleteBuyGiftProduct.do', {'buyGiftProductId':giftId}, function(data){
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

//删除促销商品
buyGiftEvent.deleteProduct = function(dom, buyGiftEventId){
	if($('#productListTable tr[name=product]').length == 1){
		alert('至少保留一个促销商品！');
	}else{
		//删除数据库中的记录
		if(buyGiftEventId != null){
			$.post($('#initPath').val()+'/pos/buyGiftEvent!deleteBuyGiftEvent.do', {'buyGiftEventId':buyGiftEventId}, function(data){
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

//选择赠品
buyGiftEvent.selectGift = function(dom){
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
				$(dom).parent().find('table[name=gift] tr').each(function(i,trobj){
					var pid = $(trobj).attr('giftProductId');
					if(productId == pid){
						isExist = true;
						return ;
					}
				});

				//不存在
				if(!isExist){
					var productName = $('#productName').val();
					var tr = '<tr giftProductId="'+productId+'">'+
						'<td class="pn-fcontent" width="200">'+productName+'</td>'+
						'<td class="pn-fcontent" width="50" align="center"><input type="text" size="5" name="maxGiftCount" value="1" onchange="buyGiftEvent.countChange(this);" /></td>'+
						'<td class="pn-fcontent" width="50" align="center"><a href="javascript:;" onclick="buyGiftEvent.deleteGift(this);">删除</a></td>'+
						'</tr>';
					$(dom).parent().find('table').append(tr);
					$('#productId').val('');
					$('#productName').val('');
				}
			}
		}
	}).show();
}

//选择促销商品
buyGiftEvent.selectProduct = function(dom){
	new $.msgbox({
		title: '选择商品',
		width: 700,
		height: 480,
		anim: 1,
		type: 'ajax',
		content: $('#initPath').val()+'/pos/Product!toSelectProductListView.do',
		onClose: function(){
			var productId = $('#productId').val();
			var productName = $('#productName').val();
			$(dom).val(productName);
			$(dom).parent().find('input[name=productId]').val(productId);
			$('#productId').val('');
			$('#productName').val('');
		}
	}).show();
}

//添加促销商品
buyGiftEvent.addBuyGiftProduct = function(){
	$("#productListTable").append($("#producttListCloneTable tr").clone());
}

//输入框的数量改变
buyGiftEvent.countChange = function(dom){
	var value = $(dom).val();
	if(value=='' || isNaN(value) || Number(value)<0){
		alert('数据无效！');
		$(dom).val(1);
	}
}

//表单验证
buyGiftEvent.validateForm = function(){
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
	//促销商品
	$('#productListTable tr[name=product]').each(function(i,dom){
		var productId = $(dom).find('input[name=productId]').val();
		if(productId == ''){
			alert('促销商品不能为空！');
			isValid = false;
			return false;
		}

		//赠品
		var productName = $(dom).find('input[name=productName]').val();
		if($(dom).find('table[name=gift] tr').length == 0){
			alert('促销商品['+productName+']的赠品为空！');
			isValid = false;
			return false;
		}
	});
	return isValid;
}

//提交
buyGiftEvent.saveOrSubmit = function(useStatus){
	//表单验证
	if(!buyGiftEvent.validateForm()){
		return ;
	}

	var data={};
	data['event.id'] = $('#eventId').val();
	data['event.type'] = 1;
	data['event.useStatus'] = useStatus;
	data['event.startTime'] = $('#startTime').val()+':00';
	data['event.endTime'] = $('#endTime').val()+':00';
	data['event.ruleDesc'] = $('#ruleDesc').val();
	//适用店面
	$('#eventShopListTable tr').each(function(i,dom){
		var shopId = $(dom).attr('shopid');
		data['event.shopList['+i+'].id'] = shopId;
	});

	//促销商品
	$('#productListTable tr[name=product]').each(function(i,dom){
		var productId = $(dom).find('input[name=productId]').val();
		var productCount = $(dom).find('input[name=productCount]').val();
		var userLimit = $(dom).find('input[name=userLimit]').val();
		data['buyGiftEventList['+i+'].productId'] = productId;
		data['buyGiftEventList['+i+'].productCount'] = productCount;
		data['buyGiftEventList['+i+'].userLimit'] = userLimit;
		var id = $(dom).find('input[name=id]').val();
		if(id != null){
			data['buyGiftEventList['+i+'].id'] = id;
		}

		//赠品
		$(dom).find('table[name=gift] tr').each(function(j,dom){
			var giftProductId = $(dom).attr('giftProductId');
			var maxGiftCount = $(dom).find('input[name=maxGiftCount]').val();
			data['buyGiftEventList['+i+'].giftList['+j+'].giftProductId'] = giftProductId;
			data['buyGiftEventList['+i+'].giftList['+j+'].maxGiftCount'] = maxGiftCount;
			var giftid = $(dom).attr('giftid');
			if(giftid != null){
				data['buyGiftEventList['+i+'].giftList['+j+'].id'] = giftid;
			}
		});
	});

	$.post($('#initPath').val()+'/pos/buyGiftEvent!saveEvent.do', data, function(data){
		if(data == 'success'){
			alert('保存成功！');
			buyGiftEvent.backList();
		}else{
			alert('保存失败：'+data);
		}
	});
}

$(document).ready(function(){
	//提示信息
	$('#userLimitTitle').qtip({
		content: {
			text: '表示仅限前多少份促销商品享受此优惠活动<br>0:表示不限制份数'
			//,title: {button: true}
		},
		show: {ready: false},
		position: {my: 'bottom left', at: 'top center'},
		hide: 'unfocus'
	});

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
							'<td class="pn-fcontent" width="50" align="center"><a href="javascript:;" onclick="buyGiftEvent.deleteShop(this);">删除</a></td></tr>';
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
	<div class="rpos">当前位置: 促销活动管理 - 修改买赠活动</div>
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
		<td width="88%" class="pn-fcontent">买赠</td>
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
						<a href="javascript:;" onclick="buyGiftEvent.deleteShop(this);">删除</a>
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
			<table id="productListTable" width="100%" class="pn-ftable" cellspacing="1">
				<tr align="center" style="">
					<td class="pn-fcontent" width="200">促销商品</td>
					<td class="pn-fcontent">购买数量</td>
					<td class="pn-fcontent" id="userLimitTitle">促销商品份数限制</td>
					<td class="pn-fcontent" width="30%">赠品</td>
					<td class="pn-fcontent">操作</td>
				</tr>
				<%--促销商品列表--%>
				<c:forEach var="buyGiftEvent" items="${event.buyGiftEventList}">
				<tr align="center" name="product">
					<td class="pn-fcontent">
						<input type="hidden" name="id" value="${buyGiftEvent.id}" />
						<input type="hidden" name="productId" value="${buyGiftEvent.productId}" />
						<input type="text" name="productName" onclick="buyGiftEvent.selectProduct(this);" value="${buyGiftEvent.product.name }" />
					</td>
					<td class="pn-fcontent"><input type="text" size="5" name="productCount" onchange="buyGiftEvent.countChange(this);" value="${buyGiftEvent.productCount}" /></td>
					<td class="pn-fcontent"><input type="text" size="5" name="userLimit" onchange="buyGiftEvent.countChange(this);" value="${buyGiftEvent.userLimit}" /></td>
					<td class="pn-fcontent">
						<a href="javascript:;" style="color:blue;" onclick="buyGiftEvent.selectGift(this);">添加赠品</a>
						<table name="gift" width="100%">
							<%--赠品列表--%>
							<c:forEach var="gift" items="${buyGiftEvent.giftList}">
							<tr giftid="${gift.id }" giftProductId="${gift.giftProductId }">
								<td class="pn-fcontent" width="200">${gift.giftProduct.name }</td>
								<td class="pn-fcontent" width="50" align="center"><input type="text" size="5" name="maxGiftCount" value="${gift.maxGiftCount }" onchange="buyGiftEvent.countChange(this);" /></td>
								<td class="pn-fcontent" width="50" align="center"><a href="javascript:;" onclick="buyGiftEvent.deleteGift(this, ${gift.id });">删除</a></td>
							</tr>
							</c:forEach>
						</table>
					</td>
					<td class="pn-fcontent"><a href="javascript:;" onclick="buyGiftEvent.deleteProduct(this,${buyGiftEvent.id});">删除</a></td>
				</tr>
				</c:forEach>
			</table>
			<div style="text-align:center; margin-top:3px;">
				<a title="添加促销商品" href="javascript:;" onclick="buyGiftEvent.addBuyGiftProduct();"><img width="25" height="20" alt="添加" src="<%=request.getContextPath()%>/css/skin/img/theme/add_icon.png"/></a>
			</div>
		</td>
	</tr>
</table>

<div style="text-align:center; margin-top:10px; margin-bottom: 20px;">
	<input type="button" class="submit" value="保存" onclick="buyGiftEvent.saveOrSubmit(${event.useStatus});" />&nbsp;&nbsp;
	<c:if test="${event.useStatus == 1}">
		<input type="button" class="submit" value="提交" onclick="buyGiftEvent.saveOrSubmit(2);" />&nbsp;&nbsp;
	</c:if>
	<input type="button" class="return-button" value="返回" onclick="buyGiftEvent.backList();" />
</div>
</form>

<!-- 用于克隆 -->
<table id="producttListCloneTable" style="display: none;">
	<tr align="center" name="product">
		<td class="pn-fcontent">
			<input type="hidden" name="productId" />
			<input type="text" name="productName" onclick="buyGiftEvent.selectProduct(this);" />
		</td>
		<td class="pn-fcontent"><input type="text" size="5" name="productCount" value="1" onchange="buyGiftEvent.countChange(this);" /></td>
		<td class="pn-fcontent"><input type="text" size="5" name="userLimit" value="0" onchange="buyGiftEvent.countChange(this);" /></td>
		<td class="pn-fcontent">
			<a href="javascript:;" style="color:blue;" onclick="buyGiftEvent.selectGift(this);">添加赠品</a>
			<table name="gift" width="100%"></table>
		</td>
		<td class="pn-fcontent"><a href="javascript:;" onclick="buyGiftEvent.deleteProduct(this);">删除</a></td>
	</tr>
</table>
</div>
</body>
</html>