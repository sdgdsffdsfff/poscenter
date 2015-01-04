package mmb.poscenter.domain;

import java.io.Serializable;

/**
 * 换购商品
 * <br/>赠品
 */
public class SwapBuyProduct implements Serializable{

	private static final long serialVersionUID = 1L;
	
	/**
	 * id
	 */
	public int id;
	
	/**
	 * 换购活动id
	 */
	public int swapBuyEventId;
	
	/**
	 * 赠送商品id
	 */
	public int giftProductId;
	
	/**
	 * 赠送商品名称【数据库不含此字段】
	 * @return
	 */
	public String giftProductName;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getSwapBuyEventId() {
		return swapBuyEventId;
	}

	public void setSwapBuyEventId(int swapBuyEventId) {
		this.swapBuyEventId = swapBuyEventId;
	}

	public int getGiftProductId() {
		return giftProductId;
	}

	public void setGiftProductId(int giftProductId) {
		this.giftProductId = giftProductId;
	}

	public String getGiftProductName() {
		return giftProductName;
	}

	public void setGiftProductName(String giftProductName) {
		this.giftProductName = giftProductName;
	}
	
}
