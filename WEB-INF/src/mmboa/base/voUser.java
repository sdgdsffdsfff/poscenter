/**
 * 
 */
package mmboa.base;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import mmb.framework.PermissionFrk;
import mmb.system.admin.UserCompanyPermission;
import mmb.system.admin.UserGroupBean;
import mmboa.util.BinaryFlag;

/**
 * @author Bomb
 * 
 */
public class voUser implements Serializable {
	public static DecimalFormat codeFormat = new DecimalFormat("000000");
	public int id;

	public String username;

	public String password;

	public String code; // 员工编号
	public String departments; // 部门，多个则逗号间隔

	public int flag; // 管理员则为1

	public Timestamp createDatetime;

	public String name;

	public String phone;
	public String email;

	public String address;

	public String postcode;

	public String nick;

	public int gender;// 性别

	public int staffId;// 员工id

	/**
	 * 安全等级： <br/>
	 * 10: 超级管理员 <br/>
	 * 9: 高级管理员 <br/>
	 * 5: 普通管理员 <br/>
	 */
	public int securityLevel;

	/**
	 * 权限： <br/>
	 * 10: 超级管理员 <br/>
	 * 9: 高级管理员 <br/>
	 * 8: 平台运维部 <br/>
	 * 7: 销售部 <br/>
	 * 6： 商品采购部 <br/>
	 * 5: 推广部 <br/>
	 * 4: 运营中心 <br/>
	 * 3：客服部 <br/>
	 * 2：物流库存部
	 */
	public int permission;

	UserGroupBean group; // 三个组的组合权限，权限以外的参数为groupId对应的主组
	List companyPermList;

	// public UserInfoBean userInfo;
	//
	// /**
	// * @return Returns the userInfo.
	// */
	// public UserInfoBean getUserInfo() {
	// return userInfo;
	// }
	//
	// /**
	// * @param userInfo
	// * The userInfo to set.
	// */
	// public void setUserInfo(UserInfoBean userInfo) {
	// this.userInfo = userInfo;
	// }

	/**
	 * @return Returns the address.
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address
	 *            The address to set.
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	public Timestamp getCreateDatetime() {
		return createDatetime;
	}

	public void setCreateDatetime(Timestamp createDatetime) {
		this.createDatetime = createDatetime;
	}

	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            The name to set.
	 */
	public void setName(String name) {
		if (name == null) {
			this.name = "";
		} else {
			this.name = name;
		}
	}

	/**
	 * @return Returns the nick.
	 */
	public String getNick() {
		return nick;
	}

	/**
	 * @param nick
	 *            The nick to set.
	 */
	public void setNick(String nick) {
		if (nick == null) {
			this.nick = "";
		} else {
			this.nick = nick;
		}
	}

	/**
	 * @return Returns the phone.
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * @param phone
	 *            The phone to set.
	 */
	public void setPhone(String phone) {
		if (phone == null) {
			this.phone = "";
		} else {
			this.phone = phone;
		}
	}

	/**
	 * @return Returns the postcode.
	 */
	public String getPostcode() {
		return postcode;
	}

	/**
	 * @param postcode
	 *            The postcode to set.
	 */
	public void setPostcode(String postcode) {
		if (postcode == null) {
			this.postcode = "";
		} else {
			this.postcode = postcode;
		}
	}

	/**
	 * @return Returns the flag.
	 */
	public int getFlag() {
		return flag;
	}

	/**
	 * @param flag
	 *            The flag to set.
	 */
	public void setFlag(int flag) {
		this.flag = flag;
	}

