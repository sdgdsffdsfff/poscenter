package mmb.poscenter.action;

import java.util.HashMap;
import java.util.Map;

import mmb.poscenter.domain.ShopLeaseOrder;
import mmb.poscenter.domain.ShopSaledOrder;
import mmb.poscenter.service.ShopLeaseOrderService;
import mmb.poscenter.service.ShopSaledOrderService;

import com.opensymphony.xwork2.ActionSupport;

public class ShopOrderAction extends ActionSupport {
	private static final long serialVersionUID = 1L;
	ShopLeaseOrderService slos = new ShopLeaseOrderService();
	ShopSaledOrderService ssos = new ShopSaledOrderService();
	private Page<ShopLeaseOrder> leaseOrderPage = new Page<ShopLeaseOrder>(); //租赁订单
	private Page<ShopSaledOrder> saledOrderPage = new Page<ShopSaledOrder>(); //销售订单
	private int orderType; //订单类型
	private String memberName; //会员名称
	private String shopName; //店面名称
	private String serialNumber; //订单流水号
	private int payMethod; //支付方式
	private String swipCardNumber; //刷卡流水号
	
	public Page<ShopLeaseOrder> getLeaseOrderPage() {
		return leaseOrderPage;
	}

	public void setLeaseOrderPage(Page<ShopLeaseOrder> leaseOrderPage) {
		this.leaseOrderPage = leaseOrderPage;
	}

	public Page<ShopSaledOrder> getSaledOrderPage() {
		return saledOrderPage;
	}

	public void setSaledOrderPage(Page<ShopSaledOrder> saledOrderPage) {
		this.saledOrderPage = saledOrderPage;
	}
	
	public int getOrderType() {
		return orderType;
	}

	public void setOrderType(int orderType) {
		this.orderType = orderType;
	}
	
	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}
	
	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	
	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	
	public int getPayMethod() {
		return payMethod;
	}

	public void setPayMethod(int payMethod) {
		this.payMethod = payMethod;
	}

	public String getSwipCardNumber() {
		return swipCardNumber;
	}

	public void setSwipCardNumber(String swipCardNumber) {
		this.swipCardNumber = swipCardNumber;
	}

	/**
	 * 跳转至租赁订单列表界面
	 * @return
	 */
	public String shopLeaseOrderList(){
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("orderType", orderType); //订单类型
		param.put("memberName", memberName);
		param.put("shopName", shopName);
		this.leaseOrderPage = slos.getLeaseOrderPage(this.leaseOrderPage, param);
		return "shopLeaseOrderList";
	}
	
	/**
	 * 跳转至销售订单列表界面
	 * @return
	 */
	public String shopSaledOrderList(){
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("orderType", orderType); //订单类型
		param.put("memberName", memberName);
		param.put("shopName", shopName);
		param.put("serialNumber", serialNumber);
		param.put("payMethod", payMethod);
		param.put("swipCardNumber", swipCardNumber);
		this.saledOrderPage = ssos.getShopSaledOrderPage(this.saledOrderPage, param);
		return "shopSaledOrderList";
	}

}