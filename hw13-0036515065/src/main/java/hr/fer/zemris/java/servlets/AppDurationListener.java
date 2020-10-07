package hr.fer.zemris.java.servlets;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * ContextListener used for writing down the time at which the server started
 * running.
 * 
 * @author Jura Milić
 *
 */
@WebListener
public class AppDurationListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		sce.getServletContext().setAttribute("begin", System.currentTimeMillis());
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
	}

}
