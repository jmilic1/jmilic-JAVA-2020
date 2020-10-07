package hr.fer.zemris.java.model;

/**
 * Class model for a Poll containing an id, title and message.
 * 
 * @author Jura Milić
 *
 */
public class PollEntity {
	/**
	 * Id of poll.
	 */
	private long id;
	/**
	 * Title of poll.
	 */
	private String title;
	/**
	 * Message of poll.
	 */
	private String message;

	/**
	 * Constructs a PollEntity.
	 */
	public PollEntity() {
	}

	/**
	 * Get poll id.
	 * 
	 * @return id
	 */
	public long getId() {
		return id;
	}

	/**
	 * Set poll id.
	 * 
	 * @param id id of poll
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * Get title of poll.
	 * 
	 * @return title of poll
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Set title of poll.
	 * 
	 * @param title poll title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Return message of poll.
	 * 
	 * @return message of poll
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Set message of poll.
	 * 
	 * @param message poll message
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "Poll id=" + id;
	}
}
