package mmb.poscenter.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import mmb.poscenter.domain.ComboEvent;
import mmb.poscenter.domain.ComboProduct;
import mmb.poscenter.domain.Event;
import mmb.poscenter.domain.Product;
import mmb.poscenter.domain.Shop;
import mmboa.util.db.BaseService;
import mmboa.util.db.DbUtil;

import org.apache.log4j.Logger;

public class ComboEventService extends BaseService {
	
	private static Logger log = Logger.getLogger(ComboEventService.class);
	
	/**
	 * 保存套餐活动
	 * @param event 活动对象
	 * @return
	 * @throws Exception 
	 */
	public void saveEvent(Event event) throws Exception {
		Connection conn = DbUtil.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean oldAutoCommit = true;
		try {
			//记录原来事务提交的方式
			oldAutoCommit = conn.getAutoCommit();
			//关闭自动提交事务
			conn.setAutoCommit(false);
			
			//保存活动
			ps = conn.prepareStatement("insert into event(type,start_time,end_time,rule_desc,use_status) values(?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1, event.getType());
			ps.setTimestamp(2, event.getStartTime());
			ps.setTimestamp(3, event.getEndTime());
			ps.setString(4, event.getRuleDesc());
			ps.setInt(5, event.getUseStatus());
			ps.executeUpdate();
			
			//获取活动id
			int eventId = 0;
			rs = ps.getGeneratedKeys();
        	if(rs.next()) {
        		eventId = rs.getInt(1);
        	}
        	
        	//保存活动店面
        	ps = conn.prepareStatement("insert into shop_event(event_id,shop_id) values(?,?)");
			for(Shop shop : event.getShopList()) {
				ps.setInt(1, eventId);
				ps.setInt(2, shop.getId());
				ps.addBatch();
			}
			ps.executeBatch();
			
			//保存套餐
			ps = conn.prepareStatement("insert into combo_event(event_id,combo_price) values(?,?)", Statement.RETURN_GENERATED_KEYS);
			for(ComboEvent comboEvent : event.getComboEventList()) {
				ps.setInt(1, eventId);
				ps.setDouble(2, comboEvent.getComboPrice());
				ps.addBatch();
			}
			ps.executeBatch();
			
			//获取买赠活动id列表
			List<Integer> comboEventIdList = new ArrayList<Integer>();
			rs = ps.getGeneratedKeys();
        	while(rs.next()) {
        		comboEventIdList.add(rs.getInt(1));
        	}
			
        	//保存赠品
			ps = conn.prepareStatement("insert into combo_product(combo_event_id,product_id,product_count) values(?,?,?)");
			for(int i=0; i<comboEventIdList.size(); i++) {
				int comboEventId = comboEventIdList.get(i);
				ComboEvent comboEvent = event.getComboEventList().get(i);
				for(ComboProduct product : comboEvent.getComboProductList()) {
					ps.setInt(1, comboEventId);
					ps.setInt(2, product.getProductId());
					ps.setInt(3, product.getProductCount());
					ps.addBatch();
				}
			}
			ps.executeBatch();
			
        	conn.commit();
		} catch (Exception e) {
			try {
				//回滚事务
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			log.error("保存买赠活动时出现异常：", e);
			throw e;
		} finally {
			DbUtil.closeConnection(rs, ps, conn, oldAutoCommit);
		}
	}
	
	/**
	 * 修改套餐活动
	 * @param event 活动对象
	 * @return
	 * @throws Exception 
	 */
	public void updateEvent(Event event) throws Exception {
		Connection conn = DbUtil.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean oldAutoCommit = true;
		try {
			//记录原来事务提交的方式
			oldAutoCommit = conn.getAutoCommit();
			//关闭自动提交事务
			conn.setAutoCommit(false);
			
			//修改活动
			ps = conn.prepareStatement("update event set start_time=?,end_time=?,rule_desc=?,use_status=? where id=?");
			ps.setTimestamp(1, event.getStartTime());
			ps.setTimestamp(2, event.getEndTime());
			ps.setString(3, event.getRuleDesc());
			ps.setInt(4, event.getUseStatus());
			ps.setInt(5, event.getId());
			ps.executeUpdate();
			
        	//修改活动店面
			ps = conn.prepareStatement("delete from shop_event where event_id=?");
			ps.setInt(1, event.getId());
			ps.executeUpdate();
        	ps = conn.prepareStatement("insert into shop_event(event_id,shop_id) values(?,?)");
			for(Shop shop : event.getShopList()) {
				ps.setInt(1, event.getId());
				ps.setInt(2, shop.getId());
				ps.addBatch();
			}
			ps.executeBatch();
			
			//区分新增和修改的套餐
			List<ComboEvent> insertComboEventList = new ArrayList<ComboEvent>();
			List<ComboEvent> updateComboEventList = new ArrayList<ComboEvent>();
			for(ComboEvent comboEvent : event.getComboEventList()) {
				if(comboEvent.getId() == 0) {
					insertComboEventList.add(comboEvent);
				}else{
					updateComboEventList.add(comboEvent);
				}
			}
			
			//保存新增套餐
			int count = 0;
			ps = conn.prepareStatement("insert into combo_event(event_id,combo_price) values(?,?)", Statement.RETURN_GENERATED_KEYS);
			for(ComboEvent comboEvent : insertComboEventList) {
				ps.setInt(1, event.getId());
				ps.setDouble(2, comboEvent.getComboPrice());
				ps.addBatch();
				count++;
				if(count == insertComboEventList.size()) {
					ps.executeBatch();
				}
			}
			
			//获取买赠活动id列表
			List<Integer> comboEventIdList = new ArrayList<Integer>();
			rs = ps.getGeneratedKeys();
        	while(rs.next()) {
        		comboEventIdList.add(rs.getInt(1));
        	}
			
			//保存修改套餐
			count = 0;
			ps = conn.prepareStatement("update combo_event set combo_price=? where id=?");
			for(ComboEvent comboEvent : updateComboEventList) {
				ps.setDouble(1, comboEvent.getComboPrice());
				ps.setInt(2, comboEvent.getId());
				ps.addBatch();
				count++;
				if(count == updateComboEventList.size()) {
					ps.executeBatch();
				}
			}
			
        	//保存新增的套餐商品
			ps = conn.prepareStatement("insert into combo_product(combo_event_id,product_id,product_count) values(?,?,?)");
			for(int i=0; i<comboEventIdList.size(); i++) {
				int comboEventId = comboEventIdList.get(i);
				ComboEvent comboEvent = insertComboEventList.get(i);
				for(ComboProduct product : comboEvent.getComboProductList()) {
					ps.setInt(1, comboEventId);
					ps.setInt(2, product.getProductId());
					ps.setInt(3, product.getProductCount());
					ps.addBatch();
				}
			}
			if(comboEventIdList.size() > 0) {
				ps.executeBatch();
			}
			count = 0;
			for(ComboEvent comboEvent : updateComboEventList) {
				for(ComboProduct product : comboEvent.getComboProductList()) {
					if(product.getId() == 0) {
						ps.setInt(1, comboEvent.getId());
						ps.setInt(2, product.getProductId());
						ps.setInt(3, product.getProductCount());
						ps.addBatch();
						count++;
					}
				}
			}
			if(count > 0) {
				ps.executeBatch();
			}
			
			//保存修改的套餐商品
			count = 0;
			ps = conn.prepareStatement("update combo_product set product_count=? where id=?");
			for(ComboEvent comboEvent : updateComboEventList) {
				for(ComboProduct product : comboEvent.getComboProductList()) {
					if(product.getId() != 0) {
						ps.setInt(1, product.getProductCount());
						ps.setInt(2, product.getId());
						ps.addBatch();
						count++;
					}
				}
			}
			if(count > 0) {
				ps.executeBatch();
			}
			
        	conn.commit();
		} catch (Exception e) {
			try {
				//回滚事务
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			log.error("修改套餐活动时出现异常：", e);
			throw e;
		} finally {
			DbUtil.closeConnection(rs, ps, conn, oldAutoCommit);
		}
	}

	/**
	 * 获取活动的详细信息
	 * @param eventId 活动id
	 * @return
	 */
	public Event getDetailEvent(int eventId) {
		Event event = null;
		Connection conn = DbUtil.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			// 获取活动信息
			ps = conn.prepareStatement("select e.id,e.type,e.start_time,e.end_time,e.rule_desc,e.use_status from event e where e.id=?");
			ps.setInt(1, eventId);
			rs = ps.executeQuery();
			if (rs.next()) {
				event = new Event();
				event.setId(rs.getInt("id"));
				event.setType(rs.getInt("type"));
				event.setStartTime(rs.getTimestamp("start_time"));
				event.setEndTime(rs.getTimestamp("end_time"));
				event.setRuleDesc(rs.getString("rule_desc"));
				event.setUseStatus(rs.getInt("use_status"));
			}

			// 获取活动店面信息
			ps = conn.prepareStatement("select s.id,s.`name` from event e join shop_event se on e.id=se.event_id and e.id=? join shop s on s.id=se.shop_id ");
			ps.setInt(1, eventId);
			rs = ps.executeQuery();
			List<Shop> shopList = new ArrayList<Shop>();
			Shop shop = null;
			while (rs.next()) {
				shop = new Shop();
				shop.setId(rs.getInt("id"));
				shop.setName(rs.getString("name"));
				shopList.add(shop);
			}
			event.setShopList(shopList);
			
			//获取套餐信息
			ps = conn.prepareStatement("select b.id,b.event_id,b.combo_price from combo_event b where b.event_id=?");
			ps.setInt(1, eventId);
			rs = ps.executeQuery();
			List<ComboEvent> comboEventList = new ArrayList<ComboEvent>();
			ComboEvent comboEvent = null;
			while (rs.next()) {
				comboEvent = new ComboEvent();
				comboEvent.setId(rs.getInt("id"));
				comboEvent.setEventId(rs.getInt("event_id"));
				comboEvent.setComboPrice(rs.getDouble("combo_price"));
				comboEventList.add(comboEvent);
			}
			event.setComboEventList(comboEventList);
			
			//获取套餐商品
			StringBuilder sql = new StringBuilder();
			sql.append("select cp.id,cp.combo_event_id,cp.product_id,cp.product_count,p.name ");
			sql.append(" from combo_product cp join product p on p.id=cp.product_id where cp.combo_event_id in(");
			for(ComboEvent cEvent : comboEventList) {
				sql.append(cEvent.getId()).append(",");
			}
			sql.deleteCharAt(sql.length()-1);
			sql.append(")");
			sql.append(" order by cp.combo_event_id");
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			ComboProduct comboProduct = null;
			while (rs.next()) {
				comboProduct = new ComboProduct();
				comboProduct.setId(rs.getInt("id"));
				comboProduct.setComboEventId(rs.getInt("combo_event_id"));
				comboProduct.setProductCount(rs.getInt("product_count"));
				Product p = new Product();
				p.setId(rs.getInt("product_id"));
				p.setName(rs.getString("name"));
				comboProduct.setProductId(p.getId());
				comboProduct.setProduct(p);
				
				for(ComboEvent cEvent : comboEventList) {
					if(cEvent.getId() == comboProduct.getComboEventId()) {
						cEvent.getComboProductList().add(comboProduct);
					}
				}
			}
		} catch (Exception e) {
			log.error("获取活动的详细信息时出现异常：", e);
		} finally {
			DbUtil.closeConnection(rs, ps, conn);
		}
		return event;
	}
	
	/**
	 * 删除活动
	 * @param eventId 活动id
	 * @throws Exception 
	 */
	public void deleteEvent(int eventId) throws Exception {
		Connection conn = DbUtil.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean oldAutoCommit = true;
		try {
			//记录原来事务提交的方式
			oldAutoCommit = conn.getAutoCommit();
			//关闭自动提交事务
			conn.setAutoCommit(false);
			
			//获取套餐信息
			ps = conn.prepareStatement("select b.id from combo_event b where b.event_id=?");
			ps.setInt(1, eventId);
			rs = ps.executeQuery();
			List<Integer> comboEventIdList = new ArrayList<Integer>();
			while (rs.next()) {
				comboEventIdList.add(rs.getInt("id"));
			}
			
			//删除套餐商品
			StringBuilder sql = new StringBuilder();
			sql.append("delete from combo_product where combo_event_id in(");
			for(Integer cEventId : comboEventIdList) {
				sql.append(cEventId).append(",");
			}
			sql.deleteCharAt(sql.length()-1);
			sql.append(")");
			ps = conn.prepareStatement(sql.toString());
			ps.executeUpdate();
			
			//删除套餐
			ps = conn.prepareStatement("delete from combo_event where event_id=?");
			ps.setInt(1, eventId);
			ps.executeUpdate();
			
			//删除活动
			ps = conn.prepareStatement("delete from event where id=?");
			ps.setInt(1, eventId);
			ps.executeUpdate();
			
			//删除店面活动
			ps = conn.prepareStatement("delete from shop_event where event_id=?");
			ps.setInt(1, eventId);
			ps.executeUpdate();
			
        	conn.commit();
		} catch (Exception e) {
			try {
				//回滚事务
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			log.error("删除活动时出现异常：", e);
			throw e;
		} finally {
			DbUtil.closeConnection(rs, ps, conn, oldAutoCommit);
		}
	}

	/**
	 * 删除套餐
	 * @param comboEventId 买赠活动id
	 * @throws Exception 
	 */
	public void deleteComboEvent(int comboEventId) throws Exception {
		Connection conn = DbUtil.getConnection();
		PreparedStatement ps = null;
		boolean oldAutoCommit = true;
		try {
			//记录原来事务提交的方式
			oldAutoCommit = conn.getAutoCommit();
			//关闭自动提交事务
			conn.setAutoCommit(false);
			
			//删除套餐商品
			ps = conn.prepareStatement("delete from combo_product where combo_event_id=?");
			ps.setInt(1, comboEventId);
			ps.executeUpdate();
			
			//删除套餐
			ps = conn.prepareStatement("delete from combo_event where id=?");
			ps.setInt(1, comboEventId);
			ps.executeUpdate();
			
        	conn.commit();
		} catch (Exception e) {
			try {
				//回滚事务
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			log.error("删除套餐时出现异常：", e);
			throw e;
		} finally {
			DbUtil.closeConnection(null, ps, conn, oldAutoCommit);
		}
	}

	/**
	 * 删除套餐商品
	 * @param comboProductId 餐商品id
	 * @throws Exception 
	 */
	public void deleteComboProduct(int comboProductId) throws Exception {
		Connection conn = DbUtil.getConnection();
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement("delete from combo_product where id=?");
			ps.setInt(1, comboProductId);
			ps.executeUpdate();
		} catch (Exception e) {
			log.error("删除套餐商品时出现异常：", e);
			throw e;
		} finally {
			DbUtil.closeConnection(null, ps, conn);
		}
	}

}
