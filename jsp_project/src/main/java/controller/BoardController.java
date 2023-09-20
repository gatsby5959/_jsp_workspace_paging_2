package controller;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
//import org.apache.catalina.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import domain.BoardVO;
import domain.PagingVO;
import handler.FileHandler;
import handler.PagingHandler;
import net.coobird.thumbnailator.Thumbnails;
import service.BoardService;
import service.BoardServiceImpl;
import service.CommentService;


@WebServlet("/brd/*")
public class BoardController extends HttpServlet {
	private static final long serialVersionUID = 1L;
    //로그 객체 선언
	private static final Logger log = LoggerFactory.getLogger(BoardController.class);
	//requestDespatcher객체
	private RequestDispatcher rdp;
	//service interface
	private BoardService bsv;
	private CommentService csv;
//	private Service bsv2; //이건 안쓸듯한데 일단 박아봄 서버에 있어용 카탈리나 안씀 
	
	private String destPage; //destPage : 목적지 주소 저장변수
	//isOk
	private int isOk;
	
	private String savePath; // 파일경로를 저장할 변수
	

    public BoardController() {
       //bsv의 객체 연결
       bsv = new BoardServiceImpl(); 
    }


	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//encoding 설정, contentType설정 요청경로파악
		log.info("보드의 서비스 함수 시작11111111111111111111111111111111111111111111111111111111");
		request.setCharacterEncoding("utf-8");
//		log.info("에러확인용1");
		response.setCharacterEncoding("utf-8");
//		log.info("에러확인용2");
		response.setContentType("text/html; charset=UTF-8");
//		log.info("에러확인용3");
		//jsp에서 오는 요청 주소
		String uri = request.getRequestURI(); //  /brd/register
//		log.info("에러확인용4");
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

//		230919주석처리 이미지 때문에...			
//		case "insert":
//			try {
//				log.info("insert시작");
//				String title = request.getParameter("title");
//				String writer = request.getParameter("writer");
//				String content = request.getParameter("contents");
//				log.info(">>>insert check 1");
//				BoardVO bvo = new BoardVO(title, writer, content);
//				log.info("bvo >>>>>" + bvo);
//				isOk = bsv.add(bvo);
//				log.info((isOk>0)?"OK":"Fail");
//				
//				destPage = "/index.jsp";
//				log.info("인서트끝    isOK값은 " + isOk );
//			} catch (Exception e) {
//				log.info("인서트 에러");
//				e.printStackTrace();
//			}
//			break;//insert끝
		
			
			//D:\전경환\_HtmlCssJs\image
		case "insert":
			try {
				//파일을 업로드 할 물리적인 경로 설정(업로드 할때 설정)
				// D:\전경환\_jsp_workspace_paging\.metadata\.plugins\org.eclipse.wst.server.core\tmp0\wtpwebapps\jsp_project\_fileUpload\
				savePath = getServletContext().getRealPath("/_fileUpload"); //상대경로 가져오기
				File fileDir = new File(savePath);
				log.info("파일저장위치(savePath) : " + savePath);
				
				//파일 객체를 생성하기 위한 객체(파일에 대한 정보를 설정)
				DiskFileItemFactory fileItemFactory = new DiskFileItemFactory();
				fileItemFactory.setRepository(fileDir); //저장할 위치 set (file 객체로 지정)  (셋은 이렇게 해야함)
				fileItemFactory.setSizeThreshold(5*1024*1024); //저장을 위한 임시메모리 용량5메가 임시로 해놈 (이보다 크면 더 늘려줄꺼임) byte단위
				BoardVO bvo = new BoardVO();
				
				//multipart/form-data형식으로 넘어온 request 객체를 다루기 쉽게 변환해주는 객체형식으로 저장
				ServletFileUpload fileUpload = new ServletFileUpload(fileItemFactory);  //요기서  fileItemFactory 담아야함
				
				List<FileItem> itemList = fileUpload.parseRequest(request); //apach.common꺼 임포트하기 다 에러중
				//DB로 넘기기 위한 BoardVO객체로 변환(경로와 파일이름만...) . 이미지는 저장  이미지는 따로 파일 업로드 위치에 업로드 될것임
				
				//bvo로 변환? 할껴임
				//리스트에서 뺴올꺼임
				for(FileItem item : itemList) {
					switch(item.getFieldName()) {
					case "title":
						bvo.setTitle(item.getString("utf-8")); //인코딩 형식을 담아서 변환(한글때문에..)
						break;
					case "writer":
						bvo.setWriter(item.getString("utf-8"));
						break;
					case "content":
						bvo.setContent(item.getString("utf-8"));
						break;
					case "image_file":
						//이미지 저장 처리 필요
						//이미지가 필수X 없는 경우에도 처리
						//이미지가 있는지 체크
						if(item.getSize()>0) {	//데이터 크기가 있다면 이미지가 있는걸로 처리
							String fileName = item.getName()      		          //(가끔)경로를 포함해서 들어오는 케이스가 있음 그래서...
										.substring(item.getName().indexOf("/")+1);	//파일 이름만 분리 
							//시스템의 현재시간_파일이름.jpg
							fileName = System.currentTimeMillis()+"_"+fileName;
							
							//파일 객체 생성 : D:~/fileUpload/시간_cat2.jpg 
							//에러를 보면 아래 경로에 폴더 하나가 안보일거임 그냥 윈도우탐색기에서 그냥 만들기 (이클립스에서 가끔 못만들어줌)
							//D:\전경환\_jsp_workspace_paging\.metadata\.plugins\org.eclipse.wst.server.core\tmp0\wtpwebapps\jsp_project\_fileUpload\1695114911351_Aclass.jpg
							File uploadFilePath = new File(fileDir+File.separator+fileName);
							log.info("파일경로+이름      " + uploadFilePath);
							
							//저장
							try {
								item.write(uploadFilePath);	//자바 객체를 디스크에 쓰기
								bvo.setImage_File(fileName);
								
								//썸메일 작업 : 트래픽 과다사용 방지
								Thumbnails.of(uploadFilePath).size(60, 60)
									.toFile(new File(fileDir+File.separator+"_th_"+fileName));
							} catch (Exception e) {
								log.info(">>>>> file writer on disk 에러입니당") ;
								e.printStackTrace();
							}
						}
						break; //end of image_file
						
						
					} //end of switch
				} //end of for
				
				//DB에 bvo 저장요청
				isOk = bsv.add(bvo); //선생님은 register
//				log.info(">>>>>insert > "+(isOk>0)? "OK","Fail");
				log.info(">>>>>insert >> "+  ((isOk>0)?"OK":"Fail"));
				destPage = "pageList";
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
			
		
		case "list":
			try {
				log.info("case \"list\": 에 도착 그러나 특별한 작업 안 할 예정");
			
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
				log.info("디테일잘들어옴");
				
				int bno = Integer.parseInt(request.getParameter("bno"));
				BoardVO bvo = bsv.detailview(bno); // select * from board; 실행
				
//				bsv.hitcount(bno); //jgh230914 하나 팜
				
				request.setAttribute("bvo", bvo); // 리퀘스트에 키 벨류 로 저장함 나중에 detail.jsp에서 쓰려고...
				destPage = "/board/detail.jsp";
			} catch (Exception e) {
				log.info("detail 에러");
				e.printStackTrace();
			}
			break;
			
		case "modify" :
			try {
				log.info("modify 진입");
				int bno = Integer.parseInt(request.getParameter("bno"));
				BoardVO bvo = bsv.getDetailForModi(bno);
				request.setAttribute("bvo", bvo);
				destPage="/board/modify.jsp";
			} catch (Exception e) {
				log.info("modify 에러");
				e.printStackTrace();
			}
			break;
			
		case "edit" :
			try {
				//파일 저장 경로 설정
				savePath = getServletContext().getRealPath("/_fileUpload"); // 이경로로 들어가게끔 해주세요

				File fileDir = new File(savePath);
				//디스크에 기록할 파일 정보를 setting 하는 객체
				DiskFileItemFactory fileItemFactory = new DiskFileItemFactory();
				fileItemFactory.setRepository(fileDir); //저장 경로 설정
				fileItemFactory.setSizeThreshold(2*1024*1024); //임시저장용량 2메가 //
				
				BoardVO bvo = new BoardVO();
				
				ServletFileUpload fileUpload = new ServletFileUpload(fileItemFactory);
				log.info(">> update 준비 >");
				
				List<FileItem> itemList = fileUpload.parseRequest(request);
				
				String old_file = null; //수정하기전 원래 그림파일
				
				for(FileItem item : itemList) {
					switch(item.getFieldName()) {
					case "bno":
						bvo.setBno(Integer.parseInt(item.getString("utf-8")));
						break;
					case "title":
						bvo.setTitle(item.getString("utf-8"));
						break;
					case "content":
						bvo.setContent(item.getString("utf-8"));
						break;
					case "image_file":
						//수정 이전 파일
						old_file = item.getString("utf-8"); //여기 안타면 null
						break;
					case "new_file":
						//새로운 파일이 있는지 확인
						if(item.getSize()>0) {
							//기존 파일 삭제 (기존파일이 있을 경우우)
							if(old_file != null) {
								//기존 파일 삭제 (기존 파일이 있을 경우)
								FileHandler fileHandler = new FileHandler();
								isOk = fileHandler.deleteFile(old_file, savePath);
							}
							//new 파일의 경로와 파일명 (다시)생성
							String fileName = item.getName().substring(
									item.getName().lastIndexOf(File.separator)+1);	
							
							log.info("new_fileName"+fileName);
							
							//실제 저장될 파일 이름  시스템 현재 시간_파일이름.jpg
							fileName = System.currentTimeMillis()+"_"+fileName;
							
							//파일 객체 생성 : D:~
							File uploadFilePath = new File(fileDir+File.separator+fileName);
							//저장
							try {
								item.write(uploadFilePath);
								bvo.setImage_File(fileName);
								
								//썸내일 작업 : 트레픽 과다 사용 방지
								Thumbnails.of(uploadFilePath)
								.size(60, 60)
								.toFile(new File(fileDir+File.separator+"_th_"+fileName));
								
							} catch (Exception e) {
								log.info(">> new File save  error>");
								e.printStackTrace();
							}
						}else {//새로운 파일이 없다면... 기존 파일을 다시 담기
							bvo.setImage_File(old_file);
						}
						break;
					}//end ofswitch 
				}//end of for
				log.info("bvo는 " + bvo);
				isOk = bsv.modifyForEdit(bvo);
//				destPage = "detail?bno="+bno;
				destPage = "pageList"; // 중간에 대문자 조심
				//관련 사진 부분
//				D:\전경환\_jsp_workspace_paging\.metadata\.plugins\org.eclipse.wst.server.core\tmp0\wtpwebapps\jsp_project\_fileUpload
				
								
				
//				230919주석처리이하
//				log.info("edit 진입");
//				int bno = Integer.parseInt(request.getParameter("bno")); //modify.jsp에서 누르는 순간 /brd/edit로 보냄
//				String title = request.getParameter("title");
//				String content = request.getParameter("content");
//				
//				BoardVO bvo = new BoardVO(bno,title,content);
//				log.info("bvo는 " + bvo);
//				isOk = bsv.modifyForEdit(bvo);
//				log.info((isOk>0)?"OK":"Fail");
//				destPage = "detail?bno="+bno;
				
			} catch (Exception e) {
				log.info("edit 에러");
				e.printStackTrace();
			}
			break;
		
			//관련 사진 부분
//			D:\전경환\_jsp_workspace_paging\.metadata\.plugins\org.eclipse.wst.server.core\tmp0\wtpwebapps\jsp_project\_fileUpload
		case "remove" :
			try {
				log.info("리무브 들어옴");

				int bno = Integer.parseInt(request.getParameter("bno"));
				//삭제할 bno의 image_file Name을 불러오기
				String fileName = bsv.getFileName(bno);  //파일 이름 불러오기 이 다음 path설정해야함...
				//savePath 생성
				savePath = getServletContext().getRealPath("/_fileUpload");
				//파일 핸들러에서 삭제 요청
				FileHandler filehandler = new FileHandler();
				isOk = filehandler.deleteFile(fileName, savePath); // 삭제해줄꺼임  인트 리턴해줄꺼임
				log.info(   isOk>0? "file remove Ok" : "Fail"   );
				isOk = bsv.remove(bno);
				log.info(   isOk>0? "OK" : "Fail"   );
				destPage="pageList";
			} catch (Exception e) {
				log.info("remove 에러");
				e.printStackTrace();
			}
			break;
			
		case "count" :
			try {
				log.info("count 케이스 들어옴");
				int bno = Integer.parseInt(request.getParameter("bno"));

				bsv.hitcount(bno);
				
				destPage="/brd/detail";
			} catch (Exception e) {
				log.info("count 에러");
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
