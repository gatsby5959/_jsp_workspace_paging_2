package handler;

import domain.PagingVO;

public class PagingHandler {
	//jsp화면 list화면 하단에 나오는 페이지네이션에 대한 핸들링클래스 1 2 3 4 5 6 이런거 어쩔때는 10 12 13
	
	private int startPage; //현재 화면에서 보여줄 왼쪽끝의 시작 페이지네이션 번호
	private int endPage; //현재화면에서 보여줄 끝 페이지네이션 번호
	private int realEndPage; // 실제 끝 페이지 번호
	private boolean prev, next; // 보이는 페이지 번호 외의 이전, 다음 페이지의 존재여부

	private int totalCount; // 전체 글 수
	private PagingVO pgvo;
	
	public PagingHandler(PagingVO pgvo, int totalCount) {
		this.pgvo=pgvo;
		this.totalCount = totalCount;
		
		//1~10까지는 10 , 11~20 20, 21~30 30
		//현재페이지 번호 / 한화면의 게시글 수(10) * 한 화면의 게시글 수(10)
		// 1/10= 0.1 무조건올림(씰)   1*10= 10
		// 2/10= 0.2 무조건올림(씰)   1*10= 10
		this.endPage=(int)Math.ceil(pgvo.getPageNo()/(double)pgvo.getQty()) * pgvo.getQty();
		this.startPage = this.endPage-9; //이러다 음수나오면??
//		if(this.startPage < 1) { //230914_0204 JGH임시추가
//			this.startPage = 1;
//		}
		
		//게시글 수 = 11개 / 1,2 페이지
		//전체 게시글 수 / 한 화면의 게시글수
		//나머지가 생기면 무조건 1페이지 더 생겨야함 -> 나눈 결과가 1.2가 나오면 씰해서 페이지가 2가 되어버림 해결완료
		//페이지네이션의 마지막 페이지
		this.realEndPage=(int)Math.ceil(totalCount/(double)pgvo.getQty());
		if(this.realEndPage < this.endPage) {
			this.endPage = this.realEndPage;
		}
		
		this.prev = this.startPage > 1;
		this.next = this.endPage < this.realEndPage;
		
	}//생성자 끝  public PagingHandler(PagingVO pgvo, int totalCount)

	public int getStartPage() {
		return startPage;
	}

	public void setStartPage(int startPage) {
		this.startPage = startPage;
	}

	public int getEndPage() {
		return endPage;
	}

	public void setEndPage(int endPage) {
		this.endPage = endPage;
	}

	public int getRealEndPage() {
		return realEndPage;
	}

	public void setRealEndPage(int realEndPage) {
		this.realEndPage = realEndPage;
	}

	public boolean isPrev() {
		return prev;
	}

	public void setPrev(boolean prev) {
		this.prev = prev;
	}

	public boolean isNext() {
		return next;
	}

	public void setNext(boolean next) {
		this.next = next;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public PagingVO getPgvo() {
		return pgvo;
	}

	public void setPgvo(PagingVO pgvo) {
		this.pgvo = pgvo;
	}

	@Override
	public String toString() {
		return "PagingHandler [startPage=" + startPage + ", endPage=" + endPage + ", realEndPage=" + realEndPage
				+ ", prev=" + prev + ", next=" + next + ", totalCount=" + totalCount + ", pgvo=" + pgvo + "]";
	}
	
	
	
}
