package com.ix.ecw.databridge.model;

public class ECWConfig {

	String url;

	String username;

	String password;

	

	public String getUrl() {
		return url;
	}



	public void setUrl(String url) {
		this.url = url;
	}



	public String getUsername() {
		return username;
	}



	public void setUsername(String username) {
		this.username = username;
	}



	public String getPassword() {
		return password;
	}



	public void setPassword(String password) {
		this.password = password;
	}



	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "\"ECW [ecwUrl " + url + ", userName = " + username + ", password = " + password + "]";
	}

}
