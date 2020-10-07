<%@ page import="hr.fer.zemris.java.model.BlogEntry" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<body>

<a href="../../blog/servleti/main">Index</a>
<%Object id = request.getSession().getAttribute("current.user.id");
Object fn = request.getSession().getAttribute("current.user.fn");
Object ln = request.getSession().getAttribute("current.user.ln");
Object nick = request.getSession().getAttribute("current.user.nick");
if (id != null) {
	out.print("<div><p>Your first name is " + fn);
	out.print("<p>Your last name is " + ln);
	out.print("<br><a href=\"../../servleti/logout\">Log out</a></div>");
} %>

<%  out.print("<h1>Requested blog:</h1>");
	BlogEntry entry = (BlogEntry) request.getSession().getAttribute("entry");
	if (entry != null){
		out.print("<h2>" + entry.getTitle() + "</h2>");
		out.print("<h3>Posted on " + entry.getCreatedAt() + "</h3>");
		out.print("<h4>Modified on " + entry.getLastModifiedAt() + "</h4>");
		out.print("<h2>By " + entry.getCreator().getNick() + "</h2>");
		out.print("<p>" + entry.getText());
		out.print("<br>");
	} %>
	
<%
	if (nick != null && nick.equals(request.getAttribute("nick"))){
		out.print("<a href=\"../../../servleti/author/" + nick + "/edit\">Edit blog entry</a><br>");
	}
%>

	<p>Comments:</p>
	<c:forEach var="comment" items="${comments}">
		<p>${comment.getMessage()}</p>
		<p>Posted by user with Email: ${comment.getUsersEMail()}</p>
		<p>
			At time: ${comment.getPostedOn()}<br>
			<hr>
	</c:forEach>



<%  
	if (request.getSession().getAttribute("current.user.id") != null){
		out.print("<form action=\"../../newComment\" method=\"post\"> Comment: <input type=\"text\" name=\"message\"> <br> <input type=\"submit\" value=\"Post\"></form>");
	}
	%>
</body>
</html>