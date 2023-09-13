package controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import domain.MemberVO;
import service.MemberService;
import service.MemberServiceImpl;

@WebServlet("/mem/*")
public class MemberController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger log = LoggerFactory.getLogger(MemberController.class);
	private RequestDispatcher rdp;
	private String destPage;
	private int isOk;
	
	private MemberService msv; //인터페이스 생성
	
       
    public MemberController() {
        msv = new MemberServiceImpl();
    }

    
    protected void service(HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException{
    	log.info("컨트롤러의 서비스 함수 시작");
    	request.setCharacterEncoding("utf-8");
    	response.setCharacterEncoding("utf-8");
    	response.setContentType("text/html; chartset=UTF-8");
    	
    	String uri = request.getRequestURI();
    	String path = uri.substring(uri.lastIndexOf("/")+1);
    	log.info("path>>>"+path);
    
    	switch(path) {
    	case "join":
    		log.info("회원가입 페이지 열기");
    		destPage = "/member/join.jsp";
    		break;
    	
    	
    	case "register":
    		try {
				String id = request.getParameter("id");
				String pwd = request.getParameter("pwd");
				String email = request.getParameter("email");
				int age = Integer.parseInt(request.getParameter("age"));
				MemberVO mvo = new MemberVO(id, pwd, email, age);
				log.info(">>>join>check1"+mvo);
				isOk = msv.register(mvo);
				log.info("check4"+((isOk>0)?"OK":"Fail"));
				destPage = "/index.jsp";
				
			} catch (Exception e) {
				e.printStackTrace();
			}
    		break;
    	
    	case "login":
    		try {
				//파라미터가 DB에 있는지 확인
    			//id pw 일치하는 데이터 가져오기
    			//session: 모든 jsp페이지에 공유되는 데이터
    			//아이디가 없다면 메세지보내서 alert창
    			String id = request.getParameter("id");
    			String pwd = request.getParameter("pwd");
    			MemberVO mvo = new MemberVO(id, pwd);
    			
    			MemberVO loginmvo = msv.login(mvo); // login관련 메퍼는 * 로 모든 정보를 가져온다. 나중에 수정할때 등등 다 뽑아 슬수 있따.
    			log.info("login check 1 >>>" + loginmvo);
    			//가져온 데이터를 세션에 저장
    			//세션가져오기
    			if(loginmvo != null) {
    				//연결된 세션이 있다면 기존의 세션 가져오기, 없으면 새로 생성
    				HttpSession ses = request.getSession(); // (세션에 넣으면 다른 케이스에서도 잡아넴)
    				ses.setAttribute("ses", loginmvo);// 다른케이스문의 jsp라도... 이후?에서 ses하면 내가 로그인 한 세션의 정보(모든정보)이라 생각하면 됨 (키,밸류)
    				ses.setMaxInactiveInterval(10*60); //로그인 유지시간(초단위) 10분
    			}else {
    				request.setAttribute("msg_login", 0); //맵형식, (키 밸류) 값
    			}
    			destPage = "/index.jsp";
			} catch (Exception e) {
				e.printStackTrace();
			}
    		break;
    		
    	case "logout":
    		try {
				//연결된 세션이 있다면 해당 세션을 가져오기
    			HttpSession ses = request.getSession(); //로그인한 세션
    			//lastlogin 시간 기록, id가 필요
    			//session에서 id가져오기
    			MemberVO mvo = (MemberVO)ses.getAttribute("ses");
    			String id = mvo.getId();
    			log.info("ses에서 id 추출 >>> " +id);
    			isOk = msv.lastLogin(id); //아직 세션 안끝음 마지막 로그인 시간 기록업데이트
    			ses.invalidate();//세션 끝음
    			log.info("logout >>" + (isOk>0 ? "OK":"Fail"));
    			destPage="/index.jsp";
			} catch (Exception e) {
				e.printStackTrace();
			}
    		break;
    		
    	case "list":
    		try {
    			List<MemberVO> list = msv.getList();
    			request.setAttribute("list", list);
    			destPage="/member/list.jsp";
    		}catch (Exception e) {
				e.printStackTrace();
			}
    		break;
    	
    	case "modify":
    		try {
				destPage="/member/modify.jsp";
			} catch (Exception e) {
				e.printStackTrace();
			}
    		break;
    	
    	case "update":
    		//jsp파라미터 가져와서 mvo 객체 생성
    		try {
				log.info("컨트롤러의 update케이스 시작");
				String id = request.getParameter("id");//아읻
				String pwd = request.getParameter("pwd");
				String email = request.getParameter("email");
				int age = Integer.parseInt((request.getParameter("age")));
				
				MemberVO mvo = new MemberVO(id, pwd, email, age);
				
				//msv에게 수정요청 -> mdao 수정요청 -> mapper 수정요청
				log.info("mvo는 "+mvo);
				isOk = msv.updateForEdit(mvo);
				log.info((isOk>0)?"OK":"Fail");
				
				log.info("update check 4 " + ( isOk>0 ? " ok" : "fail" )   );
				destPage="logout";
			} catch (Exception e) {
				e.printStackTrace();
			}
    		break;
    		
    	case "remove" :
    		try {
				//세션에 저장된 id만 가져오기
    			HttpSession ses = request.getSession(); //선언해서   리퀘스트에 세션이 있는지 확인하고   잇으면 가져오고    없으면 생성할것임    
    			MemberVO mvo = (MemberVO)ses.getAttribute("ses"); //현재 로그인 된 사용자정보를 가져와서 mvo에 넣어줌
    			String id = mvo.getId();
    			log.info("삭제할 id 추출"+id);
    			isOk = msv.remove(id); //아직 세션 안끝음
    			
    			//세션 끊고 index로 이동
    			ses.invalidate();//세션끝음
    			log.info("logout>>" + (isOk>0?"ok":"Fail"));
    			destPage = "/index.jsp";
    		} catch (Exception e) {
				e.printStackTrace();
			}
    		break;
    		
    		
    	}//스위치케이스문끝
    	rdp = request.getRequestDispatcher(destPage);
    	rdp.forward(request, response);
    }//서비스함수끝
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		service(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		service(request, response);
	}

}
