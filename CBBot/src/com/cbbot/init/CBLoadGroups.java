package com.cbbot.init;

import java.util.List;

import com.cbbot.CBInfo;
import com.cbbot.group.CBServerGroup;
import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.wrapper.ServerGroup;

public class CBLoadGroups extends CBLoad{

	public CBLoadGroups(CBInfo info) {
		this.loadAll(info);
	}

	private void loadAll(CBInfo info) {
		TS3Api api = info.getApi();
		List<ServerGroup> serverGroups = api.getServerGroups();
		
		for(int i = 0; i < serverGroups.size(); i++){
			CBServerGroup tmpSGroup = new CBServerGroup(info, serverGroups.get(i));
			info.getServerGroups().add(tmpSGroup);
			
			if(tmpSGroup.getGroupName().equalsIgnoreCase("männlich")){
				info.setMannlich(tmpSGroup);
			}
			if(tmpSGroup.getGroupName().equalsIgnoreCase("weiblich")){
				info.setWeiblich(tmpSGroup);
			}
			if(tmpSGroup.getGroupName().equalsIgnoreCase("18+")){
				info.setPlus18(tmpSGroup);
			}
			if(tmpSGroup.getGroupName().equalsIgnoreCase("16+")){
				info.setPlus16(tmpSGroup);
			}
			if(tmpSGroup.getGroupName().equalsIgnoreCase("14+")){
				info.setPlus14(tmpSGroup);
			}
			if(tmpSGroup.getGroupName().equalsIgnoreCase("12+")){
				info.setPlus12(tmpSGroup);
			}
		}
		
	}

}
