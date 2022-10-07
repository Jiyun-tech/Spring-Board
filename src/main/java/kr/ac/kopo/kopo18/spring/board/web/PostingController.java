package kr.ac.kopo.kopo18.spring.board.web;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import kr.ac.kopo.kopo18.spring.board.domain.Comment;
import kr.ac.kopo.kopo18.spring.board.domain.Posting;
import kr.ac.kopo.kopo18.spring.board.domain.User;
import kr.ac.kopo.kopo18.spring.board.dto.Pagination;
import kr.ac.kopo.kopo18.spring.board.service.CommentService;
import kr.ac.kopo.kopo18.spring.board.service.PostingService;
import kr.ac.kopo.kopo18.spring.board.service.UserService;

@Controller
@Transactional
public class PostingController {
	
	@Autowired
	private PostingService postingService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CommentService commentService;	
	
//	@GetMapping(value = "viewBoard") // 원래 controller (답글 전)
//	public String viewBoard(Model model, 
//									@RequestParam(value="page", defaultValue="1") int page,
//									@RequestParam(value="countPerPage", defaultValue="10") int countPerPage,
//									@RequestParam(value="pageSize", defaultValue="5") int pageSize) {
//		List<Posting> postings = postingService.findAll(page, countPerPage);
//		Long countAll = postingService.count();
//		
//		Pagination p = postingService.getPagination(page, countPerPage, pageSize, countAll);
//		
//		model.addAttribute("postings", postings);
//		model.addAttribute("countAll", countAll);
//		model.addAttribute("get_pagination", p);
//
//		return "viewBoard";
//	}
	
	@GetMapping(value = "viewBoard")
	public String viewBoard(Model model, 
									@PageableDefault(page = 0, size = 10) @SortDefault.SortDefaults({
										@SortDefault(sort = "originalPostingId", direction = Sort.Direction.DESC),
										@SortDefault(sort = "reCount")
									}) Pageable pageable,
									@RequestParam(value="pageSize", defaultValue="5") int pageSize) {
			
		Page<Posting> postings = postingService.findWithPagination(pageable);
		Long countAll = postingService.count();
		
		Pagination p = postingService.getPagination(pageable.getPageNumber()+1, pageable.getPageSize(), pageSize, countAll);
		
		Date date = new Date();
		SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd");
		String today = fm.format(date);
		
		model.addAttribute("postings", postings.getContent());
		model.addAttribute("countAll", countAll);
		model.addAttribute("get_pagination", p);
		model.addAttribute("today", today);
		model.addAttribute("page",pageable.getPageNumber());

		return "viewBoard";
	}

	
	// "글쓰기" 버튼 클릭 시 -> 글쓰기 페이지로 이동
	@GetMapping(value = "goToCreatePosting")
	public String goToCreatePosting(Model model,
					@RequestParam(value="originalPostingId", defaultValue="0") Long originalPostingId,
					@RequestParam(value="page", defaultValue="0") Long page) {
		
		Posting oPosting = null;
		
		if (originalPostingId > 0 ) {
			oPosting = postingService.findOnebyId(originalPostingId);
			model.addAttribute("originalPosting", oPosting);
		}
		
		model.addAttribute("originalPostingId", originalPostingId);
		model.addAttribute("page", page);
		
		return "createPosting";
	}
	
	// "등록" 버튼 클릭 시 -> 등록 
	@PostMapping(value = "createPosting")
	public String createPosting(Model model, @RequestParam(value = "userName", defaultValue="") String userName,
											@RequestParam(value = "password", defaultValue="") String password,
											@RequestParam(value = "title", defaultValue="") String title,
											@RequestParam(value = "content", defaultValue="") String content,
											@RequestParam(value = "originalPostingId", defaultValue="0") Long originalPostingId,
											@RequestParam(value="page", defaultValue="1") Long page) {
		User user = userService.findOneByUserName(userName);

		model.addAttribute("userName", userName);
		model.addAttribute("title", title);
		model.addAttribute("content", content);
		
		if (userName.equals("") || password.equals("") || title.equals("") || content.equals("")) {
			return "createPosting";
		} else if ( user == null ) {
			model.addAttribute("userName", null);
			return "createPosting";
		} else if ( !user.getPassword().equals(password)) {
			return "createPosting";
		} else {
			Date date = new Date();
			Posting posting = new Posting();
			posting.setTitle(title);
			posting.setContent(content);
			posting.setCreateDate(date);
//			posting.setUpdateDate(date);
			posting.setUser(user);
			
			if (originalPostingId == 0) {
				posting.setLevel(0L);
				posting.setReCount(0L);
			} else {
				Posting oPosting = postingService.findOnebyId(originalPostingId);
				posting.setLevel(oPosting.getLevel() + 1);
				posting.setReCount(oPosting.getReCount() + 1);
				if (oPosting.getLevel() == 0L) {
					posting.setOriginalPostingId(originalPostingId);
					posting.setUpperPostingId(originalPostingId);
				} else {
					posting.setOriginalPostingId(oPosting.getOriginalPostingId());
					posting.setUpperPostingId(originalPostingId);
					
				}
				
				
				// ==> ReCount!!!
				Collection<Posting> postingList = postingService.findAllByOriginalPostingId(posting.getOriginalPostingId());
				Stream<Posting> postingStream = postingList.stream().filter(p -> p.getReCount() >= posting.getReCount());
				postingStream.forEach(p -> {p.setReCount(p.getReCount() + 1);
											postingService.save(p);});
			}
				
			Posting savedPosting = postingService.save(posting); // DB에 저장
			
			// Original Posting(새 글) 인 경우
			if (savedPosting.getOriginalPostingId() == null) {
				savedPosting.setOriginalPostingId(savedPosting.getId());
				savedPosting.setViewCount(0L);
				postingService.save(savedPosting);
			} 

			model.addAttribute("function","done");
			model.addAttribute("page", page);
			
			return "redirect:/viewBoard?page="+page;
		}
	}

