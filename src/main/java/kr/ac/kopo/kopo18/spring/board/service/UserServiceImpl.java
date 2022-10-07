package kr.ac.kopo.kopo18.spring.board.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.ac.kopo.kopo18.spring.board.domain.User;
import kr.ac.kopo.kopo18.spring.board.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public User findOneByUserName(String userName) {
		return userRepository.findOneByUserName(userName);
	}

	@Override
	public User save(User user) {
		return userRepository.save(user);
	}

	@Override
	public User findOneById(Long userId) {
		return userRepository.findOneById(userId);
	}

	@Override
	public List<User> findByOrderById() {
		return userRepository.findByOrderById();
	}

	@Override
	public void deleteOneById(Long userId) {
		userRepository.deleteOneById(userId);
	}
	

}
