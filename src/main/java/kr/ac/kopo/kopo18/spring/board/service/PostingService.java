package kr.ac.kopo.kopo18.spring.board.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import kr.ac.kopo.kopo18.spring.board.domain.Posting;
import kr.ac.kopo.kopo18.spring.board.dto.Pagination;

public interface PostingService {
	
	// C
	Posting save(Posting posting);
	
	// R
	Posting findOnebyId(Long id);
	List<Posting> findAll(int currentPage, int countPerPage);
	List<Posting> findAllByUserIdOrderById(Long userId);
	long count();
	Long countByUserId(Long userId);
	Page<Posting> findWithPagination(Pageable pageable);
	
	// U
	
	// D
	void deleteById(Long userId, Long postingId);
	
	// Pagination
	Pagination getPagination(int currentPage, int countPerPage, int pageSize, Long countAll);
	
	// ReCount (답글 정렬)
	List<Posting> findAllByOriginalPostingId(Long originalPostingId);
	
	

}
