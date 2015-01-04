package mmb.poscenter.domain;

import java.io.Serializable;

/**
 * 收货单条目
 */
public class ReceiveOrderItem implements Serializable{

	private static final long serialVersionUID = 1L;

	/**
	 * 收货单id
	 */
	public int id;
	
	/**
	 * 收货单号
	 */
	public int orderId;
	
	/**
	 * 商品id
	 */
	public int productId;
	
	/**
	 * 商品编码
	 */
	public String productCode;
	
	/**
	 * 商品名称
	 */
	public String productName;
	
	/**
	 * 供应商id
	 */
	public int supplierId;
	
	/**
	 * 供应商名称
	 */
	public String supplierName;
	
	/**
	 * 实收商品数量
	 */
	public int receiveCount;
	
	/**
	 * 发货商品数量
	 * @return
	 */
	public int sendCount;
	

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

	public int getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(int supplierId) {
		this.supplierId = supplierId;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public int getReceiveCount() {
		return receiveCount;
	}

	public void setReceiveCount(int receiveCount) {
		this.receiveCount = receiveCount;
	}

	public int getSendCount() {
		return sendCount;
	}

	public void setSendCount(int sendCount) {
		this.sendCount = sendCount;
	}
	
	
	
}
