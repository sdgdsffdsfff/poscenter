package mmb.system.admin;

import mmboa.util.BinaryFlag;

/**
 * @author bomb
 * 
 */
public class UserPermissionBean {
	public int id;

	public int userId;

	public int securityLevel;

	public int permission;

	public int[] groups;

	public BinaryFlag flag;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPermission() {
		return permission;
	}

	public void setPermission(int permission) {
		this.permission = permission;
	}

	public int getSecurityLevel() {
		return securityLevel;
	}

	public void setSecurityLevel(int securityLevel) {
		this.securityLevel = securityLevel;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int[] getGroups() {
		return groups;
	}

	public void setGroups(int[] groups) {
		this.groups = groups;
	}

	public BinaryFlag getFlag() {
		return flag;
	}

	public void setFlag(BinaryFlag flag) {
		this.flag = flag;
	}

	public boolean isInFlag(int id) {
		return this.flag.get(id);
	}

	public boolean isInGroups(int id) {
		if (groups == null) {
			return false;
		}
		for (int i = 0; i < groups.length; i++) {
			if (groups[i] == id) {
				return true;
			}
		}
		return false;
	}

}
