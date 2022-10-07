package kr.ac.kopo.kopo18.spring.board.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.transaction.annotation.Transactional;

import kr.ac.kopo.kopo18.spring.board.domain.Comment;


public interface CommentRepository extends JpaRepository<Comment, Long>, 
											JpaSpecificationExecutor<Comment>{
	
	// C
	Comment save(Comment comment);
	
	// R
	List<Comment> findAllByOrderByIdDesc(PageRequest pageable);
	List<Comment> findAllByPostingIdOrderById(Long postingId, PageRequest pageRequest);
	List<Comment> findAllByUserIdOrderById(Long userId);
	Long countByPostingId(Long postingId);
	Long countByUserId(Long userId);
	Comment findOneById(Long commentId);
	long count();
	
	// U
	
	// D --> User와 연결 끊고 삭제하는 method 구현 (@ PostingServiceImpl)
	@Transactional
	void deleteOneById(Long id);
	
	// Pagination
	Page<Comment> findAll(Pageable pageable);

}
