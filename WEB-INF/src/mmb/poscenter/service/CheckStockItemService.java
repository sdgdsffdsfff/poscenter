package mmb.poscenter.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import mmb.poscenter.action.Page;
import mmb.poscenter.domain.CheckStock;
import mmb.poscenter.domain.CheckStockItem;
import mmb.poscenter.domain.Invoice;
import mmb.poscenter.domain.Product;
import mmboa.util.db.BaseService;
import mmboa.util.db.DbUtil;

import org.apache.log4j.Logger;

public class CheckStockItemService extends BaseService  {
	
	private static Logger log = Logger.getLogger(CheckStockItemService.class);
	
	/**
	 * 分页获取盘点条目信息
	 * @param page 分页信息
	 * @param param 查询参数[checkStockId:盘点id]
	 * @return
	 */
	public Page<CheckStockItem> getCheckStockItemPage(Page<CheckStockItem> page, Map<String, Object> param) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			conn = DbUtil.getConnection();
			
			//所属盘点单
			Integer checkStockId = (Integer)param.get("checkStockId");
			
			//查询总记录数
			StringBuilder countSql = new StringBuilder();
			countSql.append("SELECT COUNT(*) from (");
			countSql.append(" SELECT c.product_id from check_stock_item c join product p on c.product_id=p.id");
			countSql.append(" where c.check_stock_id=?");
			countSql.append(" GROUP BY c.product_id) cp");
			ps = conn.prepareStatement(countSql.toString());
			ps.setInt(1, checkStockId);
			rs = ps.executeQuery();
			if(rs.next()){
				page.setTotalRecords(rs.getInt(1));
			}
			
