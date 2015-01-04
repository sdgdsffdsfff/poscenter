package mmb.poscenter.domain;

import java.sql.Timestamp;

/**
 * 盘点条目
 */
public class CheckStockItem {
	
	/**
	 * id
	 */
	public int id;
	
	/**
	 * 所属盘点id
	 */
	public int checkStockId;
	
	/**
	 * 商品id
	 */
	public int productId;
	
	/**
	 * 对应商品
	 */
	public Product product;
	
	/**
	 * 盘点负责人
	 */
	public String charger;
	
	/**
	 * 进价
	 */
	public double purchasePrice;
	
	/**
	 *售价
	 */
	public double salePrice;
	
	/**
	 * 盘点数量
	 */
	public int count;
	
	/**
	 * 电脑库存
	 */
	public int stock;
	
	/**
	 * 盘点时间
	 */
	public Timestamp checkTime;
	
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
	
	public int getCheckStockId() {
		return checkStockId;
	}

	public void setCheckStockId(int checkStockId) {
		this.checkStockId = checkStockId;
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
	
	public String getCharger() {
		return charger;
	}

	public void setCharger(String charger) {
		this.charger = charger;
	}

	public double getPurchasePrice() {
		return purchasePrice;
	}

	public void setPurchasePrice(double purchasePrice) {
		this.purchasePrice = purchasePrice;
	}

	public double getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(double salePrice) {
		this.salePrice = salePrice;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
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

}
