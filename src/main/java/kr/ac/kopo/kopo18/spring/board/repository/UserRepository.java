package kr.ac.kopo.kopo18.spring.board.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import kr.ac.kopo.kopo18.spring.board.domain.User;


public interface UserRepository extends JpaRepository<User, Long>
											, JpaSpecificationExecutor<User>{
	
	// C / U
	User save(User user);
	
	
	// R
	User findOneByUserName(String userName);
	User findOneById(Long userId);
	List<User> findByOrderById();
	
	// D
	void deleteOneById(Long userId);
	
}
