package mmb.poscenter.service;

import java.io.StringReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import mmb.poscenter.action.Page;
import mmb.poscenter.domain.Invoice;
import mmb.poscenter.domain.SendOrder;
import mmb.poscenter.domain.SendOrderItem;
import mmb.poscenter.domain.Shop;
import mmboa.util.LogUtil;
import mmboa.util.db.BaseService;
import mmboa.util.db.DbOperation;
import mmboa.util.db.DbUtil;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.google.gson.stream.JsonReader;

public class SendOrderService extends BaseService {
	
	private static Logger log = Logger.getLogger(PurchaseOrderService.class);
	
	/**
	 * 分页获取发货单列表信息
	 * @param page 分页信息
	 * @param param [shopName:店面名称；startTime:开始时间；endTime:结束时间；sendOrder:发货单对象]
	 * @return
	 */
	public Page<SendOrder> getSendOrderPage(Page<SendOrder> page, Map<String, Object> param) {
		Connection conn = DbUtil.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			//查询条件
			StringBuilder condSql = new StringBuilder();
			String shopName = (String) param.get("shopName");
			Timestamp startTime = (Timestamp) param.get("startTime");
			Timestamp endTime = (Timestamp) param.get("endTime");
			SendOrder order = (SendOrder) param.get("sendOrder");
			if(StringUtils.isNotBlank(shopName)) {
				condSql.append(" and s.name like ? ");
			}
			if(StringUtils.isNotBlank(order.getOrderNumber())) {
				condSql.append(" and p.order_number like ? ");
			}
			if(StringUtils.isNotBlank(order.getCharger())) {
				condSql.append(" and p.charger like ? ");
			}
			if(order.getUseStatus() != -1) {
				condSql.append(" and p.use_status=? ");
			}
			if(startTime != null) {
				condSql.append(" and p.create_time>=? ");
			}
			if(endTime != null) {
				condSql.append(" and p.create_time<=? ");
			}
			
			//查询总记录数
			int index = 1;
			ps = conn.prepareStatement("select count(p.id) from send_order p left join shop s on p.shop_id=s.id where 1=1 " + condSql);
			if(StringUtils.isNotBlank(shopName)) {
				ps.setString(index++, "%"+shopName+"%");
			}
			if(StringUtils.isNotBlank(order.getOrderNumber())) {
				ps.setString(index++, "%"+order.getOrderNumber()+"%");
			}
			if(StringUtils.isNotBlank(order.getCharger())) {
				ps.setString(index++, "%"+order.getCharger()+"%");
			}
			if(order.getUseStatus() != -1) {
				ps.setInt(index++, order.getUseStatus());
			}
			if(startTime != null) {
				ps.setTimestamp(index++, startTime);
			}
			if(endTime != null) {
				ps.setTimestamp(index++, endTime);
			}
			rs = ps.executeQuery();
		    if(rs.next()){
		    	page.setTotalRecords(rs.getInt(1));
		    }
		    
		    //获取列表数据
		    if(page.getTotalRecords() > 0) {
		    	List<SendOrder> list = new ArrayList<SendOrder>();
		    	SendOrder p;
		    	StringBuilder sql = new StringBuilder(50);
		    	sql.append("SELECT p.id,p.order_number,p.charger,p.create_time,p.use_status,p.department,p.shop_id,s.name shopName from send_order p left join shop s on p.shop_id=s.id");
		    	sql.append(" where 1=1 ").append(condSql);
		    	sql.append(" order by p.id desc limit ");
		    	sql.append(page.getFirstResult()).append(",").append(page.getPageCount());
		    	ps = conn.prepareStatement(sql.toString());
		    	index = 1;
		    	if(StringUtils.isNotBlank(shopName)) {
					ps.setString(index++, "%"+shopName+"%");
				}
		    	if(StringUtils.isNotBlank(order.getOrderNumber())) {
		    		ps.setString(index++, "%"+order.getOrderNumber()+"%");
				}
				if(StringUtils.isNotBlank(order.getCharger())) {
					ps.setString(index++, "%"+order.getCharger()+"%");
				}
				if(order.getUseStatus() != -1) {
					ps.setInt(index++, order.getUseStatus());
				}
				if(startTime != null) {
					ps.setTimestamp(index++, startTime);
				}
				if(endTime != null) {
					ps.setTimestamp(index++, endTime);
				}
		    	rs = ps.executeQuery();
		    	while(rs.next()){
		    		p = new SendOrder();
		    		p.setId(rs.getInt("id"));
		    		p.setOrderNumber(rs.getString("order_number"));
		    		p.setCharger(rs.getString("charger"));
		    		p.setCreateTime(rs.getTimestamp("create_time"));
		    		p.setUseStatus(rs.getInt("use_status"));
		    		p.setDepartment(rs.getString("department"));
		    		//店面信息
					Shop shop = new Shop();
					shop.setId(rs.getInt("shop_id"));
					shop.setName(rs.getString("shopName"));
					p.setShopId(shop.getId());
					p.setShop(shop);
		    		list.add(p);
		    	}
		    	page.setList(list);
		    }
		}catch(Exception e){
			log.error("分页获取发货单列表信息时出现异常：", e);
		}finally{
			DbUtil.closeConnection(rs, ps, conn);
		}
		return page;
	}

	/**
	 * 根据发货单id获取发货单对象信息
	 * @param id 发货单id
	 * @return
	 */
	public SendOrder getSendOrderById(int id) {
		return (SendOrder) this.getXXX("`id`="+id, "send_order", SendOrder.class.getName());
	}
	
	/**
	 * 根据发货单id获取发货单对象的详细信息（包括店面信息）
	 * @param id 发货单id
	 * @return
	 */
	public SendOrder getDetailById(int id) {
		DbOperation db = new DbOperation();
		SendOrder order = null;
		try{
			//查询
			String sql = "SELECT p.id,p.order_number,p.charger,p.create_time,p.use_status,p.department,p.shop_id,s.name shopName" +
					" from send_order p left join shop s on p.shop_id=s.id " +
					" where p.id="+id;
			ResultSet rs = db.executeQuery(sql);
			if(rs.next()){
				order = new SendOrder();
				order.setId(rs.getInt("id"));
				order.setOrderNumber(rs.getString("order_number"));
				order.setCharger(rs.getString("charger"));
				order.setCreateTime(rs.getTimestamp("create_time"));
				order.setUseStatus(rs.getInt("use_status"));
				order.setDepartment(rs.getString("department"));
				//店面信息
				Shop shop = new Shop();
				shop.setId(rs.getInt("shop_id"));
				shop.setName(rs.getString("shopName"));
				order.setShopId(shop.getId());
				order.setShop(shop);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			db.release();
		}
		return order;
	}
	
	/**
	 * 更新发货单信息
	 * @param sendOrder 发货单对象
	 * @return
	 */
	public boolean updateSendOrder(SendOrder sendOrder) {
		//更新语句
		StringBuilder set = new StringBuilder();
		set.append("`order_number`='").append(sendOrder.getOrderNumber()).append("', ");
		set.append("`shop_id`='").append(sendOrder.getShopId()).append("', ");
		set.append("`charger`='").append(sendOrder.getCharger()).append("', ");
		set.append("`department`='").append(sendOrder.getDepartment()).append("', ");
		set.append("`use_status`=").append(sendOrder.getUseStatus());
		
		return this.updateXXX(set.toString(), "`id`="+sendOrder.getId(), "send_order");
	}
	
	/**
	 * 删除发货单信息，同时删除该发货单下的所有条目
	 * @param id 发货单id
	 * @return
	 */
	public boolean deleteSendOrderById(int id) {
		boolean success = false;
		Connection conn = DbUtil.getConnection();
		PreparedStatement ps = null;
		boolean oldAutoCommit = true;
		try {
			//记录原来事务提交的方式
			oldAutoCommit = conn.getAutoCommit();
			//关闭自动提交事务
			conn.setAutoCommit(false);
			
			//删除发货单条目数据
			ps = conn.prepareStatement("delete from send_order_item where send_order_id=?");
			ps.setInt(1, id);
			ps.executeUpdate();
			
			//删除发货单数据
			ps = conn.prepareStatement("delete from send_order where id=?");
			ps.setInt(1, id);
			ps.executeUpdate();
			
        	conn.commit();
			success = true;
		} catch (Exception e) {
			try {
				//回滚事务
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
			LogUtil.logAccess("删除发货单时出现错误："+e.getMessage());
		} finally {
			DbUtil.closeConnection(null, ps, null);
			if(conn != null){
				try {
					//还原事务提交方式
					conn.setAutoCommit(oldAutoCommit);
					conn.close();
				} catch (SQLException e) {
				}
			}
		}
		return success;
	}

	/**
	 * 提交发货单
	 * @param id 发货单id
	 * @throws Exception 
	 */
	public void submitSendOrder(int id) throws Exception {
		Connection conn = DbUtil.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean oldAutoCommit = true;
		try {
			//记录原来事务提交的方式
			oldAutoCommit = conn.getAutoCommit();
			//关闭自动提交事务
			conn.setAutoCommit(false);
			
			//更新发货单的状态字段
			ps = conn.prepareStatement("update send_order set use_status=1 where id=?");
			ps.setInt(1, id);
			ps.executeUpdate();
			
			//获取发货单信息
			SendOrder sendOrder = this.getSendOrderById(id);
			
			//获取所有发货单条目
			SendOrderItemService sois = new SendOrderItemService();
			List<SendOrderItem> itemList = sois.getAllItemListByOrderId(id);
			
			//获取提交发货单前的库存信息，并生成进销存记录
			List<Invoice> invoiceList = new ArrayList<Invoice>();
			Invoice invoice = new Invoice();
			StringBuilder sql = new StringBuilder();
			sql.append("select id,stock from product where id in(");
			for(SendOrderItem item : itemList) {
				sql.append(item.getProductId()).append(",");
			}
			sql.deleteCharAt(sql.length()-1).append(")");
        	ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while (rs.next()) {
				invoice = new Invoice();
				invoice.setOperType("1"); //1:发货
				invoice.setSerialNumber(sendOrder.getOrderNumber());
				invoice.setProductId(rs.getInt("id"));
				invoice.setBeforeCount(rs.getInt("stock"));
				invoice.setAfterCount(invoice.getBeforeCount()); //处理多条条目中存在相同商品的问题
				for(SendOrderItem item : itemList) {
					if(item.getProductId() == invoice.getProductId()) {
						invoice.setAfterCount(invoice.getAfterCount() - item.getCount());
						invoice.setCount(invoice.getCount() + item.getCount());
					}
				}
				invoiceList.add(invoice);
			}
			new InvoiceService().addBatInvoice(conn, invoiceList);
			
			//更新商品库存信息
			ps = conn.prepareStatement("update product set stock = stock - ? where id = ?");
			for(SendOrderItem item : itemList) {
				ps.setInt(1, item.getCount());
				ps.setInt(2, item.getProductId());
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
			log.error("提交发货单时出现错误：", e);
			throw e;
		} finally {
			DbUtil.closeConnection(rs, ps, conn, oldAutoCommit);
		}
	}
	
	/**
	 * 获取指定店面的所有已提交的发货单
	 * @param shopCode 店面编号
	 * @return
	 */
	public List<SendOrder> getSubmitSendOrderList(String shopCode) {
		DbOperation db = new DbOperation();
		List<SendOrder> list = new ArrayList<SendOrder>();
		try{
			String sql = "SELECT o.id,o.order_number,i.id itemId,i.product_id,i.count " +
					" from send_order o join send_order_item i on o.id=i.send_order_id join shop s on o.shop_id=s.id and s.code='"+shopCode+"'" +
					" where o.use_status=1 ORDER BY o.id";
			ResultSet rs = db.executeQuery(sql);
		    SendOrder p = null;
		    rs = db.executeQuery(sql.toString());
		    int nowOrderId = 0;
		    while(rs.next()){
		    	int orderId = rs.getInt("id");
		    	//新订单
		    	if(nowOrderId != orderId) {
		    		if(nowOrderId != 0) {
		    			list.add(p);
		    		}
		    		p = new SendOrder();
		    		p.setItemList(new ArrayList<SendOrderItem>());
		    		p.setId(orderId);
		    		p.setOrderNumber(rs.getString("order_number"));
		    		nowOrderId = orderId;
		    	}
		    	//订单条目
		    	SendOrderItem item = new SendOrderItem();
		    	item.setId(rs.getInt("itemId"));
		    	item.setProductId(rs.getInt("product_id"));
		    	item.setCount(rs.getInt("count"));
		    	p.getItemList().add(item);
		    	
		    	if(rs.isLast()) {
		    		list.add(p);
		    	}
		    }
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			db.release();
		}
		return list;
	}

	/**
	 * posadmin同步发货单信息
	 * @return 发货单数据
	 * @throws Exception 
	 */
	public String syncSendOrder(String json) throws Exception {
		//解析json字符串
		String shopCode = null;
		JsonReader jr = new JsonReader(new StringReader(json));
		jr.beginObject();
		String attrName = null;
		while (jr.hasNext()) {
			attrName = jr.nextName();
			if ("shopCode".equals(attrName)) {
				shopCode = jr.nextString();
			}
		}
		jr.endObject();
		
		//获取所有已提交的发货单
		List<SendOrder> orderList = this.getSubmitSendOrderList(shopCode);
		
		//拼装JSON字符串
		StringBuilder jsonBack = new StringBuilder();
		jsonBack.append("{\"sendOrderList\":[");
		for(int i=0; i<orderList.size(); i++) {
			SendOrder order = orderList.get(i);
			jsonBack.append(i>0 ? "," : "");
			jsonBack.append("{\"sendOrderId\":"+order.getId()+", \"orderNumber\":\""+order.getOrderNumber()+"\", \"items\":[");
			for(int j=0; j<order.getItemList().size(); j++) {
				SendOrderItem item = order.getItemList().get(j);
				jsonBack.append(j>0 ? "," : "");
				jsonBack.append("{\"id\":"+item.getId()+",\"productId\":"+item.getProductId()+",\"count\":"+item.getCount()+"}");
			}
			jsonBack.append("]}");
		}
		jsonBack.append("]}");
		
		return jsonBack.toString();
	}

	/**
	 * posadmin同步发货单状态信息
	 * @param json
	 * @return
	 */
	public boolean syncSendOrderStatus(String json) {
		boolean success = false;
		Connection conn = DbUtil.getConnection();
		PreparedStatement ps = null;
		boolean oldAutoCommit = true;
		try {
			//记录原来事务提交的方式
			oldAutoCommit = conn.getAutoCommit();
			//关闭自动提交事务
			conn.setAutoCommit(false);
			
			//解析json字符串
			List<Integer> orderIdList = this.doSendOrderStatusJson(json);
			
			//更新发货单的状态字段
			ps = conn.prepareStatement("update send_order set use_status=2 where id=?");
			for(Integer orderId : orderIdList) {
				ps.setInt(1, orderId);
				ps.addBatch();
			}
			ps.executeBatch();
			
        	conn.commit();
			success = true;
		} catch (Exception e) {
			try {
				//回滚事务
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			log.error("posadmin同步发货单状态信息时出现异常：", e);
		} finally {
			DbUtil.closeConnection(null, ps, null);
			if(conn != null){
				try {
					//还原事务提交方式
					conn.setAutoCommit(oldAutoCommit);
					conn.close();
				} catch (SQLException e) {}
			}
		}
		return success;
	}
	
	/**
	 * 解析json格式的posadmin同步发货单状态信息
	 * @param json
	 * @return
	 */
	private List<Integer> doSendOrderStatusJson(String json) {
		List<Integer> orderIdList = new ArrayList<Integer>();
		try {
			
			StringReader sr = new StringReader(json);
			JsonReader jr = new JsonReader(sr);
			jr.beginObject();
			String attrName = null;
			while (jr.hasNext()) {
				attrName = jr.nextName();
				if ("sendOrderIdList".equals(attrName)) {
					jr.beginArray();
					while (jr.hasNext()) {
						jr.beginObject();
						while (jr.hasNext()) {
							attrName = jr.nextName();
							if ("sendOrderId".equals(attrName)) { //商品id
								orderIdList.add(jr.nextInt());
							}
						}
						jr.endObject();
					}
					jr.endArray();
				}
			}
			jr.endObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return orderIdList;
	}

}
