// REACT 연결

package kr.ac.kopo.kopo18.spring.board.web;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.ac.kopo.kopo18.spring.board.domain.Posting;
import kr.ac.kopo.kopo18.spring.board.service.CommentService;
import kr.ac.kopo.kopo18.spring.board.service.PostingService;
import kr.ac.kopo.kopo18.spring.board.service.UserService;

@RestController
@RequestMapping("postapi")
public class ReactPostingController {
	
	@Autowired
	PostingService postingService;

	@Autowired
	UserService userService;
	
	@Autowired
	CommentService commentService;
	
	// 1. Find Postings for the page
	@GetMapping(value = "/board", produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<Page<Posting>> getPosts(@PageableDefault(page = 0, size = 10) Pageable pageable) {

		Page<Posting> posts = postingService.findWithPagination(pageable);
		
		return new ResponseEntity<Page<Posting>>(posts, HttpStatus.OK);
	}
	
	// 2. Find Posting by ID (view One Posting)
	@GetMapping(value = "/posting/{postingId}", produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<Posting> getPosting(@PathVariable("postingId") Long postingId) {

		Posting posting = postingService.findOnebyId(postingId);
		Long count = posting.getViewCount();
		posting.setViewCount(count+1);
		postingService.save(posting);
		
		return new ResponseEntity<Posting>(posting, HttpStatus.OK);
	}
	
	
//	// 2. Find Author by Posting Id
//	@GetMapping(value = "/board/author", produces = {MediaType.APPLICATION_JSON_VALUE})
//	public ResponseEntity<User> getAuthor(@RequestParam(value="postingId", defaultValue="50") Long postingId) {
//		
//		Long userId = postingService.findOnebyId(postingId).getUser().getId();
//		User user = userService.findOneById(userId);
//		
//		return new ResponseEntity<User>(user, HttpStatus.OK);
//	}
	
//	// 2. Find Author by Posting Id
//	@GetMapping(value = "/board/commentNumber", produces = {MediaType.APPLICATION_JSON_VALUE})
//	public Long getCommentNumber(@RequestParam(value="postingId", defaultValue="50") Long postingId) {
//		
//		Long commentNumber = commentService.countByPostingId(postingId);
//		
//		return commentNumber;
//	}
	
//	// 2. Find Author by Posting Id
//	@GetMapping(value = "/board/author", produces = {MediaType.APPLICATION_JSON_VALUE})
//	public String getAuthor(@RequestParam(value="postingId", defaultValue="50") Long postingId) {
//		
//		Long userId = postingService.findOnebyId(postingId).getUser().getId();
//		User user = userService.findOneById(userId);
//		
//		return user.getUserName();
//	}
}
