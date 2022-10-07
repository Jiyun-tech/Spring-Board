package kr.ac.kopo.kopo18.spring.board.service;

import java.util.List;

import kr.ac.kopo.kopo18.spring.board.domain.User;

public interface UserService {
	
	// C / U
	User save(User user);
	
	
	// R
	User findOneByUserName(String userName);
	User findOneById(Long userId);
	List<User> findByOrderById();
	
	// D
	void deleteOneById(Long userId);

}
