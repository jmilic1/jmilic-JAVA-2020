<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session = "true" import="java.util.Random"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE HTML>
<html>

<head>
	<style>
	body{
		background-color: <c:choose>
							<c:when test="${sessionScope.pickedBgCol==null}">
								white
							</c:when>
							<c:otherwise>
								${sessionScope.pickedBgCol}
							</c:otherwise>
						  </c:choose>
	}</style>
	</head>
	
	<body>
		
		<a href="/webapp2">Index</a>
		
		
		<p style="color:  <% 
		Random rand = new Random();

		switch (Math.abs(rand.nextInt() % 4)) {
		case (1):
			out.write("blue");
			break;
		case (2):
			out.write("pink");
			break;
		case (3):
			out.write("purple");
			break;
		case(0):
			out.write("red");
			break;
		}%>">What do you call a bee that can't make up its mind? A Maybe</p>
	</body>
</html>