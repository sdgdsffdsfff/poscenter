package mmb.poscenter.domain;

import java.io.Serializable;

/**
 * 套餐商品
 */
public class ComboProduct implements Serializable{

	private static final long serialVersionUID = 1L;
	
	/**
	 * id
	 */
	public int id;
	
	/**
	 * 套餐活动id
	 */
	public int comboEventId;
	
	/**
	 * 商品id
	 */
	public int productId;
	
	/**
	 * 商品
	 */
	public Product product;
	
	/**
	 * 数量
	 */
	public int productCount;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getComboEventId() {
		return comboEventId;
	}

	public void setComboEventId(int comboEventId) {
		this.comboEventId = comboEventId;
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
	
}
