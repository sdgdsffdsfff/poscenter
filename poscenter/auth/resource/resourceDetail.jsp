<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false" %>
<table cellspacing="1" cellpadding="2" border="0" width="100%" class="pn-ftable" style="font-size:13px;">
<tbody>
	<tr>
		<td width="12%" class="pn-flabel pn-flabel-h">上级资源:</td>
		<td width="88%" class="pn-fcontent" colspan="3"><span>${resource.parent.resName }</span></td>
	</tr>
	<tr>
		<td width="12%" class="pn-flabel pn-flabel-h">资源编号:</td>
		<td width="88%" class="pn-fcontent" colspan="3"><span>${resource.id }</span></td>
	</tr>
	<tr>
		<td width="12%" class="pn-flabel pn-flabel-h">资源名称:</td>
		<td width="88%" class="pn-fcontent" colspan="3"><span>${resource.resName }</span></td>
	</tr>
	<tr>
		<td width="12%" class="pn-flabel pn-flabel-h">资源路径:</td>
		<td width="88%" class="pn-fcontent" colspan="3"><span>${resource.resUrl }</span></td>
	</tr>
	<tr>
		<td width="12%" class="pn-flabel pn-flabel-h">资源描述:</td>
		<td width="88%" class="pn-fcontent" colspan="3">${resource.resDesc }</td>
	</tr>
</tbody>
</table>
