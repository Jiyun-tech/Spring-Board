package kr.ac.kopo.kopo18.spring.board.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import kr.ac.kopo.kopo18.spring.board.domain.Comment;
import kr.ac.kopo.kopo18.spring.board.domain.Posting;
import kr.ac.kopo.kopo18.spring.board.domain.User;
import kr.ac.kopo.kopo18.spring.board.dto.Pagination;
import kr.ac.kopo.kopo18.spring.board.repository.CommentRepository;
import kr.ac.kopo.kopo18.spring.board.repository.PostingRepository;
import kr.ac.kopo.kopo18.spring.board.repository.UserRepository;

@Service
public class CommentServiceImpl implements CommentService {
	
	@Autowired
	private CommentRepository commentRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PostingRepository postingRepository;

	@Override
	public Comment save(Comment comment) {
		return commentRepository.save(comment);
	}

	@Override
	public List<Comment> findAllByOrderByIdDesc(PageRequest pageable) {
		return commentRepository.findAllByOrderByIdDesc(pageable);
	}
	
	@Override
	public List<Comment> findAllByPostingIdOrderById(Long postingId, int currentPage, int countPerPage) {
		PageRequest pageRequest = PageRequest.of(currentPage-1, countPerPage);
		return commentRepository.findAllByPostingIdOrderById(postingId, pageRequest);
	}
	
	@Override
	public List<Comment> findAllByUserIdOrderById(Long userId) {
		return commentRepository.findAllByUserIdOrderById(userId);
	}

	@Override
	public Comment findOneById(Long commentId) {
		return commentRepository.findOneById(commentId);
	}

	@Override
	public long count() {
		return commentRepository.count();
	}
	
	@Override
	public Long countByPostingId(Long postingId) {
		return commentRepository.countByPostingId(postingId);
	}
	
	@Override
	public Long countByUserId(Long userId) {
		return commentRepository.countByUserId(userId);
	}

	@Override
	public void deleteOneById(Long commentId) {
		Comment comment = commentRepository.findOneById(commentId);
		User user = userRepository.findOneById(comment.getUser().getId());
		Posting posting = postingRepository.findOneById(comment.getPosting().getId());
		
		comment.setUser(null);
		comment.setPosting(null);
		
		user.getComments().remove(comment);
		posting.getComments().remove(comment);
		
		userRepository.save(user);
		postingRepository.save(posting);
	}

	@Override
	public Page<Comment> findAll(Pageable pageable) {
		return commentRepository.findAll(pageable);
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

}
