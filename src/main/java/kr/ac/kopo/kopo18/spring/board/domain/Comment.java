package kr.ac.kopo.kopo18.spring.board.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
public class Comment {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY) // auto_increment 추가 (GenerationType.IDENTITY)
	@Column
	private Long id;
	// 용도 : 시스템에서 사용하는 구분자
	@Column(nullable = false, length = 200)
	private String content;
	
	@Column
	private Date createDate;
	
	@Column
	private Date updateDate;

//	@JsonBackReference
	@ManyToOne(optional=false)
	@JoinColumn(name="user_id")	// DB column 이름 (생략 가능)
	private User user;
	
	@JsonBackReference
	@ManyToOne(optional=false)
	@JoinColumn(name="posting_id")	// DB column 이름 (생략 가능)
	private Posting posting;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Posting getPosting() {
		return posting;
	}

	public void setPosting(Posting posting) {
		this.posting = posting;
	}
	
}
