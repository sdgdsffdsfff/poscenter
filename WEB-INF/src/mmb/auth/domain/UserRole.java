package mmb.auth.domain;

/**
 * 用户角色
 * @author qiuranke
 *
 */
public class UserRole {

	/**
	 * 用户id
	 */
	public int userId;
	
	/**
	 * 角色id
	 */
	public int roleId;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}
	
	
	
	
}
