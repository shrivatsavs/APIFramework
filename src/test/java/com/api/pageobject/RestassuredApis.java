package com.api.pageobject;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.log4j.Logger;

import com.api.helpers.PropertyFiles;
import com.api.helpers.TestBase;

public class RestassuredApis extends TestBase{
	
	private static Logger logger = Logger.getLogger(RestassuredApis.class);

	public Response addUsers(String payload, int expStatusCode) {
		fullUrl = PropertyFiles.getProperty("baseURL") + PropertyFiles.getProperty("postUsers");
		
		Response response = RestAssured.given().header("contentType","application/json")
				.and().body(payload).post(fullUrl).then().extract().response();
			logger.info(response.getBody().toString());
			response.prettyPrint();
			int statusCode = response.getStatusCode();
			assertReturnedResponseCode(statusCode,expStatusCode);
		return response;	
		
	}
	
	public Response getUsers(int expStatusCode) {
		fullUrl = PropertyFiles.getProperty("baseURL") + PropertyFiles.getProperty("getUsers");
		
		Response response = RestAssured.given().header("contentType","application/json")
				.and().get(fullUrl).then().extract().response();
		
			response.prettyPrint();
			int statusCode = response.getStatusCode();
			assertReturnedResponseCode(statusCode,expStatusCode);
		return response;	
		
	}
	
	/*public static Responses postRequest(String payload) throws Exception {
		Header apiKeyval = new Header();
		apiKeyval.setName("contentType");
		apiKeyval.setValue("application/json");
		
		HashMap<String, Header> headers = new HashMap<>();
		headers.put("contentType", apiKeyval);
		String fullUrl = "https://reqres.in/api/users";
		Responses response = RESTWrapper.doRequest(headers, ContentType.JSON, fullUrl, payload,
				Method.POST);
		return response;
	}*/
}
