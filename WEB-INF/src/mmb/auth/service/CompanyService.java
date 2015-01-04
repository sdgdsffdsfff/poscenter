package mmb.auth.service;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import mmb.auth.domain.Company;
import mmb.poscenter.action.Page;
import mmboa.util.db.BaseService;
import mmboa.util.db.DbUtil;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

public class CompanyService extends BaseService {
	
	private static Logger log = Logger.getLogger(CompanyService.class);
	
	/**
	 * 分页获取机构列表信息
	 * @param page 分页信息
	 * @param param 查询条件[name:机构名称]
	 * @return
	 */
	public Page<Company> getCompanyPage(Page<Company> page, Map<String, Object> param){
		Connection conn = DbUtil.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			// 查询条件
			String name = (String) param.get("name");
			StringBuffer condSql = new StringBuffer();
			if (StringUtils.isNotBlank(name)) {
				condSql.append(" and c.`name` like ? ");
			}

			// 查询总记录数
			int index = 1;
			ps = conn.prepareStatement("select count(c.id) from auth_company c where 1=1 " + condSql);
			if (StringUtils.isNotBlank(name)) {
				ps.setString(index++, "%" + name + "%");
			}
			rs = ps.executeQuery();
			if (rs.next()) {
				page.setTotalRecords(rs.getInt(1));
			}

			// 查询列表数据
			if (page.getTotalRecords() > 0) {
				StringBuilder sql = new StringBuilder(50);
				sql.append("select c.id,c.`name`,c.`telephone`,c.create_time");
				sql.append(" from auth_company c where 1=1 ").append(condSql);
				sql.append(" order by c.id desc limit ");
				sql.append(page.getFirstResult()).append(",").append(page.getPageCount());
				ps = conn.prepareStatement(sql.toString());
				index = 1;
				if (StringUtils.isNotBlank(name)) {
					ps.setString(index++, "%" + name + "%");
				}
				rs = ps.executeQuery();
				List<Company> companyList = new ArrayList<Company>();
				Company company = null;
				while (rs.next()) {
					company = new Company();
					company.setId(rs.getInt("id"));
					company.setName(rs.getString("name"));
					company.setTelephone(rs.getString("telephone"));
					company.setCreateTime(rs.getTimestamp("create_time"));
					companyList.add(company);
				}
				page.setList(companyList);
			}
		} catch (Exception e) {
			log.error("分页获取机构列表信息时出现异常：", e);
		} finally {
			DbUtil.closeConnection(rs, ps, conn);
		}
		return page;
	}
	
	/**
	 * 根据id获取机构信息
	 * @param id 机构id
	 * @return
	 */
	public Company getCompanyById(int id) {
		return (Company) this.getXXX("`id`="+id, "auth_company", Company.class.getName());
	}
	
	/**
	 * 保存机构信息
	 * @param company 机构信息
	 * @throws IOException 
	 */
	public void saveCompany(Company company) throws IOException {
		company.setCreateTime(new Timestamp(new Date().getTime()));
		this.addXXX(company, "auth_company");
	}
	
	/**
	 * 更新机构信息
	 * @param company 机构信息
	 * @throws IOException 
	 */
	public void updateCompany(Company company) throws IOException {
		//更新语句
		StringBuilder set = new StringBuilder();
		set.append("`name`='").append(company.getName()).append("', ");
		set.append("`telephone`='").append(company.getTelephone()).append("'");
		this.updateXXX(set.toString(), "`id`="+company.getId(), "auth_company");
	}
	
	/**
	 * 删除机构信息
	 * @param id 机构id
	 * @return
	 */
	public boolean deleteCompanyById(int id) {
		return this.deleteXXX("`id`="+id, "auth_company");
	}
	
	/**
	 * 判断机构下是否包含部门
	 * @param id 机构id
	 * @return true:有部门	false:无部门
	 * @throws Exception 
	 */
	public boolean hasDepartment(int id) throws Exception {
		boolean hasDepartment = false;
		Connection conn = DbUtil.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("select count(d.id) from auth_department d where d.company_id=?");
			ps.setInt(1, id);
			rs = ps.executeQuery();
			if (rs.next()) {
				hasDepartment = rs.getInt(1) > 0;
			}
		} catch (Exception e) {
			log.error("判断机构下是否包含部门时出现异常：", e);
			throw e;
		} finally {
			DbUtil.closeConnection(rs, ps, conn);
		}
		return hasDepartment;
	}
	
	/**
	 * 判断机构名称是否唯一
	 * @param name 机构名称
	 * @param exceptId 排除的机构id
	 * @return
	 * @throws Exception
	 */
	public boolean isUniqueCompanyName(String name, int exceptId) throws Exception {
		boolean isUnique = true;
		Connection conn = DbUtil.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			//查询条件
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT count(c.id) from auth_company c where c.name=?");
			if(exceptId != 0) {
				sql.append(" and c.id != ?");
			}
			ps = conn.prepareStatement(sql.toString());
			ps.setString(1, name);
			if(exceptId != 0) {
				ps.setInt(2, exceptId);
			}
			rs = ps.executeQuery();
		    if(rs.next()){
		    	isUnique = rs.getInt(1)==0;
		    }
		}catch(Exception e){
			log.error("判断机构名称是否唯一时出现异常：", e);
			throw e;
		}finally{
			DbUtil.closeConnection(rs, ps, conn);
		}
		return isUnique;
	}

}
