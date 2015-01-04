<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false" %>

<form id="goodsClassForm" action="<%=request.getContextPath()%>/pos/goodsClass!save.do" method="post">
	<input type="hidden" id="goodsClassObjId" name="goodsClass.id" value="${goodsClass.id }" />
	<input type="hidden" name="goodsClass.parentId" value="${goodsClass.parent.id }" />
	<input type="hidden" name="treeLevel" value="${goodsClass.treeLevel }" />
	<table cellspacing="1" cellpadding="2" border="0" width="100%" class="pn-ftable" style="font-size:13px;">
	<tbody>
		<tr>
			<td width="12%" class="pn-flabel pn-flabel-h">上级分类:</td>
			<td width="88%" class="pn-fcontent" colspan="3"><span>${goodsClass.parent.name }</span></td>
		</tr>
		<tr>
			<td width="12%" class="pn-flabel pn-flabel-h"><span class="pn-frequired">*</span>分类名称:</td>
			<td width="88%" class="pn-fcontent" colspan="3"><input type="text" id="goodsClassName" name="goodsClass.name" maxlength="100" class="required" value="${goodsClass.name }"></td>
		</tr>
		<tr>
			<td width="12%" class="pn-flabel pn-flabel-h">分类描述:</td>
			<td width="88%" class="pn-fcontent" colspan="3"><textarea maxlength="255" name="goodsClass.desc" rows="4" cols="60">${goodsClass.desc }</textarea></td>
		</tr>
		<tr>
			<td class="pn-fbutton" colspan="4"><input type="submit" class="submit" value="提交" /> &nbsp; <input type="reset" class="reset" value="重置" /></td>
		</tr>
	</tbody>
	</table>
</form>

<script>
$(document).ready(function() {
	//验证表单
	$("#goodsClassForm").validate();

	//提交表单
	$('#goodsClassForm').submit(function(){
		if(!$('#goodsClassForm').valid()){return;}
		$(this).ajaxSubmit({
			dataType: 'text',
			success: function(json){
				if(json == 'true'){
					var goodsClassObjId = $('#goodsClassObjId').val();
					//修改
					if(goodsClassObjId){
						//更新节点名称
						GoodsClassTree.setItemText(goodsClassObjId, $('input[id=goodsClassName]', '#goodsClassForm').val(), '');
						//刷新表单域
						$('#goodsClassInfo').load($('#initPath').val()+'/pos/goodsClass!toGoodsClassDetailView.do', {id: goodsClassObjId});
					}
					//新增
					else{
						//刷新树节点
						GoodsClassTree.refreshItem(GoodsClassTree.getSelectedItemId());
						//刷新表单域
						$('#goodsClassInfo').load($('#initPath').val()+'/pos/goodsClass!toGoodsClassDetailView.do', {id: GoodsClassTree.getSelectedItemId()});
					}
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