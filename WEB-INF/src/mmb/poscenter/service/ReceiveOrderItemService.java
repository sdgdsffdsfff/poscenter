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
import mmb.poscenter.domain.Invoice;
import mmb.poscenter.domain.ReceiveOrder;
import mmb.poscenter.domain.ReceiveOrderItem;
import mmboa.util.db.DbUtil;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

public class ReceiveOrderItemService {
	
	private static Logger log = Logger.getLogger(ReceiveOrderItemService.class);
	
	/**
	 * 批量插入数据库
	 * @param list
	 */
	public boolean batInsertItem(List<ReceiveOrderItem> list) {
		boolean success = false;
		Connection conn = DbUtil.getConnection();
		PreparedStatement ps = null;
		boolean oldAutoCommit = true;
		try {
			// 记录原来事物提交的方式
			oldAutoCommit = conn.getAutoCommit();
			// 关闭自动提交事务
			conn.setAutoCommit(false);

			// 批量插入
			if (list != null && !list.isEmpty()) {
				int count = 0;
				ps = conn
						.prepareStatement("insert into receive_order_item(`order_number`,`product_id`,`supplier_id`,`receive_count`) values (?,?,?,?)");
				for (ReceiveOrderItem roi : list) {
					ps.setInt(1, roi.getOrderId());
					ps.setInt(2, roi.getProductId());
					ps.setInt(3, roi.getSupplierId());
					ps.setInt(4, roi.getReceiveCount());
					ps.addBatch();
					count++;

					// 批量执行
					if (count % 100 == 0 || count == list.size()) {
						ps.executeBatch();
						conn.commit();
					}
				}
			}

			success = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
				}
			}
			if (conn != null) {
				try {
					// 还原事物提交方式
					conn.setAutoCommit(oldAutoCommit);
					conn.close();
				} catch (SQLException e) {
				}
			}
		}

		return success;
	}
	
	/**
	 * 确认收货
	 * @param receiveOrder 收货单对象
	 * @param itemList 收货单条目
	 * @throws Exception 
	 */
	public void confirmReceive(ReceiveOrder receiveOrder, List<ReceiveOrderItem> itemList) throws Exception {
		Connection conn = DbUtil.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean oldAutoCommit = true;
		try {
			// 记录原来事务提交的方式
			oldAutoCommit = conn.getAutoCommit();
			// 关闭自动提交事务
			conn.setAutoCommit(false);
			
			//更新收货单
			ps = conn.prepareStatement("update receive_order set order_number = ? , charger = ? , create_time = ? ,is_acquired = 1 where id = ? ");
			ps.setString(1, receiveOrder.getOrderNumber());
			ps.setString(2, receiveOrder.getCharger());
			ps.setTimestamp(3,  new Timestamp(new Date().getTime()));
			ps.setInt(4, receiveOrder.getId());
			ps.executeUpdate();
			
			// 批量更新收货单条目
			ps = conn.prepareStatement("update receive_order_item set receive_count = ? where product_id = ? and order_id = ? ");
			for (ReceiveOrderItem roi : itemList) {
				ps.setInt(1, roi.getReceiveCount());
				ps.setInt(2, roi.getProductId());
				ps.setInt(3, receiveOrder.getId());
				ps.addBatch();
			}
			ps.executeBatch();
			
			//获取收货前的库存信息，并生成进销存记录
			List<Invoice> invoiceList = new ArrayList<Invoice>();
			Invoice invoice = new Invoice();
			StringBuilder sql = new StringBuilder();
			sql.append("select id,stock from product where id in(");
			for(ReceiveOrderItem item : itemList) {
				sql.append(item.getProductId()).append(",");
			}
			sql.deleteCharAt(sql.length()-1).append(")");
        	ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while (rs.next()) {
				invoice = new Invoice();
				invoice.setOperType("0"); //0:收货
				invoice.setSerialNumber(receiveOrder.getOrderNumber());
				invoice.setProductId(rs.getInt("id"));
				invoice.setBeforeCount(rs.getInt("stock"));
				invoice.setAfterCount(invoice.getBeforeCount()); //处理多条条目中存在相同商品的问题
				for(ReceiveOrderItem item : itemList) {
					if(item.getProductId() == invoice.getProductId()) {
						invoice.setAfterCount(invoice.getAfterCount() + item.getReceiveCount());
						invoice.setCount(invoice.getCount() + item.getReceiveCount());
					}
				}
				invoiceList.add(invoice);
			}
			new InvoiceService().addBatInvoice(conn, invoiceList);
			
			//更新商品库存信息
			ps = conn.prepareStatement("update product set stock = stock + ? where id = ?");
			for(ReceiveOrderItem item : itemList) {
				ps.setInt(1, item.getReceiveCount());
				ps.setInt(2, item.getProductId());
				ps.addBatch();
			}
			ps.executeBatch();

			conn.commit();
		} catch (Exception e) {
			log.error("确认收货时出现异常：", e);
			throw e;
		} finally {
			DbUtil.closeConnection(rs, ps, conn, oldAutoCommit);
		}
	}
	
	
	public List<ReceiveOrderItem>  getReceiveOrderItemList(ReceiveOrderItem r){
		
		List<ReceiveOrderItem> temp = new ArrayList<ReceiveOrderItem>();
		ReceiveOrderItem roi = null;
		Connection conn = DbUtil.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			ps = conn.prepareStatement("SELECT r.receive_count,r.send_count,p.bar_code,p.name productName,s.name from receive_order_item r LEFT JOIN product p ON r.product_id = p.id LEFT JOIN supplier s on r.supplier_id = s.id where r.order_id = ?");
			ps.setInt(1, r.getOrderId());
			rs = ps.executeQuery();
			while(rs.next()){
				roi = new ReceiveOrderItem();
				roi.setReceiveCount(rs.getInt("receive_count"));
				roi.setSendCount(rs.getInt("send_count"));
				roi.setProductCode(rs.getString("bar_code"));
				roi.setProductName(rs.getString("productName"));
				roi.setSupplierName(rs.getString("name"));
				temp.add(roi);
			}
		}catch(Exception e){
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
				}
			}
			if (conn != null) {
				try {
					// 还原事物提交方式
					conn.close();
				} catch (SQLException e) {
				}
			}
		}
		
		
		return temp;
	}

	/**
	 * 分页获取收货单条目列表信息
	 * @param page 分页信息
	 * @param param 查询参数[receiveOrderId:收货单id；barCode:商品条形码；productName:商品名称；supplierName:供应商名称]
	 * @return
	 */
	public Page<ReceiveOrderItem> getReceiveOrderItemPage(Page<ReceiveOrderItem> page, Map<String, Object> param) {
		Connection conn = DbUtil.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			//查询条件
			StringBuilder condSql = new StringBuilder();
			Integer receiveOrderId = (Integer) param.get("receiveOrderId");
			String barCode = (String) param.get("barCode");
			String productName = (String) param.get("productName");
			String supplierName = (String) param.get("supplierName");
			condSql.append(" and i.order_id="+receiveOrderId);
			if(StringUtils.isNotBlank(barCode)) {
				condSql.append(" and p.`bar_code` like ? ");
			}
			if(StringUtils.isNotBlank(productName)) {
				condSql.append(" and p.`name` like ? ");
			}
			if(StringUtils.isNotBlank(supplierName)) {
				condSql.append(" and s.`name` like ? ");
			}
			
			//查询总记录数
			int index = 1;
			ps = conn.prepareStatement("select count(i.id) from receive_order_item i LEFT JOIN product p on i.product_id=p.id LEFT JOIN supplier s on i.supplier_id=s.id where 1=1 "+condSql);
			if(StringUtils.isNotBlank(barCode)) {
				ps.setString(index++, "%"+barCode+"%");
			}
			if(StringUtils.isNotBlank(productName)) {
				ps.setString(index++, "%"+productName+"%");
			}
			if(StringUtils.isNotBlank(supplierName)) {
				ps.setString(index++, "%"+supplierName+"%");
			}
			rs = ps.executeQuery();
			if(rs.next()){
		    	page.setTotalRecords(rs.getInt(1));
		    }
		    
		    //获取列表数据
		    if(page.getTotalRecords() > 0) {
		    	List<ReceiveOrderItem> list = new ArrayList<ReceiveOrderItem>();
		    	ReceiveOrderItem item;
		    	StringBuilder sql = new StringBuilder();
		    	sql.append("SELECT i.id,i.order_id,i.product_id,i.supplier_id,i.send_count,i.receive_count,p.`name` productName,p.bar_code,s.`name` supplierName");
		    	sql.append(" from receive_order_item i LEFT JOIN product p on i.product_id=p.id LEFT JOIN supplier s on i.supplier_id=s.id");
		    	sql.append(" where 1=1 ").append(condSql);
		    	sql.append(" limit ");
		    	sql.append(page.getFirstResult()).append(",").append(page.getPageCount());
		    	ps = conn.prepareStatement(sql.toString());
		    	index = 1;
		    	if(StringUtils.isNotBlank(barCode)) {
					ps.setString(index++, "%"+barCode+"%");
				}
				if(StringUtils.isNotBlank(productName)) {
					ps.setString(index++, "%"+productName+"%");
				}
				if(StringUtils.isNotBlank(supplierName)) {
					ps.setString(index++, "%"+supplierName+"%");
				}
		    	rs = ps.executeQuery();
		    	while(rs.next()){
		    		item = new ReceiveOrderItem();
		    		item.setId(rs.getInt("id"));
		    		item.setOrderId(rs.getInt("order_id"));
		    		item.setSendCount(rs.getInt("send_count"));
		    		item.setReceiveCount(rs.getInt("receive_count"));
		    		//商品信息
		    		item.setProductId(rs.getInt("product_id"));
		    		item.setProductCode(rs.getString("bar_code"));
		    		item.setProductName(rs.getString("productName"));
		    		//供应商信息
		    		item.setSupplierId(rs.getInt("supplier_id"));
		    		item.setSupplierName(rs.getString("supplierName"));
		    		list.add(item);
		    	}
		    	page.setList(list);
		    }
		}catch(Exception e){
			log.error("分页获取收货单条目列表信息时出现异常：", e);
		}finally{
			DbUtil.closeConnection(rs, ps, conn);
		}
		return page;
	}

}
