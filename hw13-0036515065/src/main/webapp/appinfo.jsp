<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	session="true"%>
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

	<a href="/webapp2">Index</a>
	
	<p>This website has been running for: <%
		long start = (long) request.getServletContext().getAttribute("begin");
		long now = System.currentTimeMillis();
		long diff = now-start;
		
		int day = (int) diff / (1000 * 3600 * 24);
		int hour = (int) diff / (1000 * 3600) - day*24;
		int minute = (int) diff / (1000 * 60) - hour*60;
		int second = (int) diff / (1000) - minute*60;
		
		out.write(day + " days, " + hour + " hours, " + minute + " minutes and " + second + " seconds");
	%></p>

</body>
</html>