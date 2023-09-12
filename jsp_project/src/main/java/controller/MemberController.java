package controller;

import java.io.IOException;

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
    			
    			MemberVO loginmvo = msv.login(mvo);
    			log.info("login check 1 >>>" + loginmvo);
    			//가져온 데이터를 세션에 저장
    			//세션가져오기
    			if(loginmov != null) {
    				//연결된 세션이 있다면 기존의 세션 가져오기, 없으면 새로 생성
    				HttpSession ses = request.getSession();
    				ses.setAttribute("ses", loginmvo);// 뒤?에서 ses하면 내가 로그인 한 세션이라 생각하면 됨 (키,밸류)
    				
    			}
			} catch (Exception e) {
				// TODO: handle exception
			}
    		break;
    		
    	}//스위치문끝
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
