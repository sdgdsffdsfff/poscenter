package mmb.poscenter.domain;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 店面进销存
 */
public class ShopInvoice implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 记录序号
	 */
	public int id;
	
	/**
	 * 店面id
	 */
	public int shopId;
	
	/**
	 * 对应店面
	 */
	public Shop shop;
	
	/**
	 * 商品id
	 */
	public int productId;
	
	/**
	 * 对应商品
	 */
	public Product product;
	
	/**
	 * 流水编码
	 */
	public String serialNumber;
	
	/**
	 * 操作类型
	 */
	public String operType;
	
	/**
	 * 进销前数据量
	 */
	public int beforeCount;
	
	/**
	 * 这次进销的数据量
	 */
	public int count;
	
	/**
	 * 进销后数据量
	 */
	public int afterCount;
	
	/**
	 * 操作人用户名
	 */
	public String operUser;
	
	/**
	 * 操作时间
	 */
	public Timestamp createTime;
	
	/**
	 * 备注
	 */
	public String remark;
	
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

	public Shop getShop() {
		return shop;
	}

	public void setShop(Shop shop) {
		this.shop = shop;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}
	
	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getOperType() {
		return operType;
	}

	public void setOperType(String operType) {
		this.operType = operType;
	}

	public int getBeforeCount() {
		return beforeCount;
	}

	public void setBeforeCount(int beforeCount) {
		this.beforeCount = beforeCount;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getAfterCount() {
		return afterCount;
	}

	public void setAfterCount(int afterCount) {
		this.afterCount = afterCount;
	}

	public String getOperUser() {
		return operUser;
	}

	public void setOperUser(String operUser) {
		this.operUser = operUser;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
