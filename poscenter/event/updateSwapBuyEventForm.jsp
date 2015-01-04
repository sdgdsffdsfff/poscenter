<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>SendOrder form</title>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/skin/css/admin.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/skin/css/theme.css" />
<link rel="STYLESHEET" type="text/css" href="<%=request.getContextPath()%>/js/jquery/msgbox/jquery.msgbox.css" />

<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.validate.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/My97DatePicker/WdatePicker.js"></script>

<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/msgbox/jquery.dragndrop.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/msgbox/jquery.msgbox.js"></script>

<script type="text/javascript">
var swapBuyEvent={};

//表单验证
swapBuyEvent.validateForm = function(){
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
	//促销活动
	$('#productListTable tr[name=swap]').each(function(i,dom){
		var money = $(dom).find('input[name=money]').val();
		if(money == ''){
			alert('换购所需价格不能为空！');
			isValid = false;
			return false;
		}
		var appendMoney = $(dom).find('input[name=appendMoney]').val();
        if(appendMoney == '' || appendMoney == 0){
            alert('换购添加价格不能为空！');
			isValid = false;
			return false;
        }
        
		//换购商品
		if($(dom).find('table[name=gift] tr').length == 0){
			alert('换购商品不能为空！');
			isValid = false;
			return false;
		}
	});

	return isValid;

}

//返回列表
swapBuyEvent.backList = function(){
	window.location.href = $('#initPath').val()+'/pos/event!eventList.do';
}

//删除选择的店面
swapBuyEvent.deleteShop = function(dom){
	$(dom).parent().parent().remove();
}

//输入框的数量改变
swapBuyEvent.countChange = function(dom, defaultValue){
	var value = $(dom).val();
	if(value=='' || isNaN(value) || Number(value)<0){
		alert('数据无效！');
		if(defaultValue==null){
			$(dom).val('');
		}else{
			$(dom).val(defaultValue);
		} 
	}
}

//改变金额
swapBuyEvent.changeMoney = function(dom){
	var value = $(dom).val();
	if(value=='' || isNaN(value) || Number(value)<0){
		alert('金额无效！');
		$(dom).val('');
		return;
	}
	var index = $(dom).parent().parent().index();
	$('#productListTable tr[name=swap]').each(function(i,domE){
		if(i+1 != index){
			var money = $(domE).find('input[name=money]').val();
			if(Number(money) == Number(value)){
				alert('消费总金额等于 '+money+' 的记录已存在！');
				$(dom).val('');
			}
		}
	});
}

//删除换购信息
swapBuyEvent.deleteSwapBuyEvent = function(dom, swapBuyEventId){
	if($('#productListTable tr[name=swap]').length == 1){
		alert('至少保留一条换购信息！');
	}else{
		//删除数据库中的记录
		if(swapBuyEventId != null){
			$.post($('#initPath').val()+'/pos/swapBuyEvent!deleteSwapBuyEvent.do', {'swapBuyEventId':swapBuyEventId}, function(data){
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

//添加换购活动
swapBuyEvent.addSwapBuyEvent = function(){
	$("#productListTable").append($("#producttListCloneTable tr").clone());
}

//提交
swapBuyEvent.saveOrSubmit = function(useStatus){
   
   //表单验证
	if(!swapBuyEvent.validateForm()){
		return ;
	}
  
	var data={};
	data['event.id'] = $('#eventId').val();
	data['event.useStatus'] = useStatus;
	data['event.startTime'] = $('#startTime').val()+':00';
	data['event.endTime'] = $('#endTime').val()+':00';
	data['event.ruleDesc'] = $('#ruleDesc').val();
	//适用店面
	$('#eventShopListTable tr').each(function(i,dom){
		var shopId = $(dom).attr('shopid');
		data['event.shopList['+i+'].id'] = shopId;
	});
	//换购信息
	$('#productListTable tr[name=swap]').each(function(i,dom){
		var money = $(dom).find('input[name=money]').val();
		var appendMoney = $(dom).find('input[name=appendMoney]').val();
		var productId = $(dom).find('input[name=productId]').val();
		data['swapBuyEventList['+i+'].money'] = money;
		data['swapBuyEventList['+i+'].appendMoney'] = appendMoney;
		
		var id = $(dom).find('input[name=id]').val();
		if(id != null){
		   data['swapBuyEventList['+i+'].id'] = id;
		}
		
		//换购商品
	   $(dom).find('table tr').each(function(j,dom){
			var giftProductId = $(dom).attr('giftProductId');
			data['swapBuyEventList['+i+'].swapBuyProductList['+j+'].giftProductId'] = giftProductId;
		});

	});
	
	$.post($('#initPath').val()+'/pos/swapBuyEvent!saveEvent.do', data, function(data){
		if(data == 'success'){
			alert('保存成功！');
			swapBuyEvent.backList();
		}else{
			alert('保存失败：'+data);
		}
	});
}

//选择换购商品
swapBuyEvent.selectGift = function(dom){
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
						'<td class="pn-fcontent" width="50" align="center"><a href="javascript:;" onclick="swapBuyEvent.deleteGift(this);">删除</a></td>'+
						'</tr>';
					$(dom).parent().find('table').append(tr);
					$('#productId').val('');
					$('#productName').val('');
				}
			}
		}
	}).show();
}


