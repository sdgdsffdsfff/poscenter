package mmb.poscenter.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import mmb.poscenter.domain.Event;
import mmb.poscenter.domain.Shop;
import mmb.poscenter.domain.SwapBuyEvent;
import mmb.poscenter.domain.SwapBuyProduct;
import mmboa.util.db.BaseService;
import mmboa.util.db.DbUtil;

import org.apache.log4j.Logger;

public class SwapBuyEventService extends BaseService {

	private static Logger log = Logger.getLogger(SwapBuyEventService.class);
	
	/**
	 * 保存换购活动信息
	 * @param swapBuyEvent
	 * @return
	 */
	public void saveEvent(Event event, List<SwapBuyEvent> swapBuyEventList) {
		Connection conn = DbUtil.getConnection();
		PreparedStatement ps = null;
		PreparedStatement ps_swap_product = null;
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
			ps.setInt(5, event.getUseStatus()); //1:未生效
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
			
			//保存换购活动信息
			ps = conn.prepareStatement("insert into swap_buy_event(event_id,money,append_money) values(?,?,?)",Statement.RETURN_GENERATED_KEYS);
			for(SwapBuyEvent sbe : swapBuyEventList){
				ps.setInt(1, eventId);
				ps.setDouble(2, sbe.getMoney());
				ps.setDouble(3, sbe.getAppendMoney());
				ps.addBatch();
			}
			ps.executeBatch();
			
			//获取换购活动id列表
			List<Integer> swapBuyEventIdList = new ArrayList<Integer>();
			rs = ps.getGeneratedKeys();
			while(rs.next()){
				swapBuyEventIdList.add(rs.getInt(1));
			}
			
			//保存换购商品信息表
			ps = conn.prepareStatement("insert into swap_buy_product(swap_buy_event_id,gift_product_id) values(?,?)");
			for(int i = 0 ;i < swapBuyEventIdList.size(); i++ ){
				int swapBuyEventId = swapBuyEventIdList.get(i);
				for(SwapBuyProduct sbp : swapBuyEventList.get(i).getSwapBuyProductList() ){
					ps.setInt(1, swapBuyEventId);
					ps.setInt(2, sbp.getGiftProductId());
					ps.addBatch();
				}
			}
			ps.executeBatch();
			
			conn.commit();
		}catch(Exception e){
			try {
				//回滚事务
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			log.error("保存换购活动信息异常", e);
		}finally{
			DbUtil.closeConnection(rs, ps, null);
			DbUtil.closeConnection(null, ps_swap_product, conn , oldAutoCommit);
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
			
			//获取换购活动信息
			ps = conn.prepareStatement("select sbe.id,sbe.event_id,sbe.money,sbe.append_money from swap_buy_event sbe WHERE sbe.event_id = ?");
			ps.setInt(1, eventId);
			rs = ps.executeQuery();
			List<SwapBuyEvent> swapBuyEventList = new ArrayList<SwapBuyEvent>();
			SwapBuyEvent swapBuyEvent = null;
			StringBuilder swapBuyEventIds = new StringBuilder();
			while (rs.next()) {
				swapBuyEvent = new SwapBuyEvent();
				swapBuyEventIds.append(rs.getInt("id")).append(",");
				swapBuyEvent.setId(rs.getInt("id"));
				swapBuyEvent.setEventId(rs.getInt("event_id"));
				swapBuyEvent.setMoney(rs.getDouble("money"));
				swapBuyEvent.setAppendMoney(rs.getDouble("append_money"));
				List<SwapBuyProduct> temp = new ArrayList<SwapBuyProduct>();
				swapBuyEvent.setSwapBuyProductList(temp);
				swapBuyEventList.add(swapBuyEvent);
			}
			
			if(swapBuyEventIds.length() != 0){
				swapBuyEventIds.deleteCharAt(swapBuyEventIds.length()-1);
				
				// 查询换购商品
				ps = conn.prepareStatement("select sbp.id,sbp.swap_buy_event_id,sbp.gift_product_id,p.`name` from swap_buy_product sbp LEFT JOIN product p on sbp.gift_product_id = p.id WHERE sbp.swap_buy_event_id in ( "+swapBuyEventIds.toString()+" )");
				rs = ps.executeQuery();
				SwapBuyProduct swapBuyProduct = null;
				List<SwapBuyProduct>  allSbpList = new ArrayList<SwapBuyProduct>();
				while(rs.next()){
					swapBuyProduct = new SwapBuyProduct();
					swapBuyProduct.setId(rs.getInt("id"));
					swapBuyProduct.setGiftProductId(rs.getInt("gift_product_id"));
					swapBuyProduct.setGiftProductName(rs.getString("name"));
					swapBuyProduct.setSwapBuyEventId(rs.getInt("swap_buy_event_id"));
					allSbpList.add(swapBuyProduct);
				}
				
				//将换购商品信息拆分入对象
				for(SwapBuyEvent tempSbe : swapBuyEventList){
					for(SwapBuyProduct sbp : allSbpList){
						if(tempSbe.getId() == sbp.getSwapBuyEventId()){
							tempSbe.getSwapBuyProductList().add(sbp);
						}
					}
				}
			}
			
			event.setSwapBuyEventList(swapBuyEventList);
		} catch (Exception e) {
			log.error("获取换购活动的详细信息时出现异常：", e);
		} finally {
			DbUtil.closeConnection(rs, ps, conn);
		}
		return event;
	}
	
