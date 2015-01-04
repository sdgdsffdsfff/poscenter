package mmb.poscenter.service;

import java.io.StringReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.google.gson.stream.JsonReader;

import mmb.poscenter.action.Page;
import mmb.poscenter.domain.Member;
import mmboa.util.db.BaseService;
import mmboa.util.db.DbOperation;
import mmboa.util.db.DbUtil;

public class MemberService extends BaseService {

	private static Logger log = Logger.getLogger(MemberService.class);
	
	/**
	 * 得到中心库与店面店同步的JSON串
	 * @return
	 */
	public String getUpdateMemberInfo(String lastUpdateTime){
		StringBuilder temp = new StringBuilder(1000);
		DbOperation db = new DbOperation();
		try{
			StringBuilder sql = new StringBuilder(50);
			sql.append("select m.id,m.id_card,m.name,m.mobile,m.register_time,m.use_state from member m where m.change_time > '"+lastUpdateTime+"'");
			ResultSet rs = db.executeQuery(sql.toString());
			temp.append("{\"member\":[");
			while(rs.next()){
				temp.append("{\"id\":\"").append(rs.getString("id")).append("\",");
				temp.append("\"id_card\":\"").append(rs.getString("id_card")==null ? "" : rs.getString("id_card")).append("\",");
				temp.append("\"name\":\"").append(rs.getString("name")==null ? "" : rs.getString("name")).append("\",");
				temp.append("\"mobile\":\"").append(rs.getString("mobile")==null ? "" : rs.getString("mobile")).append("\",");
				temp.append("\"register_time\":").append(rs.getTimestamp("register_time") == null ? 0 : rs.getTimestamp("register_time").getTime()).append(",");
				temp.append("\"use_state\":").append(rs.getInt("use_state")).append("},");
				if(rs.isLast()){
					temp.deleteCharAt(temp.length()-1);
				}
			}
			temp.append("]}");
		}catch(Exception e){
			log.error("得到中心库与店面店同步的JSON串时出现异常：", e);
		}finally{
			this.release(db);
		}
		return temp.toString();
	}
	
