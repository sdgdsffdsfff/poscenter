<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false" %>
<form id="menuForm" action="<%=request.getContextPath()%>/pos/menu!save.do" method="post">
	<input type="hidden" id="menuObjId" name="menu.id" value="${menu.id }" />
	<input type="hidden" name="menu.parentId" value="${menu.parentId }" />
	<input type="hidden" name="menu.treeLevel" value="${menu.treeLevel }" />
	<table cellspacing="1" cellpadding="2" border="0" width="100%" class="pn-ftable" style="font-size:13px;">
	<tbody>
		<tr>
			<td width="12%" class="pn-flabel pn-flabel-h">上级菜单:</td>
			<td width="88%" class="pn-fcontent" colspan="3"><span>${menu.parent.menuName }</span></td>
		</tr>
		<tr>
			<td width="12%" class="pn-flabel pn-flabel-h"><span class="pn-frequired">*</span>菜单名称:</td>
			<td width="88%" class="pn-fcontent" colspan="3"><input type="text" id="menuName" name="menu.menuName" maxlength="100" class="required" value="${menu.menuName }"></td>
		</tr>
		<tr>
			<td width="12%" class="pn-flabel pn-flabel-h"><span class="pn-frequired">*</span>关联资源:</td>
			<td width="88%" class="pn-fcontent" colspan="3">
				<input type="hidden" id="resourceId" name="menu.resourceId" value="${menu.resourceId }" />
				<input type="text" id="resourceName" class="required" value="${menu.resource.resName }" />
			</td>
		</tr>
		<tr>
			<td width="12%" class="pn-flabel pn-flabel-h">菜单描述:</td>
			<td width="88%" class="pn-fcontent" colspan="3"><textarea maxlength="255" name="menu.menuDesc" rows="4" cols="60">${menu.menuDesc }</textarea></td>
		</tr>
		<tr>
			<td class="pn-fbutton" colspan="4">
				<input type="submit" class="submit" value="提交" /> &nbsp; 
				<input type="button" class="return-button" onclick="menuList.backDetail();" value="返回" />
			</td>
		</tr>
	</tbody>
	</table>
</form>

<script>
$(document).ready(function() {
	//获取焦点
	$('#menuName').focus();
	
	//验证表单
	$("#menuForm").validate();

	//选择资源
	$('#resourceName').click(function(){
		new $.msgbox({
			title: '选择资源',
			width: 300,
			height: 400,
			type: 'ajax',
			content: $('#initPath').val()+'/poscenter/auth/resource/resourceTree.jsp?IDS=resourceId&NAMES=resourceName'
		}).show();
	});

	//提交表单
	$('#menuForm').submit(function(){
		if(!$('#menuForm').valid()){return;}
		$(this).ajaxSubmit({
			dataType: 'text',
			success: function(json){
				if(json == 'true'){
					var menuObjId = $('#menuObjId').val();
					//修改
					if(menuObjId){
						//更新节点名称
						MenuTree.setItemText(menuObjId, $('input[id=menuName]', '#menuForm').val(), '');
						//刷新表单域
						$('#menuInfo').load($('#initPath').val()+'/pos/menu!toMenuDetailView.do', {id: menuObjId});
					}
					//新增
					else{
						//刷新树节点
						MenuTree.refreshItem(MenuTree.getSelectedItemId());
						//刷新表单域
						$('#menuInfo').load($('#initPath').val()+'/pos/menu!toMenuDetailView.do', {id: MenuTree.getSelectedItemId()});
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