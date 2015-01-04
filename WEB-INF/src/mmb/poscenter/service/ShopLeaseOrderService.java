package mmb.poscenter.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import mmb.poscenter.action.Page;
import mmb.poscenter.domain.Member;
import mmb.poscenter.domain.Product;
import mmb.poscenter.domain.ShopLeaseOrder;
import mmb.poscenter.domain.ShopLeaseOrderProduct;
import mmboa.util.db.BaseService;
import mmboa.util.db.DbOperation;
import mmboa.util.db.DbUtil;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class ShopLeaseOrderService extends BaseService {
	
	private static Logger log = Logger.getLogger(ShopLeaseOrderService.class);
	
	public boolean batInserData(List<ShopLeaseOrder>  list){
		Connection conn = DbUtil.getConnection();
		StringBuilder temp = new StringBuilder();
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		PreparedStatement ps_item = null;
		boolean sync = false;
		boolean oldAutoCommit = true;
		try {
			// 记录原来事务提交的方式
			oldAutoCommit = conn.getAutoCommit();
			// 关闭自动提交事务
			conn.setAutoCommit(false);
			ps = conn.prepareStatement("insert into shop_lease_order(serial_number,pos_code,cashier_id,member_id,price,deposit,order_type,create_time,shop_id,lease_order_id) values(?,?,?,?,?,?,?,?,?,?)");
			ps_item = conn.prepareStatement("insert into shop_lease_order_product(lease_order_id,product_id,count,pre_price,per_deposit,start_time,end_time,time_length,lease_style,shop_id) values (?,?,?,?,?,?,?,?,?,?)");
			int shopId = 0;
			if(list.size() > 0){
			  ps2 = conn.prepareStatement(" select id from shop where code = '"+list.get(0).getShopCode()+"'");
			  ResultSet rs = ps2.executeQuery();
			  if(rs.next()){
				  shopId = rs.getInt("id");
			  }
			}
			
			int count = 0 ;
			for(ShopLeaseOrder slo : list){
				ps.setString(1, slo.getSerialNumber());
				ps.setString(2, slo.getPosCode());
				ps.setInt(3, slo.getCashierId());
				ps.setString(4, slo.getMemberId());
				ps.setDouble(5,slo.getPrice());
				ps.setDouble(6, slo.getDeposit());
				ps.setInt(7, slo.getOrderType());
				ps.setTimestamp(8, slo.getCreateTime());
				ps.setInt(9,shopId);
				ps.setInt(10, slo.getLeaseOrderId());
				ps.addBatch();
				for(ShopLeaseOrderProduct slop : slo.getShopLeaseOrderProduct()){
					ps_item.setInt(1, slop.getLeaseOrderId());
					ps_item.setInt(2, slop.getProductId());
					ps_item.setInt(3, slop.getCount());
					ps_item.setDouble(4, slop.getPrePrice());
					ps_item.setDouble(5, slop.getPerDeposit());
					ps_item.setTimestamp(6, slop.getStartTime());
					ps_item.setTimestamp(7, slop.getEndTime());
					ps_item.setDouble(8, slop.getTimeLength());
					ps_item.setInt(9, slop.getLeaseStyle());
					ps_item.setInt(10,shopId);
					ps_item.addBatch();
				}
				count ++;
				if(count % 100 == 0 || count == list.size()){
					ps.executeBatch();
					ps_item.executeBatch();
				}
				
			}
			conn.commit();
			sync = true;
		} catch (Exception e) {
			try {
				//回滚事务
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			log.error("插入店面租赁信息出现异常：", e);
			temp.append("{\"message\":\"插入店面租赁信息出现异常："+e+"\"}");
		} finally {
			if(conn != null){
				try {
					//还原事务提交方式
					conn.setAutoCommit(oldAutoCommit);
					conn.close();
				} catch (SQLException e) {
				}
			}
			DbUtil.closeConnection(null, ps, conn);
		}
		 return sync;
	}
	
	/**
	 * 解析店面店传来的字符串信息
	 * @param json
	 * @return
	 */
	public List<ShopLeaseOrder>  parsePosadminJson(String json){
		 List<ShopLeaseOrder> temp  =  new  ArrayList<ShopLeaseOrder>(); 
		 Gson gson = new GsonBuilder().setDateFormat("yyyyMMdd HH:mm:ss").create();
		 temp = gson.fromJson(json, new TypeToken<List<ShopLeaseOrder>>(){}.getType());
		 return temp;
	}

   /**
    * 分页获取租赁订单信息
    * @param leaseOrderPage
    * @param param 查询参数[shopName:店面名称；orderType:订单类型；memberName:会员姓名]
    * @return
    */
	public Page<ShopLeaseOrder> getLeaseOrderPage(Page<ShopLeaseOrder> shopLeaseOrderPage, Map<String, Object> param) {
		Connection conn = DbUtil.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			//查询条件
			StringBuilder condSql = new StringBuilder();
			int orderType = (Integer)param.get("orderType");
			String memberName = (String)param.get("memberName");
			String shopName = (String)param.get("shopName");
			condSql.append(" and  slo.order_type=").append(orderType);
			if(StringUtils.isNotBlank(shopName)) {
				condSql.append(" and s.name like ? ");
			}
			if(StringUtils.isNotBlank(memberName)) {
				condSql.append(" and m.name like ? ");
			}
			
			//查询总记录数
			int index = 1;
			ps = conn.prepareStatement("select count(slo.id) from shop_lease_order slo left join member m on slo.member_id=m.id join shop s on slo.shop_id=s.id where 1=1 " + condSql);
			if(StringUtils.isNotBlank(shopName)) {
				ps.setString(index++, "%"+shopName+"%");
			}
			if(StringUtils.isNotBlank(memberName)) {
				ps.setString(index++, "%"+memberName+"%");
			}
			rs = ps.executeQuery();
		    if(rs.next()){
		    	shopLeaseOrderPage.setTotalRecords(rs.getInt(1));
		    }
			
			//获取列表数据
			if(shopLeaseOrderPage.getTotalRecords() > 0) {
				//获取每页显示的订单id列表
				List<Integer> orderIdList = new ArrayList<Integer>();
				StringBuilder sql = new StringBuilder();
				sql.append(" select slo.id orderId from shop_lease_order slo left join member m on slo.member_id=m.id join shop s on slo.shop_id=s.id");
				sql.append(" where 1=1 ").append(condSql);
				sql.append(" limit "+shopLeaseOrderPage.getFirstResult()).append(",").append(shopLeaseOrderPage.getPageCount());
				ps = conn.prepareStatement(sql.toString());
		    	index = 1;
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
				List<ShopLeaseOrder> shopLeaseOrderList = this.getShopLeaseOrderList(orderIdList);
				shopLeaseOrderPage.setList(shopLeaseOrderList);
			}
		}catch(Exception e){
			log.error("分页获取租赁订单信息时出现异常：", e);
		}finally{
			DbUtil.closeConnection(rs, ps, conn);
		}
		return shopLeaseOrderPage;
	}
	
	
	/**
	 * 根据订单ids获取租赁订单和订单商品详细信息
	 * @param orderIdList 订单id列表
	 * @return
	 */
	public List<ShopLeaseOrder> getShopLeaseOrderList(List<Integer> orderIdList) {
		List<ShopLeaseOrder> shopLeaseOrderList = new ArrayList<ShopLeaseOrder>();
		DbOperation db = new DbOperation();
		try{
			//查询sql
			StringBuilder sql = new StringBuilder();
			sql.append("select slo.id orderId,slo.serial_number,slo.member_id,m.`name` memberName,slo.price,slo.deposit,slo.order_type,slop.id,slop.count,slop.pre_price,slop.per_deposit,slop.start_time,slop.end_time,slop.time_length,slop.lease_style, slop.product_id,p.`name` productName,s.name shopName ");
			sql.append(" from shop_lease_order slo");
			sql.append(" join shop_lease_order_product slop on slo.lease_order_id=slop.lease_order_id and slo.shop_id = slop.shop_id ");
			sql.append(" join member m on slo.member_id=m.id");
			sql.append(" join product p on slop.product_id=p.id");
			sql.append(" join shop s on slo.shop_id=s.id");
			sql.append(" where slo.id in (");
			for(Integer id : orderIdList) {
				sql.append(id).append(",");
			}
			sql.deleteCharAt(sql.length()-1);
			sql.append(") ");
			sql.append(" order by slo.id");
			
			ResultSet rs = db.executeQuery(sql.toString());
			ShopLeaseOrder shopLeaseOrder = new ShopLeaseOrder();
			int currentOrderId = -1;
			while(rs.next()){
				int orderId = rs.getInt("orderId");
				//新订单
				if(currentOrderId != orderId) {
					currentOrderId = orderId;
					//订单信息
					shopLeaseOrder = new ShopLeaseOrder();
					shopLeaseOrder.setId(orderId);
					shopLeaseOrder.setSerialNumber(rs.getString("serial_number"));
					shopLeaseOrder.setMemberId(rs.getString("member_id"));
					shopLeaseOrder.setOrderType(rs.getInt("order_type"));
					shopLeaseOrder.setPrice(rs.getDouble("price"));
					shopLeaseOrder.setDeposit(rs.getDouble("deposit"));
					shopLeaseOrder.setShopName(rs.getString("shopName"));
					
					//会员信息
					Member member = new Member();
					member.setName(rs.getString("memberName"));
					shopLeaseOrder.setMember(member);
					shopLeaseOrderList.add(shopLeaseOrder);
				}
				//订单商品信息
				ShopLeaseOrderProduct slop = new ShopLeaseOrderProduct();
				slop.setId(rs.getInt("id"));
				slop.setProductId(rs.getInt("product_id"));
				slop.setCount(rs.getInt("count"));
				slop.setPrePrice(rs.getDouble("pre_price"));
				slop.setPerDeposit(rs.getDouble("per_deposit"));
				slop.setStartTime(rs.getTimestamp("start_time"));
				slop.setEndTime(rs.getTimestamp("end_time"));
				slop.setTimeLength(rs.getDouble("time_length"));
				slop.setLeaseStyle(rs.getInt("lease_style"));
				//商品信息
				Product product = new Product();
				product.setName(rs.getString("productName"));
				slop.setProduct(product);
				shopLeaseOrder.getShopLeaseOrderProduct().add(slop);
			}
		}catch(Exception e){
			log.error("根据订单ids获取租赁订单和订单商品详细信息时出现异常：", e);
		}finally{
			this.release(db);
		}
		
		return shopLeaseOrderList;
	}
	
}
