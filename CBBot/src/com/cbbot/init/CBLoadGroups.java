package com.cbbot.init;

import java.util.List;

import com.cbbot.CBInfo;
import com.cbbot.group.CBGameGroup;
import com.cbbot.group.CBServerGroup;
import com.cbbot.kategorie.CBKategorie;
import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.wrapper.ServerGroup;

public class CBLoadGroups extends CBLoad{

	public CBLoadGroups(CBInfo info) {
		reloadAllServerGroups(info);
	}
	
	public static void reloadAllServerGroups(CBInfo info){
		//Lösche absolut alle Gruppen aus den listen
		info.getServerGroups().clear();
		info.getGameGroups().clear();
		
		TS3Api api = info.getApi();
		List<ServerGroup> serverGroups = api.getServerGroups();
		
		for(int i = 0; i < serverGroups.size(); i++){
			CBServerGroup tmpSGroup = new CBServerGroup(info, serverGroups.get(i));
			
			
			//Standart gruppen setzen
			if(tmpSGroup.getGroupName().equalsIgnoreCase("männlich")){
				info.setMannlich(tmpSGroup);
			}
			else if(tmpSGroup.getGroupName().equalsIgnoreCase("weiblich")){
				info.setWeiblich(tmpSGroup);
			}
			else if(tmpSGroup.getGroupName().equalsIgnoreCase("18+")){
				info.setPlus18(tmpSGroup);
			}
			else if(tmpSGroup.getGroupName().equalsIgnoreCase("16+")){
				info.setPlus16(tmpSGroup);
			}
			else if(tmpSGroup.getGroupName().equalsIgnoreCase("14+")){
				info.setPlus14(tmpSGroup);
			}
			else if(tmpSGroup.getGroupName().equalsIgnoreCase("12+")){
				info.setPlus12(tmpSGroup);
			}
			//Die weiteren Gruppen
			else if(tmpSGroup.getKategorieByName(CBKategorie.game) != null){
				info.getGameGroups().add(new CBGameGroup(info, serverGroups.get(i)));
			}
			else if(tmpSGroup.getKategorieByName(CBKategorie.clan) != null){
				//Clan/Gilden System implementierung
			}
			else{
				//Nicht kategorisierte Gruppen!
			}
			
			
			info.getServerGroups().add(tmpSGroup);
		}
	}

}
