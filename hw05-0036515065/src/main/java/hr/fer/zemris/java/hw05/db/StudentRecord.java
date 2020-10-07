package hr.fer.zemris.java.hw05.db;

/**
 * Basic class used for storing an individual student's information. Two student
 * records are considered equal only if they share the same JMBAG.
 * 
 * @author Jura Milić
 *
 */
public class StudentRecord {
	/**
	 * JMBAG of student
	 */
	private String jmbag;
	/**
	 * Last name of student
	 */
	private String lastName;
	/**
	 * First name of student
	 */
	private String firstName;
	/**
	 * Final grade of student
	 */
	private Integer finalGrade;

	/**
	 * Constructs a StudentRecord with information set accordingly
	 * 
	 * @param jmbag      given JMBAG
	 * @param lastName   given last name
	 * @param firstName  given first name
	 * @param finalGrade given grade
	 */
	public StudentRecord(String jmbag, String lastName, String firstName, int finalGrade) {
		this.jmbag = jmbag;
		this.lastName = lastName;
		this.firstName = firstName;
		this.finalGrade = finalGrade;
	}

	/**
	 * @return JMBAG
	 */
	public String getJmbag() {
		return jmbag;
	}

	/**
	 * @param jmbag
	 */
	public void setJmbag(String jmbag) {
		this.jmbag = jmbag;
	}

	/**
	 * @return last name
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param last name
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return first name
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param first name
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return final grade
	 */
	public Integer getFinalGrade() {
		return finalGrade;
	}

	/**
	 * @param final grade
	 */
	public void setFinalGrade(int finalGrade) {
		this.finalGrade = finalGrade;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((jmbag == null) ? 0 : jmbag.hashCode());
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
		StudentRecord other = (StudentRecord) obj;
		if (jmbag == null) {
			if (other.jmbag != null)
				return false;
		} else if (!jmbag.equals(other.jmbag))
			return false;
		return true;
	}

}