	/**
	 * 批量更新Member信息表
	 * @param list
	 * @return
	 */
	public boolean batUpdateMemberInfo(List<Member> list) {
		boolean sync = false;
		Connection conn = DbUtil.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean oldAutoCommit = true;
		try {
			// 记录原来事务提交的方式
			oldAutoCommit = conn.getAutoCommit();
			// 关闭自动提交事务
			conn.setAutoCommit(false);
			
			ps = conn.prepareStatement("update member m  SET m.id_card = ?, m.mobile = ?, m.name = ?, m.register_time = ?, m.use_state = ? ,m.change_time = ? where m.id = ? ");
			
			int count = 0;
			for(Member member : list){
				ps.setString(1, member.getIdCard());
				ps.setString(2, member.getMobile());
				ps.setString(3, member.getName());
				ps.setTimestamp(4, member.getRegisterTime());
				ps.setInt(5, member.getUseState());
				Date d = new Date();
				ps.setTimestamp(6, new Timestamp(d.getTime()));
				ps.setString(7, member.getId());
				count ++ ;
				ps.addBatch();
				
				//批量执行
				if (count%100==0 || count==list.size()) {
					ps.executeBatch();
				}
			}
			conn.commit();
			sync = true;
		} catch (Exception e) {
			try {
				//回滚事务
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			log.error("更新店面会员信息出现异常：", e);
		} finally {
			DbUtil.closeConnection(rs, ps, conn, oldAutoCommit);
		}
		return sync;
	}
	
	/**
	 * 批量导入member信息
	 * @param list
	 * @return
	 */
	public boolean batInsertMemberInfo(List<String> list) {
		boolean sync = false;
		Connection conn = DbUtil.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean oldAutoCommit = true;
		try {
			// 记录原来事务提交的方式
			oldAutoCommit = conn.getAutoCommit();
			// 关闭自动提交事务
			conn.setAutoCommit(false);
			
			ps = conn.prepareStatement("insert member(id,change_time) values (?,?) ");
			int count = 0;
			Date d = new Date();
			for(String id : list){
				ps.setString(1, id);
				ps.setTimestamp(2, new Timestamp(d.getTime()));
				count ++ ;
				ps.addBatch();
				//批量执行
				if (count%100==0 || count==list.size()) {
					ps.executeBatch();
				}
			}
			conn.commit();
			sync = true;
		} catch (Exception e) {
			try {
				//回滚事务
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			log.error("批量插入会员信息出现异常：", e);
		} finally {
			DbUtil.closeConnection(rs, ps, conn, oldAutoCommit);
		}
		return sync;
	}
	
	/**
	 * 过滤掉重复的Member信息
	 * @param list
	 * @return
	 */
	public List<String> filterDuplicateMemberInfo(List<String> list) {
		List<String> tmp = new ArrayList<String>();
		Connection conn = DbUtil.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			for (String id : list) {
				int i = 0;
				ps = conn.prepareStatement("select count(1) from member where id = ?");
				ps.setString(1, id);
				rs = ps.executeQuery();
				if (rs.next()) {
					i = rs.getInt(1);
				}
				if (i == 0) {
					tmp.add(id);
				}
			}
		} catch (Exception e) {
			try {
				// 回滚事务
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			log.error("批量插入会员信息出现异常：", e);
		} finally {
			DbUtil.closeConnection(rs, ps, conn);
		}
		return tmp;
	}
	
	//解析posadmin的请求的json串信息
	public String parseAdminRequestJson(String json){
		String temp = "";
		try{
			JsonReader jr = new JsonReader(new StringReader(json));	
			long lut = 0;
			jr.beginObject();
			while(jr.hasNext()){
				if("lastUpdateTime".equals(jr.nextName())){
					lut = jr.nextLong();
				}
			}
			jr.endObject();
			Timestamp t = new Timestamp(lut);
			DateFormat  df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			temp = df.format(t);
		}catch(Exception e){
			log.error("解析posadmin的请求的json串信息时出现异常：", e);
		}
		return temp;
	}
	
	/**
	 * 解析店面店向中心库同步会员基本信息的请求json
	 * @param json
	 * @return
	 */
	public List<Member> parseMemberJsonFromAdmin(String json) {
		List<Member> temp = new ArrayList<Member>();
		Member member = null;
		try {
			JsonReader jr = new JsonReader(new StringReader(json));
			jr.beginObject();
			if ("member".equals(jr.nextName())) {
				jr.beginArray();
				String attrName;
				while (jr.hasNext()) {
					member = new Member();
					jr.beginObject();
					while (jr.hasNext()) {
						attrName = jr.nextName();
						if ("id".equals(attrName)) { // 会员id
							member.setId(jr.nextString());
						} else if ("id_card".equals(attrName)) { // 商品数量
							member.setIdCard(jr.nextString());
						} else if ("name".equals(attrName)) { // 单价
							member.setName(jr.nextString());
						} else if ("mobile".equals(attrName)) {
							member.setMobile(jr.nextString());
						} else if ("register_time".equals(attrName)) {
							member.setRegisterTime(new Timestamp(jr.nextLong()));
						} else if ("use_state".equals(attrName)) {
							member.setUseState(jr.nextInt());
						}
					}
					jr.endObject();
					temp.add(member);
				}
				jr.endArray();
			}
			jr.endObject();
		} catch (Exception e) {
			log.error("解析json格式的店面店向中心库同步会员基本信息数据时出现异常：", e);
		}
		return temp;
	}
		
	/**
	 * 获取会员列表信息
	 * @param page 分页信息
	 * @return
	 */
	public Page<Member> getMemberList(Page<Member> page, String memberId, String memberName) {
		page = getPageFullValues(page, memberId, memberName);
		if (page.getTotalRecords() <= 0) {
			return page;
		}
		List<Member> tmp = new ArrayList<Member>();
		DbOperation db = new DbOperation();
		Member m;
		try {
			StringBuilder sql = new StringBuilder(50);
			sql.append("select m.id,m.name,m.mobile,m.id_card,m.register_time,m.change_time,m.use_state from member m where 1 = 1 ");
			if (StringUtils.isNotBlank(memberId)) {
				sql.append(" and m.id like '%" + memberId + "%' ");
			}
			if (StringUtils.isNotBlank(memberName)) {
				sql.append(" and m.name like '%" + memberName + "%' ");
			}
			sql.append(" order by m.use_state asc limit ");
			sql.append(page.getFirstResult()).append(",").append(page.getPageCount());
			ResultSet rs = db.executeQuery(sql.toString());
			while (rs.next()) {
				m = new Member();
				m.setId(rs.getString("id"));
				m.setName(rs.getString("name"));
				m.setMobile(rs.getString("mobile"));
				m.setIdCard(rs.getString("id_card"));
				m.setRegisterTime(rs.getTimestamp("register_time"));
				m.setChangeTime(rs.getTimestamp("change_time"));
				m.setUseState(rs.getInt("use_state"));
				tmp.add(m);
			}
			page.setList(tmp);
		} catch (Exception e) {
			log.error("获取会员列表信息时出现异常：", e);
		} finally {
			this.release(db);
		}
		return page;
	}

	/**
	 * 获取当前分页的信息
	 * @param page
	 * @return
	 */
	public Page<Member> getPageFullValues(Page<Member> page, String memberId, String memberName) {
		DbOperation db = new DbOperation();
		try {
			StringBuilder sb = new StringBuilder(50);
			sb.append("select count(1) from member m where 1 = 1");
			if (StringUtils.isNotBlank(memberId)) {
				sb.append(" and m.id like '%" + memberId + "%' ");
			}
			if (StringUtils.isNotBlank(memberName)) {
				sb.append(" and m.name like '%" + memberName + "%'");
			}
			ResultSet rs = db.executeQuery(sb.toString());
			if (rs.next()) {
				page.setTotalRecords(rs.getInt(1));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.release(db);
		}
		return page;
	}

	/**
	 * 根据会员id获取会员对象信息
	 * @param id 会员id
	 * @return
	 */
	public Member getMemberById(String id) {
		Member member = (Member) this.getXXX("`id`='" + id + "'", "member", Member.class.getName());
		return member;
	}
 	
}
