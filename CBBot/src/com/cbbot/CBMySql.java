package com.cbbot;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
/**
 * 
 * Meine MySql klasse, damit ich auf einfachste weise mich mit der Datenbank verbinden kann
 * Daten abfragen und setzen kann
 *
 * @author D. Lehmann
 * @copyright D. Lehmann
 *
 * @version 0.0
 *
 */
public class CBMySql {

	
	private String 	ip;
	private String 	user;
	private String 	pass;
	private String 	database;
	private int 	port;
	
	private Connection 			con 	= null;
	private Statement 			st 		= null;
	private PreparedStatement	pst 	= null;
	private ResultSet 			result 	= null;
	
	public CBMySql(){
		
	}
	
	
	public CBMySql(String ip, String user, String pass, String database){
		this.ip = ip;
		this.user = user;
		this.pass = pass;
		this.database = database;
		this.port = 3306;
	}
	
	
	public CBMySql(String ip, String user, String pass, String database, int port){
		this.ip = ip;
		this.user = user;
		this.pass = pass;
		this.database = database;
		this.port = port;
	}
	
	public boolean open(){
		try {
			Class.forName("com.mysql.jdbc.Driver");
			
			con = DriverManager.getConnection("jdbc:mysql://" 
			+ this.ip + ":" 
			+ this.port + "/" 
			+ this.database + "?" 
			+ "user=" + this.user 
			+ "&password=" + this.pass);
			//st = con.createStatement();
			//result = st.executeQuery(query);
			return con.isValid(10);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	public ResultSet query(String query){
		try{
			if(query.contains("SELECT")){
				st = con.createStatement();
				result = st.executeQuery(query);
			}
			else if(query.contains("INSERT")){
				pst = con.prepareStatement(query);
				pst.executeUpdate();
			}
			else if(query.contains("UPDATE")){
				pst = con.prepareStatement(query);
				pst.executeUpdate();
			}
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		return result;
	}
	/**
	 * INSERT Query statement
	 * 
	 * @param tabelle	Table from the database
	 * @param spalte	Row from the table
	 * @param value		Value to add there
	 * @return			String query
	 */
	public String insertQuery(String tabelle, Object[] spalte, Object[] value){
		String tmpSpalte = "";
		String tmpValue = "";
		for(int i = 0; i < spalte.length; i++){
			if(i == (spalte.length-1)){
				tmpSpalte += spalte[i];
			}
			else{
				tmpSpalte += spalte[i] + ", ";
			}
		}
		for(int i = 0; i < value.length; i++){
			if(i == (value.length-1)){
				tmpValue += value[i];
			}
			else{
				tmpValue += value[i] + ", ";
			}
		}
		return "INSERT INTO " + tabelle + " (" + tmpSpalte + ")" + " VALUES " + "(" + tmpValue + ");";
	}
	
	/**
	 * INSERT Query statement 
	 * Only one Value
	 * 
	 * @param tabelle	Table from the database
	 * @param spalte	Row from the Table
	 * @param value		Value for the Row
	 * @return			String query
	 */
	public String insertQuery(String tabelle, String spalte, String value){
		return "INSERT INTO " + tabelle + " (" + spalte + ")" + " VALUES " + "('" + value + "');";
	}
	
	/**
	 * INSERT Query statement
	 * 
	 * @param tabelle				Table from the database
	 * @param spalte				Row from the Table
	 * @param value					Value for the Row
	 * @param whereSpalte			Row for identifying
	 * @param whereSpaltenInhalt	Value for identifying
	 * @return						String query
	 */
	public String insertQuery(String tabelle, Object[] spalte, Object[] value, String whereSpalte, String whereSpaltenInhalt){
		String tmpSpalte = "";
		String tmpValue = "";
		for(int i = 0; i < spalte.length; i++){
			if(i == (spalte.length-1)){
				tmpSpalte += spalte[i];
			}
			else{
				tmpSpalte += spalte[i] + ", ";
			}
		}
		for(int i = 0; i < value.length; i++){
			if(i == (value.length-1)){
				tmpValue += value[i];
			}
			else{
				tmpValue += value[i] + ", ";
			}
		}
		return "INSERT INTO " + tabelle + " (" + tmpSpalte + ")" + " VALUES " + "(" + tmpValue + ")" + " WHERE " + whereSpalte + "=" + whereSpaltenInhalt + ";";
	}
	
	public String selectQuery(String tabelle, String spalte){
		return "SELECT " + spalte + " FROM " + tabelle;
	}
	public String selectQuery(String tabelle, String spalte, String where){
		return "SELECT " + spalte + " FROM " + tabelle + " WHERE " + where;
	}
	
	public String getIp() {
		return ip;
	}


	public String getUser() {
		return user;
	}


	public String getPass() {
		return pass;
	}


	public String getDatabase() {
		return database;
	}


	public int getPort() {
		return port;
	}


	public void setIp(String ip) {
		this.ip = ip;
	}


	public void setUser(String user) {
		this.user = user;
	}


	public void setPass(String pass) {
		this.pass = pass;
	}


	public void setDatabase(String database) {
		this.database = database;
	}


	public void setPort(int port) {
		this.port = port;
	}
	public void close(){
		if (result != null) {
			 try {
				result.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		 }
		 if (st != null) {
			 try {
				st.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		 }
		 if (con != null) {
			 try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		 }
	}
}
