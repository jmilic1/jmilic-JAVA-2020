<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<body>

	
	

	<h1>An error occured</h1>
	
	<form action="drawServlet">
	<textarea id="text" name="text">
	${text}
	</textarea>
	<input type="submit" value="Submit">
	</form>
</body>
</html>