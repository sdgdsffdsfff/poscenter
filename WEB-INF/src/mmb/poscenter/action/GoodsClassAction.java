package mmb.poscenter.action;

import javax.servlet.http.HttpServletRequest;

import mmb.poscenter.domain.GoodsClass;
import mmb.poscenter.service.GoodsClassService;
import mmb.poscenter.util.ResponseUtils;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;

public class GoodsClassAction extends ActionSupport {
	
	private static final long serialVersionUID = 1L;
	private GoodsClassService gcs = new GoodsClassService();
	private GoodsClass goodsClass;
	private String id; //商品分类Id
	
	public GoodsClass getGoodsClass() {
		return goodsClass;
	}

	public void setGoodsClass(GoodsClass goodsClass) {
		this.goodsClass = goodsClass;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 跳转到商品分类表单页面
	 * @return
	 */
	public String toGoodsClassFormView(){
		HttpServletRequest request = ServletActionContext.getRequest();
		//新增
		if(StringUtils.isBlank(id)) {
			goodsClass = new GoodsClass();
			//获取父节点信息，若父节点id为空则增加一级节点
			String parentId = request.getParameter("parentId");
			if(!StringUtils.isBlank(parentId)) {
				GoodsClass parentGoodsClass = gcs.getGoodsClassById(parentId);
				goodsClass.setParent(parentGoodsClass);
			}
			
			//设置层级
			goodsClass.setTreeLevel(Integer.parseInt(request.getParameter("treeLevel")));
		}
		//修改
		else {
			goodsClass = gcs.getGoodsClassById(id);
		}
		return INPUT;
	}
	
	/**
	 * 跳转到商品分类详情页面
	 * @return
	 */
	public String toGoodsClassDetailView() {
		//获取商品分类信息
		this.goodsClass = gcs.getGoodsClassById(id);
		//获取父节点信息
		if(this.goodsClass!=null && !StringUtils.isBlank(this.goodsClass.getParentId())) {
			GoodsClass parentGoodsClass = gcs.getGoodsClassById(this.goodsClass.getParentId());
			goodsClass.setParent(parentGoodsClass);
		}
		return "detail";
	}
	
	/**
	 * 保存商品分类信息
	 */
	public void save() {
		//保存商品分类
		boolean success = gcs.saveGoodsClass(goodsClass);
		ResponseUtils.renderText(ServletActionContext.getResponse(), ""+success);
	}
	
	/**
	 * 删除节点及其所有子孙节点
	 */
	public void removeAll() {
		boolean success = gcs.removeAll(id);
		ResponseUtils.renderText(ServletActionContext.getResponse(), ""+success);
	}
	
	/**
	 * 根据id获取XML格式的子节点列表数据
	 */
	public void getChildrenXmlById() {
		//获取XML格式的子节点列表数据
		String xml = gcs.listChildrenXml(id);
		ResponseUtils.renderXml(ServletActionContext.getResponse(), xml);
	}

}
