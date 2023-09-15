package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import domain.CommentVO;
import service.CommentService;
import service.CommentServiceImpl;

@WebServlet("/cmt/*")
public class CommentController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger log = LoggerFactory.getLogger(CommentController.class);
	//비동기 방식 =>   페이지 이동방식 X   =>   //destPage X requestdispahcer X
	private CommentService csv;
	private int isOk;
	
    public CommentController() {
    	csv = new CommentServiceImpl();
     
    }

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		//response의 setContentType 설정은 하지 않음.  이거쓰면 제이슨파일은 안들어옴
		
		String uri = request.getRequestURI(); //
		// /brd/detail?bno=1;   => 동기방식의 주소체계
		// 동기방식에서는 get, post 2가지 방식
		
		// /cmt/list/10 ,  /cmt/post   /cmt/update => RestAPI방식(비동기에서 많이사용)
		// get은 리스트를 보여줄 때 사용, post는 등록할때 사용 , put => update할때, delete=>delete할때 사용
		// 스프링일떄는 위내용을 맞추지만 지금은 그냥 겟과 포스트 방식만 쓸것임
		
		String pathUri = uri.substring("/cmt/".length()); //post, list/10 일수도 있고
		String path = pathUri;  
		String pathVar = ""; //없으면 공백처리함
		if(pathUri.contains("/")) { //패스값을 달고 왔다면...
			path = pathUri.substring(0,pathUri.lastIndexOf("/")); //list
			pathVar = pathUri.substring(pathUri.lastIndexOf("/")+1); //10
		}
		
		log.info(">>> uri > " + uri);
		log.info(">>> pathUri > " + pathUri);
		log.info(">>> path > " + path);
		log.info(">>> pathVar > " + pathVar);
		
		switch(path) {
		case "post":
			try {
				//JSON 방식으로 화면에서 보낸 데이터를 받을 준비
				//String형태로 값을 받아 객체로 변환 
				//json-simple-1.1.1 라이브러리를 사용하여
				//Json 형태의 스트링을 객체 형태로 변환(해서 디비에 담을꺼에요)
				StringBuffer sb = new StringBuffer(); //String누적 객체
				//append
				String line ="";
				BufferedReader br = request.getReader(); //cmtData를 읽어오는 객체
				while((line=br.readLine())!=null) { //널이 아닐떄까지 읽어라
					sb.append(line);
				}
				log.info(">>>>>>sb : " + sb.toString());
				
				//객체로 변환
				JSONParser parser = new JSONParser();
				JSONObject jsonObj = (JSONObject)parser.parse(sb.toString());
				
				//CommentVO 형태로 변환
				int bno = Integer.parseInt(jsonObj.get("bno").toString());
				String writer = jsonObj.get("writer").toString();
				String content = jsonObj.get("content").toString();
				
				//csv DB로 저장
				CommentVO cvo = new CommentVO(bno, writer, content);
				log.info(">>> cvo "+ cvo);
				isOk = csv.post(cvo);
				log.info((isOk>0)?"OK":"Fail");
				
				//화면에 출력
				PrintWriter out = response.getWriter();
				out.print(isOk);
				
				
			}catch(Exception e) {
				log.info(">> Comment > post > error ");
				e.printStackTrace();
			}
			break;
		}
		
	}


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		service(request, response);
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		service(request, response);
	}

}