	// R : 글 읽기
	@GetMapping(value = "viewPosting")
	public String viewPosting(Model model, @RequestParam(value="posting_id") Long posting_id,
											@RequestParam(value="page", defaultValue="0") Long page,
											@RequestParam(value = "updatedComment_id", defaultValue="-1") Long updatedComment_id,
											@RequestParam(value = "comment_page", defaultValue="0") int comment_page,
											@RequestParam(value = "comment_countPerPage", defaultValue="10") int comment_countPerPage,
											@RequestParam(value = "comment_pageSize", defaultValue="5") int comment_pageSize) {
		Posting posting = postingService.findOnebyId(posting_id);
		User user = posting.getUser();
		
		Long viewCount = posting.getViewCount();
		posting.setViewCount(viewCount+1);
		postingService.save(posting);
		
		Long comment_countAll = commentService.countByPostingId(posting_id);
		
		if (comment_page < 1) {
			if ( comment_countAll.intValue() % comment_countPerPage == 0) {
				comment_page = comment_countAll.intValue() / comment_countPerPage;
			} else {
				comment_page = comment_countAll.intValue() / comment_countPerPage + 1;
			}
			
			if (comment_page == 0) {
				comment_page = 1;
			}
		} 
		
		
		// comment 받아오기
		List<Comment> comments = commentService.findAllByPostingIdOrderById(posting_id, comment_page, comment_countPerPage);
		
		Pagination p = commentService.getPagination(comment_page, comment_countPerPage, comment_pageSize, comment_countAll);
		
		
		model.addAttribute("posting", posting);
		model.addAttribute("user", user);
		model.addAttribute("page", page);
		model.addAttribute("comments", comments);
		model.addAttribute("updatedComment_id", updatedComment_id);
		model.addAttribute("get_pagination", p);
		return "viewPosting";
	}
	
	// U : 글 수정 -> 수정 페이지로 이동
	@GetMapping(value = "updatePosting")
	public String updatePosting(Model model, @RequestParam(value="posting_id") Long posting_id,
											@RequestParam(value="page", defaultValue="0") Long page) {
		Posting posting = postingService.findOnebyId(posting_id);
		User user = posting.getUser();
		
		model.addAttribute("posting", posting);
		model.addAttribute("user", user);
		model.addAttribute("page", page);
		return "updatePosting";
	}
	
	// U : 글 수정 -> 수정 완료
	@PostMapping(value = "updatePosting")
	public String updatePosting(Model model, @RequestParam(value = "posting_id") Long posting_id,
											@RequestParam(value="page", defaultValue="0") Long page,
											@RequestParam(value = "userName", defaultValue="") String userName,
											@RequestParam(value = "password", defaultValue="") String password,
											@RequestParam(value = "title", defaultValue="") String title,
											@RequestParam(value = "content", defaultValue="") String content) {
		Posting posting = postingService.findOnebyId(posting_id);
		User user = posting.getUser();
		
		model.addAttribute("userName", userName);
		model.addAttribute("title", title);
		model.addAttribute("content", content);
		
		if (password.equals("") || title.equals("") || content.equals("")) {
			return "redirect:/updatePosting?posting_id="+posting_id+"&page="+page;
		} else if ( !user.getPassword().equals(password)) {
			return "redirect:/updatePosting?posting_id="+posting_id+"&page="+page;
		} else {
			Date date = new Date();
			posting.setTitle(title);
			posting.setContent(content);
			posting.setUpdateDate(date);
			postingService.save(posting);
						
			return "redirect:/viewPosting?posting_id="+posting_id+"&page="+page;
		}
	}
	
	
	// D : 글 삭제 --> 삭제 페이지로 이동
	@GetMapping(value = "deletePosting")
	public String deletePosting(Model model, @RequestParam(value="posting_id") Long posting_id,
											@RequestParam(value="page", defaultValue="0") Long page) {
		Posting posting = postingService.findOnebyId(posting_id);
		User user = posting.getUser();
		
		model.addAttribute("posting", posting);
		model.addAttribute("user", user);
		return "deletePosting";
	}
	
	// D : 글 삭제 --> 삭제 실행
	@PostMapping(value = "deletePosting")
	public String deletePosting2(Model model, @RequestParam(value="posting_id") Long posting_id,
											@RequestParam(value="password") String password,
											@RequestParam(value="page", defaultValue="0") Long page) {
		Posting posting = postingService.findOnebyId(posting_id);
		User user = posting.getUser();
		
		if (user.getPassword().equals(password)) {
			postingService.deleteById(user.getId(), posting_id);
			return "redirect:/viewBoard?page="+page;
		} else {
			return "redirect:/deletePosting?posting_id="+posting_id;
		}
	}
	

}
