/**
 *
 */
package com.api.helpers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;


/**
 * This class keeps the state of the connection for the examples. This class is
 * a singleton, and all examples get the instance in their default constructors
 * - thus sharing state (cookies, server url).
 *
 * Some simple methods are implemented to get commonly used paths.
 *
 */
public class RestConnector implements Runnable {
	protected Map<String, String> cookies;
	protected String host;
	protected String port;
	protected String domain;
	protected String project;

	public RestConnector init(Map<String, String> cookies, String host, String port, String domain, String project) {
		Thread thread = new Thread(this);
		thread.start();
		this.cookies = cookies;
		this.host = host;
		this.port = port;
		this.domain = domain;
		this.project = project;
		return this;
	}

	private RestConnector() {
	}

	private static RestConnector instance = new RestConnector();

	public static RestConnector getInstance() {
		return instance;

	}

	/**
	 * @param entityType
	 * @return
	 */
	public String buildEntityCollectionUrl(String entityType) {
		return buildUrl("qcbin/rest/domains/" + domain + "/projects/" + project + "/" + entityType + "s");
	}

	public synchronized String buildEntityUrl(String entityType, String entityId) {
		// For now, we won't have both entityId and query together - both are
		// mutually exclusive
		if (entityId != null) {
			return buildUrl(
					"qcbin/rest/domains/" + domain + "/projects/" + project + "/" + entityType + "s" + "/" + entityId);
		}

		else {
			return buildUrl("qcbin/rest/domains/" + domain + "/projects/" + project + "/" + entityType + "s");
		}
	}
	public synchronized String buildTestEntityUrl( String entityId) {
		// For now, we won't have both entityId and query together - both are
		// mutually exclusive
		if (entityId != null) {
			return buildUrl(
					"qcbin/rest/domains/" + domain + "/projects/" + project + "/tests" + "/" + entityId);
		}

		else {
			return buildUrl("qcbin/rest/domains/" + domain + "/projects/" + project + "/tests");
		}
	}
	public synchronized String buildEntityUrl(String entityType, String entityId, String query) {
		// For now, we won't have both entityId and query together - both are
		// mutually exclusive
		if (entityId != null) {
			return buildUrl(
					"qcbin/rest/domains/" + domain + "/projects/" + project + "/" + entityType + "s" + "/" + entityId);
		}

		// Setting page size to 1500 by default
		else if (query != null) {
			return buildUrl("qcbin/rest/domains/" + domain + "/projects/" + project + "/" + entityType + "s" + "?"
					+ "query=" + "{" + query + "}" + "&page-size=1500");
		}

		else {
			return buildUrl("qcbin/rest/domains/" + domain + "/projects/" + project + "/" + entityType + "s");
		}
	}

	/**
	 * @param path on the server to use
	 * @return a url on the server for the path parameter
	 */
	public synchronized String buildUrl(String path) {
		return String.format("http://%1$s:%2$s/%3$s", host, port, path);
	}

	/**
	 * @return the cookies
	 */
	public Map<String, String> getCookies() {
		return cookies;
	}

	/**
	 * @param cookies the cookies to set
	 */
	public void setCookies(Map<String, String> cookies) {
		this.cookies = cookies;
	}

	/**
	 * @return the host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * @param host the host to set
	 */
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * @return the port
	 */
	public String getPort() {
		return port;
	}

	/**
	 * @param port the port to set
	 */
	public void setPort(String port) {
		this.port = port;
	}

	/**
	 * @return the domain
	 */
	public String getDomain() {
		return domain;
	}

	/**
	 * @param domain the domain to set
	 */
	public void setDomain(String domain) {
		this.domain = domain;
	}

	/**
	 * @return the project
	 */
	public String getProject() {
		return project;
	}

	/**
	 * @param project the project to set
	 */
	public synchronized void setProject(String project) {
		this.project = project;
	}

	public synchronized Response httpPut(String url, byte[] data, Map<String, String> headers) throws Exception {
		return doHttp("PUT", url, null, data, headers, cookies);
	}

	public synchronized Response httpPost(String url, byte[] data, Map<String, String> headers) throws Exception {
		return doHttp("POST", url, null, data, headers, cookies);
	}

	public synchronized Response httpDelete(String url, Map<String, String> headers) throws Exception {
		return doHttp("DELETE", url, null, null, headers, cookies);
	}

	public synchronized Response httpGet(String url, String queryString, Map<String, String> headers) throws Exception {
		return doHttp("GET", url, queryString, null, headers, cookies);
	}

	/**
	 * @param type        of the http operation: get post put delete
	 * @param url         to work on
	 * @param queryString
	 * @param data        to write, if a writable operation
	 * @param headers     to use in the request
	 * @param cookies     to use in the request and update from the response
	 * @return http response
	 * @throws Exception
	 */
	private synchronized Response doHttp(String type, String url, String queryString, byte[] data, Map<String, String> headers,
			Map<String, String> cookies) throws Exception {
		if ((queryString != null) && !queryString.isEmpty()) {
			queryString = queryString.replaceAll("\\s", "%20");
			url += "?" + queryString;
		}
		HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
		con.setRequestMethod(type);
		String cookieString = getCookieString();
		prepareHttpRequest(con, headers, data, cookieString);
		con.connect();
		Response ret = retrieveHtmlResponse(con);
		updateCookies(ret);
		return ret;
	}

