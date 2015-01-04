<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>新增数量折扣活动</title>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/skin/css/admin.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/skin/css/theme.css" />
<link rel="STYLESHEET" type="text/css" href="<%=request.getContextPath()%>/js/jquery/msgbox/jquery.msgbox.css" />

<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/My97DatePicker/WdatePicker.js"></script>

<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/msgbox/jquery.dragndrop.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/msgbox/jquery.msgbox.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/poscenter/event/commonEvent.js"></script>

<script type="text/javascript">
var countDiscountEvent={};

//删除选择的店面
countDiscountEvent.deleteShop = function(dom){
	$(dom).parent().parent().remove();
}

//删除商品
countDiscountEvent.deleteProduct = function(dom){
	$(dom).parent().parent().remove();
}

//删除阶梯折扣
countDiscountEvent.deleteLadderDiscount = function(dom){
	$(dom).parent().parent().prev().find('a').show();
	$(dom).parent().parent().remove();
}

//选择商品
countDiscountEvent.selectProduct = function(dom){
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
				$('#fixedProductListTable tr').each(function(i,trobj){
					var pid = $(trobj).find('input[name=productId]').val();
					if(productId == pid){
						isExist = true;
						alert('固定折扣商品列表中已存在该商品！');
						return ;
					}
				});
				$('#ladderProductListTable tr[name=product]').each(function(i,trobj){
					var pid = $(trobj).find('input[name=productId]').val();
					if(productId == pid){
						isExist = true;
						alert('阶梯折扣商品列表中已存在该商品！');
						return ;
					}
				});

				//不存在
				if(!isExist){
					var productId = $('#productId').val();
					var productName = $('#productName').val();
					$(dom).val(productName);
					$(dom).parent().find('input[name=productId]').val(productId);
					$('#productId').val('');
					$('#productName').val('');
				}
			}
		}
	}).show();
}

//添加固定折扣商品
countDiscountEvent.addFixedDiscountProduct = function(){
	$("#fixedProductListTable").append($("#fixedDiscountCloneTable tr").clone());
}

//添加阶梯折扣商品
countDiscountEvent.addLadderDiscountProduct = function(){
	$("#ladderProductListTable").append($("#ladderDiscountCloneTable tr[name=product]").clone());
}

//添加阶梯折扣
countDiscountEvent.addLadderDiscount = function(dom){
	var disTable = $(dom).parent().find('table');
	var disSize = $(disTable).find('tr').length;

	var tr = '<tr>'+
		'<td align="center">第'+(disSize+1)+'件</td>'+
		'<td align="center"><input type="text" size="5" name="discount" onchange="countDiscountEvent.countChange(this);" /></td>'+
		'<td><a href="javascript:;" onclick="countDiscountEvent.deleteLadderDiscount(this);">删除</a></td>'+
		'</tr>';
	$(disTable).find('a').hide();
	$(disTable).append(tr);
}

//输入框的数量改变
countDiscountEvent.countChange = function(dom, defaultValue){
	var value = $(dom).val();
	if(value=='' || isNaN(value) || Number(value)<=0 || Number(value)>100){
		alert('数据无效！');
		if(defaultValue==null){
			$(dom).val('');
		}else{
			$(dom).val(defaultValue);
		} 
	}
}

//表单验证
countDiscountEvent.validateForm = function(){
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
	var fixed = $("#fixedProductListTable tr:gt(0)").length;
	var ladder = $("#ladderProductListTable tr[name=product]").length;
	if(fixed+ladder == 0){
		alert('折扣商品不能为空！');
		return false;
	}
	
	//固定折扣商品
	$('#fixedProductListTable tr:gt(0)').each(function(i,dom){
		var productName = $(dom).find('input[name=productName]').val();
		var discount = $(dom).find('input[name=discount]').val();
		if(productName == ''){
			alert('请选择固定折扣商品！');
			isValid = false;
			return false;
		}
		if(discount == ''){
			alert('折扣为空！');
			isValid = false;
			return false;
		}
	});
	if(!isValid) return false;
	//阶梯折扣商品
	$('#ladderProductListTable tr[name=product]').each(function(i,dom){
		var productName = $(dom).find('input[name=productName]').val();
		if(productName == ''){
			alert('请选择阶梯折扣商品！');
			isValid = false;
			return false;
		}
		$(dom).find('table tr').each(function(j,domE){
			if(!isValid) return false;
			var discount = $(domE).find('input[name=discount]').val();
			if(discount == ''){
				alert('折扣为空！');
				isValid = false;
				return false;
			}
		});
	});
	if(!isValid) return false;
	//阶梯折扣商品的折扣值要递减
	$('#ladderProductListTable tr[name=product]').each(function(i,dom){
		var productName = $(dom).find('input[name=productName]').val();
		var preDiscount = 0;
		$(dom).find('table tr').each(function(j,domE){
			if(!isValid) return false;
			var discount = $(domE).find('input[name=discount]').val();
			if(j>0 && Number(discount)>Number(preDiscount)){
				alert('（'+productName+'）的第'+(j+1)+'件商品折扣不能大于第'+j+'件商品折扣！');
				isValid = false;
				return false;
			}
			preDiscount = discount;
		});
	});
	return isValid;
}

