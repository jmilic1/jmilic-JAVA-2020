<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE HTML>
<html>

<body>
	<h1>${poll.getTitle()}</h1>
	<p>${poll.getMessage()}</p>
	<ol>
		<c:forEach items="${pollActualOptions}" var="pollOption">
        	<li><a href="glasanje-glasaj?id=${pollOption.getId()}">${pollOption.getTitle()}</a></li>
        </c:forEach>
        
	</ol>
</body>
</html>