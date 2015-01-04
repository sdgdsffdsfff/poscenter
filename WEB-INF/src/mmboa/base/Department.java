package mmboa.base;

public class Department {
	public int id;
	public int parentId;
	public int childCount;	// 下级部门数量
	public int userCount;	// 员工数量
	public String name;
	public int workplace_id;
	public int seq;
	public int deptLevel;//部门级别
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getParentId() {
		return parentId;
	}
	public void setParentId(int parentId) {
		this.parentId = parentId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getSeq() {
		return seq;
	}
	public void setSeq(int seq) {
		this.seq = seq;
	}
	public int getChildCount() {
		return childCount;
	}
	public void setChildCount(int childCount) {
		this.childCount = childCount;
	}
	public int getUserCount() {
		return userCount;
	}
	public void setUserCount(int userCount) {
		this.userCount = userCount;
	}
	public int getDeptLevel() {
		return deptLevel;
	}
	public void setDeptLevel(int deptLevel) {
		this.deptLevel = deptLevel;
	}
	public int getWorkplace_id() {
		return workplace_id;
	}
	public void setWorkplace_id(int workplace_id) {
		this.workplace_id = workplace_id;
	}
}
