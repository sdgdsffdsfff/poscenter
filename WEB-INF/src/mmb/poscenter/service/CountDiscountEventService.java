package mmb.poscenter.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import mmb.poscenter.domain.CountDiscountEvent;
import mmb.poscenter.domain.Event;
import mmb.poscenter.domain.Product;
import mmb.poscenter.domain.Shop;
import mmboa.util.db.BaseService;
import mmboa.util.db.DbUtil;

import org.apache.log4j.Logger;

public class CountDiscountEventService extends BaseService {
	
	private static Logger log = Logger.getLogger(CountDiscountEventService.class);
	
	/**
	 * 保存数量折扣活动
	 * @param event 活动对象
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
			
			//保存数量折扣
			ps = conn.prepareStatement("insert into count_discount_event(event_id,product_id,product_count,type,discount) values(?,?,?,?,?)");
			for(CountDiscountEvent countDiscountEvent : event.getCountDiscountEventList()) {
				ps.setInt(1, eventId);
				ps.setInt(2, countDiscountEvent.getProductId());
				ps.setInt(3, countDiscountEvent.getProductCount());
				ps.setInt(4, countDiscountEvent.getType());
				ps.setDouble(5, countDiscountEvent.getDiscount());
				ps.addBatch();
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
			log.error("保存数量折扣活动时出现异常：", e);
			throw e;
		} finally {
			DbUtil.closeConnection(rs, ps, conn, oldAutoCommit);
		}
	}
	
	/**
	 * 修改数量折扣活动
	 * @param event 活动对象
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
			
			//区分新增和修改的数量折扣
			List<CountDiscountEvent> insertCountDiscountEventList = new ArrayList<CountDiscountEvent>();
			List<CountDiscountEvent> updateCountDiscountEventList = new ArrayList<CountDiscountEvent>();
			for(CountDiscountEvent countDiscountEvent : event.getCountDiscountEventList()) {
				if(countDiscountEvent.getId() == 0) {
					insertCountDiscountEventList.add(countDiscountEvent);
				}else{
					updateCountDiscountEventList.add(countDiscountEvent);
				}
			}
			
			//保存数量折扣
			ps = conn.prepareStatement("insert into count_discount_event(event_id,product_id,product_count,type,discount) values(?,?,?,?,?)");
			for(CountDiscountEvent countDiscountEvent : insertCountDiscountEventList) {
				ps.setInt(1, event.getId());
				ps.setInt(2, countDiscountEvent.getProductId());
				ps.setInt(3, countDiscountEvent.getProductCount());
				ps.setInt(4, countDiscountEvent.getType());
				ps.setDouble(5, countDiscountEvent.getDiscount());
				ps.addBatch();
			}
			ps.executeBatch();
			
			//保存修改数量折扣
			ps = conn.prepareStatement("update count_discount_event set product_id=?, discount=? where id=?");
			for(CountDiscountEvent countDiscountEvent : updateCountDiscountEventList) {
				ps.setInt(1, countDiscountEvent.getProductId());
				ps.setDouble(2, countDiscountEvent.getDiscount());
				ps.setInt(3, countDiscountEvent.getId());
				ps.addBatch();
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
			log.error("修改数量折扣活动时出现异常：", e);
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
			
			//获取数量折扣信息
			ps = conn.prepareStatement("select b.id,b.event_id,b.product_id,b.product_count,b.type,b.discount,p.name from count_discount_event b join product p on p.id=b.product_id where b.event_id=?");
			ps.setInt(1, eventId);
			rs = ps.executeQuery();
			List<CountDiscountEvent> countDiscountEventList = new ArrayList<CountDiscountEvent>();
			CountDiscountEvent countDiscountEvent = null;
			while (rs.next()) {
				countDiscountEvent = new CountDiscountEvent();
				countDiscountEvent.setId(rs.getInt("id"));
				countDiscountEvent.setEventId(rs.getInt("event_id"));
				countDiscountEvent.setProductCount(rs.getInt("product_count"));
				countDiscountEvent.setType(rs.getInt("type"));
				countDiscountEvent.setDiscount(rs.getDouble("discount"));
				Product p = new Product();
				p.setId(rs.getInt("product_id"));
				p.setName(rs.getString("name"));
				countDiscountEvent.setProductId(p.getId());
				countDiscountEvent.setProduct(p);
				countDiscountEventList.add(countDiscountEvent);
			}
			event.setCountDiscountEventList(countDiscountEventList);
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
			
			//删除数量折扣活动
			ps = conn.prepareStatement("delete from count_discount_event where event_id=?");
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
	 * 删除指定商品的折扣
	 * @param eventId 活动id
	 * @param productId 商品id
	 * @throws Exception 
	 */
	public void deleteByProduct(int eventId, int productId) throws Exception {
		Connection conn = DbUtil.getConnection();
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement("delete from count_discount_event where event_id=? and product_id=?");
			ps.setInt(1, eventId);
			ps.setInt(2, productId);
			ps.executeUpdate();
		} catch (Exception e) {
			log.error("删除指定商品的折扣时出现异常：", e);
			throw e;
		} finally {
			DbUtil.closeConnection(null, ps, conn);
		}
	}

	/**
	 * 删除数量折扣
	 * @param countDiscountEventId 数量折扣活动id
	 * @throws Exception 
	 */
	public void deleteCountDiscountEvent(int countDiscountEventId) throws Exception {
		Connection conn = DbUtil.getConnection();
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement("delete from count_discount_event where id=?");
			ps.setInt(1, countDiscountEventId);
			ps.executeUpdate();
		} catch (Exception e) {
			log.error("删除数量折扣时出现异常：", e);
			throw e;
		} finally {
			DbUtil.closeConnection(null, ps, conn);
		}
	}

}
