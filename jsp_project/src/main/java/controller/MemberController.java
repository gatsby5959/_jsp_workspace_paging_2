package controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
