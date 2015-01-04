package mmb.poscenter.domain;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import mmb.poscenter.domain.Member;
import mmb.poscenter.domain.ShopSaledOrderProduct;

public class ShopSaledOrder {
	
	/**
	 * 销售订单id
	 */
	public int id;
	
	/**
	 * 店面店销售订单id
	 */
	public int saledOrderId;
	
	/**
	 * 订单流水号
	 */
	public String serialNumber;
	
	/**
	 * pos机编号
	 */
	public String posCode;
	
	/**
	 * 收银员id
	 */
	public int cashierId;
	
	/**
	 * 会员id
	 */
	public String memberId;
	
	/**
	 * 所属会员
	 */
	private Member member;
	
	/**
	 * 订单总金额
	 */
	public double price;
	
	/**
	 * 销售时间
	 */
	public Timestamp saledTime;

	
	/**
	 * 订单类型[0:购买订单；1:退货订单]
	 */
	public int orderType;
	
	/**
	 * 购买商品时用户缴纳的现金
	 * <br/>该属性无数据库对应字段，只是用来传递数据
	 */
	private double cash;
	
	/**
	 * 订单商品列表数据
	 */
	private List<ShopSaledOrderProduct> shopSaledOrderProduct = new ArrayList<ShopSaledOrderProduct>();
	
	/**
	 * 订单收银来源列表数据
	 */
	private List<ShopMoneySource> shopMoneySource = new ArrayList<ShopMoneySource>();
	
	/**
	 * 退货订单退款去向列表
	 */
	private List<ShopMoneyDestination> shopMoneyDestinationList = new ArrayList<ShopMoneyDestination>();
	
	/**
	 * 店面销售订单活动列表
	 */
	private List<ShopSaledOrderEvent> shopSaledOrderEventList = new ArrayList<ShopSaledOrderEvent>();
	
	/**
	 * 店面id
	 */
    public int shopId;
    
    /**
     * 店面名称
     */
    public String shopName;
    
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
	
	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	
	public String getPosCode() {
		return posCode;
	}

	public void setPosCode(String posCode) {
		this.posCode = posCode;
	}

	public int getCashierId() {
		return cashierId;
	}

	public void setCashierId(int cashierId) {
		this.cashierId = cashierId;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	
	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public Timestamp getSaledTime() {
		return saledTime;
	}

	public void setSaledTime(Timestamp saledTime) {
		this.saledTime = saledTime;
	}

	public double getCash() {
		return cash;
	}

	public void setCash(double cash) {
		this.cash = cash;
	}

	public int getOrderType() {
		return orderType;
	}

	public void setOrderType(int orderType) {
		this.orderType = orderType;
	}



	public List<ShopSaledOrderProduct> getShopSaledOrderProduct() {
		return shopSaledOrderProduct;
	}

	public void setShopSaledOrderProduct(
			List<ShopSaledOrderProduct> shopSaledOrderProduct) {
		this.shopSaledOrderProduct = shopSaledOrderProduct;
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

	public int getSaledOrderId() {
		return saledOrderId;
	}

	public void setSaledOrderId(int saledOrderId) {
		this.saledOrderId = saledOrderId;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public List<ShopMoneySource> getShopMoneySource() {
		return shopMoneySource;
	}

	public void setShopMoneySource(List<ShopMoneySource> shopMoneySource) {
		this.shopMoneySource = shopMoneySource;
	}

	public List<ShopMoneyDestination> getShopMoneyDestinationList() {
		return shopMoneyDestinationList;
	}

	public void setShopMoneyDestinationList(
			List<ShopMoneyDestination> shopMoneyDestinationList) {
		this.shopMoneyDestinationList = shopMoneyDestinationList;
	}

	public List<ShopSaledOrderEvent> getShopSaledOrderEventList() {
		return shopSaledOrderEventList;
	}

	public void setShopSaledOrderEventList(
			List<ShopSaledOrderEvent> shopSaledOrderEventList) {
		this.shopSaledOrderEventList = shopSaledOrderEventList;
	}
	
	
}
