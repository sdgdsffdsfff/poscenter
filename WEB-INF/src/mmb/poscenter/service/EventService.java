package mmb.poscenter.service;

import java.io.StringReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mmb.poscenter.action.Page;
import mmb.poscenter.domain.Event;
import mmb.poscenter.domain.Shop;
import mmboa.util.db.BaseService;
import mmboa.util.db.DbUtil;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

public class EventService extends BaseService {
	
	private static Logger log = Logger.getLogger(EventService.class);
	
	/**
	 * 分页获取活动列表信息
	 * @param page 分页信息
	 * @param param [event:活动对象]
	 * @return
	 */
	public Page<Event> getEventPage(Page<Event> page, Map<String, Object> param) {
		Connection conn = DbUtil.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			//查询条件
			StringBuilder condSql = new StringBuilder();
			Event event = (Event) param.get("event");
			if(event.getUseStatus() != 0) {
				condSql.append(" and e.use_status=? ");
			}
			if(event.getType() != 0){
				condSql.append(" and e.type = ? ");
			}
			if(event.getStartTime() != null && event.getEndTime() != null ){
				condSql.append(" and ( e.start_time <= ? and e.end_time  >=  ? ) or ( e.start_time >= ? and e.end_time >= ? ) or ( e.start_time <= ? and e.end_time <= ? ) or ( e.start_time > =? and e.end_time <= ? )");
			}
			if(event.getStartTime() != null && event.getEndTime() == null ){
				condSql.append(" and e.end_time  >=  ? ");
			}
			if(event.getEndTime() != null && event.getStartTime() == null ){
				condSql.append(" and e.end_time  <=  ? ");
			}
       
			//查询总记录数
			int index = 1;
			ps = conn.prepareStatement("select count(e.id) from event e where 1=1 " + condSql);
			if(event.getUseStatus() != 0) {
				ps.setInt(index++, event.getUseStatus());
			}
			if(event.getType() != 0){
				ps.setInt(index++, event.getType());
			}
			if(event.getStartTime() != null && event.getEndTime() != null ){
				ps.setTimestamp(index++, event.getStartTime());
				ps.setTimestamp(index++, event.getEndTime());
				ps.setTimestamp(index++, event.getStartTime());
				ps.setTimestamp(index++, event.getEndTime());
				ps.setTimestamp(index++, event.getStartTime());
				ps.setTimestamp(index++, event.getEndTime());
				ps.setTimestamp(index++, event.getStartTime());
				ps.setTimestamp(index++, event.getEndTime());
			}
			if(event.getStartTime() != null && event.getEndTime() == null ){
				ps.setTimestamp(index++, event.getStartTime());
			}
			if(event.getEndTime() != null && event.getStartTime() == null ){
				ps.setTimestamp(index++, event.getEndTime());
			}
			rs = ps.executeQuery();
		    if(rs.next()){
		    	page.setTotalRecords(rs.getInt(1));
		    }
		    
		    //获取列表数据
		    if(page.getTotalRecords() > 0) {
		    	List<Event> list = new ArrayList<Event>();
		    	Event e;
		    	StringBuilder sql = new StringBuilder(50);
		    	sql.append("SELECT e.id,e.type,e.start_time,e.end_time,e.rule_desc,e.use_status from event e");
		    	sql.append(" where 1=1 ").append(condSql);
		    	sql.append(" order by e.id desc limit ");
		    	sql.append(page.getFirstResult()).append(",").append(page.getPageCount());
		    	ps = conn.prepareStatement(sql.toString());
		    	index = 1;
		    	if(event.getUseStatus() != 0) {
					ps.setInt(index++, event.getUseStatus());
				}
		    	if(event.getType() != 0){
					ps.setInt(index++, event.getType());
				}
		    	if(event.getStartTime() != null && event.getEndTime() != null ){
					ps.setTimestamp(index++, event.getStartTime());
					ps.setTimestamp(index++, event.getEndTime());
					ps.setTimestamp(index++, event.getStartTime());
					ps.setTimestamp(index++, event.getEndTime());
					ps.setTimestamp(index++, event.getStartTime());
					ps.setTimestamp(index++, event.getEndTime());
					ps.setTimestamp(index++, event.getStartTime());
					ps.setTimestamp(index++, event.getEndTime());
				}
				if(event.getStartTime() != null && event.getEndTime() == null ){
					ps.setTimestamp(index++, event.getStartTime());
				}
				if(event.getEndTime() != null && event.getStartTime() == null ){
					ps.setTimestamp(index++, event.getEndTime());
				}
		    	rs = ps.executeQuery();
		    	StringBuilder eventIds = new StringBuilder();
		    	while(rs.next()){
		    		e = new Event();
		    		e.setId(rs.getInt("id"));
		    		eventIds.append(rs.getInt("id")+",");
		    		e.setType(rs.getInt("type"));
		    		e.setStartTime(rs.getTimestamp("start_time"));
		    		e.setEndTime(rs.getTimestamp("end_time"));
		    		e.setRuleDesc(rs.getString("rule_desc"));
		    		e.setUseStatus(rs.getInt("use_status"));
		    		list.add(e);
		    	}
		    	//查询店面信息
		    	Map<Integer,List<Shop>>  map = new HashMap<Integer,List<Shop>>(); 
		    	if(eventIds.length() > 0 ){
		    		eventIds.deleteCharAt(eventIds.length()-1);
		    	    ps = conn.prepareStatement("select se.event_id,se.shop_id,s.`name` from shop_event se LEFT JOIN  shop s ON se.shop_id = s.id  where se.event_id IN ( "+eventIds+" )");
		    	    rs = ps.executeQuery();
		    	    Shop tempshop = null;
		    	    List<Shop> tmpshops = null;
		    	    while(rs.next()){
		    	    	tempshop = new Shop() ;
		    	        int eId = rs.getInt("event_id");
		    	        tempshop.setId(rs.getInt("shop_id"));
		    	        tempshop.setName(rs.getString("name"));
		    	         if(map.containsKey(eId)){
		    	        	 map.get(eId).add(tempshop);
		    	         }else{
		    	        	 tmpshops =  new ArrayList<Shop>();
		    	        	 tmpshops.add(tempshop);
		    	        	 map.put(eId, tmpshops);
		    	         }
		    	    }
		    	
		    	}
		    	
		    	//将店面信息赋值到相应的event
		    	for(Event evt : list){
		    		evt.setShopList(map.get(evt.getId()));
		    	}
		    	
		    	page.setList(list);
		    }
		}catch(Exception e){
			log.error("分页获取活动列表信息时出现异常：", e);
		}finally{
			DbUtil.closeConnection(rs, ps, conn);
		}
		return page;
	}

	/**
	 * 判断指定时间段中同一类型的活动是否唯一
	 * @param type 活动类型
	 * @param startTime 开始时间
	 * @param endTime 结束时间
	 * @param exceptEventId 排除的活动id
	 * @param shopList 适用店面列表
	 * @return
	 * @throws Exception 
	 */
	public boolean isUniqueEvent(int type, Timestamp startTime, Timestamp endTime, int exceptEventId, List<Shop> shopList) throws Exception {
		boolean isUnique = true;
		Connection conn = DbUtil.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			//查询条件
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT count(e.id) from `event` e join shop_event se on e.id=se.event_id ");
			sql.append(" and se.shop_id in (");
			for(Shop shop : shopList) {
				sql.append(shop.getId()).append(",");
			}
			sql.deleteCharAt(sql.length()-1).append(")");
			sql.append(" where e.use_status!=3 and e.type=? ");
			sql.append(" and ((e.start_time<? and e.end_time>?) or (e.start_time<? and e.end_time>?) or (e.start_time>? and e.end_time<?))");
			if(exceptEventId != 0) {
				sql.append(" and e.id != ?");
			}
			ps = conn.prepareStatement(sql.toString());
			ps.setInt(1, type);
			ps.setTimestamp(2, startTime);
			ps.setTimestamp(3, startTime);
			ps.setTimestamp(4, endTime);
			ps.setTimestamp(5, endTime);
			ps.setTimestamp(6, startTime);
			ps.setTimestamp(7, endTime);
			if(exceptEventId != 0) {
				ps.setInt(8, exceptEventId);
			}
			rs = ps.executeQuery();
		    if(rs.next()){
		    	isUnique = rs.getInt(1)==0;
		    }
		}catch(Exception e){
			log.error("判断指定时间段中同一类型的活动是否唯一时出现异常：", e);
			throw e;
		}finally{
			DbUtil.closeConnection(rs, ps, conn);
		}
		return isUnique;
	}

	/**
	 * 检查过期活动
	 * <br>修改过期活动的状态
	 * @throws Exception 
	 */
	public void checkOutdatedEvent() throws Exception {
		Connection conn = DbUtil.getConnection();
		PreparedStatement ps = null;
		try{
			ps = conn.prepareStatement("update `event` set use_status=3 where use_status=2 and end_time<?");
			ps.setTimestamp(1, new Timestamp(new Date().getTime()));
			ps.executeUpdate();
		}catch(Exception e){
			log.error("检查过期活动时出现异常：", e);
			throw e;
		}finally{
			DbUtil.closeConnection(null, ps, conn);
		}
	}
	
	/**
	 * 获取店面同步的活动列表数据
	 * @param shopId 店面id
	 * @param exceptEventIds 排除的活动ids
	 * @return
	 * @throws Exception 
	 */
	public List<Event> getSyncEventList(int shopId, List<Integer> exceptEventIds) throws Exception {
		List<Event> eventList = new ArrayList<Event>();
		Connection conn = DbUtil.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			//查询需要同步的活动
			List<Event> simpleEventList = new ArrayList<Event>();
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT e.id,e.type from `event` e join shop_event se on e.id=se.event_id and se.shop_id=? where e.use_status!=1 ");
			if(exceptEventIds!=null && !exceptEventIds.isEmpty()) {
				sql.append(" and e.id not in(");
				for(Integer id : exceptEventIds) {
					sql.append(id).append(",");
				}
				sql.deleteCharAt(sql.length()-1);
				sql.append(")");
			}
			ps = conn.prepareStatement(sql.toString());
			ps.setInt(1, shopId);
			rs = ps.executeQuery();
			Event event = null;
	    	while(rs.next()) {
	    		event = new Event();
	    		event.setId(rs.getInt("id"));
	    		event.setType(rs.getInt("type"));
	    		simpleEventList.add(event);
	    	}
	    	
	    	//获取所有活动的详细信息
	    	BuyGiftEventService es1 = new BuyGiftEventService();
	    	SwapBuyEventService es2 = new SwapBuyEventService();
	    	MoneyDiscountEventService es3 = new MoneyDiscountEventService();
	    	CountDiscountEventService es4 = new CountDiscountEventService();
	    	ComboEventService es5 = new ComboEventService();
	    	for(Event e : simpleEventList) {
	    		if(e.getType() == 1) {
	    			e = es1.getDetailEvent(e.getId());
	    		}else if(e.getType() == 2){
	    			e = es2.getDetailEvent(e.getId());
	    		}else if(e.getType() == 3){
	    			e = es3.getDetailEvent(e.getId());
	    		}else if(e.getType() == 4){
	    			e = es4.getDetailEvent(e.getId());
	    		}else if(e.getType() == 5){
	    			e = es5.getDetailEvent(e.getId());
	    		}
	    		e.setShopList(null);
	    		eventList.add(e);
	    	}
		}catch(Exception e){
			log.error("获取店面同步的活动列表数据时出现异常：", e);
			throw e;
		}finally{
			DbUtil.closeConnection(rs, ps, conn);
		}
		return eventList;
	}

	/**
	 * 店面同步促销活动
	 * @param json 店面请求数据
	 * @return
	 * @throws Exception 
	 */
	public String syncEvent(String json) throws Exception {
		String result = "";
		try {
			//解析请求JSON数据
			String shopCode = null;
			String eventIds = null;
			JsonReader jr = new JsonReader(new StringReader(json));
			jr.beginObject();
			String attrName = null;
			while (jr.hasNext()) {
				attrName = jr.nextName();
				if ("shopCode".equals(attrName)) {
					shopCode = jr.nextString();
				} else if ("eventIds".equals(attrName)) {
					eventIds = jr.nextString();
				}  
			}
			jr.endObject();
			
			//获取店面id
			Shop shop = new ShopService().getShopByCode(shopCode);
			
			//排除的活动ids
			List<Integer> exceptEventIds = new ArrayList<Integer>();
			if(StringUtils.isNotBlank(eventIds)) {
				for(String eventId : eventIds.split(",")) {
					exceptEventIds.add(Integer.parseInt(eventId));
				}
			}
			
			//获取店面同步的活动列表数据
			List<Event> eventList = this.getSyncEventList(shop.getId(), exceptEventIds);
			Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
			result = gson.toJson(eventList);
		} catch (Exception e) {
			log.error("店面同步促销活动时出现异常：", e);
			result = e.toString();
			throw e;
		}
		return result;
	}

}
