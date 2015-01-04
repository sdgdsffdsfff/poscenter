package mmb.system.admin;

import java.sql.ResultSet;
import java.sql.SQLException;

import mmboa.util.BinaryFlag;
import mmboa.util.Secure;
import mmboa.util.StringUtil;
import mmboa.util.db.BaseService;
import mmboa.util.db.DbOperation;
import mmboa.base.voUser;
import mmb.framework.PermissionFrk;

public class AdminService extends BaseService {
    public AdminService(int useConnType, DbOperation dbOp) {
        this.useConnType = useConnType;
        this.dbOp = dbOp;
    }    
    
    public AdminService() {
        this.useConnType = CONN_IN_METHOD;
    }
    
    // 用于登录，如果用户存在并且密码正确则返回用户bean
    public voUser getAdmin(String username, String password) {
    	voUser vo = getAdmin(username);
    	if(vo == null)
    		return null;
    	if(vo.getPassword().equals(Secure.encryptPwd(password)))
    		return vo;
    	return null;
    }
    
    // 根据用户名获取管理员账号
    public voUser getAdmin(String username) {
		voUser vo = null;
		DbOperation dbOp = getDbOp(DbOperation.DB_SLAVE);
		try {

			ResultSet rs = dbOp.executeQuery("select * from user u left join user_permission up on u.id=up.user_id where username='" + StringUtil.toSql(username) + "'");
			if (rs.next()) {
				vo = new voUser();
				vo.setId(rs.getInt("id"));
				vo.setFlag(rs.getInt("flag"));
				vo.setNick(rs.getString("nick"));
				vo.setName(rs.getString("name"));
				if(rs.getInt("up.id") != 0) {
					vo.setSecurityLevel(rs.getInt("security_level"));
					vo.setPermission(rs.getInt("permission"));
					vo.setGroups(PermissionFrk.string2ints(rs.getString("groups")));
					vo.setBinaryFlag(new BinaryFlag(rs.getBytes("flags")));
				}
				vo.setUsername(username);
				vo.setEmail(rs.getString("email"));
				vo.setPassword(rs.getString("password"));
				vo.setStaffId(rs.getInt("staff_id"));
				vo.setDepartments(rs.getString("departments"));
			}

		} catch (SQLException sqle) {
			sqle.printStackTrace();
		} finally {
			this.release(dbOp);
		}
		return vo;
	}
}
