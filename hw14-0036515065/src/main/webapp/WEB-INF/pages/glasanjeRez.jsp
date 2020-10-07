<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE HTML>
<html>
<body>

<a href="../index.html">Index</a> <br>

	<h1>Rezultati glasanja</h1>
	<p>Ovo su rezultati glasanja.</p>
	<table border="1" class="rez">
		<thead>
			<tr>
				<th>Opcije Ankete</th>
				<th>Broj glasova</th>
			</tr>
		</thead>
		<tbody>
		
		<c:forEach items="${votes}" var="vote">
        	<tr>
        		<td>
        			${vote.getTitle()}
        		</td>
        		<td>
        			${vote.getVotesCount()}
        		</td>
        	</tr>
        </c:forEach>
		
		</tbody>
	</table>

	<h2>Grafički prikaz rezultata</h2>
	<img alt="Pie-chart" src="glasanje-grafika" width="400" height="400" />

	<h2>Rezultati u XLS formatu</h2>
	<p>
		Rezultati u XLS formatu dostupni su <a href="glasanje-xls">ovdje</a>
	</p>

	<h2>Razno</h2>
	<p>Primjeri pobjednika:</p>
	<ul>
		
		<c:forEach items="${bestVotes}" var="vote">
        	<li>
        		<a href="${ vote.getLink() }" target="_blank">${ vote.getTitle() }</a></li>
        </c:forEach>
	</ul>
</body>
</html>