package mmb.poscenter.domain;

import java.io.Serializable;

public class ShopMoneySource implements Serializable{

	private static final long serialVersionUID = -81819343752834389L;
	
	/**
	 * 金钱来源id
	 */
	public int id;
	
	/**
	 * 店面id
	 */
	public int shopId;
	
	/**
	 * 销售订单id
	 */
	public int orderId;
	
	/**
	 * 来源类型[1:现金；2:会员卡；3:银行卡；4:购物券]
	 */
	public int type;
	
	/**
	 * 金额
	 */
	public double money;
	
	/**
	 * 刷卡单号
	 */
	public String swipCardNumber;

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

	public String getSwipCardNumber() {
		return swipCardNumber;
	}

	public void setSwipCardNumber(String swipCardNumber) {
		this.swipCardNumber = swipCardNumber;
	}
	
}