	/**
	 * @param con          to set the headers and bytes in
	 * @param headers      to use in the request, such as content-type
	 * @param bytes        the actual data to post in the connection.
	 * @param cookiestring the cookies data from clientside, such as lwsso,
	 *                     qcsession, jsession etc..
	 * @throws IOException
	 */
	private synchronized void prepareHttpRequest(HttpURLConnection con, Map<String, String> headers, byte[] bytes,
			String cookieString) throws IOException {
		String contentType = null;
		// Attach cookie information if it exists.
		if ((cookieString != null) && !cookieString.isEmpty()) {
			con.setRequestProperty("Cookie", cookieString);
		}
		// Send data from headers.
		if (headers != null) {
			// Skip the content-type header. The content-type header should only
			// be sent if you are sending content. See below.
			contentType = headers.remove("Content-Type");
			Iterator<Entry<String, String>> headersIterator = headers.entrySet().iterator();
			while (headersIterator.hasNext()) {
				Entry<String, String> header = headersIterator.next();
				con.setRequestProperty(header.getKey(), header.getValue());
			}
		}
		// If there is data to attach to the request, it's handled here. Note
		// that if data exists, we take into account previously removed
		// content-type.
		if ((bytes != null) && (bytes.length > 0)) {
			con.setDoOutput(true);
			// Warning: If you add a content-type header then it is an error not
			// to send information.
			if (contentType != null) {
				con.setRequestProperty("Content-Type", contentType);
			}
			OutputStream out = con.getOutputStream();
			out.write(bytes);
			out.flush();
			out.close();
		}
	}

	/**
	 * @param con that already connected to it's url with an http request, and that
	 *            should contain a response for us to retrieve
	 * @return a response from the server to the previously submitted http request
	 * @throws Exception
	 */
	private synchronized Response retrieveHtmlResponse(HttpURLConnection con) throws Exception {
		Response ret = new Response();
		ret.setStatusCode(con.getResponseCode());
		ret.setResponseHeaders(con.getHeaderFields());
		InputStream inputStream;
		// Select the source of the input bytes, first try "regular" input
		try {
			inputStream = con.getInputStream();
		}
		/*
		 * If the connection to the server failed, for example 404 or 500,
		 * con.getInputStream() throws an exception, which is saved. The body of the
		 * exception page is stored in the response data.
		 */
		catch (Exception e) {
			inputStream = con.getErrorStream();
			ret.setFailure(e);
		}
		// This takes the data from the previously decided stream (error or
		// input) and stores it in a byte[] inside the response.
		ByteArrayOutputStream container = new ByteArrayOutputStream();
		byte[] buf = new byte[1024];
		int read;
		while ((read = inputStream.read(buf, 0, 1024)) > 0) {
			container.write(buf, 0, read);
		}
		ret.setResponseData(container.toByteArray());
		return ret;
	}

	public synchronized void updateCookies(Response response) {
		Iterable<String> newCookies = response.getResponseHeaders().get("Set-Cookie");
		if (newCookies != null) {
			for (String cookie : newCookies) {
				int equalIndex = cookie.indexOf('=');
				int semicolonIndex = cookie.indexOf(';');
				String cookieKey = cookie.substring(0, equalIndex);
				String cookieValue = cookie.substring(equalIndex + 1, semicolonIndex);
				cookies.put(cookieKey, cookieValue);
			}

		}
	}

	public synchronized String getCookieString() {
		StringBuilder sb = new StringBuilder();
		if (!cookies.isEmpty()) {
			Set<Entry<String, String>> cookieEntries = cookies.entrySet();
			for (Entry<String, String> entry : cookieEntries) {
				sb.append(entry.getKey()).append("=").append(entry.getValue()).append(";");
			}
		}
		return sb.toString();
	}

	// author: Sayed
	/*
	 * Function to build Defect URL
	 * 
	 */

	public synchronized String buildEntityUrlDefect(String entityType, String entityId, String query) {
		// For now, we won't have both entityId and query together - both are
		// mutually exclusive
		if (query != null & entityId != null) {
			return buildUrl("qcbin/rest/domains/" + domain + "/projects/" + project + "/" + entityType + "s" + "?"
					+ entityId + "=USER-TEMPLATE-21,USER-TEMPLATE-25,DETECTED-IN-REL,DETECTED-IN-RCYC&" + "query=" + "{"
					+ query + "}");

		} else if (query != null) {
			return buildUrl("qcbin/rest/domains/" + domain + "/projects/" + project + "/" + entityType + "s" + "?"
					+ "query=" + "{" + query + "}");
		} else if (entityId != null) {
			return buildUrl(
					"qcbin/rest/domains/" + domain + "/projects/" + project + "/" + entityType + "s" + "/" + entityId);
		} else {
			return buildUrl("qcbin/rest/domains/" + domain + "/projects/" + project + "/" + entityType + "s");
		}
	}

