package com.cbbot.group;

import com.cbbot.CBInfo;
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
		
		if((this.getGroupName().contains("Admin") && this.getGroupName().contains("Server")) || (this.getGroupName().contains("Query") && (this.getGroupName().contains("Admin") && this.getGroupName().contains("Server")))){
			this.getKategorie().add(info.getAdminKat());
			this.getKategorie().add(info.getNormalKat());
		}
		else{
			this.getKategorie().add(info.getNormalKat());
		}
		
		this.loadDBID();
		
		info.getLog().addLogEntry("Die Servergruppe: " + this.getGroupName() + " ID: " + this.getGroupID() + " wurde erstellt");
	}
	
}
