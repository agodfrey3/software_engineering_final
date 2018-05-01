package StockFinalProject;

import java.io.Serializable;
import java.util.Vector;

public class SGUsers implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String username;
	private int turnCounter = 0;
	private Double balance = 0.0;
	private int number_of_longs = 0;
	private int number_of_shorts = 0;
	private int number_of_correct = 0;
	private int number_of_neutral = 0;
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
	
	public int getTurnCounter() {
		return turnCounter;
	}
	
	public void setTurnCounter(int n) {
		turnCounter = n;
	}
}

