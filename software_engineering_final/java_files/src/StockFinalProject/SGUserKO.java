package StockFinalProject;

import java.io.Serializable;

public class SGUserKO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String userKey;
	private SGUsers userObj;
	
	public SGUserKO(String key, SGUsers user) {
		this.setUserKey(key);
		this.setUserObj(user);
	}
	
	public String getUserKey() {
		return userKey;
	}

	public SGUsers getUserObj() {
		return userObj;
	}
	
	public void setUserKey(String key) {
		this.userKey = key;
	}

	public void setUserObj(SGUsers userObj) {
		this.userObj = userObj;
	}
}
