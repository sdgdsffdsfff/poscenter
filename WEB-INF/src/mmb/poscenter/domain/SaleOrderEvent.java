package mmb.poscenter.domain;

import java.io.Serializable;

/**
 * 销售订单活动表
 */
public class SaleOrderEvent implements Serializable {
	
	private static final long serialVersionUID = 1L;

	/**
	 *主键id
	 */
	public int id;
	
	/**
	 * 销售订单id
	 */
	public int saleOrderId;
	
	/**
	 * 活动id
	 */
	public int eventId;
	
	/**
	 * 详细活动id
	 */
	public int detailEventId;
	
	/**
	 * 附件品赠品信息id
	 */
	public int extId;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getSaleOrderId() {
		return saleOrderId;
	}

	public void setSaleOrderId(int saleOrderId) {
		this.saleOrderId = saleOrderId;
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
	
}
