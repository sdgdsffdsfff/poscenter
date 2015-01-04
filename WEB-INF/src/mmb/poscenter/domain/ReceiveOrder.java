package mmb.poscenter.domain;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 收货单
 */
public class ReceiveOrder implements Serializable{

	private static final long serialVersionUID = 1L;
	
	/**
	 * 收货单id
	 */
	public int id;
	
	/**
	 * 收货单号
	 */
	public String orderNumber;
	
	/**
	 * 收货负责人
	 */
	public String charger;
	
    /**
     * 收货时间
     */
	public Timestamp createTime;
	
	/**
	 * 采购单号
	 */
	public int purchaseId;
	
	/**
	 * 采购单编码【数据库无此字段】
	 */
	public String purchaseCode;
    
	/**
	 * 是否确认
	 */
	public int isRequired = -1;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public String getCharger() {
		return charger;
	}

	public void setCharger(String charger) {
		this.charger = charger;
	}

	public int getPurchaseId() {
		return purchaseId;
	}

	public void setPurchaseId(int purchaseId) {
		this.purchaseId = purchaseId;
	}

	public String getPurchaseCode() {
		return purchaseCode;
	}

	public void setPurchaseCode(String purchaseCode) {
		this.purchaseCode = purchaseCode;
	}

	public int getIsRequired() {
		return isRequired;
	}

	public void setIsRequired(int isRequired) {
		this.isRequired = isRequired;
	}

   
    
	
	
}
