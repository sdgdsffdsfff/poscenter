package mmb.poscenter.domain;

import java.io.Serializable;

/**
 * 金钱去向表
 */
public class ShopMoneyDestination implements Serializable{

	private static final long serialVersionUID = 1L;
	
	/**
	 * 主键
	 */
	public int id;
	
	/**
	 * 店面id
	 */
	public int shopId;
	
	/**
	 * 退货订单id
	 */
	public int orderId;
	
	/**
	 * 去向类型[1:现金；2:会员卡；3:银行卡]
	 */
	public int type;
	
	/**
	 * 金额
	 */
	public double money;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getShopId() {
		return shopId;
	}

	public void setShopId(int shopId) {
		this.shopId = shopId;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}
	
}
