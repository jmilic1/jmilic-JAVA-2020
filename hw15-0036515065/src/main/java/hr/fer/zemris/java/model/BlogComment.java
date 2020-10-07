package hr.fer.zemris.java.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Class which models a Blog Comment in database.
 * 
 * @author Jura Milić
 *
 */
@Entity
@Table(name = "blog_comments")
public class BlogComment {
	/**
	 * id of comment
	 */
	private Long id;
	/**
	 * Parent blog Entry of comment
	 */
	private BlogEntry blogEntry;
	/**
	 * Email of comment author
	 */
	private String usersEMail;
	/**
	 * Comment's message
	 */
	private String message;
	/**
	 * Date on which comment was posted
	 */
	private Date postedOn;

	/**
	 * @return id
	 */
	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return blogEntry
	 */
	@ManyToOne
	@JoinColumn(nullable = false)
	public BlogEntry getBlogEntry() {
		return blogEntry;
	}

	/**
	 * @param blogEntry
	 */
	public void setBlogEntry(BlogEntry blogEntry) {
		this.blogEntry = blogEntry;
	}

	/**
	 * @return email of user
	 */
	@Column(length = 100, nullable = false)
	public String getUsersEMail() {
		return usersEMail;
	}

	/**
	 * @param email of user
	 */
	public void setUsersEMail(String usersEMail) {
		this.usersEMail = usersEMail;
	}

	/**
	 * @return message of comment
	 */
	@Column(length = 4096, nullable = false)
	public String getMessage() {
		return message;
	}

	/**
	 * @param message of comment
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return date of comment
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	public Date getPostedOn() {
		return postedOn;
	}

	/**
	 * @param date of comment
	 */
	public void setPostedOn(Date postedOn) {
		this.postedOn = postedOn;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BlogComment other = (BlogComment) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}