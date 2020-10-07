package hr.fer.zemris.java.webserver;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import hr.fer.zemris.java.custom.scripting.exec.SmartScriptEngine;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.webserver.RequestContext.RCCookie;

/**
 * Program which creates a HTTP server on localhost. To access the server you
 * can type www.localhost.com:5721/index.html to get most of the server's
 * functionality. If user wants to access other files available through server
 * they can search for them in browser by typing
 * www.localhost.com:5721/(path_to_file) with folder webroot being the root for
 * these resources. It should be noted that the private folder is not accessible
 * to user.
 * 
 * @author Jura Milić
 *
 */
public class SmartHttpServer {
	/**
	 * Address of server.
	 */
	private String address;
	/**
	 * Domain of server.
	 */
	private String domainName;
	/**
	 * Port of server.
	 */
	private int port;
	/**
	 * Number of threads in thread pool.
	 */
	private int workerThreads;
	/**
	 * Duration of user sessions.
	 */
	private int sessionTimeout;
	/**
	 * Map of mime types keyed by their respective file extensions.
	 */
	private Map<String, String> mimeTypes = new HashMap<String, String>();
	/**
	 * Main thread which runs the server.
	 */
	private ServerThread serverThread;
	/**
	 * Pool of threads.
	 */
	private ExecutorService threadPool;
	/**
	 * Path to root for server resources.
	 */
	private Path documentRoot;
	/**
	 * Map of WebWorkers keyed by the path we want the user to use to access them.
	 */
	private Map<String, IWebWorker> workersMap = new HashMap<String, IWebWorker>();
	/**
	 * Map of session specific variables keyed by their SessionID.
	 */
	private Map<String, SessionMapEntry> sessions = new HashMap<String, SmartHttpServer.SessionMapEntry>();
	/**
	 * Random generator used for making session IDs.
	 */
	private Random sessionRandom = new Random();

	/**
	 * A map entry used for containing a sessions parameters.
	 * 
	 * @author Jura Milić
	 *
	 */
	private static class SessionMapEntry {
		/**
		 * SessionID
		 */
		String sid;
		/**
		 * Host of session.
		 */
		String host;
		/**
		 * Time until this session becomes invalid.
		 */
		long validUntil;
		/**
		 * Permanent parameters for this session.
		 */
		Map<String, String> map;

		/**
		 * Constructs a SessionMapEntry with values set appropriately.
		 * 
		 * @param sid        given SessionID
		 * @param host       given host
		 * @param validUntil given validUntil
		 * @param map        given persistent parameters
		 */
		private SessionMapEntry(String sid, String host, long validUntil, Map<String, String> map) {
			this.sid = sid;
			this.host = host;
			this.validUntil = validUntil;
			this.map = map;
		}
	}

	/**
	 * Constructs a SmartHttpServer with values stored in .properties file of given
	 * path.
	 * 
	 * @param configFileName path to given file
	 */
	public SmartHttpServer(String configFileName) {
		Properties properties = new Properties();
		try {
			properties.load(Files.newInputStream(Paths.get(configFileName)));
		} catch (IOException e) {
			throw new IllegalArgumentException("Could not load config!");
		}

		address = properties.getProperty("server.address");
		domainName = properties.getProperty("server.domainName");
		port = Integer.valueOf(properties.getProperty("server.port"));
		workerThreads = Integer.valueOf(properties.getProperty("server.workerThreads"));
		documentRoot = Paths.get(properties.getProperty("server.documentRoot"));
		sessionTimeout = Integer.valueOf(properties.getProperty("session.timeout"));

		Path mimes = Paths.get(properties.getProperty("server.mimeConfig"));

		String mimeData = "";
		try {
			mimeData = new String(Files.readAllBytes(mimes));
		} catch (IOException e) {
			throw new RuntimeException("Unable to read mime properties!");
		}

		String[] lines = mimeData.split("\\r?\\n");

		for (String line : lines) {
			if (line.length() == 0) {
				continue;
			}

			if (line.startsWith("#")) {
				continue;
			}

			int index = line.indexOf("=");
			String extension = line.substring(0, index - 1);

			String mimeType = line.substring(index + 2);

			if (mimeTypes.containsKey(extension)) {
				throw new RuntimeException("Extension already exists in mime map!");
			}

			mimeTypes.put(extension, mimeType);
		}

		Path workers = Paths.get(properties.getProperty("server.workers"));
		String workerData = "";
		try {
			workerData = new String(Files.readAllBytes(workers));
		} catch (IOException e) {
			throw new RuntimeException("Unable to read worker properties.");
		}

		lines = workerData.split("\\r?\\n");

		for (String line : lines) {
			if (line.length() == 0) {
				continue;
			}

			if (line.startsWith("#")) {
				continue;
			}

			int index = line.indexOf("=");
			String path = line.substring(0, index - 1);
			path = Paths.get(path).toString();

			String fqcn = line.substring(index + 2);

			Object newObject = null;
			try {
				Class<?> referenceToClass = this.getClass().getClassLoader().loadClass(fqcn);

				newObject = referenceToClass.getDeclaredConstructor().newInstance();
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException e) {
				throw new RuntimeException("Unable to make an instance of a worker!");
			}

			IWebWorker iww = (IWebWorker) newObject;

			if (workersMap.containsKey(path)) {
				throw new RuntimeException("Worker already exists!");
			}
			workersMap.put(path, iww);
		}

		serverThread = new ServerThread();
	}

