package mmboa.base;

import java.util.ArrayList;

import mmb.system.admin.UserPermissionBean;
import mmboa.util.db.BaseService;
import mmboa.util.db.DbOperation;

public class UserCompetenceService extends BaseService {

	public UserCompetenceService(int useConnType, DbOperation dbOp) {
		this.useConnType = useConnType;
		this.dbOp = dbOp;
	}

	public UserCompetenceService() {
		this.useConnType = CONN_IN_METHOD;
	}

	public ArrayList getAll(String query) {
		return getXXXList(query, "user", "mmboa.base.voUser");
	}
	public int getUserCount(String condition){
		
		return getXXXCount(condition, "user_permission", "user_id");
	}

	public boolean delUserCompetence(String condition) {
		return deleteXXX(condition, "user_permission");
	}
	public boolean addUserCompetence(UserPermissionBean up) {
		return addXXX(up, "user_permission");
	}
}
