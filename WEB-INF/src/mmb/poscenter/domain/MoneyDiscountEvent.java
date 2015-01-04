package mmb.poscenter.domain;

import java.io.Serializable;

/**
 * 金额折扣活动
 */
public class MoneyDiscountEvent implements Serializable{

	private static final long serialVersionUID = 1L;
	
	/**
	 * 金额折扣活动id
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
	 * 类型[1:减钱；2:折扣]
	 */
	public int type;
	
	/**
	 * 数值
	 */
	public double numberValue;

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

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public double getNumberValue() {
		return numberValue;
	}

	public void setNumberValue(double numberValue) {
		this.numberValue = numberValue;
	}

}
