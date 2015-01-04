package mmb.poscenter.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import mmb.poscenter.domain.BuyGiftEvent;
import mmb.poscenter.domain.BuyGiftProduct;
import mmb.poscenter.domain.ComboEvent;
import mmb.poscenter.domain.ComboProduct;
import mmb.poscenter.domain.CountDiscountEvent;
import mmb.poscenter.domain.Event;
import mmb.poscenter.domain.MoneyDiscountEvent;
import mmb.poscenter.domain.Product;
import mmb.poscenter.domain.ShopSaledOrder;
import mmb.poscenter.domain.ShopSaledOrderEvent;
import mmb.poscenter.domain.SwapBuyEvent;
import mmb.poscenter.domain.SwapBuyProduct;
import mmboa.util.db.BaseService;
import mmboa.util.db.DbUtil;

public class ShopSaledOrderEventService extends BaseService {

	public ShopSaledOrder getShopSaledOrder(int saledOrderId,int shopId){
		Connection conn = DbUtil.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		ShopSaledOrder shopSaledOrder = null;
		try{
			ps = conn.prepareStatement("SELECT sso.id,sso.cashier_id,sso.member_id,sso.order_type,sso.pos_code,sso.price,sso.saled_order_id,sso.saled_time,sso.serial_number,sso.shop_id FROM shop_saled_order sso where sso.saled_order_id = ? and sso.shop_id = ? ");
			ps.setInt(1, saledOrderId);
			ps.setInt(2, shopId);
			rs = ps.executeQuery();
			while(rs.next()){
				shopSaledOrder = new ShopSaledOrder();
				shopSaledOrder.setId(rs.getInt("id"));
				shopSaledOrder.setCashierId(rs.getInt("cashier_id"));
				shopSaledOrder.setMemberId(rs.getString("member_id"));
			    shopSaledOrder.setOrderType(rs.getInt("order_type"));
			    shopSaledOrder.setPosCode(rs.getString("pos_code"));
			    shopSaledOrder.setPrice(rs.getDouble("price"));
			    shopSaledOrder.setSaledOrderId(rs.getInt("saled_order_id"));
			    shopSaledOrder.setSaledTime(rs.getTimestamp("saled_time"));
			    shopSaledOrder.setSerialNumber(rs.getString("serial_number"));
			    shopSaledOrder.setShopId(rs.getInt("shop_Id"));
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			DbUtil.closeConnection(rs, ps, conn);
		}
		
		return shopSaledOrder;
	}
	
	public Event getShopSaledOrderEvent(int saledId,int shopId){
        Event tempEvent = new Event();
		Connection conn = DbUtil.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
		   ps =conn.prepareStatement("select ssoe.id,ssoe.shop_event_id,ssoe.saled_order_id,ssoe.event_id,ssoe.detail_event_id,ssoe.ext_id,ssoe.shop_id,e.type from shop_saled_order_event ssoe left join event e on ssoe.event_id = e.id  where ssoe.saled_order_id = ? and ssoe.shop_id = ? ");
		   ps.setInt(1, saledId);
		   ps.setInt(2, shopId);
		   rs = ps.executeQuery();
		   List<ShopSaledOrderEvent> list = new ArrayList<ShopSaledOrderEvent>();
		   ShopSaledOrderEvent ssoe = null;
		   while(rs.next()){
			   ssoe = new ShopSaledOrderEvent();
			   ssoe.setId(rs.getInt("id"));
			   ssoe.setSaledOrderId(rs.getInt("saled_order_id"));
			   ssoe.setEventId(rs.getInt("event_id"));
			   ssoe.setEventType(rs.getInt("type"));
			   ssoe.setDetailEventId(rs.getInt("detail_event_id"));
			   ssoe.setExtId(rs.getInt("ext_id"));
			   ssoe.setShopEventId(rs.getInt("shop_event_id"));
			   ssoe.setShopId(rs.getInt("shop_id"));
			   list.add(ssoe);
		   }
		   tempEvent.setBuyGiftEventList(this.getBuyGiftEventList(conn, list));
		   tempEvent.setComboEventList(this.getComboEventList(conn, list));
		   tempEvent.setCountDiscountEventList(this.getCountDiscountEventList(conn, list));
		   tempEvent.setMoneyDiscountEventList(this.getMoneyDiscountEventList(conn, list));
		   tempEvent.setSwapBuyEventList(this.getSwapBuyEventList(conn, list));
		   
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			DbUtil.closeConnection(rs, ps, conn);
		}
		
		return tempEvent;
	}
	
