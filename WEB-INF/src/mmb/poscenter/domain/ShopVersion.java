package mmb.poscenter.domain;

import java.io.File;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 店面系统版本
 * @author likaige
 */
public class ShopVersion implements Serializable {
	
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	public int id;
	
	/**
	 * 版本号
	 */
	public String versionNumber;
	
	/**
	 * 版本名称
	 */
	public String versionName;
	
	/**
	 * 版本描述
	 */
	public String versionDesc;
	
	/**
	 * 文件路径
	 */
	public String filePath;
	
	/**
	 * 文件对象
	 */
	private File deployFile;
	
	/**
	 * 发布时间
	 */
	public Timestamp createTime;
	
	/**
	 * 使用状态[1:未发布；2:已发布]
	 */
	public int useStatus;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getVersionNumber() {
		return versionNumber;
	}

	public void setVersionNumber(String versionNumber) {
		this.versionNumber = versionNumber;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public String getVersionDesc() {
		return versionDesc;
	}

	public void setVersionDesc(String versionDesc) {
		this.versionDesc = versionDesc;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
	public File getDeployFile() {
		return deployFile;
	}

	public void setDeployFile(File deployFile) {
		this.deployFile = deployFile;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public int getUseStatus() {
		return useStatus;
	}

	public void setUseStatus(int useStatus) {
		this.useStatus = useStatus;
	}
	
}
