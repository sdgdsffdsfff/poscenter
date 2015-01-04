<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false" %>
<table cellspacing="1" cellpadding="2" border="0" width="100%" class="pn-ftable" style="font-size:13px;">
<tbody>
	<tr>
		<td width="12%" class="pn-flabel pn-flabel-h">上级菜单:</td>
		<td width="88%" class="pn-fcontent" colspan="3"><span>${menu.parent.menuName }</span></td>
	</tr>
	<tr>
		<td width="12%" class="pn-flabel pn-flabel-h">菜单编号:</td>
		<td width="88%" class="pn-fcontent" colspan="3"><span>${menu.id }</span></td>
	</tr>
	<tr>
		<td width="12%" class="pn-flabel pn-flabel-h">菜单名称:</td>
		<td width="88%" class="pn-fcontent" colspan="3"><span>${menu.menuName }</span></td>
	</tr>
	<tr>
		<td width="12%" class="pn-flabel pn-flabel-h">关联资源:</td>
		<td width="88%" class="pn-fcontent" colspan="3"><span>${menu.resource.resName }</span></td>
	</tr>
	<tr>
		<td width="12%" class="pn-flabel pn-flabel-h">菜单描述:</td>
		<td width="88%" class="pn-fcontent" colspan="3">${menu.menuDesc }</td>
	</tr>
</tbody>
</table>
