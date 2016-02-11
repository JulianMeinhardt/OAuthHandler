package de.spintegration.oauth;

import java.io.Serializable;

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
			builder.append(thiscallbackURL);
			
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
}

