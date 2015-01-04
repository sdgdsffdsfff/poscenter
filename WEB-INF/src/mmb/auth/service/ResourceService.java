package mmb.auth.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mmb.auth.domain.Resource;
import mmb.poscenter.util.TreeSequenceUtil;
import mmboa.util.db.BaseService;
import mmboa.util.db.DbOperation;
import mmboa.util.db.DbUtil;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

public class ResourceService extends BaseService {
	
	private static Logger log = Logger.getLogger(ResourceService.class);
	
	/**
	 * 根据id获取资源信息
	 * @param id 资源id
	 * @return
	 */
	public Resource getResourceById(String id) {
		return (Resource) this.getXXX("`id`='"+id+"'", "auth_resource", Resource.class.getName());
	}
	
	/**
	 * 修改节点的‘是否为叶子节点’的状态
	 * @param conn 数据库连接对象，此方法中不处理事务
	 * @param id 资源id
	 * @param isLeaf 是否为叶子节点
	 * @throws SQLException
	 */
	public void updateIsLeaf(Connection conn, String id, int isLeaf) throws SQLException {
		String sql = "update auth_resource set `is_leaf`=? where id=?";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setInt(1, isLeaf);
		ps.setString(2, id);
		ps.executeUpdate();
		ps.close();
	}
	
	/**
	 * 新增资源
	 * @param conn 数据库连接对象，此方法中不处理事务
	 * @param resource 资源对象
	 * @throws SQLException
	 */
	public void addResource(Connection conn, Resource resource) throws SQLException {
		String sql = "insert into auth_resource(id,parent_id,`res_name`,res_url,`res_desc`,tree_level,is_leaf,create_time) values(?,?,?,?,?,?,?,?)";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setString(1, resource.getId());
		ps.setString(2, resource.getParentId());
		ps.setString(3, resource.getResName());
		ps.setString(4, resource.getResUrl());
		ps.setString(5, resource.getResDesc());
		ps.setInt(6, resource.getTreeLevel());
		ps.setInt(7, resource.getIsLeaf());
		ps.setTimestamp(8, resource.getCreateTime());
		ps.executeUpdate();
		ps.close();
	}
	
	/**
	 * 修改资源
	 * @param conn 数据库连接对象，此方法中不处理事务
	 * @param resource 资源对象
	 * @throws SQLException
	 */
	public void updateResource(Connection conn, Resource resource) throws SQLException {
		String sql = "update auth_resource set `res_name`=?,res_url=?,`res_desc`=? where id=?";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setString(1, resource.getResName());
		ps.setString(2, resource.getResUrl());
		ps.setString(3, resource.getResDesc());
		ps.setString(4, resource.getId());
		ps.executeUpdate();
		ps.close();
	}
	
