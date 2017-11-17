/**
 * 
 */
package com.pollit.entities;

/**
 * @author Dominique Mas
 *
 */
public class Answer {

	private long id;
	private long version;
	private String text;
	private long nbVotes;
	
	public Answer() {}

	/**
	 * This constructor creates a new Answer object. 
	 * The number of votes is automatically set to 0.
	 * @param id
	 * @param text
	 */
	public Answer(long id, String text) {
		setId(id);
		setText(text);
		setNbVotes(0);
	}
	
	/**
	 * This constructor creates a new Answer object. 
	 * The id is not set and the number of votes is automatically set to 0.
	 * @param id
	 * @param text
	 */
	public Answer(String text) {
		setText(text);
		setNbVotes(0);
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the version
	 */
	public long getVersion() {
		return version;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(long version) {
		this.version = version;
	}

	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * @return the nbVotes
	 */
	public long getNbVotes() {
		return nbVotes;
	}

	/**
	 * @param nbVotes the nbVotes to set
	 */
	public void setNbVotes(long nbVotes) {
		this.nbVotes = nbVotes;
	}
}
