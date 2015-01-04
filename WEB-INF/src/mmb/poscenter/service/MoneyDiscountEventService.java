package mmb.poscenter.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import mmb.poscenter.domain.Event;
import mmb.poscenter.domain.MoneyDiscountEvent;
import mmb.poscenter.domain.Shop;
import mmboa.util.db.BaseService;
import mmboa.util.db.DbUtil;

import org.apache.log4j.Logger;

public class MoneyDiscountEventService extends BaseService {
	
	private static Logger log = Logger.getLogger(MoneyDiscountEventService.class);
	
	/**
	 * 保存金额折扣活动
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
			
			//保存金额折扣
			ps = conn.prepareStatement("insert into money_discount_event(event_id,money,type,number_value) values(?,?,?,?)");
			for(MoneyDiscountEvent moneyDiscountEvent : event.getMoneyDiscountEventList()) {
				ps.setInt(1, eventId);
				ps.setDouble(2, moneyDiscountEvent.getMoney());
				ps.setInt(3, moneyDiscountEvent.getType());
				ps.setDouble(4, moneyDiscountEvent.getNumberValue());
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
			log.error("保存金额折扣活动时出现异常：", e);
			throw e;
		} finally {
			DbUtil.closeConnection(rs, ps, conn, oldAutoCommit);
		}
	}
	
	/**
	 * 修改金额折扣活动
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
			
			//区分新增和修改的金额折扣
			List<MoneyDiscountEvent> insertMoneyDiscountEventList = new ArrayList<MoneyDiscountEvent>();
			List<MoneyDiscountEvent> updateMoneyDiscountEventList = new ArrayList<MoneyDiscountEvent>();
			for(MoneyDiscountEvent moneyDiscountEvent : event.getMoneyDiscountEventList()) {
				if(moneyDiscountEvent.getId() == 0) {
					insertMoneyDiscountEventList.add(moneyDiscountEvent);
				}else{
					updateMoneyDiscountEventList.add(moneyDiscountEvent);
				}
			}
			
			//保存金额折扣
			ps = conn.prepareStatement("insert into money_discount_event(event_id,money,type,number_value) values(?,?,?,?)");
			for(MoneyDiscountEvent moneyDiscountEvent : insertMoneyDiscountEventList) {
				ps.setInt(1, event.getId());
				ps.setDouble(2, moneyDiscountEvent.getMoney());
				ps.setInt(3, moneyDiscountEvent.getType());
				ps.setDouble(4, moneyDiscountEvent.getNumberValue());
				ps.addBatch();
			}
			ps.executeBatch();
			
			//保存修改金额折扣
			ps = conn.prepareStatement("update money_discount_event set money=?, type=?, number_value=? where id=?");
			for(MoneyDiscountEvent moneyDiscountEvent : updateMoneyDiscountEventList) {
				ps.setDouble(1, moneyDiscountEvent.getMoney());
				ps.setInt(2, moneyDiscountEvent.getType());
				ps.setDouble(3, moneyDiscountEvent.getNumberValue());
				ps.setInt(4, moneyDiscountEvent.getId());
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
			log.error("修改金额折扣活动时出现异常：", e);
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
			
			//获取金额折扣信息
			ps = conn.prepareStatement("select b.id,b.event_id,b.money,b.type,b.number_value from money_discount_event b where b.event_id=?");
			ps.setInt(1, eventId);
			rs = ps.executeQuery();
			List<MoneyDiscountEvent> moneyDiscountEventList = new ArrayList<MoneyDiscountEvent>();
			MoneyDiscountEvent moneyDiscountEvent = null;
			while (rs.next()) {
				moneyDiscountEvent = new MoneyDiscountEvent();
				moneyDiscountEvent.setId(rs.getInt("id"));
				moneyDiscountEvent.setEventId(rs.getInt("event_id"));
				moneyDiscountEvent.setMoney(rs.getDouble("money"));
				moneyDiscountEvent.setType(rs.getInt("type"));
				moneyDiscountEvent.setNumberValue(rs.getDouble("number_value"));
				moneyDiscountEventList.add(moneyDiscountEvent);
			}
			event.setMoneyDiscountEventList(moneyDiscountEventList);
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
			
			//删除金额折扣活动
			ps = conn.prepareStatement("delete from money_discount_event where event_id=?");
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
	 * 删除金额折扣
	 * @param moneyDiscountEventId 金额折扣活动id
	 * @throws Exception 
	 */
	public void deleteMoneyDiscountEvent(int moneyDiscountEventId) throws Exception {
		Connection conn = DbUtil.getConnection();
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement("delete from money_discount_event where id=?");
			ps.setInt(1, moneyDiscountEventId);
			ps.executeUpdate();
		} catch (Exception e) {
			log.error("删除金额折扣时出现异常：", e);
			throw e;
		} finally {
			DbUtil.closeConnection(null, ps, conn);
		}
	}

}
