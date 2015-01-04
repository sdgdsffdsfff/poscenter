package mmb.poscenter.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

/**
 * 商品分类对象
 * @author likaige
 */
public class GoodsClass implements Serializable{

	private static final long serialVersionUID = 1L;

	/**记录号*/
	public String id;
	
	/**父Id*/
	public String parentId;
	
	/**父对象*/
	public GoodsClass parent;
	
	/**名称*/
	public String name;
	
	/**描述*/
	public String desc;
	
	/**树级别*/
	public int treeLevel;
	
	/**是否叶子节点[0:不是；1:是]*/
	public int isLeaf;
	
	/**创建时间*/
	public Timestamp createTime;
	
	/**
	 * 子节点列表
	 */
	private Set<GoodsClass> children = new HashSet<GoodsClass>();

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

	public GoodsClass getParent() {
		return parent;
	}

	public void setParent(GoodsClass parent) {
		this.parent = parent;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
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

	public Set<GoodsClass> getChildren() {
		return children;
	}

	public void setChildren(Set<GoodsClass> children) {
		this.children = children;
	}

}
