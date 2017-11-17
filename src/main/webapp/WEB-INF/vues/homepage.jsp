<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%-- 		<link rel="shortcut icon" href="<c:url value="/img/favicon.gif"/>"> --%>
		
		<link rel="shortcut icon" href="<c:url value="/favicon.ico"/>" />
		
		<title>Poll it!</title>
		<link rel="stylesheet" type="text/css" 
			href="<c:url value="/css/main.css"/>">
	</head>
	<body>
		<div id="banner">
 			<a class="logo" href="<c:url value="/user/homepage"/>">
			</a>
			<a href="<c:url value="/do/listmypolls"/>">
				<span class="optbanner">${user.firstName}</span>
			</a>
			<a href="<c:url value="/do/listtheirpolls"/>">
				<span class="optbanner">J'ai r�pondu � ...</span>
			</a>
			<a href="<c:url value="/user/modify"/>">
				<span class="optbanner">Mon compte</span>
			</a>
			<a href="<c:url value="/user/connect"/>">
				<span class="optbanner">Se d�connecter</span>
			</a>
		</div>
		
		<div id="polls">
			<form method="post" action="<c:url value="/do/validate"/>">
				<div class="poll">
					<textarea class="polltextarea" name="text">Exprimez-vous !</textarea>
					<br/><br/>
					<input class="pollanswer" type="text" name="answer1" value="R�ponse n�1">
					<br/>
					<input class="pollanswer" type="text" name="answer2" value="R�ponse n�2">
					<br/>
					<input class="pollanswer" type="text" name="answer3" value="R�ponse n�3">
					<br/>
					<input class="pollanswer" type="text" name="answer4" value="R�ponse n�4">
					<br/>
				</div>
			</form>
			<div class="poll">
				Post� le 16 d�c. 2016 � 14h38
				<hr/><br/><br/>
				Pour ou contre la 6�me r�publique ?
			</div>
		</div>
	</body>
</html>