package hr.fer.zemris.java.webserver;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Class which represents context for a user request for SmartHttpServer.
 * 
 * @author Jura Milić
 *
 */
public class RequestContext {
	/**
	 * A class which models a cookie of a request context.
	 * 
	 * @author Jura Milić
	 *
	 */
	public static class RCCookie {
		/**
		 * Name of cookie
		 */
		private String name;
		/**
		 * Value of cookie
		 */
		private String value;
		/**
		 * Domain of cookie
		 */
		private String domain;
		/**
		 * Path of cookie
		 */
		private String path;
		/**
		 * Max Age of cookie
		 */
		private Integer maxAge;

		/**
		 * Constructs an RCCookie if values set appropriately.
		 * 
		 * @param name   given name
		 * @param value  given value
		 * @param maxAge given Max Age
		 * @param domain given domain
		 * @param path   given path
		 */
		public RCCookie(String name, String value, Integer maxAge, String domain, String path) {
			this.name = name;
			this.value = value;
			this.domain = domain;
			this.path = path;
			this.maxAge = maxAge;
		}

		/**
		 * Returns name of cookie
		 * 
		 * @return name
		 */
		public String getName() {
			return name;
		}

		/**
		 * Returns value of cookie
		 * 
		 * @return value
		 */
		public String getValue() {
			return value;
		}

		/**
		 * Returns domain of cookie
		 * 
		 * @return domain
		 */
		public String getDomain() {
			return domain;
		}

		/**
		 * Returns path of cookie
		 * 
		 * @return path
		 */
		public String getPath() {
			return path;
		}

		/**
		 * Returns max age of cookie
		 * 
		 * @return max age
		 */
		public Integer getMaxAge() {
			return maxAge;
		}
	}

	/**
	 * OutputStream which sends server response to user.
	 */
	private OutputStream outputStream;
	/**
	 * Charset of server response.
	 */
	private Charset charset;

	/**
	 * Encoding of charset.
	 */
	public String encoding = "UTF-8";
	/**
	 * Status code of response.
	 */
	public int statusCode = 200;
	/**
	 * Status text of response.
	 */
	public String statusText = "OK";
	/**
	 * Mime type of response.
	 */
	public String mimeType = "text/html";
	/**
	 * Length of response
	 */
	public Long contentLength = null;
	
	/**
	 * Map of response parameters.
	 */
	private Map<String, String> parameters;
	/**
	 * Map of temporary response parameters.
	 */
	private Map<String, String> temporaryParameters;
	/**
	 * Map of persistent response parameters. These parameters are stored within a
	 * session.
	 */
	private Map<String, String> persistentParameters;
	/**
	 * Cookies to be added in response header
	 */
	private List<RCCookie> outputCookies;
	/**
	 * Internal dispatcher that uses this request context.
	 */
	private IDispatcher dispatcher;
	/**
	 * Keeps track if this context sent a header to user.
	 */
	private boolean headerGenerated = false;
	/**
	 * ID of session.
	 */
	private String sessionID;

	/**
	 * Constructs a RequestContext with temporaryParameters, dispatcher and
	 * sessionID set to null and other values set appropriately.
	 * 
	 * @param outputStream         given output stream
	 * @param parameters           given parameters
	 * @param persistentParameters given persistent parameters
	 * @param outputCookies        given output cookies
	 */
	public RequestContext(OutputStream outputStream, Map<String, String> parameters,
			Map<String, String> persistentParameters, List<RCCookie> outputCookies) {
		this(outputStream, parameters, persistentParameters, outputCookies, null, null, null);
	}

	/**
	 * Constructs a RequestContext with values set appropriately.
	 * 
	 * @param outputStream         given output stream
	 * @param parameters           given parameters
	 * @param persistentParameters given persistent parameters
	 * @param outputCookies        given output cookies
	 * @param temporaryParameters  given temporary parameters
	 * @param dispatcher           given dispatcher
	 * @param sessionID            given session ID
	 */
	public RequestContext(OutputStream outputStream, Map<String, String> parameters,
			Map<String, String> persistentParameters, List<RCCookie> outputCookies,
			Map<String, String> temporaryParameters, IDispatcher dispatcher, String sessionID) {
		if (outputStream == null) {
			throw new NullPointerException();
		}
		this.outputStream = outputStream;
		this.parameters = parameters;
		this.persistentParameters = persistentParameters;
		this.outputCookies = outputCookies;
		this.temporaryParameters = temporaryParameters;
		this.dispatcher = dispatcher;
		this.sessionID = sessionID;
		charset = Charset.forName(encoding);
	}

