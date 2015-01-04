<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<div class="body-box">
	<table cellspacing="1" cellpadding="2" border="0" width="100%" class="pn-ftable" style="font-size:13px;">
		<tr>
			<td width="20%" class="pn-flabel pn-flabel-h">用户名:</td>
			<td width="80%" class="pn-fcontent">${user.userName }</td>
		</tr>
		<tr>
			<td class="pn-flabel pn-flabel-h">真实姓名:</td>
			<td class="pn-fcontent">${user.userRealName }</td>
		</tr>
		<tr>
			<td class="pn-flabel pn-flabel-h">所属部门:</td>
			<td class="pn-fcontent">
				${user.departmentName }
			</td>
		</tr>
		<c:if test="${user.id != 0}">
			<tr>
				<td class="pn-flabel pn-flabel-h"> 状态:</td>
				<td class="pn-fcontent">
					<c:if test="${user.useStatus == 1}">
					有效
					</c:if>
					<c:if test="${user.useStatus == 2}">
					禁用
					</c:if>
				</td>
			</tr>
		</c:if>
		<tr>
			<td class="pn-flabel pn-flabel-h">拥有角色:</td>
			<td class="pn-fcontent">
				${roleName}
			</td>
		</tr>
		<tr>
			<td class="pn-fcontent" align="center" colspan="2">
				<input type="button" class="return-button" value="返回" onclick="$('a.jMsgbox-closeWrap').click();" />
			</td>
		</tr>
	</table>
</div>
