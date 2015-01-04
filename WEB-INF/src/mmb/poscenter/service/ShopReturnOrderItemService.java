package mmb.poscenter.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import mmb.poscenter.domain.Product;
import mmb.poscenter.domain.ShopReturnOrderItem;
import mmboa.util.db.BaseService;
import mmboa.util.db.DbOperation;

public class ShopReturnOrderItemService extends BaseService {
	
	/**
	 * 根据店面退货单id获取所有店面退货单条目
	 * @param shopReturnOrderId 店面退货单id
	 * @return
	 */
	public List<ShopReturnOrderItem> getAllItemListByOrderId(int shopReturnOrderId) {
		DbOperation db = new DbOperation();
		List<ShopReturnOrderItem> list = new ArrayList<ShopReturnOrderItem>();
		try{
		    //获取列表数据
		    ShopReturnOrderItem item;
		    String sql = "SELECT i.id,i.shop_return_order_id,i.product_id,i.return_count,i.receive_count,p.`name` productName " +
		    		"from shop_return_order_item i LEFT JOIN product p on i.product_id=p.id where i.shop_return_order_id="+shopReturnOrderId;
		    ResultSet rs = db.executeQuery(sql);
		    while(rs.next()){
		    	item = new ShopReturnOrderItem();
	    		item.setId(rs.getInt("id"));
	    		item.setShopReturnOrderId(rs.getInt("shop_return_order_id"));
	    		item.setReturnCount(rs.getInt("return_count"));
	    		item.setReceiveCount(rs.getInt("receive_count"));
	    		//商品信息
	    		Product p = new Product();
	    		p.setId(rs.getInt("product_id"));
	    		p.setName(rs.getString("productName"));
	    		item.setProductId(p.getId());
	    		item.setProduct(p);
	    		list.add(item);
		    }
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			db.release();
		}
		return list;
	}

}
