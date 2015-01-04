package mmb.auth.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 *   用户
 */
public class User implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 记录号
	 */
	public int id;
	
	/**
	 * 所属部门编号
	 */
	public int departmentId;
	
	/**
	 * 所属部门名称（数据库无此字段）
	 */
	public String departmentName;
	
	/**
	 * 用户名
	 */
	public String userName;
	
	/**
	 * 密码
	 */
	public String userPassword;
	
	/**
	 * 真实姓名
	 */
	public String userRealName;
	
	/**
	 * 使用状态[1:有效；2:禁用]
	 */
	public int useStatus;
	
	/**
	 * 创建时间
	 */
	public Timestamp createTime;
	
	/**
	 * 存放用户的所有权限
	 */
	private List<Resource> resourceList = new ArrayList<Resource>();
	
	/**
	 * 存放用户的所有菜单（树形）
	 */
	private List<Menu> menuList = new ArrayList<Menu>();
	
	/**
	 * 存放用户拥有的角色
	 */
	private List<Role> roleList = new ArrayList<Role>();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public String getUserRealName() {
		return userRealName;
	}

	public void setUserRealName(String userRealName) {
		this.userRealName = userRealName;
	}

	public int getUseStatus() {
		return useStatus;
	}

	public void setUseStatus(int useStatus) {
		this.useStatus = useStatus;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public int getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(int departmentId) {
		this.departmentId = departmentId;
	}

	public List<Resource> getResourceList() {
		return resourceList;
	}

	public void setResourceList(List<Resource> resourceList) {
		this.resourceList = resourceList;
	}

	public List<Menu> getMenuList() {
		return menuList;
	}

	public void setMenuList(List<Menu> menuList) {
		this.menuList = menuList;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public List<Role> getRoleList() {
		return roleList;
	}

	public void setRoleList(List<Role> roleList) {
		this.roleList = roleList;
	}
	
	
	
}
