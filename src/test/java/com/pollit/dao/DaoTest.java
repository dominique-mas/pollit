/**
 * 
 */
package com.pollit.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import com.pollit.dao.DaoException;
import com.pollit.dao.DaoImpl;
import com.pollit.entities.Answer;
import com.pollit.entities.Poll;

import junit.framework.TestCase;

/**
 * @author Dominique Mas
 *
 */
public class DaoTest extends TestCase {

	// couche [dao]
	private DaoImpl dao;
	
	// Constructeur
	public DaoTest() {
		dao = new DaoImpl();
		dao.init();
	}
	
	// liste des personnes
	private void doListe(Collection polls) {
		Iterator iter = polls.iterator();
		while (iter.hasNext()) {
			System.out.println(iter.next());
		}
	}

	// Test 1
	public void test1() {
		// Liste actuelle
		Collection polls = dao.getAll();
		int nbPolls = polls.size();
		// Affichage
		doListe(polls);
		// Ajout d'un sondage
		ArrayList<Answer> a1 = new ArrayList<Answer>();
		a1.add(new Answer(-1, "Pour"));
		a1.add(new Answer(-1, "Contre"));
		Poll p1 = new Poll(-1, "Êtes-vous pour ou contre la légalisation du cannabis ?", a1);
		dao.saveOne(p1);
		long id1 = p1.getId();
		// Vérification - On aura un plantage si la personne n'est pas trouvée
		p1 = dao.getOne(id1);
		assertNotNull(p1.getText());
		// Suppression
		dao.deleteOne(id1);
		// Vérification
		int codeErreur = 0;
		boolean erreur = false;
		try {
			p1 = dao.getOne(id1);
		} catch (DaoException ex) {
			erreur = true;
			codeErreur = ex.getCode();
		}
		// On doit avoir une erreur de code 2
		assertTrue(erreur);
		assertEquals(2, codeErreur);
		// Liste des sondages
		polls = dao.getAll();
		assertEquals(nbPolls, polls.size());
	}
	
	// Modification - suppression d'un élément inexistant
	public void test2() {
		// D'abord ajout
		ArrayList<Answer> a1 = new ArrayList<Answer>();
		a1.add(new Answer(-1, "Pour"));
		a1.add(new Answer(-1, "Contre"));
		Poll p1 = new Poll(-1, "Êtes-vous pour ou contre la légalisation du cannabis ?", a1);
		dao.saveOne(p1);
		// Récupération copie du sondage
		Poll p2 = dao.getOne(p1.getId());
		// Modification de la copie avec un id inexistant
		p2.setId(p1.getId() + 1);
		// Modification de la copie
		p2.setText("Êtes-vous pour ou contre la peine de mort ?");
		// Sauvegarde de la copie - on doit avoir une DaoException de code 2
		boolean erreur = false;
		int codeErreur = 0;
		try {
			dao.saveOne(p2);
		} catch (DaoException ex) {
			erreur = true;
			codeErreur = ex.getCode();
		}
		// Vérification - on doit avoir une erreur de code 2
		assertTrue(erreur);
		assertEquals(2, codeErreur);
		// Suppression sondage p1
		dao.deleteOne(p1.getId());
		// Vérification
		try {
			p1 = dao.getOne(p1.getId());
		} catch (DaoException ex) {
			erreur = true;
			codeErreur = ex.getCode();
		}
		// On doit avoir une erreur de code 2
		assertTrue(erreur);
		assertEquals(2, codeErreur);
	}
	
