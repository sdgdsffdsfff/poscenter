package mmb.poscenter.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import mmb.poscenter.domain.BuyGiftEvent;
import mmb.poscenter.domain.BuyGiftProduct;
import mmb.poscenter.domain.Event;
import mmb.poscenter.domain.Product;
import mmb.poscenter.domain.Shop;
import mmboa.util.db.BaseService;
import mmboa.util.db.DbUtil;

import org.apache.log4j.Logger;

public class BuyGiftEventService extends BaseService {
	
	private static Logger log = Logger.getLogger(BuyGiftEventService.class);
	
	/**
	 * 保存买赠活动
	 * @param event 活动对象
	 * @param buyGiftEventList 买赠活动信息
	 * @return
	 * @throws Exception 
	 */
	public void saveEvent(Event event, List<BuyGiftEvent> buyGiftEventList) throws Exception {
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
			
			//保存买赠活动
			ps = conn.prepareStatement("insert into buy_gift_event(event_id,product_id,product_count,user_limit) values(?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
			for(BuyGiftEvent buyGiftEvent : buyGiftEventList) {
				ps.setInt(1, eventId);
				ps.setInt(2, buyGiftEvent.getProductId());
				ps.setInt(3, buyGiftEvent.getProductCount());
				ps.setInt(4, buyGiftEvent.getUserLimit());
				ps.addBatch();
			}
			ps.executeBatch();
			
			//获取买赠活动id列表
			List<Integer> buyGiftEventIdList = new ArrayList<Integer>();
			rs = ps.getGeneratedKeys();
        	while(rs.next()) {
        		buyGiftEventIdList.add(rs.getInt(1));
        	}
			
        	//保存赠品
			ps = conn.prepareStatement("insert into buy_gift_product(buy_gift_event_id,gift_product_id,max_gift_count) values(?,?,?)");
			for(int i=0; i<buyGiftEventIdList.size(); i++) {
				int buyGiftEventId = buyGiftEventIdList.get(i);
				BuyGiftEvent buyGiftEvent = buyGiftEventList.get(i);
				for(BuyGiftProduct gift : buyGiftEvent.getGiftList()) {
					ps.setInt(1, buyGiftEventId);
					ps.setInt(2, gift.getGiftProductId());
					ps.setInt(3, gift.getMaxGiftCount());
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
	 * 修改买赠活动
	 * @param event 活动对象
	 * @param buyGiftEventList 买赠活动信息
	 * @return
	 * @throws Exception 
	 */
	public void updateEvent(Event event, List<BuyGiftEvent> buyGiftEventList) throws Exception {
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
			
			//区分新增和修改
			List<BuyGiftEvent> insertBuyGiftEventList = new ArrayList<BuyGiftEvent>();
			List<BuyGiftEvent> updateBuyGiftEventList = new ArrayList<BuyGiftEvent>();
			for(BuyGiftEvent buyGiftEvent : buyGiftEventList) {
				if(buyGiftEvent.getId() == 0) {
					insertBuyGiftEventList.add(buyGiftEvent);
				}else{
					updateBuyGiftEventList.add(buyGiftEvent);
				}
			}
			
			//保存新增买赠活动
			int count = 0;
			ps = conn.prepareStatement("insert into buy_gift_event(event_id,product_id,product_count,user_limit) values(?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
			for(BuyGiftEvent buyGiftEvent : insertBuyGiftEventList) {
				ps.setInt(1, event.getId());
				ps.setInt(2, buyGiftEvent.getProductId());
				ps.setInt(3, buyGiftEvent.getProductCount());
				ps.setInt(4, buyGiftEvent.getUserLimit());
				ps.addBatch();
				count++;
				if(count == insertBuyGiftEventList.size()) {
					ps.executeBatch();
				}
			}
			//获取买赠活动id列表
			List<Integer> buyGiftEventIdList = new ArrayList<Integer>();
			rs = ps.getGeneratedKeys();
        	while(rs.next()) {
        		buyGiftEventIdList.add(rs.getInt(1));
        	}
			
			//保存修改买赠活动
			count = 0;
			ps = conn.prepareStatement("update buy_gift_event set product_id=?,product_count=?,user_limit=? where id=?");
			for(BuyGiftEvent buyGiftEvent : updateBuyGiftEventList) {
				ps.setInt(1, buyGiftEvent.getProductId());
				ps.setInt(2, buyGiftEvent.getProductCount());
				ps.setInt(3, buyGiftEvent.getUserLimit());
				ps.setInt(4, buyGiftEvent.getId());
				ps.addBatch();
				count++;
				if(count == updateBuyGiftEventList.size()) {
					ps.executeBatch();
				}
			}
			
        	//保存新增的赠品
			ps = conn.prepareStatement("insert into buy_gift_product(buy_gift_event_id,gift_product_id,max_gift_count) values(?,?,?)");
			for(int i=0; i<buyGiftEventIdList.size(); i++) {
				int buyGiftEventId = buyGiftEventIdList.get(i);
				BuyGiftEvent buyGiftEvent = insertBuyGiftEventList.get(i);
				for(BuyGiftProduct gift : buyGiftEvent.getGiftList()) {
					ps.setInt(1, buyGiftEventId);
					ps.setInt(2, gift.getGiftProductId());
					ps.setInt(3, gift.getMaxGiftCount());
					ps.addBatch();
				}
			}
			if(buyGiftEventIdList.size() > 0) {
				ps.executeBatch();
			}
			count = 0;
			for(BuyGiftEvent buyGiftEvent : updateBuyGiftEventList) {
				for(BuyGiftProduct gift : buyGiftEvent.getGiftList()) {
					if(gift.getId() == 0) {
						ps.setInt(1, buyGiftEvent.getId());
						ps.setInt(2, gift.getGiftProductId());
						ps.setInt(3, gift.getMaxGiftCount());
						ps.addBatch();
						count++;
					}
				}
			}
			if(count > 0) {
				ps.executeBatch();
			}
			
			//保存修改的赠品
			count = 0;
			ps = conn.prepareStatement("update buy_gift_product set max_gift_count=? where id=?");
			for(BuyGiftEvent buyGiftEvent : updateBuyGiftEventList) {
				for(BuyGiftProduct gift : buyGiftEvent.getGiftList()) {
					if(gift.getId() != 0) {
						ps.setInt(1, gift.getMaxGiftCount());
						ps.setInt(2, gift.getId());
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
			log.error("修改买赠活动时出现异常：", e);
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
			
			//获取促销商品信息
			ps = conn.prepareStatement("select b.id,b.product_id,b.product_count,b.user_limit,p.name from buy_gift_event b join product p on p.id=b.product_id where b.event_id=?");
			ps.setInt(1, eventId);
			rs = ps.executeQuery();
			List<BuyGiftEvent> buyGiftEventList = new ArrayList<BuyGiftEvent>();
			BuyGiftEvent buyGiftEvent = null;
			while (rs.next()) {
				buyGiftEvent = new BuyGiftEvent();
				buyGiftEvent.setId(rs.getInt("id"));
				buyGiftEvent.setProductCount(rs.getInt("product_count"));
				buyGiftEvent.setUserLimit(rs.getInt("user_limit"));
				Product p = new Product();
				p.setId(rs.getInt("product_id"));
				p.setName(rs.getString("name"));
				buyGiftEvent.setProductId(p.getId());
				buyGiftEvent.setProduct(p);
				buyGiftEventList.add(buyGiftEvent);
			}
			event.setBuyGiftEventList(buyGiftEventList);
			
			//获取赠品信息
			StringBuilder sql = new StringBuilder();
			sql.append("select g.id,g.buy_gift_event_id,g.gift_product_id,g.max_gift_count,p.name ");
			sql.append(" from buy_gift_product g join product p on p.id=g.gift_product_id where g.buy_gift_event_id in(");
			for(BuyGiftEvent gEvent : buyGiftEventList) {
				sql.append(gEvent.getId()).append(",");
			}
			sql.deleteCharAt(sql.length()-1);
			sql.append(")");
			sql.append(" order by g.buy_gift_event_id");
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			BuyGiftProduct gift = null;
			while (rs.next()) {
				gift = new BuyGiftProduct();
				gift.setId(rs.getInt("id"));
				gift.setBuyGiftEventId(rs.getInt("buy_gift_event_id"));
				gift.setMaxGiftCount(rs.getInt("max_gift_count"));
				Product p = new Product();
				p.setId(rs.getInt("gift_product_id"));
				p.setName(rs.getString("name"));
				gift.setGiftProductId(p.getId());
				gift.setGiftProduct(p);
				
				for(BuyGiftEvent gEvent : buyGiftEventList) {
					if(gEvent.getId() == gift.getBuyGiftEventId()) {
						gEvent.getGiftList().add(gift);
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
			
			//获取促销商品信息
			ps = conn.prepareStatement("select b.id from buy_gift_event b where b.event_id=?");
			ps.setInt(1, eventId);
			rs = ps.executeQuery();
			List<Integer> buyGiftEventIdList = new ArrayList<Integer>();
			while (rs.next()) {
				buyGiftEventIdList.add(rs.getInt("id"));
			}
			
			//删除赠品
			StringBuilder sql = new StringBuilder();
			sql.append("delete from buy_gift_product where buy_gift_event_id in(");
			for(Integer gEventId : buyGiftEventIdList) {
				sql.append(gEventId).append(",");
			}
			sql.deleteCharAt(sql.length()-1);
			sql.append(")");
			ps = conn.prepareStatement(sql.toString());
			ps.executeUpdate();
			
			//删除促销商品
			ps = conn.prepareStatement("delete from buy_gift_event where event_id=?");
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
	 * 删除买赠活动
	 * @param buyGiftEventId 买赠活动id
	 * @throws Exception 
	 */
	public void deleteBuyGiftEvent(int buyGiftEventId) throws Exception {
		Connection conn = DbUtil.getConnection();
		PreparedStatement ps = null;
		boolean oldAutoCommit = true;
		try {
			//记录原来事务提交的方式
			oldAutoCommit = conn.getAutoCommit();
			//关闭自动提交事务
			conn.setAutoCommit(false);
			
			//删除该促销商品的所有赠品
			ps = conn.prepareStatement("delete from buy_gift_product where buy_gift_event_id=?");
			ps.setInt(1, buyGiftEventId);
			ps.executeUpdate();
			
			//删除促销商品
			ps = conn.prepareStatement("delete from buy_gift_event where id=?");
			ps.setInt(1, buyGiftEventId);
			ps.executeUpdate();
			
        	conn.commit();
		} catch (Exception e) {
			try {
				//回滚事务
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			log.error("删除买赠活动时出现异常：", e);
			throw e;
		} finally {
			DbUtil.closeConnection(null, ps, conn, oldAutoCommit);
		}
	}

	/**
	 * 删除赠品
	 * @param buyGiftProductId 赠品id
	 * @throws Exception 
	 */
	public void deleteBuyGiftProduct(int buyGiftProductId) throws Exception {
		Connection conn = DbUtil.getConnection();
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement("delete from buy_gift_product where id=?");
			ps.setInt(1, buyGiftProductId);
			ps.executeUpdate();
		} catch (Exception e) {
			log.error("删除赠品时出现异常：", e);
			throw e;
		} finally {
			DbUtil.closeConnection(null, ps, conn);
		}
	}

}
