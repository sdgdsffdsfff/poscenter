package mmb.auth.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import mmb.auth.domain.Role;
import mmb.poscenter.action.Page;
import mmboa.util.db.BaseService;
import mmboa.util.db.DbOperation;
import mmboa.util.db.DbUtil;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

public class RoleService extends BaseService{
	
	private static Logger log = Logger.getLogger(RoleService.class);

	/**
	 * 分页获取角色列表数据
	 * @param page
	 * @return
	 */
	public Page<Role> getRolePage(Page<Role> page,Map<String,String> param) {
			DbOperation db = new DbOperation();
			try{
				//查询总记录数
				StringBuilder sb = new StringBuilder(50);
				sb.append("select count(1) from auth_role ar ");
				
				if(param!=null){
					 sb.append("where 1 = 1 ");
					if(StringUtils.isNotBlank(param.get("rolename").toString())){
						sb.append(" and ar.role_name like '%").append(param.get("rolename").toString()).append("%' ");
					}
					if(StringUtils.isNotBlank(param.get("rolechname").toString())){
						sb.append(" and ar.role_ch_name like '%").append(param.get("rolechname").toString()).append("%' ");
					}
				}
				
				ResultSet rs = db.executeQuery(sb.toString());
			    if(rs.next()){
			    	page.setTotalRecords(rs.getInt(1));
			    }
			    
			    //查询列表数据
			    if(page.getTotalRecords() > 0) {
			    	List<Role> list = new ArrayList<Role>();
			    	Role role;
			    	StringBuilder sql = new StringBuilder(50);
			    	sql.append("SELECT ar.id,ar.create_time,ar.role_ch_name,ar.role_desc,ar.role_name FROM auth_role ar ");
			    	
			    	if(param!=null){
			    		sql.append("where 1 = 1 ");
						if(StringUtils.isNotBlank(param.get("rolename").toString())){
							sql.append(" and ar.role_name like '%").append(param.get("rolename").toString()).append("%' ");
						}
						if(StringUtils.isNotBlank(param.get("rolechname").toString())){
							sql.append(" and ar.role_ch_name like '%").append(param.get("rolechname").toString()).append("%' ");
						}
					}
			    	
			    	sql.append(" order by ar.id desc limit ");
			    	sql.append(page.getFirstResult()).append(",").append(page.getPageCount());
			    	rs = db.executeQuery(sql.toString());
			    	while(rs.next()){
			    		role = new Role();
						role.setId(rs.getInt("id"));
						role.setRoleName(rs.getString("role_name"));
						role.setCreateTime(rs.getTimestamp("create_time"));
						role.setRoleChName(rs.getString("role_ch_name"));
						role.setRoleDesc(rs.getString("role_desc"));
						list.add(role);
			    	}
			    	page.setList(list);
			    }
			}catch(Exception e){
				log.error("分页获取角色列表数据时出现异常：", e);
			}finally{
				this.release(db);
			}
	   return page;
	}

	/**
	 * 根据角色id获取角色信息
	 * @param id 角色id
	 * @return
	 */
	public Role getRoleById(int id) {
		 Connection conn = DbUtil.getConnection();
		 PreparedStatement ps = null;
		 ResultSet rs = null;
		 Role role = null;
		try{
			ps = conn.prepareStatement("SELECT ar.id,ar.create_time,ar.role_ch_name,ar.role_desc,ar.role_name FROM auth_role ar where ar.id = ? ");
		    ps.setInt(1, id);
			rs = ps.executeQuery();
			if(rs.next()){
				role = new Role();
				role.setId(rs.getInt("id"));
				role.setRoleName(rs.getString("role_name"));
				role.setCreateTime(rs.getTimestamp("create_time"));
				role.setRoleChName(rs.getString("role_ch_name"));
				role.setRoleDesc(rs.getString("role_desc"));
			}
		}catch(Exception e){
			log.error("获取角色信息出错", e);
		}finally{
			DbUtil.closeConnection(rs, ps, conn);
		}
		return role;
	}

	/**
	 * 删除角色数据
	 * @param roleId 角色id
	 */
	public void deleteRoleById(int roleId) {
		Connection conn = DbUtil.getConnection();
		PreparedStatement ps = null;
		boolean oldAutoCommit = true;
		try {
			oldAutoCommit = conn.getAutoCommit();
			conn.setAutoCommit(false);
			
			//删除角色资源中间表数据
			ps = conn.prepareStatement("delete from auth_role_resource where role_id = ? ");
			ps.setInt(1, roleId);
			ps.executeUpdate();
			
			//删除用户角色中间表数据
			ps = conn.prepareStatement("delete from auth_user_role where role_id = ? ");
			ps.setInt(1, roleId);
			ps.executeUpdate();

			//删除角色表数据
			ps = conn.prepareStatement("delete from auth_role where id = ? ");
			ps.setInt(1, roleId);
			ps.executeUpdate();
			conn.commit();
		} catch (Exception e) {
			log.error("删除角色出错！", e);
		} finally {
			DbUtil.closeConnection(null, ps, conn, oldAutoCommit);
		}
	}
	
