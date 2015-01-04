<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false" %>
<form id="resourceForm" action="<%=request.getContextPath()%>/pos/resource!save.do" method="post">
	<input type="hidden" id="resourceObjId" name="resource.id" value="${resource.id }" />
	<input type="hidden" name="resource.parentId" value="${resource.parent.id }" />
	<input type="hidden" name="resource.treeLevel" value="${resource.treeLevel }" />
	<table cellspacing="1" cellpadding="2" border="0" width="100%" class="pn-ftable" style="font-size:13px;">
	<tbody>
		<tr>
			<td width="12%" class="pn-flabel pn-flabel-h">上级资源:</td>
			<td width="88%" class="pn-fcontent" colspan="3"><span>${resource.parent.resName }</span></td>
		</tr>
		<tr>
			<td width="12%" class="pn-flabel pn-flabel-h"><span class="pn-frequired">*</span>资源名称:</td>
			<td width="88%" class="pn-fcontent" colspan="3"><input type="text" id="resourceName" name="resource.resName" maxlength="100" class="required" value="${resource.resName }"></td>
		</tr>
		<tr>
			<td width="12%" class="pn-flabel pn-flabel-h"><span class="pn-frequired">*</span>资源路径:</td>
			<td width="88%" class="pn-fcontent" colspan="3"><input type="text" size="50" name="resource.resUrl" class="required" value="${resource.resUrl }"></td>
		</tr>
		<tr>
			<td width="12%" class="pn-flabel pn-flabel-h">资源描述:</td>
			<td width="88%" class="pn-fcontent" colspan="3"><textarea maxlength="255" name="resource.resDesc" rows="4" cols="60">${resource.resDesc }</textarea></td>
		</tr>
		<tr>
			<td class="pn-fbutton" colspan="4">
				<input type="submit" class="submit" value="提交" /> &nbsp; 
				<input type="button" class="return-button" onclick="resourceList.backDetail();" value="返回" />
			</td>
		</tr>
	</tbody>
	</table>
</form>

<script>
$(document).ready(function() {
	//获取焦点
	$('#resourceName').focus();
	
	//验证表单
	$("#resourceForm").validate();

	//提交表单
	$('#resourceForm').submit(function(){
		if(!$('#resourceForm').valid()){return;}
		$(this).ajaxSubmit({
			dataType: 'text',
			success: function(json){
				if(json == 'true'){
					var resourceObjId = $('#resourceObjId').val();
					//修改
					if(resourceObjId){
						//更新节点名称
						ResourceTree.setItemText(resourceObjId, $('input[id=resourceName]', '#resourceForm').val(), '');
						//刷新表单域
						$('#resourceInfo').load($('#initPath').val()+'/pos/resource!toResourceDetailView.do', {id: resourceObjId});
					}
					//新增
					else{
						//刷新树节点
						ResourceTree.refreshItem(ResourceTree.getSelectedItemId());
						//刷新表单域
						$('#resourceInfo').load($('#initPath').val()+'/pos/resource!toResourceDetailView.do', {id: ResourceTree.getSelectedItemId()});
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