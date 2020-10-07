<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session = "true" %>
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
		<a href="colors.jsp">Background color chooser</a>
		<a href="trigonometric?a=0&b=90">Trigonometric</a>
		<form action="trigonometric" method="GET">
 			Početni kut:<br><input type="number" name="a" min="0" max="360" step="1" value="0"><br>
 			Završni kut:<br><input type="number" name="b" min="0" max="360" step="1" value="360"><br>
 			<input type="submit" value="Tabeliraj"><input type="reset" value="Reset">
		</form>
		<br>
		<a href="stories/funny.jsp">Funny Story</a>
		<br>
		
		<a href="report.jsp">Our OS data usage charts</a>
		<br>
		
		<a href="powers?a=1&b=100&n=3">Powers</a>
		<form action="powers" method="GET">
 			a:<br><input type="number" name="a" min="-100" max="100" step="1" value="0"><br>
 			b:<br><input type="number" name="b" min="-100" max="100" step="1" value="0"><br>
 			n:<br><input type="number" name="n" min="1" max="5" step="1" value="1"><br>
 			<input type="submit" value="Power"><input type="reset" value="Reset">
		</form>
		<br>
		
		<a href="appinfo.jsp">App Info</a>
		<br>
		<a href="glasanje">Band voting</a>
		
	</body>
</html>