<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>

<html>
	<head>
  	<title>Poll it!</title>
  </head>
  <body>
  	<h2>Les erreurs suivantes se sont produites</h2>
    <ul>
			<c:forEach var="erreur" items="${erreurs}">
				<li>${erreur}</li>
			</c:forEach>
    </ul>
  </body>
</html>
