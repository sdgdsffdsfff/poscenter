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
import mmb.auth.domain.Department;
import mmb.poscenter.action.Page;
import mmboa.util.db.BaseService;
import mmboa.util.db.DbUtil;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

public class DepartmentService extends BaseService {
	
	private static Logger log = Logger.getLogger(DepartmentService.class);
	
	/**
	 * 分页获取部门列表信息
	 * @param page 分页信息
	 * @param param 查询条件[companyName:机构名称；name:部门名称]
	 * @return
	 */
	public Page<Department> getDepartmentPage(Page<Department> page, Map<String, Object> param){
		Connection conn = DbUtil.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			// 查询条件
			String companyName = (String) param.get("companyName");
			String name = (String) param.get("name");
			StringBuffer condSql = new StringBuffer();
			if (StringUtils.isNotBlank(companyName)) {
				condSql.append(" and c.`name` like ? ");
			}
			if (StringUtils.isNotBlank(name)) {
				condSql.append(" and d.`name` like ? ");
			}

			// 查询总记录数
			int index = 1;
			ps = conn.prepareStatement("select count(c.id) from auth_department d join auth_company c on d.company_id=c.id where 1=1 " + condSql);
			if (StringUtils.isNotBlank(companyName)) {
				ps.setString(index++, "%" + companyName + "%");
			}
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
				sql.append("select d.id,d.`name`,d.`remark`,d.create_time,c.`name` companyName");
				sql.append(" from auth_department d join auth_company c on d.company_id=c.id where 1=1 ").append(condSql);
				sql.append(" order by d.id desc limit ");
				sql.append(page.getFirstResult()).append(",").append(page.getPageCount());
				ps = conn.prepareStatement(sql.toString());
				index = 1;
				if (StringUtils.isNotBlank(companyName)) {
					ps.setString(index++, "%" + companyName + "%");
				}
				if (StringUtils.isNotBlank(name)) {
					ps.setString(index++, "%" + name + "%");
				}
				rs = ps.executeQuery();
				List<Department> departmentList = new ArrayList<Department>();
				Department department = null;
				while (rs.next()) {
					department = new Department();
					department.setId(rs.getInt("id"));
					department.setName(rs.getString("name"));
					department.setRemark(rs.getString("remark"));
					department.setCreateTime(rs.getTimestamp("create_time"));
					//机构信息
					Company company = new Company();
					company.setName(rs.getString("companyName"));
					department.setCompany(company);
					departmentList.add(department);
				}
				page.setList(departmentList);
			}
		} catch (Exception e) {
			log.error("分页获取部门列表信息时出现异常：", e);
		} finally {
			DbUtil.closeConnection(rs, ps, conn);
		}
		return page;
	}
	
	/**
	 * 根据id获取部门信息
	 * @param id 部门id
	 * @return
	 */
	public Department getDepartmentById(int id) {
		Department department = null;
		Connection conn = DbUtil.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("select d.id,d.`name`,d.`remark`,d.create_time,d.company_id,c.`name` companyName from auth_department d join auth_company c on d.company_id=c.id where d.id=? ");
			ps.setInt(1, id);
			rs = ps.executeQuery();
			if (rs.next()) {
				department = new Department();
				department.setId(rs.getInt("id"));
				department.setName(rs.getString("name"));
				department.setRemark(rs.getString("remark"));
				department.setCreateTime(rs.getTimestamp("create_time"));
				//机构信息
				Company company = new Company();
				company.setId(rs.getInt("company_id"));
				company.setName(rs.getString("companyName"));
				department.setCompanyId(company.getId());
				department.setCompany(company);
			}
		} catch (Exception e) {
			log.error("根据id获取部门信息时出现异常：", e);
		} finally {
			DbUtil.closeConnection(rs, ps, conn);
		}
		return department;
	}
	
	/**
	 * 保存部门信息
	 * @param department 部门信息
	 * @throws IOException 
	 */
	public void saveDepartment(Department department) throws IOException {
		department.setCreateTime(new Timestamp(new Date().getTime()));
		this.addXXX(department, "auth_department");
	}
	
	/**
	 * 更新部门信息
	 * @param department 部门信息
	 * @throws IOException 
	 */
	public void updateDepartment(Department department) throws IOException {
		//更新语句
		StringBuilder set = new StringBuilder();
		set.append("`name`='").append(department.getName()).append("', ");
		set.append("`remark`='").append(department.getRemark()).append("', ");
		set.append("company_id=").append(department.getCompanyId());
		this.updateXXX(set.toString(), "`id`="+department.getId(), "auth_department");
	}
	
	/**
	 * 删除部门信息
	 * @param id 部门id
	 * @return
	 */
	public boolean deleteDepartmentById(int id) {
		return this.deleteXXX("`id`="+id, "auth_department");
	}
	
	/**
	 * 判断该部门下是否有用户
	 * @param id 部门id
	 * @return true:有用户	false:无用户
	 * @throws Exception 
	 */
	public boolean hasUser(int id) throws Exception {
		boolean hasUser = false;
		Connection conn = DbUtil.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("select count(d.id) from auth_user d where d.department_id=?");
			ps.setInt(1, id);
			rs = ps.executeQuery();
			if (rs.next()) {
				hasUser = rs.getInt(1) > 0;
			}
		} catch (Exception e) {
			log.error("判断该部门下是否有用户时出现异常：", e);
			throw e;
		} finally {
			DbUtil.closeConnection(rs, ps, conn);
		}
		return hasUser;
	}
	
	/**
	 * 判断部门名称是否唯一
	 * @param companyId 机构id
	 * @param name 部门名称
	 * @param exceptId 排除的部门id
	 * @return
	 * @throws Exception 
	 */
	public boolean isUniqueDepartmentName(int companyId, String name, int exceptId) throws Exception {
		boolean isUnique = true;
		Connection conn = DbUtil.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			//查询条件
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT count(c.id) from auth_department c where c.company_id=? and c.name=?");
			if(exceptId != 0) {
				sql.append(" and c.id != ?");
			}
			ps = conn.prepareStatement(sql.toString());
			ps.setInt(1, companyId);
			ps.setString(2, name);
			if(exceptId != 0) {
				ps.setInt(3, exceptId);
			}
			rs = ps.executeQuery();
		    if(rs.next()){
		    	isUnique = rs.getInt(1)==0;
		    }
		}catch(Exception e){
			log.error("判断部门名称是否唯一时出现异常：", e);
			throw e;
		}finally{
			DbUtil.closeConnection(rs, ps, conn);
		}
		return isUnique;
	}

}