//提交
countDiscountEvent.saveOrSubmit = function(useStatus){
	//表单验证
	if(!countDiscountEvent.validateForm()){
		return ;
	}

	var data={};
	data['event.useStatus'] = useStatus;
	data['event.type'] = 4;
	data['event.startTime'] = $('#startTime').val()+':00';
	data['event.endTime'] = $('#endTime').val()+':00';
	data['event.ruleDesc'] = $('#ruleDesc').val();
	//适用店面
	$('#eventShopListTable tr').each(function(i,dom){
		var shopId = $(dom).attr('shopid');
		data['event.shopList['+i+'].id'] = shopId;
	});

	//固定折扣商品
	var count = 0;
	$('#fixedProductListTable tr:gt(0)').each(function(i,dom){
		var productId = $(dom).find('input[name=productId]').val();
		var discount = $(dom).find('input[name=discount]').val();
		data['event.countDiscountEventList['+count+'].productId'] = productId;
		data['event.countDiscountEventList['+count+'].discount'] = discount/100;
		data['event.countDiscountEventList['+count+'].type'] = 2;
		count++;
	});

	//阶梯折扣商品
	$('#ladderProductListTable tr[name=product]').each(function(i,dom){
		var productId = $(dom).find('input[name=productId]').val();
		$(dom).find('table tr').each(function(j,domE){
			var discount = $(domE).find('input[name=discount]').val();
			data['event.countDiscountEventList['+count+'].productId'] = productId;
			data['event.countDiscountEventList['+count+'].discount'] = discount/100;
			data['event.countDiscountEventList['+count+'].productCount'] = (j+1);
			data['event.countDiscountEventList['+count+'].type'] = 1;
			count++;
		});
	});

	//保存
	$.post($('#initPath').val()+'/pos/countDiscountEvent!saveEvent.do', data, function(result){
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
							'<td class="pn-fcontent" width="50" align="center"><a href="javascript:;" onclick="countDiscountEvent.deleteShop(this);">删除</a></td></tr>';
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
	<div class="rpos">当前位置: 促销活动管理 - 新增数量折扣活动</div>
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
			<input type="radio" id="eventType4" onclick="commonEvent.toAddEventView(4);" checked="checked"><label for="eventType4">数量折扣</label>
			<input type="radio" id="eventType5" onclick="commonEvent.toAddEventView(5);"><label for="eventType5">套餐</label>
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
			<%-- 固定折扣商品 --%>
			<div>
				<span style="font-weight:bold;">固定折扣商品</span>（只要商品数量>=2时，全部享受一个折扣优惠）
			</div>
			<table id="fixedProductListTable" width="100%" class="pn-ftable" cellspacing="1">
				<tr align="center">
					<td class="pn-fcontent" width="40%">商品</td>
					<td class="pn-fcontent" width="40%">折扣<span style="color:#FF0000; font-weight:bold;">（例如：95折）</span></td>
					<td class="pn-fcontent">操作</td>
				</tr>
				<%--
				<tr align="center">
					<td class="pn-fcontent">
						<input type="hidden" name="productId" />
						<input type="text" name="productName" onclick="countDiscountEvent.selectProduct(this);" />
					</td>
					<td class="pn-fcontent"><input type="text" size="10" name="discount" onchange="countDiscountEvent.countChange(this);" /></td>
					<td class="pn-fcontent"><a href="javascript:;" onclick="countDiscountEvent.deleteProduct(this);">删除</a></td>
				</tr>
				--%>
			</table>
			<div style="text-align:center; margin-top:3px;">
				<a title="添加固定折扣商品" href="javascript:;" onclick="countDiscountEvent.addFixedDiscountProduct();"><img width="25" height="20" alt="添加" src="<%=request.getContextPath()%>/css/skin/img/theme/add_icon.png"/></a>
			</div>
			
			<%-- 阶梯折扣商品 --%>
			<div>
				<span style="font-weight:bold;">阶梯折扣商品</span>（第一件默认是原价，从第二件起产生阶梯折扣）
			</div>
			<table id="ladderProductListTable" width="100%" class="pn-ftable" cellspacing="1">
				<tr align="center">
					<td class="pn-fcontent" width="40%">商品</td>
					<td class="pn-fcontent" width="40%">折扣<span style="color:#FF0000; font-weight:bold;">（例如：95折）</span></td>
					<td class="pn-fcontent">操作</td>
				</tr>
				<%--
				<tr align="center" name="product">
					<td class="pn-fcontent">
						<input type="hidden" name="productId" />
						<input type="text" name="productName" onclick="countDiscountEvent.selectProduct(this);" />
					</td>
					<td class="pn-fcontent">
						<a href="javascript:;" style="color:blue;" onclick="countDiscountEvent.addLadderDiscount(this);">添加折扣</a>
						<table width="100%">
							<tr>
								<td align="center">第1件</td>
								<td align="center"><input type="hidden" size="5" name="discount" value="100" onchange="countDiscountEvent.countChange(this,100);" />100</td>
								<td></td>
							</tr>
						</table>
					</td>
					<td class="pn-fcontent"><a href="javascript:;" onclick="countDiscountEvent.deleteProduct(this);">删除</a></td>
				</tr>
				--%>
			</table>
			<div style="text-align:center; margin-top:3px;">
				<a title="添加阶梯折扣商品" href="javascript:;" onclick="countDiscountEvent.addLadderDiscountProduct();"><img width="25" height="20" alt="添加" src="<%=request.getContextPath()%>/css/skin/img/theme/add_icon.png"/></a>
			</div>
		</td>
	</tr>
</table>

<div style="text-align:center; margin-top:10px; margin-bottom: 20px;">
	<input type="button" class="submit" value="保存" onclick="countDiscountEvent.saveOrSubmit(1);" />&nbsp;&nbsp;
	<input type="button" class="submit" value="提交" onclick="countDiscountEvent.saveOrSubmit(2);" />&nbsp;&nbsp;
	<input type="button" class="return-button" value="返回" onclick="commonEvent.backEventList();" />
</div>
</form>

<!-- 用于克隆 -->
<table id="fixedDiscountCloneTable" style="display: none;">
	<tr align="center">
		<td class="pn-fcontent">
			<input type="hidden" name="productId" />
			<input type="text" name="productName" onclick="countDiscountEvent.selectProduct(this);" />
		</td>
		<td class="pn-fcontent"><input type="text" size="10" name="discount" onchange="countDiscountEvent.countChange(this);" /></td>
		<td class="pn-fcontent"><a href="javascript:;" onclick="countDiscountEvent.deleteProduct(this);">删除</a></td>
	</tr>
</table>
<table id="ladderDiscountCloneTable" style="display: none;">
	<tr align="center" name="product">
		<td class="pn-fcontent">
			<input type="hidden" name="productId" />
			<input type="text" name="productName" onclick="countDiscountEvent.selectProduct(this);" />
		</td>
		<td class="pn-fcontent">
			<a href="javascript:;" style="color:blue;" onclick="countDiscountEvent.addLadderDiscount(this);">添加折扣</a>
			<table width="100%">
				<tr>
					<td align="center">第1件</td>
					<td align="center"><input type="hidden" size="5" name="discount" value="100" onchange="countDiscountEvent.countChange(this,100);" />100</td>
					<td></td>
				</tr>
			</table>
		</td>
		<td class="pn-fcontent"><a href="javascript:;" onclick="countDiscountEvent.deleteProduct(this);">删除</a></td>
	</tr>
</table>
</div>
</body>
</html>