package mmb.poscenter.domain;

import java.io.Serializable;
import java.sql.Timestamp;


public class Member  implements Serializable {

	private static final long serialVersionUID = 4351933227205896379L;

	/**
	 * 会员id
	 */
	public String id;
	
	/**
	 * 会员名称
	 */
	public String name;
	
	/**
	 * 会员身份证号
	 */
	public String idCard;
	
	/**
	 * 手机号
	 */
	public String mobile;
	 
	/**
	 * 注册时间
	 */
	public Timestamp registerTime;
	
	/**
	 * 使用状态[0:使用中; 1:已注销; 2:新卡;3:挂失]
	 */
	public int useState;
	
	/**
	 * 会员信息更改时间
	 */
	public Timestamp changeTime;
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
	
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Timestamp getRegisterTime() {
		return registerTime;
	}

	public void setRegisterTime(Timestamp registerTime) {
		this.registerTime = registerTime;
	}

	public int getUseState() {
		return useState;
	}

	public void setUseState(int useState) {
		this.useState = useState;
	}

	public Timestamp getChangeTime() {
		return changeTime;
	}

	public void setChangeTime(Timestamp changeTime) {
		this.changeTime = changeTime;
	}



	
	
}
