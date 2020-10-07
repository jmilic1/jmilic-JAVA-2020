package hr.fer.zemris.java.model;

/**
 * Class model for options in a poll.
 * 
 * @author Jura Milić
 *
 */
public class PollOptionEntity implements Comparable<PollOptionEntity> {
	/**
	 * Id of pollOption.
	 */
	private long id;
	/**
	 * Title of pollOption.
	 */
	private String title;
	/**
	 * Link of pollOption.
	 */
	private String link;
	/**
	 * Poll ID of pollOption.
	 */
	private long pollID;
	/**
	 * Vote count of pollOption.
	 */
	private long votesCount;

	/**
	 * Constructs a PollOptionEntity.
	 */
	public PollOptionEntity() {
	}

	/**
	 * Returns id of PollOption.
	 * 
	 * @return PollOption id
	 */
	public long getId() {
		return id;
	}

	/**
	 * Set id of PollOption.
	 * 
	 * @param id PollOption id
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * Returns title of PollOption.
	 * 
	 * @return PollOption title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Set title of PollOption.
	 * 
	 * @param title PollOption title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Returns link of PollOption.
	 * 
	 * @return PollOption link
	 */
	public String getLink() {
		return link;
	}

	/**
	 * Sets link of PollOption.
	 * 
	 * @param link PollOption link
	 */
	public void setLink(String link) {
		this.link = link;
	}

	/**
	 * Returns Poll ID of PollOption.
	 * 
	 * @return PollOption pollID
	 */
	public long getPollID() {
		return pollID;
	}

	/**
	 * Sets the Poll id of PollOption.
	 * 
	 * @param pollID PollOption pollID
	 */
	public void setPollID(long pollID) {
		this.pollID = pollID;
	}

	/**
	 * Returns votes count of PollOption.
	 * 
	 * @return PollOption votesCount
	 */
	public long getVotesCount() {
		return votesCount;
	}

	/**
	 * Sets votes count of PollOption.
	 * 
	 * @param votesCount PollOption votesCount
	 */
	public void setVotesCount(long votesCount) {
		this.votesCount = votesCount;
	}

	@Override
	public String toString() {
		return "PollOption id=" + id;
	}

	@Override
	public int compareTo(PollOptionEntity o) {
		return Integer.compare((int) o.votesCount, (int) votesCount);
	}
}