	// author: Sayed
	/*
	 * Function to build defect id URL
	 * 
	 */

	public synchronized String updateEntityUrlDefect(String entityType, String defectID) {

		return buildUrl(
				"qcbin/rest/domains/" + domain + "/projects/" + project + "/" + entityType + "s" + "/" + defectID);

	}

	
	// author: Krishna
		/*
		 * Function to build TestConfigID from test set.
		 * 
		 */
	public synchronized String buildEntityUrlTestSetConfigid(String entityType, String entityId, String query) {

		return buildUrl("qcbin/rest/domains/" + domain + "/projects/" + project + "/" + entityType + "s" + "?"
				+ "query=" + "{" + query + "}" + "&fields=test-config.name");
	}
	
	

	// author: Krishna
		/*
		 * Function to build URL for test case Bulk UPdate
		 * 
		 */
	public synchronized String buildEntityUrlBulkUpdate(String entityType, String query) {

		return buildUrl("qcbin/rest/domains/" + domain + "/projects/" + project + "/" + entityType + "s" + "?"
				+ "query=" + "{" + query + "}");
	}
	
	/************************************************************************************
	# Author Name:Sayed Abdhahir
	# Date:11-01-2019
	# Description:To build Entity URL 
	************************************************************/
	public synchronized String buildEntityUrlNullPlannedDateId(String entityType) {

		return buildUrl("qcbin/rest/domains/" + domain + "/projects/" + project + "/" + entityType + "s"
				+ "?fields=user-template-01,name,end-date,user-template-07&query={user-template-07['']}");
	}
	
	/***********************************************************************************
	# Author Name: Divya R
	# Date: 11-07-2019
	# Description: To build url for getting test folder id 
	*********************************************************************************/
	public synchronized String buildEntityUrlTestSetFolderID(String entityType,String Value) {
			return buildUrl("qcbin/rest/domains/" + domain + "/projects/" + project + "/" + entityType + "s"
            + "?fields=id&query={name['"+Value+"']}");
}
	/***********************************************************************************
	# Author Name: Sumathi R
	# Date: 11-07-2019
	# Description: To build url for getting requirement coverage status
	*********************************************************************************/
	public synchronized String buildEntityUrlRequirementCoverageStatus(String entityType,String Value) {
        return buildUrl("qcbin/rest/domains/" + domain + "/projects/" + project + "/" + entityType + "s"
                + "?query={test-id["+Value+"]}");
    }
	/***********************************************************************************
	# Author Name: Sumathi R
	# Date: 11-28-2019
	# Description: To build url for getting requirement coverage status
	*********************************************************************************/
	public synchronized String buildEntityUrlDelReq(String entityType,String Value) {
        return buildUrl("qcbin/rest/domains/" + domain + "/projects/" + project + "/" + entityType + "s"
                + "?query={test-id["+Value+"]}");
    }
	
	
	/***********************************************************************************
	# Author Name: Sumathi R
	# Date: 11-16-2019
	# Description: To build url for getting requirement ID for that RFS
	*********************************************************************************/
	public synchronized String buildEntityUrlRequirementID(String entityType,String Value) {
        return buildUrl("qcbin/rest/domains/" + domain + "/projects/" + project + "/" + entityType + "s"
                + "?fields=id&query={name['"+Value+"']}");
    }
	/***********************************************************************************
	# Author Name: Sumathi R
	# Date: 11-19-2019
	# Description: To build url for getting RFS id for that TC
	*********************************************************************************/
	public synchronized String buildEntityUrlRFSID(String entityType,String Value) {
        return buildUrl("qcbin/rest/domains/" + domain + "/projects/" + project + "/" + entityType + "s"
                + "?fields=user-template-14&query={id['"+Value+"']}");
        
    }
	/***********************************************************************************
	# Author Name: Sumathi R
	# Date: 1-23-2020
	# Description: To build url for getting parent folder id
	*********************************************************************************/
	public synchronized  String buildEntityUrlPFolderID(String entityType,String Value) {
        return buildUrl("qcbin/rest/domains/" + domain + "/projects/" + project + "/" + entityType + "s"
                + "?fields=id&query={name['"+Value+"']}");
        
    }

	/***********************************************************************************
	# Author Name: Sumathi R
	# Date: 1-23-2020
	# Description: To build url for getting sub-folder id
	*********************************************************************************/
	public synchronized String buildEntityUrlSubFolderID(String entityType,String Value) {
	    return buildUrl("qcbin/rest/domains/" + domain + "/projects/" + project + "/" + entityType + "s"
	            + "?fields=id&query={parent-id['"+Value+"']}");
	    
	}
	/***********************************************************************************
	# Author Name: Sumathi R
	# Date: 1-28-2020
	# Description: To build url for getting sub-folder id
	*********************************************************************************/
	public synchronized String buildEntityUrlTestcases(String entityType,String Value) {
	    return buildUrl("qcbin/rest/domains/" + domain + "/projects/" + project + "/" + entityType + "s"
	            + "?fields=id&query={parent-id['"+Value+"']}");
	    
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
	

}
