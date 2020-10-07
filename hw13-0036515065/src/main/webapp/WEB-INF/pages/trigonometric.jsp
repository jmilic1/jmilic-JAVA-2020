<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE HTML>
<html>
	<head>
	<style>
	body{
		background-color: ${sessionScope.pickedBgCol}
	}</style>
	</head>
	
	<body>
		<a href="/webapp2">Index</a><br>
		<table border = "1">
			<tr>
				<th>Angle</th>
				<th>Sin</th>
				<th>Cos</th>
			</tr>
			<c:forEach items="${vals}" var="val">
        	<tr>
        		<td>
        			${val.getAngle()}
        		</td>
        		<td>
        			${val.getSin()}
        		</td>
        		<td>
        			${val.getCos()}
        		</td>
        </c:forEach>
		</table>
		
	</body>
</html>