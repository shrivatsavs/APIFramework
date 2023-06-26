package com.api.dataproviders;

import java.io.IOException;

import org.testng.annotations.DataProvider;

import com.api.helpers.PropertyFiles;

public class UserDataProvider {
	 public static String userName;
	 public static String userJob;
	
	 
	 @DataProvider(name = "addUsers")
		public Object[][] addUsers() throws IOException	{
		 userName = PropertyFiles.getProperty("username");
		 userJob = PropertyFiles.getProperty("job");
			Object[][] CADPArry = {
					{ userName, userJob, 201}};
			return CADPArry;

		}
	 
	 @DataProvider(name = "getUsers")
		public Object[][] getUsers() throws IOException	{
			Object[][] CADPArry = {
					{200}};
			return CADPArry;

		}
}
