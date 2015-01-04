package mmb.poscenter.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import mmb.poscenter.action.Page;
import mmb.poscenter.domain.ReceiveOrder;
import mmb.poscenter.domain.ReceiveOrderItem;
import mmboa.util.db.BaseService;
import mmboa.util.db.DbOperation;
import mmboa.util.db.DbUtil;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

public class ReceiveOrderService extends BaseService {
	
	private static Logger log = Logger.getLogger(ReceiveOrderService.class);

	  /**
	   * 插入收货单信息
	   * @param ro
	   * @return
	   */
	  public boolean  insertOrder(ReceiveOrder ro){
		   boolean success = false;
		   Connection conn = DbUtil.getConnection();
		   PreparedStatement ps = null;
		   try{
			   ps = conn
				.prepareStatement("insert into receive_order(`order_number`,`charger`,`purchase_id`) values (?,?,?)");
			   ps.setString(1, ro.getOrderNumber());
			   ps.setString(2, ro.getCharger());
			   ps.setInt(3,ro.getPurchaseId());
			   ps.executeUpdate();
			   success = true;
		   }catch(Exception e){
			   e.printStackTrace();
		   }finally{
			   if (ps != null) {
					try {
						ps.close();
					} catch (SQLException e) {
					}
				}
				if (conn != null) {
					try {
						conn.close();
					} catch (SQLException e) {
					}
				}
		   }
		   return success;
	  }
	
	  
	  /**
	   * 更新收货单信息
	   * @param ro
	   * @return
	   */
	  public boolean  updateOrder(ReceiveOrder ro){
		   boolean success = false;
		   Connection conn = DbUtil.getConnection();
		   PreparedStatement ps = null;
		   try{
			   ps = conn
				.prepareStatement("update receive_order set create_time = ? ,order_number = ? ,charger = ? where purchase_id = ?");
			   ps.setTimestamp(1, new Timestamp(new Date().getTime()));
			   ps.setString(2, ro.getOrderNumber());
			   ps.setString(3,ro.getCharger());
			   ps.setInt(4,ro.getPurchaseId());
			   ps.executeUpdate();
			   success = true;
		   }catch(Exception e){
			   e.printStackTrace();
		   }finally{
			   if (ps != null) {
					try {
						ps.close();
					} catch (SQLException e) {
					}
				}
				if (conn != null) {
					try {
						conn.close();
					} catch (SQLException e) {
					}
				}
		   }
		   return success;
	  }
	  
