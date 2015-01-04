<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false"%>
<%@ include file="/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>促销活动列表</title>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/skin/css/admin.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/skin/css/theme.css" />

<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript">
var eventList={};

//新增买赠活动
eventList.addBuyGiftEvent = function(){
	window.location.href = $('#initPath').val()+'/pos/buyGiftEvent!toEventFormView.do';
}

//查看
eventList.showEvent = function(type, id){
	var url;
	if(type == 1){
		url = '/pos/buyGiftEvent!toEventDetailView.do?event.id='+id;
	}else if(type == 2){
		url = '/pos/swapBuyEvent!catSwapBuyEventDetail.do?event.id='+id;
	}else if(type == 3){
		url = '/pos/moneyDiscountEvent!toEventDetailView.do?event.id='+id;
	}else if(type == 4){
		url = '/pos/countDiscountEvent!toEventDetailView.do?event.id='+id;
	}else if(type == 5){
		url = '/pos/comboEvent!toEventDetailView.do?event.id='+id;
	}
	window.location.href = $('#initPath').val()+url;
}

//修改
eventList.updateEvent = function(type, id){
	var url;
	if(type == 1){
		url = '/pos/buyGiftEvent!toEventFormView.do?event.id='+id;
	}else if(type == 2){
		url = '/pos/swapBuyEvent!toSwapBuyEventFormView.do?event.id='+id;
	}else if(type == 3){
		url = '/pos/moneyDiscountEvent!toEventFormView.do?event.id='+id;
	}else if(type == 4){
		url = '/pos/countDiscountEvent!toEventFormView.do?event.id='+id;
	}else if(type == 5){
		url = '/pos/comboEvent!toEventFormView.do?event.id='+id;
	}
	window.location.href = $('#initPath').val()+url;
}

//删除
eventList.deleteEvent = function(type, id){
	if(window.confirm('确定删除该促销活动吗？')){
		var url;
		if(type == 1){
			url = '/pos/buyGiftEvent!deleteEvent.do?event.id='+id;
		}else if(type == 2){
			url = '/pos/swapBuyEvent!deleteEvent.do?event.id='+id;
		}else if(type == 3){
			url = '/pos/moneyDiscountEvent!deleteEvent.do?event.id='+id;
		}else if(type == 4){
			url = '/pos/countDiscountEvent!deleteEvent.do?event.id='+id;
		}else if(type == 5){
			url = '/pos/comboEvent!deleteEvent.do?event.id='+id;
		}
		window.location.href = $('#initPath').val()+url;
	}
}

//跳转到指定页面
eventList.goPage = function(pageNum,pageCount){
	var url = $('#initPath').val()+'/pos/event!eventList.do?event.type='+$('#type').val()+'&event.useStatus='+$('#useStatus').val()+'&event.startTime='+$('#startTime').val()+'&event.endTime='+$('#endTime').val();
	if(pageNum != null && pageCount != null){
		url += '&page.pageNum='+pageNum;
	}
	if(pageCount != null){
		url += '&page.pageCount='+pageCount;
	}
	window.location.href = url;
}

//显示选中类型的活动
eventList.goSelectedEvent=function(){
    window.location.href = $('#initPath').val()+'/pos/event!eventList.do?event.type='+$('#type').val()+'&event.useStatus='+$('#useStatus').val();
}

//查询符合条件的活动
eventList.goQueryEvent=function(){
   window.location.href = $('#initPath').val()+'/pos/event!eventList.do?event.type='+$('#type').val()+'&event.useStatus='+$('#useStatus').val()+'&event.startTime='+$('#startTime').val()+'&event.endTime='+$('#endTime').val();
}

</script>
</head>
<body>
<input type="hidden" id="initPath" value="<%=request.getContextPath()%>" />
<div class="box-positon">
	<div class="rpos">当前位置: 促销活动管理 - 列表</div>
	<div class="ropt">
		<auth:authorize ifGranted="addEvent">
		<input type="button" onclick="eventList.addBuyGiftEvent();" class="submit" value="新增" /> &nbsp; 
		</auth:authorize>
	</div>
	<div class="clear"></div>
</div>

<div id="bodyBox" class="body-box">
<form style="padding-top:5px;" action="<%=request.getContextPath()%>/pos/sendOrder!sendOrderList.do" method="post">
<div>
	活动类型:
	<select id="type" name="event.type" onchange="eventList.goSelectedEvent()" >
		<option value="0">-全部-</option>
		<option value="1" <c:if test="${event.type == 1}">selected="selected"</c:if>>买赠</option>
		<option value="2" <c:if test="${event.type == 2}">selected="selected"</c:if>>换购</option>
		<option value="3" <c:if test="${event.type == 3}">selected="selected"</c:if>>金额折扣</option>
		<option value="4" <c:if test="${event.type == 4}">selected="selected"</c:if>>数量折扣</option>
		<option value="5" <c:if test="${event.type == 5}">selected="selected"</c:if>>套餐</option>
	</select>&nbsp;&nbsp;
	创建时间:<input type="text" id="startTime" size="15" name="startTime" class="Wdate" onclick="WdatePicker()" value="<fmt:formatDate value="${event.startTime}" pattern="yyyy-MM-dd HH:mm" />" readonly="readonly" />
	- <input type="text" id="endTime" size="15" name="endTime" class="Wdate" onclick="WdatePicker()" value="<fmt:formatDate value="${event.endTime}" pattern="yyyy-MM-dd HH:mm" />" readonly="readonly"/>&nbsp;&nbsp;
	状态:
	<select id="useStatus" name="event.useStatus" onchange="eventList.goSelectedEvent()">
		<option value="0">-全部-</option>
		<option value="1" <c:if test="${event.useStatus == 1}">selected="selected"</c:if>>未生效</option>
		<option value="2" <c:if test="${event.useStatus == 2}">selected="selected"</c:if>>已生效</option>
		<option value="3" <c:if test="${event.useStatus == 3}">selected="selected"</c:if>>已过期</option>
	</select>&nbsp;&nbsp;
	<input type="button" value="查询" class="query" onclick="eventList.goQueryEvent()"/>
