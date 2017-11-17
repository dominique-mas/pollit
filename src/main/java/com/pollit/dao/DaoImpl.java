/**
 * 
 */
package com.pollit.dao;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import com.pollit.entities.Answer;
import com.pollit.entities.Poll;

/**
 * @author Dominique Mas
 *
 */
public class DaoImpl implements IDao {

	// Une liste de sondages
	private ArrayList<Poll> polls = new ArrayList<Poll>();
	
	// N° du prochain sondage
	private long pollId = 0;
	// N° de la prochaine réponse
	private long answerId = 0;
	
	// initialisations
	public void init() {
		ArrayList<Answer> a1 = new ArrayList<Answer>();
		a1.add(new Answer(-1, "Pour"));
		a1.add(new Answer(-1, "Contre"));
		Poll p1 = new Poll(-1, "Pour ou contre la 6e république ?", a1);
		saveOne(p1);
		
		ArrayList<Answer> a2 = new ArrayList<Answer>();
		a2.add(new Answer(-1, "Pizza"));
		a2.add(new Answer(-1, "Sushi"));
		Poll p2 = new Poll(-1, "Plutôt pizza ou sushi ?", a2);
		saveOne(p2);
		
		ArrayList<Answer> a3 = new ArrayList<Answer>();
		a3.add(new Answer(-1, "Prochainement !"));
		a3.add(new Answer(-1, "Le plus loin possible..."));
		a3.add(new Answer(-1, "Je n'imagine pas la fin du monde"));
		Poll p3 = new Poll(-1, "Quand imaginez-vous la fin du monde ?", a3);
		saveOne(p3);
	}
	
	// Liste des sondages
	public Collection getAll() {
		return polls;
	}
	
	// Obtenir un sondage en particulier
	public Poll getOne(long id) {
		// On cherche le sondage
		int i = getPosition(id);
		// A-t'on trouvé ?
		if (i != -1) return new Poll(polls.get(i));
		else throw new DaoException("Sondage d'id [" + id + "] inconnu", 2);
	}
	
	// Ajouter ou modifier un sondage
	public void saveOne(Poll poll) {
		// Le paramètre poll est-il valide ?
		check(poll);
		// Ajout ou modification ?
		
		if (poll.getId() == -1) {
			// Ajout
			poll.setId(getNextPollId());
			for (Answer a : poll.getAnswers()) {
				a.setId(getNextAnswerId());
			}
			poll.setVersion(1);
			polls.add(poll);
		} else {
			// Modification - On cherche le sondage
			int i = getPosition(poll.getId());
			// A-t'on trouvé ?
			if (i == -1) {
				throw new DaoException("Le sondage d'id [" + poll.getId()
						+ "] qu'on veut modifier n'existe pas", 2);
			}
			// A-t'on la bonne version de l'original ?
			Poll original = polls.get(i);
			if (original.getVersion() != poll.getVersion()) {
				throw new DaoException("L'original du sondage [" + poll 
						+ "] a changé depuis sa lecture initiale", 3);
			}
			// On attend 10 ms
			/*try {
				wait(10);
			} catch (InterruptedException e) {
				// On affiche la trace de l'exception
				e.printStackTrace();
				return;
			}*/
			// C'est bon - On fait la modification
			original.setVersion(original.getVersion() + 1);
			original.setText(poll.getText());
			original.setAnswers(poll.getAnswers());
		}
	}
	
	// Suppression d'un sondage
	public void deleteOne(long id) {
		// On cherche le sondage
		int i = getPosition(id);
		// A-t'on trouvé ?
		if (i == -1) {
			throw new DaoException("Sondage d'id [" + id + "] inconnu", 2);
		} else {
			// On supprime le sondage
			polls.remove(i);
		}
	}
	
	// Vote pour un sondage
	public void vote(long pollId, long answerId) {
		Poll p = getOne(pollId);
		ArrayList<Answer> answers = p.getAnswers();
		Iterator<Answer> it = answers.iterator();
		Answer a = null;
		boolean found = false;
		// On parcourt la liste des réponses
		while (!found && it.hasNext()) {
			 a = it.next();
			 if (a.getId() == answerId) found = true;
		}
		if (!found) 
			throw new DaoException("Réponse d'id [" + answerId + "] inconnue", 14);
		a.setNbVotes(a.getNbVotes() + 1);
	}
	
	// Générateur d'id pour les sondages
	private long getNextPollId() {
		pollId++;
		return pollId;
	}
	
	// Générateur d'id pour les réponses
	private long getNextAnswerId() {
		answerId++;
		return answerId;
	}
	
	// Rechercher un sondage
	private int getPosition(long id) {
		int i = 0;
		boolean found = false;
		// On parcourt la liste des sondages
		while (i < polls.size() && !found) {
			if (id == polls.get(i).getId()) {
				found = true;
			}
			else i++;
		}
		// Résultat ?
		return found ? i : -1;
	}
	
	// Vérification d'un sondage
	private void check(Poll p) {
		// Sondage p
		if (p == null) throw new DaoException("Sondage null", 10);
		// Id
		if (p.getId() != -1 && p.getId() < 0)
			throw new DaoException("Id [" + p.getId() + "] invalide", 11);
		// Text
		if (p.getText() == null || "".equals(p.getText().trim()))
			throw new DaoException("Question manquante", 12);
		// Answers
		if (p.getAnswers() == null || p.getAnswers().size() < 2)
			throw new DaoException("Nombre de réponses insuffisantes", 13);
	}
}
