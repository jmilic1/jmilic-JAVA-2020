package hr.fer.zemris.java.webserver;

/**
 * Interface for a Web Worker which can process a request using a
 * RequestContext.
 * 
 * @author Jura Milić
 *
 */
public interface IWebWorker {
	/**
	 * Processes a request using given context.
	 * 
	 * @param context given context
	 * @throws Exception if error occurred
	 */
	public void processRequest(RequestContext context) throws Exception;
}
