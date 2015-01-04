package mmb.poscenter.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import mmb.poscenter.action.Page;
import mmb.poscenter.domain.AdjustPriceBill;
import mmb.poscenter.domain.Product;
import mmb.poscenter.domain.Shop;
import mmboa.util.db.BaseService;
import mmboa.util.db.DbUtil;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class AdjustPriceBillService extends BaseService {
	
	private static Logger log = Logger.getLogger(AdjustPriceBillService.class);
	
	/**
	 * 分页获取调价单列表信息
	 * @param page 分页信息
	 * @param param 查询条件[shopCode:店面编号；shopName:店面名称；billNumber:调价单号；productName:商品名称；auditStatus:审核状态]
	 * @return
	 */
	public Page<AdjustPriceBill> getAdjustPriceBillPage(Page<AdjustPriceBill> page, Map<String, Object> param) {
		Connection conn = DbUtil.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			// 查询条件
			String shopCode = (String) param.get("shopCode");
			String shopName = (String) param.get("shopName");
			String billNumber = (String) param.get("billNumber");
			String productName = (String) param.get("productName");
			int auditStatus = (Integer) param.get("auditStatus");
			StringBuffer condSql = new StringBuffer();
			if (StringUtils.isNotBlank(shopCode)) {
				condSql.append(" and s.`code` like ? ");
			}
			if (StringUtils.isNotBlank(shopName)) {
				condSql.append(" and s.`name` like ? ");
			}
			if (StringUtils.isNotBlank(billNumber)) {
				condSql.append(" and b.`bill_number` like ? ");
			}
			if (StringUtils.isNotBlank(productName)) {
				condSql.append(" and p.`name` like ? ");
			}
			if (auditStatus != 0) {
				condSql.append(" and b.audit_status = ? ");
			}

			// 查询总记录数
			int index = 1;
			ps = conn.prepareStatement("select count(b.id) from adjust_price_bill b join product p on b.product_id=p.id join shop s on b.shop_id=s.id where 1=1 " + condSql);
			if (StringUtils.isNotBlank(shopCode)) {
				ps.setString(index++, "%" + shopCode + "%");
			}
			if (StringUtils.isNotBlank(shopName)) {
				ps.setString(index++, "%" + shopName + "%");
			}
			if (StringUtils.isNotBlank(billNumber)) {
				ps.setString(index++, "%" + billNumber + "%");
			}
			if (StringUtils.isNotBlank(productName)) {
				ps.setString(index++, "%" + productName + "%");
			}
			if (auditStatus != 0) {
				ps.setInt(index++, auditStatus);
			}
			rs = ps.executeQuery();
			if (rs.next()) {
				page.setTotalRecords(rs.getInt(1));
			}

			// 查询列表数据
			if (page.getTotalRecords() > 0) {
				StringBuilder sql = new StringBuilder(50);
				sql.append("select b.id,b.shop_id,b.product_id,b.bill_number,b.target_price,b.audit_status,b.use_status,b.create_time,p.name,p.bar_code,p.sale_price,p.limit_price,p.lock_price,s.name shopName,s.code shopCode");
				sql.append(" from adjust_price_bill b join product p on b.product_id=p.id");
				sql.append(" join shop s on b.shop_id=s.id");
				sql.append(" where 1=1 ").append(condSql);
				sql.append(" order by b.id desc limit ");
				sql.append(page.getFirstResult()).append(",").append(page.getPageCount());
				ps = conn.prepareStatement(sql.toString());
				index = 1;
				if (StringUtils.isNotBlank(shopCode)) {
					ps.setString(index++, "%" + shopCode + "%");
				}
				if (StringUtils.isNotBlank(shopName)) {
					ps.setString(index++, "%" + shopName + "%");
				}
				if (StringUtils.isNotBlank(billNumber)) {
					ps.setString(index++, "%" + billNumber + "%");
				}
				if (StringUtils.isNotBlank(productName)) {
					ps.setString(index++, "%" + productName + "%");
				}
				if (auditStatus != 0) {
					ps.setInt(index++, auditStatus);
				}
				rs = ps.executeQuery();
				List<AdjustPriceBill> list = new ArrayList<AdjustPriceBill>();
				AdjustPriceBill bill;
				while (rs.next()) {
					bill = new AdjustPriceBill();
					bill.setId(rs.getInt("id"));
					bill.setBillNumber(rs.getString("bill_number"));
					bill.setTargetPrice(rs.getDouble("target_price"));
					bill.setAuditStatus(rs.getInt("audit_status"));
					bill.setUseStatus(rs.getInt("use_status"));
					bill.setCreateTime(rs.getTimestamp("create_time"));
					Product p = new Product();
					p.setId(rs.getInt("product_id"));
					p.setName(rs.getString("name"));
					p.setBarCode(rs.getString("bar_code"));
					p.setSalePrice(rs.getDouble("sale_price"));
					p.setLimitPrice(rs.getDouble("limit_price"));
			    	p.setLockPrice(rs.getDouble("lock_price"));
					bill.setProductId(p.getId());
					bill.setProduct(p);
					Shop shop = new Shop();
					shop.setId(rs.getInt("shop_id"));
					shop.setCode(rs.getString("shopCode"));
					shop.setName(rs.getString("shopName"));
					bill.setShopId(shop.getId());
					bill.setShop(shop);
					list.add(bill);
				}
				page.setList(list);
			}
		} catch (Exception e) {
			log.error("分页获取调价单列表信息时出现异常：", e);
		} finally {
			DbUtil.closeConnection(rs, ps, conn);
		}
		return page;
	}

	/**
	 * 根据调价单id获取调价单对象信息
	 * @param id 调价单id
	 * @return
	 */
	public AdjustPriceBill getAdjustPriceBillById(int id) {
		return (AdjustPriceBill) this.getXXX("`id`="+id, "adjust_price_bill", AdjustPriceBill.class.getName());
	}
	
	/**
	 * 保存调价单信息
	 * @param adjustPriceBill 调价单信息
	 * @throws Exception 
	 */
	public void saveAdjustPriceBill(AdjustPriceBill adjustPriceBill) throws Exception {
		Connection conn = DbUtil.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			//保存
			ps = conn.prepareStatement("insert adjust_price_bill(shop_id,id,product_id,bill_number,target_price,use_status,audit_status,create_time) values(?,?,?,?,?,?,?,?)");
			ps.setInt(1, adjustPriceBill.getShopId());
			ps.setInt(2, adjustPriceBill.getId());
			ps.setInt(3, adjustPriceBill.getProductId());
			ps.setString(4, adjustPriceBill.getBillNumber());
			ps.setDouble(5, adjustPriceBill.getTargetPrice());
			ps.setInt(6, adjustPriceBill.getUseStatus());
			ps.setInt(7, adjustPriceBill.getAuditStatus());
			ps.setTimestamp(8, adjustPriceBill.getCreateTime());
			ps.executeUpdate();
		} catch (Exception e) {
			log.error("保存调价单信息时出现异常：", e);
			throw e;
		} finally {
			DbUtil.closeConnection(rs, ps, conn);
		}
	}
	
	/**
	 * 店面提交调价单
	 * @param json
	 * @return
	 */
	public String submitAdjustPriceBill(String json) {
		String resutl = "true";
		try {
			//解析数据
			Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
			AdjustPriceBill bill = gson.fromJson(json, AdjustPriceBill.class);
			
			//根据店面编号获取店面信息
			Shop shop = new ShopService().getShopByCode(bill.getShopCode());
			
			//店面不存在
			if(shop == null) {
				throw new Exception("店面编号不存在");
			}
			
			//设置店面id
			bill.setShopId(shop.getId());
			
			//保存调价单
			this.saveAdjustPriceBill(bill);
		} catch (Exception e) {
			resutl = e.toString();
			log.error("店面提交调价单时出现异常：", e);
		}
		return resutl;
	}
	
	/**
	 * 店面同步调价单的审核状态
	 * @param json
	 * @return
	 */
	public String syncAdjustPriceBill(String json) {
		String result = "true";
		Connection conn = DbUtil.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			//解析数据
			Gson gson = new Gson();
			List<AdjustPriceBill> billList = gson.fromJson(json, new TypeToken<List<AdjustPriceBill>>(){}.getType());
			
			//获取调价单的审核状态
			StringBuilder sql = new StringBuilder();
			sql.append("select b.id,b.audit_status from adjust_price_bill b join shop s on b.shop_id=s.id and s.code=? where b.audit_status!=1 and b.id in(");
			for(AdjustPriceBill bill : billList) {
				sql.append(bill.getId()).append(",");
			}
			sql.deleteCharAt(sql.length()-1).append(")");
			ps = conn.prepareStatement(sql.toString());
			ps.setString(1, billList.get(0).getShopCode());
			rs = ps.executeQuery();
			billList = new ArrayList<AdjustPriceBill>();
			AdjustPriceBill bill = null;
		    while(rs.next()) {
		    	bill = new AdjustPriceBill();
				bill.setId(rs.getInt("id"));
				bill.setAuditStatus(rs.getInt("audit_status"));
				billList.add(bill);
		    }
			
			//返回数据
		    result = gson.toJson(billList);
		} catch (Exception e) {
			result = e.toString();
			log.error("店面同步调价单的审核状态时出现异常：", e);
		} finally {
			DbUtil.closeConnection(rs, ps, conn);
		}
		return result;
	}

	/**
	 * 审核调价单
	 * @param adjustPriceBill 调价单信息
	 * @throws Exception 
	 */
	public void auditAdjustPriceBill(AdjustPriceBill adjustPriceBill) throws Exception {
		Connection conn = DbUtil.getConnection();
		PreparedStatement ps = null;
		try {
			//修改审核状态
			ps = conn.prepareStatement("update adjust_price_bill set audit_status=? where shop_id=? and id=?");
			ps.setInt(1, adjustPriceBill.getAuditStatus());
			ps.setInt(2, adjustPriceBill.getShopId());
			ps.setInt(3, adjustPriceBill.getId());
			ps.executeUpdate();
		} catch (Exception e) {
			log.error("审核调价单时出现异常：", e);
			throw e;
		} finally {
			DbUtil.closeConnection(null, ps, conn);
		}
	}

}
