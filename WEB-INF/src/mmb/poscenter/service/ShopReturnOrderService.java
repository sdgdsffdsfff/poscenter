package mmb.poscenter.service;

import java.io.ByteArrayOutputStream;
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
import java.util.List;
import java.util.Map;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import mmb.poscenter.action.Page;
import mmb.poscenter.domain.Invoice;
import mmb.poscenter.domain.Shop;
import mmb.poscenter.domain.ShopReturnOrder;
import mmb.poscenter.domain.ShopReturnOrderItem;
import mmboa.util.db.BaseService;
import mmboa.util.db.DbUtil;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.google.gson.Gson;

public class ShopReturnOrderService extends BaseService {
	
	private static Logger log = Logger.getLogger(ShopReturnOrderService.class);
	
	/**
	 * 分页获取店面退货单列表信息
	 * @param page 分页信息
	 * @param param [shopName:店面名称；startTime:开始时间；endTime:结束时间；shopReturnOrder:店面退货单对象]
	 * @return
	 */
	public Page<ShopReturnOrder> getShopReturnOrderPage(Page<ShopReturnOrder> page, Map<String, Object> param) {
		Connection conn = DbUtil.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			// 查询条件
			StringBuilder condSql = new StringBuilder();
			String shopName = (String) param.get("shopName");
			Timestamp startTime = (Timestamp) param.get("startTime");
			Timestamp endTime = (Timestamp) param.get("endTime");
			ShopReturnOrder order = (ShopReturnOrder) param.get("shopReturnOrder");
			if (StringUtils.isNotBlank(shopName)) {
				condSql.append(" and s.name like ? ");
			}
			if (StringUtils.isNotBlank(order.getOrderNumber())) {
				condSql.append(" and o.order_number like ? ");
			}
			if (StringUtils.isNotBlank(order.getCharger())) {
				condSql.append(" and o.charger like ? ");
			}
			if (order.getUseStatus() != -1) {
				condSql.append(" and o.use_status=? ");
			}
			if (startTime != null) {
				condSql.append(" and o.create_time>=? ");
			}
			if (endTime != null) {
				condSql.append(" and o.create_time<=? ");
			}

			// 查询总记录数
			int index = 1;
			ps = conn.prepareStatement("select count(o.id) from shop_return_order o join shop s on o.shop_id=s.id where 1=1 " + condSql);
			if (StringUtils.isNotBlank(shopName)) {
				ps.setString(index++, "%" + shopName + "%");
			}
			if (StringUtils.isNotBlank(order.getOrderNumber())) {
				ps.setString(index++, "%" + order.getOrderNumber() + "%");
			}
			if (StringUtils.isNotBlank(order.getCharger())) {
				ps.setString(index++, "%" + order.getCharger() + "%");
			}
			if (order.getUseStatus() != -1) {
				ps.setInt(index++, order.getUseStatus());
			}
			if (startTime != null) {
				ps.setTimestamp(index++, startTime);
			}
			if (endTime != null) {
				ps.setTimestamp(index++, endTime);
			}
			rs = ps.executeQuery();
			if (rs.next()) {
				page.setTotalRecords(rs.getInt(1));
			}

			// 获取列表数据
			if (page.getTotalRecords() > 0) {
				List<ShopReturnOrder> list = new ArrayList<ShopReturnOrder>();
				ShopReturnOrder ro;
				StringBuilder sql = new StringBuilder(50);
				sql.append("select o.id,o.order_number,o.charger,o.use_status,o.create_time,o.shop_id,s.`code` shopCode,s.`name` shopName from shop_return_order o join shop s on o.shop_id=s.id");
				sql.append(" where 1=1 ").append(condSql);
				sql.append(" order by o.id desc limit ");
				sql.append(page.getFirstResult()).append(",").append(page.getPageCount());
				ps = conn.prepareStatement(sql.toString());
				index = 1;
				if (StringUtils.isNotBlank(shopName)) {
					ps.setString(index++, "%" + shopName + "%");
				}
				if (StringUtils.isNotBlank(order.getOrderNumber())) {
					ps.setString(index++, "%" + order.getOrderNumber() + "%");
				}
				if (StringUtils.isNotBlank(order.getCharger())) {
					ps.setString(index++, "%" + order.getCharger() + "%");
				}
				if (order.getUseStatus() != -1) {
					ps.setInt(index++, order.getUseStatus());
				}
				if (startTime != null) {
					ps.setTimestamp(index++, startTime);
				}
				if (endTime != null) {
					ps.setTimestamp(index++, endTime);
				}
				rs = ps.executeQuery();
				while (rs.next()) {
					ro = new ShopReturnOrder();
					ro.setId(rs.getInt("id"));
					ro.setOrderNumber(rs.getString("order_number"));
					ro.setCharger(rs.getString("charger"));
					ro.setCreateTime(rs.getTimestamp("create_time"));
					ro.setUseStatus(rs.getInt("use_status"));
					//店面信息
		    		Shop shop = new Shop();
		    		shop.setId(rs.getInt("shop_id"));
		    		shop.setCode(rs.getString("shopCode"));
		    		shop.setName(rs.getString("shopName"));
		    		ro.setShopId(shop.getId());
		    		ro.setShop(shop);
					list.add(ro);
				}
				page.setList(list);
			}
		} catch (Exception e) {
			log.error("分页获取店面退货单列表信息时出现异常：", e);
		} finally {
			DbUtil.closeConnection(rs, ps, conn);
		}
		return page;
	}

	/**
	 * 根据店面退货单id获取店面退货单对象信息
	 * @param id 店面退货单id
	 * @return
	 */
	public ShopReturnOrder getShopReturnOrderById(int id) {
		return (ShopReturnOrder) this.getXXX("`id`="+id, "shop_return_order", ShopReturnOrder.class.getName());
	}
	
	/**
	 * 根据店面退货单id获取店面退货单详细信息（包括店面信息）
	 * @param id 店面退货单id
	 * @return
	 */
	public ShopReturnOrder getDetailById(int id) {
		ShopReturnOrder order = null;
		Connection conn = DbUtil.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			String sql = "select o.id,o.order_number,o.charger,o.use_status,o.create_time,o.shop_id,s.`code` shopCode,s.`name` shopName " +
					"from shop_return_order o join shop s on o.shop_id=s.id where o.id=?";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, id);
			rs = ps.executeQuery();
			if (rs.next()) {
				order = new ShopReturnOrder();
				order.setId(rs.getInt("id"));
				order.setOrderNumber(rs.getString("order_number"));
				order.setCharger(rs.getString("charger"));
				order.setCreateTime(rs.getTimestamp("create_time"));
				order.setUseStatus(rs.getInt("use_status"));
				//店面信息
	    		Shop shop = new Shop();
	    		shop.setId(rs.getInt("shop_id"));
	    		shop.setCode(rs.getString("shopCode"));
	    		shop.setName(rs.getString("shopName"));
	    		order.setShopId(shop.getId());
	    		order.setShop(shop);
			}
		} catch (Exception e) {
			log.error("根据店面退货单id获取店面退货单详细信息（包括店面信息）时出现异常：", e);
		} finally {
			DbUtil.closeConnection(rs, ps, conn);
		}
		return order;
	}
	
	/**
	 * 确认退货
	 * @param shopReturnOrder 店面退货单
	 * @param itemList 店面退货单条目
	 * @throws Exception 
	 */
	public void confirmReturn(ShopReturnOrder shopReturnOrder, List<ShopReturnOrderItem> itemList) throws Exception {
		Connection conn = DbUtil.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean oldAutoCommit = true;
		try {
			//记录原来事务提交的方式
			oldAutoCommit = conn.getAutoCommit();
			//关闭自动提交事务
			conn.setAutoCommit(false);
			
			//保存店面退货单信息
			ps = conn.prepareStatement("update shop_return_order set charger=?,use_status=? where id=?");
			ps.setString(1, shopReturnOrder.getCharger());
			ps.setInt(2, 1);
			ps.setInt(3, shopReturnOrder.getId());
			ps.executeUpdate();
			
			//批量保存退货单条目的实际收货量
        	ps = conn.prepareStatement("update shop_return_order_item set receive_count=? where id=?");
        	for(ShopReturnOrderItem item : itemList) {
        		ps.setInt(1, item.getReceiveCount());
        		ps.setInt(2, item.getId());
        		ps.addBatch();
        	}
        	ps.executeBatch();
			
			//查看进销存前的库存信息，并生成进销存记录
			List<Invoice> invoiceList = new ArrayList<Invoice>();
			Invoice invoice = new Invoice();
			StringBuilder sql = new StringBuilder();
			sql.append("select id,stock from product where id in(");
			for(ShopReturnOrderItem item : itemList) {
				sql.append(item.getProductId()).append(",");
			}
			sql.deleteCharAt(sql.length()-1);
			sql.append(")");
        	ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while (rs.next()) {
				invoice = new Invoice();
				invoice.setOperType("2");
				invoice.setSerialNumber(shopReturnOrder.getOrderNumber());
				invoice.setProductId(rs.getInt("id"));
				invoice.setBeforeCount(rs.getInt("stock"));
				invoice.setAfterCount(invoice.getBeforeCount()); //处理多条条目中存在相同商品的问题
				for(ShopReturnOrderItem item : itemList) {
					if(item.getProductId() == invoice.getProductId()) {
						invoice.setAfterCount(invoice.getAfterCount() + item.getReceiveCount());
						invoice.setCount(invoice.getCount() + item.getReceiveCount());
					}
				}
				invoiceList.add(invoice);
			}
			new InvoiceService().addBatInvoice(conn, invoiceList);
			
			// 更新商品商品库存信息
			int count = 0;
			ps = conn.prepareStatement("update product set stock=stock+? where id=?");
			for(ShopReturnOrderItem item : itemList) {
				ps.setInt(1, item.getReceiveCount());
				ps.setInt(2, item.getProductId());
				ps.addBatch();
				count++;
				//批量执行
				if (count%100==0 || count==itemList.size()) {
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
			log.error("确认退货时出现异常：", e);
			throw e;
		} finally {
			DbUtil.closeConnection(rs, ps, conn, oldAutoCommit);
		}
	}

	/**
	 * 导出Excel店面退货单
	 * @param order 店面退货单对象
	 * @return excel文件字节数据
	 */
	public byte[] exportExcel(ShopReturnOrder order) {
		byte[] data = null;
		
		//获取所有店面退货单条目信息
		ShopReturnOrderItemService itemService = new ShopReturnOrderItemService();
		List<ShopReturnOrderItem> allItemList = itemService.getAllItemListByOrderId(order.getId());
		
		WritableWorkbook book = null;
		ByteArrayOutputStream baos = null;
		try {
			//把工作薄保存到内存中
			baos = new ByteArrayOutputStream();
			book = Workbook.createWorkbook(baos);
			WritableSheet sheet = book.createSheet("退货单信息", 0);
			
			//设置列宽
			sheet.setColumnView(0, 15);
			sheet.setColumnView(1, 35);
			sheet.setColumnView(2, 15);
			sheet.setColumnView(3, 15);
			
			//采购单信息
			WritableCellFormat format = new WritableCellFormat();
			format.setAlignment(Alignment.RIGHT);
			sheet.addCell(new Label(0, 0, "店面编号：", format));
			sheet.addCell(new Label(0, 1, "店面名称：", format));
			sheet.addCell(new Label(0, 2, "退货单号：", format));
			sheet.addCell(new Label(0, 3, "负责人：", format));
			sheet.addCell(new Label(0, 4, "创建时间：", format));
			sheet.addCell(new Label(1, 0, order.getShop().getCode()));
			sheet.addCell(new Label(1, 1, order.getShop().getName()));
			sheet.addCell(new Label(1, 2, order.getOrderNumber()));
			sheet.addCell(new Label(1, 3, order.getCharger()));
			sheet.addCell(new Label(1, 4, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(order.getCreateTime())));
			
			//条目信息
			WritableFont font = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD); 
			format = new WritableCellFormat(font);
			format.setAlignment(Alignment.CENTRE);
			sheet.addCell(new Label(0, 5, "序号", format));
			sheet.addCell(new Label(1, 5, "商品名称", format));
			sheet.addCell(new Label(2, 5, "退货数量", format));
			sheet.addCell(new Label(3, 5, "实收数量", format));
			format = new WritableCellFormat();
			format.setAlignment(Alignment.CENTRE);
			for(int i=0; i<allItemList.size(); i++) {
				ShopReturnOrderItem item = allItemList.get(i);
				sheet.addCell(new jxl.write.Number(0, 6+i, i+1, format));
				sheet.addCell(new Label(1, 6+i, item.getProduct().getName()));
				sheet.addCell(new jxl.write.Number(2, 6+i, item.getReturnCount()));
				sheet.addCell(new jxl.write.Number(3, 6+i, item.getReceiveCount()));
			}
			
			//写入数据并关闭文件
			book.write();
			book.close();
			
			//转化为字节数组
			data = baos.toByteArray();
		} catch (Exception e) {
			log.error("导出Excel店面退货单时出现异常：", e);
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

	/**
	 * 店面系统提交店面退货单信息
	 * @param json 店面提交的JSON字符串
	 * @return
	 */
	public String submitShopReturnOrder(String json) {
		String resutl = "true";
		
		try {
			//解析数据
			Gson gson = new Gson();
			ShopReturnOrder shopReturnOrder = gson.fromJson(json, ShopReturnOrder.class);
			
			//根据店面编号获取店面信息
			ShopService shopService = new ShopService();
			String shopCode = shopReturnOrder.getShop().getCode();
			Shop shop = shopService.getShopByCode(shopCode);
			
			//店面不存在
			if(shop == null) {
				throw new Exception("店面编号不存在");
			}
			
			//设置店面id
			shopReturnOrder.setShopId(shop.getId());
			
			//批量保存店面库存
			this.batchSaveShopReturnOrder(shopReturnOrder);
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
	public void batchSaveShopReturnOrder(ShopReturnOrder shopReturnOrder) throws Exception {
		Connection conn = DbUtil.getConnection();
		PreparedStatement ps = null;
		boolean oldAutoCommit = true;
		try {
			//记录原来事务提交的方式
			oldAutoCommit = conn.getAutoCommit();
			//关闭自动提交事务
			conn.setAutoCommit(false);
			
			//保存收货单信息
			ps = conn.prepareStatement("insert into shop_return_order(shop_id,order_number,`create_time`,`use_status`) values (?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1, shopReturnOrder.getShopId());
			ps.setString(2, shopReturnOrder.getOrderNumber());
			ps.setTimestamp(3, new Timestamp(new Date().getTime()));
			ps.setInt(4, 0);
			ps.executeUpdate();
			
			//获取订单id
			int orderId = 0;
			ResultSet rs = ps.getGeneratedKeys();
			if(rs.next()) {
				orderId = rs.getInt(1);
			}
			
			//批量保存收货单条目
			ps = conn.prepareStatement("insert into shop_return_order_item(`shop_return_order_id`,`product_id`,`return_count`) values (?,?,?)");
			for(ShopReturnOrderItem item : shopReturnOrder.getItemList()) {
				ps.setInt(1, orderId);
				ps.setInt(2, item.getProductId());
				ps.setInt(3, item.getReturnCount());
				ps.addBatch();
			}
			ps.executeBatch();
			
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
