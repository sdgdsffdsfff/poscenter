package mmb.poscenter.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import mmb.poscenter.action.Page;
import mmb.poscenter.domain.GoodsClass;
import mmb.poscenter.domain.Product;
import mmb.poscenter.domain.Shop;
import mmb.poscenter.domain.ShopStock;
import mmboa.util.db.BaseService;
import mmboa.util.db.DbUtil;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class ShopStockService extends BaseService  {
	
	private static Logger log = Logger.getLogger(ShopStockService.class);
	
	/**
	 * 分页获取商品列表数据
	 * @param page 分页信息 
	 * @param param 查询参数[barCode:条形码；productName:商品名称；goodsClassName:商品分类名称]
	 * @return
	 */
	public Page<Product> getProductPage(Page<Product> page, Map<String, Object> param) {
		Connection conn = DbUtil.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			//查询条件
			StringBuilder condSql = new StringBuilder();
			String barCode = (String) param.get("barCode");
			String productName = (String) param.get("productName");
			String goodsClassName = (String) param.get("goodsClassName");
			if(StringUtils.isNotBlank(barCode)) {
				condSql.append(" and p.`bar_code` like ? ");
			}
			if(StringUtils.isNotBlank(productName)) {
				condSql.append(" and p.`name` like ? ");
			}
			if(StringUtils.isNotBlank(goodsClassName)) {
				condSql.append(" and gc.`name` like ? ");
			}
			
			//查询总记录数
			int index = 1;
			StringBuilder countSql = new StringBuilder();
			countSql.append("select count(p.id) from product p LEFT JOIN goods_class gc on p.goods_class_id=gc.id ");
			countSql.append("where p.is_delete = 0").append(condSql);
			ps = conn.prepareStatement(countSql.toString());
			if(StringUtils.isNotBlank(barCode)) {
				ps.setString(index++, "%"+barCode+"%");
			}
			if(StringUtils.isNotBlank(productName)) {
				ps.setString(index++, "%"+productName+"%");
			}
			if(StringUtils.isNotBlank(goodsClassName)) {
				ps.setString(index++, "%"+goodsClassName+"%");
			}
			rs = ps.executeQuery();
			if(rs.next()){
		    	page.setTotalRecords(rs.getInt(1));
		    }
			
		    //查询列表数据
		    if(page.getTotalRecords() > 0) {
		    	StringBuilder sql = new StringBuilder(50);
		    	sql.append("select p.id,p.bar_code,p.name,p.lease_price,p.month_lease_price,p.sale_price,p.deposit,p.stock,p.supplier_id,p.goods_class_id,gc.`name` goodsClassName,s.`name` supplierName");
		    	sql.append(" from product p ");
		    	sql.append(" LEFT JOIN goods_class gc on p.goods_class_id=gc.id LEFT JOIN supplier s on p.supplier_id=s.id");
		    	sql.append(" where p.is_delete=0 ").append(condSql);
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
				if(StringUtils.isNotBlank(goodsClassName)) {
					ps.setString(index++, "%"+goodsClassName+"%");
				}
		    	rs = ps.executeQuery();
		    	List<Product> tmp = new ArrayList<Product>();
		    	Product p;
		    	while(rs.next()){
		    		p = new Product();
		    		p.setId(rs.getInt("id"));
		    		p.setBarCode(rs.getString("bar_code"));
		    		p.setName(rs.getString("name"));
		    		p.setLeasePrice(rs.getDouble("lease_price"));
		    		p.setMonthLeasePrice(rs.getDouble("month_lease_price"));
		    		p.setSalePrice(rs.getDouble("sale_price"));
		    		p.setDeposit(rs.getDouble("deposit"));
		    		p.setStock(rs.getInt("stock"));
		    		//供应商信息
		    		p.setSupplierId(rs.getInt("supplier_id"));
		    		p.setSupplierName(rs.getString("supplierName"));
		    		//商品分类信息
		    		GoodsClass gc = new GoodsClass();
		    		gc.setId(rs.getString("goods_class_id"));
		    		gc.setName(rs.getString("goodsClassName"));
		    		p.setGoodsClassId(gc.getId());
		    		p.setGoodsClass(gc);
		    		tmp.add(p);
		    	}
		    	page.setList(tmp);
		    }
		}catch(Exception e){
			log.error("分页获取商品列表数据时出现异常：", e);
		}finally{
			DbUtil.closeConnection(rs, ps, conn);
		}
		return page;
	}

	/**
	 * 分页获取店面库存列表数据
	 * @param page 分页信息
	 * @param param 查询参数[productId:商品id；shopCode:店面编码；shopName:店面名称]
	 * @return
	 */
	public Page<ShopStock> getShopStockPage(Page<ShopStock> page, Map<String, Object> param) {
		Connection conn = DbUtil.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			//查询条件
			StringBuilder condSql = new StringBuilder();
			Integer productId = (Integer) param.get("productId");
			String shopCode = (String) param.get("shopCode");
			String shopName = (String) param.get("shopName");
			condSql.append(" and ss.product_id=").append(productId);
			if(StringUtils.isNotBlank(shopCode)) {
				condSql.append(" and s.`code` like ? ");
			}
			if(StringUtils.isNotBlank(shopName)) {
				condSql.append(" and s.`name` like ? ");
			}
			
			//查询总记录数
			int index = 1;
			StringBuilder countSql = new StringBuilder();
			countSql.append("select count(*) from shop_stock ss join shop s on ss.shop_id=s.id");
			countSql.append(" where 1=1 ").append(condSql);
			ps = conn.prepareStatement(countSql.toString());
			if(StringUtils.isNotBlank(shopCode)) {
				ps.setString(index++, "%"+shopCode+"%");
			}
			if(StringUtils.isNotBlank(shopName)) {
				ps.setString(index++, "%"+shopName+"%");
			}
			rs = ps.executeQuery();
			if(rs.next()){
		    	page.setTotalRecords(rs.getInt(1));
		    }
			
		    //查询列表数据
		    if(page.getTotalRecords() > 0) {
		    	StringBuilder sql = new StringBuilder(50);
		    	sql.append("select ss.id,ss.shop_id,ss.product_id,ss.stock,ss.create_time,s.`code` shopCode,s.`name` shopName");
		    	sql.append(" from shop_stock ss join shop s on ss.shop_id=s.id ");
		    	sql.append(" where 1=1 ").append(condSql);
		    	sql.append(" order by id desc ");
		    	sql.append(" limit ");
		    	sql.append(page.getFirstResult()).append(",").append(page.getPageCount());
		    	ps = conn.prepareStatement(sql.toString());
		    	index = 1;
		    	if(StringUtils.isNotBlank(shopCode)) {
					ps.setString(index++, "%"+shopCode+"%");
				}
				if(StringUtils.isNotBlank(shopName)) {
					ps.setString(index++, "%"+shopName+"%");
				}
		    	rs = ps.executeQuery();
		    	List<ShopStock> list = new ArrayList<ShopStock>();
		    	ShopStock shopStock;
		    	while(rs.next()){
		    		shopStock = new ShopStock();
		    		shopStock.setId(rs.getInt("id"));
		    		shopStock.setProductId(rs.getInt("product_id"));
		    		shopStock.setStock(rs.getInt("stock"));
		    		shopStock.setCreateTime(rs.getTimestamp("create_time"));
		    		//店面信息
		    		Shop shop = new Shop();
		    		shop.setId(rs.getInt("shop_id"));
		    		shop.setCode(rs.getString("shopCode"));
		    		shop.setName(rs.getString("shopName"));
		    		shopStock.setShopId(shop.getId());
		    		shopStock.setShop(shop);
		    		list.add(shopStock);
		    	}
		    	page.setList(list);
		    }
		}catch(Exception e){
			log.error("分页获取店面库存列表数据时出现异常：", e);
		}finally{
			DbUtil.closeConnection(rs, ps, conn);
		}
		return page;
	}
	
	/**
	 * 店面系统提交店面库存信息
	 * @param json 店面提交的JSON字符串
	 * @return
	 */
	public String submitShopStock(String json) {
		String resutl = "true";
		
		try {
			//解析数据
			Gson gson = new Gson();
			List<ShopStock> shopStockList = gson.fromJson(json, new TypeToken<List<ShopStock>>(){}.getType());
			
			if(shopStockList!=null && !shopStockList.isEmpty()) {
				//根据店面编号获取店面信息
				ShopService shopService = new ShopService();
				String shopCode = ((ShopStock)shopStockList.get(0)).getShop().getCode();
				Shop shop = shopService.getShopByCode(shopCode);
				
				//店面不存在
				if(shop == null) {
					throw new Exception("店面编号不存在");
				}
				
				//设置店面id
				for(ShopStock shopStock : shopStockList) {
					shopStock.setShopId(shop.getId());
				}
				
				//批量保存店面库存
				this.batchSaveShopStock(shopStockList);
			}
		} catch (Exception e) {
			resutl = e.getMessage();
			log.error("店面系统提交店面库存信息时出现异常：", e);
		}
		
		return resutl;
	}

	/**
	 * 批量保存店面库存
	 * @param shopStockList 店面库存列表
	 * @return
	 * @throws Exception 
	 */
	public void batchSaveShopStock(List<ShopStock> shopStockList) throws Exception {
		Connection conn = DbUtil.getConnection();
		PreparedStatement ps = null;
		boolean oldAutoCommit = true;
		try {
			//记录原来事务提交的方式
			oldAutoCommit = conn.getAutoCommit();
			//关闭自动提交事务
			conn.setAutoCommit(false);
			
			//保存店面库存
			int count = 0;
			ps = conn.prepareStatement("insert shop_stock(shop_id,product_id,stock,create_time) values(?,?,?,?)");
			for(ShopStock shopStock : shopStockList) {
				ps.setInt(1, shopStock.getShopId());
				ps.setInt(2, shopStock.getProductId());
				ps.setInt(3, shopStock.getStock());
				ps.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
				ps.addBatch();
				count++;
				//批量执行
				if (count%100==0 || count==shopStockList.size()) {
					ps.executeBatch();
				}
			}
			
        	conn.commit();
		} catch (Exception e) {
			try {
				//回滚事务
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			log.error("批量保存店面库存时出现异常：", e);
			throw e;
		} finally {
			DbUtil.closeConnection(null, ps, null);
			if(conn != null){
				try {
					//还原事务提交方式
					conn.setAutoCommit(oldAutoCommit);
					conn.close();
				} catch (SQLException e) { }
			}
		}
	}

}
