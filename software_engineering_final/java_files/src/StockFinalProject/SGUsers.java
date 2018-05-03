package StockFinalProject;

import java.io.Serializable;
import java.util.Vector;

public class SGUsers implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String username;
	private String actionPerformed;
	private int turnCounter = 0;
	private Double balance = 0.0;
	private int number_of_longs = 0;
	private int number_of_shorts = 0;
	private int number_of_correct = 0;
	private int number_of_neutral = 0;
	
	private Vector<String>stockslonged_vector = new Vector<String>();
	private Vector<String>stocksshorted_vector = new Vector<String>();
	
	public void setUserName(String userName) {
		username = userName;
	}

	public void setActionPerformed(String action) {this.actionPerformed = action;}

	public String getActionPerformed() { return this.actionPerformed; }

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
	
	public int getNumLongs() {
		return number_of_longs;
	}
	
	public void setNumLongs(int n) {
		number_of_longs = n;
	}
	
	public void incrementNumLongs() {
		number_of_longs++;
	}
	
	public int getNumShorts() {
		return number_of_shorts;
	}
	
	public void setNumShorts(int n) {
		number_of_shorts = n;
	}
	
	public void incrementNumShorts() {
		number_of_shorts++;
	}
	
	public int getNumCorrect() {
		return number_of_correct;
	}
	
	public void setNumCorrect(int n) {
		number_of_correct = n;
	}
	
	public void incrementNumCorrect() {
		number_of_correct++;
	}
	
	public int getNumNeutral() {
		return number_of_neutral;
	}
	
	public void setNumNeutral(int n) {
		number_of_neutral = n;
	}
	
	public void incrementNumNeutral() {
		number_of_neutral++;
	}
}

