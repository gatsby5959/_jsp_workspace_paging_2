package service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dao.BoardDAO;
import dao.BoardDAOImpl;
import domain.BoardVO;
import domain.PagingVO;

public class BoardServiceImpl implements BoardService {

	//로그 객체 선언
	private static final Logger log = LoggerFactory.getLogger(BoardServiceImpl.class);
	//DAO객체 생성
	private BoardDAO bdao; //interface
	
	public BoardServiceImpl() {
		bdao = new BoardDAOImpl(); //dao -> class
	}
	
	@Override
	public int add(BoardVO bvo) {
		log.info("service add check 2");
		log.info("bvo는 "+bvo.toString());
		return bdao.insert(bvo);
	}

	@Override
	public int getTotalCount(PagingVO pgvo) {
		log.info("getTotalCount 2");
		return bdao.getTotalCount(pgvo);
	}

	@Override
	public List<BoardVO> getPageList(PagingVO pgvo) {
		log.info("getPageList 2");
		return bdao.getPageList(pgvo);
	}

}
