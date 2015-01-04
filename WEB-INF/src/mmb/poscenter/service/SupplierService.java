package mmb.poscenter.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import mmb.poscenter.action.Page;
import mmb.poscenter.domain.Supplier;
import mmboa.util.db.BaseService;
import mmboa.util.db.DbOperation;
import mmboa.util.db.DbUtil;

import org.apache.log4j.Logger;

public class SupplierService extends BaseService {
	
	private static Logger log = Logger.getLogger(SupplierService.class);
	
	/**
	 *  供应商列表信息
	 * @param page
	 * @return
	 */
	public Page<Supplier> getSupplierList(Page<Supplier> page){
		page = getPageFullValues(page);
		
		if(page.getTotalRecords() > 0) {
			List<Supplier>  tmp = new ArrayList<Supplier>();
			DbOperation db = new DbOperation("oa");
			Supplier s ;
			try{
				StringBuilder sql = new StringBuilder(50);
				if(page.getSearch()== null || "".equals(page.getSearch()) || "null".equals(page.getSearch())){
					sql.append("select id,name,phone from supplier where is_delete = 0 order by id desc limit ");
					sql.append(page.getFirstResult()).append(",").append(page.getPageCount());
				}else{
					sql.append("select id,name,phone from supplier where name like '%"+page.getSearch()+"%' and is_delete = 0 order by id desc limit ");
					sql.append(page.getFirstResult()).append(",").append(page.getPageCount());
				}
				ResultSet rs = db.executeQuery(sql.toString());
				while(rs.next()){
					s = new Supplier();
					s.setId(rs.getInt("id"));
					s.setPhone(rs.getString("phone"));
					s.setName(rs.getString("name"));
					tmp.add(s);
				}
				page.setList(tmp);
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				this.release(db);
			}
		}
		return page;
	}

	/**
	 *   获取当前分页的信息
	 * @param page
	 * @return
	 */
	public Page<Supplier> getPageFullValues(Page<Supplier> page){
		DbOperation db = new DbOperation("oa");
		try{
			StringBuilder sb = new StringBuilder(50);
			sb.append("select count(1) from supplier where is_delete = 0");
			ResultSet rs = db.executeQuery(sb.toString());
		    if(rs.next()){
		    	page.setTotalRecords(rs.getInt(1));
		    }
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			this.release(db);
		}
		return page;
	}
	
	/**
	 * 根据供应商id获取供应商对象信息
	 * @param id 供应商id
	 * @return
	 */
	public Supplier getSupplierById(int id) {
		return (Supplier) this.getXXX("`id`="+id, "supplier", Supplier.class.getName());
	}
	
	/**
	 * 更新供应商信息
	 * @param supplier 供应商信息
	 * @return
	 */
	public boolean updateSupplier(Supplier supplier) {
		//更新语句
		StringBuilder set = new StringBuilder();
		set.append("`name`='").append(supplier.getName()).append("', ");
		set.append("`phone`='").append(supplier.getPhone()).append("'");
		return this.updateXXX(set.toString(), "`id`="+supplier.getId(), "supplier");
	}
	
	/**
	 * 删除供应商信息（假删）
	 * @param id 供应商id
	 * @return
	 */
	public boolean deleteSupplierById(int id) {
		return this.updateXXX("`is_delete`=1", "`id`="+id, "supplier");
	}

	/**
	 * 判断该供应商是否被商品信息关联
	 * @param id 供应商id
	 * @return true:已被使用	false:未被使用
	 * @throws Exception 
	 */
	public boolean hasUsed(int id) throws Exception {
		boolean hasUsed = true;
		Connection conn = DbUtil.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("SELECT count(m.id) from product m where m.supplier_id=?");
			ps.setInt(1, id);
			rs = ps.executeQuery();
			if (rs.next()) {
				hasUsed = rs.getInt(1) > 0;
			}
		} catch (Exception e) {
			log.error("判断该供应商是否被商品信息关联时出现异常：", e);
			throw e;
		} finally {
			DbUtil.closeConnection(rs, ps, conn);
		}
		return hasUsed;
	}

}
