package mmb.poscenter.action;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import mmb.poscenter.domain.ShopVersion;
import mmb.poscenter.service.ShopVersionService;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;

public class ShopVersionAction extends ActionSupport {
	
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(ShopVersionAction.class);
	
	private ShopVersionService svs = new ShopVersionService();
	private Page<ShopVersion> page = new Page<ShopVersion>();
	private ShopVersion shopVersion = new ShopVersion();
	
	//部署包
	private File deploy;
	private String deployFileName;
	
	public Page<ShopVersion> getPage() {
		return page;
	}

	public void setPage(Page<ShopVersion> page) {
		this.page = page;
	}

	public ShopVersion getShopVersion() {
		return shopVersion;
	}

	public void setShopVersion(ShopVersion ShopVersion) {
		this.shopVersion = ShopVersion;
	}
	
	public File getDeploy() {
		return deploy;
	}

	public void setDeploy(File deploy) {
		this.deploy = deploy;
	}

	public String getDeployFileName() {
		return deployFileName;
	}

	public void setDeployFileName(String deployFileName) {
		this.deployFileName = deployFileName;
	}

	/**
	 * 跳转至店面系统版本列表界面
	 * @return
	 */
	public String shopVersionList(){
		this.page = svs.getShopVersionPage(page);
		return SUCCESS;
	}
	
	/**
	 * 跳转到店面系统版本表单页面
	 * @return
	 */
	public String toShopVersionFormView() {
		//修改
		if(this.shopVersion.getId() != 0) {
			this.shopVersion = svs.getShopVersionById(this.shopVersion.getId());
		}
		//新增
		else {
			//String versionNumber = svs.getNewVersionNumber();
			//this.shopVersion.setVersionNumber(versionNumber);
		}
		return INPUT;
	}
	
	/**
	 * 保存店面系统版本信息
	 * @return
	 */
	public String saveShopVersion() {
		try {
			//新建
			if(this.shopVersion.getId() == 0) {
				if(this.deploy != null) {
					//验证版本号是否有效
					deployFileName = deployFileName.substring(0, deployFileName.lastIndexOf("."));
					if(this.validVersionNumber(deployFileName)) {
						this.shopVersion.setVersionNumber(deployFileName);
						this.shopVersion.setDeployFile(deploy);
						svs.saveShopVersion(shopVersion);
					} else {
						throw new Exception("系统部署包文件命名错误！");
					}
				} else {
					throw new Exception("系统部署包为空！");
				}
			}
			//修改
			else {
				if(this.deploy != null) {
					//验证版本号是否一致
					deployFileName = deployFileName.substring(0, deployFileName.lastIndexOf("."));
					if(deployFileName.equals(shopVersion.getVersionNumber())) {
						this.shopVersion.setDeployFile(deploy);
					} else {
						throw new Exception("系统部署包文件名与版本号不一致！");
					}
				}
				svs.updateShopVersion(shopVersion);
			}
		} catch (Exception e) {
			ServletActionContext.getRequest().setAttribute("message", e.getMessage());
			log.error("保存店面系统版本信息时出现异常：", e);
			return ERROR;
		}
		return this.shopVersionList();
	}
	
	/**
	 * 删除店面系统版本信息
	 * @return
	 */
	public String deleteShopVersion() {
		//删除
		svs.deleteShopVersionById(this.shopVersion.getId());
		return this.shopVersionList();
	}
	
	/**
	 * 发布版本
	 * @return
	 */
	public String releaseVersion() {
		svs.releaseVersion(this.shopVersion.getId());
		return this.shopVersionList();
	}
	
	/**
	 * 验证版本号是否有效
	 * @param deployFileName 部署包文件名称[V2013050601]
	 * @return
	 */
	private boolean validVersionNumber(String deployFileName) {
		boolean isValid = true;
		isValid = deployFileName.matches("V\\d{10}");
		if(isValid) {
			try {
				new SimpleDateFormat("yyyyMMdd").parse(deployFileName.substring(1, 9));
			} catch (ParseException e) {
				isValid = false;
			}
		}
		return isValid;
	}
	
}
