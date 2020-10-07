<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session = "true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE HTML>
<html>
	<head>
	<title>OS usage</title>
	<style>
	body{
		background-color: ${sessionScope.pickedBgCol}
	}</style>
	</head>
	
	<body>
	
	<a href="/webapp2">Index</a>
	<p>Here are the results of OS
usage in survey that we completed.”</p>
   
   <img src="dynamicChart">
    
	</body>
</html>