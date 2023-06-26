package com.api.helpers;



public class Constants {
	
	public static final String DEFAULT_ENV = "stg";
	public static final String DEFAULT_FLOW = "console";
	public static final String BASE_PROP_FILE="config/base.properties";
	public static final String APIENDPOINTS_PROP_FILE="config/apiendpoints.properties";
	public static final String ENV_KEY = "env";
	public static final String FLOW_KEY = "flow";
	public static final String PRIMARY_BOOK = "flow";
//	public static final int SUCCESS_CODE = 200;
	
	public static final String HOST = PropertyFiles.getProperty("HPALMServer");
	public static final String PORT = PropertyFiles.getProperty("HPALMPort");
	public static final String USERNAME = PropertyFiles.getProperty("HPALMUserName");
	public static final String PASSWORD = PropertyFiles.getProperty("HPALMPassword");
	public static final boolean VERSIONED = false;
	public static final String DOMAIN = PropertyFiles.getProperty("HPALMDomain");
	public static final String PROJECT = PropertyFiles.getProperty("HPALMProject");
}
