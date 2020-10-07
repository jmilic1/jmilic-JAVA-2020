package hr.fer.zemris.java.webserver.workers;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Web worker calculates the sum of two numbers a and b and generates one of two
 * pictures based on whether the sum is an even or odd number.
 * 
 * @author Jura Milić
 *
 */
public class SumWorker implements IWebWorker {

	@Override
	public void processRequest(RequestContext context) throws Exception {
		Integer a = 1;
		Integer b = 2;

		try {
			a = Integer.valueOf(context.getParameter("a"));
		} catch (NumberFormatException ex) {
		}

		try {
			b = Integer.valueOf(context.getParameter("b"));
		} catch (NumberFormatException ex) {
		}
		
		Integer sum = Integer.valueOf(a+b);

		context.setTemporaryParameter("varA", a.toString());
		context.setTemporaryParameter("varB", b.toString());
		context.setTemporaryParameter("zbroj", sum.toString());

		if (sum % 2 == 0) {
			context.setTemporaryParameter("imgName", "images/fruits.png");
		} else {
			context.setTemporaryParameter("imgName", "images/gimli.png");
		}
		context.getDispatcher().dispatchRequest("/private/pages/calc.smscr");
	}
}
