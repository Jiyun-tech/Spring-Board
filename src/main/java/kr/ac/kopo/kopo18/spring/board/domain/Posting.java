package kr.ac.kopo.kopo18.spring.board.domain;

import java.util.Collection;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@DynamicInsert	//@ColumnDefault 적용 위한 annotation
public class Posting {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY) // auto_increment 추가 (GenerationType.IDENTITY)
	@Column
	private Long id;	// 용도 : 시스템에서 사용하는 구분자
	
	@Column(nullable = false, length=100)
	private String title;
	
	@Column(columnDefinition = "text")
	private String content;
	
	@Column
	private Date createDate;
	
	@Column
	private Date updateDate;
	
	@Column
	@ColumnDefault("0")
	private Long viewCount;
	
//	@JsonBackReference
	@ManyToOne(optional=false)
	@JoinColumn(name="user_id")	// DB column 이름 (생략 가능)
	private User user;
	
	// 댓글
	@JsonManagedReference
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="posting", orphanRemoval = true)
	@Column
	private Collection<Comment> comments;
	
	// 글 Level
	@Column
	private Long level;
	
	// 답글
	@Column
	private Long originalPostingId;
	
	@Column
	private Long upperPostingId;
	
	@Column
	private Long reCount;
	
//	// 답글
//	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="posting", orphanRemoval = true)
//	@Column
//	private Collection<Posting> postings;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public Long getViewCount() {
		return viewCount;
	}

	public void setViewCount(Long viewCount) {
		this.viewCount = viewCount;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Collection<Comment> getComments() {
		return comments;
	}

	public void setComments(Collection<Comment> comments) {
		this.comments = comments;
	}

	public Long getLevel() {
		return level;
	}

	public void setLevel(Long level) {
		this.level = level;
	}

	public Long getOriginalPostingId() {
		return originalPostingId;
	}

	public void setOriginalPostingId(Long originalPostingId) {
		this.originalPostingId = originalPostingId;
	}

	public Long getUpperPostingId() {
		return upperPostingId;
	}

	public void setUpperPostingId(Long upperPostingId) {
		this.upperPostingId = upperPostingId;
	}

	public Long getReCount() {
		return reCount;
	}

	public void setReCount(Long reCount) {
		this.reCount = reCount;
	}

	
}
