package mmb.poscenter.domain;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 调价卡
 */
public class PriceCard implements Serializable {
	
	private static final long serialVersionUID = 1L;

	//调价卡卡号
	public String id;
	
	/**
	 * 类型[1:红卡，2:蓝卡]
	 */
	public int type;
	
	//店铺id
	public int shopId;
	
	//供应商id
	public int supplierId;
	
	//店员姓名
	public String clerkName;
	
	//开卡时间
	public Timestamp openTime;
	
	/**
	 * 使用状态[1:白卡；2:可使用；3:停用]
	 */
	public int state;
	
	/**
	 * 密码
	 */
	public String password;
	
	/**
	 * 剩余点数
	 */
	public double point;
	
	
	//店铺名称【数据库无此字段】
	private String shopName;
	
	//供应商名称【数据库无此字段】
	private String supplierName;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getShopId() {
		return shopId;
	}

	public void setShopId(int shopId) {
		this.shopId = shopId;
	}

	public int getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(int supplierId) {
		this.supplierId = supplierId;
	}

	public String getClerkName() {
		return clerkName;
	}

	public void setClerkName(String clerkName) {
		this.clerkName = clerkName;
	}

	public Timestamp getOpenTime() {
		return openTime;
	}

	public void setOpenTime(Timestamp openTime) {
		this.openTime = openTime;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public double getPoint() {
		return point;
	}

	public void setPoint(double point) {
		this.point = point;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

}
