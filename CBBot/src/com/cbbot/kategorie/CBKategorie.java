package com.cbbot.kategorie;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.cbbot.CBInfo;

public class CBKategorie {
	/**
	 * Die Admin Kategorie
	 */
	public static final String admin = "Admin";
	/**
	 * Die Normale Kategorie
	 */
	public static final String normal = "Normal";
	/**
	 * Die Spiele Kategorie
	 */
	public static final String game = "Spiel";
	/**
	 * Die Clan Kategorie
	 */
	public static final String clan = "Clan";

	private int kID;
	private String kName;
	
	public CBKategorie(CBInfo info, String name){
		this.kName = name;
		this.loadKID(info);
	}
	private void loadKID(CBInfo info) {
		boolean kategorieInDB = false;
		info.getSql().open();
		ResultSet res = info.getSql().query("SELECT * FROM kategorie;");
		try {
			while(res.next()){
				if(res.getString("kName").equals(this.kName)){
					this.kID = res.getInt("k_ID");
					res.afterLast();
					kategorieInDB = true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		info.getSql().close();
		if(!kategorieInDB){
			this.createDB(info);
			this.loadKID(info);
		}
		info.getLog().addLogEntry("Kategorie: " + this.kName + " wurde"
				+ " mit dieser ID: " + this.kID + " erfolgreich erstellt");
		
		
	}
	public int getkID() {
		return kID;
	}
	public void setkID(int kID) {
		this.kID = kID;
	}
	public String getkName() {
		return kName;
	}
	public void setkName(String kName) {
		this.kName = kName;
	}
	public void updateDB(CBInfo info){
		info.getSql().open();
		info.getSql().query("UPDATE kategorie SET kName = " + this.kName.trim() + " "
				+ "WHERE k_ID = " + this.kID + ";");
		info.getSql().close();
		
		info.getLog().addLogEntry("Kategorie: " + this.kName + " wurde"
				+ " mit dieser ID: " + this.kID + " erfolgreich aktualisiert");
	}
	public void createDB(CBInfo info){
		info.getSql().open();
		info.getSql().query("INSERT INTO kategorie(kName) VALUES ('" + this.kName.trim() + "');");
		
		ResultSet res = info.getSql().query("SELECT * FROM kategorie;");
		try {
			while(res.next()){
				int id = res.getInt("k_ID");
				String name = res.getString("kName");
				if(this.kName.toLowerCase().trim().equals(name.toLowerCase().trim())){
					this.kID = id;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		info.getSql().close();
		info.getLog().addLogEntry("Kategorie: " + this.kName + " wurde"
				+ " mit dieser ID: " + this.kID + " erfolgreich in der Datenbank"
				+ " erstellt");
	}
}
