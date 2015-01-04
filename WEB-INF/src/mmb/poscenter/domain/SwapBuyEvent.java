package mmb.poscenter.domain;

import java.io.Serializable;
import java.util.List;

/**
 * 换购活动
 */
public class SwapBuyEvent implements Serializable{

	private static final long serialVersionUID = 1L;
	
	/**
	 * 换购活动id
	 */
	public int id;
	
	/**
	 * 活动id
	 */
	public int eventId;
	
	/**
	 * 金额
	 */
	public double money;
	
	/**
	 * 添加金额
	 */
	public double appendMoney;
	
	/**
	 * 换购商品
	 */
	public List<SwapBuyProduct> swapBuyProductList;
	
	/**
	 * 所属活动
	 */
	public Event event;
	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getEventId() {
		return eventId;
	}

	public void setEventId(int eventId) {
		this.eventId = eventId;
	}

	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}

	public double getAppendMoney() {
		return appendMoney;
	}

	public void setAppendMoney(double appendMoney) {
		this.appendMoney = appendMoney;
	}
    
	public List<SwapBuyProduct> getSwapBuyProductList() {
		return swapBuyProductList;
	}

	public void setSwapBuyProductList(List<SwapBuyProduct> swapBuyProductList) {
		this.swapBuyProductList = swapBuyProductList;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}
	
	
}
