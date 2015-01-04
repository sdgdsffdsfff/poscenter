package mmb.poscenter.domain;

import java.io.Serializable;

/**
 * 采购单条目
 */
public class PurchaseOrderItem implements Serializable{

	private static final long serialVersionUID = 1L;

	/**
	 * 采购单条目id
	 */
	public int id;
	
	/**
	 * 所属采购单id
	 */
	public int purchaseOrderId;
	
	/**
	 * 商品id
	 */
	public int productId;
	
	/**
	 * 对应商品
	 */
	public Product product;
	
	/**
	 * 供应商id
	 */
	public int supplierId;
	
	/**
	 * 对应供应商
	 */
	public Supplier supplier;
	
	/**
	 * 商品数量
	 */
	public int count;
	
	/**
	 * 采购价
	 */
	public double purchasePrice;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPurchaseOrderId() {
		return purchaseOrderId;
	}

	public void setPurchaseOrderId(int purchaseOrderId) {
		this.purchaseOrderId = purchaseOrderId;
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

	public int getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(int supplierId) {
		this.supplierId = supplierId;
	}

	public Supplier getSupplier() {
		return supplier;
	}

	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public double getPurchasePrice() {
		return purchasePrice;
	}

	public void setPurchasePrice(double purchasePrice) {
		this.purchasePrice = purchasePrice;
	}

}