	/**
	 * 收货单列表信息
	 * @param page 分页信息
	 * @param param [startTime:开始时间；endTime:结束时间；receiveOrder:收货单对象]
	 * @return
	 */
	public Page<ReceiveOrder> getReceiveOrderList(Page<ReceiveOrder> page, Map<String, Object> param) {
		Connection conn = DbUtil.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			//查询条件
			StringBuilder condSql = new StringBuilder();
			Timestamp startTime = (Timestamp) param.get("startTime");
			Timestamp endTime = (Timestamp) param.get("endTime");
			ReceiveOrder order = (ReceiveOrder) param.get("receiveOrder");
			if(StringUtils.isNotBlank(order.getOrderNumber())) {
				condSql.append(" and o.order_number like ? ");
			}
			if(StringUtils.isNotBlank(order.getCharger())) {
				condSql.append(" and o.charger like ? ");
			}
			if(order.getIsRequired() != -1) {
				condSql.append(" and o.is_acquired=? ");
			}
			if(startTime != null) {
				condSql.append(" and o.create_time>=? ");
			}
			if(endTime != null) {
				condSql.append(" and o.create_time<=? ");
			}
			
			//查询总记录数
			int index = 1;
			ps = conn.prepareStatement("select count(o.id) from receive_order o where 1=1 " + condSql);
			if(StringUtils.isNotBlank(order.getOrderNumber())) {
				ps.setString(index++, "%"+order.getOrderNumber()+"%");
			}
			if(StringUtils.isNotBlank(order.getCharger())) {
				ps.setString(index++, "%"+order.getCharger()+"%");
			}
			if(order.getIsRequired() != -1) {
				ps.setInt(index++, order.getIsRequired());
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
		    	List<ReceiveOrder> list = new ArrayList<ReceiveOrder>();
		    	ReceiveOrder ro;
		    	StringBuilder sql = new StringBuilder(50);
		    	sql.append("select o.id,o.order_number,o.charger,o.purchase_id,o.is_acquired,o.create_time from receive_order o");
		    	sql.append(" where 1=1 ").append(condSql);
		    	sql.append(" order by o.id desc limit ");
		    	sql.append(page.getFirstResult()).append(",").append(page.getPageCount());
		    	ps = conn.prepareStatement(sql.toString());
		    	index = 1;
		    	if(StringUtils.isNotBlank(order.getOrderNumber())) {
		    		ps.setString(index++, "%"+order.getOrderNumber()+"%");
				}
				if(StringUtils.isNotBlank(order.getCharger())) {
					ps.setString(index++, "%"+order.getCharger()+"%");
				}
				if(order.getIsRequired() != -1) {
					ps.setInt(index++, order.getIsRequired());
				}
				if(startTime != null) {
					ps.setTimestamp(index++, startTime);
				}
				if(endTime != null) {
					ps.setTimestamp(index++, endTime);
				}
		    	rs = ps.executeQuery();
		    	while(rs.next()){
		    		ro = new ReceiveOrder();
		    		ro.setId(rs.getInt("id"));
					ro.setOrderNumber(rs.getString("order_number"));
					ro.setCharger(rs.getString("charger"));
					ro.setPurchaseId(rs.getInt("purchase_id"));
					ro.setCreateTime(rs.getTimestamp("create_time"));
					ro.setIsRequired(rs.getInt("is_acquired"));
		    		list.add(ro);
		    	}
		    	page.setList(list);
		    }
		}catch(Exception e){
			log.error("分页获取采购单列表信息时出现异常：", e);
		}finally{
			DbUtil.closeConnection(rs, ps, conn);
		}
		return page;
	}
	  
		
		public ReceiveOrder getReceiveOrder(ReceiveOrder ro){
			   Connection conn = DbUtil.getConnection();
			   PreparedStatement ps = null;
			   ResultSet rs = null;
			   try{
				   ps = conn
					.prepareStatement("select r.order_number from receive_order r where r.id = ?");
				   ps.setInt(1, ro.getId());
				   rs =  ps.executeQuery();
				   if(rs.next()){
					   ro.setOrderNumber(rs.getString("order_number"));
				   }
			   }catch(Exception e){
				   e.printStackTrace();
			   }finally{
				   if (ps != null) {
						try {
							ps.close();
						} catch (SQLException e) {
						}
					}
					if (conn != null) {
						try {
							conn.close();
						} catch (SQLException e) {
						}
					}
			   }
			
			return ro;
		}
		
		/**
		 * 得到收货单商品待确认列表
		 * @param ro
		 * @return
		 */
		public List<ReceiveOrderItem> getReceiveOrderItem(ReceiveOrder ro){
			List<ReceiveOrderItem> tmp = new ArrayList<ReceiveOrderItem>();
			DbOperation db = new DbOperation("oa");
			ReceiveOrderItem roi;
			try{
		        StringBuilder sql = new StringBuilder(50);
		        sql.append("SELECT r.id,r.send_count,r.order_id,r.product_id,p.bar_code,p.name from receive_order_item r LEFT JOIN product p on r.product_id=p.id WHERE r.order_id ="+ro.id);
		        ResultSet rs = db.executeQuery(sql.toString());
		        while(rs.next()){
		        	roi = new ReceiveOrderItem();
				    roi.setId(rs.getInt("id"));
				    roi.setSendCount(rs.getInt("send_count"));
				    roi.setOrderId(rs.getInt("order_id"));
				    roi.setProductCode(rs.getString("bar_code"));
				    roi.setProductId(rs.getInt("product_id"));
				    roi.setProductName(rs.getString("name"));
				    tmp.add(roi);
				}
				
			}catch(Exception e){
				
			}finally{
				this.release(db);
			}
			return tmp;
			
		}
		
		public boolean saveReceiveOrderAndReceiveOrderItem(){
			
			
			return false;
		}
	
	
}
