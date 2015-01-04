package mmb.auth.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import mmb.auth.domain.Menu;
import mmb.auth.domain.Resource;
import mmb.auth.domain.Role;

import mmb.auth.domain.User;
import mmb.poscenter.action.Page;
import mmboa.util.Secure;
import mmboa.util.db.BaseService;
import mmboa.util.db.DbOperation;
import mmboa.util.db.DbUtil;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

public class UserService extends BaseService{
	
	private static Logger log = Logger.getLogger(UserService.class);
	
	/**
	 * 根据id获取用户的详细信息（包括权限、角色、菜单等信息）
	 * @param id 用户id
	 * @return
	 */
	public User getUserDetail(int id) {
		Connection conn = DbUtil.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		User user = null;
		try {
			//获取用户信息
			ps = conn.prepareStatement("SELECT au.id,au.department_id,au.create_time,au.user_name,au.user_password,au.user_real_name,au.use_status from auth_user au where au.id=?");
			ps.setInt(1, id);
			rs = ps.executeQuery();
			if (rs.next()) {
				user = new User();
				user.setId(rs.getInt("id"));
				user.setUserName(rs.getString("user_name"));
				user.setDepartmentId(rs.getInt("department_id"));
				user.setCreateTime(rs.getTimestamp("create_time"));
				user.setUserPassword(rs.getString("user_password"));
				user.setUserRealName(rs.getString("user_real_name"));
				user.setUseStatus(rs.getInt("use_status"));
			}
			
			//获取用户的所有权限
			List<Resource> resourceList = new ArrayList<Resource>();
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT res.id,res.res_name,res.res_url");
			sql.append(" from auth_user_role ur join auth_role r on ur.role_id=r.id and ur.user_id=?");
			sql.append(" join auth_role_resource rr on r.id=rr.role_id");
			sql.append(" join auth_resource res on rr.res_id=res.id");
			ps = conn.prepareStatement(sql.toString());
			ps.setInt(1, id);
			rs = ps.executeQuery();
			Resource r = null;
			while (rs.next()) {
				r = new Resource();
		    	r.setId(rs.getString("id"));
		    	r.setResName(rs.getString("res_name"));
		    	r.setResUrl(rs.getString("res_url"));
		    	resourceList.add(r);
			}
			user.setResourceList(resourceList);
			
			if(!resourceList.isEmpty()) {
				//获取用户的所有菜单
				List<Menu> menuList = new ArrayList<Menu>();
				sql = new StringBuilder();
				sql.append("SELECT m.id,m.parent_id,m.menu_name,m.res_id,r.res_url");
				sql.append(" from auth_menu m join auth_resource r on m.res_id=r.id and r.id in(");
				for(Resource res : resourceList) {
					sql.append("'").append(res.getId()).append("',");
				}
				sql.deleteCharAt(sql.length()-1).append(")");
				sql.append("ORDER BY m.id");
				ps = conn.prepareStatement(sql.toString());
				rs = ps.executeQuery();
				Menu menu = null;
				while (rs.next()) {
					menu = new Menu();
					menu.setId(rs.getString("id"));
					menu.setParentId(rs.getString("parent_id"));
					menu.setMenuName(rs.getString("menu_name"));
					r = new Resource();
			    	r.setId(rs.getString("res_id"));
			    	r.setResUrl(rs.getString("res_url"));
			    	menu.setResource(r);
			    	menuList.add(menu);
				}
				//组装树形结构的菜单
				List<Menu> menuTree = new ArrayList<Menu>();
				Menu preMenu = null; //记录上一个菜单对象
				for(Menu m : menuList) {
					//一级
					if(StringUtils.isBlank(m.getParentId())) {
						menuTree.add(m);
					}
					//儿子
					else if(m.getParentId().equals(preMenu.getId())) {
						m.setParent(preMenu);
						preMenu.getChildren().add(m);
					}
					//兄弟
					else if(m.getParentId().equals(preMenu.getParentId())) {
						m.setParent(preMenu.getParent());
						preMenu.getParent().getChildren().add(m);
					}
					//是前一个的长辈
					else if(m.getId().length() < preMenu.getId().length()) {
						//找到当前节点的父亲
						Menu p = preMenu.getParent();
						for(int i=0; i<(preMenu.getId().length()-m.getId().length())/2-1; i++) {
							p = p.getParent();
						}
						m.setParent(p);
						p.getChildren().add(m);
					}
					preMenu = m;
				}
				user.setMenuList(menuTree);
			}
		} catch (Exception e) {
			log.error("根据id获取用户的详细信息时出现异常：", e);
		} finally {
			DbUtil.closeConnection(rs, ps, conn);
		}
		return user;
	}
	
