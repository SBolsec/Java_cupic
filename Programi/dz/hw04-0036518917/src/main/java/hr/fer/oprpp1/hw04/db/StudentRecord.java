package hr.fer.oprpp1.hw04.db;

public class StudentRecord {

	/** jmbag - unique identifier **/
	private String jmbag;
	/** Student's last name **/
	private String lastName;
	/** Student's first name **/
	private String firstName;
	/** Student's final grade */
	private int finalGrade;
	
	/**
	 * Constructor which initializes the student.
	 * @param jmbag jmbag of the student
	 * @param lastName student's last name
	 * @param firstName student's first name
	 * @param finalGrade student's final grade
	 */
	public StudentRecord(String jmbag, String lastName, String firstName, int finalGrade) {
		super();
		this.jmbag = jmbag;
		this.lastName = lastName;
		this.firstName = firstName;
		this.finalGrade = finalGrade;
	}

	/**
	 * Returns student's jmbag.
	 * @return student's jmbag
	 */
	public String getJmbag() {
		return jmbag;
	}

	/**
	 * Sets student's jmbag.
	 * @param jmbag jmbag to be set
	 */
	public void setJmbag(String jmbag) {
		this.jmbag = jmbag;
	}

	/**
	 * Returns student's last name
	 * @return student's last name.
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Sets student's last name.
	 * @param lastName last name to be set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * Returns student's first name.
	 * @return student's first name
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Sets student's first name.
	 * @param firstName first name to be set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * Returns student's final grade.
	 * @return student's final grade
	 */
	public int getFinalGrade() {
		return finalGrade;
	}

	/**
	 * Sets students' final grade.
	 * @param finalGrade final grade to be set
	 */
	public void setFinalGrade(int finalGrade) {
		this.finalGrade = finalGrade;
	}

	/**
	 * Returns the hash code of this student.
	 * @return hash code of this student
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((jmbag == null) ? 0 : jmbag.hashCode());
		return result;
	}

	/**
	 * Two students are equal if they have the same jmbag.
	 * @param obj object to be tested
	 * @return true if the passed object is equal to this object, false otherwise
	 */
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