	/**
	 * Sets encoding which will be used in header generation.
	 * 
	 * @param encoding given encoding
	 * @throws RuntimeException if header was already generated.
	 */
	public void setEncoding(String encoding) {
		if (headerGenerated) {
			throw new RuntimeException("Header already generated!");
		}
		this.encoding = encoding;
		charset = Charset.forName(encoding);
	}

	/**
	 * Sets status code of header.
	 * 
	 * @param statusCode given status code
	 * @throws RuntimeException if header was already generated.
	 */
	public void setStatusCode(int statusCode) {
		if (headerGenerated) {
			throw new RuntimeException("Header already generated!");
		}
		this.statusCode = statusCode;
	}

	/**
	 * Sets status text of header.
	 * 
	 * @param statusText given status text
	 * @throws RuntimeException if header was already generated
	 */
	public void setStatusText(String statusText) {
		if (headerGenerated) {
			throw new RuntimeException("Header already generated!");
		}
		this.statusText = statusText;
	}

	/**
	 * Sets the mime type of response which will be used in header generation.
	 * 
	 * @param mimeType given mime of response
	 * @throws RuntimeException if header was already generated
	 */
	public void setMimeType(String mimeType) {
		if (headerGenerated) {
			throw new RuntimeException("Header already generated!");
		}
		this.mimeType = mimeType;
	}

	/**
	 * Sets the length of response which is used in header generation.
	 * 
	 * @param contentLength given length of content
	 * @throws RuntimeException if header was already generated
	 */
	public void setContentLength(Long contentLength) {
		if (headerGenerated) {
			throw new RuntimeException("Header already generated!");
		}
		this.contentLength = contentLength;
	}

	/**
	 * Returns the value of given parameter.
	 * 
	 * @param name given parameter's name
	 * @return parameter value
	 */
	public String getParameter(String name) {
		if (parameters == null) {
			return null;
		}

		return parameters.get(name);
	}

	/**
	 * Returns a set of parameter names currently stored as context parameters.
	 * 
	 * @return read-only set of parameter names
	 */
	public Set<String> getParameterNames() {
		Set<String> set = parameters.keySet();
		return Collections.unmodifiableSet(set);
	}

	/**
	 * Returns the value of given persistent parameter.
	 * 
	 * @param name given persistent parameter's name
	 * @return value of persistent parameter
	 */
	public String getPersistentParameter(String name) {
		if (persistentParameters == null) {
			return null;
		}

		return persistentParameters.get(name);
	}

	/**
	 * Returns a set of persistent parameter names currently stored as context
	 * persistent parameters.
	 * 
	 * @return read-only set of persistent parameter names
	 */
	public Set<String> getPersistentParameterNames() {
		Set<String> set = persistentParameters.keySet();
		return Collections.unmodifiableSet(set);
	}

	/**
	 * Adds a new persistent parameter to context
	 * 
	 * @param name  name of new persistent parameter
	 * @param value value of new persistent parameter
	 */
	public void setPersistentParameter(String name, String value) {
		persistentParameters.put(name, value);
	}

	/**
	 * Remove a persistent parameter from context
	 * 
	 * @param name given persistent parameter name to be removed
	 */
	public void removePersistentParameter(String name) {
		persistentParameters.remove(name);
	}

	/**
	 * Returns value of given temporary parameter.
	 * 
	 * @param name given temporary parameter's name
	 * @return value of temporary parameter
	 */
	public String getTemporaryParameter(String name) {
		if (temporaryParameters == null) {
			return null;
		}

		return temporaryParameters.get(name);
	}

	/**
	 * Returns a set of temporary parameter names currently stored as context
	 * temporary parameters.
	 * 
	 * @return read-only set of temporary parameter names
	 */
	public Set<String> getTemporaryParameterNames() {
		Set<String> set = temporaryParameters.keySet();
		return Collections.unmodifiableSet(set);
	}

