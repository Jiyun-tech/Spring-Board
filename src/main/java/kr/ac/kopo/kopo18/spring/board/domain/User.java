package kr.ac.kopo.kopo18.spring.board.domain;

import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
public class User {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY) // auto_increment 추가 (GenerationType.IDENTITY)
	@Column
	private Long id;				// 용도 : 시스템에서 사용하는 구분자
	
	@Column(nullable = false, length = 50, unique = true)
	private String userName;
	
	@Column(nullable = false, length = 20)
	private String password;
	
	@JsonBackReference
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="user", orphanRemoval = true)
	@Column
	private Collection<Posting> postings;
	
	@JsonBackReference
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="user", orphanRemoval = true)
	@Column
	private Collection<Comment> comments;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Collection<Posting> getPostings() {
		return postings;
	}

	public void setPostings(Collection<Posting> postings) {
		this.postings = postings;
	}

//	public Collection<Reply> getReplies() {
//		return replies;
//	}
//
//	public void setReplies(Collection<Reply> replies) {
//		this.replies = replies;
//	}

	public Collection<Comment> getComments() {
		return comments;
	}

	public void setComments(Collection<Comment> comments) {
		this.comments = comments;
	}
	
	
	

}