//删除换购商品
swapBuyEvent.deleteGift = function(dom, giftId){
	//删除数据库中的记录
	if(giftId != null){
		$.post($('#initPath').val()+'/pos/swapBuyEvent!deleteSwapBuyProduct.do', {'swapBuyProductId':giftId}, function(data){
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

$(document).ready(function(){
	//验证表单
	$("#swapBuyEventForm").validate();

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
							'<td class="pn-fcontent" width="50" align="center"><a href="javascript:;" onclick="swapBuyEvent.deleteShop(this);">删除</a></td></tr>';
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
	<div class="rpos">当前位置: 促销活动管理 - 修改换购活动</div>
	<div class="ropt"></div>
	<div class="clear"></div>
</div>

<div id="bodyBox" class="body-box">
	<form id="swapBuyEventForm" action="<%=request.getContextPath()%>/pos/swapBuyEvent!saveEvent.do" method="post" onsubmit="return swapBuyEvent.validateForm();">
	    <input type="hidden" id="eventId" value="${event.id }" />
	    <input type="hidden" id="shopIds" />
        <input type="hidden" id="shopNames" />
	    <input type="hidden" id="productId" />
        <input type="hidden" id="productName" />
	<table cellspacing="1" cellpadding="2" border="0" width="100%" class="pn-ftable" style="font-size:13px;">
	    <tr>
			<td width="12%" class="pn-flabel pn-flabel-h"><span class="pn-frequired">*</span>活动类型：</td>
			<td width="88%" class="pn-fcontent">换购</td>
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
						<a href="javascript:;" onclick="swapBuyEvent.deleteShop(this);">删除</a>
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
					<td class="pn-fcontent" width="200">需消费金额：</td>
					<td class="pn-fcontent">添加金额:</td>
					<td class="pn-fcontent">换购商品:</td>
					<td class="pn-fcontent">操作</td>
				</tr>
				<c:forEach var="swapBuyEvent" items="${event.swapBuyEventList}">
				<tr align="center" name="swap" >
					<td class="pn-fcontent">
					   <input type="hidden" name="id"  value="${swapBuyEvent.id}" >
					   <input type="text" class="required" name="money" value="${swapBuyEvent.money}" onchange="swapBuyEvent.changeMoney(this);" />
					</td>
					<td class="pn-fcontent">
						<input type="text" id="appendMoney" name="appendMoney" class="required" value="${swapBuyEvent.appendMoney}" onchange="swapBuyEvent.countChange(this);" />
					</td>
					<td class="pn-fcontent">
			             <a href="javascript:;" style="color:blue;" onclick="swapBuyEvent.selectGift(this);">添加换购商品</a>
			             <table name="gift" width="100%">
			             <c:forEach var="swapBuyProduct" items="${swapBuyEvent.swapBuyProductList}" >
			              <tr giftId="${swapBuyProduct.id}" giftProductId="${swapBuyProduct.giftProductId}">
			                  <td class="pn-fcontent" width="200">${swapBuyProduct.giftProductName}</td>
					          <td class="pn-fcontent" width="50" align="center"><a href="javascript:;" onclick="swapBuyEvent.deleteGift(this,${swapBuyProduct.id});">删除</a></td>
				          </tr>
			             </c:forEach>
			             </table>
		            </td>
					<td class="pn-fcontent"><a href="javascript:;" onclick="swapBuyEvent.deleteSwapBuyEvent(this,${swapBuyEvent.id});">删除</a></td>
				</tr>
			   </c:forEach>
			</table>
			<div style="text-align:center; margin-top:3px;">
				<a title="添加换购" href="javascript:;" onclick="swapBuyEvent.addSwapBuyEvent();"><img width="25" height="20" alt="添加" src="<%=request.getContextPath()%>/css/skin/img/theme/add_icon.png"/></a>
			</div>
		</td>
	</tr>
</table>

<div style="text-align:center; margin-top:10px; margin-bottom: 20px;">
	<input type="button" class="submit" value="保存" onclick="swapBuyEvent.saveOrSubmit(${event.useStatus});" />&nbsp;&nbsp;
	<c:if test="${event.useStatus == 1}">
		<input type="button" class="submit" value="提交" onclick="swapBuyEvent.saveOrSubmit(2);" />&nbsp;&nbsp;
	</c:if>
	<input type="button" class="return-button" value="返回" onclick="swapBuyEvent.backList();" />
</div>
</form>
	<!-- 用于克隆 -->
<table id="producttListCloneTable" style="display: none;">
	<tr align="center" name="swap">
		<td class="pn-fcontent">
			<input type="text" class="required" name="money" onchange="swapBuyEvent.changeMoney(this);" />
		</td>
		<td class="pn-fcontent">
			<input type="text" id="appendMoney" name="appendMoney" class="required" onchange="swapBuyEvent.countChange(this);" />
		</td>
		<td class="pn-fcontent">
			<a href="javascript:;" style="color:blue;" onclick="swapBuyEvent.selectGift(this);">添加换购商品</a>
			<table name="gift" width="100%"></table>
		</td>
		<td class="pn-fcontent">
		    <a href="javascript:;" onclick="swapBuyEvent.deleteSwapBuyEvent(this);">删除</a>
		</td>
	</tr>
</table>
	
</div>
</body>
</html>