package fr.insee.omphale.utilitaireDuGroupeJava2010.helper;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import fr.insee.omphale.utilitaireDuGroupeJava2010.util.ContexteApplication;

public class StartUpListener implements ServletContextListener {

	public StartUpListener() {
	}

	public void contextDestroyed(ServletContextEvent servletcontextevent) {
	}

	public void contextInitialized(ServletContextEvent sce) {
		ContexteApplication.getInstance();
	}
}