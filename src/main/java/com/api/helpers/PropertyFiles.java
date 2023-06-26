package com.api.helpers;

import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;


public class PropertyFiles {

	private static Properties allProp = new Properties();
	private static Properties envProp = new Properties();
	private static Properties apiendpointProp = new Properties();
	private static Logger logger = Logger.getLogger(PropertyFiles.class);

	static {
		try {
			allProp.load(Thread.currentThread().getContextClassLoader()
					.getResourceAsStream(Constants.BASE_PROP_FILE));
		} catch (IOException e) {
			logger.error("could not find file " + Constants.BASE_PROP_FILE, e);
		}

		if (!allProp.containsKey(Constants.ENV_KEY)) {
			allProp.put(Constants.ENV_KEY, Constants.DEFAULT_ENV);
		}

		if (!allProp.containsKey(Constants.FLOW_KEY)) {
			allProp.put(Constants.FLOW_KEY, Constants.DEFAULT_FLOW);
		}

		try {
			envProp.load(Thread
					.currentThread()
					.getContextClassLoader()
					.getResourceAsStream(
							"config/env/" + allProp.getProperty(Constants.ENV_KEY)
							+ "/system.properties"));
		} catch (IOException e) {
			logger.error("could not find file " + "config/env/" + allProp.getProperty(Constants.ENV_KEY)
					+ "/system.properties", e);
		}

		try {
			apiendpointProp.load(Thread
						.currentThread()
						.getContextClassLoader()
						.getResourceAsStream(Constants.APIENDPOINTS_PROP_FILE));
		} catch (IOException e) {
			logger.error("could not find file " + Constants.APIENDPOINTS_PROP_FILE, e);
		}
		
		allProp.putAll(envProp);
		allProp.putAll(apiendpointProp);

	}
	
	
	public static String getProperty(String key) {
		return allProp.getProperty(key);
	}
	
	//DO not create object
	private PropertyFiles() {
		
	}

}
