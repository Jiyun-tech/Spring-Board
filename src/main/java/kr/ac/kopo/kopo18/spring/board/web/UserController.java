package kr.ac.kopo.kopo18.spring.board.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import kr.ac.kopo.kopo18.spring.board.domain.User;
import kr.ac.kopo.kopo18.spring.board.service.CommentService;
import kr.ac.kopo.kopo18.spring.board.service.PostingService;
import kr.ac.kopo.kopo18.spring.board.service.UserService;

@Controller
@Transactional
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private PostingService postingService;
	
	@Autowired
	private CommentService commentService;
	
	@GetMapping(value = "user")
	public String user() {
		return "user";
	}
	
	@PostMapping(value = "createUser")
	public String createUser(Model model, 
									@RequestParam(value="userName", defaultValue="") String userName,
									@RequestParam(value="password", defaultValue="") String password,
									@RequestParam(value="passwordCheck", defaultValue="") String passwordCheck) {
		
		List<User> users = userService.findByOrderById();
		for (int i = 0; i < users.size(); i++) {
			if (users.get(i).getUserName().equals(userName)) {
				return "userCreateFailed";
			}
		}
		
		if (userName.equals("") || password.equals("") || passwordCheck.equals("")) {
			return "user";	
		} else if (password.equals(passwordCheck)) {
			User user = new User();
			user.setUserName(userName);
			user.setPassword(password);
			userService.save(user);
			model.addAttribute("userName", userName);
			return "userCreateDone";
		} else {
			return "userCreateFailed";
		}
		
	}
	
	@RequestMapping(value = "selectUser", method={RequestMethod.GET,RequestMethod.POST})
	public String selectUser(Model model, 
									@RequestParam(value="findUserName", defaultValue="") String userName) {
		
		User user = null;
		String foundName = "";
		
		if (userName.equals("")) {
			return "user";
		} else {
			List<User> users = userService.findByOrderById();
			for (int i = 0; i < users.size(); i++) {
				if (users.get(i).getUserName().equals(userName)) {
					user = userService.findOneByUserName(userName);
					foundName = userName;
					break;
				} 
			}
			
			if (users.size() < 1 || foundName.equals("")) {
				model.addAttribute("userName","등록된 사용자가 없습니다");
				model.addAttribute("countPosting",null);
				model.addAttribute("countComment",null);
				return "viewUser";
			} else {
				Long countPosting = postingService.countByUserId(user.getId());
				Long countComment = commentService.countByUserId(user.getId());
				model.addAttribute("user",user);
				model.addAttribute("userName",foundName);
				model.addAttribute("countPosting",countPosting);
				model.addAttribute("countComment",countComment);
				return "viewUser";
			}
		}
		
	}
	
	@PostMapping(value = "updateUser")
	public String updateUser(Model model, 
									@RequestParam(value="userName", defaultValue="") String userName,
									@RequestParam(value="userPassword", defaultValue="") String get_password) {
		User user = userService.findOneByUserName(userName);
		String password = user.getPassword();
		
		if (get_password.equals("")) {
			return "redirect:/selectUser?findUserName="+userName;	
		} else if (get_password.equals(password)) {
			model.addAttribute("userName",userName);
			model.addAttribute("userPassword",get_password);
			return "userUpdate";
		} else {
			return "redirect:/selectUser?findUserName="+userName;
		}
		
	}
	
	@PostMapping(value = "userUpdateDone")
	public String usreUpdateDone (Model model, 
										@RequestParam(value="userName", defaultValue="") String userName,
										@RequestParam(value="password", defaultValue="") String password,
										@RequestParam(value="passwordCheck", defaultValue="") String passwordCheck) {
		
		model.addAttribute("userName", userName);
		
		if (password.equals(passwordCheck)) {
			User user = userService.findOneByUserName(userName);
			user.setPassword(password);
			userService.save(user);
			model.addAttribute("function","updateDone");
			return "userUpdateDone";
		} else {
			model.addAttribute("function","updateFailed");
			return "userUpdateDone";
		}
	}
	
	@PostMapping(value = "deleteUser")
	public String deleteUser(Model model, 
									@RequestParam(value="userName", defaultValue="") String userName,
									@RequestParam(value="userPassword", defaultValue="") String get_password,
									@RequestParam(value="countPosting", defaultValue="0") Long countPosting,
									@RequestParam(value="countComment", defaultValue="0") Long countComment) {
		User user = userService.findOneByUserName(userName);
		String password = user.getPassword();
		
//		User fakeUser = userService.findOneByUserName("탈퇴회원");
		
		if (get_password.equals("")) {
			return "user";	
		} else if (get_password.equals(password)) {
			
// 			#사용자 삭제 시, 게시글/포스팅은 남아있도록 하고자 하였으나 실패함.
//			List<Posting> postings = postingService.findAllByUserIdOrderById(user.getId());
//			for (int i = 0; i < postings.size(); i++) {
//				Posting tempPosting = postings.get(i);
//				tempPosting.setUser(fakeUser);
//				postingService.save(tempPosting);
//			}
//			
//			List<Comment> comments = commentService.findAllByUserIdOrderById(user.getId());
//			for (int i = 0; i < comments.size(); i++) {
//				Comment tempComment = comments.get(i);
//				tempComment.setUser(fakeUser);
//				commentService.save(tempComment);
//			}
//			
//			postings.clear();
//			comments.clear();
//			user.setPostings(postings);
//			user.setComments(comments);
//			userService.save(user);

			userService.deleteOneById(user.getId());
			model.addAttribute("userName", userName);
			model.addAttribute("countPosting", countPosting);
			model.addAttribute("countComment", countComment);
			return "userDeleteDone";
		} else {
			return "user";
		}
		
	}

}
