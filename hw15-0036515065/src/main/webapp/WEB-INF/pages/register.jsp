<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<body>
<a href="main">Index</a>
<%
		Boolean error = (Boolean) request.getAttribute("errorRegister");
		if (error != null) {
			if (error) {
				out.print("<p>Nickname already exists!");
			}
		}

	%>
	<p>Registration form for new users:</p>
	
	<form action="register" method="post">
		First Name: <input type="text" name="firstName"> <br>
		Last Name: <input type="text" name="lastName"> <br> 
		email: <input type="text" name="email"> <br> 
		nick: <input type="text" name="nick"> <br> 
		password: <input type="text" name="password"><br> <input type="submit">
	</form>


</body>
</html>