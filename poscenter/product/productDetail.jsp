<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<table cellspacing="1" cellpadding="2" border="0" width="100%" class="pn-ftable" style="font-size:13px;">
<tbody>
	<tr>
		<td class="pn-flabel pn-flabel-h">条形码:</td>
		<td class="pn-fcontent" colspan="3"><span>${product.barCode }</span></td>
	</tr>
	<tr>
		<td class="pn-flabel pn-flabel-h">商品名称:</td>
		<td class="pn-fcontent" colspan="3"><span>${product.name }</span></td>
	</tr>
	<tr>
		<td class="pn-flabel pn-flabel-h">商品分类:</td>
		<td class="pn-fcontent" colspan="3"><span>${product.goodsClass.name }</span></td>
	</tr>
	<tr>
		<td class="pn-flabel pn-flabel-h">供应商:</td>
		<td class="pn-fcontent" colspan="3"><span>${product.supplierName}</span></td>
	</tr>
	<tr>
		<td width="20%" class="pn-flabel pn-flabel-h">日租赁价格(元):</td>
		<td width="30%" class="pn-fcontent"><fmt:formatNumber value="${product.leasePrice }" pattern="#,##0.00" /></td>
		<td width="20%" class="pn-flabel pn-flabel-h">包月租赁价格(元):</td>
		<td width="30%" class="pn-fcontent"><fmt:formatNumber value="${product.monthLeasePrice }" pattern="#,##0.00" /></td>
	</tr>
	<tr>
		<td class="pn-flabel pn-flabel-h">押金(元):</td>
		<td class="pn-fcontent" colspan="3"><fmt:formatNumber value="${product.deposit }" pattern="#,##0.00" /></td>
	</tr>
	<tr>
		<td class="pn-flabel pn-flabel-h">库存量:</td>
		<td class="pn-fcontent" colspan="3">${product.stock }</td>
	</tr>
	<tr>
		<td class="pn-flabel pn-flabel-h">标牌价(元):</td>
		<td class="pn-fcontent" colspan="3"><fmt:formatNumber value="${product.salePrice }" pattern="#,##0.00" /></td>
	</tr>
	<tr>
		<td class="pn-flabel pn-flabel-h">限价(元):</td>
		<td class="pn-fcontent" colspan="3"><fmt:formatNumber value="${product.limitPrice }" pattern="#,##0.00" /></td>
	</tr>
	<tr>
		<td class="pn-flabel pn-flabel-h">锁价(元):</td>
		<td class="pn-fcontent" colspan="3"><fmt:formatNumber value="${product.lockPrice }" pattern="#,##0.00" /></td>
	</tr>
	<tr>
		<td class="pn-flabel pn-flabel-h">红卡额度:</td>
		<td class="pn-fcontent"><fmt:formatNumber value="${product.redLines }" pattern="#,##0.00" /></td>
		<td class="pn-flabel pn-flabel-h">蓝卡额度:</td>
		<td class="pn-fcontent"><fmt:formatNumber value="${product.blueLines }" pattern="#,##0.00" /></td>
	</tr>
	<tr>
		<td align="center" colspan="4" class="pn-fcontent">
			<input type="button" value="返回" onclick="$('a.jMsgbox-closeWrap').click();" class="return-button" />
		</td>
	</tr>
</tbody>
</table>
