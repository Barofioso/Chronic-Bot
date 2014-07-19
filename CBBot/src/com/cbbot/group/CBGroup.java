package com.cbbot.group;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.cbbot.CBInfo;
import com.cbbot.kategorie.CBKategorie;

public class CBGroup {
	
	private int GroupDBID;	//Datenbank
	private int GroupID;	//TS3
	private String GroupName;
	private int GroupNameMode;
	private int GroupSaveDB;
	private int GroupSortID;
	private int GroupKat;
	private ArrayList<CBKategorie> kategorie = new ArrayList<CBKategorie>();
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

	public ArrayList<CBKategorie> getKategorie() {
		return kategorie;
	}

	public CBKategorie getKategorieByName(String name){
		for(int i = 0; i < this.kategorie.size(); i++){
			if(this.kategorie.get(i).getkName().equals(name)){
				return this.kategorie.get(i);
			}
		}
		return null;
	}
	public CBKategorie getKategorieByID(int kID){
		for(int i = 0; i < this.kategorie.size(); i++){
			if(this.kategorie.get(i).getkID() == kID){
				return this.kategorie.get(i);
			}
		}
		return null;
	}
	public void addKategorie(CBKategorie kat){
		this.kategorie.add(kat);
	}
	
	public void setKategorie(ArrayList<CBKategorie> kategorie) {
		this.kategorie = kategorie;
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

	private void setGRKA() {
		for(int i = 0; i < this.kategorie.size(); i++){
			if(!this.checkGRKA(this.kategorie.get(i))){
				this.info.getSql().open();
				String query = "INSERT INTO grka(g_ID,k_ID) VALUES (" + this.GroupDBID + "," + this.kategorie.get(i).getkID() + ");";
				this.info.getSql().query(query);
				this.info.getLog().addLogEntry("[Neue Gruppe mit einer neuen Kategorie] " + query);
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

	public CBInfo getInfo() {
		return this.info;
	}

	public void setBot(CBInfo info) {
		this.info = info;
	}

}