	/**
	 * 获取买赠活动的list
	 * @param conn
	 * @param saledOrderEventlist 订单活动列表信息
	 * @return
	 */
	public  List<BuyGiftEvent>  getBuyGiftEventList(Connection conn , List<ShopSaledOrderEvent> shopSaledOrderEventlist){
	    List<BuyGiftEvent> tmp = new ArrayList<BuyGiftEvent>();
	    BuyGiftEvent buyGiftEvent = null;
		PreparedStatement ps_1 = null;
		PreparedStatement ps_2 = null;
		ResultSet rs = null;
		try{
			 Map<Integer,Set<Integer>>  data = this.getUsefulIds(1, shopSaledOrderEventlist);
			 Set<Integer> key = data.keySet();
			 for (Iterator<Integer> it = key.iterator(); it.hasNext();) {
		            int buyGiftEventId = (Integer) it.next();
		          //买赠活动实体信息
		            ps_1 = conn.prepareStatement("SELECT bge.id,bge.event_id,bge.product_id,bge.product_count,bge.user_limit,p.`name`,p.sale_price FROM buy_gift_event bge LEFT JOIN product p on bge.product_id = p.id WHERE bge.id = ?");
		            ps_1.setInt(1, buyGiftEventId);
		            rs = ps_1.executeQuery();
		            if(rs.next()){
		            	buyGiftEvent = new BuyGiftEvent();
		            	buyGiftEvent.setId(rs.getInt("id"));
		            	buyGiftEvent.setEventId(rs.getInt("event_id"));
						buyGiftEvent.setProductId(rs.getInt("product_id"));
						Product product = new Product();
						product.setId(rs.getInt("product_id"));
						product.setName(rs.getString("name"));
						product.setSalePrice(rs.getDouble("sale_price"));
						buyGiftEvent.setProduct(product);
						buyGiftEvent.setProductCount(rs.getInt("product_count"));
						buyGiftEvent.setUserLimit(rs.getInt("user_limit"));
		            }
		            
		            StringBuilder extIds = new StringBuilder();
		            List<BuyGiftProduct> giftlist = new ArrayList<BuyGiftProduct>();
		            BuyGiftProduct buyGiftProduct = null;
		            for(Iterator<Integer> its = data.get(buyGiftEventId).iterator(); its.hasNext();){
		            	  int extId = (Integer)its.next();
		            	  extIds.append(extId);
		            	  extIds.append(",");
		            }   
		            if(extIds.length() > 1){
		            	extIds.deleteCharAt(extIds.length()-1);
		            	ps_2 = conn.prepareStatement("select bgp.id,bgp.buy_gift_event_id,bgp.gift_product_id,bgp.max_gift_count,p.`name` from buy_gift_product bgp LEFT JOIN product p on bgp.gift_product_id = p.id where bgp.id in ( "+ extIds.toString()+ " )");
		            	rs = ps_2.executeQuery();
		            	while(rs.next()){
		            		buyGiftProduct = new BuyGiftProduct();
		            		buyGiftProduct.setId(rs.getInt("id"));
		            		buyGiftProduct.setBuyGiftEventId(rs.getInt("buy_gift_event_id"));
							buyGiftProduct.setGiftProductId(rs.getInt("gift_product_id"));
							buyGiftProduct.setMaxGiftCount(rs.getInt("max_gift_count"));
							Product p1 = new Product();
							p1.setId(rs.getInt("gift_product_id"));
							p1.setName(rs.getString("name"));
							buyGiftProduct.setGiftProduct(p1);
							giftlist.add(buyGiftProduct);
		            	}
		            }
		            
		            buyGiftEvent.setGiftList(giftlist);
		            tmp.add(buyGiftEvent);
		            
		     }
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			DbUtil.closeConnection(null, ps_1, null);
			DbUtil.closeConnection(rs, ps_2, null);
		}
		return tmp;
	}
   
