package mmb.poscenter.service;

import java.io.StringReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.google.gson.stream.JsonReader;

import mmb.poscenter.action.Page;
import mmb.poscenter.domain.Shop;
import mmboa.util.db.BaseService;
import mmboa.util.db.DbOperation;
import mmboa.util.db.DbUtil;

public class ShopService extends BaseService {
	
	private static Logger log = Logger.getLogger(ShopService.class);
	
	/**
	 * 店面列表信息
	 * @param page
	 * @return
	 */
	public Page<Shop> getShopPage(Page<Shop> page){
		DbOperation db = new DbOperation();
		try{
			//查询总记录数
			StringBuilder sb = new StringBuilder(50);
			sb.append("select count(id) from shop s ");
			ResultSet rs = db.executeQuery(sb.toString());
		    if(rs.next()){
		    	page.setTotalRecords(rs.getInt(1));
		    }
		    
		    //查询列表数据
		    if(page.getTotalRecords() > 0) {
		    	List<Shop> list = new ArrayList<Shop>();
		    	Shop s;
		    	StringBuilder sql = new StringBuilder(50);
		    	sql.append("select s.id,s.code,s.name,s.address,s.ip_address,s.charger,s.create_time");
		    	sql.append(" from shop s");
		    	sql.append(" order by s.id desc limit ");
		    	sql.append(page.getFirstResult()).append(",").append(page.getPageCount());
		    	rs = db.executeQuery(sql.toString());
		    	while(rs.next()){
		    		s = new Shop();
		    		s.setId(rs.getInt("id"));
		    		s.setCode(rs.getString("code"));
		    		s.setName(rs.getString("name"));
		    		s.setAddress(rs.getString("address"));
		    		s.setIpAddress(rs.getString("ip_address"));
		    		s.setCharger(rs.getString("charger"));
		    		s.setCreateTime(rs.getTimestamp("create_time"));
		    		list.add(s);
		    	}
		    	page.setList(list);
		    }
		}catch(Exception e){
			log.error("分页获取店面列表数据时出现异常：", e);
		}finally{
			this.release(db);
		}
		return page;
	}

	/**
	 * 更新店面IP地址
	 * @param json
	 * @return
	 */
	public String updateShopIpAddress(String json){
		JsonReader jr = new JsonReader(new StringReader(json));
		Connection conn = null;
		PreparedStatement ps = null;
		String shopCode = "";
		String shopIpAddress = "";
        try{
        	jr.beginObject();
        	String attrName = null;
			while (jr.hasNext()) {
				attrName = jr.nextName();
				if ("shopCode".equals(attrName)) {
					shopCode = jr.nextString();
				} else if ("shopIpAddress".equals(attrName)) {
					shopIpAddress = jr.nextString();
				}  
			}
			conn = DbUtil.getConnection();
			ps = conn.prepareStatement("update shop s set s.ip_address = ? where s.code = ?");
			ps.setString(1, shopIpAddress);
			ps.setString(2,shopCode);
			ps.executeUpdate();
			
          }catch(Exception e){
        	e.printStackTrace();
        	return "{\"message\":\"error\"}";
          }finally{
        	  DbUtil.closeConnection(null, ps, conn);
          }
          return "{\"message\":\"ok\"}";
	}
	
	/**
	 * 根据店面id获取店面对象信息
	 * @param id 店面id
	 * @return
	 */
	public Shop getShopById(int id) {
		return (Shop) this.getXXX("`id`="+id, "shop", Shop.class.getName());
	}
	
	/**
	 * 根据店面编号获取店面对象信息
	 * @param code 编号
	 * @return
	 */
	public Shop getShopByCode(String code) {
		return (Shop) this.getXXX("`code`='"+code+"'", "shop", Shop.class.getName());
	}
	
	/**
	 * 更新店面信息
	 * @param shop 店面信息
	 * @return
	 */
	public boolean updateShop(Shop shop) {
		//更新语句
		StringBuilder set = new StringBuilder();
		set.append("`code`='").append(shop.getCode()).append("', ");
		set.append("`name`='").append(shop.getName()).append("', ");
		set.append("`address`='").append(shop.getAddress()).append("', ");
		set.append("`charger`='").append(shop.getCharger()).append("'");
		
		return this.updateXXX(set.toString(), "`id`="+shop.getId(), "shop");
	}
	
	/**
	 * 删除店面信息
	 * @param id 店面id
	 * @return
	 */
	public boolean deleteShopById(int id) {
		return this.deleteXXX("`id`="+id, "shop");
	}

}
