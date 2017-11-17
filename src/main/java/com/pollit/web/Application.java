package com.pollit.web;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

import com.pollit.dao.DaoException;
import com.pollit.dao.DaoImpl;
import com.pollit.entities.Answer;
import com.pollit.entities.Poll;
import com.pollit.service.IService;
import com.pollit.service.ServiceImpl;
import com.pollit.util.EmptyStringPredicate;

@SuppressWarnings("serial")
public class Application extends HttpServlet {
	// paramètres d'instance
	private String urlErreurs = null;

	private ArrayList erreursInitialisation = new ArrayList<String>();

	private String[] parametres = { "urlList", "urlEdit", "urlErreurs" };

	private Map params = new HashMap<String, String>();

	// service
	IService service = null;

	// init
	@SuppressWarnings("unchecked")
	public void init() throws ServletException {
		// on récupère les paramètres d'initialisation de la servlet
		ServletConfig config = getServletConfig();
		// on traite les autres paramètres d'initialisation
		String valeur = null;
		for (int i = 0; i < parametres.length; i++) {
			// valeur du paramètre
			valeur = config.getInitParameter(parametres[i]);
			// paramètre présent ?
			if (valeur == null) {
				// on note l'erreur
				erreursInitialisation.add("Le paramètre [" + parametres[i]
						+ "] n'a pas été initialisé");
			} else {
				// on mémorise la valeur du paramètre
				params.put(parametres[i], valeur);
			}
		}
		// l'url de la vue [erreurs] a un traitement particulier
		urlErreurs = config.getInitParameter("urlErreurs");
		if (urlErreurs == null)
			throw new ServletException(
					"Le paramètre [urlErreurs] n'a pas été initialisé");
		// instanciation de la couche [service]
		service = (IService) new XmlBeanFactory(new ClassPathResource("spring-config.xml"))
				.getBean("service");
	}

