package mmb.auth.domain;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * 角色
 * @author qiuranke
 *
 */
public class Role {
	
	/**
	 * 记录号
	 */
	public int id;
	
	/**
	 * 角色名称
	 */
	public String roleName;
	
	/**
	 * 角色中文名称
	 */
	public String roleChName;
	
	/**
	 * 角色描述
	 */
	public String roleDesc;
	
	/**
	 * 创建时间
	 */
	public Timestamp createTime;
	
	/**
	 * 资源列表
	 */
	public List<Resource> resourceList = new ArrayList<Resource>();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleChName() {
		return roleChName;
	}

	public void setRoleChName(String roleChName) {
		this.roleChName = roleChName;
	}

	public String getRoleDesc() {
		return roleDesc;
	}

	public void setRoleDesc(String roleDesc) {
		this.roleDesc = roleDesc;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public List<Resource> getResourceList() {
		return resourceList;
	}

	public void setResourceList(List<Resource> resourceList) {
		this.resourceList = resourceList;
	}
	
	

}
