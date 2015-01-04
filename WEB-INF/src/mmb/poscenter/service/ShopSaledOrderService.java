package mmb.poscenter.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import mmb.poscenter.action.Page;
import mmb.poscenter.domain.Member;
import mmb.poscenter.domain.Product;
import mmb.poscenter.domain.ShopMoneyDestination;
import mmb.poscenter.domain.ShopMoneySource;
import mmb.poscenter.domain.ShopSaledOrder;
import mmb.poscenter.domain.ShopSaledOrderEvent;
import mmb.poscenter.domain.ShopSaledOrderProduct;
import mmboa.util.db.BaseService;
import mmboa.util.db.DbOperation;
import mmboa.util.db.DbUtil;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class ShopSaledOrderService  extends BaseService{
	
	private static Logger log = Logger.getLogger(ShopSaledOrderService.class);
	
	/**
	 * 批量插入店面销售订单列表
	 * @param list 店面销售订单列表
	 * @return 是否操作成功
	 */
	public boolean batInserData(List<ShopSaledOrder> list) {
		Connection conn = DbUtil.getConnection();
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		PreparedStatement ps_item = null;
		PreparedStatement ps_money = null;
		PreparedStatement ps_md = null;
		PreparedStatement ps_event = null;
		boolean sync = false;
		boolean oldAutoCommit = true;
		try {
			// 记录原来事务提交的方式
			oldAutoCommit = conn.getAutoCommit();
			// 关闭自动提交事务
			conn.setAutoCommit(false);
			ps = conn.prepareStatement("insert into shop_saled_order(saled_order_id,serial_number,pos_code,cashier_id,member_id,price,saled_time,order_type,shop_id) values(?,?,?,?,?,?,?,?,?)");
			ps_item = conn.prepareStatement("insert into shop_saled_order_product(product_id,saled_order_id,count,pre_price,order_type,event_remark,shop_id) values (?,?,?,?,?,?,?)");
			ps_money = conn.prepareStatement("insert into shop_money_source(order_id,type,money,swip_card_number,shop_id) values(?,?,?,?,?)");
			ps_md = conn.prepareStatement("insert into shop_money_destination(order_id,type,money,shop_id) values(?,?,?,?)");
			ps_event = conn.prepareStatement("insert into shop_saled_order_event(shop_event_id,saled_order_id,event_id,detail_event_id,ext_id,shop_id) values(?,?,?,?,?,?)");
			int shopId = 0;
			if (list.size() > 0) {
				ps2 = conn.prepareStatement(" select id from shop where code = '" + list.get(0).getShopCode() + "'");
				ResultSet rs = ps2.executeQuery();
				if (rs.next()) {
					shopId = rs.getInt("id");
				}
			}
			int count = 0;
			for (ShopSaledOrder sso : list) {
				ps.setInt(1, sso.getSaledOrderId());
				ps.setString(2, sso.getSerialNumber());
				ps.setString(3, sso.getPosCode());
				ps.setInt(4, sso.getCashierId());
				ps.setString(5, sso.getMemberId());
				ps.setDouble(6, sso.getPrice());
				ps.setTimestamp(7, sso.getSaledTime());
				ps.setInt(8, sso.getOrderType());
				ps.setInt(9, shopId);
				ps.addBatch();
				for (ShopSaledOrderProduct ssop : sso.getShopSaledOrderProduct()) {
					ps_item.setInt(1, ssop.getProductId());
					ps_item.setInt(2, ssop.getSaledOrderId());
					ps_item.setInt(3, ssop.getCount());
					ps_item.setDouble(4, ssop.getPrePrice());
					ps_item.setInt(5, ssop.getOrderType());
					ps_item.setString(6, ssop.getEventRemark());
					ps_item.setInt(7, shopId);
					ps_item.addBatch();
				}
				for (ShopMoneySource sms : sso.getShopMoneySource()) {
					if (sms == null) {
						continue;
					}
					ps_money.setInt(1, sms.getOrderId());
					ps_money.setInt(2, sms.getType());
					ps_money.setDouble(3, sms.getMoney());
					ps_money.setString(4, sms.getSwipCardNumber());
					ps_money.setInt(5, shopId);
					ps_money.addBatch();
				}
				//退款去向列表
				for (ShopMoneyDestination smd : sso.getShopMoneyDestinationList()) {
					ps_md.setInt(1, smd.getOrderId());
					ps_md.setInt(2, smd.getType());
					ps_md.setDouble(3, smd.getMoney());
					ps_md.setInt(4, shopId);
					ps_md.addBatch();
				}
				
				//店面活动列表
				for(ShopSaledOrderEvent ssoe : sso.getShopSaledOrderEventList()){
					ps_event.setInt(1, ssoe.getShopEventId());
					ps_event.setInt(2, ssoe.getSaledOrderId());
					ps_event.setInt(3, ssoe.getEventId());
					ps_event.setInt(4, ssoe.getDetailEventId());
					ps_event.setInt(5, ssoe.getExtId());
					ps_event.setInt(6, shopId);
					ps_event.addBatch();
					
				}
				
				count++;
				if (count % 100 == 0 || count == list.size()) {
					ps.executeBatch();
					ps_item.executeBatch();
					ps_money.executeBatch();
					ps_md.executeBatch();
					ps_event.executeBatch();
				}
			}
			conn.commit();
			sync = true;
		} catch (Exception e) {
			try {
				// 回滚事务
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			log.error("插入店面 销售订单信息出现异常：", e);
		} finally {
			DbUtil.closeConnection(null, ps, null);
			DbUtil.closeConnection(null, ps2, null);
			DbUtil.closeConnection(null, ps_item, null);
			DbUtil.closeConnection(null, ps_money, null);
			DbUtil.closeConnection(null, ps_md, null);
			DbUtil.closeConnection(null, ps_event, null);
			if (conn != null) {
				try {
					// 还原事务提交方式
					conn.setAutoCommit(oldAutoCommit);
					conn.close();
				} catch (SQLException e) {}
			}
		}
		return sync;
	}
	
	/**
	 * 解析店面店传来的字符串信息
	 * @param json
	 * @return
	 */
	public List<ShopSaledOrder>  parsePosadminJson(String json){
		 List<ShopSaledOrder> temp  =  new  ArrayList<ShopSaledOrder>(); 
		 Gson gson = new GsonBuilder().setDateFormat("yyyyMMdd HH:mm:ss").create();
		 temp = gson.fromJson(json, new TypeToken<List<ShopSaledOrder>>(){}.getType());
		 return temp;
	}

	/**
	 * 分页获取销售订单信息
	 * @param page 分页信息
	 * @param param 查询参数[shopName:店面名称；orderType:订单类型；memberName:会员姓名；serialNumber:订单号；payMethod:支付方式；swipCardNumber:刷卡流水号]
	 * @return
	 */
	public Page<ShopSaledOrder> getShopSaledOrderPage(Page<ShopSaledOrder> shopSaledOrderPage, Map<String, Object> param) {
		Connection conn = DbUtil.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			//查询条件
			StringBuilder condSql = new StringBuilder();
			int orderType = (Integer)param.get("orderType");
			String memberName = (String)param.get("memberName");
			String shopName = (String)param.get("shopName");
			String serialNumber = (String)param.get("serialNumber");
			Integer payMethod = (Integer)param.get("payMethod");
			String swipCardNumber = (String)param.get("swipCardNumber");
			condSql.append(" where sso.order_type=").append(orderType);
			if(StringUtils.isNotBlank(serialNumber)) {
				condSql.append(" and sso.serial_number like ? ");
			}
			if(StringUtils.isNotBlank(shopName)) {
				condSql.append(" and s.name like ? ");
			}
			if(StringUtils.isNotBlank(memberName)) {
				condSql.append(" and m.name like ? ");
			}
			
			//订单金钱来源明细查询条件
			StringBuilder msSql = new StringBuilder();
			if((payMethod!=null && payMethod!=0) || StringUtils.isNotBlank(swipCardNumber)) {
				msSql.append(" join shop_money_source ms on sso.saled_order_id=ms.order_id and sso.shop_id=ms.shop_id");
				if(payMethod!=null && payMethod!=0) {
					msSql.append(" and ms.type = ? ");
				}
				if(StringUtils.isNotBlank(swipCardNumber)) {
					msSql.append(" and ms.swip_card_number like ? ");
				}
			}
			
			//查询总记录数
			int index = 1;
			ps = conn.prepareStatement("select count(sso.id) from shop_saled_order sso left join member m on sso.member_id=m.id join shop s on sso.shop_id=s.id "+msSql + condSql);
			if((payMethod!=null && payMethod!=0) || StringUtils.isNotBlank(swipCardNumber)) {
				if(payMethod!=null && payMethod!=0) {
					ps.setInt(index++, payMethod);
				}
				if(StringUtils.isNotBlank(swipCardNumber)) {
					ps.setString(index++, "%"+swipCardNumber+"%");
				}
			}
			if(StringUtils.isNotBlank(serialNumber)) {
				ps.setString(index++, "%"+serialNumber+"%");
			}
			if(StringUtils.isNotBlank(shopName)) {
				ps.setString(index++, "%"+shopName+"%");
			}
			if(StringUtils.isNotBlank(memberName)) {
				ps.setString(index++, "%"+memberName+"%");
			}
			rs = ps.executeQuery();
		    if(rs.next()){
		    	shopSaledOrderPage.setTotalRecords(rs.getInt(1));
		    }
			
			//获取列表数据
			if(shopSaledOrderPage.getTotalRecords() > 0) {
				//获取每页显示的订单id列表
				List<Integer> orderIdList = new ArrayList<Integer>();
				StringBuilder sql = new StringBuilder();
				sql.append(" select sso.id orderId from shop_saled_order sso left join member m on sso.member_id=m.id join shop s on sso.shop_id=s.id");
				sql.append(msSql).append(condSql);
				sql.append(" order by sso.id desc");
				sql.append(" limit "+shopSaledOrderPage.getFirstResult()).append(",").append(shopSaledOrderPage.getPageCount());
				ps = conn.prepareStatement(sql.toString());
		    	index = 1;
		    	if((payMethod!=null && payMethod!=0) || StringUtils.isNotBlank(swipCardNumber)) {
					if(payMethod!=null && payMethod!=0) {
						ps.setInt(index++, payMethod);
					}
					if(StringUtils.isNotBlank(swipCardNumber)) {
						ps.setString(index++, "%"+swipCardNumber+"%");
					}
				}
		    	if(StringUtils.isNotBlank(serialNumber)) {
					ps.setString(index++, "%"+serialNumber+"%");
				}
		    	if(StringUtils.isNotBlank(shopName)) {
					ps.setString(index++, "%"+shopName+"%");
				}
		    	if(StringUtils.isNotBlank(memberName)) {
					ps.setString(index++, "%"+memberName+"%");
				}
		    	rs = ps.executeQuery();
				while(rs.next()){
					orderIdList.add(rs.getInt("orderId"));
				}
				
				//获取订单和订单商品信息
				List<ShopSaledOrder> orderList = this.getShopSaledOrderList(orderIdList);
				shopSaledOrderPage.setList(orderList);
			}
		}catch(Exception e){
			log.error("分页获取销售订单信息时出现异常：", e);
		}finally{
			DbUtil.closeConnection(rs, ps, conn);
		}
		return shopSaledOrderPage;
	}


	/**
	 * 根据订单ids获取销售订单和订单商品详细信息
	 * @param orderIdList 订单id列表
	 * @return
	 */
	private List<ShopSaledOrder> getShopSaledOrderList(List<Integer> orderIdList) {
		List<ShopSaledOrder> orderList = new ArrayList<ShopSaledOrder>();
		DbOperation db = new DbOperation();
		Connection conn = DbUtil.getConnection();
		PreparedStatement ps = null;
		try{
			//查询sql
			StringBuilder sql = new StringBuilder();
			sql.append("select sso.saled_order_id,sso.shop_id,sso.id orderId,sso.serial_number,sso.member_id,m.`name` memberName,sso.price,sso.order_type,sso.saled_time,ssop.id,ssop.count,ssop.pre_price,ssop.product_id,ssop.event_remark,p.`name` productName,s.name shopName");
			sql.append(" from shop_saled_order sso");
			sql.append(" join shop_saled_order_product ssop on sso.saled_order_id=ssop.saled_order_id and sso.shop_id = ssop.shop_id ");
			sql.append(" left join member m on sso.member_id=m.id");
			sql.append(" left join shop s on sso.shop_id=s.id");
			sql.append(" join product p on ssop.product_id=p.id");
			sql.append(" where sso.id in (");
			for(Integer id : orderIdList) {
				sql.append(id).append(",");
			}
			sql.deleteCharAt(sql.length()-1);
			sql.append(") ");
			sql.append(" order by sso.id desc");
			
			ResultSet rs = db.executeQuery(sql.toString());
			ShopSaledOrder shopSaledOrder = new ShopSaledOrder();
			int currentOrderId = -1;
			while(rs.next()){
				int orderId = rs.getInt("orderId");
				//新订单
				if(currentOrderId != orderId) {
					currentOrderId = orderId;
					//订单信息
					shopSaledOrder = new ShopSaledOrder();
					shopSaledOrder.setId(orderId);
					shopSaledOrder.setSerialNumber(rs.getString("serial_number"));
					shopSaledOrder.setMemberId(rs.getString("member_id"));
					shopSaledOrder.setOrderType(rs.getInt("order_type"));
					shopSaledOrder.setPrice(rs.getDouble("price"));
					shopSaledOrder.setSaledTime(rs.getTimestamp("saled_time"));
					shopSaledOrder.setSaledOrderId(rs.getInt("saled_order_id"));
					shopSaledOrder.setShopId(rs.getInt("shop_id"));
					shopSaledOrder.setShopName(rs.getString("shopName"));
					//会员信息
					Member member = new Member();
					member.setName(rs.getString("memberName"));
					shopSaledOrder.setMember(member);
					orderList.add(shopSaledOrder);
				}
				//订单商品信息
				ShopSaledOrderProduct ssop = new ShopSaledOrderProduct();
				ssop.setId(rs.getInt("id"));
				ssop.setProductId(rs.getInt("product_id"));
				ssop.setCount(rs.getInt("count"));
				ssop.setPrePrice(rs.getDouble("pre_price"));
				ssop.setEventRemark(rs.getString("event_remark"));
				//商品信息
				Product product = new Product();
				product.setName(rs.getString("productName"));
				ssop.setProduct(product);
				shopSaledOrder.getShopSaledOrderProduct().add(ssop);
			}
			
			//获取金钱来源的List
			sql = new StringBuilder();
			sql.append("select sso.id ssoid,ms.money,ms.type,ms.order_id,ms.swip_card_number from shop_money_source ms join shop_saled_order sso on sso.saled_order_id=ms.order_id and sso.shop_id=ms.shop_id and sso.id in(");
			for(Integer id : orderIdList) {
				sql.append(id).append(",");
			}
			sql.deleteCharAt(sql.length()-1);
			sql.append(") ");
			sql.append(" order by ms.type ");
			rs = db.executeQuery(sql.toString());
			ShopMoneySource ms = null;
			while(rs.next()){
				int orderId = rs.getInt("ssoid");
				ms = new ShopMoneySource();
				ms.setMoney(rs.getDouble("money"));
				ms.setOrderId(rs.getInt("order_id"));
				ms.setType(rs.getInt("type"));
				ms.setSwipCardNumber(rs.getString("swip_card_number"));
				for(ShopSaledOrder order : orderList) {
					if(orderId == order.getId()) {
						order.getShopMoneySource().add(ms);
					}
				}
			}
			
			//获取金钱去向列表
			sql = new StringBuilder();
			sql.append("select sso.id ssoid,md.id,md.order_id,md.type,md.money from shop_money_destination md join shop_saled_order sso on sso.saled_order_id=md.order_id and sso.shop_id=md.shop_id and sso.id in(");
			for(Integer id : orderIdList) {
				sql.append(id).append(",");
			}
			sql.deleteCharAt(sql.length()-1);
			sql.append(") ");
			sql.append(" order by md.type ");
			rs = db.executeQuery(sql.toString());
			ShopMoneyDestination md = null;
			while(rs.next()){
				int orderId = rs.getInt("ssoid");
				md = new ShopMoneyDestination();
				md.setId(rs.getInt("id"));
				md.setMoney(rs.getDouble("money"));
				md.setOrderId(rs.getInt("order_id"));
				md.setType(rs.getInt("type"));
				for(ShopSaledOrder order : orderList) {
					if(orderId == order.getId()) {
						order.getShopMoneyDestinationList().add(md);
					}
				}
			}
			
			
			
			
			
		   //获取活动信息列表
			Map<Set<Integer>,List<ShopSaledOrderEvent>> tmpmap = new HashMap<Set<Integer>,List<ShopSaledOrderEvent>>();
			Set<Integer> tmpset = null;
			List<ShopSaledOrderEvent> tmplist = null;
			ShopSaledOrderEvent tmpsoe = null;
			for(ShopSaledOrder order : orderList){
				ps = conn.prepareStatement("select ssoe.id,ssoe.shop_event_id,ssoe.saled_order_id,ssoe.event_id,ssoe.detail_event_id,ssoe.ext_id,ssoe.shop_id from shop_saled_order_event ssoe where ssoe.saled_order_id  = ? and ssoe.shop_id = ? ");
				ps.setInt(1,order.getSaledOrderId());
				ps.setInt(2,order.getShopId());
				rs = ps.executeQuery();
				while(rs.next()){
					tmpsoe = new ShopSaledOrderEvent();
					tmpsoe.setId(rs.getInt("id"));
					tmpsoe.setShopEventId(rs.getInt("shop_event_id"));
					tmpsoe.setSaledOrderId(rs.getInt("saled_order_id"));
					tmpsoe.setEventId(rs.getInt("event_id"));
					tmpsoe.setDetailEventId(rs.getInt("detail_event_id"));
					tmpsoe.setExtId(rs.getInt("ext_id"));
					tmpsoe.setShopId(rs.getInt("shop_id"));
					tmpset = new HashSet<Integer>();
					tmpset.add(rs.getInt("saled_order_id"));
					tmpset.add(rs.getInt("shop_id"));
					if(tmpmap.containsKey(tmpset)){
						tmpmap.get(tmpset).add(tmpsoe);
					}else{
						tmplist = new ArrayList<ShopSaledOrderEvent>();
						tmplist.add(tmpsoe);
						tmpmap.put(tmpset, tmplist);
					}
				}
				
			}
			
			for(ShopSaledOrder order : orderList) {
				tmpset  = new HashSet<Integer>();
				tmpset.add(order.getSaledOrderId());
				tmpset.add(order.getShopId());
			    order.setShopSaledOrderEventList(tmpmap.get(tmpset));
			}
			
		}catch(Exception e){
			log.error("根据订单ids获取销售订单和订单商品详细信息时出现异常：", e);
		}finally{
			DbUtil.closeConnection(null,ps,conn);
			this.release(db);
		}
		return orderList;
	}

}
