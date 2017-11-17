<%@ page language="java" pageEncoding="ISO-8859-1" contentType="text/html;charset=ISO-8859-1"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>

<html>
	<head>
		<title>Poll it!</title>
	</head>
	<body>
		<h2>Ajout/Modification d'un sondage</h2>
		<c:if test="${erreurEdit!=''}">
			<h3>Echec de la mise à jour :</h3>
  		L'erreur suivante s'est produite : ${erreurEdit}
			<hr>
		</c:if>
		<form method="post" action="<c:url value="/do/validate"/>">
			<table border="1">
				<tr>
					<td>Id</td>
					<td>${poll.id}</td>
				</tr>
				<tr>
					<td>Version</td>
					<td>${poll.version}</td>
				</tr>
				<tr>
					<td>Question</td>
					<td><input type="text" value="${poll.text}" name="text" size="50"/></td>
					<td>${errorText}</td>
				</tr>
				<tr>
					<td>Réponse 1</td>
					<td><input type="text" value="${poll.answers[0].text}" name="answer1" size="50"/></td>
					<td>${errorAnswers}</td>
				</tr>
				<tr>
					<td>Réponse 2</td>
					<td><input type="text" value="${poll.answers[1].text}" name="answer2" size="50"/></td>
				</tr>
				<tr>
					<td>Réponse 3</td>
					<td><input type="text" value="${poll.answers[2].text}" name="answer3" size="50"/></td>
				</tr>
				<tr>
					<td>Réponse 4</td>
					<td><input type="text" value="${poll.answers[3].text}" name="answer4" size="50"/></td>
				</tr>
			</table>
		
		
			<input type="hidden" value="${poll.id}" name="id">
      		<input type="hidden" value="${poll.version}" name="version">
			<input type="submit" value="Valider">
			<a href="<c:url value="/do/list"/>">Annuler</a>
		</form>
	</body>
</html>