package controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//import org.apache.catalina.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import domain.BoardVO;
import domain.PagingVO;
import handler.PagingHandler;
import service.BoardService;
import service.BoardServiceImpl;


@WebServlet("/brd/*")
public class BoardController extends HttpServlet {
	private static final long serialVersionUID = 1L;
    //로그 객체 선언
	private static final Logger log = LoggerFactory.getLogger(BoardController.class);
	//requestDespatcher객체
	private RequestDispatcher rdp;
	//service interface
	private BoardService bsv;
//	private Service bsv2; //이건 안쓸듯한데 일단 박아봄 서버에 있어용 카탈리나 안씀 
	//destPage : 목적지 주소 저장변수
	private String destPage;
	//isOk
	private int isOk;
	

    public BoardController() {
       //bsv의 객체 연결
       bsv = new BoardServiceImpl(); 
    }


	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//encoding 설정, contentType설정 요청경로파악
		log.info("보드의 서비스 함수 시작11111111111111111111111111111111111111111111111111111111");
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=UTF-8");
	
		//jsp에서 오는 요청 주소
		String uri = request.getRequestURI(); //  /brd/register
		String path = uri.substring(uri.lastIndexOf("/")+1); 
		log.info("path>>>>> "+path);
		log.info("switch_case문 바로 위");
		switch(path) {
		case "register":
			try {
				log.info("reister케이스시작");
				//목적지 주소 설정
				destPage = "/board/register.jsp";
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;//register끝
			
		case "insert":
			try {
				log.info("insert시작");
				String title = request.getParameter("title");
				String writer = request.getParameter("writer");
				String content = request.getParameter("contents");
				log.info(">>>insert check 1");
				BoardVO bvo = new BoardVO(title, writer, content);
				log.info("bvo >>>>>" + bvo);
				isOk = bsv.add(bvo);
				log.info((isOk>0)?"OK":"Fail");
				
				destPage = "/index.jsp";
				log.info("인서트끝    isOK값은 " + isOk );
			} catch (Exception e) {
				log.info("인서트 에러");
				e.printStackTrace();
			}
			break;//insert끝
		
		case "list":
			try {
				log.info("단순리스트 옴");
				log.info("특별한 작업 안 할 예정");
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;//단순list끝
			
		case "pageList":
			try {
				//jsp에서 파라미터 받기
				PagingVO pgvo = new PagingVO();
				log.info("페이징전");
				if(request.getParameter("pageNo")!=null) {
					log.info("request.getParameter(\"pageNo\")가 있는 경우 페이징 진입");
					int pageNo = Integer.parseInt(request.getParameter("pageNo")); //정확히 pageNo가 맞아야함
					int qty = Integer.parseInt(request.getParameter("qty"));
					log.info("pageNo "+pageNo+ " qty " + qty);
					pgvo = new PagingVO(pageNo, qty); //값이 있으면
				}
				//검색어 받기
				pgvo.setType(request.getParameter("type"));//넘긴 키가 "type","typeee1234"등으로 이름 자체가 없어도 null로 됨 에러는 안남
				pgvo.setKeyword(request.getParameter("keyword")); //기역우는 .jsp에서 type kewyword라는 값으로 넘길 예정임
				log.info("type : " + pgvo.getType()+ " keyword : "+ pgvo.getKeyword());
				
				//PagingVO totalCount
				int totalCount = bsv.getTotalCount(pgvo); //DB에 전체 카운트 요청
				log.info("전체 게시글 수" + totalCount);
				//bsv pgvo주고 ,limit적용한 row10개 가져오기.
				List<BoardVO> list = bsv.getPageList(pgvo);
				log.info("pagestart "+pgvo.getPageStart()); //DB에서 조회할 시작 row인덱스 구하기
				request.setAttribute("list", list);
				//페이지 정보를 list.jsp로 보내기
				log.info("ph = new PagingHandler("+ pgvo + "," + totalCount + " )" );
				PagingHandler ph = new PagingHandler(pgvo, totalCount);
				log.info("ph는 페이지 하단 번호를 관리하는 부분이고 값은 " + ph +" 입니다.");
				request.setAttribute("ph", ph);
				log.info("paging 성공~!!");
				destPage="/board/list.jsp";
			} catch (Exception e) {
				log.info("pageList 에러");
				e.printStackTrace();
			}
			break;
			
		case "detail" :
			try {
				
			} catch (Exception e) {
				log.info("detail 에러");
				e.printStackTrace();
			}
			break;
			
		case "modify" :
			try {
				
			} catch (Exception e) {
				log.info("modify 에러");
				e.printStackTrace();
			}
			break;
			
		case "edit" :
			try {
				
			} catch (Exception e) {
				log.info("edit 에러");
				e.printStackTrace();
			}
			break;
		
		case "remove" :
			try {
				
			} catch (Exception e) {
				log.info("remove 에러");
				e.printStackTrace();
			}
			break;
		
		}//switch case문끝///////////
		
		//.jsp파일이 아니라면 다시 컨트롤러에 case문이 있는거 찾아서 case문 실행
		rdp = request.getRequestDispatcher(destPage);
		rdp.forward(request,response); //대충 위에꺼랑 한세트
		
		log.info("서비스 함수 끝2222222222222222222222222222222222222222222222222222222222222222");
	} //service()의 끝
	



	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		service(request,response);
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		service(request,response);
	}

}
