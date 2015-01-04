package mmb.poscenter.domain;

import java.io.Serializable;
import java.sql.Timestamp;

import mmb.poscenter.domain.Product;

/**
 * 店面租赁订单商品
 */
public class ShopLeaseOrderProduct implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 租赁订单商品id
	 */
	public int id;
	
	/**
	 * 商品id
	 */
	public int productId;
	
	/**
	 * 对应商品
	 */
	private Product product;
	
	/**
	 * 租赁订单id
	 */
	public int leaseOrderId;
	
	/**
	 * 购买数量
	 */
	public int count;
	
	/**
	 * 单价
	 */
	public double prePrice;
	
	/**
	 * 单押金
	 */
	public double perDeposit;
	
	/**
     * 租赁开始时间
     */
	public Timestamp startTime;
	
	/**
	 * 租赁结束时间
	 */
	public Timestamp endTime;
	
	/**
	 * 租赁时长
	 */
	public double timeLength;
	
	/**
	 * 租赁方式[0:按日租赁；1:包月租赁]
	 */
	public int leaseStyle;

	/**
	 * 店面编码
	 */
	public String shopCode;
	
	/**
	 *  店面id
	 */
	public int shopId;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public int getLeaseOrderId() {
		return leaseOrderId;
	}

	public void setLeaseOrderId(int leaseOrderId) {
		this.leaseOrderId = leaseOrderId;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public double getPrePrice() {
		return prePrice;
	}

	public void setPrePrice(double prePrice) {
		this.prePrice = prePrice;
	}

	public double getPerDeposit() {
		return perDeposit;
	}

	public void setPerDeposit(double perDeposit) {
		this.perDeposit = perDeposit;
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

	public double getTimeLength() {
		return timeLength;
	}

	public void setTimeLength(double timeLength) {
		this.timeLength = timeLength;
	}

	public int getLeaseStyle() {
		return leaseStyle;
	}

	public void setLeaseStyle(int leaseStyle) {
		this.leaseStyle = leaseStyle;
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
 
}
