<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<div  class="body-box">
	<table cellspacing="1" cellpadding="2" border="0" width="100%" class="pn-ftable" style="font-size:13px;">
		<tr>
			<td width="12%" class="pn-flabel pn-flabel-h">卡号:</td>
			<td width="88%" class="pn-fcontent">${priceCard.id }</td>
		</tr>
		<tr>
			<td class="pn-flabel pn-flabel-h">店铺名称:</td>
			<td class="pn-fcontent">${priceCard.shopName }</td>
		</tr>
		<tr>
			<td class="pn-flabel pn-flabel-h">店员姓名:</td>
			<td class="pn-fcontent">${priceCard.clerkName }</td>
		</tr>
		<tr>
			<td class="pn-flabel pn-flabel-h">类型:</td>
			<td class="pn-fcontent"><c:if test="${priceCard.type==1 }">红卡</c:if><c:if test="${priceCard.type==2 }">蓝卡</c:if></td>
		</tr>
		<tr <c:if test="${priceCard.type==1 }">style="display: none;"</c:if> >
				<td class="pn-flabel pn-flabel-h">供货商:</td>
				<td class="pn-fcontent">${priceCard.supplierName}</td>
		</tr>
		<tr>
			<td class="pn-flabel pn-flabel-h">余额:</td>
			<td class="pn-fcontent"> 
			 ${priceCard.point}
			</td>
		</tr>
		<tr>
			<td class="pn-flabel pn-flabel-h">状态:</td>
			<td class="pn-fcontent"> 
			      <c:if test="${priceCard.state == 1}">白卡</c:if>
		          <c:if test="${priceCard.state == 2}">可使用</c:if>
		          <c:if test="${priceCard.state == 3}">停用</c:if>
			</td>
		</tr>
		<tr>
			<td class="pn-fcontent" align="center" colspan="2">
			  <input type="button" class="return-button" value="返回" onclick="$('a.jMsgbox-closeWrap').click();" />
			</td>
		</tr>
	</table>
</div>
