package StockFinalProject;

import java.io.Serializable;
import java.util.Vector;

public class SGUsers implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String username;
	private Integer turnCounter = 0;
	private Double balance = 0.0;
	private Vector<String>stockslonged_vector = new Vector<String>();
	private Vector<String>stocksshorted_vector = new Vector<String>();
	
//	public SGUsers() {
//		turnCounter = 0;
//		balance = 0.0;
//	}
	
	public void setUserName(String userName) {
		username = userName;
	}
	
	public void addToTurnCounter() {
		turnCounter++;
	}
	
	public void addToStocksLonged(String newStock) {
		stockslonged_vector.add(newStock);
	}
	
	public void addToStocksShorted(String newStock) {
		stocksshorted_vector.add(newStock);
	}
	
	public String getUserName() {
		return username;
	}
	
	public Double getBalance() {
		return balance;
	}
	
	public Integer getTurnCounter() {
		return turnCounter;
	}
	
	public void setTurnCounter(int n) {
		turnCounter = n;
	}
}

