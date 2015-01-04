package mmb.poscenter.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
import mmb.poscenter.domain.GoodsClass;
import mmb.poscenter.domain.Product;
import mmb.poscenter.util.BarcodeUtil;
import mmboa.util.db.BaseService;
import mmboa.util.db.DbOperation;
import mmboa.util.db.DbUtil;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.google.gson.Gson;

public class ProductService extends BaseService {
	
	private static Logger log = Logger.getLogger(ProductService.class);
	
	/**
	 * 获取JSON格式的所有商品列表数据
	 * @return JSON字符串
	 * @throws Exception 
	 */
	public String getAllProductJson() throws Exception{
		String json = "";
		try {
			List<Product> productList = this.getAllProductList();
			json = new Gson().toJson(productList);
		} catch (Exception e) {
			log.error("获取JSON格式的所有商品列表数据时出现异常：", e);
			json = e.toString();
			throw e;
		}
		return json;
	}
	
	/**
	 * 获取所有商品列表数据
	 * @return
	 * @throws Exception 
	 */
	public List<Product> getAllProductList() throws Exception {
		List<Product> productList = new ArrayList<Product>();
		Connection conn = DbUtil.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("select p.id,p.bar_code,p.name,p.lease_price,p.month_lease_price,p.sale_price,p.limit_price,p.lock_price,p.red_lines,p.blue_lines,p.deposit,p.stock,p.supplier_id,p.goods_class_id from product p");
			rs = ps.executeQuery();
			Product p = null;
			while (rs.next()) {
				p = new Product();
				p.setId(rs.getInt("id"));
				p.setBarCode(rs.getString("bar_code"));
				p.setName(rs.getString("name"));
				p.setLeasePrice(rs.getDouble("lease_price"));
				p.setMonthLeasePrice(rs.getDouble("month_lease_price"));
				p.setDeposit(rs.getDouble("deposit"));
				p.setSalePrice(rs.getDouble("sale_price"));
				p.setLimitPrice(rs.getDouble("limit_price"));
				p.setLockPrice(rs.getDouble("lock_price"));
				p.setRedLines(rs.getDouble("red_lines"));
				p.setBlueLines(rs.getDouble("blue_lines"));
				p.setStock(rs.getInt("stock"));
				p.setSupplierId(rs.getInt("supplier_id"));
				p.setGoodsClassId(rs.getString("goods_class_id"));
				productList.add(p);
			}
		} catch (Exception e) {
			log.error("获取所有商品列表数据时出现异常：", e);
			throw e;
		} finally {
			DbUtil.closeConnection(rs, ps, conn);
		}
		return productList;
	}
	
	/**
	 * 分页获取商品列表信息
	 * @param page 分页信息
	 * @param param 查询条件[name:商品名称；supplierId:供应商id；supplierName:供应商名称；goodsClassId:商品分类id；goodsClassName:商品分类名称]
	 * @return
	 */
	public Page<Product> getProductList(Page<Product> page, Map<String, Object> param) {
		Connection conn = DbUtil.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			// 查询条件
			String name = (String) param.get("name");
			int supplierId = 0;
			if (param.get("supplierId") != null) {
				supplierId = (Integer) param.get("supplierId");
			}
			String goodsClassId = (String) param.get("goodsClassId");
			String supplierName = (String) param.get("supplierName");
			String goodsClassName = (String) param.get("goodsClassName");
			StringBuffer condSql = new StringBuffer();
			if (StringUtils.isNotBlank(name)) {
				condSql.append(" and p.`name` like ? ");
			}
			if (supplierId != 0) {
				condSql.append(" and p.supplier_id = ?");
			}
			if (supplierId == 0 && StringUtils.isNotBlank(supplierName)) {
				condSql.append(" and s.`name` like ? ");
			}
			if (StringUtils.isNotBlank(goodsClassId)) {
				condSql.append(" and p.goods_class_id like ?");
			}
			if (StringUtils.isBlank(goodsClassId) && StringUtils.isNotBlank(goodsClassName)) {
				condSql.append(" and gc.name like ?");
			}

			// 查询总记录数
			int index = 1;
			ps = conn.prepareStatement("select count(p.id) from product p LEFT JOIN goods_class gc on p.goods_class_id=gc.id LEFT JOIN supplier s on p.supplier_id=s.id where p.is_delete=0" + condSql);
			if (StringUtils.isNotBlank(name)) {
				ps.setString(index++, "%" + name + "%");
			}
			if (supplierId != 0) {
				ps.setInt(index++, supplierId);
			}
			if (supplierId == 0 && StringUtils.isNotBlank(supplierName)) {
				ps.setString(index++, "%" + supplierName + "%");
			}
			if (StringUtils.isNotBlank(goodsClassId)) {
				ps.setString(index++, "" + goodsClassId + "%");
			}
			if (StringUtils.isBlank(goodsClassId) && StringUtils.isNotBlank(goodsClassName)) {
				ps.setString(index++, "%" + goodsClassName + "%");
			}
			rs = ps.executeQuery();
			if (rs.next()) {
				page.setTotalRecords(rs.getInt(1));
			}

			// 查询列表数据
			if (page.getTotalRecords() > 0) {
				StringBuilder sql = new StringBuilder(50);
				sql.append("select p.id,p.bar_code,p.name,p.lease_price,p.month_lease_price,p.sale_price,p.limit_price,p.lock_price,p.red_lines,p.blue_lines,p.deposit,p.stock,p.supplier_id,p.goods_class_id,gc.`name` goodsClassName,s.`name` supplierName");
				sql.append(" from product p LEFT JOIN goods_class gc on p.goods_class_id=gc.id LEFT JOIN supplier s on p.supplier_id=s.id");
				sql.append(" where p.is_delete = 0 ").append(condSql);
				sql.append(" order by p.id desc limit ");
				sql.append(page.getFirstResult()).append(",").append(page.getPageCount());
				ps = conn.prepareStatement(sql.toString());
				index = 1;
				if (StringUtils.isNotBlank(name)) {
					ps.setString(index++, "%" + name + "%");
				}
				if (supplierId != 0) {
					ps.setInt(index++, supplierId);
				}
				if (supplierId == 0 && StringUtils.isNotBlank(supplierName)) {
					ps.setString(index++, "%" + supplierName + "%");
				}
				if (StringUtils.isNotBlank(goodsClassId)) {
					ps.setString(index++, "" + goodsClassId + "%");
				}
				if (StringUtils.isBlank(goodsClassId) && StringUtils.isNotBlank(goodsClassName)) {
					ps.setString(index++, "%" + goodsClassName + "%");
				}
				rs = ps.executeQuery();
				List<Product> tmp = new ArrayList<Product>();
				Product p;
				while (rs.next()) {
					p = new Product();
					p.setId(rs.getInt("id"));
					p.setBarCode(rs.getString("bar_code"));
					p.setName(rs.getString("name"));
					p.setLeasePrice(rs.getDouble("lease_price"));
					p.setMonthLeasePrice(rs.getDouble("month_lease_price"));
					p.setDeposit(rs.getDouble("deposit"));
					p.setSalePrice(rs.getDouble("sale_price"));
					p.setLimitPrice(rs.getDouble("limit_price"));
			    	p.setLockPrice(rs.getDouble("lock_price"));
			    	p.setRedLines(rs.getDouble("red_lines"));
			    	p.setBlueLines(rs.getDouble("blue_lines"));
					p.setStock(rs.getInt("stock"));
					// 供应商信息
					p.setSupplierId(rs.getInt("supplier_id"));
					p.setSupplierName(rs.getString("supplierName"));
					// 商品分类信息
					GoodsClass gc = new GoodsClass();
					gc.setId(rs.getString("goods_class_id"));
					gc.setName(rs.getString("goodsClassName"));
					p.setGoodsClassId(gc.getId());
					p.setGoodsClass(gc);
					tmp.add(p);
				}
				page.setList(tmp);
			}
		} catch (Exception e) {
			log.error("分页获取商品列表信息时出现异常：", e);
		} finally {
			DbUtil.closeConnection(rs, ps, conn);
		}
		return page;
	}

	/**
	 * 根据商品id获取商品对象信息
	 * @param id 商品id
	 * @return
	 */
	public Product getProductById(int id) {
		return (Product) this.getXXX("`id`="+id, "product", Product.class.getName());
	}
	
	/**
	 * 根据商品条形码获取商品对象信息
	 * @param barcode 商品条形码
	 * @return
	 */
	public Product getProductByBarcode(String barcode) {
		return (Product) this.getXXX("`bar_code`='"+barcode+"'", "product", Product.class.getName());
	}
	
	/**
	 * 根据商品id获取商品详细信息（包括商品分类信息、供应商信息）
	 * @param id 商品id
	 * @return
	 */
	public Product getProductDetailById(int id) {
		DbOperation db = new DbOperation();
		Product product = null;
		try{
			String sql = "SELECT p.id,p.goods_class_id,gc.`name` goodsClassName,p.bar_code,p.`name`,p.sale_price,p.limit_price,p.lock_price,p.red_lines,p.blue_lines,p.lease_price,p.month_lease_price,p.deposit,p.stock,p.supplier_id,s.`name` supplierName " +
					" from product p LEFT JOIN goods_class gc on p.goods_class_id=gc.id " +
					" LEFT JOIN supplier s on p.supplier_id=s.id " +
					" where p.id="+id;
			ResultSet rs = db.executeQuery(sql);
		    if(rs.next()){
		    	product = new Product();
		    	product.setId(rs.getInt("id"));
		    	product.setName(rs.getString("name"));
		    	product.setBarCode(rs.getString("bar_code"));
		    	product.setLeasePrice(rs.getDouble("lease_price"));
		    	product.setMonthLeasePrice(rs.getDouble("month_lease_price"));
		    	product.setDeposit(rs.getDouble("deposit"));
		    	product.setStock(rs.getInt("stock"));
		    	product.setSalePrice(rs.getDouble("sale_price"));
		    	product.setLimitPrice(rs.getDouble("limit_price"));
		    	product.setLockPrice(rs.getDouble("lock_price"));
		    	product.setRedLines(rs.getDouble("red_lines"));
		    	product.setBlueLines(rs.getDouble("blue_lines"));
		    	//商品分类信息
		    	GoodsClass gc = new GoodsClass();
		    	gc.setId(rs.getString("goods_class_id"));
		    	gc.setName(rs.getString("goodsClassName"));
		    	product.setGoodsClassId(gc.getId());
		    	product.setGoodsClass(gc);
		    	//供应商信息
		    	product.setSupplierId(rs.getInt("supplier_id"));
		    	product.setSupplierName(rs.getString("supplierName"));
		    }
		}catch(Exception e){
			log.error("根据商品id获取商品详细信息时出现异常：", e);
		}finally{
			db.release();
		}
		return product;
	}
	
	/**
	 * 更新商品信息
	 * @param product 商品信息
	 * @throws Exception 
	 */
	public void updateProduct(Product product) throws Exception {
		Connection conn = DbUtil.getConnection();
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement("update product set goods_class_id=?,supplier_id=?,bar_code=?,name=?,lease_price=?,month_lease_price=?,sale_price=?,deposit=?,limit_price=?,lock_price=?,red_lines=?,blue_lines=? where id=?");
			ps.setString(1, (StringUtils.isBlank(product.getGoodsClassId()) ? null : product.getGoodsClassId()));
			ps.setInt(2, product.getSupplierId());
			ps.setString(3, product.getBarCode());
			ps.setString(4, product.getName());
			ps.setDouble(5, product.getLeasePrice());
			ps.setDouble(6, product.getMonthLeasePrice());
			ps.setDouble(7, product.getSalePrice());
			ps.setDouble(8, product.getDeposit());
			ps.setDouble(9, product.getLimitPrice());
			ps.setDouble(10, product.getLockPrice());
			ps.setDouble(11, product.getRedLines());
			ps.setDouble(12, product.getBlueLines());
			ps.setInt(13, product.getId());
			ps.executeUpdate();
		} catch (Exception e) {
			log.error("修改商品信息时出现异常：", e);
			throw e;
		} finally {
			DbUtil.closeConnection(null, ps, conn);
		}
	}
	
	/**
	 * 删除商品信息（假删）
	 * @param id 商品id
	 * @return
	 */
	public boolean deleteProductById(int id) {
		return this.updateXXX("`is_delete`=1", "`id`="+id, "product");
	}

	/**
	 * 获取商品条形码图片路径
	 * @param id 商品id
	 * @return
	 * @throws Exception 
	 */
	public String getBarcodeImgPath(int id) throws Exception {
		//获取商品信息
		Product product = (Product) this.getProductById(id);
		
		//条形码无效
		if(!BarcodeUtil.validateEAN13Barcode(product.getBarCode())) {
			throw new Exception("["+product.getBarCode()+"]商品条形码无效！");
		}
		
		//根据条形码获取条形码图片路径（未生成时返回null）
		String barcodeImgPath = BarcodeUtil.getBarcodeImgPath(product.getBarCode());
		
		//生成条形码图片
		if(StringUtils.isBlank(barcodeImgPath)) {
			barcodeImgPath = BarcodeUtil.createEAN13BarcodeImg(product.getBarCode());
		}
		
		return barcodeImgPath;
	}
	
	/**
	 * 从Excel导入商品
	 * @param excel 商品文件
	 * @throws Exception 
	 */
	public void importProduct(File excel) throws Exception {
		Workbook book = null;
		try {
			// 获得第一个工作表对象
			book = Workbook.getWorkbook(excel);
			Sheet sheet = book.getSheet(0);

			//获取商品信息
			List<Product> productList = new ArrayList<Product>();
			Product product;
			Cell[] row;
			for (int i=1; i<sheet.getRows(); i++) {
				row = sheet.getRow(i);
				if(row.length < 7) {
					continue;
				}
				product = new Product();
				String productName = row[0].getContents();
				if(StringUtils.isBlank(productName)) {
					continue;
				}
				product.setName(productName);
				//商品分类
				GoodsClass gc = new GoodsClass();
				gc.setName(row[1].getContents());
				product.setGoodsClass(gc);
				//供应商
				product.setSupplierName(row[2].getContents());
				product.setSalePrice(Double.parseDouble(row[3].getContents()));
				product.setLeasePrice(Double.parseDouble(row[4].getContents()));
				product.setMonthLeasePrice(Double.parseDouble(row[5].getContents()));
				product.setDeposit(Double.parseDouble(row[6].getContents()));
				productList.add(product);
			}
			
			//商品列表为空或商品导入文件格式错误
			if(productList.isEmpty()) {
				throw new Exception("商品列表为空或商品导入文件格式错误");
			}
			
			//解析商品导入文件中的商品分类信息和供应商信息
			this.parseGoodsClassSupplier(productList);
			
			//生成条形码
			long baseCode = System.currentTimeMillis()/10;
			for (Product p : productList) {
				p.setBarCode(BarcodeUtil.createEAN13Barcode(""+baseCode++));
			}
			
			//批量保存商品
			this.batchSaveProduct(productList);
			
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
	 * 解析商品导入文件中的商品分类信息和供应商信息<br/>
	 * 根据商品分类名称和供应商名称获取商品分类id和供应商id
	 * @param productList 商品列表
	 */
	private void parseGoodsClassSupplier(List<Product> productList) {
		Connection conn = DbUtil.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			// 根据商品分类名称查找商品
			StringBuilder sql = new StringBuilder();
			sql.append("select gc.id,gc.`name` from goods_class gc where gc.`name` in(");
			for (int i=0; i<productList.size(); i++) {
				sql.append("?,");
			}
			sql.deleteCharAt(sql.length() - 1);
			sql.append(")");
			ps = conn.prepareStatement(sql.toString());
			int index = 1;
			for (Product product : productList) {
				ps.setString(index++, product.getGoodsClass().getName());
			}
			rs = ps.executeQuery();
			while (rs.next()) {
				String gcId = rs.getString("id");
				String gcName = rs.getString("name");
				for (Product product : productList) {
					if (gcName.equals(product.getGoodsClass().getName())) {
						product.getGoodsClass().setId(gcId);
						product.setGoodsClassId(gcId);
					}
				}
			}

			// 根据供应商名称查找供应商
			sql = new StringBuilder();
			sql.append("select id, `name` from supplier where `name` in(");
			for (int i=0; i<productList.size(); i++) {
				sql.append("?,");
			}
			sql.deleteCharAt(sql.length() - 1);
			sql.append(")");
			ps = conn.prepareStatement(sql.toString());
			index = 1;
			for (Product product : productList) {
				ps.setString(index++, product.getSupplierName());
			}
			rs = ps.executeQuery();
			while (rs.next()) {
				int supplierId = rs.getInt("id");
				String supplierName = rs.getString("name");
				for (Product product : productList) {
					if (supplierName.equals(product.getSupplierName())) {
						product.setSupplierId(supplierId);
					}
				}
			}
		} catch (Exception e) {
			log.error("解析商品导入文件中的商品分类信息和供应商信息时出现异常：", e);
		} finally {
			DbUtil.closeConnection(rs, ps, conn);
		}
	}
	
	/**
	 * 批量保存商品
	 * @param productList 商品列表
	 * @return
	 */
	public boolean batchSaveProduct(List<Product> productList) {
		boolean success = false;
		Connection conn = DbUtil.getConnection();
		PreparedStatement ps = null;
		boolean oldAutoCommit = true;
		try {
			//记录原来事务提交的方式
			oldAutoCommit = conn.getAutoCommit();
			//关闭自动提交事务
			conn.setAutoCommit(false);
			
			//保存商品
			ps = conn.prepareStatement("insert product(goods_class_id,bar_code,name,lease_price,month_lease_price,sale_price,deposit,supplier_id,stock) values(?,?,?,?,?,?,?,?,?)");
			for(Product product : productList) {
				ps.setString(1, (StringUtils.isBlank(product.getGoodsClassId()) ? null : product.getGoodsClassId()));
				ps.setString(2, product.getBarCode());
				ps.setString(3, product.getName());
				ps.setDouble(4, product.getLeasePrice());
				ps.setDouble(5, product.getMonthLeasePrice());
				ps.setDouble(6, product.getSalePrice());
				ps.setDouble(7, product.getDeposit());
				ps.setInt(8, product.getSupplierId());
				ps.setInt(9, product.getStock());
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
			log.error("批量保存商品时出现异常：", e);
		} finally {
			DbUtil.closeConnection(null, ps, conn, oldAutoCommit);
		}
		return success;
	}

	/**
	 * 导出Excel商品列表
	 */
	public byte[] exportExcel() {
		byte[] data = null;
		
		//获取所有商品信息
		Page<Product> page = new Page<Product>(1, Integer.MAX_VALUE);
		page = this.getProductList(page, new HashMap<String, Object>());
		
		WritableWorkbook book = null;
		ByteArrayOutputStream baos = null;
		try {
			//把工作薄保存到内存中
			baos = new ByteArrayOutputStream();
			book = Workbook.createWorkbook(baos);
			WritableSheet sheet = book.createSheet("商品列表信息", 0);
			
			//设置列宽
			sheet.setColumnView(0, 16);
			sheet.setColumnView(1, 35);
			sheet.setColumnView(2, 15);
			sheet.setColumnView(3, 20);
			sheet.setColumnView(4, 15);
			sheet.setColumnView(5, 15);
			sheet.setColumnView(6, 15);
			sheet.setColumnView(7, 10);
			sheet.setColumnView(8, 10);
			
			//商品列表信息
			WritableCellFormat format = new WritableCellFormat();
			WritableFont font = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD); 
			format = new WritableCellFormat(font);
			format.setAlignment(Alignment.CENTRE);
			sheet.addCell(new Label(0, 0, "条形码", format));
			sheet.addCell(new Label(1, 0, "商品名称", format));
			sheet.addCell(new Label(2, 0, "商品分类", format));
			sheet.addCell(new Label(3, 0, "供应商", format));
			sheet.addCell(new Label(4, 0, "销售价格", format));
			sheet.addCell(new Label(5, 0, "日租赁价格", format));
			sheet.addCell(new Label(6, 0, "包月租赁价格", format));
			sheet.addCell(new Label(7, 0, "押金", format));
			sheet.addCell(new Label(8, 0, "库存", format));
			format = new WritableCellFormat();
			format.setAlignment(Alignment.CENTRE);
			for(int i=0; i<page.getList().size(); i++) {
				Product product = page.getList().get(i);
				sheet.addCell(new Label(0, 1+i, product.getBarCode(), format));
				sheet.addCell(new Label(1, 1+i, product.getName()));
				sheet.addCell(new Label(2, 1+i, product.getGoodsClass().getName()));
				sheet.addCell(new Label(3, 1+i, product.getSupplierName()));
				sheet.addCell(new jxl.write.Number(4, 1+i, product.getSalePrice()));
				sheet.addCell(new jxl.write.Number(5, 1+i, product.getLeasePrice()));
				sheet.addCell(new jxl.write.Number(6, 1+i, product.getMonthLeasePrice()));
				sheet.addCell(new jxl.write.Number(7, 1+i, product.getDeposit()));
				sheet.addCell(new jxl.write.Number(8, 1+i, product.getStock()));
			}
			
			// 写入数据并关闭文件
			book.write();
			book.close();
			
			//转化为字节数组
			data = baos.toByteArray();
		} catch (Exception e) {
			log.error("导出Excel商品列表时出现异常：", e);
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
	 * 验证条形码是否有效和是否被占用
	 * @param barCode 条形码
	 * @return true:表示可以使用
	 */
	public String validateBarcode(String barCode) {
		String result = "true";
		//条形码无效
		if(!BarcodeUtil.validateEAN13Barcode(barCode)) {
			result = "商品条形码无效！";
		}
		//条形码被占用
		else {
			Product product = this.getProductByBarcode(barCode);
			if(product != null) {
				result = "条形码被占用！";
			}
		}
		return result;
	}
	
}