	/**
	 * Starts the server.
	 */
	protected synchronized void start() {
		if (!serverThread.isAlive()) {
			serverThread.start();
		}

		if (threadPool == null) {
			threadPool = Executors.newFixedThreadPool(workerThreads);
		}

		// Thread removes expired sessions every 5 minutes
		Thread myThread = new Thread(() -> {
			while (true) {
				try {
					Thread.sleep(5 * 60 * 60 * 1000);
				} catch (InterruptedException e) {
				}

				for (SessionMapEntry entry : sessions.values()) {
					if (entry.validUntil < System.currentTimeMillis() / 1000) {
						sessions.remove(entry.sid);
					}
				}
			}
		});

		myThread.setDaemon(true);
		myThread.start();
	}

	/**
	 * Shuts the server down.
	 */
	protected synchronized void stop() {
		serverThread.interrupt();
		threadPool.shutdown();
	}

	/**
	 * Thread which starts the server socket and submits a worker to it.
	 * 
	 * @author Jura Milić
	 *
	 */
	protected class ServerThread extends Thread {

		@Override
		public void run() {
			ServerSocket serverSocket = null;
			try {
				serverSocket = new ServerSocket();
				serverSocket.bind(new InetSocketAddress(address, port));

				while (!isInterrupted()) {
					Socket client = serverSocket.accept();
					threadPool.submit(new ClientWorker(client));
				}
			} catch (IOException ex) {
			} finally {
				try {
					serverSocket.close();
				} catch (IOException e) {
				}
			}
		}
	}

	/**
	 * Client worker of server.
	 * 
	 * @author Jura Milić
	 *
	 */
	private class ClientWorker implements Runnable, IDispatcher {
		/**
		 * Socket of server.
		 */
		private Socket csocket;
		/**
		 * Input stream of socket.
		 */
		private InputStream istream;
		/**
		 * Output stream of socket.
		 */
		private OutputStream ostream;
		/**
		 * Version of HTTP request.
		 */
		private String version;
		/**
		 * Method of HTTP request.
		 */
		private String method;
		/**
		 * Server host.
		 */
		private String host;
		/**
		 * Parameters of context.
		 */
		private Map<String, String> params = new HashMap<String, String>();
		/**
		 * Temporary parameters of context.
		 */
		private Map<String, String> tempParams = new HashMap<String, String>();
		/**
		 * Permanent parameters of context.
		 */
		private Map<String, String> permPrams = new HashMap<String, String>();
		/**
		 * Cookies for response header.
		 */
		private List<RCCookie> outputCookies = new ArrayList<RequestContext.RCCookie>();
		/**
		 * Session ID of request.
		 */
		private String SID;
		/**
		 * Context for request.
		 */
		private RequestContext context = null;

		/**
		 * Constructs a client worker with given server socket.
		 * 
		 * @param csocket
		 */
		public ClientWorker(Socket csocket) {
			this.csocket = csocket;
		}

