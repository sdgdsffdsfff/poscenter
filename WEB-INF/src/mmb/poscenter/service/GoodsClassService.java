package mmb.poscenter.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mmb.poscenter.domain.GoodsClass;
import mmb.poscenter.util.TreeSequenceUtil;
import mmboa.util.db.BaseService;
import mmboa.util.db.DbOperation;
import mmboa.util.db.DbUtil;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.google.gson.GsonBuilder;

public class GoodsClassService extends BaseService {
	
	private static Logger log = Logger.getLogger(GoodsClassService.class);
	
	/**
	 * 获取JSON格式的所有商品分类列表数据
	 * @return JSON字符串
	 * @throws Exception 
	 */
	public String getAllGoodsClassJson() throws Exception {
		String json = "";
		try {
			List<GoodsClass> goodClassList = this.getAllGoodsClassList();
			json = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create().toJson(goodClassList);
		} catch (Exception e) {
			log.error("获取JSON格式的所有商品分类列表数据时出现异常：", e);
			json = e.toString();
			throw e;
		}
		return json;
	}
	
	/**
	 * 获取所有商品分类列表数据
	 * @return
	 * @throws Exception 
	 */
	public List<GoodsClass> getAllGoodsClassList() throws Exception {
		List<GoodsClass> goodsClassList = new ArrayList<GoodsClass>();
		Connection conn = DbUtil.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("SELECT gc.id,gc.parent_id,gc.`name`,gc.`desc`,gc.tree_level,gc.is_leaf,gc.create_time from goods_class gc");
			rs = ps.executeQuery();
			GoodsClass gc = null;
			while (rs.next()) {
				gc = new GoodsClass();
		    	gc.setId(rs.getString("id"));
		    	gc.setParentId(rs.getString("parent_id"));
		    	gc.setName(rs.getString("name"));
		    	gc.setDesc(rs.getString("desc"));
		    	gc.setTreeLevel(rs.getShort("tree_level"));
		    	gc.setIsLeaf(rs.getShort("is_leaf"));
		    	gc.setCreateTime(rs.getTimestamp("create_time"));
		    	goodsClassList.add(gc);
			}
		} catch (Exception e) {
			log.error("获取所有商品分类列表数据时出现异常：", e);
			throw e;
		} finally {
			DbUtil.closeConnection(rs, ps, conn);
		}
		return goodsClassList;
	}
	
	/**
	 * 根据商品id获取商品对象信息
	 * @param id 商品id
	 * @return
	 */
	public GoodsClass getGoodsClassById(String id) {
		return (GoodsClass) this.getXXX("`id`='"+id+"'", "goods_class", GoodsClass.class.getName());
	}
	
	/**
	 * 修改节点的‘是否为叶子节点’的状态
	 * @param conn 数据库连接对象，此方法中不处理事务
	 * @param id 商品分类id
	 * @param isLeaf 是否为叶子节点
	 * @throws SQLException
	 */
	public void updateIsLeaf(Connection conn, String id, int isLeaf) throws SQLException {
		String sql = "update goods_class set `is_leaf`=? where id=?";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setInt(1, isLeaf);
		ps.setString(2, id);
		ps.executeUpdate();
		ps.close();
	}
	
	/**
	 * 新增商品分类
	 * @param conn 数据库连接对象，此方法中不处理事务
	 * @param goodsClass 商品分类对象
	 * @throws SQLException
	 */
	public void addGoodsClass(Connection conn, GoodsClass goodsClass) throws SQLException {
		String sql = "insert into goods_class(id,parent_id,`name`,`desc`,tree_level,is_leaf,create_time) values(?,?,?,?,?,?,?)";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setString(1, goodsClass.getId());
		ps.setString(2, goodsClass.getParentId());
		ps.setString(3, goodsClass.getName());
		ps.setString(4, goodsClass.getDesc());
		ps.setInt(5, goodsClass.getTreeLevel());
		ps.setInt(6, goodsClass.getIsLeaf());
		ps.setTimestamp(7, goodsClass.getCreateTime());
		ps.executeUpdate();
		ps.close();
	}
	
	/**
	 * 修改商品分类
	 * @param conn 数据库连接对象，此方法中不处理事务
	 * @param goodsClass 商品分类对象
	 * @throws SQLException
	 */
	public void updateGoodsClass(Connection conn, GoodsClass goodsClass) throws SQLException {
		String sql = "update goods_class set `name`=?,`desc`=?,tree_level=?,is_leaf=? where id=?";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setString(1, goodsClass.getName());
		ps.setString(2, goodsClass.getDesc());
		ps.setInt(3, goodsClass.getTreeLevel());
		ps.setInt(4, goodsClass.getIsLeaf());
		ps.setString(5, goodsClass.getId());
		ps.executeUpdate();
		ps.close();
	}
	
	/**
	 * 保存商品分类信息
	 * @param goodsClass 商品分类对象
	 * @return
	 */
	public boolean saveGoodsClass(GoodsClass goodsClass) {
		boolean success = false;
		Connection conn = DbUtil.getConnection();
		boolean oldAutoCommit = true;
		try {
			//记录原来事务提交的方式
			oldAutoCommit = conn.getAutoCommit();
			//关闭自动提交事务
			conn.setAutoCommit(false);
			
			//新增
			if(StringUtils.isBlank(goodsClass.getId())) {
				//修改父节点isLeaf的属性值
				String parentId = goodsClass.getParentId();
				if(!StringUtils.isBlank(parentId)) {
					GoodsClass parentGoodsClass = this.getGoodsClassById(parentId);
					if(parentGoodsClass.getIsLeaf() == 1) {
						this.updateIsLeaf(conn, parentGoodsClass.getId(), 0);
					}
				} else {
					goodsClass.setParentId(null);
				}
				
				//保存节点对象
				goodsClass.setIsLeaf(1);
				if(StringUtils.isBlank(parentId)) {
					goodsClass.setParent(null);
				}
				String prefix = (StringUtils.isBlank(parentId) ? TreeSequenceUtil.GOODS_CLASS_PREFIX : parentId);
				goodsClass.setId(TreeSequenceUtil.getSequenceNo("goods_class", prefix));
				goodsClass.setCreateTime(new Timestamp(new Date().getTime()));
				this.addGoodsClass(conn, goodsClass);
			}
			//修改
			else {
				GoodsClass oldGoodsClass = this.getGoodsClassById(goodsClass.getId());
				oldGoodsClass.setName(goodsClass.getName());
				oldGoodsClass.setDesc(goodsClass.getDesc());
				this.updateGoodsClass(conn, oldGoodsClass);
			}
			
        	conn.commit();
			success = true;
		} catch (Exception e) {
			try {
				//回滚事务
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			log.error("保存商品分类信息时出现异常：", e);
		} finally {
			DbUtil.closeConnection(null, null, conn, oldAutoCommit);
		}
		return success;
	}
	
	/**
	 * 根据节点id获取XML格式的子节点列表数据
	 * @param id 节点id
	 * @return XML格式的子节点数据
	 */
	public String listChildrenXml(String id) {
		//获取商品分类列表数据
		List<GoodsClass> GoodsClassList = this.listChildrenById("-1".equals(id) ? null : id);
		
		//拼装XML字符串
		StringBuilder xml = new StringBuilder();
		xml.append("<?xml version='1.0' encoding='UTF-8'?>");
		xml.append("<tree id='"+(StringUtils.isBlank(id) ? 0 : id)+"'>");
		if(StringUtils.isBlank(id)) {
			xml.append("<item text='商品分类树' id='-1' open='1'>");
		}
		for(GoodsClass goodsClass : GoodsClassList) {
			xml.append("<item text='"+goodsClass.getName()+"' id='"+goodsClass.getId()+"'  child='"+(goodsClass.getIsLeaf()==1 ? 0 : 1)+"' />");
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
	public List<GoodsClass> listChildrenById(String id) {
		List<GoodsClass> goodsClassList = new ArrayList<GoodsClass>();
		DbOperation db = new DbOperation();
		try{
			String sql = null;
			if(StringUtils.isBlank(id)) {
				sql = "SELECT gc.id,gc.parent_id,gc.`name`,gc.`desc`,gc.tree_level,gc.is_leaf,gc.create_time from goods_class gc where gc.parent_id is null";
			} else {
				sql = "SELECT gc.id,gc.parent_id,gc.`name`,gc.`desc`,gc.tree_level,gc.is_leaf,gc.create_time from goods_class gc where gc.parent_id = '" + id + "'";
			}
			ResultSet rs = db.executeQuery(sql);
		    while(rs.next()){
		    	GoodsClass gc = new GoodsClass();
		    	gc.setId(rs.getString("id"));
		    	gc.setParentId(rs.getString("parent_id"));
		    	gc.setName(rs.getString("name"));
		    	gc.setDesc(rs.getString("desc"));
		    	gc.setTreeLevel(rs.getShort("tree_level"));
		    	gc.setIsLeaf(rs.getShort("is_leaf"));
		    	gc.setCreateTime(rs.getTimestamp("create_time"));
		    	goodsClassList.add(gc);
		    }
		}catch(Exception e){
			log.error("根据节点id获取该节点下的子节点列表数据时出现异常：", e);
		}finally{
			this.release(db);
		}
		return goodsClassList;
	}
	
	/**
	 * 删除节点及其所有子孙节点
	 * @param id 商品分类id
	 */
	public boolean removeAll(String id) {
		boolean success = false;
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
			String sql = "delete from goods_class";
			if(!StringUtils.isBlank(id)) {
				sql += " where id like '" + id + "%'";
			}
			ps = conn.prepareStatement(sql);
			ps.executeUpdate();
			
			//如果该节点无兄弟节点，则修改父节点的isLeaf属性的值
			if(!StringUtils.isBlank(id) && id.length()>3){
				String parentId = id.substring(0, id.length()-2);
				sql = "select count(m.id) from goods_class m where m.parent_id = ?";
				ps = conn.prepareStatement(sql);
				ps.setString(1, parentId);
				rs = ps.executeQuery();
				
				Object result = null;
				if(rs.next()) {
					result = rs.getString(1);
				}
				if(result!=null && Integer.parseInt(result.toString())==0) {
					sql = "update goods_class m set m.is_leaf = 1 where m.id = ?";
					ps = conn.prepareStatement(sql);
					ps.setString(1, parentId);
					ps.executeUpdate();
				}
			}
			
        	conn.commit();
			success = true;
		} catch (Exception e) {
			try {
				conn.rollback(); //回滚事务
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			log.error("删除节点及其所有子孙节点时出现异常：", e);
		} finally {
			DbUtil.closeConnection(rs, ps, conn, oldAutoCommit);
		}
		return success;
	}
	
}
