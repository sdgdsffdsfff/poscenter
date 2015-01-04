package mmb.poscenter.domain;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author likaige
 * 店面
 */
public class Shop implements Serializable {
	
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	public int id;
	
	/**
	 * 店面编号
	 */
	public String code;
	
	/**
	 * 店面名称
	 */
	public String name;
	
	/**
	 * 店面地址
	 */
	public String address;
	
	/**
	 * 负责人
	 */
	public String charger;
	
	/**
	 * 创建时间
	 */
	public Timestamp createTime;
	
	/**
	 * 店面店路由ip地址
	 */
	public String ipAddress;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCharger() {
		return charger;
	}

	public void setCharger(String charger) {
		this.charger = charger;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	

}
