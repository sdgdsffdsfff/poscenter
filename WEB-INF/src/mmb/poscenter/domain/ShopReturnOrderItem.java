package mmb.poscenter.domain;

import java.io.Serializable;

/**
 * 店面退货单条目
 */
public class ShopReturnOrderItem implements Serializable{

	private static final long serialVersionUID = 1L;

	/**
	 * 店面退货单条目id
	 */
	public int id;
	
	/**
	 * 所属店面退货单id
	 */
	public int shopReturnOrderId;
	
	/**
	 * 商品id
	 */
	public int productId;
	
	/**
	 * 对应商品
	 */
	public Product product;
	
	/**
	 * 退货数量
	 */
	public int returnCount;
	
	/**
	 * 实收数量
	 */
	public int receiveCount;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getShopReturnOrderId() {
		return shopReturnOrderId;
	}

	public void setShopReturnOrderId(int shopReturnOrderId) {
		this.shopReturnOrderId = shopReturnOrderId;
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

	public int getReturnCount() {
		return returnCount;
	}

	public void setReturnCount(int returnCount) {
		this.returnCount = returnCount;
	}

	public int getReceiveCount() {
		return receiveCount;
	}

	public void setReceiveCount(int receiveCount) {
		this.receiveCount = receiveCount;
	}

}
