<%@ page language="java" pageEncoding="ISO-8859-1" contentType="text/html;charset=ISO-8859-1"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>

<html>
	<head>
		<title>Poll it!</title>
	</head>
	<body>
		<h2>Liste des sondages</h2>
		<c:forEach var="poll" items="${polls}">
			<div>
				<form method="post" action="<c:url value="/do/vote"/>">
					<c:if test="${errorVote != '' && errorPollId == poll.id}">
						<span>Erreur : ${errorVote}</span><br/>
					</c:if>
					<c:out value="${poll.text}"/><br/>
					<c:forEach var="answer" items="${poll.answers}">
						<input type="radio" name="answer" value="${answer.id}">
						<c:out value="${answer.text}"/>
						(<c:out value="${answer.nbVotes}"/>)<br/>
					</c:forEach>
					<input type="submit" value="Voter !">
					<a href="<c:url value="/do/edit?id=${poll.id}"/>">Modifier</a>
					<a href="<c:url value="/do/delete?id=${poll.id}"/>">Supprimer</a>
					<input type="hidden" value="${poll.id}" name="id">
				</form>
			</div>
		</c:forEach>
		<br/>
		<a href="<c:url value="/do/edit?id=-1"/>">Ajout</a>
	</body>
</html>
