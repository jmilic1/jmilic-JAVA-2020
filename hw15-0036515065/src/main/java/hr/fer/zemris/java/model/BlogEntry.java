package hr.fer.zemris.java.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Class which models a BlogEntry for database.
 * 
 * @author Jura Milić
 *
 */
@NamedQueries({
		@NamedQuery(name = "BlogEntry.upit1", query = "select b from BlogComment as b where b.blogEntry=:be and b.postedOn>:when") })
@Entity
@Table(name = "blog_entries")
@Cacheable(true)
public class BlogEntry {
	/**
	 * id of Entry
	 */
	private Long id;
	/**
	 * Comments of entry
	 */
	private List<BlogComment> comments = new ArrayList<>();
	/**
	 * Date on which entry was created
	 */
	private Date createdAt;
	/**
	 * Date on which entry was last modified
	 */
	private Date lastModifiedAt;
	/**
	 * Title of Entry
	 */
	private String title;
	/**
	 * Text of Entry
	 */
	private String text;
	/**
	 * Entry's creator
	 */
	private BlogUser creator;

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
	 * @return comments
	 */
	@OneToMany(mappedBy = "blogEntry", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, orphanRemoval = true)
	@OrderBy("postedOn")
	public List<BlogComment> getComments() {
		return comments;
	}

	/**
	 * @param comments
	 */
	public void setComments(List<BlogComment> comments) {
		this.comments = comments;
	}

	/**
	 * @return date of creation
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	public Date getCreatedAt() {
		return createdAt;
	}

	/**
	 * @param date of creation
	 */
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	/**
	 * @return date of modification
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = true)
	public Date getLastModifiedAt() {
		return lastModifiedAt;
	}

	/**
	 * @param date of modification
	 */
	public void setLastModifiedAt(Date lastModifiedAt) {
		this.lastModifiedAt = lastModifiedAt;
	}

	/**
	 * @return title of entry
	 */
	@Column(length = 200, nullable = false)
	public String getTitle() {
		return title;
	}

	/**
	 * @param title of entry
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return text
	 */
	@Column(length = 4096, nullable = false)
	public String getText() {
		return text;
	}

	/**
	 * @param text
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * @return creator
	 */
	@ManyToOne
	public BlogUser getCreator() {
		return creator;
	}

	/**
	 * @param creator
	 */
	public void setCreator(BlogUser creator) {
		this.creator = creator;
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
		BlogEntry other = (BlogEntry) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}