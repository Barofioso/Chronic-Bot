package com.cbbot.group;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.cbbot.CBInfo;
import com.cbbot.kategorie.CBKategorie;
/**
 * 
 * Die Gruppen Klasse dient als Basis für alle Gruppen
 *
 * @author D. Lehmann
 * @copyright D. Lehmann
 *
 * @version 0.0
 *
 */
public class CBGroup {
	
	private int GroupDBID;	//Datenbank
	private int GroupID;	//TS3
	private String GroupName;
	private int GroupNameMode;
	private int GroupSaveDB;
	private int GroupSortID;
	private int GroupKat;
	private ArrayList<CBKategorie> kategorien = new ArrayList<CBKategorie>();
	private CBInfo info;
	
	public CBGroup(CBInfo info){
		this.info = info;
	}

	public int getGroupID() {
		return GroupID;
	}

	public String getGroupName() {
		return GroupName;
	}

	public int getGroupNameMode() {
		return GroupNameMode;
	}

	public int getGroupSaveDB() {
		return GroupSaveDB;
	}

	public int getGroupSortID() {
		return GroupSortID;
	}

	public void setGroupID(int groupID) {
		GroupID = groupID;
	}

	public void setGroupName(String groupName) {
		GroupName = groupName;
	}

	public void setGroupNameMode(int groupNameMode) {
		GroupNameMode = groupNameMode;
	}

	public void setGroupSaveDB(int groupSaveDB) {
		GroupSaveDB = groupSaveDB;
	}

	public void setGroupSortID(int groupSortID) {
		GroupSortID = groupSortID;
	}

	public ArrayList<CBKategorie> getKategorien() {
		return kategorien;
	}

	public CBKategorie getKategorieByName(String name){
		for(int i = 0; i < this.kategorien.size(); i++){
			if(this.kategorien.get(i).getkName().equals(name)){
				return this.kategorien.get(i);
			}
		}
		return null;
	}
	public CBKategorie getKategorieByID(int kID){
		for(int i = 0; i < this.kategorien.size(); i++){
			if(this.kategorien.get(i).getkID() == kID){
				return this.kategorien.get(i);
			}
		}
		return null;
	}
	public boolean addKategorie(CBKategorie kat){
		for(int i = 0; i < kategorien.size(); i++){
			if(kategorien.get(i).getkID() == kat.getkID()){
				return false;
			}
		}
		this.kategorien.add(kat);
		return true;
	}
	
	public void setKategorien(ArrayList<CBKategorie> kategorie) {
		this.kategorien = kategorie;
	}

	public int getGroupDBID() {
		return GroupDBID;
	}

	public void setGroupDBID(int groupDBID) {
		GroupDBID = groupDBID;
	}
	
	public int getGroupKat() {
		return GroupKat;
	}

	public void setGroupKat(int groupKat) {
		GroupKat = groupKat;
	}
	/**
	 * Update this Object in database
	 */
	public void updateGroup(){
		this.info.getSql().open();
		this.info.getSql().query("UPDATE gruppe SET "
				+ "groupID = " + this.GroupID + ", "
				+ "groupName = '" + this.GroupName.trim() + "', "
				+ "groupNameMode = " + this.GroupNameMode + ", "
				+ "groupSortID = " + this.GroupSortID + " "
				+ "WHERE g_ID = " + this.GroupDBID + ";");
		this.info.getSql().close();
	}
	/**
	 * Creates a new database entry of this Object
	 */
	public void createDB(){
		this.info.getSql().open();
		this.info.getSql().query("INSERT INTO gruppe(groupDatabaseID, "
				+ "groupID, groupName, groupNameMode, groupSortID, groupKat) "
				+ "VALUES ("
				+ this.GroupSaveDB
				+ ", "
				+ this.GroupID
				+ ", '"
				+ this.GroupName.replace("'", "").trim()
				+ "', "
				+ this.GroupNameMode
				+ ", "
				+ this.GroupSortID
				+ ", "
				+ this.GroupKat
				+ ");");
		this.info.getSql().close();
	}
	
	public void loadDBID(){
		this.info.getSql().open();
		ResultSet res = this.info.getSql().query("SELECT * FROM gruppe WHERE groupName = '" + this.GroupName.replace("'", "").trim() + "';");
		boolean isLoaded = false;
		try {
			while(res.next()){
				if(res.getInt("groupID") == this.GroupID){
					this.setGroupDBID(res.getInt("g_ID"));
					isLoaded = true;
					break;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		this.info.getSql().close();
		
		if(!isLoaded){
			this.createDB();
			this.loadDBID();
		}
		else{
			this.setGRKA();
		}
	}

	public void updateGRKA(){
		this.setGRKA();
	}
	private void setGRKA() {
		for(int i = 0; i < this.kategorien.size(); i++){
			if(!this.checkGRKA(this.kategorien.get(i))){
				info.getSql().open();
				String query = "INSERT INTO grka(g_ID,k_ID) VALUES (" + this.GroupDBID + "," + this.kategorien.get(i).getkID() + ");";
				info.getSql().query(query);
				info.getLog().addLogEntry("[Neue Gruppe mit einer neuen Kategorie] " + query);
				info.getSql().close();
			}
		}
		
	}

	private boolean checkGRKA(CBKategorie cbKategorie) {
		this.info.getSql().open();
		ResultSet res = this.info.getSql().query("SELECT * FROM grka WHERE k_ID = " + cbKategorie.getkID() + ";");
		try {
			while(res.next()){
				if(res.getInt("g_ID") == this.getGroupDBID()){
					this.info.getSql().close();
					return true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		this.info.getSql().close();
		return false;
	}
	
	public boolean checkKategorie(CBKategorie kategorie){
		return this.checkGRKA(kategorie);
	}

	public CBInfo getInfo() {
		return this.info;
	}

	public void setBot(CBInfo info) {
		this.info = info;
	}

}