	/**
	 * 获取换购活动的list
	 * @param conn
	 * @param saledOrderEventlist
	 * @return
	 */
	public  List<SwapBuyEvent>  getSwapBuyEventList(Connection conn , List<ShopSaledOrderEvent> shopSaledOrderEventlist){
	    List<SwapBuyEvent> tmp = new ArrayList<SwapBuyEvent>();
	    SwapBuyEvent swapBuyEvent = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			ps = conn.prepareStatement("SELECT sbe.id,sbe.event_id,sbe.money,sbe.append_money,sbp.id sbpId,sbp.gift_product_id,sbp.swap_buy_event_id,p.`name` from swap_buy_event sbe LEFT JOIN swap_buy_product sbp on sbe.id = sbp.swap_buy_event_id  LEFT JOIN product p on sbp.gift_product_id = p.id WHERE sbe.id = ? and sbp.id = ?");
			for(ShopSaledOrderEvent ssoe : shopSaledOrderEventlist){
				if(ssoe.getEventType() == 2){
					ps.setInt(1,ssoe.getDetailEventId());
					ps.setInt(2,ssoe.getExtId());
					rs = ps.executeQuery();
					while(rs.next()){
						
						//换购活动实体信息
						swapBuyEvent = new SwapBuyEvent();
						swapBuyEvent.setId(rs.getInt("id"));
						swapBuyEvent.setEventId(rs.getInt("event_id"));
						swapBuyEvent.setMoney(rs.getDouble("money"));
						swapBuyEvent.setAppendMoney(rs.getDouble("append_money"));
						
						//换购活动商品信息
						List<SwapBuyProduct> list = new ArrayList<SwapBuyProduct>();
						SwapBuyProduct swapBuyProduct = new SwapBuyProduct();
						swapBuyProduct.setId(rs.getInt("sbpId"));
						swapBuyProduct.setGiftProductId(rs.getInt("gift_product_id"));
						swapBuyProduct.setGiftProductName(rs.getString("name"));
						swapBuyProduct.setSwapBuyEventId(rs.getInt("swap_buy_event_id"));
						list.add(swapBuyProduct);
						
						swapBuyEvent.setSwapBuyProductList(list);
						tmp.add(swapBuyEvent);
						
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			DbUtil.closeConnection(rs, ps, null);
		}
		return tmp;
	}
	
	/**
	 * 获得金额折扣活动的list
	 * @param conn
	 * @param saledOrderEventlist
	 * @return
	 */
	public  List<MoneyDiscountEvent>  getMoneyDiscountEventList(Connection conn , List<ShopSaledOrderEvent> shopSaledOrderEventlist){
	    List<MoneyDiscountEvent> tmp = new ArrayList<MoneyDiscountEvent>();
	    MoneyDiscountEvent moneyDiscountEvent = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			StringBuilder sbIds = new StringBuilder();
			for(ShopSaledOrderEvent ssoe : shopSaledOrderEventlist){
				if(ssoe.getEventType() == 3){
					sbIds.append(ssoe.getDetailEventId());
					sbIds.append(",");
				}
			}
			
			if(sbIds.length() > 1 ){
				sbIds.deleteCharAt(sbIds.length()-1);
				ps = conn.prepareStatement("SELECT mde.id,mde.event_id,mde.money,mde.number_value,mde.type from money_discount_event mde where mde.id  in ( "+sbIds.toString()+" ) ");
				rs = ps.executeQuery();
				while(rs.next()){
					moneyDiscountEvent = new MoneyDiscountEvent();
					moneyDiscountEvent.setId(rs.getInt("id"));
					moneyDiscountEvent.setEventId(rs.getInt("event_id"));
					moneyDiscountEvent.setMoney(rs.getDouble("money"));
					moneyDiscountEvent.setNumberValue(rs.getDouble("number_value"));
					moneyDiscountEvent.setType(rs.getInt("type"));
					tmp.add(moneyDiscountEvent);
				}
				
			}
			
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
		
			DbUtil.closeConnection(rs, ps, null);
		}
		return tmp;
	}

	
	
	/**
	 * 获得数量折扣活动的list
	 * @param conn
	 * @param saledOrderEventlist
	 * @return
	 */
	public  List<CountDiscountEvent>  getCountDiscountEventList(Connection conn , List<ShopSaledOrderEvent> shopSaledOrderEventlist){
	    List<CountDiscountEvent> tmp = new ArrayList<CountDiscountEvent>();
	    CountDiscountEvent countDiscountEvent = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			Set<Integer> countDiscountEventIds = new HashSet<Integer>();
			StringBuilder sbIds = new StringBuilder();
			for(ShopSaledOrderEvent ssoe : shopSaledOrderEventlist){
				if(ssoe.getEventType() == 4){
					if(countDiscountEventIds.contains(ssoe.getDetailEventId())){
						continue;
					}else{
						countDiscountEventIds.add(ssoe.getDetailEventId());
						sbIds.append(ssoe.getDetailEventId());
						sbIds.append(",");
					}
				}
			}
			
			if(sbIds.length() > 1){
				sbIds.deleteCharAt(sbIds.length() - 1);
				ps = conn.prepareStatement("select cde.id,cde.product_id,cde.product_count,cde.type,cde.event_id,cde.discount,p.name,p.sale_price from count_discount_event cde LEFT JOIN product p ON cde.product_id = p.id where cde.id in ( "+sbIds.toString()+" )");
				rs = ps.executeQuery();
				
				while(rs.next()){
					countDiscountEvent = new CountDiscountEvent();
					countDiscountEvent.setId(rs.getInt("id"));
					countDiscountEvent.setProductId(rs.getInt("product_id"));
					countDiscountEvent.setProductCount(rs.getInt("product_count"));
					countDiscountEvent.setType(rs.getInt("type"));
					countDiscountEvent.setEventId(rs.getInt("event_id"));
					countDiscountEvent.setDiscount(rs.getDouble("discount"));
					
					Product p1 = new Product();
					p1.setId(rs.getInt("product_id"));
					p1.setName(rs.getString("name"));
					countDiscountEvent.setProduct(p1);
					tmp.add(countDiscountEvent);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			DbUtil.closeConnection(rs, ps, null);
		}
		return tmp;
	}

	
	/**
	 * 获得套餐活动的list
	 * @param conn
	 * @param saledOrderEventlist
	 * @return
	 */
	public  List<ComboEvent>  getComboEventList(Connection conn , List<ShopSaledOrderEvent> shopSaledOrderEventlist){
	    List<ComboEvent> tmp = new ArrayList<ComboEvent>();
	    ComboEvent comboEvent = null;
		PreparedStatement ps_1 = null;
		PreparedStatement ps_2 = null;
		ResultSet rs = null;
		try{
			 Map<Integer,Set<Integer>>  data = this.getUsefulIds(5, shopSaledOrderEventlist);
			 Set<Integer> key = data.keySet();
			 for (Iterator<Integer> it = key.iterator(); it.hasNext();) {
		            int comboEventId = (Integer) it.next();
		          //买赠活动实体信息
		            ps_1 = conn.prepareStatement("SELECT ce.combo_price,ce.event_id,ce.id FROM combo_event ce where ce.id = ?");
		            ps_1.setInt(1, comboEventId);
		            rs = ps_1.executeQuery();
		            if(rs.next()){
		            	comboEvent = new ComboEvent();
		            	comboEvent.setId(rs.getInt("id"));
		            	comboEvent.setComboPrice(rs.getDouble("combo_price"));
		            	comboEvent.setEventId(rs.getInt("event_id"));
		            }
		           
		            StringBuilder extIds = new StringBuilder();
		            List<ComboProduct> comboProList = new ArrayList<ComboProduct>();
		            ComboProduct comboProduct = null;
		            for(Iterator<Integer> its = data.get(comboEventId).iterator(); its.hasNext();){
		            	  int extId = (Integer)its.next();
		            	  extIds.append(extId);
		            	  extIds.append(",");
		            }   
		            if(extIds.length() > 1){
		            	extIds.deleteCharAt(extIds.length()-1);
		            	ps_2 = conn.prepareStatement("select cp.id,cp.product_id,cp.product_count,cp.combo_event_id,p.name,p.sale_price from combo_product cp LEFT JOIN product p ON cp.product_id = p.id  where cp.id in ( "+extIds.toString()+" )");
		            	rs = ps_2.executeQuery();
		            	while(rs.next()){
		            		comboProduct = new ComboProduct();
		            		comboProduct.setId(rs.getInt("id"));
		            		comboProduct.setComboEventId(rs.getInt("combo_event_id"));
		            		comboProduct.setProductId(rs.getInt("product_id"));
		            		comboProduct.setProductCount(rs.getInt("product_count"));
		           
							Product p1 = new Product();
							p1.setId(rs.getInt("product_id"));
							p1.setName(rs.getString("name"));
							comboProduct.setProduct(p1);
							
							comboProList.add(comboProduct);
		            	}
		            }
		            
		            comboEvent.setComboProductList(comboProList);
		            tmp.add(comboEvent);
						
		   }
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			DbUtil.closeConnection(null, ps_1, null);
			DbUtil.closeConnection(rs, ps_2, null);
		}
		return tmp;
	}

	
	 public  Map<Integer,Set<Integer>>  getUsefulIds(int eventType , List<ShopSaledOrderEvent> shopSaledOrderEventlist ){
		 Map<Integer,Set<Integer>> tmp = new HashMap<Integer,Set<Integer>>();
		 Set<Integer> tmpset = null;
		 for(ShopSaledOrderEvent ssoe : shopSaledOrderEventlist ){
			 if(ssoe.getEventType() == eventType){
				 if(tmp.containsKey(ssoe.getDetailEventId())){
					  tmp.get(ssoe.getDetailEventId()).add(ssoe.getExtId());
				 }else{
					 tmpset = new HashSet<Integer>();
					 tmpset.add(ssoe.getExtId());
					 tmp.put(ssoe.getDetailEventId(), tmpset);
				 }
				 
			 } 
		 }
		 
		 return tmp;
		 
		 
		 
	 }
}
