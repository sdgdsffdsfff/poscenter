package mmb.poscenter.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * 活动
 */
public class Event implements Serializable{

	private static final long serialVersionUID = 1L;
	
	/**
	 * 活动id
	 */
	public int id;
	
	/**
	 * 活动类型[1:买赠；2:换购；3:金额折扣；4:数量折扣；5:套餐]
	 */
	public int type;
	
	/**
     * 开始时间
     */
	public Timestamp startTime;
	
	/**
	 * 结束时间
	 */
	public Timestamp endTime;

	/**
	 * 规则说明
	 */
	public String ruleDesc;
	
	/**
	 * 使用状态[1:未生效；2:已生效；3:已过期]
	 */
	public int useStatus;
	
	/**
	 * 适用的店面
	 */
	private List<Shop> shopList = new ArrayList<Shop>();
	
	/**
	 * 买赠活动列表
	 */
	private List<BuyGiftEvent> buyGiftEventList = new ArrayList<BuyGiftEvent>();
	
	/**
	 * 换购活动列表
	 */
	private List<SwapBuyEvent> swapBuyEventList = new ArrayList<SwapBuyEvent>();
	
	/**
	 * 套餐活动列表
	 */
	private List<ComboEvent> comboEventList = new ArrayList<ComboEvent>();
	
	/**
	 * 数量折扣活动列表
	 */
	private List<CountDiscountEvent> countDiscountEventList = new ArrayList<CountDiscountEvent>();
	
	/**
	 * 金额折扣活动列表
	 */
	private List<MoneyDiscountEvent> moneyDiscountEventList = new ArrayList<MoneyDiscountEvent>();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Timestamp getStartTime() {
		return startTime;
	}

	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}

	public Timestamp getEndTime() {
		return endTime;
	}

	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}

	public String getRuleDesc() {
		return ruleDesc;
	}

	public void setRuleDesc(String ruleDesc) {
		this.ruleDesc = ruleDesc;
	}

	public int getUseStatus() {
		return useStatus;
	}

	public void setUseStatus(int useStatus) {
		this.useStatus = useStatus;
	}

	public List<Shop> getShopList() {
		return shopList;
	}

	public void setShopList(List<Shop> shopList) {
		this.shopList = shopList;
	}

	public List<BuyGiftEvent> getBuyGiftEventList() {
		return buyGiftEventList;
	}

	public void setBuyGiftEventList(List<BuyGiftEvent> buyGiftEventList) {
		this.buyGiftEventList = buyGiftEventList;
	}
	
	public List<SwapBuyEvent> getSwapBuyEventList() {
		return swapBuyEventList;
	}

	public void setSwapBuyEventList(List<SwapBuyEvent> swapBuyEventList) {
		this.swapBuyEventList = swapBuyEventList;
	}

	public List<ComboEvent> getComboEventList() {
		return comboEventList;
	}

	public void setComboEventList(List<ComboEvent> comboEventList) {
		this.comboEventList = comboEventList;
	}

	public List<CountDiscountEvent> getCountDiscountEventList() {
		return countDiscountEventList;
	}

	public void setCountDiscountEventList(
			List<CountDiscountEvent> countDiscountEventList) {
		this.countDiscountEventList = countDiscountEventList;
	}

	public List<MoneyDiscountEvent> getMoneyDiscountEventList() {
		return moneyDiscountEventList;
	}

	public void setMoneyDiscountEventList(
			List<MoneyDiscountEvent> moneyDiscountEventList) {
		this.moneyDiscountEventList = moneyDiscountEventList;
	}
	
}
