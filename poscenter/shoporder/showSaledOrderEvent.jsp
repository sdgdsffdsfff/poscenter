<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<script type="text/javascript">
$(document).ready(function(){
	if($('#fixedProductListTable').find('table tr').length == 1){
		$('#fixedProductListTable').hide();
	}
	if($('#ladderProductListTable').find('table tr').length == 1){
		$('#ladderProductListTable').hide();
	}
});
</script>
<div class="body-box">
<table cellspacing="1" cellpadding="2" border="0" width="100%" class="pn-ftable" style="font-size:13px;">
	<tr>
		<td width="12%" class="pn-flabel pn-flabel-h">订单编号:</td>
		<td width="88%" class="pn-fcontent">${shopSaledOrder.serialNumber}</td>
	</tr>
	<tr>
		<td width="12%" class="pn-flabel pn-flabel-h">销售时间:</td>
		<td width="88%" class="pn-fcontent"><fmt:formatDate value="${shopSaledOrder.saledTime}" pattern="yyyy-MM-dd HH:mm" /></td>
	</tr>
	<c:if test="${shopSaledOrder.memberId!=null && shopSaledOrder.memberId!=''}">
	<tr>
		<td width="12%" class="pn-flabel pn-flabel-h">会员编号:</td>
		<td width="88%" class="pn-fcontent">${shopSaledOrder.memberId}</td>
	</tr>
	</c:if>
	<tr>
		<td class="pn-flabel pn-flabel-h" rowspan="5">详细活动:</td>
		<td class="pn-fcontent" rowspan="5" >
		  
		  <c:if test="${fn:length(event.buyGiftEventList) > 0 }">
		  <div><span style="font-weight:bold;">买赠活动</span></div>
		    <table id="buyGiftEventTable" width="100%" class="pn-ftable" cellspacing="1" style="margin-bottom:20px;">
			<tr align="center">
				<td class="pn-fcontent">促销商品</td>
				<td class="pn-fcontent" >购买数量</td>
				<td class="pn-fcontent">人数限制</td>
				<td class="pn-fcontent" >赠品</td>
			</tr>
			<%--促销商品列表--%>
			<c:forEach var="buyGiftEvent" items="${event.buyGiftEventList}">
			<tr align="center">
				<td class="pn-fcontent">${buyGiftEvent.product.name}</td>
				<td class="pn-fcontent">${buyGiftEvent.productCount}</td>
				<td class="pn-fcontent">${buyGiftEvent.userLimit}</td>
				<td class="pn-fcontent">
					<table width="100%">
						<%--赠品列表--%>
						<c:forEach var="gift" items="${buyGiftEvent.giftList}">
						<tr>
							<td class="pn-fcontent" width="200">${gift.giftProduct.name }</td>
							<td class="pn-fcontent" width="50" align="center">${gift.maxGiftCount }</td>
						</tr>
						</c:forEach>
					</table>
				</td>
			</tr>
			</c:forEach>
		</table>
	   </c:if>
	   
	   <c:if test="${fn:length(event.swapBuyEventList) > 0 }">
	   <div><span style="font-weight:bold;">换购活动</span></div>
	    <table id="swapBuyEventTable" width="100%" class="pn-ftable" cellspacing="1" style="margin-bottom:20px;">
		<tr align="center" style="">
			<td class="pn-fcontent">需消费金额</td>
			<td class="pn-fcontent" >添加金额</td>
			<td class="pn-fcontent">换购商品</td>
		</tr>
		<%--换购活动列表--%>
		<c:forEach var="swapBuyEvent" items="${event.swapBuyEventList}">
		<tr align="center">
			<td class="pn-fcontent">${swapBuyEvent.money}</td>
			<td class="pn-fcontent">${swapBuyEvent.appendMoney}</td>
			<td class="pn-fcontent">
				<%--换购商品列表--%>
				<c:forEach var="swapProduct" items="${swapBuyEvent.swapBuyProductList}">
				${swapProduct.giftProductName }<br/>
				</c:forEach>
			</td>
		</tr>
		</c:forEach>
		</table>
	   </c:if>
	   
		<c:if test="${fn:length(event.moneyDiscountEventList) > 0 }">
		<div><span style="font-weight:bold;">金额折扣活动</span></div>
		<table id="moneyDiscountEventListTable" width="100%" class="pn-ftable" cellspacing="1" style="margin-bottom:20px;">
			<tr align="center" style="">
					<td class="pn-fcontent">消费总计金额</td>
					<td class="pn-fcontent">满减优惠</td>
					<td class="pn-fcontent">打折优惠</td>
			</tr>
			<c:forEach var="moneyDiscountEvent" items="${event.moneyDiscountEventList}">
			<tr align="center">
					<td class="pn-fcontent">${moneyDiscountEvent.money}</td>
					<td class="pn-fcontent"><c:if test="${moneyDiscountEvent.type == 1 }">${moneyDiscountEvent.numberValue}</c:if></td>
					<td class="pn-fcontent"><c:if test="${moneyDiscountEvent.type == 2 }">${moneyDiscountEvent.numberValue}</c:if></td>
			</tr>
			</c:forEach>
		</table>
		</c:if>
	   
		<c:if test="${fn:length(event.comboEventList) > 0}">
		<div><span style="font-weight:bold;">套餐活动</span></div>
		<table id="comboEventListTable" width="100%" class="pn-ftable" cellspacing="1" style="margin-bottom:20px;">
			<tr align="center" style="">
					<td class="pn-fcontent" >套餐商品</td>
					<td class="pn-fcontent">套餐价</td>
			</tr>
			<c:forEach var="comboEvent" items="${event.comboEventList}">
			<tr align="center">
					<td class="pn-fcontent">
						<table width="100%">
							<%--套餐商品列表--%>
							<c:forEach var="comboProduct" items="${comboEvent.comboProductList}">
							<tr>
								<td class="pn-fcontent">${comboProduct.product.name }</td>
								<td class="pn-fcontent">${comboProduct.productCount }</td>
							</tr>
							</c:forEach>
						</table>
					</td>
				<td class="pn-fcontent">${comboEvent.comboPrice}</td>
			</tr>
			</c:forEach>
		</table>
		</c:if>
	   
		<c:if test="${fn:length(event.countDiscountEventList) > 0}">
		<div id="fixedProductListTable">
			<div><span style="font-weight:bold;">数量折扣活动-固定折扣商品</span>（只要商品数量>=2时，全部享受一个折扣优惠）</div>
			<table width="100%" class="pn-ftable" cellspacing="1">
				<tr align="center">
					<td class="pn-fcontent">商品</td>
					<td class="pn-fcontent">折扣</td>
				</tr>
				<c:forEach var="countDiscountEvent" items="${event.countDiscountEventList}">
			    <c:if test="${countDiscountEvent.type == 2}">
				<tr align="center">
					<td class="pn-fcontent">${countDiscountEvent.product.name }</td>
					<td class="pn-fcontent"><fmt:formatNumber value="${countDiscountEvent.discount * 100}" pattern="#,##0" /></td>
				</tr>
				</c:if>
				</c:forEach>
			</table>
		</div>
		<div id="ladderProductListTable">
			<div><span style="font-weight:bold;">数量折扣活动-阶梯折扣商品</span>（第一件默认是原价，从第二件起产生阶梯折扣）</div>
			<table width="100%" class="pn-ftable" cellspacing="1">
				<tr align="center">
					<td class="pn-fcontent">商品</td>
					<td class="pn-fcontent">折扣</td>
				</tr>
				<c:forEach var="countDiscountEvent" items="${event.countDiscountEventList}">
				<c:if test="${countDiscountEvent.type == 1}">
				<tr align="center">
					<td class="pn-fcontent">${countDiscountEvent.product.name }</td>
					<td class="pn-fcontent"><fmt:formatNumber value="${countDiscountEvent.discount * 100}" pattern="#,##0"  /></td>
				</tr>
				</c:if>
				</c:forEach>
			</table>
		</div>
	   </c:if>
	</td>
	</tr>
</table>

<div style="text-align:center; margin-top:10px; margin-bottom: 20px;">
	<input type="button" class="return-button" value="关闭" onclick="$('a.jMsgbox-closeWrap').click();" />
</div>
</div>
