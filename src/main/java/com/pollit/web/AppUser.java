package com.pollit.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
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
import com.pollit.dao.DaoUser;
import com.pollit.entities.User;
import com.pollit.service.IService;
import com.pollit.service.ServiceUser;

/**
 * Servlet implementation class AppUSer
 */
public class AppUser extends HttpServlet {
	private static final long serialVersionUID = 1L;
	// param�tres d'instance
	private String urlErreurs = null;

	private ArrayList erreursInitialisation = new ArrayList<String>();

	private String[] parametres = { "urlConnect", "urlHomePage", "urlSubscribe", "urlErreurs" };

	private Map params = new HashMap<String, String>();
	
	private ServiceUser service;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AppUser() {
        super();
    }
    
	public void init() throws ServletException {
		// on r�cup�re les param�tres d'initialisation de la servlet
		ServletConfig config = getServletConfig();
		// on traite les autres param�tres d'initialisation
		String valeur = null;
		for (int i = 0; i < parametres.length; i++) {
			// valeur du param�tre
			valeur = config.getInitParameter(parametres[i]);
			// param�tre pr�sent ?
			if (valeur == null) {
				// on note l'erreur
				erreursInitialisation.add("Le param�tre [" + parametres[i]
						+ "] n'a pas �t� initialis�");
			} else {
				// on m�morise la valeur du param�tre
				params.put(parametres[i], valeur);
			}
		}
		// l'url de la vue [erreurs] a un traitement particulier
		urlErreurs = config.getInitParameter("urlErreurs");
		if (urlErreurs == null)
			throw new ServletException(
					"Le param�tre [urlErreurs] n'a pas �t� initialis�");
		// instanciation des couches [service] et [dao]
		DaoUser dao = new DaoUser();
		service = new ServiceUser();
		service.setDao(dao);
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// on r�cup�re la m�thode d'envoi de la requ�te
		String method = request.getMethod().toLowerCase();
		// on r�cup�re l'action � ex�cuter
		String action = request.getPathInfo();
		// On v�rifie si l'utilisateur est connect�
		User user = (User)request.getSession().getAttribute("user");
		// action ?
		if (action == null) {
			action = "/connect";
		}
		// ex�cution action
		if (method.equals("get") && action.equals("/connect")) {
			getServletContext()
				.getRequestDispatcher((String)params.get("urlConnect")).forward(
					request, response);
		} else if (method.equals("get") && action.equals("/subscribe")) {
			getServletContext()
				.getRequestDispatcher((String)params.get("urlSubscribe"))
					.forward(request, response);
		} else if (method.equals("get") && action.equals("/homepage")) {
			if (user == null)
				getServletContext()
					.getRequestDispatcher((String)params.get("urlConnect"))
						.forward(request, response);
			else
				getServletContext()
					.getRequestDispatcher((String)params.get("urlHomePage"))
						.forward(request, response);
		} else if (method.equals("post") && action.equals("/validate")) {
			doValidate(request, response);
		} else if (method.equals("post") && action.equals("/validateUser")) {
			doValidateUser(request, response);
		} else {
			// autres cas
			getServletContext()
			.getRequestDispatcher((String)params.get("urlConnect")).forward(
				request, response);
		}
	}
	
	private void doValidate(HttpServletRequest request, HttpServletResponse response)
		throws IOException, ServletException
	{
		// Erreurs dans le formulaire de connexion
		ArrayList<String> errorsList = new ArrayList<String>();
		boolean error = false;
		
		// Le nom d'utilisateur
		String username = request.getParameter("username").trim();
		
		if (username.isEmpty()) {
			errorsList.add("Le nom d'utilisateur est vide.");
			error = true;
		} else request.setAttribute("username", username);
		
		// Le mot de passe
		String password = request.getParameter("password");
		
		if (password.isEmpty()) {
			errorsList.add("Le mot de passe est vide.");
			error = true;
		} else request.setAttribute("password", password);
		
		// Erreurs ?
		if (error) {
			request.setAttribute("errorsList", errorsList);
			getServletContext()
				.getRequestDispatcher((String)params.get("urlConnect"))
					.forward(request, response);
		} else {
			try {
				User user = service.getOne(username, password);
				request.getSession().setAttribute("user", user);
				// On redirige vers la page d'accueil de l'utilisateur
				//getServletContext()
					//.getRequestDispatcher((String)params.get("urlHomePage"))
						//.forward(request, response);
				
				response.sendRedirect("homepage");
			} catch (DaoException e) {
				errorsList.add(e.getMessage());
				request.setAttribute("errorsList", errorsList);
				getServletContext()
					.getRequestDispatcher((String)params.get("urlConnect"))
						.forward(request, response);
			}
		}
	}

