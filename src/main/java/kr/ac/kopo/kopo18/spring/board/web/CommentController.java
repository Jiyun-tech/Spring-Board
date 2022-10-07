package kr.ac.kopo.kopo18.spring.board.web;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import kr.ac.kopo.kopo18.spring.board.domain.Comment;
import kr.ac.kopo.kopo18.spring.board.domain.Posting;
import kr.ac.kopo.kopo18.spring.board.domain.User;
import kr.ac.kopo.kopo18.spring.board.service.CommentService;
import kr.ac.kopo.kopo18.spring.board.service.PostingService;
import kr.ac.kopo.kopo18.spring.board.service.UserService;

@Controller
public class CommentController {
	
	@Autowired
	private CommentService commentService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private PostingService postingService;
	
	// C : "등록" 버튼 클릭 시 코멘트 저장 및 출력
	@PostMapping(value = "createComment")
	public String createPosting(Model model, @RequestParam(value = "posting_id") Long posting_id,
											@RequestParam(value = "page") Long page,
											@RequestParam(value = "comment_userName", defaultValue="") String userName,
											@RequestParam(value = "comment_password", defaultValue="") String password,
											@RequestParam(value = "comment_content", defaultValue="") String content) {
		
		if (userName.equals("") || password.equals("") || content.equals("")) {
			return "redirect:/viewPosting?posting_id="+posting_id+"&page="+page;
		} else {
			List<User> users = userService.findByOrderById();
			User user = null;
			for (int i = 0; i < users.size(); i++) {
				if (users.get(i).getUserName().equals(userName)) {
					user = users.get(i);
				}
			}
			
			if (user == null) {
				return "redirect:/viewPosting?posting_id="+posting_id+"&page="+page;
			} else if ( !user.getPassword().equals(password)) {
				return "redirect:/viewPosting?posting_id="+posting_id+"&page="+page;
			} else {
				Date date = new Date();
				Posting posting = postingService.findOnebyId(posting_id);
				Comment comment = new Comment();
				comment.setContent(content);
				comment.setCreateDate(date);
				comment.setUpdateDate(date);
				comment.setPosting(posting);
				comment.setUser(user);
				commentService.save(comment);
				
				return "redirect:/viewPosting?posting_id="+posting_id+"&page="+page;
			}
		}
	}
	
	// U : 코멘트 "수정"
	@PostMapping(value = "updateComment")
	public String updateComment(Model model, @RequestParam(value = "posting_id") Long posting_id,
											@RequestParam(value = "page") Long page,
											@RequestParam(value = "comment_id") Long comment_id,
											@RequestParam(value = "comment_password", defaultValue="") String comment_password) {
		
		if (comment_password.equals("")) {
			return "redirect:/viewPosting?posting_id="+posting_id+"&page="+page;
		} else {
			String user_password = commentService.findOneById(comment_id).getUser().getPassword();
			
			if (comment_password.equals(user_password)) {
				return "redirect:/viewPosting?posting_id="+posting_id+"&page="+page+"&updatedComment_id="+comment_id;
			} 
			else {
				return "redirect:/viewPosting?posting_id="+posting_id+"&page="+page;
			}
		}
	}
	
	// U : 코멘트 "수정" 완료
	@PostMapping(value = "updateCommentDone") 
	public String updateCommentDone(Model model, @RequestParam(value = "posting_id") Long posting_id,
												@RequestParam(value = "page") Long page,
												@RequestParam(value = "comment_id") Long comment_id,
												@RequestParam(value = "comment_content") String comment_content,
												@RequestParam(value = "comment_password") String comment_password,
												@RequestParam(value = "function", defaultValue="") String function) {
		
		if (function.equals("updateDone")) {
			Comment comment = commentService.findOneById(comment_id);
			comment.setContent(comment_content);
			Date date = new Date();
			comment.setUpdateDate(date);
			
			commentService.save(comment);
		}
		
		return "redirect:/viewPosting?posting_id="+posting_id+"&page="+page;
	}
	
	
	// D : 코멘트 "삭제"
	@PostMapping(value = "deleteComment")
	public String deleteComment(Model model, @RequestParam(value = "posting_id") Long posting_id,
											@RequestParam(value = "page") Long page,
											@RequestParam(value = "comment_id") Long comment_id,
											@RequestParam(value ="comment_password") String comment_password) {
		
		String commentUser_password = commentService.findOneById(comment_id).getUser().getPassword();
		
		if (commentUser_password.equals(comment_password)) {
			commentService.deleteOneById(comment_id);
		} 
				
		return "redirect:/viewPosting?posting_id="+posting_id+"&page="+page;
	}
	
}
	
