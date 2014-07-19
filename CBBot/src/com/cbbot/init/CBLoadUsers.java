package com.cbbot.init;

import java.util.List;

import com.cbbot.CBInfo;
import com.cbbot.user.CBUser;
import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;

public class CBLoadUsers extends CBLoad{

	public CBLoadUsers(CBInfo info) {
		this.loadAll(info);
	}

	private void loadAll(CBInfo info) {
		TS3Api api = info.getApi();
		List<Client> clients = api.getClients();
		
		for(int i = 0; i < clients.size(); i++){
			//Sonst wird der Bot auch in der UserListe erscheinen. Und dies soll er nicht ;D
			if(!clients.get(i).getNickname().toLowerCase().trim().contains(api.whoAmI().getNickname())){
				CBUser tmpUser = new CBUser(info, clients.get(i).getId());
				info.getUsers().add(tmpUser);
				info.getLog().addLogEntry("UserName: " + tmpUser.getClientNickname() + " UserDBID: " + tmpUser.getClientDatabaseID());
			}
		}
	}
}
