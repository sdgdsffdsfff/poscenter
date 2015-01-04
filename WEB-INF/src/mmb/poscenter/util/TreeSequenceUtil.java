package mmb.poscenter.util;

import java.sql.ResultSet;

import mmboa.util.db.DbOperation;

/**
 * 树形结构对象工具类
 * @author likaige
 */
public class TreeSequenceUtil {
	
	/**商品分类记录号前缀字符*/
	public static final String GOODS_CLASS_PREFIX = "C";
	/**资源记录号前缀字符*/
	public static final String RESOURCE_PREFIX = "R";
	/**菜单记录号前缀字符*/
	public static final String MENU_PREFIX = "M";

	/**
	 * 根据id前缀获取新的id
	 * @param tableName 表名
	 * @param prefix id前缀
	 * @return
	 */
	public static String getSequenceNo(String tableName, String prefix) {
		String sequenceNo = prefix;
		
		DbOperation db = new DbOperation();
		try{
			String sql = "select MAX(SUBSTRING(id, "+(prefix.length()+1)+", 2)) from "+tableName+" where id like '"+prefix+"%' and LENGTH(id) = "+(prefix.length()+2);
			String result = null;
			ResultSet rs = db.executeQuery(sql);
			if(rs.next()){
				result = rs.getString(1);
			}
			
			if(result!=null) {
				int num = Integer.parseInt(result.toString()) + 1;
				sequenceNo += (num<10 ? "0" : "") + num;
			} else {
				sequenceNo += "01";
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			db.release();
		}
		
		return sequenceNo;
	}
}