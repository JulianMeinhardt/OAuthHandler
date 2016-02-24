package de.spintegration.oauth;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

/**
 * 
 * @author jmeinhardt
 *
 */
public class OAuthHandler implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String authURL = "";
	private String appID = "";
	private String callbackURL = "";
	private String clientSecret = "";
	
	/**
	 * empty constructor
	 */
	public OAuthHandler(){
		
	}
	
	public String getRedirectURL() throws OAuthException{
		// Check for empty metadata
		if(!(authURL.isEmpty() || appID.isEmpty() || callbackURL.isEmpty())){
			// Build oauth url
			StringBuilder builder = new StringBuilder();
			builder.append(this.authURL);
			builder.append("?");
			builder.append("response_type=code");
			builder.append("&");
			builder.append("client_id=");
			builder.append(this.appID);
			builder.append("&");
			builder.append("callback_uri=");
			builder.append(this.callbackURL);
			
			return builder.toString();
		}else{
			throw new OAuthException();
		}
	}
	
	public String generateTokenBody(String code){
		StringBuilder builder = new StringBuilder();
		builder.append("client_id=");
		builder.append(this.appID);
		builder.append("&");

		builder.append("client_secret=");
		builder.append(this.clientSecret);
		builder.append("&");

		builder.append("callback_uri=");
		builder.append(this.callbackURL);
		builder.append("&");

		builder.append("code=");
		builder.append(code);
		builder.append("&");

		builder.append("grant_type=authorization_code");
		return builder.toString();
	}
	
	public void refreshShortToken(String code){
		try{
			// Build HTTP POST to get full token
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost("https://apps.na.collabserv.com/manage/oauth2/token");
			post.addHeader("Content-Type", "application/x-www-form-urlencoded");
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			// Build POST body with all those important things
			nameValuePairs.add(new BasicNameValuePair("callback_uri", this.callbackURL));
			nameValuePairs.add(new BasicNameValuePair("client_secret", this.clientSecret));
			nameValuePairs.add(new BasicNameValuePair("client_id", this.appID));
			nameValuePairs.add(new BasicNameValuePair("grant_type", "authorization_code"));
			nameValuePairs.add(new BasicNameValuePair("code", code));
			// append it to the POST
			post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			System.out.println("Exec post with code: " + code);
			// execute and get response
			HttpResponse response = client.execute(post);
			
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			System.out.println("Status code: " + response.getStatusLine().getStatusCode());
			System.out.println("Content-Type: " + response.getEntity().getContentType());
		    String line = "";
		    	while ((line = rd.readLine()) != null) {
		    		System.out.println(line);
					if (line.startsWith("access_token=")) {
						String key = line.substring(12);
						// do something with the key
					}
				}
		}catch(Exception e){
			System.out.println("Error while refreshing short token");
			e.printStackTrace();
		}
	}

	/**
	 * @return the authURL
	 */
	public String getAuthURL() {
		return authURL;
	}

	/**
	 * @param authURL the authURL to set
	 */
	public void setAuthURL(String authURL) {
		this.authURL = authURL;
	}

	/**
	 * @return the appID
	 */
	public String getAppID() {
		return appID;
	}

	/**
	 * @param appID the appID to set
	 */
	public void setAppID(String appID) {
		this.appID = appID;
	}

	/**
	 * @return the callbackURL
	 */
	public String getCallbackURL() {
		return callbackURL;
	}

	/**
	 * @param callbackURL the callbackURL to set
	 */
	public void setCallbackURL(String callbackURL) {
		this.callbackURL = callbackURL;
	}

	/**
	 * @return the clientSecret
	 */
	public String getClientSecret() {
		return clientSecret;
	}

	/**
	 * @param clientSecret the clientSecret to set
	 */
	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}
}

