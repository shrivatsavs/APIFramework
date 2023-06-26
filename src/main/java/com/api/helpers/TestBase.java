package com.api.helpers;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;

import org.apache.http.HttpStatus;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.Reporter;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class TestBase {

	public static LinkedHashSet<String> passedTcName = new LinkedHashSet<String>();
	public static LinkedHashSet<String> failedTcName = new LinkedHashSet<String>();

	protected String fullUrl;
	private static Logger logger = Logger.getLogger(TestBase.class);
	public static int successCode;


	public static Map<String, String> map;

	public TestBase() {
	}

	public void assertReturnedResponseCode(int actResponseCode, int expResponseCode) {
		Assert.assertEquals(actResponseCode, expResponseCode, "Expected and Actual Response Code is Mismatched");
	}


	public void inputLogging(String desc, String path) {
		Reporter.log("<font color=\"blue\">" + "<b>" + desc + "</b>" + "</font>");
		Reporter.log("<b>URI - </b>" + path);

		ExtentReporterNG.log("<font color=\"blue\">" + "<b>" + desc + "</b>" + "</font>");
		ExtentReporterNG.log("<b>URI - </b>" + path);

	}

	public void assertContext(Response response) {
		if (response.getStatusCode() == 200 || response.getStatusCode() == HttpStatus.SC_CREATED) {
			Assert.assertNotNull(response, "returned response is null");
			Assert.assertNotNull(response.getResponseData().toString(), "reponse ReturnedResponse is null");
		}
	}

	// Report for POST methods for printing request and response content
	protected void reportLogging(Response response, int expReturnedStatus, String methodName, String payload) {
		// response.
		Reporter.log("<b>Method : </b>" + methodName);
		ExtentReporterNG.log("<b>Method : </b>" + methodName);
		Reporter.log("<b>Requestbody :</b>" + payload);
		ExtentReporterNG
				.log("<b>" + "ResponseCode Expected:   " + "</b>" + "</font>" + expReturnedStatus + "               "
						+ "<b>" + "  ResponseCode Actual: " + "</b>" + "</font>" + response.getStatusCode());
		ExtentReporterNG.log("<b>Requestbody :</b>" + payload);
		if (response.getResponseData().toString() != null) {
			Reporter.log("<b>Response :</b>" + response.getResponseData().toString());
			ExtentReporterNG.log("<b>Response :</b>" + response.getResponseData().toString());
		}
	}
	


}
