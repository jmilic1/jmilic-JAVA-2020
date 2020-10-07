<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<body>

<a href="../../blog/servleti/main">Index</a>
<% Object id = request.getSession().getAttribute("current.user.id");
Object fn = request.getSession().getAttribute("current.user.fn");
Object ln = request.getSession().getAttribute("current.user.ln");
if (id != null) {
	out.print("<div><p>Your first name is " + fn);
	out.print("<p>Your last name is " + ln);
	out.print("<br><a href=\"../../servleti/logout\">Log out</a></div>");
} %>

<form action="../../newBlog" method="post"> 
Title: <input type="text" name="title"> <br> 
Text: <input type="text" name="text"><br> 
<input type="submit" value="Post"></form>

</body>
</html>
