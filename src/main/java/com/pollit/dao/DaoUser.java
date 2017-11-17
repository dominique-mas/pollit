/**
 * 
 */
package com.pollit.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.pollit.entities.User;

/**
 * @author Dominique Mas
 *
 */
public class DaoUser {

	private String url = "jdbc:mysql://localhost:3306/pollit";
	private String login = "root";
	private String password = "1Pz,8sN!";
	private Connection cn = null;
	private Statement st = null;
	
	private void init() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			cn = DriverManager.getConnection(url, login, password);
			st = cn.createStatement();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void close() {
		try {
			cn.close();
			st.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public User getOne(String username, String passwordTyped) {
		
		init();
		
		try {
			String sql = "SELECT * FROM users WHERE username='" + username + "'";
			ResultSet rs = st.executeQuery(sql);
			// Utilisateur non trouvé
			if (!rs.next()) 
				throw new DaoException("Utilisateur [" + username + "] non trouvé", 101);
			String passwordInDB = rs.getString("password");
			if (!passwordTyped.equals(passwordInDB))
				throw new DaoException("Mot de passe incorrect pour l'utilisateur [" + username
						+ "]", 102);
			String lastname = rs.getString("lastname");
			String firstname = rs.getString("firstname");
			Calendar dateOfBirth = Calendar.getInstance();
			dateOfBirth.setTime(rs.getDate("date_of_birth"));
			String gender = rs.getString("gender");
			return new User(username, passwordTyped, lastname, firstname, dateOfBirth, gender);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return null;
	}

	public void insertOne(User user) {
		if (user == null) throw new DaoException("Utilisateur null", 103);
		
		if (user.getUsername() == null || "".equals(user.getUsername().trim()))
			throw new DaoException("Le nom d'utilisateur est vide", 104);
		
		init();
		try {
			String sql = "SELECT * FROM users WHERE username='" + user.getUsername() + "'";
			ResultSet rs = st.executeQuery(sql);
			// La requête a renvoyé une ligne ; le nom d'utilisateur existe déjà
			if (rs.next()) throw new DaoException("Le nom d'utilisateur existe déjà", 105);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if (user.getPassword() == null || "".equals(user.getPassword().trim()))
			throw new DaoException("Le mot de passe est vide", 106);
		
		if (user.getLastName() == null || "".equals(user.getLastName().trim()))
			throw new DaoException("Le nom est vide", 107);
		
		if (user.getFirstName() == null || "".equals(user.getFirstName().trim()))
			throw new DaoException("Le prénom est vide", 108);
		
		if (user.getDateOfBirth() == null)
			throw new DaoException("Le date est vide", 109);
		
		if (user.getGender() == null || "".equals(user.getGender().trim()))
			throw new DaoException("Le genre n'est pas précisé", 110);
		
		try {
			// Formatage de la date
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			
			String sql = "INSERT INTO users VALUES ('" + user.getUsername() + "', '"
					+ user.getPassword() + "', '" + user.getLastName() + "', '"
					+ user.getFirstName() + "', '" + format.format(user.getDateOfBirth().getTime()) + "', '"
					+ user.getGender() + "')";
			st.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
	}

	public void deleteOne(String username) {
		// TODO Auto-generated method stub
		
	}

}
