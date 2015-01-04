package mmb.poscenter.domain;

import java.io.Serializable;

/**
 * 买赠商品
 * <br/>赠品
 */
public class BuyGiftProduct implements Serializable{

	private static final long serialVersionUID = 1L;
	
	/**
	 * id
	 */
	public int id;
	
	/**
	 * 买赠活动id
	 */
	public int buyGiftEventId;
	
	/**
	 * 赠送商品id
	 */
	public int giftProductId;
	
	/**
	 * 赠送商品
	 */
	public Product giftProduct;
	
	/**
	 * 最大赠送数量
	 */
	public int maxGiftCount;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getBuyGiftEventId() {
		return buyGiftEventId;
	}

	public void setBuyGiftEventId(int buyGiftEventId) {
		this.buyGiftEventId = buyGiftEventId;
	}

	public int getGiftProductId() {
		return giftProductId;
	}

	public void setGiftProductId(int giftProductId) {
		this.giftProductId = giftProductId;
	}
	
	public Product getGiftProduct() {
		return giftProduct;
	}

	public void setGiftProduct(Product giftProduct) {
		this.giftProduct = giftProduct;
	}

	public int getMaxGiftCount() {
		return maxGiftCount;
	}

	public void setMaxGiftCount(int maxGiftCount) {
		this.maxGiftCount = maxGiftCount;
	}
	
}
