<%@ page language="java" pageEncoding="ISO-8859-1" contentType="text/html;charset=ISO-8859-1"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ page isErrorPage="true" %>

<%
  response.setStatus(200);
%>

<html>
	<head>
		<title>Poll it!</title>
	</head>
	<body>
		<h2>Poll it!</h2>
		L'exception suivante s'est produite :
		<%= exception.getMessage()%>
		<br/><br/>
		<a href="<c:url value="/do/list"/>">Retour &agrave; la liste</a>
	</body>
</html>
