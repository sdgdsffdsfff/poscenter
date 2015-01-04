package mmb.poscenter.service;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mmb.poscenter.action.Page;
import mmb.poscenter.domain.ShopVersion;
import mmboa.util.Constants;
import mmboa.util.db.BaseService;
import mmboa.util.db.DbOperation;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.google.gson.Gson;

public class ShopVersionService extends BaseService {
	
	private static Logger log = Logger.getLogger(ShopVersionService.class);
	
	/**
	 * 分页获取店面系统版本列表信息
	 * @param page 分页信息
	 * @return
	 */
	public Page<ShopVersion> getShopVersionPage(Page<ShopVersion> page){
		List<ShopVersion> list = new ArrayList<ShopVersion>();
		DbOperation db = new DbOperation();
		ShopVersion s;
		try{
			//查询总记录数
			StringBuilder sb = new StringBuilder(50);
			sb.append("select count(id) from shop_version s ");
			ResultSet rs = db.executeQuery(sb.toString());
		    if(rs.next()){
		    	page.setTotalRecords(rs.getInt(1));
		    }
		    
		    //查询列表数据
		    if(page.getTotalRecords() > 0) {
		    	StringBuilder sql = new StringBuilder(50);
		    	sql.append("select s.id,s.version_number,s.version_name,s.version_desc,s.file_path,s.create_time,s.use_status");
		    	sql.append(" from shop_version s");
		    	sql.append(" order by s.id desc limit ");
		    	sql.append(page.getFirstResult()).append(",").append(page.getPageCount());
		    	rs = db.executeQuery(sql.toString());
		    	while(rs.next()){
		    		s = new ShopVersion();
		    		s.setId(rs.getInt("id"));
		    		s.setVersionNumber(rs.getString("version_number"));
		    		s.setVersionName(rs.getString("version_name"));
		    		s.setVersionDesc(rs.getString("version_desc"));
		    		s.setFilePath(rs.getString("file_path"));
		    		s.setCreateTime(rs.getTimestamp("create_time"));
		    		s.setUseStatus(rs.getInt("use_status"));
		    		list.add(s);
		    	}
		    	page.setList(list);
		    }
		}catch(Exception e){
			log.error("分页获取店面系统版本列表信息时出现异常：", e);
		}finally{
			db.release();
		}
		return page;
	}
	
	/**
	 * 根据店面系统版本id获取店面系统版本对象信息
	 * @param id 店面系统版本id
	 * @return
	 */
	public ShopVersion getShopVersionById(int id) {
		return (ShopVersion) this.getXXX("`id`="+id, "shop_version", ShopVersion.class.getName());
	}
	
	/**
	 * 根据版本号获取店面系统版本对象信息
	 * @param versionNumber 版本号
	 * @return
	 */
	public ShopVersion getByVersionNumber(String versionNumber) {
		return (ShopVersion) this.getXXX("`version_number`='"+versionNumber+"'", "shop_version", ShopVersion.class.getName());
	}
	
	/**
	 * 获取最新的店面系统版本(已发布的版本)
	 * @return
	 */
	public ShopVersion getLatestShopVersion() {
		DbOperation db = new DbOperation();
		ShopVersion s = null;
		try{
			StringBuilder sql = new StringBuilder();
	    	sql.append("select s.id,s.version_number,s.version_name,s.version_desc,s.file_path,s.create_time");
	    	sql.append(" from shop_version s");
	    	sql.append(" where s.use_status=2");
	    	sql.append(" order by s.id desc limit 1");
	    	ResultSet rs = db.executeQuery(sql.toString());
	    	if(rs.next()){
	    		s = new ShopVersion();
	    		s.setId(rs.getInt("id"));
	    		s.setVersionNumber(rs.getString("version_number"));
	    		s.setVersionName(rs.getString("version_name"));
	    		s.setVersionDesc(rs.getString("version_desc"));
	    		s.setFilePath(rs.getString("file_path"));
	    		s.setCreateTime(rs.getTimestamp("create_time"));
	    	}
		}catch(Exception e){
			log.error("获取最新的店面系统版本时出现异常：", e);
		}finally{
			db.release();
		}
		return s;
	}
	
	/**
	 * 获取新的版本号
	 * @return 新版本号
	 */
	public String getNewVersionNumber() {
		String versionNumber = "";
		DbOperation db = new DbOperation();
		try {
			// 获取当前日期
			String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
			int count = 0;

			// 查询总记录数
			String sql = "SELECT count(id) from shop_version s where s.create_time>=STR_TO_DATE('" + date + "','%Y%m%d') and s.create_time<ADDDATE(STR_TO_DATE('" + date + "','%Y%m%d'), 1)";
			ResultSet rs = db.executeQuery(sql);
			if (rs.next()) {
				count = rs.getInt(1);
			}

			count++;
			versionNumber = "V"+date + (count > 9 ? count : "0" + count);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return versionNumber;
	}
	
	/**
	 * 更新店面系统版本信息
	 * @param shopVersion 店面系统版本信息
	 * @return
	 * @throws IOException 
	 */
	public boolean updateShopVersion(ShopVersion shopVersion) throws IOException {
		//替换部署包
		if(shopVersion.getDeployFile() != null) {
			File destFile = new File(ServletActionContext.getServletContext().getRealPath("/") + shopVersion.getFilePath());
			if(!destFile.getParentFile().exists()) {
				destFile.getParentFile().mkdirs();
			}
			File srcFile = shopVersion.getDeployFile();
			FileUtils.copyFile(srcFile, destFile);
		}
		
		//更新语句
		StringBuilder set = new StringBuilder();
		set.append("`version_name`='").append(shopVersion.getVersionName()).append("', ");
		set.append("`version_desc`='").append(shopVersion.getVersionDesc()).append("'");
		return this.updateXXX(set.toString(), "`id`="+shopVersion.getId(), "shop_version");
	}
	
	/**
	 * 删除店面系统版本信息
	 * @param id 店面系统版本id
	 * @return
	 */
	public boolean deleteShopVersionById(int id) {
		return this.deleteXXX("`id`="+id, "shop_version");
	}

	/**
	 * 保存店面系统版本信息
	 * @param shopVersion
	 * @throws IOException 
	 */
	public void saveShopVersion(ShopVersion shopVersion) throws IOException {
		//保存部署包
		String filePath = Constants.config.getProperty("shopSystemDeployPackagePath") + shopVersion.getVersionNumber()+".war";
		File destFile = new File(ServletActionContext.getServletContext().getRealPath("/") + filePath);
		if(!destFile.getParentFile().exists()) {
			destFile.getParentFile().mkdirs();
		}
		File srcFile = shopVersion.getDeployFile();
		FileUtils.copyFile(srcFile, destFile);
		
		//保存店面系统版本信息
		shopVersion.setFilePath(filePath);
		shopVersion.setUseStatus(1); //1:未发布
		shopVersion.setCreateTime(new Timestamp(new Date().getTime()));
		this.addXXX(shopVersion, "shop_version");
	}

	/**
	 * posadmin检查系统更新
	 * <br/>获取最新系统版本信息
	 * @return JSON字符串
	 */
	public String shopGetLatestShopVersion() {
		String json = "";
		ShopVersion shopVersion = this.getLatestShopVersion();
		if(shopVersion != null) {
			json = new Gson().toJson(shopVersion);
		}
		return json;
	}

	/**
	 * 发布版本
	 * @param id 版本id
	 */
	public boolean releaseVersion(int id) {
		return this.updateXXX("use_status=2", "`id`="+id, "shop_version");
	}

}
