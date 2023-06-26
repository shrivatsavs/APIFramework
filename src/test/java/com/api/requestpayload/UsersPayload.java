package com.api.requestpayload;

import net.minidev.json.JSONObject;

public class UsersPayload {
	public String usersPayload(String userName,String job){
		JSONObject obj = new JSONObject();
		obj.put("name", userName);
		obj.put("job", job);
		return  obj.toJSONString();
	}
}