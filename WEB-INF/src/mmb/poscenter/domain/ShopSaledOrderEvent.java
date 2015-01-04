package mmb.poscenter.domain;

import java.io.Serializable;

public class ShopSaledOrderEvent implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1298959417534644258L;

	/**
	 *主键id
	 */
	public int id ;
	
	/**
	 * 店面销售活动表的主键id
	 */
	public int shopEventId;
	
	/**
	 * 销售订单id
	 * 
	 */
	public int saledOrderId;
	
	/**
	 * 活动id
	 */
	public int eventId;
	
	/**
	 * 活动类型id 【展示用 数据库无此字段】
	 */
	public int eventType;
	
	/**
	 * 详细活动id
	 */
	public int detailEventId;
	
	/**
	 * 附件品赠品信息id
	 */
	public int extId;
	
	/**
	 * 店面店id
	 */
	public int shopId;
	
	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	

	public int getSaledOrderId() {
		return saledOrderId;
	}

	public void setSaledOrderId(int saledOrderId) {
		this.saledOrderId = saledOrderId;
	}

	public int getEventId() {
		return eventId;
	}

	public void setEventId(int eventId) {
		this.eventId = eventId;
	}

	public int getDetailEventId() {
		return detailEventId;
	}

	public void setDetailEventId(int detailEventId) {
		this.detailEventId = detailEventId;
	}

	public int getExtId() {
		return extId;
	}

	public void setExtId(int extId) {
		this.extId = extId;
	}

	public int getEventType() {
		return eventType;
	}

	public void setEventType(int eventType) {
		this.eventType = eventType;
	}

	public int getShopEventId() {
		return shopEventId;
	}

	public void setShopEventId(int shopEventId) {
		this.shopEventId = shopEventId;
	}

	public int getShopId() {
		return shopId;
	}

	public void setShopId(int shopId) {
		this.shopId = shopId;
	}
	
	

}
