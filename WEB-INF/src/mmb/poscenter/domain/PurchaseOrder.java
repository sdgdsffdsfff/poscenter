package mmb.poscenter.domain;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 采购单
 */
public class PurchaseOrder implements Serializable{

	private static final long serialVersionUID = 1L;
	
	/**
	 * 采购单id
	 */
	public int id;

	/**
	 * 采购单号
	 */
	public String orderNumber;
	
	/**
	 * 负责人
	 */
	public String charger;
	
	/**
	 * 采购单位
	 */
	public String department;
	
    /**
     * 下单时间
     */
	public Timestamp createTime;
	
	/**
	 * 使用状态[0:未提交；1:已提交；2:已归档]
	 */
	public int useStatus = -1;

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

	public String getCharger() {
		return charger;
	}

	public void setCharger(String charger) {
		this.charger = charger;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public int getUseStatus() {
		return useStatus;
	}

	public void setUseStatus(int useStatus) {
		this.useStatus = useStatus;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}
	
	
}
