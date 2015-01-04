package mmb.poscenter.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


/**
 * 店面租赁订单
 */
public class ShopLeaseOrder implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 租赁订单id
	 */
	public int id;
	
	/**
	 * 店面端租赁订单id
	 */
	public int leaseOrderId;
	
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
	 * 订单总押金
	 */
	public double deposit;
	
	/**
	 * 订单类型[0:租赁订单；1:还租订单]
	 */
	public int orderType;
	
	/**
     * 创建时间
     */
	public Timestamp createTime;
	
	/**
	 * 租赁商品时用户缴纳的现金
	 * <br/>该属性无数据库对应字段，只是用来传递数据
	 */
	private double cash;
	
	/**
	 * 订单商品列表数据
	 */
	private List<ShopLeaseOrderProduct> shopLeaseOrderProduct = new ArrayList<ShopLeaseOrderProduct>();
	
	/**
	 * 店面编码
	 */
	private String shopCode;
	
	/**
	 * 店面店id
	 */
	private int shopId;
	
	/**
	 * 店面名称
	 */
	private String shopName;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getLeaseOrderId() {
		return leaseOrderId;
	}

	public void setLeaseOrderId(int leaseOrderId) {
		this.leaseOrderId = leaseOrderId;
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

	public double getDeposit() {
		return deposit;
	}

	public void setDeposit(double deposit) {
		this.deposit = deposit;
	}

	public int getOrderType() {
		return orderType;
	}

	public void setOrderType(int orderType) {
		this.orderType = orderType;
	}
	
	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public double getCash() {
		return cash;
	}

	public void setCash(double cash) {
		this.cash = cash;
	}

	public List<ShopLeaseOrderProduct> getShopLeaseOrderProduct() {
		return shopLeaseOrderProduct;
	}

	public void setShopLeaseOrderProduct(
			List<ShopLeaseOrderProduct> shopLeaseOrderProduct) {
		this.shopLeaseOrderProduct = shopLeaseOrderProduct;
	}

	public String getShopCode() {
		return shopCode;
	}

	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
	}

	public int getShopId() {
		return shopId;
	}

	public void setShopId(int shopId) {
		this.shopId = shopId;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

}
