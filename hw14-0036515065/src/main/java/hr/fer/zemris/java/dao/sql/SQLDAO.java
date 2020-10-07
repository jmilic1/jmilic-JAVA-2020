package hr.fer.zemris.java.dao.sql;

import hr.fer.zemris.java.dao.DAO;
import hr.fer.zemris.java.dao.DAOException;
import hr.fer.zemris.java.model.PollEntity;
import hr.fer.zemris.java.model.PollOptionEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of DAO which manipulates data from database.
 * 
 * @author Jura Milić
 *
 */
public class SQLDAO implements DAO {

	@Override
	public List<PollEntity> getPollEntries() throws DAOException {
		List<PollEntity> entries = new ArrayList<>();
		Connection con = SQLConnectionProvider.getConnection();
		PreparedStatement pst = null;

		try {
			pst = con.prepareStatement("select * from Polls order by id");
			try {
				ResultSet rs = pst.executeQuery();
				try {
					while (rs != null && rs.next()) {
						PollEntity entry = new PollEntity();
						entry.setId(rs.getLong(1));
						entry.setTitle(rs.getString(2));
						entry.setMessage(rs.getString(3));
						entries.add(entry);
					}
				} finally {
					try {
						rs.close();
					} catch (Exception ignorable) {
					}
				}
			} finally {
				try {
					pst.close();
				} catch (Exception ignorable) {
				}
			}
		} catch (Exception ex) {
			throw new DAOException("Error while listing polls in database.", ex);
		}
		return entries;
	}

	@Override
	public PollEntity getPollEntry(long id) throws DAOException {
		PollEntity entry = null;
		Connection con = SQLConnectionProvider.getConnection();
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement("select * from Polls where id=?");
			pst.setLong(1, Long.valueOf(id));
			try {
				ResultSet rs = pst.executeQuery();
				try {
					if (rs != null && rs.next()) {
						entry = new PollEntity();
						entry.setId(rs.getLong(1));
						entry.setTitle(rs.getString(2));
						entry.setMessage(rs.getString(3));
					}
				} finally {
					try {
						rs.close();
					} catch (Exception ignorable) {
					}
				}
			} finally {
				try {
					pst.close();
				} catch (Exception ignorable) {
				}
			}
		} catch (Exception ex) {
			throw new DAOException("Error while getting the requested Poll.", ex);
		}
		return entry;
	}

	@Override
	public List<PollOptionEntity> getPollOptionEntries() throws DAOException {
		List<PollOptionEntity> entries = new ArrayList<>();
		Connection con = SQLConnectionProvider.getConnection();
		PreparedStatement pst = null;

		try {
			pst = con.prepareStatement("select * from PollOptions order by id");
			try {
				ResultSet rs = pst.executeQuery();
				try {
					while (rs != null && rs.next()) {
						PollOptionEntity entry = new PollOptionEntity();
						entry.setId(rs.getLong(1));
						entry.setTitle(rs.getString(2));
						entry.setLink(rs.getString(3));
						entry.setPollID(rs.getLong(4));
						entry.setVotesCount(rs.getLong(5));
						entries.add(entry);
					}
				} finally {
					try {
						rs.close();
					} catch (Exception ignorable) {
					}
				}
			} finally {
				try {
					pst.close();
				} catch (Exception ignorable) {
				}
			}
		} catch (Exception ex) {
			throw new DAOException("Error while listing poll options in database.", ex);
		}
		return entries;
	}

	@Override
	public PollOptionEntity getPollOptionEntry(long id) throws DAOException {
		PollOptionEntity entry = null;
		Connection con = SQLConnectionProvider.getConnection();
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement("select * from PollOptions where id=?");
			pst.setLong(1, Long.valueOf(id));
			try {
				ResultSet rs = pst.executeQuery();
				try {
					if (rs != null && rs.next()) {
						entry = new PollOptionEntity();
						entry.setId(rs.getLong(1));
						entry.setTitle(rs.getString(2));
						entry.setLink(rs.getString(3));
						entry.setPollID(rs.getLong(4));
						entry.setVotesCount(rs.getLong(5));
					}
				} finally {
					try {
						rs.close();
					} catch (Exception ignorable) {
					}
				}
			} finally {
				try {
					pst.close();
				} catch (Exception ignorable) {
				}
			}
		} catch (Exception ex) {
			throw new DAOException("Error while getting the requested Poll option.", ex);
		}
		return entry;
	}

}