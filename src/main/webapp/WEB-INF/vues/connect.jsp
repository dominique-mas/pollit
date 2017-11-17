<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>Se connecter à Poll it!</title>
	</head>
	<body>
		<h1>Poll it!</h1><br/>
		<p>Connexion :</p>
		<form method="post" action="<c:url value="/user/validate"/>">
			<table>
				<tr>
					<td>Nom d'utilisateur : </td>
					<td><input type="text" name="username" value="${username}" 
							size="30"/></td>
				</tr>
				<tr>
					<td>Mot de passe : </td>
					<td><input type="password" name="password" value="${password}" 
							size="30"/></td>
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
			<input type="submit" value="Valider">
			<a href="<c:url value="/user/subscribe"/>">Inscription</a>
		</form>
	</body>
</html>