</div>
</form>

<table id="goodsList" width="100%" cellspacing="1" cellpadding="0" border="0" style="" class="pn-ltable">
<thead class="pn-lthead">
<tr>
	<th>活动类型</th>
	<th>开始时间</th>
	<th>结束时间</th>
	<th>促销描述</th>
	<th>适用门店</th>
	<th>状态</th>
	<th>操作</th>
</tr>
</thead>
<tbody class="pn-ltbody">
<c:forEach var="event" items="${page.list}">
<c:set var="shops" value="" />
<c:forEach  var="shop" items="${event.shopList}" varStatus="status" >
    <c:if test="${!status.first}" >
      <c:set var="shops" value="${shops}," />
    </c:if>
      <c:set var="shops" value="${shops}${shop.name}" />
    
</c:forEach>

<tr>
	<td align="center">
		<c:choose>
			<c:when test="${event.type == 1}">买赠</c:when>
			<c:when test="${event.type == 2}">换购</c:when>
			<c:when test="${event.type == 3}">金额折扣</c:when>
			<c:when test="${event.type == 4}">数量折扣</c:when>
			<c:when test="${event.type == 5}">套餐</c:when>
		</c:choose>
	</td>
	<td align="center"><fmt:formatDate value="${event.startTime}" pattern="yyyy-MM-dd HH:mm" /></td>
	<td align="center"><fmt:formatDate value="${event.endTime}" pattern="yyyy-MM-dd HH:mm" /></td>
	<td>${event.ruleDesc }</td>
	<td>${shops}</td>
	<td align="center">
		<c:choose>
			<c:when test="${event.useStatus == 1}">未生效</c:when>
			<c:when test="${event.useStatus == 2}">已生效</c:when>
			<c:when test="${event.useStatus == 3}">已过期</c:when>
		</c:choose>
	</td>
	<td>
		&nbsp;&nbsp;
		<a href="javascript:void(0);" onclick="eventList.showEvent(${event.type},${event.id });">查看</a>
		<c:if test="${event.useStatus == 1 || event.useStatus == 2}">
			<auth:authorize ifGranted="updateEvent">
			<a href="javascript:void(0);" onclick="eventList.updateEvent(${event.type},${event.id });">修改</a>
			</auth:authorize>
		</c:if>
		<c:if test="${event.useStatus == 1}">
			<auth:authorize ifGranted="deleteEvent">
			<a href="javascript:void(0);" onclick="eventList.deleteEvent(${event.type},${event.id });">删除</a>
			</auth:authorize>
		</c:if>
	</td>
</tr>
</c:forEach>
</tbody>
</table>

<table id="dtPageTable" width="100%" style="font-size:13px;">
	<tbody>
	<tr>
		<td align="center" class="pn-sp">
		<c:if var="isFirstPage" test="${page.pageNum<=1}">
		首页 &nbsp;&nbsp;&nbsp;上一页&nbsp;&nbsp;&nbsp;
		</c:if>
		<c:if test="${!isFirstPage}">
		<a href="javascript:;" onclick="eventList.goPage(1,${page.pageCount});">首页</a>&nbsp;&nbsp;&nbsp;
		<a href="javascript:;" onclick="eventList.goPage(${page.pageNum-1},${page.pageCount});">上一页</a>
		</c:if>
		&nbsp;&nbsp;&nbsp;
		<c:if var="isLastPage" test="${page.pageNum>=page.totalPages}">
		下一页&nbsp;&nbsp;&nbsp;尾页&nbsp;&nbsp;&nbsp;
		</c:if>
		<c:if test="${!isLastPage}">
		<a href="javascript:;" onclick="eventList.goPage(${page.pageNum+1},${page.pageCount});">下一页</a>&nbsp;&nbsp;&nbsp;
		<a href="javascript:;" onclick="eventList.goPage(${page.totalPages},${page.pageCount});">尾页</a>
		</c:if>
		&nbsp;&nbsp;&nbsp;&nbsp;
		第${page.pageNum}/${page.totalPages}页,共${page.totalRecords}条记录
	</td>
	</tr>
	</tbody>
</table>
</div>
</body>
</html>