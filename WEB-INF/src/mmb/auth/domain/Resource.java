package mmb.auth.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * 资源
 */
public class Resource implements Serializable {

	private static final long serialVersionUID = 1L;

	/** Id */
	public String id;

	/** 父Id */
	public String parentId;

	/** 父对象 */
	public Resource parent;

	/** 名称 */
	public String resName;
	
	/** URL */
	public String resUrl;

	/** 描述 */
	public String resDesc;

	/** 树级别 */
	public int treeLevel;

	/** 是否叶子节点[0:不是；1:是] */
	public int isLeaf;

	/** 创建时间 */
	public Timestamp createTime;

	/** 子节点 */
	private List<Resource> children = new ArrayList<Resource>();
	
	/**
	 * 标记树节点是否为选中状态（用于数据展示，数据库中不保存）
	 */
	private boolean checked;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public Resource getParent() {
		return parent;
	}

	public void setParent(Resource parent) {
		this.parent = parent;
	}

	public String getResName() {
		return resName;
	}

	public void setResName(String resName) {
		this.resName = resName;
	}

	public String getResUrl() {
		return resUrl;
	}

	public void setResUrl(String resUrl) {
		this.resUrl = resUrl;
	}

	public String getResDesc() {
		return resDesc;
	}

	public void setResDesc(String resDesc) {
		this.resDesc = resDesc;
	}

	public int getTreeLevel() {
		return treeLevel;
	}

	public void setTreeLevel(int treeLevel) {
		this.treeLevel = treeLevel;
	}

	public int getIsLeaf() {
		return isLeaf;
	}

	public void setIsLeaf(int isLeaf) {
		this.isLeaf = isLeaf;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public List<Resource> getChildren() {
		return children;
	}

	public void setChildren(List<Resource> children) {
		this.children = children;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	
}
