package mmb.poscenter.domain;

import mmb.poscenter.domain.Product;

public class ShopSaledOrderProduct {

	/**
	 * 销售订单商品id
	 */
	public int id;
	
	/**
	 * 商品id
	 */
	public int productId;
	
	/**
	 * 对应商品
	 */
	private Product product;
	
	/**
	 * 销售订单id
	 */
	public int saledOrderId;
	
	/**
	 * 购买数量
	 */
	public int count;
	
	/**
	 * 单价
	 */
	public double prePrice;
	
	/**
	 * 活动标记
	 */
	public String eventRemark;
	
	
	/**
	 * 订单类型
	 */
	public int orderType;

	/**
	 * 店面id
	 */
    public int shopId;
    
    /**
     * 店面编码
     */
    public String shopCode;


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
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

	public int getSaledOrderId() {
		return saledOrderId;
	}

	public void setSaledOrderId(int saledOrderId) {
		this.saledOrderId = saledOrderId;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public double getPrePrice() {
		return prePrice;
	}

	public void setPrePrice(double prePrice) {
		this.prePrice = prePrice;
	}


	public int getShopId() {
		return shopId;
	}


	public void setShopId(int shopId) {
		this.shopId = shopId;
	}


	public String getShopCode() {
		return shopCode;
	}


	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
	}


	public int getOrderType() {
		return orderType;
	}


	public void setOrderType(int orderType) {
		this.orderType = orderType;
	}


	public String getEventRemark() {
		return eventRemark;
	}


	public void setEventRemark(String eventRemark) {
		this.eventRemark = eventRemark;
	}
	
	
}
