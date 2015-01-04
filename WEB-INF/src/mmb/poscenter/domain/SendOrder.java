package mmb.poscenter.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * 发货单
 */
public class SendOrder implements Serializable{

	private static final long serialVersionUID = 1L;
	
	/**
	 * 发货单id
	 */
	public int id;

	/**
	 * 发货单号
	 */
	public String orderNumber;
	
	/**
	 * 店面id
	 */
	public int shopId;
	
	/**
	 * 店面
	 */
	public Shop shop;
	
	/**
	 * 负责人
	 */
	public String charger;
	
    /**
     * 下单时间
     */
	public Timestamp createTime;
	
	/**
	 * 收货单位
	 */
	public String department;
	
	/**
	 * 使用状态[0:未提交；1:已提交；2:已归档]
	 */
	public int useStatus = -1;
	
	private List<SendOrderItem> itemList = new ArrayList<SendOrderItem>();

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
	
	public int getShopId() {
		return shopId;
	}

	public void setShopId(int shopId) {
		this.shopId = shopId;
	}

	public Shop getShop() {
		return shop;
	}

	public void setShop(Shop shop) {
		this.shop = shop;
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

	public List<SendOrderItem> getItemList() {
		return itemList;
	}

	public void setItemList(List<SendOrderItem> itemList) {
		this.itemList = itemList;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}
}
