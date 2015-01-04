package mmb.poscenter.domain;

import java.io.Serializable;

/**
 * 发货单条目
 */
public class SendOrderItem implements Serializable{

	private static final long serialVersionUID = 1L;

	/**
	 * 发货单条目id
	 */
	public int id;
	
	/**
	 * 所属发货单id
	 */
	public int sendOrderId;
	
	/**
	 * 商品id
	 */
	public int productId;
	
	/**
	 * 对应商品
	 */
	public Product product;
	
	/**
	 * 商品数量
	 */
	public int count;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getSendOrderId() {
		return sendOrderId;
	}

	public void setSendOrderId(int sendOrderId) {
		this.sendOrderId = sendOrderId;
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

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
	
}
