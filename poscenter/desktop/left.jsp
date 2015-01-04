<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title>二级菜单</title>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/skin/css/admin.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/skin/css/theme.css" />
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script>
<script type="text/javascript">
//计算当前时间
function getYMDW(){
	var now = new Date();
    var weeks = ['日','一','二','三','四','五','六'];
    return now.getFullYear()+'年'+(now.getMonth()+1)+'月'+now.getDate()+'日 星期'+weeks[now.getDay()];
}

//选中当前菜单
function selectedCurrentMenu(obj){
	window.open($(obj).find('a').attr('href'), $(obj).find('a').attr('target'));
	$(obj).siblings('li').removeClass('selectedMenu');
	$(obj).addClass('selectedMenu');
}

$(document).ready(function(){
	//计算当前时间
	$('#nowDateSpan').append(getYMDW());
	
	//加载第一个菜单页面
	var aObj = $('#second_level_menu_list li:eq(0)').find('a');
	window.open($(aObj).attr('href'), $(aObj).attr('target'));
	$('#second_level_menu_list li:eq(0)').click();
});
</script>
<style type="text/css">
.selectedMenu{
	font-weight: bold;
	color: #0033cc;
}
</style>
</head>
<body class="lbody">
<div class="left">
	<div class="date">
		<span id="nowDateSpan">现在时间：</span>
	</div>
	<ul id="second_level_menu_list" class="w-lful">
		<c:forEach var="menu" items="${secondMenuList}">
			<li onclick="selectedCurrentMenu(this)"><a target="rightFrame" href="<%=request.getContextPath()%>/${menu.resource.resUrl }">${menu.menuName }</a></li>
		</c:forEach>
     </ul>
</div>
</body>
</html>