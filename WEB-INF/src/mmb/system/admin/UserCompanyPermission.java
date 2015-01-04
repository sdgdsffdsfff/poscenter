package mmb.system.admin;

import mmboa.util.BinaryFlag;

public class UserCompanyPermission {

	int id;
	int userGroupId;
	int companyId;
	BinaryFlag flags = new BinaryFlag();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserGroupId() {
		return userGroupId;
	}

	public void setUserGroupId(int userGroupId) {
		this.userGroupId = userGroupId;
	}

	public int getCompanyId() {
		return companyId;
	}

	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}

	public BinaryFlag getFlags() {
		return flags;
	}

	public void setFlags(BinaryFlag flags) {
		this.flags = flags;
	}

	public boolean isInFlag(int f) {
		return flags.get(f);
	}
}
