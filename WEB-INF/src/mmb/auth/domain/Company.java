package mmb.auth.domain;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 公司
 */
public class Company implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	public int id;
	
	/**
	 * 公司名称
	 */
	public String name;
	
	/**
	 * 联系电话
	 */
	public String telephone;
	
	/**
	 * 创建时间
	 */
	public Timestamp createTime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	
}
