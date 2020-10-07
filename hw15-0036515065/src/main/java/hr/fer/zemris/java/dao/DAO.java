package hr.fer.zemris.java.dao;

import java.util.List;

import hr.fer.zemris.java.model.BlogComment;
import hr.fer.zemris.java.model.BlogEntry;
import hr.fer.zemris.java.model.BlogUser;

/**
 * Interface for a simple DAO which gets different entities from database.
 */
public interface DAO {

	/**
	 * Gets the entry that has the given id. If such entry does not exist, null is
	 * returned.
	 * 
	 * @param id given id
	 * @return entry or null
	 * @throws DAOException if an error occurred
	 */
	public BlogEntry getBlogEntry(Long id) throws DAOException;

	/**
	 * Gets all of the blogEntries in database
	 * 
	 * @return list of blogs
	 * @throws DAOException if an error occurred
	 */
	public List<BlogEntry> getBlogEntries() throws DAOException;

	/**
	 * Gets the blog comment that has the given id.
	 * 
	 * @param id given id
	 * @return blog comment
	 * @throws DAOException if an error occurred
	 */
	public BlogComment getBlockComment(Long id) throws DAOException;

	/**
	 * Gets all of the blog comments in database.
	 * 
	 * @return list of comments
	 * @throws DAOException if an error occurred
	 */
	public List<BlogComment> getBlockComments() throws DAOException;

	/**
	 * Gets the blog user with the given id.
	 * 
	 * @param id given id
	 * @return blog user
	 * @throws DAOException if an error occurred
	 */
	public BlogUser getBlogUserById(Long id) throws DAOException;

	/**
	 * Gets the blog user with the given nickname.
	 * 
	 * @param nick given nickname
	 * @return blog user
	 * @throws DAOException if an error occurred
	 */
	public BlogUser getBlogUserByNick(String nick) throws DAOException;

	/**
	 * Gets all of the users in database.
	 * 
	 * @return list of blog users
	 * @throws DAOException if an error occurred
	 */
	public List<BlogUser> getBlogUsers() throws DAOException;

}