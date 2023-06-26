package com.api.test.users;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.api.dataproviders.UserDataProvider;
import com.api.helpers.ReportBase;
import com.api.jsonpath.UsersJsonPath;
import com.api.pageobject.RestassuredApis;
import com.api.requestpayload.UsersPayload;

import io.restassured.response.Response;


public class Users  extends ReportBase{
			
	@Test(groups = {"Regression","Bvt"}, dataProvider = "addUsers",dataProviderClass = UserDataProvider.class,enabled = true)
	public void addNewUser(String userName, String job, int expectedCode) throws Exception {
		RestassuredApis restResponse = new RestassuredApis();
		UsersJsonPath jsonPath = new UsersJsonPath();
		UsersPayload payload = new UsersPayload();

		String userPayload = payload.usersPayload(userName, job); 
		
		Response response = restResponse.addUsers(userPayload, expectedCode);
		String expectedId = jsonPath.getId(response);
		
		Assert.assertNotNull(expectedId, "Validating if the ID is not null");
		
	
		
	}
	
	@Test(groups = {"Regression","Bvt"}, dataProvider = "getUsers",dataProviderClass = UserDataProvider.class, enabled = true)
	public void getUser(int expectedRespCode) throws Exception {
		RestassuredApis restResponse = new RestassuredApis();
		UsersJsonPath jsonPath = new UsersJsonPath();

		Response response = restResponse.getUsers(expectedRespCode);
		String expectedId = jsonPath.getUserName(response);
		
		Assert.assertNotNull(expectedId, "Validating if the ID is not null");
		
	}
}
