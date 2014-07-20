package com.cbbot.message.cmd;

import com.cbbot.CBInfo;
import com.cbbot.message.CBMessage;
import com.cbbot.user.CBUser;
import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;

public class CBGenderMessage extends CBMessage{

	public CBGenderMessage(CBInfo info, CBUser user) {
		super(info, user);
		this.sendTutorial();
	}

	public CBGenderMessage(CBInfo info, TextMessageEvent e, String command) {
		super(info,e);
		this.addClientGenderGroup(info, command, info.getUserByClientID(e.getInvokerId()));
	}

	private void addClientGenderGroup(CBInfo info, String command, CBUser user) {
		TS3Api api = info.getApi();
		if(info.getUserGroupByID(user, info.getMannlich().getGroupID()) == null &&
				info.getUserGroupByID(user, info.getWeiblich().getGroupID()) == null){
			if(command.equalsIgnoreCase("!mann")){
				api.addClientToServerGroup(info.getMannlich().getGroupID(), user.getClientDatabaseID());
				info.getUserByClientDatabaseID(user.getClientDatabaseID()).addServerGroup(info.getMannlich());
				info.getUserByClientDatabaseID(user.getClientDatabaseID()).checkACGR(info);
			}
			if(command.equalsIgnoreCase("!frau")){
				api.addClientToServerGroup(info.getWeiblich().getGroupID(), user.getClientDatabaseID());
				info.getUserByClientDatabaseID(user.getClientDatabaseID()).addServerGroup(info.getWeiblich());
				info.getUserByClientDatabaseID(user.getClientDatabaseID()).checkACGR(info);
			}
		}
		else{
			this.sendClientMessage(this.formatMessage("Geschlecht", "Hey, du versuchst etwas, was ich nicht darf. Wende dich an einen Admin bitte!"));
		}
	}

	private void sendTutorial() {
		String message ="Bitte gebe dein Geschlecht an:\n\n"
				+ " !mann\n"
				+ " !frau";
		this.sendClientMessage(this.formatMessage("Geschlecht", message));
		
	}

}
