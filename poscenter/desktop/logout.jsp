<%@ page contentType="text/html;charset=utf-8" %>
<%@ page import="java.util.*,mmb.auth.domain.*,mmb.framework.*" %>
<%
User user = (User) session.getAttribute(IConstants.USER_VIEW_KEY);
if(user!=null) {
	mmboa.util.CookieUtil ck = new mmboa.util.CookieUtil(request,response);
	ck.removeCookie("opau");
	ck.removeCookie("opap");
	session.invalidate();
}
%>
<!DOCTYPE html>
<script type="text/javascript">
parent.location="<%=request.getContextPath()%>/";
</script>
