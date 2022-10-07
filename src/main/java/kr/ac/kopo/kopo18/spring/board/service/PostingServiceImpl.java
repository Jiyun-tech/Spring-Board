package kr.ac.kopo.kopo18.spring.board.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import kr.ac.kopo.kopo18.spring.board.domain.Posting;
import kr.ac.kopo.kopo18.spring.board.domain.User;
import kr.ac.kopo.kopo18.spring.board.dto.Pagination;
import kr.ac.kopo.kopo18.spring.board.repository.PostingRepository;
import kr.ac.kopo.kopo18.spring.board.repository.UserRepository;

@Service
public class PostingServiceImpl implements PostingService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PostingRepository postingRepository;

	// C
	@Override
	public Posting save(Posting posting) {
		postingRepository.save(posting);
		return posting;
	}
	
	// R
	// R(1) 글 한 개 조회
	@Override
	public Posting findOnebyId(Long postingId) {
		Posting posting = postingRepository.findOneById(postingId);
		return posting;
	}
	// R(2) 글 목록 조회
	@Override
	public List<Posting> findAll(int currentPage, int countPerPage) {
		PageRequest pageaRequest = PageRequest.of(currentPage-1, countPerPage);
		List<Posting> postings = postingRepository.findAllByOrderByIdDesc(pageaRequest);
//		List<Posting> postings = postingRepository.getPostings(pageable);
		return postings;
	}
	
	@Override
	public List<Posting> findAllByUserIdOrderById(Long userId) {
		// TODO Auto-generated method stub
		return postingRepository.findAllByUserIdOrderById(userId);
	}
	
	// R(3) Pagination & 글 목록 조회
	@Override
	public Page<Posting> findWithPagination(Pageable pageable) {
		return postingRepository.findAll(pageable);
	}
	

	@Override
	public long count() {
		return postingRepository.count();
	}
	
	@Override
	public Long countByUserId(Long userId) {
		return postingRepository.countByUserId(userId);
	}
	
	
	// U
	
	// D : Delete Posting By postingId
	@Override
	public void deleteById(Long userId, Long postingId) {
		User user = userRepository.findOneById(userId);				// 부모
		Posting posting = postingRepository.findOneById(postingId);	// 자식
		
		posting.setUser(null);						// 자식-부모 연결 해제
		
		user.getPostings().remove(posting);			// 부모를 통해 타고 들어가 자식 삭제
		
		userRepository.save(user);					// 수정된 부모 저장
	}
	

	// 페이지
	@Override
	public Pagination getPagination(int currentPage, int countPerPage, int pageSize, Long totalCount) {
		/// (1) ppPage (fistPage)
		Long ppPage = 1L;
		
		// (2) nnPage (lastPage)
		Long nnPage = 1L;
		if (totalCount > 0 && countPerPage > 0) {
			if (totalCount%countPerPage == 0) {
				nnPage = totalCount/countPerPage;
			} else {
				nnPage = totalCount/countPerPage + 1;
			}
		} 
		
		// (3) CurrentPage 예외 처리
		Long cPage = 1L;
		if (currentPage < 1) {
			cPage = 1L;
		} else if (currentPage > nnPage) {
			cPage = nnPage;
		} else {
			cPage = (long)currentPage;
		}
		
		// (4) pPage
		Long pPage = 1L;
		if (pageSize > 0) {
			if (cPage <= pageSize) {
				pPage = 1L;
			} else {
				pPage = (cPage / pageSize) * pageSize - (pageSize-1);
			}
		} else { // pageSize가 0보다 작은 경우
			pPage = 1L;
		}
		
		// (5) nPage
		Long nPage = 1L;
		if (pageSize > 0) {
			if ( cPage >= (((nnPage- 1)/pageSize) * pageSize + 1 ) ) {
				nPage = nnPage;
			} else {
				nPage = ((cPage - 1)/pageSize + 1) * pageSize + 1;
			}
		} else { // pageSize가 0보다 작거나 같은 경우, nPage 1로 설정.
			nPage = 1L;
		}
				
		// (6) sList (목록번호 인쇄 시작)
		Long sList = 1L;
		if (pageSize > 0) {
			sList = ((cPage-1)/pageSize)*pageSize + 1;
		} else {
			sList = 1L;
		}
				
		// (7) eList (목록번호 인쇄 종료)
		Long eList = 1L;
		if (pageSize > 0) {
			eList = sList + pageSize - 1;
		}
		if (eList > nnPage) {
			eList = nnPage;
		}
		
		Pagination p = new Pagination();
		p.setPpPage(ppPage);
		p.setpPage(pPage);
		p.setpage(cPage);
		p.setnPage(nPage);
		p.setNnPage(nnPage);
		p.setsList(sList);
		p.seteList(eList);
		p.setCountPerPage((long)countPerPage);
		p.setPageSize((long)pageSize);
		
		return p;	
	}

	// for ReCount (답글 정렬)
	@Override
	public List<Posting> findAllByOriginalPostingId(Long originalPostingId) {
		// TODO Auto-generated method stub
		return postingRepository.findAllByOriginalPostingId(originalPostingId);
	}

}
