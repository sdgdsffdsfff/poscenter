package mmb.poscenter.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import mmb.poscenter.action.Page;
import mmb.poscenter.domain.Product;
import mmb.poscenter.domain.PurchaseOrderItem;
import mmb.poscenter.domain.Supplier;
import mmboa.util.db.BaseService;
import mmboa.util.db.DbOperation;
import mmboa.util.db.DbUtil;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

public class PurchaseOrderItemService extends BaseService {
	
	private static Logger log = Logger.getLogger(PurchaseOrderItemService.class);
	
	/**
	 * 分页获取采购单条目列表信息
	 * @param page 分页信息
	 * @param param 查询参数[productName:商品名称；supplierName:供应商名称]
	 * @return
	 */
	public Page<PurchaseOrderItem> getPurchaseOrderItemPage(Page<PurchaseOrderItem> page, Map<String, Object> param) {
		Connection conn = DbUtil.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			//查询条件
			StringBuilder condSql = new StringBuilder();
			Integer purchaseOrderId = (Integer) param.get("purchaseOrderId");
			String productName = (String) param.get("productName");
			String supplierName = (String) param.get("supplierName");
			condSql.append(" and i.purchase_order_id="+purchaseOrderId);
			if(StringUtils.isNotBlank(productName)) {
				condSql.append(" and p.`name` like ? ");
			}
			if(StringUtils.isNotBlank(supplierName)) {
				condSql.append(" and s.`name` like ? ");
			}
			
			//查询总记录数
			int index = 1;
			ps = conn.prepareStatement("select count(i.id) from purchase_order_item i LEFT JOIN product p on i.product_id=p.id LEFT JOIN supplier s on i.supplier_id=s.id where 1=1 "+condSql);
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
		    	List<PurchaseOrderItem> list = new ArrayList<PurchaseOrderItem>();
		    	PurchaseOrderItem item;
		    	StringBuilder sql = new StringBuilder();
		    	sql.append("SELECT i.id,i.purchase_order_id,i.product_id,i.supplier_id,i.count,i.purchase_price,p.`name` productName,s.`name` supplierName");
		    	sql.append(" from purchase_order_item i LEFT JOIN product p on i.product_id=p.id LEFT JOIN supplier s on i.supplier_id=s.id");
		    	sql.append(" where 1=1 ").append(condSql);
		    	sql.append(" limit ");
		    	sql.append(page.getFirstResult()).append(",").append(page.getPageCount());
		    	ps = conn.prepareStatement(sql.toString());
		    	index = 1;
				if(StringUtils.isNotBlank(productName)) {
					ps.setString(index++, "%"+productName+"%");
				}
				if(StringUtils.isNotBlank(supplierName)) {
					ps.setString(index++, "%"+supplierName+"%");
				}
		    	rs = ps.executeQuery();
		    	while(rs.next()){
		    		item = new PurchaseOrderItem();
		    		item.setId(rs.getInt("id"));
		    		item.setPurchaseOrderId(rs.getInt("purchase_order_id"));
		    		item.setCount(rs.getInt("count"));
		    		item.setPurchasePrice(rs.getDouble("purchase_price"));
		    		//商品信息
		    		Product p = new Product();
		    		p.setId(rs.getInt("product_id"));
		    		p.setName(rs.getString("productName"));
		    		item.setProductId(p.getId());
		    		item.setProduct(p);
		    		//供应商信息
		    		Supplier s = new Supplier();
		    		s.setId(rs.getInt("supplier_id"));
		    		s.setName(rs.getString("supplierName"));
		    		item.setSupplierId(s.getId());
		    		item.setSupplier(s);
		    		list.add(item);
		    	}
		    	page.setList(list);
		    }
		}catch(Exception e){
			log.error("分页获取采购单条目列表信息时出现异常：", e);
		}finally{
			DbUtil.closeConnection(rs, ps, conn);
		}
		return page;
	}
	
	/**
	 * 根据采购单id获取所有采购单条目
	 * @param orderId 采购单id
	 * @return
	 */
	public List<PurchaseOrderItem> getAllItemListByOrderId(int orderId) {
		DbOperation db = new DbOperation();
		List<PurchaseOrderItem> list = new ArrayList<PurchaseOrderItem>();
		try{
		    //获取列表数据
			PurchaseOrderItem item;
		    String sql = "SELECT i.id,i.purchase_order_id,i.product_id,i.supplier_id,i.count,i.purchase_price" +
		    		" from purchase_order_item i where i.purchase_order_id="+orderId;
		    ResultSet rs = db.executeQuery(sql);
		    while(rs.next()){
		    	item = new PurchaseOrderItem();
	    		item.setId(rs.getInt("id"));
	    		item.setPurchaseOrderId(rs.getInt("purchase_order_id"));
	    		item.setCount(rs.getInt("count"));
	    		item.setPurchasePrice(rs.getDouble("purchase_price"));
	    		item.setProductId(rs.getInt("product_id"));
	    		item.setSupplierId(rs.getInt("supplier_id"));
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
	 * 根据采购单id获取采购单条目数量
	 * @param orderId 采购单id
	 * @return
	 */
	public int getItemCountByOrderId(int orderId) {
		DbOperation db = new DbOperation();
		int itemCount = 0;
		try {
			String sql = "SELECT count(i.id) from purchase_order_item i where i.purchase_order_id=" + orderId;
			ResultSet rs = db.executeQuery(sql);
			if (rs.next()) {
				itemCount = rs.getInt(1);
			}
		} catch (Exception e) {
			log.error("根据采购单id获取采购单条目数量时出现异常：", e);
		} finally {
			db.release();
		}
		return itemCount;
	}

	/**
	 * 根据采购单条目id获取采购单条目对象信息
	 * @param id 采购单条目id
	 * @return
	 */
	public PurchaseOrderItem getPurchaseOrderItemById(int id) {
		return (PurchaseOrderItem) this.getXXX("`id`="+id, "purchase_order_item", PurchaseOrderItem.class.getName());
	}
	
	/**
	 * 根据采购单条目id获取采购单条目详细信息（包括商品信息、供应商信息）
	 * @param id 采购单条目id
	 * @return
	 */
	public PurchaseOrderItem getDetailById(int id) {
		DbOperation db = new DbOperation();
		PurchaseOrderItem item = null;
		try{
			String sql = "SELECT i.id,i.purchase_order_id,i.product_id,i.supplier_id,i.count,i.purchase_price,p.`name` productName,s.`name` supplierName" +
					" from purchase_order_item i LEFT JOIN product p on i.product_id=p.id LEFT JOIN supplier s on i.supplier_id=s.id where i.id="+id;
			ResultSet rs = db.executeQuery(sql);
		    if(rs.next()){
		    	item = new PurchaseOrderItem();
	    		item.setId(rs.getInt("id"));
	    		item.setPurchaseOrderId(rs.getInt("purchase_order_id"));
	    		item.setCount(rs.getInt("count"));
	    		item.setPurchasePrice(rs.getDouble("purchase_price"));
	    		//商品信息
	    		Product p = new Product();
	    		p.setId(rs.getInt("product_id"));
	    		p.setName(rs.getString("productName"));
	    		item.setProductId(p.getId());
	    		item.setProduct(p);
	    		//供应商信息
	    		Supplier s = new Supplier();
	    		s.setId(rs.getInt("supplier_id"));
	    		s.setName(rs.getString("supplierName"));
	    		item.setSupplierId(s.getId());
	    		item.setSupplier(s);
		    }
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			db.release();
		}
		return item;
	}
	
	/**
	 * 更新采购单条目信息
	 * @param purchaseOrderItem 采购单条目对象
	 * @return
	 */
	public boolean updatePurchaseOrderItem(PurchaseOrderItem purchaseOrderItem) {
		//更新语句
		StringBuilder set = new StringBuilder();
		set.append("`product_id`=").append(purchaseOrderItem.getProductId()).append(", ");
		set.append("`supplier_id`=").append(purchaseOrderItem.getSupplierId()).append(", ");
		set.append("`count`=").append(purchaseOrderItem.getCount()).append(", ");
		set.append("`purchase_price`=").append(purchaseOrderItem.getPurchasePrice());
		
		return this.updateXXX(set.toString(), "`id`="+purchaseOrderItem.getId(), "purchase_order_item");
	}
	
	/**
	 * 删除采购单条目信息
	 * @param id 采购单条目id
	 * @return
	 */
	public boolean deletePurchaseOrderItemById(int id) {
		return this.deleteXXX("`id`="+id, "purchase_order_item");
	}

}
