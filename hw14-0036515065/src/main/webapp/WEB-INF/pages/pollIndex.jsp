<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE HTML>
<html>

<head>
</head>

<body>
	<h1>Odaberite za što želite glasati:</h1>
	<ol>
		<c:forEach items="${polls}" var="poll">
        	<li><a href="glasanje?pollID=${poll.getId()} ">${poll.getTitle()}</a></li>
        </c:forEach>
        
	</ol>
</body>
</html>