		@Override
		public void run() {
			try {
				istream = new BufferedInputStream(csocket.getInputStream());
				ostream = new BufferedOutputStream(csocket.getOutputStream());
			} catch (IOException e) {
				throw new RuntimeException("Could not open input/output stream!");
			}

			String request = readRequest();
			if (request.length() == 0) {
				try {
					sendError(ostream, 400, "Bad request");
				} catch (IOException e) {
					throw new RuntimeException("Could not send error!");
				}
				return;
			}

			String[] header = request.split("\\r?\\n");

			String firstLine = header[0];
			int index = 0;
			StringBuilder sb = new StringBuilder();

			char[] data = firstLine.toCharArray();
			char c = data[index];
			while (c != ' ') {
				sb.append(c);
				c = data[++index];
			}

			method = sb.toString().trim();
			sb = new StringBuilder();

			c = data[++index];
			while (c != '?' && c != ' ') {
				sb.append(c);
				c = data[++index];
			}

			String path = sb.toString();
			String paramString = null;

			if (c == '?') {
				index++;
				c = data[index];
				sb = new StringBuilder();

				while (c != ' ') {
					sb.append(c);
					c = data[++index];
				}
				paramString = sb.toString();
			}

			sb = new StringBuilder();
			++index;
			while (index < data.length) {
				c = data[index];
				sb.append(c);
				index++;
			}

			version = sb.toString();
			if (version.endsWith("\n")) {
				version = version.substring(0, version.length() - 2);
			}

			if (!method.equals("GET") || (!version.equals("HTTP/1.0") && !version.equals("HTTP/1.1"))) {
				try {
					sendError(ostream, 400, "Wrong version or method!");
				} catch (IOException e) {
					throw new RuntimeException("Could not send error!");
				}
			}

			for (String line : header) {
				if (line.contains("Host:")) {
					String str = line.substring(line.indexOf(":"));
					data = str.toCharArray();
					index = 1;
					c = data[index];
					sb = new StringBuilder();

					if (c == ' ') {
						index++;
						c = data[index];
					}

					if (Character.isDigit(c)) {
						while (Character.isDigit(c) || c == '.') {
							sb.append(c);
							index++;
							if (index == data.length) {
								break;
							}
							c = data[index];
						}
					} else {
						while (!Character.isDigit(c) && c != ':') {
							sb.append(c);
							index++;
							if (index == data.length) {
								break;
							}
							c = data[index];
						}
					}
					host = sb.toString();
					break;
				}
			}

			if (host == null) {
				host = domainName;
			}

			checkSessions(header);
			parseParameters(paramString);

			Path docPath = Paths.get(path);
			try {
				internalDispatchRequest(docPath.toString(), true);
			} catch (Exception e1) {
				throw new RuntimeException("Unable to dispatch request!");
			}
		}

		/**
		 * Reads request. Valid requests are ones that contain a substring "\r\n\r\n".
		 * 
		 * @return the read request turned into a String
		 */
		private String readRequest() {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			int state = 0;

			l: while (true) {
				int b = 0;
				try {
					b = istream.read();
				} catch (IOException e) {
					e.printStackTrace();
				}

				if (b == -1)
					return null;
				if (b != 13) {
					bos.write(b);
				}
				switch (state) {
				case 0:
					if (b == 13) {
						state = 1;
					} else if (b == 10)
						state = 4;
					break;
				case 1:
					if (b == 10) {
						state = 2;
					} else
						state = 0;
					break;
				case 2:
					if (b == 13) {
						state = 3;
					} else
						state = 0;
					break;
				case 3:
					if (b == 10) {
						break l;
					} else
						state = 0;
					break;
				case 4:
					if (b == 10) {
						break l;
					} else
						state = 0;
					break;
				}
			}
			return new String(bos.toByteArray(), StandardCharsets.US_ASCII);
		}

		/**
		 * Sends an error to user.
		 * 
		 * @param os         output stream for sending error
		 * @param statusCode status code of error
		 * @param statusText status text of error
		 * @throws IOException if error could not be sent to user
		 */
		private void sendError(OutputStream os, int statusCode, String statusText) throws IOException {
			os.write(("HTTP/1.1 " + statusCode + " " + statusText + "\r\n" + "Server: simple java server\r\n"
					+ "Content-Type: text/plain;charset=UTF-8\r\n" + "Content-Length: 0\r\n" + "Connection: close\r\n"
					+ "\r\n").getBytes(StandardCharsets.US_ASCII));
			os.flush();
		}

		/**
		 * Parses the parameters from given String
		 * 
		 * @param paramString given String
		 */
		private void parseParameters(String paramString) {
			if (paramString == null) {
				return;
			}
			char[] data = paramString.toCharArray();
			int index = 0;

			while (true) {
				char c = data[index];
				StringBuilder sb = new StringBuilder();
				while (c != '=') {
					if (c != '&') {
						sb.append(c);
					}
					c = data[++index];
				}

				String paramName = sb.toString();
				sb = new StringBuilder();

				while (!(index >= data.length - 1) && c != '&') {
					c = data[++index];
					if (c != '&') {
						sb.append(c);
					}
				}
				String paramVal = sb.toString();
				params.put(paramName, paramVal);
				if (index == data.length - 1) {
					break;
				}
			}
		}

