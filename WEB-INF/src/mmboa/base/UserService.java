/*
 * Created on 2007-2-8
 *
 */
package mmboa.base;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import mmboa.util.StringUtil;
import mmboa.util.db.BaseService;
import mmboa.util.db.DbOperation;
import mmboa.base.voUser;
import mmb.system.admin.UserPermissionBean;

public class UserService extends BaseService {
    public UserService(int useConnType, DbOperation dbOp) {
        this.useConnType = useConnType;
        this.dbOp = dbOp;
    }

    public UserService() {
        this.useConnType = CONN_IN_METHOD;
    }

    /*
     * 请查看父类或接口对应的注释。
     */
    public boolean addUser(voUser user) {
        return addXXX(user, "user");
    }

    /*
     * 请查看父类或接口对应的注释。
     */
    public boolean deleteUser(String condition) {
        return deleteXXX(condition, "user");
    }

    /*
     * 请查看父类或接口对应的注释。
     */
    public voUser getUser(String condition) {
        return (voUser) getXXX(condition, "user", "mmboa.base.voUser");
    }

    /*
     * 请查看父类或接口对应的注释。
     */
    public int getUserCount(String condition) {
        return getXXXCount(condition, "user", "id");
    }

    /*
     * 请查看父类或接口对应的注释。
     */
    public ArrayList getUserList(String condition, int index, int count,
            String orderBy) {
        return getXXXList(condition, index, count, orderBy, "user",
                "mmboa.base.voUser");
    }
    public ArrayList getUserList(String query) {
        return getXXXList(query, "user", "mmboa.base.voUser");
    }
    
    public voUser getUserByStaffId(int id){
    	String condition = "staff_id=" + id;
    	return (voUser) getXXX(condition, "user", "mmboa.base.voUser");
    }
    public ArrayList queryUserListByPermission(String condition, int index, int count, String orderBy) {
    	ArrayList resultList = new ArrayList();
    	//数据库操作类
        DbOperation dbOp = getDbOp();
        if (!dbOp.init()) {
            return resultList;
        }
        ResultSet rs = null;

        //构建查询语句
        String query = "select * from user u left outer join user_permission up on u.id=up.user_id ";
        if (condition != null) {
            query += " where " + condition;
        }
        if (orderBy != null) {
            query += " order by " + orderBy;
        }

        if (index < 0) {
			index = 0;
		}
		if (count == -1) {
			query += " limit " + index + ", 200";
		} else {
			query += " limit " + index + ", " + count;
		}
        //query = DbOperation.getPagingQuery(query, index, count);

        //执行查询
        rs = dbOp.executeQuery(query);

        if (rs == null) {
            release(dbOp);
            return null;
        }
        try {
	        while(rs.next()){
	        	voUser vo = new voUser();
                vo.setId(rs.getInt("u.id"));
                vo.setUsername(rs.getString("username"));
                vo.setPassword(rs.getString("password"));
                vo.setCreateDatetime(rs.getTimestamp("create_datetime"));
                vo.setFlag(rs.getInt("flag"));
                vo.setName(rs.getString("name"));
                vo.setPhone(rs.getString("phone"));
                vo.setAddress(rs.getString("address"));
                vo.setPostcode(rs.getString("postcode"));
                vo.setNick(rs.getString("nick"));
                vo.setSecurityLevel(rs.getInt("security_level"));
                vo.setPermission(rs.getInt("permission"));
                resultList.add(vo);
	        }
        } catch (Exception e) {
            e.printStackTrace();
            release(dbOp);
            return resultList;
        } finally {
	        //释放数据库连接
	        release(dbOp);
        }

        return resultList;
    }

    /*
     * 请查看父类或接口对应的注释。
     */
    public boolean updateUser(String set, String condition) {
        return updateXXX(set, condition, "user");
    }
    public boolean updateUser(voUser user) {
    	DbOperation dbOp = this.getDbOp();

		try {
	    	dbOp.prepareStatement("update user set username=?,name=?,email=?,phone=?,gender=?,departments=?,password=? where id=" + user.getId());
			PreparedStatement ps=dbOp.getPStmt();
			ps.setString(1, user.getUsername());
			ps.setString(2, user.getName());
			ps.setString(3, user.getEmail());
			ps.setString(4, user.getPhone());
			ps.setInt(5, user.getGender());
			ps.setString(6, user.getDepartments());
			ps.setString(7, user.getPassword());

			int result = ps.executeUpdate();
			user.setId(dbOp.getLastInsertId());
			return result > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.release(dbOp);
		}
		return false;
    }

    /*
     * 请查看父类或接口对应的注释。
     */
    public boolean addUserPermission(UserPermissionBean userPermission) {
        return addXXX(userPermission, "user_permission");
    }

    /*
     * 请查看父类或接口对应的注释。
     */
    public boolean deleteUserPermission(String condition) {
        return deleteXXX(condition, "user_permission");
    }

    /*
     * 请查看父类或接口对应的注释。
     */
    public UserPermissionBean getUserPermission(String condition) {
        return (UserPermissionBean) getXXX(condition, "user_permission", "mmb.system.admin.UserPermissionBean");
    }

