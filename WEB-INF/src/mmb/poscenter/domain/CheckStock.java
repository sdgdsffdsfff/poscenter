package mmb.poscenter.domain;

import java.sql.Timestamp;

/**
 * 盘点
 */
public class CheckStock {
	
	/**
	 * id
	 */
	public int id;
	
	/**
	 * 盘点总负责人
	 */
	public String charger;
	
	/**
	 * 盘点时间
	 */
	public Timestamp checkTime;
	
	/**
	 * 备注
	 */
	public String remark;
	
	/**
	 * 使用状态[0:未提交；1:已提交]
	 */
	public int useStatus;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCharger() {
		return charger;
	}

	public void setCharger(String charger) {
		this.charger = charger;
	}

	public Timestamp getCheckTime() {
		return checkTime;
	}

	public void setCheckTime(Timestamp checkTime) {
		this.checkTime = checkTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public int getUseStatus() {
		return useStatus;
	}

	public void setUseStatus(int useStatus) {
		this.useStatus = useStatus;
	}
	
}