	/**
	 * @return Returns the id.
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            The id to set.
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return Returns the password.
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            The password to set.
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return Returns the username.
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username
	 *            The username to set.
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	public int getSecurityLevel() {
		return securityLevel;
	}

	public void setSecurityLevel(int securityLevel) {
		this.securityLevel = securityLevel;
	}

	public int getPermission() {
		return permission;
	}

	public void setPermission(int permission) {
		this.permission = permission;
	}

	public String getSecurityLevelName() {
		String securityLevelName = null;
		switch (this.securityLevel) {
		case 5:
			securityLevelName = "普通管理员";
			break;
		case 9:
			securityLevelName = "高级管理员";
			break;
		case 10:
			securityLevelName = "超级管理员";
			break;
		default:
			securityLevelName = "普通用户";
		}
		return securityLevelName;
	}

	public static int pmaskId = -1; // 遮盖权限组，如<=0则不遮盖

	public String isFlagInCompanyPerm(int flag) {
		getCompanyPermList();
		StringBuilder str = new StringBuilder(20);
		int mark = 0;
		for (int i = 0; i < companyPermList.size(); i++) {
			UserCompanyPermission mUserCompanyPermission = (UserCompanyPermission) companyPermList.get(i);
			if (mUserCompanyPermission.getFlags().get(flag)) {
				if (mark++ != 0) {
					str.append(",");
				}
				str.append(mUserCompanyPermission.getCompanyId());
				String temp = mUserCompanyPermission.getCompanyId() + ",";
				int index = str.indexOf(temp);
				if (index > -1) {
					str.delete(index, index + temp.length());
				}
			}
		}
		return "".equals(str.toString()) ? null : str.toString();
	}

	public boolean isCompanyPerm(int flag, int work_place) {
		String power = isFlagInCompanyPerm(flag);
		String[] powers = power.split(",");
		boolean result = false;
		for (int j = 0; j < powers.length; j++) {
			if (powers[j].equals(work_place + "")) {
				result = true;
				break;
			}
		}
		return result;
	}

	public boolean isFlagInCompanyPerm(int flag, int conmpanyId) {
		getCompanyPermList();
		for (int i = 0; i < companyPermList.size(); i++) {
			UserCompanyPermission mUserCompanyPermission = (UserCompanyPermission) companyPermList.get(i);
			if (mUserCompanyPermission.getFlags().get(flag) && conmpanyId == mUserCompanyPermission.getCompanyId()) {
				return true;
			}
		}
		return false;
	}

	public void getCompanyPermList() {
		if (companyPermList == null) {
			companyPermList = new ArrayList();
			if (groups.length > 0) {
				for (int i = 0; i < groups.length; i++) {
					Map map = PermissionFrk.getUserCompanyPermissionMap(groups[i]);
					if (map != null) {
						companyPermList.addAll(map.values());
					}
				}
			}
		}
	}

	private int accessCount;
	private long lastCheckTime;
	private long nextAccessTime;

	public int getAccessCount() {
		return accessCount;
	}

	public void setAccessCount(int accessCount) {
		this.accessCount = accessCount;
	}

	public long getLastCheckTime() {
		return lastCheckTime;
	}

	public void setLastCheckTime(long lastCheckTime) {
		this.lastCheckTime = lastCheckTime;
	}

	public void addAccessCount() {
		this.accessCount++;
	}

	public long getNextAccessTime() {
		return nextAccessTime;
	}

	public void setNextAccessTime(long nextAccessTime) {
		this.nextAccessTime = nextAccessTime;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public String getGenderName() {
		return gender == 1 ? "男" : gender == 2 ? "女" : "未填写";
	}

	public int[] groups;
	public BinaryFlag mBinaryFlag;

	public int[] getGroups() {
		return groups;
	}

	public void setGroups(int[] groups) {
		this.groups = groups;
	}

	public BinaryFlag getBinaryFlag() {
		return mBinaryFlag;
	}

	public void setBinaryFlag(BinaryFlag flag) {
		this.mBinaryFlag = flag;
	}

	// 根据ID生成员工编号
	public String getCode() {
		return "BJ" + codeFormat.format(id);
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDepartments() {
		return departments;
	}

	public void setDepartments(String departments) {
		this.departments = departments;
	}

	public int getStaffId() {
		return staffId;
	}

	public void setStaffId(int staffId) {
		this.staffId = staffId;
	}

	// 返回部门-员工信息，如果不是员工，返回用户名
	public String getDisplayName() {
		return username;

	}
	
	public UserGroupBean getGroup() {
		if (group == null) {
//			group = PermissionFrk.getUserGroup(groupId).copy();
//			if (groupId2 > 0)
//				group.mergeFlags(PermissionFrk.getUserGroup(groupId2));
//			if (groupId3 > 0)
//				group.mergeFlags(PermissionFrk.getUserGroup(groupId3));
			group = new UserGroupBean();
			if(groups == null)	// 没有任何权限
				return group;
			if (groups.length > 0) {
				for (int i = 0; i < groups.length; i++) {
					group.mergeFlags(PermissionFrk.getUserGroup(groups[i]));
				}
			}
			group.binaryFlagOr(mBinaryFlag);
			if (pmaskId > 0)
				group.maskFlags(PermissionFrk.getUserGroup(pmaskId));


		}
		return group;
	}

}