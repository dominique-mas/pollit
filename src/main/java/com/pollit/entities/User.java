/**
 * 
 */
package com.pollit.entities;

import java.util.Calendar;

/**
 * @author Dominique Mas
 *
 */
public class User {

	private String username;
	private String password;
	private String lastName;
	private String firstName;
	private Calendar dateOfBirth;
	private String gender;
	
	public User() {}
	
	public User(String username, String password, String lastName, String firstName,
			Calendar dateOfBirth, String gender) {
		this.username = username;
		this.password = password;
		this.lastName = lastName;
		this.firstName = firstName;
		this.dateOfBirth = dateOfBirth;
		this.gender = gender;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public Calendar getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Calendar dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}
	
	
}
