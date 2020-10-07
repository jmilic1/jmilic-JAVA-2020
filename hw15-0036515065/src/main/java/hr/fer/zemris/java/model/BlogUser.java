package hr.fer.zemris.java.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Class which models a Blog User in database.
 * 
 * @author Jura Milić
 *
 */
@Entity
@Table(name = "blog_users")
public class BlogUser {
	/**
	 * id of user
	 */
	private Long id;
	/**
	 * First Name of User
	 */
	private String firstName;
	/**
	 * Last name of user.
	 */
	private String lastName;
	/**
	 * Nickname of user.
	 */
	private String nick;
	/**
	 * Email of user.
	 */
	private String email;
	/**
	 * hash of user's password
	 */
	private String passwordHash;
	/**
	 * blogs created by user
	 */
	private List<BlogEntry> blogs;

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
	 * @return first name
	 */
	@Column(length = 100, nullable = false)
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return last name
	 */
	@Column(length = 100, nullable = false)
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return nickname
	 */
	@Column(length = 100, unique = true)
	public String getNick() {
		return nick;
	}

	/**
	 * @param nickname
	 */
	public void setNick(String nick) {
		this.nick = nick;
	}

	/**
	 * @return email
	 */
	@Column(length = 100, nullable = false)
	public String getEmail() {
		return email;
	}

	/**
	 * @param email
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return hash
	 */
	@Column(length = 200, nullable = false)
	public String getPasswordHash() {
		return passwordHash;
	}

	/**
	 * @param hash
	 */
	public void setPasswordHash(String hash) {
		this.passwordHash = hash;
	}

	/**
	 * @return user's blogs
	 */
	@OneToMany(mappedBy = "creator")
	public List<BlogEntry> getBlogs() {
		return blogs;
	}

	/**
	 * @param user's blogs
	 */
	public void setBlogs(List<BlogEntry> blogs) {
		this.blogs = blogs;
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
		BlogUser other = (BlogUser) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
