package mmb.auth.domain;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 部门
 */
public class Department implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	public int id;
	
	/**
	 * 公司id
	 */
	public int companyId;
	
	/**
	 * 所属公司
	 */
	public Company company;
	
	/**
	 * 部门名称
	 */
	public String name;
	
	/**
	 * 备注
	 */
	public String remark;
	
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

	public int getCompanyId() {
		return companyId;
	}

	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

}
