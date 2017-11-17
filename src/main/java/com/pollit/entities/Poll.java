/**
 * 
 */
package com.pollit.entities;

import java.io.Serializable;
import java.util.ArrayList;

import javax.persistence.*;

/**
 * @author Dominique Mas
 *
 */
@Entity
@Table(name="Poll")
public class Poll implements Serializable {

	@Id
	@Column(name="pollid", nullable=false)
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	@Column(name="pollversion", nullable=false)
	@Version
	private long version;
	@Column(name="polltext", length=140, nullable=false)
	private String text;
	// TODO Annotations ?
	private ArrayList<Answer> answers;
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public long getVersion() {
		return version;
	}
	
	public void setVersion(long version) {
		this.version = version;
	}
	
	/**
	 * @return the question
	 */
	public String getText() {
		return text;
	}
	/**
	 * @param question the question to set
	 */
	public void setText(String question) {
		this.text = question;
	}
	/**
	 * @return the answers
	 */
	public ArrayList<Answer> getAnswers() {
		return answers;
	}
	/**
	 * @param answers the answers to set
	 */
	public void setAnswers(ArrayList<Answer> answers) {
		this.answers = answers;
	}
	
	public Poll(long id, String text, ArrayList<Answer> answers) {
		setId(id);
		setText(text);
		setAnswers(answers);
	}
	
	public Poll(String text, ArrayList<Answer> answers) {
		setText(text);
		setAnswers(answers);
	}
	
	// Constructeur d'un sondage par recopie d'un autre sondage
	public Poll(Poll p) {
		setId(p.getId());
		setVersion(p.getVersion());
		setText(p.getText());
		setAnswers(p.getAnswers());
	}
	
	public Poll() {}

	public String toString() {
		return text;
	}
}
