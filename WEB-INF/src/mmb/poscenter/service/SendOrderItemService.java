package mmb.poscenter.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import mmb.poscenter.action.Page;
import mmb.poscenter.domain.Product;
import mmb.poscenter.domain.SendOrderItem;
import mmboa.util.db.BaseService;
import mmboa.util.db.DbOperation;
import mmboa.util.db.DbUtil;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

public class SendOrderItemService extends BaseService {
	
	private static Logger log = Logger.getLogger(SendOrderItemService.class);
	
	/**
	 * 分页获取发货单条目列表信息
	 * @param page 分页信息
	 * @param param 查询参数[productName:商品名称；sendOrderId:发货单id]
	 * @return
	 */
	public Page<SendOrderItem> getSendOrderItemPage(Page<SendOrderItem> page, Map<String, Object> param) {
		Connection conn = DbUtil.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			//查询条件
			StringBuilder condSql = new StringBuilder();
			Integer sendOrderId = (Integer) param.get("sendOrderId");
			String productName = (String) param.get("productName");
			condSql.append(" and i.send_order_id="+sendOrderId);
			if(StringUtils.isNotBlank(productName)) {
				condSql.append(" and p.`name` like ? ");
			}
			
			//查询总记录数
			int index = 1;
			ps = conn.prepareStatement("select count(i.id) from send_order_item i LEFT JOIN product p on i.product_id=p.id where 1=1 "+condSql);
			if(StringUtils.isNotBlank(productName)) {
				ps.setString(index++, "%"+productName+"%");
			}
			rs = ps.executeQuery();
			if(rs.next()){
		    	page.setTotalRecords(rs.getInt(1));
		    }
			
		    //获取列表数据
		    if(page.getTotalRecords() > 0) {
		    	List<SendOrderItem> list = new ArrayList<SendOrderItem>();
		    	SendOrderItem item;
		    	StringBuilder sql = new StringBuilder(50);
		    	sql.append("SELECT i.id,i.send_order_id,i.product_id,i.count,p.`name` productName");
		    	sql.append(" from send_order_item i LEFT JOIN product p on i.product_id=p.id ");
		    	sql.append(" where 1=1 ").append(condSql);
		    	sql.append(" limit ");
		    	sql.append(page.getFirstResult()).append(",").append(page.getPageCount());
		    	ps = conn.prepareStatement(sql.toString());
		    	index = 1;
				if(StringUtils.isNotBlank(productName)) {
					ps.setString(index++, "%"+productName+"%");
				}
		    	rs = ps.executeQuery();
		    	while(rs.next()){
		    		item = new SendOrderItem();
		    		item.setId(rs.getInt("id"));
		    		item.setSendOrderId(rs.getInt("send_order_id"));
		    		item.setCount(rs.getInt("count"));
		    		//商品信息
		    		Product p = new Product();
		    		p.setId(rs.getInt("product_id"));
		    		p.setName(rs.getString("productName"));
		    		item.setProductId(p.getId());
		    		item.setProduct(p);
		    		list.add(item);
		    	}
		    	page.setList(list);
		    }
		}catch(Exception e){
			log.error("分页获取发货单条目列表信息时出现异常：", e);
		}finally{
			DbUtil.closeConnection(rs, ps, conn);
		}
		return page;
	}
	
	/**
	 * 根据发货单id获取所有发货单条目
	 * @param sendOrderId 发货单id
	 * @return
	 */
	public List<SendOrderItem> getAllItemListByOrderId(int sendOrderId) {
		DbOperation db = new DbOperation();
		List<SendOrderItem> list = new ArrayList<SendOrderItem>();
		try{
		    //获取列表数据
		    SendOrderItem item;
		    String sql = "SELECT i.id,i.send_order_id,i.product_id,i.count from send_order_item i where i.send_order_id="+sendOrderId;
		    ResultSet rs = db.executeQuery(sql);
		    while(rs.next()){
		    	item = new SendOrderItem();
		    	item.setId(rs.getInt("id"));
		    	item.setSendOrderId(rs.getInt("send_order_id"));
		    	item.setCount(rs.getInt("count"));
		    	item.setProductId(rs.getInt("product_id"));
		    	list.add(item);
		    }
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			db.release();
		}
		return list;
	}

	/**
	 * 根据发货单条目id获取发货单条目对象信息
	 * @param id 发货单条目id
	 * @return
	 */
	public SendOrderItem getSendOrderItemById(int id) {
		return (SendOrderItem) this.getXXX("`id`="+id, "send_order_item", SendOrderItem.class.getName());
	}
	
	/**
	 * 根据发货单条目id获取发货单条目详细信息（包括商品信息、供应商信息）
	 * @param id 发货单条目id
	 * @return
	 */
	public SendOrderItem getDetailById(int id) {
		DbOperation db = new DbOperation();
		SendOrderItem item = null;
		try{
			String sql = "SELECT i.id,i.send_order_id,i.product_id,i.count,p.`name` productName" +
					" from send_order_item i LEFT JOIN product p on i.product_id=p.id where i.id="+id;
			ResultSet rs = db.executeQuery(sql);
		    if(rs.next()){
		    	item = new SendOrderItem();
	    		item.setId(rs.getInt("id"));
	    		item.setSendOrderId(rs.getInt("send_order_id"));
	    		item.setCount(rs.getInt("count"));
	    		//商品信息
	    		Product p = new Product();
	    		p.setId(rs.getInt("product_id"));
	    		p.setName(rs.getString("productName"));
	    		item.setProductId(p.getId());
	    		item.setProduct(p);
		    }
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			db.release();
		}
		return item;
	}
	
	/**
	 * 更新发货单条目信息
	 * @param sendOrderItem 发货单条目对象
	 * @return
	 */
	public boolean updateSendOrderItem(SendOrderItem sendOrderItem) {
		//更新语句
		StringBuilder set = new StringBuilder();
		set.append("`product_id`=").append(sendOrderItem.getProductId()).append(", ");
		set.append("`count`=").append(sendOrderItem.getCount());
		
		return this.updateXXX(set.toString(), "`id`="+sendOrderItem.getId(), "send_order_item");
	}
	
	/**
	 * 删除发货单条目信息
	 * @param id 发货单条目id
	 * @return
	 */
	public boolean deleteSendOrderItemById(int id) {
		return this.deleteXXX("`id`="+id, "send_order_item");
	}

}