	/**
	 * 根据用户名获取用户信息
	 * @param userName 用户名
	 * @param useStatus 使用状态[0：表示查询全部]
	 * @return
	 */
	public User getUserByUserName(String userName, int useStatus) {
		Connection conn = DbUtil.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		User user = null;
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT au.id,au.department_id,au.create_time,au.user_name,au.user_password,au.user_real_name,au.use_status from auth_user au where au.user_name=?");
			if(useStatus != 0) {
				sql.append(" and au.use_status=?");
			}
			ps = conn.prepareStatement(sql.toString());
			ps.setString(1, userName);
			if(useStatus != 0) {
				ps.setInt(2, useStatus);
			}
			rs = ps.executeQuery();
			if (rs.next()) {
				user = new User();
				user.setId(rs.getInt("id"));
				user.setUserName(rs.getString("user_name"));
				user.setDepartmentId(rs.getInt("department_id"));
				user.setCreateTime(rs.getTimestamp("create_time"));
				user.setUserPassword(rs.getString("user_password"));
				user.setUserRealName(rs.getString("user_real_name"));
				user.setUseStatus(rs.getInt("use_status"));
			}
		} catch (Exception e) {
			log.error("根据用户名获取用户信息时出现异常：", e);
		} finally {
			DbUtil.closeConnection(rs, ps, conn);
		}
		return user;
	}

	/**
	 * 添加新用户
	 * @param user
	 */
	public void addUser(User user){
		Connection conn = DbUtil.getConnection();
		PreparedStatement ps = null;
		try{
			ps = conn.prepareStatement("insert into auth_user(department_id,user_name,user_password,user_real_name,use_status,create_time) values(?,?,?,?,?,?)");
			ps.setInt(1, user.getDepartmentId());
			ps.setString(2, user.getUserName());
			ps.setString(3, Secure.encryptPwd(user.getUserPassword()));
			ps.setString(4, user.getUserRealName());
			ps.setInt(5, 1);
			ps.setTimestamp(6, user.getCreateTime());
			ps.execute();
		}catch(Exception e){
			log.error("增加用户信息出错", e);
		}finally{
			DbUtil.closeConnection(null, ps, conn);
		}
	}
	
	/**
	 * 通过用户id得到用户信息
	 * @param id  用户的id
	 */
	public User getUserById(int id){
		Connection conn = DbUtil.getConnection();
		PreparedStatement ps = null;
		ResultSet rs  =null;
		User user = null;
		List<Role> roleList = new ArrayList<Role>();
		try{
			ps = conn.prepareStatement("SELECT ad.name,au.id,au.department_id,au.create_time,au.user_name,au.user_password,au.user_real_name,au.use_status from auth_user au left join auth_department ad on ad.id = au.department_id  where au.id = ? ");
			ps.setInt(1, id);
			rs = ps.executeQuery();
			while(rs.next()){
				user = new User();
				user.setId(rs.getInt("id"));
				user.setUserName(rs.getString("user_name"));
				user.setDepartmentId(rs.getInt("department_id"));
				user.setDepartmentName(rs.getString("name"));
				user.setCreateTime(rs.getTimestamp("create_time"));
				user.setUserPassword(rs.getString("user_password"));
				user.setUserRealName(rs.getString("user_real_name"));
				user.setUseStatus(rs.getInt("use_status"));
			}
			
			ps = conn.prepareStatement("SELECT ar.id,ar.role_name ,ar.role_ch_name from auth_user_role aur LEFT JOIN  auth_role ar  on aur.role_id = ar.id where aur.user_id = ? ");
			ps.setInt(1, id);
			rs = ps.executeQuery();
			Role role = null;
			while(rs.next()){
				role = new Role();
				role.setId(rs.getInt("id"));
				role.setRoleName(rs.getString("role_name"));
				role.setRoleChName(rs.getString("role_ch_name"));
				roleList.add(role);
			}
		  user.setRoleList(roleList);
		}catch(Exception e){
			log.error("查询用户信息出错", e);
		}finally{
			DbUtil.closeConnection(null, ps, conn);
		}
		return user;
	}
	
	
	/**
	 * 修改用户
	 * @param user
	 */
	public void updateUser(User user){
		Connection conn = DbUtil.getConnection();
		PreparedStatement ps = null;
		try{
			if(StringUtils.isNotBlank(user.getUserPassword())){
				ps = conn.prepareStatement("update auth_user au SET au.department_id = ? ,au.user_name = ? ,au.user_password = ? ,au.user_real_name = ?,au.use_status = ?,au.create_time = ? WHERE au.id = ?");
				ps.setInt(1, user.getDepartmentId());
				ps.setString(2, user.getUserName());
				ps.setString(3, Secure.encryptPwd(user.getUserPassword()));
				ps.setString(4, user.getUserRealName());
				ps.setInt(5, user.getUseStatus());
				ps.setTimestamp(6, user.getCreateTime());
				ps.setInt(7, user.getId());
				ps.executeUpdate();
			}else{
				ps = conn.prepareStatement("update auth_user au SET au.department_id = ? ,au.user_name = ? ,au.user_real_name = ?,au.use_status = ?,au.create_time = ? WHERE au.id = ?");
				ps.setInt(1, user.getDepartmentId());
				ps.setString(2, user.getUserName());
				ps.setString(3, user.getUserRealName());
				ps.setInt(4, user.getUseStatus());
				ps.setTimestamp(5, user.getCreateTime());
				ps.setInt(6, user.getId());
				ps.executeUpdate();
			}
		}catch(Exception e){
			log.error("修改用户信息出错", e);
		}finally{
			DbUtil.closeConnection(null, ps, conn);
		}
		
	}
	
	
	/**
	 * 删除用户
	 * @param user
	 */
	public void deleteUserById(int id){
		Connection conn = DbUtil.getConnection();
		PreparedStatement ps = null;
		boolean oldAutoCommit = true;
		try{
			oldAutoCommit = conn.getAutoCommit();
			conn.setAutoCommit(false);
			ps = conn.prepareStatement("delete from  auth_user where id = ? ");
			ps.setInt(1, id);
			ps.executeUpdate();
			
			ps = conn.prepareStatement("delete from auth_user_role where user_id = ?");
			ps.setInt(1, id);
			ps.executeUpdate();
			
			conn.commit();
		}catch(Exception e){
			log.error("删除用户信息出错", e);
		}finally{
			DbUtil.closeConnection(null, ps, conn,oldAutoCommit);
		}
		
	}
	
	/**
	 * 获取用户信息列表
	 * @return
	 */
	public List<User> getUserList(){
		List<User> temp = new ArrayList<User>();
		Connection conn = DbUtil.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		User user = null;
		try{
			ps = conn.prepareStatement("SELECT au.id,au.department_id,au.create_time,au.user_name,au.user_password,au.user_real_name,au.use_status from auth_user au  where 1 = 1 ");
			rs = ps.executeQuery();
			while(rs.next()){
				user = new User();
				user.setId(rs.getInt("id"));
				user.setUserName(rs.getString("user_name"));
				user.setDepartmentId(rs.getInt("department_id"));
				user.setCreateTime(rs.getTimestamp("create_time"));
				user.setUserPassword(rs.getString("user_password"));
				user.setUserRealName(rs.getString("user_real_name"));
				user.setUseStatus(rs.getInt("use_status"));
				temp.add(user);
			}
		}catch(Exception e){
			log.error("删除用户信息出错", e);
		}finally{
			DbUtil.closeConnection(null, ps, conn);
		}
		return temp;
	}
	
	
	/**
	 * 分页获取用户列表信息
	 * @param page
	 * @return
	 */
	public Page<User> getUserPage(Page<User> page,Map<String, String> param){
		DbOperation db = new DbOperation();
		try{
			//查询总记录数
			StringBuilder sb = new StringBuilder(50);
			sb.append("select count(1) from auth_user au left join auth_department ad on au.department_id = ad.id  ");
			
			if(param!=null){
				 sb.append("where 1 = 1 ");
				if(StringUtils.isNotBlank(param.get("username").toString())){
					sb.append(" and au.user_name like '%").append(param.get("username").toString()).append("%' ");
				}
				if(StringUtils.isNotBlank(param.get("departmentname").toString())){
					sb.append(" and ad.name like '%").append(param.get("departmentname").toString()).append("%' ");
				}
			}
			ResultSet rs = db.executeQuery(sb.toString());
		    if(rs.next()){
		    	page.setTotalRecords(rs.getInt(1));
		    }
		    //查询列表数据
		    if(page.getTotalRecords() > 0) {
		    	List<User> list = new ArrayList<User>();
		    	User user;
		    	StringBuilder sql = new StringBuilder(50);
		    	sql.append("SELECT au.id,au.department_id,au.create_time,au.user_name,au.user_password,au.user_real_name,au.use_status,ad.name from auth_user au left join auth_department ad on au.department_id = ad.id ");
		    	
		    	if(param!=null){
		    		sql.append("where 1 = 1 ");
					if(StringUtils.isNotBlank(param.get("username").toString())){
						sql.append(" and au.user_name like '%").append(param.get("username").toString()).append("%' ");
					}
					if(StringUtils.isNotBlank(param.get("departmentname").toString())){
						sql.append(" and ad.name like '%").append(param.get("departmentname").toString()).append("%' ");
					}
				}
		    	
		    	sql.append(" order by au.id desc limit ");
		    	sql.append(page.getFirstResult()).append(",").append(page.getPageCount());
		    	rs = db.executeQuery(sql.toString());
		    	while(rs.next()){
		    		user = new User();
					user.setId(rs.getInt("id"));
					user.setUserName(rs.getString("user_name"));
					user.setDepartmentId(rs.getInt("department_id"));
					user.setCreateTime(rs.getTimestamp("create_time"));
					user.setUserPassword(rs.getString("user_password"));
					user.setUserRealName(rs.getString("user_real_name"));
					user.setUseStatus(rs.getInt("use_status"));
					user.setDepartmentName(rs.getString("name"));
					list.add(user);
		    	}
		    	page.setList(list);
		    }
		}catch(Exception e){
			log.error("分页获取用户列表数据时出现异常：", e);
		}finally{
			this.release(db);
		}
		return page;
	}
	
	/**
	 * 判断用户名是否已经存在
	 * @param username 用户名
	 * @return
	 */
	public boolean checkExistUserName(String username, int id) {
		boolean exist = false;
		Connection conn = DbUtil.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			// 新增用户的时候，仅查看用户名重不重复即可
			if (id == 0) {
				ps = conn.prepareStatement("select count(1) from auth_user au where au.user_name = ? ");
				ps.setString(1, username);
			} else {
				ps = conn.prepareStatement("select count(1) from auth_user au where au.user_name = ? and au.id != ? ");
				ps.setString(1, username);
				ps.setInt(2, id);
			}
			rs = ps.executeQuery();
			if (rs.next()) {
				exist = (rs.getInt(1) != 0);
			}
		} catch (Exception e) {
			log.error("检查用户信息出错", e);
		} finally {
			DbUtil.closeConnection(rs, ps, conn);
		}
		return exist;
	}

	/**
	 * 修改用户密码
	 * @param user 用户对象
	 * @throws Exception 
	 */
	public void updatePassword(User user) throws Exception {
		Connection conn = DbUtil.getConnection();
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement("update auth_user set user_password=? where id=?");
			ps.setString(1, Secure.encryptPwd(user.getUserPassword()));
			ps.setInt(2, user.getId());
			ps.executeUpdate();
		} catch (Exception e) {
			log.error("修改用户密码时出现异常：", e);
			throw e;
		} finally {
			DbUtil.closeConnection(null, ps, conn);
		}
	}
	
}
