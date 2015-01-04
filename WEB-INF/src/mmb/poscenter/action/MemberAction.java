package mmb.poscenter.action;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.List;

import mmb.poscenter.domain.Member;
import mmb.poscenter.service.MemberService;
import mmb.poscenter.util.ReadTxtUtils;

import org.apache.commons.lang3.StringUtils;

import com.opensymphony.xwork2.ActionSupport;

public class MemberAction extends ActionSupport {
	
	private static final long serialVersionUID = 1L;

	private MemberService ms = new MemberService();
	private List<Member> list;
	private Page<Member> page;
	private Member member = new Member();
	private File importFile;
	private String memberId;
	private String memberName;
	
	public List<Member> getList() {
		return list;
	}

	public void setList(List<Member> list) {
		this.list = list;
	}

	public Page<Member> getPage() {
		return page;
	}

	public void setPage(Page<Member> page) {
		this.page = page;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public File getImportFile() {
		return importFile;
	}

	public void setImportFile(File importFile) {
		this.importFile = importFile;
	}
	
	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	/**
	 * 跳转至会员列表界面
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public String memberList(){
		if(this.page == null){
			this.page = new Page<Member>();
		}
		if(StringUtils.isNotBlank(this.memberName)){
			try {
				this.memberName = new String(this.memberName.getBytes("ISO-8859-1"),"UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		setPage(ms.getMemberList(page,this.memberId,this.memberName));
		setList(this.page.list);
		return SUCCESS;
	}

	/**
	 * 跳转到会员详情页面
	 * @return
	 */
	public String toMemberDetailView() {
		//获取会员信息
		this.member = ms.getMemberById(this.member.getId());
		
		return "detail";
	}
	
	/**
	 * 跳转至会员列表界面
	 * @return
	 * @throws Exception 
	 */
	public String batImportMember() throws Exception{
		 List<String> list = ReadTxtUtils.readTxtToList(importFile, "GBK");
		 ms.batInsertMemberInfo(ms.filterDuplicateMemberInfo(list));
		return this.memberList();
	}
}