	/**
	 * 保存资源信息
	 * @param resource 资源对象
	 * @throws Exception 
	 */
	public void saveResource(Resource resource) throws Exception {
		Connection conn = DbUtil.getConnection();
		boolean oldAutoCommit = true;
		try {
			//记录原来事务提交的方式
			oldAutoCommit = conn.getAutoCommit();
			//关闭自动提交事务
			conn.setAutoCommit(false);
			
			//新增
			if(StringUtils.isBlank(resource.getId())) {
				//修改父节点isLeaf的属性值
				String parentId = resource.getParentId();
				if(!StringUtils.isBlank(parentId)) {
					Resource parentResource = this.getResourceById(parentId);
					if(parentResource.getIsLeaf() == 1) {
						this.updateIsLeaf(conn, parentResource.getId(), 0);
					}
				} else {
					resource.setParentId(null);
				}
				
				//保存节点对象
				resource.setIsLeaf(1);
				if(StringUtils.isBlank(parentId)) {
					resource.setParent(null);
				}
				String prefix = (StringUtils.isBlank(parentId) ? TreeSequenceUtil.RESOURCE_PREFIX : parentId);
				resource.setId(TreeSequenceUtil.getSequenceNo("auth_resource", prefix));
				resource.setCreateTime(new Timestamp(new Date().getTime()));
				this.addResource(conn, resource);
			}
			//修改
			else {
				Resource oldResource = this.getResourceById(resource.getId());
				oldResource.setResName(resource.getResName());
				oldResource.setResUrl(resource.getResUrl());
				oldResource.setResDesc(resource.getResDesc());
				this.updateResource(conn, oldResource);
			}
			
        	conn.commit();
		} catch (Exception e) {
			try {
				//回滚事务
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			log.error("保存资源信息时出现异常：", e);
			throw e;
		} finally {
			DbUtil.closeConnection(null, null, conn, oldAutoCommit);
		}
	}
	
	/**
	 * 根据节点id获取XML格式的子节点列表数据
	 * @param id 节点id
	 * @return XML格式的子节点数据
	 */
	public String listChildrenXml(String id) {
		//获取资源列表数据
		List<Resource> resourceList = this.listChildrenById("-1".equals(id) ? null : id);
		
		//拼装XML字符串
		StringBuilder xml = new StringBuilder();
		xml.append("<?xml version='1.0' encoding='UTF-8'?>");
		xml.append("<tree id='"+(StringUtils.isBlank(id) ? 0 : id)+"'>");
		if(StringUtils.isBlank(id)) {
			xml.append("<item text='资源树' id='-1' open='1'>");
		}
		for(Resource resource : resourceList) {
			xml.append("<item text='"+resource.getResName()+"' id='"+resource.getId()+"'  child='"+(resource.getIsLeaf()==1 ? 0 : 1)+"' />");
		}
		if(StringUtils.isBlank(id)) {
			xml.append("</item>");
		}
		xml.append("</tree>");
		
		return xml.toString();
	}
	
	/**
	 * 根据节点id获取该节点下的子节点列表数据
	 * @param id 节点id，若为null则获取第一级节点
	 * @return 子节点列表
	 */
	public List<Resource> listChildrenById(String id) {
		List<Resource> resourceList = new ArrayList<Resource>();
		DbOperation db = new DbOperation();
		try{
			String sql = "SELECT t.id,t.parent_id,t.res_name,t.res_url,t.res_desc,t.tree_level,t.is_leaf,t.create_time from auth_resource t";
			if(StringUtils.isBlank(id)) {
				sql += " where t.parent_id is null";
			} else {
				sql += " where t.parent_id = '" + id + "'";
			}
			ResultSet rs = db.executeQuery(sql);
		    while(rs.next()){
		    	Resource r = new Resource();
		    	r.setId(rs.getString("id"));
		    	r.setParentId(rs.getString("parent_id"));
		    	r.setResName(rs.getString("res_name"));
		    	r.setResUrl(rs.getString("res_url"));
		    	r.setResDesc(rs.getString("res_desc"));
		    	r.setTreeLevel(rs.getShort("tree_level"));
		    	r.setIsLeaf(rs.getShort("is_leaf"));
		    	r.setCreateTime(rs.getTimestamp("create_time"));
		    	resourceList.add(r);
		    }
		}catch(Exception e){
			log.error("根据节点id获取该节点下的子节点列表数据时出现异常：", e);
		}finally{
			this.release(db);
		}
		return resourceList;
	}
	
	/**
	 * 删除节点及其所有子孙节点
	 * @param id 资源id
	 * @throws Exception 
	 */
	public void removeAll(String id) throws Exception {
		Connection conn = DbUtil.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean oldAutoCommit = true;
		try {
			//记录原来事务提交的方式
			oldAutoCommit = conn.getAutoCommit();
			//关闭自动提交事务
			conn.setAutoCommit(false);
			
			//删除节点及其所有子孙节点
			String sql = "delete from auth_resource";
			if(!StringUtils.isBlank(id)) {
				sql += " where id like '" + id + "%'";
			}
			ps = conn.prepareStatement(sql);
			ps.executeUpdate();
			
			//如果该节点无兄弟节点，则修改父节点的isLeaf属性的值
			if(!StringUtils.isBlank(id) && id.length()>3){
				String parentId = id.substring(0, id.length()-2);
				sql = "select count(m.id) from auth_resource m where m.parent_id = ?";
				ps = conn.prepareStatement(sql);
				ps.setString(1, parentId);
				rs = ps.executeQuery();
				
				Object result = null;
				if(rs.next()) {
					result = rs.getString(1);
				}
				if(result!=null && Integer.parseInt(result.toString())==0) {
					sql = "update auth_resource m set m.is_leaf = 1 where m.id = ?";
					ps = conn.prepareStatement(sql);
					ps.setString(1, parentId);
					ps.executeUpdate();
				}
			}
        	conn.commit();
		} catch (Exception e) {
			try {
				//回滚事务
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			log.error("删除资源节点及其所有子孙节点时出现异常：", e);
			throw e;
		} finally {
			DbUtil.closeConnection(rs, ps, conn, oldAutoCommit);
		}
	}
	
	/**
	 * 获取整个资源树数据，并根据角色标记已分配的权限
	 * @param roleId 角色id
	 * @return
	 */
	public List<Resource> getAllTreeListByRole(int roleId) {
		List<Resource> resourceTree = new ArrayList<Resource>();
		Connection conn = DbUtil.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			// 获取所有资源列表
			List<Resource> resourceList = new ArrayList<Resource>();
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT t.id,t.parent_id,t.res_name,t.res_url,t.res_desc,t.tree_level,t.is_leaf,t.create_time,rr.role_id ");
			sql.append(" from auth_resource t left join auth_role_resource rr on t.id=rr.res_id and rr.role_id=? order by t.id");
			ps = conn.prepareStatement(sql.toString());
			ps.setInt(1, roleId);
			rs = ps.executeQuery();
			while (rs.next()) {
				Resource r = new Resource();
				r.setId(rs.getString("id"));
				r.setParentId(rs.getString("parent_id"));
				r.setResName(rs.getString("res_name"));
				r.setResUrl(rs.getString("res_url"));
				r.setResDesc(rs.getString("res_desc"));
				r.setTreeLevel(rs.getShort("tree_level"));
				r.setIsLeaf(rs.getShort("is_leaf"));
				r.setCreateTime(rs.getTimestamp("create_time"));
				r.setChecked(rs.getObject("role_id") != null);
				resourceList.add(r);
			}

			// 组装树形结构的菜单
			Resource preResource = null; // 记录上一个资源对象
			for (Resource m : resourceList) {
				// 一级
				if (StringUtils.isBlank(m.getParentId())) {
					resourceTree.add(m);
				}
				// 儿子
				else if (m.getParentId().equals(preResource.getId())) {
					m.setParent(preResource);
					preResource.getChildren().add(m);
				}
				// 兄弟
				else if (m.getParentId().equals(preResource.getParentId())) {
					m.setParent(preResource.getParent());
					preResource.getParent().getChildren().add(m);
				}
				// 是前一个的长辈
				else if (m.getId().length() < preResource.getId().length()) {
					// 找到当前节点的父亲
					Resource p = preResource.getParent();
					for (int i = 0; i < (preResource.getId().length() - m.getId().length()) / 2; i++) {
						p = p.getParent();
					}
					m.setParent(p);
					p.getChildren().add(m);
				}
				preResource = m;
			}
		} catch (Exception e) {
			log.error("获取整个资源树数据时出现异常：", e);
		} finally {
			DbUtil.closeConnection(rs, ps, conn);
		}
		return resourceTree;
	}
	
	/**
	 * 循环拼装XML资源树
	 * @param pResource 父资源
	 * @param xml XML格式字符串
	 */
	private void getChildrenXml(Resource pResource, StringBuilder xml) {
		xml.append("<item text='"+pResource.getResName()+"' id='"+pResource.getId()+"' "+ (pResource.isChecked() ? "checked='1'" : "") +" child='"+(pResource.getIsLeaf()==1 ? 0 : 1)+"'>");
		for(Resource resource : pResource.getChildren()) {
			this.getChildrenXml(resource, xml);
		}
		xml.append("</item>");
	}

	/**
	 * 获取整个资源树的XML数据，并根据角色标记已分配的权限
	 * @param roleId 角色id
	 * @return
	 */
	public String getAllTreeXmlByRole(int roleId) {
		//获取整个资源树数据，并根据角色标记已分配的权限
		List<Resource> resourceList = this.getAllTreeListByRole(roleId);
		
		//拼装XML字符串
		StringBuilder xml = new StringBuilder();
		xml.append("<?xml version='1.0' encoding='UTF-8'?>");
		xml.append("<tree id='0'>");
		xml.append("<item text='资源树' id='-1' open='1'>");
		for(Resource resource : resourceList) {
			this.getChildrenXml(resource, xml);
		}
		xml.append("</item>");
		xml.append("</tree>");
		return xml.toString();
	}

	/**
	 * 判断该资源和子孙资源是否已被菜单关联
	 * @param id 资源id
	 * @return true:已被使用	false:未被使用
	 * @throws Exception 
	 */
	public boolean hasUsedByMenu(String id) throws Exception {
		boolean hasUsed = true;
		Connection conn = DbUtil.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			//查询条件
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT count(m.id) from auth_menu m where m.res_id like ?");
			ps = conn.prepareStatement(sql.toString());
			ps.setString(1, id+"%");
			rs = ps.executeQuery();
		    if(rs.next()){
		    	hasUsed = rs.getInt(1)>0;
		    }
		}catch(Exception e){
			log.error("判断该资源和子孙资源是否已被菜单关联时出现异常：", e);
			throw e;
		}finally{
			DbUtil.closeConnection(rs, ps, conn);
		}
		return hasUsed;
	}
	
}
