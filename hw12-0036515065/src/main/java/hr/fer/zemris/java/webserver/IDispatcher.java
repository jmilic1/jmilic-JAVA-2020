package hr.fer.zemris.java.webserver;

/**
 * Interface for a request dispatcher whose implementations decide which
 * resource will be delegated the processing of the request.
 * 
 * @author Jura Milić
 *
 */
public interface IDispatcher {
	/**
	 * Dispatches the request based on given urlPath
	 * 
	 * @param urlPath given urlPath
	 * @throws Exception if error occurred
	 */
	void dispatchRequest(String urlPath) throws Exception;
}