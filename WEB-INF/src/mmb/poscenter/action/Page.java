package mmb.poscenter.action;

import java.util.List;

public class Page<T> {

	/**
	 * 列表数据
	 */
	public List<T> list;

	/**
	 * 页码
	 */
	private int pageNum = 1;

	/**
	 * 每页显示的记录数
	 */
	private int pageCount = 15;

	/**
	 * 总页数
	 */
	private int totalPages;

	/**
	 * 总记录数
	 */
	private int totalRecords;
	
	/**
	 * 一些查询条件的值
	 */
	private String search;

	public Page() {

	}

	public Page(int i, int j) {
		this.pageNum = i;
		this.pageCount = j;
	}
	
	/**
	 * 获取第一条记录的索引
	 * @return
	 */
	public int getFirstResult() {
		return (this.getPageNum()-1) * this.getPageCount();
	}

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

	public int getPageNum() {
		if(this.pageNum <= 0) {
			this.pageNum = 1;
		}
		if(this.pageNum > this.getTotalPages()) {
			this.pageNum = this.getTotalPages();
		}
		return pageNum;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	/**
	 * 计算总页数
	 * @return
	 */
	public int getTotalPages() {
		this.totalPages = (this.totalRecords%this.pageCount==0 ? this.totalRecords/this.pageCount : this.totalRecords/this.pageCount+1);
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public int getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(int totalRecords) {
		this.totalRecords = totalRecords;
	}

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}

}
