package mmb.poscenter.action;


import mmb.poscenter.domain.Event;
import mmb.poscenter.domain.ShopSaledOrder;
import mmb.poscenter.service.ShopSaledOrderEventService;

import com.opensymphony.xwork2.ActionSupport;

public class ShopSaledOrderEventAction extends ActionSupport{
	
	private static final long serialVersionUID = 1L;

	private Event event = new Event();
	
	private ShopSaledOrder shopSaledOrder = new ShopSaledOrder();
	
	private ShopSaledOrderEventService  ssoes = new ShopSaledOrderEventService();

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public ShopSaledOrder getShopSaledOrder() {
		return shopSaledOrder;
	}

	public void setShopSaledOrder(ShopSaledOrder shopSaledOrder) {
		this.shopSaledOrder = shopSaledOrder;
	}
	
	
	/**
	 *  店面订单活动展示页面
	 * @return
	 */
	public String toShopOrderDetailEventView(){
		  this.shopSaledOrder = ssoes.getShopSaledOrder(this.shopSaledOrder.saledOrderId, this.shopSaledOrder.shopId);
		  this.event = ssoes.getShopSaledOrderEvent(this.shopSaledOrder.saledOrderId, this.shopSaledOrder.shopId);
		return SUCCESS;
	}
	

}
