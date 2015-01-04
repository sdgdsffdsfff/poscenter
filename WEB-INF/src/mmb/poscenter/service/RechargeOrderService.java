package mmb.poscenter.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import mmb.poscenter.action.Page;
import mmb.poscenter.domain.PriceCard;
import mmb.poscenter.domain.RechargeOrder;
import mmboa.util.db.BaseService;
import mmboa.util.db.DbOperation;
import mmboa.util.db.DbUtil;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

public class RechargeOrderService extends BaseService {
	
	private static Logger log = Logger.getLogger(RechargeOrderService.class);
	
	/**
	 * 分页获取充值单列表信息
	 * @param page 分页信息
	 * @param param 查询条件[priceCardId:调价卡号；priceCardType:调价卡类型；auditStatus:审核状态；useStatus:使用状态]
	 * @return
	 */
	public Page<RechargeOrder> getRechargeOrderPage(Page<RechargeOrder> page, Map<String, Object> param) {
		Connection conn = DbUtil.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			// 查询条件
			String priceCardId = (String) param.get("priceCardId");
			int priceCardType = (Integer) param.get("priceCardType");
			int auditStatus = (Integer) param.get("auditStatus");
			int useStatus = (Integer) param.get("useStatus");
			StringBuffer condSql = new StringBuffer();
			if (StringUtils.isNotBlank(priceCardId)) {
				condSql.append(" and c.`id` like ? ");
			}
			if (priceCardType != 0) {
				condSql.append(" and c.type = ?");
			}
			if (auditStatus != 0) {
				condSql.append(" and o.audit_status = ? ");
			}
			if (useStatus != 0) {
				condSql.append(" and o.use_status = ? ");
			}

			// 查询总记录数
			int index = 1;
			ps = conn.prepareStatement("select count(o.id) from recharge_order o join price_card c on o.price_card_id=c.id where 1=1 " + condSql);
			if (StringUtils.isNotBlank(priceCardId)) {
				ps.setString(index++, "%" + priceCardId + "%");
			}
			if (priceCardType != 0) {
				ps.setInt(index++, priceCardType);
			}
			if (auditStatus != 0) {
				ps.setInt(index++, auditStatus);
			}
			if (useStatus != 0) {
				ps.setInt(index++, useStatus);
			}
			rs = ps.executeQuery();
			if (rs.next()) {
				page.setTotalRecords(rs.getInt(1));
			}

			// 查询列表数据
			if (page.getTotalRecords() > 0) {
				StringBuilder sql = new StringBuilder(50);
				sql.append("select c.shop_id,s.name shopName,c.clerk_name,c.type,c.point totalPoint,o.id,o.price_card_id,o.point,o.audit_status,o.use_status,o.create_time");
				sql.append(" from recharge_order o join price_card c on o.price_card_id=c.id");
				sql.append(" left join shop s on c.shop_id=s.id");
				sql.append(" where 1=1 ").append(condSql);
				sql.append(" order by o.id desc limit ");
				sql.append(page.getFirstResult()).append(",").append(page.getPageCount());
				ps = conn.prepareStatement(sql.toString());
				index = 1;
				if (StringUtils.isNotBlank(priceCardId)) {
					ps.setString(index++, "%" + priceCardId + "%");
				}
				if (priceCardType != 0) {
					ps.setInt(index++, priceCardType);
				}
				if (auditStatus != 0) {
					ps.setInt(index++, auditStatus);
				}
				if (useStatus != 0) {
					ps.setInt(index++, useStatus);
				}
				rs = ps.executeQuery();
				List<RechargeOrder> list = new ArrayList<RechargeOrder>();
				RechargeOrder p;
				while (rs.next()) {
					p = new RechargeOrder();
					p.setId(rs.getInt("id"));
					p.setPoint(rs.getDouble("point"));
					p.setAuditStatus(rs.getInt("audit_status"));
					p.setUseStatus(rs.getInt("use_status"));
					p.setCreateTime(rs.getTimestamp("create_time"));
					PriceCard card = new PriceCard();
					card.setId(rs.getString("price_card_id"));
					card.setClerkName(rs.getString("clerk_name"));
					card.setType(rs.getInt("type"));
					card.setShopId(rs.getInt("shop_id"));
					card.setShopName(rs.getString("shopName"));
					p.setPriceCardId(card.getId());
					p.setPriceCard(card);
					list.add(p);
				}
				page.setList(list);
			}
		} catch (Exception e) {
			log.error("分页获取充值单列表信息时出现异常：", e);
		} finally {
			DbUtil.closeConnection(rs, ps, conn);
		}
		return page;
	}

	/**
	 * 根据充值单id获取充值单对象信息
	 * @param id 充值单id
	 * @return
	 */
	public RechargeOrder getRechargeOrderById(int id) {
		return (RechargeOrder) this.getXXX("`id`="+id, "recharge_order", RechargeOrder.class.getName());
	}
	
	/**
	 * 根据充值单id获取充值单详细信息（包括调价卡信息）
	 * @param id 充值单id
	 * @return
	 */
	public RechargeOrder getDetailById(int id) {
		DbOperation db = new DbOperation();
		RechargeOrder rechargeOrder = null;
		try {
			StringBuilder sql = new StringBuilder(50);
			sql.append("select c.shop_id,s.name shopName,c.clerk_name,c.type,c.point totalPoint,o.id,o.price_card_id,o.point,o.audit_status,o.use_status,o.create_time");
			sql.append(" from recharge_order o join price_card c on o.price_card_id=c.id");
			sql.append(" left join shop s on c.shop_id=s.id");
			sql.append(" where o.id=").append(id);
			ResultSet rs = db.executeQuery(sql.toString());
		    if(rs.next()){
		    	rechargeOrder = new RechargeOrder();
		    	rechargeOrder.setId(rs.getInt("id"));
		    	rechargeOrder.setPoint(rs.getDouble("point"));
		    	rechargeOrder.setAuditStatus(rs.getInt("audit_status"));
		    	rechargeOrder.setUseStatus(rs.getInt("use_status"));
		    	rechargeOrder.setCreateTime(rs.getTimestamp("create_time"));
				PriceCard card = new PriceCard();
				card.setId(rs.getString("price_card_id"));
				card.setClerkName(rs.getString("clerk_name"));
				card.setType(rs.getInt("type"));
				card.setShopId(rs.getInt("shop_id"));
				card.setShopName(rs.getString("shopName"));
				rechargeOrder.setPriceCardId(card.getId());
				rechargeOrder.setPriceCard(card);
		    }
		} catch (Exception e) {
			log.error("根据充值单id获取充值单详细信息时出现异常：", e);
		} finally {
			db.release();
		}
		return rechargeOrder;
	}
	
	/**
	 * 修改充值单信息
	 * @param rechargeOrder 充值单信息
	 * @throws Exception 
	 */
	public void updateRechargeOrder(RechargeOrder rechargeOrder) throws Exception {
		Connection conn = DbUtil.getConnection();
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement("update recharge_order set price_card_id=?,point=?,use_status=?,audit_status=? where id=?");
			ps.setString(1, rechargeOrder.getPriceCardId());
			ps.setDouble(2, rechargeOrder.getPoint());
			ps.setInt(3, rechargeOrder.getUseStatus());
			ps.setInt(4, rechargeOrder.getAuditStatus());
			ps.setInt(5, rechargeOrder.getId());
			ps.executeUpdate();
		} catch (Exception e) {
			log.error("修改充值单信息时出现异常：", e);
			throw e;
		} finally {
			DbUtil.closeConnection(null, ps, conn);
		}
	}
	
	/**
	 * 删除充值单信息
	 * @param id 充值单id
	 * @return
	 */
	public boolean deleteById(int id) {
		return this.deleteXXX("`id`="+id, "recharge_order");
	}

	/**
	 * 审核充值单
	 * @param id 充值单id
	 * @param auditStatus 审核状态
	 * @throws Exception 
	 */
	public void auditRechargeOrder(int id, int auditStatus) throws Exception {
		Connection conn = DbUtil.getConnection();
		PreparedStatement ps = null;
		boolean oldAutoCommit = true;
		try {
			//记录原来事务提交的方式
			oldAutoCommit = conn.getAutoCommit();
			//关闭自动提交事务
			conn.setAutoCommit(false);
			
			//修改审核状态
			ps = conn.prepareStatement("update recharge_order set audit_status=? where id=?");
			ps.setInt(1, auditStatus);
			ps.setInt(2, id);
			ps.executeUpdate();
			
			//审核通过
			if(auditStatus == 2) {
				//为卡充值
				RechargeOrder order = this.getDetailById(id);
				ps = conn.prepareStatement("update price_card set point=point+? where id=?");
				ps.setDouble(1, order.getPoint());
				ps.setString(2, order.getPriceCardId());
				ps.executeUpdate();
				
				//向临时表中插入充值数据，用来店面数据同步
				ps = conn.prepareStatement("insert into temp_recharge_update(price_card_id,point,shop_id) values(?,?,?)");
				ps.setString(1, order.getPriceCardId());
				ps.setDouble(2, order.getPoint());
				ps.setInt(3, order.getPriceCard().getShopId());
				ps.executeUpdate();
			}
			
        	conn.commit();
		} catch (Exception e) {
			try {
				//回滚事务
				conn.rollback();
			} catch (SQLException e1) {}
			log.error("审核充值单时出现异常：", e);
			throw e;
		} finally {
			DbUtil.closeConnection(null, ps, conn, oldAutoCommit);
		}
	}

}
