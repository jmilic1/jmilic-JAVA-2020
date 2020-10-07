<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<body>

	
	<%
		Object id = request.getSession().getAttribute("current.user.id");
		Object fn = request.getSession().getAttribute("current.user.fn");
		Object ln = request.getSession().getAttribute("current.user.ln");
		if (id != null) {
			out.print("<div><p>Your first name is " + fn);
			out.print("<p>Your last name is " + ln);
			out.print("<br><a href=\"../../blog/servleti/logout\">Log out</a></div>");
		} else {
			out.print("<br><a href=\"register\">If you are a new user register here!</a>");
		}

		Boolean error = (Boolean) request.getAttribute("errorLogin");
		if (error != null) {
			if (error) {
				out.print("<p>Error while logging in!");
			}
		}

		String nick = (String) request.getAttribute("nickname");
		if (nick == null){
			nick = "";
		}
		out.print("<form action=\"main\" method=\"post\"> nickname: <input type=\"text\" name=\"nick\" value=\"" + nick + "\">"
				+ "<br> password: <input type=\"text\" name=\"password\"><br> <input type=\"submit\" value=\"Login\"></form>");
	%>

	<h1>Authors</h1>
	<ul>
		<c:forEach var="author" items="${users}">
			<li><a href="author/${author.getNick()}">${author.getNick()}</a></li>
		</c:forEach>
	</ul>


</body>
</html>