	private void doValidateUser(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		// Erreurs dans le formulaire d'inscription
		ArrayList<String> errorsList = new ArrayList<String>();
		boolean error = false;
		
		// Le nom d'utilisateur
		String username = request.getParameter("username").trim();
		
		if (username.isEmpty()) {
			errorsList.add("Le nom d'utilisateur est vide.");
			error = true;
		} else request.setAttribute("username", username);
		
		// Le mot de passe
		String password = request.getParameter("password");
		
		if (password.isEmpty()) {
			errorsList.add("Le mot de passe est vide.");
			error = true;
		} else request.setAttribute("password", password);
		
		// Le mot de passe tap� � nouveau
		String passwordAgain = request.getParameter("passwordAgain");
		
		if (passwordAgain.isEmpty()) {
			errorsList.add("Vous n'avez pas saisi � nouveau le mot de passe.");
			error = true;
		} else request.setAttribute("passwordAgain", passwordAgain);
		
		if (!password.isEmpty() && !passwordAgain.isEmpty() 
				&& !password.equals(passwordAgain)) {
			errorsList.add("Les mots de passe ne co�ncident pas.");
			error = true;
		}
		
		// Le nom
		String lastName = request.getParameter("lastName");
		
		if (lastName.isEmpty()) {
			errorsList.add("Le nom est vide.");
			error = true;
		} else request.setAttribute("lastName", lastName);
		
		// Le pr�nom
		String firstName = request.getParameter("firstName");
		
		if (firstName.isEmpty()) {
			errorsList.add("Le pr�nom est vide.");
			error = true;
		} else request.setAttribute("firstName", firstName);
		
		// La date de naissance
		String day = request.getParameter("day");
		String month = request.getParameter("month");
		String year = request.getParameter("year");
		Calendar dateOfBirth = Calendar.getInstance();
		try {
			dateOfBirth.setLenient(false);
			dateOfBirth.set(Integer.parseInt(year), Integer.parseInt(month),
					Integer.parseInt(day));
			// L�ve une exception si la date est invalide
			dateOfBirth.getTime();
		} catch (Exception e) {
			errorsList.add("La date est invalide.");
			error = true;
		}
		request.setAttribute("day", day);
		request.setAttribute("month", month);
		request.setAttribute("year", year);
		
		// Le genre
		String gender = request.getParameter("gender");
		
		if (gender == null) {
			errorsList.add("Vous n'avez pas s�lectionn� le genre.");
			error = true;
		} else request.setAttribute("gender", gender);
		
		// Erreurs ?
		if (error) {
			request.setAttribute("errorsList", errorsList);
			getServletContext()
				.getRequestDispatcher((String)params.get("urlSubscribe"))
					.forward(request, response);
		} else {
			try {
				User user = new User(username, password, lastName, firstName, dateOfBirth, gender);
				service.insertOne(user);
				request.getSession().setAttribute("user", user);
				// On redirige vers la page d'accueil de l'utilisateur
				getServletContext()
					.getRequestDispatcher((String)params.get("urlHomePage"))
						.forward(request, response);
			} catch (DaoException e) {
				errorsList.add(e.getMessage());
				request.setAttribute("errorsList", errorsList);
				getServletContext()
					.getRequestDispatcher((String)params.get("urlSubscribe"))
						.forward(request, response);
			}
		}
}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// on passe la main au GET
		doGet(request, response);
	}

}
