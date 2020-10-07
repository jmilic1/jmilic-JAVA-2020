package hr.fer.zemris.java.webserver.workers;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Web Worker which changes the background on index site. It tells user if
 * background was changed successfully by redirecting the user to a side html
 * site.
 * 
 * @author Jura Milić
 *
 */
public class BgColorWorker implements IWebWorker {

	@Override
	public void processRequest(RequestContext context) throws Exception {
		String color = context.getParameter("bgcolor");
		if (color != null) {
			if (color.length() == 6) {
				boolean valid = true;
				for (char c : color.toCharArray()) {
					if (!Character.isDigit(c)) {
						if (c < 'A' || c > 'F') {
							valid = false;
							break;
						}
					}
				}
				if (valid) {
					context.setPersistentParameter("background", color);
					context.setTemporaryParameter("answer", "Background color was set!");
					context.getDispatcher().dispatchRequest("/private/pages/myFile.smscr");
					return;
				}
			}
		}

		context.setTemporaryParameter("answer", "Background color was NOT changed!");
		context.getDispatcher().dispatchRequest("/private/pages/myFile.smscr");
	}
}
