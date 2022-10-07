package kr.ac.kopo.kopo18.spring.board.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import kr.ac.kopo.kopo18.spring.board.domain.Comment;
import kr.ac.kopo.kopo18.spring.board.dto.Pagination;

public interface CommentService {
	
	// C
	Comment save(Comment comment);
	
	// R
	List<Comment> findAllByOrderByIdDesc(PageRequest pageable);
	List<Comment> findAllByPostingIdOrderById(Long postingId, int currentPage, int countPerPage);
	List<Comment> findAllByUserIdOrderById(Long userId);
//	List<Posting> findAll(PageRequest pageable);
	Comment findOneById(Long commentId);
	long count();
	Long countByPostingId(Long postingId);
	Long countByUserId(Long userId);
	
	// U
	
	// D --> User와 연결 끊고 삭제하는 method 구현 (@ PostingServiceImpl)
	@Transactional
	void deleteOneById(Long id);
	
	// Pagination
	Page<Comment> findAll(Pageable pageable);
	Pagination getPagination(int currentPage, int countPerPage, int pageSize, Long totalCount);

}
