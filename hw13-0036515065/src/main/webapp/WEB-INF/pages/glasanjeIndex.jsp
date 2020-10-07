<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE HTML>
<html>

<head>
<style>
body {
	background-color: ${sessionScope.pickedBgCol}
}
</style>
</head>

<body>
	<h1>Glasanje za omiljeni bend:</h1>
	<p>Od sljedećih bendova, koji Vam je bend najdraži? Kliknite na
		link kako biste glasali!</p>
	<ol>
		<c:forEach items="${bands}" var="band">
        	<li><a href="glasanje-glasaj?id= ${band.getId()} ">${band.getBandName()}</a></li>
        </c:forEach>
        
	</ol>
</body>
</html>