	/**
	 * 修改换购活动信息
	 * @param event 活动对象
	 * @param swapBuyEventList 换购活动信息
	 * @return
	 * @throws Exception 
	 */
	public void updateEvent(Event event, List<SwapBuyEvent> swapBuyEventList) throws Exception {
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
			
			//区分新增换购活动和修改的换购活动
			List<SwapBuyEvent> insertSwapBuyEventList = new ArrayList<SwapBuyEvent>();
			List<SwapBuyEvent> updateSwapBuyEventList = new ArrayList<SwapBuyEvent>();
			for(SwapBuyEvent swapBuyEvent : swapBuyEventList) {
				if(swapBuyEvent.getId() == 0) {
					insertSwapBuyEventList.add(swapBuyEvent);
				}else{
					updateSwapBuyEventList.add(swapBuyEvent);
				}
			}
			
			//保存新增换购活动
			ps = conn.prepareStatement("insert into swap_buy_event(event_id,money,append_money) values(?,?,?)",Statement.RETURN_GENERATED_KEYS);
			for(SwapBuyEvent swapBuyEvent : insertSwapBuyEventList) {
				ps.setInt(1, event.getId());
				ps.setDouble(2, swapBuyEvent.getMoney());
				ps.setDouble(3, swapBuyEvent.getAppendMoney());
				ps.addBatch();
			}
			ps.executeBatch();
			//获取换购活动id列表
			List<Integer> swapBuyEventIdList = new ArrayList<Integer>();
			rs = ps.getGeneratedKeys();
        	while(rs.next()) {
        		swapBuyEventIdList.add(rs.getInt(1));
        	}
			
        	//保存新增的换购商品信息表
			ps = conn.prepareStatement("insert into swap_buy_product(swap_buy_event_id,gift_product_id) values(?,?)");
			for(int i = 0 ;i < swapBuyEventIdList.size(); i++ ){
				 for(SwapBuyProduct sbp : insertSwapBuyEventList.get(i).getSwapBuyProductList()){
						ps.setInt(1, swapBuyEventIdList.get(i));
						ps.setInt(2, sbp.getGiftProductId());
						ps.addBatch();
						
				 }
			}
			ps.executeBatch();
        	
			//保存修改换购活动
			ps = conn.prepareStatement("update swap_buy_event set money=?,append_money=? where id=?");
			for(SwapBuyEvent swapBuyEvent : updateSwapBuyEventList) {
				ps.setDouble(1, swapBuyEvent.getMoney());
				ps.setDouble(2, swapBuyEvent.getAppendMoney());
				ps.setInt(3, swapBuyEvent.getId());
				ps.addBatch();
			}
			ps.executeBatch();
			
			//删除对应换购活动下所有的换购商品
			ps = conn.prepareStatement("delete from swap_buy_product where swap_buy_event_id = ?");
			for(SwapBuyEvent swapBuyEvent : updateSwapBuyEventList) {
				ps.setInt(1, swapBuyEvent.getId());
				ps.addBatch();
			}
			ps.executeBatch();
			
        	//插入新的换购商品
			ps = conn.prepareStatement("insert into swap_buy_product(swap_buy_event_id,gift_product_id) values(?,?)");
			for(int i=0; i<updateSwapBuyEventList.size(); i++) {
				SwapBuyEvent sbe = updateSwapBuyEventList.get(i);
			      for(SwapBuyProduct sbp : sbe.getSwapBuyProductList()){
			    	    ps.setInt(1, sbe.getId());
			    	    ps.setInt(2, sbp.getGiftProductId());
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
			log.error("修改换购活动时出现异常：", e);
			throw e;
		} finally {
			DbUtil.closeConnection(rs, ps, conn, oldAutoCommit);
		}
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
			
			//获取换购活动信息
			ps = conn.prepareStatement("select s.id from swap_buy_event s where s.event_id = ?");
			ps.setInt(1, eventId);
			rs = ps.executeQuery();
			List<Integer> swapBuyEventIdList = new ArrayList<Integer>();
			while (rs.next()) {
				swapBuyEventIdList.add(rs.getInt("id"));
			}
			
			//删除换购商品
			StringBuilder sql = new StringBuilder();
			sql.append("delete from swap_buy_product where swap_buy_event_id in(");
			for(Integer gEventId : swapBuyEventIdList) {
				sql.append(gEventId).append(",");
			}
			sql.deleteCharAt(sql.length()-1);
			sql.append(")");
			ps = conn.prepareStatement(sql.toString());
			ps.executeUpdate();
			
			//删除换购信息
			ps = conn.prepareStatement("delete from swap_buy_event where event_id=?");
			ps.setInt(1, eventId);
			ps.executeUpdate();
			
			//删除活动
			ps = conn.prepareStatement("delete from event where id = ?");
			ps.setInt(1, eventId);
			ps.executeUpdate();
			
			//删除店面活动
			ps = conn.prepareStatement("delete from shop_event where event_id = ?");
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
	 * 删除换购信息
	 * @param swayBuyEventId
	 */
	public void deleteSwapBuyEvent(int swayBuyEventId){
		Connection conn = DbUtil.getConnection();
		PreparedStatement ps = null;
		boolean oldAutoCommit = true;
		try {
			//记录原来事务提交的方式
			oldAutoCommit = conn.getAutoCommit();
			//关闭自动提交事务
			conn.setAutoCommit(false);
			
			//删除该换购信息的所有换购商品
			ps = conn.prepareStatement("delete from swap_buy_product where swap_buy_event_id = ?");
			ps.setInt(1, swayBuyEventId);
			ps.executeUpdate();
			
			//删除换购商品
			ps = conn.prepareStatement("delete from swap_buy_event where id=?");
			ps.setInt(1, swayBuyEventId);
			ps.executeUpdate();
			
        	conn.commit();
		} catch (Exception e) {
			try {
				//回滚事务
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			log.error("删除换购活动信息时出现异常：", e);
		} finally {
			DbUtil.closeConnection(null, ps, conn, oldAutoCommit);
		}
	}
	
	/**
	 * 删除换购商品
	 * @param swayBuyProductId 赠品id
	 * @throws Exception 
	 */
	public void deleteSwapBuyProduct(int swayBuyProductId) throws Exception {
		Connection conn = DbUtil.getConnection();
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement("delete from swap_buy_product where id=?");
			ps.setInt(1, swayBuyProductId);
			ps.executeUpdate();
		} catch (Exception e) {
			log.error("删除换购商品时出现异常：", e);
			throw e;
		} finally {
			DbUtil.closeConnection(null, ps, conn);
		}
	}
	
}
