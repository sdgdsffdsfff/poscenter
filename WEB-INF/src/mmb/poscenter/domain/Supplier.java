package mmb.poscenter.domain;

import java.io.Serializable;

/**
 * 供应商
 */
public class Supplier implements Serializable {
	
	private static final long serialVersionUID = 1L;

	public int id;
	
	/**
	 * 供应商名称
	 */
	public String name;
	
	/**
	 * 联系电话
	 */
	public String phone;
	
	/**
	 * 是否删除[0-未删除  1-已删除]
	 */
	public int isDelete;

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

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public int getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(int isDelete) {
		this.isDelete = isDelete;
	}

}
