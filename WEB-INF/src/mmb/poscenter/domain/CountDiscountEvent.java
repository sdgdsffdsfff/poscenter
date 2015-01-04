package mmb.poscenter.domain;

import java.io.Serializable;

/**
 * 数量折扣活动
 */
public class CountDiscountEvent implements Serializable{

	private static final long serialVersionUID = 1L;
	
	/**
	 * 数量折扣活动id
	 */
	public int id;
	
	/**
	 * 活动id
	 */
	public int eventId;
	
	/**
	 * 商品id
	 */
	public int productId;
	
	/**
	 * 商品
	 */
	public Product product;
	
	/**
	 * 商品数量
	 */
	public int productCount;
	
	/**
	 * 类型[1:阶梯折扣；2:固定折扣]
	 * <br>1:第二件起产生阶梯折扣
	 * <br>2:只要商品数量>=2时，全部享受一个折扣优惠
	 */
	public int type;
	
	/**
	 * 折扣
	 */
	public double discount;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getEventId() {
		return eventId;
	}

	public void setEventId(int eventId) {
		this.eventId = eventId;
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

	public int getProductCount() {
		return productCount;
	}

	public void setProductCount(int productCount) {
		this.productCount = productCount;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

}