	// Gestion des versions de sondage
	public void test3() throws InterruptedException {
		// D'abord ajout
		ArrayList<Answer> a1 = new ArrayList<Answer>();
		a1.add(new Answer(-1, "Pour"));
		a1.add(new Answer(-1, "Contre"));
		Poll p1 = new Poll(-1, "Êtes-vous pour ou contre la légalisation du cannabis ?", a1);
		dao.saveOne(p1);
		// Récupération copie p2 du sondage p1
		Poll p2 = dao.getOne(p1.getId());
		// Récupération copie p3 du sondage p1
		Poll p3 = dao.getOne(p1.getId());
		// On vérifie qu'on a bien la même version
		assertEquals(p2.getVersion(), p3.getVersion());
		// Attente de 10 ms
		Thread.sleep(10);
		// Sauvegarde copie p2 - la version de p1 va changer
		dao.saveOne(p2);
		// Vérification
		Poll p2b = dao.getOne(p1.getId());
		assertFalse(p2.getVersion() == p2b.getVersion());
		// Sauvegarde copie p3 - on doit avoir une DaoException de code 3
		boolean erreur = false;
		int codeErreur = 0;
		try {
			dao.saveOne(p3);
		} catch (DaoException ex) {
			erreur = true;
			codeErreur = ex.getCode();
		}
		// vérification - on doit avoir une erreur de code 3
		assertTrue(erreur);
		assertEquals(codeErreur, 3);
		// suppression sondage p1
		dao.deleteOne(p1.getId());
		// vérification
		try {
			p1 = dao.getOne(p1.getId());
		} catch (DaoException ex) {
			erreur = true;
			codeErreur = ex.getCode();
		}
		// on doit avoir une erreur de code 2
		assertTrue(erreur);
		assertEquals(2, codeErreur);
	}
	
	// optimistic locking - accès multi-threads
	/*public void test4() throws Exception {
		// ajout d'un sondage
		ArrayList<Answer> a1 = new ArrayList<Answer>();
		a1.add(new Answer(-1, "Pour"));
		a1.add(new Answer(-1, "Contre"));
		Poll p1 = new Poll(-1, "Êtes-vous pour ou contre la légalisation du cannabis ?", a1);
		dao.saveOne(p1);
		long id1 = p1.getId();
		// création de N threads de mise à jour du nombre d'enfants
		final int N = 2;
		Thread[] taches = new Thread[N];
		for (int i = 0; i < taches.length; i++) {
			taches[i] = new ThreadDaoMajEnfants("thread n° " + i, dao, id1);
			taches[i].start();
		}
		// on attend la fin des threads
		for (int i = 0; i < taches.length; i++) {
			taches[i].join();
		}
		// on récupère le sondage
		p1 = dao.getOne(id1);
		// elle doit avoir N enfants
		assertEquals(N, p1.getNbEnfants());
		// suppression personne p1
		dao.deleteOne(p1.getId());
		// vérification
		boolean erreur = false;
		int codeErreur = 0;
		try {
			p1 = dao.getOne(p1.getId());
		} catch (DaoException ex) {
			erreur = true;
			codeErreur = ex.getCode();
		}
		// on doit avoir une erreur de code 2
		assertTrue(erreur);
		assertEquals(2, codeErreur);
	}*/

	// tests de validité de saveOne
	public void test5() {
		int codeErreur = 0;
		// sondage null
		try {
			dao.saveOne(null);
		} catch (DaoException ex) {
			codeErreur = ex.getCode();
		}
		assertEquals(10, codeErreur);
		// id erroné
		try {
			ArrayList<Answer> a1 = new ArrayList<Answer>();
			a1.add(new Answer(-1, "Pour"));
			a1.add(new Answer(-1, "Contre"));
			Poll p1 = new Poll(-2, "Êtes-vous pour ou contre la légalisation du cannabis ?", a1);
			dao.saveOne(p1);
		} catch (DaoException ex) {
			codeErreur = ex.getCode();
		}
		assertEquals(11, codeErreur);
		// Texte manquant
		try {
			ArrayList<Answer> a1 = new ArrayList<Answer>();
			a1.add(new Answer(-1, "Pour"));
			a1.add(new Answer(-1, "Contre"));
			Poll p1 = new Poll(-1, "", a1);
			dao.saveOne(p1);
		} catch (DaoException ex) {
			codeErreur = ex.getCode();
		}
		assertEquals(12, codeErreur);
		// Nombre de réponses insuffisantes (min 2 requis)
		try {
			Poll p1 = new Poll(-1, "Êtes-vous pour ou contre la légalisation du cannabis ?", null);
			dao.saveOne(p1);
		} catch (DaoException ex) {
			codeErreur = ex.getCode();
		}
		assertEquals(13, codeErreur);
	}
}
