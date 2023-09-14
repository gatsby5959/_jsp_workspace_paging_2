package service;

import java.util.List;

import domain.BoardVO;
import domain.PagingVO;

public interface BoardService {

	int add(BoardVO bvo);

	int getTotalCount(PagingVO pgvo);

	List<BoardVO> getPageList(PagingVO pgvo);

	BoardVO detailview(int bno);

	BoardVO getDetailForModi(int bno);

	int modifyForEdit(BoardVO bvo);

	int remove(int bno);

	int hitcount(int bno); //

}