    /*
     * 请查看父类或接口对应的注释。
     */
    public int getUserPermissionCount(String condition) {
        return getXXXCount(condition, "user_permission", "id");
    }

    /*
     * 请查看父类或接口对应的注释。
     */
    public ArrayList getUserPermissionList(String condition, int index, int count,
            String orderBy) {
        return getXXXList(condition, index, count, orderBy, "user_permission",
                "mmb.system.admin.UserPermissionBean");
    }

    /*
     * 请查看父类或接口对应的注释。
     */
    public boolean updateUserPermission(String set, String condition) {
        return updateXXX(set, condition, "user_permission");
    }
    
	public String getLastLoginTime(String phone) {
		if(phone == null){
			return null;
		}
		StringBuffer buf = new StringBuffer(64);
		String sql = null;
		phone=StringUtil.toSql(phone);
		buf.append("select last_login_time from user_status where phone='");
		buf.append(phone);
		buf.append("'");
		sql = buf.toString();
		//数据库操作类
        DbOperation dbOp = getDbOp();
        if (!dbOp.init() || !dbOp.setFetchSize(1)) {
            return null;
        }
        ResultSet rs = dbOp.executeQuery(sql);
        try{
	        if(rs != null){
	        	if(rs.next()){
	        		return rs.getString(1);
	        	}
	        }
        }catch(Exception e){
        	e.printStackTrace();
        } finally {
	        //释放数据库连接
	        release(dbOp);
        }
		return null;
	}    


	
	public voUser getVoUserAndGender(String condition){
		StringBuffer buf = new StringBuffer(64);
		String sql = null;
		buf.append("select u.*,ui.gender gender from user u left join user_info ui on u.id=ui.id where ");
		buf.append(condition);
		voUser vo = new voUser();
		sql = buf.toString();
		//数据库操作类
        DbOperation dbOp = getDbOp();
        if (!dbOp.init() || !dbOp.setFetchSize(1)) {
            return null;
        }
        ResultSet rs = dbOp.executeQuery(sql);
        try{
        
        	if(rs.next()){
        		vo.setId(rs.getInt("u.id"));
                vo.setUsername(rs.getString("username"));
                vo.setPassword(rs.getString("password"));
                vo.setCreateDatetime(rs.getTimestamp("create_datetime"));
                vo.setFlag(rs.getInt("flag"));
                vo.setName(rs.getString("name"));
                vo.setPhone(rs.getString("phone"));
                vo.setAddress(rs.getString("address"));
                vo.setPostcode(rs.getString("postcode"));
                vo.setNick(rs.getString("nick"));
                 
                vo.setGender(rs.getInt("gender"));
        	}
	        rs.close();
        }catch(Exception e){
        	e.printStackTrace();
        } finally {
	        //释放数据库连接
	        release(dbOp);
        }
        return vo;
	}
	
	public ArrayList getVoUserAndGenderList(String condition){
		StringBuffer buf = new StringBuffer(64);
		String sql = null;
		buf.append("select u.*,ui.gender gender from user u left join user_info ui on u.id=ui.id where ");
		buf.append(condition);
		sql = buf.toString();
		//数据库操作类
        DbOperation dbOp = getDbOp();
        ArrayList list = new ArrayList();
        ResultSet rs = dbOp.executeQuery(sql);
        try{
        
        	while(rs.next()){
        		voUser vo = new voUser();
        		vo.setId(rs.getInt("u.id"));
                vo.setUsername(rs.getString("username"));
                vo.setPassword(rs.getString("password"));
                vo.setCreateDatetime(rs.getTimestamp("create_datetime"));
                vo.setFlag(rs.getInt("flag"));
                vo.setName(rs.getString("name"));
                vo.setPhone(rs.getString("phone"));
                vo.setAddress(rs.getString("address"));
                vo.setPostcode(rs.getString("postcode"));
                vo.setNick(rs.getString("nick"));
                 
                vo.setGender(rs.getInt("gender"));
                list.add(vo);
        	}
	        rs.close();
        }catch(Exception e){
        	e.printStackTrace();
        } finally {
	        //释放数据库连接
	        release(dbOp);
        }
        return list;
	}
	
	// 部门数据
	public Department getDepartment(String condition) {
		return (Department) getXXX(condition, "department",
				"mmboa.base.Department");
	}
	public List getDepartmentList(String condition, int index, int count, String orderBy) {
		return getXXXList(condition, index, count, orderBy, "department",
				"mmboa.base.Department");
	}
	public List getDepartmentList(String query) {
		return getXXXList(query, "department", "mmboa.base.Department");
	}
	public boolean updateDepartment(String set, String condition) {
		return updateXXX(set, condition, "department");
	}
	public boolean addDepartment(Department bean) {
		return addXXX(bean, "department");
	}
	// 账号激活
	public boolean addUserActivate(UserActivate bean) {
		return addXXX(bean, "user_activate");
	}
	public UserActivate getUserActivate(String condition) {
		return (UserActivate) getXXX(condition, "user_activate",
				"mmboa.base.UserActivate");
	}
}
