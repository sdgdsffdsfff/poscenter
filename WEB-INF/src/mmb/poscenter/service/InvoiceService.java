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

import mmb.auth.domain.User;
import mmb.poscenter.action.Page;
import mmb.poscenter.domain.GoodsClass;
import mmb.poscenter.domain.Invoice;
import mmb.poscenter.domain.Product;
import mmb.poscenter.util.AuthHelper;
import mmboa.util.db.BaseService;
import mmboa.util.db.DbOperation;
import mmboa.util.db.DbUtil;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

public class InvoiceService extends BaseService {
	
	private static Logger log = Logger.getLogger(InvoiceService.class);
	
	/**
	 * 新增进销存记录
	 * @param conn 数据库连接对象，此方法中不处理事务
	 * @param invoice 进销存对象
	 * @throws SQLException
	 */
	public void addInvoice(Connection conn, Invoice invoice) throws SQLException {
		String sql = "insert into invoice(id,product_id,oper_type,before_count,count,after_count,oper_user,create_time,remark,serial_number) values(?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setInt(1, invoice.getId());
		ps.setInt(2, invoice.getProductId());
		ps.setString(3, invoice.getOperType());
		ps.setInt(4, invoice.getBeforeCount());
		ps.setInt(5, invoice.getCount());
		ps.setInt(6, invoice.getAfterCount());
		ps.setString(7, invoice.getOperUser());
		ps.setTimestamp(8, new Timestamp(new Date().getTime()));
		ps.setString(9,invoice.getRemark());
		ps.setString(10, invoice.getSerialNumber());
		ps.executeUpdate();
		ps.close();
	}
	
	/**
	 * 批量保存进销存记录
	 * @param conn 数据库连接对象，此方法中不处理事务
	 * @param list 进销存对象列表
	 * @throws SQLException
	 */
	public void addBatInvoice(Connection conn, List<Invoice> list) throws SQLException {
		String sql = "insert into invoice(id,product_id,oper_type,before_count,count,after_count,oper_user,create_time,remark,serial_number) values(?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement ps = conn.prepareStatement(sql);
		for (Invoice invoice : list) {
			ps.setInt(1, invoice.getId());
			ps.setInt(2, invoice.getProductId());
			ps.setString(3, invoice.getOperType());
			ps.setInt(4, invoice.getBeforeCount());
			ps.setInt(5, invoice.getCount());
			ps.setInt(6, invoice.getAfterCount());
			//获取当前用户信息
			User vo = AuthHelper.getCurrentUser();
			if(StringUtils.isBlank(invoice.getOperUser()) && vo != null) {
				invoice.setOperUser(vo.getUserName());
			}
			ps.setString(7, invoice.getOperUser());
			ps.setTimestamp(8, new Timestamp(new Date().getTime()));
			ps.setString(9, invoice.getRemark());
			ps.setString(10, invoice.getSerialNumber());
			ps.addBatch();
		}
		ps.executeBatch();
		ps.close();
	}
	
	/**
	 * 分页获取进销存记录列表数据
	 * @param page 分页信息
	 * @param productId 商品id
	 * @return
	 */
	public Page<Invoice> getInvoicePage(Page<Invoice> page, int productId){
		List<Invoice>  tmp = new ArrayList<Invoice>();
		DbOperation db = new DbOperation();
		Invoice in ;
		try{
			//查询条件
			String condSql = "";
			if(productId != 0) {
				condSql = " and i.product_id="+productId;
			}
			
			//查询总记录数
			StringBuilder sb = new StringBuilder(50);
			sb.append("select count(id) from invoice i where 1=1 ").append(condSql);
			ResultSet rs = db.executeQuery(sb.toString());
		    if(rs.next()){
		    	page.setTotalRecords(rs.getInt(1));
		    }
		    
		    //查询列表数据
		    if(page.getTotalRecords() > 0) {
		    	StringBuilder sql = new StringBuilder(50);
		    	sql.append("select i.remark,i.oper_user,i.before_count,i.after_count,i.count,i.create_time,i.oper_type,i.serial_number,p.bar_code,p.name productName");
		    	sql.append(" FROM invoice i LEFT JOIN product p ON i.product_id = p.id");
		    	sql.append(" where 1=1 ").append(condSql);
		    	sql.append(" order by i.create_time desc ");
		    	sql.append(" limit ");
		    	sql.append(page.getFirstResult()).append(",").append(page.getPageCount());
		    	rs = db.executeQuery(sql.toString());
		    	while(rs.next()){
		    		in = new Invoice();
		    		in.setAfterCount(rs.getInt("after_count"));
		    		in.setCount(rs.getInt("count"));
		    		in.setBeforeCount(rs.getInt("before_count"));
		    		in.setCreateTime(rs.getTimestamp("create_time"));
		    		in.setOperType(rs.getString("oper_type"));
		    		in.setProductCode(rs.getString("bar_code"));
		    		in.setProductName(rs.getString("productName"));
		    		in.setSerialNumber(rs.getString("serial_number"));
		    		in.setOperUser(rs.getString("oper_user"));
		    		in.setRemark(rs.getString("remark"));
		    		tmp.add(in);
		    	}
		    	page.setList(tmp);
		    }
		}catch(Exception e){
			log.error("分页获取进销存记录列表数据时出现异常：", e);
		}finally{
			this.release(db);
		}
		return page;
	}

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
			countSql.append("select count(*) from product p join (SELECT i.product_id from invoice i GROUP BY i.product_id) ip on p.id=ip.product_id left join goods_class gc on p.goods_class_id=gc.id ");
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
		    	sql.append(" from product p join (SELECT i.product_id from invoice i GROUP BY i.product_id) ip on p.id=ip.product_id ");
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
			log.error("分页获取采购单条目列表信息时出现异常：", e);
		}finally{
			DbUtil.closeConnection(rs, ps, conn);
		}
		return page;
	}
  
}
