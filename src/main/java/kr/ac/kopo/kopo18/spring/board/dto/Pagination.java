package kr.ac.kopo.kopo18.spring.board.dto;

public class Pagination {
	Long ppPage;		// <<
	Long pPage;		// <
	Long page;		// 현재 페이지
	Long nPage;		// >
	Long nnPage;		// >>
	Long sList;		// 페이지 목록 시작 번호
	Long eList;		// 페이지 목록 끝번호
	Long countPerPage;	// 페이지당 출력 데이터 수
	Long pageSize; 		// pager Size

	public Pagination() {}		// 기본 생성자
	
	// Getters & Setters
	public Long getPpPage() {
		return ppPage;
	}
	public void setPpPage(Long ppPage) {
		this.ppPage = ppPage;
	}
	public Long getpPage() {
		return pPage;
	}
	public void setpPage(Long pPage) {
		this.pPage = pPage;
	}
	public Long getpage() {
		return page;
	}
	public void setpage(Long cPage) {
		this.page = cPage;
	}
	public Long getnPage() {
		return nPage;
	}
	public void setnPage(Long nPage) {
		this.nPage = nPage;
	}
	public Long getNnPage() {
		return nnPage;
	}
	public void setNnPage(Long nnPage) {
		this.nnPage = nnPage;
	}
	public Long getsList() {
		return sList;
	}
	public void setsList(Long sList) {
		this.sList = sList;
	}
	public Long geteList() {
		return eList;
	}
	public void seteList(Long eList) {
		this.eList = eList;
	}
	public Long getCountPerPage() {
		return countPerPage;
	}
	public void setCountPerPage(Long countPerPage) {
		this.countPerPage = countPerPage;
	}
	public Long getPageSize() {
		return pageSize;
	}
	public void setPageSize(Long pageSize) {
		this.pageSize = pageSize;
	}

}
