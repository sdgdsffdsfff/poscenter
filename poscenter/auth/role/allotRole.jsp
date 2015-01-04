<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<script type="text/javascript">
var allotRole={};

//全选、取消
allotRole.selectAll = function(dom){
	//全选
	if($(dom).attr('checked')){
		$('#allotRoleListTable').find('input:not(:disabled)[type=checkbox][name=selectAll]').attr('checked', true);
	}else{
		$('#allotRoleListTable').find('input:not(:disabled)[type=checkbox][name=selectAll]').attr('checked', false);
	}
}

//提交
allotRole.submit = function(){
	if($('#allotRoleListTable').find('input:checked:not(:disabled)[type=checkbox][name=selectAll]').length == 0){
		if(!confirm('确定不为该用户分配任何角色吗？')){
			return ;
		}
	}

	//组装数据
	var allotRoleIds = [];
	$('#allotRoleListTable').find('input:checked:not(:disabled)[type=checkbox][name=selectAll]').each(function(i,dom){
		var allotRoleId = $(dom).parent().find('input[name=roleId]').val();
		allotRoleIds.push(allotRoleId);
	});

	$.post($('#initPath').val()+'/pos/role!allotRole.do', {'userId': $('#allotUserId').val(), 'roleIds':allotRoleIds.join(',')}, function(json){
		if(json == 'success'){
			alert('提交成功！');
			//关闭弹出框
			$('a.jMsgbox-closeWrap').click();
		}else{
			alert(json);
		}
	});
}

</script>
<div>

<input type="button" value="提交" onclick="allotRole.submit();" />&nbsp;
<input type="button" value="取消" onclick="$('a.jMsgbox-closeWrap').click();" />
<input type="hidden" id="allotUserId" value="${userId }" />
<table id="allotRoleListTable" width="100%" cellspacing="1" cellpadding="0" border="0" class="pn-ltable">
<thead class="pn-lthead">
<tr>
	<th><input type="checkbox" onclick="allotRole.selectAll(this);" /></th>
	<th>角色名称</th>
	<th>角色中文名称</th>
	<th>描述</th>
	<th>创建时间</th>
</tr>
</thead>
<tbody class="pn-ltbody">
	<c:forEach var="role" items="${page.list}">
		<c:set var="isAllotted" value="false" />
		<c:forEach var="allottedRole" items="${allottedRoleList}">
			<c:if test="${role.id == allottedRole.id}"><c:set var="isAllotted" value="true" /></c:if>
		</c:forEach>
		<tr>
			<td align="center">
				<input type="checkbox" name="selectAll" <c:if test="${isAllotted}">checked="checked"</c:if> />
				<input type="hidden" name="roleId" value="${role.id }" />
			</td>
			<td>${role.roleName }</td>
			<td>${role.roleChName }</td>
			<td>${role.roleDesc }</td>
			<td align="center"><fmt:formatDate value="${role.createTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
		</tr>
	</c:forEach>
</tbody>
</table>
</div>