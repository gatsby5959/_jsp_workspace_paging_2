package dao;

import java.util.List;

import domain.BoardVO;
import domain.PagingVO;

public interface BoardDAO {

	int insert(BoardVO bvo);

	int getTotalCount(PagingVO pgvo);

	List<BoardVO> getPageList(PagingVO pgvo);

	BoardVO selectOne(int bno);

	int update(BoardVO bvo);

	int delete(int bno);

	int hitcount(int bno);

	String getFileName(int bno);
	

}
