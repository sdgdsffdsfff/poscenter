package mmb.poscenter.domain;

import java.sql.Timestamp;

/**
 * 店面库存
 */
public class ShopStock {
	
	/**
	 * id
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
	 * 库存
	 */
	public int stock;
	
	/**
	 * 同步时间
	 */
	public Timestamp createTime;

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

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	
}
