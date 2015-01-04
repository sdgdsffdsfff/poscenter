package mmb.auth.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mmb.auth.domain.Menu;
import mmb.auth.domain.Resource;
import mmb.poscenter.util.TreeSequenceUtil;
import mmboa.util.db.BaseService;
import mmboa.util.db.DbOperation;
import mmboa.util.db.DbUtil;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

public class MenuService extends BaseService {
	
	private static Logger log = Logger.getLogger(MenuService.class);
	
	/**
	 * 根据id获取菜单信息
	 * @param id 菜单id
	 * @return
	 */
	public Menu getMenuById(String id) {
		Menu menu = null;
		Connection conn = DbUtil.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("SELECT t.id,t.parent_id,t.res_id,t.menu_name,t.menu_desc,t.tree_level,t.is_leaf,t.create_time,r.res_name from auth_menu t join auth_resource r on t.res_id=r.id and t.id=?");
			ps.setString(1, id);
			rs = ps.executeQuery();
			if (rs.next()) {
				menu = new Menu();
				menu.setId(rs.getString("id"));
				menu.setParentId(rs.getString("parent_id"));
				menu.setResourceId(rs.getString("res_id"));
				menu.setMenuName(rs.getString("menu_name"));
				menu.setMenuDesc(rs.getString("menu_desc"));
				menu.setTreeLevel(rs.getShort("tree_level"));
				menu.setIsLeaf(rs.getShort("is_leaf"));
				menu.setCreateTime(rs.getTimestamp("create_time"));
		    	Resource r = new Resource();
		    	r.setId(rs.getString("res_id"));
		    	r.setResName(rs.getString("res_name"));
		    	menu.setResource(r);
			}
		} catch (Exception e) {
			log.error("根据id获取菜单信息时出现异常：", e);
		} finally {
			DbUtil.closeConnection(rs, ps, conn);
		}
		return menu;
	}
	
	/**
	 * 修改节点的‘是否为叶子节点’的状态
	 * @param conn 数据库连接对象，此方法中不处理事务
	 * @param id 菜单id
	 * @param isLeaf 是否为叶子节点
	 * @throws SQLException
	 */
	public void updateIsLeaf(Connection conn, String id, int isLeaf) throws SQLException {
		String sql = "update auth_menu set `is_leaf`=? where id=?";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setInt(1, isLeaf);
		ps.setString(2, id);
		ps.executeUpdate();
		ps.close();
	}
	
	/**
	 * 新增菜单
	 * @param conn 数据库连接对象，此方法中不处理事务
	 * @param menu 菜单对象
	 * @throws SQLException
	 */
	public void addMenu(Connection conn, Menu menu) throws SQLException {
		String sql = "insert into auth_menu(id,parent_id,res_id,`menu_name`,menu_desc,tree_level,is_leaf,create_time) values(?,?,?,?,?,?,?,?)";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setString(1, menu.getId());
		ps.setString(2, menu.getParentId());
		ps.setString(3, menu.getResourceId());
		ps.setString(4, menu.getMenuName());
		ps.setString(5, menu.getMenuDesc());
		ps.setInt(6, menu.getTreeLevel());
		ps.setInt(7, menu.getIsLeaf());
		ps.setTimestamp(8, menu.getCreateTime());
		ps.executeUpdate();
		ps.close();
	}
	
	/**
	 * 修改菜单
	 * @param conn 数据库连接对象，此方法中不处理事务
	 * @param menu 菜单对象
	 * @throws SQLException
	 */
	public void updateMenu(Connection conn, Menu menu) throws SQLException {
		String sql = "update auth_menu set `menu_name`=?,menu_desc=?,res_id=? where id=?";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setString(1, menu.getMenuName());
		ps.setString(2, menu.getMenuDesc());
		ps.setString(3, menu.getResourceId());
		ps.setString(4, menu.getId());
		ps.executeUpdate();
		ps.close();
	}
	
	/**
	 * 保存菜单信息
	 * @param menu 菜单对象
	 * @throws Exception 
	 */
	public void saveMenu(Menu menu) throws Exception {
		Connection conn = DbUtil.getConnection();
		boolean oldAutoCommit = true;
		try {
			//记录原来事务提交的方式
			oldAutoCommit = conn.getAutoCommit();
			//关闭自动提交事务
			conn.setAutoCommit(false);
			
			//新增
			if(StringUtils.isBlank(menu.getId())) {
				//修改父节点isLeaf的属性值
				String parentId = menu.getParentId();
				if(!StringUtils.isBlank(parentId)) {
					Menu parentMenu = this.getMenuById(parentId);
					if(parentMenu.getIsLeaf() == 1) {
						this.updateIsLeaf(conn, parentMenu.getId(), 0);
					}
				} else {
					menu.setParentId(null);
				}
				
				//保存节点对象
				menu.setIsLeaf(1);
				if(StringUtils.isBlank(parentId)) {
					menu.setParent(null);
				}
				String prefix = (StringUtils.isBlank(parentId) ? TreeSequenceUtil.MENU_PREFIX : parentId);
				menu.setId(TreeSequenceUtil.getSequenceNo("auth_menu", prefix));
				menu.setCreateTime(new Timestamp(new Date().getTime()));
				this.addMenu(conn, menu);
			}
			//修改
			else {
				Menu oldMenu = this.getMenuById(menu.getId());
				oldMenu.setMenuName(menu.getMenuName());
				oldMenu.setMenuDesc(menu.getMenuDesc());
				oldMenu.setResourceId(menu.getResourceId());
				this.updateMenu(conn, oldMenu);
			}
			
        	conn.commit();
		} catch (Exception e) {
			try {
				//回滚事务
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			log.error("保存菜单信息时出现异常：", e);
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
		//获取菜单列表数据
		List<Menu> menuList = this.listChildrenById("-1".equals(id) ? null : id);
		
		//拼装XML字符串
		StringBuilder xml = new StringBuilder();
		xml.append("<?xml version='1.0' encoding='UTF-8'?>");
		xml.append("<tree id='"+(StringUtils.isBlank(id) ? 0 : id)+"'>");
		if(StringUtils.isBlank(id)) {
			xml.append("<item text='菜单树' id='-1' open='1'>");
		}
		for(Menu menu : menuList) {
			xml.append("<item text='"+menu.getMenuName()+"' id='"+menu.getId()+"'  child='"+(menu.getIsLeaf()==1 ? 0 : 1)+"' />");
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
	public List<Menu> listChildrenById(String id) {
		List<Menu> menuList = new ArrayList<Menu>();
		DbOperation db = new DbOperation();
		try{
			String sql = "SELECT t.id,t.parent_id,t.res_id,t.menu_name,t.menu_desc,t.tree_level,t.is_leaf,t.create_time from auth_menu t";
			if(StringUtils.isBlank(id)) {
				sql += " where t.parent_id is null";
			} else {
				sql += " where t.parent_id = '" + id + "'";
			}
			ResultSet rs = db.executeQuery(sql);
		    while(rs.next()){
		    	Menu r = new Menu();
		    	r.setId(rs.getString("id"));
		    	r.setParentId(rs.getString("parent_id"));
		    	r.setResourceId(rs.getString("res_id"));
		    	r.setMenuName(rs.getString("menu_name"));
		    	r.setMenuDesc(rs.getString("menu_desc"));
		    	r.setTreeLevel(rs.getShort("tree_level"));
		    	r.setIsLeaf(rs.getShort("is_leaf"));
		    	r.setCreateTime(rs.getTimestamp("create_time"));
		    	menuList.add(r);
		    }
		}catch(Exception e){
			log.error("根据节点id获取该节点下的子节点列表数据时出现异常：", e);
		}finally{
			this.release(db);
		}
		return menuList;
	}
	
	/**
	 * 删除节点及其所有子孙节点
	 * @param id 菜单id
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
			String sql = "delete from auth_menu";
			if(!StringUtils.isBlank(id)) {
				sql += " where id like '" + id + "%'";
			}
			ps = conn.prepareStatement(sql);
			ps.executeUpdate();
			
			//如果该节点无兄弟节点，则修改父节点的isLeaf属性的值
			if(!StringUtils.isBlank(id) && id.length()>3){
				String parentId = id.substring(0, id.length()-2);
				sql = "select count(m.id) from auth_menu m where m.parent_id = ?";
				ps = conn.prepareStatement(sql);
				ps.setString(1, parentId);
				rs = ps.executeQuery();
				
				Object result = null;
				if(rs.next()) {
					result = rs.getString(1);
				}
				if(result!=null && Integer.parseInt(result.toString())==0) {
					sql = "update auth_menu m set m.is_leaf = 1 where m.id = ?";
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
			log.error("删除菜单节点及其所有子孙节点时出现异常：", e);
			throw e;
		} finally {
			DbUtil.closeConnection(rs, ps, conn, oldAutoCommit);
		}
	}
	
}
