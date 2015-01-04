<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false" %>

<form id="againCheckForm">
	<input type="hidden" name="stock" value="${param.stock}" />
	<table cellspacing="1" cellpadding="2" border="0" width="100%" class="pn-ftable" style="font-size:13px;">
	<tbody>
		<tr>
			<td width="20%" class="pn-flabel pn-flabel-h"><span class="pn-frequired">*</span>复盘数量:</td>
			<td width="80%" class="pn-fcontent" colspan="3"><input type="text" name="count" /></td>
		</tr>
		<tr>
			<td class="pn-flabel pn-flabel-h"><span class="pn-frequired">*</span>盘点负责人:</td>
			<td class="pn-fcontent" colspan="3"><input type="text" name="charger" /></td>
		</tr>
		<tr>
			<td class="pn-flabel pn-flabel-h">备注:</td>
			<td class="pn-fcontent" colspan="3"><textarea name="remark" rows="4" cols="30"></textarea></td>
		</tr>
		<tr>
			<td class="pn-fbutton" colspan="4">
				<input type="button" class="submit" value="提交" onclick="againCheck.saveAgainCheck(${param.productId });" /> &nbsp; 
				<input type="button" class="return-button" value="返回" onclick="$('a.jMsgbox-closeWrap').click();" />
			</td>
		</tr>
	</tbody>
	</table>
</form>

<script>
var againCheck={};

//保存复盘
againCheck.saveAgainCheck = function(productId){
	var count = $('#againCheckForm').find('input[name=count]').val();
	var stock = $('#againCheckForm').find('input[name=stock]').val();
	var charger = $('#againCheckForm').find('input[name=charger]').val();
	var remark = $('#againCheckForm').find('textarea[name=remark]').val();
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
			$('a.jMsgbox-closeWrap').click(); //关闭弹出框
			checkStockItem.goPage();
		}else{
			alert('操作失败！');
		}
	});
}
</script>