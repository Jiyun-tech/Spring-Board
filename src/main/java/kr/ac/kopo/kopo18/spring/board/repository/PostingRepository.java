package kr.ac.kopo.kopo18.spring.board.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.transaction.annotation.Transactional;

import kr.ac.kopo.kopo18.spring.board.domain.Posting;

public interface PostingRepository extends JpaRepository<Posting, Long>
											, JpaSpecificationExecutor<Posting> {
	
	// C
	Posting save(Posting posting);
	
	// R
	List<Posting> findAllByOrderByIdDesc(PageRequest pageaRequest);
//	List<Posting> findAll(PageRequest pageable);
	List<Posting> findAllByUserIdOrderById(Long userId);
	List<Posting> findAllByOriginalPostingId(Long originalPostingId);
	
//	@Query("select p from Posting p where p.original_posting_id = :originalPostingId and p.re_count >= :currentReCount")
//	List<Posting> getPostings(Long originalPostingId, Long currentReCount);

	Posting findOneById(Long postingId);
	long count();
	Long countByUserId(Long userId);
	
	// U
	
	// D --> User와 연결 끊고 삭제하는 method 구현 (@ PostingServiceImpl)
	@Transactional
	void deleteOneById(Long id);
	
	// React
	Page<Posting> findAll(Pageable pageable);
	
}
