package hr.fer.zemris.java.dao;

import java.util.List;

import hr.fer.zemris.java.model.PollEntity;
import hr.fer.zemris.java.model.PollOptionEntity;

/**
 * Interface for manipulating the subsytem for data persistence.
 * 
 * @author Jura Milić
 *
 */
public interface DAO {
	/**
	 * Returns all Polls in database.
	 * 
	 * @return a PollEntity list
	 * @throws DAOException if Polls could not be extracted
	 */
	public List<PollEntity> getPollEntries() throws DAOException;

	/**
	 * Returns the Poll with the given id.
	 * 
	 * @param id given id
	 * @return found Poll
	 * @throws DAOException if Poll could not be extracted
	 */
	public PollEntity getPollEntry(long id) throws DAOException;

	/**
	 * Returns all PollOptions in database.
	 * 
	 * @return a PollOptionsEntity list
	 * @throws DAOException if PollOptions could not be extracted
	 */
	public List<PollOptionEntity> getPollOptionEntries() throws DAOException;

	/**
	 * Returns the PollOption with the given id.
	 * 
	 * @param id given id
	 * @return found PollOption
	 * @throws DAOException if PollOption could not be extracted
	 */
	public PollOptionEntity getPollOptionEntry(long id) throws DAOException;
}