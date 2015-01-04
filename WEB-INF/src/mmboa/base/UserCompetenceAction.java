package mmboa.base;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import mmb.framework.CustomAction;
import mmb.framework.PermissionFrk;
import mmb.system.admin.UserPermissionBean;
import mmboa.base.voUser;


public class UserCompetenceAction extends CustomAction {
	UserCompetenceService ucs=new UserCompetenceService();
	public UserCompetenceAction(HttpServletRequest request) {
		super(request);
	}
	public ArrayList<voUser> getAllUser(){
		String query="select * from user";
		return ucs.getAll(query);
	}
	public boolean checkUserById(int userid){
		boolean result=false;
		if(countUser(userid)>0){
			result=true;
		}
		return result;
		
	}
	public int countUser(int userid){
		StringBuffer condition=new StringBuffer();
		condition.append("user_id=" + userid);
		return ucs.getUserCount(condition.toString());
	}
	public boolean delCompetence(int id){
		StringBuffer condition =new StringBuffer();
		condition.append("user_id="+id);
		return ucs.delUserCompetence(condition.toString());
	}
	public boolean addCompetence(int id){
		UserPermissionBean up =new UserPermissionBean();
		up.setUserId(id);
		up.setSecurityLevel(10);
		up.setPermission(0);
		up.setFlag(mmboa.util.BinaryFlag.fromStrings(""));
		
		up.setGroups(null);
		return PermissionFrk.updateUserPermission(up, true);
	}
}
