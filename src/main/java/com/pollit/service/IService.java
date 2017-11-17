package com.pollit.service;

import java.util.Collection;

import com.pollit.entities.Poll;

public interface IService {
	// liste de tous les sondages
	Collection getAll();
	// obtenir un sondage particulier
	Poll getOne(long id);
	// ajouter/modifier un sondage
	void saveOne(Poll poll);
	// supprimer un sondage
	void deleteOne(long id);
	// voter pour un sondage
	void vote(long pollId, long answerId);
}
