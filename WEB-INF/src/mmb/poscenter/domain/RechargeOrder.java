package mmb.poscenter.domain;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 充值单
 * @author likaige
 */
public class RechargeOrder implements Serializable {
	
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	public int id;
	
	/**
	 * 调价卡id
	 */
	public String priceCardId;
	
	/**
	 * 调价卡
	 */
	private PriceCard priceCard;
	
	/**
	 * 充值点数
	 */
	public double point;
	
	/**
	 * 审核状态[1:待审核；2:审核通过；3:审核未通过]
	 */
	public int auditStatus;
	
	/**
	 * 使用状态[1:未提交；2:已提交]
	 */
	public int useStatus;
	
	/**
     * 创建时间
     */
	public Timestamp createTime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPriceCardId() {
		return priceCardId;
	}

	public void setPriceCardId(String priceCardId) {
		this.priceCardId = priceCardId;
	}
	
	public PriceCard getPriceCard() {
		return priceCard;
	}

	public void setPriceCard(PriceCard priceCard) {
		this.priceCard = priceCard;
	}

	public double getPoint() {
		return point;
	}

	public void setPoint(double point) {
		this.point = point;
	}

	public int getAuditStatus() {
		return auditStatus;
	}

	public void setAuditStatus(int auditStatus) {
		this.auditStatus = auditStatus;
	}

	public int getUseStatus() {
		return useStatus;
	}

	public void setUseStatus(int useStatus) {
		this.useStatus = useStatus;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	
}
