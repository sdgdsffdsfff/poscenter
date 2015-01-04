<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false" %>

<table cellspacing="1" cellpadding="2" border="0" width="100%" class="pn-ftable" style="font-size:13px;">
<tbody>
	<tr>
		<td width="12%" class="pn-flabel pn-flabel-h">上级分类:</td>
		<td width="88%" class="pn-fcontent" colspan="3"><span>${goodsClass.parent.name }</span></td>
	</tr>
	<tr>
		<td width="12%" class="pn-flabel pn-flabel-h">分类编号:</td>
		<td width="88%" class="pn-fcontent" colspan="3"><span>${goodsClass.id }</span></td>
	</tr>
	<tr>
		<td width="12%" class="pn-flabel pn-flabel-h">分类名称:</td>
		<td width="88%" class="pn-fcontent" colspan="3"><span>${goodsClass.name }</span></td>
	</tr>
	<tr>
		<td width="12%" class="pn-flabel pn-flabel-h">分类描述:</td>
		<td width="88%" class="pn-fcontent" colspan="3">${goodsClass.desc }</td>
	</tr>
</tbody>
</table>
