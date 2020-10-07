<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<body>


	<a href="../../main">Index</a>
	<%
		Object id = request.getSession().getAttribute("current.user.id");
		Object fn = request.getSession().getAttribute("current.user.fn");
		Object ln = request.getSession().getAttribute("current.user.ln");
		Object nick = request.getSession().getAttribute("current.user.nick");
		if (id != null) {
			out.print("<div><p>Your first name is " + fn);
			out.print("<p>Your last name is " + ln);
			out.print("<br><a href=\"../../servleti/logout\">Log out</a></div>");
		}
	%>

	<h1>blogs</h1>
	<ul>
		<c:forEach var="entry" items="${entries}">
			<li><a href="${nick}/${entry.getId()}">${entry.getTitle()}</a></li>
		</c:forEach>
	</ul>


	<%
		if (id != null && nick.equals(request.getAttribute("nick"))) {
			out.print("<a href=\"" + nick + "/new\">Add new blog entry</a>");
		}
	%>

</body>
</html>