	/**
	 * Returns the session ID of this context
	 * 
	 * @return session ID
	 */
	public String getSessionID() {
		return sessionID;
	}

	/**
	 * Adds a new temporary parameter to this context
	 * 
	 * @param name  name of new temporary parameter
	 * @param value value of new temporary parameter
	 */
	public void setTemporaryParameter(String name, String value) {
		if (temporaryParameters == null) {
			temporaryParameters = new HashMap<String, String>();
		}
		temporaryParameters.put(name, value);
	}

	/**
	 * Removes a temporary parameter from this context.
	 * 
	 * @param name name of given temporary parameter to be removed.
	 */
	public void removeTemporaryParameter(String name) {
		persistentParameters.remove(name);
	}

	/**
	 * Writes given data to output stream. Method first generates a header if it was
	 * not already written to output stream.
	 * 
	 * @param data given data
	 * @return this context
	 * @throws IOException if header or data could not be sent to stream
	 */
	public RequestContext write(byte[] data) throws IOException {
		if (!headerGenerated) {
			writeHeader();
		}
		outputStream.write(data);
		return this;
	}

	/**
	 * Writes part of given data to output stream based on given offset and length.
	 * Method first generates a header if it was not already written to output
	 * stream.
	 * 
	 * @param data   given data
	 * @param offset given offset from data
	 * @param len    given length of data to be sent
	 * @return this context
	 * @throws IOException if header or data could not be sent to stream
	 */
	public RequestContext write(byte[] data, int offset, int len) throws IOException {
		byte[] myData = new byte[len];
		for (int i = 0; i < len; i++) {
			myData[i] = myData[offset + i];
		}

		if (!headerGenerated) {
			writeHeader();
		}
		return this;
	}

	/**
	 * Writes given text to output stream. Method first generates a header if it was
	 * not already written to output stream.
	 * 
	 * @param text given text
	 * @return this context
	 * @throws IOException if text or header could not be sent to stream
	 */
	public RequestContext write(String text) throws IOException {
		if (!headerGenerated) {
			writeHeader();
		}

		outputStream.write(text.getBytes(charset));
		return this;
	}

	/**
	 * Adds a cookie to context which will be used in header
	 * 
	 * @param cookie given cookie to be added
	 * @throws RuntimeException if header was already generated
	 */
	public void addRCCookie(RCCookie cookie) {
		if (headerGenerated) {
			throw new RuntimeException("Header already generated!");
		}
		outputCookies.add(cookie);
	}

	/**
	 * Returns the dispatcher of this context
	 * 
	 * @return dispatcher
	 */
	public IDispatcher getDispatcher() {
		return dispatcher;
	}

	/**
	 * Writes the header for this context to output stream based on status code,
	 * status text, mime type of the response data, charset of data response, length
	 * of data response and context cookies. Header is generated in ISO_8859_1
	 * format.
	 * 
	 * @throws IOException if header could not be sent to stream
	 */
	private void writeHeader() throws IOException {
		StringBuilder sb = new StringBuilder();
		sb.append("HTTP/1.1 " + statusCode + " " + statusText + "\r\n");
		sb.append("Content-Type: " + mimeType);
		if (mimeType.startsWith("text/")) {
			sb.append("; charset=" + encoding);
		}
		sb.append("\r\n");
		if (contentLength != null) {
			sb.append("Content-Length: " + contentLength + "\r\n");
		}

		for (RCCookie cookie : outputCookies) {
			sb.append("Set-Cookie: " + cookie.getName());
			sb.append("=\"" + cookie.getValue() + "\"");

			if (cookie.getDomain() != null) {
				sb.append("; Domain=" + cookie.getDomain());
			}
			if (cookie.getPath() != null) {
				sb.append("; Path=" + cookie.getPath());
			}
			if (cookie.getMaxAge() != null) {
				sb.append("; Max-Age=" + cookie.getMaxAge());
			}
			sb.append("; HttpOnly");
			sb.append("\r\n");
		}
		sb.append("\r\n");

		outputStream.write(sb.toString().getBytes(StandardCharsets.ISO_8859_1));

		headerGenerated = true;
	}
}