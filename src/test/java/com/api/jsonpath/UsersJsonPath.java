package com.api.jsonpath;



import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class UsersJsonPath {
	
	public String getUserName(Response response) {
		String userName = JsonPath.from(response.getBody().asString()).getString("data[0].first_name");
		return userName;
	}
	
	public String getId(Response response) {
		String job = JsonPath.from(response.getBody().asString()).getString("id");
		return job;
	}
}