			//分页获取商品Id列表
			if(page.getTotalRecords() > 0) {
				List<Integer> productIdList = new ArrayList<Integer>();
				StringBuilder productSql = new StringBuilder();
				productSql.append(" SELECT c.product_id from check_stock_item c join product p on c.product_id=p.id");
				productSql.append(" where c.check_stock_id=?");
				productSql.append(" GROUP BY c.product_id");
				productSql.append(" LIMIT ").append(page.getFirstResult()).append(",").append(page.getPageCount());
				ps = conn.prepareStatement(productSql.toString());
				ps.setInt(1, checkStockId);
				rs = ps.executeQuery();
				while(rs.next()){
					productIdList.add(rs.getInt("product_id"));
				}
				
				//获取盘点列表数据
				if(!productIdList.isEmpty()) {
					List<CheckStockItem> CheckStockItemList = new ArrayList<CheckStockItem>();
					StringBuilder sql = new StringBuilder();
					sql.append(" SELECT c.id,c.product_id,p.`name` productName,c.count,c.charger,c.stock,c.check_time,c.remark ");
					sql.append(" from check_stock_item c join product p on c.product_id=p.id");
					sql.append(" where c.check_stock_id=?");
					sql.append(" and c.product_id in (");
					for(Integer productId : productIdList) {
						sql.append(productId).append(",");
					}
					sql.deleteCharAt(sql.length()-1);
					sql.append(")");
					sql.append(" ORDER BY c.product_id,c.check_time");
					ps = conn.prepareStatement(sql.toString());
					ps.setInt(1, checkStockId);
					rs = ps.executeQuery();
					while(rs.next()){
						CheckStockItem CheckStockItem = new CheckStockItem();
						CheckStockItem.setId(rs.getInt("id"));
						CheckStockItem.setCount(rs.getInt("count"));
						CheckStockItem.setCharger(rs.getString("charger"));
						CheckStockItem.setStock(rs.getInt("stock"));
						CheckStockItem.setCheckTime(rs.getTimestamp("check_time"));
						CheckStockItem.setRemark(rs.getString("remark"));
						//商品信息
						Product product = new Product();
						product.setName(rs.getString("productName"));
						CheckStockItem.setProductId(rs.getInt("product_id"));
						CheckStockItem.setProduct(product);
						CheckStockItemList.add(CheckStockItem);
					}
					page.setList(CheckStockItemList);
				}
			}
		}catch(Exception e){
			log.error("分页获取盘点条目信息时出现异常：", e);
		}finally{
			DbUtil.closeConnection(rs, ps, conn);
		}
		return page;
	}
	
	/**
	 * 修改盘点信息
	 * @param checkStockItem 盘点信息对象
	 * @return
	 */
	public boolean updateCheckStockItem(CheckStockItem checkStockItem) {
		//更新语句
		StringBuilder set = new StringBuilder();
		set.append("count=").append(checkStockItem.getCount()).append(", ");
		set.append("remark='").append(checkStockItem.getRemark()).append("', ");
		set.append("charger='").append(checkStockItem.getCharger()).append("', ");
		set.append("check_time='").append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())).append("'");
		return this.updateXXX(set.toString(), "`id`="+checkStockItem.getId(), "check_stock_item");
	}

	/**
	 * 获取盘点表打印数据
	 * @param checkStockId 盘点id
	 * @return
	 */
	public List<Object[]> getPrintCheckStockItemData(int checkStockId) {
		List<CheckStockItem> firstCheckStockItemList = new ArrayList<CheckStockItem>();
		List<CheckStockItem> againCheckStockItemList = new ArrayList<CheckStockItem>();
		List<Object[]> checkStockItemList = new ArrayList<Object[]>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			conn = DbUtil.getConnection();
			
			//获取初盘数据
			StringBuilder sql = new StringBuilder();
			sql.append(" SELECT cs.id,cs.product_id,p.`name` productName,p.bar_code,cs.count,cs.stock,cs.check_time,cs.remark");
			sql.append(" from check_stock_item cs join product p on cs.product_id=p.id");
			sql.append(" where cs.id in (");
			sql.append(" SELECT MIN(c.id) from check_stock_item c");
			sql.append(" where c.check_stock_id=?");
			sql.append(" GROUP BY c.product_id)");
			sql.append(" ORDER BY cs.product_id");
			ps = conn.prepareStatement(sql.toString());
			ps.setInt(1, checkStockId);
			rs = ps.executeQuery();
			while(rs.next()){
				CheckStockItem checkStockItem = new CheckStockItem();
				checkStockItem.setId(rs.getInt("id"));
				checkStockItem.setCount(rs.getInt("count"));
				checkStockItem.setStock(rs.getInt("stock"));
				checkStockItem.setCheckTime(rs.getTimestamp("check_time"));
				checkStockItem.setRemark(rs.getString("remark"));
				//商品信息
				Product product = new Product();
				product.setName(rs.getString("productName"));
				product.setBarCode(rs.getString("bar_code"));
				checkStockItem.setProductId(rs.getInt("product_id"));
				checkStockItem.setProduct(product);
				firstCheckStockItemList.add(checkStockItem);
			}
			
			//获取复盘数据
			sql = new StringBuilder();
			sql.append(" SELECT cs.id,cs.product_id,p.`name` productName,p.bar_code,cs.count,cs.stock,cs.check_time,cs.remark");
			sql.append(" from check_stock_item cs join product p on cs.product_id=p.id");
			sql.append(" where cs.id in (");
			sql.append(" SELECT max(c.id) from check_stock_item c");
			sql.append(" where c.check_stock_id=?");
			sql.append(" GROUP BY c.product_id)");
			sql.append(" ORDER BY cs.product_id");
			ps = conn.prepareStatement(sql.toString());
			ps.setInt(1, checkStockId);
			rs = ps.executeQuery();
			while(rs.next()){
				CheckStockItem checkStockItem = new CheckStockItem();
				checkStockItem.setId(rs.getInt("id"));
				checkStockItem.setCount(rs.getInt("count"));
				checkStockItem.setStock(rs.getInt("stock"));
				checkStockItem.setCheckTime(rs.getTimestamp("check_time"));
				checkStockItem.setRemark(rs.getString("remark"));
				//商品信息
				Product product = new Product();
				product.setName(rs.getString("productName"));
				product.setBarCode(rs.getString("bar_code"));
				checkStockItem.setProductId(rs.getInt("product_id"));
				checkStockItem.setProduct(product);
				againCheckStockItemList.add(checkStockItem);
			}
			
			//合并初盘和复盘数据
			for(int i=0; i<firstCheckStockItemList.size(); i++) {
				CheckStockItem first = firstCheckStockItemList.get(i);
				CheckStockItem again = againCheckStockItemList.get(i);
				
				Object[] objs = new Object[7];
				objs[0] = first.getProduct().getBarCode();
				objs[1] = first.getProduct().getName();
				objs[2] = first.getStock();
				objs[3] = first.getCount(); //初盘
				if(first.getId() != again.getId()) {
					objs[4] = again.getCount(); //复盘
				}
				objs[5] = first.getCheckTime();
				objs[6] = (first.getRemark()==null ? "" : first.getRemark()) + (again.getRemark()==null ? "" : again.getRemark());
				checkStockItemList.add(objs);
			}
		}catch(Exception e){
			log.error("获取盘点表打印数据时出现异常：", e);
		}finally{
			DbUtil.closeConnection(rs, ps, conn);
		}
		return checkStockItemList;
	}

	/**
	 * 更新商品库存
	 * @param productId 商品id
	 * @param date 查询日期
	 * @return
	 */
	public boolean updateProductStock(int productId, String date) {
		boolean success = false;
		Connection conn = DbUtil.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean oldAutoCommit = true;
		try {
			//记录原来事务提交的方式
			oldAutoCommit = conn.getAutoCommit();
			//关闭自动提交事务
			conn.setAutoCommit(false);
			
			//查询复盘数量
			int stock = 0;
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT cc.id,cc.count from check_stock cc where cc.id = ");
			sql.append(" ( SELECT MAX(c.id) from check_stock c where c.check_time>=STR_TO_DATE('"+date+"','%Y-%m-%d') and c.check_time<ADDDATE(STR_TO_DATE('"+date+"','%Y-%m-%d'), 1) and c.product_id="+productId+")");
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			if (rs.next()) {
				stock = rs.getInt("count");
			}
			
			//查看进销存前的库存信息
			int beforeStock = 0;
        	ps = conn.prepareStatement("select stock from product where id = ?");
        	ps.setInt(1, productId);
			rs = ps.executeQuery();
			if (rs.next()) {
				beforeStock = rs.getInt("stock");
			}
			
			// 更新商品商品库存信息
			ps = conn.prepareStatement("update product set stock=? where id=?");
			ps.setInt(1, stock);
			ps.setInt(2, productId);
			ps.executeUpdate();
			
 			// 生成进销存记录
			Invoice in = new Invoice();
			in.setProductId(productId);
			in.setOperType("5");
			in.setBeforeCount(beforeStock);
			in.setAfterCount(stock);
			in.setCount(stock-beforeStock);
			new InvoiceService().addInvoice(conn, in);
			
			//提交事务
			conn.commit();
			success = true;
		} catch (Exception e) {
			try {
				//回滚事务
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			log.error("更新商品库存时出现异常：", e);
		} finally {
			DbUtil.closeConnection(rs, ps, conn, oldAutoCommit);
		}
		return success;
	}

	/**
	 * 判断是否可以提交本次盘点任务
	 * @param checkStockId 盘点id
	 * @return true:可以提交；false:不能提交
	 */
	public boolean canSubmitCheckStock(int checkStockId) {
		boolean canSubmit = true;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			//保存收货单信息
			conn = DbUtil.getConnection();
			ps = conn.prepareStatement("select i.product_id, count(i.product_id) from check_stock_item i where i.check_stock_id=? GROUP BY i.product_id HAVING count(i.product_id)<2");
			ps.setInt(1, checkStockId);
			rs = ps.executeQuery();
			if(rs.next()) {
				canSubmit = false;
			}
		}catch(Exception e){
			log.error("判断是否可以提交本次盘点任务时出现异常：", e);
		}finally{
			DbUtil.closeConnection(rs, ps, conn);
		}
		return canSubmit;
	}


	/**
	 * 提交盘点任务
	 * @param checkStock 盘点对象
	 */
	public void submitCheckStock(CheckStock checkStock) {
		Connection conn = DbUtil.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean oldAutoCommit = true;
		try {
			//记录原来事务提交的方式
			oldAutoCommit = conn.getAutoCommit();
			//关闭自动提交事务
			conn.setAutoCommit(false);
			
			//保存盘点信息
			ps = conn.prepareStatement("update check_stock set charger=?,remark=?,use_status=? where id=?");
			ps.setString(1, checkStock.getCharger());
			ps.setString(2, checkStock.getRemark());
			ps.setInt(3, 1);
			ps.setInt(4, checkStock.getId());
			ps.executeUpdate();
			
			//查询复盘数量
			List<Product> productList = new ArrayList<Product>();
			Product product = null;
			StringBuilder sql = new StringBuilder();
			sql.append(" SELECT it.product_id,it.count from check_stock_item it join (");
			sql.append(" SELECT MAX(id) maxid from check_stock_item i where i.check_stock_id=? GROUP BY i.product_id) io on it.id=io.maxid");
			ps = conn.prepareStatement(sql.toString());
			ps.setInt(1, checkStock.getId());
			rs = ps.executeQuery();
			while (rs.next()) {
				product = new Product();
				product.setId(rs.getInt("product_id"));
				product.setStock(rs.getInt("count"));
				productList.add(product);
			}
			
			//查看进销存前的库存信息
			List<Invoice> invoiceList = new ArrayList<Invoice>();
			Invoice invoice = new Invoice();
			sql = new StringBuilder();
			sql.append("select id,stock from product where id in(");
			for(Product p : productList) {
				sql.append(p.getId()).append(",");
			}
			sql.deleteCharAt(sql.length()-1);
			sql.append(")");
        	ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while (rs.next()) {
				invoice = new Invoice();
				invoice.setOperType("3");
				invoice.setProductId(rs.getInt("id"));
				invoice.setBeforeCount(rs.getInt("stock"));
				invoiceList.add(invoice);
			}
			
			// 生成进销存记录
			for(Product p : productList) {
				for(Invoice in : invoiceList) {
					if(p.getId() == in.getProductId()) {
						in.setAfterCount(p.getStock());
						in.setCount(p.getStock()-in.getBeforeCount());
					}
				}
			}
			new InvoiceService().addBatInvoice(conn, invoiceList);
			
			// 更新商品商品库存信息
			int count = 0;
			ps = conn.prepareStatement("update product set stock=? where id=?");
			for(Product p : productList) {
				ps.setInt(1, p.getStock());
				ps.setInt(2, p.getId());
				ps.addBatch();
				count++;
				//批量执行
				if (count%100==0 || count==productList.size()) {
					ps.executeBatch();
				}
			}
			
			//提交事务
			conn.commit();
		} catch (Exception e) {
			try {
				//回滚事务
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			log.error("提交盘点任务时出现异常：", e);
		} finally {
			DbUtil.closeConnection(rs, ps, conn, oldAutoCommit);
		}
	}


	/**
	 * 批量保存盘点信息
	 * @param checkStockItemList 盘点列表
	 * @return
	 */
	public boolean saveBatchCheck(List<CheckStockItem> checkStockItemList) {
		boolean success = false;
		Connection conn = DbUtil.getConnection();
		PreparedStatement ps = null;
		boolean oldAutoCommit = true;
		try {
			//记录原来事务提交的方式
			oldAutoCommit = conn.getAutoCommit();
			//关闭自动提交事务
			conn.setAutoCommit(false);
			
			//区分初盘和复盘信息
			List<CheckStockItem> firstCheck = new ArrayList<CheckStockItem>();
			List<CheckStockItem> againCheck = new ArrayList<CheckStockItem>();
			for(CheckStockItem item : checkStockItemList) {
				if(item.getId() != 0) {
					firstCheck.add(item);
				} else {
					againCheck.add(item);
				}
			}
			
			//保存初盘信息
			if(!firstCheck.isEmpty()) {
				ps = conn.prepareStatement("update check_stock_item set count=?,charger=?,remark=? where id=?");
				for(CheckStockItem item : firstCheck) {
					if(item.getId() != 0) {
						ps.setInt(1, item.getCount());
						ps.setString(2, item.getCharger());
						ps.setString(3, item.getRemark());
						ps.setInt(4, item.getId());
						ps.addBatch();
					}
				}
				ps.executeBatch();
			}
			
			//保存初盘信息
			if(!againCheck.isEmpty()) {
				ps = conn.prepareStatement("insert check_stock_item(check_stock_id,product_id,count,stock,charger,remark,check_time) values(?,?,?,?,?,?,?)");
				for(CheckStockItem item : againCheck) {
					if(item.getId() == 0) {
						ps.setInt(1, item.getCheckStockId());
						ps.setInt(2, item.getProductId());
						ps.setInt(3, item.getCount());
						ps.setInt(4, item.getStock());
						ps.setString(5, item.getCharger());
						ps.setString(6, item.getRemark());
						ps.setTimestamp(7, new Timestamp(new Date().getTime()));
						ps.addBatch();
					}
				}
				ps.executeBatch();
			}
			
			//提交事务
			conn.commit();
			success = true;
		} catch (Exception e) {
			try {
				//回滚事务
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			log.error("批量保存盘点信息时出现异常：", e);
		} finally {
			DbUtil.closeConnection(null, ps, conn, oldAutoCommit);
		}
		return success;
	}

}