	// GET
	@SuppressWarnings("unchecked")
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		// on vérifie comment s'est passée l'initialisation de la servlet
		if (erreursInitialisation.size() != 0) {
			// on passe la main à la page d'erreurs
			request.setAttribute("erreurs", erreursInitialisation);
			getServletContext().getRequestDispatcher(urlErreurs).forward(
					request, response);
			// fin
			return;
		}
		// on récupère la méthode d'envoi de la requête
		String method = request.getMethod().toLowerCase();
		// on récupère l'action à exécuter
		String action = request.getPathInfo();
		// action ?
		if (action == null) {
			action = "/list";
		}
		// exécution action
		if (method.equals("get") && action.equals("/list")) {
			// liste des sondages
			doListPolls(request, response);
			return;
		}
		if (method.equals("get") && action.equals("/delete")) {
			// suppression d'un sondage
			doDeletePoll(request, response);
			return;
		}
		if (method.equals("get") && action.equals("/edit")) {
			// présentation formulaire ajout / modification d'un sondage
			doEditPoll(request, response);
			return;
		}
		if (method.equals("post") && action.equals("/validate")) {
			// validation formulaire ajout / modification d'une personne
			doValidatePoll(request, response);
			return;
		}
		if (method.equals("post") && action.equals("/vote")) {
			// Vote pour un sondage
			doVotePoll(request, response);
			return;
		}
		// autres cas
		doListPolls(request, response);
	}

	// affichage liste des sondages
	private void doListPolls(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// le modèle de la vue [list]
		request.setAttribute("polls", service.getAll());
		request.setAttribute("errorVote", "");
		// affichage de la vue [list]
		getServletContext()
				.getRequestDispatcher((String) params.get("urlList")).forward(
						request, response);
	}

	// modification / ajout d'un sondage
	private void doEditPoll(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// on récupère l'id du sondage
		long id = Long.parseLong(request.getParameter("id"));
		// ajout ou modification ?
		Poll poll = null;
		if (id != -1) {
			// modification - on récupère le sondage à modifier
			poll = service.getOne(id);
		} else {
			// ajout - on crée un sondage vide
			poll = new Poll();
			poll.setId(-1);
		}
		// on met l'objet [Poll] dans le modèle de la vue [edit]
		request.setAttribute("erreurEdit", "");
		request.setAttribute("poll", poll);
		// affichage de la vue [edit]
		getServletContext()
				.getRequestDispatcher((String) params.get("urlEdit")).forward(
						request, response);
	}

	// suppression d'un sondage
	private void doDeletePoll(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// on récupère l'id du sondage
		long id = Long.parseLong(request.getParameter("id"));
		// on supprime le sondage
		service.deleteOne(id);
		// on redirige vers la liste des personnes
		response.sendRedirect("list");
	}

	// validation modification / ajout d'un sondage
	public void doValidatePoll(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// on récupère les éléments postés
		boolean wrongForm = false;
		boolean error;
		// le texte
		String text = request.getParameter("text").trim();
		// texte valide ?
		if (text.length() == 0) {
			// on note l'erreur
			request.setAttribute("errorText", "La question est obligatoire");
			wrongForm = true;
		}
		// les réponses
		String answer1 = request.getParameter("answer1").trim();
		String answer2 = request.getParameter("answer2").trim();
		String answer3 = request.getParameter("answer3").trim();
		String answer4 = request.getParameter("answer4").trim();
		ArrayList<String> stringAnswers = new ArrayList<String>();
		stringAnswers.add(answer1);
		stringAnswers.add(answer2);
		stringAnswers.add(answer3);
		stringAnswers.add(answer4);
		stringAnswers.removeIf(new EmptyStringPredicate<String>());
		// réponses valides ?
		if (stringAnswers.size() < 2) {
			request.setAttribute("errorAnswers", "Il faut au minimum 2 réponses");
			wrongForm = true;
		}
		ArrayList<Answer> answers = new ArrayList<Answer>();
		for (String s : stringAnswers) {
			answers.add(new Answer(-1, s));
		}
		// id du sondage
		long id = Long.parseLong(request.getParameter("id"));
		// version
		long version = Long.parseLong(request.getParameter("version"));
		// le formulaire est-il erroné ?
		if (wrongForm) {
			// on réaffiche le formulaire avec les messages d'erreurs
			showForm(request, response, "");
			// fini
			return;
		}
		// le formulaire est correct - on enregistre le sondage
		Poll poll = new Poll(id, text, answers);
		poll.setVersion(version);
		try {
			// enregistrement
			service.saveOne(poll);
		} catch (DaoException ex) {
			// on réaffiche le formulaire avec le message de l'erreur survenue
			showForm(request, response, ex.getMessage());
			// fini
			return;
		}
		// on redirige vers la liste des sondages
		response.sendRedirect("list");
	}
	
	// Vote pour un sondage
	public void doVotePoll(HttpServletRequest request,
			HttpServletResponse response) 
					throws ServletException, IOException {
		
		long pollId = Long.parseLong(request.getParameter("id"));
		// On vérifie que l'utilisateur a sélectionné une réponse
		if (request.getParameter("answer") == null) {
			// le modèle de la vue [list]
			request.setAttribute("errorPollId", pollId);
			request.setAttribute("errorVote", "vous devez sélectionner une réponse !");
			request.setAttribute("polls", service.getAll());
			// affichage de la vue [list] avec l'erreur
			getServletContext()
					.getRequestDispatcher((String) params.get("urlList")).forward(
							request, response);
		} else {
			request.setAttribute("erreurEdit", "");
			long answerId = Long.parseLong(request.getParameter("answer"));
			service.vote(pollId, answerId);
			// on redirige vers la liste des sondages
			response.sendRedirect("list");
		}
	}

	// affichage formulaire pré-rempli
	private void showForm(HttpServletRequest request,
			HttpServletResponse response, String erreurEdit)
			throws ServletException, IOException {
		// on prépare le modèle de la vue [edit]
		request.setAttribute("erreurEdit", erreurEdit);
		ArrayList<Answer> answers = new ArrayList<Answer>();
		answers.add(new Answer(request.getParameter("answer1")));
		answers.add(new Answer(request.getParameter("answer2")));
		answers.add(new Answer(request.getParameter("answer3")));
		answers.add(new Answer(request.getParameter("answer4")));
		Poll p = new Poll(Long.parseLong(request.getParameter("id")), 
				request.getParameter("text"), answers);
		p.setVersion(Long.parseLong(request.getParameter("version")));
		request.setAttribute("poll", p);
		// affichage de la vue [edit]
		getServletContext()
				.getRequestDispatcher((String) params.get("urlEdit")).forward(
						request, response);
	}

	// post
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		// on passe la main au GET
		doGet(request, response);
	}

}
