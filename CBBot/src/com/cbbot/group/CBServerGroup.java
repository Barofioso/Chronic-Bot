package com.cbbot.group;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.cbbot.CBInfo;
import com.cbbot.CBMySql;
import com.cbbot.kategorie.CBKategorie;
import com.github.theholywaffle.teamspeak3.api.wrapper.ServerGroup;


public class CBServerGroup extends CBGroup{
	
	public CBServerGroup(CBInfo info, ServerGroup serverGroup){
		super(info);
		
		this.setGroupSortID(serverGroup.getSortId());
		this.setGroupSaveDB(serverGroup.getSaveDb());
		this.setGroupID(serverGroup.getId());
		this.setGroupName(serverGroup.getName());
		this.setGroupNameMode(serverGroup.getNameMode());
		this.setGroupKat(1); //StandartMerkmal serverGruppe
		
		if(this.getGroupName().equalsIgnoreCase("Server Admin") || this.getGroupName().equalsIgnoreCase("Admin Server Query")){
			this.addKategorie(info.getAdminKat());
			this.addKategorie(info.getNormalKat());
		}
		else{
			this.addKategorie(info.getNormalKat());
		}
		
		this.loadDBID();
		this.loadKats(info);
		
		info.getLog().addLogEntry("Die Servergruppe: " + this.getGroupName() + " ID: " + this.getGroupID() + " wurde erstellt");
	}
	
	private void loadKats(CBInfo info) {
		CBMySql sql = info.getSql();
		
		sql.open();
		ResultSet res = sql.query("SELECT grka.g_ID, grka.k_ID, kategorie.kName FROM grka JOIN kategorie ON grka.k_ID = kategorie.k_ID WHERE grka.g_ID = " + this.getGroupDBID() + ";");
		try {
			while(res.next()){
				if(res.getString("kName").equalsIgnoreCase(CBKategorie.game)){
					this.addKategorie(info.getGameKat());
				}
				if(res.getString("kName").equalsIgnoreCase(CBKategorie.clan)){
					this.addKategorie(info.getClanKat());
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		sql.close();
	}
	
}
