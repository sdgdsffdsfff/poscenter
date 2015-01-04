package mmb.poscenter.service;

import java.io.IOException;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.google.gson.stream.JsonReader;

import mmb.poscenter.action.Page;
import mmb.poscenter.domain.PriceCard;
import mmboa.util.db.BaseService;
import mmboa.util.db.DbUtil;

public class PriceCardService extends BaseService {
	
	private static Logger log = Logger.getLogger(PriceCardService.class);
	/**
	 * 获取红蓝卡列表信息
	 * @param page 分页信息
	 * @return
	 */
	public Page<PriceCard> getPriceCardList(Page<PriceCard> page,Map<String,Object> param){
		Connection conn = DbUtil.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			int state = (Integer)param.get("state");
			String priceCardId = (String)param.get("pricecardid");
			int type = (Integer)param.get("type");
			String clerkName = (String)param.get("clerkname");
			
			StringBuilder sb = new StringBuilder(100);
			sb.append("select count(1) from price_card pc where 1 = 1 ");
		
			if(state != 0 ){
				sb.append(" and pc.state = ? ");
			}
			if(type != 0 ){
				sb.append(" and pc.type = ? ");
			}
			if(StringUtils.isNotBlank(priceCardId)){
				sb.append(" and pc.id  =  ? ");
			}
			if(StringUtils.isNotBlank(clerkName)){
				sb.append(" and pc.clerk_name like  ? ");
			}
			
			ps = conn.prepareStatement(sb.toString());
			int index = 1 ;
			if(state != 0 ){
				ps.setInt(index++, state);
			}
			if(type != 0 ){
				ps.setInt(index++, type);
			}
			if(StringUtils.isNotBlank(priceCardId)){
				ps.setString(index++, priceCardId);
			}
			if(StringUtils.isNotBlank(clerkName)){
				ps.setString(index++, "%"+clerkName+"%");
			}
			
			rs = ps.executeQuery();
			if(rs.next()){
			    	page.setTotalRecords(rs.getInt(1));
			}
			//如果没有记录，则直接返回
			if(page.getTotalRecords() <= 0) {
				return page;
			}
			
			List<PriceCard>  list = new ArrayList<PriceCard>();
			PriceCard priceCard = null;
			sb.delete(0, sb.length()-1);
			
			sb.append("select pc.id,pc.type,pc.point,pc.clerk_name,pc.open_time,pc.shop_id,pc.state,pc.supplier_id,pc.type,s.`name` shopName,sup.`name` supplierName FROM price_card pc left JOIN shop s ON pc.shop_id = s.id left JOIN supplier sup on pc.supplier_id = sup.id where 1 = 1 ");
			if(state != 0 ){
				sb.append(" and pc.state = ? ");
			}
			if(type != 0 ){
				sb.append(" and pc.type = ? ");
			}
			if(StringUtils.isNotBlank(priceCardId)){
				sb.append(" and pc.id  =  ? ");
			}
			if(StringUtils.isNotBlank(clerkName)){
				sb.append(" and pc.clerk_name like  ? ");
			}
			sb.append("order by id desc limit ");
			sb.append(page.getFirstResult()).append(",").append(page.getPageCount());
			ps = conn.prepareStatement(sb.toString());
			index = 1;
			if(state != 0 ){
				ps.setInt(index++, state);
			}
			if(type != 0 ){
				ps.setInt(index++, type);
			}
			if(StringUtils.isNotBlank(priceCardId)){
				ps.setString(index++, priceCardId);
			}     
			if(StringUtils.isNotBlank(clerkName)){
				ps.setString(index++, "%"+clerkName+"%");
			}
			
			rs = ps.executeQuery();
			while(rs.next()){
				priceCard = new PriceCard();
				priceCard.setId(rs.getString("id"));
				priceCard.setClerkName(rs.getString("clerk_name"));
				priceCard.setOpenTime(rs.getTimestamp("open_time"));
				priceCard.setShopId(rs.getInt("shop_id"));
				priceCard.setShopName(rs.getString("shopName"));
				priceCard.setState(rs.getInt("state"));
				priceCard.setSupplierId(rs.getInt("supplier_id"));
				priceCard.setSupplierName(rs.getString("supplierName"));
				priceCard.setType(rs.getInt("type"));
				priceCard.setPoint(rs.getInt("point"));
			    list.add(priceCard);
			}
			page.setList(list);
		}catch(Exception e){
			log.error("获取红蓝卡分页信息出错！", e);
		}finally{
			DbUtil.closeConnection(rs, ps, conn);
		}
		return page;
  }

	public void addPriceCard(PriceCard priceCard){
		this.addXXX(priceCard, "price_card");
	}
	
	public PriceCard getPriceCardById(String priceCardId){
		Connection conn = DbUtil.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		PriceCard priceCard = null ;
		try{
			ps = conn.prepareStatement("select pc.id,pc.type,pc.point,pc.clerk_name,pc.open_time,pc.shop_id,pc.state,pc.supplier_id,pc.type,s.`name` shopName,sup.`name` supplierName FROM price_card pc left JOIN shop s ON pc.shop_id = s.id left JOIN supplier sup on pc.supplier_id = sup.id where pc.id = ?");
			ps.setString(1, priceCardId);
			rs = ps.executeQuery();
			if(rs.next()){
				priceCard = new PriceCard();
				priceCard.setId(rs.getString("id"));
				priceCard.setClerkName(rs.getString("clerk_name"));
				priceCard.setOpenTime(rs.getTimestamp("open_time"));
				priceCard.setShopId(rs.getInt("shop_id"));
				priceCard.setShopName(rs.getString("shopName"));
				priceCard.setState(rs.getInt("state"));
				priceCard.setSupplierId(rs.getInt("supplier_id"));
				priceCard.setSupplierName(rs.getString("supplierName"));
				priceCard.setType(rs.getInt("type"));
				priceCard.setPoint(rs.getInt("point"));
			}
			
		}catch(Exception e){
			log.error("获取红蓝卡信息出错！", e);
		}finally{
			DbUtil.closeConnection(rs, ps, conn);
		}
		return priceCard;
	}
	
	//修改红蓝卡数据
	public void updatePriceCard(PriceCard priceCard){
		Connection conn = DbUtil.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			if(StringUtils.isNotBlank(priceCard.getPassword())){
				ps = conn.prepareStatement(" update price_card pc set pc.clerk_name = ?  , pc.`password` = ? , pc.point = ? , pc.shop_id = ? , pc.state = ? , pc.supplier_id = ? where pc.id = ? and pc.type = ?   ");
				ps.setString(1, priceCard.getClerkName());
				ps.setString(2, priceCard.getPassword());
				ps.setDouble(3, priceCard.getPoint());
				ps.setInt(4, priceCard.getShopId());
				ps.setInt(5, priceCard.getState());
				ps.setInt(6, priceCard.getSupplierId());
				ps.setString(7, priceCard.getId());
				ps.setInt(8, priceCard.getType());
				ps.executeUpdate();
			}else{
				ps = conn.prepareStatement(" update price_card pc set pc.clerk_name = ?   , pc.point = ? , pc.shop_id = ? , pc.state = ? , pc.supplier_id = ? where pc.id = ? and pc.type = ?   ");
				ps.setString(1, priceCard.getClerkName());
				ps.setDouble(2, priceCard.getPoint());
				ps.setInt(3, priceCard.getShopId());
				ps.setInt(4, priceCard.getState());
				ps.setInt(5, priceCard.getSupplierId());
				ps.setString(6, priceCard.getId());
				ps.setInt(7, priceCard.getType());
				ps.executeUpdate();
			}
		    
		}catch(Exception e){
			log.error("更新红蓝卡id信息出错！", e);
		}finally{
			DbUtil.closeConnection(rs, ps, conn);
		}

	}

	//开卡
	public void openPriceCard(PriceCard priceCard){
		Connection conn = DbUtil.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			ps = conn.prepareStatement(" update price_card pc set pc.clerk_name = ? , pc.open_time = ? , pc.`password` = ? , pc.point = ? , pc.shop_id = ? , pc.state = ? , pc.supplier_id = ? where pc.id = ? and pc.type = ?   ");
			ps.setString(1, priceCard.getClerkName());
			ps.setTimestamp(2, new Timestamp(new Date().getTime()));
			ps.setString(3, priceCard.getPassword());
			ps.setDouble(4, priceCard.getPoint());
			ps.setInt(5, priceCard.getShopId());
			//状态为改为 有用卡
			ps.setInt(6, 2);
			ps.setInt(7,priceCard.getSupplierId());
			ps.setString(8, priceCard.getId());
			ps.setInt(9, priceCard.getType());
		    ps.executeUpdate();
		    
		}catch(Exception e){
			log.error("更新红蓝卡id信息出错！", e);
		}finally{
			DbUtil.closeConnection(rs, ps, conn);
		}

	}
	
	//检查有无重复的卡号信息
	public boolean checkExistId(String priceCardId,int type){
		Connection conn = DbUtil.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean exist = false;
		try{
			ps = conn.prepareStatement("select count(1) from price_card pc where pc.id = ?  ");
			ps.setString(1, priceCardId);
	
			rs = ps.executeQuery();
			if(rs.next()){
				if(rs.getInt(1) > 0){
					exist = true;
				}
			}
		}catch(Exception e){
			log.error("检查是否存在红蓝卡id信息出错！", e);
		}finally{
			DbUtil.closeConnection(rs, ps, conn);
		}
		return exist;
	}
	
    /**
     * 同步红蓝卡信息
     * @param json
     * @return
     */
	public String syncPriceCardInfo(String json) {
		StringBuilder responseJson = new StringBuilder();
		try {
			JsonReader jr = new JsonReader(new StringReader(json));
			String shopCode = "";
			jr.beginObject();
			while(jr.hasNext()){
				if("shopCode".equals(jr.nextName())){
					shopCode = jr.nextString();
				}
			}
			jr.endObject();
			ShopService ss =new ShopService();
			int shopId = ss.getShopByCode(shopCode).getId();
			List<PriceCard> priceCardList = this.getShopPriceCard(shopId);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			responseJson.append("{\"priceCardList\":[");
			for(PriceCard pc : priceCardList){
				responseJson.append("{");
				responseJson.append("\"id\":\"").append(pc.getId()).append("\",");
				responseJson.append("\"clerkName\":\"").append(pc.getClerkName()).append("\",");
				responseJson.append("\"password\":\"").append(pc.getPassword()).append("\",");
				if(pc.getOpenTime() == null){
					
				}else{
					responseJson.append("\"openTime\":\"").append(pc.getOpenTime() == null ? "" :sdf.format(pc.getOpenTime())).append("\",");	
				}
				responseJson.append("\"point\":").append(pc.getPoint()).append(",");
				responseJson.append("\"type\":").append(pc.getType()).append(",");
				responseJson.append("\"state\":").append(pc.getState()).append(",");
				responseJson.append("\"supplierId\":").append(pc.getSupplierId());
				responseJson.append("},");
			}
			responseJson.deleteCharAt(responseJson.length()-1);
			responseJson.append("],\"priceCardChargeList\":[");
			Connection conn = DbUtil.getConnection();
			PreparedStatement ps = null;
			ResultSet rs = null;
			try{
				ps = conn.prepareStatement(" SELECT pc.point totalCash,tru.point consumeCash,pc.type cardType,pc.id from temp_recharge_update tru LEFT JOIN price_card pc on pc.id = tru.price_card_id where tru.shop_id = ? ");
				ps.setInt(1, shopId);
				rs = ps.executeQuery();
				while(rs.next()){
					responseJson.append("{");
					responseJson.append("\"priceCardId\":\"").append(rs.getString("id")).append("\",");
					responseJson.append("\"cardType\":").append(rs.getInt("cardType")).append(",");
					responseJson.append("\"totalCash\":").append(rs.getDouble("totalCash")).append(",");
					responseJson.append("\"consumeCash\":").append(rs.getDouble("consumeCash"));
					if(!rs.isLast()){
						responseJson.append("},");
					}else{
						responseJson.append("}");
					}
				}
				responseJson.append("]}");
			}catch(Exception e){
				log.error("获取临时表数据出错！", e);
			}finally{
				DbUtil.closeConnection(rs, ps, conn);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return responseJson.toString();
	}

	public List<PriceCard> getShopPriceCard(int  shopId){
		List<PriceCard> temp = new ArrayList<PriceCard>();
		Connection conn = DbUtil.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;;
		try{
			ps = conn.prepareStatement(" select pc.id,pc.clerk_name,pc.open_time,pc.type,pc.state,pc.supplier_id,pc.password,pc.point from price_card pc where pc.shop_id = ? and pc.state != 1 ");
			ps.setInt(1, shopId);
			rs = ps.executeQuery();
			PriceCard pc = null;
			while(rs.next()){
				pc = new PriceCard();
				pc.setId(rs.getString("id"));
				pc.setClerkName(rs.getString("clerk_name"));
				pc.setOpenTime(rs.getTimestamp("open_time"));
				pc.setType(rs.getInt("type"));
				pc.setState(rs.getInt("state"));
				pc.setSupplierId(rs.getInt("supplier_id"));
				pc.setPassword(rs.getString("password"));
				pc.setPoint(rs.getDouble("point"));
				temp.add(pc);
			}
		}catch(Exception e){
			log.error("获取相应店面红蓝卡信息异常",e);
		}finally{
			DbUtil.closeConnection(rs, ps, conn);
		}
		return temp;
	}
	
	
	
	/**
	 * 删除临时表的一些信息
	 * @param shopId
	 */
	public boolean deleteTempPriceCardAndUpdatePoint(String shopCode,Map<String,Double> points){
		boolean success = false;
		Connection conn = DbUtil.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean autoCommit = false;
		try{
			autoCommit = conn.getAutoCommit();
			conn.setAutoCommit(false);
			ShopService ss =new ShopService();
			int shopId = ss.getShopByCode(shopCode).getId();
			ps = conn.prepareStatement("delete from temp_recharge_update where shop_id = ? ");
			ps.setInt(1, shopId);
			ps.executeUpdate();
			
			ps = conn.prepareStatement("update price_card pc set pc.point = ? where pc.id = ? ");
			for(String id : points.keySet()){
				ps.setDouble(1, points.get(id));
				ps.setString(2, id);
				ps.addBatch();
			}
			ps.executeBatch();
			
			success = true;
		}catch(Exception e){
			log.error("删除店面临时表数据出错", e);
		}finally{
			DbUtil.closeConnection(rs, ps, conn,autoCommit);
		}
		return success;
	}
	
}
