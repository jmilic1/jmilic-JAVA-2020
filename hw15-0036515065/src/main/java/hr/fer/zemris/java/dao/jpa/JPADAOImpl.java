package hr.fer.zemris.java.dao.jpa;

import java.util.List;

import javax.persistence.NoResultException;

import hr.fer.zemris.java.dao.DAO;
import hr.fer.zemris.java.dao.DAOException;
import hr.fer.zemris.java.model.BlogComment;
import hr.fer.zemris.java.model.BlogEntry;
import hr.fer.zemris.java.model.BlogUser;

/**
 * Implementation of a DAO which can get entries from a JPA database.
 * 
 * @author Jura Milić
 *
 */
public class JPADAOImpl implements DAO {

	@Override
	public BlogEntry getBlogEntry(Long id) throws DAOException {
		return JPAEMProvider.getEntityManager().find(BlogEntry.class, id);
	}

	@Override
	public List<BlogEntry> getBlogEntries() throws DAOException {
		return JPAEMProvider.getEntityManager().createQuery("SELECT a FROM BlogEntry a", BlogEntry.class)
				.getResultList();
	}

	@Override
	public BlogComment getBlockComment(Long id) throws DAOException {
		return JPAEMProvider.getEntityManager().find(BlogComment.class, id);
	}

	@Override
	public List<BlogComment> getBlockComments() throws DAOException {
		return JPAEMProvider.getEntityManager().createQuery("SELECT a FROM BlogComment a", BlogComment.class)
				.getResultList();
	}

	@Override
	public BlogUser getBlogUserById(Long id) throws DAOException {
		return JPAEMProvider.getEntityManager().find(BlogUser.class, id);
	}

	@Override
	public BlogUser getBlogUserByNick(String nick) throws DAOException {
		try {
			return JPAEMProvider.getEntityManager()
					.createQuery("select u from BlogUser u where u.nick = '" + nick + "'", BlogUser.class)
					.getSingleResult();
		} catch (NoResultException ex) {
			return null;
		}
	}

	@Override
	public List<BlogUser> getBlogUsers() throws DAOException {
		return JPAEMProvider.getEntityManager().createQuery("SELECT a FROM BlogUser a", BlogUser.class).getResultList();
	}
}