		/**
		 * Checks the session of header request.
		 * 
		 * @param header given header
		 */
		private synchronized void checkSessions(String[] header) {
			String sidCandidate = "";
			for (String line : header) {
				if (line.startsWith("Cookie:")) {
					int index = line.indexOf("sid=");
					if (index != -1) {
						index += 5;
						String tmp = line.substring(index);
						if (tmp.contains("\"")) {
							index = tmp.indexOf("\"");
							sidCandidate = tmp.substring(0, index);
						}
					}
				}
			}

			SID = sidCandidate;

			if (sidCandidate != "") {
				SessionMapEntry entry = sessions.get(sidCandidate);
				if (entry != null) {
					if (entry.host.equals(host)) {
						if (entry.validUntil > System.currentTimeMillis() / 1000) {
							entry.validUntil = System.currentTimeMillis() / 1000 + sessionTimeout;
							permPrams = entry.map;
							return;
						} else {
							sessions.remove(sidCandidate);
						}
					}
				}
			}

			String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < 20; i++) {
				char letter = alphabet.charAt(sessionRandom.nextInt(alphabet.length()));
				sb.append(letter);
			}
			sidCandidate = sb.toString();

			SessionMapEntry entry = new SessionMapEntry(sidCandidate, host,
					System.currentTimeMillis() / 1000 + sessionTimeout, new ConcurrentHashMap<String, String>());

			sessions.put(sidCandidate, entry);
			outputCookies.add(new RCCookie("sid", sidCandidate, null, host, "/"));
			permPrams = entry.map;
			SID = sidCandidate;
		}

		@Override
		public void dispatchRequest(String urlPath) throws Exception {
			internalDispatchRequest(urlPath, false);
		}

		/**
		 * Dispatches a request internally.
		 * 
		 * @param urlPath    given path to resource
		 * @param directCall says whether the dispatch is a direct call or not
		 * @throws Exception if response could not be sent.
		 */
		private void internalDispatchRequest(String urlPath, boolean directCall) throws Exception {
			if (context == null) {
				context = new RequestContext(ostream, params, permPrams, outputCookies, tempParams, this, SID);
			}

			if (urlPath.startsWith("\\private\\") && directCall) {
				sendError(ostream, 404, "Private is not allowed!");
				return;
			}
			if (urlPath.startsWith("\\ext\\")) {
				String fqcn = urlPath.substring(5);
				Object newObject = null;

				try {
					Class<?> referenceToClass = this.getClass().getClassLoader()
							.loadClass("hr.fer.zemris.java.webserver.workers." + fqcn);

					newObject = referenceToClass.getDeclaredConstructor().newInstance();
				} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
					sendError(ostream, 404, "Worker not found!");
				}

				IWebWorker iww = (IWebWorker) newObject;
				context = new RequestContext(ostream, params, permPrams, outputCookies);
				iww.processRequest(context);
				ostream.flush();
				csocket.close();
				return;
			}

			if (workersMap.containsKey(urlPath)) {
				workersMap.get(urlPath).processRequest(context);
				ostream.flush();
				csocket.close();
				return;
			}

			Path docPath = Paths.get(documentRoot.toString() + urlPath.toString());

			if (!docPath.toFile().exists()) {
				try {
					sendError(ostream, 403, "Forbidden");
					return;
				} catch (IOException e) {
					throw new RuntimeException("Unable to send error!");
				}
			}

			if (!docPath.toFile().isFile() || !docPath.toFile().canRead()) {
				try {
					sendError(ostream, 404, "Not found");
					return;
				} catch (IOException e) {
					throw new RuntimeException("Unable to send error!");
				}
			}

			String file = docPath.toString();

			String mime = "";
			int index = file.lastIndexOf(".");
			if (index != -1) {
				mime = file.substring(index + 1);
			}

			if (mime.equals("smscr")) {
				byte[] data = Files.readAllBytes(Paths.get(file));
				String document = new String(data, StandardCharsets.UTF_8);

				context.setMimeType(mimeTypes.get("txt"));
				context.setStatusCode(200);

				new SmartScriptEngine(new SmartScriptParser(document).getDocumentNode(), context).execute();
				ostream.flush();
				csocket.close();
				return;
			}

			String value = mimeTypes.get(mime);
			if (value == null) {
				value = "application/octet-stream";
			}

			context.setMimeType(value);
			context.setStatusCode(200);
			context.setContentLength(Files.size(Paths.get(file)));

			try {
				context.write(Files.readAllBytes(Paths.get(file)));
				ostream.flush();
				csocket.close();
			} catch (IOException e) {
				throw new RuntimeException("Unable to write to context!");
			}
		}
	}

	/**
	 * Start of program.
	 * 
	 * @param args path to configuration file for server
	 */
	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("I expected an argument!");
			System.exit(-1);
		}

		new SmartHttpServer(args[0]).start();
	}
}