    /**
     * 检查角色名称是否存在
     * @param roleName 角色名称
     * @param id 角色id
     * @return
     */
	public boolean checkExistRoleName(String roleName, int id) {
		 boolean exist = false;
		 Connection conn = DbUtil.getConnection();
		 PreparedStatement ps = null;
		 ResultSet rs = null;
		 try{
			 //新增角色的时候，仅查看角色名重不重复即可
			 if(id == 0 ){
				 ps = conn.prepareStatement("select count(1) from auth_role ar where ar.role_name = ? ");
				 ps.setString(1, roleName);
			 }else{
				 ps = conn.prepareStatement("select count(1) from auth_role ar where ar.role_name = ? and ar.id != ? ");
				 ps.setString(1, roleName);
				 ps.setInt(2, id);
			 }
			 rs = ps.executeQuery();
			 if(rs.next()){
				 if(rs.getInt(1) != 0 )
					  exist = true;
			 }
		 }catch(Exception e){
			log.error("检查角色信息出错", e);
		 }finally{
		     DbUtil.closeConnection(null, ps, conn);
		}
		 return exist;
	}
    
	/**
	 * 添加新角色
	 * @param role
	 */
	public void addRole(Role role) {
		Connection conn = DbUtil.getConnection();
		PreparedStatement ps = null;
		try{
			ps = conn.prepareStatement("insert into auth_role(role_name,role_ch_name,role_desc,create_time) values(?,?,?,?)");
			ps.setString(1, role.getRoleName());
			ps.setString(2, role.getRoleChName());
			ps.setString(3, role.getRoleDesc());
			ps.setTimestamp(4, role.getCreateTime());
			ps.execute();
		}catch(Exception e){
			log.error("增加角色信息出错", e);
		}finally{
			DbUtil.closeConnection(null, ps, conn);
		}
		
	}
	
	/**
	 * 更新角色信息
	 * @param role
	 */
	public void updateRole(Role role) {
		Connection conn = DbUtil.getConnection();
		PreparedStatement ps = null;
		try{
			ps = conn.prepareStatement("update auth_role ar SET ar.role_name = ? ,ar.role_ch_name = ? ,ar.role_desc = ?  WHERE ar.id = ?");
			ps.setString(1, role.getRoleName());
			ps.setString(2, role.getRoleChName());
			ps.setString(3, role.getRoleDesc());
			ps.setInt(4,role.getId());
			ps.executeUpdate();
		}catch(Exception e){
			log.error("修改角色信息出错", e);
		}finally{
			DbUtil.closeConnection(null, ps, conn);
		}
		
	}

	/**
	 * 获取指定用户的角色列表数据
	 * @param userId 用户id
	 * @return
	 * @throws Exception 
	 */
	public List<Role> getRoleListByUser(int userId) throws Exception {
		List<Role> roleList = new ArrayList<Role>();
		Connection conn = DbUtil.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("SELECT r.id,r.role_name from auth_user_role ur join auth_role r on ur.role_id=r.id and ur.user_id=?");
			ps.setInt(1, userId);
			rs = ps.executeQuery();
			Role role = null;
			while (rs.next()) {
				role = new Role();
				role.setId(rs.getInt("id"));
				role.setRoleName(rs.getString("role_name"));
				roleList.add(role);
			}
		} catch (Exception e) {
			log.error("获取指定用户的角色列表数据时出现异常：", e);
			throw e;
		} finally {
			DbUtil.closeConnection(rs, ps, conn);
		}
		return roleList;
	}

	/**
	 * 分配角色
	 * @param userId 用户id
	 * @param roleIdList 角色Ids
	 * @throws Exception 
	 */
	public void allotRole(int userId, List<Integer> roleIdList) throws Exception {
		Connection conn = DbUtil.getConnection();
		PreparedStatement ps = null;
		boolean oldAutoCommit = true;
		try {
			//记录原来事务提交的方式
			oldAutoCommit = conn.getAutoCommit();
			//关闭自动提交事务
			conn.setAutoCommit(false);
			
			//删除用户的所有角色
			ps = conn.prepareStatement("delete from auth_user_role where user_id=?");
			ps.setInt(1, userId);
			ps.executeUpdate();
			
        	//保存新分配角色
        	ps = conn.prepareStatement("insert into auth_user_role(user_id,role_id) values(?,?)");
			for(Integer roleId : roleIdList) {
				ps.setInt(1, userId);
				ps.setInt(2, roleId);
				ps.addBatch();
			}
			ps.executeBatch();
			
        	conn.commit();
		} catch (Exception e) {
			try {
				//回滚事务
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			log.error("分配角色时出现异常：", e);
			throw e;
		} finally {
			DbUtil.closeConnection(null, ps, conn, oldAutoCommit);
		}
	}

	/**
	 * 保存角色权限
	 * @param roleId
	 * @param resourceCode
	 */
	public boolean saveRoleResource(int roleId ,String resIds){
		Connection conn = DbUtil.getConnection();
		PreparedStatement ps = null;
		boolean commit = false;
		try{
			commit = conn.getAutoCommit();
			conn.setAutoCommit(false);
			ps = conn.prepareStatement("delete from auth_role_resource where role_id = ? ");
			ps.setInt(1, roleId);
			ps.executeUpdate();
			
			List<String> resourceCode = new ArrayList<String>();
			if(StringUtils.isNotBlank(resIds)){
				for(String resId : resIds.split(",")){
					if(!"-1".equals(resId)) {
						resourceCode.add(resId);
					}
				}
				
				ps = conn.prepareStatement("insert into auth_role_resource(role_id,res_id) values(?,?)");
				for(String code : resourceCode){
					ps.setInt(1, roleId);
					ps.setString(2, code);
					ps.addBatch();
				}
				ps.executeBatch();
			}
			
			conn.commit();
		}catch(Exception e){
			try {
				conn.rollback();
			} catch (SQLException e1) {
				log.error("为角色分配资源信息回滚出错！",e1);
			}
			log.error("为角色分配资源信息出错！",e);
			return false;
		}finally{
			DbUtil.closeConnection(null, ps, conn, commit);
		}
		return true;
	}
	
}
