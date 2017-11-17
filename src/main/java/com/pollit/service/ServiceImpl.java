package com.pollit.service;

import java.util.Collection;

import com.pollit.dao.IDao;
import com.pollit.entities.Poll;

public class ServiceImpl implements IService {

	// la couche [dao]
	private IDao dao;

	public IDao getDao() {
		return dao;
	}

	public void setDao(IDao dao) {
		this.dao = dao;
	}

	// liste des sondages
	public synchronized Collection getAll() {
		return dao.getAll();
	}

	// obtenir un sondage en particulier
	public synchronized Poll getOne(long id) {
		return dao.getOne(id);
	}

	// ajouter ou modifier un sondage
	public synchronized void saveOne(Poll poll) {
		dao.saveOne(poll);
	}

	// suppression d'un sondage
	public synchronized void deleteOne(long id) {
		dao.deleteOne(id);
	}
	
	// Vote pour un sondage
	public synchronized void vote(long pollId, long answerId) {
		dao.vote(pollId, answerId);
	}
}
