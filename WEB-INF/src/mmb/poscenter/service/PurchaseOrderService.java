package mmb.poscenter.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import mmb.poscenter.action.Page;
import mmb.poscenter.domain.Product;
import mmb.poscenter.domain.PurchaseOrder;
import mmb.poscenter.domain.PurchaseOrderItem;
import mmb.poscenter.domain.Supplier;
import mmboa.util.LogUtil;
import mmboa.util.db.BaseService;
import mmboa.util.db.DbUtil;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

public class PurchaseOrderService extends BaseService {
	
	private static Logger log = Logger.getLogger(PurchaseOrderService.class);
	
	/**
	 * 分页获取采购单列表信息
	 * @param page 分页信息
	 * @param param [startTime:开始时间；endTime:结束时间；purchaseOrder:采购单对象]
	 * @return
	 */
	public Page<PurchaseOrder> getPurchaseOrderPage(Page<PurchaseOrder> page, Map<String, Object> param) {
		Connection conn = DbUtil.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			//查询条件
			StringBuilder condSql = new StringBuilder();
			Timestamp startTime = (Timestamp) param.get("startTime");
			Timestamp endTime = (Timestamp) param.get("endTime");
			PurchaseOrder order = (PurchaseOrder) param.get("purchaseOrder");
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
			ps = conn.prepareStatement("select count(p.id) from purchase_order p where 1=1 " + condSql);
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
		    	List<PurchaseOrder> list = new ArrayList<PurchaseOrder>();
		    	PurchaseOrder p;
		    	StringBuilder sql = new StringBuilder(50);
		    	sql.append("SELECT p.id,p.order_number,p.charger,p.department,p.create_time,p.use_status from purchase_order p");
		    	sql.append(" where 1=1 ").append(condSql);
		    	sql.append(" order by p.id desc limit ");
		    	sql.append(page.getFirstResult()).append(",").append(page.getPageCount());
		    	ps = conn.prepareStatement(sql.toString());
		    	index = 1;
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
		    		p = new PurchaseOrder();
		    		p.setId(rs.getInt("id"));
		    		p.setOrderNumber(rs.getString("order_number"));
		    		p.setCharger(rs.getString("charger"));
		    		p.setDepartment(rs.getString("department"));
		    		p.setCreateTime(rs.getTimestamp("create_time"));
		    		p.setUseStatus(rs.getInt("use_status"));
		    		list.add(p);
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

	/**
	 * 根据采购单id获取采购单对象信息
	 * @param id 采购单id
	 * @return
	 */
	public PurchaseOrder getPurchaseOrderById(int id) {
		return (PurchaseOrder) this.getXXX("`id`="+id, "purchase_order", PurchaseOrder.class.getName());
	}
	
	/**
	 * 更新采购单信息
	 * @param purchaseOrder 采购单对象
	 * @return
	 */
	public boolean updatePurchaseOrder(PurchaseOrder purchaseOrder) {
		//更新语句
		StringBuilder set = new StringBuilder();
		set.append("`order_number`='").append(purchaseOrder.getOrderNumber()).append("', ");
		set.append("`charger`='").append(purchaseOrder.getCharger()).append("', ");
		set.append("`department`='").append(purchaseOrder.getDepartment()).append("', ");
		set.append("`use_status`=").append(purchaseOrder.getUseStatus());
		
		return this.updateXXX(set.toString(), "`id`="+purchaseOrder.getId(), "purchase_order");
	}
	
	/**
	 * 删除采购单信息，同时删除该采购单下的所有条目
	 * @param id 采购单id
	 * @return
	 */
	public boolean deletePurchaseOrderById(int id) {
		boolean success = false;
		Connection conn = DbUtil.getConnection();
		PreparedStatement ps = null;
		boolean oldAutoCommit = true;
		try {
			//记录原来事务提交的方式
			oldAutoCommit = conn.getAutoCommit();
			//关闭自动提交事务
			conn.setAutoCommit(false);
			
			//删除采购单条目数据
			ps = conn.prepareStatement("delete from purchase_order_item where purchase_order_id=?");
			ps.setInt(1, id);
			ps.executeUpdate();
			
			//删除采购单数据
			ps = conn.prepareStatement("delete from purchase_order where id=?");
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
			LogUtil.logAccess("删除采购单时出现错误："+e.getMessage());
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
	 * 提交采购单
	 * @param id 采购单id
	 */
	public boolean submitPurchaseOrder(int id) {
		boolean success = false;
		Connection conn = DbUtil.getConnection();
		PreparedStatement ps = null;
		boolean oldAutoCommit = true;
		try {
			//记录原来事务提交的方式
			oldAutoCommit = conn.getAutoCommit();
			//关闭自动提交事务
			conn.setAutoCommit(false);
			
			//更新采购单的状态字段
			ps = conn.prepareStatement("update purchase_order set use_status=1 where id=?");
			ps.setInt(1, id);
			ps.executeUpdate();
			
			//生成收货单
			ps = conn.prepareStatement("INSERT INTO receive_order (purchase_id,order_number) SELECT p.id,CONCAT('SH',SUBSTR(p.order_number,3)) FROM purchase_order p where p.id="+id, Statement.RETURN_GENERATED_KEYS);
			ps.executeUpdate();
			
			//获取收货单id
			int orderId = 0;
			ResultSet rs = ps.getGeneratedKeys();
        	if(rs.next()) {
        		orderId = rs.getInt(1);
        	}
			
			//生成收货单条目
			ps = conn.prepareStatement("INSERT INTO receive_order_item (order_id,send_count,product_id,supplier_id) SELECT ?,p.count,p.product_id,p.supplier_id FROM purchase_order_item p where p.purchase_order_id="+id);
			ps.setInt(1, orderId);
			ps.executeUpdate();
			
			//更新商品的采购参考价
			PurchaseOrderItemService pois = new PurchaseOrderItemService();
			List<PurchaseOrderItem> itemList = pois.getAllItemListByOrderId(id); //获取所有发货单条目
			int count = 0;
			ps = conn.prepareStatement("update product set purchase_refer_price=? where id = ?");
			for(PurchaseOrderItem item : itemList) {
				ps.setDouble(1, item.getPurchasePrice());
				ps.setInt(2, item.getProductId());
				ps.addBatch();
				count++;
				//批量执行
				if (count%100==0 || count==itemList.size()) {
					ps.executeBatch();
				}
			}
			
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
			LogUtil.logAccess("提交采购单时出现错误："+e.getMessage());
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
	 * 导入采购单
	 * @param excel 采购单文件
	 * @throws Exception 
	 */
	public void importPurchaseOrder(File excel) throws Exception {
		Workbook book = null;
		try {
			// 获得第一个工作表对象
			book = Workbook.getWorkbook(excel);
			Sheet sheet = book.getSheet(0);

			// 获取采购负责人和采购单位信息
			PurchaseOrder order = new PurchaseOrder();
			order.setCharger(sheet.getCell(1, 0).getContents());
			order.setDepartment(sheet.getCell(1, 1).getContents());
			
			// 获取订单条目信息
			List<PurchaseOrderItem> itemList = new ArrayList<PurchaseOrderItem>();
			PurchaseOrderItem item;
			Cell[] row;
			System.out.println("sheet.getRows()=="+sheet.getRows());
			for (int i=3; i<sheet.getRows(); i++) {
				row = sheet.getRow(i);
				if(row.length < 4) {
					continue;
				}
				item = new PurchaseOrderItem();
				String productName = row[0].getContents();
				if(StringUtils.isBlank(productName)) {
					continue;
				}
				Product product = new Product();
				product.setName(productName);
				item.setProduct(product);
				Supplier supplier = new Supplier();
				supplier.setName(row[1].getContents());
				item.setSupplier(supplier);
				item.setCount(Integer.parseInt(row[2].getContents()));
				item.setPurchasePrice(Double.parseDouble(row[3].getContents()));
				itemList.add(item);
			}
			
			//采购单条目为空或采购单文件格式错误
			if(itemList.isEmpty()) {
				throw new Exception("采购单条目为空或采购单文件格式错误");
			}
			
			//解析订单中的商品信息和供应商信息
			this.parseProductSupplier(itemList);
			
			//过滤系统中无匹配的商品
			for(int i=itemList.size()-1; i>=0; i--) {
				PurchaseOrderItem orderItem = itemList.get(i);
				if(orderItem.getProductId() == 0) {
					itemList.remove(i);
				}
			}
			
			//批量保存采购单
			this.batchSavePurchaseOrder(order, itemList);
			
		} catch (Exception e) {
			log.error("导入采购单时出现异常：", e);
			throw e;
		} finally {
			if(book != null) {
				book.close();
			}
		}
		
	}
	
	/**
	 * 解析订单中的商品信息和供应商信息 <br/>
	 * 根据商品名称和供应商名称获取商品id和供应商id
	 * @param itemList 采购单条目
	 */
	private void parseProductSupplier(List<PurchaseOrderItem> itemList) {
		Connection conn = DbUtil.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			// 根据商品名称查找商品
			StringBuilder sql = new StringBuilder();
			sql.append("select p.id,p.`name` from product p where p.`name` in(");
			for (int i=0; i<itemList.size(); i++) {
				sql.append("?,");
			}
			sql.deleteCharAt(sql.length() - 1);
			sql.append(")");
			ps = conn.prepareStatement(sql.toString());
			int index = 1;
			for (PurchaseOrderItem item : itemList) {
				ps.setString(index++, item.getProduct().getName());
			}
			rs = ps.executeQuery();
			while (rs.next()) {
				int productId = rs.getInt("id");
				String productName = rs.getString("name");
				for (PurchaseOrderItem item : itemList) {
					if (productName.equals(item.getProduct().getName())) {
						item.getProduct().setId(productId);
						item.setProductId(productId);
					}
				}
			}

			// 根据供应商名称查找供应商
			sql = new StringBuilder();
			sql.append("select id, `name` from supplier where `name` in(");
			for (int i=0; i<itemList.size(); i++) {
				sql.append("?,");
			}
			sql.deleteCharAt(sql.length() - 1);
			sql.append(")");
			ps = conn.prepareStatement(sql.toString());
			index = 1;
			for (PurchaseOrderItem item : itemList) {
				ps.setString(index++, item.getSupplier().getName());
			}
			rs = ps.executeQuery();
			while (rs.next()) {
				int supplierId = rs.getInt("id");
				String supplierName = rs.getString("name");
				for (PurchaseOrderItem item : itemList) {
					if (supplierName.equals(item.getSupplier().getName())) {
						item.getSupplier().setId(supplierId);
						item.setSupplierId(supplierId);
					}
				}
			}
		} catch (Exception e) {
			log.error("解析订单中的商品信息和供应商信息时出现异常：", e);
		} finally {
			DbUtil.closeConnection(rs, ps, conn);
		}
	}
	
	/**
	 * 批量保存采购单
	 * @param order 采购单对象
	 * @param itemList 采购单条目
	 * @return
	 */
	public boolean batchSavePurchaseOrder(PurchaseOrder order, List<PurchaseOrderItem> itemList) {
		boolean success = false;
		Connection conn = DbUtil.getConnection();
		PreparedStatement ps = null;
		boolean oldAutoCommit = true;
		try {
			//记录原来事务提交的方式
			oldAutoCommit = conn.getAutoCommit();
			//关闭自动提交事务
			conn.setAutoCommit(false);
			
			//保存采购单
			ps = conn.prepareStatement("insert purchase_order(order_number,charger,department,use_status,create_time) values(?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, "DD"+String.valueOf(new Date().getTime()).substring(2, 10));
			ps.setString(2, order.getCharger());
			ps.setString(3, order.getDepartment());
			ps.setInt(4, 0);
			ps.setTimestamp(5, new Timestamp(new Date().getTime()));
			ps.executeUpdate();
			
			//获取采购单id
			int orderId = 0;
			ResultSet rs = ps.getGeneratedKeys();
        	if(rs.next()) {
        		orderId = rs.getInt(1);
        	}
			
			//保存采购单条目
			ps = conn.prepareStatement("insert purchase_order_item(purchase_order_id,product_id,supplier_id,count,purchase_price) values(?,?,?,?,?)");
			for(PurchaseOrderItem item : itemList) {
				ps.setInt(1, orderId);
				ps.setInt(2, item.getProductId());
				ps.setInt(3, item.getSupplierId());
				ps.setInt(4, item.getCount());
				ps.setDouble(5, item.getPurchasePrice());
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
			log.error("批量保存采购单时出现异常：", e);
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
	 * 导出Excel采购单
	 * @param order 采购单对象
	 * @return excel文件字节数据
	 */
	public byte[] exportExcel(PurchaseOrder order) {
		byte[] data = null;
		
		//获取条目数量
		PurchaseOrderItemService itemService = new PurchaseOrderItemService();
		int itemCount = itemService.getItemCountByOrderId(order.getId());
		
		//获取所有采购单条目信息
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("purchaseOrderId", order.getId());
		Page<PurchaseOrderItem> page = new Page<PurchaseOrderItem>(1, itemCount);
		page = itemService.getPurchaseOrderItemPage(page, param);
		
		WritableWorkbook book = null;
		ByteArrayOutputStream baos = null;
		try {
			//把工作薄保存到内存中
			baos = new ByteArrayOutputStream();
			book = Workbook.createWorkbook(baos);
			WritableSheet sheet = book.createSheet("采购单信息", 0);
			
			//设置列宽
			sheet.setColumnView(0, 35);
			sheet.setColumnView(1, 35);
			sheet.setColumnView(2, 15);
			sheet.setColumnView(3, 15);
			
			//采购单信息
			WritableCellFormat format = new WritableCellFormat();
			format.setAlignment(Alignment.RIGHT);
			sheet.addCell(new Label(0, 0, "采购单号：", format));
			sheet.addCell(new Label(0, 1, "负责人：", format));
			sheet.addCell(new Label(0, 2, "采购单位：", format));
			sheet.addCell(new Label(0, 3, "创建时间：", format));
			sheet.addCell(new Label(1, 0, order.getOrderNumber()));
			sheet.addCell(new Label(1, 1, order.getCharger()));
			sheet.addCell(new Label(1, 2, order.getDepartment()));
			sheet.addCell(new Label(1, 3, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(order.getCreateTime())));
			
			//条目信息
			WritableFont font = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD); 
			format = new WritableCellFormat(font);
			format.setAlignment(Alignment.CENTRE);
			sheet.addCell(new Label(0, 4, "商品名称", format));
			sheet.addCell(new Label(1, 4, "供应商名称", format));
			sheet.addCell(new Label(2, 4, "采购数量", format));
			sheet.addCell(new Label(3, 4, "采购价格", format));
			for(int i=0; i<page.getList().size(); i++) {
				PurchaseOrderItem item = page.getList().get(i);
				sheet.addCell(new Label(0, 5+i, item.getProduct().getName()));
				sheet.addCell(new Label(1, 5+i, item.getSupplier().getName()));
				sheet.addCell(new jxl.write.Number(2, 5+i, item.getCount()));
				sheet.addCell(new jxl.write.Number(3, 5+i, item.getPurchasePrice()));
			}
			
			// 写入数据并关闭文件
			book.write();
			book.close();
			
			//转化为字节数组
			data = baos.toByteArray();
		} catch (Exception e) {
			log.error("导出Excel采购单时出现异常：", e);
		} finally {
			if(baos != null) {
				try {
					baos.close();
					baos = null;
				} catch (IOException e) {}
			}
		}
		
		return data;
	}

}
