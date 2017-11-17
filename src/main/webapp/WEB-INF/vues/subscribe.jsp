<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ page import="java.util.Calendar" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>S'inscrire à Poll it!</title>
	</head>
	<body>
		<h1>Poll it!</h1>
		<p>Informations liées au compte (<u>tous les champs sont obligatoires</u>) :<p/>
		<form method="post" action="<c:url value="/user/validateUser"/>">
			<table>
				<tr>
					<td>Nom d'utilisateur : </td>
					<td><input type="text" name="username" value="${username}" size="30"/></td>
				</tr>
				<tr>
					<td>Mot de passe : </td>
					<td><input type="password" name="password" value="${password}" size="30"/></td>
				</tr>
				<tr>
					<td>Confirmez le mot de passe : </td>
					<td><input type="password" name="passwordAgain" value="${passwordAgain}" 
							size="30"/></td>
				</tr>
				<tr>
					<td>Nom : </td>
					<td><input type="text" name="lastName" value="${lastName}" 
							size="30"/></td>
				</tr>
				<tr>
					<td>Prénom : </td>
					<td><input type="text" name="firstName" value="${firstName}" 
							size="30"/></td>
				</tr>
				<tr>
					<td>Date de naissance : </td>
					<td>
						<!-- Jour de naissance -->
						<select name="day">
							<c:forEach var="i" begin="1" end="31">
								<c:choose>
									<c:when test="${day == i}">
										<option value="${i}" selected>${i}</option>
									</c:when>    
									<c:otherwise>
										<option value="${i}">${i}</option>
									</c:otherwise>
								</c:choose>
							</c:forEach>
						</select>
						<!-- Mois de naissance -->
						<select name="month">
							<% String month = (String)request.getAttribute("month");%>
							<option value="0" <% if (month != null && month.equals("0")) {%> 
								selected <% } %>>Janvier</option>
							<option value="1" <% if (month != null && month.equals("1")) {%> 
								selected <% } %>>Février</option>
							<option value="2" <% if (month != null && month.equals("2")) {%> 
								selected <% } %>>Mars</option>
							<option value="3" <% if (month != null && month.equals("3")) {%> 
								selected <% } %>>Avril</option>
							<option value="4" <% if (month != null && month.equals("4")) {%> 
								selected <% } %>>Mai</option>
							<option value="5" <% if (month != null && month.equals("5")) {%> 
								selected <% } %>>Juin</option>
							<option value="6" <% if (month != null && month.equals("6")) {%> 
								selected <% } %>>Juillet</option>
							<option value="7" <% if (month != null && month.equals("7")) {%> 
								selected <% } %>>Aout</option>
							<option value="8" <% if (month != null && month.equals("8")) {%> 
								selected <% } %>>Septembre</option>
							<option value="9" <% if (month != null && month.equals("9")) {%> 
								selected <% } %>>Octobre</option>
							<option value="10" <% if (month != null && month.equals("10")) {%> 
								selected <% } %>>Novembre</option>
							<option value="11" <% if (month != null && month.equals("11")) {%> 
								selected <% } %>>Décembre</option>
						</select>
						<!-- Année de naissance -->
						<select name="year">
							<%
								Calendar c = Calendar.getInstance();
							 	int currentYear = c.get(Calendar.YEAR);
							 	String year = (String)request.getAttribute("year");
								for (int i = currentYear; i > currentYear - 125; i--) {
									if (year != null && i == Integer.parseInt(year)) {
							%>
								<option value="<%= i %>" selected><%= i %></option>
							<%
									} else {
							%>
								<option value="<%= i %>"><%= i %></option>
							<%		}
								}
							%>
						</select>
					</td>
				</tr>
				<tr>
					<td>Genre : </td>
					<td>
						<input type="radio" name="gender" value="male">Homme
						<input type="radio" name="gender" value="female">Femme
						<input type="radio" name="gender" value="other">Autre
					</td>
				</tr>
			</table>
			<br/>
			<c:if test="${errorsList != null && !errorsList.isEmpty()}">
  				<p>
  					Erreur. 
  					<c:forEach var="error" items="${errorsList}">
  						${error} 
  					</c:forEach>
  				</p>
			</c:if>
			<input type="submit" value="Valider" />
			<a href="<c:url value="/user/connect"/>">Annuler</a>
		</form>
	</body>
</html>