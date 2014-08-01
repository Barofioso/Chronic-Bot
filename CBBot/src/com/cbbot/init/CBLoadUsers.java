package com.cbbot.init;

import java.util.List;

import com.cbbot.CBInfo;
import com.cbbot.user.CBIp;
import com.cbbot.user.CBUser;
import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import com.github.theholywaffle.teamspeak3.api.wrapper.ServerQueryInfo;

public class CBLoadUsers extends CBLoad{

	public CBLoadUsers(CBInfo info) {
		this.loadAll(info);
	}

	private void loadAll(CBInfo info) {
		TS3Api api = info.getApi();
		List<Client> clients = api.getClients();
		ServerQueryInfo sqi = api.whoAmI();
		
		for(int i = 0; i < clients.size(); i++){
			//Sonst wird der Bot auch in der UserListe erscheinen. Und dies soll er nicht ;D
			if(!clients.get(i).getNickname().equalsIgnoreCase(sqi.getNickname())){
				CBUser tmpUser = new CBUser(info, clients.get(i).getId());
				
				tmpUser.loadUserChannels(info);
				tmpUser.setIpAdress(new CBIp(info.getApi().getClientInfo(tmpUser.getClientID()).getIp(), info, tmpUser));
				
				info.getUsers().add(tmpUser);
				info.getLog().addLogEntry("UserName: " + tmpUser.getClientNickname() + " UserDBID: " + tmpUser.getClientDatabaseID());
			}
		}
	}
}
