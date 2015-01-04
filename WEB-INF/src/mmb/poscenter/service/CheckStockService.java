package mmb.poscenter.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import mmb.poscenter.action.Page;
import mmb.poscenter.domain.CheckStock;
import mmboa.util.db.BaseService;
import mmboa.util.db.DbUtil;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

public class CheckStockService extends BaseService  {
	
	private static Logger log = Logger.getLogger(CheckStockService.class);
	
	/**
	 * 根据收货单id获取收货单对象信息
	 * @param id 收货单id
	 * @return
	 */
	public CheckStock getCheckStockById(int id) {
		return (CheckStock) this.getXXX("`id`="+id, "check_stock", CheckStock.class.getName());
	}
	
	/**
	 * 分页获取商品盘点信息
	 * @param page 分页信息
	 * @param param 查询参数[date:查询日期]
	 * @return
	 */
	public Page<CheckStock> getCheckStockPage(Page<CheckStock> page, Map<String, Object> param) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			conn = DbUtil.getConnection();
			
			//查询条件
			StringBuilder condSql = new StringBuilder();
			String date = (String)param.get("date");
			if(StringUtils.isNotBlank(date)) {
				condSql.append(" and c.check_time>=STR_TO_DATE(?,'%Y-%m-%d') and c.check_time<ADDDATE(STR_TO_DATE(?,'%Y-%m-%d'), 1)");
			}
			
			//查询总记录数
			ps = conn.prepareStatement(" SELECT COUNT(*) from check_stock c where 1=1" + condSql.toString());
			if(StringUtils.isNotBlank(date)) {
				ps.setString(1, date);
				ps.setString(2, date);
			}
			rs = ps.executeQuery();
			if(rs.next()){
				page.setTotalRecords(rs.getInt(1));
			}
			
			//分页获取商品Id列表
			if(page.getTotalRecords() > 0) {
				List<CheckStock> checkStockList = new ArrayList<CheckStock>();
				StringBuilder productSql = new StringBuilder();
				productSql.append(" SELECT c.id,c.charger,c.check_time,c.remark,c.use_status from check_stock c ");
				productSql.append(" where 1=1 ").append(condSql);
				productSql.append(" order by id desc ");
				productSql.append(" LIMIT ").append(page.getFirstResult()).append(",").append(page.getPageCount());
				ps = conn.prepareStatement(productSql.toString());
				if(StringUtils.isNotBlank(date)) {
					ps.setString(1, date);
					ps.setString(2, date);
				}
				rs = ps.executeQuery();
				while(rs.next()){
					CheckStock checkStock = new CheckStock();
					checkStock.setId(rs.getInt("id"));
					checkStock.setCharger(rs.getString("charger"));
					checkStock.setCheckTime(rs.getTimestamp("check_time"));
					checkStock.setRemark(rs.getString("remark"));
					checkStock.setUseStatus(rs.getInt("use_status"));
					checkStockList.add(checkStock);
				}
				page.setList(checkStockList);
			}
		}catch(Exception e){
			log.error("分页获取商品盘点信息时出现异常：", e);
		}finally{
			DbUtil.closeConnection(rs, ps, conn);
		}
		return page;
	}

	/**
	 * 生成指定日期的空的商品盘点数据
	 * @param date 指定日期(eg.2013-03-01)
	 */
	public void createEmptyCheckStock() {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			//保存收货单信息
			conn = DbUtil.getConnection();
			ps = conn.prepareStatement("insert into check_stock(`check_time`,`use_status`) values (?,?)", Statement.RETURN_GENERATED_KEYS);
			ps.setTimestamp(1, new Timestamp(new Date().getTime()));
			ps.setInt(2, 0);
			ps.executeUpdate();
			
			//获取订单id
			int orderId = 0;
			rs = ps.getGeneratedKeys();
			if(rs.next()) {
				orderId = rs.getInt(1);
			}
			
			String sql = " INSERT INTO check_stock_item (check_stock_id,product_id,count,stock,check_time) SELECT ?,p.id,-1,p.stock,? FROM product p where p.is_delete=0";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, orderId);
			ps.setTimestamp(2, new Timestamp(new Date().getTime()));
			ps.executeUpdate();
			
		}catch(Exception e){
			log.error("生成指定日期的空的商品盘点数据时出现异常：", e);
		}finally{
			DbUtil.closeConnection(rs, ps, conn);
		}
	}
	
	/**
	 * 修改盘点信息
	 * @param checkStock 盘点信息对象
	 * @return
	 */
	public boolean updateCheckStock(CheckStock checkStock) {
		//更新语句
		StringBuilder set = new StringBuilder();
		set.append("remark='").append(checkStock.getRemark()).append("', ");
		set.append("charger='").append(checkStock.getCharger()).append("', ");
		set.append("check_time='").append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())).append("'");
		return this.updateXXX(set.toString(), "`id`="+checkStock.getId(), "check_stock");
	}

}
