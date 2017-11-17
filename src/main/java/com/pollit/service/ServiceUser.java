/**
 * 
 */
package com.pollit.service;

import com.pollit.dao.DaoUser;
import com.pollit.entities.User;

/**
 * @author Dominique Mas
 *
 */
public class ServiceUser {

	private DaoUser dao;
	
	public synchronized User getOne(String username, String passwordTyped) {
		return dao.getOne(username, passwordTyped);
	}
	
	public synchronized void insertOne(User user) {
		dao.insertOne(user);
	}
	
	public synchronized void deleteOne(String username) {
		dao.deleteOne(username);
	}

	public void setDao(DaoUser dao) {
		this.dao = dao;
	}
	
	public DaoUser getDao() {
		return dao;
	}
}
