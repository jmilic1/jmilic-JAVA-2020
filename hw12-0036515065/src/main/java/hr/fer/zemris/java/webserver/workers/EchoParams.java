package hr.fer.zemris.java.webserver.workers;

import java.io.IOException;
import java.util.Set;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Web Worker outputs parameters to output as a html table.
 * 
 * @author Jura Milić
 *
 */
public class EchoParams implements IWebWorker {

	@Override
	public void processRequest(RequestContext context) throws Exception {
		context.setMimeType("text/html");
		Set<String> names = context.getParameterNames();

		context.write(
				"<html><body><table><thead><tr><th>Parameter name</th><th>Parameter value</th></tr></thead><tbody>");
		for (String name : names) {
			String value = context.getParameter(name);

			try {
				context.write("<tr><td>" + name + "</td><td>" + value + "</td>");
			} catch (IOException ex) {
				throw new RuntimeException("EchoParams error: could not send table elements to output!");
			}
		}
		context.write("</tbody></table></body></html>");
	}
}
