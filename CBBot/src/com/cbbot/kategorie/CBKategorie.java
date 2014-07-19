package com.cbbot.kategorie;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.cbbot.CBInfo;

public class CBKategorie {

	private int kID;
	private String kName;
	private CBInfo info;
	
	public CBKategorie(CBInfo info, String name){
		this.kName = name;
		this.info = info;
		this.loadKID();
	}
	private void loadKID() {
		boolean kategorieInDB = false;
		this.info.getSql().open();
		ResultSet res = this.info.getSql().query("SELECT * FROM kategorie;");
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
		this.info.getSql().close();
		if(!kategorieInDB){
			this.createDB();
			this.loadKID();
		}
		this.info.getLog().addLogEntry("Kategorie: " + this.kName + " wurde"
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
	public void updateDB(){
		this.info.getSql().open();
		this.info.getSql().query("UPDATE kategorie SET kName = " + this.kName.trim() + " "
				+ "WHERE k_ID = " + this.kID + ";");
		this.info.getSql().close();
		
		this.info.getLog().addLogEntry("Kategorie: " + this.kName + " wurde"
				+ " mit dieser ID: " + this.kID + " erfolgreich aktualisiert");
	}
	public void createDB(){
		this.info.getSql().open();
		this.info.getSql().query("INSERT INTO kategorie(kName) VALUES ('" + this.kName.trim() + "');");
		
		ResultSet res = this.info.getSql().query("SELECT * FROM kategorie;");
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
		this.info.getSql().close();
		this.info.getLog().addLogEntry("Kategorie: " + this.kName + " wurde"
				+ " mit dieser ID: " + this.kID + " erfolgreich in der Datenbank"
				+ " erstellt");
	}
}
