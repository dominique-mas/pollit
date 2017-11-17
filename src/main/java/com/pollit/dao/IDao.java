/**
 * 
 */
package com.pollit.dao;

import java.util.Collection;

import com.pollit.entities.Poll;

/**
 * @author domin
 *
 */
public interface IDao {

	// Liste de tous les sondages
	Collection getAll();
	
	Poll getOne(long id);
	
	void saveOne(Poll p);
	
	void deleteOne(long id);
	
	void vote(long pollId, long answerId);
}
