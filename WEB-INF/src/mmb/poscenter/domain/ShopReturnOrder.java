package mmb.poscenter.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * 店面退货单
 */
public class ShopReturnOrder implements Serializable{

	private static final long serialVersionUID = 1L;
	
	/**
	 * 退货单id
	 */
	public int id;

	/**
	 * 退货单号
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
     * 创建时间
     */
	public Timestamp createTime;
	
	/**
	 * 使用状态[0:未确认；1:已确认]
	 */
	public int useStatus = -1;
	
	/**
	 * 条目
	 */
	private List<ShopReturnOrderItem> itemList = new ArrayList<ShopReturnOrderItem>();

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

	public List<ShopReturnOrderItem> getItemList() {
		return itemList;
	}

	public void setItemList(List<ShopReturnOrderItem> itemList) {
		this.itemList = itemList;
	}

}
