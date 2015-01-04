package mmb.poscenter.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 买赠活动
 */
public class BuyGiftEvent implements Serializable{

	private static final long serialVersionUID = 1L;
	
	/**
	 * 买赠活动id
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
	 * 人数限制[0:表示没有人数限制]
	 */
	public int userLimit;
	
	/**
	 * 赠品列表
	 */
	private List<BuyGiftProduct> giftList = new ArrayList<BuyGiftProduct>();

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

	public int getUserLimit() {
		return userLimit;
	}

	public void setUserLimit(int userLimit) {
		this.userLimit = userLimit;
	}

	public List<BuyGiftProduct> getGiftList() {
		return giftList;
	}

	public void setGiftList(List<BuyGiftProduct> giftList) {
		this.giftList = giftList;
	}
	
}
